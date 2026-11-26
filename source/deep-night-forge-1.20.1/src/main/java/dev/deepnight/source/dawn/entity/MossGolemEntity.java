package dev.deepnight.source.dawn.entity;

import java.util.EnumSet;
import javax.annotation.Nullable;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.procedures.NoaiProcedure;
import dev.deepnight.source.dawn.procedures.PromossgolemProcedure;
import dev.deepnight.source.dawn.procedures.ProwerehurtProcedure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages.SpawnEntity;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;

public class MossGolemEntity extends dev.deepnight.NightMob implements GeoEntity {
   private static final EntityDataAccessor<Integer> DATA_ID_ATTACK_TARGET = SynchedEntityData.defineId(MossGolemEntity.class, EntityDataSerializers.INT);
   private final HurtByTargetGoal hurtByTargetGoal = new HurtByTargetGoal(this);
   private int attackTimer;
   public int clientSideAttackTime;
   private LivingEntity clientSideCachedAttackTarget;
   private final AnimatableInstanceCache factory = software.bernie.geckolib.util.GeckoLibUtil.createInstanceCache(this);

   public int getAttackDuration() {
      return 75;
   }

   public MossGolemEntity(SpawnEntity packet, Level world) {
      this(SimpleMobsModEntities.MOSS_GOLEM.get(), world);
   }

   public MossGolemEntity(EntityType<? extends dev.deepnight.NightMob> type, Level world) {
      super(type, world, dev.deepnight.Species.DAWN_MOSS_GOLEM);
      this.setMaxUpStep(2.0F);
      this.xpReward = 0;
      this.setNoAi(false);
      this.setPersistenceRequired();
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(DATA_ID_ATTACK_TARGET, 0);
   }

