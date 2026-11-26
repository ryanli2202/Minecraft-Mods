package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.MartianEntity;
import dev.deepnight.source.dawn.entity.RoarEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ProroarProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         Vec3 _center = new Vec3(x, y, z);

         for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.0), e -> true)
            .stream()
            .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
            .collect(Collectors.toList())) {
            if (!(entityiterator instanceof RoarEntity) && !(entityiterator instanceof MartianEntity)) {
               entityiterator.setDeltaMovement(
                  new Vec3((entityiterator.getX() - entity.getX()) / 15.0, entity.getDeltaMovement().y(), (entityiterator.getZ() - entity.getZ()) / 15.0)
               );
               entityiterator.hurt(new DamageSource(entityiterator.level().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(net.minecraft.world.damagesource.DamageTypes.GENERIC), entity), 3.0F);
            }
         }

         if (entity.getPersistentData().getDouble("a") == 0.0) {
            entity.getPersistentData().putDouble("a", 60.0);
         } else {
            entity.getPersistentData().putDouble("a", entity.getPersistentData().getDouble("a") - 1.0);
         }

         if (entity.getPersistentData().getDouble("a") == 1.0 && !entity.level().isClientSide()) {
            entity.discard();
         }
      }
   }
}
