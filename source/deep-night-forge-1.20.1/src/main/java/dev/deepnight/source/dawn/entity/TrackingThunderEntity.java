package dev.deepnight.source.dawn.entity;

import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.init.SimpleMobsModParticleTypes;
import dev.deepnight.source.dawn.procedures.ProtthunderProcedure;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages.SpawnEntity;
import net.minecraftforge.registries.ForgeRegistries;

public class TrackingThunderEntity extends Monster {
   public TrackingThunderEntity(SpawnEntity packet, Level world) {
      this(SimpleMobsModEntities.TRACKING_THUNDER.get(), world);
   }

   public TrackingThunderEntity(EntityType<TrackingThunderEntity> type, Level world) {
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
      this.goalSelector.addGoal(1, new NineTailsEntity.FollowTargetGoal(this, 1.0, 30.0F));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false, false));
      this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, ServerPlayer.class, false, false));
      this.goalSelector.addGoal(4, new FloatGoal(this));
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
   public void baseTick() {
      super.baseTick();
      if(dev.deepnight.source.SourceStun.stunned(this))return;
      ProtthunderProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
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
   public void aiStep() {
      super.aiStep();
      double x = this.getX();
      double y = this.getY();
      double z = this.getZ();
      Level world = this.level();

      for (int l = 0; l < 1; l++) {
         double x0 = x + this.random.nextFloat();
         double y0 = y + 0.5 + this.random.nextFloat();
         double z0 = z + this.random.nextFloat();
         double dx = (this.random.nextFloat() - 0.5) * 0.5;
         double dy = (this.random.nextFloat() - 0.5) * 0.5;
         double dz = (this.random.nextFloat() - 0.5) * 0.5;
         world.addParticle((SimpleParticleType)SimpleMobsModParticleTypes.DRAGON_THUNDER.get(), x0, y0, z0, dx, dy, dz);
      }
   }

   public static void init() {
   }

   public static Builder createAttributes() {
      Builder builder = Mob.createMobAttributes();
      builder = builder.add(Attributes.MOVEMENT_SPEED, 0.32);
      builder = builder.add(Attributes.MAX_HEALTH, 10.0);
      builder = builder.add(Attributes.ARMOR, 0.0);
      builder = builder.add(Attributes.ATTACK_DAMAGE, 3.0);
      return builder.add(Attributes.FOLLOW_RANGE, 16.0);
   }
}
