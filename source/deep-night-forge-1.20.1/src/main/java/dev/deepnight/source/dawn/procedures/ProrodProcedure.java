package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.DragonLordEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ProrodProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (!world.isClientSide()) {
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(10.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator instanceof DragonLordEntity) {
                  Entity _ent = entity;
                  _ent.teleportTo(entityiterator.getX(), entityiterator.getY() + 1.0, entityiterator.getZ());
                  if (_ent instanceof ServerPlayer _serverPlayer) {
                     _serverPlayer.connection
                        .teleport(entityiterator.getX(), entityiterator.getY() + 1.0, entityiterator.getZ(), _ent.getYRot(), _ent.getXRot());
                  }
               }
            }

            if (world instanceof ServerLevel _level) {
               _level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 2, 6.0, 1.0, 6.0, 0.2);
            }

            if (entity.getPersistentData().getDouble("tick") == 0.0) {
               entity.getPersistentData().putDouble("tick", 300.0);
            } else {
               entity.getPersistentData().putDouble("tick", entity.getPersistentData().getDouble("tick") - 1.0);
            }

            if (entity.getPersistentData().getDouble("tick") == 1.0 && !entity.level().isClientSide()) {
               entity.discard();
            }
         }
      }
   }
}
