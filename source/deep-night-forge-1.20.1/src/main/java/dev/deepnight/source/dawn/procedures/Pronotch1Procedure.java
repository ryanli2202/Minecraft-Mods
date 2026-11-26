package dev.deepnight.source.dawn.procedures;


import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.AirEntity;
import dev.deepnight.source.dawn.entity.ElementalFlash1Entity;
import dev.deepnight.source.dawn.entity.ElementalSpawnEntity;
import dev.deepnight.source.dawn.entity.NotchBossEntity;
import dev.deepnight.source.dawn.entity.NotchGrabEntity;
import dev.deepnight.source.dawn.entity.NotchPunchEntity;
import dev.deepnight.source.dawn.entity.NotchStompeffectEntity;
import dev.deepnight.source.dawn.entity.Notchflash1Entity;
import dev.deepnight.source.dawn.entity.Notchflash3Entity;
import dev.deepnight.source.dawn.entity.Notchflash4Entity;
import dev.deepnight.source.dawn.entity.Notchgroundeffect1Entity;
import dev.deepnight.source.dawn.entity.Notchgroundeffect3Entity;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.network.SimpleMobsModVariables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import dev.deepnight.source.SourceDamage;
import dev.deepnight.source.SourceDamage.StunType;

public class Pronotch1Procedure {
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

         if (!world.isClientSide() && entity.isAlive()) {
            if (entity.getPersistentData().getDouble("bbtimer") != 0.0) {
               entity.getPersistentData().putDouble("bbtimer", entity.getPersistentData().getDouble("bbtimer") - 1.0);
            } else {
               entity.getPersistentData().putDouble("bbtimer", 80.0);
               SimpleMobsModVariables.MapVariables.get(world).i = -1.0;
               SimpleMobsModVariables.MapVariables.get(world).syncData(world);
               SimpleMobsModVariables.MapVariables.get(world).j = 1.0;
               SimpleMobsModVariables.MapVariables.get(world).syncData(world);
               SimpleMobsModVariables.MapVariables.get(world).x = -1.0;
               SimpleMobsModVariables.MapVariables.get(world).syncData(world);

               while (SimpleMobsModVariables.MapVariables.get(world).i < 1.0) {
                  while (SimpleMobsModVariables.MapVariables.get(world).j < 3.0) {
                     while (SimpleMobsModVariables.MapVariables.get(world).x < 1.0) {
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
                  entity.getPersistentData().putBoolean("Mid", false);
                  entity.getPersistentData().putBoolean("Close", false);
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

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(12.0), e -> true)
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
               }
            }

            _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                  entity.getPersistentData().putBoolean("Far", false);
                  entity.getPersistentData().putBoolean("Close", true);
               }
            }

