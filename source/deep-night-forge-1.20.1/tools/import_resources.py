"""Reproducible import of supplied artwork. Source files are never modified."""
import json, pathlib, shutil, re, copy, struct, hashlib
ROOT=pathlib.Path(__file__).resolve().parents[1]
BASE=ROOT.parent/'the deep night'
RES=ROOT/'src/main/resources'
A=RES/'assets/deepnight'; D=RES/'data/deepnight'
CREATURES={
 'ashen_knight':('souls','blackknight','blackknight'),
 'thorn_tyrant':('aerlunerpg','boarboss','boarboss'),
 'hollow_colossus':('world','giant','giant'),
 'cinder_gargoyle':('souls','gargoyle','gargoyle'),
 'rootbound_ancient':('world','entb','entb'),
 # asylum_demon_texture is a different UV layout; asylumdemon.geo uses asylum_demon.
 'abyssal_demon':('souls','asylumdemon','asylum_demon'),
 'storm_herald':('aerlunerpg','air_elemental','air_elemental'),
 'blood_sovereign':('world','vampire','vampire'),
 'eclipse_wyrm':('world','blood_dragon1','blood_dragon1'),
 'the_deep_night':('aerlunerpg','cthulhu','cthulhu'),
 'ember_drake':('world','blood_dragon1','blood_dragon1'),
 'tide_drake':('world','water_dragon1','water_dragon1'),
 'dusk_drake':('world','flydragon','flydragon'),
 'dire_boar':('aerlunerpg','boar','boar'),
 'moon_stag':('aerlunerpg','deerbig','deerbig'),
 'star_unicorn':('world','unicorn','unicorn'),
 'night_goblin':('world','goblin','goblin'),
 'grave_ghoul':('world','ghoul','ghoul'),
 'serpent_guard':('souls','serpentman','serpentman_texture'),
 'dusk_assassin':('souls','undead_assassin','undead_assassin'),
 'bone_soldier':('souls','skeletonsoldier','skeleton_soldier'),
 'hungry_mimic':('souls','mimic','mimic_texture'),
 'lost_shade':('aerlunerpg','ghost','ghost'),
 'voidling':('aerlunerpg','shoggot_small','shoggot_small'),
}
WEAPONS=re.findall(r'([A-Z_]+)\((\d+),(-?[\d.]+)f,"([a-z_]+)"\)',(ROOT/'src/main/java/dev/deepnight/Weapon.java').read_text())
WEAPONS=[(name.lower(),int(rank),preset) for name,rank,speed,preset in WEAPONS]
ENCHANTS={'berserk':'Below 40% health: +12% melee damage per level.', 'spider_bite':'Melee hits poison enemies for 3/4/5 seconds. One proc per second.',
 'radiance':'Melee hits heal 0.75 health per level, once every 4 seconds. Incompatible with Life Reaper.',
 'frozen_touch':'Melee hits slow enemies for 3/4/5 seconds. Incompatible with Soul Fire.',
 'soul_fire':'Melee hits burn and wither enemies. Incompatible with Frozen Touch.',
 'life_reaper':'Kills heal 1.5 health per level. Incompatible with Radiance.',
 'boss_slayer':'+7% melee damage per level against campaign bosses, the dragon and the wither.',
 'retribution':'Reflect 1.25 damage per level against melee attackers.',
 'vitality':'+2 maximum health per level while wearing the enchanted chestplate.',
 'agility':'+5% movement speed per level while wearing the enchanted boots.',
 'leaping':'Jump Boost I/II while wearing the enchanted boots.',
 'last_stand':'Below 35% health, reduce one hit by 15% per level and gain brief Resistance. 30-second cooldown.',
 'wisdom':'+25% ore experience per level while mining with the enchanted tool.',
 'deep_guard':'Reduce damage from Deep Night mobs and wither by 8% per level (maximum enchantment level on equipped armour).'}
LANG={'itemGroup.deepnight':'The Deep Night'}
TITLES={'awakenedjingubang':'Awakened Jingubang','divinejudgement':'Divine Judgement','heavenlyskyfall':'Heavenly Skyfall','starlightcrescendo':'Starlight Crescendo','tatsukanotsurugi':'Tatsuka no Tsurugi','silentdance':'Silent Dance','tideofautumn':'Tide of Autumn','voidoculus':'Void Oculus'}
manifest=[]
def dump(path,obj):
    path.parent.mkdir(parents=True,exist_ok=True);path.write_text(json.dumps(obj,indent=2)+'\n',encoding='utf-8')
