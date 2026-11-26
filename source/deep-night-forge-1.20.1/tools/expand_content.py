"""Import the remaining modeled creatures, source sounds and canonical animation states."""
import pathlib,json,re,shutil,runpy,math,collections
from geometry_bounds import bounds
from animation_format import normalize
from creature_sizes import apply_record
ROOT=pathlib.Path(__file__).resolve().parents[1]; BASE=ROOT.parent/'the deep night'; RES=ROOT/'src/main/resources'; A=RES/'assets/deepnight';D=RES/'data/deepnight'
def read(p):
    try:
        text=p.read_text(encoding='utf-8-sig')
        text=re.sub(r'("(?:\\.|[^"\\])*")|//[^\n]*|/\*[\s\S]*?\*/',lambda m:m[1] or '',text)
        text=re.sub(r',\s*([}\]])',r'\1',text)
        return json.loads(text)
    except:return {}
def dump(p,d):p.parent.mkdir(parents=True,exist_ok=True);p.write_text(json.dumps(d,indent=2)+'\n',encoding='utf-8')
def slug(s):return re.sub('[^a-z0-9/._-]','_',s.lower())
corefile=ROOT/'tools/core_species.json'
if not corefile.exists():
    src=(ROOT/'src/main/java/dev/deepnight/Species.java').read_text()
    core=[]
    for name,title,kind,nums in re.findall(r'^    ([A-Z_]+)\("([^"]+)", Kind\.(\w+), ([^\n]+)\)[,;]',src,re.M):
        core.append(dict(id=name.lower(),title=title,kind=kind,stats=[float(x.strip().rstrip('f')) for x in nums.split(',')]))
    dump(corefile,core)
# Rebuild the original weapons, enchanting and seed resources first.
original=runpy.run_path(str(ROOT/'tools/import_resources.py'))
records=read(corefile);manifest=read(ROOT/'verification/asset-manifest.json');used={}
for r in records:
    pack,model,texture=original['CREATURES'][r['id']];r.update(pack=pack,model=model,texture=texture,style=records.index(r)%10,passive=False,aquatic=False)
new=[]
def add(id,pack,model,texture=None,kind='MOB',title=None,style=0,height=None,aquatic=False):
    if any(r['id']==id for r in records+new):return
    new.append(dict(id=id,pack=pack,model=model,texture=texture or model,kind=kind,title=title or id.replace('_',' ').title(),style=style,passive=kind=='PASSIVE',aquatic=aquatic,height=height))
# Every distinct living world/souls/aerlune model. Alternate poses are linked in coverage.
for model,texture,kind,name in [
 ('bat_evil','bat_evil','MOB','cave_bat'),('butterfly','butterfly','PET','meadow_butterfly'),('butterfly1','butterfly','PET','forest_butterfly'),
 ('centaur','centaur','PASSIVE','centaur_ranger'),('cyclops01','cyclops01','BOSS','stone_cyclops'),('cyclops1','cyclops1','BOSS','elder_cyclops'),
 ('dryad','dryad','PASSIVE','woodland_dryad'),('dryad2','dryad2','PASSIVE','verdant_dryad'),('dryad3','dryad3','PASSIVE','blossom_dryad'),
 ('ghost_guardian','ghost_guardian','MOB','ghost_guardian'),('goblin2','goblin2','MOB','goblin_swordsman'),('goblin_shaman','goblin_shaman','MOB','goblin_shaman'),
 ('gravedigger','gravedigger','MOB','gravedigger'),('kraken','kraken','BOSS','abyssal_kraken'),('kraken_tentacle','kraken_tentacle','MOB','kraken_tentacle'),
 ('nimfa','nimfa','PASSIVE','river_nymph'),('oldman1','oldman1','PASSIVE','old_wizard'),('vampire_old','vampire_old','BOSS','elder_nosferatu'),
 ('whale1','whale','PET','ocean_whale'),('wings_eye','eye_wings','MOB','winged_eye'),('blood_dragon1','blood_dragon1','BOSS','crimson_abomination'),
 ('water_dragon1','water_dragon1','BOSS','three_headed_hydra')]:add(name,'world',model,texture,kind,aquatic=name in ['abyssal_kraken','kraken_tentacle','ocean_whale'])
