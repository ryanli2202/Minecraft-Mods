package dev.deepnight.source.dawn.init;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
public final class SimpleMobsModParticleTypes {
public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, "simple_mobs");
public static final RegistryObject<ParticleType<?>> BURNT_FLAME=REGISTRY.register("burnt_flame", () -> new SimpleParticleType(false));
public static final RegistryObject<ParticleType<?>> DRAGON_THUNDER=REGISTRY.register("dragon_thunder", () -> new SimpleParticleType(false));
public static final RegistryObject<ParticleType<?>> DRAGON_THUNDER_LIT=REGISTRY.register("dragon_thunder_lit", () -> new SimpleParticleType(false));
public static final RegistryObject<ParticleType<?>> DRAGON_THUNDER_LIT_2=REGISTRY.register("dragon_thunder_lit_2", () -> new SimpleParticleType(false));
public static final RegistryObject<ParticleType<?>> EXPLOSION=REGISTRY.register("explosion", () -> new SimpleParticleType(true));
public static final RegistryObject<ParticleType<?>> FLAME=REGISTRY.register("flame", () -> new SimpleParticleType(true));
public static final RegistryObject<ParticleType<?>> FROST_1=REGISTRY.register("frost_1", () -> new SimpleParticleType(false));
public static final RegistryObject<ParticleType<?>> FROST_2=REGISTRY.register("frost_2", () -> new SimpleParticleType(false));
public static final RegistryObject<ParticleType<?>> LOSTEYEPARTICLE=REGISTRY.register("losteyeparticle", () -> new SimpleParticleType(false));
public static final RegistryObject<ParticleType<?>> SNOWYCLOUD=REGISTRY.register("snowycloud", () -> new SimpleParticleType(false));
public static final RegistryObject<ParticleType<?>> SPARKLE=REGISTRY.register("sparkle", () -> new SimpleParticleType(false));
public static final RegistryObject<ParticleType<?>> STONE_SWING=REGISTRY.register("stone_swing", () -> new SimpleParticleType(false));
public static final RegistryObject<ParticleType<?>> THUNDER_FLASH_1=REGISTRY.register("thunder_flash_1", () -> new SimpleParticleType(false));
public static final RegistryObject<ParticleType<?>> THUNDER_FLASH_2=REGISTRY.register("thunder_flash_2", () -> new SimpleParticleType(true));

}
