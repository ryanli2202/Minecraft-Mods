package dev.deepnight.source.dawn.entity;

import java.util.Random;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.init.SimpleMobsModParticleTypes;
import dev.deepnight.source.dawn.procedures.PrometeorProcedure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages.SpawnEntity;
import net.minecraftforge.registries.ForgeRegistries;

public class MeteorAttackEntity extends Monster {
   public MeteorAttackEntity(SpawnEntity packet, Level world) {
      this(SimpleMobsModEntities.METEOR_ATTACK.get(), world);
   }

   public MeteorAttackEntity(EntityType<MeteorAttackEntity> type, Level world) {
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
      PrometeorProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);

      for (int i = 1; i <= 4; i++) {
         this.level()
            .addAlwaysVisibleParticle(
               ParticleTypes.CAMPFIRE_SIGNAL_SMOKE,
               true,
               this.getX() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3),
               this.getY() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3),
               this.getZ() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3),
               0.0,
               0.0,
               0.0
            );
         this.level()
            .addAlwaysVisibleParticle(
               ParticleTypes.CLOUD,
               true,
               this.getX() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3),
               this.getY() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3),
               this.getZ() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3),
               0.0,
               0.0,
               0.0
            );
      }
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
      double x = this.getX();
      double y = this.getY();
      double z = this.getZ();
      Level world = this.level();

      for (int l = 0; l < 4; l++) {
         double x0 = x + this.random.nextFloat();
         double y0 = y + this.random.nextFloat();
         double z0 = z + this.random.nextFloat();
         double dx = (this.random.nextFloat() - 0.5) * 0.5;
         double dy = (this.random.nextFloat() - 0.5) * 0.5;
         double dz = (this.random.nextFloat() - 0.5) * 0.5;
         world.addParticle((SimpleParticleType)SimpleMobsModParticleTypes.BURNT_FLAME.get(), x0, y0, z0, dx, dy, dz);
      }
   }

   public static void init() {
   }

   public static Builder createAttributes() {
      Builder builder = Mob.createMobAttributes();
      builder = builder.add(Attributes.MOVEMENT_SPEED, 0.0);
      builder = builder.add(Attributes.MAX_HEALTH, 100.0);
      builder = builder.add(Attributes.ARMOR, 0.0);
      builder = builder.add(Attributes.ATTACK_DAMAGE, 3.0);
      builder = builder.add(Attributes.FOLLOW_RANGE, 16.0);
      builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
      return builder.add(Attributes.FLYING_SPEED, 0.0);
   }
}