for model,texture,name,kind in [('cyclops10','cyclops1','cave_cyclops','MOB'),('vampire01','vampire','vampire_noble','MOB'),('vampire1','vampire','vampire_duelist','MOB'),('vampireee','vampire','vampire_stalker','MOB'),('whal2e','whale','silver_whale','PET')]:
    add(name,'world',model,texture,kind,aquatic=name=='silver_whale')
for model,texture,kind,name in [('balder','balder','MOB','balder_knight'),('blacksmithgeo','andre_texture','PASSIVE','ashen_blacksmith'),('channeler','channeler','MOB','soul_channeler'),('merchant','merchantvillager','PASSIVE','undead_merchant')]:add(name,'souls',model,texture,kind)
for row in '''bearcandy|bearcandy|PET|candy_bear
bedbug1|bedbug|MOB|giant_bedbug
boarbig|boarbig|PET|great_boar
boss|boss|BOSS|starborn_tyrant
buterfly1|butterfly_big|BOSS|foreign_matriarch
buttermini|buttermini|PET|moon_butterfly
cannongnome|cannongnome|MOB|cannon_gnome
centipede|centipede|BOSS|alien_centipede
coockie|coockie|MOB|cookie_mimic
coockies|coockie|MOB|cookie_swarm
crowd|crowd|MOB|the_crowd
deerevil|deertallman|MOB|cursed_stag
deerevill|deertallman|MOB|tallwood_stalker
donut|donut|MOB|donut_mimic
elka|elka|MOB|evergreen_watcher
elkabiggift|elkabiggift|BOSS|giftbound_evergreen
ent|ent|BOSS|elder_forest_ent
ent_tree_small|tree_ent_small|PET|sapling_ent
evilgnomb|evilgnomb|MOB|bomb_gnome
evilgnome|evilgnome|MOB|forest_gnome
evilgnome2|evilgnome2|MOB|spear_gnome
evilgnomsh|evilgnomsh|MOB|shield_gnome
eye_beatle11|eye11|MOB|eye_beetle
gingbrdmanbr|gingerbreadman1|MOB|gingerbread_brute
gingerbreadman22|gingerbreadman1|MOB|gingerbread_man
gingirmanevil|gingirmanevil|MOB|cursed_gingerbread
gingirmanevilred|gingirmanevilred|MOB|crimson_gingerbread
gnomeh|gnomehome|PASSIVE|house_gnome
golem1|golem|BOSS|mithril_golem
house|house|BOSS|walking_house
oldmanwood|oldmanwood|PASSIVE|woodland_sage
rabbit|rabbit|PET|woodland_rabbit
rabbitpet|rabbitpet|PET|fluffy_hound
robotgnome|robotgnoem|MOB|clockwork_gnome
shoggot_small11|shoggot_small11|MOB|abyssal_shoggoth
small_bat|small_bat|PET|tiny_bat
small_beatle|small_beatle|PET|luminous_beetle
snowgolem|snowgolem|PET|frost_golem
snowman|snowman|MOB|frozen_watchman
snowman2|snowman2|MOB|frozen_sentinel
snowman3|snowman2|MOB|frostbound_revenant
snowmann|snowman|MOB|snowbound_guard
squirrel|squirrel|PET|woodland_squirrel
stump|stump|MOB|living_stump
stump2|stump2|MOB|ancient_stump
warm|warm|MOB|burrowing_worm
zombie|zombie|MOB|forest_husk
zombieghost|zombieghost|MOB|spectral_husk'''.splitlines():
    model,tex,kind,id=row.split('|');add(id,'aerlunerpg',model,tex,kind)
# Bedrock source descriptors give exact geometry/texture/animation/sound relationships.
BOX=BASE/'mobs/boxxexpansion/resource_packs/rp0'
boxgeo={};boxanimations={}
for p in (BOX/'models').rglob('*.json'):
    for g in read(p).get('minecraft:geometry',[]):boxgeo[g['description']['identifier']]=(p,g)
for p in (BOX/'animations').rglob('*.json'):
    for key,value in read(p).get('animations',{}).items():boxanimations[key]=(p,value)
