package dev.deepnight.source.dawn.entity;

import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.init.SimpleMobsModItems;
import dev.deepnight.source.dawn.procedures.NoaiProcedure;
import dev.deepnight.source.dawn.procedures.ProelehurtProcedure;
import dev.deepnight.source.dawn.procedures.ProelementalProcedure;
import dev.deepnight.source.dawn.procedures.ProelementaldeathProcedure;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.BossEvent.BossBarColor;
import net.minecraft.world.BossEvent.BossBarOverlay;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
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

public class ElementalDeityEntity extends dev.deepnight.NightMob implements GeoEntity {
   private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), BossBarColor.YELLOW, BossBarOverlay.NOTCHED_6);
   private final AnimatableInstanceCache factory = software.bernie.geckolib.util.GeckoLibUtil.createInstanceCache(this);

   public ElementalDeityEntity(SpawnEntity packet, Level world) {
      this(SimpleMobsModEntities.ELEMENTAL_DEITY.get(), world);
   }

   @Override
   protected float getEquipmentDropChance(EquipmentSlot slot) {
      return 0.0F;
   }

   public ElementalDeityEntity(EntityType<? extends dev.deepnight.NightMob> type, Level world) {
      super(type, world, dev.deepnight.Species.DAWN_ELEMENTAL_DEITY);
      this.xpReward = 0;
      this.setNoAi(false);
      this.noCulling = true;
      this.setPersistenceRequired();
      this.setMaxUpStep(3.0F);
   }

   @Override
   public Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }

   @Override
   protected void registerGoals() {
      super.registerGoals();
      this.goalSelector.addGoal(1, new NineTailsEntity.StrafeGoal(this, 0.4, 16.0F) {
         @Override
         public boolean canUse() {
            double x = ElementalDeityEntity.this.getX();
            double y = ElementalDeityEntity.this.getY();
            double z = ElementalDeityEntity.this.getZ();
            Entity entity = ElementalDeityEntity.this;
            Level world = ElementalDeityEntity.this.level();
            return super.canUse() && NoaiProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            double x = ElementalDeityEntity.this.getX();
            double y = ElementalDeityEntity.this.getY();
            double z = ElementalDeityEntity.this.getZ();
            Entity entity = ElementalDeityEntity.this;
            Level world = ElementalDeityEntity.this.level();
            return super.canContinueToUse() && NoaiProcedure.execute(entity);
         }
      });
      this.goalSelector.addGoal(2, new NineTailsEntity.FollowTargetGoal(this, 1.0, 64.0F) {
         @Override
         public boolean canUse() {
            Entity entity = ElementalDeityEntity.this;
            Level world = ElementalDeityEntity.this.level();
            return super.canUse() && NoaiProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = ElementalDeityEntity.this;
            Level world = ElementalDeityEntity.this.level();
            return super.canContinueToUse() && NoaiProcedure.execute(entity);
         }
      });
      this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, Player.class, false, false) {
         @Override
         public boolean canUse() {
            Entity entity = ElementalDeityEntity.this;
            Level world = ElementalDeityEntity.this.level();
            return super.canUse() && NoaiProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = ElementalDeityEntity.this;
            Level world = ElementalDeityEntity.this.level();
            return super.canContinueToUse() && NoaiProcedure.execute(entity);
         }
      });
      this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this, ServerPlayer.class, false, false) {
         @Override
         public boolean canUse() {
            Entity entity = ElementalDeityEntity.this;
            Level world = ElementalDeityEntity.this.level();
            return super.canUse() && NoaiProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = ElementalDeityEntity.this;
            Level world = ElementalDeityEntity.this.level();
            return super.canContinueToUse() && NoaiProcedure.execute(entity);
         }
      });
      this.goalSelector.addGoal(5, new RandomLookAroundGoal(this) {
         @Override
         public boolean canUse() {
            Entity entity = ElementalDeityEntity.this;
            Level world = ElementalDeityEntity.this.level();
            return super.canUse() && NoaiProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = ElementalDeityEntity.this;
            Level world = ElementalDeityEntity.this.level();
            return super.canContinueToUse() && NoaiProcedure.execute(entity);
         }
      });
      this.goalSelector.addGoal(6, new FloatGoal(this) {
         @Override
         public boolean canUse() {
            Entity entity = ElementalDeityEntity.this;
            Level world = ElementalDeityEntity.this.level();
            return super.canUse() && NoaiProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = ElementalDeityEntity.this;
            Level world = ElementalDeityEntity.this.level();
            return super.canContinueToUse() && NoaiProcedure.execute(entity);
         }
      });
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
   public SoundEvent getHurtSound(DamageSource ds) {
      return dev.deepnight.SafeSounds.resolve("");
   }

   @Override
   public SoundEvent getDeathSound() {
      return dev.deepnight.SafeSounds.resolve("");
   }

   @Override
   public boolean canChangeDimensions() {
      return false;
   }

   @Override
   protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHitIn) {
      super.dropCustomDeathLoot(source, looting, recentlyHitIn);
      this.spawnAtLocation(new ItemStack(SimpleMobsModItems.ELEMENTAL_GAUNTLET.get()));
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
   public void baseTick() {
      super.baseTick();
      if(dev.deepnight.source.SourceStun.stunned(this))return;
      ProelementalProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      ProelehurtProcedure.execute(this.level(), this);
      if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.PAPER) {
         return false;
      } else {
         return this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.CARROT ? false : super.hurt(source, amount);
      }
   }

   @Override
   public void tick() {
      super.tick();
      // Source attacks and stuns can pause AI while health continues changing.
      if (!this.level().isClientSide()) this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
   }

   public static void init() {
   }

   public static Builder createAttributes() {
      Builder builder = Mob.createMobAttributes();
      builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
      builder = builder.add(Attributes.MAX_HEALTH, 450.0);
      builder = builder.add(Attributes.ARMOR, 8.0);
      builder = builder.add(Attributes.ATTACK_DAMAGE, 3.0);
      builder = builder.add(Attributes.FOLLOW_RANGE, 64.0);
      return builder.add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
   }

   private PlayState predicate(AnimationState animationEvent) {
      if (!this.isAlive()) {
         animationEvent.getController().setTransitionLength(3);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.elemental_god.death",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.LEATHER_HELMET) {
         animationEvent.getController().setTransitionLength(10);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.elemental_god.attack1",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.GOLD_NUGGET) {
         animationEvent.getController().setTransitionLength(10);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.elemental_god.attack1b",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.COAL) {
         animationEvent.getController().setTransitionLength(10);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.elemental_god.attack2",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.CHARCOAL) {
         animationEvent.getController().setTransitionLength(15);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.elemental_god.attack3",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.IRON_INGOT) {
         animationEvent.getController().setTransitionLength(10);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.elemental_god.attack4",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.IRON_NUGGET) {
         animationEvent.getController().setTransitionLength(0);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.elemental_god.attack5",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.WHITE_WOOL) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.elemental_god.attack5grabbed",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.WHITE_CONCRETE) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.elemental_god.attack6",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.RED_WOOL) {
         animationEvent.getController().setTransitionLength(15);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.elemental_god.attack7",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.YELLOW_WOOL) {
         animationEvent.getController().setTransitionLength(10);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.elemental_god.attack8",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.GREEN_WOOL) {
         animationEvent.getController().setTransitionLength(0);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.elemental_god.attack9",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.ACACIA_LEAVES) {
         animationEvent.getController().setTransitionLength(0);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.elemental_god.attack10",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.SADDLE) {
         animationEvent.getController().setTransitionLength(0);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.elemental_god.attack11",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.POPPY) {
         animationEvent.getController().setTransitionLength(0);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.elemental_god.spawn",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.PAPER) {
         animationEvent.getController().setTransitionLength(0);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.elemental_god.dodge",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.CARROT) {
         animationEvent.getController().setTransitionLength(0);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.elemental_god.dodge2",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (animationEvent.isMoving()) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().thenLoop("animation.elemental_god.walk"));
      } else {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().thenLoop("animation.elemental_god.idle"));
      }

      return PlayState.CONTINUE;
   }

   public void registerControllers(ControllerRegistrar data) {
      data.add(new AnimationController<>(this, "controller", 0, this::predicate));
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

   @Override
   public void die(DamageSource source) {
      super.die(source);
      ProelementaldeathProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ());
   }

   int getMaxDeathCount() {
      return 239;
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

   public AnimatableInstanceCache getAnimatableInstanceCache() {
      return this.factory;
   }
}
