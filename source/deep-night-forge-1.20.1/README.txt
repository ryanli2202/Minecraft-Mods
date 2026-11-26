THE DEEP NIGHT 1.4.1 - DEMON KING ELEMENTAL ATTACKS

Demon King combo 1 strikes with fire, frost, then lightning. Combo 2 strikes
with lightning, then fire. Each strike has a twelve-tick elemental wind-up,
a matching particle arc and an impact sound on the original contact frame.
Successful fire hits burn for three seconds; frost hits slow for three
seconds; lightning hits add knockback. Misses do not apply status effects.
Existing direct damage, attack reach, contact frames and recovery remain.

Summoning produces a soul-fire ring and spell sound; minions arrive in
soul-fire bursts. Lightning marks its impact area with electric sparks,
then flashes with a particle burst and impact/thunder sounds. These effects
do not place fire blocks or destroy terrain. Lightning ignores Demon King
minions. All 1.4.0 roster changes and the Architect's Deity summon remain.

Replace the old Deep Night jar with 1.4.1 on client and server. Requires
Forge 1.20.1 and GeckoLib 4.8.4.

THE DEEP NIGHT 1.4.0 - FORGE 1.20.1

Boss order:
1. Corrupted Ogre
2. Orc Champion
3. Divine Guardian
4. Martian
5. Lord of Sorcery, the Skeleton Overlord
6. Dragon Lord
7. Demon King
8. The Challenger's First Form
9. The First Architect
10. The Challenger's Final Form

All other standalone bosses are absent from the summon menu. Elemental Deity
remains as the First Architect's summoned follow-up, with its original combat
and 1,350 health, but has no standalone campaign slot, sigil or spawn egg.
Other excluded bosses are unregistered.
The lightning, ice and fire dragons are now tameable, rideable companions,
available through the Pet Summoning Tool or their creative spawn eggs.
Use three Bonding Treats to tame a wild dragon, a saddle to ride, and sneak
+ empty-hand interaction to toggle sit/follow. They gain pet levels and use
their matching elemental breath. Pet dragons have 120 base health, 10 melee
damage and 8 armor. Original models are scaled to a three-block pet height.
Old saved dragon entities convert to untamed pets with pet attributes.

The ten retained boss health tiers rise in the new order, from 720 to 11,100.
Triple boss damage is retained. Each of the ten weapon tiers now uses the
corresponding boss sigil. Removed bosses' sigil recipes are removed.
Saved defeats of retained bosses stay credited by ID; removed active boss
encounters are cleared. Original combat helpers needed by retained bosses
remain; internal legacy source symbols are not registered as bosses.

Install the 1.4.0 jar on both client and server, replacing the older Deep Night
jar. Forge 47.4.x, Java 17 and GeckoLib 4.8.4 are required. Back up existing
worlds before upgrading: removed bosses and sigils are no longer registered.

Build: .\gradlew.bat build
Server regression tests: .\gradlew.bat runGameTestServer
Automated client render check: .\gradlew.bat runClient -PsmokeTest

Previous release notes

THE DEEP NIGHT 1.3.5 - FORGE 1.20.1

Fixes the Boss Summon Tool silently refusing to open when its ItemStack has
an empty NBT tag, custom name, enchantment, or mod-added metadata. The server
and client now check the actual item identity in all inventory slots, including the
offhand, rather than requiring an exact untagged stack. Unrelated renamed
items cannot substitute for the tool. All 1.3.4 balance changes are retained.

THE DEEP NIGHT 1.3.4 - FORGE 1.20.1

All 22 bosses now have at least triple their previous configured health.
Health strictly increases in campaign order (720 to 11,100 HP). Dragon Lord
follows Fire Dragon. The final four are Demon King (10,200), The Challenger's
First Form (10,500), The First Architect (10,800), and The Challenger's Final
Form (11,100). Other bosses retain their relative progression order.
Boss melee and attributed special/projectile damage is multiplied by three.
The final weapon tier now requires the final Challenger's sigil.
Saved boss health is upgraded proportionally; defeated boss IDs stay credited.
Creative /kill now bypasses native boss shields and completes the encounter.
Validation: build succeeded and all 56 required server GameTests passed,
including all 22 boss lifecycles, health migration, damage scaling, and
progression. Interactive client combat and the full modpack were not tested.
Install the 1.3.4 jar on client and server, replacing the old Deep Night jar.

THE DEEP NIGHT 1.3.3 - FORGE 1.20.1

Dragon Lord's six auxiliary hitboxes now forward hits to their owning boss.
The boss owns health, armor, damage immunity, cooldowns and kill credit;
parts no longer lose independent health or damage every nearby Dragon Lord.
Ownership persists in saves, with automatic adoption of older hitboxes.
The incoming-hit cooldown expires during stun and only starts on an accepted
hit. Native arrow/potion immunity and attack-phase invulnerability remain.

Replace the previous Deep Night jar on client and server; keep one version.

THE DEEP NIGHT 1.3.3 - FORGE 1.20.1

1.3.3 fixes Mo' Bosses melee damage running at half the animation speed.
Shared attacks now advance every server tick, so their hit frames and
recovery match the animation durations. Forest Guardian's magic can run
while its melee AI pursues a target. Orc Champion's shoulder bash now
finishes its initial cooldown, damages its target and recharges normally.

Validation: all 51 server GameTests passed, including all 22 boss lifecycle
tests, 16 shared melee variants with exact damage-frame and out-of-range
checks, Forest Guardian's aura damage, and Orc Champion's shoulder bash.
Tests use Forge 47.4.10 and GeckoLib 4.8.4. The user's full modpack and
interactive client combat have not been tested for this patch.
Replace the older Deep Night jar on both client and server with 1.3.3.
Keep only one Deep Night jar; GeckoLib 4.8.4 remains required.

