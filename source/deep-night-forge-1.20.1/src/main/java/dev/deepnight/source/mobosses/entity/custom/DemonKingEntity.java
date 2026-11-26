package dev.deepnight.source.mobosses.entity.custom;

import dev.deepnight.source.mobosses.MoBosses;
import dev.deepnight.source.mobosses.entity.ai.SoulsMeleeAttackGoal;
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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.util.GeckoLibUtil;

public class DemonKingEntity extends dev.deepnight.NightMob implements GeoEntity {
   private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
   private int specialAttackTimer = 0;
   private int musicTick = 0;
   private boolean wasTargetingPreviously = false;
   private SoulsMeleeAttackGoal meleeGoal;
   private int strikeElement;
   private static final int DEATH_ANIMATION_TICKS = 50;
   private final ServerBossEvent bossEvent = (ServerBossEvent)new ServerBossEvent(this.getDisplayName(), BossBarColor.PURPLE, BossBarOverlay.PROGRESS)
      .setDarkenScreen(true);

   public DemonKingEntity(EntityType<? extends dev.deepnight.NightMob> type, Level level) {
      super(type, level, dev.deepnight.Species.MOBOSSES_DEMON_KING);
   }

   public boolean removeWhenFarAway(double distanceToClosestPlayer) {
      return false;
   }

