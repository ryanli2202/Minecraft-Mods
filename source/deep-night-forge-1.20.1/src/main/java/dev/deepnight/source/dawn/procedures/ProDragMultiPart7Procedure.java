package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.DragonLordEntity;
import dev.deepnight.source.dawn.entity.LightningDragonEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ProDragMultiPart7Procedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         entity.getPersistentData().putBoolean("despawn", true);
         if (entity.getPersistentData().getBoolean("lightningdragon")) {
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(15.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator instanceof LightningDragonEntity) {
                  entity.getPersistentData().putBoolean("despawn", false);
                  Entity _ent = entity;
                  Vec3 look = entityiterator.getViewVector(1.0F);
                  _ent.teleportTo(entityiterator.getX() + 6.0 * look.x, entityiterator.getY(), entityiterator.getZ() + 6.0 * look.z);
               }
            }
         } else if (entity.getPersistentData().getBoolean("dragon_lord")) {
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(20.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator instanceof DragonLordEntity && dev.deepnight.source.DragonLordParts.belongsTo(entity, entityiterator)) {
                  entity.getPersistentData().putBoolean("despawn", false);
                  Entity _ent = entity;
                  Vec3 look = entityiterator.getViewVector(1.0F);
                  _ent.teleportTo(entityiterator.getX() + 7.0 * look.x, entityiterator.getY(), entityiterator.getZ() + 7.0 * look.z);
               }
            }
         }

         if (entity.getPersistentData().getBoolean("despawn") && !entity.level().isClientSide()) {
            entity.discard();
         }
      }
   }
}
