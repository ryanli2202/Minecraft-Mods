TRAVELLER'S COMPANIONS 1.4.0
Minecraft Java 1.20.1 / Forge 47.4+ / Java 17

INSTALL
Put travellers-companions-1.20.1-1.4.0.jar in the mods folder on both the
client and server. Keep the matching dependencies already in your modpack:
  Better Combat 1.9.0+1.20.1 Forge
  Player Animator 1.0.2-rc1+1.20 Forge
  Cloth Config 11.1.136 Forge
  Iron's Spells 'n Spellbooks 1.20.1-3.16.1 and Iron's Lib 1.1.0
  Common Weapons 0.2.2 Forge
  Simply Swords 1.56.0 Forge and Architectury 9.2.14
  GeckoLib 4.8.4, Curios 5.14.1 and Caelus 3.2.0
For Castelan's intended weapon also keep Epic Knights 10.11, Epic Knights
Addon 2.6 and Architectury 9.2.14. The pack's Epic Knights x Better Combat
compatibility mod is supported. These third-party mods are not bundled.
Upgrade both client and server together; replace the previous companion
JAR instead of keeping both versions. Existing levels, inventories and
recovery timers remain intact. Leonardo, Yuriko and Yushiro are new entries
after the existing five; old character indexes and progress are preserved.
Gideon uses the supplied replacement skin.
All named companions now start at 100 maximum health. Existing saves gain
the higher baseline while preserving missing health, gear and progression.
Level bonuses still add 2 maximum health per level (138 at level 20).
The release is built in a separate development directory; no launcher
profile or launcher world has been modified by this project.

MOVEMENT INTEGRATIONS (1.4.0)
Optional: ParCool 1.20.1-3.4.3.3 and Serious Player Animations 1.1.1.
Both named companions and troop soldiers support these integrations.
With ParCool installed, following companions speed up when the owner
sprints, uses ParCool Fast Run, or moves quickly. They return to ordinary
following speed when the owner slows down or they catch up.
Companions can climb/vault accessible obstacles and ledges up to two blocks
high while following or approaching an enemy. They check headroom, the
whole movement path, loaded chunks, world border and safe solid footing.
Tall walls, lava, fire, obstructed ceilings and unsafe landings are rejected.
There is no wall scaling or long-gap jumping.
In combat, an eligible incoming melee hit or projectile has a 22% chance
to trigger a short roll over roughly two blocks, if a safe escape exists.
Successful rolls have a five-second cooldown; failed attempts are also
throttled. Rolls evade their triggering hit, not subsequent damage.
Explosions, environmental damage, active casts/attacks/guards, passive
stance and hold-position orders do not trigger rolls. Existing guards
and shields retain priority. Chance and cooldown are configurable.
ParCool's action code requires Player entities. This integration reads
the owner's actual FastRun state and supplies NPC movement and poses for
fast running, climbing and rolling. It does not attach player controls,
stamina or the entire ParCool action system to companions.
With Serious Player Animations installed on the client, normal walking
uses its actual seriousplayeranimations:walking keyframes at a pace tied
to movement. Combat animations, item use and guards retain priority.
Without the respective optional mod, the existing movement or animation
remains available. Third-party mods and animation assets are not bundled.
Upgrade both client and server to 1.4.0 together (network protocol 6).

CONTROLS
Tab: companion roster. Rebind under Options > Controls > Key Binds >
Traveller's Companions. Vanilla Player List also defaults to Tab; move
that binding if you want both functions on separate keys.
Right-click your summoned companion: equipment and backpack.
Sneak + right-click: radial dialogue and orders. Esc closes a screen.
Choose a character in the roster, then Summon, Dismiss or Train.
Choose Troops for the separate four-soldier group category.

THE EIGHT TRAVELLERS
Varys, The Farwatch: 100 health, bow, 64 arrows.
Raeshel, The Unbroken: 100 health, iron sword.
Gideon, The Last Bulwark: 100 health, iron sword and shield.
Castelan, The Roadwarden: 100 health, Epic Knights Addon Steel Short Spear
  (magistuarmoryaddon:steel_short_spear).