boxids={}
for p in (BOX/'entity').rglob('*.json'):
    desc=read(p).get('minecraft:client_entity',{}).get('description',{})
    if desc:boxids[p.relative_to(BOX/'entity').as_posix()]=(p,desc)
boxselection={
 'dragon/fire_dragon.json':('inferno_dragon','BOSS'), 'kingkong/kingkong.json':('mountain_kong','BOSS'),
 'knight/knight_boss.entity.json':('iron_overlord','BOSS'),'lizard/lizard.json':('sandstorm_lizard','BOSS'),
 'mecha/mecha.entity.json':('arcane_mecha','BOSS'),'sea_snake/sea_snake.json':('leviathan','BOSS'),
 'thunder_lord/thunder_lord.json':('thunder_lord','BOSS'),'yeti/yeti_boss.json':('frost_king','BOSS'),
 'goblin/goblin_alone.json':('goblin_warlord','BOSS'),
 'bear.json':('honey_bear','PET'),'custom_frog.json':('pond_frog','PET'),
 'Pigeon.json':('city_pigeon','PET'),'scorpion.json':('dune_scorpion','PET'),
 'sea turtle.entity.json':('sea_turtle','PET'),'seagull.entity.json':('seagull','PET'),
 'seagull_idle.json':('coastal_gull','PET'),'pelican_idle.json':('pelican','PET'),'snake.entity.json':('jungle_snake','PET'),
 'kingkong/kingkong_small.json':('young_kong','PET'),'knight/little_knight.json':('little_knight','MOB'),
 'cactus/poison_cactus.json':('poison_cactus','MOB'),'goblin/goblin_barbarian.json':('goblin_barbarian','MOB'),
 'goblin/goblin_fire.json':('fire_goblin','MOB'),'goblin/goblin_normal.json':('cave_goblin','MOB'),
 'piranha/piranha.json':('piranha','MOB'),
 'skeleton/desert_skeleton.json':('desert_skeleton','MOB'),'skeleton/ice_skeleton.json':('ice_skeleton','MOB'),
 'skeleton/iron_skeleton.json':('iron_skeleton','MOB'),'skeleton/normal_skeleton.json':('lost_skeleton','MOB'),
}
for rel,(id,kind) in boxselection.items():
    if rel not in boxids:raise ValueError('Missing Boxx descriptor '+rel)
    p,desc=boxids[rel];add(id,'boxx',rel,kind=kind,aquatic=id in ['leviathan','sea_turtle','piranha']);new[-1]['descriptor']=desc
# Include provided humanoid NPC models as neutral interactive inhabitants.
import copy
mounted=copy.deepcopy(boxids['goblin/goblin_alone.json'][1])
mounted['geometry']={'default':mounted['geometry']['mount']};mounted['textures']={'default':mounted['textures']['mount']}
mounted['animations']={k.removesuffix('_mount'):v for k,v in mounted['animations'].items() if k.endswith('_mount')}
add('goblin_ravager','boxx','goblin/goblin_alone.json',kind='BOSS');new[-1]['descriptor']=mounted
drake=copy.deepcopy(boxids['dragon/fire_dragon.json'][1]);drake['geometry']={'default':'geometry.fire_dragon_saddled'}
add('inferno_wyvern','boxx','dragon/fire_dragon.json',kind='PET');new[-1]['descriptor']=drake
for var,id in [('var1','desert_spearman'),('var2','desert_bladeguard'),('var5','desert_revenant')]:
    skeleton=copy.deepcopy(boxids['skeleton/desert_skeleton.json'][1]);skeleton['geometry']={'default':skeleton['geometry'][var]}
    add(id,'boxx','skeleton/desert_skeleton.json',kind='MOB');new[-1]['descriptor']=skeleton
for rel,(p,desc) in boxids.items():
    if rel.startswith('npc/') and not re.search('death|pose|dummy|shadow|child|invisible',rel):
        gid=desc.get('geometry',{}).get('default');tex=desc.get('textures',{}).get('default')
        if gid in boxgeo and tex and (BOX/(tex+'.png')).exists():
            add('travelling_'+slug(p.stem.replace('.entity','')),'boxx',rel,kind='PASSIVE');new[-1]['descriptor']=desc
