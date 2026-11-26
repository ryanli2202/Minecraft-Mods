package dev.deepnight.source.mobosses.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.phys.Vec3;

public class NaturalMeleeAttackGoal extends MeleeAttackGoal {
   private int strafeTicksLeft = 0;
   private int strafeCooldown = 0;
   private int strafeDirection = 1;
   private int windupTicks = 0;

   public NaturalMeleeAttackGoal(PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
      super(mob, speedModifier, followingTargetEvenIfNotSeen);
   }

   public void start() {
      super.start();
      this.strafeTicksLeft = 0;
      this.strafeCooldown = 20 + this.mob.getRandom().nextInt(40);
      this.windupTicks = 0;
   }

   public void tick() {
      LivingEntity target = this.mob.getTarget();
      if (target == null) {
         super.tick();
      } else {
         double distSqr = this.mob.distanceToSqr(target);
         double reachSqr = this.mob.getBbWidth() * 2.0F * (this.mob.getBbWidth() * 2.0F) + target.getBbWidth();
         if (distSqr <= reachSqr) {
            this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
            this.strafeTicksLeft = 0;
            if (this.windupTicks < 4) {
               this.windupTicks++;
               this.mob.getNavigation().stop();
            } else {
               super.tick();
            }
         } else {
            this.windupTicks = 0;
            if (this.strafeTicksLeft > 0 && distSqr < reachSqr * 9.0) {
               this.strafeTicksLeft--;
               Vec3 toTarget = target.position().subtract(this.mob.position());
               if (toTarget.lengthSqr() > 1.0E-4) {
                  toTarget = toTarget.normalize();
                  Vec3 lateral = new Vec3(-toTarget.z, 0.0, toTarget.x).scale(this.strafeDirection);
                  double orbitRadius = Math.sqrt(reachSqr) * 1.5;
                  Vec3 dest = target.position().add(lateral.scale(orbitRadius));
                  this.mob.getNavigation().moveTo(dest.x, dest.y, dest.z, 1.0);
               }

               this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
            } else {
               if (--this.strafeCooldown <= 0 && distSqr < reachSqr * 9.0) {
                  this.strafeCooldown = 60 + this.mob.getRandom().nextInt(60);
                  this.strafeTicksLeft = 8 + this.mob.getRandom().nextInt(8);
                  this.strafeDirection = this.mob.getRandom().nextBoolean() ? 1 : -1;
               }

               super.tick();
            }
         }
      }
   }
}
