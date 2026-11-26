package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.CapedMinotaurEntity;
import dev.deepnight.source.dawn.entity.SkeletonlordEntity;
import dev.deepnight.source.dawn.entity.Spell1Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import dev.deepnight.source.SourceDamage;
import dev.deepnight.source.SourceDamage.StunType;

public class Prospell1Procedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (entity.getPersistentData().getDouble("a") == 0.0) {
            entity.getPersistentData().putDouble("a", 41.0);
            if (world instanceof Level _level) {
               if (!_level.isClientSide()) {
                  _level.playSound(
                     null,
                     BlockPos.containing(x, y, z),
                     dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_spell"),
                     SoundSource.NEUTRAL,
                     1.0F,
                     1.0F
                  );
               } else {
                  _level.playLocalSound(
                     x,
                     y,
                     z,
                     dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_spell"),
                     SoundSource.NEUTRAL,
                     1.0F,
                     1.0F,
                     false
                  );
               }
            }
         } else {
            entity.getPersistentData().putDouble("a", entity.getPersistentData().getDouble("a") - 1.0);
         }

         if (entity.getPersistentData().getDouble("a") <= 32.0 && entity.getPersistentData().getDouble("a") >= 4.0) {
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (!(entityiterator instanceof CapedMinotaurEntity)
                  && !(entityiterator instanceof Spell1Entity)
                  && !(entityiterator instanceof SkeletonlordEntity)) {
                  SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                     .setInitialPosition(entity.position())
                     .setStunType(StunType.SHORT)
                     .setImpact(0.5F)
                     .setArmorNegation(0.4F);
                  entityiterator.hurt((DamageSource)dmgSrc, 8.0F);
                  if (entityiterator instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2, false, false));
                  }
               }
            }
         }

         if (entity.getPersistentData().getDouble("a") == 3.0 && !entity.level().isClientSide()) {
            entity.discard();
         }
      }
   }
}
