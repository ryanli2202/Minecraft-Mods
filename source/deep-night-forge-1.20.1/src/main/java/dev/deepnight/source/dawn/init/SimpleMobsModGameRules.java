package dev.deepnight.source.dawn.init;
import net.minecraft.world.level.GameRules;
public final class SimpleMobsModGameRules {
// Force class initialization during mod construction, before worlds copy the rule registry.
public static void init() {}
public static final GameRules.Key<GameRules.BooleanValue> DODCBOSSESRESPAWN=GameRules.register("deepnightSourceBossesRespawn",GameRules.Category.MOBS,GameRules.BooleanValue.create(false));
}
