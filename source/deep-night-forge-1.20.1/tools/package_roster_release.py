from pathlib import Path
import hashlib,json,re,shutil,zipfile
root=Path(__file__).resolve().parents[1];version='1.4.0'
log=(root/'roster-1.4.0-tests.log').read_text();assert 'BUILD SUCCESSFUL' in log
passed=re.search(r'All (\d+) required tests passed',log);assert passed
client=(root/'verification/source-client-report.txt').read_text();assert client.startswith('PASS\n')
for id in ['dawn_fire_dragon','dawn_ice_dragon','dawn_lightning_dragon','dawn_elemental_deity']:assert 'Loaded deepnight:'+id+' /' in client
roster=json.loads((root/'verification/roster-1.4.0.json').read_text());assert [line.split(' /')[0].removeprefix('Loaded deepnight:') for line in client.splitlines()[1:11]]==[s.lower() for s in roster['bosses']]
for id in roster['pets']:assert 'Loaded deepnight:'+id.lower()+' / 6 source clips' in client
jar=root/f'build/libs/deep-night-forge-1.20.1-{version}.jar'
with zipfile.ZipFile(jar) as z:
 assert 'version="1.4.0"' in z.read('META-INF/mods.toml').decode()
 assert not any('/qa/' in n for n in z.namelist())
 for name,rank in re.findall(r'([A-Z][A-Z0-9_]*)\((\d+),',(root/'src/main/java/dev/deepnight/Weapon.java').read_text()):
  recipe=json.loads(z.read(f'data/deepnight/recipes/{name.lower()}.json'));assert recipe['key']['S']['item']=='deepnight:'+roster['bosses'][int(rank)].lower()+'_sigil'
 for id in roster['pets']:
  for sub,suffix in [('geo','.geo.json'),('animations','.animation.json'),('textures/entity','.png')]:assert f'assets/deepnight/{sub}/{id.lower()}{suffix}' in z.namelist()
 for id in roster['removed']+roster['pets']+roster['summoned_helpers']:assert f'data/deepnight/recipes/{id.lower()}_essence.json' not in z.namelist()
dist=root/'dist';shutil.copy2(jar,dist/jar.name)
with zipfile.ZipFile(dist/f'Deep-Night-{version}-Forge-1.20.1-install.zip','w',zipfile.ZIP_DEFLATED) as z:
 z.write(jar,'mods/'+jar.name);gecko=root/'compat-mods/geckolib-forge-1.20.1-4.8.4.jar';z.write(gecko,'mods/'+gecko.name)
 z.write(dist/'README-1.4.0.txt','README.txt');z.write(root/'ASSET-NOTES.txt','ASSET-NOTES.txt')
with zipfile.ZipFile(dist/f'deep-night-forge-1.20.1-{version}-source.zip','w',zipfile.ZIP_DEFLATED) as z:
 for folder in ['src','gradle','compat-mods']:
  for p in sorted((root/folder).rglob('*')):
   if p.is_file():z.write(p,p.relative_to(root))
 for file in ['README.txt','ASSET-NOTES.txt','gradlew','gradlew.bat','build.gradle','gradle.properties','settings.gradle','verification/roster-1.4.0.json','verification/source-client-report.txt','roster-1.4.0-tests.log','tools/package_roster_release.py']:
  z.write(root/file,file)
report={'version':version,'required_server_tests_passed':int(passed[1]),'automated_client_render_tested':True,'full_modpack_tested':False,'roster':roster,'artifacts':[]}
for p in [dist/jar.name,dist/f'Deep-Night-{version}-Forge-1.20.1-install.zip',dist/f'deep-night-forge-1.20.1-{version}-source.zip']:
 report['artifacts'].append({'name':p.name,'bytes':p.stat().st_size,'sha256':hashlib.sha256(p.read_bytes()).hexdigest()})
(dist/f'Deep-Night-{version}-verification.json').write_text(json.dumps(report,indent=2)+'\n');print(json.dumps(report,indent=2))
