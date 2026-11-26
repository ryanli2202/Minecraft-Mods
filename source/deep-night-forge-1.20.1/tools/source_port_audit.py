"""Trace source boss dependencies and renderer features without claiming parity."""
from pathlib import Path
import json, re
from audit_java_imports import SOURCES

ROOT = Path(__file__).resolve().parents[1]
selection = json.loads((ROOT/'tools/java_creature_selection.json').read_text())
result=[]
for pack,(folder,namespace,package) in SOURCES.items():
    root=ROOT.parent/'deobfuscated'/folder/'src/main'
    java=root/'java'/package
    index={p.stem:p for p in java.rglob('*.java')}
    for boss in selection[pack]['boss'].split():
        pending=[boss+'Entity',boss+'Renderer']; seen=set(); registry=set()
        while pending:
            name=pending.pop()
            if name in seen or name not in index:continue
            seen.add(name)
            content=index[name].read_text(encoding='utf-8')
            body=re.sub(r'^import .*?;','',content,flags=re.M)
            for imp in re.findall(r'^import '+re.escape(package.replace('/','.'))+r'\.([\w.]+);',content,re.M):
                dependency=imp.split('.')[-1]
                if re.search(r'\b'+dependency+r'\b',body):
                    if imp.startswith(('init.','network.')) or '.' not in imp:registry.add(dependency)
                    else:pending.append(dependency)
            if name.endswith('Procedure'):
                pending.extend(n for n in set(re.findall(r'\b(\w+Procedure)\.',body)) if n in index)
        renderer=index[boss+'Renderer'].read_text(encoding='utf-8')
        result.append({'pack':pack,'boss':boss,'files':[str(index[n].relative_to(root)) for n in sorted(seen)],'registry_dependencies':sorted(registry),'dynamic_texture':'getTexture()' in renderer,'emissive_layers':re.findall(r'"([^"\n]+\.png)"',renderer),'status':'source audit only; runtime parity not yet verified'})
target=ROOT/'verification/source-boss-port-audit.json'
target.write_text(json.dumps(result,indent=2),encoding='utf-8')
for pack in SOURCES:
    rows=[r for r in result if r['pack']==pack]
    print(pack,len(rows),'bosses,',len({f for r in rows for f in r['files']}),'direct dependency files')
