package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.MeteorAttackEntity;
import dev.deepnight.source.dawn.entity.MeteorgroundeffectEntity;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.init.SimpleMobsModParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Explosion.BlockInteraction;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import dev.deepnight.source.SourceDamage;
import dev.deepnight.source.SourceDamage.StunType;

public class PrometeorProcedure {
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

         world.addParticle(
            (SimpleParticleType)SimpleMobsModParticleTypes.BURNT_FLAME.get(),
            x + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -3.0, 3.0),
            y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -3.0, 3.0),
            z + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -3.0, 3.0),
            Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180.0)) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.0, 0.2),
            Math.sin(Math.toRadians(0.0 - entity.getPersistentData().getDouble("pitch"))) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.07, 0.13),
            Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.0, 0.2)
         );
         if (!world.isClientSide()) {
            entity.setDeltaMovement(
               new Vec3(
                  Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180.0)) * 1.2,
                  -1.8,
                  Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * 1.2
               )
            );
            entity.getPersistentData().putDouble("i", Mth.nextInt(net.minecraft.util.RandomSource.create(), -1, 1));
            entity.getPersistentData().putDouble("j", Mth.nextInt(net.minecraft.util.RandomSource.create(), -1, 1));
            entity.getPersistentData().putDouble("k", Mth.nextInt(net.minecraft.util.RandomSource.create(), -1, 1));
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (!entityiterator.getPersistentData().getBoolean("dragon_lord")
                  && !(entityiterator instanceof MeteorAttackEntity)
                  && !(false)
                  && !(false)) {
                  SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                     .setInitialPosition(entity.position())
                     .setStunType(StunType.LONG)
                     .setImpact(10.0F)
                     .setArmorNegation(15.0F);
                  entityiterator.hurt((DamageSource)dmgSrc, 40.0F);
                  if (!entity.level().isClientSide()) {
                     entity.discard();
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.EXPLOSION.get(), x, y, z, 20, 2.0, 2.0, 2.0, 0.5);
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 20, 2.0, 2.0, 2.0, 0.5);
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                           SoundSource.NEUTRAL,
                           4.0F,
                           0.5F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                           SoundSource.NEUTRAL,
                           4.0F,
                           0.5F,
                           false
                        );
                     }
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammer"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammer"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 20, 2.0, 2.0, 2.0, 0.5);
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
               _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (!entityiterator.getPersistentData().getBoolean("dragon_lord")
                     && !(entityiterator instanceof MeteorAttackEntity)
                     && !(false)
                     && !(false)) {
                     SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                        .setInitialPosition(entity.position())
                        .setStunType(StunType.LONG)
                        .setImpact(10.0F)
                        .setArmorNegation(15.0F);
                     entityiterator.hurt((DamageSource)dmgSrc, 40.0F);
                  }
               }

               if (world instanceof Level _level) {
                  if (!_level.isClientSide()) {
                     _level.playSound(
                        null,
                        BlockPos.containing(x, y, z),
                        dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                        SoundSource.NEUTRAL,
                        4.0F,
                        0.5F
                     );
                  } else {
                     _level.playLocalSound(
                        x, y, z, dev.deepnight.SafeSounds.resolve("entity.generic.explode"), SoundSource.NEUTRAL, 4.0F, 0.5F, false
                     );
                  }
               }

               if (world instanceof Level _level) {
                  if (!_level.isClientSide()) {
                     _level.playSound(
                        null,
                        BlockPos.containing(x, y, z),
                        dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammer"),
                        SoundSource.NEUTRAL,
                        3.0F,
                        1.0F
                     );
                  } else {
                     _level.playLocalSound(
                        x,
                        y,
                        z,
                        dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammer"),
                        SoundSource.NEUTRAL,
                        3.0F,
                        1.0F,
                        false
                     );
                  }
               }

               if (world instanceof ServerLevel _level) {
                  Entity entityToSpawn = new MeteorgroundeffectEntity(SimpleMobsModEntities.METEORGROUNDEFFECT.get(), _level);
                  entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
                  if (entityToSpawn instanceof Mob _mobToSpawn) {
                     _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                  }

                  world.addFreshEntity(entityToSpawn);
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
               _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (!entityiterator.getPersistentData().getBoolean("dragon_lord") && !(entityiterator instanceof MeteorAttackEntity)) {
                     SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                        .setInitialPosition(entity.position())
                        .setStunType(StunType.LONG)
                        .setImpact(10.0F)
                        .setArmorNegation(15.0F);
                     entityiterator.hurt((DamageSource)dmgSrc, 40.0F);
                  }
               }

               if (world instanceof ServerLevel _level) {
                  _level.sendParticles(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 20, 1.5, 1.5, 1.5, 0.1);
               }

               if (world instanceof Level _level && !_level.isClientSide()) {
                  _level.explode(null, x, y, z, 1.0F, net.minecraft.world.level.Level.ExplosionInteraction.MOB);
               }

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
               entity.getPersistentData().putDouble("despawn", 200.0);
               entity.getPersistentData().putDouble("yaw", entity.getYRot());
               entity.getPersistentData().putDouble("pitch", entity.getXRot());
            } else if (entity.getPersistentData().getDouble("despawn") == 200.0) {
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
               public void tick_PrometeorProcedure_1(ServerTickEvent event) {
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
