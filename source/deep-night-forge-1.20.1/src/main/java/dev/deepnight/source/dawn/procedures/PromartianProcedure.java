package dev.deepnight.source.dawn.procedures;


import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.CubeEntity;
import dev.deepnight.source.dawn.entity.ElementalSpawnEntity;
import dev.deepnight.source.dawn.entity.GemspikeEntity;
import dev.deepnight.source.dawn.entity.GroundbreakEffectSmallEntity;
import dev.deepnight.source.dawn.entity.MartianEntity;
import dev.deepnight.source.dawn.entity.RoarEntity;
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
import net.minecraftforge.registries.ForgeRegistries;
import dev.deepnight.source.SourceDamage;
import dev.deepnight.source.SourceDamage.StunType;

public class PromartianProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         double i = 0.0;
         double j = 0.0;
         double c = 0.0;
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

            if (entity.getPersistentData().getDouble("Poise") > 380.0 && !entity.getPersistentData().getBoolean("Stunned")) {
               entity.getPersistentData().putBoolean("Stunned", true);
            }

            if (!entity.getPersistentData().getBoolean("Aggro") && !entity.getPersistentData().getBoolean("weirdwalk")) {
               if (entity.getPersistentData().getDouble("agg") == 0.0) {
                  entity.getPersistentData().putDouble("agg", 200.0);
                  if (Math.random() > 0.6) {
                     entity.getPersistentData().putBoolean("weirdwalk", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(2, new ItemStack(Items.LEATHER_CHESTPLATE));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
                     }
                  }
               } else {
                  entity.getPersistentData().putDouble("agg", entity.getPersistentData().getDouble("agg") - 1.0);
               }
            }

            if (entity.getPersistentData().getBoolean("weirdwalk")) {
               if (entity.getPersistentData().getBoolean("Aggro")) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(2, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("weirdwalk", false);
               }

               if (entity.getPersistentData().getDouble("return") == 0.0) {
                  entity.getPersistentData().putDouble("return", 120.0);
                  if (Math.random() > 0.75) {
                     entity.getPersistentData().putBoolean("weirdwalk", false);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(2, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Blocks.AIR));
                     }
                  }
               } else {
                  entity.getPersistentData().putDouble("return", entity.getPersistentData().getDouble("return") - 1.0);
               }
            }

            if (entity.getPersistentData().getBoolean("sweep")) {
               entity.getPersistentData().putDouble("direction", Math.abs(entity.getYRot() / 360.0F) * 12.0F);
               if (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop") > 12.0) {
                  entity.getPersistentData().putDouble("direction", entity.getPersistentData().getDouble("direction") - 12.0);
               } else if (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop") < 0.0) {
                  entity.getPersistentData().putDouble("direction", entity.getPersistentData().getDouble("direction") + 12.0);
               }

               if (!(entity.getPersistentData().getDouble("loop") < 5.0)) {
                  entity.getPersistentData().putDouble("loop", 0.0);
                  entity.getPersistentData().putBoolean("sweep", false);
               } else {
                  Vec3 _center = new Vec3(
                     x
                        + entity.getLookAngle().x * 2.0
                        + Math.cos((Math.PI / 6) * (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop"))) * 3.5,
                     y + 0.5,
                     z
                        + entity.getLookAngle().z * 2.0
                        + Math.sin((Math.PI / 6) * (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop"))) * 3.5
                  );

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof MartianEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.SHORT)
                           .setImpact(4.0F)
                           .setArmorNegation(1.0F);
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
                                       + entity.getLookAngle().x * 2.0
                                       + Math.cos(
                                             (Math.PI / 6) * (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop"))
                                          )
                                          * 3.5,
                                    y + entity.getPersistentData().getDouble("loop") * 0.4,
                                    z
                                       + entity.getLookAngle().z * 2.0
                                       + Math.sin(
                                             (Math.PI / 6) * (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop"))
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
                           "/particle minecraft:portal ~ ~ ~ .25 .25 .25 0 100"
                        );
                  }

                  entity.getPersistentData().putDouble("loop", entity.getPersistentData().getDouble("loop") + 1.0);
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
                  Vec3 _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if ((entityiterator instanceof Player || entityiterator instanceof ServerPlayer) && entityiterator instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 10, 1, false, false));
                     }
                  }

                  _center = new Vec3(
                     x
                        + entity.getLookAngle().x * 1.3
                        + Math.cos((Math.PI / 6) * (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop"))) * 3.5,
                     y + 0.5,
                     z
                        + entity.getLookAngle().z * 1.3
                        + Math.sin((Math.PI / 6) * (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop"))) * 3.5
                  );

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof MartianEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(10.0F)
                           .setArmorNegation(1.5F);
                        entityiterator.hurt((DamageSource)dmgSrc, 24.0F);
                        entityiterator.setDeltaMovement(new Vec3(entityiterator.getX() - entity.getX(), 0.3, entityiterator.getZ() - entity.getZ()));
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
                                       + entity.getLookAngle().x * 1.3
                                       + Math.cos(
                                             (Math.PI / 6) * (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop"))
                                          )
                                          * 3.5,
                                    y + 0.5,
                                    z
                                       + entity.getLookAngle().z * 1.3
                                       + Math.sin(
                                             (Math.PI / 6) * (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop"))
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
                           "/particle minecraft:portal ~ ~ ~ .8 .8 .8 0 100"
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

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(15.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                  entity.getPersistentData().putBoolean("Aggro", true);
                  entity.getPersistentData().putBoolean("Far", true);
                  entity.getPersistentData().putBoolean("Mid", false);
                  entity.getPersistentData().putBoolean("Near", false);
                  entity.getPersistentData().putBoolean("Close", false);
               }
            }

            _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(8.0), e -> true)
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

            if (entity.getPersistentData().getBoolean("Far") && !entity.getPersistentData().getBoolean("Attacking")) {
               if (Math.random() >= 0.67 && entity.getPersistentData().getDouble("Atk7CD") == 0.0) {
                  entity.getPersistentData().putBoolean("Attack7", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (Math.random() > 0.33 && Math.random() <= 0.66 && entity.getPersistentData().getDouble("Atk8CD") == 0.0) {
                  entity.getPersistentData().putBoolean("Attack8", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (Math.random() <= 0.33 && entity.getPersistentData().getDouble("TPCD") == 0.0) {
                  entity.getPersistentData().putBoolean("Teleport", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               }
            }

            if (entity.getPersistentData().getBoolean("Mid")
               && !entity.getPersistentData().getBoolean("Attacking")
               && Math.random() >= 0.5
               && entity.getPersistentData().getDouble("RoarCD") == 0.0) {
               entity.getPersistentData().putBoolean("Roar", true);
               entity.getPersistentData().putBoolean("Attacking", true);
            }

            if (entity.getPersistentData().getBoolean("Near")
               && !entity.getPersistentData().getBoolean("Attacking")
               && Math.random() >= 0.5
               && entity.getPersistentData().getDouble("Atk5CD") == 0.0) {
               entity.getPersistentData().putBoolean("Attack5", true);
               entity.getPersistentData().putBoolean("Attacking", true);
            }

            if (entity.getPersistentData().getBoolean("Close") && !entity.getPersistentData().getBoolean("Attacking")) {
               if (Math.random() >= 0.8) {
                  entity.getPersistentData().putBoolean("Attack1", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (Math.random() > 0.6 && Math.random() <= 0.79 && entity.getPersistentData().getDouble("Atk2CD") == 0.0) {
                  entity.getPersistentData().putBoolean("Attack2", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (Math.random() > 0.4 && Math.random() <= 0.59) {
                  entity.getPersistentData().putBoolean("Attack3", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (Math.random() > 0.2 && Math.random() <= 0.39 && entity.getPersistentData().getDouble("Atk4CD") == 0.0) {
                  entity.getPersistentData().putBoolean("Attack4", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (Math.random() <= 0.19) {
                  entity.getPersistentData().putBoolean("Attack6", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               }
            }

            if (entity.getPersistentData().getDouble("Atk2CD") == 0.0 && entity.getPersistentData().getBoolean("Attack2")) {
               entity.getPersistentData().putDouble("Atk2CD", 300.0);
            } else if (entity.getPersistentData().getDouble("Atk2CD") > 0.0) {
               entity.getPersistentData().putDouble("Atk2CD", entity.getPersistentData().getDouble("Atk2CD") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Atk4CD") == 0.0 && entity.getPersistentData().getBoolean("Attack4")) {
               entity.getPersistentData().putDouble("Atk4CD", 260.0);
            } else if (entity.getPersistentData().getDouble("Atk4CD") > 0.0) {
               entity.getPersistentData().putDouble("Atk4CD", entity.getPersistentData().getDouble("Atk4CD") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Atk5CD") == 0.0 && entity.getPersistentData().getBoolean("Attack5")) {
               entity.getPersistentData().putDouble("Atk5CD", 180.0);
            } else if (entity.getPersistentData().getDouble("Atk5CD") > 0.0) {
               entity.getPersistentData().putDouble("Atk5CD", entity.getPersistentData().getDouble("Atk5CD") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Atk7CD") == 0.0 && entity.getPersistentData().getBoolean("Attack7")) {
               entity.getPersistentData().putDouble("Atk7CD", 200.0);
            } else if (entity.getPersistentData().getDouble("Atk7CD") > 0.0) {
               entity.getPersistentData().putDouble("Atk7CD", entity.getPersistentData().getDouble("Atk7CD") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Atk8CD") == 0.0 && entity.getPersistentData().getBoolean("Attack8")) {
               entity.getPersistentData().putDouble("Atk8CD", 200.0);
            } else if (entity.getPersistentData().getDouble("Atk8CD") > 0.0) {
               entity.getPersistentData().putDouble("Atk8CD", entity.getPersistentData().getDouble("Atk8CD") - 1.0);
            }

            if (entity.getPersistentData().getDouble("RoarCD") == 0.0 && entity.getPersistentData().getBoolean("Roar")) {
               entity.getPersistentData().putDouble("RoarCD", 400.0);
            } else if (entity.getPersistentData().getDouble("RoarCD") > 0.0) {
               entity.getPersistentData().putDouble("RoarCD", entity.getPersistentData().getDouble("RoarCD") - 1.0);
            }

            if (entity.getPersistentData().getDouble("TPCD") == 0.0 && entity.getPersistentData().getBoolean("Teleport")) {
               entity.getPersistentData().putDouble("TPCD", 300.0);
            } else if (entity.getPersistentData().getDouble("TPCD") > 0.0) {
               entity.getPersistentData().putDouble("TPCD", entity.getPersistentData().getDouble("TPCD") - 1.0);
            }

            if (entity.getPersistentData().getBoolean("Attack1")) {
               if (entity.getPersistentData().getDouble("Atk1") == 0.0) {
                  entity.getPersistentData().putDouble("Atk1", 62.0);
               } else {
                  entity.getPersistentData().putDouble("Atk1", entity.getPersistentData().getDouble("Atk1") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk1") == 62.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.LEATHER_HELMET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 61, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk1") == 44.0) {
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

                  entity.getPersistentData().putBoolean("sweep", true);
               } else if (entity.getPersistentData().getDouble("Atk1") == 30.0) {
                  if (Math.random() > 0.4) {
                     _center = new Vec3(x + entity.getLookAngle().x * 2.5, y, z + entity.getLookAngle().z * 2.5);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                           entity.getPersistentData().putBoolean("Attack1b", true);
                           entity.getPersistentData().putDouble("Atk1", 0.0);
                           entity.getPersistentData().putBoolean("Attack1", false);
                        }
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL,
                                    new Vec3(x + entity.getLookAngle().x * 2.5, y, z + entity.getLookAngle().z * 2.5),
                                    Vec2.ZERO,
                                    _level,
                                    4,
                                    "",
                                    Component.literal(""),
                                    _level.getServer(),
                                    null
                                 )
                                 .withSuppressedOutput(),
                              "/particle minecraft:portal ~ ~3 ~ 2 2 2 0 100"
                           );
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

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk1", 0.0);
                  entity.getPersistentData().putBoolean("Attack1", false);
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  entity.getPersistentData().putBoolean("Attack1", false);
                  entity.getPersistentData().putDouble("Atk1", 0.0);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack1b")) {
               if (entity.getPersistentData().getDouble("Atk1b") == 0.0) {
                  entity.getPersistentData().putDouble("Atk1b", 31.0);
               } else {
                  entity.getPersistentData().putDouble("Atk1b", entity.getPersistentData().getDouble("Atk1b") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk1b") == 31.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.GOLD_NUGGET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLD_NUGGET));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 31, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk1b") == 27.0) {
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

                  _center = new Vec3(x + entity.getLookAngle().x * 3.0, y, z + entity.getLookAngle().z * 3.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof MartianEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(14.0F)
                           .setArmorNegation(1.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 26.0F);
                        entityiterator.setDeltaMovement(new Vec3(0.0, 0.8, 0.0));
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

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk1b", 0.0);
                  entity.getPersistentData().putBoolean("Attack1b", false);
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  entity.getPersistentData().putBoolean("Attack1b", false);
                  entity.getPersistentData().putDouble("Atk1b", 0.0);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack2")) {
               if (entity.getPersistentData().getDouble("Atk2") == 0.0) {
                  entity.getPersistentData().putDouble("Atk2", 101.0);
               } else {
                  entity.getPersistentData().putDouble("Atk2", entity.getPersistentData().getDouble("Atk2") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk2") == 101.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.COAL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.COAL));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 101, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk2") == 83.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_martian,limit=1,sort=nearest] {NoAI:1b}"
                        );
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:martian.attack2"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:martian.attack2"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F,
                           false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk2") == 79.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 3.0, y, z + entity.getLookAngle().z * 3.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof MartianEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.SHORT)
                           .setImpact(7.0F)
                           .setArmorNegation(1.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 18.0F);
                     }
                  }

                  entity.getPersistentData().putDouble("yaw", entity.getYRot());
                  entity.getPersistentData().putDouble("pitch", entity.getXRot());
               } else if (entity.getPersistentData().getDouble("Atk2") == 78.0) {
                  entity.getPersistentData().putBoolean("lock", true);
               } else if (entity.getPersistentData().getDouble("Atk2") == 65.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 3.0, y, z + entity.getLookAngle().z * 3.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof MartianEntity)) {
                        entityiterator.hurt(new DamageSource(entityiterator.level().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(net.minecraft.world.damagesource.DamageTypes.GENERIC), entity), 1.0F);
                        entityiterator.setDeltaMovement(
                           new Vec3(x + entity.getLookAngle().x * 3.0 - entityiterator.getX(), 0.0, z + entity.getLookAngle().z * 3.0 - entityiterator.getZ())
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk2") == 48.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           -1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           -1.0F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.FLASH, x + entity.getLookAngle().x * 3.0, y, z + entity.getLookAngle().z * 3.0, 15, 1.0, 1.0, 1.0, 0.2);
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.EXPLOSION, x + entity.getLookAngle().x * 3.0, y, z + entity.getLookAngle().z * 3.0, 15, 0.125, 0.125, 0.125, 0.2
                     );
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.SMOKE, x + entity.getLookAngle().x * 3.0, y, z + entity.getLookAngle().z * 3.0, 15, 0.125, 0.125, 0.125, 0.2
                     );
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new ElementalSpawnEntity(SimpleMobsModEntities.ELEMENTAL_SPAWN.get(), _level);
                     entityToSpawn.moveTo(
                        entity.getX() + entity.getLookAngle().x * 3.0, y, entity.getZ() + entity.getLookAngle().z * 3.0, entity.getYRot(), 0.0F
                     );
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  _center = new Vec3(x + entity.getLookAngle().x * 3.0, y, z + entity.getLookAngle().z * 3.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof MartianEntity) && !(entityiterator instanceof ElementalSpawnEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(15.0F)
                           .setArmorNegation(3.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 30.0F);
                        entityiterator.setDeltaMovement(
                           new Vec3(
                              (entityiterator.getX() - (x + entity.getLookAngle().x * 3.0)) / 5.0,
                              0.7,
                              (entityiterator.getZ() - (z + entity.getLookAngle().z * 3.0)) / 5.0
                           )
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

                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_martian,limit=1,sort=nearest] {NoAI:0}"
                        );
                  }

                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk2", 0.0);
                  entity.getPersistentData().putBoolean("Attack2", false);
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  entity.getPersistentData().putBoolean("Attack2", false);
                  entity.getPersistentData().putDouble("Atk2", 0.0);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack3")) {
               if (entity.getPersistentData().getDouble("Atk3") == 0.0) {
                  entity.getPersistentData().putDouble("Atk3", 70.0);
               } else {
                  entity.getPersistentData().putDouble("Atk3", entity.getPersistentData().getDouble("Atk3") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk3") == 70.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.CHARCOAL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHARCOAL));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 70, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk3") == 57.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_martian,limit=1,sort=nearest] {NoAI:1b}"
                        );
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x + entity.getLookAngle().x * 3.0, y, z + entity.getLookAngle().z * 3.0),
                           dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x + entity.getLookAngle().x * 3.0,
                           y,
                           z + entity.getLookAngle().z * 3.0,
                           dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F,
                           false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk3") == 56.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 3.0, y, z + entity.getLookAngle().z * 3.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof MartianEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.SHORT)
                           .setImpact(6.0F)
                           .setArmorNegation(1.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 19.0F);
                     }
                  }

                  entity.getPersistentData().putDouble("yaw", entity.getYRot());
                  entity.getPersistentData().putDouble("pitch", entity.getXRot());
               } else if (entity.getPersistentData().getDouble("Atk3") == 55.0) {
                  entity.getPersistentData().putBoolean("lock", true);
               } else if (entity.getPersistentData().getDouble("Atk3") == 44.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.zombie.break_wooden_door"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           -1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.zombie.break_wooden_door"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           -1.0F,
                           false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk3") == 42.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 3.0, y, z + entity.getLookAngle().z * 3.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof MartianEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.KNOCKDOWN)
                           .setImpact(14.0F)
                           .setArmorNegation(1.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 24.0F);
                        entityiterator.setDeltaMovement(
                           new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 30.0F)) * 2.0, 0.6, Math.cos(Math.toRadians(entity.getYRot() + 200.0F)) * 2.0)
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL,
                                 new Vec3(x + entity.getLookAngle().x * -2.0, y, z + entity.getLookAngle().z * -2.0),
                                 Vec2.ZERO,
                                 _level,
                                 4,
                                 "",
                                 Component.literal(""),
                                 _level.getServer(),
                                 null
                              )
                              .withSuppressedOutput(),
                           "/particle minecraft:portal ~ ~ ~ .4 .4 .4 0 100"
                        );
                  }
               } else if (entity.getPersistentData().getDouble("Atk3") == 1.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_martian,limit=1,sort=nearest] {NoAI:0}"
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
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk3", 0.0);
                  entity.getPersistentData().putBoolean("Attack3", false);
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  entity.getPersistentData().putBoolean("Attack3", false);
                  entity.getPersistentData().putDouble("Atk3", 0.0);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack4")) {
               if (entity.getPersistentData().getDouble("Atk4") == 0.0) {
                  entity.getPersistentData().putDouble("Atk4", 102.0);
               } else {
                  entity.getPersistentData().putDouble("Atk4", entity.getPersistentData().getDouble("Atk4") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk4") == 102.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.IRON_INGOT));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_INGOT));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (entity.getPersistentData().getDouble("Atk4") == 53.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_martian,limit=1,sort=nearest] {NoAI:1b}"
                        );
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new GroundbreakEffectSmallEntity(SimpleMobsModEntities.GROUNDBREAK_EFFECT_SMALL.get(), _level);
                     entityToSpawn.moveTo(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0, world.getRandom().nextFloat() * 360.0F, 0.0F);
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  _center = new Vec3(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(6.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if ((entityiterator instanceof Player || entityiterator instanceof ServerPlayer) && entityiterator instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 10, 1, false, false));
                     }
                  }

                  _center = new Vec3(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof MartianEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.KNOCKDOWN)
                           .setImpact(20.0F)
                           .setArmorNegation(1.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 30.0F);
                        if (entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 3, false, false));
                        }
                     }
                  }

                  if (world instanceof ServerLevel _level) {
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
                           "/particle minecraft:campfire_cosy_smoke ~ ~ ~ 3 0 3 1 30"
                        );
                  }

                  if (world instanceof ServerLevel _level) {
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
                           "/particle minecraft:portal ~ ~ ~ 3 0 3 1 500"
                        );
                  }

                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 53, 6, false, false));
                  }

                  entity.getPersistentData().putDouble("yaw", entity.getYRot());
                  entity.getPersistentData().putDouble("pitch", entity.getXRot());
               } else if (entity.getPersistentData().getDouble("Atk4") == 51.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:martian.attack4"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:martian.attack4"),
                           SoundSource.NEUTRAL,
                           1.0F,
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
                           dev.deepnight.SafeSounds.resolve("entity.dragon_fireball.explode"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           -1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.dragon_fireball.explode"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           -1.0F,
                           false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk4") == 55.0) {
                  entity.getPersistentData().putBoolean("lock", true);
               } else if (entity.getPersistentData().getDouble("Atk4") == 1.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_martian,limit=1,sort=nearest] {NoAI:0}"
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
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk4", 0.0);
                  entity.getPersistentData().putBoolean("Attack4", false);
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  entity.getPersistentData().putBoolean("Attack4", false);
                  entity.getPersistentData().putDouble("Atk4", 0.0);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack5")) {
               if (entity.getPersistentData().getDouble("Atk5") == 0.0) {
                  entity.getPersistentData().putDouble("Atk5", 61.0);
               } else {
                  entity.getPersistentData().putDouble("Atk5", entity.getPersistentData().getDouble("Atk5") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk5") >= 32.0 && entity.getPersistentData().getDouble("Atk5") <= 43.0) {
                  entity.setDeltaMovement(new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)), 0.0, Math.cos(Math.toRadians(entity.getYRot()))));
               }

               if (entity.getPersistentData().getDouble("Atk5") == 61.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.IRON_NUGGET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_NUGGET));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 18, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk5") == 43.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 9, 5, false, false));
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.polar_bear.warning"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           -2.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.polar_bear.warning"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           -2.0F,
                           false
                        );
                     }
                  }

                  entity.getPersistentData().putBoolean("spin", true);
               } else if (entity.getPersistentData().getDouble("Atk5") == 35.0) {
                  entity.getPersistentData().putDouble("yaw", entity.getYRot());
                  entity.getPersistentData().putDouble("pitch", entity.getXRot());
               } else if (entity.getPersistentData().getDouble("Atk5") == 34.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 33, 6, false, false));
                  }

                  entity.getPersistentData().putBoolean("lock", true);
               } else if (entity.getPersistentData().getDouble("Atk5") == 2.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.WHITE_WOOL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.WHITE_WOOL));
                  }
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
                  entity.getPersistentData().putDouble("Atk6", 42.0);
               } else {
                  entity.getPersistentData().putDouble("Atk6", entity.getPersistentData().getDouble("Atk6") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk6") == 42.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.RED_WOOL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.RED_WOOL));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 42, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk6") == 31.0) {
                  entity.getPersistentData().putDouble("yaw", entity.getYRot());
                  entity.getPersistentData().putDouble("pitch", entity.getXRot());
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:martian.step"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:martian.step"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F,
                           false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk6") == 30.0) {
                  entity.getPersistentData().putBoolean("lock", true);
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_martian,limit=1,sort=nearest] {NoAI:1b}"
                        );
                  }

                  _center = new Vec3(x + entity.getLookAngle().x * 1.0, y, z + entity.getLookAngle().z * 1.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if ((entityiterator instanceof Player || entityiterator instanceof ServerPlayer) && entityiterator instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 10, 1, false, false));
                     }
                  }

                  _center = new Vec3(x + entity.getLookAngle().x * 1.0, y, z + entity.getLookAngle().z * 1.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof MartianEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(15.0F)
                           .setArmorNegation(1.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 18.0F);
                        if (entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 3, false, false));
                        }
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL,
                                 new Vec3(x + entity.getLookAngle().x * 1.0, y, z + entity.getLookAngle().z * 1.0),
                                 Vec2.ZERO,
                                 _level,
                                 4,
                                 "",
                                 Component.literal(""),
                                 _level.getServer(),
                                 null
                              )
                              .withSuppressedOutput(),
                           "/particle minecraft:campfire_cosy_smoke ~ ~ ~ 1 0 1 1 10"
                        );
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL,
                                 new Vec3(x + entity.getLookAngle().x * 1.0, y, z + entity.getLookAngle().z * 1.0),
                                 Vec2.ZERO,
                                 _level,
                                 4,
                                 "",
                                 Component.literal(""),
                                 _level.getServer(),
                                 null
                              )
                              .withSuppressedOutput(),
                           "/particle minecraft:portal ~ ~ ~ 1 0 1 0 40"
                        );
                  }
               } else if (entity.getPersistentData().getDouble("Atk6") == 1.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_martian,limit=1,sort=nearest] {NoAI:0}"
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
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk6", 0.0);
                  entity.getPersistentData().putBoolean("Attack6", false);
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  entity.getPersistentData().putBoolean("Attack6", false);
                  entity.getPersistentData().putDouble("Atk6", 0.0);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack7")) {
               if (entity.getPersistentData().getDouble("Atk7") == 0.0) {
                  entity.getPersistentData().putDouble("Atk7", 57.0);
               } else {
                  entity.getPersistentData().putDouble("Atk7", entity.getPersistentData().getDouble("Atk7") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk7") == 57.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.YELLOW_WOOL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.YELLOW_WOOL));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 57, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk7") == 38.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ogre_step"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ogre_step"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  entity.getPersistentData().putDouble("yaw", entity.getYRot());
                  entity.getPersistentData().putDouble("pitch", entity.getXRot());
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL,
                                 new Vec3(x + entity.getLookAngle().x * 1.0, y, z + entity.getLookAngle().z * 1.0),
                                 Vec2.ZERO,
                                 _level,
                                 4,
                                 "",
                                 Component.literal(""),
                                 _level.getServer(),
                                 null
                              )
                              .withSuppressedOutput(),
                           "/particle minecraft:portal ~ ~ ~ 0.4 0 0.4 0 40"
                        );
                  }
               } else if (entity.getPersistentData().getDouble("Atk7") == 35.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(25.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ()),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:martian.gem"),
                                 SoundSource.NEUTRAL,
                                 1.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 entityiterator.getX(),
                                 entityiterator.getY(),
                                 entityiterator.getZ(),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:martian.gem"),
                                 SoundSource.NEUTRAL,
                                 1.0F,
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
                                       new Vec3(x - (entity.getX() - entityiterator.getX()) * 0.2, y, z - (entity.getZ() - entityiterator.getZ()) * 0.2),
                                       Vec2.ZERO,
                                       _level,
                                       4,
                                       "",
                                       Component.literal(""),
                                       _level.getServer(),
                                       null
                                    )
                                    .withSuppressedOutput(),
                                 "/particle minecraft:dragon_breath ~ ~ ~ 0.3 0.8 0.3 0 40"
                              );
                        }
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk7") == 32.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(25.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null) && world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL,
                                    new Vec3(x - (entity.getX() - entityiterator.getX()) * 0.4, y, z - (entity.getZ() - entityiterator.getZ()) * 0.4),
                                    Vec2.ZERO,
                                    _level,
                                    4,
                                    "",
                                    Component.literal(""),
                                    _level.getServer(),
                                    null
                                 )
                                 .withSuppressedOutput(),
                              "/particle minecraft:dragon_breath ~ ~ ~ 0.3 0.8 0.3 0 40"
                           );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk7") == 29.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(25.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null) && world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL,
                                    new Vec3(x - (entity.getX() - entityiterator.getX()) * 0.6, y, z - (entity.getZ() - entityiterator.getZ()) * 0.6),
                                    Vec2.ZERO,
                                    _level,
                                    4,
                                    "",
                                    Component.literal(""),
                                    _level.getServer(),
                                    null
                                 )
                                 .withSuppressedOutput(),
                              "/particle minecraft:dragon_breath ~ ~ ~ 0.3 0.8 0.3 0 40"
                           );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk7") == 25.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(25.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null) && world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL,
                                    new Vec3(
                                       x - (entity.getX() - entityiterator.getX()) * 0.8,
                                       entityiterator.getY(),
                                       z - (entity.getZ() - entityiterator.getZ()) * 0.8
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
                              "/particle minecraft:dragon_breath ~ ~ ~ 0.3 0.8 0.3 0 40"
                           );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk7") == 22.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(25.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ()),
                                 dev.deepnight.SafeSounds.resolve("block.amethyst_block.break"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 -2.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 entityiterator.getX(),
                                 entityiterator.getY(),
                                 entityiterator.getZ(),
                                 dev.deepnight.SafeSounds.resolve("block.amethyst_block.break"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 -2.0F,
                                 false
                              );
                           }
                        }

                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ()),
                                 dev.deepnight.SafeSounds.resolve("block.amethyst_block.break"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 -1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 entityiterator.getX(),
                                 entityiterator.getY(),
                                 entityiterator.getZ(),
                                 dev.deepnight.SafeSounds.resolve("block.amethyst_block.break"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 -1.0F,
                                 false
                              );
                           }
                        }

                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ()),
                                 dev.deepnight.SafeSounds.resolve("block.amethyst_block.place"),
                                 SoundSource.NEUTRAL,
                                 1.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 entityiterator.getX(),
                                 entityiterator.getY(),
                                 entityiterator.getZ(),
                                 dev.deepnight.SafeSounds.resolve("block.amethyst_block.place"),
                                 SoundSource.NEUTRAL,
                                 1.0F,
                                 1.0F,
                                 false
                              );
                           }
                        }

                        if (world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new GemspikeEntity(SimpleMobsModEntities.GEMSPIKE.get(), _level);
                           entityToSpawn.moveTo(
                              entityiterator.getX(),
                              entityiterator.getY(),
                              entityiterator.getZ(),
                              (float)entity.getPersistentData().getDouble("yaw"),
                              (float)entity.getPersistentData().getDouble("pitch")
                           );
                           entityToSpawn.setYBodyRot((float)entity.getPersistentData().getDouble("yaw"));
                           entityToSpawn.setYHeadRot((float)entity.getPersistentData().getDouble("yaw"));
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk7") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk7", 0.0);
                  entity.getPersistentData().putBoolean("Attack7", false);
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  entity.getPersistentData().putBoolean("Attack7", false);
                  entity.getPersistentData().putDouble("Atk7", 0.0);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack8")) {
               if (entity.getPersistentData().getDouble("Atk8") == 0.0) {
                  entity.getPersistentData().putDouble("Atk8", 38.0);
               } else {
                  entity.getPersistentData().putDouble("Atk8", entity.getPersistentData().getDouble("Atk8") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk8") == 38.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.GREEN_WOOL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.GREEN_WOOL));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 57, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk8") == 18.0) {
                  entity.getPersistentData().putDouble("yaw", entity.getYRot());
                  entity.getPersistentData().putDouble("pitch", entity.getXRot());
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:martian.throw"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:martian.throw"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new CubeEntity(SimpleMobsModEntities.CUBE.get(), _level);
                     entityToSpawn.moveTo(
                        x + entity.getLookAngle().x * 2.0,
                        y + 4.0,
                        z + entity.getLookAngle().z * 2.0,
                        (float)entity.getPersistentData().getDouble("yaw"),
                        (float)entity.getPersistentData().getDouble("pitch")
                     );
                     entityToSpawn.setYBodyRot((float)entity.getPersistentData().getDouble("yaw"));
                     entityToSpawn.setYHeadRot((float)entity.getPersistentData().getDouble("yaw"));
                     entityToSpawn.setDeltaMovement(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)), 0.5, Math.cos(Math.toRadians(entity.getYRot())));
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(25.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        entityiterator.getPersistentData().putBoolean("cubemarked", true);
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk8") == 6.0) {
                  if (Math.random() >= 0.5) {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(25.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                           entity.getPersistentData().putBoolean("Attack8a", true);
                           entity.getPersistentData().putDouble("Atk8", 0.0);
                           entity.getPersistentData().putBoolean("Attack8", false);
                        }
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk8") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk8", 0.0);
                  entity.getPersistentData().putBoolean("Attack8", false);
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  entity.getPersistentData().putBoolean("Attack8", false);
                  entity.getPersistentData().putDouble("Atk8", 0.0);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack8a")) {
               if (entity.getPersistentData().getDouble("Atk8a") == 0.0) {
                  entity.getPersistentData().putDouble("Atk8a", 36.0);
               } else {
                  entity.getPersistentData().putDouble("Atk8a", entity.getPersistentData().getDouble("Atk8a") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk8a") == 36.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.BLACK_WOOL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.BLACK_WOOL));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 57, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk8a") == 20.0) {
                  entity.getPersistentData().putDouble("yaw", entity.getYRot());
                  entity.getPersistentData().putDouble("pitch", entity.getXRot());
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:martian.throw"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:martian.throw"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new CubeEntity(SimpleMobsModEntities.CUBE.get(), _level);
                     entityToSpawn.moveTo(
                        x + entity.getLookAngle().x * 3.0,
                        y + 2.5,
                        z + entity.getLookAngle().z * 3.0,
                        (float)entity.getPersistentData().getDouble("yaw"),
                        (float)entity.getPersistentData().getDouble("pitch")
                     );
                     entityToSpawn.setYBodyRot((float)entity.getPersistentData().getDouble("yaw"));
                     entityToSpawn.setYHeadRot((float)entity.getPersistentData().getDouble("yaw"));
                     entityToSpawn.setDeltaMovement(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)), 0.5, Math.cos(Math.toRadians(entity.getYRot())));
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(25.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        entityiterator.getPersistentData().putBoolean("cubemarked", true);
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk8a") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk8a", 0.0);
                  entity.getPersistentData().putBoolean("Attack8a", false);
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  entity.getPersistentData().putBoolean("Attack8a", false);
                  entity.getPersistentData().putDouble("Atk8a", 0.0);
               }
            }

            if (entity.getPersistentData().getBoolean("Roar")) {
               if (entity.getPersistentData().getDouble("r") == 0.0) {
                  entity.getPersistentData().putDouble("r", 89.0);
               } else {
                  entity.getPersistentData().putDouble("r", entity.getPersistentData().getDouble("r") - 1.0);
               }

               if (entity.getPersistentData().getDouble("r") == 89.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.BROWN_WOOL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.BROWN_WOOL));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 89, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("r") == 73.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:martian.roar"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:martian.roar"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new RoarEntity(SimpleMobsModEntities.ROAR.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), entity.getXRot());
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     entityToSpawn.setDeltaMovement(0.0, 0.0, 0.0);
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                     Vec3 _centerx = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_centerx, _centerx).inflate(10.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if ((entityiterator instanceof Player || entityiterator instanceof ServerPlayer) && entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 40, 2, false, false));
                        }
                     }
                  }
               } else if (entity.getPersistentData().getDouble("r") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("r", 0.0);
                  entity.getPersistentData().putBoolean("Roar", false);
               }

               if (entity.getPersistentData().getDouble("r") > 17.0 && entity.getPersistentData().getDouble("r") < 73.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.ENCHANTED_HIT,
                        x + entity.getLookAngle().x * 1.6,
                        y + 5.0,
                        z + entity.getLookAngle().z * 1.6,
                        10,
                        entity.getLookAngle().x,
                        0.0,
                        entity.getLookAngle().z,
                        0.3
                     );
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.DRAGON_BREATH,
                        x + entity.getLookAngle().x * 1.6,
                        y + 5.0,
                        z + entity.getLookAngle().z * 1.6,
                        10,
                        entity.getLookAngle().x,
                        0.0,
                        entity.getLookAngle().z,
                        0.3
                     );
                  }
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  entity.getPersistentData().putBoolean("Roar", false);
                  entity.getPersistentData().putDouble("r", 0.0);
               }
            }

            if (entity.getPersistentData().getBoolean("Teleport")) {
               entity.fallDistance = 0.0F;
               if (entity.getPersistentData().getDouble("t") == 0.0) {
                  entity.getPersistentData().putDouble("t", 81.0);
               } else {
                  entity.getPersistentData().putDouble("t", entity.getPersistentData().getDouble("t") - 1.0);
               }

               if (entity.getPersistentData().getDouble("t") == 81.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.LIME_WOOL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.LIME_WOOL));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 81, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("t") == 73.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_martian,sort=nearest,limit=1] {Invulnerable:1b}"
                        );
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:martian.teleport"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:martian.teleport"),
                           SoundSource.NEUTRAL,
                           1.0F,
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
                           dev.deepnight.SafeSounds.resolve("block.portal.trigger"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("block.portal.trigger"), SoundSource.NEUTRAL, 1.0F, 1.0F, false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("t") == 54.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.enderman.teleport"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           -1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.enderman.teleport"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           -1.0F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.FLASH, x, y + 2.0, z, 10, 0.3, 0.3, 0.3, 0.4);
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y + 2.0, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/particle minecraft:dragon_breath ~ ~ ~ 1.6 1.6 1.6 1 300"
                        );
                  }
               } else if (entity.getPersistentData().getDouble("t") == 31.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(20.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        Entity _ent = entity;
                        _ent.teleportTo(
                           entityiterator.getX() + entityiterator.getLookAngle().x * 5.0,
                           entityiterator.getY() + 4.0,
                           entityiterator.getZ() + entityiterator.getLookAngle().z * 5.0
                        );
                        if (_ent instanceof ServerPlayer _serverPlayer) {
                           _serverPlayer.connection
                              .teleport(
                                 entityiterator.getX() + entityiterator.getLookAngle().x * 5.0,
                                 entityiterator.getY() + 4.0,
                                 entityiterator.getZ() + entityiterator.getLookAngle().z * 5.0,
                                 _ent.getYRot(),
                                 _ent.getXRot()
                              );
                        }
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
                           "/data merge entity @e[type=deepnight:dawn_martian,sort=nearest,limit=1] {Invulnerable:0}"
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
                           "/particle minecraft:dragon_breath ~ ~ ~ 1.6 1.6 1.6 1 300"
                        );
                  }

                  i = -2.0;
                  j = 0.0;
                  c = -2.0;
               } else if (entity.getPersistentData().getDouble("t") == 29.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.shulker.teleport"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           -1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.shulker.teleport"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           -1.0F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.FLASH, x, y, z, 10, 0.3, 0.3, 0.3, 0.4);
                  }

                  while (i < 2.0) {
                     while (j < 5.0) {
                        for (; c < 2.0; c++) {
                           if (world.getBlockState(BlockPos.containing(x + i, y + j, z + c)).canOcclude()
                              && world.getBlockState(BlockPos.containing(x + i, y + j, z + c)).getDestroySpeed(world, BlockPos.containing(x + i, y + j, z + c)) > 0.0F) {
                              world.destroyBlock(BlockPos.containing(x + i, y + j, z + c), false);
                           }
                        }

                        j++;
                     }

                     i++;
                  }
               } else if (entity.getPersistentData().getDouble("t") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("t", 0.0);
                  entity.getPersistentData().putBoolean("Teleport", false);
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  entity.getPersistentData().putBoolean("Teleport", false);
                  entity.getPersistentData().putDouble("t", 0.0);
               }
            }

            if (entity.getPersistentData().getBoolean("Stunned")) {
               if (entity.getPersistentData().getDouble("stun") == 0.0) {
                  entity.getPersistentData().putDouble("stun", 92.0);
               } else {
                  entity.getPersistentData().putDouble("stun", entity.getPersistentData().getDouble("stun") - 1.0);
               }

               if (entity.getPersistentData().getDouble("stun") == 92.0) {
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
                           x, y, z, dev.deepnight.SafeSounds.resolve("simple_mobs:poise"), SoundSource.NEUTRAL, 3.0F, 1.0F, false
                        );
                     }
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 92, 7, false, false));
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

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("Stunned", false);
               }
            }
         }
      }
   }
}
