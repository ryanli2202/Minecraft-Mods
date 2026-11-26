"""Adapt the supplied three Java mod creature catalogues to Deep Night.

Run with --audit to resolve and convert everything without changing runtime files.
The selection manifest is explicit: auxiliary spells, hitboxes, corpses and
spawners never become menu entries. Original input trees remain untouched.
"""
from pathlib import Path
import json,re,copy,shutil,sys,hashlib,collections,math,functools
from audit_java_imports import SOURCES
from java_model_conversion import geometry,animations
from creature_sizes import apply_java
from boss_roster import apply_roster, clean_resources
from animation_format import normalize,validate_animation_channels
from geometry_bounds import bounds

ROOT=Path(__file__).resolve().parents[1];RES=ROOT/'src/main/resources';A=RES/'assets/deepnight';D=RES/'data/deepnight'
def read(p):return json.loads(p.read_text(encoding='utf-8-sig'))
def dump(p,v):p.parent.mkdir(parents=True,exist_ok=True);p.write_text(json.dumps(v,indent=2,ensure_ascii=False)+'\n',encoding='utf-8')
def snake(name):return re.sub(r'(?<!^)(?=[A-Z][a-z]|(?<=[a-z0-9])[A-Z])','_',name).lower()
@functools.lru_cache(None)
def source_index(java):return {p.stem:p for p in java.rglob('*.java')}
def find(java,name):
    found=source_index(java).get(name);assert found,(name,'source missing')
    return found
def resource_strings(text):
    return re.findall(r'"((?:geo/|animations/|textures/)[^"+]+\.(?:json|png))"',text)

def humanoid(id):
    # Source renderers explicitly use Minecraft's PLAYER layer.
    bones=[]
    for name,pivot,origin,size,uv in [
        ('head',[0,24,0],[-4,24,-4],[8,8,8],[0,0]),
        ('body',[0,24,0],[-4,12,-2],[8,12,4],[16,16]),
        ('right_arm',[5,22,0],[4,12,-2],[4,12,4],[40,16]),
        ('left_arm',[-5,22,0],[-8,12,-2],[4,12,4],[32,48]),
        ('right_leg',[1.9,12,0],[-0.1,0,-2],[4,12,4],[0,16]),
        ('left_leg',[-1.9,12,0],[-3.9,0,-2],[4,12,4],[16,48])]:
        bones.append({'name':name,'pivot':pivot,'cubes':[{'origin':origin,'size':size,'uv':uv}]})
    bones.append({'name':'hat','parent':'head','pivot':[0,24,0],'cubes':[{'origin':[-4,24,-4],'size':[8,8,8],'uv':[32,0],'inflate':0.5}]})
    return {'format_version':'1.12.0','minecraft:geometry':[{'description':{'identifier':'geometry.'+id,'texture_width':64,'texture_height':64},'bones':bones}]}
def style(name):
    s=name.lower()
    if any(x in s for x in ['fire','ignis','romora','karatos','demon']):return 8
    if any(x in s for x in ['lightning','storm','electric','electrode']):return 6
    if any(x in s for x in ['ice','frost','water','valar','kaneri','dialgas']):return 6
    if any(x in s for x in ['thorn','forest','earth','golem','ent','basilisk']):return 4
    if any(x in s for x in ['giant','ogre','rat','minotaur']):return 2
    if any(x in s for x in ['wraith','nil','herobrine','phantom']):return 9
    if any(x in s for x in ['wind','dragon','gryphon','harpy']):return 1
    return 0

