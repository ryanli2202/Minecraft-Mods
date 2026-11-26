package dev.deepnight.source.mobosses.entity.custom;

import java.util.EnumSet;
import dev.deepnight.source.mobosses.entity.ai.SoulsMeleeAttackGoal;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent.BossBarColor;
import net.minecraft.world.BossEvent.BossBarOverlay;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.util.GeckoLibUtil;

public class HerobrineEntity extends dev.deepnight.NightMob implements GeoEntity {
   private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
   private static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("walk");
   private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
   private static final int DEATH_ANIMATION_TICKS = 38;
   private final ServerBossEvent bossEvent = (ServerBossEvent)new ServerBossEvent(this.getDisplayName(), BossBarColor.RED, BossBarOverlay.PROGRESS)
      .setDarkenScreen(true);
   private int judgmentTimer = 140;
   private int windupTicks = -1;
   private int stunTicks = 0;
   private int blockCooldown = 0;
   private SoulsMeleeAttackGoal meleeGoal;

   public HerobrineEntity(EntityType<? extends dev.deepnight.NightMob> type, Level level) {
      super(type, level, dev.deepnight.Species.MOBOSSES_HEROBRINE);
      this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
      this.setPersistenceRequired();
   }

   public boolean removeWhenFarAway(double distanceToClosestPlayer) {
      return false;
   }

   public boolean fireImmune() {
      return true;
   }

