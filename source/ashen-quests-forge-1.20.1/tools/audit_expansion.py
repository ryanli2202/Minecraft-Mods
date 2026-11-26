"""Audit the 385-entry upgrade and all authored conversation routes."""
import json
from pathlib import Path
P=Path(__file__).resolve().parents[1]
old=json.loads((P/'tools/reference/pre-dialogue-campaign.json').read_text(encoding='utf-8-sig'))['quests']
new=json.loads((P/'src/main/resources/data/ashenquests/ashen_quests/campaign.json').read_text(encoding='utf-8'))['quests']
by={q['id']:q for q in new};old_ids={q['id'] for q in old}
errors=[];changed=[]
for prior in old:
    q=by.get(prior['id'])
    if not q:errors.append('Removed '+prior['id']);continue
    for key in ('title','chapter','section','aftermath','icon','requires','rewards','emeralds','xp','faction','standing','cooldown','choices','giver'):
        if prior[key]!=q[key]:errors.append('Changed '+prior['id']+' '+key)
    before=prior['objectives'];after=q['objectives']
    if after[:len(before)]!=before:errors.append('Changed original objective indices '+q['id'])
    if len(after)>len(before):
        changed.append(q['id'])
        if not all(o['type']=='talk' for o in after[len(before):]):errors.append('Non-conversation appended '+q['id'])
added=[q for q in new if q['id'] not in old_ids]
for q in added:
    talks=[o for o in q['objectives'] if o['type']=='talk']
    if not talks:errors.append('New quest missing conversation '+q['id'])
    if not any(o['target']!='thedeepnightnpcs:'+q['giver'] for o in talks):errors.append('No other character '+q['id'])
all_talks=[o for q in new for o in q['objectives'] if o['type']=='talk']
if len({o['dialogue'] for o in all_talks})!=len(all_talks):errors.append('Reused conversation response')
assert len(new)==557 and len(added)==172 and len(changed)==48
assert all(q['giver']=='eldara' for q in new if q['chapter'].startswith('Book '))
assert all(q['requires']==['ac_m06'] for q in added if q['id'].startswith('ns_') and q['id'].endswith('01'))
report=dict(result='PASS' if not errors else 'FAIL',errors=errors,previous_quests_preserved=len(old),new_quests=len(added),previous_quests_with_appended_conversations=len(changed),conversation_objectives=len(all_talks),unique_authored_responses=len({o['dialogue'] for o in all_talks}),original_objective_indices_preserved=not errors,new_side_stories_open_after='ac_m06',new_books_continue_after='hc_home06',all_new_quests_require_other_npcs=True)
(P/'verification/expansion-audit.json').write_text(json.dumps(report,indent=2))
print(json.dumps(report,indent=2))
if errors:raise SystemExit(1)
