package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.EntEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ProstaffinttickProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         Vec3 _center = new Vec3(x, y, z);

         for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
            .stream()
            .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
            .collect(Collectors.toList())) {
            if (entityiterator instanceof EntEntity) {
               Entity _ent = entity;
               _ent.teleportTo(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ());
               if (_ent instanceof ServerPlayer _serverPlayer) {
                  _serverPlayer.connection.teleport(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ(), _ent.getYRot(), _ent.getXRot());
               }
            }
         }

         if (world.getEntitiesOfClass(EntEntity.class, AABB.ofSize(new Vec3(x, y, z), 10.0, 10.0, 10.0), e -> true).isEmpty() && !entity.level().isClientSide()) {
            entity.discard();
         }
      }
   }
}
