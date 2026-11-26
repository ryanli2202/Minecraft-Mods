package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class ProswordProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         world.addParticle(
            ParticleTypes.SWEEP_ATTACK,
            x + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
            y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
            z + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
            0.0,
            -0.3,
            0.0
         );
         world.addParticle(
            ParticleTypes.SWEEP_ATTACK,
            x + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
            y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
            z + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
            0.0,
            -0.3,
            0.0
         );
         if (!world.isClientSide()) {
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(20.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator.getPersistentData().getBoolean("marked")) {
                  entity.setDeltaMovement(
                     new Vec3(
                        (entityiterator.getX() - entity.getX()) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.15, 0.3),
                        (entityiterator.getY() - entity.getY()) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.15, 0.3),
                        (entityiterator.getZ() - entity.getZ()) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.15, 0.3)
                     )
                  );
               }
            }

            _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator.getPersistentData().getBoolean("marked")) {
                  entityiterator.hurt(new DamageSource(entityiterator.level().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(net.minecraft.world.damagesource.DamageTypes.GENERIC), entity), 10.0F);
                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.FIREWORK, x, y, z, 10, 1.0, 1.0, 1.0, 0.5);
                  }

                  entity.getPersistentData().putBoolean("marked", false);
                  if (!entity.level().isClientSide()) {
                     entity.discard();
                  }
               }
            }

            if (entity.getPersistentData().getDouble("b") == 0.0) {
               entity.getPersistentData().putDouble("b", 5.0);
               if (world instanceof Level _level) {
                  if (!_level.isClientSide()) {
                     _level.playSound(
                        null,
                        BlockPos.containing(x, y, z),
                        dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                        SoundSource.NEUTRAL,
                        1.0F,
                        Mth.nextInt(net.minecraft.util.RandomSource.create(), -2, 2)
                     );
                  } else {
                     _level.playLocalSound(
                        x,
                        y,
                        z,
                        dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                        SoundSource.NEUTRAL,
                        1.0F,
                        Mth.nextInt(net.minecraft.util.RandomSource.create(), -2, 2),
                        false
                     );
                  }
               }
            } else {
               entity.getPersistentData().putDouble("b", entity.getPersistentData().getDouble("b") - 1.0);
            }

            if (entity.getPersistentData().getDouble("a") == 0.0) {
               entity.getPersistentData().putDouble("a", 50.0);
            } else {
               entity.getPersistentData().putDouble("a", entity.getPersistentData().getDouble("a") - 1.0);
            }

            if (entity.getPersistentData().getDouble("a") == 1.0 && !entity.level().isClientSide()) {
               entity.discard();
            }
         }
      }
   }
}
