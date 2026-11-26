package dev.deepnight.source.dawn.entity;

import java.util.Random;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.init.SimpleMobsModParticleTypes;
import dev.deepnight.source.dawn.procedures.ProlightningspearProcedure;
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
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages.SpawnEntity;
import net.minecraftforge.registries.ForgeRegistries;

public class LightningSpearEntity extends Monster {
   public LightningSpearEntity(SpawnEntity packet, Level world) {
      this(SimpleMobsModEntities.LIGHTNING_SPEAR.get(), world);
   }

   public LightningSpearEntity(EntityType<LightningSpearEntity> type, Level world) {
      super(type, world);
      this.xpReward = 0;
      this.setNoAi(false);
      this.noCulling = true;
   }

   @Override
   public Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
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
      this.level()
         .addAlwaysVisibleParticle(
            (SimpleParticleType)SimpleMobsModParticleTypes.THUNDER_FLASH_2.get(),
            true,
            this.getX() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, 5),
            this.getY() + Mth.nextInt(net.minecraft.util.RandomSource.create(), 1, 3),
            this.getZ() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, 5),
            0.0,
            0.0,
            0.0
         );
      ProlightningspearProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
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

   public static Builder createAttributes() {
      Builder builder = Mob.createMobAttributes();
      builder = builder.add(Attributes.MOVEMENT_SPEED, 0.0);
      builder = builder.add(Attributes.MAX_HEALTH, 1000.0);
      builder = builder.add(Attributes.ARMOR, 0.0);
      builder = builder.add(Attributes.ATTACK_DAMAGE, 3.0);
      return builder.add(Attributes.FOLLOW_RANGE, 16.0);
   }
}
