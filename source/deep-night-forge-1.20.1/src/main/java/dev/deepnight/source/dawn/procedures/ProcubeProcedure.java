package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import dev.deepnight.source.SourceDamage;
import dev.deepnight.source.SourceDamage.StunType;

public class ProcubeProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (!world.isClientSide()) {
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(15.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator.getPersistentData().getBoolean("cubemarked")) {
                  entity.setDeltaMovement(
                     new Vec3(
                        (entityiterator.getX() - entity.getX()) / 4.0,
                        entityiterator.getY() + 1.0 - entity.getY(),
                        (entityiterator.getZ() - entity.getZ()) / 4.0
                     )
                  );
               }
            }

            _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator.getPersistentData().getBoolean("cubemarked")) {
                  SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                     .setInitialPosition(entity.position())
                     .setStunType(StunType.SHORT)
                     .setImpact(10.0F)
                     .setArmorNegation(1.0F);
                  entityiterator.hurt((DamageSource)dmgSrc, 12.0F);
                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.EXPLOSION, x, y, z, 10, 1.0, 1.0, 1.0, 0.5);
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.zombie.break_wooden_door"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.zombie.break_wooden_door"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  if (!entity.level().isClientSide()) {
                     entity.discard();
                  }
               }
            }

            if (entity.getPersistentData().getDouble("a") == 0.0) {
               entity.getPersistentData().putDouble("a", 60.0);
            } else {
               entity.getPersistentData().putDouble("a", entity.getPersistentData().getDouble("a") - 1.0);
            }

            if (entity.getPersistentData().getDouble("a") == 1.0 && !entity.level().isClientSide()) {
               entity.discard();
            }
         }
      }
   }
}