def canonical(clips,geo,boss):
    originals=copy.deepcopy(clips)
    if not originals:
        moving={}
        for bone in geo['minecraft:geometry'][0]['bones']:
            n=bone['name']
            if re.search('arm|leg',n,re.I):
                sign=-1 if 'left' in n.lower() else 1
                moving[n]={'rotation':{'0.0':[25*sign,0,0],'0.5':[-25*sign,0,0],'1.0':[25*sign,0,0]}}
        originals={'idle':{'animation_length':1,'bones':{}},'walk':{'animation_length':1,'loop':True,'bones':moving}}
    def choose(tokens):
        for token in tokens:
            exact=next((k for k in originals if k.lower().split('.')[-1]==token),None)
            if exact:return exact
            match=next((k for k in originals if token in k.lower()),None)
            if match:return match
        return None
    mapping={}
    for role,tokens in {'idle':['idle','stand','fly','walk'], 'walk':['walk','run','move','fly'], 'fly':['fly','flight','idle'],
        'attack':['attack','slash','bite','smash','strike','swing','punch','flame'], 'sit':['sit','rest','idle'],
        'death':['death','die','dead'], 'hurt':['hurt','hit'], 'spawn':['spawn','roar','idle']}.items():
        key=choose(tokens);mapping[role]=key
        clips[role]=copy.deepcopy(originals[key]) if key else {'animation_length':1.0,'bones':{}}
        clips[role]['loop']=role in ('idle','walk','sit','fly')
        valid={b['name'] for b in geo['minecraft:geometry'][0]['bones']}
        clips[role]['bones']={n:v for n,v in clips[role].get('bones',{}).items() if n in valid}
    profile=None
    if boss:
        attack=clips['attack'];duration=max(1.2,min(5.0,float(attack.get('animation_length') or 1.5)))
        # Adapt the primary source motion into a committed Deep Night strike.
        # Keep all originals separately; the authored anticipation/contact curve
        # guarantees even static or source-procedure-only attacks have a windup.
        names=[b['name'] for b in geo['minecraft:geometry'][0]['bones']]
        arm=next((n for n in names if re.search(r'(right.?arm|arm.?right|rightarm|head|jaw)',n,re.I)),names[0])
        contact=round(duration*0.55,3)
        attack['animation_length']=duration;attack['loop']=False
        # Retain source channels that fall inside this normalized duration.
        for channels in attack.get('bones',{}).values():
            for key,value in list(channels.items()):
                if isinstance(value,dict) and 'vector' not in value:
                    channels[key]={t:v for t,v in value.items() if float(t)<=duration}
        attack.setdefault('bones',{}).setdefault(arm,{})['rotation']={
            '0.0':[0,0,0],str(round(contact*0.7,3)):[-35,-12,0],str(contact):[35,16,0],str(duration):[0,0,0]}
        attack['timeline']={str(contact):['deepnight:impact']}
        profile={'durationTicks':duration*20,'impactTicks':[contact*20],'reach':4.5}
    for key,clip in originals.items():clips['source.'+key]=clip
    def repair(value):
        # Three original GeckoLib 3 clips contain bare '-' numeric placeholders.
        # GeckoLib 4 rejects the whole animation. Use the neutral channel value.
        if value=='-':return 0
        if isinstance(value,dict):return {k:repair(v) for k,v in value.items()}
        if isinstance(value,list):return [repair(v) for v in value]
        return value
    result=normalize(repair({'format_version':'1.8.0','animations':clips}))
    validate_animation_channels(result,'import')
    return result,mapping,profile

