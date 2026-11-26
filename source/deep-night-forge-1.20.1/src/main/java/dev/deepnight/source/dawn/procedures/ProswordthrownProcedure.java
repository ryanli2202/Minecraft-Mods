package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.HerobrineEntity;
import dev.deepnight.source.dawn.entity.SwordThrownEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import dev.deepnight.source.SourceDamage;
import dev.deepnight.source.SourceDamage.StunType;

public class ProswordthrownProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         double i = 0.0;
         double j = 0.0;
         if (!entity.getPersistentData().getBoolean("clientlogged")) {
            entity.getPersistentData().putDouble("yaw", entity.getYRot());
            entity.getPersistentData().putDouble("pitch", entity.getXRot());
            entity.getPersistentData().putBoolean("clientlogged", true);
         }

         if (entity.getPersistentData().getBoolean("lock")) {
            Entity _ent = entity;
            _ent.setYRot((float)entity.getPersistentData().getDouble("yaw"));
            _ent.setXRot((float)entity.getPersistentData().getDouble("pitch"));
            _ent.setYBodyRot(_ent.getYRot());
            _ent.setYHeadRot(_ent.getYRot());
            _ent.yRotO = _ent.getYRot();
            _ent.xRotO = _ent.getXRot();
            if (_ent instanceof LivingEntity _entity) {
               _entity.yBodyRotO = _entity.getYRot();
               _entity.yHeadRotO = _entity.getYRot();
            }
         }

         if (!world.isClientSide()) {
            entity.setDeltaMovement(
               new Vec3(
                  Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180.0)) * 1.6,
                  Math.sin(Math.toRadians(0.0 - entity.getPersistentData().getDouble("pitch"))) * 3.0,
                  Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * 1.6
               )
            );
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (!(entityiterator instanceof SwordThrownEntity) && !(entityiterator instanceof HerobrineEntity)) {
                  SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                     .setInitialPosition(entity.position())
                     .setStunType(StunType.SHORT)
                     .setImpact(3.0F)
                     .setArmorNegation(25.0F);
                  entityiterator.hurt((DamageSource)dmgSrc, 20.0F);
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.crit"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           0.3F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.crit"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           0.3F,
                           false
                        );
                     }
                  }
               }
            }

            if (world.getBlockState(BlockPos.containing(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z)).canOcclude()
               || world.getBlockState(BlockPos.containing(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z)).is(net.minecraft.tags.BlockTags.LEAVES)) {
               if (world instanceof ServerLevel _level) {
                  _level.sendParticles(ParticleTypes.SQUID_INK, x, y, z, 5, 0.5, 0.5, 0.5, 0.2);
               }

               if (!entity.level().isClientSide()) {
                  entity.discard();
               }
            }

            if (entity.getPersistentData().getDouble("timer") == 0.0) {
               entity.getPersistentData().putDouble("timer", 61.0);
            } else {
               entity.getPersistentData().putDouble("timer", entity.getPersistentData().getDouble("timer") - 1.0);
            }

            if (entity.getPersistentData().getDouble("timer") == 1.0) {
               if (world instanceof ServerLevel _level) {
                  _level.sendParticles(ParticleTypes.SQUID_INK, x, y, z, 5, 0.5, 0.5, 0.5, 0.2);
               }

               if (!entity.level().isClientSide()) {
                  entity.discard();
               }
            }
         }
      }
   }
}
