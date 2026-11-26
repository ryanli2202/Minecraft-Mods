package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.init.SimpleMobsModParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import dev.deepnight.source.SourceDamage;
import dev.deepnight.source.SourceDamage.StunType;

public class ProtthunderProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (!world.isClientSide()) {
            if (Math.random() >= 0.95 && world instanceof ServerLevel _level) {
               _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.DRAGON_THUNDER_LIT.get(), x, y + 1.0, z, 1, 1.0, 1.0, 1.0, 1.0);
            }

            if (entity.isInWater() && entity instanceof LivingEntity _entity) {
               _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10, 4, false, false));
            }

            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                  SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                     .setInitialPosition(entity.position())
                     .setStunType(StunType.LONG)
                     .setImpact(0.5F)
                     .setArmorNegation(25.0F);
                  entityiterator.hurt((DamageSource)dmgSrc, 6.0F);
                  if (!entity.level().isClientSide()) {
                     entity.discard();
                  }
               }
            }

            if (entity.getPersistentData().getDouble("tick") == 0.0) {
               entity.getPersistentData().putDouble("tick", 70.0);
            } else {
               entity.getPersistentData().putDouble("tick", entity.getPersistentData().getDouble("tick") - 1.0);
            }

            if (entity.getPersistentData().getDouble("tick") == 1.0 && !entity.level().isClientSide()) {
               entity.discard();
            }
         }
      }
   }
}
