"""Deterministic NPC eye rewards; old discovery records retain their save IDs."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
# The first twelve distinct eyes finish before the stronghold. Extra trials are optional.
STAGES = [
    ('old', 'sera', '06', 'A Key in the Archive', 'submit', 'paper', 16, 'Bring 16 paper for the restored archive', 'The first key belongs to someone willing to preserve a history other than their own.'),
    ('rogue', 'iona', '08', 'The Scout Returns', 'craft', 'compass', 1, 'Craft a compass', 'A scout earns trust by finding the way home. Keep the compass; this eye is your reward.'),
    ('cold', 'tamsin', '10', 'Warmth before Winter', 'submit', 'white_wool', 16, 'Donate 16 white wool', 'These blankets will shelter people who cannot join your expedition. Take the Cold Eye with their thanks.'),
    ('black', 'fen', '12', 'Stores for the Crossing', 'submit', 'cooked_cod', 24, 'Donate 24 cooked cod', 'A ship needs provisions before it needs a legend. The Black Eye has waited long enough in my chest.'),
    ('lost', 'rowan', '16', 'No Name Left Behind', 'submit', 'book', 8, 'Donate 8 books', 'We will write down the names the crown erased. Carry the Lost Eye for those who were never allowed to leave.'),
    ('corrupted', 'elian', '18', 'The Watch Holds', 'kill', 'pillager', 12, 'Reach 12 pillager kills', 'You have stood against the raiders. I release the Corrupted Eye into your keeping; it is a key, not a badge of rank.'),
    ('magical', 'sera', '21', 'Light in the Margins', 'collect', 'enchanting_table', 1, 'Hold an enchanting table', 'You have the means to study the enchantment safely. Keep your table and take the Magical Eye.'),
    ('nether', 'mara', '24', 'A Flame Brought Home', 'submit', 'blaze_rod', 8, 'Donate 8 blaze rods', 'These rods will keep the forge alive. The Nether Eye no longer belongs in a locked royal vault.'),
    ('cursed', 'ysolde', '27', 'Gold without a Crown', 'submit', 'gold_ingot', 24, 'Donate 24 gold ingots', 'We can use the gold to repair what the old armies broke. The Cursed Eye is yours to carry beyond their borders.'),
    ('guardian', 'fen', '30', 'Silence beneath the Tide', 'kill', 'elder_guardian', 1, 'Defeat an elder guardian', 'The coast has one less threat. I kept the Guardian Eye for a traveller who could return from that depth.'),
    ('witch', 'tamsin', '32', 'Medicine after Midnight', 'submit', 'ghast_tear', 4, 'Donate 4 ghast tears', 'The infirmary can use these tears. Take the Witch Eye; no one should have to wait for luck to open a road.'),
    ('evil', 'rowan', '34', 'The Twelfth Promise', 'submit', 'emerald', 24, 'Donate 24 emeralds', 'Twelve different keys, earned through service. Keep them safe and return to Eldara for the stronghold expedition.'),
    ('wither', 'elian', '34', 'A Challenge to the Wither', 'kill', 'wither', 1, 'Defeat the Wither', 'This trial was your choice. Take the Wither Eye as another key for the journey ahead.'),
    ('undead', 'ysolde', '34', 'The Restless Patrol', 'kill', 'wither_skeleton', 24, 'Reach 24 wither skeleton kills', 'The dead should not be another king\'s army. The Undead Eye is your reward for breaking that patrol.'),
    ('exotic', 'fen', '34', 'A Light below Water', 'collect', 'conduit', 1, 'Hold a conduit', 'Keep the conduit for your own expeditions. I give you the Exotic Eye for bringing light into the deep.'),
    ('cryptic', 'sera', '34', 'The Final Annotation', 'submit', 'experience_bottle', 16, 'Donate 16 bottles of enchanting', 'Sixteen witnesses, each entrusted through work rather than chance. The Cryptic Eye completes our collection.'),
]

def expand_eyes(quests):
    quests[:] = [q for q in quests if not q['id'].startswith('ac_key')]
    by_id = {q['id']: q for q in quests}
    for i, (eye, giver, gate, title, kind, item, count, label, response) in enumerate(STAGES, 1):
        eye_name = eye.title() + ' Eye'
        next_step = (f"Next: speak to {STAGES[i][1].title()} after main quest {STAGES[i][2]}." if i < 16 else 'All sixteen eye trials are complete.')
        quests.append(dict(
            id=f'ac_key{i:02}', title=f'{i:02} | {title}', chapter='The Lost Eyes | NPC Trials',
            section='The Twelve Keys' if i <= 12 else 'Four Further Witnesses',
            story=f'{giver.title()} holds the {eye_name} in trust for the expedition.\n\n{label}. Then choose Discuss a quest with {giver.title()} and turn in this assignment through Covenant quests to receive one {eye_name}. '
                  'You do not need to find or hold the eye first. Earlier tracked kills and crafts count; donated materials are consumed at turn-in.\n\n'
                  + ('This is one of the twelve trials required before Eldara sends you to the stronghold.' if i <= 12 else 'This additional eye is optional for the stronghold quest.')
                  + '\n\n' + next_step,
            aftermath=response + ' ' + next_step, icon='endrem:'+eye+'_eye',
            requires=[f'ac_m{gate}'] + ([f'ac_key{i-1:02}'] if i > 1 else []),
            objectives=[dict(type=kind, target='minecraft:'+item, count=count, label=label),
                        dict(type='talk', target='thedeepnightnpcs:'+giver, count=1, label='Discuss the '+eye_name+' with '+giver.title(), dialogue=response)],
            rewards=[dict(item='endrem:'+eye+'_eye', count=1)], emeralds=4+i, xp=75+i*10,
            faction='Archivists', standing=10, cooldown=0, choices=[], giver=giver))
    q = by_id['ac_m35']
    if 'ac_key12' not in q['requires']: q['requires'].append('ac_key12')
    q['title'] = '35 | The Portal at the Spire'
    q['story'] = 'An End portal frame already stands at the Spire. You do not need to locate a stronghold or build another frame.\n\nComplete the first twelve NPC eye trials, then discuss the portal preparations with the Spire Guard. The trials award twelve distinct End Remastered eyes directly. Keep those keys for the existing frame at the Spire; this quest does not consume them. Four further NPC trials offer additional eyes. Existing End Remastered sources remain available for replacements. Return to Eldara for the final expedition preparations before crossing.'
    q['objectives'] = [dict(type='talk', target='thedeepnightnpcs:spire_guard', count=1, label='Discuss the existing portal at the Spire with the Spire Guard', dialogue='The End portal frame is already here at the Spire. Bring your twelve distinct quest-earned eyes to this frame; there is no need to search for a stronghold. Finish Eldara\'s expedition preparations before you open the portal and cross.')]
    q['aftermath'] = 'The Spire Guard confirms the portal plan. Eldara has the final supply and return preparations.'
    by_id['ac_m31']['story'] = 'Sera learns why the king collected relic eyes: the final witness waits beyond a door that refuses ordinary keys.\n\nGather 16 ender pearls. Continue The Lost Eyes | NPC Trials with the named companions. The first twelve trials award the distinct End Remastered keys required before the stronghold assignment. Sera begins the chain after The First Hearth. The Explorer Journal remains an optional record of eyes obtained, including quest rewards.'
    by_id['ac_m34']['story'] = by_id['ac_m34']['story'].replace('Finish any preparations and relic-eye expeditions you need before approaching the stronghold.', 'Finish the first twelve NPC eye trials before approaching the stronghold; Rowan holds the twelfth key. Four further trials offer optional extra eyes.')
    for q in quests:
        if q['id'].startswith('ac_eye'):
            q['story'] = q['story'].split('\n\n')[0] + '\n\nOptional discovery record: obtain this eye to earn its End Remastered advancement. The Lost Eyes | NPC Trials awards it directly through NPC work, and existing End Remastered sources also count. This record never consumes an eye and remains complete after the eye is used.'
        if q['id'].startswith('ac_key') or q['id'] in ('ac_m31', 'ac_m34'):
            q['story'] = q['story'].replace('stronghold', 'Spire portal')
            q['aftermath'] = q['aftermath'].replace('stronghold', 'Spire portal')
            for o in q['objectives']:
                if 'dialogue' in o: o['dialogue'] = o['dialogue'].replace('stronghold', 'Spire portal')
    by_id['ac_m31']['story'] += '\n\nAn End portal frame already exists at the Spire. Save your eyes for that frame; no stronghold search is required.'
    by_id['ac_m36']['story'] = by_id['ac_m36']['story'].replace('Open the End portal using', 'Use the End portal frame already at the Spire. Open it using')
    by_id['ac_treaty01']['story'] = by_id['ac_treaty01']['story'].replace('The stronghold is found', 'The existing End portal frame at the Spire is our crossing point')
    by_id['ac_treaty06']['story'] = 'Elian asks you to prove you can hold a night watch before the expedition leaves the Spire.\n\nReach 24 zombie kills, then return to Eldara. Earlier kills count. The End portal frame already stands at the Spire; this preparation does not require a stronghold expedition.'
    by_id['ac_treaty06']['objectives'] = [dict(type='kill', target='minecraft:zombie', count=24, label='Reach 24 zombie kills')]

if __name__ == '__main__':
    path = ROOT/'src/main/resources/data/ashenquests/ashen_quests/campaign.json'
    data = json.loads(path.read_text(encoding='utf-8'))
    expand_eyes(data['quests'])
    path.write_text(json.dumps(data, ensure_ascii=False, indent=2), encoding='utf-8')
