package dev.deepnight.source.dawn.procedures;


import dev.deepnight.source.dawn.init.SimpleMobsModParticleTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;

public class ProshockedProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (!world.isClientSide()) {
            if (Math.random() >= 0.8 && world instanceof ServerLevel _level) {
               _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.DRAGON_THUNDER.get(), x, y + 1.0, z, 1, 1.0, 1.0, 1.0, 0.1);
            }

            if (Math.random() >= 0.8 && world instanceof ServerLevel _level) {
               _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.DRAGON_THUNDER_LIT.get(), x, y + 1.0, z, 1, 1.0, 1.0, 1.0, 0.1);
            }

            if (Math.random() >= 0.99 && world instanceof ServerLevel _level) {
               _level.sendParticles(ParticleTypes.FLASH, x, y + 1.0, z, 1, 0.5, 0.5, 0.5, 0.1);
            }

            if (entity instanceof LivingEntity _entity) {
               _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.STUN.get(), 10, 0, false, false));
            }

            if (Math.random() >= 0.5) {
               dev.deepnight.source.SourceStun.apply(entity,10);
            }
         }
      }
   }
}
