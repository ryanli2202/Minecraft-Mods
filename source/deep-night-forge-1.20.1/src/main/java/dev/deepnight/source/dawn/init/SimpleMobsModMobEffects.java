package dev.deepnight.source.dawn.init;
import dev.deepnight.source.dawn.potion.ChainedMobEffect;
import dev.deepnight.source.dawn.potion.DragonSummonedMobEffect;
import dev.deepnight.source.dawn.potion.GrabMobEffect;
import dev.deepnight.source.dawn.potion.ShockMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
public final class SimpleMobsModMobEffects {
public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, "simple_mobs");
public static final RegistryObject<MobEffect> CHAINED=REGISTRY.register("chained", () -> new ChainedMobEffect());
public static final RegistryObject<MobEffect> DRAGON_SUMMONED=REGISTRY.register("dragon_summoned", () -> new DragonSummonedMobEffect());
public static final RegistryObject<MobEffect> GRAB=REGISTRY.register("grab", () -> new GrabMobEffect());
public static final RegistryObject<MobEffect> SHOCK=REGISTRY.register("shock", () -> new ShockMobEffect());

}
