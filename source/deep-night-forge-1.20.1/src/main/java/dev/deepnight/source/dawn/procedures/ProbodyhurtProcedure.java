package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.FireGiantEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ProbodyhurtProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity, float amount) {
      if (sourceentity != null) {
         if (entity instanceof LivingEntity) {
            if (!world.isClientSide()) {
               Vec3 _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator instanceof FireGiantEntity) {
                     DamageSource dmgSrc = entity instanceof LivingEntity ? sourceentity.damageSources().mobAttack((LivingEntity)sourceentity) : entity.damageSources().generic();
                     entityiterator.hurt(dmgSrc, amount);
                  }
               }
            }
         }
      }
   }
}
