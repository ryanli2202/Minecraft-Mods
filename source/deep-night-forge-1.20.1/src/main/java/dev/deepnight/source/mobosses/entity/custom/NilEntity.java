package dev.deepnight.source.mobosses.entity.custom;

import java.util.EnumSet;
import dev.deepnight.source.mobosses.MoBosses;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.BossEvent.BossBarColor;
import net.minecraft.world.BossEvent.BossBarOverlay;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.util.GeckoLibUtil;

public class NilEntity extends dev.deepnight.NightMob implements GeoEntity {
   private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
   private static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("walk");
   private static final RawAnimation RUN_ANIM = RawAnimation.begin().thenLoop("run");
   private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
   private static final String[] MELEE_ANIMS = new String[]{"melee_1", "melee_2"};
   private final ServerBossEvent bossEvent = (ServerBossEvent)new ServerBossEvent(this.getDisplayName(), BossBarColor.YELLOW, BossBarOverlay.PROGRESS)
      .setDarkenScreen(true);
   private int comboTimer = 40;
   private boolean comboActive = false;
   private boolean enraged = false;
   private boolean hadTarget = false;
   private int recentDamage = 0;
   private int burstWindow = 0;
   private int staggerTicks = 0;
   private boolean staggerRecovering = false;
   private int staggerRecoverTicks = 0;
   private int meleeCooldown = 0;
   private LivingEntity attackTarget = null;
   private String attackAnimName = null;
   private int attackTicks = 0;
   private int attackDuration = 0;
   private int[] attackHitTicks = new int[0];
   private int nextHitIndex = 0;
   private double attackReachMultiplier = 1.5;
   private static final double SWORD_BACK_REACH = 1.8;
   private static final double SWORD_TIP_REACH = 9.0;
   private static final double SWORD_WIDTH = 1.6;
   private static final int DEATH_ANIMATION_TICKS = 90;

   public boolean isAttacking() {
      return this.attackAnimName != null;
   }

   public NilEntity(EntityType<? extends dev.deepnight.NightMob> type, Level level) {
      super(type, level, dev.deepnight.Species.MOBOSSES_NIL);
      this.setPersistenceRequired();
   }

   public boolean removeWhenFarAway(double distanceToClosestPlayer) {
      return false;
   }

