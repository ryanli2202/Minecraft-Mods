package dev.deepnight.source.dawn.entity;

import java.util.EnumSet;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.procedures.ProntdeathProcedure;
import dev.deepnight.source.dawn.procedures.ProtailhurtProcedure;
import dev.deepnight.source.dawn.procedures.ProtailsProcedure;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.BossEvent.BossBarColor;
import net.minecraft.world.BossEvent.BossBarOverlay;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
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

public class NineTailsEntity extends dev.deepnight.NightMob implements GeoEntity {
   private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), BossBarColor.RED, BossBarOverlay.PROGRESS);
   private final AnimatableInstanceCache factory = software.bernie.geckolib.util.GeckoLibUtil.createInstanceCache(this);

   @Override
   public boolean canChangeDimensions() {
      return false;
   }

   @Override
   public void startSeenByPlayer(ServerPlayer player) {
      super.startSeenByPlayer(player);
      this.bossInfo.addPlayer(player);
   }

   @Override
   public void stopSeenByPlayer(ServerPlayer player) {
      super.stopSeenByPlayer(player);
      this.bossInfo.removePlayer(player);
   }

   @Override
   public void tick() {
      super.tick();
      // Source attacks and stuns can pause AI while health continues changing.
      if (!this.level().isClientSide()) this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
   }

   public NineTailsEntity(SpawnEntity packet, Level world) {
      this(SimpleMobsModEntities.NINE_TAILS.get(), world);
   }

   public NineTailsEntity(EntityType<? extends dev.deepnight.NightMob> type, Level world) {
      super(type, world, dev.deepnight.Species.DAWN_NINE_TAILS);
      this.xpReward = 50;
      this.noCulling = true;
      this.setNoAi(false);
      this.setMaxUpStep(1.7F);
      this.setPersistenceRequired();
   }

   @Override
   public Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }

   @Override
   protected void registerGoals() {
      super.registerGoals();
      this.goalSelector.addGoal(1, new NineTailsEntity.FollowTargetGoal(this, 1.0, 30.0F));
      this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
      this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, ServerPlayer.class, true, false));
      this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, true, false));
      this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.7));
      this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
      this.goalSelector.addGoal(7, new FloatGoal(this));
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
   public boolean hurt(DamageSource source, float amount) {
      ProtailhurtProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this, source.getEntity());
      return this.getItemBySlot(EquipmentSlot.FEET).getItem() == Items.LEATHER_BOOTS ? false : super.hurt(source, amount);
   }

   @Override
   public void die(DamageSource source) {
      super.die(source);
      ProntdeathProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ());
   }

   @Override
   public SoundEvent getHurtSound(DamageSource ds) {
      return dev.deepnight.SafeSounds.resolve("entity.generic.hurt");
   }

   @Override
   public SoundEvent getDeathSound() {
      return dev.deepnight.SafeSounds.resolve("entity.generic.death");
   }

   @Override
   public void baseTick() {
      super.baseTick();
      if(dev.deepnight.source.SourceStun.stunned(this))return;
      ProtailsProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
   }

   public static void init() {
   }

   public static Builder createAttributes() {
      Builder builder = Mob.createMobAttributes();
      builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
      builder = builder.add(Attributes.MAX_HEALTH, 300.0);
      builder = builder.add(Attributes.ARMOR, 2.0);
      builder = builder.add(Attributes.ATTACK_DAMAGE, 0.0);
      builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
      return builder.add(Attributes.FOLLOW_RANGE, 30.0);
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
      return 60;
   }

   private PlayState predicate(AnimationState animationEvent) {
      double d1 = this.getX() - this.xOld;
      double d0 = this.getZ() - this.zOld;
      float xvelocity = (float)Math.sqrt(d1 * d1 + d0 * d0);
      float zvelocity = (float)Math.sqrt(d1 * d1 + d0 * d0);
      float velocity = (float)Math.sqrt(d1 * d1 + d0 * d0);
      if (!this.isAlive()) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ninetails.death",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.ROSE_BUSH) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ninetails.attack1_a",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.POPPY) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ninetails.attack1_b",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.VINE) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ninetails.attack1_c",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.CORNFLOWER) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ninetails.attack1_d",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.FLOWERING_AZALEA) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ninetails.attack1_e",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.DANDELION) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ninetails.attack1_f",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.PUMPKIN) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ninetails.tailpop",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.CARVED_PUMPKIN) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ninetails.notail",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.COAL) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ninetails.attack2_a",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.CHARCOAL) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ninetails.attack2_a2",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.IRON_NUGGET) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ninetails.attack2_b",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.GOLD_NUGGET) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ninetails.attack2_c",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.GHAST_TEAR) {
         animationEvent.getController().setTransitionLength(3);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ninetails.attack2_c2",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.GOLD_INGOT) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ninetails.attack2_grabbed",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.DIAMOND) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ninetails.attack2_d",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.POTATO) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ninetails.attack2_e",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.APPLE) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ninetails.attack2_e2",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.CARROT) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ninetails.attack2_f",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.CHAIN) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ninetails.attack2_g",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.LILAC) {
         animationEvent.getController().setTransitionLength(0);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ninetails.tail_trans",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.PODZOL) {
         animationEvent.getController().setTransitionLength(0);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ninetails.stunned",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.CHEST).getItem() == Items.LEATHER_CHESTPLATE) {
         if (animationEvent.isMoving()) {
            if (this.onGround()) {
               if (velocity > 0.15F) {
                  animationEvent.getController().setTransitionLength(5);
                  animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ninetails.tail_run",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
               } else {
                  animationEvent.getController().setTransitionLength(5);
                  animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ninetails.tail_walk",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
               }
            }
         } else {
            animationEvent.getController().setTransitionLength(5);
            animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ninetails.tail_idle",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
         }
      } else if (animationEvent.isMoving()) {
         if (this.onGround()) {
            if (velocity > 0.15F) {
               animationEvent.getController().setTransitionLength(5);
               animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ninetails.run",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
            } else {
               animationEvent.getController().setTransitionLength(5);
               animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ninetails.walk",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
            }
         }
      } else {
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ninetails.idle",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      }

      return PlayState.CONTINUE;
   }

   public void registerControllers(ControllerRegistrar data) {
      data.add(new AnimationController<>(this, "controller", 0, this::predicate));
   }

   public AnimatableInstanceCache getAnimatableInstanceCache() {
      return this.factory;
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

   public static class FollowTargetGoal extends Goal {
      protected final Mob entity;
      protected final float range;
      protected final double speed;
      protected float waterCost;
      protected int timeToRecalcPath = 0;

      public FollowTargetGoal(Mob entity, double speed, float range) {
         this.entity = entity;
         this.range = range;
         this.speed = speed;
         this.setFlags(EnumSet.of(Flag.MOVE));
      }

      @Override
      public boolean canUse() {
         LivingEntity target = this.entity.getTarget();
         if (target == null) {
            return false;
         } else {
            return this.entity.distanceToSqr(target) <= this.range * this.range ? !this.entity.isDeadOrDying() : false;
         }
      }

      @Override
      public void start() {
         this.waterCost = this.entity.getPathfindingMalus(BlockPathTypes.WATER);
      }

      @Override
      public void stop() {
         this.entity.getNavigation().stop();
         this.entity.setPathfindingMalus(BlockPathTypes.WATER, this.waterCost);
      }

      @Override
      public void tick() {
         this.entity.getLookControl().setLookAt(this.entity.getTarget(), 30.0F, 30.0F);
         if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 5;
            this.entity.getNavigation().moveTo(this.entity.getNavigation().createPath(this.entity.getTarget().blockPosition(), 1), this.speed);
         }
      }
   }

   public static class StrafeGoal extends Goal {
      private final Mob mob;
      private final double speed;
      private final float radiusSqr;
      private int seeTime;
      private boolean strafingClockwise;
      private boolean strafingBackwards;
      private int strafingTime = -1;

      public StrafeGoal(Mob mob, double speed, float radius) {
         this.mob = mob;
         this.speed = speed;
         this.radiusSqr = radius * radius;
         this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
      }

      @Override
      public boolean canUse() {
         return this.mob.getTarget() != null;
      }

      @Override
      public boolean canContinueToUse() {
         return this.canUse() || !this.mob.getNavigation().isDone();
      }

      @Override
      public void start() {
         super.start();
         this.mob.setAggressive(true);
      }

      @Override
      public void stop() {
         super.stop();
         this.mob.setAggressive(false);
         this.seeTime = 0;
         this.mob.stopUsingItem();
      }

      @Override
      public boolean requiresUpdateEveryTick() {
         return true;
      }

      @Override
      public void tick() {
         LivingEntity livingentity = this.mob.getTarget();
         if (livingentity != null) {
            double d0 = this.mob.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
            boolean flag = this.mob.getSensing().hasLineOfSight(livingentity);
            boolean flag1 = this.seeTime > 0;
            if (flag != flag1) {
               this.seeTime = 0;
            }

            if (flag) {
               this.seeTime++;
            } else {
               this.seeTime--;
            }

            if (!(d0 > this.radiusSqr) && this.seeTime >= 20) {
               this.mob.getNavigation().stop();
               this.strafingTime++;
            } else {
               this.mob.getNavigation().moveTo(livingentity, this.speed);
               this.strafingTime = -1;
            }

            if (this.strafingTime >= 20) {
               if (this.mob.getRandom().nextFloat() < 0.3) {
                  this.strafingClockwise = !this.strafingClockwise;
               }

               if (this.mob.getRandom().nextFloat() < 0.3) {
                  this.strafingBackwards = !this.strafingBackwards;
               }

               this.strafingTime = 0;
            }

            if (this.strafingTime > -1) {
               if (d0 > this.radiusSqr * 0.75F) {
                  this.strafingBackwards = false;
               } else if (d0 < this.radiusSqr * 0.25F) {
                  this.strafingBackwards = true;
               }

               this.mob.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
               this.mob.lookAt(livingentity, 30.0F, 30.0F);
            } else {
               this.mob.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            }
         }
      }
   }
}
