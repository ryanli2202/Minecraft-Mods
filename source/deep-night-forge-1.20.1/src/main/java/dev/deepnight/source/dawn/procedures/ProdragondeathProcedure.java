package dev.deepnight.source.dawn.procedures;

import dev.deepnight.source.dawn.entity.FireDragonEntity;
import dev.deepnight.source.dawn.entity.IceDragonEntity;
import dev.deepnight.source.dawn.entity.LightningDragonEntity;
import dev.deepnight.source.dawn.init.SimpleMobsModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ProdragondeathProcedure {
   public static void execute(LevelAccessor world, final double x, final double y, final double z, Entity entity) {
      if (entity != null) {
         if (entity instanceof FireDragonEntity) {
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
               public void tick_ProdragondeathProcedure_1(ServerTickEvent event) {
                  if (event.phase == Phase.END) {
                     this.ticks++;
                     if (this.ticks >= this.waitTicks) {
                        this.run();
                     }
                  }
               }

               private void run() {
                  if (this.world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.CLOUD, x, y, z, 50, 5.0, 5.0, 5.0, 0.2);
                  }

                  if (this.world instanceof Level _level && !_level.isClientSide()) {
                     ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(SimpleMobsModItems.FIREDRAGON_HEART.get()));
                     entityToSpawn.setPickUpDelay(10);
                     _level.addFreshEntity(entityToSpawn);
                  }

                  MinecraftForge.EVENT_BUS.unregister(this);
               }
            }).start(world, 80);
         } else if (entity instanceof IceDragonEntity) {
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
               public void tick_ProdragondeathProcedure_2(ServerTickEvent event) {
                  if (event.phase == Phase.END) {
                     this.ticks++;
                     if (this.ticks >= this.waitTicks) {
                        this.run();
                     }
                  }
               }

               private void run() {
                  if (this.world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.CLOUD, x, y, z, 60, 6.0, 6.0, 6.0, 0.2);
                  }

                  if (this.world instanceof Level _level && !_level.isClientSide()) {
                     ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(SimpleMobsModItems.ICEDRAGON_HEART.get()));
                     entityToSpawn.setPickUpDelay(10);
                     _level.addFreshEntity(entityToSpawn);
                  }

                  MinecraftForge.EVENT_BUS.unregister(this);
               }
            }).start(world, 80);
         } else if (entity instanceof LightningDragonEntity) {
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
               public void tick_ProdragondeathProcedure_3(ServerTickEvent event) {
                  if (event.phase == Phase.END) {
                     this.ticks++;
                     if (this.ticks >= this.waitTicks) {
                        this.run();
                     }
                  }
               }

               private void run() {
                  if (this.world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.CLOUD, x, y, z, 60, 6.0, 6.0, 6.0, 0.2);
                  }

                  if (this.world instanceof Level _level && !_level.isClientSide()) {
                     ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(SimpleMobsModItems.LIGHTNINGDRAGON_HEART.get()));
                     entityToSpawn.setPickUpDelay(10);
                     _level.addFreshEntity(entityToSpawn);
                  }

                  MinecraftForge.EVENT_BUS.unregister(this);
               }
            }).start(world, 80);
         }
      }
   }
}
