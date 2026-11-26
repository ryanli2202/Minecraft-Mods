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

public class ProghostprojProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         world.addParticle(ParticleTypes.PORTAL, x, y, z, 0.0, -0.3, 0.0);
         if (!world.isClientSide()) {
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(20.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator.getPersistentData().getBoolean("marked")) {
                  entity.setDeltaMovement(
                     new Vec3(
                        (entityiterator.getX() - entity.getX()) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.03, 0.09),
                        (entityiterator.getY() + 1.0 - entity.getY()) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.03, 0.09),
                        (entityiterator.getZ() - entity.getZ()) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.03, 0.09)
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
                  entityiterator.hurt(new DamageSource(entityiterator.level().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(net.minecraft.world.damagesource.DamageTypes.GENERIC), entity), 7.0F);
                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.EXPLOSION, x, y, z, 10, 1.0, 1.0, 1.0, 0.5);
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.skeleton.death"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.skeleton.death"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  if (!entity.level().isClientSide()) {
                     entity.discard();
                  }
               }
            }

            if (entity.getPersistentData().getDouble("a") == 0.0) {
               entity.setNoGravity(true);
               entity.getPersistentData().putDouble("a", 160.0);
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
