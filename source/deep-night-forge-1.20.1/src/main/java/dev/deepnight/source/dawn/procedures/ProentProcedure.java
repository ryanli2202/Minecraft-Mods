package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.AirEntity;
import dev.deepnight.source.dawn.entity.EntEntity;
import dev.deepnight.source.dawn.entity.WhispererStem2Entity;
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
import net.minecraft.tags.BlockTags;
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
import net.minecraft.world.item.BoneMealItem;
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

public class ProentProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         double j = 0.0;
         double c = 0.0;
         double b2 = 0.0;
         double a = 0.0;
         double a2 = 0.0;
         double b = 0.0;
         double i2 = 0.0;
         double l = 0.0;
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
            if (entity.isAlive()) {
               if (entity.getPersistentData().getBoolean("hurtcd")) {
                  if (entity.getPersistentData().getDouble("hurt_cd") == 0.0) {
                     entity.getPersistentData().putDouble("hurt_cd", 30.0);
                  } else {
                     entity.getPersistentData().putDouble("hurt_cd", entity.getPersistentData().getDouble("hurt_cd") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("hurt_cd") == 1.0) {
                     entity.getPersistentData().putDouble("hurt_cd", 0.0);
                     entity.getPersistentData().putBoolean("hurtcd", false);
                  }
               }

               entity.getPersistentData().putBoolean("whisperer", true);
               if (entity.getPersistentData().getBoolean("Wake")) {
                  if (entity.getPersistentData().getDouble("Poise") > 0.0) {
                     if (entity.getPersistentData().getBoolean("Attacking")) {
                        if (Math.random() >= 0.8) {
                           entity.getPersistentData().putDouble("Poise", entity.getPersistentData().getDouble("Poise") - 1.0);
                        }
                     } else {
                        entity.getPersistentData().putDouble("Poise", entity.getPersistentData().getDouble("Poise") - 1.0);
                     }
                  }

                  if (entity.getPersistentData().getDouble("Poise") > 400.0 && !entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putBoolean("Stunned", true);
                  }

                  if (entity.getPersistentData().getBoolean("sweep")) {
                     entity.getPersistentData().putDouble("direction", Math.abs(entity.getYRot() / 360.0F) * 12.0F);
                     if (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop") > 12.0) {
                        entity.getPersistentData().putDouble("direction", entity.getPersistentData().getDouble("direction") - 12.0);
                     } else if (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop") < 0.0) {
                        entity.getPersistentData().putDouble("direction", entity.getPersistentData().getDouble("direction") + 12.0);
                     }

                     if (!(entity.getPersistentData().getDouble("loop") < 4.0)) {
                        entity.getPersistentData().putDouble("loop", 0.0);
                        entity.getPersistentData().putBoolean("sweep", false);
                     } else {
                        Vec3 _center = new Vec3(
                           x
                              + Math.cos((Math.PI / 6) * (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop")))
                                 * 3.5,
                           y + 0.5,
                           z
                              + Math.sin((Math.PI / 6) * (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop")))
                                 * 3.5
                        );

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (!entityiterator.getPersistentData().getBoolean("whisperer")) {
                              SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                 .setInitialPosition(entity.position())
                                 .setStunType(StunType.SHORT)
                                 .setImpact(3.0F)
                                 .setArmorNegation(10.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 20.0F);
                           }
                        }

                        if (world instanceof ServerLevel _level) {
                           _level.getServer()
                              .getCommands()
                              .performPrefixedCommand(
                                 new CommandSourceStack(
                                       CommandSource.NULL,
                                       new Vec3(
                                          x
                                             + Math.cos(
                                                   (Math.PI / 6)
                                                      * (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop"))
                                                )
                                                * 3.5,
                                          y + entity.getPersistentData().getDouble("loop") * 0.4,
                                          z
                                             + Math.sin(
                                                   (Math.PI / 6)
                                                      * (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop"))
                                                )
                                                * 3.5
                                       ),
                                       Vec2.ZERO,
                                       _level,
                                       4,
                                       "",
                                       Component.literal(""),
                                       _level.getServer(),
                                       null
                                    )
                                    .withSuppressedOutput(),
                                 "/particle minecraft:block minecraft:smooth_red_sandstone ~ ~ ~ .25 .25 .25 0 100"
                              );
                        }

                        entity.getPersistentData().putDouble("loop", entity.getPersistentData().getDouble("loop") + 1.0);
                     }
                  }

                  if (entity.getPersistentData().getBoolean("rsweep")) {
                     entity.getPersistentData().putDouble("direction", Math.abs(entity.getYRot() / 360.0F) * 12.0F);
                     if (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop") > 0.0) {
                        entity.getPersistentData().putDouble("direction", entity.getPersistentData().getDouble("direction") - 12.0);
                     } else if (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop") < 0.0) {
                        entity.getPersistentData().putDouble("direction", entity.getPersistentData().getDouble("direction") + 12.0);
                     }

                     if (entity.getPersistentData().getDouble("loop") <= 4.0 && entity.getPersistentData().getDouble("loop") >= 0.0) {
                        Vec3 _center = new Vec3(
                           x
                              + Math.cos((Math.PI / 6) * (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop")))
                                 * 3.5,
                           y + 0.5,
                           z
                              + Math.sin((Math.PI / 6) * (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop")))
                                 * 3.5
                        );

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (!entityiterator.getPersistentData().getBoolean("whisperer")) {
                              SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                 .setInitialPosition(entity.position())
                                 .setStunType(StunType.SHORT)
                                 .setImpact(3.0F)
                                 .setArmorNegation(10.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 20.0F);
                           }
                        }

                        if (world instanceof ServerLevel _level) {
                           _level.getServer()
                              .getCommands()
                              .performPrefixedCommand(
                                 new CommandSourceStack(
                                       CommandSource.NULL,
                                       new Vec3(
                                          x
                                             + Math.cos(
                                                   (Math.PI / 6)
                                                      * (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop"))
                                                )
                                                * 3.5,
                                          y,
                                          z
                                             + Math.sin(
                                                   (Math.PI / 6)
                                                      * (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop"))
                                                )
                                                * 3.5
                                       ),
                                       Vec2.ZERO,
                                       _level,
                                       4,
                                       "",
                                       Component.literal(""),
                                       _level.getServer(),
                                       null
                                    )
                                    .withSuppressedOutput(),
                                 "/particle minecraft:block minecraft:smooth_red_sandstone ~ ~ ~ .25 .25 .25 0 100"
                              );
                        }

                        entity.getPersistentData().putDouble("loop", entity.getPersistentData().getDouble("loop") - 1.0);
                     } else {
                        entity.getPersistentData().putDouble("loop", 0.0);
                        entity.getPersistentData().putBoolean("rsweep", false);
                     }
                  }

                  if (entity.getPersistentData().getBoolean("spin")) {
                     entity.getPersistentData().putDouble("direction", Math.abs(entity.getYRot() / 360.0F) * 12.0F);
                     if (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop") > 12.0) {
                        entity.getPersistentData().putDouble("direction", entity.getPersistentData().getDouble("direction") - 12.0);
                     } else if (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop") < 0.0) {
                        entity.getPersistentData().putDouble("direction", entity.getPersistentData().getDouble("direction") + 12.0);
                     }

                     if (!(entity.getPersistentData().getDouble("loop") < 12.0)) {
                        entity.getPersistentData().putDouble("loop", 0.0);
                        entity.getPersistentData().putBoolean("spin", false);
                     } else {
                        Vec3 _center = new Vec3(
                           x + Math.cos((Math.PI / 6) + entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop")) * 3.5,
                           y + 0.5,
                           z + Math.sin((Math.PI / 6) + entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop")) * 3.5
                        );

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (!entityiterator.getPersistentData().getBoolean("whisperer")) {
                              SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                 .setInitialPosition(entity.position())
                                 .setStunType(StunType.SHORT)
                                 .setImpact(3.0F)
                                 .setArmorNegation(10.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 20.0F);
                              entityiterator.setDeltaMovement(
                                 new Vec3((entityiterator.getX() - entity.getX()) * 0.6, 0.7, (entityiterator.getZ() - entity.getZ()) * 0.6)
                              );
                           }
                        }

                        if (world instanceof ServerLevel _level) {
                           _level.getServer()
                              .getCommands()
                              .performPrefixedCommand(
                                 new CommandSourceStack(
                                       CommandSource.NULL,
                                       new Vec3(
                                          x
                                             + Math.cos(
                                                   (Math.PI / 6)
                                                      * (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop"))
                                                )
                                                * 3.5,
                                          y + 0.5,
                                          z
                                             + Math.sin(
                                                   (Math.PI / 6)
                                                      * (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop"))
                                                )
                                                * 3.5
                                       ),
                                       Vec2.ZERO,
                                       _level,
                                       4,
                                       "",
                                       Component.literal(""),
                                       _level.getServer(),
                                       null
                                    )
                                    .withSuppressedOutput(),
                                 "/particle minecraft:block minecraft:smooth_red_sandstone ~ ~ ~ .25 .25 .25 0 100"
                              );
                        }

                        entity.getPersistentData().putDouble("loop", entity.getPersistentData().getDouble("loop") + 1.0);
                     }
                  }

                  entity.getPersistentData().putBoolean("Aggro", false);
                  entity.getPersistentData().putBoolean("Close", false);
                  entity.getPersistentData().putBoolean("Near", false);
                  entity.getPersistentData().putBoolean("Far", false);
                  entity.getPersistentData().putBoolean("Mid", false);
                  Vec3 _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(32.0), e -> true)
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
                              "pitch2",
                              Math.toDegrees(Math.atan(entity.getPersistentData().getDouble("distance") / (entityiterator.getY() - (entity.getY() + 0.8))))
                           );
                        if (entity.getPersistentData().getDouble("pitch2") > 0.0) {
                           entity.getPersistentData().putDouble("pitch2", 90.0 - entity.getPersistentData().getDouble("pitch2"));
                        } else if (entity.getPersistentData().getDouble("pitch2") < 0.0) {
                           entity.getPersistentData().putDouble("pitch2", 90.0 + entity.getPersistentData().getDouble("pitch2"));
                        }

                        entity.getPersistentData().putBoolean("Aggro", true);
                        entity.getPersistentData().putBoolean("Far", true);
                        entity.getPersistentData().putBoolean("Mid", false);
                        entity.getPersistentData().putBoolean("Near", false);
                        entity.getPersistentData().putBoolean("Close", false);
                     }
                  }

                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        entity.getPersistentData().putBoolean("Far", false);
                        entity.getPersistentData().putBoolean("Mid", true);
                        entity.getPersistentData().putBoolean("Near", false);
                        entity.getPersistentData().putBoolean("Close", false);
                     }
                  }

                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        entity.getPersistentData().putBoolean("Far", false);
                        entity.getPersistentData().putBoolean("Mid", false);
                        entity.getPersistentData().putBoolean("Near", true);
                        entity.getPersistentData().putBoolean("Close", false);
                     }
                  }

                  _center = new Vec3(x + entity.getLookAngle().x * 2.5, y, z + entity.getLookAngle().z * 2.5);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        entity.getPersistentData().putBoolean("Far", false);
                        entity.getPersistentData().putBoolean("Mid", false);
                        entity.getPersistentData().putBoolean("Near", false);
                        entity.getPersistentData().putBoolean("Close", true);
                     }
                  }

                  if (entity.onGround() && world.getBlockState(BlockPos.containing(x, y - 1.0, z)).is(BlockTags.create(new ResourceLocation("minecraft:dirt")))) {
                     if (entity.getPersistentData().getDouble("cd") != 0.0) {
                        if (entity.getPersistentData().getDouble("cd") > 0.0) {
                           entity.getPersistentData().putDouble("cd", entity.getPersistentData().getDouble("cd") - 1.0);
                        }
                     } else {
                        entity.getPersistentData().putDouble("cd", 40.0);
                        l = -6.0;
                        j = -2.0;
                        c = -6.0;

                        while (l <= 6.0) {
                           while (j <= 1.0) {
                              for (; c <= 6.0; c++) {
                                 if (world.getBlockState(BlockPos.containing(x + l, y + j, z + c)).is(BlockTags.create(new ResourceLocation("minecraft:saplings")))) {
                                    if (world instanceof Level _level) {
                                       BlockPos _bp = BlockPos.containing(x + l, y + j, z + c);
                                       if ((
                                             BoneMealItem.growCrop(new ItemStack(Items.BONE_MEAL), _level, _bp)
                                                || BoneMealItem.growWaterPlant(new ItemStack(Items.BONE_MEAL), _level, _bp, null)
                                          )
                                          && !_level.isClientSide()) {
                                          _level.levelEvent(2005, _bp, 0);
                                       }
                                    }
                                 } else if (l <= 1.0
                                    && l >= -1.0
                                    && c <= 1.0
                                    && c >= -1.0
                                    && !entity.getPersistentData().getBoolean("tree_drain")
                                    && !entity.getPersistentData().getBoolean("tree_connected")
                                    && world.getBlockState(BlockPos.containing(x + l, y, z + c)).is(BlockTags.create(new ResourceLocation("minecraft:logs")))) {
                                    entity.getPersistentData().putBoolean("tree_connected", true);
                                    entity.getPersistentData().putDouble("tree_x", x + l);
                                    entity.getPersistentData().putDouble("tree_y", y);
                                    entity.getPersistentData().putDouble("tree_z", z + c);
                                 }
                              }

                              c = -6.0;
                              j++;
                           }

                           j = -2.0;
                           l++;
                        }

                        if (world instanceof Level _level) {
                           BlockPos _bp = BlockPos.containing(x, y - 1.0, z);
                           if ((
                                 BoneMealItem.growCrop(new ItemStack(Items.BONE_MEAL), _level, _bp)
                                    || BoneMealItem.growWaterPlant(new ItemStack(Items.BONE_MEAL), _level, _bp, null)
                              )
                              && !_level.isClientSide()) {
                              _level.levelEvent(2005, _bp, 0);
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
                                 "/particle minecraft:block minecraft:dirt ~ ~ ~ .25 .25 .25 0 100"
                              );
                        }
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Near")
                     && !entity.getPersistentData().getBoolean("Attacking")
                     && Math.random() >= 0.5
                     && entity.getPersistentData().getDouble("Atk2CD") == 0.0) {
                     entity.getPersistentData().putBoolean("Attack2", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  }

                  if (!entity.getPersistentData().getBoolean("Close")
                     && !entity.getPersistentData().getBoolean("Attacking")
                     && (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1.0F) * 0.7
                        > (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0F)
                     && Math.random() <= 0.1
                     && entity.getPersistentData().getDouble("Atk4CD") == 0.0) {
                     entity.getPersistentData().putBoolean("Attack4b", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  }

                  if (entity.getPersistentData().getBoolean("Far") && !entity.getPersistentData().getBoolean("Attacking")) {
                     if (Math.random() > 0.7 && entity.getPersistentData().getDouble("Atk4CD") == 0.0) {
                        entity.getPersistentData().putBoolean("Attack4", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (Math.random() > 0.3 && Math.random() <= 0.79 && entity.getPersistentData().getDouble("Atk4CD") == 0.0) {
                        entity.getPersistentData().putBoolean("Attack4a", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (entity.getPersistentData().getDouble("Atk4CD") == 0.0) {
                        entity.getPersistentData().putBoolean("Attack4b", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Mid") && !entity.getPersistentData().getBoolean("Attacking")) {
                     if (Math.random() >= 0.8) {
                        entity.getPersistentData().putBoolean("Attack1c", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (Math.random() > 0.3 && Math.random() <= 0.79 && entity.getPersistentData().getDouble("Atk6CD") == 0.0) {
                        entity.getPersistentData().putBoolean("Attack6", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Close") && !entity.getPersistentData().getBoolean("Attacking")) {
                     if (Math.random() >= 0.8) {
                        entity.getPersistentData().putBoolean("Attack1", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (Math.random() > 0.3 && Math.random() <= 0.79 && entity.getPersistentData().getDouble("Atk3CD") == 0.0) {
                        entity.getPersistentData().putBoolean("Attack3", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (Math.random() <= 0.4 && entity.getPersistentData().getDouble("Atk5CD") == 0.0) {
                        entity.getPersistentData().putBoolean("Attack5", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (Math.random() <= 0.3) {
                        entity.getPersistentData().putBoolean("Attack1c", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     }
                  }

                  if (entity.getPersistentData().getDouble("Atk2CD") == 0.0 && entity.getPersistentData().getBoolean("Attack2")) {
                     entity.getPersistentData().putDouble("Atk2CD", 300.0);
                  } else if (entity.getPersistentData().getDouble("Atk2CD") > 0.0) {
                     entity.getPersistentData().putDouble("Atk2CD", entity.getPersistentData().getDouble("Atk2CD") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Atk3CD") == 0.0 && entity.getPersistentData().getBoolean("Attack3")) {
                     entity.getPersistentData().putDouble("Atk3CD", 200.0);
                  } else if (entity.getPersistentData().getDouble("Atk3CD") > 0.0) {
                     entity.getPersistentData().putDouble("Atk3CD", entity.getPersistentData().getDouble("Atk3CD") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Atk4CD") != 0.0
                     || !entity.getPersistentData().getBoolean("Attack4b")
                        && !entity.getPersistentData().getBoolean("Attack4a")
                        && !entity.getPersistentData().getBoolean("Attack4")) {
                     if (entity.getPersistentData().getDouble("Atk4CD") > 0.0) {
                        entity.getPersistentData().putDouble("Atk4CD", entity.getPersistentData().getDouble("Atk4CD") - 1.0);
                     }
                  } else {
                     entity.getPersistentData().putDouble("Atk4CD", Mth.nextInt(net.minecraft.util.RandomSource.create(), 220, 300));
                  }

                  if (entity.getPersistentData().getDouble("Atk5CD") == 0.0 && entity.getPersistentData().getBoolean("Attack5")) {
                     entity.getPersistentData().putDouble("Atk5CD", 180.0);
                  } else if (entity.getPersistentData().getDouble("Atk5CD") > 0.0) {
                     entity.getPersistentData().putDouble("Atk5CD", entity.getPersistentData().getDouble("Atk5CD") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Atk6CD") == 0.0 && entity.getPersistentData().getBoolean("Attack6")) {
                     entity.getPersistentData().putDouble("Atk6CD", 200.0);
                  } else if (entity.getPersistentData().getDouble("Atk6CD") > 0.0) {
                     entity.getPersistentData().putDouble("Atk6CD", entity.getPersistentData().getDouble("Atk6CD") - 1.0);
                  }

                  if (entity.getPersistentData().getBoolean("Sprout")) {
                     if (entity.getPersistentData().getDouble("SproutCD") == 0.0) {
                        entity.getPersistentData().putDouble("SproutCD", 11.0);
                     } else {
                        entity.getPersistentData().putDouble("SproutCD", entity.getPersistentData().getDouble("SproutCD") - 1.0);
                     }

                     if (entity.getPersistentData().getDouble("SproutCD") == 1.0) {
                        entity.getPersistentData().putDouble("SproutCD", 0.0);
                        entity.getPersistentData().putBoolean("Sprout", false);
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Attack1")) {
                     if (entity.getPersistentData().getDouble("Atk1") == 0.0) {
                        entity.getPersistentData().putDouble("Atk1", 71.0);
                     } else {
                        entity.getPersistentData().putDouble("Atk1", entity.getPersistentData().getDouble("Atk1") - 1.0);
                     }

                     if (entity.getPersistentData().getDouble("Atk1") == 71.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Items.LEATHER_HELMET));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                        }

                        entity.getPersistentData().putBoolean("Attacking", true);
                        entity.getPersistentData().putBoolean("focus", true);
                        if (entity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                           _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 61, 6, false, false));
                        }
                     } else if (entity.getPersistentData().getDouble("Atk1") == 41.0) {
                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:martian.attack"),
                                 SoundSource.NEUTRAL,
                                 1.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:martian.attack"),
                                 SoundSource.NEUTRAL,
                                 1.0F,
                                 1.0F,
                                 false
                              );
                           }
                        }

                        entity.getPersistentData().putDouble("loop", 0.0);
                        entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                        entity.getPersistentData().putBoolean("lock", true);
                        entity.getPersistentData().putBoolean("noai", true);
                        entity.getPersistentData().putBoolean("focus", false);
                        entity.getPersistentData().putBoolean("sweep", true);
                     } else if (entity.getPersistentData().getDouble("Atk1") == 27.0) {
                        if (Math.random() > 0.3) {
                           _center = new Vec3(x + entity.getLookAngle().x * 2.5, y, z + entity.getLookAngle().z * 2.5);

                           for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                              .stream()
                              .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                              .collect(Collectors.toList())) {
                              if (!entityiterator.getPersistentData().getBoolean("whisperer")) {
                                 entity.getPersistentData().putBoolean("lock", false);
                                 entity.getPersistentData().putBoolean("noai", false);
                                 entity.getPersistentData().putBoolean("focus", true);
                                 entity.getPersistentData().putBoolean("Attack1b", true);
                                 entity.getPersistentData().putDouble("Atk1", 0.0);
                                 entity.getPersistentData().putBoolean("Attack1", false);
                              }
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

                        entity.getPersistentData().putBoolean("lock", false);
                        entity.getPersistentData().putBoolean("noai", false);
                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putDouble("Atk1", 0.0);
                        entity.getPersistentData().putBoolean("Attack1", false);
                     }

                     if (entity.getPersistentData().getDouble("Atk1") <= 41.0 && entity.getPersistentData().getDouble("Atk1") >= 36.0) {
                        entity.setDeltaMovement(
                           new Vec3(
                              Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180.0)) * 0.1,
                              0.0,
                              Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * 0.1
                           )
                        );
                     }

                     if (entity.getPersistentData().getBoolean("Stunned")) {
                        entity.getPersistentData().putBoolean("Attack1", false);
                        entity.getPersistentData().putDouble("Atk1", 0.0);
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Attack1b")) {
                     if (entity.getPersistentData().getDouble("Atk1b") == 0.0) {
                        entity.getPersistentData().putDouble("Atk1b", 56.0);
                     } else {
                        entity.getPersistentData().putDouble("Atk1b", entity.getPersistentData().getDouble("Atk1b") - 1.0);
                     }

                     if (entity.getPersistentData().getDouble("Atk1b") == 56.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Items.GOLD_NUGGET));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLD_NUGGET));
                        }

                        entity.getPersistentData().putBoolean("Attacking", true);
                        if (entity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                           _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 56, 6, false, false));
                        }
                     } else if (entity.getPersistentData().getDouble("Atk1b") == 41.0) {
                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:martian.attack"),
                                 SoundSource.NEUTRAL,
                                 1.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:martian.attack"),
                                 SoundSource.NEUTRAL,
                                 1.0F,
                                 1.0F,
                                 false
                              );
                           }
                        }

                        entity.getPersistentData().putDouble("loop", 4.0);
                        entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                        entity.getPersistentData().putDouble("pitch", entity.getPersistentData().getDouble("pitch2"));
                        entity.getPersistentData().putBoolean("focus", false);
                        entity.getPersistentData().putBoolean("lock", true);
                        entity.getPersistentData().putBoolean("rsweep", true);
                     } else if (entity.getPersistentData().getDouble("Atk1b") == 21.0) {
                        _center = new Vec3(x + entity.getLookAngle().x * 4.0, y, z + entity.getLookAngle().z * 4.0);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (!entityiterator.getPersistentData().getBoolean("whisperer")) {
                              entity.getPersistentData().putBoolean("lock", false);
                              entity.getPersistentData().putBoolean("focus", true);
                              entity.getPersistentData().putBoolean("Attack1c", true);
                              entity.getPersistentData().putDouble("Atk1b", 0.0);
                              entity.getPersistentData().putBoolean("Attack1b", false);
                           }
                        }
                     } else if (entity.getPersistentData().getDouble("Atk1b") == 1.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                        }

                        entity.getPersistentData().putBoolean("lock", false);
                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putDouble("Atk1b", 0.0);
                        entity.getPersistentData().putBoolean("Attack1b", false);
                     }

                     if (entity.getPersistentData().getDouble("Atk1b") <= 41.0 && entity.getPersistentData().getDouble("Atk1b") >= 36.0) {
                        entity.setDeltaMovement(
                           new Vec3(
                              Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180.0)) * 0.1,
                              0.0,
                              Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * 0.1
                           )
                        );
                     }

                     if (entity.getPersistentData().getBoolean("Stunned")) {
                        entity.getPersistentData().putBoolean("Attack1b", false);
                        entity.getPersistentData().putDouble("Atk1b", 0.0);
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Attack1c")) {
                     if (entity.getPersistentData().getDouble("Atk1c") == 0.0) {
                        entity.getPersistentData().putDouble("Atk1c", 71.0);
                     } else {
                        entity.getPersistentData().putDouble("Atk1c", entity.getPersistentData().getDouble("Atk1c") - 1.0);
                     }

                     if (entity.getPersistentData().getDouble("Atk1c") == 71.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Items.GOLD_INGOT));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLD_INGOT));
                        }

                        entity.getPersistentData().putBoolean("Attacking", true);
                        entity.getPersistentData().putBoolean("focus", true);
                        if (entity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                           _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 66, 6, false, false));
                        }
                     } else if (entity.getPersistentData().getDouble("Atk1c") == 48.0) {
                        entity.getPersistentData().putBoolean("focus", false);
                        entity.getPersistentData().putBoolean("lock", true);
                        entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                        entity.getPersistentData().putDouble("pitch", entity.getPersistentData().getDouble("pitch2"));
                        entity.getPersistentData().putBoolean("noai", true);
                     } else if (entity.getPersistentData().getDouble("Atk1c") == 43.0) {
                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x + entity.getLookAngle().x * 5.0, y, z + entity.getLookAngle().z * 5.0),
                                 dev.deepnight.SafeSounds.resolve("block.anvil.land"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 0.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x + entity.getLookAngle().x * 5.0,
                                 y,
                                 z + entity.getLookAngle().z * 5.0,
                                 dev.deepnight.SafeSounds.resolve("block.anvil.land"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 0.0F,
                                 false
                              );
                           }
                        }

                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x + entity.getLookAngle().x * 5.0, y, z + entity.getLookAngle().z * 5.0),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:martian.attack"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x + entity.getLookAngle().x * 5.0,
                                 y,
                                 z + entity.getLookAngle().z * 5.0,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:martian.attack"),
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
                                       CommandSource.NULL,
                                       new Vec3(x + entity.getLookAngle().x * 5.0, y, z + entity.getLookAngle().z * 5.0),
                                       Vec2.ZERO,
                                       _level,
                                       4,
                                       "",
                                       Component.literal(""),
                                       _level.getServer(),
                                       null
                                    )
                                    .withSuppressedOutput(),
                                 "/particle minecraft:block minecraft:smooth_red_sandstone ~ ~ ~ .4 .4 .4 0 100"
                              );
                        }

                        _center = new Vec3(x + entity.getLookAngle().x * 5.0, y, z + entity.getLookAngle().z * 5.0);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (!entityiterator.getPersistentData().getBoolean("whisperer")) {
                              SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                 .setInitialPosition(entity.position())
                                 .setStunType(StunType.SHORT)
                                 .setImpact(3.0F)
                                 .setArmorNegation(10.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 26.0F);
                           }
                        }
                     } else if (entity.getPersistentData().getDouble("Atk1c") == 1.0) {
                        entity.getPersistentData().putBoolean("noai", false);
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                        }

                        entity.getPersistentData().putBoolean("lock", false);
                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putDouble("Atk1c", 0.0);
                        entity.getPersistentData().putBoolean("Attack1c", false);
                     }

                     if (entity.getPersistentData().getBoolean("Stunned")) {
                        entity.getPersistentData().putBoolean("Attack1c", false);
                        entity.getPersistentData().putDouble("Atk1c", 0.0);
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Attack2")) {
                     if (entity.getPersistentData().getDouble("Atk2") == 0.0) {
                        entity.getPersistentData().putDouble("Atk2", 71.0);
                     } else {
                        entity.getPersistentData().putDouble("Atk2", entity.getPersistentData().getDouble("Atk2") - 1.0);
                     }

                     if (entity.getPersistentData().getDouble("Atk2") == 71.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Items.COAL));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.COAL));
                        }

                        entity.getPersistentData().putBoolean("Attacking", true);
                        entity.getPersistentData().putBoolean("focus", true);
                        if (entity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                           _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 71, 6, false, false));
                        }
                     } else if (entity.getPersistentData().getDouble("Atk2") == 63.0) {
                        if (world instanceof ServerLevel _level) {
                           _level.getServer()
                              .getCommands()
                              .performPrefixedCommand(
                                 new CommandSourceStack(
                                       CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                    )
                                    .withSuppressedOutput(),
                                 "/data merge entity @e[type=deepnight:dawn_ent,sort=nearest,limit=1] {Invulnerable:1b}"
                              );
                        }
                     } else if (entity.getPersistentData().getDouble("Atk2") == 51.0) {
                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                                 SoundSource.NEUTRAL,
                                 3.0F,
                                 2.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                                 SoundSource.NEUTRAL,
                                 3.0F,
                                 2.0F,
                                 false
                              );
                           }
                        }

                        entity.getPersistentData().putBoolean("focus", false);
                        entity.getPersistentData().putBoolean("lock", true);
                        entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                        entity.getPersistentData().putDouble("pitch", entity.getPersistentData().getDouble("pitch2"));
                     } else if (entity.getPersistentData().getDouble("Atk2") == 27.0) {
                        if (world instanceof ServerLevel _level) {
                           _level.getServer()
                              .getCommands()
                              .performPrefixedCommand(
                                 new CommandSourceStack(
                                       CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                    )
                                    .withSuppressedOutput(),
                                 "/data merge entity @e[type=deepnight:dawn_ent,sort=nearest,limit=1] {Invulnerable:0}"
                              );
                        }

                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:jump"),
                                 SoundSource.NEUTRAL,
                                 3.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:jump"),
                                 SoundSource.NEUTRAL,
                                 3.0F,
                                 1.0F,
                                 false
                              );
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

                        _center = new Vec3(x, y, z);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(6.0), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (!entityiterator.getPersistentData().getBoolean("whisperer")) {
                              SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                 .setInitialPosition(entity.position())
                                 .setStunType(StunType.SHORT)
                                 .setImpact(3.0F)
                                 .setArmorNegation(10.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 26.0F);
                           }
                        }

                        _center = new Vec3(x, y, z);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(11.0), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (!entityiterator.getPersistentData().getBoolean("whisperer") && entityiterator.onGround()) {
                              entityiterator.hurt(new DamageSource(entityiterator.level().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(net.minecraft.world.damagesource.DamageTypes.GENERIC), entity), 18.0F);
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
                                 "/particle minecraft:campfire_cosy_smoke ~ ~ ~ 3 0 3 1 30"
                              );
                        }
                     } else if (entity.getPersistentData().getDouble("Atk2") == 1.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                        }

                        entity.getPersistentData().putBoolean("lock", false);
                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putDouble("Atk2", 0.0);
                        entity.getPersistentData().putBoolean("Attack2", false);
                     }

                     if (entity.getPersistentData().getBoolean("Stunned")) {
                        if (world instanceof ServerLevel _level) {
                           _level.getServer()
                              .getCommands()
                              .performPrefixedCommand(
                                 new CommandSourceStack(
                                       CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                    )
                                    .withSuppressedOutput(),
                                 "/data merge entity @e[type=deepnight:dawn_ent,sort=nearest,limit=1] {Invulnerable:0}"
                              );
                        }

                        entity.getPersistentData().putBoolean("Attack2", false);
                        entity.getPersistentData().putDouble("Atk2", 0.0);
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Attack3")) {
                     if (entity.getPersistentData().getDouble("Atk3") == 0.0) {
                        entity.getPersistentData().putDouble("Atk3", 101.0);
                     } else {
                        entity.getPersistentData().putDouble("Atk3", entity.getPersistentData().getDouble("Atk3") - 1.0);
                     }

                     if (entity.getPersistentData().getDouble("Atk3") == 101.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Items.CHARCOAL));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHARCOAL));
                        }

                        entity.getPersistentData().putBoolean("Attacking", true);
                        entity.getPersistentData().putBoolean("focus", true);
                        if (entity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                           _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 101, 6, false, false));
                        }
                     } else if (entity.getPersistentData().getDouble("Atk3") == 67.0) {
                        entity.getPersistentData().putBoolean("focus", false);
                        entity.getPersistentData().putBoolean("lock", true);
                        entity.getPersistentData().putBoolean("noai", true);
                        entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                        entity.getPersistentData().putDouble("pitch", entity.getPersistentData().getDouble("pitch2"));
                     } else if (entity.getPersistentData().getDouble("Atk3") == 59.0) {
                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x + entity.getLookAngle().x * 3.0, y, z + entity.getLookAngle().z * 3.0),
                                 dev.deepnight.SafeSounds.resolve("block.anvil.land"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 0.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x + entity.getLookAngle().x * 3.0,
                                 y,
                                 z + entity.getLookAngle().z * 3.0,
                                 dev.deepnight.SafeSounds.resolve("block.anvil.land"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 0.0F,
                                 false
                              );
                           }
                        }

                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x + entity.getLookAngle().x * 3.0, y, z + entity.getLookAngle().z * 3.0),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:martian.attack"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x + entity.getLookAngle().x * 3.0,
                                 y,
                                 z + entity.getLookAngle().z * 3.0,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:martian.attack"),
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
                                       CommandSource.NULL,
                                       new Vec3(x + entity.getLookAngle().x * 3.0, y, z + entity.getLookAngle().z * 3.0),
                                       Vec2.ZERO,
                                       _level,
                                       4,
                                       "",
                                       Component.literal(""),
                                       _level.getServer(),
                                       null
                                    )
                                    .withSuppressedOutput(),
                                 "/particle minecraft:block minecraft:smooth_red_sandstone ~ ~ ~ .4 .4 .4 0 100"
                              );
                        }

                        _center = new Vec3(x + entity.getLookAngle().x * 3.0, y, z + entity.getLookAngle().z * 3.0);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (!entityiterator.getPersistentData().getBoolean("whisperer")) {
                              SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                 .setInitialPosition(entity.position())
                                 .setStunType(StunType.SHORT)
                                 .setImpact(3.0F)
                                 .setArmorNegation(10.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 22.0F);
                           }
                        }
                     } else if (entity.getPersistentData().getDouble("Atk3") == 41.0) {
                        _center = new Vec3(x, y, z);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (!entityiterator.getPersistentData().getBoolean("whisperer")) {
                              entity.getPersistentData().putBoolean("Attack3a", true);
                              entity.getPersistentData().putBoolean("Attack3", false);
                              entity.getPersistentData().putDouble("Atk3", 0.0);
                              entity.getPersistentData().putBoolean("noai", false);
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

                        entity.getPersistentData().putBoolean("lock", false);
                        entity.getPersistentData().putBoolean("noai", false);
                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putDouble("Atk3", 0.0);
                        entity.getPersistentData().putBoolean("Attack3", false);
                     }

                     if (entity.getPersistentData().getBoolean("Stunned")) {
                        entity.getPersistentData().putBoolean("Attack3", false);
                        entity.getPersistentData().putDouble("Atk3", 0.0);
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Attack3a")) {
                     if (entity.getPersistentData().getDouble("Atk3a") == 0.0) {
                        entity.getPersistentData().putDouble("Atk3a", 64.0);
                     } else {
                        entity.getPersistentData().putDouble("Atk3a", entity.getPersistentData().getDouble("Atk3a") - 1.0);
                     }

                     if (entity.getPersistentData().getDouble("Atk3a") == 64.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Items.IRON_INGOT));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_INGOT));
                        }

                        entity.getPersistentData().putBoolean("Attacking", true);
                        if (entity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                           _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 64, 6, false, false));
                        }
                     } else if (entity.getPersistentData().getDouble("Atk3a") == 56.0) {
                        entity.getPersistentData().putBoolean("lock", true);
                        entity.getPersistentData().putBoolean("spin", true);
                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:martian.attack"),
                                 SoundSource.NEUTRAL,
                                 3.0F,
                                 2.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:martian.attack"),
                                 SoundSource.NEUTRAL,
                                 3.0F,
                                 2.0F,
                                 false
                              );
                           }
                        }

                        entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                        entity.getPersistentData().putDouble("pitch", entity.getPersistentData().getDouble("pitch2"));
                     } else if (entity.getPersistentData().getDouble("Atk3a") == 31.0) {
                        _center = new Vec3(x + entity.getLookAngle().x * 4.0, y, z + entity.getLookAngle().z * 4.0);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
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

                        entity.getPersistentData().putBoolean("lock", false);
                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putDouble("Atk3a", 0.0);
                        entity.getPersistentData().putBoolean("Attack3a", false);
                     }

                     if (entity.getPersistentData().getDouble("Atk3a") <= 48.0 && entity.getPersistentData().getDouble("Atk3a") >= 43.0) {
                        entity.getPersistentData().putBoolean("lock", false);
                        entity.getPersistentData().putBoolean("focus", true);
                        entity.setDeltaMovement(
                           new Vec3(
                              Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * 0.3,
                              0.0,
                              Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180.0)) * 0.3
                           )
                        );
                     }

                     if (entity.getPersistentData().getBoolean("Stunned")) {
                        entity.getPersistentData().putBoolean("Attack3a", false);
                        entity.getPersistentData().putDouble("Atk3a", 0.0);
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Attack3b")) {
                     if (entity.getPersistentData().getDouble("Atk3b") == 0.0) {
                        entity.getPersistentData().putDouble("Atk3b", 52.0);
                     } else {
                        entity.getPersistentData().putDouble("Atk3b", entity.getPersistentData().getDouble("Atk3b") - 1.0);
                     }

                     if (entity.getPersistentData().getDouble("Atk3b") == 52.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Items.IRON_NUGGET));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_NUGGET));
                        }

                        entity.getPersistentData().putBoolean("Attacking", true);
                        entity.getPersistentData().putBoolean("lock", true);
                        entity.getPersistentData().putBoolean("noai", true);
                        entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                        entity.getPersistentData().putDouble("pitch", entity.getPersistentData().getDouble("pitch2"));
                        if (entity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                           _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 51, 6, false, false));
                        }
                     } else if (entity.getPersistentData().getDouble("Atk3b") == 43.0) {
                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x + entity.getLookAngle().x * 4.0, y, z + entity.getLookAngle().z * 4.0),
                                 dev.deepnight.SafeSounds.resolve("block.anvil.land"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 0.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x + entity.getLookAngle().x * 4.0,
                                 y,
                                 z + entity.getLookAngle().z * 4.0,
                                 dev.deepnight.SafeSounds.resolve("block.anvil.land"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 0.0F,
                                 false
                              );
                           }
                        }

                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x + entity.getLookAngle().x * 4.0, y, z + entity.getLookAngle().z * 4.0),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:martian.attack"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x + entity.getLookAngle().x * 4.0,
                                 y,
                                 z + entity.getLookAngle().z * 4.0,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:martian.attack"),
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
                                       CommandSource.NULL,
                                       new Vec3(x + entity.getLookAngle().x * 4.0, y, z + entity.getLookAngle().z * 4.0),
                                       Vec2.ZERO,
                                       _level,
                                       4,
                                       "",
                                       Component.literal(""),
                                       _level.getServer(),
                                       null
                                    )
                                    .withSuppressedOutput(),
                                 "/particle minecraft:block minecraft:smooth_red_sandstone ~ ~ ~ .4 .4 .4 0 100"
                              );
                        }

                        _center = new Vec3(x + entity.getLookAngle().x * 4.0, y, z + entity.getLookAngle().z * 4.0);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (!entityiterator.getPersistentData().getBoolean("whisperer")) {
                              SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                 .setInitialPosition(entity.position())
                                 .setStunType(StunType.KNOCKDOWN)
                                 .setImpact(3.0F)
                                 .setArmorNegation(10.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 18.0F);
                           }
                        }
                     } else if (entity.getPersistentData().getDouble("Atk3b") == 2.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Items.FEATHER));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.FEATHER));
                        }
                     } else if (entity.getPersistentData().getDouble("Atk3b") == 1.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                        }

                        entity.getPersistentData().putBoolean("lock", false);
                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putBoolean("noai", false);
                        entity.getPersistentData().putDouble("Atk3b", 0.0);
                        entity.getPersistentData().putBoolean("Attack3b", false);
                     }

                     if (entity.getPersistentData().getBoolean("Stunned")) {
                        entity.getPersistentData().putBoolean("Attack3b", false);
                        entity.getPersistentData().putDouble("Atk3b", 0.0);
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Attack5")) {
                     if (entity.getPersistentData().getDouble("Atk5") == 0.0) {
                        entity.getPersistentData().putDouble("Atk5", 56.0);
                     } else {
                        entity.getPersistentData().putDouble("Atk5", entity.getPersistentData().getDouble("Atk5") - 1.0);
                     }

                     if (entity.getPersistentData().getDouble("Atk5") == 56.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.WHITE_CONCRETE));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.WHITE_CONCRETE));
                        }

                        entity.getPersistentData().putBoolean("Attacking", true);
                        entity.getPersistentData().putBoolean("focus", true);
                        if (entity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                           _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 56, 6, false, false));
                        }
                     } else if (entity.getPersistentData().getDouble("Atk5") == 41.0) {
                        entity.getPersistentData().putDouble("loop", 0.0);
                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:martian.attack"),
                                 SoundSource.NEUTRAL,
                                 1.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:martian.attack"),
                                 SoundSource.NEUTRAL,
                                 1.0F,
                                 1.0F,
                                 false
                              );
                           }
                        }

                        entity.getPersistentData().putBoolean("focus", false);
                        entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                        entity.getPersistentData().putDouble("pitch", entity.getPersistentData().getDouble("pitch2"));
                        entity.getPersistentData().putBoolean("lock", true);
                        entity.getPersistentData().putBoolean("sweep", true);
                     } else if (entity.getPersistentData().getDouble("Atk5") == 1.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                        }

                        entity.getPersistentData().putBoolean("lock", false);
                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putDouble("Atk5", 0.0);
                        entity.getPersistentData().putBoolean("Attack5", false);
                     }

                     if (entity.getPersistentData().getBoolean("Stunned")) {
                        entity.getPersistentData().putBoolean("Attack5", false);
                        entity.getPersistentData().putDouble("Atk5", 0.0);
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Attack6")) {
                     if (entity.getPersistentData().getDouble("Atk6") == 0.0) {
                        entity.getPersistentData().putDouble("Atk6", 76.0);
                     } else {
                        entity.getPersistentData().putDouble("Atk6", entity.getPersistentData().getDouble("Atk6") - 1.0);
                     }

                     if (entity.getPersistentData().getDouble("Atk6") == 76.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.RED_CONCRETE));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.RED_CONCRETE));
                        }

                        entity.getPersistentData().putBoolean("Attacking", true);
                        entity.getPersistentData().putBoolean("focus", true);
                        if (entity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                           _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 76, 6, false, false));
                        }
                     } else if (entity.getPersistentData().getDouble("Atk6") == 66.0) {
                        entity.getPersistentData().putBoolean("focus", false);
                        entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                        entity.getPersistentData().putDouble("pitch", entity.getPersistentData().getDouble("pitch2"));
                        entity.getPersistentData().putBoolean("lock", true);
                        entity.getPersistentData().putBoolean("noai", true);
                     } else if (entity.getPersistentData().getDouble("Atk6") == 34.0) {
                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:martian.attack"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 0.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:martian.attack"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 0.0F,
                                 false
                              );
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

                        entity.getPersistentData().putBoolean("lock", false);
                        entity.getPersistentData().putBoolean("noai", false);
                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putDouble("Atk6", 0.0);
                        entity.getPersistentData().putBoolean("Attack6", false);
                     }

                     if (entity.getPersistentData().getDouble("Atk6") <= 66.0 && entity.getPersistentData().getDouble("Atk6") >= 36.0) {
                        entity.setDeltaMovement(
                           new Vec3(
                              Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180.0)) * 0.35,
                              0.0,
                              Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * 0.35
                           )
                        );
                        _center = new Vec3(x + entity.getLookAngle().x * 2.0, y + 1.0, z + entity.getLookAngle().z * 2.0);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (!entityiterator.getPersistentData().getBoolean("whisperer")) {
                              SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                 .setInitialPosition(entity.position())
                                 .setStunType(StunType.LONG)
                                 .setImpact(3.0F)
                                 .setArmorNegation(10.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 7.0F);
                              entityiterator.setDeltaMovement(
                                 new Vec3(
                                    Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180.0)) * 1.0,
                                    0.3,
                                    Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * 1.0
                                 )
                              );
                           }
                        }
                     } else if (entity.getPersistentData().getDouble("Atk6") <= 35.0 && entity.getPersistentData().getDouble("Atk6") >= 32.0) {
                        entity.setDeltaMovement(
                           new Vec3(
                              Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180.0)) * 0.1,
                              0.0,
                              Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * 0.1
                           )
                        );
                        _center = new Vec3(x + entity.getLookAngle().x * 2.0, y + 1.0, z + entity.getLookAngle().z * 2.0);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (!(entityiterator instanceof EntEntity)) {
                              SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                 .setInitialPosition(entity.position())
                                 .setStunType(StunType.LONG)
                                 .setImpact(3.0F)
                                 .setArmorNegation(10.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 16.0F);
                              entityiterator.setDeltaMovement(
                                 new Vec3(
                                    Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 90.0)) * 1.0,
                                    1.0,
                                    Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw") - 90.0)) * 1.0
                                 )
                              );
                           }
                        }
                     }

                     if (entity.getPersistentData().getBoolean("Stunned")) {
                        entity.getPersistentData().putBoolean("Attack6", false);
                        entity.getPersistentData().putDouble("Atk6", 0.0);
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     if (entity.getPersistentData().getDouble("stun") == 0.0) {
                        entity.getPersistentData().putDouble("stun", 41.0);
                     } else {
                        entity.getPersistentData().putDouble("stun", entity.getPersistentData().getDouble("stun") - 1.0);
                     }

                     if (entity.getPersistentData().getDouble("stun") == 41.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.POPPY));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.POPPY));
                        }

                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:poise"),
                                 SoundSource.NEUTRAL,
                                 3.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:poise"),
                                 SoundSource.NEUTRAL,
                                 3.0F,
                                 1.0F,
                                 false
                              );
                           }
                        }

                        entity.getPersistentData().putBoolean("Attacking", true);
                        entity.getPersistentData().putBoolean("noai", true);
                        if (entity instanceof LivingEntity _entityx && !_entityx.level().isClientSide()) {
                           _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 41, 7, false, false));
                        }
                     } else if (entity.getPersistentData().getDouble("stun") == 1.0) {
                        entity.getPersistentData().putDouble("Poise", -120.0);
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                        }

                        entity.getPersistentData().putBoolean("noai", false);
                        entity.getPersistentData().putBoolean("lock", false);
                        entity.getPersistentData().putBoolean("focus", false);
                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putBoolean("Stunned", false);
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Waking")) {
                     if (entity.getPersistentData().getDouble("wakecd") == 0.0) {
                        entity.getPersistentData().putDouble("wakecd", 119.0);
                        if (world instanceof ServerLevel _level) {
                           _level.getServer()
                              .getCommands()
                              .performPrefixedCommand(
                                 new CommandSourceStack(
                                       CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                    )
                                    .withSuppressedOutput(),
                                 "/data merge entity @e[type=deepnight:dawn_ent,sort=nearest,limit=1] {Invulnerable:1b}"
                              );
                        }
                     } else {
                        entity.getPersistentData().putDouble("wakecd", entity.getPersistentData().getDouble("wakecd") - 1.0);
                     }

                     if (entity.getPersistentData().getDouble("wakecd") == 1.0) {
                        if (world instanceof ServerLevel _level) {
                           _level.getServer()
                              .getCommands()
                              .performPrefixedCommand(
                                 new CommandSourceStack(
                                       CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                    )
                                    .withSuppressedOutput(),
                                 "/data merge entity @e[type=deepnight:dawn_ent,sort=nearest,limit=1] {Invulnerable:0}"
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
                        entity.getPersistentData().putBoolean("Waking", false);
                        entity.getPersistentData().putBoolean("noai", false);
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Attack4")) {
                     if (entity.getPersistentData().getDouble("Atk4") == 0.0) {
                        entity.getPersistentData().putDouble("Atk4", 95.0);
                     } else {
                        entity.getPersistentData().putDouble("Atk4", entity.getPersistentData().getDouble("Atk4") - 1.0);
                     }

                     if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1, 6, false, false));
                     }

                     if (entity.getPersistentData().getDouble("Atk4") == 95.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.WHITE_WOOL));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.WHITE_WOOL));
                        }

                        entity.getPersistentData().putBoolean("Attacking", true);
                        entity.getPersistentData().putBoolean("focus", true);
                     } else if (entity.getPersistentData().getDouble("Atk4") == 75.0) {
                        entity.getPersistentData().putBoolean("focus", false);
                        entity.getPersistentData().putBoolean("lock", true);
                        entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                        entity.getPersistentData().putDouble("pitch", entity.getPersistentData().getDouble("pitch2"));
                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("entity.evoker.prepare_summon"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("entity.evoker.prepare_summon"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F,
                                 false
                              );
                           }
                        }
                     } else if (entity.getPersistentData().getDouble("Atk4") == 55.0) {
                        int horizontalRadiusSphere = 2;
                        int verticalRadiusSphere = 2;
                        int yIterationsSphere = verticalRadiusSphere;

                        for (int i = -yIterationsSphere; i <= yIterationsSphere; i++) {
                           for (int xi = -horizontalRadiusSphere; xi <= horizontalRadiusSphere; xi++) {
                              for (int zi = -horizontalRadiusSphere; zi <= horizontalRadiusSphere; zi++) {
                                 double distanceSq = (double)(xi * xi) / (horizontalRadiusSphere * horizontalRadiusSphere)
                                    + (double)(i * i) / (verticalRadiusSphere * verticalRadiusSphere)
                                    + (double)(zi * zi) / (horizontalRadiusSphere * horizontalRadiusSphere);
                                 if (distanceSq <= 1.0 && world instanceof ServerLevel _level) {
                                    _level.sendParticles(
                                       ParticleTypes.HAPPY_VILLAGER,
                                       x + xi + entity.getLookAngle().x * 2.0,
                                       y + i + 8.0,
                                       z + zi + entity.getLookAngle().z * 2.0,
                                       1,
                                       0.1,
                                       0.1,
                                       0.1,
                                       0.0
                                    );
                                 }
                              }
                           }
                        }

                        Vec3 _centerx = new Vec3(x, y, z);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_centerx, _centerx).inflate(50.0), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                              if (world instanceof ServerLevel _level) {
                                 Entity entityToSpawn = new WhispererStem2Entity(SimpleMobsModEntities.WHISPERER_STEM_2.get(), _level);
                                 entityToSpawn.moveTo(
                                    entityiterator.getX() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -6, 6),
                                    entityiterator.getY(),
                                    entityiterator.getZ() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -6, 6),
                                    world.getRandom().nextFloat() * 360.0F,
                                    0.0F
                                 );
                                 if (entityToSpawn instanceof Mob _mobToSpawn) {
                                    _mobToSpawn.finalizeSpawn(
                                       _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                                    );
                                 }

                                 world.addFreshEntity(entityToSpawn);
                              }

                              if (world instanceof Level _level) {
                                 BlockPos _bp = BlockPos.containing(
                                    entityiterator.getX() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -6, 6),
                                    entityiterator.getY(),
                                    entityiterator.getZ() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -6, 6)
                                 );
                                 if ((
                                       BoneMealItem.growCrop(new ItemStack(Items.BONE_MEAL), _level, _bp)
                                          || BoneMealItem.growWaterPlant(new ItemStack(Items.BONE_MEAL), _level, _bp, null)
                                    )
                                    && !_level.isClientSide()) {
                                    _level.levelEvent(2005, _bp, 0);
                                 }
                              }
                           }
                        }
                     } else if (entity.getPersistentData().getDouble("Atk4") == 40.0) {
                        int horizontalRadiusSphere = 2;
                        int verticalRadiusSphere = 2;
                        int yIterationsSphere = verticalRadiusSphere;

                        for (int i = -yIterationsSphere; i <= yIterationsSphere; i++) {
                           for (int xi = -horizontalRadiusSphere; xi <= horizontalRadiusSphere; xi++) {
                              for (int zi = -horizontalRadiusSphere; zi <= horizontalRadiusSphere; zi++) {
                                 double distanceSq = (double)(xi * xi) / (horizontalRadiusSphere * horizontalRadiusSphere)
                                    + (double)(i * i) / (verticalRadiusSphere * verticalRadiusSphere)
                                    + (double)(zi * zi) / (horizontalRadiusSphere * horizontalRadiusSphere);
                                 if (distanceSq <= 1.0 && world instanceof ServerLevel _level) {
                                    _level.sendParticles(
                                       ParticleTypes.HAPPY_VILLAGER,
                                       x + xi + entity.getLookAngle().x * 2.0,
                                       y + i + 8.0,
                                       z + zi + entity.getLookAngle().z * 2.0,
                                       1,
                                       0.1,
                                       0.1,
                                       0.1,
                                       0.0
                                    );
                                 }
                              }
                           }
                        }

                        Vec3 _centerx = new Vec3(x, y, z);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_centerx, _centerx).inflate(50.0), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                              if (world instanceof ServerLevel _level) {
                                 Entity entityToSpawn = new WhispererStem2Entity(SimpleMobsModEntities.WHISPERER_STEM_2.get(), _level);
                                 entityToSpawn.moveTo(
                                    entityiterator.getX() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -6, 6),
                                    entityiterator.getY(),
                                    entityiterator.getZ() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -6, 6),
                                    world.getRandom().nextFloat() * 360.0F,
                                    0.0F
                                 );
                                 if (entityToSpawn instanceof Mob _mobToSpawn) {
                                    _mobToSpawn.finalizeSpawn(
                                       _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                                    );
                                 }

                                 world.addFreshEntity(entityToSpawn);
                              }

                              if (world instanceof Level _level) {
                                 BlockPos _bp = BlockPos.containing(
                                    entityiterator.getX() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -6, 6),
                                    entityiterator.getY(),
                                    entityiterator.getZ() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -6, 6)
                                 );
                                 if ((
                                       BoneMealItem.growCrop(new ItemStack(Items.BONE_MEAL), _level, _bp)
                                          || BoneMealItem.growWaterPlant(new ItemStack(Items.BONE_MEAL), _level, _bp, null)
                                    )
                                    && !_level.isClientSide()) {
                                    _level.levelEvent(2005, _bp, 0);
                                 }
                              }
                           }
                        }
                     } else if (entity.getPersistentData().getDouble("Atk4") == 25.0) {
                        int horizontalRadiusSphere = 2;
                        int verticalRadiusSphere = 2;
                        int yIterationsSphere = verticalRadiusSphere;

                        for (int i = -yIterationsSphere; i <= yIterationsSphere; i++) {
                           for (int xi = -horizontalRadiusSphere; xi <= horizontalRadiusSphere; xi++) {
                              for (int zi = -horizontalRadiusSphere; zi <= horizontalRadiusSphere; zi++) {
                                 double distanceSq = (double)(xi * xi) / (horizontalRadiusSphere * horizontalRadiusSphere)
                                    + (double)(i * i) / (verticalRadiusSphere * verticalRadiusSphere)
                                    + (double)(zi * zi) / (horizontalRadiusSphere * horizontalRadiusSphere);
                                 if (distanceSq <= 1.0 && world instanceof ServerLevel _level) {
                                    _level.sendParticles(
                                       ParticleTypes.HAPPY_VILLAGER,
                                       x + xi + entity.getLookAngle().x * 2.0,
                                       y + i + 8.0,
                                       z + zi + entity.getLookAngle().z * 2.0,
                                       1,
                                       0.1,
                                       0.1,
                                       0.1,
                                       0.0
                                    );
                                 }
                              }
                           }
                        }

                        Vec3 _centerx = new Vec3(x, y, z);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_centerx, _centerx).inflate(50.0), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                              if (world instanceof ServerLevel _level) {
                                 Entity entityToSpawn = new WhispererStem2Entity(SimpleMobsModEntities.WHISPERER_STEM_2.get(), _level);
                                 entityToSpawn.moveTo(
                                    entityiterator.getX() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -6, 6),
                                    entityiterator.getY(),
                                    entityiterator.getZ() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -6, 6),
                                    world.getRandom().nextFloat() * 360.0F,
                                    0.0F
                                 );
                                 if (entityToSpawn instanceof Mob _mobToSpawn) {
                                    _mobToSpawn.finalizeSpawn(
                                       _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                                    );
                                 }

                                 world.addFreshEntity(entityToSpawn);
                              }

                              if (world instanceof Level _level) {
                                 BlockPos _bp = BlockPos.containing(
                                    entityiterator.getX() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -6, 6),
                                    entityiterator.getY(),
                                    entityiterator.getZ() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -6, 6)
                                 );
                                 if ((
                                       BoneMealItem.growCrop(new ItemStack(Items.BONE_MEAL), _level, _bp)
                                          || BoneMealItem.growWaterPlant(new ItemStack(Items.BONE_MEAL), _level, _bp, null)
                                    )
                                    && !_level.isClientSide()) {
                                    _level.levelEvent(2005, _bp, 0);
                                 }
                              }
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

                        entity.getPersistentData().putBoolean("lock", false);
                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putDouble("Atk4", 0.0);
                        entity.getPersistentData().putBoolean("Attack4", false);
                     }

                     if (entity.getPersistentData().getBoolean("Stunned")) {
                        entity.getPersistentData().putBoolean("Attack4", false);
                        entity.getPersistentData().putDouble("Atk4", 0.0);
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Attack4a")) {
                     if (entity.getPersistentData().getDouble("Atk4a") == 0.0) {
                        entity.getPersistentData().putDouble("Atk4a", 95.0);
                     } else {
                        entity.getPersistentData().putDouble("Atk4a", entity.getPersistentData().getDouble("Atk4a") - 1.0);
                     }

                     if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1, 6, false, false));
                     }

                     if (entity.getPersistentData().getDouble("Atk4a") == 95.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.WHITE_WOOL));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.WHITE_WOOL));
                        }

                        entity.getPersistentData().putBoolean("Attacking", true);
                        entity.getPersistentData().putBoolean("focus", true);
                        entity.getPersistentData().putDouble("wood_consumed", 0.0);
                     } else if (entity.getPersistentData().getDouble("Atk4a") == 75.0) {
                        entity.getPersistentData().putBoolean("focus", false);
                        entity.getPersistentData().putBoolean("lock", true);
                        entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                        entity.getPersistentData().putDouble("pitch", entity.getPersistentData().getDouble("pitch2"));
                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("entity.evoker.prepare_summon"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("entity.evoker.prepare_summon"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F,
                                 false
                              );
                           }
                        }
                     } else if (entity.getPersistentData().getDouble("Atk4a") <= 74.0 && entity.getPersistentData().getDouble("Atk4a") >= 20.0) {
                        for (int index3 = 0; index3 < 2; index3++) {
                           for (double var51 = -2.0; var51 <= 2.0; var51++) {
                              a = Mth.nextInt(net.minecraft.util.RandomSource.create(), -15, 15);
                              b = Mth.nextInt(net.minecraft.util.RandomSource.create(), -15, 15);
                              if (world.getBlockState(BlockPos.containing(x + a, y + var51, z + b)).is(BlockTags.create(new ResourceLocation("minecraft:logs")))
                                 && !entity.getPersistentData().getBoolean("tree_drain")) {
                                 entity.getPersistentData().putDouble("tree_x", x + a);
                                 entity.getPersistentData().putDouble("tree_y", y + var51);
                                 entity.getPersistentData().putDouble("tree_z", z + b);
                                 entity.getPersistentData().putBoolean("tree_drain", true);
                                 int horizontalRadiusSphere = 2;
                                 int verticalRadiusSphere = 2;
                                 int yIterationsSphere = verticalRadiusSphere;

                                 for (int i = -yIterationsSphere; i <= yIterationsSphere; i++) {
                                    for (int xi = -horizontalRadiusSphere; xi <= horizontalRadiusSphere; xi++) {
                                       for (int zi = -horizontalRadiusSphere; zi <= horizontalRadiusSphere; zi++) {
                                          double distanceSq = (double)(xi * xi) / (horizontalRadiusSphere * horizontalRadiusSphere)
                                             + (double)(i * i) / (verticalRadiusSphere * verticalRadiusSphere)
                                             + (double)(zi * zi) / (horizontalRadiusSphere * horizontalRadiusSphere);
                                          if (distanceSq <= 1.0 && world instanceof ServerLevel _level) {
                                             _level.sendParticles(
                                                ParticleTypes.SQUID_INK,
                                                x + xi + entity.getLookAngle().x * 2.0,
                                                y + i + 8.0,
                                                z + zi + entity.getLookAngle().z * 2.0,
                                                1,
                                                0.1,
                                                0.1,
                                                0.1,
                                                0.0
                                             );
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     } else if (entity.getPersistentData().getDouble("Atk4a") == 1.0) {
                        _center = new Vec3(x, y, z);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(50.0), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                              if (entity.getPersistentData().getDouble("wood_consumed") > 15.0) {
                                 if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
                                    _entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, 2));
                                 }

                                 entityiterator.hurt(new DamageSource(entityiterator.level().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(net.minecraft.world.damagesource.DamageTypes.MAGIC), entity), 6.0F);
                                 int horizontalRadiusSphere = 2;
                                 int verticalRadiusSphere = 2;
                                 int yIterationsSphere = verticalRadiusSphere;

                                 for (int i = -yIterationsSphere; i <= yIterationsSphere; i++) {
                                    for (int xi = -horizontalRadiusSphere; xi <= horizontalRadiusSphere; xi++) {
                                       for (int zi = -horizontalRadiusSphere; zi <= horizontalRadiusSphere; zi++) {
                                          double distanceSq = (double)(xi * xi) / (horizontalRadiusSphere * horizontalRadiusSphere)
                                             + (double)(i * i) / (verticalRadiusSphere * verticalRadiusSphere)
                                             + (double)(zi * zi) / (horizontalRadiusSphere * horizontalRadiusSphere);
                                          if (distanceSq <= 1.0 && world instanceof ServerLevel _level) {
                                             _level.sendParticles(
                                                ParticleTypes.SQUID_INK,
                                                entityiterator.getX() + xi,
                                                entityiterator.getY() + i + 1.5,
                                                entityiterator.getZ() + zi,
                                                1,
                                                0.1,
                                                0.1,
                                                0.1,
                                                0.0
                                             );
                                          }
                                       }
                                    }
                                 }
                              } else if (entity.getPersistentData().getDouble("wood_consumed") > 10.0) {
                                 if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
                                    _entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 160, 2));
                                 }

                                 int horizontalRadiusSphere = 1;
                                 int verticalRadiusSphere = 1;
                                 int yIterationsSphere = verticalRadiusSphere;

                                 for (int i = -yIterationsSphere; i <= yIterationsSphere; i++) {
                                    for (int xi = -horizontalRadiusSphere; xi <= horizontalRadiusSphere; xi++) {
                                       for (int zi = -horizontalRadiusSphere; zi <= horizontalRadiusSphere; zi++) {
                                          double distanceSq = (double)(xi * xi) / (horizontalRadiusSphere * horizontalRadiusSphere)
                                             + (double)(i * i) / (verticalRadiusSphere * verticalRadiusSphere)
                                             + (double)(zi * zi) / (horizontalRadiusSphere * horizontalRadiusSphere);
                                          if (distanceSq <= 1.0 && world instanceof ServerLevel _level) {
                                             _level.sendParticles(
                                                ParticleTypes.SQUID_INK,
                                                entityiterator.getX() + xi,
                                                entityiterator.getY() + i + 1.5,
                                                entityiterator.getZ() + zi,
                                                1,
                                                0.1,
                                                0.1,
                                                0.1,
                                                0.0
                                             );
                                          }
                                       }
                                    }
                                 }
                              } else if (!(entity.getPersistentData().getDouble("wood_consumed") > 5.0)) {
                                 if (entity.getPersistentData().getDouble("wood_consumed") > 1.0
                                    && entityiterator instanceof LivingEntity _entity
                                    && !_entity.level().isClientSide()) {
                                    _entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 160, 0));
                                 }
                              } else {
                                 int horizontalRadiusSphere = 0;
                                 int verticalRadiusSphere = 0;
                                 int yIterationsSphere = verticalRadiusSphere;

                                 for (int i = -yIterationsSphere; i <= yIterationsSphere; i++) {
                                    for (int xi = -horizontalRadiusSphere; xi <= horizontalRadiusSphere; xi++) {
                                       for (int zi = -horizontalRadiusSphere; zi <= horizontalRadiusSphere; zi++) {
                                          double distanceSq = (double)(xi * xi) / (horizontalRadiusSphere * horizontalRadiusSphere)
                                             + (double)(i * i) / (verticalRadiusSphere * verticalRadiusSphere)
                                             + (double)(zi * zi) / (horizontalRadiusSphere * horizontalRadiusSphere);
                                          if (distanceSq <= 1.0 && world instanceof ServerLevel _level) {
                                             _level.sendParticles(
                                                ParticleTypes.SQUID_INK,
                                                entityiterator.getX() + xi,
                                                entityiterator.getY() + i + 1.5,
                                                entityiterator.getZ() + zi,
                                                1,
                                                0.1,
                                                0.1,
                                                0.1,
                                                0.0
                                             );
                                          }
                                       }
                                    }
                                 }

                                 if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
                                    _entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 160, 1));
                                 }
                              }
                           }
                        }

                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                        }

                        entity.getPersistentData().putBoolean("lock", false);
                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putDouble("Atk4a", 0.0);
                        entity.getPersistentData().putBoolean("Attack4a", false);
                     }

                     if (entity.getPersistentData().getDouble("Atk4a") <= 74.0 && entity.getPersistentData().getDouble("Atk4a") >= 20.0) {
                        for (int index5 = 0; index5 < 2; index5++) {
                           for (double var48 = -2.0; var48 <= 2.0; var48++) {
                              a2 = Mth.nextInt(net.minecraft.util.RandomSource.create(), -15, 15);
                              b2 = Mth.nextInt(net.minecraft.util.RandomSource.create(), -15, 15);
                              if (world.getBlockState(BlockPos.containing(x + a2, y + var48, z + b2)).is(BlockTags.create(new ResourceLocation("minecraft:logs")))
                                 && !entity.getPersistentData().getBoolean("tree_drain2")
                                 && (entity.getPersistentData().getDouble("tree_x") != a2 || entity.getPersistentData().getDouble("tree_z") != b2)) {
                                 entity.getPersistentData().putDouble("tree_x2", x + a2);
                                 entity.getPersistentData().putDouble("tree_y2", y + var48);
                                 entity.getPersistentData().putDouble("tree_z2", z + b2);
                                 int horizontalRadiusSphere = 2;
                                 int verticalRadiusSphere = 2;
                                 int yIterationsSphere = verticalRadiusSphere;

                                 for (int i = -yIterationsSphere; i <= yIterationsSphere; i++) {
                                    for (int xi = -horizontalRadiusSphere; xi <= horizontalRadiusSphere; xi++) {
                                       for (int zi = -horizontalRadiusSphere; zi <= horizontalRadiusSphere; zi++) {
                                          double distanceSq = (double)(xi * xi) / (horizontalRadiusSphere * horizontalRadiusSphere)
                                             + (double)(i * i) / (verticalRadiusSphere * verticalRadiusSphere)
                                             + (double)(zi * zi) / (horizontalRadiusSphere * horizontalRadiusSphere);
                                          if (distanceSq <= 1.0 && world instanceof ServerLevel _level) {
                                             _level.sendParticles(
                                                ParticleTypes.SQUID_INK,
                                                x + xi + entity.getLookAngle().x * 2.0,
                                                y + i + 8.0,
                                                z + zi + entity.getLookAngle().z * 2.0,
                                                1,
                                                0.1,
                                                0.1,
                                                0.1,
                                                0.0
                                             );
                                          }
                                       }
                                    }
                                 }

                                 entity.getPersistentData().putBoolean("tree_drain2", true);
                              }
                           }
                        }
                     }

                     if (entity.getPersistentData().getBoolean("Stunned")) {
                        entity.getPersistentData().putBoolean("Attack4a", false);
                        entity.getPersistentData().putDouble("Atk4", 0.0);
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Attack4b")) {
                     if (entity.getPersistentData().getDouble("Atk4b") == 0.0) {
                        entity.getPersistentData().putDouble("Atk4b", 95.0);
                     } else {
                        entity.getPersistentData().putDouble("Atk4b", entity.getPersistentData().getDouble("Atk4b") - 1.0);
                     }

                     if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1, 6, false, false));
                     }

                     if (entity.getPersistentData().getDouble("Atk4b") == 95.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.WHITE_WOOL));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.WHITE_WOOL));
                        }

                        entity.getPersistentData().putBoolean("Attacking", true);
                        entity.getPersistentData().putBoolean("focus", true);
                        entity.getPersistentData().putDouble("wood_consumed", 0.0);
                     } else if (entity.getPersistentData().getDouble("Atk4b") == 75.0) {
                        entity.getPersistentData().putBoolean("focus", false);
                        entity.getPersistentData().putBoolean("lock", true);
                        entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                        entity.getPersistentData().putDouble("pitch", entity.getPersistentData().getDouble("pitch2"));
                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("entity.evoker.prepare_summon"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("entity.evoker.prepare_summon"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F,
                                 false
                              );
                           }
                        }
                     } else if (entity.getPersistentData().getDouble("Atk4b") <= 74.0 && entity.getPersistentData().getDouble("Atk4b") >= 20.0) {
                        for (int index7 = 0; index7 < 2; index7++) {
                           for (double var52 = -2.0; var52 <= 2.0; var52++) {
                              a = Mth.nextInt(net.minecraft.util.RandomSource.create(), -15, 15);
                              b = Mth.nextInt(net.minecraft.util.RandomSource.create(), -15, 15);
                              if (world.getBlockState(BlockPos.containing(x + a, y + var52, z + b)).is(BlockTags.create(new ResourceLocation("minecraft:logs")))
                                 && !entity.getPersistentData().getBoolean("tree_drain")) {
                                 entity.getPersistentData().putDouble("tree_x", x + a);
                                 entity.getPersistentData().putDouble("tree_y", y + var52);
                                 entity.getPersistentData().putDouble("tree_z", z + b);
                                 entity.getPersistentData().putBoolean("tree_drain", true);
                                 int horizontalRadiusSphere = 2;
                                 int verticalRadiusSphere = 2;
                                 int yIterationsSphere = verticalRadiusSphere;

                                 for (int i = -yIterationsSphere; i <= yIterationsSphere; i++) {
                                    for (int xi = -horizontalRadiusSphere; xi <= horizontalRadiusSphere; xi++) {
                                       for (int zi = -horizontalRadiusSphere; zi <= horizontalRadiusSphere; zi++) {
                                          double distanceSq = (double)(xi * xi) / (horizontalRadiusSphere * horizontalRadiusSphere)
                                             + (double)(i * i) / (verticalRadiusSphere * verticalRadiusSphere)
                                             + (double)(zi * zi) / (horizontalRadiusSphere * horizontalRadiusSphere);
                                          if (distanceSq <= 1.0 && world instanceof ServerLevel _level) {
                                             _level.sendParticles(
                                                ParticleTypes.INSTANT_EFFECT,
                                                x + xi + entity.getLookAngle().x * 2.0,
                                                y + i + 8.0,
                                                z + zi + entity.getLookAngle().z * 2.0,
                                                1,
                                                0.1,
                                                0.1,
                                                0.1,
                                                0.0
                                             );
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     } else if (entity.getPersistentData().getDouble("Atk4b") == 1.0) {
                        if (entity.getPersistentData().getDouble("wood_consumed") > 15.0) {
                           if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
                              _entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 80, 5));
                           }

                           int horizontalRadiusSphere = 4;
                           int verticalRadiusSphere = 4;
                           int yIterationsSphere = verticalRadiusSphere;

                           for (int i = -yIterationsSphere; i <= yIterationsSphere; i++) {
                              for (int xi = -horizontalRadiusSphere; xi <= horizontalRadiusSphere; xi++) {
                                 for (int zi = -horizontalRadiusSphere; zi <= horizontalRadiusSphere; zi++) {
                                    double distanceSq = (double)(xi * xi) / (horizontalRadiusSphere * horizontalRadiusSphere)
                                       + (double)(i * i) / (verticalRadiusSphere * verticalRadiusSphere)
                                       + (double)(zi * zi) / (horizontalRadiusSphere * horizontalRadiusSphere);
                                    if (distanceSq <= 1.0 && world instanceof ServerLevel _level) {
                                       _level.sendParticles(
                                          ParticleTypes.HEART,
                                          x + xi + entity.getLookAngle().x * 1.0,
                                          y + i + 4.0,
                                          z + zi + entity.getLookAngle().z * 1.0,
                                          1,
                                          0.1,
                                          0.1,
                                          0.1,
                                          0.0
                                       );
                                    }
                                 }
                              }
                           }
                        } else if (entity.getPersistentData().getDouble("wood_consumed") > 10.0) {
                           if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
                              _entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 80, 4));
                           }

                           int horizontalRadiusSphere = 3;
                           int verticalRadiusSphere = 3;
                           int yIterationsSphere = verticalRadiusSphere;

                           for (int i = -yIterationsSphere; i <= yIterationsSphere; i++) {
                              for (int xi = -horizontalRadiusSphere; xi <= horizontalRadiusSphere; xi++) {
                                 for (int zi = -horizontalRadiusSphere; zi <= horizontalRadiusSphere; zi++) {
                                    double distanceSq = (double)(xi * xi) / (horizontalRadiusSphere * horizontalRadiusSphere)
                                       + (double)(i * i) / (verticalRadiusSphere * verticalRadiusSphere)
                                       + (double)(zi * zi) / (horizontalRadiusSphere * horizontalRadiusSphere);
                                    if (distanceSq <= 1.0 && world instanceof ServerLevel _level) {
                                       _level.sendParticles(
                                          ParticleTypes.HEART,
                                          x + xi + entity.getLookAngle().x * 1.0,
                                          y + i + 4.0,
                                          z + zi + entity.getLookAngle().z * 1.0,
                                          1,
                                          0.1,
                                          0.1,
                                          0.1,
                                          0.0
                                       );
                                    }
                                 }
                              }
                           }
                        } else if (entity.getPersistentData().getDouble("wood_consumed") > 5.0) {
                           if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
                              _entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 80, 3));
                           }

                           int horizontalRadiusSphere = 2;
                           int verticalRadiusSphere = 2;
                           int yIterationsSphere = verticalRadiusSphere;

                           for (int i = -yIterationsSphere; i <= yIterationsSphere; i++) {
                              for (int xi = -horizontalRadiusSphere; xi <= horizontalRadiusSphere; xi++) {
                                 for (int zi = -horizontalRadiusSphere; zi <= horizontalRadiusSphere; zi++) {
                                    double distanceSq = (double)(xi * xi) / (horizontalRadiusSphere * horizontalRadiusSphere)
                                       + (double)(i * i) / (verticalRadiusSphere * verticalRadiusSphere)
                                       + (double)(zi * zi) / (horizontalRadiusSphere * horizontalRadiusSphere);
                                    if (distanceSq <= 1.0 && world instanceof ServerLevel _level) {
                                       _level.sendParticles(
                                          ParticleTypes.HEART,
                                          x + xi + entity.getLookAngle().x * 1.0,
                                          y + i + 4.0,
                                          z + zi + entity.getLookAngle().z * 1.0,
                                          1,
                                          0.1,
                                          0.1,
                                          0.1,
                                          0.0
                                       );
                                    }
                                 }
                              }
                           }
                        } else if (entity.getPersistentData().getDouble("wood_consumed") > 1.0) {
                           int horizontalRadiusSphere = 1;
                           int verticalRadiusSphere = 1;
                           int yIterationsSphere = verticalRadiusSphere;

                           for (int i = -yIterationsSphere; i <= yIterationsSphere; i++) {
                              for (int xi = -horizontalRadiusSphere; xi <= horizontalRadiusSphere; xi++) {
                                 for (int zi = -horizontalRadiusSphere; zi <= horizontalRadiusSphere; zi++) {
                                    double distanceSq = (double)(xi * xi) / (horizontalRadiusSphere * horizontalRadiusSphere)
                                       + (double)(i * i) / (verticalRadiusSphere * verticalRadiusSphere)
                                       + (double)(zi * zi) / (horizontalRadiusSphere * horizontalRadiusSphere);
                                    if (distanceSq <= 1.0 && world instanceof ServerLevel _level) {
                                       _level.sendParticles(
                                          ParticleTypes.HEART,
                                          x + xi + entity.getLookAngle().x * 1.0,
                                          y + i + 4.0,
                                          z + zi + entity.getLookAngle().z * 1.0,
                                          1,
                                          0.1,
                                          0.1,
                                          0.1,
                                          0.0
                                       );
                                    }
                                 }
                              }
                           }

                           if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
                              _entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 80, 2));
                           }
                        }

                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                        }

                        entity.getPersistentData().putBoolean("lock", false);
                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putDouble("Atk4b", 0.0);
                        entity.getPersistentData().putBoolean("Attack4b", false);
                     }

                     if (entity.getPersistentData().getDouble("Atk4b") <= 74.0 && entity.getPersistentData().getDouble("Atk4b") >= 20.0) {
                        for (int index9 = 0; index9 < 2; index9++) {
                           for (double var49 = -2.0; var49 <= 2.0; var49++) {
                              a2 = Mth.nextInt(net.minecraft.util.RandomSource.create(), -15, 15);
                              b2 = Mth.nextInt(net.minecraft.util.RandomSource.create(), -15, 15);
                              if (world.getBlockState(BlockPos.containing(x + a2, y + var49, z + b2)).is(BlockTags.create(new ResourceLocation("minecraft:logs")))
                                 && !entity.getPersistentData().getBoolean("tree_drain2")
                                 && (entity.getPersistentData().getDouble("tree_x") != a2 || entity.getPersistentData().getDouble("tree_z") != b2)) {
                                 entity.getPersistentData().putDouble("tree_x2", x + a2);
                                 entity.getPersistentData().putDouble("tree_y2", y + var49);
                                 entity.getPersistentData().putDouble("tree_z2", z + b2);
                                 int horizontalRadiusSphere = 2;
                                 int verticalRadiusSphere = 2;
                                 int yIterationsSphere = verticalRadiusSphere;

                                 for (int i = -yIterationsSphere; i <= yIterationsSphere; i++) {
                                    for (int xi = -horizontalRadiusSphere; xi <= horizontalRadiusSphere; xi++) {
                                       for (int zi = -horizontalRadiusSphere; zi <= horizontalRadiusSphere; zi++) {
                                          double distanceSq = (double)(xi * xi) / (horizontalRadiusSphere * horizontalRadiusSphere)
                                             + (double)(i * i) / (verticalRadiusSphere * verticalRadiusSphere)
                                             + (double)(zi * zi) / (horizontalRadiusSphere * horizontalRadiusSphere);
                                          if (distanceSq <= 1.0 && world instanceof ServerLevel _level) {
                                             _level.sendParticles(
                                                ParticleTypes.INSTANT_EFFECT,
                                                x + xi + entity.getLookAngle().x * 2.0,
                                                y + i + 8.0,
                                                z + zi + entity.getLookAngle().z * 2.0,
                                                1,
                                                0.1,
                                                0.1,
                                                0.1,
                                                0.0
                                             );
                                          }
                                       }
                                    }
                                 }

                                 entity.getPersistentData().putBoolean("tree_drain2", true);
                              }
                           }
                        }
                     }

                     if (entity.getPersistentData().getBoolean("Stunned")) {
                        entity.getPersistentData().putBoolean("Attack4b", false);
                        entity.getPersistentData().putDouble("Atk4b", 0.0);
                     }
                  }

                  ProtreechopProcedure.execute(world, x, y, z, entity);
               } else {
                  if (entity.getPersistentData().getDouble("acheck") == 0.0) {
                     entity.getPersistentData().putBoolean("lock", true);
                     entity.getPersistentData().putBoolean("noai", true);
                     entity.getPersistentData().putDouble("yaw", entity.getYRot());
                     entity.getPersistentData().putDouble("acheck", 20.0);
                  } else {
                     entity.getPersistentData().putDouble("acheck", entity.getPersistentData().getDouble("acheck") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("acheck") == 1.0) {
                     if (!(entity.getPersistentData().getDouble("Poise") > 0.0)
                        && world.getEntitiesOfClass(ServerPlayer.class, AABB.ofSize(new Vec3(x, y, z), 4.0, 4.0, 4.0), e -> true).isEmpty()
                        && world.getEntitiesOfClass(Player.class, AABB.ofSize(new Vec3(x, y, z), 4.0, 4.0, 4.0), e -> true).isEmpty()) {
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
                        entity.getPersistentData().putBoolean("Waking", true);
                        entity.getPersistentData().putBoolean("Wake", true);
                     }
                  }
               }
            } else {
               ProentdeathProcedure.execute(world, x, y, z, entity);
            }
         }
      }
   }
}
