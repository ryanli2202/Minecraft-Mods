package dev.deepnight.source.mobosses.entity.custom;

import dev.deepnight.source.mobosses.entity.ai.SoulsMeleeAttackGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
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

public class DemonKingMinionEntity extends dev.deepnight.NightMob implements GeoEntity {
   private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
   private SoulsMeleeAttackGoal attackGoal;
   private static final int DEATH_ANIMATION_TICKS = 25;

   public DemonKingMinionEntity(EntityType<? extends dev.deepnight.NightMob> type, Level level) {
      super(type, level, dev.deepnight.Species.MOBOSSES_DEMON_KING_MINION);
      this.moveControl = new FlyingMoveControl(this, 20, true);
   }

   protected PathNavigation createNavigation(Level level) {
      return new FlyingPathNavigation(this, level);
   }

   public static Builder createAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.MAX_HEALTH, 30.0)
         .add(Attributes.MOVEMENT_SPEED, 0.34)
         .add(Attributes.ATTACK_DAMAGE, 6.0)
         .add(Attributes.FLYING_SPEED, 0.75)
         .add(Attributes.FOLLOW_RANGE, 32.0)
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
         this, 1.4, "action_controller", () -> !this.isDeadOrDying(), 3, new SoulsMeleeAttackGoal.AttackDef("attack", 60, new int[]{36}, 1.5)
      );
      this.goalSelector.addGoal(1, this.attackGoal);
      this.goalSelector.addGoal(2, new WaterAvoidingRandomFlyingGoal(this, 1.0));
      this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
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
         new AnimationController(this, "action_controller", 3, state -> PlayState.STOP)
            .triggerableAnim("attack", RawAnimation.begin().thenPlay("attack"))
            .triggerableAnim("death", RawAnimation.begin().thenPlayAndHold("death"))
      );
   }

   protected void tickDeath() {
      this.deathTime++;
      if (this.deathTime == 1) {
         this.triggerAnim("action_controller", "death");
      }

      if (this.deathTime == 25) {
         this.level().broadcastEntityEvent(this, (byte)60);
         this.remove(RemovalReason.KILLED);
      }
   }

   public AnimatableInstanceCache getAnimatableInstanceCache() {
      return this.cache;
   }
}
