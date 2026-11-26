package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ProswordautoProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         double loop = 0.0;
         double particleAmount = 0.0;
         double xRadius = 0.0;
         double zRadius = 0.0;
         if (entity.getPersistentData().getBoolean("target")) {
            Entity _ent = entity;
            _ent.setYRot((float)entity.getPersistentData().getDouble("yaw2"));
            _ent.setXRot(entity.getXRot());
            _ent.setYBodyRot(_ent.getYRot());
            _ent.setYHeadRot(_ent.getYRot());
            _ent.yRotO = _ent.getYRot();
            _ent.xRotO = _ent.getXRot();
            if (_ent instanceof LivingEntity _entity) {
               _entity.yBodyRotO = _entity.getYRot();
               _entity.yHeadRotO = _entity.getYRot();
            }
         }

         if (!world.isClientSide()) {
            entity.getPersistentData().putBoolean("target", false);
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(10.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator.getPersistentData().getBoolean("herobrine_mark")) {
                  entity.setDeltaMovement(
                     new Vec3(
                        (entityiterator.getX() + entityiterator.getLookAngle().x - entity.getX()) * 0.12,
                        (entityiterator.getY() + 1.0 - entity.getY()) * 0.12,
                        (entityiterator.getZ() + entityiterator.getLookAngle().z - entity.getZ()) * 0.12
                     )
                  );
                  entity.getPersistentData()
                     .putDouble("atanyaw", Math.toDegrees(Math.atan2(entityiterator.getX() - entity.getX(), entityiterator.getZ() - entity.getZ())));
                  entity.getPersistentData()
                     .putDouble("distance", Math.hypot(Math.abs(entity.getX() - entityiterator.getX()), Math.abs(entity.getZ() - entityiterator.getZ())));
                  if (entity.getPersistentData().getDouble("atanyaw") < 0.0) {
                     entity.getPersistentData().putDouble("yaw2", 0.0 - entity.getPersistentData().getDouble("atanyaw"));
                  }

                  if (entity.getPersistentData().getDouble("atanyaw") >= 0.0) {
                     entity.getPersistentData().putDouble("yaw2", 360.0 - entity.getPersistentData().getDouble("atanyaw"));
                  }

                  entity.getPersistentData().putBoolean("target", true);
               }
            }

            if (entity.getPersistentData().getDouble("timer") == 0.0) {
               entity.getPersistentData().putDouble("timer", 100.0);
               if (world instanceof ServerLevel _level) {
                  _level.sendParticles(ParticleTypes.SQUID_INK, x, y, z, 5, 1.0, 1.0, 1.0, 0.1);
               }
            } else {
               entity.getPersistentData().putDouble("timer", entity.getPersistentData().getDouble("timer") - 1.0);
            }

            if (entity.getPersistentData().getDouble("timer") == 97.0
               || entity.getPersistentData().getDouble("timer") == 85.0
               || entity.getPersistentData().getDouble("timer") == 79.0
               || entity.getPersistentData().getDouble("timer") == 71.0
               || entity.getPersistentData().getDouble("timer") == 62.0
               || entity.getPersistentData().getDouble("timer") == 53.0) {
               _center = new Vec3(x + entity.getLookAngle().x * 0.5, y, z + entity.getLookAngle().z * 0.5);

               for (LivingEntity entityiterator : world.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center).inflate(1.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  DamageSource dmgSrc = entity instanceof LivingEntity ? entity.damageSources().mobAttack((LivingEntity)entity) : entity.damageSources().generic();
                  entityiterator.hurt(dmgSrc, 11.0F);
                  entityiterator.invulnerableTime = 0;
               }
            }

            if (entity.getPersistentData().getDouble("timer") == 30.0) {
               if (world instanceof ServerLevel _level) {
                  _level.sendParticles(ParticleTypes.SQUID_INK, x, y, z, 5, 1.0, 1.0, 1.0, 0.1);
               }

               if (!entity.level().isClientSide()) {
                  entity.discard();
               }
            }
         }
      }
   }
}
