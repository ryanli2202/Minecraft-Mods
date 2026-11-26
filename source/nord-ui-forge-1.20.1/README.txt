THE DEEP NIGHT - NORDIC INTERFACE
Minecraft Java Edition 1.20.1 / Forge 47.4.10 / Java 17

INSTALL
Copy the-deep-night-ui-forge-1.20.1-1.1.1.jar from dist into your instance's
mods folder. Install the same mod on the server for multiplayer. The artwork
and font are bundled; no separate resource pack is needed. Curios is optional;
if used, install Curios 5.14.1+1.20.1 (5.x) on client and server too.
Remove the older Deep Night UI jar when updating. Both sides need 1.1.1.
Iron's Spells 1.20.1-3.16.1 is optional; install its own dependencies normally.

CONTROLS
E: redesigned inventory. EQUIPMENT shows the player, armour and base stats.
PERSONAL VAULT / 999: opens 999 personal storage slots from the inventory.
V: opens the vault directly; rebind in Options > Controls > NORD Interface.
Vault arrows / page field: pages 1-19. Pages 1-18 have 54 slots, page 19 has 27.
Shift-click: move between the current vault page and your inventory.
STORE BACKPACK: deposits the 27 main inventory slots across all vault pages.
It preserves the hotbar and equipment. Items that do not fit remain carried.
Put down any item on the cursor before switching pages or storing a backpack.
RECIPES / MODS (R in compact layout): original inventory, recipe book and
inventory buttons injected by other mods.
SPELLS: separate Iron's inscription tab when Iron's Spells is installed.
Select a spell, take its EXTRACT scroll, put the replacement in SCROLL,
select the now-empty spell slot, then INSCRIBE SCROLL. Bound spells stay locked.
An equipped Curios book loads automatically. Loose books can go in BOOK.
F7 / UI button / Forge Mods > Config: UI compatibility and placement options.
F8: toggle the visual overhaul while in game. This does not remove storage.
/nordvault: alternate command to open storage outside other containers.

THE INTERFACE
Original transparent THE DEEP NIGHT title art and a northern mountain menu
background. Silver line ornaments, charcoal panels, and open-source Jost type.
Title menu actions remain Minecraft's original Singleplayer, Multiplayer,
Mods, Realms, Options and Quit actions with a quieter menu layout.
Moving compass 12 GUI pixels from the top; north corresponds to negative Z.
Jade and Iron's Spells no longer push it down. F7 > Compass height adjusts it.
Ornate red health bar at bottom center; absorption is shown numerically.
Level and XP progress at bottom left. Hunger at bottom right uses a green bar.
These are Minecraft stats; the green bar is food, not a new stamina mechanic.
Breath appears above health when underwater or recovering air.
Quick slots run down the right edge, with a separate offhand slot. Compact
screens use a 3-by-3 quick-slot group on the right. Number keys and wheel
selection still use normal Minecraft controls. Item counts and durability show.
Themed inventory/equipment, personal vault, main menu, pause/settings buttons
and backgrounds, plus chest/barrel, shulker, hopper and dispenser panels.
Specialized crafting machines, creative catalogue and third-party screens
retain their functional layouts. The recipe-book fallback is deliberately
available for recipe and inventory integrations.

CURIOS
With Curios installed, EQUIPMENT shows live accessory previews and a
CURIOS / ACCESSORIES button. This opens Curios' actual synchronized menu,
preserving slot validators, equipment effects, cosmetic slots, pages and
render toggles. A button returns to the Deep Night equipment tab.
The integration uses the world's existing slot definitions; it does not add,
resize or erase accessory slots. If your pack has no Curios slots, configure
them through Curios or the mods that supply your accessories.

SAVE DATA
Vaults are private to each player's UUID and to the world. They survive death,
dimension changes and reconnects. Full ItemStack NBT is stored, including
names, durability, enchantments and other mods' item data/capabilities.
Data lives in <world>/data/nordui_vaults.dat and uses normal world autosaving.
Include this file with world backups. Removing the UI mod makes the vault
inaccessible until the mod is restored; it does not move contents into a chest.
As with regular inventories, a crash can lose changes since the last save.

CONFIGURATION / OTHER MODS
See COMPATIBILITY.txt for the audit of your mod list and exact integration scope.
config/nordui-client.toml holds all visual preferences. F7 edits the common
compatibility options in game. AUTO HUD ownership yields to RPG-HUD when
installed. For the full Deep Night HUD, first disable RPG-HUD's rendering in
its own settings, then select DEEP_NIGHT. Other-mod screens retain their own
backgrounds and controls by default. JEI gets extra sidebar room; TrashSlot
has native layouts for all three containers; ParCool gets survival-bar spacing.
Iron's synchronized mana can use the themed bar. Its default spell cluster
docks to the left-center; custom anchors and the native spell wheel, cast
bar, recasts and cooldowns are retained. Both options can be switched off.
No other mod's config is overwritten.
<world>/serverconfig/nordui-server.toml controls spellTab and
spellTabRequiresTable. Portable inscription is allowed by default; the server
can require an inscription table within 8 blocks, or disable the tab.
The complete screenshot-listed pack has not been launched together. Tested
profiles and manual visual review are documented in verification.

BUILD AND VERIFY
Run gradlew.bat build with Java 17. ForgeGradle uses Gradle 8.8.
Compile-only dependencies in libs: Curios 5.14.1+1.20.1, Iron's Spells
1.20.1-3.16.1, GeckoLib 1.20.1-4.8.4, and TrashSlot 1.20.1-15.1.5.
Supply these from their official releases. They are not embedded in our jar.
Run tools/fetch-test-dependencies.ps1 to acquire the exact development jars.
gradlew.bat runClient -PsmokeTest: isolated client/world smoke checks.
gradlew.bat runClient -PsmokeTest -Pcurios: same with Curios compatibility checks.
gradlew.bat runClient -PsmokeTest -Pirons -PuiMods: Iron's/Curios + JEI, Jade,
TrashSlot/Balm and ParCool; verifies real spell replacement and save/reconnect.
Add -PrpgHud to verify AUTO ownership with RPG-HUD 3.13.
The Curios QA run expects test slots configured in run/config/curios-common.toml:
slots = ["id=ring;size=2;operation=SET", "id=necklace;size=1;operation=SET", "id=charm;size=1;operation=SET"]
gradlew.bat runGameTestServer: dedicated server storage checks.
QA Java classes, the test structure and test-only emerald ring tag are excluded
from the release jar. Test worlds are isolated in this project's run folders.
tools/create-ui-textures.ps1 regenerates the original ornate HUD texture set.

ARTWORK / ATTRIBUTION
Three original paintings/assets generated with the built-in image_gen tool:
assets/nordui/textures/gui/slate.png
assets/nordui/textures/gui/main_menu.png
assets/nordui/textures/gui/the_deep_night.png (transparent RGBA)
Exact generation prompts are in artwork/prompts.json.
Bar frames, gradient fills and quick-slot sprites are original code-drawn art.
Jost: Copyright 2020 The Jost Project Authors; SIL Open Font License 1.1.
The full license is bundled in assets/nordui/font/ofl.txt.
Font source: https://github.com/google/fonts/tree/main/ofl/jost
Curios API: https://docs.illusivesoulworks.com/1.20.x/curios/inventory/basic-inventory
Forge menus: https://docs.minecraftforge.net/en/1.20.1/gui/menus/
Original Nordic-fantasy artwork; no Skyrim game files are included.

