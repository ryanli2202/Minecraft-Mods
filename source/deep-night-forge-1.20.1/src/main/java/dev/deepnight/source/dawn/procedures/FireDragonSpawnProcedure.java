package dev.deepnight.source.dawn.procedures;

import dev.deepnight.source.dawn.entity.FireDragonAmbientEntity;
import dev.deepnight.source.dawn.entity.FireDragonEntity;
import net.minecraft.world.entity.Entity;

public class FireDragonSpawnProcedure {
   public static void execute(Entity entity) {
      if (entity != null) {
         if (entity instanceof FireDragonAmbientEntity) {
            entity.getPersistentData().putBoolean("firedragon", true);
            entity.setNoGravity(true);
         } else if (entity instanceof FireDragonEntity) {
            entity.getPersistentData().putBoolean("firedragon", true);
            entity.getPersistentData().putBoolean("Wake", true);
         }
      }
   }
}
