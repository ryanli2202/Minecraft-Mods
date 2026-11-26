from pathlib import Path
import re,json,zipfile,shutil
R=Path(__file__).resolve().parents[1]
backup=R/'verification/before-source-revision.zip'
if not backup.exists():
 with zipfile.ZipFile(backup,'w',zipfile.ZIP_DEFLATED) as z:
  for folder in ['src','tools']:
   for p in (R/folder).rglob('*'):
    if p.is_file():z.write(p,p.relative_to(R))
  for name in ['build.gradle','README.txt','ASSET-NOTES.txt','IMPORTED-CREATURES.txt']:z.write(R/name,name)
selection=json.loads((R/'tools/java_creature_selection.json').read_text())
audit=json.loads((R/'verification/dawn-animation-audit.json').read_text())
excluded={r['name'] for r in audit if r['epic_patch']}|{'Gael','Morgott'}
selection.pop('aod',None)
for kind,names in selection['dawn'].items():selection['dawn'][kind]=' '.join(n for n in names.split() if n not in excluded)
(R/'tools/java_creature_selection.json').write_text(json.dumps(selection,indent=2)+'\n')
def snake(s):return re.sub(r'(?<!^)(?=[A-Z][a-z]|(?<=[a-z0-9])[A-Z])','_',s).lower()
p=R/'src/main/java/dev/deepnight/Species.java';t=p.read_text()
with zipfile.ZipFile(R/'dist/deep-night-forge-1.20.1-1.1.1-source.zip') as z:
 old=z.read('src/main/java/dev/deepnight/Species.java').decode()
 dawn=[l for l in old.splitlines(True) if l.startswith('    DAWN_') and 'Kind.BOSS' in l and l.split('(')[0].strip()[5:].lower() not in {snake(n) for n in excluded}]
t=re.sub(r'^    AOD_[^\n]+\n','',t,flags=re.M)
t=re.sub(r'^    DAWN_[^\n]+Kind.BOSS[^\n]+\n','',t,flags=re.M)
t=t.replace('    DAWN_TINY_NECROMANCER', ''.join(dawn)+'    DAWN_TINY_NECROMANCER')
for n in excluded:t=re.sub(r'^    DAWN_'+snake(n).upper()+r'\([^\n]+\n','',t,flags=re.M)
t=t.replace('||kind==Kind.PET&&id.startsWith("aod_")&&id.contains("dragon")','')
p.write_text(t)
removed={m[1].lower() for m in re.finditer(r'^    ([A-Z0-9_]+)\("',old,re.M)}-{m[1].lower() for m in re.finditer(r'^    ([A-Z0-9_]+)\("',t,re.M)}
def remove(p):
 p=p.resolve();assert p.is_relative_to(R.resolve()) and p!=R.resolve()
 if p.is_dir():shutil.rmtree(p)
 elif p.exists():p.unlink()
remove(R/'src/main/java/dev/deepnight/source/aod')
remove(R/'src/main/java/dev/deepnight/client/NativeAoDRenderer.java')
for p in (R/'src/main/resources').rglob('*'):
 if p.is_file() and (any(x.startswith('aod_') or x=='age_of_dragons' for x in p.parts) or any(p.name==n+s for n in removed for s in ['.geo.json','.animation.json','.png','.json','_sigil.json','_spawn_egg.json','_essence.json'])):remove(p)
for name in ['boss_attacks.json','creature_sounds.json']:
 p=R/'src/main/resources/data/deepnight'/name
 v=json.loads(p.read_text());p.write_text(json.dumps({k:v for k,v in v.items() if k not in removed},indent=2))
p=R/'src/main/resources/assets/deepnight/lang/en_us.json';v=json.loads(p.read_text())
p.write_text(json.dumps({k:v for k,v in v.items() if not any(k.endswith('.'+n) or k.endswith('.'+n+'_sigil') or k.endswith('.'+n+'_spawn_egg') for n in removed)},indent=2))
p=R/'src/main/java/dev/deepnight/client/ClientEvents.java';t=p.read_text();t='\n'.join(l for l in t.splitlines() if 'NativeAoDRenderer' not in l)+'\n';p.write_text(t)
for name in ['port_mobosses.py','port_dawncraft.py']:
 p=R/'tools'/name;t=p.read_text().replace('work-in-progress/source-combat/src/main/','src/main/');p.write_text(t)
p=R/'build.gradle';p.write_text(p.read_text().replace("version = '1.1.1'","version = '1.2.0'"))
(R/'verification/removed-source-content.json').write_text(json.dumps({'removed':sorted(removed),'excluded_dawn':sorted(excluded)},indent=2))
print('Removed',len(removed),'species; restored',len(dawn),'DawnCraft bosses')
