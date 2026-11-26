package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.SkeletonlordEntity;
import dev.deepnight.source.dawn.entity.Spell1Entity;
import dev.deepnight.source.dawn.entity.Spell2Entity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class Prospell2Procedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (entity.getPersistentData().getDouble("a") == 0.0) {
            entity.getPersistentData().putDouble("a", 35.0);
         } else {
            entity.getPersistentData().putDouble("a", entity.getPersistentData().getDouble("a") - 1.0);
         }

         Vec3 _center = new Vec3(x, y, z);

         for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
            .stream()
            .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
            .collect(Collectors.toList())) {
            if (!(entityiterator instanceof SkeletonlordEntity) && !(entityiterator instanceof Spell2Entity) && !(entityiterator instanceof Spell1Entity)) {
               entityiterator.setDeltaMovement(new Vec3(entity.getX() - entityiterator.getX(), 1.7, entity.getZ() - entityiterator.getZ()));
            }
         }

         if (entity.getPersistentData().getDouble("a") == 1.0 && !entity.level().isClientSide()) {
            entity.discard();
         }
      }
   }
}
