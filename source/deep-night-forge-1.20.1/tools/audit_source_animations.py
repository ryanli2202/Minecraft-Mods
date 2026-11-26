from pathlib import Path
import re,json
root=Path(__file__).resolve().parents[1]
base=root/'src/main'
errors=[]
for model in (base/'java/dev/deepnight/source/dawn/client').glob('*Model.java'):
    text=model.read_text()
    resource=re.search(r'"(animations/[^"\n]+)"',text)
    entity=base/'java/dev/deepnight/source/dawn/entity'/(model.stem.removesuffix('Model')+'Entity.java')
    if not resource or not entity.exists():continue
    keys=json.loads((base/'resources/assets/simple_mobs'/resource[1]).read_text())['animations']
    calls=set(re.findall(r'\.then\("([^"]+)"',entity.read_text()))
    missing=sorted(calls-set(keys))
    if missing:errors.append({'entity':entity.stem,'resource':resource[1],'missing':missing,'available':list(keys)})
print(json.dumps(errors,indent=2))
