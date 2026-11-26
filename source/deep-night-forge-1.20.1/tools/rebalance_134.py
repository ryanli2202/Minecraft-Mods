from pathlib import Path
import re,json
root=Path(__file__).resolve().parents[1]
java=root/'src/main/java/dev/deepnight'
p=java/'Species.java'; s=p.read_text(encoding='utf-8')
rows=list(re.finditer(r'^    (\w+)\("([^"]+)", Kind.BOSS, ([\d.]+),',s,re.M))
old={m[1]:float(m[3]) for m in rows}
assert old['DAWN_CORRUPTED_OGRE']==240, 'One-time 1.3.3 migration already applied'
order=[m[1] for m in rows if m[1] not in ['DAWN_DRAGON_LORD','MOBOSSES_DEMON_KING','MOBOSSES_HEROBRINE','DAWN_NOTCH_BOSS','DAWN_HEROBRINE']]
order.insert(order.index('DAWN_FIRE_DRAGON')+1,'DAWN_DRAGON_LORD')
order+=['MOBOSSES_DEMON_KING','MOBOSSES_HEROBRINE','DAWN_NOTCH_BOSS','DAWN_HEROBRINE']
names={'MOBOSSES_HEROBRINE':"The Challenger's First Form",'DAWN_NOTCH_BOSS':'The First Architect','DAWN_HEROBRINE':"The Challenger's Final Form"}
health={}; previous=0
for key in order:
    health[key]=max(old[key]*3,previous+300);previous=health[key]
for m in reversed(rows):
    s=s[:m.start()]+f'    {m[1]}("{names.get(m[1],m[2])}", Kind.BOSS, {health[m[1]]:.1f},'+s[m.end():]
s=s.replace('java.util.Arrays.stream(values()).filter(Species::boss).toList()', 'java.util.List.of(\n        '+',\n        '.join(order)+'\n    )')
p.write_text(s,encoding='utf-8')
p=java/'source/SourceBosses.java';s=p.read_text();s=s.replace('attributes(Species species){return switch(species){','attributes(Species species){var builder = switch(species){');s=s.replace('default -> NightMob.attributes(species);};}', 'default -> NightMob.attributes(species);};\nif(species.boss())builder.add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH,species.health);\nreturn builder;}');p.write_text(s)
p=java/'DeepNight.java';s=p.read_text();s=s.replace('GeckoLib.initialize();','GeckoLib.initialize();\n        BossBalance.raiseHealthLimit();');p.write_text(s)
p=java/'NightMob.java';s=p.read_text();s=s.replace('super.readAdditionalSaveData(t);owner=', 'super.readAdditionalSaveData(t);\n        if(species.boss()){\n            float fraction=getMaxHealth()>0?getHealth()/getMaxHealth():0;\n            getAttribute(Attributes.MAX_HEALTH).setBaseValue(species.health);\n            setHealth(fraction*getMaxHealth());\n        }\n        owner=');p.write_text(s)
for p in (root/'src/main/resources/assets').glob('*/lang/*.json'):
    s=p.read_text(encoding='utf-8');v=json.loads(s)
    for key,name in names.items():
        id=key.lower()
        for k in v:
            if k=='entity.deepnight.'+id:v[k]=name
            if k=='item.deepnight.'+id+'_sigil':v[k]=name+' Sigil'
    p.write_text(json.dumps(v,indent=2,ensure_ascii=False)+'\n',encoding='utf-8')
# Give anonymous magic attacks an attacker so the same multiplier applies once.
for name in ['DemonKingLightningEntity','ForestGuardianAuraEntity','DivineDragonEntity']:
    p=java/f'source/mobosses/entity/custom/{name}.java';s=p.read_text().replace('this.damageSources().magic()', 'this.damageSources().indirectMagic(this, this)');p.write_text(s)
for p in [root/'build.gradle',root/'src/main/resources/META-INF/mods.toml']:
    p.write_text(p.read_text().replace('1.3.3','1.3.4'))
report=[{'stage':i+1,'id':key.lower(),'name':names.get(key,next(m[2] for m in rows if m[1]==key)),'old_health':old[key],'health':health[key]} for i,key in enumerate(order)]
(root/'verification/boss-balance-1.3.4.json').write_text(json.dumps(report,indent=2)+'\n')
print(json.dumps(report,indent=2))
