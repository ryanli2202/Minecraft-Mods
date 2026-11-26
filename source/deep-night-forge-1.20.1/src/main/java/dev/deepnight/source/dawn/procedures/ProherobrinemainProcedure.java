package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.HerobrineFearEntity;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class ProherobrinemainProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (entity.isAlive()) {
            if (!entity.getPersistentData().getBoolean("Stage2")) {
               ProherobrineProcedure.execute(world, x, y, z, entity);
            } else {
               Proherobrine2Procedure.execute(world, x, y, z, entity);
            }
         }

         entity.getPersistentData().putDouble("hf", 0.0);
         Vec3 _center = new Vec3(x, y, z);

         for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(64.0), e -> true)
            .stream()
            .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
            .collect(Collectors.toList())) {
            if (entityiterator instanceof HerobrineFearEntity) {
               entity.getPersistentData().putDouble("hf", entity.getPersistentData().getDouble("hf") + 1.0);
            }

            if ((entityiterator instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MobEffects.CONFUSION) : false)
               && entity.getPersistentData().getDouble("hf") <= 2.0
               && Math.random() >= 0.98) {
               if (world instanceof ServerLevel _level) {
                  Entity entityToSpawn = new HerobrineFearEntity(SimpleMobsModEntities.HEROBRINE_FEAR.get(), _level);
                  entityToSpawn.moveTo(
                     entityiterator.getX() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -15, 15),
                     entityiterator.getY(),
                     entityiterator.getZ() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -15, 15),
                     Mth.nextInt(net.minecraft.util.RandomSource.create(), 0, 360),
                     0.0F
                  );
                  entityToSpawn.setYBodyRot(Mth.nextInt(net.minecraft.util.RandomSource.create(), 0, 360));
                  entityToSpawn.setYHeadRot(Mth.nextInt(net.minecraft.util.RandomSource.create(), 0, 360));
                  if (entityToSpawn instanceof Mob _mobToSpawn) {
                     _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                  }

                  world.addFreshEntity(entityToSpawn);
               }

               if (world instanceof Level _level) {
                  if (!_level.isClientSide()) {
                     _level.playSound(
                        null,
                        BlockPos.containing(x, y, z),
                        dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.ambient"),
                        SoundSource.NEUTRAL,
                        3.0F,
                        1.0F
                     );
                  } else {
                     _level.playLocalSound(
                        x,
                        y,
                        z,
                        dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.ambient"),
                        SoundSource.NEUTRAL,
                        3.0F,
                        1.0F,
                        false
                     );
                  }
               }
            }
         }

         if (!entity.getPersistentData().getBoolean("Aggro")) {
            if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
               _entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 5, 3, false, false));
            }
         } else if (entity.getPersistentData().getBoolean("Far")) {
            if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
               _entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 5, 1, false, false));
            }
         } else if (entity.getPersistentData().getBoolean("Mid") && entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
            _entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 5, 0, false, false));
         }
      }
   }
}
