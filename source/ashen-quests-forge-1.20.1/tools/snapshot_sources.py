"""Capture only quest definitions and public resource-ID evidence for a portable source archive."""
import json, shutil
from pathlib import Path
from zipfile import ZipFile
P=Path(__file__).resolve().parents[1]
W=P.parent
ref=P/'tools/reference'
ref.mkdir(parents=True,exist_ok=True)
for p in (W/'boundless/questpacks').glob('*/data/*/quests/**/*.json'):
    dest=ref/p.relative_to(W)
    dest.parent.mkdir(parents=True,exist_ok=True)
    shutil.copyfile(p,dest)
shutil.copyfile(W/'entities_list.json',ref/'entities_list.json')
assets=json.loads((W/'.quest-authoring/reference/asset-index.json').read_text())
vanilla=Path.home()/'AppData/Roaming/ModrinthApp/meta/versions/1.20.1-47.4.20/1.20.1-47.4.20.jar'
with ZipFile(vanilla) as z:
    for name in z.namelist():
        for kind,prefix in [('items','assets/minecraft/models/item/'),('advancements','data/minecraft/advancements/')]:
            if name.startswith(prefix) and name.endswith('.json'):assets[kind]['minecraft:'+name[len(prefix):-5]]=vanilla.name
(ref/'asset-index.json').write_text(json.dumps(assets,indent=2),encoding='utf-8')
print('Captured quest source and resource-ID references; no player state included.')
