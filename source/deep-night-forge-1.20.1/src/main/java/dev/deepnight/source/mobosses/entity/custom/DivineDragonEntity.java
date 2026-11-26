package dev.deepnight.source.mobosses.entity.custom;

import dev.deepnight.source.mobosses.MoBosses;
import dev.deepnight.source.mobosses.entity.ai.SoulsMeleeAttackGoal;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.util.GeckoLibUtil;

public class DivineDragonEntity extends dev.deepnight.NightMob implements GeoEntity {
   private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
   private int roarTimer = 0;
   private int solarFlareTimer = 100;
   private int pillarTimer = 200;
   private int gustTimer = 300;
   private int musicTick = 0;
   private boolean isGroundPounding = false;
   private boolean wasTargetingPreviously = false;
   private SoulsMeleeAttackGoal meleeGoal;
   private final ServerBossEvent bossEvent = (ServerBossEvent)new ServerBossEvent(this.getDisplayName(), BossBarColor.YELLOW, BossBarOverlay.NOTCHED_12)
      .setDarkenScreen(true);

   public DivineDragonEntity(EntityType<? extends dev.deepnight.NightMob> type, Level level) {
      super(type, level, dev.deepnight.Species.MOBOSSES_DIVINE_DRAGON);
      this.moveControl = new FlyingMoveControl(this, 180, true);
   }

   public boolean removeWhenFarAway(double distanceToClosestPlayer) {
      return false;
   }

