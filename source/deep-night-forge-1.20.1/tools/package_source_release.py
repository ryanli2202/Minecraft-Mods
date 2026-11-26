"""Package the verified 1.2.0 native-source release without historical imports."""
from pathlib import Path
import hashlib,json,re,shutil,zipfile
r=Path(__file__).resolve().parents[1]
jar=r/'build/libs/deep-night-forge-1.20.1-1.2.0.jar'
species=(r/'src/main/java/dev/deepnight/Species.java').read_text()
bosses=re.findall(r'^    (\w+)\("[^\n]+Kind.BOSS',species,re.M)
assert len(bosses)==22
assert sum(x.startswith('DAWN_') for x in bosses)==15
assert sum(x.startswith('MOBOSSES_') for x in bosses)==7
server=(r/'source-revision-tests.log').read_text()
client=(r/'verification/source-client-report.txt').read_text()
clientlog=(r/'source-revision-client.log').read_text()
assert 'All 12 required tests passed' in server
assert client.startswith('PASS\n')
assert 'Unable to find animation' not in clientlog
assert 'Failed to parse animation' not in clientlog
with zipfile.ZipFile(jar) as z:
 names=z.namelist()
 assert not any('aod_' in n.lower() or '/aod/' in n.lower() or 'age_of_dragons' in n.lower() for n in names)
 assert not any('BossAttackProfile' in n or n.endswith('boss_attacks.json') or '/qa/' in n for n in names)
 assert not any(n.endswith('/'+x+'Entity.class') for n in names for x in ['Gael','Morgott','FirstTwin','SecondTwin','Knight4','Samurai4','Samurai5'])
 assert 'dev/deepnight/source/SourceStun.class' in names
 assert 'dev/deepnight/source/dawn/entity/LostEyeOgreEntity.class' in names
 for n in names:
  if n.endswith(('.json','.toml','.txt')):
   t=z.read(n).decode('utf-8',errors='replace')
   assert not re.search(r'age_of_dragons|aod_',t,re.I),n
dist=r/'dist';dist.mkdir(exist_ok=True)
copy=dist/jar.name;shutil.copy2(jar,copy)
gecko=r/'compat-mods/geckolib-forge-1.20.1-4.8.4.jar'
docs=['README.txt','ASSET-NOTES.txt','IMPORTED-CREATURES.txt']
install=dist/'Deep-Night-1.2.0-Forge-1.20.1-install.zip'
with zipfile.ZipFile(install,'w',zipfile.ZIP_DEFLATED) as z:
 z.write(copy,'mods/'+copy.name);z.write(gecko,'mods/'+gecko.name)
 for name in docs:z.write(r/name,name)
source=dist/'deep-night-forge-1.20.1-1.2.0-source.zip'
with zipfile.ZipFile(source,'w',zipfile.ZIP_DEFLATED) as z:
 for folder in ['src','gradle','compat-mods']:
  for p in sorted((r/folder).rglob('*')):
   if p.is_file():z.write(p,p.relative_to(r))
 for name in docs+['gradlew','gradlew.bat','build.gradle','gradle.properties','settings.gradle','.gitignore','tools/audit_source_animations.py','tools/package_source_release.py','source-revision-tests.log','source-revision-client.log','verification/source-client-report.txt','verification/dawn-animation-audit.json','verification/source-api-repairs.json','verification/source-command-remaps.json']:
  z.write(r/name,name)
report={'version':'1.2.0','bosses':bosses,'dawncraft_bosses':15,'mobosses_bosses':7,'server_tests':12,'client_result':client.splitlines()[-1],'artifacts':[]}
for p in [copy,install,source]:
 report['artifacts'].append({'name':p.name,'bytes':p.stat().st_size,'sha256':hashlib.sha256(p.read_bytes()).hexdigest()})
out=dist/'Deep-Night-1.2.0-verification.json';out.write_text(json.dumps(report,indent=2)+'\n')
print(json.dumps(report,indent=2))
