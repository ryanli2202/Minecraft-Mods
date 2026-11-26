package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.LavaBallEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import dev.deepnight.source.SourceDamage;
import dev.deepnight.source.SourceDamage.StunType;

public class ProlavaballProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, final Entity entity) {
      if (entity != null) {
         double i = 0.0;
         double j = 0.0;
         Vec3 _center = new Vec3(x, y, z);

         for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
            .stream()
            .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
            .collect(Collectors.toList())) {
            if (!(entityiterator instanceof LavaBallEntity) && !entityiterator.getPersistentData().getBoolean("firegiant")) {
               if (world instanceof ServerLevel _level) {
                  _level.sendParticles(ParticleTypes.EXPLOSION, x, y, z, 5, 2.0, 2.0, 2.0, 1.0);
               }

               if (world instanceof ServerLevel _level) {
                  _level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y, z, 8, 2.0, 2.0, 2.0, 1.0);
               }

               if (world instanceof ServerLevel _level) {
                  _level.sendParticles(ParticleTypes.FIREWORK, x, y, z, 1, 1.0, 1.0, 1.0, 1.0);
               }

               SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                  .setInitialPosition(entity.position())
                  .setStunType(StunType.LONG)
                  .setImpact(4.0F)
                  .setArmorNegation(35.0F);
               entityiterator.hurt((DamageSource)dmgSrc, 18.0F);
               entityiterator.setSecondsOnFire(6);
               if (!entity.level().isClientSide()) {
                  entity.discard();
               }
            }
         }

         if (entity.onGround()) {
            if (world instanceof ServerLevel _level) {
               _level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y, z, 8, 2.0, 2.0, 2.0, 1.0);
            }

            if (world instanceof ServerLevel _level) {
               _level.sendParticles(ParticleTypes.FIREWORK, x, y, z, 1, 1.0, 1.0, 1.0, 1.0);
            }

            if (world instanceof ServerLevel _level) {
               _level.sendParticles(ParticleTypes.EXPLOSION, x, y, z, 5, 2.0, 2.0, 2.0, 1.0);
            }

            if (world.getBlockState(BlockPos.containing(x, y - 1.0, z)).canOcclude() && Math.random() > 0.4) {
               i = -1.0;
               j = -1.0;

               while (i < 2.0) {
                  while (j < 2.0) {
                     world.setBlock(BlockPos.containing(x + i, y, z + j), Blocks.FIRE.defaultBlockState(), 3);
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
            public void tick_ProlavaballProcedure_1(ServerTickEvent event) {
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
      }
   }
}
