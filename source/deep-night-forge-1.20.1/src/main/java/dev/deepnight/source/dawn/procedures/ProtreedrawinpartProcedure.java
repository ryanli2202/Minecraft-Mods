package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.EntEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ProtreedrawinpartProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (!world.isClientSide()) {
            if (entity.getPersistentData().getDouble("timer") == 0.0) {
               entity.getPersistentData().putDouble("timer", 81.0);
            } else {
               entity.getPersistentData().putDouble("timer", entity.getPersistentData().getDouble("timer") - 1.0);
            }

            if (entity.getPersistentData().getDouble("timer") == 1.0 && !entity.level().isClientSide()) {
               entity.discard();
            }

            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(50.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator instanceof EntEntity
                  || entityiterator.getPersistentData().getBoolean("staff_inv_tick")
                  || entityiterator.getPersistentData().getBoolean("staff_inv_tick2")) {
                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.DAMAGE_INDICATOR, x, y, z, 1, 0.3, 0.3, 0.3, 0.2);
                  }

                  entity.setDeltaMovement(
                     new Vec3(
                        (entityiterator.getX() - entity.getX()) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.05, 0.14),
                        (entityiterator.getY() - entity.getY()) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.05, 0.14),
                        (entityiterator.getZ() - entity.getZ()) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.05, 0.14)
                     )
                  );
               }
            }

            _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(50.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (false) {
                  entity.setDeltaMovement(
                     new Vec3(
                        (entityiterator.getX() - entity.getX()) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.05, 0.14),
                        (entityiterator.getY() - entity.getY()) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.05, 0.14),
                        (entityiterator.getZ() - entity.getZ()) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.05, 0.14)
                     )
                  );
               }
            }

            _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if ((
                     false
                        || entityiterator instanceof EntEntity
                        || entityiterator.getPersistentData().getBoolean("staff_inv_tick")
                        || entityiterator.getPersistentData().getBoolean("staff_inv_tick2")
                  )
                  && !entity.level().isClientSide()) {
                  entity.discard();
               }
            }
         }
      }
   }
}
