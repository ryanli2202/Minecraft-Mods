package dev.deepnight.source.dawn.procedures;

import dev.deepnight.source.dawn.init.SimpleMobsModItems;
import dev.deepnight.source.dawn.init.SimpleMobsModParticleTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
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

public class ProdldeathProcedure {
   public static void execute(LevelAccessor world, final double x, final double y, final double z, final Entity entity) {
      if (entity != null) {
         if (world instanceof ServerLevel _level) {
            _level.sendParticles(ParticleTypes.SQUID_INK, x + entity.getLookAngle().x * 7.0, y, z + entity.getLookAngle().z * 7.0, 100, 10.0, 10.0, 10.0, 1.0);
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
               public void tick_ProdldeathProcedure_1(ServerTickEvent event) {
                  if (event.phase == Phase.END) {
                     this.ticks++;
                     if (this.ticks >= this.waitTicks) {
                        this.run();
                     }
                  }
               }

               private void run() {
                  if (this.world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        (SimpleParticleType)SimpleMobsModParticleTypes.DRAGON_THUNDER_LIT.get(),
                        x + entity.getLookAngle().x * 7.0,
                        y + 3.0,
                        z + entity.getLookAngle().z * 7.0,
                        50,
                        15.0,
                        15.0,
                        15.0,
                        0.0
                     );
                  }

                  if (this.world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.SQUID_INK, x + entity.getLookAngle().x * 7.0, y + 3.0, z + entity.getLookAngle().z * 7.0, 200, 15.0, 15.0, 15.0, 1.0
                     );
                  }

                  if (this.world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.CLOUD, x + entity.getLookAngle().x * 7.0, y + 3.0, z + entity.getLookAngle().z * 7.0, 400, 15.0, 15.0, 15.0, 1.0
                     );
                  }

                  if (this.world instanceof Level _level && !_level.isClientSide()) {
                     ItemEntity entityToSpawn = new ItemEntity(
                        _level,
                        x + entity.getLookAngle().x * 20.0,
                        y + 6.0,
                        z + entity.getLookAngle().z * 20.0,
                        new ItemStack(SimpleMobsModItems.DRAGON_LORD_ESSENCE.get())
                     );
                     entityToSpawn.setPickUpDelay(10);
                     entityToSpawn.setUnlimitedLifetime();
                     _level.addFreshEntity(entityToSpawn);
                  }

                  MinecraftForge.EVENT_BUS.unregister(this);
               }
            })
            .start(world, 78);
      }
   }
}
