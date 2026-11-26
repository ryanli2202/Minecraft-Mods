package dev.deepnight.source.dawn.entity;

import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.procedures.ProlavaballProcedure;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages.SpawnEntity;

public class LavaBallEntity extends Monster {
   public LavaBallEntity(SpawnEntity packet, Level world) {
      this(SimpleMobsModEntities.LAVA_BALL.get(), world);
   }

   public LavaBallEntity(EntityType<LavaBallEntity> type, Level world) {
      super(type, world);
      this.setMaxUpStep(0.6F);
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
   public boolean hurt(DamageSource source, float amount) {
      if (source.getDirectEntity() instanceof AbstractArrow) {
         return false;
      } else if (source.getDirectEntity() instanceof Player) {
         return false;
      } else if (source.getDirectEntity() instanceof ThrownPotion || source.getDirectEntity() instanceof AreaEffectCloud) {
         return false;
      } else if (source.is(net.minecraft.world.damagesource.DamageTypes.FALL)) {
         return false;
      } else if (source.is(net.minecraft.world.damagesource.DamageTypes.CACTUS)) {
         return false;
      } else if (source.is(net.minecraft.world.damagesource.DamageTypes.DROWN)) {
         return false;
      } else if (source.is(net.minecraft.world.damagesource.DamageTypes.LIGHTNING_BOLT)) {
         return false;
      } else if (source.is(net.minecraft.tags.DamageTypeTags.IS_EXPLOSION)) {
         return false;
      } else if (source.getMsgId().equals("trident")) {
         return false;
      } else if (source.is(net.minecraft.world.damagesource.DamageTypes.FALLING_ANVIL)) {
         return false;
      } else if (source.is(net.minecraft.world.damagesource.DamageTypes.DRAGON_BREATH)) {
         return false;
      } else if (source.is(net.minecraft.world.damagesource.DamageTypes.WITHER)) {
         return false;
      } else {
         return source.getMsgId().equals("witherSkull") ? false : super.hurt(source, amount);
      }
   }

   @Override
   public void baseTick() {
      super.baseTick();
      if(dev.deepnight.source.SourceStun.stunned(this))return;
      ProlavaballProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
   }

   public static void init() {
   }

   public static Builder createAttributes() {
      Builder builder = Mob.createMobAttributes();
      builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
      builder = builder.add(Attributes.MAX_HEALTH, 10.0);
      builder = builder.add(Attributes.ARMOR, 0.0);
      builder = builder.add(Attributes.ATTACK_DAMAGE, 3.0);
      return builder.add(Attributes.FOLLOW_RANGE, 16.0);
   }
}
