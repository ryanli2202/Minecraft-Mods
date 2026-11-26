"""Refresh the release's human-readable catalogue from the generated content."""
from pathlib import Path
import json,collections,re
R=Path(__file__).resolve().parents[1];c=json.loads((R/'verification/content-catalogue.json').read_text());creatures=c['creatures']
old=(R/'README.txt').read_text()
start=re.search(r'THE (?:TEN|\d+) BOSSES',old).start();end=old.index('WEAPONS: ALL 25 SUPPLIED MODELS')
bosses=[x for x in creatures if x['kind']=='BOSS']
table='THE '+str(len(bosses))+' BOSSES\nRank  Boss                            Total HP  Base damage\n'
for i,b in enumerate(bosses):table+=f'{i+1:>3}   {b["title"]:<32}{b["stats"][0]:>6.0f}    {b["stats"][1]:>5.1f}\n'
table+='''
Every encounter is designed for post-dragon equipment. Bosses enter stronger
phases at 65% and 30% total health, speed up their special attacks and summon
reinforcements. Total health continues beyond Minecraft's 1,000-HP attribute
limit using a saved reserve; the boss bar shows the entire encounter.
Melee damage and total HP rise at every rank. Cones, charges, marked blasts,
root crosses, jumpable stomps, storm strikes and void rings reward dodging.
Per-hit damage is capped before armour at 12% of the health segment (maximum
120 damage), preventing one huge hit from skipping the fight. Balance will
still depend on the destination modpack and needs ordinary survival playtesting.

'''
old=old[:start]+table+old[end:]
old=old.replace('4-13 Night Essence','4-44 Night Essence').replace('Boss tier  Diamond','Sigil tier Diamond')
if 'Sigil tiers 1-9' not in old:old=old.replace("A chooses the variant in the table below. The recipe book unlocks from sigils.","A chooses the variant in the table below. The recipe book unlocks from sigils.\nSigil tiers 1-9 correspond to the first nine bosses; tier 10 uses The Deep\nNight's final sigil (boss 41). Other bosses' sigils craft into 16 Night Essence.")
start=old.index('TAMEABLE DRAGONS AND BEASTS' if 'TAMEABLE DRAGONS AND BEASTS' in old else 'SUMMONABLE PETS');end=old.index('ENCHANTMENTS\n',start)
old=old[:start]+'''SUMMONABLE PETS
Deep Night pets have NO natural spawn entries. Craft the reusable Pet
Summoning Tool in a regular crafting table:
  [ ][P][ ]
  [G][B][G]
  [ ][A][ ]
P = ender pearl, G = gold ingot, B = book, A = golden apple.
Level-3 clerics can also offer it for 24 emeralds and 2 ender pearls.

Right-click the tool, search or page through the list, and select a pet.
It appears nearby as an adult, with full health and already tamed to you.
The tool has a 10-second recharge and requires clear ground. Its server checks
ownership, allowed species, tool possession, collision and cooldown.
All 31 Deep Night pets are included. Vanilla tameable animals and horses,
and installed mods that use Minecraft's TamableAnimal/AbstractHorse base
classes, are included automatically. Mods with independent pet ownership
systems require a specific integration and are not guessed at.

Pet levels range from 1 to 50. Each level beyond 1 adds 2% of base maximum
health and attack damage (combat pets), reaching +98% at level 50.
Pets gain 5 XP per 20 seconds together within 32 blocks of their online,
living owner. Nearby hostile kills by the owner or their pets give 15 XP;
boss kills give 100. A level needs 100 + 25 x (current level - 1) XP.
Progress, ownership, health and modifiers persist through saving/loading.
Use the pet tool on a pet to inspect its level, XP and health.

Craft 4 Bonding Treats from Night Essence + golden carrot + cooked beef.
Treats heal owned pets by 16 health and grow babies by one minute. If a pet
is created untamed using a creative spawn egg, 3 treats can still tame it.
Sneak + empty-hand interact toggles stay/follow. Saddle an adult and interact
with an empty hand to ride it on land. Dragon flight controls are not included.
Matching tamed adults breed with golden apples; babies inherit an owner.
Drakes have elemental breath, and Star Unicorns heal nearby owners.
Ember Drake and Eclipse Wyrm now use distinct, recognizable dragon meshes;
the former blood-dragon mesh belongs to the Crimson Abomination boss.

ROAMING CREATURES AND NPCS
65 ordinary enemies use the supplied creature models, including goblins,
undead, mimics, elemental creatures, woodland horrors, piranhas and skeleton
variants. 40 neutral NPCs and woodland inhabitants can trade Night Essence
for emeralds. Land enemies spawn under the appropriate monster conditions;
aquatic enemies use water, desert creatures use arid biomes, and snow
creatures use snowy biomes. Pets and bosses are excluded from natural spawns.
Ordinary enemy player kills drop essence; boss reinforcements do not.

ASSETS AND SOUND
177 registered creatures, 25 supplied weapon models, 574 imported OGG files,
and 14 normal-table enchantments. Matching creature sounds are connected to
ambient, hurt, death, attack, movement, summon and boss-music events where
the source supplies them. Remaining supplied audio is registered for use by
resource packs and /playsound; unrelated vehicle/UI sounds are not assigned
to creatures. Generic Minecraft effects cover roles without source audio.
All supplied bone-animation clips for the selected creatures are retained.
Source-only Bedrock scripting, particles, scenery and equipment are recorded
separately in verification/model-coverage.json. See ASSET-NOTES.txt.

'''+old[end:]
old=old.replace('using selected bases, not a complete port of all the supplied add-ons.','using the supplied creature bases. Source add-on scripting and world scenery\nare separate from this Java gameplay implementation.')
(R/'README.txt').write_text(old)
(R/'ASSET-NOTES.txt').write_text(f'''THE DEEP NIGHT - SUPPLIED ASSET PROVENANCE

Gameplay: new Forge Java implementation in src/main/java/dev/deepnight.
Models, textures, animations and sounds: the user's supplied bases under
"the deep night/mobs": world, souls, aerlunerpg, bosses+, and boxxexpansion.
The distinct Ember Drake, Eclipse Wyrm and Tide Drake models come from the
already supplied local Dragonfire project's searclaw, endprowler and tidewing.
Dragonfire is not a runtime dependency. All required resources are embedded.

177 modeled creatures are registered, including all distinct living creature
meshes found in the supplied packs and additional modeled variants. Decorative
statues, buildings, armor pieces, weapons, particles, projectiles, UI geometry,
and duplicate display/death poses are classified in model-coverage.json.
Death clips from separate Bedrock death entities are merged with their parent.

All 25 supplied weapon models are imported from weapons/ and its Bloodhound,
Infernicus and Primordis subfolders. Textures are retained, references remapped,
and Java's permitted element bounds respected with compensating display scale.
Gilgamesh's invalid external DRSL parent is removed from its complete mesh.
User originals are untouched. Ember and Eclipse no longer share the grotesque
blood-dragon model; that base remains available as Crimson Abomination.

The import preserves source bone clips and normalizes keyframes for GeckoLib.
Eight canonical states map supplied clips to Java AI. All 41 bosses have
explicit one-shot attack motions and contact timelines. Seven models with
only idle clips now have authored windup/strike/recovery; two static attack
poses have been animated. Their original source clips remain preserved.
Unused optional bones are omitted from the canonical attack for each mesh.
Other missing animation states may reuse idle. Bedrock-only q.property
look controls use the Java head query; the unsupported null-coalescing target
rotation expression falls back to body rotation. Original scripts, custom
particle systems and animation controllers are not executable Java content.
Mesh scale and offset are checked with the actual Java renderer.

All 574 provided OGG files are copied and namespaced; {c['sound_events']} events include
original definitions and creature-role mappings. Matching supplied sounds
play from Java creature actions. Non-creature audio remains registered but
is not randomly played by mobs. Missing sound roles use vanilla effects.

Original artwork/audio authors retain their rights. No new ownership or
redistribution licence for third-party artwork is implied. This build is for
the user's local Minecraft/modpack project; supplied base licences still apply.

tools/expand_content.py reproduces the import (calling import_resources.py
for original weapons/enchanting first). tools/validate_resources.py checks
the output. verification/content-catalogue.json and model-coverage.json record
the full roster, source bindings and model classifications.

Implementation references:
https://docs.minecraftforge.net/en/1.20.1/datastorage/saveddata/
https://github.com/ZsoltMolnarrr/BetterCombat/blob/1.20.1/README.md
''')
pets=[x['title'] for x in creatures if x['kind']=='PET']
(R/'verification/PET-CATALOGUE.txt').write_text('DEEP NIGHT PETS\n\n'+'\n'.join(pets)+'\n\nThe in-game tool also detects supported vanilla and modded tameable animals.\n')
print('Updated README, asset notes and pet catalogue.')
