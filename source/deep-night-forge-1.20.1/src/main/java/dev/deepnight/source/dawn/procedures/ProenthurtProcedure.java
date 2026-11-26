package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.SproutEntity;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ProenthurtProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity, float amount) {
      if (entity != null && sourceentity != null) {
         if (!world.isClientSide()) {
            if (!entity.getPersistentData().getBoolean("hurtcd")) {
               entity.getPersistentData().putBoolean("hurtcd", true);
               Vec3 _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator == sourceentity) {
                     if (amount > 14.0F) {
                        entity.getPersistentData().putDouble("Poise", entity.getPersistentData().getDouble("Poise") + 90.0);
                     } else if (amount > 10.0F) {
                        entity.getPersistentData().putDouble("Poise", entity.getPersistentData().getDouble("Poise") + 80.0);
                     } else if (amount > 4.0F) {
                        entity.getPersistentData().putDouble("Poise", entity.getPersistentData().getDouble("Poise") + 75.0);
                     }

                     if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() instanceof AxeItem) {
                        entity.getPersistentData().putDouble("Poise", entity.getPersistentData().getDouble("Poise") + 35.0);
                     }
                  }
               }
            }

            if (!entity.getPersistentData().getBoolean("Wake")) {
               entity.getPersistentData().putDouble("Poise", entity.getPersistentData().getDouble("Poise") + 30.0);
            }

            if (amount > 14.0F) {
               if (Math.random() >= 0.4 && !entity.getPersistentData().getBoolean("Sprout")) {
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new SproutEntity(SimpleMobsModEntities.SPROUT.get(), _level);
                     entityToSpawn.moveTo(x, y + 5.0, z, 0.0F, 0.0F);
                     entityToSpawn.setYBodyRot(0.0F);
                     entityToSpawn.setYHeadRot(0.0F);
                     entityToSpawn.setDeltaMovement(Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0), 1.0, Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0));
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new SproutEntity(SimpleMobsModEntities.SPROUT.get(), _level);
                     entityToSpawn.moveTo(x, y + 5.0, z, 0.0F, 0.0F);
                     entityToSpawn.setYBodyRot(0.0F);
                     entityToSpawn.setYHeadRot(0.0F);
                     entityToSpawn.setDeltaMovement(Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0), 1.0, Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0));
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new SproutEntity(SimpleMobsModEntities.SPROUT.get(), _level);
                     entityToSpawn.moveTo(x, y + 5.0, z, 0.0F, 0.0F);
                     entityToSpawn.setYBodyRot(0.0F);
                     entityToSpawn.setYHeadRot(0.0F);
                     entityToSpawn.setDeltaMovement(Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0), 1.0, Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0));
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  entity.getPersistentData().putBoolean("Sprout", true);
               }
            } else if (amount > 10.0F) {
               if (Math.random() >= 0.6) {
                  if (!entity.getPersistentData().getBoolean("Sprout")) {
                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new SproutEntity(SimpleMobsModEntities.SPROUT.get(), _level);
                        entityToSpawn.moveTo(x, y + 5.0, z, 0.0F, 0.0F);
                        entityToSpawn.setYBodyRot(0.0F);
                        entityToSpawn.setYHeadRot(0.0F);
                        entityToSpawn.setDeltaMovement(Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0), 1.0, Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0));
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }

                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new SproutEntity(SimpleMobsModEntities.SPROUT.get(), _level);
                        entityToSpawn.moveTo(x, y + 5.0, z, 0.0F, 0.0F);
                        entityToSpawn.setYBodyRot(0.0F);
                        entityToSpawn.setYHeadRot(0.0F);
                        entityToSpawn.setDeltaMovement(Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0), 1.0, Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0));
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  }

                  entity.getPersistentData().putBoolean("Sprout", true);
               }
            } else if (amount > 4.0F && Math.random() >= 0.8 && !entity.getPersistentData().getBoolean("Sprout")) {
               if (world instanceof ServerLevel _level) {
                  Entity entityToSpawn = new SproutEntity(SimpleMobsModEntities.SPROUT.get(), _level);
                  entityToSpawn.moveTo(x, y + 5.0, z, 0.0F, 0.0F);
                  entityToSpawn.setYBodyRot(0.0F);
                  entityToSpawn.setYHeadRot(0.0F);
                  entityToSpawn.setDeltaMovement(Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0), 1.0, Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0));
                  if (entityToSpawn instanceof Mob _mobToSpawn) {
                     _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                  }

                  world.addFreshEntity(entityToSpawn);
               }

               entity.getPersistentData().putBoolean("Sprout", true);
            }
         }
      }
   }
}
