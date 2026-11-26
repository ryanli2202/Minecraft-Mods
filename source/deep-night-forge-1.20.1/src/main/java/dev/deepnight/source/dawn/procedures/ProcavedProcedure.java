package dev.deepnight.source.dawn.procedures;


import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.CaveDwellerEntity;
import dev.deepnight.source.dawn.entity.Projectile2Entity;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.network.SimpleMobsModVariables;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import dev.deepnight.source.SourceDamage;
import dev.deepnight.source.SourceDamage.StunType;

public class ProcavedProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         double bbtimer = 0.0;
         double raytrace_distance = 0.0;
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
            entity.getPersistentData().putBoolean("Far", false);
            entity.getPersistentData().putBoolean("Line", false);
            entity.getPersistentData().putBoolean("Close", false);
            if (entity.getPersistentData().getBoolean("hurtcd")) {
               if (entity.getPersistentData().getDouble("hurt_cd") == 0.0) {
                  entity.getPersistentData().putDouble("hurt_cd", 11.0);
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

            if (entity.getPersistentData().getDouble("Poise") >= 350.0 && !entity.getPersistentData().getBoolean("Stunned")) {
               entity.getPersistentData().putBoolean("Stunned", true);
            }

            if (entity.getPersistentData().getBoolean("Roaring") && entity.getPersistentData().getDouble("Roar") == 0.0) {
               entity.getPersistentData().putDouble("Roar", 200.0);
            } else if (entity.getPersistentData().getDouble("Roar") > 0.0) {
               entity.getPersistentData().putDouble("Roar", entity.getPersistentData().getDouble("Roar") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Attack2CD") == 0.0 && entity.getPersistentData().getBoolean("Attack2")) {
               entity.getPersistentData().putDouble("Attack2CD", 200.0);
            } else if (entity.getPersistentData().getDouble("Attack2CD") > 0.0) {
               entity.getPersistentData().putDouble("Attack2CD", entity.getPersistentData().getDouble("Attack2CD") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Attack3CD") == 0.0 && entity.getPersistentData().getBoolean("Attack3")) {
               entity.getPersistentData().putDouble("Attack3CD", 260.0);
               SimpleMobsModVariables.MapVariables.get(world).aggrocave = false;
               SimpleMobsModVariables.MapVariables.get(world).syncData(world);
            } else if (entity.getPersistentData().getDouble("Attack3CD") > 0.0) {
               entity.getPersistentData().putDouble("Attack3CD", entity.getPersistentData().getDouble("Attack3CD") - 1.0);
            }

            if (entity.getPersistentData().getBoolean("Attack4") && entity.getPersistentData().getDouble("Attack4CD") == 0.0) {
               entity.getPersistentData().putDouble("Attack4CD", 240.0);
               SimpleMobsModVariables.MapVariables.get(world).aggrocave = false;
               SimpleMobsModVariables.MapVariables.get(world).syncData(world);
            } else if (entity.getPersistentData().getDouble("Attack4CD") > 0.0) {
               entity.getPersistentData().putDouble("Attack4CD", entity.getPersistentData().getDouble("Attack4CD") - 1.0);
            }

            if (!SimpleMobsModVariables.MapVariables.get(world).aggrocave && entity.getPersistentData().getDouble("Roar") == 1.0 && Math.random() <= 0.75) {
               entity.getPersistentData().putBoolean("Roaring", true);
            }

            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(16.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                  entity.getPersistentData().putBoolean("Far", true);
                  entity.getPersistentData()
                     .putDouble("atanyaw", Math.toDegrees(Math.atan2(entityiterator.getX() - entity.getX(), entityiterator.getZ() - entity.getZ())));
                  if (entity.getPersistentData().getDouble("atanyaw") < 0.0) {
                     entity.getPersistentData().putDouble("yaw2", 0.0 - entity.getPersistentData().getDouble("atanyaw"));
                  } else if (entity.getPersistentData().getDouble("atanyaw") >= 0.0) {
                     entity.getPersistentData().putDouble("yaw2", 360.0 - entity.getPersistentData().getDouble("atanyaw"));
                  }

                  entity.getPersistentData()
                     .putDouble("distance", Math.hypot(Math.abs(entity.getX() - entityiterator.getX()), Math.abs(entity.getZ() - entityiterator.getZ())));
                  entity.getPersistentData()
                     .putDouble(
                        "pitch2", Math.toDegrees(Math.atan(entity.getPersistentData().getDouble("distance") / (entityiterator.getY() - (entity.getY() + 3.0))))
                     );
                  if (entity.getPersistentData().getDouble("pitch2") > 0.0) {
                     entity.getPersistentData().putDouble("pitch2", 90.0 - entity.getPersistentData().getDouble("pitch2"));
                  } else if (entity.getPersistentData().getDouble("pitch2") < 0.0) {
                     entity.getPersistentData().putDouble("pitch2", 90.0 + entity.getPersistentData().getDouble("pitch2"));
                  }
               }
            }

            _center = new Vec3(x + entity.getLookAngle().x * 3.0, y, z + entity.getLookAngle().z * 3.0);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.5), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                  entity.getPersistentData().putBoolean("Far", false);
                  entity.getPersistentData().putBoolean("Close", true);
               }
            }

            if (entity.getPersistentData().getBoolean("Far")) {
               raytrace_distance = 6.0;

               for (int index0 = 0; index0 < 30; index0++) {
                  if (!world.getBlockState(
                           BlockPos.containing(
                              entity.level()
                                 .clip(
                                    new ClipContext(
                                       entity.getEyePosition(1.0F),
                                       entity.getEyePosition(1.0F).add(entity.getViewVector(1.0F).scale(raytrace_distance)),
                                       Block.COLLIDER,
                                       Fluid.NONE,
                                       entity
                                    )
                                 )
                                 .getBlockPos()
                                 .getX(),
                              entity.level()
                                 .clip(
                                    new ClipContext(
                                       entity.getEyePosition(1.0F),
                                       entity.getEyePosition(1.0F).add(entity.getViewVector(1.0F).scale(raytrace_distance)),
                                       Block.COLLIDER,
                                       Fluid.NONE,
                                       entity
                                    )
                                 )
                                 .getBlockPos()
                                 .getY(),
                              entity.level()
                                 .clip(
                                    new ClipContext(
                                       entity.getEyePosition(1.0F),
                                       entity.getEyePosition(1.0F).add(entity.getViewVector(1.0F).scale(raytrace_distance)),
                                       Block.COLLIDER,
                                       Fluid.NONE,
                                       entity
                                    )
                                 )
                                 .getBlockPos()
                                 .getZ()
                           )
                        )
                        .canOcclude()
                     || raytrace_distance < 14.0) {
                     Vec3 _centerx = new Vec3(
                        entity.level()
                           .clip(
                              new ClipContext(
                                 entity.getEyePosition(1.0F),
                                 entity.getEyePosition(1.0F).add(entity.getViewVector(1.0F).scale(++raytrace_distance)),
                                 Block.COLLIDER,
                                 Fluid.NONE,
                                 entity
                              )
                           )
                           .getBlockPos()
                           .getX(),
                        entity.level()
                           .clip(
                              new ClipContext(
                                 entity.getEyePosition(1.0F),
                                 entity.getEyePosition(1.0F).add(entity.getViewVector(1.0F).scale(raytrace_distance)),
                                 Block.COLLIDER,
                                 Fluid.NONE,
                                 entity
                              )
                           )
                           .getBlockPos()
                           .getY(),
                        entity.level()
                           .clip(
                              new ClipContext(
                                 entity.getEyePosition(1.0F),
                                 entity.getEyePosition(1.0F).add(entity.getViewVector(1.0F).scale(raytrace_distance)),
                                 Block.COLLIDER,
                                 Fluid.NONE,
                                 entity
                              )
                           )
                           .getBlockPos()
                           .getZ()
                     );

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_centerx, _centerx).inflate(1.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                           entity.getPersistentData().putBoolean("Line", true);
                        }
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack1")) {
               if (entity.getPersistentData().getDouble("Attack1Timer") == 0.0) {
                  entity.getPersistentData().putDouble("Attack1Timer", 31.0);
               } else if (entity.getPersistentData().getDouble("Attack1Timer") > 0.0) {
                  entity.getPersistentData().putDouble("Attack1Timer", entity.getPersistentData().getDouble("Attack1Timer") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Attack1Timer") == 30.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.GOLD_NUGGET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLD_NUGGET));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 25, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Attack1Timer") == 12.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:attack1"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("simple_mobs:attack1"), SoundSource.NEUTRAL, 1.0F, 1.0F, false
                        );
                     }
                  }

                  _center = new Vec3(x + entity.getLookAngle().x * 3.0, y, z + entity.getLookAngle().z * 3.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        if (entityiterator instanceof LivingEntity _livEnt ? _livEnt.isBlocking() : false) {
                           if ((entityiterator instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == Items.SHIELD) {
                              ItemStack _ist = entityiterator instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY;
                              if (_ist.hurt(5, net.minecraft.util.RandomSource.create(), null)) {
                                 _ist.shrink(1);
                                 _ist.setDamageValue(0);
                              }

                              if (entityiterator instanceof Player _player) {
                                 _player.getCooldowns()
                                    .addCooldown((entityiterator instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem(), 100);
                              }

                              if (world instanceof Level _level) {
                                 if (!_level.isClientSide()) {
                                    _level.playSound(
                                       null,
                                       BlockPos.containing(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ()),
                                       dev.deepnight.SafeSounds.resolve("item.shield.break"),
                                       SoundSource.NEUTRAL,
                                       1.0F,
                                       1.0F
                                    );
                                 } else {
                                    _level.playLocalSound(
                                       entityiterator.getX(),
                                       entityiterator.getY(),
                                       entityiterator.getZ(),
                                       dev.deepnight.SafeSounds.resolve("item.shield.break"),
                                       SoundSource.NEUTRAL,
                                       1.0F,
                                       1.0F,
                                       false
                                    );
                                 }
                              }
                           } else {
                              ItemStack _ist = entityiterator instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY;
                              if (_ist.hurt(5, net.minecraft.util.RandomSource.create(), null)) {
                                 _ist.shrink(1);
                                 _ist.setDamageValue(0);
                              }

                              if (entityiterator instanceof Player _player) {
                                 _player.getCooldowns()
                                    .addCooldown((entityiterator instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem(), 100);
                              }

                              if (world instanceof Level _level) {
                                 if (!_level.isClientSide()) {
                                    _level.playSound(
                                       null,
                                       BlockPos.containing(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ()),
                                       dev.deepnight.SafeSounds.resolve("item.shield.break"),
                                       SoundSource.NEUTRAL,
                                       1.0F,
                                       1.0F
                                    );
                                 } else {
                                    _level.playLocalSound(
                                       entityiterator.getX(),
                                       entityiterator.getY(),
                                       entityiterator.getZ(),
                                       dev.deepnight.SafeSounds.resolve("item.shield.break"),
                                       SoundSource.NEUTRAL,
                                       1.0F,
                                       1.0F,
                                       false
                                    );
                                 }
                              }
                           }
                        } else {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.SHORT)
                              .setImpact(1.0F)
                              .setArmorNegation(4.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 8.0F);
                        }
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Attack1Timer") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putDouble("Attack1Timer", 0.0);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("Attack1", false);
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  entity.getPersistentData().putDouble("Attack1Timer", 0.0);
                  entity.getPersistentData().putBoolean("Attack1", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack1_alt")) {
               if (entity.getPersistentData().getDouble("Attack1altTimer") == 0.0) {
                  entity.getPersistentData().putDouble("Attack1altTimer", 30.0);
               } else if (entity.getPersistentData().getDouble("Attack1altTimer") > 0.0) {
                  entity.getPersistentData().putDouble("Attack1altTimer", entity.getPersistentData().getDouble("Attack1altTimer") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Attack1altTimer") == 30.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.COAL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.COAL));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 28, 4, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Attack1altTimer") == 24.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 2.5, y, z + entity.getLookAngle().z * 2.5);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof CaveDwellerEntity)) {
                        if (entityiterator instanceof LivingEntity _livEnt && _livEnt.isBlocking()) {
                           if ((entityiterator instanceof LivingEntity _livEntx ? _livEntx.getMainHandItem() : ItemStack.EMPTY).getItem() == Items.SHIELD) {
                              ItemStack _ist = entityiterator instanceof LivingEntity _livEntx ? _livEntx.getMainHandItem() : ItemStack.EMPTY;
                              if (_ist.hurt(5, net.minecraft.util.RandomSource.create(), null)) {
                                 _ist.shrink(1);
                                 _ist.setDamageValue(0);
                              }

                              if (entityiterator instanceof Player _player) {
                                 _player.getCooldowns()
                                    .addCooldown((entityiterator instanceof LivingEntity _livEntx ? _livEntx.getMainHandItem() : ItemStack.EMPTY).getItem(), 40);
                              }

                              if (world instanceof Level _level) {
                                 if (!_level.isClientSide()) {
                                    _level.playSound(
                                       null,
                                       BlockPos.containing(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ()),
                                       dev.deepnight.SafeSounds.resolve("item.shield.break"),
                                       SoundSource.NEUTRAL,
                                       1.0F,
                                       1.0F
                                    );
                                 } else {
                                    _level.playLocalSound(
                                       entityiterator.getX(),
                                       entityiterator.getY(),
                                       entityiterator.getZ(),
                                       dev.deepnight.SafeSounds.resolve("item.shield.break"),
                                       SoundSource.NEUTRAL,
                                       1.0F,
                                       1.0F,
                                       false
                                    );
                                 }
                              }

                              entityiterator.setDeltaMovement(
                                 new Vec3((entityiterator.getX() - entity.getX()) / 4.5, 0.4, (entityiterator.getZ() - entity.getZ()) / 4.5)
                              );
                           } else {
                              ItemStack _ist = entityiterator instanceof LivingEntity _livEntx ? _livEntx.getOffhandItem() : ItemStack.EMPTY;
                              if (_ist.hurt(5, net.minecraft.util.RandomSource.create(), null)) {
                                 _ist.shrink(1);
                                 _ist.setDamageValue(0);
                              }

                              if (entityiterator instanceof Player _player) {
                                 _player.getCooldowns()
                                    .addCooldown((entityiterator instanceof LivingEntity _livEntx ? _livEntx.getOffhandItem() : ItemStack.EMPTY).getItem(), 40);
                              }

                              if (world instanceof Level _level) {
                                 if (!_level.isClientSide()) {
                                    _level.playSound(
                                       null,
                                       BlockPos.containing(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ()),
                                       dev.deepnight.SafeSounds.resolve("item.shield.break"),
                                       SoundSource.NEUTRAL,
                                       1.0F,
                                       1.0F
                                    );
                                 } else {
                                    _level.playLocalSound(
                                       entityiterator.getX(),
                                       entityiterator.getY(),
                                       entityiterator.getZ(),
                                       dev.deepnight.SafeSounds.resolve("item.shield.break"),
                                       SoundSource.NEUTRAL,
                                       1.0F,
                                       1.0F,
                                       false
                                    );
                                 }
                              }

                              entityiterator.setDeltaMovement(
                                 new Vec3((entityiterator.getX() - entity.getX()) / 4.5, 0.4, (entityiterator.getZ() - entity.getZ()) / 4.5)
                              );
                           }
                        } else {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.SHORT)
                              .setImpact(0.6F)
                              .setArmorNegation(0.4F);
                           entityiterator.hurt((DamageSource)dmgSrc, 8.0F);
                           entityiterator.setDeltaMovement(
                              new Vec3((entityiterator.getX() - entity.getX()) / 4.7, 0.6, (entityiterator.getZ() - entity.getZ()) / 4.7)
                           );
                        }
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Attack1altTimer") == 1.0) {
                  entity.getPersistentData().putBoolean("Attacking", false);
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putDouble("Attack1altTimer", 0.0);
                  entity.getPersistentData().putBoolean("Attack1_alt", false);
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  entity.getPersistentData().putDouble("Attack1altTimer", 0.0);
                  entity.getPersistentData().putBoolean("Attack1_alt", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Dodge")) {
               if (entity.getPersistentData().getDouble("DodgeTimer") == 0.0) {
                  entity.getPersistentData().putDouble("DodgeTimer", 37.0);
               } else if (entity.getPersistentData().getDouble("DodgeTimer") >= 1.0) {
                  entity.fallDistance = 0.0F;
                  entity.getPersistentData().putDouble("DodgeTimer", entity.getPersistentData().getDouble("DodgeTimer") - 1.0);
               }

               if (entity.getPersistentData().getDouble("DodgeTimer") == 37.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.LILAC));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.LILAC));
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("DodgeTimer") == 27.0) {
                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.setDeltaMovement(new Vec3(Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.8, 0.8), 0.6, Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.8, 0.8)));
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("DodgeTimer") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("DodgeTimer", 0.0);
                  if (Math.random() <= 0.3) {
                     entity.getPersistentData().putBoolean("Attack2", true);
                  }

                  entity.getPersistentData().putBoolean("Dodge", false);
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  entity.getPersistentData().putDouble("DodgeTimer", 0.0);
                  entity.getPersistentData().putBoolean("Dodge", false);
               }
            }

            if (!entity.getPersistentData().getBoolean("Attacking")
               && entity.getPersistentData().getDouble("Closeattack") == 0.0
               && entity.getPersistentData().getBoolean("Close")
               && !entity.getPersistentData().getBoolean("Farattacks")) {
               if (Math.random() <= 0.48) {
                  entity.getPersistentData().putBoolean("Attack1", true);
               } else if (Math.random() >= 0.53) {
                  entity.getPersistentData().putBoolean("Attack1_alt", true);
               } else if (!((entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null) instanceof Animal)) {
                  entity.getPersistentData().putBoolean("Dodge", true);
               }

               entity.getPersistentData().putDouble("Closeattack", 26.0);
            } else if (entity.getPersistentData().getDouble("Closeattack") >= 1.0) {
               entity.getPersistentData().putDouble("Closeattack", entity.getPersistentData().getDouble("Closeattack") - 1.0);
            }

            if (!entity.getPersistentData().getBoolean("Attacking")
               && entity.getPersistentData().getBoolean("Far")
               && !entity.getPersistentData().getBoolean("Farattacks")
               && entity.getPersistentData().getDouble("Attack2CD") == 0.0
               && Math.random() >= 0.8) {
               entity.getPersistentData().putBoolean("Attack2", true);
            }

            if (entity.getPersistentData().getBoolean("Attack2")) {
               entity.fallDistance = 0.0F;
               if (entity.getPersistentData().getDouble("Attack2Timer") == 0.0) {
                  entity.getPersistentData().putDouble("Attack2Timer", 36.0);
               } else {
                  entity.getPersistentData().putDouble("Attack2Timer", entity.getPersistentData().getDouble("Attack2Timer") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Attack2Timer") == 8.0 && world instanceof Level _level) {
                  if (!_level.isClientSide()) {
                     _level.playSound(
                        null,
                        BlockPos.containing(x, y, z),
                        dev.deepnight.SafeSounds.resolve("simple_mobs:attack2"),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F
                     );
                  } else {
                     _level.playLocalSound(
                        x, y, z, dev.deepnight.SafeSounds.resolve("simple_mobs:attack2"), SoundSource.NEUTRAL, 1.0F, 1.0F, false
                     );
                  }
               }

               if (entity.getPersistentData().getDouble("Attack2Timer") == 36.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.CHARCOAL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHARCOAL));
                  }

                  entity.getPersistentData().putBoolean("Farattacks", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Attack2Timer") == 30.0) {
                  entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putDouble("pitch", entity.getXRot());
               } else if (entity.getPersistentData().getDouble("Attack2Timer") == 26.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(15.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        entity.setDeltaMovement(new Vec3((entityiterator.getX() - entity.getX()) * 0.1, 0.8, (entityiterator.getZ() - entity.getZ()) * 0.1));
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Attack2Timer") >= 4.0 && entity.getPersistentData().getDouble("Attack2Timer") <= 8.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof CaveDwellerEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(0.7F)
                           .setArmorNegation(0.7F);
                        entityiterator.hurt((DamageSource)dmgSrc, 12.0F);
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Attack2Timer") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("noai", false);
                  entity.getPersistentData().putBoolean("Attack2", false);
                  entity.getPersistentData().putBoolean("Farattacks", false);
                  entity.getPersistentData().putDouble("Attack2Timer", 0.0);
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  entity.getPersistentData().putDouble("Attack2Timer", 0.0);
                  entity.getPersistentData().putBoolean("Farattacks", false);
                  entity.getPersistentData().putBoolean("Attack2", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Line")
               && !entity.getPersistentData().getBoolean("Attacking")
               && entity.getPersistentData().getBoolean("Far")
               && !entity.getPersistentData().getBoolean("Farattacks")
               && entity.getPersistentData().getDouble("Attack3CD") == 0.0
               && Math.random() <= 0.5) {
               entity.getPersistentData().putBoolean("Attack3", true);
            }

            if (entity.getPersistentData().getBoolean("Attack3")) {
               if (entity.getPersistentData().getDouble("Attack3Timer") == 0.0) {
                  entity.getPersistentData().putDouble("Attack3Timer", 60.0);
               } else {
                  entity.getPersistentData().putDouble("Attack3Timer", entity.getPersistentData().getDouble("Attack3Timer") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Attack3Timer") == 60.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.IRON_INGOT));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_INGOT));
                  }

                  entity.getPersistentData().putBoolean("Farattacks", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 65, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Attack3Timer") == 54.0 && world instanceof Level _level) {
                  if (!_level.isClientSide()) {
                     _level.playSound(
                        null,
                        BlockPos.containing(x, y, z),
                        dev.deepnight.SafeSounds.resolve("simple_mobs:flame"),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F
                     );
                  } else {
                     _level.playLocalSound(
                        x, y, z, dev.deepnight.SafeSounds.resolve("simple_mobs:flame"), SoundSource.NEUTRAL, 1.0F, 1.0F, false
                     );
                  }
               }

               if (entity.getPersistentData().getDouble("Attack3Timer") == 1.0) {
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
                           "/data merge entity @e[type=simple_mobs:cave_dweller,limit=1,sort=nearest] {NoAI:0}"
                        );
                  }

                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putDouble("Attack3Timer", 0.0);
                  entity.getPersistentData().putBoolean("Farattacks", false);
                  entity.getPersistentData().putBoolean("Attack3", false);
               }

               if (entity.getPersistentData().getDouble("Attack3Timer") == 38.0) {
                  entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putDouble("pitch", entity.getPersistentData().getDouble("pitch2"));
               } else if (entity.getPersistentData().getDouble("Attack3Timer") == 37.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=simple_mobs:cave_dweller,limit=1,sort=nearest] {NoAI:1b}"
                        );
                  }

                  entity.getPersistentData().putBoolean("lock", true);
               }

               if (entity.getPersistentData().getDouble("Attack3Timer") >= 12.0
                  && entity.getPersistentData().getDouble("Attack3Timer") <= 32.0
                  && world instanceof ServerLevel _level) {
                  Entity entityToSpawn = new Projectile2Entity(SimpleMobsModEntities.PROJECTILE_2.get(), _level);
                  entityToSpawn.moveTo(
                     x + entity.getLookAngle().x * 3.6,
                     y + 4.3,
                     z + entity.getLookAngle().z * 3.6,
                     entity.getYRot() + (float)Mth.nextDouble(net.minecraft.util.RandomSource.create(), -20.0, 20.0),
                     (float)entity.getPersistentData().getDouble("pitch2") + (float)Mth.nextDouble(net.minecraft.util.RandomSource.create(), -10.0, 10.0)
                  );
                  entityToSpawn.setYBodyRot(entity.getYRot() + (float)Mth.nextDouble(net.minecraft.util.RandomSource.create(), -20.0, 20.0));
                  entityToSpawn.setYHeadRot(entity.getYRot() + (float)Mth.nextDouble(net.minecraft.util.RandomSource.create(), -20.0, 20.0));
                  entityToSpawn.setDeltaMovement(
                     Math.sin(Math.toRadians(entity.getYRot() + 180.0F)), entity.getDeltaMovement().y(), Math.cos(Math.toRadians(entity.getYRot()))
                  );
                  if (entityToSpawn instanceof Mob _mobToSpawn) {
                     _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                  }

                  world.addFreshEntity(entityToSpawn);
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  entity.getPersistentData().putDouble("Attack3Timer", 0.0);
                  entity.getPersistentData().putBoolean("Farattacks", false);
                  entity.getPersistentData().putBoolean("Attack3", false);
               }
            }

            if (Math.random() >= 0.85
               && !entity.getPersistentData().getBoolean("Attacking")
               && entity.getPersistentData().getBoolean("Far")
               && !entity.getPersistentData().getBoolean("Farattacks")
               && entity.getPersistentData().getDouble("Roar") == 0.0) {
               entity.getPersistentData().putBoolean("Roaring", true);
            }

            if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0F)
                  <= (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1.0F) / 2.0F
               && Math.random() <= 0.06
               && !entity.getPersistentData().getBoolean("Attacking")
               && entity.getPersistentData().getBoolean("Far")
               && !entity.getPersistentData().getBoolean("Farattacks")
               && entity.getPersistentData().getDouble("Attack4CD") == 0.0) {
               entity.getPersistentData().putBoolean("Attack4", true);
            }

            if (entity.getPersistentData().getBoolean("Attack4")) {
               if (entity.getPersistentData().getDouble("Attack4Timer") == 0.0) {
                  entity.getPersistentData().putDouble("Attack4Timer", 72.0);
               } else {
                  entity.getPersistentData().putDouble("Attack4Timer", entity.getPersistentData().getDouble("Attack4Timer") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Attack4Timer") == 72.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.IRON_NUGGET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_NUGGET));
                  }

                  entity.getPersistentData().putBoolean("Farattacks", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 16, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Attack4Timer") == 58.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:roll"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("simple_mobs:roll"), SoundSource.NEUTRAL, 3.0F, 1.0F, false
                        );
                     }
                  }

                  bbtimer = 40.0;
                  entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putDouble("pitch", entity.getXRot());
               } else if (entity.getPersistentData().getDouble("Attack4Timer") == 56.0) {
                  entity.getPersistentData().putBoolean("noai", true);
                  entity.getPersistentData().putBoolean("lock", true);
               }

               if (entity.getPersistentData().getDouble("Attack4Timer") <= 56.0 && entity.getPersistentData().getDouble("Attack4Timer") >= 9.0) {
                  entity.setDeltaMovement(new Vec3(entity.getLookAngle().x * 0.5, entity.getDeltaMovement().y(), entity.getLookAngle().z * 0.5));
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(6.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator instanceof ServerPlayer && entityiterator instanceof Player && entityiterator instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 10, 1, false, false));
                     }
                  }

                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof CaveDwellerEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.SHORT)
                           .setImpact(7.0F)
                           .setArmorNegation(2.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 10.0F);
                        entityiterator.setDeltaMovement(new Vec3(Math.random(), 0.5, Math.random()));
                     }
                  }
               }

               if (entity.getPersistentData().getDouble("Attack4Timer") == 2.0) {
                  bbtimer = 0.0;
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.GOLD_INGOT));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLD_INGOT));
                  }
               }

               if (entity.getPersistentData().getDouble("Attack4Timer") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putDouble("Attack4Timer", 0.0);
                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("noai", false);
                  entity.getPersistentData().putBoolean("Farattacks", false);
                  entity.getPersistentData().putBoolean("Attack4", false);
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  entity.getPersistentData().putDouble("Attack4Timer", 0.0);
                  entity.getPersistentData().putBoolean("Farattacks", false);
                  entity.getPersistentData().putBoolean("Attack4", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Stunned")) {
               if (entity.getPersistentData().getDouble("stun") == 0.0) {
                  entity.getPersistentData().putDouble("stun", 91.0);
               } else {
                  entity.getPersistentData().putDouble("stun", entity.getPersistentData().getDouble("stun") - 1.0);
               }

               if (entity.getPersistentData().getDouble("stun") == 91.0) {
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

                  entity.getPersistentData().putDouble("yaw", entity.getYRot());
                  entity.getPersistentData().putDouble("pitch", entity.getXRot());
               } else if (entity.getPersistentData().getDouble("stun") == 90.0) {
                  entity.getPersistentData().putBoolean("lock", true);
               } else if (entity.getPersistentData().getDouble("stun") == 1.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=simple_mobs:cave_dweller,limit=1,sort=nearest] {NoAI:0}"
                        );
                  }

                  entity.getPersistentData().putBoolean("lock", false);
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putDouble("Poise", -80.0);
                  entity.getPersistentData().putDouble("stun", 0.0);
                  entity.getPersistentData().putBoolean("noai", false);
                  entity.getPersistentData().putBoolean("Stunned", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
               }
            }

            if (entity.getPersistentData().getDouble("bbtimer") != 0.0) {
               entity.getPersistentData().putDouble("bbtimer", entity.getPersistentData().getDouble("bbtimer") - 1.0);
            } else {
               entity.getPersistentData().putDouble("bbtimer", 60.0 - bbtimer);
               SimpleMobsModVariables.MapVariables.get(world).i = -2.0;
               SimpleMobsModVariables.MapVariables.get(world).syncData(world);
               SimpleMobsModVariables.MapVariables.get(world).j = 1.0;
               SimpleMobsModVariables.MapVariables.get(world).syncData(world);
               SimpleMobsModVariables.MapVariables.get(world).x = -2.0;
               SimpleMobsModVariables.MapVariables.get(world).syncData(world);

               while (SimpleMobsModVariables.MapVariables.get(world).i < 3.0) {
                  while (SimpleMobsModVariables.MapVariables.get(world).j < 5.0) {
                     while (SimpleMobsModVariables.MapVariables.get(world).x < 3.0) {
                        if (world.getBlockState(
                                    BlockPos.containing(
                                       x + SimpleMobsModVariables.MapVariables.get(world).i + entity.getLookAngle().x * 1.5,
                                       y + SimpleMobsModVariables.MapVariables.get(world).j,
                                       z + SimpleMobsModVariables.MapVariables.get(world).x + entity.getLookAngle().z * 1.5
                                    )
                                 )
                                 .getDestroySpeed(
                                    world,
                                    BlockPos.containing(
                                       x + SimpleMobsModVariables.MapVariables.get(world).i + entity.getLookAngle().x * 1.5,
                                       y + SimpleMobsModVariables.MapVariables.get(world).j,
                                       z + SimpleMobsModVariables.MapVariables.get(world).x + entity.getLookAngle().z * 1.5
                                    )
                                 )
                              <= 2.0F
                           && world.getBlockState(
                                    BlockPos.containing(
                                       x + SimpleMobsModVariables.MapVariables.get(world).i + entity.getLookAngle().x * 1.5,
                                       y + SimpleMobsModVariables.MapVariables.get(world).j,
                                       z + SimpleMobsModVariables.MapVariables.get(world).x + entity.getLookAngle().z * 1.5
                                    )
                                 )
                                 .getDestroySpeed(
                                    world,
                                    BlockPos.containing(
                                       x + SimpleMobsModVariables.MapVariables.get(world).i + entity.getLookAngle().x * 1.5,
                                       y + SimpleMobsModVariables.MapVariables.get(world).j,
                                       z + SimpleMobsModVariables.MapVariables.get(world).x + entity.getLookAngle().z * 1.5
                                    )
                                 )
                              >= 0.0F) {
                           world.destroyBlock(
                              BlockPos.containing(
                                 x + SimpleMobsModVariables.MapVariables.get(world).i + entity.getLookAngle().x * 1.5,
                                 y + SimpleMobsModVariables.MapVariables.get(world).j,
                                 z + SimpleMobsModVariables.MapVariables.get(world).x + entity.getLookAngle().z * 1.5
                              ),
                              false
                           );
                        }

                        SimpleMobsModVariables.MapVariables.get(world).x++;
                        SimpleMobsModVariables.MapVariables.get(world).syncData(world);
                     }

                     SimpleMobsModVariables.MapVariables.get(world).x = -2.0;
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
               SimpleMobsModVariables.MapVariables.get(world).j = 1.0;
               SimpleMobsModVariables.MapVariables.get(world).syncData(world);
               SimpleMobsModVariables.MapVariables.get(world).x = 0.0;
               SimpleMobsModVariables.MapVariables.get(world).syncData(world);
            }

            if (entity.getPersistentData().getBoolean("Roaring")) {
               if (entity.getPersistentData().getDouble("Roar1") == 0.0) {
                  entity.getPersistentData().putDouble("Roar1", 45.0);
               } else {
                  entity.getPersistentData().putDouble("Roar1", entity.getPersistentData().getDouble("Roar1") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Roar1") == 3.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putDouble("Roar1", 0.0);
                  entity.getPersistentData().putBoolean("Farattacks", false);
                  entity.getPersistentData().putBoolean("Roaring", false);
               } else if (entity.getPersistentData().getDouble("Roar1") == 25.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(11.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof CaveDwellerEntity)) {
                        if (entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 60, 1, false, false));
                        }

                        if (entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 1, false, false));
                        }

                        if (entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2, false, false));
                        }
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Roar1") >= 43.0) {
                  entity.getPersistentData().putBoolean("Farattacks", true);
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.LEATHER_HELMET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 45, 5, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Roar1") == 37.0 && world instanceof Level _level) {
                  if (!_level.isClientSide()) {
                     _level.playSound(
                        null,
                        BlockPos.containing(x, y, z),
                        dev.deepnight.SafeSounds.resolve("simple_mobs:roar"),
                        SoundSource.NEUTRAL,
                        3.0F,
                        1.0F
                     );
                  } else {
                     _level.playLocalSound(
                        x, y, z, dev.deepnight.SafeSounds.resolve("simple_mobs:roar"), SoundSource.NEUTRAL, 3.0F, 1.0F, false
                     );
                  }
               }
            }

            if (entity.onGround()
               && Math.sqrt(entity.getDeltaMovement().x() * entity.getDeltaMovement().x() + entity.getDeltaMovement().z() * entity.getDeltaMovement().z())
                  > 0.1) {
               if (entity.getPersistentData().getDouble("Run") == 0.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:small_step"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:small_step"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  entity.getPersistentData().putDouble("Run", 10.0);
               } else {
                  entity.getPersistentData().putDouble("Run", entity.getPersistentData().getDouble("Run") - 1.0);
               }
            }
         }
      }
   }
}
