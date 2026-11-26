"""Inventory reconstructed source creatures without changing the source mods."""
from pathlib import Path
import json,re,collections
ROOT=Path(__file__).resolve().parents[1]
SOURCES={'aod':('age_of_dragons-1.1.2-neoforge-1.21.1','age_of_dragons','net/mcreator/ageofdragons'), 'dawn':('dawncraft_mobs-1.18-1.3.9a','simple_mobs','net/mcreator/simplemobs'), 'mobosses':('mobosses-1.0.7','mobosses','net/hell/mobosses')}
def audit():
    result=[]
    for prefix,(folder,namespace,package) in SOURCES.items():
        root=ROOT.parent/'deobfuscated'/folder/'src/main'
        java=root/'java'/package
        entities=java/'entity'
        lang=json.loads((root/'resources/assets'/namespace/'lang/en_us.json').read_text(encoding='utf-8-sig'))
        for p in entities.rglob('*.java'):
            text=p.read_text()
            m=re.search(r'public class (\w+) extends (\w+)',text)
            if not m: continue
            name,base=m.groups()
            if base not in ('Monster','PathfinderMob','TamableAnimal','Animal','AbstractIllager','Mob','AgeableMob','FlyingMob'):continue
            renderers=list(java.rglob(name.removesuffix('Entity')+'Renderer.java'))
            result.append(dict(pack=prefix,entity=name,base=base,boss='ServerBossEvent' in text,
                health=(re.search(r'Attributes.MAX_HEALTH,\s*([\d.]+)',text) or [None,'0'])[1],
                renderer=str(renderers[0].relative_to(root)) if renderers else None,
                java=str(p.relative_to(root))))
    (ROOT/'verification/java-import-inventory.json').write_text(json.dumps(result,indent=2)+'\n')
    for pack in SOURCES:
        rows=[r for r in result if r['pack']==pack]
        print(pack,len(rows),dict(collections.Counter(r['base'] for r in rows)))
        print('Bosses:',', '.join(r['entity'] for r in rows if r['boss']))
        print('Pets:',', '.join(r['entity'] for r in rows if r['base']=='TamableAnimal'))
    return result
if __name__=='__main__':audit()