            if (entity.getPersistentData().getDouble("Atk1cd") == 0.0 && entity.getPersistentData().getBoolean("Attack1")) {
               entity.getPersistentData().putDouble("Atk1cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 200, 260));
            } else if (entity.getPersistentData().getDouble("Atk1cd") > 0.0) {
               entity.getPersistentData().putDouble("Atk1cd", entity.getPersistentData().getDouble("Atk1cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Atk2cd") == 0.0 && entity.getPersistentData().getBoolean("Attack2")) {
               entity.getPersistentData().putDouble("Atk2cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 200, 260));
            } else if (entity.getPersistentData().getDouble("Atk2cd") > 0.0) {
               entity.getPersistentData().putDouble("Atk2cd", entity.getPersistentData().getDouble("Atk2cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Atk4cd") == 0.0 && entity.getPersistentData().getBoolean("Attack4")) {
               entity.getPersistentData().putDouble("Atk4cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 240, 340));
            } else if (entity.getPersistentData().getDouble("Atk4cd") > 0.0) {
               entity.getPersistentData().putDouble("Atk4cd", entity.getPersistentData().getDouble("Atk4cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Atk5cd") == 0.0 && entity.getPersistentData().getBoolean("Attack5")) {
               entity.getPersistentData().putDouble("Atk5cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 240, 340));
            } else if (entity.getPersistentData().getDouble("Atk5cd") > 0.0) {
               entity.getPersistentData().putDouble("Atk5cd", entity.getPersistentData().getDouble("Atk5cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Atk7cd") == 0.0 && entity.getPersistentData().getBoolean("Attack7")) {
               entity.getPersistentData().putDouble("Atk7cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 240, 340));
            } else if (entity.getPersistentData().getDouble("Atk7cd") > 0.0) {
               entity.getPersistentData().putDouble("Atk7cd", entity.getPersistentData().getDouble("Atk7cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Atk8cd") == 0.0 && entity.getPersistentData().getBoolean("Attack8")) {
               entity.getPersistentData().putDouble("Atk8cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 240, 340));
            } else if (entity.getPersistentData().getDouble("Atk8cd") > 0.0) {
               entity.getPersistentData().putDouble("Atk8cd", entity.getPersistentData().getDouble("Atk8cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("strafecd") != 0.0
               || !entity.getPersistentData().getBoolean("Strafe1") && !entity.getPersistentData().getBoolean("Strafe2")) {
               if (entity.getPersistentData().getDouble("strafecd") > 0.0) {
                  entity.getPersistentData().putDouble("strafecd", entity.getPersistentData().getDouble("strafecd") - 1.0);
               }
            } else {
               entity.getPersistentData().putDouble("strafecd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 100, 200));
            }

            if (entity.getPersistentData().getBoolean("Mid") && entity instanceof LivingEntity _entity) {
               _entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 10, 1, false, false));
            }

            if (!entity.getPersistentData().getBoolean("Aggro") && entity instanceof LivingEntity _entity) {
               _entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 10, 4, false, false));
            }

            if (entity.getPersistentData().getBoolean("Mid")) {
               if (entity instanceof LivingEntity _entity) {
                  _entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 10, 1, false, false));
               }

               if (entity.getPersistentData().getDouble("Atk2cd") == 0.0 && Math.random() > 0.7 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack2", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (entity.getPersistentData().getDouble("Atk4cd") == 0.0 && Math.random() > 0.4 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack4", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (entity.getPersistentData().getDouble("Atk7cd") == 0.0 && Math.random() > 0.4 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack7", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (entity.getPersistentData().getDouble("Atk8cd") == 0.0 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack8", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               }
            }

            if (entity.getPersistentData().getBoolean("Close") && !entity.getPersistentData().getBoolean("Attacking")) {
               if (entity.getPersistentData().getDouble("Atk1cd") == 0.0 && Math.random() > 0.7 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack1", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (entity.getPersistentData().getDouble("Atk5cd") == 0.0 && Math.random() > 0.4 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack5", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (Math.random() < 0.3 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack3", true);
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
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 12, 6, false, false));
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new Notchflash1Entity(SimpleMobsModEntities.NOTCHFLASH_1.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("focus", true);
               } else if (entity.getPersistentData().getDouble("d") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("d", 0.0);
                  entity.getPersistentData().putBoolean("Dodge", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack1")) {
               if (entity.getPersistentData().getDouble("Atk1") == 0.0) {
                  entity.getPersistentData().putDouble("Atk1", 55.0);
               } else {
                  entity.getPersistentData().putDouble("Atk1", entity.getPersistentData().getDouble("Atk1") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk1") == 55.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 15, 6, false, false));
                  }

                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.LEATHER_HELMET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("focus", true);
               } else if (entity.getPersistentData().getDouble("Atk1") == 32.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 32, 6, false, false));
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:notch_punch"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:notch_punch"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  _center = new Vec3(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        if (entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.STUN.get(), 60, 1, false, false));
                        }

                        entity.getPersistentData().putBoolean("Attack1Grabbed", true);
                        entityiterator.getPersistentData().putBoolean("notch_grabbed", true);
                        entity.getPersistentData().putBoolean("focus", false);
                        entity.getPersistentData().putDouble("Atk1", 0.0);
                        entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                        entity.getPersistentData().putDouble("pitch", entity.getPersistentData().getDouble("pitch2"));
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

                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk1", 0.0);
                  entity.getPersistentData().putBoolean("Attack1", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack1Grabbed")) {
               if (entity.getPersistentData().getDouble("Atk1g") == 0.0) {
                  entity.getPersistentData().putDouble("Atk1g", 65.0);
               } else {
                  entity.getPersistentData().putDouble("Atk1g", entity.getPersistentData().getDouble("Atk1g") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk1g") == 65.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 62, 6, false, false));
                  }

                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.GOLD_NUGGET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLD_NUGGET));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("lock", true);
                  entity.getPersistentData().putBoolean("noai", true);
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new NotchGrabEntity(SimpleMobsModEntities.NOTCH_GRAB.get(), _level);
                     entityToSpawn.moveTo(x + entity.getLookAngle().x * 1.5, y + 0.5, z + entity.getLookAngle().z * 1.5, 0.0F, 0.0F);
                     entityToSpawn.setYBodyRot(0.0F);
                     entityToSpawn.setYHeadRot(0.0F);
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
                           dev.deepnight.SafeSounds.resolve("entity.turtle.egg_crack"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.turtle.egg_crack"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator.getPersistentData().getBoolean("notch_grabbed")) {
                        Entity _ent = entityiterator;
                        _ent.teleportTo(x + entity.getLookAngle().x * 1.5, y + 0.5, z + entity.getLookAngle().z * 1.5);
                        if (_ent instanceof ServerPlayer _serverPlayer) {
                           _serverPlayer.connection
                              .teleport(x + entity.getLookAngle().x * 1.5, y + 0.5, z + entity.getLookAngle().z * 1.5, _ent.getYRot(), _ent.getXRot());
                        }
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk1g") == 48.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator instanceof NotchGrabEntity) {
                        Entity _ent = entityiterator;
                        _ent.teleportTo(x + entity.getLookAngle().x * 1.5, y + 2.0, z + entity.getLookAngle().z * 1.5);
                        if (_ent instanceof ServerPlayer _serverPlayer) {
                           _serverPlayer.connection
                              .teleport(x + entity.getLookAngle().x * 1.5, y + 2.0, z + entity.getLookAngle().z * 1.5, _ent.getYRot(), _ent.getXRot());
                        }
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk1g") == 38.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator instanceof NotchGrabEntity) {
                        Entity _ent = entityiterator;
                        _ent.teleportTo(x - entity.getLookAngle().x * 2.0, y + 1.0, z - entity.getLookAngle().z * 2.0);
                        if (_ent instanceof ServerPlayer _serverPlayer) {
                           _serverPlayer.connection
                              .teleport(x - entity.getLookAngle().x * 2.0, y + 1.0, z - entity.getLookAngle().z * 2.0, _ent.getYRot(), _ent.getXRot());
                        }
                     }

                     if (entityiterator.getPersistentData().getBoolean("notch_grabbed")) {
                        Entity _ent = entityiterator;
                        _ent.teleportTo(x - entity.getLookAngle().x * 0.5, y + 3.0, z - entity.getLookAngle().z * 0.5);
                        if (_ent instanceof ServerPlayer _serverPlayer) {
                           _serverPlayer.connection
                              .teleport(x - entity.getLookAngle().x * 0.5, y + 3.0, z - entity.getLookAngle().z * 0.5, _ent.getYRot(), _ent.getXRot());
                        }
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk1g") != 34.0) {
                  if (entity.getPersistentData().getDouble("Atk1g") == 30.0) {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator instanceof NotchGrabEntity && !entityiterator.level().isClientSide()) {
                           entityiterator.discard();
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk1g") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("noai", false);
                     entity.getPersistentData().putBoolean("lock", false);
                     Entity _ent = entity;
                     _ent.setYRot(entity.getYRot() + 180.0F);
                     _ent.setXRot(0.0F);
                     _ent.setYBodyRot(_ent.getYRot());
                     _ent.setYHeadRot(_ent.getYRot());
                     _ent.yRotO = _ent.getYRot();
                     _ent.xRotO = _ent.getXRot();
                     if (_ent instanceof LivingEntity _entityx) {
                        _entityx.yBodyRotO = _entityx.getYRot();
                        _entityx.yHeadRotO = _entityx.getYRot();
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("Atk1g", 0.0);
                     entity.getPersistentData().putBoolean("Attack1Grabbed", false);
                  }
               } else {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator instanceof NotchGrabEntity) {
                        Entity _ent = entityiterator;
                        _ent.teleportTo(x - entity.getLookAngle().x * 2.0, y, z - entity.getLookAngle().z * 2.0);
                        if (_ent instanceof ServerPlayer _serverPlayer) {
                           _serverPlayer.connection
                              .teleport(x - entity.getLookAngle().x * 2.0, y, z - entity.getLookAngle().z * 2.0, _ent.getYRot(), _ent.getXRot());
                        }
                     }

                     if (entityiterator.getPersistentData().getBoolean("notch_grabbed")) {
                        Entity _ent = entityiterator;
                        _ent.teleportTo(x - entity.getLookAngle().x * 2.0, y, z - entity.getLookAngle().z * 2.0);
                        if (_ent instanceof ServerPlayer _serverPlayer) {
                           _serverPlayer.connection
                              .teleport(x - entity.getLookAngle().x * 2.0, y, z - entity.getLookAngle().z * 2.0, _ent.getYRot(), _ent.getXRot());
                        }

                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.KNOCKDOWN)
                           .setImpact(2.0F)
                           .setArmorNegation(40.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 30.0F);
                        entityiterator.getPersistentData().putBoolean("notch_grabbed", false);
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
                     Entity entityToSpawn = new Notchgroundeffect1Entity(SimpleMobsModEntities.NOTCHGROUNDEFFECT_1.get(), _level);
                     entityToSpawn.moveTo(x - entity.getLookAngle().x * 1.5, y, z - entity.getLookAngle().z * 1.5, world.getRandom().nextFloat() * 360.0F, 0.0F);
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack2")) {
               if (entity.getPersistentData().getDouble("Atk2") == 0.0) {
                  entity.getPersistentData().putDouble("Atk2", 50.0);
               } else {
                  entity.getPersistentData().putDouble("Atk2", entity.getPersistentData().getDouble("Atk2") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk2") == 50.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.COAL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.COAL));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 50, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk2") == 41.0) {
                  entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putDouble("pitch", entity.getXRot());
               } else if (entity.getPersistentData().getDouble("Atk2") == 40.0) {
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putBoolean("lock", true);
                  entity.getPersistentData().putBoolean("noai", true);
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new Notchflash1Entity(SimpleMobsModEntities.NOTCHFLASH_1.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), entity.getXRot());
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new Notchflash4Entity(SimpleMobsModEntities.NOTCHFLASH_4.get(), _level);
                     entityToSpawn.moveTo(x - entity.getLookAngle().x, y, z - entity.getLookAngle().z, entity.getYRot(), entity.getXRot());
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
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
                           dev.deepnight.SafeSounds.resolve("entity.lightning_bolt.thunder"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.5F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.lightning_bolt.thunder"),
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
                           dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
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
                           dev.deepnight.SafeSounds.resolve("entity.polar_bear.warning"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.3F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.polar_bear.warning"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.3F,
                           false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk2") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("noai", false);
                  entity.getPersistentData().putDouble("Atk2", 0.0);
                  entity.getPersistentData().putBoolean("Attack2", false);
               }

               if (entity.getPersistentData().getDouble("Atk2") <= 40.0 && entity.getPersistentData().getDouble("Atk2") >= 32.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.FLASH, x, y, z, 1, 0.1, 0.1, 0.1, 0.0);
                  }

                  entity.setDeltaMovement(
                     new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)), entity.getDeltaMovement().y(), Math.cos(Math.toRadians(entity.getYRot())))
                  );
                  _center = new Vec3(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof NotchBossEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.SHORT)
                           .setImpact(5.0F)
                           .setArmorNegation(30.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 28.0F);
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack3")) {
               if (entity.getPersistentData().getDouble("Atk3") == 0.0) {
                  entity.getPersistentData().putDouble("Atk3", 40.0);
               } else {
                  entity.getPersistentData().putDouble("Atk3", entity.getPersistentData().getDouble("Atk3") - 1.0);
               }

               if (entity instanceof LivingEntity _entity) {
                  _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1, 6, false, false));
               }

               if (entity.getPersistentData().getDouble("Atk3") == 40.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.CHARCOAL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHARCOAL));
                  }
               } else if (entity.getPersistentData().getDouble("Atk3") <= 30.0 && entity.getPersistentData().getDouble("Atk3") >= 29.0) {
                  entity.setDeltaMovement(
                     new Vec3(
                        Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 0.5,
                        entity.getDeltaMovement().y(),
                        Math.cos(Math.toRadians(entity.getYRot())) * 0.5
                     )
                  );
               } else if (entity.getPersistentData().getDouble("Atk3") == 18.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        entity.getPersistentData().putBoolean("Attack3a", true);
                        entity.getPersistentData().putBoolean("Attack3", false);
                        entity.getPersistentData().putDouble("Atk3a", 0.0);
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk3") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk3", 0.0);
                  entity.getPersistentData().putBoolean("Attack3", false);
               }

               if (entity.getPersistentData().getDouble("Atk3") == 30.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:notch_punch"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:notch_punch"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  _center = new Vec3(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.SHORT)
                           .setImpact(2.0F)
                           .setArmorNegation(60.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 20.0F);
                        if (world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new Notchflash3Entity(SimpleMobsModEntities.NOTCHFLASH_3.get(), _level);
                           entityToSpawn.moveTo(x, y, z, entity.getYRot(), (float)Mth.nextDouble(net.minecraft.util.RandomSource.create(), -50.0, 50.0));
                           entityToSpawn.setYBodyRot(entity.getYRot());
                           entityToSpawn.setYHeadRot(entity.getYRot());
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }
                     }
                  }
               }

