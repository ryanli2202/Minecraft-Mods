package dev.deepnight.source.dawn.procedures;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.LevelAccessor;

public class ProseedProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z) {
      world.addParticle(ParticleTypes.SPORE_BLOSSOM_AIR, x, y, z, 0.0, 0.0, 0.0);
      world.addParticle(ParticleTypes.CRIMSON_SPORE, x, y, z, 0.0, 0.0, 0.0);
   }
}
