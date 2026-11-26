THE DEEP NIGHT GUIDE
Forge 1.20.1 - Version 1.2.0

INSTALL
Copy the-deep-night-guide-forge-1.20.1-1.2.0.jar into your modpack's mods folder.
Updating from 1.0.0 or 1.1.0: replace the old guide JAR; do not keep both installed.
Your existing taming, weapon, armour and boss progress is preserved.
Use Minecraft Java 1.20.1, Java 17 and Forge 47.3.25 or later in the 47.x series.
Launch the pack, enter a world and press J. The key is rebindable in Options >
Controls > Key Binds > The Deep Night Guide. If another mod uses J, rebind one.
This is a client guide: a server does not need to install it. No required content
mod, GeckoLib or Patchouli dependency. Keep your pack's existing JEI for its
optional All recipes / Uses buttons. Those open JEI's full custom recipe displays.

USE
- Categories: Ores, Armour, Weapons, Tools, Items / blocks, Mobs, Pets, Bosses,
  World & lore, and All content.
- Search names, registry IDs and mod names; @dragonfire limits results by mod.
  Multiple search words must all match. Ctrl+F focuses the search field.
- Click an entry to read its page. Every entry has an item/spawn-egg icon; entities
  without spawn eggs and blocks without inventory items use a generic icon.
- Scroll over the middle column to browse entries. Scroll over the right page
  to read the rest. Page Up/Down scroll the page; arrow keys move through entries.
- Click ingredients to open their pages; Back returns to the previous entry.
- Recipe arrows cycle all exposed result recipes. Ingredient alternatives cycle.
- Click an ID to copy it. J or Escape closes the guide.
- Reload notes re-reads installed mod data and your custom entries file.

COLLECTION CHECKLISTS
Separate checklists track taming/recruitment, weapons, armour and boss defeats. The filter
cycles All / Missing / Done. Entries can be checked and unchecked manually.
Checklist totals respect the current search and mod filter. Search @deepnight
to see progress against just the Deep Night content mod's creatures or equipment.
After opening the guide, inventory weapons/armour and nearby loaded pets with
standard Minecraft ownership are recorded automatically every two seconds.
Villagers++ recruitment and other custom ownership systems can be marked manually.
Unticking suppresses automatic completion until you check that entry again.
Progress is per player and per singleplayer save or multiplayer server address,
stored under config/deepnightguide/progress/. Server world resets at the same
address retain that server's checklist; back up/remove its progress file to reset.
Progress is local to this client and does not sync between computers.

BOSSES AND DEFEAT CHECKLIST
Bosses appear in both the Bosses category and Boss checklist, with their existing
icons, habitats, loot and manual notes. Detection uses live forge/c/minecraft boss
tags, vanilla bosses, entity classes with boss bars, 32 explicit pack encounters
and all 41 Deep Night campaign bosses. Mowzie's Mobs uses its hasBossBar predicate so its
ordinary creatures are not misclassified just for sharing a base-class boss field.
Deep Night uses its Species boss predicate for the same reason: ordinary enemies
and neutral NPCs share NightMob's boss-bar field but are not campaign bosses.
The FantasyCraft king dragon, Surtr and five Hydra variants are deliberately
included as major encounters, even though the Java port has no boss bars for them.
Only loaded entities appear; notes for absent mods do not create ghost entries.

Defeat progress uses separate boss:<namespace>:<entity> keys. Taming a creature
never marks it defeated. For bosses that can also be tamed, each progress state
has its own control. The Boss checklist uses All / Missing / Done filters.
The guide requests your vanilla entity-kill statistics when opened and every
10 seconds while open, then records credited kills. Earlier recorded kills count.
Use the manual checkbox for scripted victories, shared/team victories, or mods
that do not award normal kill statistics. Unticking suppresses automatic checking.
These checks track entity types, not unique named/NBT variants or individual
phases built from the same type. Some custom bosses may need explicit overrides.

