package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.init.SimpleMobsModMobEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class PronotchgrabProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         double loop = 0.0;
         double particleAmount = 0.0;
         double xRadius = 0.0;
         double zRadius = 0.0;
         entity.setDeltaMovement(new Vec3(0.0, 0.0, 0.0));
         Vec3 _center = new Vec3(x, y, z);

         for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
            .stream()
            .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
            .collect(Collectors.toList())) {
            if (entityiterator.getPersistentData().getBoolean("notch_grabbed")) {
               entityiterator.setDeltaMovement(
                  new Vec3(
                     (entity.getX() - entityiterator.getX()) / 4.0,
                     (entity.getY() - entityiterator.getY()) / 4.0,
                     (entity.getZ() - entityiterator.getZ()) / 4.0
                  )
               );
               if (entityiterator instanceof LivingEntity _entity) {
                  _entity.addEffect(new MobEffectInstance(SimpleMobsModMobEffects.GRAB.get(), 20, 1, false, false));
               }
            }

            if ((entityiterator instanceof Player || entityiterator instanceof ServerPlayer) && !entityiterator.getPersistentData().getBoolean("one")) {
               entityiterator.getPersistentData().putBoolean("notch_grabbed", true);
               entity.getPersistentData().putBoolean("one", true);
            }
         }
      }
   }
}
