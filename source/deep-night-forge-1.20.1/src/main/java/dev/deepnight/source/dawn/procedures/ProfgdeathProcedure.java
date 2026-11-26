package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class ProfgdeathProcedure {
   public static void execute(LevelAccessor world, final double x, final double y, final double z, final Entity entity) {
      if (entity != null) {
         Vec3 _center = new Vec3(x, y, z);

         for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(40.0), e -> true)
            .stream()
            .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
            .collect(Collectors.toList())) {
            if ((entityiterator instanceof Player || entityiterator instanceof ServerPlayer) && world instanceof ServerLevel _level) {
               _level.getServer()
                  .getCommands()
                  .performPrefixedCommand(
                     new CommandSourceStack(
                           CommandSource.NULL,
                           new Vec3(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ()),
                           Vec2.ZERO,
                           _level,
                           4,
                           "",
                           Component.literal(""),
                           _level.getServer(),
                           null
                        )
                        .withSuppressedOutput(),
                     "/dc removeWaypoint @p \"Main Quest - Fire Giant's Altar\""
                  );
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
               public void tick_ProfgdeathProcedure_1(ServerTickEvent event) {
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
                           dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           0.5F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           0.5F,
                           false
                        );
                     }
                  }

                  if (this.world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.CAMPFIRE_COSY_SMOKE, x + entity.getLookAngle().x * -2.0, y, z + entity.getLookAngle().z * -2.0, 50, 3.0, 2.0, 3.0, 0.3
                     );
                  }

                  MinecraftForge.EVENT_BUS.unregister(this);
               }
            })
            .start(world, 59);
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
            public void tick_ProfgdeathProcedure_2(ServerTickEvent event) {
               if (event.phase == Phase.END) {
                  this.ticks++;
                  if (this.ticks >= this.waitTicks) {
                     this.run();
                  }
               }
            }

            private void run() {
               if (this.world instanceof ServerLevel _level) {
                  _level.sendParticles(ParticleTypes.CLOUD, x + entity.getLookAngle().x * -3.0, y, z + entity.getLookAngle().z * -3.0, 300, 5.0, 6.0, 5.0, 0.2);
               }

               MinecraftForge.EVENT_BUS.unregister(this);
            }
         }).start(world, 140);
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
            public void tick_ProfgdeathProcedure_3(ServerTickEvent event) {
               if (event.phase == Phase.END) {
                  this.ticks++;
                  if (this.ticks >= this.waitTicks) {
                     this.run();
                  }
               }
            }

            private void run() {
               if (this.world instanceof ServerLevel _level) {
                  _level.sendParticles(ParticleTypes.CLOUD, x + entity.getLookAngle().x * -3.0, y, z + entity.getLookAngle().z * -3.0, 100, 5.0, 5.0, 5.0, 0.4);
               }

               MinecraftForge.EVENT_BUS.unregister(this);
            }
         }).start(world, 160);
      }
   }
}
