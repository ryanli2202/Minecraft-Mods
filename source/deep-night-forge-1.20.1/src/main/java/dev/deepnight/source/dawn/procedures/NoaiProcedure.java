package dev.deepnight.source.dawn.procedures;

import net.minecraft.world.entity.Entity;

public class NoaiProcedure {
   public static boolean execute(Entity entity) {
      return entity == null ? false : !entity.getPersistentData().getBoolean("noai");
   }
}
