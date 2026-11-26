package dev.deepnight.source.dawn.entity;

import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.procedures.ProghostprojProcedure;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
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
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;

public class GhostprojectileEntity extends Monster implements GeoEntity {
   private final AnimatableInstanceCache factory = software.bernie.geckolib.util.GeckoLibUtil.createInstanceCache(this);

   public GhostprojectileEntity(SpawnEntity packet, Level world) {
      this(SimpleMobsModEntities.GHOSTPROJECTILE.get(), world);
   }

   public GhostprojectileEntity(EntityType<GhostprojectileEntity> type, Level world) {
      super(type, world);
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
      ProghostprojProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
   }

   public static void init() {
   }

   public static Builder createAttributes() {
      Builder builder = Mob.createMobAttributes();
      builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
      builder = builder.add(Attributes.MAX_HEALTH, 200.0);
      builder = builder.add(Attributes.ARMOR, 0.0);
      return builder.add(Attributes.ATTACK_DAMAGE, 3.0);
   }

   private PlayState predicate(AnimationState animationEvent) {
      animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.ghostproj.bite",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      return PlayState.CONTINUE;
   }

   public void registerControllers(ControllerRegistrar data) {
      data.add(new AnimationController<>(this, "controller", 0, this::predicate));
   }

   public AnimatableInstanceCache getAnimatableInstanceCache() {
      return this.factory;
   }
}
