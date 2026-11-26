package dev.deepnight.source.dawn.procedures;


import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.AirEntity;
import dev.deepnight.source.dawn.entity.DraggingSpellEntity;
import dev.deepnight.source.dawn.entity.GhostprojectileEntity;
import dev.deepnight.source.dawn.entity.GroundbreakEffectBigEntity;
import dev.deepnight.source.dawn.entity.ProjectileMagicEntity;
import dev.deepnight.source.dawn.entity.SkeletonlordEntity;
import dev.deepnight.source.dawn.entity.Spell1Entity;
import dev.deepnight.source.dawn.entity.Spell2Entity;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
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
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import dev.deepnight.source.SourceDamage;
import dev.deepnight.source.SourceDamage.StunType;

public class ProskelelordProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, final Entity entity) {
      if (entity != null) {
         if (entity.getPersistentData().getBoolean("lock")) {
            Entity _ent = entity;
            _ent.setYRot((float)entity.getPersistentData().getDouble("lock_xz"));
            _ent.setXRot((float)entity.getPersistentData().getDouble("lock_y"));
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

         if (entity.getPersistentData().getDouble("c_a3timer") == 21.0) {
            Vec3 _center = new Vec3(x + entity.getLookAngle().x * 1.0, y, z + entity.getLookAngle().z * 1.0);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.5), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (!(entityiterator instanceof SkeletonlordEntity)) {
                  entityiterator.setDeltaMovement(new Vec3(0.0, -2.0, 0.0));
                  if (entityiterator instanceof LivingEntity _entity) {
                     _entity.removeAllEffects();
                  }
               }
            }
         }

