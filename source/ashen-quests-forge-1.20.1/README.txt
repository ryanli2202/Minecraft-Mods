THE ASHEN COVENANT: CHRONICLES 1.3.0
NPC-driven quest mod for Minecraft Java 1.20.1 / Forge 47.4.10+ / Java 17

INSTALL
Copy BOTH updated JARs into the modpack's mods folder:
  ashen-quests-forge-1.20.1-1.3.0.jar
  thedeepnightnpcs-forge-1.20.1-1.4.0.jar
Replace older versions of these two mods; do not keep duplicate versions.
For multiplayer, install the same two jars on the server and every client.
Speak to Eldara and choose Main quests to accept and turn in all main quests.
Press the minus/hyphen key (-, beside 0) to open the tracking journal. Rebind it under
Options > Controls > Key Binds > The Ashen Covenant. /ashenquests also opens it.
If another mod uses -, change that conflicting binding in Controls.

This release requires The Deep Night NPCs 1.4.0. It runs without Boundless,
FTB Quests, or a separate quest library.
It includes the quests inside its jar. No config questpack installation is needed.
Disable the old Ashen Covenant and Deep Night packs in Boundless if you leave
Boundless installed, to avoid running two copies of the campaign. Boundless can
instead be removed if your pack does not otherwise need it. This download does
not remove mods, change options, or edit any existing world.

CONTENT
573 quests: 276 main story quests from Eldara and 297 NPC assignments.
Books I (96), II (48) and III (36) retain their original IDs and tasks.
Book IV: The Roads That Remember adds 48 quests about an excluded river
community, flood repairs, a relief fleet, and the struggle for a shared compact.
Book V: The Winter of Unwritten Names adds 48 quests about northern villages,
winter passage, fuel shortages, an observatory, a refuge, and a new covenant.
Book IV starts after hc_home06, the Book III finale. Book V follows Book IV.
Every main quest is required on the route to the Book V finale. The first twelve NPC eye trials are required before the Spire portal preparation quest;
other side quests remain optional. Two investigation branches per chapter reunite before its end.
Six decisions total change saved aftermath text; they do not exclude quests.
The stories describe settlements, repairs, expeditions and community decisions;
these are narrative events, not automatically generated structures or terrain edits.

76 new side quests open after ac_m06 (The First Hearth): eight each from Rowan,
Sera, Mara, Elian, Iona, Tamsin, Fen and Ysolde; six each from Caelen and Jorge.
Each character's chain follows a smaller personal or community project and sends
you to other characters for advice, reports and practical arrangements.
48 existing quests also gain a conversation objective at the end of their tasks.
There are 313 individually written conversation responses across 220 quests.
22 repeatable relief contracts and all older side routes remain available.
Every quest guarantees emeralds and retains its original available bonus items.

NPC QUEST GIVERS AND CONVERSATIONS
Right-click Eldara > Main quests to accept and turn in every main-story quest.
For side quests, right-click the named giver > Covenant quests. Caelen's random
hunts and Jorge's trade services remain separate options on their dialogue wheels.
The conversation journal shows the selected giver's assignments; the hotkey
journal shows every quest and can track one on the HUD.

For a Discuss objective:
1. Accept the quest from its giver.
2. Complete the earlier listed material, crafting, combat or travel objectives.
3. Right-click the named NPC > Discuss a quest > the assignment's title.
4. Read the character's response. The conversation is recorded for this quest.
5. Visit the next named NPC if required, then return to the giver for the reward.
The topic appears only while the accepted quest is ready for that conversation.
If absent, check earlier objectives and the character's identity in the journal.
Normal lore dialogue and previous conversations cannot substitute for a quest topic.
Each conversation belongs to one accepted quest. Multi-NPC reports must be made
in order. Renewed repeatable quests require a fresh conversation if they contain one.
Recorded responses remain readable beneath their objectives in the journal.
Keep required collect/donation items until turn-in; donations are consumed there.
Journal phrases and decisions are entered at turn-in after discussions.

Acceptance, discussion and rewards are validated on the server. You must remain
near the live NPC with line of sight. Minus (-) and /ashenquests track progress;
they cannot accept or reward quests. Close and reopen an NPC conversation if its
session expires. Repeatable contracts must be accepted again after their cooldown.