BOSSPLUS=BASE/'mobs/bosses+'
for key,title in [('endershulk','Endershulk Regent'),('frog_boss','Bog Sovereign'),('golem_boss','Runic Golem'),('mammoth_boss','Glacial Mammoth'),('phantom_boss','Phantom Monarch'),('piglin_boss','Piglin Warchief'),('sculkpion_boss','Sculkpion'),('yeti_boss','Rimefang Yeti')]:
    add('ancient_'+key,'bosses+',key,kind='BOSS',title=title)
for key in ['phantom_ghost','sculkpion_minion']:add(key,'bosses+',key)
# Distinct recognisable dragons from the supplied Dragonfire base, not the blood-abomination mesh.
DRAGON=ROOT.parent/'dragonfire-forge-1.20.1/src/main/resources/assets/dragonfire'
for r in records:
    if r['id'] in ['ember_drake','eclipse_wyrm','tide_drake']:
        r.update(pack='dragonfire',model={'ember_drake':'searclaw','eclipse_wyrm':'endprowler','tide_drake':'tidewing'}[r['id']],texture=None)
records=[r for r in records if r['id']!='the_deep_night']+new+[r for r in records if r['id']=='the_deep_night']
records.sort(key=lambda r:0 if r['kind']=='BOSS' else 1 if r['kind']=='PET' else 2)
errors=[];mapped=[]
def resolve(r):
    pack=r['pack'];name=r['model'];tex=r.get('texture');desc=r.get('descriptor',{})
    if pack=='boxx':
        gp,g=boxgeo[desc['geometry'].get('default',next(iter(desc['geometry'].values())))];geometry={'format_version':'1.12.0','minecraft:geometry':[g]}
        texture=BOX/(desc['textures'].get('default',next(iter(desc['textures'].values())))+'.png')
        anims={k:boxanimations[v][1] for k,v in desc.get('animations',{}).items() if v in boxanimations}
        # Death is supplied as a separate entity in this pack; merge that same model's clips.
        for rel,(p,d) in boxids.items():
            if 'death' in rel and d.get('geometry',{}).get('default')==g['description']['identifier']:
                anims.update({k:boxanimations[v][1] for k,v in d.get('animations',{}).items() if v in boxanimations})
                desc.setdefault('sound_effects',{}).update(d.get('sound_effects',{}))
        return gp,geometry,texture,anims
    if pack=='dragonfire':
        gp=DRAGON/f'geo/{name}.geo.json';return gp,read(gp),DRAGON/f'textures/entity/{name}.png',read(DRAGON/f'animations/{name}.animation.json')['animations']
    base=BASE/'mobs'/pack
    if pack=='bosses+':
        gp=next(p for p in (base/'entity').rglob(name+'.geo.json'));ap=next((base/'animations').rglob(name+'.animation.json'))
        texture=next((base/'textures').rglob(name+'.png'),None)
        if texture is None and name=='mammoth_boss':texture=next((base/'textures').rglob('mammoth_boss_1.png'))
        return gp,read(gp),texture,read(ap)['animations']
    gp=base/f'geo/{name}.geo.json';anims=read(base/f'animations/{name}.animation.json').get('animations',{})
    textures=list((base/'textures').rglob(tex+'.png'))
    if not textures:
        aliases={'butterfly':'butterfly_blue','small_bat':'bat_evil','squirrel':'squirrelblock','oldmanwood':'oldman_trade'}
        if tex in aliases:textures=list((base/'textures').rglob(aliases[tex]+'.png'))
    if not textures:raise ValueError('No texture '+str(base)+' / '+tex)
    texture=sorted(textures,key=lambda p:('entities' not in p.parts,'entity' not in p.parts))[0]
    return gp,read(gp),texture,anims
STATES={'idle':['idle_main','idle_combat','idle','static','general','pose'], 'walk':['walk_combat','walk','move','run','swim'],
 'attack':['melee_attack','attack_1','attack1','attack','scratch_attack','shoot','throwing','attack_underwater'],
 'death':['death','dead','death_1','death1'], 'hurt':['hurt','damage','hit'], 'sit':['sit','sleep'],
 'fly':['fly_idle','idle_fly','fly','animFlying','glide'], 'spawn':['spawn_animation','spawn','summon']}
