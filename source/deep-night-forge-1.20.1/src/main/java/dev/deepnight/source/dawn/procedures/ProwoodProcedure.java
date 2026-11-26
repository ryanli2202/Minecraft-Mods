package dev.deepnight.source.dawn.procedures;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;

public class ProwoodProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z) {
      double a = 0.0;
      double b = 0.0;
      double i2 = 0.0;
      double a2 = 0.0;
      double b2 = 0.0;
      double l = 0.0;
      world.destroyBlock(BlockPos.containing(x, y, z), false);
   }
}
