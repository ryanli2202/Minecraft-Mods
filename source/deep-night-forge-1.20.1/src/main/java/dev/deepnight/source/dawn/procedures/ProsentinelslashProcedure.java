package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.SentinelKnightEntity;
import dev.deepnight.source.dawn.init.SimpleMobsModParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import dev.deepnight.source.SourceDamage;
import dev.deepnight.source.SourceDamage.StunType;

public class ProsentinelslashProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         double loop = 0.0;
         double particleAmount = 0.0;
         double i = 0.0;
         if (!entity.getPersistentData().getBoolean("clientlogged")) {
            entity.getPersistentData().putDouble("yaw", entity.getYRot());
            entity.getPersistentData().putDouble("pitch", entity.getXRot());
            entity.getPersistentData().putBoolean("clientlogged", true);
         }

         if (entity.getPersistentData().getBoolean("lock")) {
            Entity _ent = entity;
            _ent.setYRot((float)entity.getPersistentData().getDouble("yaw"));
            _ent.setXRot((float)entity.getPersistentData().getDouble("pitch"));
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
            entity.setDeltaMovement(
               new Vec3(
                  Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180.0)) * 1.0,
                  Math.sin(Math.toRadians(0.0 - entity.getPersistentData().getDouble("pitch"))) * 0.2,
                  Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * 1.0
               )
            );
            if (world.getBlockState(BlockPos.containing(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z)).canOcclude()
               || world.getBlockState(BlockPos.containing(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z)).is(net.minecraft.tags.BlockTags.LEAVES)) {
               if (world instanceof ServerLevel _level) {
                  _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.SPARKLE.get(), x, y, z, 20, 1.0, 1.0, 1.0, 0.4);
               }

               if (!entity.level().isClientSide()) {
                  entity.discard();
               }
            }

            loop = -3.0;

            while (i <= 3.0) {
               Vec3 _center = new Vec3(x, y + i, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator != entity && !(entityiterator instanceof SentinelKnightEntity)) {
                     SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                        .setInitialPosition(entity.position())
                        .setStunType(StunType.LONG)
                        .setImpact(1.0F)
                        .setArmorNegation(70.0F);
                     entityiterator.hurt((DamageSource)dmgSrc, 12.0F);
                  }
               }

               i++;
            }

            if (entity.getPersistentData().getDouble("despawn") == 0.0) {
               entity.getPersistentData().putDouble("despawn", 17.0);
               entity.getPersistentData().putDouble("yaw", entity.getYRot());
               entity.getPersistentData().putDouble("pitch", entity.getXRot());
            } else {
               entity.getPersistentData().putDouble("despawn", entity.getPersistentData().getDouble("despawn") - 1.0);
            }

            if (entity.getPersistentData().getDouble("despawn") == 16.0) {
               entity.getPersistentData().putBoolean("lock", true);
            } else if (entity.getPersistentData().getDouble("despawn") == 1.0 && !entity.level().isClientSide()) {
               entity.discard();
            }
         }
      }
   }
}
