EYE PROGRESSION UPDATE 1.4.0
Use with Ashen Covenant 1.3.0. NPC dialogue directs players to the sixteen
eye trials and explicitly identifies the existing End portal frame at the Spire.
Start with Sera after The First Hearth. The Spire Guard confirms portal preparation.

THE DEEP NIGHT NPCs 1.4.0
Minecraft Java 1.20.1 / Forge 47.4.10 or later in the 47.x series / Java 17

INSTALL
Put the release JAR in your instance's mods folder. Install the same JAR on
the server and on every joining client. No additional library is required.
The Deep Night and The Deep Night Guide are optional companion mods.
The supplied Eldara, Caelen, Spire Guard, and Jorge skins are included unchanged.
When upgrading, replace the old NPCs JAR with this version on both client and
server; do not keep two versions installed. Existing NPCs and hunt saves remain
compatible. The network protocol requires matching mod versions on both sides.

ASHEN COVENANT INTEGRATION
With Ashen Covenant: Chronicles 1.3.0 installed, right-click Eldara and choose
Main quests to accept and turn in the main story. The minus-key journal tracks
objectives; it cannot accept quests or grant rewards remotely.
Eight new companions provide their own quests through Covenant quests:
Rowan, Sera, Mara, Elian, Iona, Tamsin, Fen, and Ysolde. Their eight supplied skins
are included byte-for-byte, with the appropriate classic/slim player models.
With Chronicles 1.3.0, Caelen and Jorge also provide their authored side stories.
Accepted quests add a Discuss a quest option when earlier objectives are met.
Choose the assignment to hear its specific response and record the conversation.
Some reports require several NPCs in order, including the Spire Guard. Ordinary
lore topics do not count. Return to the original giver to receive the reward.
The NPC mod can still run alone for dialogue, Caelen's hunts, and Jorge's services.
The quest mod requires this NPC version. Install matching versions on all clients
and the server. Existing NPCs, hunts, stock and placements remain compatible.

PLACE THE NPCS
There is NO natural spawning, biome injection, structure spawning, or automatic
placement. Find the twelve eggs in the "The Deep Night NPCs" creative tab, or use:

/summon thedeepnightnpcs:eldara ~ ~ ~
/summon thedeepnightnpcs:caelen ~ ~ ~
/summon thedeepnightnpcs:spire_guard ~ ~ ~
/summon thedeepnightnpcs:jorge ~ ~ ~
/summon thedeepnightnpcs:rowan ~ ~ ~
/summon thedeepnightnpcs:sera ~ ~ ~
/summon thedeepnightnpcs:mara ~ ~ ~
/summon thedeepnightnpcs:elian ~ ~ ~
/summon thedeepnightnpcs:iona ~ ~ ~
/summon thedeepnightnpcs:tamsin ~ ~ ~
/summon thedeepnightnpcs:fen ~ ~ ~
/summon thedeepnightnpcs:ysolde ~ ~ ~

For a precise guard post, summon on a clear, solid floor. The original placement
is its saved patrol centre. To relocate a post, remove that guard and place a new
one. Administrators can remove the nearest guard within five blocks with:
/kill @e[type=thedeepnightnpcs:spire_guard,distance=..5,sort=nearest,limit=1]
Substitute eldara, caelen, or jorge to remove those NPCs.

BEHAVIOUR
All NPCs except Spire Guards stay at their placement X/Z and face nearby players. Gravity
lets them settle on the floor. They cannot be pushed, leashed, put in vehicles,
or sent through portals. All twelve NPCs persist and resist ordinary damage.
The administrator /kill command remains available.

Spire Guards have an iron sword and a visible, usable shield. They wander within
a five-block horizontal radius and a three-block vertical range of their post.
Patrol routes are checked against this area; displacement outside it is corrected.
They engage nearby Enemy-marked mobs within the area and do not pursue indefinitely.
They carry no equipment drops and have eight basic interaction topics.