def resolve(pack,name,kind):
    folder,namespace,package=SOURCES[pack]
    root=ROOT.parent/'deobfuscated'/folder/'src/main';java=root/'java'/package;assets=root/'resources/assets'/namespace
    ep=find(java,name+'Entity');entity=ep.read_text();renderer=find(java,name+'Renderer') if name!='DemonKingMinion' else None
    render=renderer.read_text() if renderer else ''
    model_names=re.findall(r'import [\w.]+\.((?:Model\w+|\w+Model));',render)
    if not model_names:model_names=re.findall(r'new (\w+Model)\(',render)
    if name=='DemonKingMinion':model_names=['DemonKingMinionModel']
    assert model_names,(pack,name,'model class not resolved')
    vanilla=model_names[0] in ('HumanoidModel','PlayerModel')
    model=renderer if vanilla else find(java,model_names[0]);model_text='' if vanilla else model.read_text();paths=resource_strings(model_text)
    source_geo=next((p for p in paths if p.startswith('geo/')),None)
    source_anim=next((p for p in paths if p.startswith('animations/')),None)
    texture=next((p for p in paths if p.startswith('textures/')),None)
    entity_id=snake(name);registry_id=None
    for f in (java/'init').glob('*Entities.java'):
        t=f.read_text();m=re.search(r'"([a-z0-9_]+)"\s*,\s*(?:EntityType\.)?Builder.of\('+name+r'Entity::new',t)
        if m:registry_id=m[1]
    entity_id=registry_id or entity_id
    id=pack+'_'+entity_id
    geo=read(assets/source_geo) if source_geo else humanoid(id) if vanilla else geometry(model_text,id)
    if pack=='dawn' and name=='Spiritnick':
        # This NPC's body is its equipped custom armor; its skin is transparent.
        parts=[geometry(find(java,'Modelspiritnick_'+part).read_text(),id) for part in ('head','chest','leg')]
        geo=parts[0]
        geo['minecraft:geometry'][0]['bones']=sum((p['minecraft:geometry'][0]['bones'] for p in parts),[])
        texture='textures/entities/nick_wins.png'
    if pack=='dawn' and name=='GiantSkele':texture='textures/giant_skele.png'
    for bone in geo['minecraft:geometry'][0]['bones']:
        for cube in bone.get('cubes',[]):
            for axis,size in enumerate(cube['size']):
                if size<0:cube['origin'][axis]+=size;cube['size'][axis]=-size
    clips=read(assets/source_anim)['animations'] if source_anim else {}
    if not source_anim:
        animation_names=re.findall(r'import [\w.]+\.animations\.(\w+);',render)
        for animation_name in animation_names:clips.update(animations(find(java,animation_name).read_text()))
    if not texture:
        paths=resource_strings(render)
        texture=next((p for p in paths if p.startswith('textures/')),None)
        if not texture:
            fixed=re.findall(r'"'+namespace+r':(textures/[^"+]+\.png)"',render)
            # Prefer the actual default skin; some renderers declare overlays first.
            default=re.search(r'\.define\(TEXTURE,\s*"([^"]+)"',entity)
            if default and default[1]!='blank':texture='textures/entities/'+default[1]+'.png'
            elif fixed:texture=fixed[0]
            elif default:texture='textures/entities/'+default[1]+'.png'
    assert texture and (assets/texture).exists(),(pack,name,'texture',texture)
    assert sum(len(b.get('cubes',[])) for b in geo['minecraft:geometry'][0]['bones'])>0,(name,'empty model')
    source_count=len(clips)
    anim,mapping,profile=canonical(clips,geo,kind=='BOSS')
    lo,hi=bounds(geo,anim['animations']['idle']);model_height=(hi[1]-lo[1])/16
    assert model_height>0,(name,'no model height')
    height=min(4.5,max(1.0,model_height)) if kind=='BOSS' else min(2.5,max(0.5,model_height))
    scale=height/model_height;lift=-lo[1]/16*scale
    width=min(3.0 if kind=='BOSS' else 1.7,max(0.5,(hi[0]-lo[0])/16*scale*0.6))
    signature=hashlib.sha256(json.dumps([geo,anim],sort_keys=True).encode()).hexdigest()
    calibration_file=ROOT/'tools/java_render_calibration.json'
    calibration=read(calibration_file).get(id) if calibration_file.exists() else None
    if calibration and calibration['signature']==signature:
        b=calibration['bounds'];scale=height/(b[4]-b[1]);lift=-b[1]*scale
        width=min(3.0 if kind=='BOSS' else 1.7,max(0.5,(b[3]-b[0])*scale*0.6))
    lang=read(assets/'lang/en_us.json');title=lang.get('entity.'+namespace+'.'+entity_id,re.sub(r'(?<=[a-z])(?=[A-Z])',' ',name))
    title=re.sub(r'\s*Player\b','',title).strip()
    if pack=='dawn' and name=='Gryphon':title='DawnCraft Gryphon'
    elif name=='Herobrine':title=('DawnCraft ' if pack=='dawn' else "Mo’Bosses ")+title
    info=dict(id=id,title=title,pack=pack,source_entity=namespace+':'+entity_id,source_java=str(ep.relative_to(root)),
        model=str(model.relative_to(root)),texture=texture,animation=source_anim or 'Java keyframes',kind=kind,
        scale=round(scale,6),lift=round(lift,6),width=round(width,3),height=round(height,3),style=style(name),
        animations=mapping,source_animation_count=source_count,dragon='Dragon' in name,
        ranged=any(t in name for t in ['Mage','Shaman','Range','Spirit','Orb','Harpy']),
        source_health=float((re.search(r'Attributes.MAX_HEALTH,\s*([\d.]+)',entity) or [None,'40'])[1]),asset_signature=signature)
    return info,geo,anim,profile,assets/texture

