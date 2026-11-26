package dev.deepnight.source.dawn.procedures;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class ProchainedProcedure {
   public static void execute(Entity entity) {
      if (entity != null) {
         entity.setDeltaMovement(new Vec3(0.0, 0.0, 0.0));
         if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
            _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 5));
         }
      }
   }
}
