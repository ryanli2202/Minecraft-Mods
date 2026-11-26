package dev.deepnight.source.dawn.entity;

import javax.annotation.Nullable;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.procedures.DragonLordSpawnProcedure;
import dev.deepnight.source.dawn.procedures.NoaiProcedure;
import dev.deepnight.source.dawn.procedures.ProDLHurtProcedure;
import dev.deepnight.source.dawn.procedures.ProDragonLordProcedure;
import dev.deepnight.source.dawn.procedures.ProdldeathProcedure;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.BossEvent.BossBarColor;
import net.minecraft.world.BossEvent.BossBarOverlay;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages.SpawnEntity;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.keyframe.event.SoundKeyframeEvent;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;

public class DragonLordEntity extends dev.deepnight.NightMob implements GeoEntity {
   private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), BossBarColor.RED, BossBarOverlay.PROGRESS);
   private final AnimatableInstanceCache factory = software.bernie.geckolib.util.GeckoLibUtil.createInstanceCache(this);

   public DragonLordEntity(SpawnEntity packet, Level world) {
      this(SimpleMobsModEntities.DRAGON_LORD.get(), world);
   }

   public DragonLordEntity(EntityType<? extends dev.deepnight.NightMob> type, Level world) {
      super(type, world, dev.deepnight.Species.DAWN_DRAGON_LORD);
      this.xpReward = 0;
      this.setNoAi(false);
      this.noCulling = true;
      this.setMaxUpStep(4.0F);
      this.setPersistenceRequired();
   }

   @Override
   public Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }

   @Override
   protected float getEquipmentDropChance(EquipmentSlot slot) {
      return 0.0F;
   }

   @Override
   protected void registerGoals() {
      super.registerGoals();
      this.goalSelector.addGoal(1, new NineTailsEntity.FollowTargetGoal(this, 1.0, 64.0F) {
         @Override
         public boolean canUse() {
            Entity entity = DragonLordEntity.this;
            Level world = DragonLordEntity.this.level();
            return super.canUse() && NoaiProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = DragonLordEntity.this;
            Level world = DragonLordEntity.this.level();
            return super.canContinueToUse() && NoaiProcedure.execute(entity);
         }
      });
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, false, false) {
         @Override
         public boolean canUse() {
            Entity entity = DragonLordEntity.this;
            Level world = DragonLordEntity.this.level();
            return super.canUse() && NoaiProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = DragonLordEntity.this;
            Level world = DragonLordEntity.this.level();
            return super.canContinueToUse() && NoaiProcedure.execute(entity);
         }
      });
      this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, ServerPlayer.class, false, false) {
         @Override
         public boolean canUse() {
            Entity entity = DragonLordEntity.this;
            Level world = DragonLordEntity.this.level();
            return super.canUse() && NoaiProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = DragonLordEntity.this;
            Level world = DragonLordEntity.this.level();
            return super.canContinueToUse() && NoaiProcedure.execute(entity);
         }
      });
      this.goalSelector.addGoal(4, new FloatGoal(this) {
         @Override
         public boolean canUse() {
            Entity entity = DragonLordEntity.this;
            Level world = DragonLordEntity.this.level();
            return super.canUse() && NoaiProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = DragonLordEntity.this;
            Level world = DragonLordEntity.this.level();
            return super.canContinueToUse() && NoaiProcedure.execute(entity);
         }
      });
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
   public void tick() {
      super.tick();
      // Source attacks and stuns use NoAI, so the health display cannot depend on AI ticks.
      if (!this.level().isClientSide()) {
         this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
      }
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
   public void baseTick() {
      super.baseTick();
      // Incoming-hit immunity must expire even while the combat AI is stunned.
      if (!this.level().isClientSide() && this.getPersistentData().getBoolean("hurtcd")) {
         double remaining = this.getPersistentData().getDouble("hurt_cd");
         remaining = remaining > 0 ? remaining - 1 : 18;
         if (remaining <= 1) {
            if (this.getItemBySlot(EquipmentSlot.FEET).is(Items.DIRT)) {
               this.setItemSlot(EquipmentSlot.FEET, net.minecraft.world.item.ItemStack.EMPTY);
            }
            this.getPersistentData().putBoolean("hurtcd", false);
            remaining = 0;
         }
         this.getPersistentData().putDouble("hurt_cd", remaining);
      }
      if(dev.deepnight.source.SourceStun.stunned(this))return;
      ProDragonLordProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      if (source.getDirectEntity() instanceof AbstractArrow) {
         return false;
      }

      if (source.getDirectEntity() instanceof ThrownPotion || source.getDirectEntity() instanceof AreaEffectCloud) {
         return false;
      }

      if (source.is(net.minecraft.world.damagesource.DamageTypes.FALL)) {
         return false;
      }

      if (this.getItemBySlot(EquipmentSlot.FEET).getItem() == Items.DIRT) {
         return false;
      }

      boolean accepted = super.hurt(source, amount);
      if (accepted && amount > 0) ProDLHurtProcedure.execute(this.level(), this);
      return accepted;
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
   public boolean isPushable() {
      return false;
   }

   @Override
   protected void pushEntities() {
   }

   @Override
   public void push(double pX, double pY, double pZ) {
   }

   @Override
   protected void doPush(Entity entityIn) {
   }

   @Override
   public boolean causeFallDamage(float l, float d, DamageSource source) {
      return false;
   }

   @Override
   public boolean fireImmune() {
      return true;
   }

   @Override
   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag
   ) {
      SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
      DragonLordSpawnProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
      return retval;
   }

   public static void init() {
   }

   public static Builder createAttributes() {
      Builder builder = Mob.createMobAttributes();
      builder = builder.add(Attributes.MOVEMENT_SPEED, 0.0);
      builder = builder.add(Attributes.MAX_HEALTH, 2000.0);
      builder = builder.add(Attributes.ARMOR, 40.0);
      builder = builder.add(Attributes.ATTACK_DAMAGE, 3.0);
      builder = builder.add(Attributes.FOLLOW_RANGE, 128.0);
      return builder.add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
   }

   private PlayState predicate(AnimationState animationEvent) {
      double d1 = this.getX() - this.xOld;
      double d0 = this.getZ() - this.zOld;
      float xvelocity = (float)Math.sqrt(d1 * d1 + d0 * d0);
      float zvelocity = (float)Math.sqrt(d1 * d1 + d0 * d0);
      float velocity = (float)Math.sqrt(d1 * d1 + d0 * d0);
      if (!this.isAlive()) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.dragon.death",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.LEATHER_HELMET) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.dragon_g.attack1",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.GOLD_NUGGET) {
         animationEvent.getController().setTransitionLength(0);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.dragon_g.attack1a",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.TORCH) {
         animationEvent.getController().setTransitionLength(0);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.dragon.spawn",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.COAL) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.dragon_g.attack2",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.CHARCOAL) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.dragon_g.attack3",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.IRON_INGOT) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.dragon_g.attack4",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.IRON_NUGGET) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.dragon_g.attack5",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.WHITE_WOOL) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.dragon_g.attack6",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.WHITE_CONCRETE) {
         animationEvent.getController().setTransitionLength(10);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.dragon_g.attack7",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.RED_WOOL) {
         animationEvent.getController().setTransitionLength(0);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.dragon_g.attack7trans",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.YELLOW_WOOL) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().thenLoop("animation.dragon_g.attack8"));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.GREEN_WOOL) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.dragon_g.attack9",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.ACACIA_LEAVES) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.dragon_g.attack10",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.OAK_LEAVES) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.dragon_g.attack10a",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.CHAIN) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.dragon_g.attack10b",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.SADDLE) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.dragon_g.attack11",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.POPPY) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.dragon_g.attack12",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.PAPER) {
         animationEvent.getController().setTransitionLength(0);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.dragon_g.attack13",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.CARROT) {
         animationEvent.getController().setTransitionLength(0);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.dragon_g.attack14",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.GOLDEN_CARROT) {
         animationEvent.getController().setTransitionLength(0);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.dragon_g.attack14a",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.SAND) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.dragon_g.up",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.onGround()) {
         if (velocity > 0.1F) {
            animationEvent.getController().setTransitionLength(5);
            animationEvent.getController().setAnimation(RawAnimation.begin().thenLoop("animation.dragon.walk"));
         } else {
            animationEvent.getController().setTransitionLength(5);
            animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.dragon.idle",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
         }
      } else {
         animationEvent.getController().setTransitionLength(10);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.dragon.fly_move",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      }

      return PlayState.CONTINUE;
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

   int getMaxDeathCount() {
      return 78;
   }

   @Override
   public void die(DamageSource source) {
      super.die(source);
      ProdldeathProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
   }

   @Override
   public void knockback(double strength, double ratioX, double ratioZ) {
      if (!this.isDeadOrDying()) {
         super.knockback(strength, ratioX, ratioZ);
      }
   }

   public void registerControllers(ControllerRegistrar data) {
      data.add(new AnimationController<>(this, "controller", 0, this::predicate));
      AnimationController controller = new AnimationController<>(this, "controller", 0, this::predicate);
      controller.setSoundKeyframeHandler(this::soundListener);
      data.add(controller);
   }

   private <ENTITY extends GeoEntity> void soundListener(SoundKeyframeEvent<ENTITY> event) {
      LocalPlayer player = Minecraft.getInstance().player;
      if (player != null) {
         if (this.onGround()) {
            player.playSound(SoundEvent.createVariableRangeEvent(new ResourceLocation("simple_mobs", "giant_footstep")), 1.0F, 2.0F);
         } else {
            player.playSound(SoundEvent.createVariableRangeEvent(new ResourceLocation("simple_mobs", "dragon_wing_flap2")), 1.0F, 2.0F);
         }
      }
   }

   public AnimatableInstanceCache getAnimatableInstanceCache() {
      return this.factory;
   }
}
