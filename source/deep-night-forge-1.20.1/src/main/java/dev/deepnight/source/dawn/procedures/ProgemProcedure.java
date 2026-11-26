package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.GemspikeEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import dev.deepnight.source.SourceDamage;
import dev.deepnight.source.SourceDamage.StunType;

public class ProgemProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (entity.getPersistentData().getDouble("a") == 0.0) {
            entity.getPersistentData().putDouble("a", 21.0);
         } else {
            entity.getPersistentData().putDouble("a", entity.getPersistentData().getDouble("a") - 1.0);
         }

         if (entity.getPersistentData().getDouble("a") == 16.0) {
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (!(entityiterator instanceof GemspikeEntity)) {
                  SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                     .setInitialPosition(entity.position())
                     .setStunType(StunType.LONG)
                     .setImpact(15.0F)
                     .setArmorNegation(1.0F);
                  entityiterator.hurt((DamageSource)dmgSrc, 14.0F);
                  entityiterator.setDeltaMovement(
                     new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)), 0.7, Math.cos(Math.toRadians(entity.getYRot())))
                  );
               }
            }
         } else if (entity.getPersistentData().getDouble("a") == 2.0 && !entity.level().isClientSide()) {
            entity.discard();
         }
      }
   }
}
