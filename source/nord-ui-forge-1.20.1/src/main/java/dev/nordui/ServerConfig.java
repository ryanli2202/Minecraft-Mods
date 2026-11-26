package dev.nordui;

import net.minecraftforge.common.ForgeConfigSpec;

public final class ServerConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.BooleanValue SPELL_TAB, REQUIRE_TABLE;
    static {
        var b = new ForgeConfigSpec.Builder();
        SPELL_TAB = b.comment("Allow inventory access to Iron's inscription menu. Uses real books and scrolls.").define("spellTab", true);
        REQUIRE_TABLE = b.comment("Require an inscription table within 8 blocks for the spell tab. False allows portable inscription.").define("spellTabRequiresTable", false);
        SPEC = b.build();
    }
}
