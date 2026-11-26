package dev.deepnight.source.dawn.entity;

import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.procedures.ProwindProcedure;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages.SpawnEntity;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;

public class WindEntity extends Monster implements GeoEntity {
   public static final EntityDataAccessor<Float> pitch = SynchedEntityData.defineId(WindEntity.class, EntityDataSerializers.FLOAT);
   private final AnimatableInstanceCache factory = software.bernie.geckolib.util.GeckoLibUtil.createInstanceCache(this);

   public WindEntity(SpawnEntity packet, Level world) {
      this(SimpleMobsModEntities.WIND.get(), world);
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(pitch, 0.0F);
   }

   public WindEntity(EntityType<WindEntity> type, Level world) {
      super(type, world);
      this.xpReward = 0;
      this.noCulling = true;
      this.setPersistenceRequired();
   }

   @Override
   public boolean fireImmune() {
      return true;
   }

   @Override
   public void baseTick() {
      super.baseTick();
      if(dev.deepnight.source.SourceStun.stunned(this))return;
      ProwindProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
   }

   @Override
   public Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }

   @Override
   protected void registerGoals() {
      super.registerGoals();
   }

   @Override
   public MobType getMobType() {
      return MobType.UNDEFINED;
   }

   @Override
   public boolean removeWhenFarAway(double distanceToClosestPlayer) {
      return false;
   }

   @Override
   public SoundEvent getHurtSound(DamageSource ds) {
      return dev.deepnight.SafeSounds.resolve("");
   }

   @Override
   public SoundEvent getDeathSound() {
      return dev.deepnight.SafeSounds.resolve("");
   }

   @Override
   public boolean skipAttackInteraction(Entity entity) {
      return true;
   }

   @Override
   public boolean causeFallDamage(float l, float d, DamageSource source) {
      return false;
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      return source.is(net.minecraft.world.damagesource.DamageTypes.FALL) ? false : super.hurt(source, amount);
   }

   @Override
   public boolean isPushable() {
      return false;
   }

   @Override
   protected void doPush(Entity entityIn) {
   }

   @Override
   protected void pushEntities() {
   }

   @Override
   public void aiStep() {
      super.aiStep();
   }

   public static void init() {
   }

   public static Builder createAttributes() {
      Builder builder = Mob.createMobAttributes();
      builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
      builder = builder.add(Attributes.MAX_HEALTH, 100.0);
      builder = builder.add(Attributes.ARMOR, 0.0);
      builder = builder.add(Attributes.ATTACK_DAMAGE, 3.0);
      return builder.add(Attributes.FOLLOW_RANGE, 16.0);
   }

   private PlayState predicate(AnimationState animationEvent) {
      animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.wind",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      return PlayState.CONTINUE;
   }

   public void registerControllers(ControllerRegistrar data) {
      data.add(new AnimationController<>(this, "controller", 0, this::predicate));
   }

   public AnimatableInstanceCache getAnimatableInstanceCache() {
      return this.factory;
   }
}
