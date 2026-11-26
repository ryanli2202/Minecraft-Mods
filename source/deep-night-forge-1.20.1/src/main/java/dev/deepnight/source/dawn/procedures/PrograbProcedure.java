package dev.deepnight.source.dawn.procedures;


import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;

public class PrograbProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (!world.isClientSide()) {
            if (entity instanceof LivingEntity _entity) {
               _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.STUN.get(), 10, 0, false, false));
            }

            dev.deepnight.source.SourceStun.apply(entity,10);
         }
      }
   }
}
