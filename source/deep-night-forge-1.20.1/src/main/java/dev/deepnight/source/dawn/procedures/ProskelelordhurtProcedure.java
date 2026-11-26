package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.Spell3Entity;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ProskelelordhurtProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
      if (entity != null && sourceentity != null) {
         if (!world.isClientSide()) {
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(100.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == sourceentity) {
                  entity.getPersistentData().putBoolean("cheese", true);
               }
            }

            _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == sourceentity) {
                  entity.getPersistentData().putBoolean("cheese", false);
                  if (!entity.getPersistentData().getBoolean("hurtcd")) {
                     entity.getPersistentData().putBoolean("hurtcd", true);
                     if (entity.getPersistentData().getDouble("Old HP") == 0.0) {
                        entity.getPersistentData().putDouble("Old HP", entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0);
                     } else {
                        entity.getPersistentData()
                           .putDouble(
                              "DMG", entity.getPersistentData().getDouble("Old HP") - (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0F)
                           );
                     }

                     if (entity.getPersistentData().getDouble("DMG") > 14.0) {
                        entity.getPersistentData().putDouble("Poise", entity.getPersistentData().getDouble("Poise") + 90.0);
                     } else if (entity.getPersistentData().getDouble("DMG") > 10.0) {
                        entity.getPersistentData().putDouble("Poise", entity.getPersistentData().getDouble("Poise") + 70.0);
                     } else if (entity.getPersistentData().getDouble("DMG") > 4.0) {
                        entity.getPersistentData().putDouble("Poise", entity.getPersistentData().getDouble("Poise") + 55.0);
                     }

                     entity.getPersistentData().putDouble("Old HP", entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0);
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("cheese")
               && world.getEntitiesOfClass(Spell3Entity.class, AABB.ofSize(new Vec3(x, y, z), 200.0, 200.0, 200.0), e -> true).isEmpty()) {
               if (world instanceof ServerLevel _level) {
                  Entity entityToSpawn = new Spell3Entity(SimpleMobsModEntities.SPELL_3.get(), _level);
                  entityToSpawn.moveTo(sourceentity.getX(), sourceentity.getY(), sourceentity.getZ(), Mth.nextInt(net.minecraft.util.RandomSource.create(), 1, 360), 0.0F);
                  entityToSpawn.setYBodyRot(Mth.nextInt(net.minecraft.util.RandomSource.create(), 1, 360));
                  entityToSpawn.setYHeadRot(Mth.nextInt(net.minecraft.util.RandomSource.create(), 1, 360));
                  if (entityToSpawn instanceof Mob _mobToSpawn) {
                     _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                  }

                  world.addFreshEntity(entityToSpawn);
               }

               entity.getPersistentData().putBoolean("cheese", false);
            }
         }
      }
   }
}
