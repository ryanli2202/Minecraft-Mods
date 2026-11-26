"""Regenerate source-verified notes for the supplied Deep Night Java port.

Usage: python tools/build_deepnight_entries.py [path/to/deep-night-forge-1.20.1]
The generated JSON ships in the guide; the content mod is never a required dependency.
"""
from pathlib import Path
from collections import Counter
import hashlib
import json
import re
import sys

root = Path(__file__).resolve().parents[1]
source = Path(sys.argv[1]) if len(sys.argv) > 1 else root.parent / 'deep-night-forge-1.20.1'
java = source / 'src/main/java/dev/deepnight'
data = source / 'src/main/resources/data/deepnight'
lang = json.loads((source / 'src/main/resources/assets/deepnight/lang/en_us.json').read_text(encoding='utf-8'))
species_source = (java / 'Species.java').read_text(encoding='utf-8')
species = []
for ident, title, kind, values in re.findall(r'^\s+([A-Z_]+)\("([^"]+)", Kind\.(\w+), ([^\n]+)\)[,;]', species_source, re.M):
    fields = [v.strip().removesuffix('f') for v in values.split(',')]
    species.append(dict(id=ident.lower(), title=title, kind=kind, health=float(fields[0]), damage=float(fields[1]), armor=float(fields[2]), style=int(fields[8]), aquatic=fields[9]=='true', ranged=fields[10]=='true'))
counts = Counter(s['kind'] for s in species)
assert counts == {'BOSS': 41, 'PET': 31, 'MOB': 65, 'PASSIVE': 40}, counts
bosses = [s for s in species if s['kind'] == 'BOSS']
entries = {}

def item_name(ident):
    return lang.get('item.' + ident.replace(':', '.'), ident.split(':')[-1].replace('_', ' ').title())

def entry(key, sections, **fields):
    entries[key] = dict(fields, sections=sections)

