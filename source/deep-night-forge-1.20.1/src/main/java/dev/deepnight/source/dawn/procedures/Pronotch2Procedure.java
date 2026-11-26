package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.AirEntity;
import dev.deepnight.source.dawn.entity.NotchBossEntity;
import dev.deepnight.source.dawn.entity.Notchflash2Entity;
import dev.deepnight.source.dawn.entity.Notchgroundeffect1Entity;
import dev.deepnight.source.dawn.entity.Notchgroundeffect2Entity;
import dev.deepnight.source.dawn.entity.Notchgroundeffect3Entity;
import dev.deepnight.source.dawn.entity.Notchgroundeffect4Entity;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.init.SimpleMobsModParticleTypes;
import dev.deepnight.source.dawn.network.SimpleMobsModVariables;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import dev.deepnight.source.SourceDamage;
import dev.deepnight.source.SourceDamage.StunType;

public class Pronotch2Procedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         double raytrace_distance = 0.0;
         double i = 0.0;
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

         if (!world.isClientSide() && entity.isAlive()) {
            if (entity.getPersistentData().getDouble("bbtimer") != 0.0) {
               entity.getPersistentData().putDouble("bbtimer", entity.getPersistentData().getDouble("bbtimer") - 1.0);
            } else {
               entity.getPersistentData().putDouble("bbtimer", 80.0);
               SimpleMobsModVariables.MapVariables.get(world).i = -3.0;
               SimpleMobsModVariables.MapVariables.get(world).syncData(world);
               SimpleMobsModVariables.MapVariables.get(world).j = 1.0;
               SimpleMobsModVariables.MapVariables.get(world).syncData(world);
               SimpleMobsModVariables.MapVariables.get(world).x = -3.0;
               SimpleMobsModVariables.MapVariables.get(world).syncData(world);

               while (SimpleMobsModVariables.MapVariables.get(world).i < 3.0) {
                  while (SimpleMobsModVariables.MapVariables.get(world).j < 5.0) {
                     while (SimpleMobsModVariables.MapVariables.get(world).x < 3.0) {
                        if (world.getBlockState(
                                 BlockPos.containing(
                                    x + SimpleMobsModVariables.MapVariables.get(world).i + entity.getLookAngle().x * 0.5,
                                    y + SimpleMobsModVariables.MapVariables.get(world).j,
                                    z + SimpleMobsModVariables.MapVariables.get(world).x + entity.getLookAngle().z * 0.5
                                 )
                              )
                              .getDestroySpeed(
                                 world,
                                 BlockPos.containing(
                                    x + SimpleMobsModVariables.MapVariables.get(world).i + entity.getLookAngle().x * 0.5,
                                    y + SimpleMobsModVariables.MapVariables.get(world).j,
                                    z + SimpleMobsModVariables.MapVariables.get(world).x + entity.getLookAngle().z * 0.5
                                 )
                              )
                           > -1.0F) {
                           world.destroyBlock(
                              BlockPos.containing(
                                 x + SimpleMobsModVariables.MapVariables.get(world).i + entity.getLookAngle().x * 0.5,
                                 y + SimpleMobsModVariables.MapVariables.get(world).j,
                                 z + SimpleMobsModVariables.MapVariables.get(world).x + entity.getLookAngle().z * 0.5
                              ),
                              false
                           );
                        }

                        SimpleMobsModVariables.MapVariables.get(world).x++;
                        SimpleMobsModVariables.MapVariables.get(world).syncData(world);
                     }

                     SimpleMobsModVariables.MapVariables.get(world).x = -3.0;
                     SimpleMobsModVariables.MapVariables.get(world).syncData(world);
                     SimpleMobsModVariables.MapVariables.get(world).j++;
                     SimpleMobsModVariables.MapVariables.get(world).syncData(world);
                  }

                  SimpleMobsModVariables.MapVariables.get(world).j = 1.0;
                  SimpleMobsModVariables.MapVariables.get(world).syncData(world);
                  SimpleMobsModVariables.MapVariables.get(world).i++;
                  SimpleMobsModVariables.MapVariables.get(world).syncData(world);
               }

               SimpleMobsModVariables.MapVariables.get(world).i = 0.0;
               SimpleMobsModVariables.MapVariables.get(world).syncData(world);
               SimpleMobsModVariables.MapVariables.get(world).j = 0.0;
               SimpleMobsModVariables.MapVariables.get(world).syncData(world);
               SimpleMobsModVariables.MapVariables.get(world).x = 0.0;
               SimpleMobsModVariables.MapVariables.get(world).syncData(world);
            }

            entity.getPersistentData().putBoolean("Aggro", false);
            entity.getPersistentData().putBoolean("Close", false);
            entity.getPersistentData().putBoolean("Mid", false);
            entity.getPersistentData().putBoolean("Far", false);
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(32.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                  entity.getPersistentData().putBoolean("Aggro", true);
                  entity.getPersistentData().putBoolean("Far", true);
                  entity.getPersistentData()
                     .putDouble("atanyaw", Math.toDegrees(Math.atan2(entityiterator.getX() - entity.getX(), entityiterator.getZ() - entity.getZ())));
                  if (entity.getPersistentData().getDouble("atanyaw") < 0.0) {
                     entity.getPersistentData().putDouble("yaw2", 0.0 - entity.getPersistentData().getDouble("atanyaw"));
                  }

                  if (entity.getPersistentData().getDouble("atanyaw") >= 0.0) {
                     entity.getPersistentData().putDouble("yaw2", 360.0 - entity.getPersistentData().getDouble("atanyaw"));
                  }
               }
            }

            _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                  entity.getPersistentData().putBoolean("Far", false);
                  entity.getPersistentData().putBoolean("Mid", true);
                  entity.getPersistentData().putBoolean("Close", false);
               }
            }

            _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                  entity.getPersistentData().putBoolean("Mid", false);
                  entity.getPersistentData().putBoolean("Close", false);
               }
            }

            _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                  entity.getPersistentData().putBoolean("Mid", false);
                  entity.getPersistentData().putBoolean("Close", true);
               }
            }

            if (entity.getPersistentData().getDouble("Atk2cd") == 0.0 && entity.getPersistentData().getBoolean("Attack2")) {
               entity.getPersistentData().putDouble("Atk2cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 200, 260));
            } else if (entity.getPersistentData().getDouble("Atk2cd") > 0.0) {
               entity.getPersistentData().putDouble("Atk2cd", entity.getPersistentData().getDouble("Atk2cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Atk3cd") == 0.0 && entity.getPersistentData().getBoolean("Attack3")) {
               entity.getPersistentData().putDouble("Atk3cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 160, 300));
            } else if (entity.getPersistentData().getDouble("Atk3cd") > 0.0) {
               entity.getPersistentData().putDouble("Atk3cd", entity.getPersistentData().getDouble("Atk3cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Atk4cd") == 0.0 && entity.getPersistentData().getBoolean("Attack4")) {
               entity.getPersistentData().putDouble("Atk4cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 240, 340));
            } else if (entity.getPersistentData().getDouble("Atk4cd") > 0.0) {
               entity.getPersistentData().putDouble("Atk4cd", entity.getPersistentData().getDouble("Atk4cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Atk5cd") == 0.0 && entity.getPersistentData().getBoolean("Attack5")) {
               entity.getPersistentData().putDouble("Atk5cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 300, 500));
            } else if (entity.getPersistentData().getDouble("Atk5cd") > 0.0) {
               entity.getPersistentData().putDouble("Atk5cd", entity.getPersistentData().getDouble("Atk5cd") - 1.0);
            }

            if (!entity.getPersistentData().getBoolean("logged")) {
               entity.getPersistentData().putBoolean("logged", true);
               entity.getPersistentData().putBoolean("Log", true);
               entity.getPersistentData().putBoolean("Attacking", true);
            } else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0F)
                  < (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1.0F) * 0.75
               && !entity.getPersistentData().getBoolean("Attacking")) {
               entity.getPersistentData().putBoolean("Unlog", true);
               entity.getPersistentData().putBoolean("Attacking", true);
            }

            if (!entity.getPersistentData().getBoolean("Aggro") && entity instanceof LivingEntity _entity) {
               _entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 10, 4, false, false));
            }

            if (entity.getPersistentData().getBoolean("Far") || entity.getPersistentData().getBoolean("Mid")) {
               if (entity instanceof LivingEntity _entity) {
                  _entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 10, 1, false, false));
               }

               if (entity.getPersistentData().getDouble("Atk5cd") == 0.0 && Math.random() > 0.7 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack5", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               }
            }

            if (entity.getPersistentData().getBoolean("Mid")) {
               if (entity instanceof LivingEntity _entity) {
                  _entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 10, 0, false, false));
               }

               if (entity.getPersistentData().getDouble("Atk3cd") == 0.0
                  && Math.random() >= 0.5
                  && Math.random() <= 0.75
                  && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack3", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (!entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack1", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               }
            }

            if (entity.getPersistentData().getBoolean("Close") && !entity.getPersistentData().getBoolean("Attacking")) {
               if (entity.getPersistentData().getDouble("Atk2cd") == 0.0 && Math.random() > 0.75 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack2", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (entity.getPersistentData().getDouble("Atk3cd") == 0.0
                  && Math.random() >= 0.5
                  && Math.random() <= 0.75
                  && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack3", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (entity.getPersistentData().getDouble("Atk4cd") == 0.0 && !entity.getPersistentData().getBoolean("Attacking") && Math.random() <= 0.25
                  )
                {
                  entity.getPersistentData().putBoolean("Attack4", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (!entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack6", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               }
            }

            if (entity.getPersistentData().getBoolean("Dodge")) {
               if (entity.getPersistentData().getDouble("d") == 0.0) {
                  entity.getPersistentData().putDouble("d", 12.0);
               } else {
                  entity.getPersistentData().putDouble("d", entity.getPersistentData().getDouble("d") - 1.0);
               }

               if (entity.getPersistentData().getDouble("d") == 12.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 12, 2, false, false));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new Notchflash2Entity(SimpleMobsModEntities.NOTCHFLASH_2.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               } else if (entity.getPersistentData().getDouble("d") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putDouble("d", 0.0);
                  entity.getPersistentData().putBoolean("Dodge", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack1")) {
               if (entity.getPersistentData().getDouble("Atk1") == 0.0) {
                  entity.getPersistentData().putDouble("Atk1", 80.0);
               } else {
                  entity.getPersistentData().putDouble("Atk1", entity.getPersistentData().getDouble("Atk1") - 1.0);
               }

               if (entity instanceof LivingEntity _entity) {
                  _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1, 6, false, false));
               }

               if (entity.getPersistentData().getDouble("Atk1") == 80.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.PAPER));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.PAPER));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("focus", true);
               } else if (entity.getPersistentData().getDouble("Atk1") == 55.0) {
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putBoolean("noai", true);
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:notch_sweep"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:notch_sweep"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  entity.getPersistentData().putDouble("i", 5.0);
               } else if (entity.getPersistentData().getDouble("Atk1") <= 54.0 && entity.getPersistentData().getDouble("Atk1") >= 48.0) {
                  if (entity.getPersistentData().getDouble("i") >= 0.0) {
                     entity.getPersistentData().putDouble("direction", Math.abs(entity.getYRot() / 360.0F * 12.0F));
                     entity.getPersistentData()
                        .putDouble("nowdirection", entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("i"));
                     if (entity.getPersistentData().getDouble("nowdirection") > 12.0) {
                        entity.getPersistentData().putDouble("nowdirection", entity.getPersistentData().getDouble("nowdirection") - 12.0);
                     } else if (entity.getPersistentData().getDouble("nowdirection") < 0.0) {
                        entity.getPersistentData().putDouble("nowdirection", entity.getPersistentData().getDouble("nowdirection") + 12.0);
                     }

                     while (i <= 4.0) {
                        if (world instanceof ServerLevel _level) {
                           _level.sendParticles(
                              (SimpleParticleType)SimpleMobsModParticleTypes.STONE_SWING.get(),
                              x + Math.cos((Math.PI / 6) * entity.getPersistentData().getDouble("nowdirection")) * (1.0 + i),
                              y,
                              z + Math.sin((Math.PI / 6) * entity.getPersistentData().getDouble("nowdirection")) * (1.0 + i),
                              2,
                              0.1,
                              0.1,
                              0.1,
                              0.1
                           );
                        }

                        _center = new Vec3(
                           x + Math.cos((Math.PI / 6) * entity.getPersistentData().getDouble("nowdirection")) * (1.0 + i),
                           y,
                           z + Math.sin((Math.PI / 6) * entity.getPersistentData().getDouble("nowdirection")) * (1.0 + i)
                        );

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (!(entityiterator instanceof NotchBossEntity)) {
                              SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                 .setInitialPosition(entity.position())
                                 .setStunType(StunType.LONG)
                                 .setImpact(2.0F)
                                 .setArmorNegation(30.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 36.0F);
                              entityiterator.setDeltaMovement(
                                 new Vec3(
                                    Math.sin(Math.toRadians(entity.getYRot() + 90.0F)) * 2.0, 0.7, Math.cos(Math.toRadians(entity.getYRot() + 270.0F)) * 2.0
                                 )
                              );
                           }
                        }

                        i++;
                     }

                     entity.getPersistentData().putDouble("i", entity.getPersistentData().getDouble("i") - 1.0);
                  }
               } else if (entity.getPersistentData().getDouble("Atk1") == 30.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 3.0, y, z + entity.getLookAngle().z * 3.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof NotchBossEntity)) {
                        entity.getPersistentData().putBoolean("Attack1a", true);
                        entity.getPersistentData().putBoolean("noai", false);
                        entity.getPersistentData().putDouble("Atk1", 0.0);
                        entity.getPersistentData().putBoolean("Attack1", false);
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk1") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("noai", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk1", 0.0);
                  entity.getPersistentData().putBoolean("Attack1", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack1a")) {
               if (entity.getPersistentData().getDouble("Atk1a") == 0.0) {
                  entity.getPersistentData().putDouble("Atk1a", 75.0);
               } else {
                  entity.getPersistentData().putDouble("Atk1a", entity.getPersistentData().getDouble("Atk1a") - 1.0);
               }

               if (entity instanceof LivingEntity _entity) {
                  _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1, 6, false, false));
               }

               if (entity.getPersistentData().getDouble("Atk1a") == 75.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.BLACK_WOOL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.BLACK_WOOL));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("focus", true);
               } else if (entity.getPersistentData().getDouble("Atk1a") == 51.0) {
                  entity.getPersistentData().putBoolean("focus", false);
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:notch_sweep"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:notch_sweep"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  entity.getPersistentData().putBoolean("noai", true);
                  entity.getPersistentData().putDouble("i", 0.0);
               } else if (entity.getPersistentData().getDouble("Atk1a") <= 50.0 && entity.getPersistentData().getDouble("Atk1a") >= 44.0) {
                  if (entity.getPersistentData().getDouble("i") <= 5.0) {
                     entity.getPersistentData().putDouble("direction", Math.abs(entity.getYRot() / 360.0F * 12.0F));
                     entity.getPersistentData()
                        .putDouble("nowdirection", entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("i"));
                     if (entity.getPersistentData().getDouble("nowdirection") > 12.0) {
                        entity.getPersistentData().putDouble("nowdirection", entity.getPersistentData().getDouble("nowdirection") - 12.0);
                     } else if (entity.getPersistentData().getDouble("nowdirection") < 0.0) {
                        entity.getPersistentData().putDouble("nowdirection", entity.getPersistentData().getDouble("nowdirection") + 12.0);
                     }

                     while (i <= 4.0) {
                        if (world instanceof ServerLevel _level) {
                           _level.sendParticles(
                              (SimpleParticleType)SimpleMobsModParticleTypes.STONE_SWING.get(),
                              x + Math.cos((Math.PI / 6) * entity.getPersistentData().getDouble("nowdirection")) * (1.0 + i),
                              y,
                              z + Math.sin((Math.PI / 6) * entity.getPersistentData().getDouble("nowdirection")) * (1.0 + i),
                              2,
                              0.1,
                              0.1,
                              0.1,
                              0.1
                           );
                        }

                        _center = new Vec3(
                           x + Math.cos((Math.PI / 6) * entity.getPersistentData().getDouble("nowdirection")) * (1.0 + i),
                           y,
                           z + Math.sin((Math.PI / 6) * entity.getPersistentData().getDouble("nowdirection")) * (1.0 + i)
                        );

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (!(entityiterator instanceof NotchBossEntity)) {
                              SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                 .setInitialPosition(entity.position())
                                 .setStunType(StunType.LONG)
                                 .setImpact(2.0F)
                                 .setArmorNegation(30.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 36.0F);
                              entityiterator.setDeltaMovement(
                                 new Vec3(
                                    Math.sin(Math.toRadians(entity.getYRot() - 90.0F)) * 2.0, 0.7, Math.cos(Math.toRadians(entity.getYRot() + 90.0F)) * 2.0
                                 )
                              );
                           }
                        }

                        i++;
                     }

                     entity.getPersistentData().putDouble("i", entity.getPersistentData().getDouble("i") + 1.0);
                  }
               } else if (entity.getPersistentData().getDouble("Atk1a") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("noai", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk1a", 0.0);
                  entity.getPersistentData().putBoolean("Attack1a", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack3")) {
               if (entity.getPersistentData().getDouble("Atk3") == 0.0) {
                  entity.getPersistentData().putDouble("Atk3", 121.0);
               } else {
                  entity.getPersistentData().putDouble("Atk3", entity.getPersistentData().getDouble("Atk3") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk3") == 121.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 6, false, false));
                  }

                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.GRAY_WOOL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.GRAY_WOOL));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("focus", true);
               } else if (entity.getPersistentData().getDouble("Atk3") == 102.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 2, false, false));
                  }

                  entity.getPersistentData().putDouble("i", 14.0);
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:notch_spin"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:notch_spin"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F,
                           false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk3") <= 101.0 && entity.getPersistentData().getDouble("Atk3") >= 59.0) {
                  if (!(entity.getPersistentData().getDouble("i") >= 0.0)) {
                     entity.getPersistentData().putDouble("i", 14.0);
                  } else {
                     entity.getPersistentData().putDouble("direction", Math.abs(entity.getYRot() / 360.0F * 14.0F));
                     entity.getPersistentData()
                        .putDouble("nowdirection", entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("i"));
                     if (entity.getPersistentData().getDouble("nowdirection") > 14.0) {
                        entity.getPersistentData().putDouble("nowdirection", entity.getPersistentData().getDouble("nowdirection") - 14.0);
                     } else if (entity.getPersistentData().getDouble("nowdirection") < 0.0) {
                        entity.getPersistentData().putDouble("nowdirection", entity.getPersistentData().getDouble("nowdirection") + 14.0);
                     }

                     while (i <= 4.0) {
                        if (world instanceof ServerLevel _level) {
                           _level.sendParticles(
                              (SimpleParticleType)SimpleMobsModParticleTypes.STONE_SWING.get(),
                              x + Math.cos((Math.PI / 7) * entity.getPersistentData().getDouble("nowdirection")) * (1.0 + i),
                              y,
                              z + Math.sin((Math.PI / 7) * entity.getPersistentData().getDouble("nowdirection")) * (1.0 + i),
                              2,
                              0.5,
                              0.5,
                              0.5,
                              0.1
                           );
                        }

                        _center = new Vec3(
                           x + Math.cos((Math.PI / 7) * entity.getPersistentData().getDouble("nowdirection")) * (1.0 + i),
                           y,
                           z + Math.sin((Math.PI / 7) * entity.getPersistentData().getDouble("nowdirection")) * (1.0 + i)
                        );

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (!(entityiterator instanceof NotchBossEntity)) {
                              SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                 .setInitialPosition(entity.position())
                                 .setStunType(StunType.LONG)
                                 .setImpact(2.0F)
                                 .setArmorNegation(30.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 26.0F);
                              entityiterator.setDeltaMovement(
                                 new Vec3(
                                    Math.sin(Math.toRadians(entity.getYRot() + 90.0F)) * 2.0, 0.7, Math.cos(Math.toRadians(entity.getYRot() + 270.0F)) * 2.0
                                 )
                              );
                           }
                        }

                        i++;
                     }

                     entity.getPersistentData().putDouble("i", entity.getPersistentData().getDouble("i") - 1.0);
                  }
               } else if (entity.getPersistentData().getDouble("Atk3") == 58.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 58, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk3") == 2.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.GRAY_DYE));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GRAY_DYE));
                  }
               } else if (entity.getPersistentData().getDouble("Atk3") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("noai", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk3", 0.0);
                  entity.getPersistentData().putBoolean("Attack3", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack2")) {
               if (entity.getPersistentData().getDouble("Atk2") == 0.0) {
                  entity.getPersistentData().putDouble("Atk2", 90.0);
               } else {
                  entity.getPersistentData().putDouble("Atk2", entity.getPersistentData().getDouble("Atk2") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk2") == 90.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 50, 6, false, false));
                  }

                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.BLACK_CONCRETE));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.BLACK_CONCRETE));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("focus", true);
               } else if (entity.getPersistentData().getDouble("Atk2") <= 80.0 && entity.getPersistentData().getDouble("Atk2") >= 50.0) {
                  entity.setDeltaMovement(
                     new Vec3(
                        Math.sin(Math.toRadians(entity.getYRot())) * 0.07,
                        entity.getDeltaMovement().y(),
                        Math.cos(Math.toRadians(entity.getYRot() + 180.0F)) * 0.07
                     )
                  );
               } else if (entity.getPersistentData().getDouble("Atk2") == 46.0) {
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putBoolean("noai", true);
               } else if (entity.getPersistentData().getDouble("Atk2") <= 44.0 && entity.getPersistentData().getDouble("Atk2") >= 42.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:crumble"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("simple_mobs:crumble"), SoundSource.NEUTRAL, 3.0F, 1.0F, false
                        );
                     }
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           0.3F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           0.3F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new Notchgroundeffect2Entity(SimpleMobsModEntities.NOTCHGROUNDEFFECT_2.get(), _level);
                     entityToSpawn.moveTo(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.CAMPFIRE_COSY_SMOKE, x + entity.getLookAngle().x * 3.5, y, z + entity.getLookAngle().z * 3.5, 15, 2.0, 2.0, 3.0, 1.0
                     );
                  }

                  while (i <= 4.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * i, y, z + entity.getLookAngle().z * i);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof NotchBossEntity)) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.KNOCKDOWN)
                              .setImpact(15.0F)
                              .setArmorNegation(30.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 58.0F);
                        }
                     }

                     i++;
                  }
               } else if (entity.getPersistentData().getDouble("Atk2") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("noai", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk2", 0.0);
                  entity.getPersistentData().putBoolean("Attack2", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack4")) {
               if (entity.getPersistentData().getDouble("Atk4") == 0.0) {
                  entity.getPersistentData().putDouble("Atk4", 80.0);
               } else {
                  entity.getPersistentData().putDouble("Atk4", entity.getPersistentData().getDouble("Atk4") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk4") == 80.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 6, false, false));
                  }

                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.GRAY_CONCRETE));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.GRAY_CONCRETE));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("focus", true);
               } else if (entity.getPersistentData().getDouble("Atk4") == 47.0) {
                  entity.getPersistentData().putBoolean("focus", false);
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_notch_boss,limit=1,sort=nearest] {NoAI:1b}"
                        );
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:crumble"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("simple_mobs:crumble"), SoundSource.NEUTRAL, 2.0F, 1.0F, false
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
                           0.5F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.5F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new Notchgroundeffect1Entity(SimpleMobsModEntities.NOTCHGROUNDEFFECT_1.get(), _level);
                     entityToSpawn.moveTo(x + entity.getLookAngle().x * 1.2, y, z + entity.getLookAngle().z * 1.2, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x + entity.getLookAngle().x * 1.2, y, z + entity.getLookAngle().z * 1.2, 50, 1.0, 1.0, 1.0, 0.3
                     );
                  }
               } else if (entity.getPersistentData().getDouble("Atk4") <= 46.0 && entity.getPersistentData().getDouble("Atk4") >= 44.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 1.2, y, z + entity.getLookAngle().z * 1.2);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof NotchBossEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(15.0F)
                           .setArmorNegation(40.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 40.0F);
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk4") == 1.0) {
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
                           "/data merge entity @e[type=deepnight:dawn_notch_boss,limit=1,sort=nearest] {NoAI:0}"
                        );
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk4", 0.0);
                  entity.getPersistentData().putBoolean("Attack4", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack5")) {
               if (entity.getPersistentData().getDouble("Atk5") == 0.0) {
                  entity.getPersistentData().putDouble("Atk5", 70.0);
               } else {
                  entity.getPersistentData().putDouble("Atk5", entity.getPersistentData().getDouble("Atk5") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk5") == 70.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 70, 6, false, false));
                  }

                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.BLUE_WOOL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.BLUE_WOOL));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new Notchflash2Entity(SimpleMobsModEntities.NOTCHFLASH_2.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               } else if (entity.getPersistentData().getDouble("Atk5") == 68.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(30.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        Entity _ent = entity;
                        _ent.teleportTo(
                           entityiterator.getX() + entityiterator.getLookAngle().x,
                           entityiterator.getY() + 8.0,
                           entityiterator.getZ() + entityiterator.getLookAngle().z
                        );
                        if (_ent instanceof ServerPlayer _serverPlayer) {
                           _serverPlayer.connection
                              .teleport(
                                 entityiterator.getX() + entityiterator.getLookAngle().x,
                                 entityiterator.getY() + 8.0,
                                 entityiterator.getZ() + entityiterator.getLookAngle().z,
                                 _ent.getYRot(),
                                 _ent.getXRot()
                              );
                        }

                        entity.setNoGravity(true);
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk5") == 62.0) {
                  entity.setNoGravity(false);
               } else if (entity.getPersistentData().getDouble("Atk5") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk5", 0.0);
                  entity.getPersistentData().putBoolean("Attack5", false);
               }

               entity.fallDistance = 0.0F;
               if (entity.getPersistentData().getDouble("Atk5") < 63.0 && entity.onGround()) {
                  entity.getPersistentData().putBoolean("Attack5g", true);
                  entity.getPersistentData().putDouble("Atk5", 0.0);
                  entity.getPersistentData().putBoolean("Attack5", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack5g")) {
               if (entity.getPersistentData().getDouble("Atk5g") == 0.0) {
                  entity.getPersistentData().putDouble("Atk5g", 60.0);
               } else {
                  entity.getPersistentData().putDouble("Atk5g", entity.getPersistentData().getDouble("Atk5g") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk5g") == 60.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 6, false, false));
                  }

                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.BLUE_CONCRETE));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.BLUE_CONCRETE));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("noai", true);
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:martian.attack4"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:martian.attack4"),
                           SoundSource.NEUTRAL,
                           3.0F,
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
                           dev.deepnight.SafeSounds.resolve("simple_mobs:crumble"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("simple_mobs:crumble"), SoundSource.NEUTRAL, 3.0F, 1.0F, false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new Notchgroundeffect3Entity(SimpleMobsModEntities.NOTCHGROUNDEFFECT_3.get(), _level);
                     entityToSpawn.moveTo(x + entity.getLookAngle().x * 1.2, y, z + entity.getLookAngle().z * 1.2, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new AirEntity(SimpleMobsModEntities.AIR.get(), _level);
                     entityToSpawn.moveTo(x + entity.getLookAngle().x * 1.2, y, z + entity.getLookAngle().z * 1.2, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x + entity.getLookAngle().x * 1.2, y, z + entity.getLookAngle().z * 1.2, 50, 1.0, 1.0, 1.0, 0.3
                     );
                  }
               } else if (entity.getPersistentData().getDouble("Atk5g") <= 59.0 && entity.getPersistentData().getDouble("Atk5g") >= 57.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 1.2, y, z + entity.getLookAngle().z * 1.2);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(8.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof NotchBossEntity)
                        && !(entityiterator instanceof Notchgroundeffect3Entity)
                        && !(entityiterator instanceof AirEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(4.0F)
                           .setArmorNegation(45.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 70.0F);
                        entityiterator.setDeltaMovement(
                           new Vec3((entityiterator.getX() - entity.getX()) * 0.3, 0.5, (entityiterator.getZ() - entity.getZ()) * 0.3)
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk5g") == 56.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_notch_boss,limit=1,sort=nearest] {NoAI:1b}"
                        );
                  }
               } else if (entity.getPersistentData().getDouble("Atk5g") == 1.0) {
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
                           "/data merge entity @e[type=deepnight:dawn_notch_boss,limit=1,sort=nearest] {NoAI:0}"
                        );
                  }

                  entity.getPersistentData().putBoolean("noai", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk5g", 0.0);
                  entity.getPersistentData().putBoolean("Attack5g", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack6")) {
               if (entity.getPersistentData().getDouble("Atk6") == 0.0) {
                  entity.getPersistentData().putDouble("Atk6", 50.0);
               } else {
                  entity.getPersistentData().putDouble("Atk6", entity.getPersistentData().getDouble("Atk6") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk6") == 50.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 50, 6, false, false));
                  }

                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.MAGENTA_WOOL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.MAGENTA_WOOL));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("focus", true);
               } else if (entity.getPersistentData().getDouble("Atk6") == 42.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_wing_flap"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           0.8F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_wing_flap"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           0.8F,
                           false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk6") == 38.0) {
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putBoolean("noai", true);
               } else if (entity.getPersistentData().getDouble("Atk6") <= 37.0 && entity.getPersistentData().getDouble("Atk6") >= 35.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 1.0, y, z + entity.getLookAngle().z * 1.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof NotchBossEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.SHORT)
                           .setImpact(15.0F)
                           .setArmorNegation(35.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 40.0F);
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk6") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("noai", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk6", 0.0);
                  entity.getPersistentData().putBoolean("Attack6", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Log")) {
               if (entity.getPersistentData().getDouble("l") == 0.0) {
                  entity.getPersistentData().putDouble("l", 140.0);
               } else {
                  entity.getPersistentData().putDouble("l", entity.getPersistentData().getDouble("l") - 1.0);
               }

               if (entity.getPersistentData().getDouble("l") <= 120.0 && entity.getPersistentData().getDouble("l") >= 80.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL,
                                 new Vec3(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z),
                                 Vec2.ZERO,
                                 _level,
                                 4,
                                 "",
                                 Component.literal(""),
                                 _level.getServer(),
                                 null
                              )
                              .withSuppressedOutput(),
                           "/particle minecraft:block minecraft:blackstone ~ ~ ~ 1 1 1 0.1 30 normal"
                        );
                  }
               } else if (entity.getPersistentData().getDouble("l") <= 80.0
                  && entity.getPersistentData().getDouble("l") >= 60.0
                  && world instanceof ServerLevel _level) {
                  _level.getServer()
                     .getCommands()
                     .performPrefixedCommand(
                        new CommandSourceStack(
                              CommandSource.NULL,
                              new Vec3(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0),
                              Vec2.ZERO,
                              _level,
                              4,
                              "",
                              Component.literal(""),
                              _level.getServer(),
                              null
                           )
                           .withSuppressedOutput(),
                        "/particle minecraft:block minecraft:blackstone ~ ~ ~ 1.4 1.4 1.4 0.1 30 normal"
                     );
               }

               if (entity.getPersistentData().getDouble("l") == 140.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.GREEN_CONCRETE));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.GREEN_CONCRETE));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_notch_boss,limit=1,sort=nearest] {NoAI:1b}"
                        );
                  }
               } else if (entity.getPersistentData().getDouble("l") == 70.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_notch_boss,limit=1,sort=nearest] {NoAI:0}"
                        );
                  }

                  entity.getPersistentData().putBoolean("focus", true);
               } else if (entity.getPersistentData().getDouble("l") == 53.0) {
                  entity.getPersistentData().putBoolean("focus", false);
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_notch_boss,limit=1,sort=nearest] {NoAI:1b}"
                        );
                  }
               } else if (entity.getPersistentData().getDouble("l") == 51.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:crumble"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("simple_mobs:crumble"), SoundSource.NEUTRAL, 2.0F, 1.0F, false
                        );
                     }
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.5F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.5F,
                           false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("l") <= 50.0 && entity.getPersistentData().getDouble("l") >= 48.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.CAMPFIRE_COSY_SMOKE, x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0, 40, 2.0, 2.0, 2.0, 1.0
                     );
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new Notchgroundeffect2Entity(SimpleMobsModEntities.NOTCHGROUNDEFFECT_2.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  while (i <= 4.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * i, y, z + entity.getLookAngle().z * i);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof NotchBossEntity)) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.KNOCKDOWN)
                              .setImpact(15.0F)
                              .setArmorNegation(30.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 58.0F);
                        }
                     }

                     i++;
                  }
               } else if (entity.getPersistentData().getDouble("l") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(2, new ItemStack(Items.LEATHER_CHESTPLATE));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
                  }

                  Entity _entityx = entity;
                  if (_entityx instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entityx instanceof LivingEntity _living) {
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
                           "/data merge entity @e[type=deepnight:dawn_notch_boss,limit=1,sort=nearest] {NoAI:0}"
                        );
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("l", 0.0);
                  entity.getPersistentData().putBoolean("Log", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Unlog")) {
               if (entity.getPersistentData().getDouble("ul") == 0.0) {
                  entity.getPersistentData().putDouble("ul", 90.0);
               } else {
                  entity.getPersistentData().putDouble("ul", entity.getPersistentData().getDouble("ul") - 1.0);
               }

               if (entity.getPersistentData().getDouble("ul") == 90.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.POPPY));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.POPPY));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 22, 6, false, false));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
               } else if (entity.getPersistentData().getDouble("ul") == 72.0) {
                  entity.getPersistentData().putBoolean("focus", false);
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_notch_boss,limit=1,sort=nearest] {NoAI:1b}"
                        );
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_notch_boss,sort=nearest,limit=1] {Invulnerable:1b}"
                        );
                  }
               } else if (entity.getPersistentData().getDouble("ul") == 68.0) {
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new Notchgroundeffect4Entity(SimpleMobsModEntities.NOTCHGROUNDEFFECT_4.get(), _level);
                     entityToSpawn.moveTo(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z, world.getRandom().nextFloat() * 360.0F, 0.0F);
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:crumble"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("simple_mobs:crumble"), SoundSource.NEUTRAL, 3.0F, 1.0F, false
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
                           3.0F,
                           0.5F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           0.5F,
                           false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("ul") == 32.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.zombie.break_wooden_door"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           0.3F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.zombie.break_wooden_door"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           0.3F,
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
                           0.5F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.dragon_fireball.explode"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           0.5F,
                           false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("ul") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  Entity _entityx = entity;
                  if (_entityx instanceof Player _player) {
                     _player.getInventory().armor.set(2, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entityx instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Blocks.AIR));
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_notch_boss,sort=nearest,limit=1] {Invulnerable:0}"
                        );
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_notch_boss,limit=1,sort=nearest] {NoAI:0}"
                        );
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("ul", 0.0);
                  entity.getPersistentData().putBoolean("removelog", true);
                  entity.getPersistentData().putBoolean("Unlog", false);
               }

               if (entity.getPersistentData().getDouble("ul") <= 68.0 && entity.getPersistentData().getDouble("ul") >= 66.0) {
                  _center = new Vec3(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof NotchBossEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.SHORT)
                           .setImpact(15.0F)
                           .setArmorNegation(30.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 25.0F);
                     }
                  }
               }

               if (entity.getPersistentData().getDouble("ul") <= 32.0 && entity.getPersistentData().getDouble("ul") >= 30.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.CAMPFIRE_COSY_SMOKE, x + entity.getLookAngle().x, y, z + entity.getLookAngle().z, 20, 2.0, 2.0, 2.0, 1.0
                     );
                  }

                  _center = new Vec3(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof NotchBossEntity) && !(entityiterator instanceof Notchgroundeffect4Entity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.KNOCKDOWN)
                           .setImpact(2.0F)
                           .setArmorNegation(30.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 70.0F);
                        entityiterator.setDeltaMovement(new Vec3(entityiterator.getX() - entity.getX(), 1.2, entityiterator.getZ() - entity.getZ()));
                     }
                  }
               }
            }
         }
      }
   }
}