DIALOGUE WHEEL
Right-click an NPC to begin. Click a wheel sector or press 1-8. Left/Right changes
topic pages. Backspace, right mouse button, or Back returns to the previous topic.
Topics returns to the start. Scroll over text (or use Up/Down) for long passages;
scroll over the wheel to change topic pages. Escape / Farewell closes the screen.
Conversations do not pause the world and close if you move more than eight blocks
away. The interface scales to the window and shows a live portrait with your skins.

ELDARA
163 nodes: a greeting, 18 subject introductions, and 144 substantial answers,
totalling over 11,000 words. Subjects include beginnings, the Deep Night, the Spire,
Eldara's history, the fallen covenant, refuges, the watch, hunting, fieldcraft,
magic, beasts, dimensions, ruins, major threats, short tales, conscience,
practical guidance, and the open chronicle. All topics are available immediately.
Lore is written to fit the existing Deep Night Guide / Ashen Covenant themes.
The stories do not claim to add structures, portal recipes, or hidden mechanics.

CAELEN'S CONTRACTS
Available hunts shows up to three different randomly selected targets per player.
Select one to see the exact entity ID, required count, emeralds, and XP, then accept.
One active contract per player. Normal hunts require 3-10 kills; high-health targets
(100+ base max health) require 1-2. Rewards reflect target base health and count.
Fresh reports have a 60-second game-time cooldown. Active work is preserved when
the offer board changes. Contracts have no time limit.

Only death events attributed directly to the player count, including ordinary
player-owned projectiles. Guard kills, independent pet kills, and environmental
deaths without direct player attribution do not count. Modded damage systems may
attribute an attack differently; check the ledger after your first kill.

Return to Caelen, select My contract, and Claim reward. Payment is consumed on the
server before granting emeralds and XP, preventing repeated claims. Overflow
emeralds drop beside the player. Abandoning requires a confirmation in the UI,
clears that hunt's progress, and pays nothing. Offers and progress are personal.
Records are stored by player UUID in world/data/thedeepnightnpcs_hunts.dat and
survive deaths, relogs, dimension changes, and normal server restarts.

MODPACK TARGET DISCOVERY
The server inspects the installed entity registry once when needed and again after
server data reloads. Living entities in the MONSTER category or implementing the
Enemy marker are eligible. This includes hostile mobs from other mods and some
nonstandard categories (for example the vanilla dragon). NPCs here are excluded.
One detached instance per entity type is used for classification and base health;
these are never added to the world. Types that cannot be constructed are logged
and skipped. No absent mod is hard-coded into the board.

There is no universal Minecraft flag that captures every mod's idea of hostility.
For unusual classifications, add entity IDs to config/thedeepnightnpcs-common.toml:

additionalHostileMobs = ["examplemod:hostile_creature"]
excludedMobs = ["minecraft:ender_dragon", "examplemod:unavailable_boss"]

Restart the server after editing this common config. Data packs can instead extend
data/thedeepnightnpcs/tags/entity_types/hunt_targets.json and
data/thedeepnightnpcs/tags/entity_types/excluded_hunt_targets.json. Use /reload for
tag changes. Exclusions take precedence. Invalid/absent entity IDs are not offered.

Registry presence does not guarantee a target spawns naturally or remains reachable:
bosses, summons, and special encounters follow their owning mod's rules. Caelen
does not summon quarry or locate it. Abandon unsuitable hunts or exclude them.

JORGE THE ARMSKEEPER
Right-click Jorge for his eight-spoke wheel: Buy ingots, Buy books, Enchant gear,
and five dialogue topics about his role, stock, enchanting, books, and equipment.

Buy ingots / Buy books opens a stock summary. Browse stock opens the normal
Minecraft trading interface: select a listed offer, put emeralds in the payment
slot (or let the screen fill it from your inventory), and take the result. Prices,
quantities, enchantments, and remaining stock are visible before purchase. Tools
are enchanted in a separate enchanting interface, not purchased here.

Each player has three random, distinct ingot offers and up to five different
enchanted-book offers. Ingot offers allow eight purchases per shipment; book
offers allow three. Book prices include the book itself. Books select a supported
level and may include treasure enchantments or curses when marked tradeable.
Use an anvil to apply purchased books to compatible equipment.

