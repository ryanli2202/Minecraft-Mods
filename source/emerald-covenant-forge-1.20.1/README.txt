THE EMERALD COVENANT 1.1.1
Minecraft Java 1.20.1 | Forge 47.3+ | Java 17

INSTALL
Place emerald-covenant-forge-1.20.1-1.1.1.jar in your modpack's mods folder.
When upgrading, replace the previous Emerald Covenant JAR; do not load both.
Existing bell-bearing unlocks are preserved. Old maximumPrice values above 999
are corrected to the new 640 default automatically when the world loads.
For multiplayer, install the same JAR on the server and every player's client.
Restart Minecraft. This is a Forge mod, not a Fabric, NeoForge 1.21 or Bedrock mod.
It has no mandatory dependencies beyond Forge. Use the Forge version required
by your pack: the tested Iron's 3.16.1 installation requires Forge 47.4+.

OPEN THE SHOP
Press H in a world. Change the binding in Options > Controls > Key Binds >
The Emerald Covenant > Open the Emerald Covenant. Escape closes the shop.
/covenant shop also opens it. You must be alive and outside spectator mode.

MERCHANTS
Garrick, the Forgemaster: weapons, ordered by base attack damage descending.
Seren, the Iron Warden: armour, ordered by total set protection descending.
Orin, Keeper of the Archive: spell scrolls and spellbooks in separate tabs.
Merri, the Hearthkeeper: food, with ordinary meals around 1-3 emeralds each.

The shop is populated from the server's live item registry, including modded
weapons, armour, spellbooks and edible items. It uses item classes, attributes,
tags and conventional item names to discover stock. Iron's spell registry is
enumerated explicitly, including add-on spells and every enabled spell level.
Scrolls contain the real spell NBT; blank Iron's scrolls are not sold.
Disabled Iron's spells are omitted. Existing casting/learning requirements
from the spell mod still apply after buying a scroll.

Search by item name or ID. Use @modid to restrict a source; combine terms, e.g.
"@simplyswords runic". Reverse the default order or show unlocked stock only.
Select a row, choose a quantity, then Purchase. Hover the small item icon for
the mod's original tooltip. Wheel-scroll the list or use the page arrows.

Weapon damage is the unenchanted main-hand attack attribute (including the
normal 1-point player base), without player skills, buffs, enchantments or
conditional combat effects. Ranged weapons with no melee attribute show
"variable damage"; ammunition and charge cannot be reduced to one universal
damage value. AttackDamage overrides can provide a pack-specific sort value.

Armour sets are grouped using matching registry-name stems after removing
piece names, and the strongest piece per equipment slot contributes to the
set's total. Pieces are sold individually, with set and piece protection shown.
Nonstandard set names can be corrected through armorSet/setProtection overrides.
Horse armour is also included and uses its own protection value.

EMERALDS AND PRICES
Emerald items in your main inventory and offhand pay for purchases. Emerald
blocks, backpacks and bank balances are not counted. Prices consider rarity,
weapon strength, armour protection, durability and spell level. At the default
multiplier, equipment and spells stay within these rarity bands:
  Common:     16-64 emeralds
  Uncommon:   72-128 emeralds
  Rare:       144-256 emeralds
  Epic:       288-448 emeralds
  Legendary:  480-640 emeralds
Strength and spell level adjust the price within a band, so higher rarity
always costs more than the previous band. Food uses a separate 1-12 emerald
formula; ordinary meals usually cost 1-3. Bearings have their own prices below.
The default maximum is 640 emeralds per item, including explicit JSON prices.
The configurable maximum cannot exceed 999. Old four-digit JSON overrides are
clamped to the configured maximum, so the item stays available at a fair price.
Bulk purchases can total over 999; this cap is per item, not per basket.
All prices and unlocks come from the server, including in single-player.
Creative players also pay emeralds.

Trades are all-or-nothing: not enough money or inventory room changes nothing.
Payment can free a slot for the purchase. Items never spill on the ground.
Purchase requests cannot choose their own item NBT or price. Replayed purchase
packets cannot buy twice. A shop session expires after 10 idle minutes; reopen
it if it expires. World reloads also invalidate open shop sessions.

BELL BEARINGS
Buy bearings from the Bell bearings tab, then click Offer bearings, or hold a
bearing and right-click. Each is consumed only on its first successful offering.
An already unlocked bearing is not consumed. Unlocks persist through death,
reconnection and world saves, and are separate per player and world.

