package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.TrackingThunderEntity;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.init.SimpleMobsModMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import dev.deepnight.source.SourceDamage;
import dev.deepnight.source.SourceDamage.StunType;

public class Prothunder3Procedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (!world.isClientSide()) {
            if (entity.getPersistentData().getDouble("tick") == 0.0) {
               entity.getPersistentData().putDouble("tick", 60.0);
               if (world instanceof Level _level) {
                  if (!_level.isClientSide()) {
                     _level.playSound(
                        null,
                        BlockPos.containing(x, y, z),
                        dev.deepnight.SafeSounds.resolve("simple_mobs:electricity_charge"),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F
                     );
                  } else {
                     _level.playLocalSound(
                        x,
                        y,
                        z,
                        dev.deepnight.SafeSounds.resolve("simple_mobs:electricity_charge"),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F,
                        false
                     );
                  }
               }
            } else {
               entity.getPersistentData().putDouble("tick", entity.getPersistentData().getDouble("tick") - 1.0);
            }

            if (entity.getPersistentData().getDouble("tick") == 15.0) {
               Vec3 _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (!entityiterator.getPersistentData().getBoolean("lightningdragon") && !entityiterator.getPersistentData().getBoolean("dragon_lord")) {
                     SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                        .setInitialPosition(entity.position())
                        .setStunType(StunType.SHORT)
                        .setImpact(2.0F)
                        .setArmorNegation(25.0F);
                     entityiterator.hurt((DamageSource)dmgSrc, 20.0F);
                     if (entityiterator instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(SimpleMobsModMobEffects.SHOCK.get(), 30, 0, false, false));
                     }
                  }
               }

               if (world instanceof Level _level) {
                  if (!_level.isClientSide()) {
                     _level.playSound(
                        null,
                        BlockPos.containing(x, y, z),
                        dev.deepnight.SafeSounds.resolve("entity.lightning_bolt.thunder"),
                        SoundSource.NEUTRAL,
                        2.0F,
                        0.1F
                     );
                  } else {
                     _level.playLocalSound(
                        x,
                        y,
                        z,
                        dev.deepnight.SafeSounds.resolve("entity.lightning_bolt.thunder"),
                        SoundSource.NEUTRAL,
                        2.0F,
                        0.1F,
                        false
                     );
                  }
               }

               if (world instanceof ServerLevel _level) {
                  _level.sendParticles(ParticleTypes.FLASH, x, y, z, 10, 1.0, 1.0, 1.0, 0.4);
               }

               if (world instanceof ServerLevel _level) {
                  Entity entityToSpawn = new TrackingThunderEntity(SimpleMobsModEntities.TRACKING_THUNDER.get(), _level);
                  entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
                  if (entityToSpawn instanceof Mob _mobToSpawn) {
                     _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                  }

                  world.addFreshEntity(entityToSpawn);
               }
            } else if (entity.getPersistentData().getDouble("tick") == 1.0 && !entity.level().isClientSide()) {
               entity.discard();
            }
         }
      }
   }
}
