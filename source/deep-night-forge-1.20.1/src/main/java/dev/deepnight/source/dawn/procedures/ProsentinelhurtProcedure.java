package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ProsentinelhurtProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
      if (entity != null && sourceentity != null) {
         if (!entity.getPersistentData().getBoolean("Blocking")
            && !entity.getPersistentData().getBoolean("Shieldoff")
            && !entity.getPersistentData().getBoolean("Attacking")) {
            entity.getPersistentData().putBoolean("Block", true);
         }

         if (entity.getPersistentData().getBoolean("Blocking") && entity.getPersistentData().getDouble("BlockCD") > 600.0) {
            entity.getPersistentData().putDouble("BlockCD", entity.getPersistentData().getDouble("BlockCD") + 100.0);
         }

         if (!entity.getPersistentData().getBoolean("Aggro") && entity.getPersistentData().getBoolean("Shieldoff")) {
            entity.getPersistentData().putBoolean("Off", true);
         }

         if (!entity.getPersistentData().getBoolean("Aggro") && entity.getPersistentData().getBoolean("Guardb")) {
            entity.getPersistentData().putBoolean("Guardc", true);
            entity.getPersistentData().putBoolean("Guardb", false);
         }

         if (entity.getPersistentData().getBoolean("Parry") && sourceentity == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               dev.deepnight.source.SourceStun.apply(sourceentity,40);

               entity.getPersistentData().putBoolean("Parried", true);
            }
         }

         if (!entity.getPersistentData().getBoolean("hurtcd")) {
            entity.getPersistentData().putBoolean("hurtcd", true);
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entity.getPersistentData().getBoolean("Blocking") && entity.getPersistentData().getBoolean("Aggro")) {
                  if (entity.getPersistentData().getDouble("Old HP") == 0.0) {
                     entity.getPersistentData().putDouble("Old HP", entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0);
                  } else {
                     entity.getPersistentData()
                        .putDouble(
                           "DMG", entity.getPersistentData().getDouble("Old HP") - (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0F)
                        );
                  }

                  if (entity.getPersistentData().getDouble("DMG") > 0.8) {
                     entity.getPersistentData().putDouble("Poise", entity.getPersistentData().getDouble("Poise") + 80.0);
                  }

                  entity.getPersistentData().putDouble("Old HP", entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0);
               }
            }
         }
      }
   }
}
