package dev.deepnight.source.dawn.init;
import dev.deepnight.source.dawn.entity.AirEntity;
import dev.deepnight.source.dawn.entity.AsharmorFireEntity;
import dev.deepnight.source.dawn.entity.AsharmorIceEntity;
import dev.deepnight.source.dawn.entity.BlackholeEntity;
import dev.deepnight.source.dawn.entity.BloodSplash1Entity;
import dev.deepnight.source.dawn.entity.BloodSplash2Entity;
import dev.deepnight.source.dawn.entity.CapedMinotaurEntity;
import dev.deepnight.source.dawn.entity.CaveDwellerEntity;
import dev.deepnight.source.dawn.entity.ClawMarkEntity;
import dev.deepnight.source.dawn.entity.CorruptedOgreEntity;
import dev.deepnight.source.dawn.entity.CubeEntity;
import dev.deepnight.source.dawn.entity.DLMultiPart2Entity;
import dev.deepnight.source.dawn.entity.DLMultiPart3Entity;
import dev.deepnight.source.dawn.entity.DLMultiPart4Entity;
import dev.deepnight.source.dawn.entity.DLMultiPart5Entity;
import dev.deepnight.source.dawn.entity.DLMultiPart6Entity;
import dev.deepnight.source.dawn.entity.DLMultiPart7Entity;
import dev.deepnight.source.dawn.entity.DragMultiPart2Entity;
import dev.deepnight.source.dawn.entity.DragMultiPart3Entity;
import dev.deepnight.source.dawn.entity.DragMultiPart4Entity;
import dev.deepnight.source.dawn.entity.DragMultiPart5Entity;
import dev.deepnight.source.dawn.entity.DragMultiPart6Entity;
import dev.deepnight.source.dawn.entity.DraggingSpellEntity;
import dev.deepnight.source.dawn.entity.DragonFlameEntity;
import dev.deepnight.source.dawn.entity.DragonLightningBreathEntity;
import dev.deepnight.source.dawn.entity.DragonLordEntity;
import dev.deepnight.source.dawn.entity.DragonSmokeEntity;
import dev.deepnight.source.dawn.entity.ElementalAttack1Entity;
import dev.deepnight.source.dawn.entity.ElementalAttack1PlayerEntity;
import dev.deepnight.source.dawn.entity.ElementalAttack2Entity;
import dev.deepnight.source.dawn.entity.ElementalAttackPlayerEntity;
import dev.deepnight.source.dawn.entity.ElementalChainEntity;
import dev.deepnight.source.dawn.entity.ElementalDeityEntity;
import dev.deepnight.source.dawn.entity.ElementalFlash1Entity;
import dev.deepnight.source.dawn.entity.ElementalFlash2Entity;
import dev.deepnight.source.dawn.entity.ElementalFlash3Entity;
import dev.deepnight.source.dawn.entity.ElementalGrabEntity;
import dev.deepnight.source.dawn.entity.ElementalProjectileEntity;
import dev.deepnight.source.dawn.entity.ElementalShieldEntity;
import dev.deepnight.source.dawn.entity.ElementalSpawnEntity;
import dev.deepnight.source.dawn.entity.EntEntity;
import dev.deepnight.source.dawn.entity.FireDragMultiPart1Entity;
import dev.deepnight.source.dawn.entity.FireDragMultiPartEntity;
import dev.deepnight.source.dawn.entity.FireDragProjEntity;
import dev.deepnight.source.dawn.entity.FireDragonAmbientEntity;
import dev.deepnight.source.dawn.entity.FireDragonEntity;
import dev.deepnight.source.dawn.entity.FireDragonGrabEntity;
import dev.deepnight.source.dawn.entity.FireGiantEntity;
import dev.deepnight.source.dawn.entity.FireballEntity;
import dev.deepnight.source.dawn.entity.FlameBallEntity;
import dev.deepnight.source.dawn.entity.GemspikeEntity;
import dev.deepnight.source.dawn.entity.GhostprojectileEntity;
import dev.deepnight.source.dawn.entity.GrabbedEntity;
import dev.deepnight.source.dawn.entity.GroundSpikeEntity;
import dev.deepnight.source.dawn.entity.GroundbreakEffectBigEntity;
import dev.deepnight.source.dawn.entity.GroundbreakEffectSmallEntity;
import dev.deepnight.source.dawn.entity.HerobrineEntity;
import dev.deepnight.source.dawn.entity.HerobrineFearEntity;
import dev.deepnight.source.dawn.entity.HerobrineFlashEntity;
import dev.deepnight.source.dawn.entity.HerobrineSpike1Entity;
import dev.deepnight.source.dawn.entity.HerobrineSpike2Entity;
import dev.deepnight.source.dawn.entity.Herobrinese2Entity;
import dev.deepnight.source.dawn.entity.Herobrinese3Entity;
import dev.deepnight.source.dawn.entity.HerobrineseEntity;
import dev.deepnight.source.dawn.entity.IceDragMultiPart1Entity;
import dev.deepnight.source.dawn.entity.IceDragMultiPart2Entity;
import dev.deepnight.source.dawn.entity.IceDragMultiPart3Entity;
import dev.deepnight.source.dawn.entity.IceDragMultiPart4Entity;
import dev.deepnight.source.dawn.entity.IceDragMultiPart5Entity;
import dev.deepnight.source.dawn.entity.IceDragMultiPart6Entity;
import dev.deepnight.source.dawn.entity.IceDragMultiPartEntity;
import dev.deepnight.source.dawn.entity.IceDragonAmbientEntity;
import dev.deepnight.source.dawn.entity.IceDragonBreathEntity;
import dev.deepnight.source.dawn.entity.IceDragonEntity;
import dev.deepnight.source.dawn.entity.IceDragonProjectileEntity;
import dev.deepnight.source.dawn.entity.Korok1Entity;
import dev.deepnight.source.dawn.entity.Korok2Entity;
import dev.deepnight.source.dawn.entity.Korok3Entity;
import dev.deepnight.source.dawn.entity.Korok4Entity;
import dev.deepnight.source.dawn.entity.Korok5BalloonEntity;
import dev.deepnight.source.dawn.entity.Korok5Entity;
import dev.deepnight.source.dawn.entity.LavaBallEntity;
import dev.deepnight.source.dawn.entity.LightningDragMultiPart1Entity;
import dev.deepnight.source.dawn.entity.LightningDragMultiPart2Entity;
import dev.deepnight.source.dawn.entity.LightningDragMultiPart3Entity;
import dev.deepnight.source.dawn.entity.LightningDragMultiPart4Entity;
import dev.deepnight.source.dawn.entity.LightningDragMultiPart5Entity;
import dev.deepnight.source.dawn.entity.LightningDragMultiPart6Entity;
import dev.deepnight.source.dawn.entity.LightningDragMultiPart7Entity;
import dev.deepnight.source.dawn.entity.LightningDragMultiPartEntity;
import dev.deepnight.source.dawn.entity.LightningDragonAmbientEntity;
import dev.deepnight.source.dawn.entity.LightningDragonEntity;
import dev.deepnight.source.dawn.entity.LightningSpearEntity;
import dev.deepnight.source.dawn.entity.LostEyeOgreEntity;
import dev.deepnight.source.dawn.entity.LosteyeEffect1Entity;
import dev.deepnight.source.dawn.entity.LosteyeEffect2Entity;
import dev.deepnight.source.dawn.entity.LosteyeEffect3Entity;
import dev.deepnight.source.dawn.entity.LosteyeprojEntity;
import dev.deepnight.source.dawn.entity.MartianEntity;
import dev.deepnight.source.dawn.entity.MeteorAttackEntity;
import dev.deepnight.source.dawn.entity.MeteorgroundeffectEntity;
import dev.deepnight.source.dawn.entity.MossGolemEntity;
import dev.deepnight.source.dawn.entity.MultiPartLegEntity;
import dev.deepnight.source.dawn.entity.Multipartbody2Entity;
import dev.deepnight.source.dawn.entity.MultipartbodyEntity;
import dev.deepnight.source.dawn.entity.NineTailsEntity;
import dev.deepnight.source.dawn.entity.NotchBossEntity;
import dev.deepnight.source.dawn.entity.NotchFlash5Entity;
import dev.deepnight.source.dawn.entity.NotchGrabEntity;
import dev.deepnight.source.dawn.entity.NotchPunchEntity;
import dev.deepnight.source.dawn.entity.NotchStompeffectEntity;
import dev.deepnight.source.dawn.entity.Notchflash1Entity;
import dev.deepnight.source.dawn.entity.Notchflash2Entity;
import dev.deepnight.source.dawn.entity.Notchflash3Entity;
import dev.deepnight.source.dawn.entity.Notchflash4Entity;
import dev.deepnight.source.dawn.entity.Notchgroundeffect1Entity;
import dev.deepnight.source.dawn.entity.Notchgroundeffect2Entity;
import dev.deepnight.source.dawn.entity.Notchgroundeffect3Entity;
import dev.deepnight.source.dawn.entity.Notchgroundeffect4Entity;
import dev.deepnight.source.dawn.entity.Projectile2Entity;
import dev.deepnight.source.dawn.entity.ProjectileMagicEntity;
import dev.deepnight.source.dawn.entity.RoarEntity;
import dev.deepnight.source.dawn.entity.RodParticlesEntity;
import dev.deepnight.source.dawn.entity.RumbleEntity;
import dev.deepnight.source.dawn.entity.SeedGunEntity;
import dev.deepnight.source.dawn.entity.SentinelFlashEntity;
import dev.deepnight.source.dawn.entity.SentinelKnightEntity;
import dev.deepnight.source.dawn.entity.SentinelSlashEntity;
import dev.deepnight.source.dawn.entity.ShogunateEffect5Entity;
import dev.deepnight.source.dawn.entity.ShogunateEffect6Entity;
import dev.deepnight.source.dawn.entity.ShogunateEffect7Entity;
import dev.deepnight.source.dawn.entity.ShogunateEffect8Entity;
import dev.deepnight.source.dawn.entity.SkeletonlordEntity;
import dev.deepnight.source.dawn.entity.Spell1Entity;
import dev.deepnight.source.dawn.entity.Spell2Entity;
import dev.deepnight.source.dawn.entity.Spell3Entity;
import dev.deepnight.source.dawn.entity.SphereEntity;
import dev.deepnight.source.dawn.entity.SproutEntity;
import dev.deepnight.source.dawn.entity.StaffInteractEntity;
import dev.deepnight.source.dawn.entity.SummonDragEntity;
import dev.deepnight.source.dawn.entity.SwordAutoEntity;
import dev.deepnight.source.dawn.entity.SwordProjectileEntity;
import dev.deepnight.source.dawn.entity.SwordThrownEntity;
import dev.deepnight.source.dawn.entity.TailDartEntity;
import dev.deepnight.source.dawn.entity.Thundereffect1Entity;
import dev.deepnight.source.dawn.entity.Thundereffect2Entity;
import dev.deepnight.source.dawn.entity.Thundereffect3Entity;
import dev.deepnight.source.dawn.entity.Thundereffect4Entity;
import dev.deepnight.source.dawn.entity.TrackingThunderEntity;
import dev.deepnight.source.dawn.entity.TreeDrainParticlesEntity;
import dev.deepnight.source.dawn.entity.WhispererStem2Entity;
import dev.deepnight.source.dawn.entity.WindEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
public final class SimpleMobsModEntities {
public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "simple_mobs");
public static final RegistryObject<EntityType<AirEntity>> AIR=register(
      "air",
      Builder.<AirEntity>of(AirEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(AirEntity::new)
         .sized(0.6F, 1.8F)
   );
public static final RegistryObject<EntityType<AsharmorFireEntity>> ASHARMOR_FIRE=register(
      "asharmor_fire",
      Builder.<AsharmorFireEntity>of(AsharmorFireEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(AsharmorFireEntity::new)
         .sized(0.6F, 0.6F)
   );
public static final RegistryObject<EntityType<AsharmorIceEntity>> ASHARMOR_ICE=register(
      "asharmor_ice",
      Builder.<AsharmorIceEntity>of(AsharmorIceEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(AsharmorIceEntity::new)
         .sized(0.6F, 0.6F)
   );
public static final RegistryObject<EntityType<BlackholeEntity>> BLACKHOLE=register(
      "blackhole",
      Builder.<BlackholeEntity>of(BlackholeEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(BlackholeEntity::new)
         .sized(3.0F, 3.0F)
   );
public static final RegistryObject<EntityType<BloodSplash1Entity>> BLOOD_SPLASH_1=register(
      "blood_splash_1",
      Builder.<BloodSplash1Entity>of(BloodSplash1Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(BloodSplash1Entity::new)
         .sized(2.0F, 2.0F)
   );
public static final RegistryObject<EntityType<BloodSplash2Entity>> BLOOD_SPLASH_2=register(
      "blood_splash_2",
      Builder.<BloodSplash2Entity>of(BloodSplash2Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(BloodSplash2Entity::new)
         .sized(2.0F, 2.0F)
   );
public static final RegistryObject<EntityType<CapedMinotaurEntity>> CAPED_MINOTAUR=register(
      "caped_minotaur",
      Builder.<CapedMinotaurEntity>of(CapedMinotaurEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(CapedMinotaurEntity::new)
         .sized(0.6F, 1.8F)
   );
public static final RegistryObject<EntityType<CaveDwellerEntity>> CAVE_DWELLER=register(
      "cave_dweller",
      Builder.<CaveDwellerEntity>of(CaveDwellerEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(CaveDwellerEntity::new)
         .sized(2.0F, 3.5F)
   );
public static final RegistryObject<EntityType<ClawMarkEntity>> CLAW_MARK=register(
      "claw_mark",
      Builder.<ClawMarkEntity>of(ClawMarkEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(ClawMarkEntity::new)
         .sized(1.0F, 1.0F)
   );
public static final java.util.function.Supplier<EntityType<CorruptedOgreEntity>> CORRUPTED_OGRE=()->(EntityType<CorruptedOgreEntity>)(EntityType<?>)dev.deepnight.DeepNight.HOSTILES.get(dev.deepnight.Species.DAWN_CORRUPTED_OGRE).get();
public static final RegistryObject<EntityType<CubeEntity>> CUBE=register(
      "cube",
      Builder.<CubeEntity>of(CubeEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(CubeEntity::new)
         .sized(0.6F, 1.8F)
   );
public static final RegistryObject<EntityType<DLMultiPart2Entity>> DL_MULTI_PART_2=register(
      "dl_multi_part_2",
      Builder.<DLMultiPart2Entity>of(DLMultiPart2Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(DLMultiPart2Entity::new)
         .sized(10.0F, 6.0F)
   );
public static final RegistryObject<EntityType<DLMultiPart3Entity>> DL_MULTI_PART_3=register(
      "dl_multi_part_3",
      Builder.<DLMultiPart3Entity>of(DLMultiPart3Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(DLMultiPart3Entity::new)
         .sized(7.0F, 6.0F)
   );
public static final RegistryObject<EntityType<DLMultiPart4Entity>> DL_MULTI_PART_4=register(
      "dl_multi_part_4",
      Builder.<DLMultiPart4Entity>of(DLMultiPart4Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(DLMultiPart4Entity::new)
         .sized(7.0F, 6.0F)
   );
public static final RegistryObject<EntityType<DLMultiPart5Entity>> DL_MULTI_PART_5=register(
      "dl_multi_part_5",
      Builder.<DLMultiPart5Entity>of(DLMultiPart5Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(DLMultiPart5Entity::new)
         .sized(7.0F, 6.0F)
   );
public static final RegistryObject<EntityType<DLMultiPart6Entity>> DL_MULTI_PART_6=register(
      "dl_multi_part_6",
      Builder.<DLMultiPart6Entity>of(DLMultiPart6Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(DLMultiPart6Entity::new)
         .sized(6.0F, 10.0F)
   );
public static final RegistryObject<EntityType<DLMultiPart7Entity>> DL_MULTI_PART_7=register(
      "dl_multi_part_7",
      Builder.<DLMultiPart7Entity>of(DLMultiPart7Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(DLMultiPart7Entity::new)
         .sized(5.0F, 10.0F)
   );
public static final RegistryObject<EntityType<DraggingSpellEntity>> DRAGGING_SPELL=register(
      "dragging_spell",
      Builder.<DraggingSpellEntity>of(DraggingSpellEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(DraggingSpellEntity::new)
         .fireImmune()
         .sized(0.6F, 1.8F)
   );
public static final RegistryObject<EntityType<DragonFlameEntity>> DRAGON_FLAME=register(
      "dragon_flame",
      Builder.<DragonFlameEntity>of(DragonFlameEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(DragonFlameEntity::new)
         .fireImmune()
         .sized(0.5F, 0.5F)
   );
public static final RegistryObject<EntityType<DragonLightningBreathEntity>> DRAGON_LIGHTNING_BREATH=register(
      "dragon_lightning_breath",
      Builder.<DragonLightningBreathEntity>of(DragonLightningBreathEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(DragonLightningBreathEntity::new)
         .fireImmune()
         .sized(0.5F, 0.5F)
   );
public static final java.util.function.Supplier<EntityType<DragonLordEntity>> DRAGON_LORD=()->(EntityType<DragonLordEntity>)(EntityType<?>)dev.deepnight.DeepNight.HOSTILES.get(dev.deepnight.Species.DAWN_DRAGON_LORD).get();
public static final RegistryObject<EntityType<DragonSmokeEntity>> DRAGON_SMOKE=register(
      "dragon_smoke",
      Builder.<DragonSmokeEntity>of(DragonSmokeEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(DragonSmokeEntity::new)
         .sized(1.0F, 1.0F)
   );
public static final RegistryObject<EntityType<DragMultiPart2Entity>> DRAG_MULTI_PART_2=register(
      "drag_multi_part_2",
      Builder.<DragMultiPart2Entity>of(DragMultiPart2Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(DragMultiPart2Entity::new)
         .sized(6.0F, 5.0F)
   );
public static final RegistryObject<EntityType<DragMultiPart3Entity>> DRAG_MULTI_PART_3=register(
      "drag_multi_part_3",
      Builder.<DragMultiPart3Entity>of(DragMultiPart3Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(DragMultiPart3Entity::new)
         .sized(6.0F, 5.0F)
   );
public static final RegistryObject<EntityType<DragMultiPart4Entity>> DRAG_MULTI_PART_4=register(
      "drag_multi_part_4",
      Builder.<DragMultiPart4Entity>of(DragMultiPart4Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(DragMultiPart4Entity::new)
         .sized(6.0F, 5.0F)
   );
public static final RegistryObject<EntityType<DragMultiPart5Entity>> DRAG_MULTI_PART_5=register(
      "drag_multi_part_5",
      Builder.<DragMultiPart5Entity>of(DragMultiPart5Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(DragMultiPart5Entity::new)
         .sized(6.0F, 5.0F)
   );
public static final RegistryObject<EntityType<DragMultiPart6Entity>> DRAG_MULTI_PART_6=register(
      "drag_multi_part_6",
      Builder.<DragMultiPart6Entity>of(DragMultiPart6Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(DragMultiPart6Entity::new)
         .sized(5.0F, 9.0F)
   );
public static final RegistryObject<EntityType<ElementalAttack1Entity>> ELEMENTAL_ATTACK_1=register(
      "elemental_attack_1",
      Builder.<ElementalAttack1Entity>of(ElementalAttack1Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(ElementalAttack1Entity::new)
         .sized(1.0F, 2.0F)
   );
public static final RegistryObject<EntityType<ElementalAttack1PlayerEntity>> ELEMENTAL_ATTACK_1_PLAYER=register(
      "elemental_attack_1_player",
      Builder.<ElementalAttack1PlayerEntity>of(ElementalAttack1PlayerEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(ElementalAttack1PlayerEntity::new)
         .sized(1.0F, 2.0F)
   );
public static final RegistryObject<EntityType<ElementalAttack2Entity>> ELEMENTAL_ATTACK_2=register(
      "elemental_attack_2",
      Builder.<ElementalAttack2Entity>of(ElementalAttack2Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(ElementalAttack2Entity::new)
         .sized(0.6F, 1.8F)
   );
public static final RegistryObject<EntityType<ElementalAttackPlayerEntity>> ELEMENTAL_ATTACK_PLAYER=register(
      "elemental_attack_player",
      Builder.<ElementalAttackPlayerEntity>of(ElementalAttackPlayerEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(ElementalAttackPlayerEntity::new)
         .sized(0.6F, 1.8F)
   );
public static final RegistryObject<EntityType<ElementalChainEntity>> ELEMENTAL_CHAIN=register(
      "elemental_chain",
      Builder.<ElementalChainEntity>of(ElementalChainEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(ElementalChainEntity::new)
         .sized(3.0F, 3.0F)
   );
public static final java.util.function.Supplier<EntityType<ElementalDeityEntity>> ELEMENTAL_DEITY=()->(EntityType<ElementalDeityEntity>)(EntityType<?>)dev.deepnight.DeepNight.HOSTILES.get(dev.deepnight.Species.DAWN_ELEMENTAL_DEITY).get();
public static final RegistryObject<EntityType<ElementalFlash1Entity>> ELEMENTAL_FLASH_1=register(
      "elemental_flash_1",
      Builder.<ElementalFlash1Entity>of(ElementalFlash1Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(ElementalFlash1Entity::new)
         .sized(1.0F, 1.0F)
   );
public static final RegistryObject<EntityType<ElementalFlash2Entity>> ELEMENTAL_FLASH_2=register(
      "elemental_flash_2",
      Builder.<ElementalFlash2Entity>of(ElementalFlash2Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(ElementalFlash2Entity::new)
         .sized(1.5F, 1.5F)
   );
public static final RegistryObject<EntityType<ElementalFlash3Entity>> ELEMENTAL_FLASH_3=register(
      "elemental_flash_3",
      Builder.<ElementalFlash3Entity>of(ElementalFlash3Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(ElementalFlash3Entity::new)
         .sized(1.5F, 1.5F)
   );
public static final RegistryObject<EntityType<ElementalGrabEntity>> ELEMENTAL_GRAB=register(
      "elemental_grab",
      Builder.<ElementalGrabEntity>of(ElementalGrabEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(ElementalGrabEntity::new)
         .sized(2.0F, 2.0F)
   );
public static final RegistryObject<EntityType<ElementalProjectileEntity>> ELEMENTAL_PROJECTILE=register(
      "elemental_projectile",
      Builder.<ElementalProjectileEntity>of(ElementalProjectileEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(ElementalProjectileEntity::new)
         .sized(0.6F, 0.6F)
   );
public static final RegistryObject<EntityType<ElementalShieldEntity>> ELEMENTAL_SHIELD=register(
      "elemental_shield",
      Builder.<ElementalShieldEntity>of(ElementalShieldEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(ElementalShieldEntity::new)
         .sized(3.0F, 3.0F)
   );
public static final RegistryObject<EntityType<ElementalSpawnEntity>> ELEMENTAL_SPAWN=register(
      "elemental_spawn",
      Builder.<ElementalSpawnEntity>of(ElementalSpawnEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(ElementalSpawnEntity::new)
         .sized(4.0F, 4.0F)
   );
public static final java.util.function.Supplier<EntityType<EntEntity>> ENT=()->(EntityType<EntEntity>)(EntityType<?>)dev.deepnight.DeepNight.HOSTILES.get(dev.deepnight.Species.DAWN_ENT).get();
public static final RegistryObject<EntityType<FireballEntity>> FIREBALL=register(
      "fireball",
      Builder.<FireballEntity>of(FireballEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(FireballEntity::new)
         .fireImmune()
         .sized(0.2F, 0.2F)
   );
public static final java.util.function.Supplier<EntityType<FireDragonEntity>> FIRE_DRAGON=()->(EntityType<FireDragonEntity>)(EntityType<?>)dev.deepnight.DeepNight.HOSTILES.get(dev.deepnight.Species.DAWN_FIRE_DRAGON).get();
public static final RegistryObject<EntityType<FireDragonAmbientEntity>> FIRE_DRAGON_AMBIENT=register(
      "fire_dragon_ambient",
      Builder.<FireDragonAmbientEntity>of(FireDragonAmbientEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(FireDragonAmbientEntity::new)
         .sized(8.0F, 8.0F)
   );
public static final RegistryObject<EntityType<FireDragonGrabEntity>> FIRE_DRAGON_GRAB=register(
      "fire_dragon_grab",
      Builder.<FireDragonGrabEntity>of(FireDragonGrabEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(FireDragonGrabEntity::new)
         .fireImmune()
         .sized(0.6F, 1.8F)
   );
public static final RegistryObject<EntityType<FireDragMultiPartEntity>> FIRE_DRAG_MULTI_PART=register(
      "fire_drag_multi_part",
      Builder.<FireDragMultiPartEntity>of(FireDragMultiPartEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(FireDragMultiPartEntity::new)
         .sized(3.0F, 2.0F)
   );
public static final RegistryObject<EntityType<FireDragMultiPart1Entity>> FIRE_DRAG_MULTI_PART_1=register(
      "fire_drag_multi_part_1",
      Builder.<FireDragMultiPart1Entity>of(FireDragMultiPart1Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(FireDragMultiPart1Entity::new)
         .sized(4.0F, 2.0F)
   );
public static final RegistryObject<EntityType<FireDragProjEntity>> FIRE_DRAG_PROJ=register(
      "fire_drag_proj",
      Builder.<FireDragProjEntity>of(FireDragProjEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(FireDragProjEntity::new)
         .fireImmune()
         .sized(0.6F, 1.8F)
   );
public static final java.util.function.Supplier<EntityType<FireGiantEntity>> FIRE_GIANT=()->(EntityType<FireGiantEntity>)(EntityType<?>)dev.deepnight.DeepNight.HOSTILES.get(dev.deepnight.Species.DAWN_FIRE_GIANT).get();
public static final RegistryObject<EntityType<FlameBallEntity>> FLAME_BALL=register(
      "flame_ball",
      Builder.<FlameBallEntity>of(FlameBallEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(FlameBallEntity::new)
         .sized(1.0F, 1.0F)
   );
public static final RegistryObject<EntityType<GemspikeEntity>> GEMSPIKE=register(
      "gemspike",
      Builder.<GemspikeEntity>of(GemspikeEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(GemspikeEntity::new)
         .fireImmune()
         .sized(0.6F, 1.8F)
   );
public static final RegistryObject<EntityType<GhostprojectileEntity>> GHOSTPROJECTILE=register(
      "ghostprojectile",
      Builder.<GhostprojectileEntity>of(GhostprojectileEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(GhostprojectileEntity::new)
         .sized(0.6F, 0.6F)
   );
public static final RegistryObject<EntityType<GrabbedEntity>> GRABBED=register(
      "grabbed",
      Builder.<GrabbedEntity>of(GrabbedEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(GrabbedEntity::new)
         .fireImmune()
         .sized(0.6F, 1.8F)
   );
public static final RegistryObject<EntityType<GroundbreakEffectBigEntity>> GROUNDBREAK_EFFECT_BIG=register(
      "groundbreak_effect_big",
      Builder.<GroundbreakEffectBigEntity>of(GroundbreakEffectBigEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(GroundbreakEffectBigEntity::new)
         .sized(0.6F, 1.8F)
   );
public static final RegistryObject<EntityType<GroundbreakEffectSmallEntity>> GROUNDBREAK_EFFECT_SMALL=register(
      "groundbreak_effect_small",
      Builder.<GroundbreakEffectSmallEntity>of(GroundbreakEffectSmallEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(GroundbreakEffectSmallEntity::new)
         .sized(0.6F, 1.8F)
   );
public static final RegistryObject<EntityType<GroundSpikeEntity>> GROUND_SPIKE=register(
      "ground_spike",
      Builder.<GroundSpikeEntity>of(GroundSpikeEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(GroundSpikeEntity::new)
         .fireImmune()
         .sized(0.5F, 0.5F)
   );
public static final java.util.function.Supplier<EntityType<HerobrineEntity>> HEROBRINE=()->(EntityType<HerobrineEntity>)(EntityType<?>)dev.deepnight.DeepNight.HOSTILES.get(dev.deepnight.Species.DAWN_HEROBRINE).get();
public static final RegistryObject<EntityType<HerobrineseEntity>> HEROBRINESE=register(
      "herobrinese",
      Builder.<HerobrineseEntity>of(HerobrineseEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(HerobrineseEntity::new)
         .sized(1.0F, 1.0F)
   );
public static final RegistryObject<EntityType<Herobrinese2Entity>> HEROBRINESE_2=register(
      "herobrinese_2",
      Builder.<Herobrinese2Entity>of(Herobrinese2Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(Herobrinese2Entity::new)
         .sized(1.0F, 1.0F)
   );
public static final RegistryObject<EntityType<Herobrinese3Entity>> HEROBRINESE_3=register(
      "herobrinese_3",
      Builder.<Herobrinese3Entity>of(Herobrinese3Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(Herobrinese3Entity::new)
         .sized(1.0F, 1.0F)
   );
public static final RegistryObject<EntityType<HerobrineFearEntity>> HEROBRINE_FEAR=register(
      "herobrine_fear",
      Builder.<HerobrineFearEntity>of(HerobrineFearEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(HerobrineFearEntity::new)
         .sized(5.0F, 20.0F)
   );
public static final RegistryObject<EntityType<HerobrineFlashEntity>> HEROBRINE_FLASH=register(
      "herobrine_flash",
      Builder.<HerobrineFlashEntity>of(HerobrineFlashEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(HerobrineFlashEntity::new)
         .sized(1.0F, 1.0F)
   );
public static final RegistryObject<EntityType<HerobrineSpike1Entity>> HEROBRINE_SPIKE_1=register(
      "herobrine_spike_1",
      Builder.<HerobrineSpike1Entity>of(HerobrineSpike1Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(HerobrineSpike1Entity::new)
         .sized(1.0F, 1.0F)
   );
public static final RegistryObject<EntityType<HerobrineSpike2Entity>> HEROBRINE_SPIKE_2=register(
      "herobrine_spike_2",
      Builder.<HerobrineSpike2Entity>of(HerobrineSpike2Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(HerobrineSpike2Entity::new)
         .sized(1.0F, 1.0F)
   );
public static final java.util.function.Supplier<EntityType<IceDragonEntity>> ICE_DRAGON=()->(EntityType<IceDragonEntity>)(EntityType<?>)dev.deepnight.DeepNight.HOSTILES.get(dev.deepnight.Species.DAWN_ICE_DRAGON).get();
public static final RegistryObject<EntityType<IceDragonAmbientEntity>> ICE_DRAGON_AMBIENT=register(
      "ice_dragon_ambient",
      Builder.<IceDragonAmbientEntity>of(IceDragonAmbientEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(IceDragonAmbientEntity::new)
         .sized(9.0F, 9.0F)
   );
public static final RegistryObject<EntityType<IceDragonBreathEntity>> ICE_DRAGON_BREATH=register(
      "ice_dragon_breath",
      Builder.<IceDragonBreathEntity>of(IceDragonBreathEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(IceDragonBreathEntity::new)
         .fireImmune()
         .sized(0.5F, 0.5F)
   );
public static final RegistryObject<EntityType<IceDragonProjectileEntity>> ICE_DRAGON_PROJECTILE=register(
      "ice_dragon_projectile",
      Builder.<IceDragonProjectileEntity>of(IceDragonProjectileEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(IceDragonProjectileEntity::new)
         .sized(1.0F, 1.0F)
   );
public static final RegistryObject<EntityType<IceDragMultiPartEntity>> ICE_DRAG_MULTI_PART=register(
      "ice_drag_multi_part",
      Builder.<IceDragMultiPartEntity>of(IceDragMultiPartEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(IceDragMultiPartEntity::new)
         .sized(3.0F, 2.0F)
   );
public static final RegistryObject<EntityType<IceDragMultiPart1Entity>> ICE_DRAG_MULTI_PART_1=register(
      "ice_drag_multi_part_1",
      Builder.<IceDragMultiPart1Entity>of(IceDragMultiPart1Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(IceDragMultiPart1Entity::new)
         .sized(4.0F, 2.0F)
   );
public static final RegistryObject<EntityType<IceDragMultiPart2Entity>> ICE_DRAG_MULTI_PART_2=register(
      "ice_drag_multi_part_2",
      Builder.<IceDragMultiPart2Entity>of(IceDragMultiPart2Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(IceDragMultiPart2Entity::new)
         .sized(8.0F, 6.0F)
   );
public static final RegistryObject<EntityType<IceDragMultiPart3Entity>> ICE_DRAG_MULTI_PART_3=register(
      "ice_drag_multi_part_3",
      Builder.<IceDragMultiPart3Entity>of(IceDragMultiPart3Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(IceDragMultiPart3Entity::new)
         .sized(6.0F, 5.0F)
   );
public static final RegistryObject<EntityType<IceDragMultiPart4Entity>> ICE_DRAG_MULTI_PART_4=register(
      "ice_drag_multi_part_4",
      Builder.<IceDragMultiPart4Entity>of(IceDragMultiPart4Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(IceDragMultiPart4Entity::new)
         .sized(6.0F, 5.0F)
   );
public static final RegistryObject<EntityType<IceDragMultiPart5Entity>> ICE_DRAG_MULTI_PART_5=register(
      "ice_drag_multi_part_5",
      Builder.<IceDragMultiPart5Entity>of(IceDragMultiPart5Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(IceDragMultiPart5Entity::new)
         .sized(6.0F, 5.0F)
   );
public static final RegistryObject<EntityType<IceDragMultiPart6Entity>> ICE_DRAG_MULTI_PART_6=register(
      "ice_drag_multi_part_6",
      Builder.<IceDragMultiPart6Entity>of(IceDragMultiPart6Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(IceDragMultiPart6Entity::new)
         .sized(5.0F, 10.0F)
   );
public static final RegistryObject<EntityType<Korok1Entity>> KOROK_1=register(
      "korok_1",
      Builder.<Korok1Entity>of(Korok1Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(Korok1Entity::new)
         .sized(0.6F, 1.0F)
   );
public static final RegistryObject<EntityType<Korok2Entity>> KOROK_2=register(
      "korok_2",
      Builder.<Korok2Entity>of(Korok2Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(Korok2Entity::new)
         .sized(0.6F, 1.0F)
   );
public static final RegistryObject<EntityType<Korok3Entity>> KOROK_3=register(
      "korok_3",
      Builder.<Korok3Entity>of(Korok3Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(Korok3Entity::new)
         .sized(0.6F, 1.0F)
   );
public static final RegistryObject<EntityType<Korok4Entity>> KOROK_4=register(
      "korok_4",
      Builder.<Korok4Entity>of(Korok4Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(Korok4Entity::new)
         .sized(0.6F, 1.0F)
   );
public static final RegistryObject<EntityType<Korok5Entity>> KOROK_5=register(
      "korok_5",
      Builder.<Korok5Entity>of(Korok5Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(Korok5Entity::new)
         .sized(0.6F, 1.0F)
   );
public static final RegistryObject<EntityType<Korok5BalloonEntity>> KOROK_5_BALLOON=register(
      "korok_5_balloon",
      Builder.<Korok5BalloonEntity>of(Korok5BalloonEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(Korok5BalloonEntity::new)
         .sized(0.8F, 0.8F)
   );
public static final RegistryObject<EntityType<LavaBallEntity>> LAVA_BALL=register(
      "lava_ball",
      Builder.<LavaBallEntity>of(LavaBallEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(LavaBallEntity::new)
         .fireImmune()
         .sized(0.5F, 0.5F)
   );
public static final java.util.function.Supplier<EntityType<LightningDragonEntity>> LIGHTNING_DRAGON=()->(EntityType<LightningDragonEntity>)(EntityType<?>)dev.deepnight.DeepNight.HOSTILES.get(dev.deepnight.Species.DAWN_LIGHTNING_DRAGON).get();
public static final RegistryObject<EntityType<LightningDragonAmbientEntity>> LIGHTNING_DRAGON_AMBIENT=register(
      "lightning_dragon_ambient",
      Builder.<LightningDragonAmbientEntity>of(LightningDragonAmbientEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(LightningDragonAmbientEntity::new)
         .sized(8.0F, 8.0F)
   );
public static final RegistryObject<EntityType<LightningDragMultiPartEntity>> LIGHTNING_DRAG_MULTI_PART=register(
      "lightning_drag_multi_part",
      Builder.<LightningDragMultiPartEntity>of(LightningDragMultiPartEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(LightningDragMultiPartEntity::new)
         .sized(3.0F, 2.0F)
   );
public static final RegistryObject<EntityType<LightningDragMultiPart1Entity>> LIGHTNING_DRAG_MULTI_PART_1=register(
      "lightning_drag_multi_part_1",
      Builder.<LightningDragMultiPart1Entity>of(LightningDragMultiPart1Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(LightningDragMultiPart1Entity::new)
         .sized(4.0F, 2.0F)
   );
public static final RegistryObject<EntityType<LightningDragMultiPart2Entity>> LIGHTNING_DRAG_MULTI_PART_2=register(
      "lightning_drag_multi_part_2",
      Builder.<LightningDragMultiPart2Entity>of(LightningDragMultiPart2Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(LightningDragMultiPart2Entity::new)
         .sized(6.0F, 5.0F)
   );
public static final RegistryObject<EntityType<LightningDragMultiPart3Entity>> LIGHTNING_DRAG_MULTI_PART_3=register(
      "lightning_drag_multi_part_3",
      Builder.<LightningDragMultiPart3Entity>of(LightningDragMultiPart3Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(LightningDragMultiPart3Entity::new)
         .sized(6.0F, 5.0F)
   );
public static final RegistryObject<EntityType<LightningDragMultiPart4Entity>> LIGHTNING_DRAG_MULTI_PART_4=register(
      "lightning_drag_multi_part_4",
      Builder.<LightningDragMultiPart4Entity>of(LightningDragMultiPart4Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(LightningDragMultiPart4Entity::new)
         .sized(6.0F, 5.0F)
   );
public static final RegistryObject<EntityType<LightningDragMultiPart5Entity>> LIGHTNING_DRAG_MULTI_PART_5=register(
      "lightning_drag_multi_part_5",
      Builder.<LightningDragMultiPart5Entity>of(LightningDragMultiPart5Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(LightningDragMultiPart5Entity::new)
         .sized(6.0F, 5.0F)
   );
public static final RegistryObject<EntityType<LightningDragMultiPart6Entity>> LIGHTNING_DRAG_MULTI_PART_6=register(
      "lightning_drag_multi_part_6",
      Builder.<LightningDragMultiPart6Entity>of(LightningDragMultiPart6Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(LightningDragMultiPart6Entity::new)
         .sized(5.0F, 9.0F)
   );
public static final RegistryObject<EntityType<LightningDragMultiPart7Entity>> LIGHTNING_DRAG_MULTI_PART_7=register(
      "lightning_drag_multi_part_7",
      Builder.<LightningDragMultiPart7Entity>of(LightningDragMultiPart7Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(LightningDragMultiPart7Entity::new)
         .sized(4.0F, 9.0F)
   );
public static final RegistryObject<EntityType<LightningSpearEntity>> LIGHTNING_SPEAR=register(
      "lightning_spear",
      Builder.<LightningSpearEntity>of(LightningSpearEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(LightningSpearEntity::new)
         .sized(1.0F, 1.0F)
   );
public static final RegistryObject<EntityType<LosteyeprojEntity>> LOSTEYEPROJ=register(
      "losteyeproj",
      Builder.<LosteyeprojEntity>of(LosteyeprojEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(LosteyeprojEntity::new)
         .sized(0.5F, 0.5F)
   );
public static final RegistryObject<EntityType<LosteyeEffect1Entity>> LOSTEYE_EFFECT_1=register(
      "losteye_effect_1",
      Builder.<LosteyeEffect1Entity>of(LosteyeEffect1Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(LosteyeEffect1Entity::new)
         .sized(0.6F, 0.6F)
   );
public static final RegistryObject<EntityType<LosteyeEffect2Entity>> LOSTEYE_EFFECT_2=register(
      "losteye_effect_2",
      Builder.<LosteyeEffect2Entity>of(LosteyeEffect2Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(LosteyeEffect2Entity::new)
         .sized(0.6F, 0.6F)
   );
public static final RegistryObject<EntityType<LosteyeEffect3Entity>> LOSTEYE_EFFECT_3=register(
      "losteye_effect_3",
      Builder.<LosteyeEffect3Entity>of(LosteyeEffect3Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(LosteyeEffect3Entity::new)
         .sized(0.6F, 0.6F)
   );
public static final RegistryObject<EntityType<LostEyeOgreEntity>> LOST_EYE_OGRE=register("lost_eye_ogre",Builder.<LostEyeOgreEntity>of(LostEyeOgreEntity::new,MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(LostEyeOgreEntity::new).sized(0.6F,0.6F));
public static final java.util.function.Supplier<EntityType<MartianEntity>> MARTIAN=()->(EntityType<MartianEntity>)(EntityType<?>)dev.deepnight.DeepNight.HOSTILES.get(dev.deepnight.Species.DAWN_MARTIAN).get();
public static final RegistryObject<EntityType<MeteorgroundeffectEntity>> METEORGROUNDEFFECT=register(
      "meteorgroundeffect",
      Builder.<MeteorgroundeffectEntity>of(MeteorgroundeffectEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(MeteorgroundeffectEntity::new)
         .sized(0.5F, 0.4F)
   );
public static final RegistryObject<EntityType<MeteorAttackEntity>> METEOR_ATTACK=register(
      "meteor_attack",
      Builder.<MeteorAttackEntity>of(MeteorAttackEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(MeteorAttackEntity::new)
         .sized(2.0F, 2.0F)
   );
public static final java.util.function.Supplier<EntityType<MossGolemEntity>> MOSS_GOLEM=()->(EntityType<MossGolemEntity>)(EntityType<?>)dev.deepnight.DeepNight.HOSTILES.get(dev.deepnight.Species.DAWN_MOSS_GOLEM).get();
public static final RegistryObject<EntityType<MultipartbodyEntity>> MULTIPARTBODY=register(
      "multipartbody",
      Builder.<MultipartbodyEntity>of(MultipartbodyEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(MultipartbodyEntity::new)
         .sized(5.0F, 14.0F)
   );
public static final RegistryObject<EntityType<Multipartbody2Entity>> MULTIPARTBODY_2=register(
      "multipartbody_2",
      Builder.<Multipartbody2Entity>of(Multipartbody2Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(Multipartbody2Entity::new)
         .sized(5.0F, 12.0F)
   );
public static final RegistryObject<EntityType<MultiPartLegEntity>> MULTI_PART_LEG=register(
      "multi_part_leg",
      Builder.<MultiPartLegEntity>of(MultiPartLegEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(MultiPartLegEntity::new)
         .fireImmune()
         .sized(2.0F, 2.0F)
   );
public static final java.util.function.Supplier<EntityType<NineTailsEntity>> NINE_TAILS=()->(EntityType<NineTailsEntity>)(EntityType<?>)dev.deepnight.DeepNight.HOSTILES.get(dev.deepnight.Species.DAWN_NINE_TAILS).get();
public static final RegistryObject<EntityType<Notchflash1Entity>> NOTCHFLASH_1=register(
      "notchflash_1",
      Builder.<Notchflash1Entity>of(Notchflash1Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(Notchflash1Entity::new)
         .sized(2.0F, 2.0F)
   );
public static final RegistryObject<EntityType<Notchflash2Entity>> NOTCHFLASH_2=register(
      "notchflash_2",
      Builder.<Notchflash2Entity>of(Notchflash2Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(Notchflash2Entity::new)
         .sized(1.0F, 2.0F)
   );
public static final RegistryObject<EntityType<Notchflash3Entity>> NOTCHFLASH_3=register(
      "notchflash_3",
      Builder.<Notchflash3Entity>of(Notchflash3Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(Notchflash3Entity::new)
         .sized(1.4F, 1.4F)
   );
public static final RegistryObject<EntityType<Notchflash4Entity>> NOTCHFLASH_4=register(
      "notchflash_4",
      Builder.<Notchflash4Entity>of(Notchflash4Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(Notchflash4Entity::new)
         .sized(5.0F, 5.0F)
   );
public static final RegistryObject<EntityType<Notchgroundeffect1Entity>> NOTCHGROUNDEFFECT_1=register(
      "notchgroundeffect_1",
      Builder.<Notchgroundeffect1Entity>of(Notchgroundeffect1Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(Notchgroundeffect1Entity::new)
         .sized(0.5F, 0.5F)
   );
public static final RegistryObject<EntityType<Notchgroundeffect2Entity>> NOTCHGROUNDEFFECT_2=register(
      "notchgroundeffect_2",
      Builder.<Notchgroundeffect2Entity>of(Notchgroundeffect2Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(Notchgroundeffect2Entity::new)
         .sized(0.5F, 0.5F)
   );
public static final RegistryObject<EntityType<Notchgroundeffect3Entity>> NOTCHGROUNDEFFECT_3=register(
      "notchgroundeffect_3",
      Builder.<Notchgroundeffect3Entity>of(Notchgroundeffect3Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(Notchgroundeffect3Entity::new)
         .sized(0.5F, 0.4F)
   );
public static final RegistryObject<EntityType<Notchgroundeffect4Entity>> NOTCHGROUNDEFFECT_4=register(
      "notchgroundeffect_4",
      Builder.<Notchgroundeffect4Entity>of(Notchgroundeffect4Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(Notchgroundeffect4Entity::new)
         .sized(0.5F, 0.5F)
   );
public static final java.util.function.Supplier<EntityType<NotchBossEntity>> NOTCH_BOSS=()->(EntityType<NotchBossEntity>)(EntityType<?>)dev.deepnight.DeepNight.HOSTILES.get(dev.deepnight.Species.DAWN_NOTCH_BOSS).get();
public static final RegistryObject<EntityType<NotchFlash5Entity>> NOTCH_FLASH_5=register(
      "notch_flash_5",
      Builder.<NotchFlash5Entity>of(NotchFlash5Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(NotchFlash5Entity::new)
         .sized(0.6F, 1.0F)
   );
public static final RegistryObject<EntityType<NotchGrabEntity>> NOTCH_GRAB=register(
      "notch_grab",
      Builder.<NotchGrabEntity>of(NotchGrabEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(NotchGrabEntity::new)
         .sized(0.3F, 0.3F)
   );
public static final RegistryObject<EntityType<NotchPunchEntity>> NOTCH_PUNCH=register(
      "notch_punch",
      Builder.<NotchPunchEntity>of(NotchPunchEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(NotchPunchEntity::new)
         .sized(3.0F, 3.0F)
   );
public static final RegistryObject<EntityType<NotchStompeffectEntity>> NOTCH_STOMPEFFECT=register(
      "notch_stompeffect",
      Builder.<NotchStompeffectEntity>of(NotchStompeffectEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(NotchStompeffectEntity::new)
         .sized(4.0F, 1.0F)
   );
public static final RegistryObject<EntityType<Projectile2Entity>> PROJECTILE_2=register(
      "projectile_2",
      Builder.<Projectile2Entity>of(Projectile2Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(Projectile2Entity::new)
         .fireImmune()
         .sized(1.0F, 1.8F)
   );
public static final RegistryObject<EntityType<ProjectileMagicEntity>> PROJECTILE_MAGIC=register(
      "projectile_magic",
      Builder.<ProjectileMagicEntity>of(ProjectileMagicEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(ProjectileMagicEntity::new)
         .fireImmune()
         .sized(0.6F, 1.8F)
   );
public static final RegistryObject<EntityType<RoarEntity>> ROAR=register(
      "roar",
      Builder.<RoarEntity>of(RoarEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(RoarEntity::new)
         .fireImmune()
         .sized(0.0F, 0.0F)
   );
public static final RegistryObject<EntityType<RodParticlesEntity>> ROD_PARTICLES=register(
      "rod_particles",
      Builder.<RodParticlesEntity>of(RodParticlesEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(RodParticlesEntity::new)
         .sized(0.5F, 0.5F)
   );
public static final RegistryObject<EntityType<RumbleEntity>> RUMBLE=register(
      "rumble",
      Builder.<RumbleEntity>of(RumbleEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(RumbleEntity::new)
         .sized(1.0F, 1.0F)
   );
public static final RegistryObject<EntityType<SeedGunEntity>> SEED_GUN=register(
      "projectile_seed_gun",
      Builder.<SeedGunEntity>of(SeedGunEntity::new, MobCategory.MISC)
         .setCustomClientFactory(SeedGunEntity::new)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(1)
         .sized(0.5F, 0.5F)
   );
public static final RegistryObject<EntityType<SentinelFlashEntity>> SENTINEL_FLASH=register(
      "sentinel_flash",
      Builder.<SentinelFlashEntity>of(SentinelFlashEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(SentinelFlashEntity::new)
         .sized(1.0F, 1.0F)
   );
public static final java.util.function.Supplier<EntityType<SentinelKnightEntity>> SENTINEL_KNIGHT=()->(EntityType<SentinelKnightEntity>)(EntityType<?>)dev.deepnight.DeepNight.HOSTILES.get(dev.deepnight.Species.DAWN_SENTINEL_KNIGHT).get();
public static final RegistryObject<EntityType<SentinelSlashEntity>> SENTINEL_SLASH=register(
      "sentinel_slash",
      Builder.<SentinelSlashEntity>of(SentinelSlashEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(SentinelSlashEntity::new)
         .sized(2.0F, 2.0F)
   );
public static final RegistryObject<EntityType<ShogunateEffect5Entity>> SHOGUNATE_EFFECT_5=register(
      "shogunate_effect_5",
      Builder.<ShogunateEffect5Entity>of(ShogunateEffect5Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(ShogunateEffect5Entity::new)
         .sized(0.5F, 0.5F)
   );
public static final RegistryObject<EntityType<ShogunateEffect6Entity>> SHOGUNATE_EFFECT_6=register(
      "shogunate_effect_6",
      Builder.<ShogunateEffect6Entity>of(ShogunateEffect6Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(ShogunateEffect6Entity::new)
         .sized(1.0F, 1.0F)
   );
public static final RegistryObject<EntityType<ShogunateEffect7Entity>> SHOGUNATE_EFFECT_7=register(
      "shogunate_effect_7",
      Builder.<ShogunateEffect7Entity>of(ShogunateEffect7Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(ShogunateEffect7Entity::new)
         .sized(1.0F, 1.0F)
   );
public static final RegistryObject<EntityType<ShogunateEffect8Entity>> SHOGUNATE_EFFECT_8=register(
      "shogunate_effect_8",
      Builder.<ShogunateEffect8Entity>of(ShogunateEffect8Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(ShogunateEffect8Entity::new)
         .sized(1.0F, 1.0F)
   );
public static final java.util.function.Supplier<EntityType<SkeletonlordEntity>> SKELETONLORD=()->(EntityType<SkeletonlordEntity>)(EntityType<?>)dev.deepnight.DeepNight.HOSTILES.get(dev.deepnight.Species.DAWN_SKELETONLORD).get();
public static final RegistryObject<EntityType<Spell1Entity>> SPELL_1=register(
      "spell_1",
      Builder.<Spell1Entity>of(Spell1Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(Spell1Entity::new)
         .sized(0.6F, 1.8F)
   );
public static final RegistryObject<EntityType<Spell2Entity>> SPELL_2=register(
      "spell_2",
      Builder.<Spell2Entity>of(Spell2Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(Spell2Entity::new)
         .sized(0.6F, 1.8F)
   );
public static final RegistryObject<EntityType<Spell3Entity>> SPELL_3=register(
      "spell_3",
      Builder.<Spell3Entity>of(Spell3Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(Spell3Entity::new)
         .sized(1.0F, 1.0F)
   );
public static final RegistryObject<EntityType<SphereEntity>> SPHERE=register(
      "sphere",
      Builder.<SphereEntity>of(SphereEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(SphereEntity::new)
         .sized(1.0F, 1.0F)
   );
public static final RegistryObject<EntityType<SproutEntity>> SPROUT=register(
      "sprout",
      Builder.<SproutEntity>of(SproutEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(SproutEntity::new)
         .sized(0.6F, 1.8F)
   );
public static final RegistryObject<EntityType<StaffInteractEntity>> STAFF_INTERACT=register(
      "staff_interact",
      Builder.<StaffInteractEntity>of(StaffInteractEntity::new, MobCategory.CREATURE)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(StaffInteractEntity::new)
         .sized(1.7F, 3.5F)
   );
public static final RegistryObject<EntityType<SummonDragEntity>> SUMMON_DRAG=register(
      "summon_drag",
      Builder.<SummonDragEntity>of(SummonDragEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(SummonDragEntity::new)
         .sized(0.1F, 0.1F)
   );
public static final RegistryObject<EntityType<SwordAutoEntity>> SWORD_AUTO=register(
      "sword_auto",
      Builder.<SwordAutoEntity>of(SwordAutoEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(SwordAutoEntity::new)
         .sized(1.0F, 1.0F)
   );
public static final RegistryObject<EntityType<SwordProjectileEntity>> SWORD_PROJECTILE=register(
      "sword_projectile",
      Builder.<SwordProjectileEntity>of(SwordProjectileEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(SwordProjectileEntity::new)
         .fireImmune()
         .sized(0.6F, 1.8F)
   );
public static final RegistryObject<EntityType<SwordThrownEntity>> SWORD_THROWN=register(
      "sword_thrown",
      Builder.<SwordThrownEntity>of(SwordThrownEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(SwordThrownEntity::new)
         .sized(0.6F, 0.6F)
   );
public static final RegistryObject<EntityType<TailDartEntity>> TAIL_DART=register(
      "tail_dart",
      Builder.<TailDartEntity>of(TailDartEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(TailDartEntity::new)
         .fireImmune()
         .sized(0.6F, 1.95F)
   );
public static final RegistryObject<EntityType<Thundereffect1Entity>> THUNDEREFFECT_1=register(
      "thundereffect_1",
      Builder.<Thundereffect1Entity>of(Thundereffect1Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(Thundereffect1Entity::new)
         .sized(1.0F, 1.0F)
   );
public static final RegistryObject<EntityType<Thundereffect2Entity>> THUNDEREFFECT_2=register(
      "thundereffect_2",
      Builder.<Thundereffect2Entity>of(Thundereffect2Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(Thundereffect2Entity::new)
         .sized(1.0F, 1.0F)
   );
public static final RegistryObject<EntityType<Thundereffect3Entity>> THUNDEREFFECT_3=register(
      "thundereffect_3",
      Builder.<Thundereffect3Entity>of(Thundereffect3Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(Thundereffect3Entity::new)
         .sized(1.0F, 1.0F)
   );
public static final RegistryObject<EntityType<Thundereffect4Entity>> THUNDEREFFECT_4=register(
      "thundereffect_4",
      Builder.<Thundereffect4Entity>of(Thundereffect4Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(Thundereffect4Entity::new)
         .sized(1.0F, 1.0F)
   );
public static final RegistryObject<EntityType<TrackingThunderEntity>> TRACKING_THUNDER=register(
      "tracking_thunder",
      Builder.<TrackingThunderEntity>of(TrackingThunderEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(TrackingThunderEntity::new)
         .sized(0.0F, 0.0F)
   );
public static final RegistryObject<EntityType<TreeDrainParticlesEntity>> TREE_DRAIN_PARTICLES=register(
      "tree_drain_particles",
      Builder.<TreeDrainParticlesEntity>of(TreeDrainParticlesEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(TreeDrainParticlesEntity::new)
         .sized(1.0F, 1.0F)
   );
public static final RegistryObject<EntityType<WhispererStem2Entity>> WHISPERER_STEM_2=register(
      "whisperer_stem_2",
      Builder.<WhispererStem2Entity>of(WhispererStem2Entity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(WhispererStem2Entity::new)
         .sized(1.4F, 4.0F)
   );
public static final RegistryObject<EntityType<WindEntity>> WIND=register(
      "wind",
      Builder.<WindEntity>of(WindEntity::new, MobCategory.MONSTER)
         .setShouldReceiveVelocityUpdates(true)
         .setTrackingRange(64)
         .setUpdateInterval(3)
         .setCustomClientFactory(WindEntity::new)
         .sized(0.6F, 0.6F)
   );
private static <T extends net.minecraft.world.entity.Entity> RegistryObject<EntityType<T>> register(String name,EntityType.Builder<T> builder){return REGISTRY.register(name,()->builder.build(name));}
}
