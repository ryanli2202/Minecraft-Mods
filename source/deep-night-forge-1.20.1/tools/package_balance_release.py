from pathlib import Path
import hashlib,json,re,shutil,zipfile

root=Path(__file__).resolve().parents[1]
version='1.3.4'
log=(root/'balance-1.3.4-final.log').read_text()
assert 'BUILD SUCCESSFUL' in log
passed=re.search(r'All (\d+) required tests passed',log)
assert passed
stats=json.loads((root/'verification/boss-balance-1.3.4.json').read_text())
assert len(stats)==22 and all(row['health']>=row['old_health']*3 for row in stats)
assert all(a['health']<b['health'] for a,b in zip(stats,stats[1:]))
jar=root/f'build/libs/deep-night-forge-1.20.1-{version}.jar'
with zipfile.ZipFile(jar) as z:
    assert 'dev/deepnight/BossBalance.class' in z.namelist()
    assert not any('/qa/' in n for n in z.namelist())
    assert 'version="1.3.4"' in z.read('META-INF/mods.toml').decode()
    lang=json.loads(z.read('assets/deepnight/lang/en_us.json'))
    for row in stats[-3:]:assert lang['entity.deepnight.'+row['id']]==row['name']
    for name,rank in re.findall(r'([A-Z][A-Z0-9_]*)\((\d+),',(root/'src/main/java/dev/deepnight/Weapon.java').read_text()):
        expected='deepnight:'+stats[-1 if int(rank)==9 else int(rank)]['id']+'_sigil'
        recipe=json.loads(z.read(f'data/deepnight/recipes/{name.lower()}.json'))
        assert recipe['key']['S']['item']==expected
dist=root/'dist';shutil.copy2(jar,dist/jar.name)
docs=['README.txt','ASSET-NOTES.txt','IMPORTED-CREATURES.txt']
with zipfile.ZipFile(dist/f'Deep-Night-{version}-Forge-1.20.1-install.zip','w',zipfile.ZIP_DEFLATED) as z:
    z.write(jar,'mods/'+jar.name)
    gecko=root/'compat-mods/geckolib-forge-1.20.1-4.8.4.jar';z.write(gecko,'mods/'+gecko.name)
    for name in docs:z.write(root/name,name)
    z.write(root/'verification/boss-balance-1.3.4.json','boss-balance.json')
with zipfile.ZipFile(dist/f'deep-night-forge-1.20.1-{version}-source.zip','w',zipfile.ZIP_DEFLATED) as z:
    for folder in ['src','gradle','compat-mods']:
        for path in sorted((root/folder).rglob('*')):
            if path.is_file():z.write(path,path.relative_to(root))
    for name in docs+['gradlew','gradlew.bat','build.gradle','gradle.properties','settings.gradle','tools/package_balance_release.py','verification/boss-balance-1.3.4.json','balance-1.3.4-final.log']:
        z.write(root/name,name)
report={'version':version,'required_server_tests_passed':int(passed[1]),'interactive_client_tested':False,'bosses':stats,'artifacts':[]}
for p in [dist/jar.name,dist/f'Deep-Night-{version}-Forge-1.20.1-install.zip',dist/f'deep-night-forge-1.20.1-{version}-source.zip']:
    report['artifacts'].append({'name':p.name,'bytes':p.stat().st_size,'sha256':hashlib.sha256(p.read_bytes()).hexdigest()})
(dist/f'Deep-Night-{version}-verification.json').write_text(json.dumps(report,indent=2)+'\n')
print(json.dumps(report['artifacts'],indent=2))