   public static Builder createAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.MAX_HEALTH, 1000.0)
         .add(Attributes.ATTACK_DAMAGE, 20.0)
         .add(Attributes.MOVEMENT_SPEED, 0.4)
         .add(Attributes.ARMOR, 12.0)
         .add(Attributes.FOLLOW_RANGE, 64.0)
         .add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
   }

   public boolean isEnraged() {
      return this.getHealth() < this.getMaxHealth() * 0.4F;
   }

   public boolean isAttacking() {
      return this.meleeGoal != null && this.meleeGoal.isAttacking();
   }

   public boolean isPushable() {
      return false;
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(0, new HerobrineEntity.DodgeGoal(this));
      this.goalSelector.addGoal(1, new HerobrineEntity.TeleportGoal(this));
      this.meleeGoal = new SoulsMeleeAttackGoal(
         this,
         1.45,
         "attack_controller",
         () -> !this.isDeadOrDying() && this.windupTicks < 0,
         3,
         new SoulsMeleeAttackGoal.AttackDef("sword1", 10, new int[]{7}, 1.6),
         new SoulsMeleeAttackGoal.AttackDef("sword2", 10, new int[]{7}, 1.6)
      );
      this.goalSelector.addGoal(2, this.meleeGoal);
      this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Player.class, true));
   }

   public boolean hurt(DamageSource source, float amount) {
      if (source.is(DamageTypes.LIGHTNING_BOLT)) {
         return false;
      }

      if (!this.level().isClientSide) {
         if (this.stunTicks > 0) {
            amount *= 1.5F;
            if (this.random.nextFloat() < 0.5F) {
               this.triggerAnim("attack_controller", "stun_hit");
            }
         } else if (this.blockCooldown <= 0 && this.windupTicks < 0 && source.getEntity() instanceof LivingEntity attacker && this.random.nextFloat() < 0.25F) {
            Vec3 toAttacker = attacker.position().subtract(this.position());
            if (toAttacker.lengthSqr() > 1.0E-4) {
               toAttacker = toAttacker.normalize();
               Vec3 look = this.getLookAngle();
               if (look.dot(toAttacker) > 0.5) {
                  this.blockCooldown = 60;
                  this.triggerAnim("attack_controller", "block");
                  this.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 0.8F);
                  return super.hurt(source, amount * 0.25F);
               }
            }
         }
      }

      return super.hurt(source, amount);
   }

   public void aiStep() {
      super.aiStep();
      if (!this.level().isClientSide) {
         if (this.blockCooldown > 0) {
            this.blockCooldown--;
         }

         if (this.stunTicks > 0) {
            this.stunTicks--;
         }

         LivingEntity target = this.getTarget();
         if (target != null && target.isAlive()) {
            this.handleThunderJudgment(target);
         } else {
            this.windupTicks = -1;
         }

         this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
         this.bossEvent.setColor(this.isEnraged() ? BossBarColor.PURPLE : BossBarColor.RED);
      }
   }

   private void handleThunderJudgment(LivingEntity target) {
      if (this.windupTicks >= 0) {
         if (this.windupTicks == 0 && this.meleeGoal != null) {
            this.meleeGoal.cancel();
         }

         this.getNavigation().stop();
         this.getLookControl().setLookAt(target, 10.0F, 10.0F);
         this.windupTicks++;
         if (this.windupTicks == 1) {
            this.triggerAnim("attack_controller", "powerup");
            this.playSound(SoundEvents.EVOKER_PREPARE_SUMMON, 1.5F, 0.6F);
         }

         if (this.windupTicks == 40) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.HOSTILE, 3.0F, 0.8F);
         }

         if (this.windupTicks >= 47) {
            this.unleashThunderJudgment(target);
            this.windupTicks = -1;
            this.stunTicks = 70;
         }
      } else {
         int cooldown = this.isEnraged() ? 130 : 200;
         if (--this.judgmentTimer <= 0) {
            this.judgmentTimer = cooldown;
            this.windupTicks = 0;
         }
      }
   }

   private void unleashThunderJudgment(LivingEntity target) {
      if (this.level() instanceof ServerLevel serverLevel) {
         this.triggerAnim("attack_controller", "falcon_punch");
         int bolts = this.isEnraged() ? 7 : 5;
         double radius = 4.0;

         for (int i = 0; i < bolts; i++) {
            double angle = (Math.PI * 2) / bolts * i;
            double x = target.getX() + Math.cos(angle) * radius;
            double z = target.getZ() + Math.sin(angle) * radius;
            LightningBolt bolt = (LightningBolt)EntityType.LIGHTNING_BOLT.create(this.level());
            if (bolt != null) {
               bolt.moveTo(x, target.getY(), z);
               serverLevel.addFreshEntity(bolt);
            }
         }

         for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(10.0))) {
            if (entity != this) {
               entity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 60, 0));
               Vec3 push = entity.position().subtract(this.position());
               if (push.lengthSqr() > 1.0E-4) {
                  push = push.normalize().scale(1.5);
                  entity.push(push.x, 0.4, push.z);
               }
            }
         }

         serverLevel.sendParticles(ParticleTypes.SOUL, this.getX(), this.getY() + 1.0, this.getZ(), 40, 1.0, 1.0, 1.0, 0.05);
      }
   }

   protected SoundEvent getHurtSound(DamageSource damageSource) {
      return SoundEvents.PLAYER_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SoundEvents.ENDER_DRAGON_DEATH;
   }

   public void registerControllers(ControllerRegistrar controllers) {
      controllers.add(new AnimationController(this, "main_controller", 5, state -> {
         if (this.isDeadOrDying() || this.isAttacking()) {
            return PlayState.STOP;
         } else {
            return state.isMoving() ? state.setAndContinue(WALK_ANIM) : state.setAndContinue(IDLE_ANIM);
         }
      }));
      controllers.add(
         new AnimationController(this, "attack_controller", 3, state -> PlayState.STOP)
            .triggerableAnim("roll", RawAnimation.begin().thenPlay("roll"))
            .triggerableAnim("sword1", RawAnimation.begin().thenPlay("sword1"))
            .triggerableAnim("sword2", RawAnimation.begin().thenPlay("sword2"))
            .triggerableAnim("block", RawAnimation.begin().thenPlay("block"))
            .triggerableAnim("powerup", RawAnimation.begin().thenPlay("powerup"))
            .triggerableAnim("falcon_punch", RawAnimation.begin().thenPlay("falcon_punch"))
            .triggerableAnim("stun_hit", RawAnimation.begin().thenPlay("stun_hit"))
            .triggerableAnim("death", RawAnimation.begin().thenPlayAndHold("death"))
      );
   }

   protected void tickDeath() {
      this.deathTime++;
      if (this.deathTime == 1) {
         this.triggerAnim("attack_controller", "death");
         this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.HOSTILE, 2.0F, 0.6F);
      }

      if (this.deathTime == 38) {
         this.level().broadcastEntityEvent(this, (byte)60);
         this.remove(RemovalReason.KILLED);
      }
   }

   public void startSeenByPlayer(ServerPlayer p) {
      super.startSeenByPlayer(p);
      this.bossEvent.addPlayer(p);
   }

   public void stopSeenByPlayer(ServerPlayer p) {
      super.stopSeenByPlayer(p);
      this.bossEvent.removePlayer(p);
   }

   public AnimatableInstanceCache getAnimatableInstanceCache() {
      return this.cache;
   }

   static class DodgeGoal extends Goal {
      private final HerobrineEntity h;
      private int cooldown = 0;

      public DodgeGoal(HerobrineEntity h) {
         this.h = h;
         this.setFlags(EnumSet.of(Flag.MOVE));
      }

      public boolean canUse() {
         return this.h.windupTicks < 0
            && !this.h.isAttacking()
            && this.cooldown <= 0
            && this.h.getLastHurtByMob() != null
            && this.h.getRandom().nextFloat() < 0.3F;
      }

      public void start() {
         this.cooldown = 60;
         this.h.triggerAnim("attack_controller", "roll");
         LivingEntity attacker = this.h.getLastHurtByMob();
         Vec3 dodgeDir;
         if (attacker != null) {
            Vec3 away = this.h.position().subtract(attacker.position());
            if (away.lengthSqr() < 1.0E-4) {
               away = new Vec3(1.0, 0.0, 0.0);
            }

            away = away.normalize();
            dodgeDir = new Vec3(-away.z, 0.0, away.x).scale(this.h.getRandom().nextBoolean() ? 1.0 : -1.0);
         } else {
            dodgeDir = new Vec3(this.h.getRandom().nextDouble() - 0.5, 0.0, this.h.getRandom().nextDouble() - 0.5);
         }

         this.h.setDeltaMovement(this.h.getDeltaMovement().add(dodgeDir.x * 0.6, 0.2, dodgeDir.z * 0.6));
      }

      public boolean canContinueToUse() {
         return false;
      }

      public void tick() {
         if (this.cooldown > 0) {
            this.cooldown--;
         }
      }
   }

   static class TeleportGoal extends Goal {
      private final HerobrineEntity h;
      private long lastTeleportTick = -1000L;

      public TeleportGoal(HerobrineEntity h) {
         this.h = h;
      }

      public boolean canUse() {
         if (this.h.windupTicks >= 0) {
            return false;
         }

         LivingEntity target = this.h.getTarget();
         if (target == null) {
            return false;
         }

         if (this.h.tickCount - this.lastTeleportTick < 100L) {
            return false;
         }

         if (this.isBeingWatched(target)) {
            return false;
         }

         double distSqr = this.h.distanceToSqr(target);
         return distSqr > 36.0 || this.h.getRandom().nextFloat() < 0.01F;
      }

      private boolean isBeingWatched(LivingEntity target) {
         Vec3 toHerobrine = this.h.position().subtract(target.getEyePosition());
         if (toHerobrine.lengthSqr() < 1.0E-4) {
            return true;
         }

         toHerobrine = toHerobrine.normalize();
         Vec3 look = target.getLookAngle();
         return look.dot(toHerobrine) > 0.85;
      }

      public void start() {
         this.lastTeleportTick = this.h.tickCount;
         LivingEntity target = this.h.getTarget();
         if (target != null) {
            double angle = this.h.getRandom().nextDouble() * Math.PI * 2.0;
            double dist = 2.5 + this.h.getRandom().nextDouble() * 2.0;
            double x = target.getX() + Math.cos(angle) * dist;
            double z = target.getZ() + Math.sin(angle) * dist;
            this.h.level().playSound(null, this.h.getX(), this.h.getY(), this.h.getZ(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.HOSTILE, 1.0F, 0.7F);
            this.h.teleportTo(x, target.getY(), z);
            this.h.level().playSound(null, this.h.getX(), this.h.getY(), this.h.getZ(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.HOSTILE, 1.0F, 1.3F);
            if (this.h.level() instanceof ServerLevel serverLevel) {
               serverLevel.sendParticles(ParticleTypes.PORTAL, this.h.getX(), this.h.getY() + 1.0, this.h.getZ(), 30, 0.3, 0.5, 0.3, 0.05);
            }
         }
      }

      public boolean canContinueToUse() {
         return false;
      }
   }
}