NPCs are placed with the creative spawn eggs or /summon, as in the NPC mod.
They do not generate naturally or appear automatically in existing worlds.
For example: /summon thedeepnightnpcs:eldara ~ ~ ~
Use rowan, sera, mara, elian, iona, tamsin, fen or ysolde for the other characters.
All eight companions use the user's supplied skins unchanged; six use slim arms.

UPGRADING EXISTING WORLDS
Existing completions, standing, choices, acceptance, tracked quests and objective
counters are preserved. New conversations on unfinished quests must be completed.
Completed quests are grandfathered and do not need their added conversations.
The Spire portal quest now requires a Spire Guard conversation instead of Eye Spy.
The Last Watch now counts zombie kills instead of stronghold silverfish.
Other existing objective indices remain unchanged.
Quests that were never accepted still require acceptance from their giver.
Previously completed one-time quests are never reset. Existing repeatable
cooldowns remain in force, and each renewed contract requires acceptance.

USING THE JOURNAL
Search titles, stories, objectives or quest IDs. Click the chapter selector to
advance; right-click it to go back. The filter cycles All, Available, Ready to turn in,
Completed and Missing content. Scroll over the list to browse quests. Scroll
over the story to read all objectives, rewards, prerequisites and aftermath.
Track one quest for a HUD checklist. It advances to unfinished steps and names the next NPC. Track again to remove it.
Accept and turn in quests through their giver to unlock dependent quests.
For a journal phrase, type the phrase shown during the NPC quest conversation. Phrases ignore case and surrounding spaces.
For a decision, cycle the response button before turning in. Your choice is saved.
The Factions tab shows Dawnstead, Hearthwrights, Archivists, Free Watch and
Wayfarers. First completions grant standing: Ally 25, Trusted 100, Steward 250,
Luminary 500. Standing records reputation; it does not gate equipment or quests.
Repeat claims grant no additional standing.

OBJECTIVE RULES
Hold/collect: items must be in your inventory, equipped armor or offhand at claim.
They are not consumed. Items in backpacks/Curios/modded containers must be moved
to the ordinary inventory for detection.
Donate/submit: the listed amounts are consumed only when the claim succeeds.
Defeat: reads your lifetime Minecraft entity-killed statistics, including existing
kills in this world. Normal vanilla kill credit rules apply, including modded
bosses that award normal kill statistics. Killing an untracked summoned minion
does not count as defeating its boss.
Advancement: reads the real server advancement progress, including past completion.
Effect: the exact status effect must be active when claiming.
Craft/smelt: quantities accumulate from normal Forge crafting and furnace-output
events after this mod is installed. Collect smelted output yourself; hopper-only
extraction does not count. Automation or modded workstations must emit Forge's
normal events to count. Simply finding or buying an item is not crafting it.
Visit/biome: records discoveries every second after installation. Stay briefly
in the destination. Earlier visits cannot be inferred from an old save.
Older interaction objectives: right-click the specified entity; these retain their
lifetime interaction counters. New Discuss objectives use quest-specific topics
after acceptance, as described above, and never use lifetime interaction counts.
Tame: records a successful standard Forge taming event after installation.
New event counters are lifetime totals from installation, including events before
a particular quest unlocks. Imported kill objectives retain lifetime semantics.
The engine also supports item-fishing counters for added datapack quests.

REWARDS AND SAVES
Server validates prerequisites, objectives, story responses and rewards.
Full inventory: make space and claim again. No donations or rewards are lost.
Duplicate claims and replayed network requests cannot duplicate a reward.
Different players have separate progress and rewards; this release has no parties
or automatic shared completion. Progress survives death, dimension changes and
world restarts. Data is stored per UUID in world/data/ashenquests_journals.dat.
Cooldowns use saved world game time; /time set does not bypass them, and time
does not pass while the server is stopped.
Boundless progress is not imported automatically. Chronicles starts a fresh
journal; existing Minecraft kill statistics and advancements can still count.
Old Boundless files and saved progress are left intact.

