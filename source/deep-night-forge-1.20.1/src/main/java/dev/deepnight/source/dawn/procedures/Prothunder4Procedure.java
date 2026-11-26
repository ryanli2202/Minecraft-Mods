package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.Thundereffect4Entity;
import dev.deepnight.source.dawn.init.SimpleMobsModParticleTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import dev.deepnight.source.SourceDamage;
import dev.deepnight.source.SourceDamage.StunType;

public class Prothunder4Procedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (world.isClientSide()) {
            int particleCount = 25;
            int tick = entity.tickCount;
            if (tick > 4 && tick < 25) {
               for (int i = 1; i <= particleCount; i++) {
                  double yaw = i * 360.0F / particleCount;
                  double speed = 0.1;
                  double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                  double zSpeed = speed * Math.sin(Math.toRadians(yaw));
                  world.addParticle(
                     (SimpleParticleType)SimpleMobsModParticleTypes.EXPLOSION.get(),
                     x,
                     y + (float)Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.0, 1.0),
                     z,
                     xSpeed,
                     0.01,
                     zSpeed
                  );
               }

               for (int i = 1; i <= particleCount; i++) {
                  double yaw = i * 360.0F / particleCount;
                  double speed = 0.2;
                  double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                  double zSpeed = speed * Math.sin(Math.toRadians(yaw));
                  world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y + (float)Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.0, 2.0), z, xSpeed, 0.02, zSpeed);
               }

               for (int i = 1; i <= particleCount; i++) {
                  double yaw = i * 360.0F / particleCount;
                  double speed = 1.0;
                  double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                  double zSpeed = speed * Math.sin(Math.toRadians(yaw));
                  if (Math.random() >= 0.97) {
                     world.addParticle(
                        (SimpleParticleType)SimpleMobsModParticleTypes.THUNDER_FLASH_1.get(),
                        x + (float)Mth.nextDouble(net.minecraft.util.RandomSource.create(), -4.0, 4.0),
                        y + (float)Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.0, 3.0),
                        z + (float)Mth.nextDouble(net.minecraft.util.RandomSource.create(), -4.0, 4.0),
                        xSpeed,
                        0.0,
                        zSpeed
                     );
                  }
               }
            }
         }

         if (!world.isClientSide()) {
            if (entity.getPersistentData().getDouble("tick") == 0.0) {
               entity.getPersistentData().putDouble("tick", 41.0);
            } else {
               entity.getPersistentData().putDouble("tick", entity.getPersistentData().getDouble("tick") - 1.0);
            }

            if (entity.getPersistentData().getDouble("tick") == 40.0) {
               Vec3 _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (!(entityiterator instanceof Thundereffect4Entity) && !entityiterator.getPersistentData().getBoolean("dragon_lord")) {
                     SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                        .setInitialPosition(entity.position())
                        .setStunType(StunType.SHORT)
                        .setImpact(2.0F)
                        .setArmorNegation(35.0F);
                     entityiterator.hurt((DamageSource)dmgSrc, 30.0F);
                  }
               }

               if (world instanceof ServerLevel _level) {
                  _level.sendParticles(ParticleTypes.FLASH, x, y + 1.5, z, 30, 1.0, 3.0, 1.0, 0.5);
               }

               if (world instanceof ServerLevel _level) {
                  _level.sendParticles(ParticleTypes.EXPLOSION_EMITTER, x, y + 1.5, z, 5, 0.3, 0.3, 0.3, 0.5);
               }
            } else if (entity.getPersistentData().getDouble("tick") == 1.0 && !entity.level().isClientSide()) {
               entity.discard();
            }
         }
      }
   }
}
