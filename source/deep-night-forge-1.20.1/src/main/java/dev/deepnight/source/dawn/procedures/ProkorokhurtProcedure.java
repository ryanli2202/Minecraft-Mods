package dev.deepnight.source.dawn.procedures;

import dev.deepnight.source.dawn.init.SimpleMobsModParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;

public class ProkorokhurtProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (entity.getPersistentData().getBoolean("awake")) {
            entity.getPersistentData().putBoolean("hurt", true);
         } else {
            if (world instanceof ServerLevel _level) {
               _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.SNOWYCLOUD.get(), x, y, z, 4, 0.5, 0.5, 0.5, 0.5);
            }

            if (!entity.level().isClientSide()) {
               entity.discard();
            }
         }
      }
   }
}
