package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.DragonLightningBreathEntity;
import dev.deepnight.source.dawn.init.SimpleMobsModMobEffects;
import dev.deepnight.source.dawn.init.SimpleMobsModParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import dev.deepnight.source.SourceDamage;
import dev.deepnight.source.SourceDamage.StunType;

public class ProDragLightningProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, final Entity entity) {
      if (entity != null) {
         double loop = 0.0;
         double particleAmount = 0.0;
         if (!entity.getPersistentData().getBoolean("clientlogged")) {
            entity.getPersistentData().putDouble("yaw", entity.getYRot());
            entity.getPersistentData().putDouble("pitch", entity.getXRot());
            if (entity.getPersistentData().getDouble("pitch") == 90.0) {
               entity.getPersistentData().putDouble("pitch", 89.0);
            } else if (entity.getPersistentData().getDouble("pitch") == -90.0) {
               entity.getPersistentData().putDouble("pitch", -89.0);
            }

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

         if (Math.random() > 0.8) {
            world.addParticle(
               (SimpleParticleType)SimpleMobsModParticleTypes.DRAGON_THUNDER.get(),
               x + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
               y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
               z + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
               Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180.0)) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.0, 0.2),
               Math.sin(Math.toRadians(0.0 - entity.getPersistentData().getDouble("pitch"))) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.07, 0.13),
               Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.0, 0.2)
            );
         }

         if (Math.random() > 0.9) {
            world.addParticle(
               (SimpleParticleType)SimpleMobsModParticleTypes.DRAGON_THUNDER_LIT.get(),
               x + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -2.0, 2.0),
               y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -2.0, 2.0),
               z + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -2.0, 2.0),
               Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180.0)) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.0, 0.2),
               Math.sin(Math.toRadians(0.0 - entity.getPersistentData().getDouble("pitch"))) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.07, 0.13),
               Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.0, 0.2)
            );
         }

         if (Math.random() > 0.6) {
            world.addParticle(
               (SimpleParticleType)SimpleMobsModParticleTypes.DRAGON_THUNDER_LIT_2.get(),
               x + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
               y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
               z + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
               Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180.0)) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.0, 0.2),
               Math.sin(Math.toRadians(0.0 - entity.getPersistentData().getDouble("pitch"))) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.07, 0.13),
               Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.0, 0.2)
            );
         }

         if (Math.random() > 0.6) {
            world.addParticle(
               ParticleTypes.LARGE_SMOKE,
               x + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.5, 0.5),
               y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.5, 0.5),
               z + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.5, 0.5),
               Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180.0)) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.0, 0.2),
               Math.sin(Math.toRadians(0.0 - entity.getPersistentData().getDouble("pitch"))) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.07, 0.13),
               Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.0, 0.2)
            );
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

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (!entityiterator.getPersistentData().getBoolean("lightningdragon") && !(entityiterator instanceof DragonLightningBreathEntity)) {
                  SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                     .setInitialPosition(entity.position())
                     .setStunType(StunType.SHORT)
                     .setImpact(2.0F)
                     .setArmorNegation(25.0F);
                  entityiterator.hurt((DamageSource)dmgSrc, 8.0F);
                  if (entityiterator instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(SimpleMobsModMobEffects.SHOCK.get(), 10, 0, false, false));
                  }

                  if (!entity.level().isClientSide()) {
                     entity.discard();
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
               entity.getPersistentData().putDouble("despawn", 41.0);
               entity.getPersistentData().putDouble("yaw", entity.getYRot());
               entity.getPersistentData().putDouble("pitch", entity.getXRot());
            } else if (entity.getPersistentData().getDouble("despawn") == 40.0) {
               entity.getPersistentData().putBoolean("lock", true);
            } else {
               entity.getPersistentData().putDouble("despawn", entity.getPersistentData().getDouble("despawn") - 1.0);
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
               public void tick_ProDragLightningProcedure_1(ServerTickEvent event) {
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
            }).start(world, 80);
         }
      }
   }
}