   @Override
   public Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }

   @Override
   protected void registerGoals() {
      super.registerGoals();
      this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Player.class, false, false) {
         @Override
         public boolean canUse() {
            Entity entity = MossGolemEntity.this;
            Level world = MossGolemEntity.this.level();
            return super.canUse() && NoaiProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = MossGolemEntity.this;
            Level world = MossGolemEntity.this.level();
            return super.canContinueToUse() && NoaiProcedure.execute(entity);
         }
      });
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, ServerPlayer.class, false, false) {
         @Override
         public boolean canUse() {
            Entity entity = MossGolemEntity.this;
            Level world = MossGolemEntity.this.level();
            return super.canUse() && NoaiProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = MossGolemEntity.this;
            Level world = MossGolemEntity.this.level();
            return super.canContinueToUse() && NoaiProcedure.execute(entity);
         }
      });
      this.targetSelector.addGoal(3, new HurtByTargetGoal(this) {
         @Override
         public boolean canUse() {
            Entity entity = MossGolemEntity.this;
            Level world = MossGolemEntity.this.level();
            return super.canUse() && NoaiProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = MossGolemEntity.this;
            Level world = MossGolemEntity.this.level();
            return super.canContinueToUse() && NoaiProcedure.execute(entity);
         }
      });
      this.goalSelector.addGoal(4, new MossGolemEntity.MossGolemAttackGoal(this, 1.0, true));
      this.goalSelector.addGoal(5, new NineTailsEntity.FollowTargetGoal(this, 1.0, 10.0F) {
         @Override
         public boolean canUse() {
            Entity entity = MossGolemEntity.this;
            Level world = MossGolemEntity.this.level();
            return super.canUse() && NoaiProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = MossGolemEntity.this;
            Level world = MossGolemEntity.this.level();
            return super.canContinueToUse() && NoaiProcedure.execute(entity);
         }
      });
      this.goalSelector.addGoal(6, new RandomStrollGoal(this, 1.0) {
         @Override
         public boolean canUse() {
            Entity entity = MossGolemEntity.this;
            Level world = MossGolemEntity.this.level();
            return super.canUse() && NoaiProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = MossGolemEntity.this;
            Level world = MossGolemEntity.this.level();
            return super.canContinueToUse() && NoaiProcedure.execute(entity);
         }
      });
      this.goalSelector.addGoal(7, new RandomLookAroundGoal(this) {
         @Override
         public boolean canUse() {
            Entity entity = MossGolemEntity.this;
            Level world = MossGolemEntity.this.level();
            return super.canUse() && NoaiProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = MossGolemEntity.this;
            Level world = MossGolemEntity.this.level();
            return super.canContinueToUse() && NoaiProcedure.execute(entity);
         }
      });
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
   public double getMyRidingOffset() {
      return -0.35;
   }

   @Override
   public SoundEvent getHurtSound(DamageSource ds) {
      return dev.deepnight.SafeSounds.resolve("block.deepslate_bricks.place");
   }

   @Override
   public SoundEvent getDeathSound() {
      return dev.deepnight.SafeSounds.resolve("entity.generic.death");
   }

   public static void init() {
   }

   public static Builder createAttributes() {
      Builder builder = Mob.createMobAttributes();
      builder = builder.add(Attributes.MOVEMENT_SPEED, 0.17);
      builder = builder.add(Attributes.MAX_HEALTH, 1000.0);
      builder = builder.add(Attributes.ARMOR, 50.0);
      builder = builder.add(Attributes.ATTACK_DAMAGE, 3.0);
      builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
      return builder.add(Attributes.FOLLOW_RANGE, 32.0);
   }

   public float getAttackDamage() {
      float f = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
      if (this.level().getDifficulty() == Difficulty.HARD) {
         f *= 1.5F;
      }

      return f;
   }

   @Override
   public void aiStep() {
      LivingEntity target = this.getTarget();
      if (target == null) {
         this.goalSelector.addGoal(5, this.hurtByTargetGoal);
      }

      if (this.attackTimer > 0) {
         this.attackTimer--;
      }

      this.destroyLeavesBlocks();
      this.doLaserParticleEffects();
      super.aiStep();
   }

   @Override
   public void baseTick() {
      super.baseTick();
      if(dev.deepnight.source.SourceStun.stunned(this))return;
      PromossgolemProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      ProwerehurtProcedure.execute(this.level(), this);
      if (source.getDirectEntity() instanceof ThrownPotion || source.getDirectEntity() instanceof AreaEffectCloud) {
         return false;
      } else if (source.is(net.minecraft.world.damagesource.DamageTypes.FALL)) {
         return false;
      } else if (source.is(net.minecraft.world.damagesource.DamageTypes.CACTUS)) {
         return false;
      } else if (source.is(net.minecraft.world.damagesource.DamageTypes.DROWN)) {
         return false;
      } else if (source.is(net.minecraft.world.damagesource.DamageTypes.LIGHTNING_BOLT)) {
         return false;
      } else {
         return source.is(net.minecraft.world.damagesource.DamageTypes.WITHER) ? false : super.hurt(source, amount);
      }
   }

   private void destroyLeavesBlocks() {
      if (this.horizontalCollision && ForgeEventFactory.getMobGriefingEvent(this.level(), this)) {
         boolean flag = false;
         AABB aabb = this.getBoundingBox().inflate(0.2);

         for (BlockPos blockpos : BlockPos.betweenClosed(
            Mth.floor(aabb.minX), Mth.floor(aabb.minY), Mth.floor(aabb.minZ), Mth.floor(aabb.maxX), Mth.floor(aabb.maxY), Mth.floor(aabb.maxZ)
         )) {
            BlockState blockstate = this.level().getBlockState(blockpos);
            Block block = blockstate.getBlock();
            if (block instanceof LeavesBlock) {
               flag = this.level().destroyBlock(blockpos, true, this) || flag;
            }
         }
      }
   }

   private void doLaserParticleEffects() {
      if (this.isAlive()) {
         if (this.level().isClientSide && this.hasActiveAttackTarget()) {
            if (this.clientSideAttackTime < this.getAttackDuration()) {
               this.clientSideAttackTime++;
            }

            LivingEntity livingentity = this.getActiveAttackTarget();
            if (livingentity != null) {
               this.getLookControl().setLookAt(livingentity, 90.0F, 90.0F);
               this.getLookControl().tick();
               double d5 = this.getAttackAnimationScale(0.0F);
               double d0 = livingentity.getX() - this.getX();
               double d1 = livingentity.getY(0.5) - this.getEyeY();
               double d2 = livingentity.getZ() - this.getZ();
               double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
               d0 /= d3;
               d1 /= d3;
               d2 /= d3;
               double d4 = this.random.nextDouble();

               while (d4 < d3) {
                  d4 += 1.8 - d5 + this.random.nextDouble() * (1.7 - d5);
                  this.level().addParticle(ParticleTypes.SMOKE, this.getX() + d0 * d4, this.getEyeY() + d1 * d4, this.getZ() + d2 * d4, 0.0, 0.0, 0.0);
               }
            }
         }

         if (this.hasActiveAttackTarget()) {
            this.setYRot(this.yHeadRot);
         }
      }
   }

   @Nullable
   public LivingEntity getActiveAttackTarget() {
      if (!this.hasActiveAttackTarget()) {
         return null;
      }

      if (this.level().isClientSide) {
         if (this.clientSideCachedAttackTarget != null) {
            return this.clientSideCachedAttackTarget;
         } else {
            Entity entity = this.level().getEntity(this.entityData.get(DATA_ID_ATTACK_TARGET));
            if (entity instanceof LivingEntity) {
               this.clientSideCachedAttackTarget = (LivingEntity)entity;
               return this.clientSideCachedAttackTarget;
            } else {
               return null;
            }
         }
      } else {
         return this.getTarget();
      }
   }

   void setActiveAttackTarget(int p_32818_) {
      this.entityData.set(DATA_ID_ATTACK_TARGET, p_32818_);
   }

   public boolean hasActiveAttackTarget() {
      return this.entityData.get(DATA_ID_ATTACK_TARGET) != 0;
   }

   public float getAttackAnimationScale(float scale) {
      return (this.clientSideAttackTime + scale) / this.getAttackDuration();
   }

   @Override
   public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> accessor) {
      super.onSyncedDataUpdated(accessor);
      if (DATA_ID_ATTACK_TARGET.equals(accessor)) {
         this.clientSideAttackTime = 0;
         this.clientSideCachedAttackTarget = null;
      }
   }

   public int getAttackTimer() {
      return this.attackTimer;
   }

   @Override
   protected void tickDeath() {
      this.deathTime++;
      if (this.deathTime == this.getMaxDeathCount()) {
         this.remove(RemovalReason.KILLED);
         if (this.level().isClientSide()) {
            this.level()
               .addParticle(
                  ParticleTypes.CLOUD,
                  this.getX() + this.random.nextFloat(),
                  this.getY() + this.random.nextFloat(),
                  this.getZ() + this.random.nextFloat(),
                  2.0,
                  2.0,
                  2.0
               );
         }
      }
   }

   int getMaxDeathCount() {
      return 50;
   }

   private PlayState predicate(AnimationState animationEvent) {
      if (!this.isAlive()) {
         animationEvent.getController().setTransitionLength(0);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.brute.death",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.LEATHER_HELMET) {
         animationEvent.getController().setTransitionLength(20);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.brute.attack1a",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.GOLD_INGOT) {
         animationEvent.getController().setTransitionLength(15);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.brute.attack1b",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.COAL) {
         animationEvent.getController().setTransitionLength(10);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.brute.attack2",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.CHARCOAL) {
         animationEvent.getController().setTransitionLength(4);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.brute.attack2a",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.IRON_INGOT) {
         animationEvent.getController().setTransitionLength(10);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.brute.attack3",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.IRON_NUGGET) {
         animationEvent.getController().setTransitionLength(2);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.brute.attack3b",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.YELLOW_CONCRETE) {
         animationEvent.getController().setTransitionLength(0);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.brute.sleep",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.RED_WOOL) {
         animationEvent.getController().setTransitionLength(0);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.brute.wake",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.CACTUS) {
         animationEvent.getController().setTransitionLength(0);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.brute.stun1",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.BAKED_POTATO) {
         animationEvent.getController().setTransitionLength(1);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.brute.stun2",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.POPPY) {
         animationEvent.getController().setTransitionLength(0);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.brute.trans",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (animationEvent.isMoving()) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.brute.walk",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.brute.idle",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      }

      return PlayState.CONTINUE;
   }

   public void registerControllers(ControllerRegistrar data) {
      data.add(new AnimationController<>(this, "controller", 0, this::predicate));
   }

   public AnimatableInstanceCache getAnimatableInstanceCache() {
      return this.factory;
   }

   public class MossGolemAttackGoal extends Goal {
      private final MossGolemEntity golem;
      private int laserAttackTick;
      private int laserCooldown = 0;
      private final double speedModifier;
      private final boolean followingTargetEvenIfNotSeen;
      private int ticksUntilNextPathRecalculation;
      private int ticksUntilNextAttack;
      private Path path;
      private boolean canPenalize = false;

      @Override
      public boolean canUse() {
         LivingEntity livingentity = this.golem.getTarget();
         if (livingentity == null) {
            return false;
         } else if (!livingentity.isAlive()) {
            return false;
         } else if (!this.golem.getPersistentData().getBoolean("Far")
            || this.golem.getPersistentData().getBoolean("noai")
            || this.golem.getPersistentData().getBoolean("nolaser")) {
            return false;
         } else if (!this.canPenalize) {
            this.path = this.golem.getNavigation().createPath(livingentity, 0);
            return this.path != null
               ? true
               : this.getAttackReachSqr(livingentity) >= this.golem.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
         } else if (--this.ticksUntilNextPathRecalculation <= 0) {
            this.path = this.golem.getNavigation().createPath(livingentity, 0);
            this.ticksUntilNextPathRecalculation = 4 + this.golem.getRandom().nextInt(7);
            return this.path != null;
         } else {
            return true;
         }
      }

      @Override
      public boolean requiresUpdateEveryTick() {
         return true;
      }

      @Override
      public void tick() {
         LivingEntity livingentity = this.golem.getTarget();
         this.laserCooldown--;
         if (livingentity != null) {
            boolean flag = this.golem.hasLineOfSight(livingentity);
            this.golem.getLookControl().setLookAt(livingentity, 90.0F, 90.0F);
            if (!flag) {
               this.golem.setTarget((LivingEntity)null);
            } else {
               this.laserTick(livingentity);
            }
         }
      }

      @Override
      public void start() {
         this.laserAttackTick = -10;
         LivingEntity livingentity = this.golem.getTarget();
         if (livingentity != null) {
            this.golem.getLookControl().setLookAt(livingentity, 90.0F, 90.0F);
         }

         this.golem.getNavigation().moveTo(this.path, this.speedModifier);
         this.golem.setAggressive(true);
         this.ticksUntilNextPathRecalculation = 0;
         this.ticksUntilNextAttack = 0;
      }

      @Override
      public boolean canContinueToUse() {
         LivingEntity livingentity = this.golem.getTarget();
         if (livingentity == null) {
            return false;
         } else if (!livingentity.isAlive()) {
            return false;
         } else if (!this.golem.getPersistentData().getBoolean("Far")
            || this.golem.getPersistentData().getBoolean("noai")
            || this.golem.getPersistentData().getBoolean("nolaser")) {
            return false;
         } else {
            return !this.followingTargetEvenIfNotSeen
               ? !this.golem.getNavigation().isDone()
               : !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player)livingentity).isCreative();
         }
      }

      @Override
      public void stop() {
         this.golem.setActiveAttackTarget(0);
         this.golem.setTarget((LivingEntity)null);
         this.golem.setAggressive(false);
         this.golem.getNavigation().stop();
      }

      public MossGolemAttackGoal(MossGolemEntity entity, double speed, boolean memory) {
         this.golem = entity;
         this.speedModifier = speed;
         this.followingTargetEvenIfNotSeen = memory;
         this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
      }

      protected void laserTick(LivingEntity livingentity) {
         if (this.laserCooldown <= 0) {
            this.laserAttackTick++;
         }

         if (this.laserAttackTick == 0) {
            this.golem.setActiveAttackTarget(livingentity.getId());
            if (!this.golem.isSilent()) {
               this.golem.playSound(this.getLaserChargeSound(), 2.0F, 1.0F);
            }
         } else if (this.laserAttackTick >= this.golem.getAttackDuration()) {
            float f = 2.0F;
            Vec3 vec3 = this.golem.getLookAngle().multiply(1.0, 1.0, 1.0).normalize().scale(3.0);
            if (this.golem.level().getDifficulty() == Difficulty.HARD) {
               f *= 2.0F;
            }

            this.golem.playSound(this.getLaserFireSound(), 1.0F, 0.7F);
            livingentity.playSound(this.getLaserFireSound(), 1.0F, 0.7F);
            livingentity.hurt(this.golem.damageSources().indirectMagic(this.golem, this.golem), f);
            livingentity.hurt(this.golem.damageSources().mobAttack(this.golem), 25.0F);
            this.golem.setTarget((LivingEntity)null);
            this.laserCooldown = 20;
         }
      }

      protected double getAttackReachSqr(LivingEntity entity) {
         return this.golem.getBbWidth() * 1.5F * this.golem.getBbWidth() * 1.5F + entity.getBbWidth();
      }

      protected SoundEvent getLaserChargeSound() {
         return dev.deepnight.SafeSounds.resolve("simple_mobs:laser_charge");
      }

      protected SoundEvent getLaserFireSound() {
         return dev.deepnight.SafeSounds.resolve("simple_mobs:electricity_zap");
      }
   }
}