WORLD AND LORE
Eight original Deep Night chapters develop the Ashen Covenant, Dawnstead,
crossings between worlds, dragons, elemental magic, great encounters and the
world you choose to rebuild. These are original setting fiction inspired by the
existing campaign; they are not official lore copied from the installed mods.
Named settlements and narrators are story framing, not automatically spawned NPCs.

The dimension atlas lists exactly the realms registered by the current server.
It includes 17 authored templates covering the dimension IDs found in the supplied
pack and vanilla: Overworld, Nether, End, Afterdark, Gransys, the Sift, the Sealed
Realm, Crypt, Sepulchra, Dimensional Dungeons and its builder realm, DungeonZ,
Iron's pocket realm, the Scorched Kingdom, Distortion, Isengard and the Real End.
The 17 count includes the three vanilla dimensions. Uninstalled dimensions do
not appear. Other dimensions receive a generic explorer's entry and their real ID.
Technical dimensions are included and labelled; registration does not guarantee
a Survival entrance. Field notes are separated from the original narrative.

To edit lore, copy lore.example.json to config/deepnightguide/lore.json. Its
chapters and dimensions objects override individual entries from the bundled
chronicle. A chapter value of null hides that chapter. Dimension templates apply
only if the server registers that dimension. Click Reload notes after editing.

COVERAGE
The runtime catalog indexes every registered non-air item, blocks without items,
and every registered non-player entity. That includes other people's content mods,
including your custom mods. Technical entities and creative-only items remain
visible so the catalog does not silently omit registry entries. A registered item
is one catalog entry; arbitrary NBT variants are not separate checklist targets.
Categories use item classes, tags and naming conventions, with custom overrides.

Bundled, source-verified notes cover the supplied custom Java ports:
- Deep Night 1.0.0 / 1.0.1: all 177 creatures (41 campaign bosses, 31 pets, 65 enemies,
  40 neutral NPCs), all 25 weapons, 41 sigils and 5 support items. Boss pages
  explain their campaign order, prerequisites, summoning, base stats, attack
  patterns and rewards. Pet pages cover summoning, taming, breeding, riding,
  care and levelling. Weapon pages include tier, required boss and ingredients.
  These 248 notes use the actual deepnight namespace. Bosses use their own
  sigil icons; other creatures use their spawn eggs. Pets appear in both Pets
  and the Taming checklist. Neutral NPCs trade 1 Emerald for 2 Night Essence.
  The Deep Night content mod itself requires Forge 47.4+ and GeckoLib; keep
  those installed as required by that mod. They are not guide dependencies.
- Dragonfire 1.0.1: all 27 breeds, habitats, taming, riding, scales and breeding.
- FantasyCraft 1.0.1: all 69 creature types and the 18 tameable species, foods,
  habitats, care and riding information.
- Elemental Staffs 1.0.1: six elementals, essence acquisition and all six weapons.
- Villagers++ source: all 15 recruit roles, invitations, landmarks and management.
Absent mods do not create ghost catalog entries. These notes describe the supplied
ports, not unrelated mods that use similar names.

Other mods are indexed automatically. The guide reads shipped loot tables, ore
features, Forge spawn modifiers, taming tags/advancements, compatible Patchouli
entries and Alex's Mobs' installed Animal Dictionary. Manuals are read from the
installed mods; their artwork and manuals are not copied into this JAR.
In singleplayer it also reads the current world's final biome spawn tables.
These identify eligible biomes, not guaranteed nearby mobs or exact coordinates.

WHAT CANNOT BE INFERRED AUTOMATICALLY
All registry content can be listed, but not every mod exposes every gameplay rule.
Missing bespoke taming, ritual, trading and scripted acquisition steps are marked
as undocumented rather than guessed. Food used for breeding is not treated as
proof of taming food. Ownership-class detection may need overrides for special mobs.
The guide directly shows recipes with static item results from the world's recipe
manager. JEI supplies broader machine/fluid/brewing/special recipe support when
the relevant mods register integrations with it. Arbitrary scripts cannot be
reverse-engineered into a complete guide automatically.

