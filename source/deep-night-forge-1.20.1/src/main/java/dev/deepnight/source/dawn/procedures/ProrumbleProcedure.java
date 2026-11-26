package dev.deepnight.source.dawn.procedures;

import dev.deepnight.source.dawn.entity.AirEntity;
import dev.deepnight.source.dawn.entity.AsharmorFireEntity;
import dev.deepnight.source.dawn.entity.AsharmorIceEntity;
import dev.deepnight.source.dawn.entity.BloodSplash1Entity;
import dev.deepnight.source.dawn.entity.BloodSplash2Entity;
import dev.deepnight.source.dawn.entity.ClawMarkEntity;
import dev.deepnight.source.dawn.entity.DragonSmokeEntity;
import dev.deepnight.source.dawn.entity.ElementalFlash1Entity;
import dev.deepnight.source.dawn.entity.ElementalFlash2Entity;
import dev.deepnight.source.dawn.entity.ElementalFlash3Entity;
import dev.deepnight.source.dawn.entity.ElementalGrabEntity;
import dev.deepnight.source.dawn.entity.ElementalShieldEntity;
import dev.deepnight.source.dawn.entity.ElementalSpawnEntity;
import dev.deepnight.source.dawn.entity.HerobrineFlashEntity;
import dev.deepnight.source.dawn.entity.Herobrinese2Entity;
import dev.deepnight.source.dawn.entity.Herobrinese3Entity;
import dev.deepnight.source.dawn.entity.HerobrineseEntity;
import dev.deepnight.source.dawn.entity.LosteyeEffect1Entity;
import dev.deepnight.source.dawn.entity.LosteyeEffect3Entity;
import dev.deepnight.source.dawn.entity.NotchFlash5Entity;
import dev.deepnight.source.dawn.entity.NotchStompeffectEntity;
import dev.deepnight.source.dawn.entity.Notchflash1Entity;
import dev.deepnight.source.dawn.entity.Notchflash2Entity;
import dev.deepnight.source.dawn.entity.Notchflash3Entity;
import dev.deepnight.source.dawn.entity.Notchflash4Entity;
import dev.deepnight.source.dawn.entity.Notchgroundeffect1Entity;
import dev.deepnight.source.dawn.entity.Notchgroundeffect2Entity;
import dev.deepnight.source.dawn.entity.Notchgroundeffect3Entity;
import dev.deepnight.source.dawn.entity.Notchgroundeffect4Entity;
import dev.deepnight.source.dawn.entity.RumbleEntity;
import dev.deepnight.source.dawn.entity.SentinelFlashEntity;
import dev.deepnight.source.dawn.entity.ShogunateEffect5Entity;
import dev.deepnight.source.dawn.entity.ShogunateEffect6Entity;
import dev.deepnight.source.dawn.entity.ShogunateEffect7Entity;
import dev.deepnight.source.dawn.entity.ShogunateEffect8Entity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ProrumbleProcedure {
   public static void execute(LevelAccessor world, final Entity entity) {
      if (entity != null) {
         entity.setDeltaMovement(new Vec3(0.0, 0.0, 0.0));
         if (entity instanceof RumbleEntity) {
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
               public void tick_ProrumbleProcedure_1(ServerTickEvent event) {
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
            }).start(world, 46);
         } else if (entity instanceof BloodSplash1Entity
            || entity instanceof BloodSplash2Entity
            || entity instanceof AirEntity
            || entity instanceof LosteyeEffect1Entity
            || entity instanceof Notchflash4Entity
            || entity instanceof ElementalSpawnEntity) {
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
               public void tick_ProrumbleProcedure_2(ServerTickEvent event) {
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
            }).start(world, 25);
         } else if (entity instanceof Herobrinese2Entity
            || entity instanceof ShogunateEffect8Entity
            || entity instanceof ShogunateEffect7Entity
            || entity instanceof ShogunateEffect6Entity
            || entity instanceof AsharmorFireEntity
            || entity instanceof AsharmorIceEntity
            || entity instanceof ElementalShieldEntity) {
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
               public void tick_ProrumbleProcedure_3(ServerTickEvent event) {
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
            }).start(world, 35);
         } else if (entity instanceof LosteyeEffect3Entity
            || entity instanceof Notchflash1Entity
            || entity instanceof NotchFlash5Entity
            || entity instanceof Notchflash2Entity
            || entity instanceof ElementalFlash2Entity
            || entity instanceof ElementalFlash1Entity
            || entity instanceof ElementalFlash3Entity
            || entity instanceof Herobrinese3Entity
            || entity instanceof HerobrineseEntity) {
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
               public void tick_ProrumbleProcedure_4(ServerTickEvent event) {
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
            }).start(world, 20);
         } else if (entity instanceof Notchgroundeffect1Entity
            || entity instanceof Notchgroundeffect2Entity
            || entity instanceof Notchgroundeffect3Entity
            || entity instanceof Notchgroundeffect4Entity) {
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
               public void tick_ProrumbleProcedure_5(ServerTickEvent event) {
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
         } else if (entity instanceof NotchStompeffectEntity
            || entity instanceof ShogunateEffect5Entity
            || entity instanceof ClawMarkEntity
            || entity instanceof DragonSmokeEntity
            || entity instanceof SentinelFlashEntity) {
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
               public void tick_ProrumbleProcedure_6(ServerTickEvent event) {
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
            }).start(world, 16);
         } else if (entity instanceof Notchflash3Entity || entity instanceof HerobrineFlashEntity) {
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
               public void tick_ProrumbleProcedure_7(ServerTickEvent event) {
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
            }).start(world, 12);
         } else if (entity instanceof ElementalGrabEntity) {
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
               public void tick_ProrumbleProcedure_8(ServerTickEvent event) {
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
            }).start(world, 50);
         }
      }
   }
}
