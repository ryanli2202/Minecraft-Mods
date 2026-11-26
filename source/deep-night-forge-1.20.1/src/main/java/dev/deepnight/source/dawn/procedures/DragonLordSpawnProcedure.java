package dev.deepnight.source.dawn.procedures;

import dev.deepnight.source.dawn.entity.DLMultiPart2Entity;
import dev.deepnight.source.dawn.entity.DLMultiPart3Entity;
import dev.deepnight.source.dawn.entity.DLMultiPart4Entity;
import dev.deepnight.source.dawn.entity.DLMultiPart5Entity;
import dev.deepnight.source.dawn.entity.DLMultiPart6Entity;
import dev.deepnight.source.dawn.entity.DLMultiPart7Entity;
import dev.deepnight.source.dawn.entity.DragonLordEntity;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;

public class DragonLordSpawnProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (entity instanceof DragonLordEntity) {
            entity.getPersistentData().putBoolean("dragon_lord", true);
            if (world instanceof ServerLevel _level) {
               Entity entityToSpawn = new DLMultiPart2Entity(SimpleMobsModEntities.DL_MULTI_PART_2.get(), _level);
               entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
               if (entityToSpawn instanceof Mob _mobToSpawn) {
                  _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
               }

               world.addFreshEntity(entityToSpawn);
            }

            if (world instanceof ServerLevel _level) {
               Entity entityToSpawn = new DLMultiPart3Entity(SimpleMobsModEntities.DL_MULTI_PART_3.get(), _level);
               entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
               if (entityToSpawn instanceof Mob _mobToSpawn) {
                  _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
               }

               world.addFreshEntity(entityToSpawn);
            }

            if (world instanceof ServerLevel _level) {
               Entity entityToSpawn = new DLMultiPart4Entity(SimpleMobsModEntities.DL_MULTI_PART_4.get(), _level);
               entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
               if (entityToSpawn instanceof Mob _mobToSpawn) {
                  _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
               }

               world.addFreshEntity(entityToSpawn);
            }

            if (world instanceof ServerLevel _level) {
               Entity entityToSpawn = new DLMultiPart5Entity(SimpleMobsModEntities.DL_MULTI_PART_5.get(), _level);
               entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
               if (entityToSpawn instanceof Mob _mobToSpawn) {
                  _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
               }

               world.addFreshEntity(entityToSpawn);
            }

            if (world instanceof ServerLevel _level) {
               Entity entityToSpawn = new DLMultiPart6Entity(SimpleMobsModEntities.DL_MULTI_PART_6.get(), _level);
               entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
               if (entityToSpawn instanceof Mob _mobToSpawn) {
                  _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
               }

               world.addFreshEntity(entityToSpawn);
            }

            if (world instanceof ServerLevel _level) {
               Entity entityToSpawn = new DLMultiPart7Entity(SimpleMobsModEntities.DL_MULTI_PART_7.get(), _level);
               entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
               if (entityToSpawn instanceof Mob _mobToSpawn) {
                  _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
               }

               world.addFreshEntity(entityToSpawn);
            }
         }
      }
   }
}
