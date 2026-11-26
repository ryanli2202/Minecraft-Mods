package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ProcaveddeathProcedure {
   public static void execute(LevelAccessor world, final double x, final double y, final double z) {
      Vec3 _center = new Vec3(x, y, z);

      for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(30.0), e -> true)
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
                  "/dc removeWaypoint @p \"Extermination Request - Cave Dweller\""
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
            public void tick_ProcaveddeathProcedure_1(ServerTickEvent event) {
               if (event.phase == Phase.END) {
                  this.ticks++;
                  if (this.ticks >= this.waitTicks) {
                     this.run();
                  }
               }
            }

            private void run() {
               if (this.world instanceof ServerLevel _level) {
                  _level.getServer()
                     .getCommands()
                     .performPrefixedCommand(
                        new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null)
                           .withSuppressedOutput(),
                        "/particle minecraft:dust 0 0 0 2 ~ ~ ~ 0.5 0.7 0.5 0.1 100"
                     );
               }

               MinecraftForge.EVENT_BUS.unregister(this);
            }
         })
         .start(world, 70);
   }
}
