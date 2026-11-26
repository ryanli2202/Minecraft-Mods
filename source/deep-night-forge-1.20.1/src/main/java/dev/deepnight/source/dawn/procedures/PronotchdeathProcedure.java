package dev.deepnight.source.dawn.procedures;

import dev.deepnight.source.dawn.entity.ElementalDeityEntity;
import dev.deepnight.source.dawn.entity.ElementalSpawnEntity;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class PronotchdeathProcedure {
   public static void execute(LevelAccessor world, final double x, final double y, final double z, final Entity entity) {
      if (entity != null) {
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
            public void tick_PronotchdeathProcedure_1(ServerTickEvent event) {
               if (event.phase == Phase.END) {
                  this.ticks++;
                  if (this.ticks >= this.waitTicks) {
                     this.run();
                  }
               }
            }

            private void run() {
               if (this.world instanceof ServerLevel _level) {
                  _level.sendParticles(ParticleTypes.EXPLOSION, x, y, z, 5, 1.0, 1.0, 1.0, 0.4);
               }

               if (this.world instanceof ServerLevel _level) {
                  _level.sendParticles(ParticleTypes.FLASH, x, y, z, 1, 0.0, 0.0, 0.0, 0.4);
               }

               MinecraftForge.EVENT_BUS.unregister(this);
            }
         }).start(world, 80);
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
            public void tick_PronotchdeathProcedure_2(ServerTickEvent event) {
               if (event.phase == Phase.END) {
                  this.ticks++;
                  if (this.ticks >= this.waitTicks) {
                     this.run();
                  }
               }
            }

            private void run() {
               if (this.world instanceof ServerLevel _level) {
                  _level.sendParticles(ParticleTypes.EXPLOSION, x, y, z, 5, 1.0, 1.0, 1.0, 0.4);
               }

               if (this.world instanceof ServerLevel _level) {
                  _level.sendParticles(ParticleTypes.FLASH, x, y, z, 1, 0.0, 0.0, 0.0, 0.4);
               }

               MinecraftForge.EVENT_BUS.unregister(this);
            }
         }).start(world, 86);
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
            public void tick_PronotchdeathProcedure_3(ServerTickEvent event) {
               if (event.phase == Phase.END) {
                  this.ticks++;
                  if (this.ticks >= this.waitTicks) {
                     this.run();
                  }
               }
            }

            private void run() {
               if (this.world instanceof ServerLevel _level) {
                  _level.sendParticles(ParticleTypes.EXPLOSION, x, y, z, 5, 1.0, 1.0, 1.0, 0.4);
               }

               if (this.world instanceof ServerLevel _level) {
                  _level.sendParticles(ParticleTypes.FLASH, x, y, z, 1, 0.0, 0.0, 0.0, 0.4);
               }

               MinecraftForge.EVENT_BUS.unregister(this);
            }
         }).start(world, 88);
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
               public void tick_PronotchdeathProcedure_4(ServerTickEvent event) {
                  if (event.phase == Phase.END) {
                     this.ticks++;
                     if (this.ticks >= this.waitTicks) {
                        this.run();
                     }
                  }
               }

               private void run() {
                  if (this.world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("item.trident.thunder"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           0.5F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("item.trident.thunder"), SoundSource.NEUTRAL, 1.0F, 0.5F, false
                        );
                     }
                  }

                  if (this.world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("item.totem.use"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("item.totem.use"), SoundSource.NEUTRAL, 1.0F, 1.0F, false
                        );
                     }
                  }

                  if (this.world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("block.note_block.chime"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("block.note_block.chime"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  if (this.world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.FLASH, x, y, z, 2, 0.5, 0.5, 0.5, 0.4);
                  }

                  if (this.world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new ElementalSpawnEntity(SimpleMobsModEntities.ELEMENTAL_SPAWN.get(), _level);
                     entityToSpawn.moveTo(x, y, z, this.world.getRandom().nextFloat() * 360.0F, 0.0F);
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(
                           _level, this.world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                        );
                     }

                     this.world.addFreshEntity(entityToSpawn);
                  }

                  if (this.world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new ElementalDeityEntity(SimpleMobsModEntities.ELEMENTAL_DEITY.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, this.world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }
                     this.world.addFreshEntity(entityToSpawn);
                  }

                  if (!entity.level().isClientSide()) {
                     entity.discard();
                  }

                  MinecraftForge.EVENT_BUS.unregister(this);
               }
            })
            .start(world, 90);
      }
   }
}
