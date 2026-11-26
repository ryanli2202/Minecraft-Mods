package dev.deepnight.source.mobosses.entity.custom;

import net.minecraft.nbt.CompoundTag;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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

public class DemonKingLightningEntity extends Entity implements GeoEntity {
   private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
   private int lifeTime = 57;

   public DemonKingLightningEntity(EntityType<?> type, Level level) {
      super(type, level);
   }

   public void tick() {
      super.tick();
      if (!this.level().isClientSide) {
         if (this.level() instanceof net.minecraft.server.level.ServerLevel server) {
            if (this.lifeTime>50) {
               for (int i=0;i<12;i++) {
                  double angle=i*Math.PI/6;
                  server.sendParticles(net.minecraft.core.particles.ParticleTypes.ELECTRIC_SPARK,getX()+Math.cos(angle)*2,getY()+0.15,getZ()+Math.sin(angle)*2,1,0,0.05,0,0.01);
               }
            } else if (this.lifeTime==50) {
               server.sendParticles(net.minecraft.core.particles.ParticleTypes.FLASH,getX(),getY()+1,getZ(),1,0,0,0,0);
               server.sendParticles(net.minecraft.core.particles.ParticleTypes.ELECTRIC_SPARK,getX(),getY()+1,getZ(),40,1,1.5,1,0.15);
               server.playSound(null,blockPosition(),SoundEvents.LIGHTNING_BOLT_IMPACT,SoundSource.HOSTILE,0.8F,0.8F);
            }
         }
         if (this.lifeTime == 50) {
            this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(2.0)).forEach(target -> {
               if (!(target instanceof DemonKingEntity) && !(target instanceof DemonKingMinionEntity)) {
                  target.hurt(new net.minecraft.world.damagesource.DamageSource(this.damageSources().magic().typeHolder(), this, this), 12.0F);
                  target.setRemainingFireTicks(3);
               }
            });
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.HOSTILE, 1.0F, 1.0F);
         }

         if (this.lifeTime-- <= 0) {
            this.discard();
         }
      }
   }

   public void registerControllers(ControllerRegistrar controllers) {
      controllers.add(new AnimationController(this, "lightning_controller", 0, state -> state.setAndContinue(RawAnimation.begin().thenPlay("spawn"))));
   }

   public AnimatableInstanceCache getAnimatableInstanceCache() {
      return this.cache;
   }

   protected void defineSynchedData() {
   }

   protected void readAdditionalSaveData(CompoundTag compound) {
   }

   protected void addAdditionalSaveData(CompoundTag compound) {
   }
}
