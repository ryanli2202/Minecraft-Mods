from pathlib import Path
import hashlib,json,re,shutil,zipfile
root=Path(__file__).resolve().parents[1]
dist=root/'dist';dist.mkdir(exist_ok=True)
version=re.search(r"version = '([^']+)'",(root/'build.gradle').read_text()).group(1)
jar=root/f'build/libs/the-deep-night-guide-forge-1.20.1-{version}.jar'
if not jar.exists():raise SystemExit('Build the release JAR first')
with zipfile.ZipFile(jar) as z:
    assert not any(n.startswith('dev/deepnightguide/qa/') for n in z.namelist()),'QA classes leaked into release'
    assert 'META-INF/mods.toml' in z.namelist()
    assert 'dev/deepnightguide/compat/GuideJeiPlugin.class' in z.namelist()
    assert len(json.loads(z.read('assets/deepnightguide/custom_entries.json')))>=228
    lore=json.loads(z.read('assets/deepnightguide/lore.json'))
    assert len(lore['chapters'])==8 and len(lore['dimensions'])==17
    assert len(json.loads(z.read('assets/deepnightguide/boss_entries.json')))>=32
    deepnight=json.loads(z.read('assets/deepnightguide/deepnight_entries.json'))
    assert len(deepnight)==248
    assert sum(v.get('boss',False) for v in deepnight.values())==41
    assert sum(v.get('tameable',False) for v in deepnight.values())==31
    assert sum('WEAPONS' in v.get('categories',[]) for v in deepnight.values())==25
    assert not any(n.startswith(('mezz/','software/','net/minecraft/')) for n in z.namelist()),'Dependency classes bundled'
shutil.copy2(jar,dist/jar.name)
for name in ['README.txt','entries.example.json','lore.example.json']:shutil.copy2(root/name,dist/name)
with zipfile.ZipFile(dist/f'The-Deep-Night-Guide-{version}-Install.zip','w',zipfile.ZIP_DEFLATED) as z:
    z.write(jar,'mods/'+jar.name)
    for name in ['README.txt','entries.example.json','lore.example.json']:z.write(root/name,name)
with zipfile.ZipFile(dist/f'The-Deep-Night-Guide-{version}-Source.zip','w',zipfile.ZIP_DEFLATED) as z:
    for folder in ['src','gradle','tools']:
        for f in (root/folder).rglob('*'):
            if f.is_file() and '__pycache__' not in f.parts and f.name not in ['inspect_pack.py','inspect_lore_sources.py']:z.write(f,'deep-night-guide-forge-1.20.1/'+f.relative_to(root).as_posix())
    for name in ['build.gradle','settings.gradle','gradle.properties','gradlew','gradlew.bat','README.txt','entries.example.json','lore.example.json','.gitignore']:
        z.write(root/name,'deep-night-guide-forge-1.20.1/'+name)
    for name in ['custom-entry-coverage.json','client-smoke-test.txt','vanilla-smoke-test.txt','lore-content-coverage.json','deepnight-content-coverage.json','deepnight-smoke-test.txt']:
        if (root/'verification'/name).exists():z.write(root/'verification'/name,'deep-night-guide-forge-1.20.1/verification/'+name)
manifest={f.name:{'bytes':f.stat().st_size,'sha256':hashlib.sha256(f.read_bytes()).hexdigest()} for f in dist.iterdir() if f.suffix in {'.jar','.zip'}}
(dist/'SHA256.json').write_text(json.dumps(manifest,indent=2))
print(json.dumps(manifest,indent=2))