def choose(anims,terms):
    for term in terms:
        for key,value in anims.items():
            if key.lower()==term.lower() or key.lower().endswith('.'+term.lower()) or key.lower().endswith('_'+term.lower()):return value
    for term in terms:
        for key,value in anims.items():
            if term.lower() in key.lower():return value
    return None
for r in records:
    try:
        gp,g,texture,anims=resolve(r)
        if not g.get('minecraft:geometry') or not texture or not texture.is_file():raise ValueError('Incomplete model/texture')
        idle=choose(anims,STATES['idle']) or next(iter(anims.values()),{'loop':True,'bones':{}})
        canonical={}
        for state,terms in STATES.items():
            clip=choose(anims,terms) or idle
            canonical[state]=normalize({k:v for k,v in clip.items() if k in ['loop','animation_length','bones']})
            canonical[state]['loop']=state in ['idle','walk','fly','sit']
        for key,value in anims.items():canonical['source.'+slug(key)]=normalize({k:v for k,v in value.items() if k in ['loop','animation_length','bones']})
        dump(A/f'geo/{r["id"]}.geo.json',g);dump(A/f'animations/{r["id"]}.animation.json',{'format_version':'1.8.0','animations':canonical})
        dst=A/f'textures/entity/{r["id"]}.png';dst.parent.mkdir(parents=True,exist_ok=True);shutil.copyfile(texture,dst)
        r['geometry_source']=str(gp);r['texture_source']=str(texture);r['clips']=len(anims)
        used.setdefault(str(gp),[]).append(r['id'])
        if 'stats' not in r or r['pack']=='dragonfire':
            height=r.get('height') or (4 if r['kind']=='BOSS' else 1.5 if r['kind']=='PET' else 1.9)
            if r['kind']=='PET' and re.search('butterfly|beetle|bat|frog|squirrel|pigeon|snake|scorpion',r['id']):height=0.55
            elif r['kind']=='PET' and re.search('rabbit|hound|gull|pelican|turtle',r['id']):height=0.85
            elif r['id'] in ['ocean_whale','silver_whale']:height=2.5
            lo,hi=bounds(g,canonical['idle']);native=max(1,(hi[1]-lo[1])/16);scale=height/native
            r['stats']=[90 if r['kind']=='PET' else 36,8,5,0.29, min(height,1.6 if r['kind']!='BOSS' else 3),height,round(scale,5)]
            r['lift']=round(max(0,-lo[1]/16*scale),4)
        r.setdefault('lift',{'the_deep_night':3.5,'voidling':0.3}.get(r['id'],0))
        # Verified in the Java renderer: Bedrock's rotated root points upward here.
        if r['id']=='ancient_phantom_boss':r['lift']=0
        calibration=read(ROOT/'tools/render_calibration.json').get(r['id'])
        if calibration:r['stats'][6]=calibration['scale'];r['lift']=calibration['lift']
        apply_record(r)
        mapped.append(r)
    except Exception as e:errors.append({'id':r['id'],'pack':r['pack'],'model':r['model'],'error':str(e)})
dump(ROOT/'verification/expansion-missing.json',errors)
dump(ROOT/'tools/expanded_species.json',mapped)
print('RESOLVED',len(mapped),'UNRESOLVED',len(errors));print(json.dumps(errors,indent=2))

if errors:raise SystemExit('Resolve every incomplete creature before generating the registry.')
bosses=[r for r in mapped if r['kind']=='BOSS'];lang=read(A/'lang/en_us.json')
for i,r in enumerate(bosses):
    r['stats'][0]=360+80*i;r['stats'][1]=12+1.6*i;r['stats'][2]=min(30,6+i*0.75)
    if r['id'] not in original['CREATURES']:
        r['style']=6 if re.search('storm|thunder|ice|frost|rime|glacial',r['id']) else 8 if re.search('dragon|hydra|crimson|inferno',r['id']) else 1 if re.search('mammoth|kong|cyclops',r['id']) else 4 if re.search('ent|tree|evergreen|house',r['id']) else i%10
    if r['id']=='the_deep_night':r['style']=9
