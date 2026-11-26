package dev.deepnight.source.dawn.procedures;


import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.IceDragonProjectileEntity;
import dev.deepnight.source.dawn.init.SimpleMobsModParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.block.Blocks;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class ProicedragprojProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, final Entity entity) {
      if (entity != null) {
         double i = 0.0;
         double j = 0.0;
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
            if (world instanceof ServerLevel _level) {
               _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.FROST_2.get(), x, y, z, 2, 1.0, 1.0, 1.0, 0.1);
            }

            if (world instanceof ServerLevel _level) {
               _level.sendParticles(ParticleTypes.SNOWFLAKE, x, y, z, 2, 1.0, 1.0, 1.0, 0.1);
            }

            entity.setDeltaMovement(
               new Vec3(
                  Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180.0)) * 1.6,
                  Math.sin(Math.toRadians(0.0 - entity.getPersistentData().getDouble("pitch"))) * 3.0,
                  Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * 1.6
               )
            );
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (!entityiterator.getPersistentData().getBoolean("icedragon") && !(entityiterator instanceof IceDragonProjectileEntity)) {
                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.EXPLOSION, x, y, z, 2, 2.0, 2.0, 2.0, 0.1);
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.FROST_2.get(), x, y, z, 20, 3.0, 3.0, 3.0, 0.3);
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.SNOWYCLOUD.get(), x, y, z, 30, 3.0, 3.0, 3.0, 0.3);
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.FIREWORK, x, y, z, 10, 1.0, 1.0, 1.0, 1.0);
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("block.glass.break"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("block.glass.break"), SoundSource.NEUTRAL, 2.0F, 1.0F, false
                        );
                     }
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.dragon_fireball.explode"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           2.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.dragon_fireball.explode"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           2.0F,
                           false
                        );
                     }
                  }

                  if (entityiterator instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(dev.deepnight.source.SourceStun.FROZEN.get(), 60, 0, false, false));
                  }

                  entityiterator.hurt(new DamageSource(entityiterator.level().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(net.minecraft.world.damagesource.DamageTypes.GENERIC), entity), 10.0F);
                  if (!entity.level().isClientSide()) {
                     entity.discard();
                  }
               }
            }

            if (world.getBlockState(BlockPos.containing(x, y - 1.0, z)).canOcclude()
               || world.getBlockState(BlockPos.containing(x, y - 1.0, z)).is(net.minecraft.tags.BlockTags.LEAVES)) {
               if (world instanceof ServerLevel _level) {
                  _level.sendParticles(ParticleTypes.EXPLOSION, x, y, z, 2, 2.0, 2.0, 2.0, 0.1);
               }

               if (world instanceof ServerLevel _level) {
                  _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.FROST_2.get(), x, y, z, 20, 3.0, 3.0, 3.0, 0.3);
               }

               if (world instanceof ServerLevel _level) {
                  _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.SNOWYCLOUD.get(), x, y, z, 30, 3.0, 3.0, 3.0, 0.3);
               }

               if (world instanceof ServerLevel _level) {
                  _level.sendParticles(ParticleTypes.FIREWORK, x, y, z, 10, 1.0, 1.0, 1.0, 1.0);
               }

               if (world instanceof Level _level) {
                  if (!_level.isClientSide()) {
                     _level.playSound(
                        null,
                        BlockPos.containing(x, y, z),
                        dev.deepnight.SafeSounds.resolve("block.glass.break"),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F
                     );
                  } else {
                     _level.playLocalSound(
                        x, y, z, dev.deepnight.SafeSounds.resolve("block.glass.break"), SoundSource.NEUTRAL, 1.0F, 1.0F, false
                     );
                  }
               }

               _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.5), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (!entityiterator.getPersistentData().getBoolean("icedragon") && !(entityiterator instanceof IceDragonProjectileEntity)) {
                     if (entityiterator instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(dev.deepnight.source.SourceStun.FROZEN.get(), 60, 0, false, false));
                     }

                     entityiterator.hurt(new DamageSource(entityiterator.level().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(net.minecraft.world.damagesource.DamageTypes.GENERIC), entity), 10.0F);
                  }
               }

               if (world instanceof Level _level && !_level.isClientSide()) {
                  _level.explode(null, x, y, z, 4.0F, net.minecraft.world.level.Level.ExplosionInteraction.MOB);
               }

               if (world.getBlockState(BlockPos.containing(x, y - 1.0, z)).canOcclude() && Math.random() > 0.4) {
                  i = -1.0;
                  j = -1.0;

                  while (i < 2.0) {
                     while (j < 2.0) {
                        world.setBlock(BlockPos.containing(x + i, y, z + j), Blocks.ICE.defaultBlockState(), 3);
                        j++;
                     }

                     i++;
                  }
               }

               if (!entity.level().isClientSide()) {
                  entity.discard();
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
               public void tick_ProicedragprojProcedure_1(ServerTickEvent event) {
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
            }).start(world, 100);
         }
      }
   }
}
