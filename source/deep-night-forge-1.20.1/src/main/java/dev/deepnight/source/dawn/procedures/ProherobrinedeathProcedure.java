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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class ProherobrinedeathProcedure {
   public static void execute(LevelAccessor world, final double x, final double y, final double z) {
      if (!(world instanceof ServerLevel)) return;
      if (world instanceof ServerLevel _level) {
         _level.sendParticles(ParticleTypes.SOUL, x, y, z, 30, 0.5, 1.4, 0.5, 0.2);
      }

      if (world instanceof ServerLevel _level) {
         _level.sendParticles(ParticleTypes.SQUID_INK, x, y, z, 10, 0.5, 1.4, 0.5, 0.3);
      }

      if (world instanceof Level _level) {
         if (!_level.isClientSide()) {
            _level.playSound(
               null, BlockPos.containing(x, y, z), dev.deepnight.SafeSounds.resolve("entity.ghast.scream"), SoundSource.NEUTRAL, 2.0F, 0.0F
            );
         } else {
            _level.playLocalSound(
               x, y, z, dev.deepnight.SafeSounds.resolve("entity.ghast.scream"), SoundSource.NEUTRAL, 2.0F, 0.0F, false
            );
         }
      }

      if (world instanceof Level _level) {
         if (!_level.isClientSide()) {
            _level.playSound(
               null,
               BlockPos.containing(x, y, z),
               dev.deepnight.SafeSounds.resolve("entity.enderman.death"),
               SoundSource.NEUTRAL,
               2.0F,
               0.0F
            );
         } else {
            _level.playLocalSound(
               x, y, z, dev.deepnight.SafeSounds.resolve("entity.enderman.death"), SoundSource.NEUTRAL, 2.0F, 0.0F, false
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
         public void tick_ProherobrinedeathProcedure_1(ServerTickEvent event) {
            if (event.phase == Phase.END) {
               this.ticks++;
               if (this.ticks >= this.waitTicks) {
                  this.run();
               }
            }
         }

         private void run() {
            if (this.world instanceof ServerLevel _level) {
               _level.sendParticles(ParticleTypes.SOUL, x, y, z, 80, 1.0, 2.0, 1.0, 0.2);
            }

            MinecraftForge.EVENT_BUS.unregister(this);
         }
      }).start(world, 30);
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
            public void tick_ProherobrinedeathProcedure_2(ServerTickEvent event) {
               if (event.phase == Phase.END) {
                  this.ticks++;
                  if (this.ticks >= this.waitTicks) {
                     this.run();
                  }
               }
            }

            private void run() {
               if ((this.world instanceof Level _lvl ? _lvl.dimension() : Level.OVERWORLD) == Level.NETHER) {
                  this.world.setBlock(BlockPos.containing(x, y + 3.0, z), Blocks.NETHER_PORTAL.defaultBlockState(), 3);
               }

               Vec3 _center = new Vec3(x, y, z);

               for (Entity entityiterator : this.world
                  .getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(64.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator instanceof ServerPlayer _player) {
                     dev.deepnight.source.SourceAdvancements.award(_player, "simple_mobs:the_last_invader");
                  }
               }

               if (this.world instanceof ServerLevel _level) {
                  var reward = new net.minecraft.world.entity.item.ItemEntity(_level, x, y + 2, z,
                     new net.minecraft.world.item.ItemStack(dev.deepnight.source.dawn.init.SimpleMobsModItems.ACCURSED_SWORD.get()));
                  reward.setDefaultPickUpDelay();
                  _level.addFreshEntity(reward);
               }

               if (this.world instanceof Level _level) {
                  if (!_level.isClientSide()) {
                     _level.playSound(
                        null,
                        BlockPos.containing(x, y, z),
                        dev.deepnight.SafeSounds.resolve("entity.enderman.scream"),
                        SoundSource.NEUTRAL,
                        2.0F,
                        0.0F
                     );
                  } else {
                     _level.playLocalSound(
                        x, y, z, dev.deepnight.SafeSounds.resolve("entity.enderman.scream"), SoundSource.NEUTRAL, 2.0F, 0.0F, false
                     );
                  }
               }

               if (this.world instanceof ServerLevel _level) {
                  _level.sendParticles(ParticleTypes.SOUL, x, y, z, 30, 0.5, 1.4, 0.5, 0.3);
               }

               if (this.world instanceof ServerLevel _level) {
                  _level.sendParticles(ParticleTypes.CLOUD, x, y, z, 30, 1.0, 2.0, 1.0, 0.3);
               }

               MinecraftForge.EVENT_BUS.unregister(this);
            }
         })
         .start(world, 60);
   }
}