def main():
    selection=read(ROOT/'tools/java_creature_selection.json');resolved=[];failures=[]
    for pack,kinds in selection.items():
        for kind,names in kinds.items():
            for name in names.split():
                if pack=='dawn' and name in ('Protector','Sprout'):continue # Invisible spell/particle controllers.
                try:resolved.append(resolve(pack,name,kind.upper()))
                except Exception as e:failures.append((pack,name,str(e)))
    dump(ROOT/'verification/java-import-resolution.json',{'resolved':[r[0] for r in resolved],'failures':failures})
    print('Resolved',len(resolved),'creatures;',len(failures),'failures')
    for f in failures:print(*f)
    if failures:raise SystemExit(1)
    if '--audit' in sys.argv:return
    write_runtime(resolved)

def write_runtime(resolved):
    backup=ROOT/'verification/pre-java-import';backup.mkdir(exist_ok=True)
    java=ROOT/'src/main/java/dev/deepnight/Species.java'
    old=ROOT/'verification/java-import-catalogue.json'
    if old.exists():
        keep={r[0]['id'] for r in resolved}
        for entry in read(old):
            id=entry['id']
            if id in keep:continue
            for relative in [f'assets/deepnight/geo/{id}.geo.json',f'assets/deepnight/animations/{id}.animation.json',f'assets/deepnight/textures/entity/{id}.png',f'assets/deepnight/models/item/{id}_spawn_egg.json',f'data/deepnight/forge/biome_modifier/{id}.json']:
                target=(RES/relative).resolve();assert target.is_relative_to(RES.resolve());target.unlink(missing_ok=True)
    for path in [java,A/'lang/en_us.json',A/'sounds.json',D/'creature_sounds.json',D/'boss_attacks.json']:
        target=backup/path.name
        if not target.exists():shutil.copy2(path,target)
    # Always start from the pre-import files, making repeated runs idempotent.
    lang=read(backup/'en_us.json');sounds=read(backup/'sounds.json');mapping=read(backup/'creature_sounds.json');profiles=read(backup/'boss_attacks.json')
    for pack,(folder,namespace,package) in SOURCES.items():
        assets=ROOT.parent/'deobfuscated'/folder/'src/main/resources/assets'/namespace
        source_events=read(assets/'sounds.json') if (assets/'sounds.json').exists() else {}
        for key,value in source_events.items():
            v=copy.deepcopy(value)
            for i,sound in enumerate(v.get('sounds',[])):
                name=sound if isinstance(sound,str) else sound['name']
                ns,path=name.split(':',1) if ':' in name else (namespace,name)
                if ns==namespace:
                    newname='deepnight:imports/'+pack+'/'+path
                    if isinstance(sound,str):v['sounds'][i]=newname
                    else:sound['name']=newname
            sounds[pack+'.'+key]=v
        for source in (assets/'sounds').rglob('*.ogg'):
            target=A/'sounds/imports'/pack/source.relative_to(assets/'sounds');target.parent.mkdir(parents=True,exist_ok=True);shutil.copy2(source,target)
    rows=[];boss_stage=41;catalogue=[]
    for info,geo,anim,profile,texture in resolved:
        id=info['id'];kind=info['kind'];is_boss=kind=='BOSS';is_pet=kind=='PET'
        health=360+boss_stage*80 if is_boss else min(160,max(45,info['source_health']*0.6)) if is_pet else min(120,max(20,info['source_health']*0.4))
        damage=round(12+boss_stage*1.6,1) if is_boss else 8 if is_pet else 7
        armor=30 if is_boss else 5
        stats=[health,damage,armor,0.29,info['width'],info['height'],info['scale'],info['lift'],info['style']]
        info.update(health=health,damage=damage,boss_stage=boss_stage if is_boss else None)
        if is_boss:
            boss_stage+=1;profiles[id]=profile
            dump(A/f'models/item/{id}_sigil.json',{'parent':'minecraft:item/generated','textures':{'layer0':'deepnight:item/boss_sigil'}})
            dump(D/f'recipes/{id}_essence.json',{'type':'minecraft:crafting_shapeless','ingredients':[{'item':f'deepnight:{id}_sigil'}],'result':{'item':'deepnight:night_essence','count':16}})
            lang['item.deepnight.'+id+'_sigil']=info['title']+' Sigil'
        else:
            dump(A/f'models/item/{id}_spawn_egg.json',{'parent':'minecraft:item/template_spawn_egg'})
            lang['item.deepnight.'+id+'_spawn_egg']=info['title']+' Spawn Egg'
        if kind in ('MOB','PASSIVE'):
            dump(D/f'forge/biome_modifier/{id}.json',{'type':'forge:add_spawns','biomes':'#minecraft:is_overworld','spawners':{'type':'deepnight:'+id,'weight':1,'minCount':1,'maxCount':1}})
        dump(A/f'geo/{id}.geo.json',geo);dump(A/f'animations/{id}.animation.json',anim)
        shutil.copy2(texture,A/f'textures/entity/{id}.png')
        lang['entity.deepnight.'+id]=info['title']
        java_values=', '.join(str(v)+('f' if 4<=i<=7 else '') for i,v in enumerate(stats))
        rows.append('    '+id.upper()+'('+json.dumps(info['title'],ensure_ascii=False)+', Kind.'+kind+', '+java_values+', false, '+str(info['ranged']).lower()+')')
        # Map named source creature sound methods, never unrelated UI sounds.
        folder,namespace,package=SOURCES[info['pack']]
        entity=(ROOT.parent/'deobfuscated'/folder/'src/main'/info['source_java']).read_text()
        fields={}
        if info['pack']=='mobosses':
            registration=(ROOT.parent/'deobfuscated'/folder/'src/main/java'/package/'MoBosses.java').read_text()
            fields=dict(re.findall(r'(\w+)\s*=\s*SOUND_EVENTS.register\(\s*"([^"]+)"',registration))
        roles={}
        for method,role in [('getAmbientSound','ambient'),('getHurtSound','hurt'),('getDeathSound','death')]:
            block=re.search(method+r'\([^)]*\)\s*\{(.*?)\n\s*\}',entity,re.S)
            if block:
                match=re.search('"'+namespace+':([^" ]+)"',block[1])
                if match and info['pack']+'.'+match[1] in sounds:roles[role]=info['pack']+'.'+match[1]
                field=re.search(r'MoBosses\.(\w+)\.get\(\)',block[1])
                if field and field[1] in fields:roles[role]='mobosses.'+fields[field[1]]
        for field in re.findall(r'MoBosses\.(\w+)\.get\(\)',entity):
            if field in fields and 'MUSIC' in field:roles['music']='mobosses.'+fields[field]
            if field in fields and 'SWING' in field:roles['attack']='mobosses.'+fields[field]
        if roles:mapping[id]=roles
        info['texture_sha256']=hashlib.sha256(texture.read_bytes()).hexdigest();catalogue.append(info)
    source=(backup/'Species.java').read_text()
    marker=';\n    public enum Kind'
    assert marker in source
    source=source.replace(marker,',\n'+',\n'.join(rows)+marker)
    source=source.replace('public static final int BOSS_COUNT=41;','private static final java.util.List<Species> BOSSES=java.util.Arrays.stream(values()).filter(Species::boss).toList();\n    public static final int BOSS_COUNT=BOSSES.size();\n    public int bossStage(){return BOSSES.indexOf(this);}')
    source=source.replace('return values()[stage];','return BOSSES.get(stage);')
    source=source.replace('this==DUSK_DRAKE;','this==DUSK_DRAKE||kind==Kind.PET&&id.startsWith("aod_")&&id.contains("dragon");')
    source=source.replace('    public boolean passive()', '''    public String breathElement(){
        if(this==EMBER_DRAKE)return "fire";
        if(this==TIDE_DRAKE)return "water";
        if(id.contains("electrica")||id.contains("electrode"))return "lightning";
        if(id.contains("frostlok"))return "frost";
        if(id.contains("fire"))return "fire";
        if(id.contains("water"))return "water";
        if(id.contains("earth"))return "earth";
        if(id.contains("wind"))return "wind";
        return "spirit";
    }
    public boolean passive()''')
    java.write_text(apply_roster(apply_java(source)),encoding='utf-8')
    dump(A/'lang/en_us.json',lang);dump(A/'sounds.json',sounds);dump(D/'creature_sounds.json',mapping);dump(D/'boss_attacks.json',profiles)
    variables=set()
    for animation_file in (A/'animations').glob('*.json'):
        variables.update(re.findall(r'\b(?:q|query|v|variable)\.[a-zA-Z0-9_]+',animation_file.read_text()))
    dump(D/'molang_variables.json',sorted(variables))
    dump(ROOT/'verification/java-import-catalogue.json',catalogue)
    clean_resources(ROOT)
    print('Wrote',len(catalogue),'creatures;',boss_stage,'total campaign bosses;',dict(collections.Counter(r['kind'] for r in catalogue)))
if __name__=='__main__':main()
