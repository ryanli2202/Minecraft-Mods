package dev.deepnight.source.dawn.procedures;

import net.minecraft.world.entity.Entity;

public class HerobrinehurtProcedure {
   public static void execute(Entity entity) {
      if (entity != null) {
         if (entity.getPersistentData().getBoolean("eat")) {
            entity.getPersistentData().putBoolean("hurt", true);
         }
      }
   }
}
