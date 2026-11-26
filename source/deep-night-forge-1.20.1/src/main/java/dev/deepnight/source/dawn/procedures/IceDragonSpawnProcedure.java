package dev.deepnight.source.dawn.procedures;

import dev.deepnight.source.dawn.entity.IceDragonAmbientEntity;
import dev.deepnight.source.dawn.entity.IceDragonEntity;
import net.minecraft.world.entity.Entity;

public class IceDragonSpawnProcedure {
   public static void execute(Entity entity) {
      if (entity != null) {
         if (entity instanceof IceDragonAmbientEntity) {
            entity.getPersistentData().putBoolean("icedragon", true);
            entity.setNoGravity(true);
         } else if (entity instanceof IceDragonEntity) {
            entity.getPersistentData().putBoolean("icedragon", true);
            entity.getPersistentData().putBoolean("Wake", true);
         }
      }
   }
}