   public static Builder createAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.MAX_HEALTH, 800.0)
         .add(Attributes.MOVEMENT_SPEED, 0.28)
         .add(Attributes.ATTACK_DAMAGE, 15.0)
         .add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
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
         () -> !this.isDeadOrDying(),
         4,
         null,
         this::elementalHit,
         new SoulsMeleeAttackGoal.AttackDef("combo_1", 160, new int[]{40, 80, 120}, 1.8),
         new SoulsMeleeAttackGoal.AttackDef("combo_2", 90, new int[]{40, 75}, 1.8)
      ) {
         @Override protected void onAttackFrame(AttackDef attack, int tick) {
            DemonKingEntity.this.elementalFrame(attack, tick);
         }
      };
      this.goalSelector.addGoal(1, this.meleeGoal);
      this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0));
      this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
   }

   private void handleMusic() {
      if (this.isAlive() && this.getTarget() != null) {
         if (this.musicTick <= 0) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), (SoundEvent)MoBosses.DEMON_KING_MUSIC.get(), SoundSource.RECORDS, 1.0F, 1.0F);
            this.musicTick = 1200;
         }

         this.musicTick--;
      } else {
         this.stopBossMusic();
         this.musicTick = 0;
      }
   }

   private void stopBossMusic() {
      if (!this.level().isClientSide && this.level() instanceof ServerLevel serverLevel) {
         for (ServerPlayer player : serverLevel.players()) {
            if (player.distanceToSqr(this) < 4096.0) {
               player.connection.send(new ClientboundStopSoundPacket(((SoundEvent)MoBosses.DEMON_KING_MUSIC.get()).getLocation(), SoundSource.RECORDS));
            }
         }
      }
   }

   public void aiStep() {
      super.aiStep();
      if (!this.level().isClientSide) {
         LivingEntity target = this.getTarget();
         if (target != null && target.isAlive()) {
            this.handleMusic();
            if (!this.isAttacking()) {
               this.specialAttackTimer++;
               if (this.specialAttackTimer >= 200) {
                  this.triggerAnim("attack_controller", "invocar");
                  this.summoningEffects();
                  this.spawnMinions();

                  for (int i = 0; i < 3; i++) {
                     double xOffset = (this.random.nextDouble() - 0.5) * 6.0;
                     double zOffset = (this.random.nextDouble() - 0.5) * 6.0;
                     if (this.level() instanceof ServerLevel serverLevel) {
                        DemonKingLightningEntity lightning = (DemonKingLightningEntity)((EntityType)MoBosses.DEMON_KING_LIGHTNING.get()).create(serverLevel);
                        if (lightning != null) {
                           lightning.moveTo(target.getX() + xOffset, target.getY(), target.getZ() + zOffset);
                           serverLevel.addFreshEntity(lightning);
                        }
                     }
                  }

                  this.specialAttackTimer = 0;
               }
            }

            this.wasTargetingPreviously = true;
         } else if (this.wasTargetingPreviously) {
            this.stopBossMusic();
            this.musicTick = 0;
            this.wasTargetingPreviously = false;
         }

         this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
      }
   }

   private void spawnMinions() {
      if (this.level() instanceof ServerLevel serverLevel) {
         for (int i = 0; i < 3; i++) {
            DemonKingMinionEntity minion = (DemonKingMinionEntity)((EntityType)MoBosses.DEMON_KING_MINION.get()).create(serverLevel);
            if (minion != null) {
               double x = this.getX() + (this.random.nextDouble() - 0.5) * 4.0;
               double z = this.getZ() + (this.random.nextDouble() - 0.5) * 4.0;
               minion.moveTo(x, this.getY(), z, this.random.nextFloat() * 360.0F, 0.0F);
               minion.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(minion.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
               serverLevel.addFreshEntity(minion);
               serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.SOUL_FIRE_FLAME, x, this.getY()+0.8, z, 24, 0.5, 0.8, 0.5, 0.02);
               serverLevel.playSound(null, x, this.getY(), z, SoundEvents.FIRECHARGE_USE, SoundSource.HOSTILE, 0.65F, 0.7F);
            }
         }
      }
   }

   private void elementalFrame(SoulsMeleeAttackGoal.AttackDef attack, int tick) {
      if (!(level() instanceof ServerLevel server)) return;
      for (int i=0;i<attack.hitTicks.length;i++) {
         int remaining=attack.hitTicks[i]-tick;
         if (remaining<0 || remaining>12) continue;
         strikeElement=attack.animName.equals("combo_2")?(i==0?2:0):i%3;
         var particle=switch(strikeElement) {
            case 0 -> net.minecraft.core.particles.ParticleTypes.FLAME;
            case 1 -> net.minecraft.core.particles.ParticleTypes.SNOWFLAKE;
            default -> net.minecraft.core.particles.ParticleTypes.ELECTRIC_SPARK;
         };
         var sound=switch(strikeElement) {
            case 0 -> SoundEvents.FIRECHARGE_USE;
            case 1 -> SoundEvents.GLASS_BREAK;
            default -> SoundEvents.LIGHTNING_BOLT_IMPACT;
         };
         if (remaining==12) server.playSound(null,getX(),getY(),getZ(),SoundEvents.EVOKER_PREPARE_ATTACK,SoundSource.HOSTILE,0.5F,0.8F+strikeElement*0.25F);
         if (remaining%3==0) {
            double facing=Math.toRadians(getYHeadRot());
            double radius=remaining==0?3.5:1.8;
            for (int point=0;point<13;point++) {
               double angle=facing+Math.toRadians(-70+point*140.0/12);
               server.sendParticles(particle,getX()-Math.sin(angle)*radius,getY()+1.5,getZ()+Math.cos(angle)*radius,1,0.08,0.15,0.08,0.02);
            }
         }
         if (remaining==0) server.playSound(null,getX(),getY()+1.5,getZ(),sound,SoundSource.HOSTILE,0.9F,0.85F);
         break;
      }
   }

   private void elementalHit(LivingEntity target) {
      if (!(level() instanceof ServerLevel server)) return;
      switch(strikeElement) {
         case 0 -> target.setSecondsOnFire(3);
         case 1 -> target.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN,60,0));
         default -> target.knockback(0.6,getX()-target.getX(),getZ()-target.getZ());
      }
      server.sendParticles(net.minecraft.core.particles.ParticleTypes.CRIT,target.getX(),target.getY()+target.getBbHeight()*0.5,target.getZ(),12,0.35,0.5,0.35,0.1);
   }

   private void summoningEffects() {
      if (!(level() instanceof ServerLevel server)) return;
      server.playSound(null,blockPosition(),SoundEvents.EVOKER_CAST_SPELL,SoundSource.HOSTILE,1.1F,0.65F);
      for (int i=0;i<40;i++) {
         double angle=i*Math.PI*2/40;
         server.sendParticles(net.minecraft.core.particles.ParticleTypes.SOUL_FIRE_FLAME,getX()+Math.cos(angle)*3,getY()+0.15,getZ()+Math.sin(angle)*3,2,0.05,0.2,0.05,0.02);
      }
   }

   protected SoundEvent getAmbientSound() {
      return (SoundEvent)MoBosses.DEMON_KING_AMBIENT.get();
   }

   protected SoundEvent getHurtSound(DamageSource damageSource) {
      return (SoundEvent)MoBosses.DEMON_KING_HURT.get();
   }

   protected SoundEvent getDeathSound() {
      return (SoundEvent)MoBosses.DEMON_KING_HURT.get();
   }

   public void die(DamageSource cause) {
      super.die(cause);
      this.stopBossMusic();
   }

   public void remove(RemovalReason reason) {
      this.stopBossMusic();
      super.remove(reason);
   }

   public void registerControllers(ControllerRegistrar controllers) {
      controllers.add(
         new AnimationController(
            this,
            "base_controller",
            5,
            state -> !this.isDeadOrDying() && !this.isAttacking()
               ? state.setAndContinue(state.isMoving() ? RawAnimation.begin().thenLoop("walk") : RawAnimation.begin().thenLoop("idle"))
               : PlayState.STOP
         )
      );
      controllers.add(
         new AnimationController(this, "attack_controller", 4, state -> PlayState.STOP)
            .triggerableAnim("magic", RawAnimation.begin().thenPlay("magic"))
            .triggerableAnim("invocar", RawAnimation.begin().thenPlay("magic"))
            .triggerableAnim("combo_1", RawAnimation.begin().thenPlay("combo_1"))
            .triggerableAnim("combo_2", RawAnimation.begin().thenPlay("combo_2"))
            .triggerableAnim("death", RawAnimation.begin().thenPlayAndHold("death"))
      );
   }

   protected void tickDeath() {
      this.deathTime++;
      if (this.deathTime == 1) {
         this.triggerAnim("attack_controller", "death");
      }

      if (this.deathTime == 50) {
         this.level().broadcastEntityEvent(this, (byte)60);
         this.remove(RemovalReason.KILLED);
      }
   }

   public ResourceLocation getDefaultLootTable() { return new ResourceLocation("deepnight", "entities/mobosses_demon_king"); }

   private void summonCustomLightning(LivingEntity target) {
      if (this.level() instanceof ServerLevel serverLevel) {
         DemonKingLightningEntity lightning = (DemonKingLightningEntity)((EntityType)MoBosses.DEMON_KING_LIGHTNING.get()).create(serverLevel);
         if (lightning != null) {
            lightning.moveTo(target.getX(), target.getY(), target.getZ());
            serverLevel.addFreshEntity(lightning);
            this.level().playSound(null, this.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.HOSTILE, 1.0F, 1.0F);
         }
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
}
