package dev.deepnight.source.dawn.procedures;

import net.minecraft.world.entity.Entity;

public class ProfiregiantmarkProcedure {
   public static void execute(Entity entity) {
      if (entity != null) {
         entity.getPersistentData().putBoolean("firegiant", true);
      }
   }
}
