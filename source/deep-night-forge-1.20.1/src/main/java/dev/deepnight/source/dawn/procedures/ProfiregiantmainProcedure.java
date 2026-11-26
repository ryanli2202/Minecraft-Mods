package dev.deepnight.source.dawn.procedures;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;

public class ProfiregiantmainProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (entity.isAlive()) {
            if (!entity.getPersistentData().getBoolean("Stage2")) {
               ProfiregiantProcedure.execute(world, x, y, z, entity);
            } else {
               Profiregiant2Procedure.execute(world, x, y, z, entity);
            }
         }

         if (entity.getPersistentData().getBoolean("Aggro")
            && (!entity.getPersistentData().getBoolean("Close") || !entity.getPersistentData().getBoolean("Around"))
            && entity instanceof LivingEntity _entity
            && !_entity.level().isClientSide()) {
            _entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20, 1, false, false));
         }
      }
   }
}
