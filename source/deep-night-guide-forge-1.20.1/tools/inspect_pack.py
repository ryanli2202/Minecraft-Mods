import json, zipfile, collections
from pathlib import Path
mods = Path(r'C:\Users\ryanl\AppData\Roaming\ModrinthApp\profiles\fantasycraft 2.0\mods')
for jar in mods.glob('*.jar'):
    if not any(s in jar.name.lower() for s in ['alexsmobs','mythicmount','saintsdragon','iceandfire','bookofdragon','kindred','monstie']): continue
    with zipfile.ZipFile(jar) as z:
        names=[n for n in z.namelist() if n.endswith('.json') and any(s in n for s in ['patchouli','book/','tame','taming'])]
        print(jar.name, len(names), names[:8])
        for n in names:
            if any(s in n for s in ['crow.json','hippogryph.json','thunder','taming','tame_items']):
                print(n,z.read(n).decode('utf-8')[:4500])
