package dev.deepnight.source.dawn.entity;

import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.init.SimpleMobsModItems;
import dev.deepnight.source.dawn.procedures.NoaiProcedure;
import dev.deepnight.source.dawn.procedures.ProcavedProcedure;
import dev.deepnight.source.dawn.procedures.ProcaveddeathProcedure;
import dev.deepnight.source.dawn.procedures.ProwerehurtProcedure;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
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
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
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

public class CaveDwellerEntity extends Monster implements GeoEntity {
   private final AnimatableInstanceCache factory = software.bernie.geckolib.util.GeckoLibUtil.createInstanceCache(this);

   public CaveDwellerEntity(SpawnEntity packet, Level world) {
      this(SimpleMobsModEntities.CAVE_DWELLER.get(), world);
   }

   public CaveDwellerEntity(EntityType<CaveDwellerEntity> type, Level world) {
      super(type, world);
      this.xpReward = 25;
      this.noCulling = true;
      this.setNoAi(false);
      this.setPersistenceRequired();
      this.setMaxUpStep(1.0F);
   }

   @Override
   public Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }

   @Override
   protected void registerGoals() {
      super.registerGoals();
      this.goalSelector.addGoal(1, new NineTailsEntity.FollowTargetGoal(this, 1.0, 30.0F) {
         @Override
         public boolean canUse() {
            Entity entity = CaveDwellerEntity.this;
            Level world = CaveDwellerEntity.this.level();
            return super.canUse() && NoaiProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = CaveDwellerEntity.this;
            Level world = CaveDwellerEntity.this.level();
            return super.canContinueToUse() && NoaiProcedure.execute(entity);
         }
      });
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, false, false) {
         @Override
         public boolean canUse() {
            Entity entity = CaveDwellerEntity.this;
            Level world = CaveDwellerEntity.this.level();
            return super.canUse() && NoaiProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = CaveDwellerEntity.this;
            Level world = CaveDwellerEntity.this.level();
            return super.canContinueToUse() && NoaiProcedure.execute(entity);
         }
      });
      this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, ServerPlayer.class, false, false) {
         @Override
         public boolean canUse() {
            Entity entity = CaveDwellerEntity.this;
            Level world = CaveDwellerEntity.this.level();
            return super.canUse() && NoaiProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = CaveDwellerEntity.this;
            Level world = CaveDwellerEntity.this.level();
            return super.canContinueToUse() && NoaiProcedure.execute(entity);
         }
      });
      this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this, Animal.class, false, false) {
         @Override
         public boolean canUse() {
            Entity entity = CaveDwellerEntity.this;
            Level world = CaveDwellerEntity.this.level();
            return super.canUse() && NoaiProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = CaveDwellerEntity.this;
            Level world = CaveDwellerEntity.this.level();
            return super.canContinueToUse() && NoaiProcedure.execute(entity);
         }
      });
      this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0) {
         @Override
         public boolean canUse() {
            Entity entity = CaveDwellerEntity.this;
            Level world = CaveDwellerEntity.this.level();
            return super.canUse() && NoaiProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = CaveDwellerEntity.this;
            Level world = CaveDwellerEntity.this.level();
            return super.canContinueToUse() && NoaiProcedure.execute(entity);
         }
      });
      this.targetSelector.addGoal(6, new HurtByTargetGoal(this) {
         @Override
         public boolean canUse() {
            Entity entity = CaveDwellerEntity.this;
            Level world = CaveDwellerEntity.this.level();
            return super.canUse() && NoaiProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = CaveDwellerEntity.this;
            Level world = CaveDwellerEntity.this.level();
            return super.canContinueToUse() && NoaiProcedure.execute(entity);
         }
      });
      this.goalSelector.addGoal(7, new RandomLookAroundGoal(this) {
         @Override
         public boolean canUse() {
            Entity entity = CaveDwellerEntity.this;
            Level world = CaveDwellerEntity.this.level();
            return super.canUse() && NoaiProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = CaveDwellerEntity.this;
            Level world = CaveDwellerEntity.this.level();
            return super.canContinueToUse() && NoaiProcedure.execute(entity);
         }
      });
      this.goalSelector.addGoal(8, new FloatGoal(this) {
         @Override
         public boolean canUse() {
            Entity entity = CaveDwellerEntity.this;
            Level world = CaveDwellerEntity.this.level();
            return super.canUse() && NoaiProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = CaveDwellerEntity.this;
            Level world = CaveDwellerEntity.this.level();
            return super.canContinueToUse() && NoaiProcedure.execute(entity);
         }
      });
   }

   @Override
   public MobType getMobType() {
      return MobType.UNDEFINED;
   }

   @Override
   protected float getEquipmentDropChance(EquipmentSlot slot) {
      return 0.0F;
   }

   @Override
   public boolean removeWhenFarAway(double distanceToClosestPlayer) {
      return false;
   }

   @Override
   public SoundEvent getHurtSound(DamageSource ds) {
      return dev.deepnight.SafeSounds.resolve("entity.generic.hurt");
   }

   @Override
   public SoundEvent getDeathSound() {
      return dev.deepnight.SafeSounds.resolve("simple_mobs:cavedwellerdeath");
   }

   @Override
   public void baseTick() {
      super.baseTick();
      if(dev.deepnight.source.SourceStun.stunned(this))return;
      ProcavedProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      ProwerehurtProcedure.execute(this.level(), this);
      return super.hurt(source, amount);
   }

   @Override
   protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHitIn) {
      super.dropCustomDeathLoot(source, looting, recentlyHitIn);
      this.spawnAtLocation(new ItemStack(SimpleMobsModItems.DWELLER_ARMOR_FRAGMENT.get()));
   }

   public static void init() {
   }

   public static Builder createAttributes() {
      Builder builder = Mob.createMobAttributes();
      builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
      builder = builder.add(Attributes.MAX_HEALTH, 200.0);
      builder = builder.add(Attributes.ARMOR, 3.0);
      builder = builder.add(Attributes.ATTACK_DAMAGE, 0.0);
      builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
      return builder.add(Attributes.FOLLOW_RANGE, 32.0);
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
      return 70;
   }

   @Override
   public boolean fireImmune() {
      return true;
   }

   @Override
   public void die(DamageSource source) {
      super.die(source);
      ProcaveddeathProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ());
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

   private PlayState predicate(AnimationState animationEvent) {
      double d1 = this.getX() - this.xOld;
      double d0 = this.getZ() - this.zOld;
      float xvelocity = (float)Math.sqrt(d1 * d1 + d0 * d0);
      float zvelocity = (float)Math.sqrt(d1 * d1 + d0 * d0);
      float velocity = (float)Math.sqrt(d1 * d1 + d0 * d0);
      if (!this.isAlive()) {
         animationEvent.getController().setTransitionLength(0);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.cavedweller.death",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.LEATHER_HELMET) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.cavedweller.roar",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.GOLD_NUGGET) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.cavedweller.attack1",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.COAL) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.cavedweller.attack1_alt",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.CHARCOAL) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.cavedweller.attack2",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.LILAC) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.cavedweller.walk",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.IRON_INGOT) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.cavedweller.attack3",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.IRON_NUGGET) {
         animationEvent.getController().setTransitionLength(0);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.cavedweller.attack4",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.GOLD_INGOT) {
         animationEvent.getController().setTransitionLength(0);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.cavedweller.attack4tran",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.PAPER) {
         animationEvent.getController().setTransitionLength(0);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.cavedweller.stun",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (animationEvent.isMoving()) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.cavedweller.walk",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.cavedweller.idle",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      }

      return PlayState.CONTINUE;
   }

   public void registerControllers(ControllerRegistrar data) {
      data.add(new AnimationController<>(this, "controller", 0, this::predicate));
   }

   public AnimatableInstanceCache getAnimatableInstanceCache() {
      return this.factory;
   }
}
