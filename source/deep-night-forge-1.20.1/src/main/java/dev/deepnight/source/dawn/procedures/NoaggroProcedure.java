package dev.deepnight.source.dawn.procedures;

import net.minecraft.world.entity.Entity;

public class NoaggroProcedure {
   public static boolean execute(Entity entity) {
      return entity == null ? false : !entity.getPersistentData().getBoolean("noaggro") && !entity.getPersistentData().getBoolean("noai");
   }
}