               if (entity.getPersistentData().getBoolean("hurt")) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk3", 0.0);
                  entity.getPersistentData().putBoolean("Attack3", false);
                  entity.getPersistentData().putBoolean("hurt", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack3a")) {
               if (entity.getPersistentData().getDouble("Atk3a") == 0.0) {
                  entity.getPersistentData().putDouble("Atk3a", 30.0);
               } else {
                  entity.getPersistentData().putDouble("Atk3a", entity.getPersistentData().getDouble("Atk3a") - 1.0);
               }

               if (entity instanceof LivingEntity _entity) {
                  _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1, 6, false, false));
               }

               if (entity.getPersistentData().getDouble("Atk3a") == 30.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.IRON_INGOT));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_INGOT));
                  }
               } else if (entity.getPersistentData().getDouble("Atk3a") <= 29.0 && entity.getPersistentData().getDouble("Atk3a") >= 28.0) {
                  entity.setDeltaMovement(
                     new Vec3(
                        Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 0.5,
                        entity.getDeltaMovement().y(),
                        Math.cos(Math.toRadians(entity.getYRot())) * 0.5
                     )
                  );
               } else if (entity.getPersistentData().getDouble("Atk3a") == 18.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        entity.getPersistentData().putBoolean("Attack3b", true);
                        entity.getPersistentData().putBoolean("Attack3a", false);
                        entity.getPersistentData().putDouble("Atk3a", 0.0);
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk3a") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk3a", 0.0);
                  entity.getPersistentData().putBoolean("Attack3a", false);
               }

               if (entity.getPersistentData().getDouble("Atk3a") == 28.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:notch_punch"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:notch_punch"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  _center = new Vec3(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(2.0F)
                           .setArmorNegation(60.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 20.0F);
                        if (world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new Notchflash3Entity(SimpleMobsModEntities.NOTCHFLASH_3.get(), _level);
                           entityToSpawn.moveTo(x, y, z, entity.getYRot(), (float)Mth.nextDouble(net.minecraft.util.RandomSource.create(), -50.0, 50.0));
                           entityToSpawn.setYBodyRot(entity.getYRot());
                           entityToSpawn.setYHeadRot(entity.getYRot());
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }
                     }
                  }
               }

               if (entity.getPersistentData().getBoolean("hurt")) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk3a", 0.0);
                  entity.getPersistentData().putBoolean("Attack3a", false);
                  entity.getPersistentData().putBoolean("hurt", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack3b")) {
               if (entity.getPersistentData().getDouble("Atk3b") == 0.0) {
                  entity.getPersistentData().putDouble("Atk3b", 30.0);
               } else {
                  entity.getPersistentData().putDouble("Atk3b", entity.getPersistentData().getDouble("Atk3b") - 1.0);
               }

               if (entity instanceof LivingEntity _entity) {
                  _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1, 6, false, false));
               }

               if (entity.getPersistentData().getDouble("Atk3b") == 30.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.IRON_NUGGET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_NUGGET));
                  }
               } else if (entity.getPersistentData().getDouble("Atk3b") <= 29.0 && entity.getPersistentData().getDouble("Atk3b") >= 28.0) {
                  entity.setDeltaMovement(
                     new Vec3(
                        Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 0.5,
                        entity.getDeltaMovement().y(),
                        Math.cos(Math.toRadians(entity.getYRot())) * 0.5
                     )
                  );
               } else if (entity.getPersistentData().getDouble("Atk3b") == 18.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        entity.getPersistentData().putBoolean("Attack3c", true);
                        entity.getPersistentData().putBoolean("Attack3b", false);
                        entity.getPersistentData().putDouble("Atk3b", 0.0);
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk3b") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk3b", 0.0);
                  entity.getPersistentData().putBoolean("Attack3b", false);
               }

               if (entity.getPersistentData().getDouble("Atk3b") == 26.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:notch_punch"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:notch_punch"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  _center = new Vec3(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(2.0F)
                           .setArmorNegation(60.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 20.0F);
                        entityiterator.setDeltaMovement(
                           new Vec3(Math.sin(Math.toRadians(entity.getYRot() - 90.0F)) * 0.4, 0.2, Math.cos(Math.toRadians(entity.getYRot() + 90.0F)) * 0.4)
                        );
                        if (world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new Notchflash3Entity(SimpleMobsModEntities.NOTCHFLASH_3.get(), _level);
                           entityToSpawn.moveTo(x, y, z, entity.getYRot(), (float)Mth.nextDouble(net.minecraft.util.RandomSource.create(), -50.0, 50.0));
                           entityToSpawn.setYBodyRot(entity.getYRot());
                           entityToSpawn.setYHeadRot(entity.getYRot());
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }
                     }
                  }
               }

               if (entity.getPersistentData().getBoolean("hurt")) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk3b", 0.0);
                  entity.getPersistentData().putBoolean("Attack3b", false);
                  entity.getPersistentData().putBoolean("hurt", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack3c")) {
               if (entity.getPersistentData().getDouble("Atk3c") == 0.0) {
                  entity.getPersistentData().putDouble("Atk3c", 40.0);
               } else {
                  entity.getPersistentData().putDouble("Atk3c", entity.getPersistentData().getDouble("Atk3c") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk3c") == 40.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 12, 6, false, false));
                  }

                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.WHITE_WOOL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.WHITE_WOOL));
                  }
               } else if (entity.getPersistentData().getDouble("Atk3c") == 28.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk3c") == 26.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 26, 6, false, false));
                  }

                  entity.getPersistentData().putBoolean("noai", true);
               } else if (entity.getPersistentData().getDouble("Atk3c") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("noai", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk3c", 0.0);
                  entity.getPersistentData().putBoolean("Attack3c", false);
               }

               if (entity.getPersistentData().getDouble("Atk3c") == 34.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:notch_punch"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:notch_punch"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  _center = new Vec3(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(2.0F)
                           .setArmorNegation(50.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 30.0F);
                        entityiterator.setDeltaMovement(
                           new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 0.4, 0.8, Math.cos(Math.toRadians(entity.getYRot())) * 0.4)
                        );
                        if (world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new Notchflash3Entity(SimpleMobsModEntities.NOTCHFLASH_3.get(), _level);
                           entityToSpawn.moveTo(x, y, z, entity.getYRot(), (float)Mth.nextDouble(net.minecraft.util.RandomSource.create(), 40.0, 80.0));
                           entityToSpawn.setYBodyRot(entity.getYRot());
                           entityToSpawn.setYHeadRot(entity.getYRot());
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack4")) {
               if (entity.getPersistentData().getDouble("Atk4") == 0.0) {
                  entity.getPersistentData().putDouble("Atk4", 70.0);
               } else {
                  entity.getPersistentData().putDouble("Atk4", entity.getPersistentData().getDouble("Atk4") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk4") == 70.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.WHITE_CONCRETE));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.WHITE_CONCRETE));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk4") == 60.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 8, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk4") == 52.0) {
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new NotchPunchEntity(SimpleMobsModEntities.NOTCH_PUNCH.get(), _level);
                     entityToSpawn.moveTo(x + entity.getLookAngle().x * 1.0, y, z + entity.getLookAngle().z * 1.0, entity.getYRot(), entity.getXRot());
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
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
                           dev.deepnight.SafeSounds.resolve("simple_mobs:notch_punch2"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:notch_punch2"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 42, 1));
                  }
               } else if (entity.getPersistentData().getDouble("Atk4") == 10.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 6, false, false));
                  }

                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(10.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator instanceof NotchPunchEntity && !entityiterator.level().isClientSide()) {
                        entityiterator.discard();
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

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putDouble("Atk4", 0.0);
                  entity.getPersistentData().putBoolean("Attack4", false);
               }

               if (entity.getPersistentData().getDouble("Atk4") <= 52.0 && entity.getPersistentData().getDouble("Atk4") >= 10.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof NotchBossEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.SHORT)
                           .setImpact(1.0F)
                           .setArmorNegation(30.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 10.0F);
                        if (Math.random() >= 0.97 && world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new ElementalFlash1Entity(SimpleMobsModEntities.ELEMENTAL_FLASH_1.get(), _level);
                           entityToSpawn.moveTo(
                              x + entity.getLookAngle().x * 3.0 + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
                              y + 1.0 + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
                              z + entity.getLookAngle().z * 3.0 + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
                              entity.getYRot(),
                              0.0F
                           );
                           entityToSpawn.setYBodyRot(entity.getYRot());
                           entityToSpawn.setYHeadRot(entity.getYRot());
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack5")) {
               if (entity.getPersistentData().getDouble("Atk5") == 0.0) {
                  entity.getPersistentData().putDouble("Atk5", 71.0);
               } else {
                  entity.getPersistentData().putDouble("Atk5", entity.getPersistentData().getDouble("Atk5") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk5") == 71.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.RED_WOOL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.RED_WOOL));
                  }

                  entity.getPersistentData().putBoolean("noai", true);
               } else if (entity.getPersistentData().getDouble("Atk5") == 37.0) {
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

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:giant_footstep"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:giant_footstep"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y, z, 20, 1.0, 1.0, 1.0, 1.0);
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new ElementalSpawnEntity(SimpleMobsModEntities.ELEMENTAL_SPAWN.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), entity.getXRot());
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new Notchgroundeffect3Entity(SimpleMobsModEntities.NOTCHGROUNDEFFECT_3.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), entity.getXRot());
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               } else if (entity.getPersistentData().getDouble("Atk5") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("noai", false);
                  entity.getPersistentData().putDouble("Atk5", 0.0);
                  entity.getPersistentData().putBoolean("Attack5", false);
               }

               if (entity.getPersistentData().getDouble("Atk5") <= 37.0 && entity.getPersistentData().getDouble("Atk5") >= 34.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof NotchBossEntity)
                        && !(entityiterator instanceof NotchStompeffectEntity)
                        && !(entityiterator instanceof Notchgroundeffect3Entity)
                        && !(entityiterator instanceof ElementalSpawnEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.KNOCKDOWN)
                           .setImpact(1.0F)
                           .setArmorNegation(30.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 50.0F);
                        entityiterator.setDeltaMovement(new Vec3(0.0, 1.2, 0.0));
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack6")) {
               if (entity.getPersistentData().getDouble("Atk6") == 0.0) {
                  entity.getPersistentData().putDouble("Atk6", 40.0);
               } else {
                  entity.getPersistentData().putDouble("Atk6", entity.getPersistentData().getDouble("Atk6") - 1.0);
               }

               if (entity instanceof LivingEntity _entity) {
                  _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1, 6, false, false));
               }

               if (entity.getPersistentData().getDouble("Atk6") == 40.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.YELLOW_WOOL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.YELLOW_WOOL));
                  }
               } else if (entity.getPersistentData().getDouble("Atk6") == 24.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:notch_punch"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:notch_punch"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F,
                           false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk6") == 16.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        entity.getPersistentData().putBoolean("Attack6a", true);
                        entity.getPersistentData().putBoolean("Attack6", false);
                        entity.getPersistentData().putDouble("Atk6", 0.0);
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

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk6", 0.0);
                  entity.getPersistentData().putBoolean("Attack6", false);
               }

               if (entity.getPersistentData().getDouble("Atk6") <= 24.0 && entity.getPersistentData().getDouble("Atk6") >= 22.0) {
                  _center = new Vec3(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.SHORT)
                           .setImpact(2.0F)
                           .setArmorNegation(30.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 20.0F);
                        if (world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new Notchflash3Entity(SimpleMobsModEntities.NOTCHFLASH_3.get(), _level);
                           entityToSpawn.moveTo(x, y, z, entity.getYRot(), (float)Mth.nextDouble(net.minecraft.util.RandomSource.create(), -50.0, 50.0));
                           entityToSpawn.setYBodyRot(entity.getYRot());
                           entityToSpawn.setYHeadRot(entity.getYRot());
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }
                     }
                  }
               }

               if (entity.getPersistentData().getBoolean("hurt")) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk6", 0.0);
                  entity.getPersistentData().putBoolean("Attack6", false);
                  entity.getPersistentData().putBoolean("hurt", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack6a")) {
               if (entity.getPersistentData().getDouble("Atk6a") == 0.0) {
                  entity.getPersistentData().putDouble("Atk6a", 45.0);
               } else {
                  entity.getPersistentData().putDouble("Atk6a", entity.getPersistentData().getDouble("Atk6a") - 1.0);
               }

               if (entity instanceof LivingEntity _entity) {
                  _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1, 6, false, false));
               }

               if (entity.getPersistentData().getDouble("Atk6a") == 45.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.GREEN_WOOL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.GREEN_WOOL));
                  }
               } else if (entity.getPersistentData().getDouble("Atk6a") <= 40.0 && entity.getPersistentData().getDouble("Atk6a") >= 34.0) {
                  entity.setDeltaMovement(
                     new Vec3(
                        Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 0.3,
                        entity.getDeltaMovement().y(),
                        Math.cos(Math.toRadians(entity.getYRot())) * 0.3
                     )
                  );
               } else if (entity.getPersistentData().getDouble("Atk6a") == 20.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        entity.getPersistentData().putBoolean("Attack6b", true);
                        entity.getPersistentData().putBoolean("Attack6a", false);
                        entity.getPersistentData().putDouble("Atk6a", 0.0);
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk6a") == 2.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.SADDLE));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.SADDLE));
                  }
               } else if (entity.getPersistentData().getDouble("Atk6a") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk6a", 0.0);
                  entity.getPersistentData().putBoolean("Attack6a", false);
               }

               if (entity.getPersistentData().getDouble("Atk6a") == 36.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:notch_punch"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:notch_punch"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F,
                           false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk6a") <= 35.0 && entity.getPersistentData().getDouble("Atk6a") >= 30.0) {
                  _center = new Vec3(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(1.0F)
                           .setArmorNegation(30.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 20.0F);
                        if (world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new Notchflash3Entity(SimpleMobsModEntities.NOTCHFLASH_3.get(), _level);
                           entityToSpawn.moveTo(x, y, z, entity.getYRot(), (float)Mth.nextDouble(net.minecraft.util.RandomSource.create(), -50.0, 50.0));
                           entityToSpawn.setYBodyRot(entity.getYRot());
                           entityToSpawn.setYHeadRot(entity.getYRot());
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack6b")) {
               if (entity.getPersistentData().getDouble("Atk6b") == 0.0) {
                  entity.getPersistentData().putDouble("Atk6b", 57.0);
               } else {
                  entity.getPersistentData().putDouble("Atk6b", entity.getPersistentData().getDouble("Atk6b") - 1.0);
               }

               if (entity instanceof LivingEntity _entity) {
                  _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1, 6, false, false));
               }

               if (entity.getPersistentData().getDouble("Atk6b") == 57.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.ACACIA_LEAVES));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.ACACIA_LEAVES));
                  }
               } else if (entity.getPersistentData().getDouble("Atk6b") == 38.0) {
                  entity.getPersistentData().putBoolean("noai", true);
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:notch_punch"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:notch_punch"),
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
                           dev.deepnight.SafeSounds.resolve("simple_mobs:weakogre_punch"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.5F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:weakogre_punch"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.5F,
                           false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk6b") == 2.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.SADDLE));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.SADDLE));
                  }
               } else if (entity.getPersistentData().getDouble("Atk6b") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("noai", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk6b", 0.0);
                  entity.getPersistentData().putBoolean("Attack6b", false);
               }

               if (entity.getPersistentData().getDouble("Atk6b") <= 38.0 && entity.getPersistentData().getDouble("Atk6b") >= 36.0) {
                  _center = new Vec3(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.SHORT)
                           .setImpact(1.0F)
                           .setArmorNegation(30.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 20.0F);
                        entityiterator.setDeltaMovement(
                           new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 2.0, 0.5, Math.cos(Math.toRadians(entity.getYRot())) * 2.0)
                        );
                        if (world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new Notchflash3Entity(SimpleMobsModEntities.NOTCHFLASH_3.get(), _level);
                           entityToSpawn.moveTo(x, y, z, entity.getYRot(), (float)Mth.nextDouble(net.minecraft.util.RandomSource.create(), -50.0, 50.0));
                           entityToSpawn.setYBodyRot(entity.getYRot());
                           entityToSpawn.setYHeadRot(entity.getYRot());
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }

                        if (world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new ElementalFlash1Entity(SimpleMobsModEntities.ELEMENTAL_FLASH_1.get(), _level);
                           entityToSpawn.moveTo(x, y, z, entity.getYRot(), (float)Mth.nextDouble(net.minecraft.util.RandomSource.create(), -50.0, 50.0));
                           entityToSpawn.setYBodyRot(entity.getYRot());
                           entityToSpawn.setYHeadRot(entity.getYRot());
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack7")) {
               if (entity.getPersistentData().getDouble("Atk7") == 0.0) {
                  entity.getPersistentData().putDouble("Atk7", 94.0);
               } else {
                  entity.getPersistentData().putDouble("Atk7", entity.getPersistentData().getDouble("Atk7") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk7") == 94.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.SCUTE));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.SCUTE));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
               } else if (entity.getPersistentData().getDouble("Atk7") == 54.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 14, 1, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk7") == 40.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10, 3, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk7") == 30.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           0.5F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                           SoundSource.NEUTRAL,
                           3.0F,
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

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new AirEntity(SimpleMobsModEntities.AIR.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), (float)Mth.nextDouble(net.minecraft.util.RandomSource.create(), -50.0, 50.0));
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new Notchgroundeffect3Entity(SimpleMobsModEntities.NOTCHGROUNDEFFECT_3.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), (float)Mth.nextDouble(net.minecraft.util.RandomSource.create(), -50.0, 50.0));
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putBoolean("noai", true);
               } else if (entity.getPersistentData().getDouble("Atk7") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("noai", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk7", 0.0);
                  entity.getPersistentData().putBoolean("Attack7", false);
               }

               if (entity.getPersistentData().getDouble("Atk7") >= 54.0 && entity.getPersistentData().getDouble("Atk7") <= 94.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        entity.getPersistentData().putBoolean("Attack7a", true);
                        entity.getPersistentData().putDouble("Atk7", 0.0);
                        entity.getPersistentData().putBoolean("Attack7", false);
                     }
                  }
               }

               if (entity.getPersistentData().getDouble("Atk7") <= 30.0 && entity.getPersistentData().getDouble("Atk7") >= 26.0) {
                  _center = new Vec3(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(2.0F)
                           .setArmorNegation(30.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 40.0F);
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack7a")) {
               if (entity.getPersistentData().getDouble("Atk7a") == 0.0) {
                  entity.getPersistentData().putDouble("Atk7a", 45.0);
               } else {
                  entity.getPersistentData().putDouble("Atk7a", entity.getPersistentData().getDouble("Atk7a") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk7a") == 45.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.SAND));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.SAND));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
               } else if (entity.getPersistentData().getDouble("Atk7a") == 40.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10, 3, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk7a") == 30.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           0.5F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                           SoundSource.NEUTRAL,
                           3.0F,
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

                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putBoolean("noai", true);
               } else if (entity.getPersistentData().getDouble("Atk7a") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("noai", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk7a", 0.0);
                  entity.getPersistentData().putBoolean("Attack7a", false);
               }

               if (entity.getPersistentData().getDouble("Atk7a") == 30.0) {
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new AirEntity(SimpleMobsModEntities.AIR.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), (float)Mth.nextDouble(net.minecraft.util.RandomSource.create(), -50.0, 50.0));
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new Notchgroundeffect3Entity(SimpleMobsModEntities.NOTCHGROUNDEFFECT_3.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), (float)Mth.nextDouble(net.minecraft.util.RandomSource.create(), -50.0, 50.0));
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               }

               if (entity.getPersistentData().getDouble("Atk7a") <= 30.0 && entity.getPersistentData().getDouble("Atk7a") >= 26.0) {
                  _center = new Vec3(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(2.0F)
                           .setArmorNegation(30.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 40.0F);
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack8")) {
               if (entity.getPersistentData().getDouble("Atk8") == 0.0) {
                  entity.getPersistentData().putDouble("Atk8", 90.0);
               } else {
                  entity.getPersistentData().putDouble("Atk8", entity.getPersistentData().getDouble("Atk8") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk8") == 90.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.OAK_LEAVES));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.OAK_LEAVES));
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 6, false, false));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
               } else if (entity.getPersistentData().getDouble("Atk8") == 80.0) {
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new Notchflash1Entity(SimpleMobsModEntities.NOTCHFLASH_1.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 6, 10, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk8") == 74.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk8") == 64.0) {
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putBoolean("noai", true);
               } else if (entity.getPersistentData().getDouble("Atk8") == 58.0) {
                  entity.getPersistentData().putBoolean("noai", false);
                  entity.getPersistentData().putBoolean("focus", true);
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 12, 10, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk8") == 56.0) {
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new Notchflash1Entity(SimpleMobsModEntities.NOTCHFLASH_1.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               } else if (entity.getPersistentData().getDouble("Atk8") == 42.0) {
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putBoolean("noai", true);
               } else if (entity.getPersistentData().getDouble("Atk8") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("noai", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk8", 0.0);
                  entity.getPersistentData().putBoolean("Attack8", false);
               }

               if (entity.getPersistentData().getDouble("Atk8") == 64.0 || entity.getPersistentData().getDouble("Atk8") == 34.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
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
                           dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           0.5F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           0.5F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new NotchStompeffectEntity(SimpleMobsModEntities.NOTCH_STOMPEFFECT.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), (float)Mth.nextDouble(net.minecraft.util.RandomSource.create(), -50.0, 50.0));
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new Notchgroundeffect1Entity(SimpleMobsModEntities.NOTCHGROUNDEFFECT_1.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), (float)Mth.nextDouble(net.minecraft.util.RandomSource.create(), -50.0, 50.0));
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               }

               if (entity.getPersistentData().getDouble("Atk8") <= 64.0 && entity.getPersistentData().getDouble("Atk8") >= 60.0
                  || entity.getPersistentData().getDouble("Atk8") <= 34.0 && entity.getPersistentData().getDouble("Atk8") >= 30.0) {
                  _center = new Vec3(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof NotchBossEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(1.0F)
                           .setArmorNegation(30.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 24.0F);
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("hurt")) {
               entity.getPersistentData().putBoolean("hurt", false);
            }
         }
      }
   }
}