Close the shop, right-click Jorge again, and choose Refresh stock at either
counter. Each counter can be refreshed for free after its own sixty-second
game-time cooldown. Refreshing replaces offers with a new random selection and
restores the new offers' uses. Stock does not change automatically while buying.
Refreshing books does not change ingots, or vice versa. Sold-out offers remain
sold out until a refresh. "Recyclable" means refreshable stock, not selling items
back. Jorge has no buyback service.

Stock is personal and saved in world/data/thedeepnightnpcs_jorge_stock.dat. Prices,
uses, books, and cooldowns survive relogging and server restarts. Visiting a
different Jorge does not reset them. Each Jorge serves one trading customer at
a time; others can still read dialogue or use enchanting. Menus close when the
player leaves his vicinity or Jorge is removed. Unused payment and input items
return through the normal Minecraft menu behavior.

INGOT AND BOOK COMPATIBILITY
Ingots are discovered from forge:ingots, c:ingots, their child tags, and item IDs
ending in _ingot. This includes installed modded ingots. Nuggets, storage blocks,
and ordinary vanilla bricks are not selected by default. Copper costs 1 emerald per ingot, iron 2,
gold 4, netherite 32, and other ingots 6. Bundles contain 1-4 ingots except
netherite (one). Each listing is capped at 64 emeralds. Books cost 10-64 emeralds
depending on level, a random price component, and treasure status.

The book shelf discovers installed enchantments whose isTradeable flag permits
trading. Exclusions and extra ingot IDs can be set in the [jorge] section of
config/thedeepnightnpcs-common.toml:

[jorge]
additionalIngots = ["examplemod:metal_bar"]
excludedIngots = ["examplemod:progression_locked_ingot"]
excludedEnchantments = ["examplemod:unavailable_enchantment"]

Restart after editing this config. Removed or excluded entries are pruned from
saved stock when Jorge is opened. Refresh to replace the remaining selection.

ENCHANTING AT JORGE
Choose Enchant gear. Put an eligible unenchanted tool, weapon, armour piece, or
plain book in the item slot and lapis in the second slot. The normal three offers,
player enchantment seed, XP requirements, lapis costs, Forge hooks, and available
enchantments apply. Jorge does not charge emeralds for this service.

Bookshelves are counted around the block at Jorge's feet using normal enchanting
table spacing. Leave the adjacent one-block ring clear; place bookshelves two
blocks away, at his foot level or one block above. Fifteen effective shelves
provide the usual level-30 option, which spends three XP levels and three lapis.
Without shelves the service still works at lower power. No enchanting-table
block is placed or required. This service does not repair equipment or replace
the anvil's book-combination mechanics.

CUSTOMISING DIALOGUE
Resource packs may override assets/thedeepnightnpcs/dialogue/eldara.json,
caelen.json, spire_guard.json, or jorge.json. Each file is an object keyed by node ID. A node
has title, text, and children (an array of node IDs); root is required. Use newline
characters for paragraph breaks. Every child must exist. Changing resources takes
effect when opening a new conversation after a resource reload.
Source authoring files are tools/*-dialogue.txt. Run tools/build-dialogue.ps1 after
editing them to regenerate the bundled JSON, item models, and English names.

BUILD / VERIFICATION
gradlew.bat build
gradlew.bat runClient -PsmokeTest
gradlew.bat runClient -PjorgeTest
gradlew.bat runServer -PserverTest
gradlew.bat runServer -PserverTest -PtestDeepNight

The optional compatibility profile uses the neighbouring Deep Night 1.0.3 and
GeckoLib 4.8.4 development artifacts. They are not release dependencies.
QA code is excluded from the release JAR. Runtime reports and screenshots are in
verification. QA uses separate generated worlds; it does not install into or
change your actual modpack instance. Source networking follows Forge's 1.20.1
SimpleImpl documentation: https://docs.minecraftforge.net/en/1.20.1/networking/simpleimpl/

Supplied artwork remains the property of its original owner.
