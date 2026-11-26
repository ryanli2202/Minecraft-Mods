package dev.deepnight.source.mobosses.entity.custom;

import dev.deepnight.source.mobosses.entity.ai.SoulsMeleeAttackGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.util.GeckoLibUtil;

public class RoyalKnightEntity extends dev.deepnight.NightMob implements GeoEntity {
   private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
   private SoulsMeleeAttackGoal attackGoal;

   public RoyalKnightEntity(EntityType<? extends dev.deepnight.NightMob> type, Level level) {
      super(type, level, dev.deepnight.Species.MOBOSSES_ROYAL_KNIGHT);
   }

   public static Builder createAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.MAX_HEALTH, 40.0)
         .add(Attributes.MOVEMENT_SPEED, 0.33)
         .add(Attributes.ATTACK_DAMAGE, 8.0)
         .add(Attributes.FOLLOW_RANGE, 32.0)
         .add(Attributes.ARMOR, 8.0)
         .add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
   }

   public boolean isAttacking() {
      return this.attackGoal != null && this.attackGoal.isAttacking();
   }

   public boolean isPushable() {
      return false;
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.attackGoal = new SoulsMeleeAttackGoal(
         this,
         1.35,
         "attack_controller",
         () -> !this.isDeadOrDying(),
         3,
         new SoulsMeleeAttackGoal.AttackDef("attack1", 45, new int[]{27}, 1.5),
         new SoulsMeleeAttackGoal.AttackDef("attack2", 45, new int[]{27}, 1.5),
         new SoulsMeleeAttackGoal.AttackDef("attack3", 45, new int[]{27}, 1.5),
         new SoulsMeleeAttackGoal.AttackDef("attack4", 34, new int[]{20}, 1.5)
      );
      this.goalSelector.addGoal(2, this.attackGoal);
      this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
      this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
   }

   public void registerControllers(ControllerRegistrar controllers) {
      controllers.add(new AnimationController(this, "base_controller", 5, state -> {
         if (this.isAttacking()) {
            return PlayState.STOP;
         } else {
            return state.isMoving() ? state.setAndContinue(RawAnimation.begin().thenLoop("walk")) : state.setAndContinue(RawAnimation.begin().thenLoop("idle"));
         }
      }));
      controllers.add(
         new AnimationController(this, "attack_controller", 2, state -> PlayState.STOP)
            .triggerableAnim("attack1", RawAnimation.begin().thenPlay("attack1"))
            .triggerableAnim("attack2", RawAnimation.begin().thenPlay("attack2"))
            .triggerableAnim("attack3", RawAnimation.begin().thenPlay("attack3"))
            .triggerableAnim("attack4", RawAnimation.begin().thenPlay("attack4"))
      );
   }

   public AnimatableInstanceCache getAnimatableInstanceCache() {
      return this.cache;
   }
}
