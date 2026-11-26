package dev.deepnight.source.dawn.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;

public class ProgbsmallProcedure {
   public static void execute(LevelAccessor world, Entity entity) {
      if (entity != null) {
         if (!world.isClientSide()) {
            if (entity.getPersistentData().getDouble("spawn") == 0.0) {
               entity.getPersistentData().putDouble("spawn", 21.0);
            } else {
               entity.getPersistentData().putDouble("spawn", entity.getPersistentData().getDouble("spawn") - 1.0);
            }

            if (entity.getPersistentData().getDouble("spawn") == 21.0) {
               dev.deepnight.source.SourceStun.groundBreak(entity,1.5);
            } else if (entity.getPersistentData().getDouble("spawn") == 1.0 && !entity.level().isClientSide()) {
               entity.discard();
            }
         }
      }
   }
}
