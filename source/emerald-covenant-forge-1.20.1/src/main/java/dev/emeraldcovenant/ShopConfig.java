package dev.emeraldcovenant;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public final class ShopConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.BooleanValue REQUIRE_BEARINGS;
    public static final ForgeConfigSpec.DoubleValue PRICE_MULTIPLIER;
    public static final ForgeConfigSpec.IntValue MAX_PRICE;
    private static final ForgeConfigSpec.IntValue PRICING_VERSION;
    static {
        var b=new ForgeConfigSpec.Builder();
        REQUIRE_BEARINGS=b.comment("Require offered bell bearings for advanced merchant stock. All stock remains visible.").define("requireBellBearings",true);
        PRICE_MULTIPLIER=b.comment("Multiplier applied to generated prices, including food and bearings. Every unit price is still limited by maximumPrice.").defineInRange("priceMultiplier",1.0,0.05,100.0);
        MAX_PRICE=b.comment("Maximum emerald cost per item, including JSON overrides. Default 640; never more than 999. Legacy 2048 settings are corrected to 640 on load.").defineInRange("maximumPrice",640,1,999);
        PRICING_VERSION=b.comment("Internal economy migration marker. Updated automatically; leave this value unchanged.").defineInRange("pricingVersion",0,0,1);
        SPEC=b.build();
    }
    public static void loaded(ModConfigEvent.Loading event) {
        if(event.getConfig().getSpec()!=SPEC||PRICING_VERSION.get()!=0)return;
        // Forge clamps an old out-of-range value to 999 rather than to the new default.
        // Perform the one-time migration explicitly; subsequent deliberate edits are preserved.
        MAX_PRICE.set(Math.min(640,MAX_PRICE.get()));
        PRICING_VERSION.set(1);
        event.getConfig().save();
        Covenant.LOG.info("Covenant economy updated: maximum unit price {} emeralds",MAX_PRICE.get());
    }
}
