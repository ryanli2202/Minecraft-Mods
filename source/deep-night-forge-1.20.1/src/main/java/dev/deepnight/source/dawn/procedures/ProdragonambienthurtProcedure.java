package dev.deepnight.source.dawn.procedures;

import java.util.Random;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ProdragonambienthurtProcedure {
   public static void execute(Entity entity, Entity sourceentity) {
      if (entity != null && sourceentity != null) {
         if (sourceentity instanceof LivingEntity) {
            entity.getPersistentData().putDouble("shot", entity.getPersistentData().getDouble("shot") + 1.0);
         }

         if (!entity.getPersistentData().getBoolean("fall")) {
            if (entity.getPersistentData().getDouble("shot") == Mth.nextInt(net.minecraft.util.RandomSource.create(), 2, 3)) {
               entity.getPersistentData().putBoolean("fall", true);
            } else {
               entity.getPersistentData().putBoolean("glide", true);
            }
         }
      }
   }
}