# Preserve the ten original weapon unlock identities even as more bosses join the ladder.
original_bosses=list(original['CREATURES'])[:10]
for name,rank,preset in original['WEAPONS']:
    recipe=read(D/f'recipes/{name}.json');recipe['key']['S']['item']='deepnight:'+original_bosses[rank]+'_sigil';dump(D/f'recipes/{name}.json',recipe)
entries=[]
for r in mapped:
    s=r['stats'];title=json.dumps(r['title']);id=r['id'];r['ranged']=bool(re.search('shaman|channeler|eye|cactus|wizard',id))
    entries.append(f'    {id.upper()}({title}, Kind.{r["kind"]}, '+', '.join(str(round(n,5)) for n in s[:4])+', '+', '.join(str(round(n,5))+'f' for n in s[4:])+f', {r["lift"]}f, {r["style"]}, {str(r["aquatic"]).lower()}, {str(r["ranged"]).lower()})')
    lang['entity.deepnight.'+id]=r['title']
    egg=A/f'models/item/{id}_spawn_egg.json'
    if r['kind']!='BOSS':dump(egg,{'parent':'minecraft:item/template_spawn_egg'});lang['item.deepnight.'+id+'_spawn_egg']=r['title']+' Spawn Egg'
    if r['kind']=='BOSS':
        dump(A/f'models/item/{id}_sigil.json',{'parent':'minecraft:item/generated','textures':{'layer0':'deepnight:item/boss_sigil'}})
        lang['item.deepnight.'+id+'_sigil']=r['title']+' Sigil'
        if id not in original_bosses:dump(D/f'recipes/{id}_sigil_essence.json',{'type':'minecraft:crafting_shapeless','ingredients':[{'item':'deepnight:'+id+'_sigil'}],'result':{'item':'deepnight:night_essence','count':16}})
    spawn=D/f'forge/biome_modifier/{id}.json'
    if r['kind'] in ['BOSS','PET']:
        if spawn.exists():spawn.unlink() # Only this exact generated resource, inside this project.
    else:
        biomes='#minecraft:is_ocean' if r['aquatic'] else ['minecraft:desert','minecraft:badlands'] if re.search('sand|desert|cactus',id) else ['minecraft:snowy_plains','minecraft:snowy_taiga'] if re.search('ice|snow|frost|frozen',id) else '#minecraft:is_overworld'
        dump(spawn,{'type':'forge:add_spawns','biomes':biomes,'spawners':{'type':'deepnight:'+id,'weight':1 if r['kind']=='PASSIVE' else 2,'minCount':1,'maxCount':1}})
body='''package dev.deepnight;
// Generated by tools/expand_content.py from the supplied creature catalogue.
public enum Species {
ENTRIES;
    public enum Kind { BOSS, PET, MOB, PASSIVE }
    public final String title,id;
    public final Kind kind;
    public final double health,damage,armor,speed;
    public final float width,height,scale,lift;
    public final int style;
    public final boolean aquatic,ranged;
    Species(String title,Kind kind,double health,double damage,double armor,double speed,float width,float height,float scale,float lift,int style,boolean aquatic,boolean ranged){
        this.title=title;this.id=name().toLowerCase(java.util.Locale.ROOT);this.kind=kind;
        this.health=health;this.damage=damage;this.armor=armor;this.speed=speed;this.width=width;this.height=height;this.scale=scale;this.lift=lift;this.style=style;this.aquatic=aquatic;this.ranged=ranged;
    }
    public boolean boss(){return kind==Kind.BOSS;}
    public boolean passive(){return kind==Kind.PASSIVE;}
    public boolean dragon(){return this==EMBER_DRAKE||this==TIDE_DRAKE||this==DUSK_DRAKE;}
    public static final int BOSS_COUNT=BOSS_TOTAL;
    public static Species boss(int stage){return values()[stage];}
}
'''.replace('ENTRIES',',\n'.join(entries)).replace('BOSS_TOTAL',str(len(bosses)))
(ROOT/'src/main/java/dev/deepnight/Species.java').write_text(body,encoding='utf-8')