Loot and ore notes describe the installed JARs' data. Datapacks, LootJS, KubeJS and
server configuration can change these; they are not guaranteed drop rates or a
live simulation. Current-world biome tables are available in singleplayer only.
This guide lists the requested content categories, not every advancement, biome,
structure, enchantment or spell as a separate section.

ADD OR CORRECT NOTES
Copy entries.example.json to config/deepnightguide/entries.json in your instance,
replace the example IDs and instructions, then click Reload notes. Keys begin
with item: or entity: followed by the full registry ID. A section with the same
heading replaces the imported section. New headings append to the page.
Set tameable explicitly to true or false to override inferred tameability.
Set boss explicitly to true or false to include/exclude an encounter; false wins
over automatic boss detection. Existing notes and taming fields still work.
An optional icon field specifies an installed item ID, such as deepnight:ashen_knight_sigil.
Invalid or absent icon items fall back to the entry's ordinary icon.
Valid categories: ORES, ARMOUR, WEAPONS, TOOLS, ITEMS, MOBS, BOSSES. Classification overrides
add categories. Write instructions that match your actual modpack configuration.
The Pets browser follows tameable, just like the Taming checklist.

BUILD
The source ZIP includes the Gradle wrapper and generated custom notes.
With Java 17: gradlew.bat build
First-time dependencies require internet. JEI's public API is compile-only; JEI
is optional at runtime and is not bundled. The build outputs build/libs/*.jar.
For an offline build against an installed JEI JAR:
  gradlew.bat build "-PjeiJar=ABSOLUTE_PATH_TO_JEI_JAR" --offline

TESTING
An isolated Forge client test exercises the real keybind, catalog, classification,
live recipes, multiword/mod searches, taming notes, progress persistence, manual
untick suppression, world isolation and rendering. Additional integration checks
load Dragonfire, FantasyCraft, Elemental Staffs and JEI, verifying all 27 dragon
breeds, exactly 18 tameable FantasyCraft species, Earth Glove categorization,
custom egg icons and the JEI recipe handoff. A data-only scan reads the installed
full pack's JARs. This is not a full-pack gameplay or multiplayer certification.
Villagers++ notes were checked against its source; its gameplay was not loaded in
the integration client. See verification/ for test output and screenshots.
QA classes are excluded from the release JAR.

Version 1.1.0 adds runtime checks for vanilla/custom boss grouping, ordinary-mob
exclusion, separate boss/taming keys, server-to-client kill-stat synchronization,
automatic defeat completion, Missing/Done filters and manual suppression. Lore
checks verify eight chapters, one page per live dimension, unknown-dimension
fallbacks, absent-mod filtering and rendering at GUI scales 2 and 4.

Version 1.2.0 adds integration checks against the actual Deep Night 1.0.0 / 1.0.1 JARs:
exact 41/31/105/25 category counts, every species' live boss/pet classification,
all 25 live weapon recipes, sigil and spawn-egg icons, summoning/taming notes,
and automatic pet, weapon and boss tracking. The client also loads Dragonfire,
FantasyCraft, Elemental Staffs and JEI to check compatibility with their pages.
Deep Night boss checklist ticks do not change the server's campaign progression.
Shared campaign victories may need a manual tick if no personal kill was credited.
Regenerate the bundled notes from the supplied content source with:
  python tools/build_deepnight_entries.py PATH_TO_DEEP_NIGHT_SOURCE
Run the isolated integration client with Java 17 and local dependency JARs:
  gradlew.bat runClient -PsmokeTest -PtestContent -PtestDeepNight "-PforgeVersion=47.4.10" "-PpackMods=PATH_TO_PACK_MODS"
The final Deep Night 1.0.1 integration run also uses -Pgecko484 with the local
GeckoLib 4.8.4 compatibility JAR. See build.gradle for the local test directories.

References for development:
https://docs.minecraftforge.net/en/1.20.1/misc/keymappings/
https://docs.minecraftforge.net/en/1.20.1/gui/screens/
https://github.com/mezz/JustEnoughItems
