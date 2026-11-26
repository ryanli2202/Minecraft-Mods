package dev.deepnight.source.dawn.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;

public class PrometeorgroundProcedure {
   public static void execute(LevelAccessor world, Entity entity) {
      if (entity != null) {
         if (!world.isClientSide()) {
            if (entity.getPersistentData().getDouble("tick") == 0.0) {
               entity.getPersistentData().putDouble("tick", 81.0);
            } else {
               entity.getPersistentData().putDouble("tick", entity.getPersistentData().getDouble("tick") - 1.0);
            }

            if (entity.getPersistentData().getDouble("tick") == 1.0 && !entity.level().isClientSide()) {
               entity.discard();
            }
         }
      }
   }
}