def take(src,dst):
    assert src.is_file() and src.stat().st_size>8, f'Missing or invalid asset {src}'
    dst.parent.mkdir(parents=True,exist_ok=True);shutil.copyfile(src,dst)
    manifest.append({'source':str(src.relative_to(BASE)),'output':str(dst.relative_to(RES)),'sha256':hashlib.sha256(src.read_bytes()).hexdigest()})
def normalize(v):
    if isinstance(v,list):return [normalize(x) for x in v]
    if isinstance(v,dict):
        if set(v)=={'vector'}:return normalize(v['vector'])
        return {('post' if k=='vector' else k):normalize(x) for k,x in v.items()}
    return v
def model(item,tex):
    dump(A/f'models/item/{item}.json',{'parent':'minecraft:item/generated','textures':{'layer0':f'deepnight:item/{tex}'}})
def title(s):return s.replace('_',' ').title()
def recipe(name,pattern,keys):
    dump(D/f'recipes/{name}.json',{'type':'minecraft:crafting_shaped','pattern':pattern,'key':{k:{'item':v} for k,v in keys.items()},'result':{'item':'deepnight:'+name}})
    dump(D/f'advancements/recipes/{name}.json',{'parent':'minecraft:recipes/root','criteria':{'has_material':{'trigger':'minecraft:inventory_changed','conditions':{'items':[{'items':[next(iter(keys.values()))]}]}}},'rewards':{'recipes':['deepnight:'+name]}})
for index,(name,(pack,geo,tex)) in enumerate(CREATURES.items()):
    source=BASE/'mobs'/pack
    take(source/f'geo/{geo}.geo.json',A/f'geo/{name}.geo.json')
    take(source/f'textures/entities/{tex}.png',A/f'textures/entity/{name}.png')
    animations=json.loads((source/f'animations/{geo}.animation.json').read_text())['animations']
    selected={}
    for state in ['idle','walk','attack']:
        original=animations.get(state,animations.get('attack1' if state=='attack' else 'idle',next(iter(animations.values()))))
        selected[state]=normalize({k:v for k,v in original.items() if k in ['loop','animation_length','bones']})
        selected[state]['loop']=True
    dump(A/f'animations/{name}.animation.json',{'format_version':'1.8.0','animations':selected})
    LANG[f'entity.deepnight.{name}']=title(name)
    if index>=10:
        LANG[f'item.deepnight.{name}_spawn_egg']=title(name)+' Spawn Egg'
        dump(A/f'models/item/{name}_spawn_egg.json',{'parent':'minecraft:item/template_spawn_egg'})
    else:
        item=name+'_sigil';LANG['item.deepnight.'+item]=title(name)+' Sigil'
        model(item,'boss_sigil')
    if index>=10:
        biomes='#minecraft:is_overworld'
        if name in ['ember_drake','dire_boar']:biomes=['minecraft:savanna','minecraft:badlands','minecraft:savanna_plateau']
        if name in ['tide_drake']:biomes=['minecraft:beach','minecraft:swamp','minecraft:mangrove_swamp']
        if name in ['dusk_drake','moon_stag']:biomes=['minecraft:taiga','minecraft:dark_forest','minecraft:old_growth_pine_taiga']
        if name=='star_unicorn':biomes=['minecraft:flower_forest','minecraft:meadow','minecraft:cherry_grove']
        if name in ['serpent_guard','hungry_mimic']:biomes=['minecraft:desert','minecraft:badlands']
        if name=='voidling':biomes=['minecraft:end_highlands','minecraft:end_midlands']
        dump(D/f'forge/biome_modifier/{name}.json',{'type':'forge:add_spawns','biomes':biomes,'spawners':{'type':'deepnight:'+name,'weight':2 if index<16 else 5,'minCount':1,'maxCount':1 if index<16 else 2}})
        if index>=16:
            dump(D/f'loot_tables/entities/{name}.json',{'type':'minecraft:entity','pools':[{'rolls':1,'entries':[{'type':'minecraft:item','name':'minecraft:bone','functions':[{'function':'minecraft:set_count','count':{'min':0,'max':2}}]}]}]})
