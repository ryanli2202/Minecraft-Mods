"""Check every imported registry/resource, animation, texture and sound reference."""
from pathlib import Path
import json,re,struct,collections
from PIL import Image
from animation_format import validate_animation_channels
R=Path(__file__).resolve().parents[1];A=R/'src/main/resources/assets/deepnight';D=R/'src/main/resources/data/deepnight'
def read(p):return json.loads(p.read_text(encoding='utf-8'))
rows=read(R/'verification/java-import-catalogue.json');errors=[];channels=0
def check(ok,reason):
    if not ok:errors.append(reason)
profiles=read(D/'boss_attacks.json');java=(R/'src/main/java/dev/deepnight/Species.java').read_text()
for r in rows:
    id=r['id'];g=read(A/f'geo/{id}.geo.json')['minecraft:geometry'][0];a=read(A/f'animations/{id}.animation.json')
    check(id.upper()+'(' in java,'Unregistered '+id)
    names=[b['name'] for b in g['bones']]
    check(len(names)==len(set(names)),'Duplicate bones '+id)
    check(all(b.get('parent',names[0]) in names for b in g['bones']),'Orphan bones '+id)
    for b in g['bones']:
        check(all(all(v>=0 for v in c['size']) for c in b.get('cubes',[])),'Negative cube '+id)
    channels+=validate_animation_channels(a,id)
    check(set(a['animations'])>={'idle','walk','attack','death','hurt','sit','fly','spawn'},'Missing runtime animation '+id)
    img=Image.open(A/f'textures/entity/{id}.png').convert('RGBA');desc=g['description']
    check(img.getchannel('A').getbbox() is not None,'Invisible texture '+id)
    check(abs(img.width/desc['texture_width']-img.height/desc['texture_height'])<0.0001,'UV aspect mismatch '+id+': '+str(img.size)+' '+str((desc['texture_width'],desc['texture_height'])))
    biome=D/f'forge/biome_modifier/{id}.json'
    check(biome.exists()==(r['kind'] in ['MOB','PASSIVE']),'Wrong spawn category '+id)
    if r['kind']=='BOSS':
        clip=a['animations']['attack'];p=profiles[id]
        check(clip['loop'] is False,'Looping boss strike '+id)
        check(abs(p['durationTicks']-clip['animation_length']*20)<0.001,'Duration mismatch '+id)
        check([float(t)*20 for t in clip['timeline']]==p['impactTicks'],'Contact mismatch '+id)
        check(all(0<t<p['durationTicks'] for t in p['impactTicks']),'Invalid contacts '+id)
        check(set(clip['bones']).issubset(set(names)),'Missing attack bones '+id+': '+str(set(clip['bones'])-set(names)))
sounddefs=read(A/'sounds.json')
for event,data in sounddefs.items():
    if not event.startswith(('aod.','dawn.','mobosses.')):continue
    for sound in data['sounds']:
        s={'name':sound} if isinstance(sound,str) else sound
        ns,path=s['name'].split(':',1) if ':' in s['name'] else ('deepnight',s['name'])
        if ns!='deepnight':continue
        check(path in sounddefs if s.get('type')=='event' else (A/'sounds'/(path+'.ogg')).exists(),'Missing sound '+event+': '+path)
report={'creatures':len(rows),'by_source':dict(collections.Counter(r['pack'] for r in rows)),'by_role':dict(collections.Counter(r['kind'] for r in rows)),'animation_channels':channels,'errors':errors}
(R/'verification/java-import-resource-check.json').write_text(json.dumps(report,indent=2)+'\n')
print(json.dumps(report,indent=2))
raise SystemExit(bool(errors))
