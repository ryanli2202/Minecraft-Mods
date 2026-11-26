package dev.deepnight.source.dawn.procedures;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PronogravityProcedure {
   public static void execute(LevelAccessor world, final Entity entity) {
      if (entity != null) {
         entity.getPersistentData().putDouble("loop", 0.0);
         entity.setNoGravity(true);
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
            public void tick_PronogravityProcedure_1(ServerTickEvent event) {
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
         if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
            _entity.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 100, 1, false, false));
         }
      }
   }
}