Forgemaster (48 emeralds): advanced weapon stock.
Warden (48): advanced armour; sets with 15+ protection require it throughout.
Archivist (48): advanced spells and spellbooks.
Sovereign (128): rare/epic/legendary stock, very strong weapons, level 6+ spells
and armour sets with 24+ protection. It may be required alongside a merchant's
own bearing. The selected item's panel lists the exact missing bearings.
Version 1.1.1 also names a single missing bearing on the purchase button and
lists all missing bearings in its tooltip and server rejection message.
If Forge and Warden are sworn but rare stock is locked, offer Sovereign too.
You do not need to buy or offer Forge/Warden again. Unlock rules are unchanged.

Basic stock and food are available immediately. All stock stays visible unless
you enable the Unlocked only filter. Bearing prices follow priceMultiplier.
The bearings are also in the Tools & Utilities creative tab. They can be added
to quests or loot with these IDs:
  emeraldcovenant:forgemaster_bell_bearing
  emeraldcovenant:warden_bell_bearing
  emeraldcovenant:archivist_bell_bearing
  emeraldcovenant:sovereign_bell_bearing
This release does not change your pack's mob or structure loot tables.

PACK CONFIGURATION
World settings: <world>/serverconfig/emeraldcovenant-server.toml
  requireBellBearings = true
  priceMultiplier = 1.0
  maximumPrice = 640
Use defaultconfigs/emeraldcovenant-server.toml to establish new-world defaults.

Item overrides: config/emeraldcovenant-catalogue.json
An empty file is created on first catalogue load. See catalogue.example.json;
merge only entries you want, rather than blindly replacing your configuration.
Supported per-item fields:
  category: WEAPONS, ARMOUR, SPELLS, SPELLBOOKS, FOOD or BEARINGS
  price: explicit emerald cost, positive integer (not multiplied; capped by
         maximumPrice, default 640, absolute maximum 999)
  rarity: 0 common, 1 uncommon, 2 rare, 3 epic, 4 legendary
  attackDamage, protection, armorSet, setProtection
  requiredBearings: bitmask; 1 forge, 2 warden, 4 archivist, 8 sovereign
                    add values for combinations; 0 removes the requirement
  disabled: true to remove the offer
  nbt: SNBT for a prepared item variant
Use item IDs as keys, or an exact offer key for one variant. Exact keys take
precedence over item-wide overrides. An extraOffers list supports named custom
offers and NBT variants from other spell mods. Missing optional items are ignored.

Item tags emeraldcovenant:weapons, :armour, :spells, :spellbooks and :food can
classify unusual registered items. All dynamic third-party spell systems cannot
be inferred from their item IDs; Iron's is explicitly supported, while other
systems may need prepared NBT offers. Unusual equipment with neither recognizable
attributes, classes, tags nor names may need a category override.

Operator commands (permission level 2):
  /covenant reload  -- rebuild after catalogue/config edits
  /covenant audit   -- write emeraldcovenant-catalogue-audit.json in game directory
Normal datapack /reload invalidates the catalogue; the next opening rebuilds it.
Check the server log for skipped items/integration warnings.

ARTWORK
The fantasy frame, four blocky Minecraft villager portraits and pixel bell icon
were generated with the built-in image_gen tool. The exact prompts are in
artwork/generation-prompts.json. Original images are artwork/pixel-frame.png,
artwork/pixel-merchants.png and artwork/bell-bearing.png. Runtime textures are
in src/main/resources/assets/emeraldcovenant/textures/: a 384x256 frame, four
64x64 portraits in a 128x128 atlas, and a 32x32 item icon. Nearest-neighbor
export and disabled texture blur keep the pixels crisp. Run tools/export-
artwork.ps1 to reproduce these exports. All runtime art is embedded in the JAR.

SOURCE AND BUILD
Use Java 17. Run gradlew.bat build (Windows) or ./gradlew build (other systems).
Default build target is Forge 47.4.10; -PforgeVersion=47.3.25 can test the lower
supported version. The release JAR is reobfuscated for normal Forge installations.
The source archive excludes Gradle caches, third-party jars and test worlds.

TESTS
gradlew.bat runClient -PsmokeTest runs a generated throwaway world and captures
screenshots. gradlew.bat runGameTestServer runs dedicated-server checks.
For optional integration checks, place your own third-party mod jars and their
dependencies in compat-mods, then use -PcompatDir=compat-mods -PsmokeTest runClient.
QA classes and the test structure are excluded from the release JAR.
See verification/TEST-RESULTS.txt for the tested scope and remaining limitations.
