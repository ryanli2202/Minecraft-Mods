package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.CorruptedOgreEntity;
import dev.deepnight.source.dawn.entity.LosteyeEffect1Entity;
import dev.deepnight.source.dawn.entity.LosteyeprojEntity;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.init.SimpleMobsModParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import dev.deepnight.source.SourceDamage;
import dev.deepnight.source.SourceDamage.StunType;

public class ProlosteyeProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (entity.getPersistentData().getBoolean("focus")) {
            Entity _ent = entity;
            _ent.setYRot((float)entity.getPersistentData().getDouble("yaw2"));
            _ent.setXRot((float)entity.getPersistentData().getDouble("pitch2"));
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
            entity.getPersistentData().putBoolean("despawn", true);
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(27.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                  entity.getPersistentData()
                     .putDouble("atanyaw", Math.toDegrees(Math.atan2(entityiterator.getX() - entity.getX(), entityiterator.getZ() - entity.getZ())));
                  entity.getPersistentData()
                     .putDouble("distance", Math.hypot(Math.abs(entity.getX() - entityiterator.getX()), Math.abs(entity.getZ() - entityiterator.getZ())));
                  if (entity.getPersistentData().getDouble("atanyaw") < 0.0) {
                     entity.getPersistentData().putDouble("yaw2", 0.0 - entity.getPersistentData().getDouble("atanyaw"));
                  }

                  if (entity.getPersistentData().getDouble("atanyaw") >= 0.0) {
                     entity.getPersistentData().putDouble("yaw2", 360.0 - entity.getPersistentData().getDouble("atanyaw"));
                  }

                  entity.getPersistentData()
                     .putDouble(
                        "pitch2", Math.toDegrees(Math.atan(entity.getPersistentData().getDouble("distance") / (entityiterator.getY() - (entity.getY() + 2.0))))
                     );
                  if (entity.getPersistentData().getDouble("pitch2") > 0.0) {
                     entity.getPersistentData().putDouble("pitch2", 90.0 - entity.getPersistentData().getDouble("pitch2"));
                  } else if (entity.getPersistentData().getDouble("pitch2") < 0.0) {
                     entity.getPersistentData().putDouble("pitch2", 90.0 + entity.getPersistentData().getDouble("pitch2"));
                  }
               }
            }

            _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(27.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator instanceof CorruptedOgreEntity) {
                  entity.getPersistentData().putBoolean("despawn", false);
                  if (!entityiterator.getPersistentData().getBoolean("nofollow")) {
                     entity.getPersistentData().putBoolean("zap", false);
                     entity.setDeltaMovement(
                        new Vec3(
                           (entityiterator.getX() - entity.getX()) * 0.6,
                           (entityiterator.getY() + 4.0 - entity.getY()) * 0.9,
                           (entityiterator.getZ() - entity.getZ()) * 0.6
                        )
                     );
                  } else if (entityiterator.getPersistentData().getBoolean("nofollow")) {
                     entity.getPersistentData().putBoolean("zap", true);
                  }

                  if (entityiterator.getPersistentData().getBoolean("fire")) {
                     entity.getPersistentData().putBoolean("Attack1", true);
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("zap")) {
               _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                     SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                        .setInitialPosition(entity.position())
                        .setStunType(StunType.SHORT)
                        .setImpact(1.5F)
                        .setArmorNegation(1.0F);
                     entityiterator.hurt((DamageSource)dmgSrc, 2.0F);
                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.LOSTEYEPARTICLE.get(), x, y + 0.5, z, 2, 0.3, 0.3, 0.3, 0.2);
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(
                           (SimpleParticleType)SimpleMobsModParticleTypes.LOSTEYEPARTICLE.get(),
                           entityiterator.getX(),
                           entityiterator.getY() + 1.0,
                           entityiterator.getZ(),
                           2,
                           0.3,
                           0.6,
                           0.3,
                           0.2
                        );
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack1")) {
               if (entity.getPersistentData().getDouble("Atk1") == 0.0) {
                  entity.getPersistentData().putDouble("Atk1", 35.0);
               } else {
                  entity.getPersistentData().putDouble("Atk1", entity.getPersistentData().getDouble("Atk1") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk1") == 35.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.LEATHER_HELMET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("focus", true);
               } else if (entity.getPersistentData().getDouble("Atk1") == 28.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:electricity_zap"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           0.5F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:electricity_zap"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           0.5F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new LosteyeEffect1Entity(SimpleMobsModEntities.LOSTEYE_EFFECT_1.get(), _level);
                     entityToSpawn.moveTo(
                        x + entity.getLookAngle().x * -1.5,
                        y + 1.0,
                        z + entity.getLookAngle().z * -1.5,
                        (float)entity.getPersistentData().getDouble("yaw2"),
                        (float)entity.getPersistentData().getDouble("pitch2")
                     );
                     entityToSpawn.setYBodyRot((float)entity.getPersistentData().getDouble("yaw2"));
                     entityToSpawn.setYHeadRot((float)entity.getPersistentData().getDouble("yaw2"));
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new LosteyeprojEntity(SimpleMobsModEntities.LOSTEYEPROJ.get(), _level);
                     entityToSpawn.moveTo(
                        x + entity.getLookAngle().x,
                        y,
                        z + entity.getLookAngle().z,
                        (float)entity.getPersistentData().getDouble("yaw2"),
                        (float)entity.getPersistentData().getDouble("pitch2")
                     );
                     entityToSpawn.setYBodyRot((float)entity.getPersistentData().getDouble("yaw2"));
                     entityToSpawn.setYHeadRot((float)entity.getPersistentData().getDouble("yaw2"));
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               } else if (entity.getPersistentData().getDouble("Atk1") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk1", 0.0);
                  entity.getPersistentData().putBoolean("Attack1", false);
               }
            }

            if (entity.getPersistentData().getBoolean("despawn") && !entity.level().isClientSide()) {
               entity.discard();
            }
         }
      }
   }
}