Mara, The Surebolt: 100 health, crossbow, 64 arrows.
Leonardo: 100 health, Graybeard's Staff and three starter spell scrolls.
Yuriko: 100 health, Simply Swords Iron Katana (simplyswords:iron_katana).
Yushiro: 100 health, Common Weapons Big Katana (common_weapons:test).
All eight use the supplied skins and the standard player model. Each has
an original backstory, combat lines, injury lines and four dialogue tiers.
Every starter pack includes four bread. Starters are granted once, so
dismissing and resummoning cannot replenish items or ammunition.

TROOP GROUPS
A player can summon one group of four alongside two named companions
(six companions in total). Choose one type from Tab > Troops:
  Footsquires: iron swords.
  Footpaladins: spears and shields; block frontal melee and projectiles
    while their shields are raised between attacks. Rear hits and damage
    that bypasses shields still hurt them. Passive stance disables blocking.
  Footarchers: bows and 64 starter arrows each. Restock their backpacks.
Each soldier has 20 base health, fixed level-one combat stats, and no
training, bond, victory progression, spells or named character abilities.
Right-click a soldier for gear and backpack. Sneak + right-click for
follow/hold and aggressive/defensive/passive orders, available immediately.
Each type has four persistent soldiers, numbered 1-4. Their health, gear,
food and ammunition survive dismissal, type changes and world restarts.
Supplies are granted once per soldier. Troops use your supplied
footsquire.png, footpaladin.png and footarcher.png skins, respectively,
with the existing combat animations.
Footpaladins use the configured Castelan starter spear, including its
Epic Knights iron pike / vanilla trident fallback when unavailable.
A fallen soldier recovers for ten real-time minutes; survivors remain.
Dismiss the survivors before recalling a complete group, and wait until
all four are ready. Other troop types remain available during recovery.
Every new group summon requires four safe, separate positions. Unsafe
summons create no partial group and grant no starter supplies.
Logout and owner death dismiss the group without a recovery penalty.
Dimension travel and catch-up preserve surviving soldiers and supplies;
they do not revive casualties. Friendly-fire protection also covers troops.

COMPANION RULES
Each player owns a separate roster and may summon up to two different
named companions at a time, plus one troop group. Select either named
companion to dismiss or train them individually. Existing single-companion saves migrate automatically.
Companions are automatically tamed and follow their owner. Aggressive
stance acquires nearby monsters only. Aggressive and defensive companions
also attack mobs their owner attacks, including passive and neutral mobs.
They never start fights with passive or neutral mobs on their own.
Only the owner-selected passive/neutral target can take companion damage;
nearby bystanders remain protected from arrows, spells and sweeping hits.
Players and companion party members remain protected. Passive stance
never attacks, even when the owner attacks. Slimes, ghasts and other
monsters outside the Monster base class remain eligible automatic targets.
Scout's insight only counts monsters.
Equipment, enchantments, durability, backpack items, levels, bond and kills
persist. Dismissal preserves current health. Death preserves equipment and
items, dismisses the companion and starts a fixed ten-minute REAL-TIME
recovery for that character. The timer persists over logout/restart and
continues while offline. A recovered companion returns at full health.
Other characters remain available while one recovers.
Logout and owner death dismiss both named companions and the troop group
without a recovery penalty.
Dimension travel transfers both companions, provided there is a safe place
to stand. Following companions catch up across unloaded chunks. Holding
companions remain stationed; dismiss them from the roster to recall them.
Summoning and teleporting require collision-free, solid, non-hazardous ground.

