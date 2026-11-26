package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.AirEntity;
import dev.deepnight.source.dawn.entity.GroundbreakEffectBigEntity;
import dev.deepnight.source.dawn.entity.GroundbreakEffectSmallEntity;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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

public class PromossgolemProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         double i = 0.0;
         if (entity.getPersistentData().getBoolean("lock")) {
            Entity _ent = entity;
            _ent.setYRot((float)entity.getPersistentData().getDouble("yaw"));
            _ent.setXRot(entity.getXRot());
            _ent.setYBodyRot(_ent.getYRot());
            _ent.setYHeadRot(_ent.getYRot());
            _ent.yRotO = _ent.getYRot();
            _ent.xRotO = _ent.getXRot();
            if (_ent instanceof LivingEntity _entity) {
               _entity.yBodyRotO = _entity.getYRot();
               _entity.yHeadRotO = _entity.getYRot();
            }
         }

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
            if (entity.getPersistentData().getBoolean("Wake")) {
               if (entity.isAlive()) {
                  entity.getPersistentData().putBoolean("Far", false);
                  entity.getPersistentData().putBoolean("Close", false);
                  Vec3 _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(32.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        entity.getPersistentData()
                           .putDouble("atanyaw", Math.toDegrees(Math.atan2(entityiterator.getX() - entity.getX(), entityiterator.getZ() - entity.getZ())));
                        if (entity.getPersistentData().getDouble("atanyaw") < 0.0) {
                           entity.getPersistentData().putDouble("yaw2", 0.0 - entity.getPersistentData().getDouble("atanyaw"));
                        }

                        if (entity.getPersistentData().getDouble("atanyaw") >= 0.0) {
                           entity.getPersistentData().putDouble("yaw2", 360.0 - entity.getPersistentData().getDouble("atanyaw"));
                        }

                        entity.getPersistentData()
                           .putDouble(
                              "pitch2",
                              Math.toDegrees(
                                 Math.atan(
                                    Math.hypot(Math.abs(entity.getX() - entityiterator.getX()), Math.abs(entity.getZ() - entityiterator.getZ()))
                                       / Math.abs(entity.getY() + 7.0 - entityiterator.getY())
                                 )
                              )
                           );
                        entity.getPersistentData().putBoolean("Far", true);
                        entity.getPersistentData().putBoolean("Close", false);
                     }
                  }

                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        entity.getPersistentData().putBoolean("Far", false);
                     }
                  }

                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        entity.getPersistentData().putBoolean("Far", false);
                        entity.getPersistentData().putBoolean("Close", true);
                     }
                  }

                  if (entity.getPersistentData().getBoolean("hurtcd")) {
                     if (entity.getPersistentData().getDouble("hurt_cd") == 0.0) {
                        entity.getPersistentData().putDouble("hurt_cd", 6.0);
                     } else {
                        entity.getPersistentData().putDouble("hurt_cd", entity.getPersistentData().getDouble("hurt_cd") - 1.0);
                     }

                     if (entity.getPersistentData().getDouble("hurt_cd") == 1.0) {
                        entity.getPersistentData().putDouble("hurt_cd", 0.0);
                        entity.getPersistentData().putBoolean("hurtcd", false);
                     }
                  }

                  if (entity.getPersistentData().getDouble("Poise") > 0.0 && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putDouble("Poise", entity.getPersistentData().getDouble("Poise") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Poise") >= 300.0
                     && !entity.getPersistentData().getBoolean("Stunned")
                     && !entity.getPersistentData().getBoolean("Stunned_2")) {
                     entity.getPersistentData().putBoolean("Stunned", true);
                  }

                  if (entity.getPersistentData().getDouble("Atk3Timer") == 0.0 && entity.getPersistentData().getBoolean("Attack3")) {
                     entity.getPersistentData().putDouble("Atk3Timer", Mth.nextInt(net.minecraft.util.RandomSource.create(), 300, 400));
                  } else if (entity.getPersistentData().getDouble("Atk3Timer") > 0.0) {
                     entity.getPersistentData().putDouble("Atk3Timer", entity.getPersistentData().getDouble("Atk3Timer") - 1.0);
                  }

                  if (entity.getPersistentData().getBoolean("Close")) {
                     if (Math.random() <= 0.5 && !entity.getPersistentData().getBoolean("Attacking")) {
                        entity.getPersistentData().putBoolean("Attack1", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (Math.random() <= 0.5
                        && !entity.getPersistentData().getBoolean("Attacking")
                        && entity.getPersistentData().getDouble("Atk3Timer") == 0.0) {
                        entity.getPersistentData().putBoolean("Attack3", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (!entity.getPersistentData().getBoolean("Attacking")) {
                        entity.getPersistentData().putBoolean("Attack2", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Waking")) {
                     if (entity.getPersistentData().getDouble("wakecd") == 0.0) {
                        entity.getPersistentData().putDouble("wakecd", 81.0);
                     } else {
                        entity.getPersistentData().putDouble("wakecd", entity.getPersistentData().getDouble("wakecd") - 1.0);
                     }

                     if (entity.getPersistentData().getDouble("wakecd") == 1.0) {
                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:stone_2"),
                                 SoundSource.NEUTRAL,
                                 3.0F,
                                 0.5F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:stone_2"),
                                 SoundSource.NEUTRAL,
                                 3.0F,
                                 0.5F,
                                 false
                              );
                           }
                        }

                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                        }

                        entity.getPersistentData().putDouble("wakecd", 0.0);
                        entity.getPersistentData().putBoolean("lock", false);
                        entity.getPersistentData().putBoolean("noai", false);
                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putBoolean("Waking", false);
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Attack1")) {
                     if (entity.getPersistentData().getDouble("atk1") == 0.0) {
                        entity.getPersistentData().putDouble("atk1", 61.0);
                     } else {
                        entity.getPersistentData().putDouble("atk1", entity.getPersistentData().getDouble("atk1") - 1.0);
                     }

                     if (entity.getPersistentData().getDouble("atk1") == 61.0) {
                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:stone_2"),
                                 SoundSource.NEUTRAL,
                                 1.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:stone_2"),
                                 SoundSource.NEUTRAL,
                                 1.0F,
                                 1.0F,
                                 false
                              );
                           }
                        }

                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Items.LEATHER_HELMET));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                        }

                        if (entity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                           _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 6, false, false));
                        }

                        entity.getPersistentData().putBoolean("focus", true);
                        entity.getPersistentData().putBoolean("nolaser", true);
                     } else if (entity.getPersistentData().getDouble("atk1") == 39.0) {
                        entity.getPersistentData().putBoolean("focus", false);
                        entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                        entity.getPersistentData().putBoolean("lock", true);
                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:stone_1"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:stone_1"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F,
                                 false
                              );
                           }
                        }

                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F,
                                 false
                              );
                           }
                        }

                        if (world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new GroundbreakEffectSmallEntity(SimpleMobsModEntities.GROUNDBREAK_EFFECT_SMALL.get(), _level);
                           entityToSpawn.moveTo(
                              x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0, world.getRandom().nextFloat() * 360.0F, 0.0F
                           );
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }

                        _center = new Vec3(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (entityiterator != entity) {
                              SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                 .setInitialPosition(entity.position())
                                 .setStunType(StunType.SHORT)
                                 .setImpact(15.0F)
                                 .setArmorNegation(20.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 25.0F);
                           }
                        }
                     } else if (entity.getPersistentData().getDouble("atk1") == 29.0) {
                        _center = new Vec3(x + entity.getLookAngle().x * 1.0, y, z + entity.getLookAngle().z * 1.0);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                              if (Math.random() >= 0.25) {
                                 entity.getPersistentData().putBoolean("Attack1a", true);
                                 entity.getPersistentData().putBoolean("Attack1", false);
                                 entity.getPersistentData().putDouble("atk1", 0.0);
                                 entity.getPersistentData().putBoolean("lock", false);
                              } else {
                                 entity.getPersistentData().putBoolean("Attack2", true);
                                 entity.getPersistentData().putDouble("atk1", 0.0);
                                 entity.getPersistentData().putBoolean("Attack1", false);
                                 entity.getPersistentData().putBoolean("lock", false);
                              }
                           }
                        }
                     } else if (entity.getPersistentData().getDouble("atk1") == 1.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                        }

                        entity.getPersistentData().putDouble("atk1", 0.0);
                        entity.getPersistentData().putBoolean("nolaser", false);
                        entity.getPersistentData().putBoolean("lock", false);
                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putBoolean("Attack1", false);
                     }

                     if (entity.getPersistentData().getBoolean("Stunned")) {
                        entity.getPersistentData().putDouble("atk1", 0.0);
                        entity.getPersistentData().putBoolean("focus", false);
                        entity.getPersistentData().putBoolean("Attack1", false);
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Attack1a")) {
                     if (entity.getPersistentData().getDouble("atk1a") == 0.0) {
                        entity.getPersistentData().putDouble("atk1a", 66.0);
                     } else {
                        entity.getPersistentData().putDouble("atk1a", entity.getPersistentData().getDouble("atk1a") - 1.0);
                     }

                     if (entity.getPersistentData().getDouble("atk1a") == 66.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Items.GOLD_INGOT));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLD_INGOT));
                        }

                        if (entity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                           _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 6, false, false));
                        }

                        entity.getPersistentData().putBoolean("focus", true);
                        entity.getPersistentData().putBoolean("nolaser", true);
                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:stone_2"),
                                 SoundSource.NEUTRAL,
                                 1.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:stone_2"),
                                 SoundSource.NEUTRAL,
                                 1.0F,
                                 1.0F,
                                 false
                              );
                           }
                        }
                     } else if (entity.getPersistentData().getDouble("atk1a") == 54.0) {
                        entity.getPersistentData().putBoolean("focus", false);
                        entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:stone_1"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:stone_1"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F,
                                 false
                              );
                           }
                        }

                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F,
                                 false
                              );
                           }
                        }

                        entity.getPersistentData().putBoolean("lock", true);
                        if (world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new GroundbreakEffectSmallEntity(SimpleMobsModEntities.GROUNDBREAK_EFFECT_SMALL.get(), _level);
                           entityToSpawn.moveTo(
                              x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0, world.getRandom().nextFloat() * 360.0F, 0.0F
                           );
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }

                        _center = new Vec3(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (entityiterator != entity) {
                              SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                 .setInitialPosition(entity.position())
                                 .setStunType(StunType.SHORT)
                                 .setImpact(15.0F)
                                 .setArmorNegation(20.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 25.0F);
                           }
                        }
                     } else if (entity.getPersistentData().getDouble("atk1a") == 26.0) {
                        if (Math.random() > 0.85) {
                           entity.getPersistentData().putBoolean("Attack3", true);
                           entity.getPersistentData().putBoolean("lock", false);
                           entity.getPersistentData().putDouble("atk1a", 0.0);
                           entity.getPersistentData().putBoolean("Attack1a", false);
                        }
                     } else if (entity.getPersistentData().getDouble("atk1a") == 1.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                        }

                        entity.getPersistentData().putDouble("atk1a", 0.0);
                        entity.getPersistentData().putBoolean("lock", false);
                        entity.getPersistentData().putBoolean("nolaser", false);
                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putBoolean("Attack1a", false);
                     }

                     if (entity.getPersistentData().getBoolean("Stunned")) {
                        entity.getPersistentData().putDouble("atk1a", 0.0);
                        entity.getPersistentData().putBoolean("focus", false);
                        entity.getPersistentData().putBoolean("Attack1a", false);
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Attack2")) {
                     if (entity.getPersistentData().getDouble("atk2") == 0.0) {
                        entity.getPersistentData().putDouble("atk2", 62.0);
                     } else {
                        entity.getPersistentData().putDouble("atk2", entity.getPersistentData().getDouble("atk2") - 1.0);
                     }

                     if (entity.getPersistentData().getDouble("atk2") == 62.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Items.COAL));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.COAL));
                        }

                        if (entity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                           _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 6, false, false));
                        }

                        entity.getPersistentData().putBoolean("focus", true);
                        entity.getPersistentData().putBoolean("nolaser", true);
                     } else if (entity.getPersistentData().getDouble("atk2") == 54.0) {
                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:stone_2"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:stone_2"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F,
                                 false
                              );
                           }
                        }

                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:sweep"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:sweep"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F,
                                 false
                              );
                           }
                        }
                     } else if (entity.getPersistentData().getDouble("atk2") == 44.0) {
                        entity.getPersistentData().putBoolean("focus", false);
                        entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                        entity.getPersistentData().putBoolean("lock", true);
                     } else if (entity.getPersistentData().getDouble("atk2") == 42.0) {
                        if (world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new GroundbreakEffectSmallEntity(SimpleMobsModEntities.GROUNDBREAK_EFFECT_SMALL.get(), _level);
                           entityToSpawn.moveTo(
                              x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0, world.getRandom().nextFloat() * 360.0F, 0.0F
                           );
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }

                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:stone_1"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:stone_1"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F,
                                 false
                              );
                           }
                        }

                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F,
                                 false
                              );
                           }
                        }

                        _center = new Vec3(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (entityiterator != entity) {
                              SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                 .setInitialPosition(entity.position())
                                 .setStunType(StunType.SHORT)
                                 .setImpact(15.0F)
                                 .setArmorNegation(20.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 30.0F);
                           }
                        }
                     } else if (entity.getPersistentData().getDouble("atk2") == 29.0) {
                        _center = new Vec3(x + entity.getLookAngle().x * 1.0, y, z + entity.getLookAngle().z * 1.0);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                              entity.getPersistentData().putDouble("atk2", 0.0);
                              entity.getPersistentData().putBoolean("Attack2b", true);
                              entity.getPersistentData().putBoolean("Attack2", false);
                              entity.getPersistentData().putBoolean("lock", false);
                           }
                        }
                     } else if (entity.getPersistentData().getDouble("atk2") == 2.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.POPPY));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.POPPY));
                        }
                     } else if (entity.getPersistentData().getDouble("atk2") == 1.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                        }

                        entity.getPersistentData().putDouble("atk2", 0.0);
                        entity.getPersistentData().putBoolean("lock", false);
                        entity.getPersistentData().putBoolean("nolaser", false);
                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putBoolean("Attack2", false);
                     }

                     if (entity.getPersistentData().getBoolean("Stunned")) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.POPPY));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.POPPY));
                        }

                        entity.getPersistentData().putDouble("atk2", 0.0);
                        entity.getPersistentData().putBoolean("focus", false);
                        entity.getPersistentData().putBoolean("Attack2", false);
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Attack2b")) {
                     if (entity.getPersistentData().getDouble("atk2b") == 0.0) {
                        entity.getPersistentData().putDouble("atk2b", 55.0);
                     } else {
                        entity.getPersistentData().putDouble("atk2b", entity.getPersistentData().getDouble("atk2b") - 1.0);
                     }

                     if (entity.getPersistentData().getDouble("atk2b") == 55.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Items.CHARCOAL));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHARCOAL));
                        }

                        if (entity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                           _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 6, false, false));
                        }

                        entity.getPersistentData().putBoolean("focus", true);
                        entity.getPersistentData().putBoolean("nolaser", true);
                     } else if (entity.getPersistentData().getDouble("atk2b") == 51.0) {
                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:stone_2"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:stone_2"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F,
                                 false
                              );
                           }
                        }

                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:sweep"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:sweep"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F,
                                 false
                              );
                           }
                        }
                     } else if (entity.getPersistentData().getDouble("atk2b") == 43.0) {
                        entity.getPersistentData().putBoolean("focus", false);
                        entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                        entity.getPersistentData().putBoolean("lock", true);
                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:stone_1"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 0.5F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:stone_1"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 0.5F,
                                 false
                              );
                           }
                        }

                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F,
                                 false
                              );
                           }
                        }
                     } else if (entity.getPersistentData().getDouble("atk2b") == 41.0) {
                        _center = new Vec3(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (entityiterator != entity) {
                              SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                 .setInitialPosition(entity.position())
                                 .setStunType(StunType.SHORT)
                                 .setImpact(15.0F)
                                 .setArmorNegation(20.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 30.0F);
                           }
                        }

                        if (world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new GroundbreakEffectSmallEntity(SimpleMobsModEntities.GROUNDBREAK_EFFECT_SMALL.get(), _level);
                           entityToSpawn.moveTo(
                              x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0, world.getRandom().nextFloat() * 360.0F, 0.0F
                           );
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }
                     } else if (entity.getPersistentData().getDouble("atk2b") == 2.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.POPPY));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.POPPY));
                        }
                     } else if (entity.getPersistentData().getDouble("atk2b") == 1.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                        }

                        entity.getPersistentData().putDouble("atk2b", 0.0);
                        entity.getPersistentData().putBoolean("lock", false);
                        entity.getPersistentData().putBoolean("nolaser", false);
                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putBoolean("Attack2b", false);
                     }

                     if (entity.getPersistentData().getBoolean("Stunned")) {
                        entity.getPersistentData().putDouble("atk2b", 0.0);
                        entity.getPersistentData().putBoolean("focus", false);
                        entity.getPersistentData().putBoolean("Attack2b", false);
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Attack3")) {
                     if (entity.getPersistentData().getDouble("atk3") == 0.0) {
                        entity.getPersistentData().putDouble("atk3", 101.0);
                     } else {
                        entity.getPersistentData().putDouble("atk3", entity.getPersistentData().getDouble("atk3") - 1.0);
                     }

                     if (entity.getPersistentData().getDouble("atk3") == 101.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Items.IRON_INGOT));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_INGOT));
                        }

                        if (entity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                           _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 101, 6, false, false));
                        }

                        entity.getPersistentData().putBoolean("focus", true);
                        entity.getPersistentData().putBoolean("nolaser", true);
                     } else if (entity.getPersistentData().getDouble("atk3") == 87.0) {
                        entity.getPersistentData().putBoolean("focus", false);
                        entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                        entity.getPersistentData().putBoolean("lock", true);
                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:stone_2"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:stone_2"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F,
                                 false
                              );
                           }
                        }

                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:giant_footstep"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:giant_footstep"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F,
                                 false
                              );
                           }
                        }

                        _center = new Vec3(x, y, z);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (entityiterator != entity) {
                              SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                 .setInitialPosition(entity.position())
                                 .setStunType(StunType.SHORT)
                                 .setImpact(15.0F)
                                 .setArmorNegation(20.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 10.0F);
                           }
                        }
                     } else if (entity.getPersistentData().getDouble("atk3") == 75.0) {
                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:jump"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:jump"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F,
                                 false
                              );
                           }
                        }

                        if (world instanceof ServerLevel _level) {
                           _level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y, z, 10, 1.0, 1.0, 1.0, 0.5);
                        }

                        entity.setDeltaMovement(
                           new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 1.5, 1.0, Math.cos(Math.toRadians(entity.getYRot())) * 1.5)
                        );
                     } else if (entity.getPersistentData().getDouble("atk3") == 1.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                        }

                        entity.getPersistentData().putDouble("atk3", 0.0);
                        entity.getPersistentData().putBoolean("lock", false);
                        entity.getPersistentData().putBoolean("nolaser", false);
                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putBoolean("Attack3", false);
                     }

                     if (entity.getPersistentData().getDouble("atk3") <= 73.0 && entity.getPersistentData().getDouble("atk3") >= 20.0 && entity.onGround()) {
                        entity.getPersistentData().putBoolean("Attack3b", true);
                        entity.getPersistentData().putDouble("atk3", 0.0);
                        entity.getPersistentData().putBoolean("Attack3", false);
                     }

                     if (entity.getPersistentData().getBoolean("Stunned")) {
                        entity.getPersistentData().putDouble("atk3", 0.0);
                        entity.getPersistentData().putBoolean("focus", false);
                        entity.getPersistentData().putBoolean("Attack3", false);
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Attack3b")) {
                     if (entity.getPersistentData().getDouble("atk3b") == 0.0) {
                        entity.getPersistentData().putDouble("atk3b", 63.0);
                     } else {
                        entity.getPersistentData().putDouble("atk3b", entity.getPersistentData().getDouble("atk3b") - 1.0);
                     }

                     if (entity.getPersistentData().getDouble("atk3b") == 63.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Items.IRON_NUGGET));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_NUGGET));
                        }

                        entity.getPersistentData().putBoolean("nolaser", true);
                        if (entity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                           _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 63, 6, false, false));
                        }

                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:stone_1"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 0.5F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:stone_1"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 0.5F,
                                 false
                              );
                           }
                        }

                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:jump"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 0.5F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:jump"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 0.5F,
                                 false
                              );
                           }
                        }
                     } else if (entity.getPersistentData().getDouble("atk3b") == 61.0) {
                        if (world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new GroundbreakEffectBigEntity(SimpleMobsModEntities.GROUNDBREAK_EFFECT_BIG.get(), _level);
                           entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }

                        _center = new Vec3(x, y, z);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.0), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (entityiterator != entity) {
                              SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                 .setInitialPosition(entity.position())
                                 .setStunType(StunType.SHORT)
                                 .setImpact(15.0F)
                                 .setArmorNegation(20.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 40.0F);
                           }
                        }

                        if (world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new AirEntity(SimpleMobsModEntities.AIR.get(), _level);
                           entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }
                     } else if (entity.getPersistentData().getDouble("atk3b") == 1.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                        }

                        entity.getPersistentData().putDouble("atk3b", 0.0);
                        entity.getPersistentData().putBoolean("lock", false);
                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putBoolean("nolaser", false);
                        entity.getPersistentData().putBoolean("Attack3b", false);
                     }

                     if (entity.getPersistentData().getBoolean("Stunned")) {
                        entity.getPersistentData().putDouble("atk3b", 0.0);
                        entity.getPersistentData().putBoolean("focus", false);
                        entity.getPersistentData().putBoolean("Attack3b", false);
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     if (entity.getPersistentData().getDouble("stun") == 0.0) {
                        entity.getPersistentData().putDouble("stun", 61.0);
                     } else {
                        entity.getPersistentData().putDouble("stun", entity.getPersistentData().getDouble("stun") - 1.0);
                     }

                     if (entity.getPersistentData().getDouble("stun") == 61.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.CACTUS));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.CACTUS));
                        }

                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:poise"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:poise"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F,
                                 false
                              );
                           }
                        }

                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:stone_2"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 0.5F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:stone_2"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 0.5F,
                                 false
                              );
                           }
                        }

                        entity.getPersistentData().putBoolean("nolaser", true);
                        if (entity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                           _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 61, 6, false, false));
                        }

                        entity.getPersistentData().putBoolean("Attacking", true);
                        if (!entity.getPersistentData().getBoolean("lock")) {
                           entity.getPersistentData().putDouble("yaw", entity.getYRot());
                           entity.getPersistentData().putBoolean("lock", true);
                        }
                     } else if (entity.getPersistentData().getDouble("stun") == 1.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                        }

                        entity.getPersistentData().putDouble("stun", 0.0);
                        entity.getPersistentData().putDouble("Poise", -40.0);
                        entity.getPersistentData().putBoolean("lock", false);
                        entity.getPersistentData().putBoolean("nolaser", false);
                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putBoolean("Stunned", false);
                     }

                     if (entity.getPersistentData().getDouble("Poise") >= 320.0) {
                        entity.getPersistentData().putDouble("stun", 0.0);
                        entity.getPersistentData().putBoolean("Stunned_2", true);
                        entity.getPersistentData().putBoolean("Stunned", false);
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Stunned_2")) {
                     if (entity.getPersistentData().getDouble("stun2") == 0.0) {
                        entity.getPersistentData().putDouble("stun2", 112.0);
                     } else {
                        entity.getPersistentData().putDouble("stun2", entity.getPersistentData().getDouble("stun2") - 1.0);
                     }

                     if (entity.getPersistentData().getDouble("stun2") == 112.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Items.BAKED_POTATO));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.BAKED_POTATO));
                        }

                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:poise"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:poise"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F,
                                 false
                              );
                           }
                        }

                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:stone_2"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 0.8F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:stone_2"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 0.8F,
                                 false
                              );
                           }
                        }

                        if (entity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                           _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 61, 6, false, false));
                        }
                     } else if (entity.getPersistentData().getDouble("stun2") == 1.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                        }

                        entity.getPersistentData().putDouble("stun2", 0.0);
                        entity.getPersistentData().putDouble("Poise", -80.0);
                        entity.getPersistentData().putBoolean("lock", false);
                        entity.getPersistentData().putBoolean("nolaser", false);
                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putBoolean("Stunned_2", false);
                     }
                  }
               }
            } else {
               if (entity.getPersistentData().getDouble("atimer") == 0.0) {
                  entity.getPersistentData().putDouble("atimer", 31.0);
                  entity.getPersistentData().putDouble("yaw", entity.getYRot());
                  entity.getPersistentData().putBoolean("noai", true);
                  entity.getPersistentData().putBoolean("lock", true);
               } else {
                  entity.getPersistentData().putDouble("atimer", entity.getPersistentData().getDouble("atimer") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atimer") == 1.0) {
                  entity.getPersistentData().putBoolean("Around", false);
                  Vec3 _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator instanceof ServerPlayer || entityiterator instanceof Player) {
                        entity.getPersistentData().putBoolean("Around", true);
                     }
                  }

                  while (i <= 7.0) {
                     _center = new Vec3(x + entity.getLookAngle().x + i, y, z + entity.getLookAngle().z + i);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator instanceof ServerPlayer || entityiterator instanceof Player) {
                           entity.getPersistentData().putBoolean("Around", true);
                        }
                     }

                     i++;
                  }

                  if (!(entity.getPersistentData().getDouble("Poise") >= 1.0) && !entity.getPersistentData().getBoolean("Around")) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.YELLOW_CONCRETE));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.YELLOW_CONCRETE));
                     }
                  } else {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.RED_WOOL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.RED_WOOL));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     entity.getPersistentData().putBoolean("Wake", true);
                     entity.getPersistentData().putBoolean("Waking", true);
                  }
               }
            }
         }
      }
   }
}