for item,pack,texture in [('boss_summon_tool','world','ancientrelic'),('boss_sigil','world','blood_gem'),('night_essence','world','gem1'),('bonding_treat','world','candy1'),('field_guide','world','book2')]:
    take(BASE/f'mobs/{pack}/textures/item/{texture}.png',A/f'textures/item/{item}.png')
    if item!='boss_sigil':model(item,item);LANG['item.deepnight.'+item]=title(item)
bosses=list(CREATURES)[:10];variants={}
for name,rank,preset in WEAPONS:
    source=BASE/'weapons'
    models=list(source.rglob(name+'.json'))
    assert models,f'Missing weapon model {name}'
    data=json.loads(models[0].read_text())
    texname={'destined_blade':'destinedblade','tatsukanotsurugi':'tatsukunotsurugi','voidoculus':'void_oculus'}.get(name,name)
    textures=list(source.rglob(texname+'.png')); assert textures,name
    take(textures[0],A/f'textures/item/{name}.png')
    metas=list(source.rglob(texname+'.png.mcmeta'))
    if metas:take(metas[0],A/f'textures/item/{name}.png.mcmeta')
    data['textures']={key:'deepnight:item/'+name for key in data['textures']}
    # Fit oversized Blockbench elements into Java's accepted [-16,32] bounds.
    coords=[abs(v-8) for e in data.get('elements',[]) for k in ['from','to'] for v in e[k]]
    factor=max(1,max(coords,default=0)/23.99)
    if factor>1:
        for e in data['elements']:
            for k in ['from','to']:e[k]=[(v-8)/factor+8 for v in e[k]]
            if 'rotation' in e:e['rotation']['origin']=[(v-8)/factor+8 for v in e['rotation']['origin']]
        for pose in data.get('display',{}).values():pose['scale']=[min(4,x*factor) for x in pose.get('scale',[1,1,1])]
    data.pop('groups',None);data.pop('texture_size',None)
    if data.get('elements'):data.pop('parent',None)
    dump(A/f'models/item/{name}.json',data)
    dump(D/f'weapon_attributes/{name}.json',{'parent':'deepnight:fist' if preset=='fist' else 'bettercombat:'+('sword' if preset=='dual_sword' else preset)})
    LANG['item.deepnight.'+name]=TITLES.get(name,title(name))
    n=variants.get(rank,0);variants[rank]=n+1
    recipe(name,[' S ','ENE',' A '],{'S':'deepnight:'+bosses[rank]+'_sigil','E':'deepnight:night_essence','N':'minecraft:netherite_ingot','A':['minecraft:diamond','minecraft:amethyst_shard','minecraft:ender_pearl'][n]})
# Fists use a bundled data preset; animation names are verified against the installed Better Combat jar.
dump(D/'weapon_attributes/fist.json',{'parent':'bettercombat:fist','attributes':{'attack_range':2.5}})
dump(D/'recipes/bonding_treat.json',{'type':'minecraft:crafting_shapeless','ingredients':[{'item':x} for x in ['deepnight:night_essence','minecraft:golden_carrot','minecraft:cooked_beef']],'result':{'item':'deepnight:bonding_treat','count':4}})
dump(D/'recipes/field_guide.json',{'type':'minecraft:crafting_shapeless','ingredients':[{'item':'minecraft:book'},{'item':'minecraft:ender_pearl'}],'result':{'item':'deepnight:field_guide'}})
for name,description in ENCHANTS.items():LANG['enchantment.deepnight.'+name]=title(name);LANG['enchantment.deepnight.'+name+'.desc']=description
dump(A/'lang/en_us.json',LANG)
dump(ROOT/'verification/asset-manifest.json',manifest)
dump(ROOT/'verification/content-catalogue.json',{'creatures':CREATURES,'weapons':WEAPONS,'enchantments':ENCHANTS})
print(f'Imported {len(CREATURES)} creatures, {len(WEAPONS)} weapon models, {len(ENCHANTS)} enchantment descriptions.')
