package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.DragonLordEntity;
import dev.deepnight.source.dawn.entity.FireDragonAmbientEntity;
import dev.deepnight.source.dawn.entity.FireDragonEntity;
import dev.deepnight.source.dawn.entity.IceDragonAmbientEntity;
import dev.deepnight.source.dawn.entity.IceDragonEntity;
import dev.deepnight.source.dawn.entity.LightningDragonAmbientEntity;
import dev.deepnight.source.dawn.entity.LightningDragonEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import dev.deepnight.source.SourceDamage;
import dev.deepnight.source.SourceDamage.StunType;

public class Promulti1hurtProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity, float amount) {
      if (sourceentity != null) {
         if (entity.getPersistentData().getBoolean("firedragon")) {
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(12.5), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (sourceentity instanceof LivingEntity && (entityiterator instanceof FireDragonAmbientEntity || entityiterator instanceof FireDragonEntity)) {
                  SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)sourceentity)
                     .setInitialPosition(entity.position())
                     .setStunType(StunType.LONG)
                     .setImpact(4.0F)
                     .setArmorNegation(30.0F);
                  entityiterator.hurt((DamageSource)dmgSrc, amount);
               }
            }
         } else if (entity.getPersistentData().getBoolean("icedragon")) {
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(12.5), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (sourceentity instanceof LivingEntity && (entityiterator instanceof IceDragonAmbientEntity || entityiterator instanceof IceDragonEntity)) {
                  SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)sourceentity)
                     .setInitialPosition(entity.position())
                     .setStunType(StunType.LONG)
                     .setImpact(4.0F)
                     .setArmorNegation(30.0F);
                  entityiterator.hurt((DamageSource)dmgSrc, amount);
               }
            }
         } else if (entity.getPersistentData().getBoolean("lightningdragon")) {
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(12.5), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (sourceentity instanceof LivingEntity
                  && (entityiterator instanceof LightningDragonAmbientEntity || entityiterator instanceof LightningDragonEntity)) {
                  SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)sourceentity)
                     .setInitialPosition(entity.position())
                     .setStunType(StunType.LONG)
                     .setImpact(4.0F)
                     .setArmorNegation(30.0F);
                  entityiterator.hurt((DamageSource)dmgSrc, amount);
               }
            }
         } else if (entity.getPersistentData().getBoolean("dragon_lord")) {
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(12.5), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (sourceentity instanceof LivingEntity && entityiterator instanceof DragonLordEntity) {
                  SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)sourceentity)
                     .setInitialPosition(entity.position())
                     .setStunType(StunType.LONG)
                     .setImpact(4.0F)
                     .setArmorNegation(30.0F);
                  entityiterator.hurt((DamageSource)dmgSrc, amount);
               }
            }
         }
      }
   }
}
