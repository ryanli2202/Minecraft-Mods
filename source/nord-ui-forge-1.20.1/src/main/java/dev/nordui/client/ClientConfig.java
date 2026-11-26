package dev.nordui.client;

import net.minecraftforge.common.ForgeConfigSpec;

public final class ClientConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.BooleanValue ENABLED, INVENTORY, HUD, COMPASS, BUTTONS, CONTAINERS;
    public enum HudOwner { AUTO, DEEP_NIGHT, OTHER_MOD }
    public static final ForgeConfigSpec.EnumValue<HudOwner> HUD_OWNER;
    public static final ForgeConfigSpec.BooleanValue TITLE, MOD_SCREENS, JEI_SPACE, AUTO_SPACING, IRONS_MANA, IRONS_DOCK, SPELLS, HOTBAR;
    public static final ForgeConfigSpec.IntValue COMPASS_Y, HUD_LIFT, HOTBAR_X, HOTBAR_Y;
    static {
        var b=new ForgeConfigSpec.Builder();
        ENABLED=b.comment("Master visual toggle. F8 changes this in game. Storage is always retained.").define("enabled",true);
        INVENTORY=b.define("replaceSurvivalInventory",true);
        HUD=b.comment("Replace vanilla health, hunger, XP and hotbar backgrounds.").define("nordicHud",true);
        COMPASS=b.define("compass",true);
        BUTTONS=b.comment("Theme standard buttons including title, pause and settings menus.").define("themeButtons",true);
        CONTAINERS=b.comment("Theme vanilla chest, barrel, shulker, hopper and dispenser screens.").define("themeStorageContainers",true);
        b.push("compatibility");
        HUD_OWNER=b.comment("AUTO yields health/XP/hotbar to RPG-HUD when installed. OTHER_MOD leaves vanilla overlays available. DEEP_NIGHT forces our layout; disable the other mod's HUD first.").defineEnum("hudOwner",HudOwner.AUTO);
        TITLE=b.define("replaceTitleScreen",true);
        MOD_SCREENS=b.comment("Opt in to global button/background theming on third-party screens. Off protects maps, shops, dialogue, skill trees and config screens.").define("themeThirdPartyScreens",false);
        JEI_SPACE=b.comment("Use the compact inventory/vault when JEI is installed to leave room for recipe sidebars.").define("reserveJeiSpace",true);
        AUTO_SPACING=b.comment("Lift bars above ParCool's default stamina area. Compass height is controlled separately.").define("automaticHudSpacing",true);
        IRONS_MANA=b.comment("Replace only Iron's mana overlay with a themed mana bar. Spell wheel, selection, cooldown, casting and recast overlays remain native.").define("themeIronsMana",true);
        IRONS_DOCK=b.comment("Move Iron's default Hotbar-anchored spell cluster to the left-center when Deep Night owns the HUD. Custom anchors remain untouched.").define("dockIronsSpellsLeft",true);
        SPELLS=b.define("showSpellsTab",true);
        HOTBAR=b.comment("Disable to retain the original hotbar for mods that attach HUD elements to it.").define("replaceHotbar",true);
        COMPASS_Y=b.comment("Additional compass vertical offset in GUI pixels, relative to 12 pixels from the top. Other mods do not shift the compass automatically.").defineInRange("compassYOffset",0,-200,400);
        HUD_LIFT=b.comment("Lift survival bars in GUI pixels above their default location.").defineInRange("survivalBarsLift",0,0,250);
        HOTBAR_X=b.defineInRange("hotbarXOffset",0,-500,500);
        HOTBAR_Y=b.defineInRange("hotbarYOffset",0,-300,300);
        b.pop();
        SPEC=b.build();
    }
    public static boolean active() { return ENABLED.get(); }
    public static boolean ownsHud() { return active() && HUD.get() && (HUD_OWNER.get()==HudOwner.DEEP_NIGHT || HUD_OWNER.get()==HudOwner.AUTO && !dev.nordui.compat.UiCompat.rpgHud()); }
}
