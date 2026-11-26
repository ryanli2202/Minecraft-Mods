package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.FireGiantEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class PromultipartlegProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (entity.getPersistentData().getDouble("move") == 0.0) {
            entity.getPersistentData().putDouble("move", 10.0);
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator instanceof FireGiantEntity) {
                  Entity _ent = entityiterator;
                  Vec3 look = entity.getLookAngle();
                  Vec3 tangent = new Vec3(-look.z, 0.0, look.x);
                  entity.teleportTo(entityiterator.getX() + -2.0 * tangent.x, entityiterator.getY(), entityiterator.getZ() + -2.0 * tangent.z);
                  if (_ent instanceof ServerPlayer _serverPlayer) {
                     _serverPlayer.connection.teleport(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ(), _ent.getYRot(), _ent.getXRot());
                  }
               }
            }

            if (world.getEntitiesOfClass(FireGiantEntity.class, AABB.ofSize(new Vec3(x, y, z), 10.0, 10.0, 10.0), e -> true).isEmpty()
               && !entity.level().isClientSide()) {
               entity.discard();
            }
         } else {
            entity.getPersistentData().putDouble("move", entity.getPersistentData().getDouble("move") - 1.0);
         }
      }
   }
}
