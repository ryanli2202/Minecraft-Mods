package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.CaveDwellerEntity;
import dev.deepnight.source.dawn.entity.FireDragonAmbientEntity;
import dev.deepnight.source.dawn.entity.Projectile2Entity;
import dev.deepnight.source.dawn.init.SimpleMobsModParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import dev.deepnight.source.SourceDamage;
import dev.deepnight.source.SourceDamage.StunType;

public class Proproject2Procedure {
   public static void execute(LevelAccessor world, double x, double y, double z, final Entity entity) {
      if (entity != null) {
         double loop = 0.0;
         double particleAmount = 0.0;
         if (!entity.getPersistentData().getBoolean("clientlogged")) {
            entity.getPersistentData().putDouble("yaw", entity.getYRot());
            entity.getPersistentData().putDouble("pitch", entity.getXRot());
            entity.getPersistentData().putBoolean("clientlogged", true);
         }

         world.addParticle(
            (SimpleParticleType)SimpleMobsModParticleTypes.FLAME.get(),
            x + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.8, 0.8),
            y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.8, 0.8),
            z + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.8, 0.8),
            Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180.0)) * 0.2,
            Math.sin(Math.toRadians(0.0 - entity.getPersistentData().getDouble("pitch"))) * 0.3,
            Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * 0.2
         );
         world.addParticle(
            (SimpleParticleType)SimpleMobsModParticleTypes.FLAME.get(),
            x + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.5, 0.5),
            y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.5, 0.5),
            z + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.5, 0.5),
            Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180.0)) * 0.2,
            Math.sin(Math.toRadians(0.0 - entity.getPersistentData().getDouble("pitch"))) * 0.3,
            Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * 0.2
         );
         world.addParticle(
            ParticleTypes.FLAME,
            x + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
            y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
            z + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
            Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.3, 0.3),
            Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.2, 0.2),
            Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.3, 0.3)
         );
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
                  Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180.0)) * 2.0,
                  Math.sin(Math.toRadians(0.0 - entity.getPersistentData().getDouble("pitch"))) * 3.0,
                  Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * 2.0
               )
            );
            entity.getPersistentData().putDouble("i", Mth.nextInt(net.minecraft.util.RandomSource.create(), -1, 1));
            entity.getPersistentData().putDouble("j", Mth.nextInt(net.minecraft.util.RandomSource.create(), -1, 1));
            entity.getPersistentData().putDouble("k", Mth.nextInt(net.minecraft.util.RandomSource.create(), -1, 1));
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (!(entityiterator instanceof FireDragonAmbientEntity)
                  && !(entityiterator instanceof CaveDwellerEntity)
                  && !(entityiterator instanceof Projectile2Entity)
                  && !entityiterator.getPersistentData().getBoolean("firegiant")) {
                  SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                     .setInitialPosition(entity.position())
                     .setStunType(StunType.SHORT)
                     .setImpact(1.0F)
                     .setArmorNegation(60.0F);
                  entityiterator.hurt((DamageSource)dmgSrc, 10.0F);
                  entityiterator.setSecondsOnFire(4);
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
               if (entity.onGround()) {
                  world.setBlock(
                     BlockPos.containing(
                        x + entity.getPersistentData().getDouble("i"),
                        y + entity.getPersistentData().getDouble("j") + 1.0,
                        z + entity.getPersistentData().getDouble("k")
                     ),
                     Blocks.FIRE.defaultBlockState(),
                     3
                  );
               }

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
               entity.getPersistentData().putDouble("despawn", 41.0);
               entity.getPersistentData().putDouble("yaw", entity.getYRot());
               entity.getPersistentData().putDouble("pitch", entity.getXRot());
            } else if (entity.getPersistentData().getDouble("despawn") == 40.0) {
               entity.getPersistentData().putBoolean("lock", true);
            } else {
               entity.getPersistentData().putDouble("despawn", entity.getPersistentData().getDouble("despawn") - 1.0);
            }
         }

         (new Object() {
            private int ticks = 0;
            private float waitTicks;
            private LevelAccessor world;

            public void start(LevelAccessor world, int waitTicks) {
               this.waitTicks = waitTicks;
               MinecraftForge.EVENT_BUS.register(this);
               this.world = world;
            }

            @SubscribeEvent
            public void tick_Proproject2Procedure_1(ServerTickEvent event) {
               if (event.phase == Phase.END) {
                  this.ticks++;
                  if (this.ticks >= this.waitTicks) {
                     this.run();
                  }
               }
            }

            private void run() {
               if (!entity.level().isClientSide()) {
                  entity.discard();
               }

               MinecraftForge.EVENT_BUS.unregister(this);
            }
         }).start(world, 60);
      }
   }
}