EQUIPMENT AND COMBAT
Six equipment slots: head, chest, legs, feet, main hand and off hand.
27 backpack slots plus normal player inventory. Shift-click moves stacks;
armour auto-routes into an empty appropriate slot. Put weapons in Hand.
Companions other than Leonardo can use a melee weapon, bow or crossbow. Ranged weapons use
real arrows from the backpack, including tipped arrows; replace spent ammo.
Leonardo uses the spells equipped in his spell menu. Firearm and dual-wield
AI are not provided.
Better Combat's installed weapon attributes select melee reach, combo,
damage multiplier, attack windup and animation. The renderer samples its
actual PlayerAnimator keyframes, limb bends and held-item animation tracks.
Melee is a single-target companion AI; it does not reproduce the player's
entire sweeping hitbox implementation. Weapon sounds and special effects
provided only by player attack mixins may differ.
Food in the backpack restores health out of combat. Known poisonous foods
and chorus fruit are excluded. Armour and weapon models render on the NPC.

LEONARDO'S SPELLS
Open Leonardo's inventory, then press Spells. Six single-scroll slots
control his loadout. Magic Missile I, Firebolt I and Heal I are supplied
on his first summon. Drag or shift-click collected compatible Iron's
scrolls into these slots; move them back into your inventory to unequip.
Scrolls retain their spell level, are never consumed by casting, and are
not duplicated when moved or when Leonardo is dismissed. Unsupported
scrolls are rejected and identified in the spell-menu tooltip.
The menu displays synchronized mana. Leonardo starts with 150 maximum
mana, gains 10 per trained level and regenerates 5 per second. Casting
uses Iron's real spells, mana costs, configured cooldowns and original
animation resources. Mana and cooldowns survive dismissal and scroll swaps.
He aims offensive spells at eligible combat targets, heals himself below 70%
health and casts equipped defensive buffs during combat. Passive stance
disables casting. Removing an active scroll interrupts its cast.
Twenty-nine supported combat and support spells in the installed Iron version:
  Magic Missile, Magic Arrow, Firebolt, Fire Arrow, Icicle, Snowball,
  Poison Arrow, Acid Orb, Blood Needles, Lightning Lance, Guiding Bolt,
  Wither Skull, Fireball, Magma Bomb, Lob Creeper, Ball Lightning,
  Ray of Frost, Fire Breath, Cone of Cold, Poison Breath, Electrocute,
  Blood Slash, Flaming Strike, Frostwave, Heat Surge, Heal, Greater Heal,
  Oakskin and Fortify.
Configured spell levels and enabled/disabled settings are respected.
Player-specific travel/utility spells and other unlisted spells cannot
be equipped. The staff is visual equipment; the equipped scrolls select
his spells, rather than the spells in a player's spellbook.

KATANA COMPANIONS
Yuriko uses the installed Better Combat katana combo, reach, attack timing
and animations. Her katana guard blocks frontal monster melee attacks and
reflects incoming enemy projectiles, including arrows and fireballs, back
toward their attacker. A successful parry opens an 8-tick guard window,
with 28 ticks between new guard windows, and wears the katana. The guard
pose is visible to other players. Rear attacks, explosions, unblockable
damage and environmental damage bypass it. Passive stance disables it.
Other equipped items whose registry name includes "katana" support guard.
Yushiro uses Common Weapons' actual ohtati3 basic and ohtati right-click
special animation resources. Two basic cuts build into a special sequence
with two timed sweeping hits; the special has a six-second cooldown.
The Big Katana's animated blade/sheath model plays with the special.
Hits use the same target and bystander protection, including the special sweep.
Common Weapons' player procedures are not invoked on an NPC. This bridge
reproduces their normal basic and right-click special, not sneak variants.

PROGRESSION AND RPG ORDERS
Levels 1-20. Training from level L costs 4 + 2*(L-1) emeralds, removed by
the server. Named companions only. Each level adds 2 maximum health and 0.25 base melee damage.
Ranged damage and firing speed also improve. Crossbows charge before
firing, use crossbow sounds and launch crossbow projectiles. Training is available while a
companion is dismissed or recovering; it never clears a death cooldown.
Level 1: talk, backstory, follow/hold position.
Level 3: aggressive / defensive / passive combat stances; more dialogue.
Level 5: Rally grants you both 10 seconds of Resistance and heals the
  companion for 4 health. Two-minute persistent cooldown.
