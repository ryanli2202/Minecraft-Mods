package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.IceDragonBreathEntity;
import dev.deepnight.source.dawn.entity.WindEntity;
import dev.deepnight.source.dawn.init.SimpleMobsModParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import dev.deepnight.source.SourceDamage;
import dev.deepnight.source.SourceDamage.StunType;

public class ProdragbreathProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         double loop = 0.0;
         double particleAmount = 0.0;
         if (!entity.getPersistentData().getBoolean("clientlogged")) {
            entity.getPersistentData().putDouble("yaw", entity.getYRot());
            entity.getPersistentData().putDouble("pitch", entity.getXRot());
            entity.getPersistentData().putBoolean("clientlogged", true);
         }

         if (Math.random() > 0.6) {
            world.addParticle(
               (SimpleParticleType)SimpleMobsModParticleTypes.FROST_2.get(),
               x + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.8, 0.8),
               y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.8, 0.8),
               z + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.8, 0.8),
               Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180.0)) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.0, 0.2),
               Math.sin(Math.toRadians(0.0 - entity.getPersistentData().getDouble("pitch"))) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.07, 0.26),
               Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.0, 0.2)
            );
         }

         world.addParticle(
            (SimpleParticleType)SimpleMobsModParticleTypes.SNOWYCLOUD.get(),
            x + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
            y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
            z + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
            Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180.0)) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.3, 0.4),
            Math.sin(Math.toRadians(0.0 - entity.getPersistentData().getDouble("pitch"))) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.3, 0.4),
            Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.3, 0.4)
         );
         world.addParticle(
            ParticleTypes.SNOWFLAKE,
            x + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.3, 0.3),
            y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.3, 0.3),
            z + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.3, 0.3),
            Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.1, 0.1),
            Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.1, 0.1),
            Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.1, 0.1)
         );

         for (int index0 = 0; index0 < 2; index0++) {
            world.addParticle(
               (SimpleParticleType)SimpleMobsModParticleTypes.FROST_1.get(),
               x + Mth.nextDouble(net.minecraft.util.RandomSource.create(), z + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.3, 0.3), 1.0),
               y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
               z + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
               Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180.0)) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.0, 0.2),
               Math.sin(Math.toRadians(0.0 - entity.getPersistentData().getDouble("pitch"))) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.07, 0.24),
               Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.0, 0.2)
            );
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
                  Math.sin(Math.toRadians(0.0 - entity.getPersistentData().getDouble("pitch"))) * 1.5,
                  Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * 1.0
               )
            );
            entity.getPersistentData().putDouble("i", Mth.nextInt(net.minecraft.util.RandomSource.create(), -1, 1));
            entity.getPersistentData().putDouble("j", Mth.nextInt(net.minecraft.util.RandomSource.create(), -1, 1));
            entity.getPersistentData().putDouble("k", Mth.nextInt(net.minecraft.util.RandomSource.create(), -1, 1));
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (!entityiterator.getPersistentData().getBoolean("icedragon")
                  && !(entityiterator instanceof IceDragonBreathEntity)
                  && !(entityiterator instanceof WindEntity)) {
                  SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                     .setInitialPosition(entity.position())
                     .setStunType(StunType.SHORT)
                     .setImpact(2.0F)
                     .setArmorNegation(25.0F);
                  entityiterator.hurt((DamageSource)dmgSrc, 6.0F);
                  if (entityiterator instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 1, false, false));
                  }
               }
            }

            if (world.getBlockState(
                  BlockPos.containing(
                     x + entity.getPersistentData().getDouble("i"),
                     y + entity.getPersistentData().getDouble("j"),
                     z + entity.getPersistentData().getDouble("k")
                  )
               )
               .canOcclude()) {
               if (!entity.level().isClientSide()) {
                  entity.discard();
               }
            } else if (world.getBlockState(
                     BlockPos.containing(
                        x + entity.getPersistentData().getDouble("i"),
                        y + entity.getPersistentData().getDouble("j"),
                        z + entity.getPersistentData().getDouble("k")
                     )
                  )
                  .is(net.minecraft.tags.BlockTags.LEAVES)) {
               world.destroyBlock(
                  BlockPos.containing(
                     x + entity.getPersistentData().getDouble("i"),
                     y + entity.getPersistentData().getDouble("j"),
                     z + entity.getPersistentData().getDouble("k")
                  ),
                  false
               );
               if (!entity.level().isClientSide()) {
                  entity.discard();
               }
            }

            if (entity.getPersistentData().getDouble("despawn") == 0.0) {
               entity.getPersistentData().putDouble("despawn", 60.0);
               entity.getPersistentData().putDouble("yaw", entity.getYRot());
               entity.getPersistentData().putDouble("pitch", entity.getXRot());
            } else {
               entity.getPersistentData().putDouble("despawn", entity.getPersistentData().getDouble("despawn") - 1.0);
            }

            if (entity.getPersistentData().getDouble("despawn") == 59.0) {
               entity.getPersistentData().putBoolean("lock", true);
            } else if (entity.getPersistentData().getDouble("despawn") == 1.0 && !entity.level().isClientSide()) {
               entity.discard();
            }
         }
      }
   }
}
