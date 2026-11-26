"""Check conversion fidelity and local modpack evidence, without changing the profile."""
import json, collections, hashlib
from zipfile import ZipFile
from pathlib import Path
P=Path(__file__).resolve().parents[1]
W=P.parent
REF=P/'tools/reference'
SOURCE=REF/'boundless/questpacks' if (REF/'boundless/questpacks').exists() else W/'boundless/questpacks'
campaign=json.loads((P/'src/main/resources/data/ashenquests/ashen_quests/campaign.json').read_text(encoding='utf-8'))['quests']
by_id={q['id']:q for q in campaign}
originals=[(p,json.loads(p.read_text(encoding='utf-8-sig'))) for p in SOURCE.glob('*/data/*/quests/*.json')]
errors=[]
for p,old in originals:
    q=by_id.get(old['id'])
    if not q: errors.append('Missing original '+old['id']);continue
    expected=[(k,t[k],t.get('count',1)) for t in old['completion']['complete'] for k in ('collect','submit','kill','achieve','effect','field') if k in t]
    actual=[(o['type'],o['target'],o['count']) for o in q['objectives'] if o['type']!='talk']
    if expected!=actual: errors.append('Changed objectives '+q['id'])
    if old.get('dependencies',[])!=q['requires']:errors.append('Changed prerequisites '+q['id'])
    if [r for r in old.get('reward',{}).get('items',[]) if r['item']!='minecraft:emerald']!=q['rewards']:errors.append('Lost bonus reward '+q['id'])
    if bool(old.get('repeatable'))!=bool(q['cooldown']):errors.append('Changed repeatability '+q['id'])
entities=set(json.loads((REF/'entities_list.json' if (REF/'entities_list.json').exists() else W/'entities_list.json').read_text())['entities'])
assets=json.loads((REF/'asset-index.json' if (REF/'asset-index.json').exists() else W/'.quest-authoring/reference/asset-index.json').read_text())
vanilla=Path.home()/'AppData/Roaming/ModrinthApp/meta/versions/1.20.1-47.4.20/1.20.1-47.4.20.jar'
if vanilla.exists():
    with ZipFile(vanilla) as z:
        for name in z.namelist():
            for kind,prefix in [('items','assets/minecraft/models/item/'),('advancements','data/minecraft/advancements/')]:
                if name.startswith(prefix) and name.endswith('.json'):assets[kind]['minecraft:'+name[len(prefix):-5]]=vanilla.name
missing_evidence=[]
for q in campaign:
    for o in q['objectives']:
        kind=o['type'];target=o['target']
        found=True
        if kind=='talk':found=target in {'thedeepnightnpcs:'+n for n in ('eldara','rowan','sera','mara','elian','iona','tamsin','fen','ysolde','caelen','jorge','spire_guard')}
        elif kind in ('kill','interact','tame'):found=target in entities
        elif kind in ('collect','submit','craft','smelt','fish'):found=target in assets['items']
        elif kind=='achieve':found=target in assets['advancements']
        if not found:missing_evidence.append(dict(quest=q['id'],type=kind,target=target))
def ancestors(id,seen=None):
    seen=set() if seen is None else seen
    for dep in by_id[id]['requires']:
        if dep not in seen:seen.add(dep);ancestors(dep,seen)
    return seen
main={q['id'] for q in campaign if q['chapter'].startswith('Book ')}
chain=ancestors('wn_spring06')|{'wn_spring06'}
if main!=chain:errors.append('Finale must require exactly all main-story quests')
if missing_evidence:errors.append('Missing target evidence: '+str(len(missing_evidence)))
report=dict(result='PASS' if not errors else 'FAIL',errors=errors,imported=len(originals),total=len(campaign),new=len(campaign)-len(originals),main_story=len(main),finale='wn_spring06',all_main_story_required=chain==main,side_quests_not_required=chain==main,original_objectives_dependencies_and_bonus_rewards_preserved=not errors,emeralds_for_every_quest=all(q['emeralds']>0 for q in campaign),source_evidence_missing=missing_evidence,evidence_scope='Entity targets checked against supplied 3252-entity export; item/advancement targets checked against existing modpack JAR asset index and Minecraft 1.20.1 JAR. This is static evidence, not a full 355-mod runtime test.')
(P/'verification/conversion-audit.json').write_text(json.dumps(report,indent=2),encoding='utf-8')
print(json.dumps(report,indent=2))
if errors:raise SystemExit(1)
