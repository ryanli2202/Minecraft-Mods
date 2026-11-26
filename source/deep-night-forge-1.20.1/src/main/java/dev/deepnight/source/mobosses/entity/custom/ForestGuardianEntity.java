package dev.deepnight.source.mobosses.entity.custom;

import dev.deepnight.source.mobosses.MoBosses;
import dev.deepnight.source.mobosses.entity.ai.SoulsMeleeAttackGoal;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.BossEvent.BossBarColor;
import net.minecraft.world.BossEvent.BossBarOverlay;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
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

public class ForestGuardianEntity extends dev.deepnight.NightMob implements GeoEntity {
   private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
   private final ServerBossEvent bossEvent = (ServerBossEvent)new ServerBossEvent(this.getDisplayName(), BossBarColor.PURPLE, BossBarOverlay.PROGRESS)
      .setDarkenScreen(true);
   private int attackAnimationTimeout = 0;
   private SoulsMeleeAttackGoal meleeGoal;

   public ForestGuardianEntity(EntityType<? extends dev.deepnight.NightMob> type, Level level) {
      super(type, level, dev.deepnight.Species.MOBOSSES_FOREST_GUARDIAN);
      this.setPersistenceRequired();
   }

   public boolean fireImmune() {
      return true;
   }

   public boolean canBeAffected(MobEffectInstance effect) {
      return effect.getEffect()!=MobEffects.POISON && super.canBeAffected(effect);
   }

   public void checkDespawn() {
   }

   public boolean shouldDespawnInPeaceful() {
      return false;
   }

   public boolean isAttacking() {
      return this.meleeGoal != null && this.meleeGoal.isAttacking();
   }

   public boolean isPushable() {
      return false;
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.meleeGoal = new SoulsMeleeAttackGoal(
         this, 1.3, "attack_controller", () -> !this.isDeadOrDying(), 3, new SoulsMeleeAttackGoal.AttackDef("skill2", 90, new int[]{54}, 2.0)
      );
      this.goalSelector.addGoal(1, this.meleeGoal);
      this.goalSelector.addGoal(2, new ForestGuardianEntity.ForestMagicGoal(this));
      this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0));
      this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
      this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
   }

   public static Builder createAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.MAX_HEALTH, 500.0)
         .add(Attributes.MOVEMENT_SPEED, 0.31)
         .add(Attributes.ATTACK_DAMAGE, 20.0)
         .add(Attributes.ARMOR, 15.0)
         .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
         .add(Attributes.FOLLOW_RANGE, 64.0);
   }

   public void swing(InteractionHand hand, boolean updateSelf) {
      super.swing(hand, updateSelf);
      this.level().broadcastEntityEvent(this, (byte)4);
   }

   public void handleEntityEvent(byte id) {
      if (id == 4) {
         this.attackAnimationTimeout = 80;
      } else {
         super.handleEntityEvent(id);
      }
   }

   public void tick() {
      super.tick();
      if (this.attackAnimationTimeout > 0) {
         this.attackAnimationTimeout--;
      }
   }

   public void aiStep() {
      super.aiStep();
      this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
   }

   public void startSeenByPlayer(ServerPlayer player) {
      super.startSeenByPlayer(player);
      this.bossEvent.addPlayer(player);
   }

   public void stopSeenByPlayer(ServerPlayer player) {
      super.stopSeenByPlayer(player);
      this.bossEvent.removePlayer(player);
   }

   public void registerControllers(ControllerRegistrar controllers) {
      controllers.add(new AnimationController(this, "controller", 5, state -> {
         if (this.isAttacking()) {
            return PlayState.STOP;
         } else if (this.attackAnimationTimeout > 0) {
            return state.setAndContinue(RawAnimation.begin().thenPlay("skill2"));
         } else {
            return state.isMoving() ? state.setAndContinue(RawAnimation.begin().thenLoop("walk")) : state.setAndContinue(RawAnimation.begin().thenLoop("idle"));
         }
      }));
      controllers.add(
         new AnimationController(this, "attack_controller", 2, state -> PlayState.STOP).triggerableAnim("skill2", RawAnimation.begin().thenPlay("skill2"))
      );
   }

   public AnimatableInstanceCache getAnimatableInstanceCache() {
      return this.cache;
   }

   static class ForestMagicGoal extends Goal {
      private final ForestGuardianEntity guardian;
      private int cooldownTimer;

      public ForestMagicGoal(ForestGuardianEntity guardian) {
         this.guardian = guardian;
         // This spell does not control movement or looking. Claiming LOOK lets
         // the higher-priority melee pursuit permanently starve it.
      }

      @Override
      public boolean requiresUpdateEveryTick() {
         return true;
      }

      public boolean canUse() {
         return this.guardian.getTarget() != null && this.guardian.getTarget().isAlive()
            && this.guardian.distanceTo(this.guardian.getTarget()) < 20.0F && !this.guardian.isAttacking();
      }

      public void tick() {
         LivingEntity target = this.guardian.getTarget();
         if (target != null) {
            this.cooldownTimer++;
            if (this.cooldownTimer >= 100) {
               this.guardian.level().broadcastEntityEvent(this.guardian, (byte)4);
               Level level = this.guardian.level();
               ForestGuardianAuraEntity aura = (ForestGuardianAuraEntity)((EntityType)MoBosses.FOREST_GUARDIAN_AURA.get()).create(level);
               if (aura != null) {
                  aura.moveTo(target.getX(), target.getY(), target.getZ());
                  level.addFreshEntity(aura);
               }

               for (int i = 0; i < 20; i++) {
                  LightningBolt lightning = (LightningBolt)EntityType.LIGHTNING_BOLT.create(level);
                  if (lightning != null) {
                     lightning.moveTo(
                        target.getX() + (level.random.nextDouble() - 0.5) * 10.0, target.getY(), target.getZ() + (level.random.nextDouble() - 0.5) * 10.0
                     );
                     level.addFreshEntity(lightning);
                  }
               }

               this.cooldownTimer = 0;
            }
         }
      }
   }
}
