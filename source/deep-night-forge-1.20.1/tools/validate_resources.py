import pathlib,json,re,zipfile,struct
from animation_format import validate_animation_channels
ROOT=pathlib.Path(__file__).resolve().parents[1];RES=ROOT/'src/main/resources';A=RES/'assets/deepnight';D=RES/'data/deepnight'
catalogue=json.loads((ROOT/'verification/content-catalogue.json').read_text())
WEAPONS=catalogue['weapons'];CREATURES=catalogue['creatures'];ENCHANTS=catalogue['enchantments']
def verify(condition,message):
    if not condition:raise AssertionError(message)
checks=0;animation_channels=0
boss_profiles=json.loads((D/'boss_attacks.json').read_text())
for path in RES.rglob('*.json'):json.loads(path.read_text(encoding='utf-8'));checks+=1
bc=ROOT.parent/'fantasy-races-forge-1.20.1/compat-mods/bettercombat-forge-1.9.0+1.20.1.jar'
with zipfile.ZipFile(bc) as z:
    for item,rank,preset in WEAPONS:
        p=A/f'models/item/{item}.json';m=json.loads(p.read_text());verify(len(m.get('elements',[]))>0,'No weapon geometry '+item)
        verify('parent' not in m,'External parent in complete weapon model '+item)
        for texture in m['textures'].values():
            if not texture.startswith('#'):verify((A/('textures/'+texture.split(':')[1]+'.png')).is_file(),'Missing texture '+texture)
        for element in m['elements']:
            for k in ['from','to']:verify(all(-16<=v<=32 for v in element[k]),'Element out of bounds '+item)
            if 'rotation' in element:verify(element['rotation']['angle'] in [-45,-22.5,0,22.5,45],'Invalid rotation '+item)
        attrs=json.loads((D/f'weapon_attributes/{item}.json').read_text());parent=attrs['parent'].split(':')[-1]
        verify(f'data/bettercombat/weapon_attributes/{parent}.json' in z.namelist(),'Missing Better Combat parent '+parent)
        boss='the_deep_night' if rank==9 else CREATURES[rank]['id']
        recipe=json.loads((D/f'recipes/{item}.json').read_text());verify(recipe['key']['S']['item']==f'deepnight:{boss}_sigil','Wrong boss recipe '+item)
for creature in CREATURES:
    name=creature['id']
    g=json.loads((A/f'geo/{name}.geo.json').read_text());verify(bool(g['minecraft:geometry'][0]['bones']),'Missing bones '+name)
    anim=json.loads((A/f'animations/{name}.animation.json').read_text());verify(set(anim['animations'])>={'idle','walk','attack','death','hurt','sit','fly','spawn'},'Missing animation channels '+name)
    animation_channels+=validate_animation_channels(anim,name)
    if creature['kind']=='BOSS':
        profile=boss_profiles[name];clip=anim['animations']['attack']
        verify(clip.get('loop') is False,'Boss attack must play once: '+name)
        verify('q.anim_time' not in json.dumps(clip),'Unbound Bedrock time alias in boss attack: '+name)
        verify(clip['bones']!=anim['animations']['idle']['bones'],'Boss uses idle instead of attack: '+name)
        verify(abs(clip['animation_length']*20-profile['durationTicks'])<0.001,'Attack duration mismatch: '+name)
        markers=sorted(float(t)*20 for t in clip.get('timeline',{}))
        verify(markers==profile['impactTicks'],'Damage/animation marker mismatch: '+name)
        verify(all(0<t<profile['durationTicks'] for t in markers),'Contact outside animation: '+name)
        verify(set(clip['bones'])<={b['name'] for b in g['minecraft:geometry'][0]['bones']},'Attack references missing bones: '+name)
    png=(A/f'textures/entity/{name}.png').read_bytes();verify(png[:8]==b'\x89PNG\r\n\x1a\n','Invalid PNG '+name)
    w,h=struct.unpack('>II',png[16:24]);desc=g['minecraft:geometry'][0]['description']
    verify(w/desc['texture_width']==h/desc['texture_height'],f'Texture/UV aspect ratio mismatch {name}: {w}x{h} vs {desc}')
verify(not (D/'recipes/boss_summon_tool.json').exists(),'Tool must never be craftable')
verify(len(WEAPONS)==25 and len(ENCHANTS)==14,'Wrong content totals')
sounddefs=json.loads((A/'sounds.json').read_text());soundmap=json.loads((D/'creature_sounds.json').read_text())
for key,entry in sounddefs.items():
    for sound in entry['sounds']:
        sound={'name':sound} if isinstance(sound,str) else sound
        name=sound['name'].split(':')[-1]
        if sound.get('type')=='event':verify(name in sounddefs,'Missing sound event '+name)
        else:verify((A/f'sounds/{name}.ogg').is_file(),'Missing audio '+name)
for creature,roles in soundmap.items():
    for role,event in roles.items():verify(event in sounddefs,f'Missing {creature} {role} sound event')
audio=list((A/'sounds').rglob('*.ogg'))
for file in audio:verify(file.read_bytes()[:4]==b'OggS','Invalid OGG '+str(file))
verify(len(audio)==catalogue['sound_files'],'Not all source sounds copied')
for c in CREATURES:
    spawn=D/f'forge/biome_modifier/{c["id"]}.json'
    verify(spawn.exists()==(c['kind'] not in ['PET','BOSS']),'Incorrect natural spawn entry '+c['id'])
verify((D/'recipes/pet_summoning_tool.json').exists(),'Missing pet summoning recipe')
verify((A/'geo/ember_drake.geo.json').read_bytes()!=(A/'geo/eclipse_wyrm.geo.json').read_bytes(),'Ember and Eclipse share a mesh')
text=f'PASS: {checks} resource JSON files parsed; 25 weapon models, textures, moveset parents and gated recipes checked; {len(CREATURES)} creature models, eight animation channels each, texture dimensions and spawn policy checked; {len(audio)} OGG files and {len(sounddefs)} sound event references checked; 14 enchantments catalogued.\n'
text+=f'PASS: {animation_channels} bone animation channels validated, including all preserved source clips and eased constant/timed keyframes.\n'
text+=f'PASS: {len(boss_profiles)} boss attack timelines match one-shot clips and contact markers; no boss uses an idle fallback.\n'
(ROOT/'verification/resource-validation.txt').write_text(text)
print(text)
