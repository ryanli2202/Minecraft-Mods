from pathlib import Path
import json,re,collections
from audit_java_imports import SOURCES
R=Path(__file__).resolve().parents[1]
rows=json.loads((R/'verification/java-import-catalogue.json').read_text(encoding='utf8'))
ogg=len(list((R/'src/main/resources/assets/deepnight/sounds').rglob('*.ogg')))
intro='''THE DEEP NIGHT 1.1.0 - JAVA CREATURE EXPANSION
Minecraft Java 1.20.1 / Forge 47.4.10 / Java 17 / GeckoLib 4

This release embeds adapted creatures from the three supplied local mods:
Age of Dragons 1.1.2 (NeoForge 1.21.1), DawnCraft Mobs 1.3.9a (Forge 1.18.2),
and Mo'Bosses 1.0.7 (NeoForge 1.21.1). Their original JARs are not dependencies.
Install the new Deep Night JAR in place of the previous Deep Night JAR.

230 imported creatures: 73 bosses, 27 pets, 107 enemies, and 23 neutral NPCs.
Combined totals: 407 creatures, 114 bosses, and 58 native summonable pets.
Vanilla and compatible installed tameable mobs still appear in the pet menu.

CAMPAIGN AND SAVE COMPATIBILITY
The original 41 boss IDs and stage numbers are unchanged. Existing active
encounters and saved progress retain their meaning. After defeating The Deep
Night at stage 41, the same Boss Summon Tool continues into the new encounters.
Players who already completed 1.0.3 start the expansion on their next summon.
Boss difficulty continues the existing ascending health/damage ladder.
All imported bosses award sigils; each new sigil crafts into 16 Night Essence.
The original weapon recipes and their original sigils remain unchanged.
Large essence rewards are split into legal stacks.

PET MENU
Imported pets arrive adult, healthy and owned by the summoner. They use the
existing cooldown, collision checks, stay/follow, taming, breeding, saddles,
ownership protection, saved XP and levels 1-50. Dragon breath has fire, frost,
water, lightning, earth, wind or spirit effects. Riding follows Deep Night's
existing land-mount controls; source-mod aerial riding controls are not ported.
Bosses never appear in the free pet menu. Bosses and pets have no natural spawn
entries. Roaming creatures use low-weight overworld spawn entries.

ADAPTATION SCOPE
This is the requested creature adaptation to Deep Night's systems. The source
mods' separate quest/progression, items, equipment systems, world structures,
dimensions and original scripted combat are not installed wholesale. Bosses
use Deep Night's three phases, telegraphs, capped damage, contact timelines,
co-op participation and saved encounter authority. Some source phase/form
variants are separate named encounters. Hostile and friendly dragon variants
retain separate IDs. The DawnCraft Gryphon is adapted into a bonded pet.

Assets retain their original ownership. Java mesh and keyframe exports are
converted to GeckoLib 4. Native GeckoLib geometry and clips are imported with
syntax repairs. Original clips are retained under source.* alongside runtime
idle, walk, fly, attack, sit, hurt, death and spawn aliases. Missing locomotion
gets simple limb motion. Boss primary attacks receive a committed anticipation,
contact and recovery curve. Extra source emissive/transparent overlays and
equipment layers are not all reproduced; canonical source skins are used.
Spiritnick's visible armor mesh is combined; Giant Skeleton uses its visible
source layer. Bare '-' animation placeholders are neutralized; negative cube
extents are normalized; absent runtime bones are filtered. Original input
folders and JARs are unchanged.

Projectile, spell, hitbox, corpse, spawner and invisible controller entities
are excluded from summoners. The explicit selection is in
tools/java_creature_selection.json. The complete selected provenance and
per-creature conversion are in verification/java-import-catalogue.json.

BUILD
gradlew.bat build --offline
gradlew.bat runGameTestServer --offline
gradlew.bat runClient -PsmokeTest --offline
Python: tools/validate_java_imports.py
Re-import (requires the supplied sibling deobfuscated source folders):
  tools/import_java_creatures.py
The imported source/resources are committed to this project; a normal Java
build does not need the original source-mod trees.

'''
lines=[intro,'NEW BOSS LADDER\nRank | Boss | Source | Total HP | Base damage']
labels={'aod':'Age of Dragons','dawn':'DawnCraft', 'mobosses':"Mo'Bosses"}
for r in rows:
    if r['kind']=='BOSS':lines.append(f"{r['boss_stage']+1:3} | {r['title']} | {labels[r['pack']]} | {r['health']} | {r['damage']}")
for kind,title in [('PET','NEW PET MENU ENTRIES'),('MOB','NEW ROAMING ENEMIES'),('PASSIVE','NEW NEUTRAL NPCS')]:
    lines.append('\n'+title)
    for r in rows:
        if r['kind']==kind:lines.append(f"{r['title']} | {labels[r['pack']]} | deepnight:{r['id']}")
(R/'IMPORTED-CREATURES.txt').write_text('\n'.join(lines)+'\n',encoding='utf8')
p=R/'README.txt';text=p.read_text(encoding='utf8')
text=text.replace('THE DEEP NIGHT 1.0.3','THE DEEP NIGHT 1.1.0').replace('deep-night-forge-1.20.1-1.0.3.jar','deep-night-forge-1.20.1-1.1.0.jar')
notice='''1.1.0 adds 230 adapted creatures from Age of Dragons, DawnCraft Mobs and
Mo'Bosses. The campaign now has 114 bosses and the native pet menu has 58 pets.
The original 41 stages and saved encounters retain their meaning; new bosses
follow the old campaign. See IMPORTED-CREATURES.txt for the full new roster,
source versions, adaptation details, and limitations.

'''
if notice not in text:text=text.replace('1.0.3 fixes attack',notice+'1.0.3 fixes attack')
text=text.replace('THE 41 BOSSES','THE ORIGINAL 41 BOSSES (NEW STAGES: SEE IMPORTED-CREATURES.txt)')
text=text.replace('All 31 Deep Night pets','All 58 Deep Night pets')
text=text.replace('65 ordinary enemies','172 ordinary enemies').replace('40 neutral NPCs','63 neutral NPCs')
text=text.replace('177 registered creatures, 25 supplied weapon models, 574 imported OGG files,',f'407 registered creatures, 25 supplied weapon models, {ogg} imported OGG files,')
p.write_text(text,encoding='utf8')
p=R/'ASSET-NOTES.txt';text=p.read_text(encoding='utf8')
addition='''\n1.1.0 JAVA MOD ADAPTATIONS
230 additional creatures from the user-supplied Age of Dragons 1.1.2,
DawnCraft Mobs 1.3.9a (Simple Mobs), and Mo'Bosses 1.0.7. These inputs retain
their original authorship and asset rights. The reconstructed reference Java
files are read as model/animation data; foreign-loader binaries are not bundled.
See IMPORTED-CREATURES.txt and verification/java-import-catalogue.json for
scope, transformations, source entities, textures and provenance hashes.
The original mod metadata is preserved in import-provenance/ in the JAR.
'''
if '1.1.0 JAVA MOD ADAPTATIONS' not in text:p.write_text(text+addition,encoding='utf8')
for key,(folder,namespace,package) in SOURCES.items():
    source=R.parent/'deobfuscated'/folder/'src/main/resources/META-INF'
    for name in ['mods.toml','neoforge.mods.toml','LICENSE','LICENSE.txt']:
        if (source/name).exists():
            target=R/'src/main/resources/import-provenance'/key/name;target.parent.mkdir(parents=True,exist_ok=True);target.write_bytes((source/name).read_bytes())
print('Wrote expansion guide and provenance; OGG files:',ogg)
