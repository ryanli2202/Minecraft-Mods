from pathlib import Path
import json,re
R=Path(__file__).resolve().parents[1]
S=R.parent/'deobfuscated/dawncraft_mobs-1.18-1.3.9a/src/main'
J=S/'java/net/mcreator/simplemobs'
index={p.stem:p for p in J.rglob('*.java')}
selection=json.loads((R/'tools/java_creature_selection.json').read_text())['dawn']
patches={p.stem for p in (S/'resources/data/simple_mobs/epicfight_mobpatch').glob('*.json')}
registry=(J/'init/SimpleMobsModEntities.java').read_text()
result=[]
for kind,names in selection.items():
 for name in names.split():
  t=index[name+'Entity'].read_text()
  declaration=re.search(r'public static final RegistryObject<EntityType<'+name+r'Entity>>.*?;',registry,re.S)[0]
  path=re.search(r'register\(\s*"([^"]+)"',declaration)[1]
  row={'name':name,'kind':kind,'path':path,'epic_patch':path in patches,'geo':'IAnimatable' in t}
  result.append(row)
  if kind=='boss':print(row)
(R/'verification/dawn-animation-audit.json').write_text(json.dumps(result,indent=2))
print('Patched:',[r['name'] for r in result if r['epic_patch']])