   public static Builder createAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.MAX_HEALTH, 600.0)
         .add(Attributes.FOLLOW_RANGE, 64.0)
         .add(Attributes.MOVEMENT_SPEED, 0.6)
         .add(Attributes.FLYING_SPEED, 1.5)
         .add(Attributes.ATTACK_DAMAGE, 35.0)
         .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
         .add(Attributes.ARMOR, 20.0);
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
         this,
         1.4,
         "attack_controller",
         () -> !this.isDeadOrDying() && !this.isGroundPounding,
         3,
         new SoulsMeleeAttackGoal.AttackDef("attack", 60, new int[]{36}, 3.0)
      );
      this.goalSelector.addGoal(1, this.meleeGoal);
      this.goalSelector.addGoal(2, new WaterAvoidingRandomFlyingGoal(this, 1.0));
      this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 32.0F));
      this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
   }

   public boolean isEnraged() {
      return this.getHealth() < this.getMaxHealth() / 2.0F;
   }

   public boolean fireImmune() {
      return true;
   }

   public boolean hurt(DamageSource source, float amount) {
      return !source.is(DamageTypes.FALL) && !source.is(DamageTypes.EXPLOSION) ? super.hurt(source, amount) : false;
   }

   public void aiStep() {
      super.aiStep();
      if (!this.level().isClientSide) {
         LivingEntity target = this.getTarget();
         if (target != null && target.isAlive()) {
            this.handleMusic();
            int cooldownMod = this.isEnraged() ? 2 : 1;
            this.bossEvent.setColor(this.isEnraged() ? BossBarColor.RED : BossBarColor.YELLOW);
            if (!this.isAttacking()) {
               if (this.roarTimer++ > 200 / cooldownMod) {
                  this.performDivineRoar();
                  this.roarTimer = 0;
               }

               if (this.solarFlareTimer++ > 260 / cooldownMod) {
                  this.performSolarFlare(target);
                  this.solarFlareTimer = 0;
               }

               if (this.pillarTimer++ > 320 / cooldownMod) {
                  this.performDivinePillars(target);
                  this.pillarTimer = 0;
               }

               if (this.gustTimer++ > 160 / cooldownMod) {
                  this.performWingGust(target);
                  this.gustTimer = 0;
               }

               if (!this.isGroundPounding && this.getY() > target.getY() + 8.0 && this.distanceToSqr(target) < 30.0) {
                  this.isGroundPounding = true;
                  this.triggerAnim("attack_controller", "explosion");
               }
            }

            if (this.isGroundPounding) {
               this.setDeltaMovement(0.0, -2.0, 0.0);
               if (this.onGround()) {
                  this.performImpactEffect();
                  this.isGroundPounding = false;
               }
            } else if (!this.isAttacking()) {
               this.getMoveControl().setWantedPosition(target.getX(), target.getY() + 3.0, target.getZ(), 1.6);
               this.getLookControl().setLookAt(target, 20.0F, 20.0F);
            }

            this.wasTargetingPreviously = true;
         } else if (this.wasTargetingPreviously) {
            this.musicTick = 0;
            this.wasTargetingPreviously = false;
         }

         this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
      }
   }

   private void performDivineRoar() {
      this.triggerAnim("attack_controller", "angel_destruction");
      this.playSound((SoundEvent)MoBosses.DRAGON_GROWL.get(), 2.5F, 0.7F);

      for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(12.0))) {
         if (entity != this) {
            Vec3 push = entity.position().subtract(this.position()).normalize().scale(2.5);
            entity.push(push.x, 0.6, push.z);
            entity.hurt(new net.minecraft.world.damagesource.DamageSource(this.damageSources().magic().typeHolder(), this, this), 12.0F);
         }
      }
   }

   private void performSolarFlare(LivingEntity target) {
      this.triggerAnim("attack_controller", "aliento1");
      this.playSound(SoundEvents.BEACON_ACTIVATE, 2.0F, 1.0F);

      for (int i = 0; i < 3; i++) {
         LightningBolt bolt = (LightningBolt)EntityType.LIGHTNING_BOLT.create(this.level());
         if (bolt != null) {
            bolt.moveTo(target.getX() + (this.random.nextDouble() * 6.0 - 3.0), target.getY(), target.getZ() + (this.random.nextDouble() * 6.0 - 3.0));
            this.level().addFreshEntity(bolt);
         }
      }
   }

   private void performDivinePillars(LivingEntity target) {
      this.triggerAnim("attack_controller", "aliento2");
      this.playSound(SoundEvents.AMETHYST_BLOCK_CHIME, 2.0F, 0.5F);
   }

   private void performWingGust(LivingEntity target) {
      this.triggerAnim("attack_controller", "attack");
      this.playSound(SoundEvents.ENDER_DRAGON_FLAP, 3.0F, 0.6F);
      Vec3 look = this.getLookAngle();
      target.push(look.x * 4.0, 0.3, look.z * 4.0);
   }

   private void performImpactEffect() {
      this.playSound((SoundEvent)SoundEvents.GENERIC_EXPLODE, 2.0F, 0.8F);
      if (this.level() instanceof ServerLevel sl) {
         sl.sendParticles(ParticleTypes.DRAGON_BREATH, this.getX(), this.getY(), this.getZ(), 80, 3.0, 1.0, 3.0, 0.2);
      }

      this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(6.0)).forEach(e -> {
         if (e != this) {
            e.hurt(this.damageSources().mobAttack(this), 20.0F);
         }
      });
   }

   private void handleMusic() {
      if (this.isAlive() && this.getTarget() != null) {
         if (this.musicTick <= 0) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)MoBosses.DRAGON_MUSIC.get(), SoundSource.RECORDS, 1.0F, 1.0F);
            this.musicTick = 1200;
         }

         this.musicTick--;
      } else {
         this.stopBossMusic();
         this.musicTick = 0;
      }
   }

   private void stopBossMusic() {
      if (!this.level().isClientSide) {
         ServerLevel serverLevel = (ServerLevel)this.level();

         for (ServerPlayer player : serverLevel.players()) {
            if (player.distanceToSqr(this) < 4096.0) {
               player.connection.send(new ClientboundStopSoundPacket(((SoundEvent)MoBosses.DRAGON_MUSIC.get()).getLocation(), SoundSource.RECORDS));
            }
         }
      }
   }

   public void registerControllers(ControllerRegistrar controllers) {
      controllers.add(
         new AnimationController(
            this,
            "base_controller",
            5,
            state -> {
               if (this.isDeadOrDying() || this.isAttacking()) {
                  return PlayState.STOP;
               } else {
                  return !state.isMoving() && this.onGround()
                     ? state.setAndContinue(RawAnimation.begin().thenLoop("idle"))
                     : state.setAndContinue(RawAnimation.begin().thenLoop("walk"));
               }
            }
         )
      );
      controllers.add(
         new AnimationController(this, "attack_controller", 4, state -> PlayState.STOP)
            .triggerableAnim("aliento1", RawAnimation.begin().thenPlay("aliento1"))
            .triggerableAnim("aliento2", RawAnimation.begin().thenPlay("aliento2"))
            .triggerableAnim("explosion", RawAnimation.begin().thenPlay("explosion"))
            .triggerableAnim("angel_destruction", RawAnimation.begin().thenPlay("angel_destruction"))
            .triggerableAnim("attack", RawAnimation.begin().thenPlay("attack"))
            .triggerableAnim("death", RawAnimation.begin().thenPlayAndHold("death"))
      );
   }

   protected void tickDeath() {
      super.tickDeath();
      if (this.deathTime == 1) {
         this.triggerAnim("attack_controller", "death");
      }
   }

   public void die(DamageSource cause) {
      super.die(cause);
      this.stopBossMusic();
   }

   public void remove(RemovalReason reason) {
      this.stopBossMusic();
      super.remove(reason);
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

   public ResourceLocation getDefaultLootTable() { return new ResourceLocation("deepnight", "entities/mobosses_divine_dragon"); }
}