unlock = 'Defeat the Ender Dragon to unlock the campaign and receive the Boss Summon Tool. An existing Free the End advancement also unlocks it on login. Reclaim a lost tool with /deepnight reclaim.'
arena = 'Right-click the Boss Summon Tool on Easy, Normal or Hard. It summons your next undefeated boss near you, in the current dimension. Prepare broad, flat, solid ground and at least 8 blocks of headroom; a roughly 50 x 50 arena gives room to dodge. Bosses have no natural spawns.'
coop = 'For campaign credit, unlock the dragon gate, be on this same campaign stage, deal damage yourself or through your owned pet, and survive within 128 blocks in the same dimension when the boss dies. The guide checkbox records your collection only; it does not advance the campaign. Mark shared victories manually if your normal kill statistic was not credited.'
pet_summon = 'No natural spawns. Right-click the reusable Pet Summoning Tool, search for this pet, then choose Summon. It arrives as a full-health, already-tamed adult at level 1. Leave clear space nearby; the tool recharges for 10 seconds.'
pet_tool = 'Craft a Pet Summoning Tool with an Ender Pearl above a Book, a Gold Ingot on each side of the Book, and a Golden Apple below. A level-3 Cleric also sells one for a base price of 24 Emeralds plus 2 Ender Pearls.'
pet_tame = 'The Pet Summoning Tool gives ownership immediately. If this species was instead spawned untamed with a creative spawn egg or command, feed it 3 Bonding Treats. Only its owner can care for or ride a tamed pet.'
pet_care = 'Bonding Treats heal 16 health; on babies they also advance growth by 60 seconds. Feed Golden Apples to two tamed adults of the same species to breed; the baby inherits ownership. Craft 4 treats from 1 Night Essence, 1 Golden Carrot and 1 Cooked Beef.'
pet_growth = 'Levels 1-50 grant +2% base health and attack per level above 1 (up to +98%). Stay within 32 blocks of your living, online owner for 5 XP every 20 seconds. Nearby pets get 15 XP for a hostile kill by their owner or owned pet, or 100 XP for a Deep Night boss. Each level needs 100 + 25 x (level - 1) XP. Interact with the Pet Summoning Tool to inspect level and XP.'
styles = {
    0: 'A forward cleave reaches 10 blocks. Move out of the outlined cone during its wind-up.',
    1: 'A charge sweeps a lane up to 24 blocks ahead. Step sideways out of the marked lane.',
    2: 'A ground slam hits within 8 blocks and knocks targets back. Leave the marked ring or get above the low shockwave.',
    3: 'A narrow fire cone reaches 16 blocks and ignites targets. Strafe outside the marked cone.',
    4: 'A cross-shaped strike hits along the two axes within 12 blocks and inflicts Slowness II. Dodge diagonally between the marked arms.',
    5: 'A blast targets your marked position within a 5-block radius. Move away during the wind-up. Later phases also hit within 6 blocks of the boss.',
    6: 'A blast targets your marked position within a 4-block radius and inflicts Slowness II. Later phases also hit within 6 blocks of the boss.',
    7: 'A 12-block pulse heals the boss when it damages a target. Retreat outside the marked ring.',
    8: 'A long fire breath cone reaches 22 blocks and ignites targets. Move sideways out of the cone during the wind-up.',
    9: 'Alternates an inner blast inside 7 blocks and an outer ring from 7 to 16 blocks. A second blast targets your marked position within 4 blocks. Watch both rings; hits inflict Wither.'
}
for s in species:
    ident = s['id']; kind = s['kind']; boss = kind == 'BOSS'; pet = kind == 'PET'
    sections = {}
    if boss:
        rank = bosses.index(s) + 1
        previous = 'the Ender Dragon unlock' if rank == 1 else bosses[rank - 2]['title']
        sections['Campaign'] = [f"Boss {rank} of 41: {s['title']}. Requires {previous} before this stage can be summoned.", unlock]
        sections['Where to find'] = [arena, 'One campaign boss can be active across all dimensions. Sneak + use the tool or run /deepnight status for progress and its last location. /deepnight abandon cancels the fight without advancing.']
        sections['Combat'] = [f"Total health: {s['health']:g}; base melee damage: {s['damage']:g}; armour: {s['armor']:g}. These are base values before difficulty, effects and armour mitigation.", styles[s['style']], 'Phases change below 65% and 30% total health, accelerating attacks and calling reinforcements. The boss bar includes reserve health above 1,000 HP. Single hits are capped at 12% of a health segment before armour. Bosses retreat after 30 loaded seconds without a living Survival fighter nearby; retreat does not count as victory.']
        sections['Victory and rewards'] = [f"Each eligible campaign survivor receives 4 {s['title']} Sigils and {rank + 3} Night Essence. Only a legitimate combat victory advances the campaign; /kill does not.", coop]
    elif pet:
        sections['Where to find'] = [pet_summon, pet_tool]
        sections['Taming'] = [pet_tame]
        sections['Pet care and breeding'] = [pet_care]
        sections['Commands and riding'] = ['Sneak + right-click with an empty hand to switch stay/follow. Equip a Saddle on your adult pet, then interact with an empty hand to ride. Riding uses land controls; there are no flight controls.']
        sections['Growth'] = [pet_growth]
        sections['Abilities'] = [f"Level-1 base health: {s['health']:g}; attack damage: {s['damage']:g}; armour: {s['armor']:g}. Follows and defends its owner."]
        if ident in ('ember_drake', 'tide_drake', 'dusk_drake'):
            sections['Abilities'].append('Breath attacks reach targets within 14 blocks with line of sight, every 2.5 seconds; base breath damage is 6 and scales with pet level.' + (' Ember breath also ignites targets for 3 seconds.' if ident == 'ember_drake' else ''))
        if ident == 'star_unicorn':
            sections['Abilities'].append('Heals its nearby owner for 2 health every 10 seconds while active, adult and within 10 blocks.')
        if s['aquatic'] or ident == 'tide_drake':
            sections['Abilities'].append('Can breathe underwater.' + (' The summoning tool can also place this aquatic pet in open water.' if s['aquatic'] else ''))
    else:
        spawn = json.loads((data / f'forge/biome_modifier/{ident}.json').read_text())
        selection = spawn['biomes']; biomes = [selection] if isinstance(selection, str) else selection
        names = {'#minecraft:is_overworld': 'Overworld-tagged biomes', '#minecraft:is_ocean': 'ocean-tagged biomes'}
        where = ', '.join(names.get(b, b.split(':')[-1].replace('_', ' ').title()) for b in biomes)
        rule = 'Requires water at the spawn position.' if s['aquatic'] else 'Requires solid ground with brightness above 8.' if kind == 'PASSIVE' else 'Uses normal monster light, difficulty and ground spawn rules.'
        sections['Where to find'] = [f'Naturally eligible in {where}. {rule} Biomes are eligible locations, not guaranteed nearby spawns; server configuration can change them.']
        sections['Behaviour'] = [f"{'Neutral NPC; retaliates if attacked' if kind == 'PASSIVE' else 'Hostile creature; attacks players'}. Base health: {s['health']:g}; attack damage: {s['damage']:g}; armour: {s['armor']:g}. Not a campaign boss and cannot be tamed."]
        if kind == 'PASSIVE':
            sections['Trading'] = ['Interact while holding an Emerald to exchange 1 Emerald for 2 Night Essence. Names such as blacksmith or weapons seller do not give this port a separate profession shop.']
        else:
            sections['Drops'] = ['Drops 1-2 Night Essence when recently hit by a player and killed. Boss-summoned reinforcements do not drop essence.']
            if s['ranged']:
                sections['Combat'] = ['Can also deal ranged magic damage with line of sight within 18 blocks, every 3.25 seconds.']
            if ident == 'grave_ghoul':
                sections['Combat'] = ['Successful melee hits inflict Hunger for 5 seconds.']
    icon = f'deepnight:{ident}_sigil' if boss else f'deepnight:{ident}_spawn_egg'
    entry('entity:deepnight:' + ident, sections, boss=boss, tameable=pet, icon=icon, categories=['MOBS'] + (['BOSSES'] if boss else ['PETS'] if pet else []))

