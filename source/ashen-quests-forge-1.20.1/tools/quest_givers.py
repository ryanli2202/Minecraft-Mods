"""Explicit ownership for established arcs; named requests retain their author."""
import re
from collections import Counter

NAMES = ('rowan', 'sera', 'mara', 'elian', 'iona', 'tamsin', 'fen', 'ysolde')
ARC_OWNERS = {
    'knight':'elian', 'ranger':'iona', 'mage':'sera', 'healer':'tamsin',
    'colony':'rowan', 'relic':'sera', 'dragon':'ysolde', 'sea':'fen',
    'wild':'iona', 'border':'elian', 'forge':'mara', 'college':'sera',
    'steward':'rowan', 'atlas':'iona', 'harbor':'fen', 'garden':'tamsin',
    'sky':'ysolde', 'watch':'elian', 'souls':'sera', 'eye':'sera',
    'trial':'elian', 'legend':'elian', 'contract':'rowan',
}
OVERRIDES = {'ac_trial08':'fen', 'ac_legend06':'iona', 'ac_legend08':'elian',
             'hc_contract01':'rowan', 'hc_contract05':'elian',
             'hc_contract08':'sera', 'hc_contract10':'rowan'}

def assign_givers(quests):
    for q in quests:
        if q['chapter'].startswith(('Book I |', 'Book II |', 'Book III |')):
            giver = 'eldara'
        elif q['id'] in OVERRIDES:
            giver = OVERRIDES[q['id']]
        elif q['section'].split(' | ')[0].lower() in NAMES:
            giver = q['section'].split(' | ')[0].lower()
        elif not q['id'].startswith(('ac_', 'lc_', 'hc_')):
            giver = 'elian'  # The recovered Deep Night ledger belongs to Elian.
        else:
            # Preserve the named requester of individual errands and relief contracts.
            name = re.search(r'\b(' + '|'.join(NAMES) + r')\b', q['story'].split('\n\n')[0], re.I)
            arc = re.sub(r'\d+$', '', q['id'].split('_',1)[1])
            giver = name.group(0).lower() if name else ARC_OWNERS.get(arc, 'rowan')
        q['giver'] = giver
        q['story'] = q['story'].replace('through the quest book', 'in person to the quest giver').replace('Turn in through the quest book', 'Turn in to the quest giver')
        q['story'] = q['story'].replace('Repeatable contract: use Claim in this journal after the cooldown after claiming.', 'Repeatable contract: speak to the quest giver to accept this work again after its 20-minute cooldown.')
        q['story'] = q['story'].replace('Deliver the listed materials to the relief ledger in person to the quest giver.', 'Deliver the listed materials to the quest giver in person.')
        if q['id']=='ac_m01':
            q['story'] = q['story'].replace('You find a letter', 'Eldara hands you a letter')
            q['story'] = q['story'].replace('This journal tells a story through letters; Rowan and the other named characters are narrators, not spawned NPCs.', 'Eldara coordinates the main chronicle. Rowan and the other companions offer their own requests when you speak to them.')
            q['story'] = q['story'].replace('Keep a crafting table in your inventory, then claim this entry. Pin your next quest. Claiming each story reward unlocks the next entry.', 'Accept this quest from Eldara, keep a crafting table in your inventory, then turn it in to her. Press minus (-) to track progress. Return to Eldara for each new main assignment.')
    assert sum(q['giver']=='eldara' for q in quests)==180
    assert set(NAMES) <= {q['giver'] for q in quests}
    return dict(Counter(q['giver'] for q in quests))

