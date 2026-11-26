from pathlib import Path
import re,json,zipfile
R=Path(__file__).resolve().parents[1];O=R/'src/main/java/dev/deepnight';D=O/'source/dawn';A=R/'src/main/resources/assets/deepnight';DATA=R/'src/main/resources/data/deepnight'
for p in (D/'procedures').glob('*.java'):
 t=p.read_text();t=re.sub(r'Comparator.comparingDouble\(_entcnd -> _entcnd.distanceToSqr\((\w+)\)\)',r'dev.deepnight.source.SourceSupport.byDistance(\1)',t);p.write_text(t)
# All source sound identifiers resolve to registered original OGGs.
sounds=json.loads((R/'src/main/resources/assets/simple_mobs/sounds.json').read_text())
(D/'DawnSounds.java').write_text('''package dev.deepnight.source.dawn;
public final class DawnSounds {
public static final net.minecraftforge.registries.DeferredRegister<net.minecraft.sounds.SoundEvent> REGISTRY=net.minecraftforge.registries.DeferredRegister.create(net.minecraftforge.registries.ForgeRegistries.SOUND_EVENTS,"simple_mobs");
static {
'''+''.join(f'REGISTRY.register("{n}",()->net.minecraft.sounds.SoundEvent.createVariableRangeEvent(new net.minecraft.resources.ResourceLocation("simple_mobs","{n}")));\n' for n in sounds)+'}\n}\n')
p=D/'DawnRuntime.java';t=p.read_text().replace('bus.addListener(DawnRuntime::attributes);','DawnSounds.REGISTRY.register(bus);\nbus.addListener(DawnRuntime::attributes);');p.write_text(t)
# Source registered dimensions and base stats replace the previous mesh estimates.
selection=json.loads((R/'tools/java_creature_selection.json').read_text());species=(O/'Species.java').read_text();fireimmune=[];report=[]
for pack,folder,package,regfile in [('dawn','dawncraft_mobs-1.18-1.3.9a','net/mcreator/simplemobs','init/SimpleMobsModEntities.java'),('mobosses','mobosses-1.0.7','net/hell/mobosses','MoBosses.java')]:
 src=R.parent/'deobfuscated'/folder/'src/main/java'/package;reg=(src/regfile).read_text()
 for name in selection[pack]['boss'].split()+(['DemonKingMinion'] if pack=='mobosses' else []):
  n=pack+'_'+re.sub(r'(?<!^)(?=[A-Z][a-z]|(?<=[a-z0-9])[A-Z])','_',name).lower();cls=name+'Entity'
  declaration=next(m[0] for m in re.finditer(r'public static final [^;]+;',reg,re.S) if cls+'::new' in m[0])
  dims=re.search(r'\.sized\(([^,]+),\s*([^\)]+)\)',declaration)
  if dims:width,height=dims.groups()
  else:raise ValueError(cls)
  code=next(src.rglob(cls+'.java')).read_text();attrs=code[code.index('static Builder createAttributes()'):] if 'static Builder createAttributes()' in code else code[code.index('static AttributeSupplier.Builder createAttributes()'):]
  stat=lambda key,default:re.search(r'add\(Attributes.'+key+r',\s*([\d.]+)',attrs)[1] if re.search(r'add\(Attributes.'+key+r',\s*([\d.]+)',attrs) else default
  hp,damage,armor,speed=stat('MAX_HEALTH','20'),stat('ATTACK_DAMAGE','2'),stat('ARMOR','0'),stat('MOVEMENT_SPEED','0.3')
  pattern=r'(^    '+n.upper()+r'\("[^"]+", Kind.\w+, )[^\n]+'
  old=re.search(pattern,species,re.M)[0];tail=old[old.rfind(', ',0,old.rfind(', ')):] # use final style/aquatic/ranged from original row
  values=old[old.index('Kind.'):].split(', ')
  ending=', '.join(values[-3:])
  row=re.search(pattern,species,re.M)[1]+f'{hp}, {damage}, {armor}, {speed}, {width.lower()}, {height.lower()}, 1f, 0f, '+ending
  species=species.replace(old,row)
  if '.fireImmune()' in declaration:fireimmune.append(n.upper())
  report.append({'id':n,'width':width,'height':height,'health':hp,'damage':damage})
(O/'Species.java').write_text(species)
(R/'verification/source-boss-dimensions.json').write_text(json.dumps(report,indent=2))
p=O/'source/SourceBosses.java';t=p.read_text();i=t.rfind('}')
t=t[:i]+'''public static net.minecraft.world.entity.EntityType.Builder<NightMob> builder(Species s){
var b=EntityType.Builder.<NightMob>of((type,level)->create(type,level,s),s.passive()?MobCategory.CREATURE:MobCategory.MONSTER).sized(s.width,s.height).clientTrackingRange(s.boss()?16:10);
if(!s.boss()&&!s.id.startsWith("mobosses_")||switch(s){case '''+', '.join(fireimmune)+'''->true;default->false;})b.fireImmune();
return b;
}
'''+t[i:];p.write_text(t)
p=O/'DeepNight.java';t=p.read_text();t=re.sub(r'EntityType.Builder.<NightMob>of\(\(t,l\)->dev.deepnight.source.SourceBosses.create\(t,l,s\),s.passive\(\)\?MobCategory.CREATURE:MobCategory.MONSTER\)\s*.sized\(s.width,s.height\).clientTrackingRange\(s.boss\(\)\?16:10\).fireImmune\(\)', 'dev.deepnight.source.SourceBosses.builder(s)',t);p.write_text(t)
p=O/'SummonTool.java';t=p.read_text().replace('        boss.bind(player); boss.setTarget(player);','        boss.finalizeSpawn(level,level.getCurrentDifficultyAt(ground),net.minecraft.world.entity.MobSpawnType.MOB_SUMMONED,null,null);\n        boss.bind(player); boss.setTarget(player);');p.write_text(t)
bosses=re.findall(r'^    (\w+)\("[^\n]+Kind.BOSS',species,re.M)
with zipfile.ZipFile(R/'dist/deep-night-forge-1.20.1-1.1.1-source.zip') as z:
 for boss in bosses:
  for rel in ['assets/deepnight/models/item/'+boss.lower()+'_sigil.json','data/deepnight/recipes/'+boss.lower()+'_essence.json']:
   p=R/'src/main/resources'/rel;p.parent.mkdir(parents=True,exist_ok=True);p.write_bytes(z.read('src/main/resources/'+rel))
lang=json.loads((A/'lang/en_us.json').read_text())
for boss in bosses:
 title=re.search(r'^    '+boss+r'\("([^"]+)',species,re.M)[1]
 lang['entity.deepnight.'+boss.lower()]=title;lang['item.deepnight.'+boss.lower()+'_sigil']=title+' Sigil'
(A/'lang/en_us.json').write_text(json.dumps(lang,indent=2,ensure_ascii=False))
for p in (DATA/'recipes').glob('*.json'):
 t=p.read_text()
 if '_sigil' in t and '"key"' in t:
  v=json.loads(t);rank=re.search(r'\b'+p.stem.upper()+r'\((\d+),',(O/'Weapon.java').read_text())
  if rank:
   index=len(bosses)-1 if int(rank[1])==9 else int(rank[1]);v['key']['S']['item']='deepnight:'+bosses[index].lower()+'_sigil';p.write_text(json.dumps(v,indent=2))
print('Source dimensions installed for',len(report),'creatures;',len(bosses),'campaign bosses')
