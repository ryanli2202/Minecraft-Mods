"""Validate and package this mod only. No dependencies, saves or launcher files."""
import hashlib,json,pathlib,re,shutil,zipfile
root=pathlib.Path(__file__).resolve().parents[1];dist=root/'dist';dist.mkdir(exist_ok=True)
version=re.search(r"^version = '([^']+)'",(root/'build.gradle').read_text(),re.MULTILINE).group(1)
jar=root/f'build/libs/travellers-companions-1.20.1-{version}.jar'
characters=['varys','raeshel','gideon','castelan','mara','leonardo','yuriko','yushiro','footsquire','footpaladin','footarcher']
with zipfile.ZipFile(jar) as z:
    names=z.namelist()
    assert 'META-INF/mods.toml' in names
    assert len([n for n in names if n.startswith('assets/travellerscompanions/textures/entity/') and n.endswith('.png')])==len(characters)
    for skin in characters:
        name=f'assets/travellerscompanions/textures/entity/{skin}.png'
        assert z.read(name)==(root/'src/main/resources'/name).read_bytes(),f'Stale packaged skin: {skin}'
    skins=json.loads((root/'verification/skins.json').read_text())
    for skin in characters[1:]:
        assert hashlib.sha256(z.read(f'assets/travellerscompanions/textures/entity/{skin}.png')).hexdigest()==skins[f'{skin}.png']['source_sha256'],f'Skin differs from supplied original: {skin}'
    assert not any('/qa/' in n or 'structures/empty.nbt' in n for n in names)
    assert not any(n.startswith(('net/bettercombat/','dev/kosmx/','io/redspace/','net/mcreator/','software/bernie/','com/alrex/','net/mcvader/')) for n in names)
    for name in names:
        if name.endswith('.json') or name.endswith('.mcmeta'):json.loads(z.read(name))
shutil.copyfile(jar,dist/jar.name)
with zipfile.ZipFile(dist/'Travellers-Companions-Forge-1.20.1-install.zip','w',zipfile.ZIP_DEFLATED) as z:
    z.write(jar,'mods/'+jar.name)
    for name in ['parkour-client-smoke-test.txt','parkour-movement.png','parkour-test-summary.txt']:z.write(root/'verification'/name,'verification/'+name)
    for name in ['README.txt','ASSET-NOTES.txt']:z.write(root/name,name)
    for name in ['troops-archer-roster.png','troops-squire-roster.png','troop-client-smoke-test.txt','troops-active-roster.png','troops-roster-small.png','troop-orders.png','troop-equipment.png','client-smoke-test.txt','party-client-smoke-test.txt','test-summary.txt','skins.json','companions-idle.png','companions-specials.png','leonardo-spells.png','leonardo-spells-swapped.png','yushiro-special-live.png','yushiro-roster.png','eight-roster-small.png','interaction-wheel.png','party-two-roster.png','party-two-roster-small.png']:z.write(root/'verification'/name,'verification/'+name)
with zipfile.ZipFile(dist/'Travellers-Companions-Forge-1.20.1-source.zip','w',zipfile.ZIP_DEFLATED) as z:
    for p in sorted(root.rglob('*')):
        rel=p.relative_to(root)
        if not p.is_file() or any(s in {'.gradle','build','run','dist','libs','__pycache__','integration-research'} for s in rel.parts) or p.suffix=='.log':continue
        if p.name in {'cfr-0.152.jar','extend_companions.py','extend_roster_ui.py'}:continue
        z.write(p,str(rel))
hashes={p.name:hashlib.sha256(p.read_bytes()).hexdigest() for p in sorted(dist.iterdir()) if p.suffix in {'.jar','.zip'}}
(dist/'SHA256SUMS.txt').write_text(''.join(f'{h}  {n}\n' for n,h in hashes.items()))
print(json.dumps(hashes,indent=2))
