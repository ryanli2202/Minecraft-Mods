package dev.deepnight.source.dawn.procedures;

import dev.deepnight.source.dawn.entity.LightningDragonEntity;
import net.minecraft.world.entity.Entity;

public class LightningDragonSpawnProcedure {
   public static void execute(Entity entity) {
      if (entity != null) {
         if (entity instanceof LightningDragonEntity) {
            entity.getPersistentData().putBoolean("lightningdragon", true);
            entity.getPersistentData().putBoolean("Wake", true);
         }
      }
   }
}
