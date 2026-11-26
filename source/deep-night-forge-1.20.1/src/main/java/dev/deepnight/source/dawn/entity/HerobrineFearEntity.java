package dev.deepnight.source.dawn.entity;

import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.procedures.NoaiProcedure;
import dev.deepnight.source.dawn.procedures.ProhfProcedure;
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
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
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

public class HerobrineFearEntity extends Monster implements GeoEntity {
   private final AnimatableInstanceCache factory = software.bernie.geckolib.util.GeckoLibUtil.createInstanceCache(this);

   public HerobrineFearEntity(SpawnEntity packet, Level world) {
      this(SimpleMobsModEntities.HEROBRINE_FEAR.get(), world);
   }

   public HerobrineFearEntity(EntityType<HerobrineFearEntity> type, Level world) {
      super(type, world);
      this.xpReward = 0;
      this.noCulling = true;
      this.setNoAi(false);
   }

   @Override
   public Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }

   @Override
   protected void registerGoals() {
      super.registerGoals();
      this.goalSelector.addGoal(1, new NineTailsEntity.FollowTargetGoal(this, 0.0, 30.0F) {
         @Override
         public boolean canUse() {
            Entity entity = HerobrineFearEntity.this;
            Level world = HerobrineFearEntity.this.level();
            return super.canUse() && NoaiProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = HerobrineFearEntity.this;
            Level world = HerobrineFearEntity.this.level();
            return super.canContinueToUse() && NoaiProcedure.execute(entity);
         }
      });
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, false, false) {
         @Override
         public boolean canUse() {
            Entity entity = HerobrineFearEntity.this;
            Level world = HerobrineFearEntity.this.level();
            return super.canUse() && NoaiProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = HerobrineFearEntity.this;
            Level world = HerobrineFearEntity.this.level();
            return super.canContinueToUse() && NoaiProcedure.execute(entity);
         }
      });
      this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, ServerPlayer.class, false, false) {
         @Override
         public boolean canUse() {
            Entity entity = HerobrineFearEntity.this;
            Level world = HerobrineFearEntity.this.level();
            return super.canUse() && NoaiProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            Entity entity = HerobrineFearEntity.this;
            Level world = HerobrineFearEntity.this.level();
            return super.canContinueToUse() && NoaiProcedure.execute(entity);
         }
      });
   }

   @Override
   public MobType getMobType() {
      return MobType.UNDEFINED;
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
   protected void doPush(Entity entityIn) {
   }

   @Override
   protected void pushEntities() {
   }

   public static void init() {
   }

   @Override
   public void baseTick() {
      super.baseTick();
      if(dev.deepnight.source.SourceStun.stunned(this))return;
      ProhfProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
   }

   public static Builder createAttributes() {
      Builder builder = Mob.createMobAttributes();
      builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
      builder = builder.add(Attributes.MAX_HEALTH, 10.0);
      builder = builder.add(Attributes.ARMOR, 0.0);
      builder = builder.add(Attributes.ATTACK_DAMAGE, 3.0);
      builder = builder.add(Attributes.FOLLOW_RANGE, 64.0);
      return builder.add(Attributes.KNOCKBACK_RESISTANCE, 5.0);
   }

   @Override
   public boolean skipAttackInteraction(Entity entity) {
      return true;
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      return source.is(net.minecraft.world.damagesource.DamageTypes.FELL_OUT_OF_WORLD) ? super.hurt(source, amount) : false;
   }

   private PlayState predicate(AnimationState animationEvent) {
      if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.LEATHER_HELMET) {
         animationEvent.getController().setTransitionLength(0);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.hf.1",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.GOLD_NUGGET) {
         animationEvent.getController().setTransitionLength(0);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.hf.2",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
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