# Remap every supplied OGG and source sound event into the mod's namespace.
sounddefs={};sourceevents={};soundfiles=[]
for pack,base in [('world',BASE/'mobs/world'),('souls',BASE/'mobs/souls'),('aerlunerpg',BASE/'mobs/aerlunerpg'),('bosses_plus',BOSSPLUS),('boxx',BOX),('dragonfire',DRAGON)]:
    files={}
    for file in (base/'sounds').rglob('*.ogg'):
        relative=file.relative_to(base/'sounds').with_suffix('').as_posix();key=slug(relative);dest=A/f'sounds/imported/{pack}/{key}.ogg'
        dest.parent.mkdir(parents=True,exist_ok=True);shutil.copyfile(file,dest);files[relative.lower()]=f'deepnight:imported/{pack}/{key}'
        event=f'imported.{pack}.'+slug(relative).replace('/','.');sounddefs[event]={'sounds':[{'name':files[relative.lower()],'stream':True} if re.search('music|theme|bossfight|epic',relative,re.I) else files[relative.lower()]]};soundfiles.append(str(file))
    definitions=read(base/'sounds.json') or read(base/'sounds/sound_definitions.json').get('sound_definitions',{})
    pool={}
    for old,entry in definitions.items():
        sounds=[]
        for value in entry.get('sounds',[]):
            info={'name':value} if isinstance(value,str) else value.copy();name=info.get('name','').split(':')[-1].removeprefix('sounds/').lower()
            name=name.removesuffix('.ogg')
            if name in files:
                output={k:v for k,v in info.items() if k in ['volume','pitch','weight','stream']};output['name']=files[name]
                if re.search('music|theme|bossfight|epic',name,re.I):output['stream']=True
                sounds.append(output)
        if sounds:
            key=f'source.{pack}.'+slug(old);sounddefs[key]={'sounds':sounds};pool[old]=key
    # Individual file events remain available even if the supplied sound definition omits them.
    for file in files:pool.setdefault(file,f'imported.{pack}.'+slug(file).replace('/','.'))
    sourceevents[pack]=pool
aliases={
 'blackknight':['undead'],'asylumdemon':['demon'],'gargoyle':['gargoyle'],'serpentman':['serpent'], 'skeletonsoldier':['undead'],'undead_assassin':['undead'],
 'giant':['giant','gigant'],'entb':['ent'],'vampire':['vampire'],'blood_dragon1':['demon'],'water_dragon1':['snake'],
 'goblin':['gnome'],'goblin2':['gnome'],'goblin_shaman':['gnome'],'ghost_guardian':['ghost'],'ghoul':['ghoul'],'wings_eye':['buzzing'],
 'boar':['demonhit'],'boarboss':['demonhit'],'deerbig':['deer'],'air_elemental':['air'],'cthulhu':['bosses','epic'],
 'searclaw':['searclaw','breath_fire'],'endprowler':['endprowler','breath_fire'],'tidewing':['tidewing','breath_fire'],
}
soundmap={}
for r in mapped:
    pack='bosses_plus' if r['pack']=='bosses+' else r['pack'];pool=sourceevents[pack];model=r['model'];short=pathlib.Path(model).stem.replace('.entity','')
    terms=aliases.get(short,[short.replace('_boss',''),r['id'].replace('ancient_','')]);desc=r.get('descriptor',{})
    explicit=set(desc.get('sound_effects',{}).values())
    candidates={old:new for old,new in pool.items() if old in explicit or any(term in old.lower() for term in terms)}
    if not candidates and pack in ['world','aerlunerpg']:
        other=sourceevents['aerlunerpg' if pack=='world' else 'world'];candidates={old:new for old,new in other.items() if any(term in old.lower() for term in terms)}
    roles={}
    for role,pattern in [('ambient','idle|ambient|growl|speak|voice'),('hurt','hurt|hit'),('death','death|die'),('attack','attack|spit|shoot|slash|roar|howl|throw|bighit|breath|swing'),('spawn','spawn|summon|roar|howl'),('step','step|walk|swim|fly'),('music','theme|music|bosses|epic|ossfight')]:
        matches=[v for k,v in candidates.items() if re.search(pattern,k,re.I)]
        if not matches and role=='ambient':matches=[v for k,v in candidates.items() if not re.search('theme|music|epic|ossfight|attack|hurt|hit|death|die|step|walk|spawn|summon|breath',k,re.I)][:3]
        if matches:
            key=r['id']+'.'+role;sounddefs[key]={'subtitle':'subtitles.deepnight.'+key,'sounds':[{'name':'deepnight:'+v,'type':'event'} for v in dict.fromkeys(matches)]};roles[role]=key;lang['subtitles.deepnight.'+key]=r['title']+' '+role
    soundmap[r['id']]=roles