   public static Builder createAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.MAX_HEALTH, 700.0)
         .add(Attributes.ATTACK_DAMAGE, 16.0)
         .add(Attributes.MOVEMENT_SPEED, 0.34)
         .add(Attributes.ARMOR, 8.0)
         .add(Attributes.FOLLOW_RANGE, 48.0)
         .add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
   }

   public boolean isStaggered() {
      return this.staggerTicks > 0 || this.staggerRecovering;
   }

   private double meleeReachSqr(LivingEntity target) {
      float w = this.getBbWidth() * 2.0F;
      return w * w + target.getBbWidth();
   }

   private boolean isTargetInBladeSweep(LivingEntity target) {
      Vec3 look = this.getLookAngle();
      Vec3 hand = this.position().add(0.0, this.getBbHeight() * 0.6, 0.0);
      Vec3 backEnd = hand.subtract(look.scale(1.8));
      Vec3 tipEnd = hand.add(look.scale(9.0));
      Vec3 targetPoint = target.position().add(0.0, target.getBbHeight() * 0.5, 0.0);
      double dist = distancePointToSegment(targetPoint, backEnd, tipEnd);
      double bladeRadius = 1.6 + target.getBbWidth() * 0.5;
      return dist <= bladeRadius;
   }

   private static double distancePointToSegment(Vec3 point, Vec3 a, Vec3 b) {
      Vec3 ab = b.subtract(a);
      double abLenSqr = ab.lengthSqr();
      if (abLenSqr < 1.0E-6) {
         return point.distanceTo(a);
      }

      double t = point.subtract(a).dot(ab) / abLenSqr;
      t = Math.max(0.0, Math.min(1.0, t));
      Vec3 closest = a.add(ab.scale(t));
      return point.distanceTo(closest);
   }

   public boolean isPushable() {
      return false;
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(1, new NilEntity.ShoulderBashGoal(this));
      this.goalSelector.addGoal(2, new NilEntity.OrcMeleeGoal(this));
      this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Player.class, true));
   }

   private void beginMeleeAttack(LivingEntity target) {
      if (this.comboActive) {
         this.comboActive = false;
         if (this.random.nextBoolean()) {
            this.startAttack(target, "combo_1", 90, new int[]{22, 50, 76}, 2.6);
         } else {
            this.startAttack(target, "combo_2", 60, new int[]{21, 45}, 2.6);
         }
      } else if (this.random.nextBoolean()) {
         this.startAttack(target, "melee_1", 25, new int[]{10}, 1.5);
      } else {
         this.startAttack(target, "melee_2", 45, new int[]{20}, 1.5);
      }
   }

   private void startAttack(LivingEntity target, String anim, int durationTicks, int[] hitTicks, double reachMultiplier) {
      this.attackTarget = target;
      this.attackAnimName = anim;
      this.attackTicks = 0;
      this.attackDuration = durationTicks;
      this.attackHitTicks = hitTicks;
      this.nextHitIndex = 0;
      this.attackReachMultiplier = reachMultiplier;
      this.triggerAnim("attack_controller", anim);
      this.playSound((SoundEvent)MoBosses.NIL_SWING.get(), 1.0F, 1.0F);
      this.getNavigation().stop();
   }

   private void cancelAttack() {
      this.attackAnimName = null;
      this.attackTarget = null;
   }

   private void tickAttack() {
      if (this.isAttacking()) {
         if (this.attackTarget != null && this.attackTarget.isAlive()) {
            this.getLookControl().setLookAt(this.attackTarget, 30.0F, 30.0F);
         }

         this.attackTicks++;

         while (this.nextHitIndex < this.attackHitTicks.length && this.attackTicks >= this.attackHitTicks[this.nextHitIndex]) {
            this.landHit();
            this.nextHitIndex++;
         }

         if (this.attackTicks >= this.attackDuration) {
            this.attackAnimName = null;
            this.attackTarget = null;
            this.meleeCooldown = this.enraged ? 3 : 6;
         }
      }
   }

   private void landHit() {
      LivingEntity target = this.attackTarget;
      if (target != null && target.isAlive()) {
         boolean inRange;
         if (this.attackAnimName != null && this.attackAnimName.startsWith("combo")) {
            inRange = this.isTargetInBladeSweep(target);
         } else {
            double reach = this.meleeReachSqr(target) * this.attackReachMultiplier;
            inRange = this.distanceToSqr(target) <= reach;
         }

         if (inRange) {
            boolean success = target.hurt(this.damageSources().mobAttack(this), (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE));
            if (success) {
               this.playSound((SoundEvent)MoBosses.NIL_SWORD_HIT.get(), 1.0F, 1.0F);
               Vec3 push = target.position().subtract(this.position());
               if (push.lengthSqr() > 1.0E-4) {
                  push = push.normalize().scale(0.4);
                  target.push(push.x, 0.15, push.z);
               }
            }
         }
      }
   }

   public boolean hurt(DamageSource source, float amount) {
      boolean result = super.hurt(source, amount);
      if (!this.level().isClientSide && result && this.isAlive()) {
         this.recentDamage += (int)amount;
         this.burstWindow = 30;
         if (!this.isStaggered() && this.recentDamage >= this.getMaxHealth() * 0.12F) {
            this.startStagger();
         }
      }

      return result;
   }

   private void startStagger() {
      this.cancelAttack();
      this.recentDamage = 0;
      this.staggerTicks = 45;
      this.staggerRecovering = false;
      this.getNavigation().stop();
      this.triggerAnim("attack_controller", "falling");
      this.playSound((SoundEvent)MoBosses.NIL_GROUND_BREAK.get(), 1.0F, 1.0F);
   }

   public void aiStep() {
      super.aiStep();
      if (!this.level().isClientSide) {
         if (this.meleeCooldown > 0) {
            this.meleeCooldown--;
         }

         this.tickAttack();
         if (this.burstWindow > 0) {
            this.burstWindow--;
         } else {
            this.recentDamage = 0;
         }

         if (this.staggerTicks > 0) {
            this.staggerTicks--;
            this.getNavigation().stop();
            if (this.staggerTicks == 0) {
               this.staggerRecovering = true;
               this.triggerAnim("attack_controller", "get_up");
               this.staggerRecoverTicks = 20;
            }
         } else if (this.staggerRecovering) {
            this.getNavigation().stop();
            if (--this.staggerRecoverTicks <= 0) {
               this.staggerRecovering = false;
            }
         }

         if (!this.enraged && this.getHealth() < this.getMaxHealth() * 0.35F) {
            this.enraged = true;
            this.triggerAnim("attack_controller", "rage");
            this.playSound((SoundEvent)MoBosses.NIL_GRUNT_BIG.get(), 1.5F, 0.9F);
         }

         LivingEntity target = this.getTarget();
         if (target != null && target.isAlive()) {
            if (!this.hadTarget) {
               this.hadTarget = true;
               this.playSound((SoundEvent)MoBosses.NIL_YELL.get(), 1.5F, 1.0F);
            }

            if (!this.isStaggered() && --this.comboTimer <= 0) {
               this.comboTimer = this.enraged ? 40 : 70;
               this.comboActive = true;
            }
         } else {
            this.hadTarget = false;
         }

         this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
         this.bossEvent.setColor(this.enraged ? BossBarColor.RED : BossBarColor.YELLOW);
      }
   }

   protected SoundEvent getAmbientSound() {
      return (SoundEvent)MoBosses.NIL_GRUNT.get();
   }

   protected SoundEvent getHurtSound(DamageSource damageSource) {
      return (SoundEvent)MoBosses.NIL_HURT.get();
   }

   protected SoundEvent getDeathSound() {
      return (SoundEvent)MoBosses.NIL_DEATH.get();
   }

   public void registerControllers(ControllerRegistrar controllers) {
      controllers.add(new AnimationController(this, "main_controller", 5, state -> {
         if (this.isDeadOrDying()) {
            return PlayState.STOP;
         } else if (this.isStaggered() || this.isAttacking()) {
            return PlayState.STOP;
         } else {
            return state.isMoving() ? state.setAndContinue(this.enraged ? RUN_ANIM : WALK_ANIM) : state.setAndContinue(IDLE_ANIM);
         }
      }));
      controllers.add(
         new AnimationController(this, "attack_controller", 3, state -> PlayState.STOP)
            .triggerableAnim("melee_1", RawAnimation.begin().thenPlay("melee_1"))
            .triggerableAnim("melee_2", RawAnimation.begin().thenPlay("melee_2"))
            .triggerableAnim("combo_1", RawAnimation.begin().thenPlay("combo_1"))
            .triggerableAnim("combo_2", RawAnimation.begin().thenPlay("combo_2"))
            .triggerableAnim("shoulder_bash", RawAnimation.begin().thenPlay("shoulder_bash"))
            .triggerableAnim("rage", RawAnimation.begin().thenPlay("rage"))
            .triggerableAnim("falling", RawAnimation.begin().thenPlayAndHold("falling"))
            .triggerableAnim("get_up", RawAnimation.begin().thenPlay("get_up"))
            .triggerableAnim("death", RawAnimation.begin().thenPlayAndHold("death"))
      );
   }

   protected void tickDeath() {
      this.deathTime++;
      if (this.deathTime == 1) {
         this.triggerAnim("attack_controller", "death");
      }

      if (this.deathTime == 90) {
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

   static class OrcMeleeGoal extends Goal {
      private final NilEntity nil;
      private int pathRecalcTicks = 0;

      public OrcMeleeGoal(NilEntity nil) {
         this.nil = nil;
         this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
      }

      public boolean canUse() {
         LivingEntity target = this.nil.getTarget();
         return target != null && target.isAlive() && !this.nil.isStaggered();
      }

      public boolean canContinueToUse() {
         return this.nil.isAttacking() ? true : this.canUse();
      }

      public void start() {
         this.pathRecalcTicks = 0;
      }

      public void stop() {
         if (!this.nil.isAttacking()) {
            this.nil.getNavigation().stop();
         }
      }

      public void tick() {
         if (!this.nil.isAttacking()) {
            LivingEntity target = this.nil.getTarget();
            if (target != null) {
               this.nil.getLookControl().setLookAt(target, 30.0F, 30.0F);
               double distSqr = this.nil.distanceToSqr(target);
               double reachSqr = this.nil.meleeReachSqr(target);
               if (distSqr <= reachSqr) {
                  this.nil.getNavigation().stop();
                  if (this.nil.meleeCooldown <= 0) {
                     this.nil.beginMeleeAttack(target);
                  }
               } else if (--this.pathRecalcTicks <= 0) {
                  this.pathRecalcTicks = 10;
                  this.nil.getNavigation().moveTo(target, this.nil.enraged ? 1.5 : 1.3);
               }
            }
         }
      }
   }

   static class ShoulderBashGoal extends Goal {
      private final NilEntity nil;
      private int nextBashTick;
      private int bashTicks = 0;
      private Vec3 chargeDir = Vec3.ZERO;
      private boolean hasHit = false;

      public ShoulderBashGoal(NilEntity nil) {
         this.nil = nil;
         this.nextBashTick = nil.tickCount + 20;
         this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
      }

      @Override
      public boolean requiresUpdateEveryTick() {
         return true;
      }

      public boolean canUse() {
         if (this.nil.isStaggered() || this.nil.comboActive || this.nil.isAttacking()) {
            return false;
         } else if (this.nil.tickCount < this.nextBashTick) {
            return false;
         } else {
            LivingEntity target = this.nil.getTarget();
            if (target != null && target.isAlive()) {
               double distSqr = this.nil.distanceToSqr(target);
               return distSqr > 16.0 && distSqr < 400.0;
            } else {
               return false;
            }
         }
      }

      public void start() {
         LivingEntity target = this.nil.getTarget();
         this.nextBashTick = this.nil.tickCount + 80;
         this.bashTicks = 15;
         this.hasHit = false;
         this.nil.triggerAnim("attack_controller", "shoulder_bash");
         if (target != null) {
            Vec3 dir = target.position().subtract(this.nil.position());
            this.chargeDir = dir.lengthSqr() > 1.0E-4 ? dir.normalize() : this.nil.getLookAngle();
            this.nil.getLookControl().setLookAt(target, 30.0F, 30.0F);
         }
      }

      public boolean canContinueToUse() {
         return this.bashTicks > 0;
      }

      public void tick() {
         this.bashTicks--;
         this.nil.setDeltaMovement(this.nil.getDeltaMovement().add(this.chargeDir.x * 0.5, 0.0, this.chargeDir.z * 0.5));
         LivingEntity target = this.nil.getTarget();
         if (!this.hasHit && target != null && this.nil.distanceToSqr(target) < 6.0) {
            this.hasHit = true;
            target.hurt(this.nil.damageSources().mobAttack(this.nil), (float)this.nil.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.75F);
            this.nil.playSound((SoundEvent)MoBosses.NIL_GROUND_BREAK.get(), 1.0F, 1.1F);
            Vec3 push = target.position().subtract(this.nil.position());
            if (push.lengthSqr() > 1.0E-4) {
               push = push.normalize().scale(1.6);
               target.push(push.x, 0.5, push.z);
            }
         }
      }

      public void stop() {
         this.bashTicks = 0;
      }
   }
}
