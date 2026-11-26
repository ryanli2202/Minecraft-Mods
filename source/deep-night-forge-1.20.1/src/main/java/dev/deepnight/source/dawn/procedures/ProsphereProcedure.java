package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.DraggingSpellEntity;
import dev.deepnight.source.dawn.entity.HerobrineseEntity;
import dev.deepnight.source.dawn.entity.SphereEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ProsphereProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         double loop = 0.0;
         double particleAmount = 0.0;
         double xRadius = 0.0;
         double zRadius = 0.0;
         if (entity.getPersistentData().getDouble("timer") == 0.0) {
            entity.getPersistentData().putDouble("timer", 19.0);
         } else {
            entity.getPersistentData().putDouble("timer", entity.getPersistentData().getDouble("timer") - 1.0);
         }

         if (entity.getPersistentData().getDouble("timer") < 15.0 && entity.getPersistentData().getDouble("timer") >= 12.0) {
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(20.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (!(entityiterator instanceof SphereEntity)
                  && !(entityiterator instanceof HerobrineseEntity)
                  && !(entityiterator instanceof DraggingSpellEntity)) {
                  entityiterator.hurt(new DamageSource(entityiterator.level().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(net.minecraft.world.damagesource.DamageTypes.GENERIC), entity), 1.0F);
                  if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 2, false, false));
                  }

                  entityiterator.setDeltaMovement(new Vec3((entity.getX() - entityiterator.getX()) / 5.0, 0.0, (entity.getZ() - entityiterator.getZ()) / 5.0));
               }
            }
         }

         if (entity.getPersistentData().getDouble("timer") == 1.0 && !entity.level().isClientSide()) {
            entity.discard();
         }
      }
   }
}
