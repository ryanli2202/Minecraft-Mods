import pathlib,json,collections
BASE=pathlib.Path(__file__).resolve().parents[2]/'the deep night/mobs'
def read(p):
    try:return json.loads(p.read_text(encoding='utf-8-sig'))
    except:return {}
for pack in ['world','souls','aerlunerpg']:
    b=BASE/pack;lang=read(b/'lang/en_us.json')
    print('\n===',pack,'===')
    print('ENTITY LABELS:',{k.split('.')[-1]:v for k,v in lang.items() if k.startswith('entity.')})
    print('MODELS:',[p.name.split('.')[0] for p in (b/'geo').glob('*.json')])
    print('SOUNDS:',list(read(b/'sounds.json')))
for pack in ['bosses+','boxxexpansion/resource_packs/rp0']:
    b=BASE/pack;print('\n===',pack,'===')
    print('MODELS:',[str(p.relative_to(b)) for p in b.rglob('*.json') if '.geo.' in p.name])
    if 'boxx' in pack:
        for p in (b/'entity').rglob('*.json'):
            d=read(p).get('minecraft:client_entity',{}).get('description',{})
            if d:print('CLIENT',str(p.relative_to(b)),d.get('identifier'),d.get('geometry'),d.get('textures'),list(d.get('animations',{})),d.get('sound_effects',{}))
    else:print('ANIMATIONS:',[p.name for p in (b/'animations').rglob('*.json')])
    sounds=[p for p in b.rglob('*.ogg')];print('SOUND TOTAL',len(sounds),round(sum(p.stat().st_size for p in sounds)/1024/1024,2),'MiB')
