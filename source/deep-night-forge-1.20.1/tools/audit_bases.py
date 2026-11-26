import json, pathlib, collections
root=pathlib.Path(__file__).resolve().parents[2]/'the deep night'
for pack in ['world','souls','aerlunerpg']:
    base=root/'mobs'/pack
    print('\nPACK',pack)
    for geo in (base/'geo').glob('*.json'):
        name=geo.name.split('.')[0]
        if name not in ['blood_dragon1','water_dragon1','flydragon','unicorn','giant','cyclops1','entb','vampire','goblin','goblin_shaman','ghoul','ghost_guardian','asylumdemon','blackknight','gargoyle','serpentman','skeletonsoldier','undead_assassin','mimic','cthulhu','boarboss','boar','deerbig','creator_void','air_elemental','golem1','ent','shoggot_small','ghost']: continue
        try:
            g=json.loads(geo.read_text()); a=json.loads((base/'animations'/f'{name}.animation.json').read_text())
            tex=[str(p.relative_to(base)) for p in (base/'textures').rglob(f'{name}*.png')]
            print(name, 'textures=',tex, 'clips=',list(a['animations']), 'bones=',len(g['minecraft:geometry'][0]['bones']))
        except Exception as e: print(name,str(e))
for pack in root.iterdir():
    files=list(pack.rglob('*')); files=[p for p in files if p.is_file()]
    print(pack.name, 'files=',len(files),'placeholder/empty=',sum(p.stat().st_size<3 for p in files))
