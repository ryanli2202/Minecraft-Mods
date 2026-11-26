package dev.deepnight.source.mobosses.entity.ai;

import java.util.EnumSet;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;

public class SoulsMeleeAttackGoal extends Goal {
   private final Mob mob;
   private final double speedModifier;
   private final String controllerName;
   private final BooleanSupplier canAct;
   private final SoulsMeleeAttackGoal.AttackDef[] attacks;
   private final Consumer<LivingEntity> onSwing;
   private final Consumer<LivingEntity> onHit;
   private final int recoveryCooldownTicks;
   private int pathRecalcTicks = 0;
   private int cooldown = 0;
   private LivingEntity attackTarget;
   private SoulsMeleeAttackGoal.AttackDef currentAttack;
   private int attackTicks;
   private int nextHitIndex;

   public SoulsMeleeAttackGoal(
      Mob mob,
      double speedModifier,
      String controllerName,
      BooleanSupplier canAct,
      int recoveryCooldownTicks,
      Consumer<LivingEntity> onSwing,
      Consumer<LivingEntity> onHit,
      SoulsMeleeAttackGoal.AttackDef... attacks
   ) {
      this.mob = mob;
      this.speedModifier = speedModifier;
      this.controllerName = controllerName;
      this.canAct = canAct;
      this.recoveryCooldownTicks = recoveryCooldownTicks;
      this.onSwing = onSwing;
      this.onHit = onHit;
      this.attacks = attacks;
      this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
   }

   public SoulsMeleeAttackGoal(
      Mob mob,
      double speedModifier,
      String controllerName,
      BooleanSupplier canAct,
      Consumer<LivingEntity> onSwing,
      Consumer<LivingEntity> onHit,
      SoulsMeleeAttackGoal.AttackDef... attacks
   ) {
      this(mob, speedModifier, controllerName, canAct, 6, onSwing, onHit, attacks);
   }

   public SoulsMeleeAttackGoal(Mob mob, double speedModifier, String controllerName, BooleanSupplier canAct, SoulsMeleeAttackGoal.AttackDef... attacks) {
      this(mob, speedModifier, controllerName, canAct, 6, null, null, attacks);
   }

   public SoulsMeleeAttackGoal(
      Mob mob, double speedModifier, String controllerName, BooleanSupplier canAct, int recoveryCooldownTicks, SoulsMeleeAttackGoal.AttackDef... attacks
   ) {
      this(mob, speedModifier, controllerName, canAct, recoveryCooldownTicks, null, null, attacks);
   }

   public boolean isAttacking() {
      return this.currentAttack != null;
   }

   @Override
   public boolean requiresUpdateEveryTick() {
      // Hit frames and animation durations are measured in server ticks (20/s).
      return true;
   }

   public void cancel() {
      this.currentAttack = null;
      this.attackTarget = null;
   }

   private double reachSqr(LivingEntity target, double multiplier) {
      float w = this.mob.getBbWidth() * 2.0F;
      return (w * w + target.getBbWidth()) * multiplier;
   }

   public boolean canUse() {
      if (!this.canAct.getAsBoolean()) {
         return false;
      }

      LivingEntity target = this.mob.getTarget();
      return target != null && target.isAlive();
   }

   public boolean canContinueToUse() {
      return this.isAttacking() ? true : this.canUse();
   }

   public void stop() {
      if (!this.isAttacking()) {
         this.mob.getNavigation().stop();
      }
   }

   public void tick() {
      if (this.cooldown > 0) {
         this.cooldown--;
      }

      if (this.isAttacking()) {
         this.tickAttack();
      } else {
         LivingEntity target = this.mob.getTarget();
         if (target != null) {
            this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
            double distSqr = this.mob.distanceToSqr(target);
            double baseReach = this.reachSqr(target, 1.0);
            if (distSqr <= baseReach) {
               this.mob.getNavigation().stop();
               if (this.cooldown <= 0 && this.attacks.length > 0 && this.canAct.getAsBoolean()) {
                  this.beginAttack(target);
               }
            } else if (--this.pathRecalcTicks <= 0) {
               this.pathRecalcTicks = 10;
               this.mob.getNavigation().moveTo(target, this.speedModifier);
            }
         }
      }
   }

   private void beginAttack(LivingEntity target) {
      this.attackTarget = target;
      this.currentAttack = this.attacks[this.mob.getRandom().nextInt(this.attacks.length)];
      this.attackTicks = 0;
      this.nextHitIndex = 0;
      this.onAttackFrame(this.currentAttack, 0);
      this.mob.getNavigation().stop();
      if (this.mob instanceof GeoEntity geo) {
         geo.triggerAnim(this.controllerName, this.currentAttack.animName);
      }

      if (this.onSwing != null) {
         this.onSwing.accept(target);
      }
   }

   private void tickAttack() {
      if (this.attackTarget != null && this.attackTarget.isAlive()) {
         this.mob.getLookControl().setLookAt(this.attackTarget, 30.0F, 30.0F);
      }

      this.attackTicks++;
      this.onAttackFrame(this.currentAttack, this.attackTicks);

      while (this.nextHitIndex < this.currentAttack.hitTicks.length && this.attackTicks >= this.currentAttack.hitTicks[this.nextHitIndex]) {
         this.landHit();
         this.nextHitIndex++;
      }

      if (this.attackTicks >= this.currentAttack.durationTicks) {
         this.currentAttack = null;
         this.attackTarget = null;
         this.cooldown = this.recoveryCooldownTicks;
      }
   }

   /** Optional presentation hook, called on the same clock as the animation's hit frames. */
   protected void onAttackFrame(AttackDef attack, int tick) {}

   private void landHit() {
      LivingEntity target = this.attackTarget;
      if (target != null && target.isAlive()) {
         double reach = this.reachSqr(target, this.currentAttack.reachMultiplier);
         if (!(this.mob.distanceToSqr(target) > reach)) {
            boolean success = target.hurt(this.mob.damageSources().mobAttack(this.mob), (float)this.mob.getAttributeValue(Attributes.ATTACK_DAMAGE));
            if (success) {
               if (this.onHit != null) {
                  this.onHit.accept(target);
               }

               Vec3 push = target.position().subtract(this.mob.position());
               if (push.lengthSqr() > 1.0E-4) {
                  push = push.normalize().scale(0.4);
                  target.push(push.x, 0.15, push.z);
               }
            }
         }
      }
   }

   public static class AttackDef {
      public final String animName;
      public final int durationTicks;
      public final int[] hitTicks;
      public final double reachMultiplier;

      public AttackDef(String animName, int durationTicks, int[] hitTicks, double reachMultiplier) {
         this.animName = animName;
         this.durationTicks = durationTicks;
         this.hitTicks = hitTicks;
         this.reachMultiplier = reachMultiplier;
      }
   }
}
