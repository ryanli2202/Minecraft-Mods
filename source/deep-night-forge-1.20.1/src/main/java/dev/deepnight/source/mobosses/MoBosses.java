package dev.deepnight.source.mobosses;
import dev.deepnight.source.mobosses.entity.custom.*;
import net.minecraft.world.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
public final class MoBosses {
public static final net.minecraftforge.registries.DeferredRegister<SoundEvent> SOUNDS=net.minecraftforge.registries.DeferredRegister.create(net.minecraftforge.registries.ForgeRegistries.SOUND_EVENTS,"mobosses");
public static final net.minecraftforge.registries.RegistryObject<SoundEvent> DRAGON_AMBIENT=SOUNDS.register("divine_dragon_ambient",()->SoundEvent.createVariableRangeEvent(new ResourceLocation("mobosses","divine_dragon_ambient")));
public static final net.minecraftforge.registries.RegistryObject<SoundEvent> DRAGON_BEAM=SOUNDS.register("divine_dragon_beam",()->SoundEvent.createVariableRangeEvent(new ResourceLocation("mobosses","divine_dragon_beam")));
public static final net.minecraftforge.registries.RegistryObject<SoundEvent> DRAGON_GROWL=SOUNDS.register("divine_dragon_growl",()->SoundEvent.createVariableRangeEvent(new ResourceLocation("mobosses","divine_dragon_growl")));
public static final net.minecraftforge.registries.RegistryObject<SoundEvent> DRAGON_MUSIC=SOUNDS.register("divine_dragon_music",()->SoundEvent.createVariableRangeEvent(new ResourceLocation("mobosses","divine_dragon_music")));
public static final net.minecraftforge.registries.RegistryObject<SoundEvent> DEMON_KING_MUSIC=SOUNDS.register("demon_king_music",()->SoundEvent.createVariableRangeEvent(new ResourceLocation("mobosses","demon_king_music")));
public static final net.minecraftforge.registries.RegistryObject<SoundEvent> DEMON_KING_HURT=SOUNDS.register("demon_king_hurt",()->SoundEvent.createVariableRangeEvent(new ResourceLocation("mobosses","demon_king_hurt")));
public static final net.minecraftforge.registries.RegistryObject<SoundEvent> DEMON_KING_AMBIENT=SOUNDS.register("demon_king_growl",()->SoundEvent.createVariableRangeEvent(new ResourceLocation("mobosses","demon_king_growl")));
public static final net.minecraftforge.registries.RegistryObject<SoundEvent> NIL_GRUNT=SOUNDS.register("nil_grunt",()->SoundEvent.createVariableRangeEvent(new ResourceLocation("mobosses","nil_grunt")));
public static final net.minecraftforge.registries.RegistryObject<SoundEvent> NIL_GRUNT_BIG=SOUNDS.register("nil_grunt_big",()->SoundEvent.createVariableRangeEvent(new ResourceLocation("mobosses","nil_grunt_big")));
public static final net.minecraftforge.registries.RegistryObject<SoundEvent> NIL_HURT=SOUNDS.register("nil_hurt",()->SoundEvent.createVariableRangeEvent(new ResourceLocation("mobosses","nil_hurt")));
public static final net.minecraftforge.registries.RegistryObject<SoundEvent> NIL_DEATH=SOUNDS.register("nil_death",()->SoundEvent.createVariableRangeEvent(new ResourceLocation("mobosses","nil_death")));
public static final net.minecraftforge.registries.RegistryObject<SoundEvent> NIL_SWING=SOUNDS.register("nil_swing",()->SoundEvent.createVariableRangeEvent(new ResourceLocation("mobosses","nil_swing")));
public static final net.minecraftforge.registries.RegistryObject<SoundEvent> NIL_SWORD_HIT=SOUNDS.register("nil_sword_hit",()->SoundEvent.createVariableRangeEvent(new ResourceLocation("mobosses","nil_sword_hit")));
public static final net.minecraftforge.registries.RegistryObject<SoundEvent> NIL_GROUND_BREAK=SOUNDS.register("nil_ground_break",()->SoundEvent.createVariableRangeEvent(new ResourceLocation("mobosses","nil_ground_break")));
public static final net.minecraftforge.registries.RegistryObject<SoundEvent> NIL_YELL=SOUNDS.register("nil_yell",()->SoundEvent.createVariableRangeEvent(new ResourceLocation("mobosses","nil_yell")));
public static final java.util.function.Supplier<EntityType<DivineDragonEntity>> DIVINE_DRAGON=()->(EntityType<DivineDragonEntity>)(EntityType<?>)dev.deepnight.DeepNight.HOSTILES.get(dev.deepnight.Species.MOBOSSES_DIVINE_DRAGON).get();
public static final java.util.function.Supplier<EntityType<RoyalKnightEntity>> ROYAL_KNIGHT=()->(EntityType<RoyalKnightEntity>)(EntityType<?>)dev.deepnight.DeepNight.HOSTILES.get(dev.deepnight.Species.MOBOSSES_ROYAL_KNIGHT).get();
public static final java.util.function.Supplier<EntityType<RoyalGuardianEntity>> ROYAL_GUARDIAN=()->(EntityType<RoyalGuardianEntity>)(EntityType<?>)dev.deepnight.DeepNight.HOSTILES.get(dev.deepnight.Species.MOBOSSES_ROYAL_GUARDIAN).get();
public static final java.util.function.Supplier<EntityType<DemonKingEntity>> DEMON_KING=()->(EntityType<DemonKingEntity>)(EntityType<?>)dev.deepnight.DeepNight.HOSTILES.get(dev.deepnight.Species.MOBOSSES_DEMON_KING).get();
public static final java.util.function.Supplier<EntityType<DemonKingMinionEntity>> DEMON_KING_MINION=()->(EntityType<DemonKingMinionEntity>)(EntityType<?>)dev.deepnight.DeepNight.HOSTILES.get(dev.deepnight.Species.MOBOSSES_DEMON_KING_MINION).get();
public static final net.minecraftforge.registries.RegistryObject<EntityType<DemonKingLightningEntity>> DEMON_KING_LIGHTNING=dev.deepnight.DeepNight.ENTITIES.register(
      "mobosses_demon_king_lightning", () -> EntityType.Builder.of(DemonKingLightningEntity::new, MobCategory.MISC).sized(1.5F, 10.0F).build("demon_king_lightning")
   );
public static final java.util.function.Supplier<EntityType<ForestGuardianEntity>> FOREST_GUARDIAN=()->(EntityType<ForestGuardianEntity>)(EntityType<?>)dev.deepnight.DeepNight.HOSTILES.get(dev.deepnight.Species.MOBOSSES_FOREST_GUARDIAN).get();
public static final net.minecraftforge.registries.RegistryObject<EntityType<ForestGuardianAuraEntity>> FOREST_GUARDIAN_AURA=dev.deepnight.DeepNight.ENTITIES.register(
      "mobosses_forest_guardian_aura", () -> EntityType.Builder.of(ForestGuardianAuraEntity::new, MobCategory.MISC).sized(10.0F, 1.0F).build("forest_guardian_aura")
   );
public static final java.util.function.Supplier<EntityType<HerobrineEntity>> HEROBRINE=()->(EntityType<HerobrineEntity>)(EntityType<?>)dev.deepnight.DeepNight.HOSTILES.get(dev.deepnight.Species.MOBOSSES_HEROBRINE).get();
public static final java.util.function.Supplier<EntityType<NilEntity>> NIL=()->(EntityType<NilEntity>)(EntityType<?>)dev.deepnight.DeepNight.HOSTILES.get(dev.deepnight.Species.MOBOSSES_NIL).get();
public static void register(net.minecraftforge.eventbus.api.IEventBus bus){SOUNDS.register(bus);}
}