MODPACK COMPATIBILITY
This is written for the supplied fantasycraft 2.0 questpacks and mod references.
The quest engine requires Forge and The Deep Night NPCs 1.3.0. Specific story objectives still
need their original content mods, including the pack's magic, boss and dragon mods.
Missing objectives display the exact missing ID and block dependent quests with
an explanation. They are never silently auto-completed. An absent OPTIONAL bonus
reward item is omitted, with a note in Rewards; emeralds are always paid.
The Emerald Covenant shop works with these ordinary emerald rewards without a
hard dependency on either mod.
Operators can run /ashenquests audit to count quests requiring missing content.

CUSTOMIZING
Campaign is data/ashenquests/ashen_quests/campaign.json inside the jar.
A normal data pack can override that exact path with a complete edited campaign.
Additional files under data/<namespace>/ashen_quests/*.json can add unique IDs:
{"quests": [ ... quest definitions ... ]}. Preserve IDs to preserve save progress.
The optional giver field accepts eldara, rowan, sera, mara, elian, iona, tamsin,
fen, ysolde, caelen or jorge. Old datapacks without giver
default to Eldara. Use /reload. Validation rejects duplicate IDs, cycles, unknown prerequisites,
unsupported objective types, unsafe quantities, and quests without emeralds.
An invalid reload retains the last valid campaign and logs the problem.
For a conversation objective use type="talk", target="thedeepnightnpcs:<kind>",
count=1, a label, and nonempty dialogue text (at most 2000 characters). Targets
include the eleven named characters and spire_guard. Place objectives in discussion
order. Preserve both quest IDs and existing objective indices when updating saves.
Examples and all authored definitions are included in the source archive.

BUILD AND VERIFICATION
Keep both source project folders beside each other for the combined build.
Java 17; Gradle wrapper 8.8; ForgeGradle 6.0.35; tested with Forge 47.4.10.
python tools/build_campaign.py
python tools/audit_content.py
python tools/audit_expansion.py
gradlew.bat build
gradlew.bat runGameTestServer
gradlew.bat runClient -PsmokeTest
QA classes and the test structure are excluded from the release jar.
The smoke test creates its own temporary world in this project's run directory.
Reports and screenshots are in verification. Core behavior tested on a dedicated
Forge test server and a real client/integrated server. The full 355-mod profile
has not been launched as part of this verification; static modpack references
were checked separately. No real playthrough was used for testing.

SOURCE AND CREDIT
The original quest definitions come from the user's local Boundless questpacks:
The Ashen Covenant and The Deep Night. Their original objectives, prerequisites, item
bonus rewards and IDs are preserved; selected quests append conversations. This is an independent Forge implementation;
the Boundless mod and its Java implementation are not bundled or required.
New campaign writing and runtime are supplied for Ryan's modpack.
Technical reference: https://docs.minecraftforge.net/en/1.20.1/networking/simpleimpl/
Key mappings: https://docs.minecraftforge.net/en/1.20.1/misc/keymappings/




END REMASTERED EYE PROGRESSION (1.3.0)
An End portal frame already exists at the Spire. No stronghold search is required.
Start The Lost Eyes | NPC Trials with Sera after The First Hearth (ac_m06).
Each trial requires the previous trial plus its listed main-story milestone.
Complete the objective, discuss it with the named NPC, then turn in for one eye.
The first twelve trials are required for The Portal at the Spire (ac_m35).
Speak with the Spire Guard there, then finish Eldara's expedition preparations.
Use the existing Spire frame to enter the End in A Sky without Dawn (ac_m36).
Four further trials award optional Wither, Undead, Exotic and Cryptic Eyes.
No eye is consumed by these quests. End Remastered's normal sources remain
available for replacement eyes. Save quest eyes for the Spire instead of throwing
them to locate a stronghold. This update does not change End Remastered loot,
recipes, trades, portal rules, or your world's existing portal blocks.

New trial IDs ac_key01..ac_key16 preserve all legacy discovery completions.
Already-completed story quests stay complete. Existing players may undertake the
new trials in order; existing story completions satisfy their milestone gates.
Missing End Remastered rewards block acceptance/claiming with an explanation,
so eyes cannot be permanently lost by claiming without the mod installed.
