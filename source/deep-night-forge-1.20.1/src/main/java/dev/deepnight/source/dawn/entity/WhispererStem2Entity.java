package dev.deepnight.source.dawn.entity;

import javax.annotation.Nullable;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.procedures.Prostem2Procedure;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
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

public class WhispererStem2Entity extends Monster implements GeoEntity {
   private final AnimatableInstanceCache factory = software.bernie.geckolib.util.GeckoLibUtil.createInstanceCache(this);

   public WhispererStem2Entity(SpawnEntity packet, Level world) {
      this(SimpleMobsModEntities.WHISPERER_STEM_2.get(), world);
   }

   public WhispererStem2Entity(EntityType<WhispererStem2Entity> type, Level world) {
      super(type, world);
      this.xpReward = 0;
      this.setNoAi(false);
      this.noCulling = true;
   }

   @Override
   public Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }

   @Override
   protected void registerGoals() {
      super.registerGoals();
      this.goalSelector.addGoal(1, new NineTailsEntity.FollowTargetGoal(this, 0.0, 30.0F));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false, false));
      this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, ServerPlayer.class, false, false));
      this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Animal.class, false, false));
   }

   @Override
   public MobType getMobType() {
      return MobType.UNDEFINED;
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
   public boolean isPushable() {
      return false;
   }

   @Override
   protected void pushEntities() {
      super.pushEntities();
   }

   @Override
   public void push(Entity other) {}

   @Override
   public void push(double pX, double pY, double pZ) {
   }

   @Override
   public void knockback(double strength, double ratioX, double ratioZ) {
      if (!this.isDeadOrDying()) {
         super.knockback(strength, ratioX, ratioZ);
      }
   }

   @Override
   public void baseTick() {
      super.baseTick();
      if(dev.deepnight.source.SourceStun.stunned(this))return;
      Prostem2Procedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
   }

   public static void init() {
   }

   public static Builder createAttributes() {
      Builder builder = Mob.createMobAttributes();
      builder = builder.add(Attributes.MOVEMENT_SPEED, 0.0);
      builder = builder.add(Attributes.MAX_HEALTH, 80.0);
      builder = builder.add(Attributes.ARMOR, 4.0);
      builder = builder.add(Attributes.ATTACK_DAMAGE, 3.0);
      builder = builder.add(Attributes.FOLLOW_RANGE, 40.0);
      return builder.add(Attributes.KNOCKBACK_RESISTANCE, 5.0);
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      if (source.getEntity() == null) {
         return false;
      } else {
         return source.getEntity().getPersistentData().getBoolean("whisperer") ? false : super.hurt(source, amount);
      }
   }

   @Override
   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag
   ) {
      SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
      this.getPersistentData().putBoolean("whisperer", true);
      return retval;
   }

   private PlayState predicate(AnimationState animationEvent) {
      if (this.isAlive()) {
         if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.LEATHER_HELMET) {
            animationEvent.getController().setTransitionLength(0);
            animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ws.attack",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
         } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.COAL) {
            animationEvent.getController().setTransitionLength(0);
            animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ws.spawn",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
         } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.CHARCOAL) {
            animationEvent.getController().setTransitionLength(0);
            animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ws.despawn",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
         } else {
            animationEvent.getController().setTransitionLength(5);
            animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ws.idle",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
         }
      }

      return PlayState.CONTINUE;
   }

   public void registerControllers(ControllerRegistrar data) {
      data.add(new AnimationController<>(this, "controller", 0, this::predicate));
   }

   public AnimatableInstanceCache getAnimatableInstanceCache() {
      return this.factory;
   }
}
