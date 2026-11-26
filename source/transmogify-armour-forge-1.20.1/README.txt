TRANSMOGIFY ARMOUR 1.0.0
Minecraft Java 1.20.1 | Forge 47.4.10+ (47.x) | Java 17

INSTALL
Copy transmogify-armour-forge-1.20.1-1.0.0.jar into your Forge instance's mods
folder. For multiplayer, install the same version on the server and all clients.
Single-player only needs the client installation. No extra library is required.
This is for Forge 1.20.1, not Fabric, NeoForge, or Bedrock.

USE
1. Put on your real armour.
2. Press the = key (rebindable in Options > Controls > Transmogify Armour).
3. Choose Head, Chest, Legs, or Feet. Search by item name, item ID, or @mod.
4. Click an item to preview that appearance. Matching set selects the other
   recognized pieces from the same set in your occupied armour slots.
5. Press Apply to save. Cancel or Escape discards the preview.

Original slot restores that slot's appearance; Reset all restores the whole
outfit. Press Apply to save resets. Scroll or use arrows to browse more results.
Hover an item for its name, mod and registry ID. Hover the bottom status text
to read a long message. If your armour changes while the menu is open, Reopen
refreshes the equipment snapshot.

WHAT CHANGES
Only the worn appearance changes. Your real item's protection, toughness,
durability, enchantments, attributes, abilities, name and inventory icon remain.
No target item is granted or consumed; all installed armour looks are free.
Enchant glint, compatible leather dye and armour trims are retained visually.
The original item's tooltip lists its cosmetic appearance.

Appearance is saved on the armour item, not your character. It follows that
item into chests, through trading, and through world saves. On death it follows
the normal item-drop/keepInventory rules. Newly equipped items keep their own
appearance. Other players with the mod see the saved appearance.

MOD SUPPORT
The wardrobe discovers all registered ArmorItem pieces from installed mods;
there is no hard-coded list of allowed mods or armour materials. Cosmetic
selection happens before Forge's custom armour-model and texture hooks, which
also allows models supplied by mods using the normal Forge armour layer.

Matching set recognizes common equipment names in registry IDs. A mod with
unusual naming can always be selected piece by piece. Ambiguous sets are not
automatically grouped. Only occupied armour slots receive a look.

Mods that replace the entire armour rendering layer or use a separate cosmetic
equipment system may require a compatibility adapter. Curios accessories,
elytra, horse armour and non-ArmorItem wearables are outside this wardrobe.
NBT-only variants and colours are not separate catalogue entries. A removed
target mod safely falls back to the real armour appearance. Compatibility with
every mod cannot be guaranteed; see verification for the actual tested cases.

BUILD / VERIFY
Use Java 17 and run gradlew.bat build (or ./gradlew build).
The installable reobfuscated JAR is written to build/libs.
gradlew.bat runGameTestServer runs dedicated-server validation.
gradlew.bat runClient -PsmokeTest uses an isolated generated test world.
Optional: place matching third-party test mods in compat-mods and add -Pcompat.
Test helper classes and the test structure are excluded from the release JAR.
Third-party mods and Minecraft game files are not included in the source ZIP.

DEVELOPER REFERENCES
https://docs.minecraftforge.net/en/1.20.1/misc/keymappings/
https://docs.minecraftforge.net/en/1.20.1/networking/simpleimpl/
https://github.com/MinecraftForge/MinecraftForge/blob/1.20.x/patches/minecraft/net/minecraft/client/renderer/entity/layers/HumanoidArmorLayer.java.patch
