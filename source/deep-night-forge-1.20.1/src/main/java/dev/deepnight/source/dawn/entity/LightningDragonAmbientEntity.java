package dev.deepnight.source.dawn.entity;

import javax.annotation.Nullable;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.procedures.NoaggroProcedure;
import dev.deepnight.source.dawn.procedures.NoaiProcedure;
import dev.deepnight.source.dawn.procedures.ProdragonambienthurtProcedure;
import dev.deepnight.source.dawn.procedures.ProlightningdragonambientProcedure;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages.SpawnEntity;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.keyframe.event.SoundKeyframeEvent;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;

public class LightningDragonAmbientEntity extends Monster implements GeoEntity {
   public static final EntityDataAccessor<Float> atanyaw = SynchedEntityData.defineId(LightningDragonAmbientEntity.class, EntityDataSerializers.FLOAT);
   public static final EntityDataAccessor<Float> pitch = SynchedEntityData.defineId(LightningDragonAmbientEntity.class, EntityDataSerializers.FLOAT);
   public double oldX = 0.0;
   public double oldZ = 0.0;
   private final AnimatableInstanceCache factory = software.bernie.geckolib.util.GeckoLibUtil.createInstanceCache(this);

   public LightningDragonAmbientEntity(SpawnEntity packet, Level world) {
      this(SimpleMobsModEntities.LIGHTNING_DRAGON_AMBIENT.get(), world);
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(atanyaw, 0.0F);
      this.entityData.define(pitch, 0.0F);
   }

   public LightningDragonAmbientEntity(EntityType<LightningDragonAmbientEntity> type, Level world) {
      super(type, world);
      this.xpReward = 0;
      this.setNoAi(false);
      this.setPersistenceRequired();
      this.noCulling = true;
      this.setMaxUpStep(1.0F);
   }

