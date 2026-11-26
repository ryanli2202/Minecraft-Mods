package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.Thundereffect2Entity;
import dev.deepnight.source.dawn.init.SimpleMobsModParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import dev.deepnight.source.SourceDamage;
import dev.deepnight.source.SourceDamage.StunType;

public class Prothunder2Procedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         double i = 0.0;
         double x1 = 0.0;
         double y1 = 0.0;
         double z1 = 0.0;
         int particleCount = 15;
         int tick = entity.tickCount;
         if (tick % 10 == 1) {
            for (double var26 = 1.0; var26 <= particleCount; var26++) {
               double yaw = var26 * 360.0 / particleCount;
               double speed = 0.65;
               double xSpeed = speed * Math.cos(Math.toRadians(yaw));
               double zSpeed = speed * Math.sin(Math.toRadians(yaw));
               world.addParticle(
                  (SimpleParticleType)SimpleMobsModParticleTypes.EXPLOSION.get(), x, y + (float)Mth.nextDouble(net.minecraft.util.RandomSource.create(), 1.0, 3.0), z, xSpeed, 0.0, zSpeed
               );
            }

            for (double var27 = 1.0; var27 <= particleCount; var27++) {
               double yaw = var27 * 360.0 / particleCount;
               double speed = 0.3;
               double xSpeed = speed * Math.cos(Math.toRadians(yaw));
               double zSpeed = speed * Math.sin(Math.toRadians(yaw));
               world.addParticle(
                  (SimpleParticleType)SimpleMobsModParticleTypes.EXPLOSION.get(), x, y + (float)Mth.nextDouble(net.minecraft.util.RandomSource.create(), 1.0, 3.0), z, xSpeed, 0.0, zSpeed
               );
            }
         }

         if (!world.isClientSide()) {
            if (world instanceof ServerLevel _level) {
               _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.DRAGON_THUNDER_LIT.get(), x, y, z, 1, 6.0, 6.0, 6.0, 0.5);
            }

            if (entity.getPersistentData().getDouble("tick") == 0.0) {
               entity.getPersistentData().putDouble("tick", 65.0);
               if (world instanceof Level _level) {
                  if (!_level.isClientSide()) {
                     _level.playSound(
                        null,
                        BlockPos.containing(x, y, z),
                        dev.deepnight.SafeSounds.resolve("simple_mobs:thunder_effect2"),
                        SoundSource.NEUTRAL,
                        3.0F,
                        1.0F
                     );
                  } else {
                     _level.playLocalSound(
                        x,
                        y,
                        z,
                        dev.deepnight.SafeSounds.resolve("simple_mobs:thunder_effect2"),
                        SoundSource.NEUTRAL,
                        3.0F,
                        1.0F,
                        false
                     );
                  }
               }
            } else {
               entity.getPersistentData().putDouble("tick", entity.getPersistentData().getDouble("tick") - 1.0);
            }

            if (entity.getPersistentData().getDouble("tick") == 14.0) {
               Vec3 _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(13.5), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (!(entityiterator instanceof Thundereffect2Entity) && !entityiterator.getPersistentData().getBoolean("dragon_lord")) {
                     SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                        .setInitialPosition(entity.position())
                        .setStunType(StunType.KNOCKDOWN)
                        .setImpact(2.0F)
                        .setArmorNegation(100.0F);
                     entityiterator.hurt((DamageSource)dmgSrc, 70.0F);
                  }
               }

               for (double var28 = 0.0; var28 <= 150.0; var28++) {
                  x1 = entity.getX() + 10.0 * Math.cos(var28 * Math.PI / 10.0);
                  y1 = entity.getY() + 1.5;
                  z1 = entity.getZ() + 10.0 * Math.sin(var28 * Math.PI / 10.0);
                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.EXPLOSION.get(), x1, y1, z1, 1, 0.4, 0.4, 0.4, 0.1);
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.CLOUD, x1, y1, z1, 1, 0.1, 0.1, 0.1, 0.1);
                  }
               }

               if (world instanceof Level _level) {
                  if (!_level.isClientSide()) {
                     _level.playSound(
                        null,
                        BlockPos.containing(x, y, z),
                        dev.deepnight.SafeSounds.resolve("entity.lightning_bolt.thunder"),
                        SoundSource.NEUTRAL,
                        3.0F,
                        0.0F
                     );
                  } else {
                     _level.playLocalSound(
                        x,
                        y,
                        z,
                        dev.deepnight.SafeSounds.resolve("entity.lightning_bolt.thunder"),
                        SoundSource.NEUTRAL,
                        3.0F,
                        0.0F,
                        false
                     );
                  }
               }

               if (world instanceof Level _level) {
                  if (!_level.isClientSide()) {
                     _level.playSound(
                        null,
                        BlockPos.containing(x, y, z),
                        dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                        SoundSource.NEUTRAL,
                        3.0F,
                        1.0F
                     );
                  } else {
                     _level.playLocalSound(
                        x, y, z, dev.deepnight.SafeSounds.resolve("entity.generic.explode"), SoundSource.NEUTRAL, 3.0F, 1.0F, false
                     );
                  }
               }

               if (world instanceof ServerLevel _level) {
                  _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.DRAGON_THUNDER_LIT.get(), x, y, z, 70, 5.0, 5.0, 5.0, 1.0);
               }

               if (world instanceof ServerLevel _level) {
                  _level.sendParticles(ParticleTypes.FLASH, x, y, z, 2, 0.5, 0.5, 0.5, 0.5);
               }

               if (world instanceof ServerLevel _level) {
                  _level.sendParticles(ParticleTypes.EXPLOSION, x, y, z, 10, 5.0, 5.0, 5.0, 0.5);
               }

               if (world instanceof ServerLevel _level) {
                  _level.sendParticles(ParticleTypes.LARGE_SMOKE, x, y, z, 50, 6.0, 6.0, 6.0, 0.5);
               }
            } else if (entity.getPersistentData().getDouble("tick") == 1.0 && !entity.level().isClientSide()) {
               entity.discard();
            }
         }
      }
   }
}
