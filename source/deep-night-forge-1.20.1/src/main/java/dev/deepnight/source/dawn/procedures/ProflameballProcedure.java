package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.init.SimpleMobsModParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import dev.deepnight.source.SourceDamage;
import dev.deepnight.source.SourceDamage.StunType;

public class ProflameballProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         world.addParticle(
            ParticleTypes.FLAME,
            x + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
            y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
            z + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
            0.0,
            0.0,
            0.0
         );
         if (!world.isClientSide()) {
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(30.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator.getPersistentData().getBoolean("flametarget") && !entity.getPersistentData().getBoolean("explode")) {
                  entity.setDeltaMovement(
                     new Vec3(
                        (entityiterator.getX() - entity.getX()) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.02, 0.04),
                        (entityiterator.getY() + 1.0 - entity.getY()) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.02, 0.04),
                        (entityiterator.getZ() - entity.getZ()) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.02, 0.04)
                     )
                  );
               }
            }

            _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator.getPersistentData().getBoolean("flametarget")) {
                  entity.getPersistentData().putBoolean("explode", true);
               }
            }

            if (entity.getPersistentData().getBoolean("explode")) {
               if (entity.getPersistentData().getDouble("b") == 0.0) {
                  entity.getPersistentData().putDouble("b", 51.0);
               } else {
                  entity.getPersistentData().putDouble("b", entity.getPersistentData().getDouble("b") - 1.0);
               }

               if (entity.getPersistentData().getDouble("b") == 51.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(0, new ItemStack(Items.LEATHER_BOOTS));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("item.firecharge.use"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.5F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("item.firecharge.use"), SoundSource.NEUTRAL, 2.0F, 0.5F, false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("b") == 1.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 10, 2.0, 2.0, 2.0, 0.0);
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.FLASH, x, y, z, 10, 3.0, 3.0, 3.0, 0.0);
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.FLAME.get(), x, y, z, 40, 3.0, 3.0, 3.0, 0.5);
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.firework_rocket.blast"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           0.6F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.firework_rocket.blast"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           0.6F,
                           false
                        );
                     }
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.dragon_fireball.explode"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           0.4F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.dragon_fireball.explode"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           0.4F,
                           false
                        );
                     }
                  }

                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(6.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!entity.getPersistentData().getBoolean("firegiant")) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(1.0F)
                           .setArmorNegation(60.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 30.0F);
                        if (!entity.level().isClientSide()) {
                           entity.discard();
                        }
                     }
                  }
               }
            }

            if (entity.getPersistentData().getDouble("a") == 0.0) {
               entity.getPersistentData().putDouble("a", 300.0);
            } else {
               entity.getPersistentData().putDouble("a", entity.getPersistentData().getDouble("a") - 1.0);
            }

            if (entity.getPersistentData().getDouble("a") == 1.0 && !entity.level().isClientSide()) {
               entity.discard();
            }

            if (entity.getPersistentData().getDouble("c") == 0.0) {
               entity.getPersistentData().putDouble("c", 20.0);
               if (world instanceof Level _level) {
                  if (!_level.isClientSide()) {
                     _level.playSound(
                        null,
                        BlockPos.containing(x, y, z),
                        dev.deepnight.SafeSounds.resolve("block.fire.ambient"),
                        SoundSource.NEUTRAL,
                        2.0F,
                        1.0F
                     );
                  } else {
                     _level.playLocalSound(
                        x, y, z, dev.deepnight.SafeSounds.resolve("block.fire.ambient"), SoundSource.NEUTRAL, 2.0F, 1.0F, false
                     );
                  }
               }
            } else {
               entity.getPersistentData().putDouble("c", entity.getPersistentData().getDouble("c") - 1.0);
            }
         }
      }
   }
}
