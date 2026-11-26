package dev.deepnight.source.dawn.entity;

import java.util.Random;
import javax.annotation.Nullable;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.procedures.NoaiProcedure;
import dev.deepnight.source.dawn.procedures.Prokorok5Procedure;
import dev.deepnight.source.dawn.procedures.ProkorokhurtProcedure;
import dev.deepnight.source.dawn.procedures.Prokorokintereact3Procedure;
import dev.deepnight.source.dawn.procedures.ProkorokspawnProcedure;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
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

public class Korok5Entity extends Monster implements GeoEntity {
   private final AnimatableInstanceCache factory = software.bernie.geckolib.util.GeckoLibUtil.createInstanceCache(this);

   public Korok5Entity(SpawnEntity packet, Level world) {
      this(SimpleMobsModEntities.KOROK_5.get(), world);
   }

   public Korok5Entity(EntityType<Korok5Entity> type, Level world) {
      super(type, world);
      this.xpReward = 0;
      this.setNoAi(false);
      this.setPersistenceRequired();
   }

   @Override
   public Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }

   @Override
   protected void registerGoals() {
      super.registerGoals();
      this.goalSelector.addGoal(1, new RandomStrollGoal(this, 1.0, 20) {
         @Override
         protected Vec3 getPosition() {
            net.minecraft.util.RandomSource random = Korok5Entity.this.getRandom();
            double dir_x = Korok5Entity.this.getX() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
            double dir_y = Korok5Entity.this.getY() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
            double dir_z = Korok5Entity.this.getZ() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
            return new Vec3(dir_x, dir_y, dir_z);
         }

         @Override
         public boolean canUse() {
            double x = Korok5Entity.this.getX();
            double y = Korok5Entity.this.getY();
            double z = Korok5Entity.this.getZ();
            Entity entity = Korok5Entity.this;
            Level world = Korok5Entity.this.level();
            return super.canUse() && NoaiProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            double x = Korok5Entity.this.getX();
            double y = Korok5Entity.this.getY();
            double z = Korok5Entity.this.getZ();
            Entity entity = Korok5Entity.this;
            Level world = Korok5Entity.this.level();
            return super.canContinueToUse() && NoaiProcedure.execute(entity);
         }
      });
      this.goalSelector.addGoal(2, new FloatGoal(this));
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
   public boolean hurt(DamageSource source, float amount) {
      ProkorokhurtProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
      return super.hurt(source, amount);
   }

   @Override
   public InteractionResult mobInteract(Player sourceentity, InteractionHand hand) {
      ItemStack itemstack = sourceentity.getItemInHand(hand);
      InteractionResult retval = InteractionResult.sidedSuccess(this.level().isClientSide());
      super.mobInteract(sourceentity, hand);
      double x = this.getX();
      double y = this.getY();
      double z = this.getZ();
      Entity entity = this;
      Level world = this.level();
      Prokorokintereact3Procedure.execute(world, entity, sourceentity);
      return retval;
   }

   @Override
   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag
   ) {
      SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
      this.getPersistentData().putBoolean("noai", true);
      ProkorokspawnProcedure.execute(this.level(), this);
      return retval;
   }

   @Override
   public void baseTick() {
      super.baseTick();
      if(dev.deepnight.source.SourceStun.stunned(this))return;
      Prokorok5Procedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
   }

   public static void init() {
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

   public static Builder createAttributes() {
      Builder builder = Mob.createMobAttributes();
      builder = builder.add(Attributes.MOVEMENT_SPEED, 0.33);
      builder = builder.add(Attributes.MAX_HEALTH, 100.0);
      builder = builder.add(Attributes.ARMOR, 0.0);
      builder = builder.add(Attributes.ATTACK_DAMAGE, 3.0);
      builder = builder.add(Attributes.FOLLOW_RANGE, 16.0);
      builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
      return builder.add(Attributes.FLYING_SPEED, 3.0);
   }

   private PlayState predicate(AnimationState animationEvent) {
      if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.LEATHER_HELMET) {
         animationEvent.getController().setTransitionLength(0);
         animationEvent.getController()
            .setAnimation(RawAnimation.begin().thenPlay("animation.korok.trigger").thenLoop("animation.korok.fly"));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.COAL) {
         animationEvent.getController().setTransitionLength(0);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.korok.allow",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.IRON_INGOT) {
         animationEvent.getController().setTransitionLength(3);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.korok.deny",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.CHARCOAL) {
         animationEvent.getController().setTransitionLength(3);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.korok.disappear",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.GOLD_INGOT) {
         animationEvent.getController().setTransitionLength(0);
         animationEvent.getController()
            .setAnimation(RawAnimation.begin().thenPlay("animation.korok.fall").thenLoop("animation.korok.idle"));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.WHITE_WOOL) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().thenLoop("animation.korok.dance1"));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.BLACK_WOOL) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().thenLoop("animation.korok.dance2"));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.RED_WOOL) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().thenLoop("animation.korok.dance3"));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.BLUE_WOOL) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().thenLoop("animation.korok.dance4"));
      } else if (this.getItemBySlot(EquipmentSlot.CHEST).getItem() == Items.LEATHER_CHESTPLATE) {
         if (this.onGround()) {
            if (animationEvent.isMoving()) {
               animationEvent.getController().setTransitionLength(3);
               animationEvent.getController().setAnimation(RawAnimation.begin().thenLoop("animation.korok.run"));
            } else {
               animationEvent.getController().setTransitionLength(3);
               animationEvent.getController().setAnimation(RawAnimation.begin().thenLoop("animation.korok.idle"));
            }
         }
      } else {
         animationEvent.getController().setTransitionLength(0);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.korok.dormant",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
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
