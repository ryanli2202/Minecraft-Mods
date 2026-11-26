package dev.deepnight.source.dawn.procedures;

import dev.deepnight.source.dawn.entity.MultiPartLegEntity;
import dev.deepnight.source.dawn.entity.MultipartbodyEntity;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;

public class ProfgspawnProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         entity.getPersistentData().putBoolean("firegiant", true);
         if (world instanceof ServerLevel _level) {
            Entity entityToSpawn = new MultiPartLegEntity(SimpleMobsModEntities.MULTI_PART_LEG.get(), _level);
            entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
            if (entityToSpawn instanceof Mob _mobToSpawn) {
               _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
            }

            world.addFreshEntity(entityToSpawn);
         }

         if (world instanceof ServerLevel _level) {
            Entity entityToSpawn = new MultipartbodyEntity(SimpleMobsModEntities.MULTIPARTBODY.get(), _level);
            entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
            if (entityToSpawn instanceof Mob _mobToSpawn) {
               _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
            }

            world.addFreshEntity(entityToSpawn);
         }
      }
   }
}