1.3.2 fixes Elemental Deity's delayed victory crash. The original Elemental
Deity and DawnCraft Herobrine advancements are included, and optional source
advancement awards safely tolerate a missing or disabled advancement.
DawnCraft Herobrine's missing Accursed Sword trophy item is also restored.
Skeleton Lord's original aggression callback now uses Forge 1.20.1's target
event, so it leaves its idle damage resistance and engages its opponent.
Its malformed invulnerability commands are also repaired.
Dragon Lord releases dive invulnerability when an early collision starts
the follow-up attack. Ent's corpse is removed after its native staff reward
appears. All retained DawnCraft native bars update during NoAI/stuns; Divine
Guardian and Divine Knight receive the campaign bar their sources lacked.

See BOSS-AUDIT-1.3.2.txt for the per-boss combat, defeat, reward, delayed
callback and client rendering results. Automated tests use isolated arenas
with controlled survival targets and multiple health phases. They are not
an exhaustive test of every attack combination or the supplied full modpack.
The 1.2.2 sound repairs and 1.3.1 creative replacement menu are retained.

1.3.1 lets creative players summon any boss directly from the menu even
while an encounter is active. Select a boss and choose Replace active boss
to switch encounters without awarding defeat credit. An invalid selection
or unsuitable arena leaves the existing fight intact. Survival players
still finish or end the current encounter before summoning another.

Dragon Lord's health bar now updates during its NoAI attack phases and
stuns. Damage, healing and death are reflected without waiting for its AI
to resume. The original attacks and damage rules are retained, along with
all 1.2.2 sound fixes.

1.3.0 adds the Boss Summon Tool menu and fixes creative-mode progression.
Use the tool to browse all 22 bosses, search by name, view their native 3D
models, check defeated/next/locked status, and summon your next opponent.
Completed bosses can be resummoned. The summoner, a creative player or an
operator can end an active encounter without earning defeat credit.

Creative players unlock the tool immediately and keep encounters active.
Defeating the current campaign boss in creative, including with /kill while
the summoner is nearby, advances the campaign. Future creative test summons
and rematches do not advance progress or repeat first-clear sigil/essence
rewards. Survival still requires the Ender Dragon unlock and boss order.
Existing campaign progress is preserved. This release includes all 1.2.2
sound protections and the source game-rule initialization fix below.

1.2.2 routes all imported sound-ID lookups through SafeSounds.resolve.
Unknown or malformed IDs return Minecraft's silent sound; each bad ID is
logged once. Empty IDs remain intentionally silent. Existing valid sounds
are unchanged. Replace the previous Deep Night jar; keep only one version.
Also registers the bundled Divine Dragon beam sound and initializes the
source boss respawn game rule before worlds load, preventing death-handler
crashes caused by a missing rule value.

New boss code must use SafeSounds.resolve(String) for dynamic sound IDs.
compileJava runs verifySoundLookups to reject direct nullable registry
lookups and missing bundled sound definitions. Server GameTests check all
bundled sound registrations, invalid-ID playback, and the Dragon Lord attack
frame from the original crash report. Run build and runGameTestServer before
shipping new bosses. This protects against missing-sound crashes, not every
possible combat bug.

1.2.1 fixed the Dragon Lord thunder spear attack crash caused by a misspelled
sound registry lookup; this fix is also included in 1.3.0.

Install on the client and server with Forge 47.4.x, Java 17 and GeckoLib
4.8.4 for Forge 1.20.1. The install ZIP includes both required mod jars.
Replace the old Deep Night jar; keep only one version of each mod installed.
Back up existing worlds before this roster-changing upgrade. Removed entities
and items cannot be restored by the new build. Completed retained bosses are
migrated by ID; removed active encounters no longer block the campaign.

The campaign has exactly 22 bosses: 15 DawnCraft and 7 Mo'Bosses.
All other campaign bosses and all Age of Dragons content are removed.
DawnCraft entities driven by Epic Fight animation patches, plus Gael,
Morgott and the twin summoner, are excluded. Existing unrelated non-boss
Deep Night creatures, pets, weapons and enchantments remain.

Bosses instantiate the ported source entity classes, using their own combat
procedures/AI, contact timing, hitboxes, multipart helpers, movement, phases,
projectiles, particles, sounds, models, animations and renderers. The shared
Deep Night boss attack profiles, health phases and per-hit cap are removed.
Campaign unlocking, summoning, rewards and defeat credit remain Deep Night's.

The original sources target different Minecraft/loader/GeckoLib versions.
Compatibility changes are described in ASSET-NOTES.txt. Epic Fight is not
required: a local effect system supplies stun, freeze, tremor and source
impact/partial armor-negation handling. Do not install the original DawnCraft
Mobs or Mo'Bosses jars alongside this embedded port (they share asset IDs).

Defeat the Ender Dragon to obtain the Boss Summon Tool. Use it in a broad,
flat, open arena; allow at least 20 blocks overhead for the tallest bosses.
Use or sneak + use opens the boss menu. /deepnight reclaim restores the tool;
/deepnight abandon ends a stuck fight without credit, as does the menu's
End active encounter control.

Build: .\gradlew.bat build
Server verification: .\gradlew.bat runGameTestServer
Client verification: .\gradlew.bat runClient -PsmokeTest
Boss menu verification: .\gradlew.bat runClient -PbossMenuTest
Tests and the automatic client gallery are excluded from the release jar.
The source archive contains the final port; historical import generators
are not part of the supported build process.

Lost Eye Ogre remains the Corrupted Ogre's native combat helper; it is not a
standalone encounter and cannot block campaign progression.