Level 8: Scout's insight counts hostiles within 24 blocks and marks them
  with Glowing for 8 seconds; deeper character dialogue.
Level 12: Field dressing consumes one backpack bread, heals you both for
  6 health, and requires the companion to be out of combat.
Level 15: a personal oath and the final dialogue tier.
Bond and victory totals record your adventures. Bond is a roleplaying
stat, not a second currency or an extra gate on emerald training.
Aggressive stance attacks nearby hostiles. Defensive fights to defend you
or the companion; both also assist attacks initiated by the owner. Passive does not attack. Hold stops walking, but allows
fighting enemies in reach. A new summon returns to following mode.

CONFIG
config/travellerscompanions-common.toml:
  publicCombatChat: server-wide combat/injury barks (default true).
    false sends them to the owner only. Barks have a 12-second throttle.
  teleportToOwner: safe catch-up teleport (default true).
  eatBackpackFood: out-of-combat food healing (default true).
  parcoolCompanionMovement: enable the optional NPC parkour bridge (true).
  seriousPlayerWalking: use installed SPA walking keyframes (true).
  dodgeRollChance: chance per eligible dodge attempt (0.22).
  dodgeRollCooldownTicks: minimum time between successful rolls (100).
  castelanStartingSpear: custom item registry ID. If unavailable, falls
    back to Epic Knights iron_pike, then vanilla trident.
Changing the starter weapon affects only a character's first summon.
The death recovery remains ten minutes and the level cap remains twenty.

BUILD AND VERIFY
Run with Java 17:
  python tools/fetch_dependencies.py
  python tools/import_pack_dependencies.py "PATH/TO/YOUR/MODPACK/mods"
  gradlew.bat build
The dependency downloader pins versions and verifies SHA-512 hashes.
The import script copies the exact integration JARs from your pack into
libs and verifies their SHA-256 hashes; it never edits your pack. The
source archive includes the dependency manifest, not third-party JARs.
Epic Knights dependencies are added to development runs with -PepicTest.
  gradlew.bat runGameTestServer
  gradlew.bat runGameTestServer -PepicTest
  gradlew.bat runClient -PsmokeTest -PepicTest
  gradlew.bat runGameTestServer -PparkourTest -PepicTest
  gradlew.bat runClient -PparkourTest -PparkourSmoke -PepicTest
The integration import also pins the ParCool and SPA versions above.
ParCool is compile-only by default; -PparkourTest adds both optional mods
to development runs. The dedicated parkour client check creates its own
disposable world and captures verification/parkour-movement.png.
QA-only classes and the empty test structure are excluded from the JAR.
Server tests use a fresh disposable world in run/gametest-* on every run
to prevent saved test mobs from interfering with later projectile tests.
The client smoke test creates a disposable flat world in this project's
run/saves, hides its test window, exercises real networking and captures
screenshots in verification/. It does not access launcher saves.
Source skins are bundled in src/main/resources; no asset download is needed.
Server data is stored in the world's data/travellerscompanions.dat.

VALIDATION SCOPE
Dedicated-server behavioural checks and integrated-client rendering/network
checks use Forge 47.4.10, the pinned libraries, and the Epic Knights versions
listed above. This is not a full playthrough of the several-hundred-mod
fantasycraft pack. Its Epic Knights Addon emits existing missing-resource
and legacy smithing-recipe warnings in the isolated test environment;
those are outside this mod and did not prevent the companion tests.

API REFERENCES
https://github.com/ZsoltMolnarrr/BetterCombat/tree/1.20.1
https://github.com/KosmX/minecraftPlayerAnimator
https://github.com/Magistu/Epic-Knights
https://modrinth.com/mod/epic-knights-addon
See ASSET-NOTES.txt for supplied skin handling.
