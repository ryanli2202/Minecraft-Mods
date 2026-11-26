package dev.deepnight.source.dawn.entity;

import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.procedures.ProcapedminotaurProcedure;
import dev.deepnight.source.dawn.procedures.ProwerehurtProcedure;
import dev.deepnight.source.dawn.procedures.StepbackProcedure;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.SpawnPlacements.Type;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
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

@EventBusSubscriber
public class CapedMinotaurEntity extends Monster implements GeoEntity {
   private final AnimatableInstanceCache factory = software.bernie.geckolib.util.GeckoLibUtil.createInstanceCache(this);

   public CapedMinotaurEntity(SpawnEntity packet, Level world) {
      this(SimpleMobsModEntities.CAPED_MINOTAUR.get(), world);
   }

   public CapedMinotaurEntity(EntityType<CapedMinotaurEntity> type, Level world) {
      super(type, world);
      this.noCulling = true;
      this.xpReward = 0;
      this.setNoAi(false);
   }

   @Override
   public Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }

   @Override
   protected void registerGoals() {
      super.registerGoals();
      this.goalSelector.addGoal(1, new AvoidEntityGoal<Player>(this, Player.class, 12.0F, 0.8, 1.0) {
         @Override
         public boolean canUse() {
            double x = CapedMinotaurEntity.this.getX();
            double y = CapedMinotaurEntity.this.getY();
            double z = CapedMinotaurEntity.this.getZ();
            Entity entity = CapedMinotaurEntity.this;
            Level world = CapedMinotaurEntity.this.level();
            return super.canUse() && StepbackProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            double x = CapedMinotaurEntity.this.getX();
            double y = CapedMinotaurEntity.this.getY();
            double z = CapedMinotaurEntity.this.getZ();
            Entity entity = CapedMinotaurEntity.this;
            Level world = CapedMinotaurEntity.this.level();
            return super.canContinueToUse() && StepbackProcedure.execute(entity);
         }
      });
      this.goalSelector.addGoal(2, new AvoidEntityGoal<ServerPlayer>(this, ServerPlayer.class, 12.0F, 1.5, 1.5) {
         @Override
         public boolean canUse() {
            double x = CapedMinotaurEntity.this.getX();
            double y = CapedMinotaurEntity.this.getY();
            double z = CapedMinotaurEntity.this.getZ();
            Entity entity = CapedMinotaurEntity.this;
            Level world = CapedMinotaurEntity.this.level();
            return super.canUse() && StepbackProcedure.execute(entity);
         }

         @Override
         public boolean canContinueToUse() {
            double x = CapedMinotaurEntity.this.getX();
            double y = CapedMinotaurEntity.this.getY();
            double z = CapedMinotaurEntity.this.getZ();
            Entity entity = CapedMinotaurEntity.this;
            Level world = CapedMinotaurEntity.this.level();
            return super.canContinueToUse() && StepbackProcedure.execute(entity);
         }
      });
      this.goalSelector.addGoal(3, new NineTailsEntity.FollowTargetGoal(this, 1.0, 30.0F));
      this.targetSelector.addGoal(4, new HurtByTargetGoal(this));
      this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, ServerPlayer.class, false, false));
      this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, Player.class, false, false));
      this.goalSelector.addGoal(7, new RandomStrollGoal(this, 1.0));
      this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
      this.goalSelector.addGoal(9, new FloatGoal(this));
   }

   @Override
   public MobType getMobType() {
      return MobType.UNDEFINED;
   }

   @Override
   public SoundEvent getHurtSound(DamageSource ds) {
      return dev.deepnight.SafeSounds.resolve("entity.cow.hurt");
   }

   @Override
   public SoundEvent getDeathSound() {
      return dev.deepnight.SafeSounds.resolve("entity.cow.death");
   }

   @Override
   public void baseTick() {
      super.baseTick();
      if(dev.deepnight.source.SourceStun.stunned(this))return;
      ProcapedminotaurProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      ProwerehurtProcedure.execute(this.level(), this);
      return super.hurt(source, amount);
   }

   public static void init() {
      SpawnPlacements.register(
         SimpleMobsModEntities.CAPED_MINOTAUR.get(),
         Type.ON_GROUND,
         Types.MOTION_BLOCKING_NO_LEAVES,
         (entityType, world, reason, pos, random) -> world.getDifficulty() != Difficulty.PEACEFUL
            && Monster.isDarkEnoughToSpawn(world, pos, random)
            && Mob.checkMobSpawnRules(entityType, world, reason, pos, random)
      );
   }

   public static Builder createAttributes() {
      Builder builder = Mob.createMobAttributes();
      builder = builder.add(Attributes.MOVEMENT_SPEED, 0.23);
      builder = builder.add(Attributes.MAX_HEALTH, 30.0);
      builder = builder.add(Attributes.ARMOR, 2.0);
      builder = builder.add(Attributes.ATTACK_DAMAGE, 3.0);
      builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
      return builder.add(Attributes.FOLLOW_RANGE, 30.0);
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
      return 30;
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
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.minotaur.death",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.LEATHER_HELMET) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.minotaur.attack1",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.IRON_INGOT) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.minotaur.attack2",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.COAL) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.minotaur.attack3",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.POPPY) {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.minotaur.idle2",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.KELP) {
         animationEvent.getController().setTransitionLength(0);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.minotaur.stun",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      } else if (animationEvent.isMoving()) {
         if (this.onGround()) {
            if (velocity > 0.22F) {
               animationEvent.getController().setTransitionLength(5);
               animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.minotaur.run",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
            } else {
               animationEvent.getController().setTransitionLength(5);
               animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.minotaur.walk",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
            }
         }
      } else {
         animationEvent.getController().setTransitionLength(5);
         animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.minotaur.idle",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
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
