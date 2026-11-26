from pathlib import Path
import hashlib,json,re,shutil,zipfile
root=Path(__file__).resolve().parents[1];version='1.4.1'
log=(root/'demon-effects-1.4.1-tests.log').read_text();assert 'BUILD SUCCESSFUL' in log
passed=re.search(r'All (\d+) required tests passed',log);assert passed and int(passed[1])==49
jar=root/f'build/libs/deep-night-forge-1.20.1-{version}.jar';dist=root/'dist'
with zipfile.ZipFile(jar) as z:
 assert 'version="1.4.1"' in z.read('META-INF/mods.toml').decode()
 assert not any('/qa/' in n for n in z.namelist())
 assert 'dev/deepnight/source/mobosses/entity/custom/DemonKingEntity$1.class' in z.namelist()
shutil.copy2(jar,dist/jar.name)
with zipfile.ZipFile(dist/f'Deep-Night-{version}-Forge-1.20.1-install.zip','w',zipfile.ZIP_DEFLATED) as z:
 z.write(jar,'mods/'+jar.name);gecko=root/'compat-mods/geckolib-forge-1.20.1-4.8.4.jar';z.write(gecko,'mods/'+gecko.name)
 z.write(dist/'README-1.4.1.txt','README.txt');z.write(root/'ASSET-NOTES.txt','ASSET-NOTES.txt')
with zipfile.ZipFile(dist/f'deep-night-forge-1.20.1-{version}-source.zip','w',zipfile.ZIP_DEFLATED) as z:
 for folder in ['src','gradle','compat-mods']:
  for p in sorted((root/folder).rglob('*')):
   if p.is_file():z.write(p,p.relative_to(root))
 for file in ['README.txt','ASSET-NOTES.txt','gradlew','gradlew.bat','build.gradle','gradle.properties','settings.gradle','verification/roster-1.4.0.json','demon-effects-1.4.1-tests.log','tools/package_demon_effects_release.py']:
  z.write(root/file,file)
report={'version':version,'required_server_tests_passed':int(passed[1]),'interactive_audio_visual_tested':False,'full_modpack_tested':False,'artifacts':[]}
for p in [dist/jar.name,dist/f'Deep-Night-{version}-Forge-1.20.1-install.zip',dist/f'deep-night-forge-1.20.1-{version}-source.zip']:
 report['artifacts'].append({'name':p.name,'bytes':p.stat().st_size,'sha256':hashlib.sha256(p.read_bytes()).hexdigest()})
(dist/f'Deep-Night-{version}-verification.json').write_text(json.dumps(report,indent=2)+'\n');print(json.dumps(report,indent=2))
