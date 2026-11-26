package dev.deepnight.source.mobosses.entity.custom;

import net.minecraft.nbt.CompoundTag;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ForestGuardianAuraEntity extends Entity implements GeoEntity {
   private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
   private int lifeTicks = 0;

   public ForestGuardianAuraEntity(EntityType<?> type, Level level) {
      super(type, level);
   }

   public void tick() {
      super.tick();
      this.lifeTicks++;
      if (this.lifeTicks == 10 && !this.level().isClientSide) {
         for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4.0))) {
            if (!(entity instanceof ForestGuardianEntity)) {
               entity.hurt(new net.minecraft.world.damagesource.DamageSource(this.damageSources().magic().typeHolder(), this, this), 8.0F);
               entity.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 1));
            }
         }
      }

      if (this.lifeTicks >= 40) {
         this.discard();
      }
   }

   protected void defineSynchedData() {
   }

   protected void readAdditionalSaveData(CompoundTag compound) {
   }

   protected void addAdditionalSaveData(CompoundTag compound) {
   }

   public void registerControllers(ControllerRegistrar controllers) {
      controllers.add(new AnimationController(this, "controller", 0, state -> state.setAndContinue(RawAnimation.begin().thenPlay("spawn"))));
   }

   public AnimatableInstanceCache getAnimatableInstanceCache() {
      return this.cache;
   }
}