weapons = re.findall(r'([A-Z_]+)\((\d+),(-?[\d.]+)f,"([^"]+)"\)', (java / 'Weapon.java').read_text())
assert len(weapons) == 25
for ident, rank, speed, preset in weapons:
    rank = int(rank); ident = ident.lower(); boss = bosses[-1] if rank == 9 else bosses[rank]
    recipe = json.loads((data / f'recipes/{ident}.json').read_text())
    ingredients = recipe['key']; sigil = ingredients['S']['item']; variant = ingredients['A']['item']
    assert sigil == f"deepnight:{boss['id']}_sigil"
    sections = {
        'Where to find': [f"Craft after defeating {boss['title']} (campaign boss {bosses.index(boss)+1} of 41). This is a tier-{rank+1} weapon."],
        'Crafting': [f"Crafting table: {item_name(sigil)} in the top centre; Night Essence, Netherite Ingot, Night Essence across the middle; {item_name(variant)} in the bottom centre. Requires 1 sigil, 2 Night Essence, 1 Netherite Ingot and 1 {item_name(variant)}. The live recipe panel reflects loaded datapacks."],
        'Weapon properties': [f"Base player attack damage: {10+rank}; attack speed: {4+float(speed):g}; durability: {2400+rank*240}. Enchantable and fireproof. Better Combat moveset: {preset.replace('_',' ')}; ordinary melee works without Better Combat."],
        'Checklist': ['Carrying this weapon in your inventory automatically records it in the Weapon checklist after the guide has been opened. You can also mark it manually.']
    }
    if rank == 5: sections['Weapon properties'].append('Successful melee hits ignite the target for 4 seconds.')
    entry('item:deepnight:' + ident, sections, categories=['WEAPONS'])

for boss in bosses:
    rank = bosses.index(boss)+1
    used = [item_name('deepnight:'+w[0].lower()) for w in weapons if (int(w[1]) == 9 and rank == 41) or (int(w[1]) < 9 and rank == int(w[1])+1)]
    sections = {'Where to find': [f"Earn 4 on an eligible campaign victory against {boss['title']} (boss {rank}/41).", coop]}
    sections['Uses'] = ['Weapon ingredient for: '+', '.join(used)+'.'] if used else ['Convert one sigil into 16 Night Essence with its shapeless crafting recipe.']
    entry(f"item:deepnight:{boss['id']}_sigil", sections)

entry('item:deepnight:boss_summon_tool', {'Where to find':[unlock], 'Use':[arena, 'Sneak + right-click for progress. /deepnight status reports the active fight; /deepnight abandon cancels it without advancing. The tool is reusable.']}, categories=['TOOLS'])
entry('item:deepnight:pet_summoning_tool', {'Where to find':[pet_tool], 'Use':[pet_summon, 'Includes the 31 Deep Night pets plus supported installed TamableAnimal and horse species. Custom ownership systems may not be supported. Use on a tamed pet to inspect its level, XP and health.']}, categories=['TOOLS'])
entry('item:deepnight:bonding_treat', {'Crafting':['Craft 4 with 1 Night Essence, 1 Golden Carrot and 1 Cooked Beef (shapeless).'], 'Pet care':[pet_tame, pet_care]})
entry('item:deepnight:night_essence', {'Where to find':['Ordinary Deep Night creatures drop 1-2 after a player-assisted kill; boss reinforcements do not. Eligible campaign victories grant 4-44, depending on stage.', 'Trade 1 Emerald to a neutral Deep Night NPC for 2 Night Essence. Intermediate boss sigils that have no weapon tier convert into 16 essence.'], 'Uses':['Ingredient for all 25 Deep Night weapons and Bonding Treats.']})
entry('item:deepnight:field_guide', {'Where to find':['Received with the initial campaign unlock, or use /deepnight guide for a replacement.'], 'Use':['Right-click for the Deep Night content mod\'s field instructions. Press J to open this full Deep Night Guide.']})

output = root / 'src/main/resources/assets/deepnightguide/deepnight_entries.json'
output.write_text(json.dumps(entries, indent=2, ensure_ascii=False)+'\n', encoding='utf-8')
assert len(entries) == 248
content_version = re.search(r"version = '([^']+)'", (source/'build.gradle').read_text()).group(1)
coverage = dict(content_version=content_version, guide_version='1.2.0', creatures=len(species), kinds=dict(counts), weapons=len(weapons), sigils=len(bosses), support_items=5, total_notes=len(entries), boss_ids=[s['id'] for s in bosses], pet_ids=[s['id'] for s in species if s['kind']=='PET'], weapon_ids=[w[0].lower() for w in weapons], source_sha256={p.name:hashlib.sha256(p.read_bytes()).hexdigest() for p in sorted(java.glob('*.java'))})
(root/'verification/deepnight-content-coverage.json').write_text(json.dumps(coverage, indent=2)+'\n')
print(f'Generated {len(entries)} notes: {dict(counts)}, {len(weapons)} weapons, 41 sigils, 5 support items.')
