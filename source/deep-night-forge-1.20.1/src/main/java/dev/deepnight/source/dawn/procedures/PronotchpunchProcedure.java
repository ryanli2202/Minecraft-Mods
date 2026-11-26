package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.NotchBossEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PronotchpunchProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, final Entity entity) {
      if (entity != null) {
         Vec3 _center = new Vec3(x, y, z);

         for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
            .stream()
            .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
            .collect(Collectors.toList())) {
            if (entityiterator instanceof NotchBossEntity) {
               Entity _ent = entity;
               _ent.teleportTo(
                  entityiterator.getX() + entityiterator.getLookAngle().x * 1.0,
                  entityiterator.getY(),
                  entityiterator.getZ() + entityiterator.getLookAngle().z * 1.0
               );
               if (_ent instanceof ServerPlayer _serverPlayer) {
                  _serverPlayer.connection
                     .teleport(
                        entityiterator.getX() + entityiterator.getLookAngle().x * 1.0,
                        entityiterator.getY(),
                        entityiterator.getZ() + entityiterator.getLookAngle().z * 1.0,
                        _ent.getYRot(),
                        _ent.getXRot()
                     );
               }

               _ent = entity;
               _ent.setYRot(entityiterator.getYRot());
               _ent.setXRot(entityiterator.getXRot());
               _ent.setYBodyRot(_ent.getYRot());
               _ent.setYHeadRot(_ent.getYRot());
               _ent.yRotO = _ent.getYRot();
               _ent.xRotO = _ent.getXRot();
               if (_ent instanceof LivingEntity _entity) {
                  _entity.yBodyRotO = _entity.getYRot();
                  _entity.yHeadRotO = _entity.getYRot();
               }
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
            public void tick_PronotchpunchProcedure_1(ServerTickEvent event) {
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
         }).start(world, 70);
      }
   }
}
