"""The campaign retains only source bosses that do not require Epic Fight."""
import re
import json

def retained(species_id):
    return species_id.startswith(('aod_', 'mobosses_'))

def apply_roster(source):
    return re.sub(r'^    ([A-Z_0-9]+)\("[^\n]+Kind\.BOSS[^\n]+\n',lambda m:m[0] if retained(m[1].lower()) else '',source,flags=re.M)

def clean_resources(root):
    """Keep recipes and generated data aligned with the actual registered roster."""
    source=(root/'src/main/java/dev/deepnight/Species.java').read_text()
    all_ids={m[1].lower() for m in re.finditer(r'^    ([A-Z_0-9]+)\("',source,re.M)}
    bosses=[m[1].lower() for m in re.finditer(r'^    ([A-Z_0-9]+)\("[^\n]+Kind\.BOSS',source,re.M)]
    assets=root/'src/main/resources/assets/deepnight'
    data=root/'src/main/resources/data/deepnight'
    def write(p,v):p.write_text(json.dumps(v,indent=2,ensure_ascii=False)+'\n',encoding='utf-8')
    removed=[]
    for p in (assets/'models/item').glob('*_sigil.json'):
        species=p.name.removesuffix('_sigil.json')
        if species in bosses:continue
        removed.append(species);p.unlink()
        for path in [data/f'recipes/{species}_essence.json',assets/f'geo/{species}.geo.json',assets/f'animations/{species}.animation.json',assets/f'textures/entity/{species}.png',data/f'forge/biome_modifier/{species}.json']:
            path.unlink(missing_ok=True)
    for name in ['boss_attacks.json','creature_sounds.json']:
        p=data/name;v=json.loads(p.read_text());write(p,{k:x for k,x in v.items() if k in all_ids})
    p=assets/'lang/en_us.json';v=json.loads(p.read_text())
    for species in removed:
        v.pop('entity.deepnight.'+species,None);v.pop('item.deepnight.'+species+'_sigil',None)
    write(p,v)
    weapons=(root/'src/main/java/dev/deepnight/Weapon.java').read_text()
    for name,rank in re.findall(r'([A-Z][A-Z0-9_]*)\((\d+),',weapons):
        p=data/f'recipes/{name.lower()}.json';v=json.loads(p.read_text())
        # The final weapon tier stays gated behind the final retained boss.
        boss=bosses[-1] if int(rank)==9 else bosses[int(rank)]
        v['key']['S']['item']='deepnight:'+boss+'_sigil';write(p,v)
    (root/'verification/retained-boss-roster.json').write_text(json.dumps({'bosses':bosses,'species_count':len(all_ids),'removed':removed},indent=2)+'\n')
    print('Retained',len(bosses),'bosses;',len(all_ids),'creatures; removed',len(removed),'boss resource sets')
