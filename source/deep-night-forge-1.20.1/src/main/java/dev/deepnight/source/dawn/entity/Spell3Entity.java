package dev.deepnight.source.dawn.entity;

import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.procedures.ProSpell3Procedure;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
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
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;

public class Spell3Entity extends Monster implements GeoEntity {
   private final AnimatableInstanceCache factory = software.bernie.geckolib.util.GeckoLibUtil.createInstanceCache(this);

   public Spell3Entity(SpawnEntity packet, Level world) {
      this(SimpleMobsModEntities.SPELL_3.get(), world);
   }

   public Spell3Entity(EntityType<Spell3Entity> type, Level world) {
      super(type, world);
      this.xpReward = 0;
      this.noCulling = true;
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
   public SoundEvent getHurtSound(DamageSource ds) {
      return dev.deepnight.SafeSounds.resolve("");
   }

   @Override
   public SoundEvent getDeathSound() {
      return dev.deepnight.SafeSounds.resolve("");
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
   public void baseTick() {
      super.baseTick();
      if(dev.deepnight.source.SourceStun.stunned(this))return;
      ProSpell3Procedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
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
      builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
      builder = builder.add(Attributes.MAX_HEALTH, 100.0);
      builder = builder.add(Attributes.ARMOR, 0.0);
      return builder.add(Attributes.ATTACK_DAMAGE, 3.0);
   }

   private PlayState predicate(AnimationState animationEvent) {
      animationEvent.getController().setAnimation(RawAnimation.begin().then("animation.spell3.spin",software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT));
      return PlayState.CONTINUE;
   }

   public void registerControllers(ControllerRegistrar data) {
      data.add(new AnimationController<>(this, "controller", 0, this::predicate));
   }

   public AnimatableInstanceCache getAnimatableInstanceCache() {
      return this.factory;
   }
}
