package dev.deepnight.source.dawn.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;

public class ProwerehurtProcedure {
   public static void execute(LevelAccessor world, Entity entity) {
      if (entity != null) {
         if (!world.isClientSide() && !entity.getPersistentData().getBoolean("hurtcd")) {
            entity.getPersistentData().putBoolean("hurtcd", true);
            if (entity.getPersistentData().getDouble("Old HP") == 0.0) {
               entity.getPersistentData().putDouble("Old HP", entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0);
            } else {
               entity.getPersistentData()
                  .putDouble("DMG", entity.getPersistentData().getDouble("Old HP") - (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0F));
            }

            if (entity.getPersistentData().getDouble("DMG") > 14.0) {
               entity.getPersistentData().putDouble("Poise", entity.getPersistentData().getDouble("Poise") + 90.0);
            } else if (entity.getPersistentData().getDouble("DMG") > 10.0) {
               entity.getPersistentData().putDouble("Poise", entity.getPersistentData().getDouble("Poise") + 70.0);
            } else if (entity.getPersistentData().getDouble("DMG") > 4.0) {
               entity.getPersistentData().putDouble("Poise", entity.getPersistentData().getDouble("Poise") + 55.0);
            }

            entity.getPersistentData().putDouble("Old HP", entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0);
         }
      }
   }
}
