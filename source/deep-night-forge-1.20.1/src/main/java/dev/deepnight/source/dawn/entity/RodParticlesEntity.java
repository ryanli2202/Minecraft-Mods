package dev.deepnight.source.dawn.entity;

import java.util.Random;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.init.SimpleMobsModParticleTypes;
import dev.deepnight.source.dawn.procedures.ProrodProcedure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages.SpawnEntity;
import net.minecraftforge.registries.ForgeRegistries;

public class RodParticlesEntity extends PathfinderMob {
   public RodParticlesEntity(SpawnEntity packet, Level world) {
      this(SimpleMobsModEntities.ROD_PARTICLES.get(), world);
   }

   public RodParticlesEntity(EntityType<RodParticlesEntity> type, Level world) {
      super(type, world);
      this.xpReward = 0;
      this.setNoAi(false);
      this.setPersistenceRequired();
      this.moveControl = new FlyingMoveControl(this, 10, true);
   }

   @Override
   public Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }

   @Override
   protected PathNavigation createNavigation(Level world) {
      return new FlyingPathNavigation(this, world);
   }

   @Override
   protected void registerGoals() {
      super.registerGoals();
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
   public boolean causeFallDamage(float l, float d, DamageSource source) {
      return false;
   }

   @Override
   public void baseTick() {
      super.baseTick();
      if(dev.deepnight.source.SourceStun.stunned(this))return;
      this.level()
         .addAlwaysVisibleParticle(
            (SimpleParticleType)SimpleMobsModParticleTypes.THUNDER_FLASH_1.get(),
            true,
            this.getX() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -15, 15),
            this.getY() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -1, 1),
            this.getZ() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -15, 15),
            0.0,
            0.0,
            0.0
         );
      this.level()
         .addAlwaysVisibleParticle(
            (SimpleParticleType)SimpleMobsModParticleTypes.THUNDER_FLASH_2.get(),
            true,
            this.getX() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -15, 15),
            this.getY() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -1, 1),
            this.getZ() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -15, 15),
            0.0,
            0.0,
            0.0
         );
      this.level()
         .addAlwaysVisibleParticle(
            (SimpleParticleType)SimpleMobsModParticleTypes.THUNDER_FLASH_2.get(),
            true,
            this.getX() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -15, 15),
            this.getY() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -1, 1),
            this.getZ() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -15, 15),
            0.0,
            0.0,
            0.0
         );
      this.level()
         .addAlwaysVisibleParticle(
            (SimpleParticleType)SimpleMobsModParticleTypes.EXPLOSION.get(),
            true,
            this.getX() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -15, 15),
            this.getY() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 0),
            this.getZ() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -15, 15),
            0.0,
            0.0,
            0.0
         );
      this.level()
         .addAlwaysVisibleParticle(
            (SimpleParticleType)SimpleMobsModParticleTypes.EXPLOSION.get(),
            true,
            this.getX() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -15, 15),
            this.getY() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 0),
            this.getZ() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -15, 15),
            0.0,
            0.0,
            0.0
         );
      ProrodProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
   }

   @Override
   public boolean isPushable() {
      return false;
   }

   @Override
   public boolean skipAttackInteraction(Entity entity) {
      return true;
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      return source.is(net.minecraft.world.damagesource.DamageTypes.FELL_OUT_OF_WORLD) ? super.hurt(source, amount) : false;
   }

   @Override
   protected void doPush(Entity entityIn) {
   }

   @Override
   protected void pushEntities() {
   }

   @Override
   protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
   }

   @Override
   public void setNoGravity(boolean ignored) {
      super.setNoGravity(true);
   }

   @Override
   public void aiStep() {
      super.aiStep();
      this.setNoGravity(true);
   }

   public static void init() {
   }

   public static Builder createAttributes() {
      Builder builder = Mob.createMobAttributes();
      builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
      builder = builder.add(Attributes.MAX_HEALTH, 100.0);
      builder = builder.add(Attributes.ARMOR, 0.0);
      builder = builder.add(Attributes.ATTACK_DAMAGE, 3.0);
      builder = builder.add(Attributes.FOLLOW_RANGE, 16.0);
      return builder.add(Attributes.FLYING_SPEED, 0.3);
   }
}