   @Override
   public Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }

   @Override
   protected float getEquipmentDropChance(EquipmentSlot slot) {
      return 0.0F;
   }

   @Override
   protected void registerGoals() {
      super.registerGoals();
      this.goalSelector.addGoal(1, new NineTailsEntity.FollowTargetGoal(this, 1.0, 64.0F) {
         @Override
         public boolean canUse() {
            Entity entity = LightningDragonAmbientEntity.this;
            Level world = LightningDragonAmbientEntity.this.level();
            return super.canUse() && NoaggroProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = LightningDragonAmbientEntity.this;
            Level world = LightningDragonAmbientEntity.this.level();
            return super.canContinueToUse() && NoaggroProcedure.execute(entity);
         }
      });
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, false, false) {
         @Override
         public boolean canUse() {
            Entity entity = LightningDragonAmbientEntity.this;
            Level world = LightningDragonAmbientEntity.this.level();
            return super.canUse() && NoaggroProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = LightningDragonAmbientEntity.this;
            Level world = LightningDragonAmbientEntity.this.level();
            return super.canContinueToUse() && NoaggroProcedure.execute(entity);
         }
      });
      this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, ServerPlayer.class, false, false) {
         @Override
         public boolean canUse() {
            Entity entity = LightningDragonAmbientEntity.this;
            Level world = LightningDragonAmbientEntity.this.level();
            return super.canUse() && NoaggroProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = LightningDragonAmbientEntity.this;
            Level world = LightningDragonAmbientEntity.this.level();
            return super.canContinueToUse() && NoaggroProcedure.execute(entity);
         }
      });
      this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this, Animal.class, false, false) {
         @Override
         public boolean canUse() {
            Entity entity = LightningDragonAmbientEntity.this;
            Level world = LightningDragonAmbientEntity.this.level();
            return super.canUse() && NoaggroProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = LightningDragonAmbientEntity.this;
            Level world = LightningDragonAmbientEntity.this.level();
            return super.canContinueToUse() && NoaggroProcedure.execute(entity);
         }
      });
      this.goalSelector.addGoal(5, new RandomLookAroundGoal(this) {
         @Override
         public boolean canUse() {
            Entity entity = LightningDragonAmbientEntity.this;
            Level world = LightningDragonAmbientEntity.this.level();
            return super.canUse() && NoaiProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = LightningDragonAmbientEntity.this;
            Level world = LightningDragonAmbientEntity.this.level();
            return super.canContinueToUse() && NoaiProcedure.execute(entity);
         }
      });
      this.goalSelector.addGoal(6, new FloatGoal(this) {
         @Override
         public boolean canUse() {
            Entity entity = LightningDragonAmbientEntity.this;
            Level world = LightningDragonAmbientEntity.this.level();
            return super.canUse() && NoaiProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = LightningDragonAmbientEntity.this;
            Level world = LightningDragonAmbientEntity.this.level();
            return super.canContinueToUse() && NoaiProcedure.execute(entity);
         }
      });
   }

   @Override
   public MobType getMobType() {
      return MobType.UNDEFINED;
   }

   @Override
   public void baseTick() {
      super.baseTick();
      if(dev.deepnight.source.SourceStun.stunned(this))return;
      ProlightningdragonambientProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      ProdragonambienthurtProcedure.execute(this, source.getEntity());
      return super.hurt(source, amount);
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
   public boolean isPushable() {
      return false;
   }

   @Override
   public SoundEvent getDeathSound() {
      return dev.deepnight.SafeSounds.resolve("");
   }

   @Override
   public boolean causeFallDamage(float l, float d, DamageSource source) {
      return false;
   }

   @Override
   protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
   }

   @Override
   public boolean fireImmune() {
      return true;
   }

   @Override
   public void aiStep() {
      super.aiStep();
   }

   @Override
   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag
   ) {
      SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
      this.getPersistentData().putBoolean("lightningdragon", true);
      return retval;
   }

   public static void init() {
   }

   public static Builder createAttributes() {
      Builder builder = Mob.createMobAttributes();
      builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
      builder = builder.add(Attributes.MAX_HEALTH, 1000.0);
      builder = builder.add(Attributes.ARMOR, 0.0);
      builder = builder.add(Attributes.ATTACK_DAMAGE, 3.0);
      builder = builder.add(Attributes.FOLLOW_RANGE, 128.0);
      return builder.add(Attributes.FLYING_SPEED, 10.0);
   }

   private PlayState predicate(AnimationState animationEvent) {
      if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.LEATHER_HELMET) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.dragon.attack1",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.dragon.idle2",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      }

      return PlayState.CONTINUE;
   }

   private PlayState predicate1(AnimationState animationEvent) {
      double d1 = this.getX() - this.xOld;
      double d0 = this.getZ() - this.zOld;
      float xvelocity = (float)Math.sqrt(d1 * d1 + d0 * d0);
      float zvelocity = (float)Math.sqrt(d1 * d1 + d0 * d0);
      float velocity = (float)Math.sqrt(d1 * d1 + d0 * d0);
      if (!this.isAlive()) {
         animationEvent.getController().setTransitionLength(10);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.dragon.death",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.POPPY) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.dragon.drop",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.onGround()) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.dragon.idle",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.dragon.fly_move",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      }

      return PlayState.CONTINUE;
   }

   @Override
   protected void tickDeath() {
      this.deathTime++;
      if (this.deathTime == this.getMaxDeathCount()) {
         this.remove(RemovalReason.KILLED);
         if (this.level().isClientSide()) {
            this.level()
               .addParticle(
                  ParticleTypes.ASH,
                  this.getX() + this.random.nextFloat(),
                  this.getY() + this.random.nextFloat(),
                  this.getZ() + this.random.nextFloat(),
                  0.0,
                  0.0,
                  0.0
               );
         }
      }
   }

   int getMaxDeathCount() {
      return 78;
   }

   @Override
   protected void pushEntities() {
      if (!this.isDeadOrDying()) {
         super.pushEntities();
      }
   }

   @Override
   public void knockback(double strength, double ratioX, double ratioZ) {
      if (!this.isDeadOrDying()) {
         super.knockback(strength, ratioX, ratioZ);
      }
   }

   public void registerControllers(ControllerRegistrar data) {
      data.add(new AnimationController<>(this, "controller", 0, this::predicate));
      data.add(new AnimationController<>(this, "controller1", 1, this::predicate1));
      AnimationController controller = new AnimationController<>(this, "controller1", 1, this::predicate1);
      controller.setSoundKeyframeHandler(this::soundListener);
      data.add(controller);
   }

   private <ENTITY extends GeoEntity> void soundListener(SoundKeyframeEvent<ENTITY> event) {
      LocalPlayer player = Minecraft.getInstance().player;
      if (player != null) {
         player.playSound(SoundEvent.createVariableRangeEvent(new ResourceLocation("simple_mobs", "dragon_wing_flap2")), 1.0F, 2.0F);
      }
   }

   public AnimatableInstanceCache getAnimatableInstanceCache() {
      return this.factory;
   }
}