         if (!world.isClientSide()) {
            if (entity.getPersistentData().getBoolean("hurtcd")) {
               if (entity.getPersistentData().getDouble("hurt_cd") == 0.0) {
                  entity.getPersistentData().putDouble("hurt_cd", 18.0);
               } else {
                  entity.getPersistentData().putDouble("hurt_cd", entity.getPersistentData().getDouble("hurt_cd") - 1.0);
               }

               if (entity.getPersistentData().getDouble("hurt_cd") == 1.0) {
                  entity.getPersistentData().putDouble("hurt_cd", 0.0);
                  entity.getPersistentData().putBoolean("hurtcd", false);
               }
            }

            if (entity.getPersistentData().getDouble("Poise") > 0.0) {
               if (entity.getPersistentData().getBoolean("Attacking")) {
                  if (Math.random() >= 0.8) {
                     entity.getPersistentData().putDouble("Poise", entity.getPersistentData().getDouble("Poise") - 1.0);
                  }
               } else {
                  entity.getPersistentData().putDouble("Poise", entity.getPersistentData().getDouble("Poise") - 1.0);
               }
            }

            if (entity.getPersistentData().getDouble("Poise") >= 400.0 && !entity.getPersistentData().getBoolean("Stunned")) {
               entity.getPersistentData().putBoolean("Stunned", true);
            }

            if (entity.getPersistentData().getDouble("c_a3cd") == 0.0 && entity.getPersistentData().getBoolean("c_a3")) {
               entity.getPersistentData().putDouble("c_a3cd", 100.0);
               entity.getPersistentData().putBoolean("aggro", false);
            } else if (entity.getPersistentData().getDouble("c_a3cd") > 0.0) {
               entity.getPersistentData().putDouble("c_a3cd", entity.getPersistentData().getDouble("c_a3cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("c_a4cd") == 0.0 && entity.getPersistentData().getBoolean("c_a4")) {
               entity.getPersistentData().putDouble("c_a3cd", 60.0);
            } else if (entity.getPersistentData().getDouble("c_a3cd") > 0.0) {
               entity.getPersistentData().putDouble("c_a3cd", entity.getPersistentData().getDouble("c_a3cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("m_a1cd") == 0.0 && entity.getPersistentData().getBoolean("m_a1")) {
               entity.getPersistentData().putDouble("m_a1cd", 120.0);
            } else if (entity.getPersistentData().getDouble("m_a1cd") > 0.0) {
               entity.getPersistentData().putDouble("m_a1cd", entity.getPersistentData().getDouble("m_a1cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("m_a2cd") == 0.0 && entity.getPersistentData().getBoolean("m_a2")) {
               entity.getPersistentData().putDouble("m_a2cd", 200.0);
            } else if (entity.getPersistentData().getDouble("m_a2cd") > 0.0) {
               entity.getPersistentData().putDouble("m_a2cd", entity.getPersistentData().getDouble("m_a2cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("m_a3cd") == 0.0 && entity.getPersistentData().getBoolean("m_a3")) {
               entity.getPersistentData().putDouble("m_a3cd", 260.0);
            } else if (entity.getPersistentData().getDouble("m_a3cd") > 0.0) {
               entity.getPersistentData().putDouble("m_a3cd", entity.getPersistentData().getDouble("m_a3cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("m_a4cd") == 0.0 && entity.getPersistentData().getBoolean("m_a4")) {
               entity.getPersistentData().putDouble("m_a4cd", 300.0);
            } else if (entity.getPersistentData().getDouble("m_a4cd") > 0.0) {
               entity.getPersistentData().putDouble("m_a4cd", entity.getPersistentData().getDouble("m_a4cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("f_a1cd") == 0.0 && entity.getPersistentData().getBoolean("f_a1")) {
               entity.getPersistentData().putDouble("f_a1cd", 240.0);
            } else if (entity.getPersistentData().getDouble("f_a1cd") > 0.0) {
               entity.getPersistentData().putDouble("f_a1cd", entity.getPersistentData().getDouble("f_a1cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("f_a2cd") == 0.0 && entity.getPersistentData().getBoolean("f_a2")) {
               entity.getPersistentData().putDouble("f_a2cd", 120.0);
            } else if (entity.getPersistentData().getDouble("f_a2cd") > 0.0) {
               entity.getPersistentData().putDouble("f_a2cd", entity.getPersistentData().getDouble("f_a2cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("f_a3cd") == 0.0 && entity.getPersistentData().getBoolean("f_a3b")) {
               entity.getPersistentData().putDouble("f_a3cd", 200.0);
            } else if (entity.getPersistentData().getDouble("f_a3cd") > 0.0) {
               entity.getPersistentData().putDouble("f_a3cd", entity.getPersistentData().getDouble("f_a3cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("f_a4cd") == 0.0 && entity.getPersistentData().getBoolean("f_a4")) {
               entity.getPersistentData().putDouble("f_a4cd", 160.0);
            } else if (entity.getPersistentData().getDouble("f_a4cd") > 0.0) {
               entity.getPersistentData().putDouble("f_a4cd", entity.getPersistentData().getDouble("f_a4cd") - 1.0);
            }

            if (entity.isAlive()) {
               entity.getPersistentData().putBoolean("Close", false);
               entity.getPersistentData().putBoolean("Mid", false);
               entity.getPersistentData().putBoolean("Mid2", false);
               entity.getPersistentData().putBoolean("Far", false);
               Vec3 _center = new Vec3(x + entity.getLookAngle().x * 1.0, y, z + entity.getLookAngle().z * 1.0);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(20.0), e -> true)
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
                     entity.getPersistentData().putBoolean("Mid2", false);
                     entity.getPersistentData().putBoolean("Mid", false);
                     entity.getPersistentData().putBoolean("Close", false);
                  }
               }

               _center = new Vec3(x + entity.getLookAngle().x * 1.0, y, z + entity.getLookAngle().z * 1.0);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(6.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                     entity.getPersistentData().putBoolean("Far", false);
                     entity.getPersistentData().putBoolean("Mid2", true);
                     entity.getPersistentData().putBoolean("Mid", false);
                     entity.getPersistentData().putBoolean("Close", false);
                  }
               }

               _center = new Vec3(x + entity.getLookAngle().x * 1.0, y, z + entity.getLookAngle().z * 1.0);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                     entity.getPersistentData().putBoolean("Far", false);
                     entity.getPersistentData().putBoolean("Mid2", false);
                     entity.getPersistentData().putBoolean("Mid", true);
                     entity.getPersistentData().putBoolean("Close", false);
                  }
               }

               _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                     entity.getPersistentData().putBoolean("Far", false);
                     entity.getPersistentData().putBoolean("Mid2", false);
                     entity.getPersistentData().putBoolean("Mid", false);
                     entity.getPersistentData().putBoolean("Close", true);
                  }
               }

               if (entity.getPersistentData().getBoolean("dodge1")) {
                  if (entity.getPersistentData().getDouble("dodge1timer") == 0.0) {
                     entity.getPersistentData().putDouble("dodge1timer", 31.0);
                  } else {
                     entity.getPersistentData().putDouble("dodge1timer", entity.getPersistentData().getDouble("dodge1timer") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("dodge1timer") == 31.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 8, false, false));
                     }

                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.MAGENTA_WOOL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.MAGENTA_WOOL));
                     }
                  } else if (entity.getPersistentData().getDouble("dodge1timer") == 26.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("entity.ender_dragon.flap"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("entity.ender_dragon.flap"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F,
                              false
                           );
                        }
                     }

                     entity.setDeltaMovement(
                        new Vec3(Math.sin(Math.toRadians(entity.getYRot() - 90.0F)), 0.6, Math.cos(Math.toRadians(entity.getYRot() + 90.0F)))
                     );
                  } else if (entity.getPersistentData().getDouble("dodge1timer") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putDouble("dodge1timer", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("dodge1", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("dodge1timer", 0.0);
                     entity.getPersistentData().putBoolean("dodge1", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("dodge2")) {
                  if (entity.getPersistentData().getDouble("dodge2timer") == 0.0) {
                     entity.getPersistentData().putDouble("dodge2timer", 28.0);
                  } else {
                     entity.getPersistentData().putDouble("dodge2timer", entity.getPersistentData().getDouble("dodge2timer") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("dodge2timer") == 28.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 8, false, false));
                     }

                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.CORNFLOWER));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.CORNFLOWER));
                     }
                  } else if (entity.getPersistentData().getDouble("dodge2timer") == 23.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("entity.ender_dragon.flap"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("entity.ender_dragon.flap"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F,
                              false
                           );
                        }
                     }

                     entity.setDeltaMovement(
                        new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 90.0F)), 0.6, Math.cos(Math.toRadians(entity.getYRot() + 270.0F)))
                     );
                  } else if (entity.getPersistentData().getDouble("dodge2timer") == 18.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeleton_magicthrow"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeleton_magicthrow"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F,
                              false
                           );
                        }
                     }

                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new ProjectileMagicEntity(SimpleMobsModEntities.PROJECTILE_MAGIC.get(), _level);
                        entityToSpawn.moveTo(x, y, z, entity.getYRot(), entity.getXRot());
                        entityToSpawn.setYBodyRot(entity.getYRot());
                        entityToSpawn.setYHeadRot(entity.getYRot());
                        entityToSpawn.setDeltaMovement(0.0, 0.0, 0.0);
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  } else if (entity.getPersistentData().getDouble("dodge2timer") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putDouble("dodge2timer", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("dodge2", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("dodge2timer", 0.0);
                     entity.getPersistentData().putBoolean("dodge2", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("c_a1")) {
                  if (entity.getPersistentData().getDouble("c_a1timer") == 0.0) {
                     entity.getPersistentData().putDouble("c_a1timer", 41.0);
                  } else {
                     entity.getPersistentData().putDouble("c_a1timer", entity.getPersistentData().getDouble("c_a1timer") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("c_a1timer") == 41.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 41, 8, false, false));
                     }

                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.LEATHER_HELMET));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                     }
                  } else if (entity.getPersistentData().getDouble("c_a1timer") == 41.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("entity.ender_dragon.flap"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("entity.ender_dragon.flap"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F,
                              false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("c_a1timer") == 29.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 1.0, y, z + entity.getLookAngle().z * 1.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof SkeletonlordEntity)) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.SHORT)
                              .setImpact(2.0F)
                              .setArmorNegation(0.7F);
                           entityiterator.hurt((DamageSource)dmgSrc, 8.0F);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("c_a1timer") == 24.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 3.0, y, z + entity.getLookAngle().z * 3.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof SkeletonlordEntity)) {
                           entity.getPersistentData().putBoolean("c_a2", true);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("c_a1timer") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putDouble("c_a1timer", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("c_a1", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("c_a1timer", 0.0);
                     entity.getPersistentData().putBoolean("c_a1", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("c_a2")) {
                  if (entity.getPersistentData().getDouble("c_a2timer") == 0.0) {
                     entity.getPersistentData().putDouble("c_a2timer", 31.0);
                  } else {
                     entity.getPersistentData().putDouble("c_a2timer", entity.getPersistentData().getDouble("c_a2timer") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("c_a2timer") == 31.0) {
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 31, 8, false, false));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     entity.getPersistentData().putBoolean("c_a1", false);
                     entity.getPersistentData().putDouble("c_a1timer", 0.0);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.GOLD_NUGGET));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLD_NUGGET));
                     }
                  } else if (entity.getPersistentData().getDouble("c_a2timer") == 25.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("entity.ender_dragon.flap"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              0.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("entity.ender_dragon.flap"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              0.0F,
                              false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("c_a2timer") == 20.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 1.0, y, z + entity.getLookAngle().z * 1.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof SkeletonlordEntity)) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.SHORT)
                              .setImpact(2.0F)
                              .setArmorNegation(0.7F);
                           entityiterator.hurt((DamageSource)dmgSrc, 8.0F);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("c_a2timer") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("c_a2", false);
                     entity.getPersistentData().putDouble("c_a2timer", 0.0);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("c_a2timer", 0.0);
                     entity.getPersistentData().putBoolean("c_a2", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("c_a3")) {
                  if (entity.getPersistentData().getDouble("c_a3timer") == 0.0) {
                     entity.getPersistentData().putDouble("c_a3timer", 81.0);
                  } else {
                     entity.getPersistentData().putDouble("c_a3timer", entity.getPersistentData().getDouble("c_a3timer") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("c_a3timer") == 81.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.COAL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.COAL));
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 81, 8, false, false));
                     }

                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_ca3"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_ca3"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F,
                              false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("c_a3timer") == 54.0) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_skeletonlord,limit=1,sort=nearest] {NoAI:1b}"
                           );
                     }

                     _center = new Vec3(x + entity.getLookAngle().x * 1.0, y, z + entity.getLookAngle().z * 1.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof SkeletonlordEntity)) {
                           if (entityiterator instanceof LivingEntity _entity) {
                              _entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 60, 1, false, false));
                           }

                           entityiterator.hurt(new DamageSource(entityiterator.level().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(net.minecraft.world.damagesource.DamageTypes.GENERIC), entity), 6.0F);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("c_a3timer") == 40.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 1.0, y, z + entity.getLookAngle().z * 1.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof SkeletonlordEntity)) {
                           if (entityiterator instanceof LivingEntity _entity) {
                              _entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 60, 1, false, false));
                           }

                           entityiterator.hurt(new DamageSource(entityiterator.level().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(net.minecraft.world.damagesource.DamageTypes.GENERIC), entity), 6.0F);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("c_a3timer") == 30.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 1.0, y, z + entity.getLookAngle().z * 1.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof SkeletonlordEntity)) {
                           if (entityiterator instanceof LivingEntity _entity) {
                              _entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 60, 1, false, false));
                           }

                           entityiterator.hurt(new DamageSource(entityiterator.level().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(net.minecraft.world.damagesource.DamageTypes.GENERIC), entity), 6.0F);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("c_a3timer") == 1.0) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_skeletonlord,limit=1,sort=nearest] {NoAI:0}"
                           );
                     }

                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("c_a3timer", 0.0);
                     entity.getPersistentData().putBoolean("c_a3", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("c_a3timer", 0.0);
                     entity.getPersistentData().putBoolean("c_a3", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("c_a4")) {
                  if (entity.getPersistentData().getDouble("c_a4timer") == 0.0) {
                     entity.getPersistentData().putDouble("c_a4timer", 41.0);
                  } else {
                     entity.getPersistentData().putDouble("c_a4timer", entity.getPersistentData().getDouble("c_a4timer") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("c_a4timer") == 41.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.CHARCOAL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHARCOAL));
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 41, 6, false, false));
                     }

                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_c4"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_c4"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F,
                              false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("c_a4timer") == 27.0) {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null) && world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new Spell2Entity(SimpleMobsModEntities.SPELL_2.get(), _level);
                           entityToSpawn.moveTo(
                              entityiterator.getX() + (entity.getX() - entityiterator.getX()) * 0.2,
                              entityiterator.getY() + (entity.getX() - entityiterator.getX()) * 0.2,
                              entityiterator.getZ() + (entity.getX() - entityiterator.getX()) * 0.2,
                              0.0F,
                              0.0F
                           );
                           entityToSpawn.setYBodyRot(0.0F);
                           entityToSpawn.setYHeadRot(0.0F);
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("c_a4timer") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putDouble("c_a4timer", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("c_a4", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("c_a4timer", 0.0);
                     entity.getPersistentData().putBoolean("c_a4", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("m_a1")) {
                  if (entity.getPersistentData().getDouble("m_a1timer") == 0.0) {
                     entity.getPersistentData().putDouble("m_a1timer", 61.0);
                  } else {
                     entity.getPersistentData().putDouble("m_a1timer", entity.getPersistentData().getDouble("m_a1timer") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("m_a1timer") == 61.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 61, 8, false, false));
                     }

                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.IRON_INGOT));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_INGOT));
                     }
                  } else if (entity.getPersistentData().getDouble("m_a1timer") == 41.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:spell_form"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:spell_form"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F,
                              false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("m_a1timer") == 34.0) {
                     entity.getPersistentData().putDouble("lock_xz", entity.getYRot());
                     entity.getPersistentData().putDouble("lock_y", entity.getXRot());
                  } else if (entity.getPersistentData().getDouble("m_a1timer") == 33.0) {
                     entity.getPersistentData().putBoolean("lock", true);
                  } else if (entity.getPersistentData().getDouble("m_a1timer") == 31.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammer"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammer"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F,
                              false
                           );
                        }
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_skeletonlord,limit=1,sort=nearest] {NoAI:1b}"
                           );
                     }

                     _center = new Vec3(x + entity.getLookAngle().x * 1.8, y, z + entity.getLookAngle().z * 1.8);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if ((entityiterator instanceof Player || entityiterator instanceof ServerPlayer) && entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 10, 1, false, false));
                        }
                     }

                     _center = new Vec3(x + entity.getLookAngle().x * 1.8, y, z + entity.getLookAngle().z * 1.8);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof SkeletonlordEntity)) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.LONG)
                              .setImpact(25.0F)
                              .setArmorNegation(15.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 10.0F);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("m_a1timer") == 1.0) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_skeletonlord,limit=1,sort=nearest] {NoAI:0}"
                           );
                     }

                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putDouble("m_a1timer", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("m_a1", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("m_a1timer", 0.0);
                     entity.getPersistentData().putBoolean("m_a1", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("m_a2")) {
                  if (entity.getPersistentData().getDouble("m_a2timer") == 0.0) {
                     entity.getPersistentData().putDouble("m_a2timer", 96.0);
                  } else {
                     entity.getPersistentData().putDouble("m_a2timer", entity.getPersistentData().getDouble("m_a2timer") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("m_a2timer") == 96.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 96, 8, false, false));
                     }

                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.IRON_NUGGET));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_NUGGET));
                     }
                  } else if (entity.getPersistentData().getDouble("m_a2timer") == 71.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:spell_form"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:spell_form"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F,
                              false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("m_a2timer") == 69.0) {
                     entity.getPersistentData().putDouble("lock_xz", entity.getYRot());
                     entity.getPersistentData().putDouble("lock_y", entity.getXRot());
                  } else if (entity.getPersistentData().getDouble("m_a2timer") == 68.0) {
                     entity.getPersistentData().putBoolean("lock", true);
                  } else if (entity.getPersistentData().getDouble("m_a2timer") == 66.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if ((entityiterator instanceof Player || entityiterator instanceof ServerPlayer) && entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 10, 1, false, false));
                        }
                     }

                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammer"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammer"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F,
                              false
                           );
                        }
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_skeletonlord,limit=1,sort=nearest] {NoAI:1b}"
                           );
                     }

                     _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof SkeletonlordEntity)) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.LONG)
                              .setImpact(25.0F)
                              .setArmorNegation(15.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 16.0F);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("m_a2timer") == 45.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_afterhammer"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_afterhammer"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F,
                              false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("m_a2timer") == 44.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if ((entityiterator instanceof Player || entityiterator instanceof ServerPlayer) && entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 30, 1, false, false));
                        }
                     }

                     _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.25), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof SkeletonlordEntity)) {
                           if (entityiterator instanceof LivingEntity _entity) {
                              _entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 20, 1, false, false));
                           }

                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.KNOCKDOWN)
                              .setImpact(10.0F)
                              .setArmorNegation(20.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 12.0F);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("m_a2timer") == 1.0) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_skeletonlord,limit=1,sort=nearest] {NoAI:0}"
                           );
                     }

                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putDouble("m_a2timer", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("m_a2", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("m_a2timer", 0.0);
                     entity.getPersistentData().putBoolean("m_a2", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("m_a3")) {
                  if (entity.getPersistentData().getDouble("m_a3timer") == 0.0) {
                     entity.getPersistentData().putDouble("m_a3timer", 121.0);
                  } else {
                     entity.getPersistentData().putDouble("m_a3timer", entity.getPersistentData().getDouble("m_a3timer") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("m_a3timer") == 121.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 35, 8, false, false));
                     }

                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.WHITE_WOOL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.WHITE_WOOL));
                     }
                  } else if (entity.getPersistentData().getDouble("m_a3timer") == 94.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_blast"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_blast"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F,
                              false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("m_a3timer") == 86.0) {
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 11, 8, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("m_a3timer") == 59.0) {
                     entity.getPersistentData().putDouble("lock_xz", entity.getYRot());
                     entity.getPersistentData().putDouble("lock_y", entity.getXRot());
                  } else if (entity.getPersistentData().getDouble("m_a3timer") == 58.0) {
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 58, 8, false, false));
                     }

                     entity.getPersistentData().putBoolean("lock", true);
                  } else if (entity.getPersistentData().getDouble("m_a3timer") == 56.0) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_skeletonlord,limit=1,sort=nearest] {NoAI:1b}"
                           );
                     }

                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if ((entityiterator instanceof Player || entityiterator instanceof ServerPlayer) && entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 20, 2, false, false));
                        }
                     }

                     _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof SkeletonlordEntity)) {
                           if (entityiterator instanceof LivingEntity _entity) {
                              _entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 60, 1, false, false));
                           }

                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.KNOCKDOWN)
                              .setImpact(20.0F)
                              .setArmorNegation(15.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 28.0F);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("m_a3timer") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_skeletonlord,limit=1,sort=nearest] {NoAI:0}"
                           );
                     }

                     entity.getPersistentData().putDouble("m_a3timer", 0.0);
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("m_a3", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("m_a3timer", 0.0);
                     entity.getPersistentData().putBoolean("m_a3", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("m_a4")) {
                  if (entity.getPersistentData().getDouble("m_a4timer") == 0.0) {
                     entity.getPersistentData().putDouble("m_a4timer", 67.0);
                  } else {
                     entity.getPersistentData().putDouble("m_a4timer", entity.getPersistentData().getDouble("m_a4timer") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("m_a4timer") == 67.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.RED_CONCRETE));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.RED_CONCRETE));
                     }

                     if (entity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 101, 6, false, false));
                     }

                     entity.getPersistentData().putBoolean("focus", true);
                  } else if (entity.getPersistentData().getDouble("m_a4timer") == 62.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:spell_form"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:spell_form"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F,
                              false
                           );
                        }
                     }

                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putDouble("lock_xz", entity.getPersistentData().getDouble("yaw2"));
                     entity.getPersistentData().putDouble("lock_y", entity.getXRot());
                     entity.getPersistentData().putBoolean("lock", true);
                  } else if (entity.getPersistentData().getDouble("m_a4timer") == 54.0) {
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
                           entityiterator.hurt((DamageSource)dmgSrc, 16.0F);
                        }
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y, z, 10, 1.0, 1.0, 1.0, 0.5);
                     }

                     entity.setDeltaMovement(
                        new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 1.7, 0.8, Math.cos(Math.toRadians(entity.getYRot())) * 1.7)
                     );
                  } else if (entity.getPersistentData().getDouble("m_a4timer") == 2.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.GREEN_CONCRETE));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.GREEN_CONCRETE));
                     }
                  } else if (entity.getPersistentData().getDouble("m_a4timer") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putDouble("m_a4timer", 0.0);
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putBoolean("nolaser", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("m_a4", false);
                  }

                  if (entity.getPersistentData().getDouble("m_a4timer") <= 49.0
                     && entity.getPersistentData().getDouble("m_a4timer") >= 10.0
                     && entity.onGround()) {
                     entity.getPersistentData().putBoolean("m_a4a", true);
                     entity.getPersistentData().putDouble("m_a4timer", 0.0);
                     entity.getPersistentData().putBoolean("m_a4", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("m_a4timer", 0.0);
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putBoolean("m_a4", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("m_a4a")) {
                  if (entity.getPersistentData().getDouble("m_a4atimer") == 0.0) {
                     entity.getPersistentData().putDouble("m_a4atimer", 44.0);
                  } else {
                     entity.getPersistentData().putDouble("m_a4atimer", entity.getPersistentData().getDouble("m_a4atimer") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("m_a4atimer") == 44.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.YELLOW_CONCRETE));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.YELLOW_CONCRETE));
                     }

                     if (entity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 44, 6, false, false));
                     }

                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammer"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammer"),
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
                              dev.deepnight.SafeSounds.resolve("simple_mobs:jump"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              0.5F
                           );
                        } else {
                           _level.playLocalSound(
                              x, y, z, dev.deepnight.SafeSounds.resolve("simple_mobs:jump"), SoundSource.NEUTRAL, 2.0F, 0.5F, false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("m_a4atimer") == 42.0) {
                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new GroundbreakEffectBigEntity(SimpleMobsModEntities.GROUNDBREAK_EFFECT_BIG.get(), _level);
                        entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
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
                           entityiterator.hurt((DamageSource)dmgSrc, 18.0F);
                        }
                     }

                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new AirEntity(SimpleMobsModEntities.AIR.get(), _level);
                        entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  } else if (entity.getPersistentData().getDouble("m_a4atimer") == 2.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.GREEN_CONCRETE));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.GREEN_CONCRETE));
                     }
                  } else if (entity.getPersistentData().getDouble("m_a4atimer") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putDouble("m_a4atimer", 0.0);
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("m_a4a", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("m_a4atimer", 0.0);
                     entity.getPersistentData().putBoolean("m_a4a", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("f_a1")) {
                  if (entity.getPersistentData().getDouble("f_a1timer") == 0.0) {
                     entity.getPersistentData().putDouble("f_a1timer", 81.0);
                  } else {
                     entity.getPersistentData().putDouble("f_a1timer", entity.getPersistentData().getDouble("f_a1timer") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("f_a1timer") == 81.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.RED_WOOL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.RED_WOOL));
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 81, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("f_a1timer") == 72.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_book"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_book"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F,
                              false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("f_a1timer") == 47.0) {
                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new GhostprojectileEntity(SimpleMobsModEntities.GHOSTPROJECTILE.get(), _level);
                        entityToSpawn.moveTo(
                           entity.getX() + entity.getLookAngle().x * 1.5, entity.getY() + 2.0, entity.getZ() + entity.getLookAngle().z * 1.5, 0.0F, 0.0F
                        );
                        entityToSpawn.setYBodyRot(0.0F);
                        entityToSpawn.setYHeadRot(0.0F);
                        entityToSpawn.setDeltaMovement(0.0, 0.3, 0.0);
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }

                     (new Object() {
                           private int ticks = 0;
                           private float waitTicks;
                           private LevelAccessor world;

                           public void start(LevelAccessor world, int waitTicks) {
                              this.waitTicks = waitTicks;
                              MinecraftForge.EVENT_BUS.register(this);
                              this.world = world;
                           }

                           @SubscribeEvent
                           public void tick_ProskelelordProcedure_1(ServerTickEvent event) {
                              if (event.phase == Phase.END) {
                                 this.ticks++;
                                 if (this.ticks >= this.waitTicks) {
                                    this.run();
                                 }
                              }
                           }

                           private void run() {
                              if (this.world instanceof ServerLevel _level) {
                                 Entity entityToSpawn = new GhostprojectileEntity(SimpleMobsModEntities.GHOSTPROJECTILE.get(), _level);
                                 entityToSpawn.moveTo(
                                    entity.getX() + entity.getLookAngle().x * 1.5 + Math.random(),
                                    entity.getY() + 2.1,
                                    entity.getZ() + entity.getLookAngle().z * 1.5 + Math.random(),
                                    0.0F,
                                    0.0F
                                 );
                                 entityToSpawn.setYBodyRot(0.0F);
                                 entityToSpawn.setYHeadRot(0.0F);
                                 entityToSpawn.setDeltaMovement(0.0, 0.5, 0.0);
                                 if (entityToSpawn instanceof Mob _mobToSpawn) {
                                    _mobToSpawn.finalizeSpawn(
                                       _level, this.world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                                    );
                                 }

                                 this.world.addFreshEntity(entityToSpawn);
                              }

                              MinecraftForge.EVENT_BUS.unregister(this);
                           }
                        })
                        .start(world, 8);
                     (new Object() {
                           private int ticks = 0;
                           private float waitTicks;
                           private LevelAccessor world;

                           public void start(LevelAccessor world, int waitTicks) {
                              this.waitTicks = waitTicks;
                              MinecraftForge.EVENT_BUS.register(this);
                              this.world = world;
                           }

                           @SubscribeEvent
                           public void tick_ProskelelordProcedure_2(ServerTickEvent event) {
                              if (event.phase == Phase.END) {
                                 this.ticks++;
                                 if (this.ticks >= this.waitTicks) {
                                    this.run();
                                 }
                              }
                           }

                           private void run() {
                              if (this.world instanceof ServerLevel _level) {
                                 Entity entityToSpawn = new GhostprojectileEntity(SimpleMobsModEntities.GHOSTPROJECTILE.get(), _level);
                                 entityToSpawn.moveTo(
                                    entity.getX() + entity.getLookAngle().x * 1.5 - Math.random(),
                                    entity.getY() + 1.9,
                                    entity.getZ() + entity.getLookAngle().z * 1.5 - Math.random(),
                                    0.0F,
                                    0.0F
                                 );
                                 entityToSpawn.setYBodyRot(0.0F);
                                 entityToSpawn.setYHeadRot(0.0F);
                                 entityToSpawn.setDeltaMovement(0.0, 0.5, 0.0);
                                 if (entityToSpawn instanceof Mob _mobToSpawn) {
                                    _mobToSpawn.finalizeSpawn(
                                       _level, this.world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                                    );
                                 }

                                 this.world.addFreshEntity(entityToSpawn);
                              }

                              MinecraftForge.EVENT_BUS.unregister(this);
                           }
                        })
                        .start(world, 16);
                     (new Object() {
                           private int ticks = 0;
                           private float waitTicks;
                           private LevelAccessor world;

                           public void start(LevelAccessor world, int waitTicks) {
                              this.waitTicks = waitTicks;
                              MinecraftForge.EVENT_BUS.register(this);
                              this.world = world;
                           }

                           @SubscribeEvent
                           public void tick_ProskelelordProcedure_3(ServerTickEvent event) {
                              if (event.phase == Phase.END) {
                                 this.ticks++;
                                 if (this.ticks >= this.waitTicks) {
                                    this.run();
                                 }
                              }
                           }

                           private void run() {
                              if (this.world instanceof ServerLevel _level) {
                                 Entity entityToSpawn = new GhostprojectileEntity(SimpleMobsModEntities.GHOSTPROJECTILE.get(), _level);
                                 entityToSpawn.moveTo(
                                    entity.getX() + entity.getLookAngle().x * 1.5 + Math.random(),
                                    entity.getY() + 2.0,
                                    entity.getZ() + entity.getLookAngle().z * 1.5 - Math.random(),
                                    0.0F,
                                    0.0F
                                 );
                                 entityToSpawn.setYBodyRot(0.0F);
                                 entityToSpawn.setYHeadRot(0.0F);
                                 entityToSpawn.setDeltaMovement(0.0, 0.6, 0.0);
                                 if (entityToSpawn instanceof Mob _mobToSpawn) {
                                    _mobToSpawn.finalizeSpawn(
                                       _level, this.world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                                    );
                                 }

                                 this.world.addFreshEntity(entityToSpawn);
                              }

                              MinecraftForge.EVENT_BUS.unregister(this);
                           }
                        })
                        .start(world, 24);
                  } else if (entity.getPersistentData().getDouble("f_a1timer") == 17.0) {
                     entity.getPersistentData().putBoolean("target", true);
                  } else if (entity.getPersistentData().getDouble("f_a1timer") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putDouble("f_a1timer", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("f_a1", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("f_a1timer", 0.0);
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putBoolean("f_a1", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("target")) {
                  if (entity.getPersistentData().getDouble("marktimer") == 0.0) {
                     entity.getPersistentData().putDouble("marktimer", 160.0);
                  } else {
                     entity.getPersistentData().putDouble("marktimer", entity.getPersistentData().getDouble("marktimer") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("marktimer") == 160.0) {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(50.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                           entityiterator.getPersistentData().putBoolean("marked", true);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("marktimer") == 1.0) {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(50.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator.getPersistentData().getBoolean("marked")) {
                           entityiterator.getPersistentData().putBoolean("marked", false);
                        }
                     }
                  }
               }

               if (entity.getPersistentData().getBoolean("f_a2")) {
                  if (entity.getPersistentData().getDouble("f_a2timer") == 0.0) {
                     entity.getPersistentData().putDouble("f_a2timer", 31.0);
                  } else {
                     entity.getPersistentData().putDouble("f_a2timer", entity.getPersistentData().getDouble("f_a2timer") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("f_a2timer") == 31.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.YELLOW_WOOL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.YELLOW_WOOL));
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 31, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("f_a2timer") == 26.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeleton_magicthrow"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeleton_magicthrow"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F,
                              false
                           );
                        }
                     }

                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new ProjectileMagicEntity(SimpleMobsModEntities.PROJECTILE_MAGIC.get(), _level);
                        entityToSpawn.moveTo(
                           entity.getX() + entity.getLookAngle().x * 1.5,
                           entity.getY() + 2.0,
                           entity.getZ() + entity.getLookAngle().z * 1.5,
                           entity.getYRot(),
                           entity.getXRot() - 30.0F
                        );
                        entityToSpawn.setYBodyRot(entity.getYRot());
                        entityToSpawn.setYHeadRot(entity.getYRot());
                        entityToSpawn.setDeltaMovement(0.0, 0.0, 0.0);
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  } else if (entity.getPersistentData().getDouble("f_a2timer") == 21.0) {
                     if (0.5 <= Math.random()) {
                        entity.getPersistentData().putBoolean("f_a2a", true);
                     }
                  } else if (entity.getPersistentData().getDouble("f_a2timer") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putDouble("f_a2timer", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("f_a2", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("f_a2timer", 0.0);
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putBoolean("f_a2", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("f_a2a")) {
                  if (entity.getPersistentData().getDouble("f_a2atimer") == 0.0) {
                     entity.getPersistentData().putDouble("f_a2atimer", 30.0);
                  } else {
                     entity.getPersistentData().putDouble("f_a2atimer", entity.getPersistentData().getDouble("f_a2atimer") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("f_a2atimer") == 30.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     entity.getPersistentData().putBoolean("f_a2", false);
                     entity.getPersistentData().putDouble("f_a2timer", 0.0);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.GREEN_WOOL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.GREEN_WOOL));
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("f_a2atimer") == 26.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeleton_magicthrow"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeleton_magicthrow"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F,
                              false
                           );
                        }
                     }

                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new ProjectileMagicEntity(SimpleMobsModEntities.PROJECTILE_MAGIC.get(), _level);
                        entityToSpawn.moveTo(
                           entity.getX() + entity.getLookAngle().x * 1.5,
                           entity.getY() + 2.0,
                           entity.getZ() + entity.getLookAngle().z * 1.5,
                           entity.getYRot(),
                           entity.getXRot() - 30.0F
                        );
                        entityToSpawn.setYBodyRot(entity.getYRot());
                        entityToSpawn.setYHeadRot(entity.getYRot());
                        entityToSpawn.setDeltaMovement(0.0, 0.0, 0.0);
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  } else if (entity.getPersistentData().getDouble("f_a2atimer") == 1.0) {
                     entity.getPersistentData().putDouble("f_a2timer", 0.0);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("f_a2a", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("f_a2atimer", 0.0);
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putBoolean("f_a2a", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("f_a3")) {
                  if (entity.getPersistentData().getDouble("f_a3timer") == 0.0) {
                     entity.getPersistentData().putDouble("f_a3timer", 61.0);
                  } else {
                     entity.getPersistentData().putDouble("f_a3timer", entity.getPersistentData().getDouble("f_a3timer") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("f_a3timer") == 61.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.BLACK_WOOL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.BLACK_WOOL));
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 61, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("f_a3timer") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.BROWN_WOOL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.BROWN_WOOL));
                     }

                     entity.getPersistentData().putBoolean("f_a3a", true);
                     entity.getPersistentData().putBoolean("f_a3", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("f_a3a", 0.0);
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putBoolean("f_a3", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("f_a3a")) {
                  if (entity.getPersistentData().getDouble("f_a3atimer") == 0.0) {
                     entity.getPersistentData().putDouble("f_a3atimer", 31.0);
                  } else {
                     entity.getPersistentData().putDouble("f_a3atimer", entity.getPersistentData().getDouble("f_a3atimer") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("f_a3atimer") == 31.0) {
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 35, 6, false, false));
                     }

                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(20.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null) && world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new Spell1Entity(SimpleMobsModEntities.SPELL_1.get(), _level);
                           entityToSpawn.moveTo(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ(), 0.0F, 0.0F);
                           entityToSpawn.setYBodyRot(0.0F);
                           entityToSpawn.setYHeadRot(0.0F);
                           entityToSpawn.setDeltaMovement(0.0, 0.0, 0.0);
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }
                     }
                  }

                  if (!entity.getPersistentData().getBoolean("Far")) {
                     entity.getPersistentData().putBoolean("f_a3b", true);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("f_a3atimer", 0.0);
                     entity.getPersistentData().putBoolean("f_a3b", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("f_a3b")) {
                  if (entity.getPersistentData().getDouble("f_a3btimer") == 0.0) {
                     entity.getPersistentData().putDouble("f_a3btimer", 41.0);
                  } else {
                     entity.getPersistentData().putDouble("f_a3btimer", entity.getPersistentData().getDouble("f_a3btimer") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("f_a3btimer") == 41.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     entity.getPersistentData().putBoolean("f_a3a", false);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.LIME_WOOL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.LIME_WOOL));
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 41, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("f_a3btimer") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("f_a3b", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("f_a3btimer", 0.0);
                     entity.getPersistentData().putBoolean("f_a3b", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("f_a4")) {
                  if (entity.getPersistentData().getDouble("f_a4timer") == 0.0) {
                     entity.getPersistentData().putDouble("f_a4timer", 81.0);
                  } else {
                     entity.getPersistentData().putDouble("f_a4timer", entity.getPersistentData().getDouble("f_a4timer") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("f_a4timer") == 81.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.BLUE_WOOL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.BLUE_WOOL));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 81, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("f_a4timer") == 55.0) {
                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new DraggingSpellEntity(SimpleMobsModEntities.DRAGGING_SPELL.get(), _level);
                        entityToSpawn.moveTo(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z, 0.0F, 0.0F);
                        entityToSpawn.setYBodyRot(0.0F);
                        entityToSpawn.setYHeadRot(0.0F);
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  } else if (entity.getPersistentData().getDouble("f_a4timer") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("f_a4timer", 0.0);
                     entity.getPersistentData().putBoolean("f_a4", false);
                  }

                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if ((entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null) == entityiterator) {
                        entity.getPersistentData().putBoolean("f_a4a", true);
                        entity.getPersistentData().putBoolean("f_a4", false);
                        entity.getPersistentData().putDouble("f_a4timer", 0.0);
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("f_a4timer", 0.0);
                     entity.getPersistentData().putBoolean("f_a4", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("f_a4a")) {
                  if (entity.getPersistentData().getDouble("f_a4atimer") == 0.0) {
                     entity.getPersistentData().putDouble("f_a4atimer", 41.0);
                  } else {
                     entity.getPersistentData().putDouble("f_a4atimer", entity.getPersistentData().getDouble("f_a4atimer") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("f_a4atimer") == 41.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.LIGHT_BLUE_WOOL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.LIGHT_BLUE_WOOL));
                     }

                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammerform"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammerform"),
                              SoundSource.NEUTRAL,
                              1.0F,
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
                        if (entityiterator instanceof DraggingSpellEntity && !entityiterator.level().isClientSide()) {
                           entityiterator.discard();
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("f_a4atimer") == 31.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammer"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammer"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F,
                              false
                           );
                        }
                     }

                     _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(6.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if ((entityiterator instanceof Player || entityiterator instanceof ServerPlayer) && entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 10, 1, false, false));
                        }
                     }

                     _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.25), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.KNOCKDOWN)
                              .setImpact(10.0F)
                              .setArmorNegation(3.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 12.0F);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("f_a4atimer") == 1.0) {
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("f_a4atimer", 0.0);
                     entity.getPersistentData().putBoolean("f_a4a", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("f_a4atimer", 0.0);
                     entity.getPersistentData().putBoolean("f_a4a", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  if (entity.getPersistentData().getDouble("stun") == 0.0) {
                     entity.getPersistentData().putDouble("stun", 81.0);
                  } else {
                     entity.getPersistentData().putDouble("stun", entity.getPersistentData().getDouble("stun") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("stun") == 81.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:poise"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x, y, z, dev.deepnight.SafeSounds.resolve("simple_mobs:poise"), SoundSource.NEUTRAL, 1.0F, 1.0F, false
                           );
                        }
                     }

                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.PAPER));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.PAPER));
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 91, 7, false, false));
                     }

                     entity.getPersistentData().putDouble("lock_xz", entity.getYRot());
                     entity.getPersistentData().putDouble("lock_y", entity.getXRot());
                  } else if (entity.getPersistentData().getDouble("stun") == 80.0) {
                     entity.getPersistentData().putBoolean("lock", true);
                  } else if (entity.getPersistentData().getDouble("stun") == 1.0) {
                     entity.getPersistentData().putBoolean("lock", false);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_skeletonlord,limit=1,sort=nearest] {NoAI:0}"
                           );
                     }

                     entity.getPersistentData().putDouble("Poise", -80.0);
                     entity.getPersistentData().putDouble("stun", 0.0);
                     entity.getPersistentData().putBoolean("Stunned", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Close")) {
                  if (Math.random() <= 0.3 && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("c_a1", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (entity.getPersistentData().getDouble("c_a4cd") == 0.0
                     && Math.random() <= 0.3
                     && !entity.getPersistentData().getBoolean("Attacking")
                     && (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0F)
                        < (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1.0F) * 0.6) {
                     entity.getPersistentData().putBoolean("c_a4", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (entity.getPersistentData().getDouble("c_a3cd") == 0.0
                     && Math.random() >= 0.31
                     && Math.random() <= 0.6
                     && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("c_a3", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (Math.random() >= 0.61 && Math.random() <= 0.8 && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("dodge1", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (Math.random() >= 0.81 && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("dodge2", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  }
               }

               if (entity.getPersistentData().getBoolean("Mid")) {
                  if (entity.getPersistentData().getDouble("m_a1cd") == 0.0
                     && Math.random() >= 0.31
                     && Math.random() <= 0.65
                     && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("m_a1", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (entity.getPersistentData().getDouble("m_a3cd") == 0.0
                     && Math.random() <= 0.5
                     && !entity.getPersistentData().getBoolean("Attacking")
                     && (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0F)
                        < (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1.0F) * 0.6) {
                     entity.getPersistentData().putBoolean("m_a3", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (entity.getPersistentData().getDouble("m_a4cd") == 0.0
                     && Math.random() <= 0.5
                     && !entity.getPersistentData().getBoolean("Attacking")
                     && (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0F)
                        < (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1.0F) * 0.6) {
                     entity.getPersistentData().putBoolean("m_a4", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (entity.getPersistentData().getDouble("m_a2cd") == 0.0
                     && Math.random() <= 0.3
                     && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("m_a2", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (Math.random() >= 0.66 && Math.random() <= 0.8 && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("dodge1", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (Math.random() >= 0.81 && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("dodge2", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  }
               }

               if (entity.getPersistentData().getBoolean("Mid2")) {
                  if (entity.getPersistentData().getDouble("f_a2cd") == 0.0
                     && Math.random() >= 0.31
                     && Math.random() <= 0.66
                     && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("f_a2", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (entity.getPersistentData().getDouble("f_a1cd") == 0.0
                     && Math.random() <= 0.3
                     && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("f_a1", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (entity.getPersistentData().getDouble("f_a3cd") == 0.0
                     && Math.random() >= 0.67
                     && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("f_a3", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  }
               }

               if (entity.getPersistentData().getBoolean("Far")) {
                  if (entity.getPersistentData().getDouble("f_a2cd") == 0.0
                     && Math.random() >= 0.51
                     && Math.random() <= 0.75
                     && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("f_a2", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (entity.getPersistentData().getDouble("f_a4cd") == 0.0
                     && Math.random() <= 0.25
                     && !entity.getPersistentData().getBoolean("Attacking")
                     && (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0F)
                        < (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1.0F) * 0.6) {
                     entity.getPersistentData().putBoolean("f_a4", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (entity.getPersistentData().getDouble("f_a1cd") == 0.0
                     && Math.random() <= 0.5
                     && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("f_a1", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (entity.getPersistentData().getDouble("f_a3cd") == 0.0
                     && Math.random() >= 0.86
                     && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("f_a3", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  }
               }

               if (!entity.getPersistentData().getBoolean("aggro")) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 40, 7, false, false));
                  }

                  if (!entity.getPersistentData().getBoolean("Attacking")) {
                     if (entity.getPersistentData().getDouble("readtimer") == 0.0) {
                        entity.getPersistentData().putDouble("readtimer", 200.0);
                     } else {
                        entity.getPersistentData().putDouble("readtimer", entity.getPersistentData().getDouble("readtimer") - 1.0);
                     }

                     if (entity.getPersistentData().getDouble("readtimer") == 2.0) {
                        entity.getPersistentData().putDouble("lock_xz", entity.getYRot());
                        entity.getPersistentData().putDouble("lock_y", entity.getXRot());
                     } else if (entity.getPersistentData().getDouble("readtimer") == 1.0) {
                        entity.getPersistentData().putBoolean("down", true);
                        entity.getPersistentData().putBoolean("lock", true);
                     }
                  }
               }

               if (entity.getPersistentData().getBoolean("down")) {
                  if (entity.getPersistentData().getDouble("downtimer") == 0.0) {
                     entity.getPersistentData().putDouble("downtimer", 61.0);
                  } else {
                     entity.getPersistentData().putDouble("downtimer", entity.getPersistentData().getDouble("downtimer") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("downtimer") == 61.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 61, 8, false, false));
                     }

                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.LILAC));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.LILAC));
                     }
                  } else if (entity.getPersistentData().getDouble("downtimer") == 39.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_down"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_down"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F,
                              false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("downtimer") == 21.0) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_skeletonlord,sort=nearest,limit=1] {Invulnerable:1b}"
                           );
                     }
                  } else if (entity.getPersistentData().getDouble("downtimer") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.ROSE_BUSH));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.ROSE_BUSH));
                     }

                     entity.getPersistentData().putBoolean("read", true);
                     entity.getPersistentData().putBoolean("down", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("read")) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 8, false, false));
                  }

                  if (entity.getPersistentData().getBoolean("aggro")) {
                     entity.getPersistentData().putBoolean("up", true);
                     entity.getPersistentData().putBoolean("read", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("up")) {
                  if (entity.getPersistentData().getDouble("uptimer") == 0.0) {
                     entity.getPersistentData().putDouble("uptimer", 41.0);
                  } else {
                     entity.getPersistentData().putDouble("uptimer", entity.getPersistentData().getDouble("uptimer") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("uptimer") == 41.0) {
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 41, 8, false, false));
                     }

                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.KELP));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.KELP));
                     }
                  } else if (entity.getPersistentData().getDouble("uptimer") == 31.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_up"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_up"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F,
                              false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("uptimer") == 12.0) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_skeletonlord,sort=nearest,limit=1] {Invulnerable:0}"
                           );
                     }
                  } else if (entity.getPersistentData().getDouble("uptimer") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("up", false);
                  }
               }
            }
         }
      }
   }
}
