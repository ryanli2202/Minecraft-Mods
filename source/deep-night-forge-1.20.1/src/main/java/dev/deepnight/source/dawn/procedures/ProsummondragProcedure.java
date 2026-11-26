package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.FireDragonAmbientEntity;
import dev.deepnight.source.dawn.entity.FireDragonEntity;
import dev.deepnight.source.dawn.entity.IceDragonAmbientEntity;
import dev.deepnight.source.dawn.entity.IceDragonEntity;
import dev.deepnight.source.dawn.entity.LightningDragonAmbientEntity;
import dev.deepnight.source.dawn.entity.LightningDragonEntity;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.init.SimpleMobsModMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ProsummondragProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (entity.getPersistentData().getDouble("spawn") == 0.0) {
            entity.getPersistentData().putDouble("spawn", Mth.nextInt(net.minecraft.util.RandomSource.create(), 61, 141));
         } else {
            entity.getPersistentData().putDouble("spawn", entity.getPersistentData().getDouble("spawn") - 1.0);
         }

         if (entity.getPersistentData().getDouble("spawn") == 10.0) {
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(20.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (world.getEntitiesOfClass(FireDragonEntity.class, AABB.ofSize(new Vec3(x, y, z), 300.0, 300.0, 300.0), e -> true).isEmpty()
                  && world.getEntitiesOfClass(FireDragonAmbientEntity.class, AABB.ofSize(new Vec3(x, y, z), 300.0, 300.0, 300.0), e -> true).isEmpty()
                  && world.getEntitiesOfClass(IceDragonEntity.class, AABB.ofSize(new Vec3(x, y, z), 300.0, 300.0, 300.0), e -> true).isEmpty()
                  && world.getEntitiesOfClass(IceDragonAmbientEntity.class, AABB.ofSize(new Vec3(x, y, z), 300.0, 300.0, 300.0), e -> true).isEmpty()
                  && world.getEntitiesOfClass(LightningDragonEntity.class, AABB.ofSize(new Vec3(x, y, z), 300.0, 300.0, 300.0), e -> true).isEmpty()
                  && world.getEntitiesOfClass(LightningDragonAmbientEntity.class, AABB.ofSize(new Vec3(x, y, z), 300.0, 300.0, 300.0), e -> true).isEmpty()
                  && (entityiterator instanceof Player || entityiterator instanceof ServerPlayer)
                  && world.canSeeSkyFromBelowWater(BlockPos.containing(entityiterator.getX(), entityiterator.getY() + 1.0, entityiterator.getZ()))) {
                  if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
                     _entity.addEffect(new MobEffectInstance(SimpleMobsModMobEffects.DRAGON_SUMMONED.get(), 3600, 0, false, false));
                  }

                  if (Math.random() >= 0.7) {
                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = dev.deepnight.DeepNight.PETS.get(dev.deepnight.Species.DAWN_ICE_DRAGON).get().create(_level);
                        entityToSpawn.moveTo(
                           entityiterator.getX() + entityiterator.getLookAngle().x * -25.0,
                           entityiterator.getY() + 30.0,
                           entityiterator.getZ() + entityiterator.getLookAngle().z * -25.0,
                           entityiterator.getYRot(),
                           0.0F
                        );
                        entityToSpawn.setYBodyRot(entityiterator.getYRot());
                        entityToSpawn.setYHeadRot(entityiterator.getYRot());
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  } else if (Math.random() >= 0.7) {
                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = dev.deepnight.DeepNight.PETS.get(dev.deepnight.Species.DAWN_FIRE_DRAGON).get().create(_level);
                        entityToSpawn.moveTo(
                           entityiterator.getX() + entityiterator.getLookAngle().x * -25.0,
                           entityiterator.getY() + 30.0,
                           entityiterator.getZ() + entityiterator.getLookAngle().z * -25.0,
                           entityiterator.getYRot(),
                           0.0F
                        );
                        entityToSpawn.setYBodyRot(entityiterator.getYRot());
                        entityToSpawn.setYHeadRot(entityiterator.getYRot());
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  } else if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = dev.deepnight.DeepNight.PETS.get(dev.deepnight.Species.DAWN_LIGHTNING_DRAGON).get().create(_level);
                     entityToSpawn.moveTo(
                        entityiterator.getX() + entityiterator.getLookAngle().x * -25.0,
                        entityiterator.getY() + 30.0,
                        entityiterator.getZ() + entityiterator.getLookAngle().z * -25.0,
                        entityiterator.getYRot(),
                        0.0F
                     );
                     entityToSpawn.setYBodyRot(entityiterator.getYRot());
                     entityToSpawn.setYHeadRot(entityiterator.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               }
            }
         } else if (entity.getPersistentData().getDouble("spawn") == 1.0 && !entity.level().isClientSide()) {
            entity.discard();
         }
      }
   }
}