dump(A/'sounds.json',sounddefs);dump(D/'creature_sounds.json',soundmap);dump(A/'lang/en_us.json',lang)
lang['item.deepnight.pet_summoning_tool']='Pet Summoning Tool';dump(A/'lang/en_us.json',lang)
shutil.copyfile(A/'textures/item/bonding_treat.png',A/'textures/item/pet_summoning_tool.png')
dump(A/'models/item/pet_summoning_tool.json',{'parent':'minecraft:item/generated','textures':{'layer0':'deepnight:item/pet_summoning_tool'}})
dump(D/'recipes/pet_summoning_tool.json',{'type':'minecraft:crafting_shaped','pattern':[' E ','GBG',' A '],'key':{'E':{'item':'minecraft:ender_pearl'},'G':{'item':'minecraft:gold_ingot'},'B':{'item':'minecraft:book'},'A':{'item':'minecraft:golden_apple'}},'result':{'item':'deepnight:pet_summoning_tool'}})
dump(D/'advancements/recipes/pet_summoning_tool.json',{'parent':'minecraft:recipes/root','criteria':{'book':{'trigger':'minecraft:inventory_changed','conditions':{'items':[{'items':['minecraft:book']}]}}},'rewards':{'recipes':['deepnight:pet_summoning_tool']}})
dump(ROOT/'tools/expanded_species.json',mapped)
dump(ROOT/'verification/content-catalogue.json',{'creatures':mapped,'weapons':original['WEAPONS'],'enchantments':original['ENCHANTS'],'boss_count':len(bosses),'pet_count':sum(r['kind']=='PET' for r in mapped),'sound_files':len(soundfiles),'sound_events':len(sounddefs)})
coverage=[]
alternate={'cyclops_sleep':'elder_cyclops','mimicchest':'hungry_mimic','oldman_trade':'woodland_sage','squirrelblock':'woodland_squirrel','mecha_start':'arcane_mecha','ground':'desert_skeleton'}
for pack,folder in [('world',BASE/'mobs/world/geo'),('souls',BASE/'mobs/souls/geo'),('aerlunerpg',BASE/'mobs/aerlunerpg/geo'),('bosses+',BOSSPLUS/'entity'),('boxx',BOX/'models')]:
    for p in folder.rglob('*.json'):
        ids=used.get(str(p),[])
        stem=p.name.split('.')[0];parts=str(p.relative_to(folder)).lower()
        if ids:category='implemented creature'
        elif stem in alternate:category='alternate pose or model part of '+alternate[stem]
        elif 'pose' in parts:category='display pose of an implemented creature'
        elif re.search('armor|weapons|3ditems|demonhammer|axeyellow|boarhead|head|hatc|bookitem|rod|scrol|pipe|fur_',parts):category='equipment, wearable or trophy'
        elif re.search('fireball|projectile|ammo|particle|energy|enen|lightning|tornado|shockwave|laser|spike|thunder|tsunami|boulder|circle|crystal|blackhole|infection|stunning|explosion',parts):category='projectile, spell effect or effect prop'
        else:category='scenery, furniture, vehicle, UI, decoration or static prop'
        coverage.append({'source':str(p.relative_to(BASE)),'classification':category,'entities':ids})
dump(ROOT/'verification/model-coverage.json',coverage)
import hashlib
sourcefiles=sorted(set(soundfiles+[r[k] for r in mapped for k in ['geometry_source','texture_source']]))
dump(ROOT/'verification/expanded-asset-provenance.json',[{'source':p,'sha256':hashlib.sha256(pathlib.Path(p).read_bytes()).hexdigest()} for p in sourcefiles])
print('REGISTRY',len(mapped),'BOSSES',len(bosses),'PETS',sum(r['kind']=='PET' for r in mapped),'SOUNDS',len(soundfiles))
runpy.run_path(str(ROOT/'tools/build_boss_attacks.py'),run_name='__main__')
