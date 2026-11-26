package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.GhostprojectileEntity;
import dev.deepnight.source.dawn.entity.ProjectileMagicEntity;
import dev.deepnight.source.dawn.entity.SkeletonlordEntity;
import dev.deepnight.source.dawn.network.SimpleMobsModVariables;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import dev.deepnight.source.SourceDamage;
import dev.deepnight.source.SourceDamage.StunType;

public class ProprojmagicProcedure {
   public static void execute(LevelAccessor world, final double x, final double y, final double z, final Entity entity) {
      if (entity != null) {
         double loop = 0.0;
         double particleAmount = 0.0;
         if (entity.getPersistentData().getDouble("timer") == 0.0) {
            entity.getPersistentData().putDouble("timer", 80.0);
         } else if (entity.getPersistentData().getDouble("timer") == 1.0) {
            if (!entity.level().isClientSide()) {
               entity.discard();
            }
         } else {
            entity.getPersistentData().putDouble("timer", entity.getPersistentData().getDouble("timer") - 1.0);
         }

         if (entity.getPersistentData().getDouble("loop") < 8.0) {
            world.addParticle(ParticleTypes.CLOUD, x, y, z, 0.0, -0.05, 0.0);
            world.addParticle(
               ParticleTypes.PORTAL,
               x + Math.cos((Math.PI / 4) * entity.getPersistentData().getDouble("loop")) * SimpleMobsModVariables.MapVariables.get(world).samradius,
               y + Math.sin((Math.PI / 4) * entity.getPersistentData().getDouble("loop")) * SimpleMobsModVariables.MapVariables.get(world).samradius,
               z + Math.sin((Math.PI / 4) * entity.getPersistentData().getDouble("loop")) * SimpleMobsModVariables.MapVariables.get(world).samradius,
               0.0,
               0.0,
               0.0
            );
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
                  public void tick_ProprojmagicProcedure_1(ServerTickEvent event) {
                     if (event.phase == Phase.END) {
                        this.ticks++;
                        if (this.ticks >= this.waitTicks) {
                           this.run();
                        }
                     }
                  }

                  private void run() {
                     this.world
                        .addParticle(
                           ParticleTypes.PORTAL,
                           x
                              + Math.cos((Math.PI / 4) * entity.getPersistentData().getDouble("loop"))
                                 * SimpleMobsModVariables.MapVariables.get(this.world).samradius,
                           y
                              + Math.sin((Math.PI / 4) * entity.getPersistentData().getDouble("loop"))
                                 * SimpleMobsModVariables.MapVariables.get(this.world).samradius,
                           z
                              + Math.sin((Math.PI / 4) * entity.getPersistentData().getDouble("loop"))
                                 * SimpleMobsModVariables.MapVariables.get(this.world).samradius,
                           0.0,
                           0.0,
                           0.0
                        );
                     MinecraftForge.EVENT_BUS.unregister(this);
                  }
               })
               .start(world, 10);
            if (entity.getPersistentData().getDouble("loop") == 7.0) {
               entity.getPersistentData().putDouble("loop", 0.0);
            } else {
               entity.getPersistentData().putDouble("loop", entity.getPersistentData().getDouble("loop") + 1.0);
            }
         }

         entity.setDeltaMovement(
            new Vec3(
               Math.sin(Math.toRadians(entity.getYRot() + 180.0F)), Math.sin(Math.toRadians(entity.getXRot())), Math.cos(Math.toRadians(entity.getYRot()))
            )
         );
         Vec3 _center = new Vec3(x, y, z);

         for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.0), e -> true)
            .stream()
            .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
            .collect(Collectors.toList())) {
            if (!(entityiterator instanceof ProjectileMagicEntity)
               && !(entityiterator instanceof SkeletonlordEntity)
               && !(entityiterator instanceof GhostprojectileEntity)) {
               SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                  .setInitialPosition(entity.position())
                  .setStunType(StunType.SHORT)
                  .setImpact(2.0F)
                  .setArmorNegation(0.4F);
               entityiterator.hurt((DamageSource)dmgSrc, 8.0F);
            }
         }

         if (entity.onGround() && !entity.level().isClientSide()) {
            entity.discard();
         }
      }
   }
}
