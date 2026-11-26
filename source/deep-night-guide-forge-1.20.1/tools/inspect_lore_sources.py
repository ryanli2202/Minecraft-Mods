import json,zipfile,collections
from pathlib import Path
mods=Path(r'C:\Users\ryanl\AppData\Roaming\ModrinthApp\profiles\fantasycraft 2.0\mods')
report={'dimensions':{},'boss_tags':{},'boss_class_candidates':{}}
for jar in mods.glob('*.jar'):
    with zipfile.ZipFile(jar) as z:
        dims=[]; bosses=[]
        for n in z.namelist():
            p=n.split('/')
            if len(p)>=4 and p[0]=='data' and p[2]=='dimension' and n.endswith('.json'):
                dims.append(p[1]+':'+('/'.join(p[3:])[:-5]))
            if '/tags/entity_types/' in n and any(s in n.lower() for s in ['boss','miniboss']) and n.endswith('.json'):
                report['boss_tags'][jar.name+'!'+n]=json.loads(z.read(n))
            if n.endswith('.class') and ('entity' in n.lower()) and b'net/minecraft/server/level/ServerBossEvent' in z.read(n):bosses.append(n)
        if dims:report['dimensions'][jar.name]=dims
        if bosses:report['boss_class_candidates'][jar.name]=bosses
out=Path(__file__).resolve().parents[1]/'verification/lore-source-audit.json';out.write_text(json.dumps(report,indent=2))
print('DIMENSIONS',json.dumps(report['dimensions'],indent=2))
print('TAGS',json.dumps(report['boss_tags'],indent=2)[:10000])
print('BOSS CLASS COUNTS',json.dumps({k:len(v) for k,v in report['boss_class_candidates'].items()},indent=2))
