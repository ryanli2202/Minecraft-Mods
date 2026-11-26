package dev.deepnight.source.dawn.procedures;


import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.SimpleMobsMod;
import dev.deepnight.source.dawn.entity.NineTailsEntity;
import dev.deepnight.source.dawn.entity.TailDartEntity;
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

public class ProtailsProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, final Entity entity) {
      if (entity != null) {
         if (entity.isAlive()) {
            double distance = 0.0;
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

            if (entity.getPersistentData().getBoolean("sweep")) {
               entity.getPersistentData().putDouble("direction", Math.abs(entity.getYRot() / 360.0F * 14.0F) + 2.0F);
               if (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop") > 14.0) {
                  entity.getPersistentData().putDouble("direction", entity.getPersistentData().getDouble("direction") - 14.0);
               } else if (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop") < 0.0) {
                  entity.getPersistentData().putDouble("direction", entity.getPersistentData().getDouble("direction") + 14.0);
               }

               if (!(entity.getPersistentData().getDouble("loop") > -3.0)) {
                  entity.getPersistentData().putDouble("loop", 0.0);
                  entity.getPersistentData().putBoolean("sweep", false);
               } else {
                  Vec3 _center = new Vec3(
                     x + Math.cos((Math.PI / 7) * (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop"))) * 1.6,
                     y + 1.0,
                     z + Math.sin((Math.PI / 7) * (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop"))) * 1.6
                  );

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof NineTailsEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(1.5F)
                           .setArmorNegation(10.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 10.0F);
                        entityiterator.setDeltaMovement(
                           new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 90.0F)) * 2.0, 0.4, Math.cos(Math.toRadians(entity.getYRot() + 270.0F)) * 2.0)
                        );
                     }
                  }

                  entity.getPersistentData().putDouble("loop", entity.getPersistentData().getDouble("loop") - 1.0);
               }
            }

            if (entity.getPersistentData().getBoolean("grab")) {
               if (entity.getPersistentData().getDouble("timer") == 0.0) {
                  entity.getPersistentData().putDouble("timer", 60.0);
               } else {
                  entity.getPersistentData().putDouble("timer", entity.getPersistentData().getDouble("timer") - 1.0);
               }

               if (entity.getPersistentData().getDouble("timer") == 60.0) {
                  entity.getPersistentData().putBoolean("Attack2c", false);
                  entity.getPersistentData().putDouble("Atk2c", 0.0);
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.GOLD_INGOT));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLD_INGOT));
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_grab"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_grab"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F,
                           false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("timer") == 11.0) {
                  entity.getPersistentData().putDouble("Atk2cCD", 160.0);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("grab", false);
               }

               Vec3 _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                     entity.getPersistentData().putBoolean("hold", true);
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/effect give @p[distance=..3] dawntweaks:immobilized 2 1 true"
                           );
                     }

                     if (entityiterator instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 2, 1, false, false));
                     }

                     if (entityiterator instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 2, 2, false, false));
                     }

                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 6, 7, false, false));
                     }

                     if ((entityiterator instanceof Player || entityiterator instanceof ServerPlayer) && entityiterator instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.STUN.get(), 2, 6, false, false));
                     }

                     if (entity.getPersistentData().getDouble("timer") == 41.0) {
                        entityiterator.hurt(new DamageSource(entityiterator.level().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(net.minecraft.world.damagesource.DamageTypes.GENERIC), entity), 12.0F);
                        if (entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance(MobEffects.POISON, 180, 0, false, false));
                        }

                        if (entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, 2, false, false));
                        }
                     } else if (entity.getPersistentData().getDouble("timer") == 26.0) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.SHORT)
                           .setImpact(18.0F)
                           .setArmorNegation(15.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 1.0F);
                        entityiterator.setDeltaMovement(
                           new Vec3((entityiterator.getX() - entity.getX()) / 3.0, 0.3, (entityiterator.getX() - entity.getX()) / 3.0)
                        );
                     }
                  }
               }

               if (!entity.getPersistentData().getBoolean("hold")) {
                  entity.getPersistentData().putBoolean("Attacking", false);
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("grab", false);
               }
            }

            if (!world.isClientSide()) {
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

               if (!SimpleMobsModVariables.MapVariables.get(world).aggro_tails) {
                  if (entity.getPersistentData().getBoolean("Tails")) {
                     if (entity.getPersistentData().getDouble("tailre") == 0.0) {
                        entity.getPersistentData().putDouble("tailre", 100.0);
                     } else if (entity.getPersistentData().getDouble("tailre") == 1.0) {
                        entity.getPersistentData().putBoolean("notail", true);
                        entity.getPersistentData().putDouble("tailre", 0.0);
                     } else {
                        entity.getPersistentData().putDouble("tailre", entity.getPersistentData().getDouble("tailre") - 1.0);
                     }
                  } else {
                     entity.getPersistentData().putDouble("tailre", 0.0);
                  }
               }

               entity.getPersistentData().putBoolean("Close", false);
               entity.getPersistentData().putBoolean("Close2", false);
               entity.getPersistentData().putBoolean("Mid", false);
               entity.getPersistentData().putBoolean("Far", false);
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
                           Math.toDegrees(Math.atan(entity.getPersistentData().getDouble("distance") / (entityiterator.getY() + 1.5 - (entity.getY() + 1.5))))
                        );
                     if (entity.getPersistentData().getDouble("pitch2") > 0.0) {
                        entity.getPersistentData().putDouble("pitch2", entity.getPersistentData().getDouble("pitch2") - 90.0);
                     } else if (entity.getPersistentData().getDouble("pitch2") < 0.0) {
                        entity.getPersistentData().putDouble("pitch2", entity.getPersistentData().getDouble("pitch2") - -90.0);
                     }
                  }
               }

               _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(18.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                     entity.getPersistentData().putBoolean("Far", true);
                  }
               }

               _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(6.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                     entity.getPersistentData().putBoolean("Far", false);
                     entity.getPersistentData().putBoolean("Mid", true);
                  }
               }

               _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.75), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                     entity.getPersistentData().putBoolean("Far", false);
                     entity.getPersistentData().putBoolean("Mid", false);
                     entity.getPersistentData().putBoolean("Close2", true);
                  }
               }

               _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                     entity.getPersistentData().putBoolean("Far", false);
                     entity.getPersistentData().putBoolean("Mid", false);
                     entity.getPersistentData().putBoolean("Close2", false);
                     entity.getPersistentData().putBoolean("Close", true);
                  }
               }

               if (entity.getPersistentData().getDouble("Atk2cCD") >= 1.0) {
                  entity.getPersistentData().putDouble("Atk2cCD", entity.getPersistentData().getDouble("Atk2cCD") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk2eCD") >= 1.0) {
                  entity.getPersistentData().putDouble("Atk2eCD", entity.getPersistentData().getDouble("Atk2eCD") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk2gCD") == 0.0 && entity.getPersistentData().getBoolean("Attack2g")) {
                  entity.getPersistentData().putDouble("Atk2gCD", 300.0);
               } else if (entity.getPersistentData().getDouble("Atk2gCD") > 0.0) {
                  entity.getPersistentData().putDouble("Atk2gCD", entity.getPersistentData().getDouble("Atk2gCD") - 1.0);
               }

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

               if (entity.getPersistentData().getBoolean("notail")) {
                  if (entity.getPersistentData().getDouble("notailan") == 0.0) {
                     entity.getPersistentData().putDouble("notailan", 40.0);
                  } else {
                     entity.getPersistentData().putDouble("notailan", entity.getPersistentData().getDouble("notailan") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("notailan") == 40.0) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_nine_tails,sort=nearest,limit=1] {Invulnerable:1b}"
                           );
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.CARVED_PUMPKIN));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.CARVED_PUMPKIN));
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("notailan") == 15.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:tail"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x, y, z, dev.deepnight.SafeSounds.resolve("simple_mobs:tail"), SoundSource.NEUTRAL, 1.0F, 1.0F, false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("notailan") == 4.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(2, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Blocks.AIR));
                     }
                  } else if (entity.getPersistentData().getDouble("notailan") == 1.0) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_nine_tails,sort=nearest,limit=1] {Invulnerable:0}"
                           );
                     }

                     entity.getPersistentData().putBoolean("Tails", false);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("popped", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("notail", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Tailpop")) {
                  if (entity.getPersistentData().getDouble("pop") == 0.0) {
                     entity.getPersistentData().putDouble("pop", 60.0);
                  } else {
                     entity.getPersistentData().putDouble("pop", entity.getPersistentData().getDouble("pop") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("pop") == 60.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     entity.getPersistentData().putBoolean("popped", true);
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_nine_tails,sort=nearest,limit=1] {Invulnerable:1b}"
                           );
                     }

                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.PUMPKIN));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.PUMPKIN));
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("pop") == 32.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:tail"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x, y, z, dev.deepnight.SafeSounds.resolve("simple_mobs:tail"), SoundSource.NEUTRAL, 1.0F, 1.0F, false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("pop") == 4.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(2, new ItemStack(Items.LEATHER_CHESTPLATE));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
                     }
                  } else if (entity.getPersistentData().getDouble("pop") == 1.0) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_nine_tails,sort=nearest,limit=1] {Invulnerable:0}"
                           );
                     }

                     entity.getPersistentData().putBoolean("Tails", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Tailpop", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack1a")) {
                  if (entity.getPersistentData().getDouble("Atk1a") == 0.0) {
                     entity.getPersistentData().putDouble("Atk1a", 21.0);
                  } else {
                     entity.getPersistentData().putDouble("Atk1a", entity.getPersistentData().getDouble("Atk1a") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Atk1a") == 21.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.ROSE_BUSH));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.ROSE_BUSH));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("Atk1a") == 10.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_punch"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_punch"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F,
                              false
                           );
                        }
                     }

                     _center = new Vec3(x + entity.getLookAngle().x * 1.3, y, z + entity.getLookAngle().z * 1.3);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(0.65), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof NineTailsEntity)) {
                           if ((entityiterator instanceof LivingEntity _livEnt ? _livEnt.isBlocking() : false)
                              && !world.getEntitiesOfClass(
                                    NineTailsEntity.class,
                                    AABB.ofSize(
                                       new Vec3(
                                          entityiterator.getX() + entityiterator.getLookAngle().x * 2.0,
                                          entityiterator.getY() + entityiterator.getLookAngle().y * 2.0,
                                          entityiterator.getZ() + entityiterator.getLookAngle().z * 2.0
                                       ),
                                       4.0,
                                       4.0,
                                       4.0
                                    ),
                                    e -> true
                                 )
                                 .isEmpty()) {
                              if ((entityiterator instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == Items.SHIELD) {
                                 ItemStack _ist = entityiterator instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY;
                                 if (_ist.hurt(6, net.minecraft.util.RandomSource.create(), null)) {
                                    _ist.shrink(1);
                                    _ist.setDamageValue(0);
                                 }

                                 if (entityiterator instanceof Player _player) {
                                    _player.getCooldowns()
                                       .addCooldown(
                                          (entityiterator instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem(), 100
                                       );
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
                                 if (_ist.hurt(6, net.minecraft.util.RandomSource.create(), null)) {
                                    _ist.shrink(1);
                                    _ist.setDamageValue(0);
                                 }

                                 if (entityiterator instanceof Player _player) {
                                    _player.getCooldowns()
                                       .addCooldown(
                                          (entityiterator instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem(), 100
                                       );
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
                                 .setImpact(0.3F)
                                 .setArmorNegation(1.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 6.0F);
                           }
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk1a") == 6.0) {
                     if (Math.random() < 0.3) {
                        entity.getPersistentData().putDouble("Atk1a", 0.0);
                        entity.getPersistentData().putBoolean("Attack1a", false);
                        entity.getPersistentData().putBoolean("Attack1b", true);
                     }
                  } else if (entity.getPersistentData().getDouble("Atk1a") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("Atk1a", 0.0);
                     SimpleMobsModVariables.MapVariables.get(world).aggro_tails = false;
                     SimpleMobsModVariables.MapVariables.get(world).syncData(world);
                     if (Math.random() < 0.3) {
                        entity.getPersistentData().putBoolean("Attacking", true);
                        entity.getPersistentData().putBoolean("Dodge2", true);
                     }

                     entity.getPersistentData().putBoolean("Attack1a", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("Atk1a", 0.0);
                     entity.getPersistentData().putBoolean("Attack1a", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack1b")) {
                  if (entity.getPersistentData().getDouble("Atk1b") == 0.0) {
                     entity.getPersistentData().putDouble("Atk1b", 40.0);
                  } else {
                     entity.getPersistentData().putDouble("Atk1b", entity.getPersistentData().getDouble("Atk1b") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Atk1b") == 40.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.POPPY));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.POPPY));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 6, false, false));
                     }

                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_attack1b"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_attack1b"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F,
                              false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk1b") == 22.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 2.0, y + 0.7, z + entity.getLookAngle().z * 2.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof NineTailsEntity)) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.LONG)
                              .setImpact(1.2F)
                              .setArmorNegation(15.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 9.0F);
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
                     SimpleMobsModVariables.MapVariables.get(world).aggro_tails = false;
                     SimpleMobsModVariables.MapVariables.get(world).syncData(world);
                     entity.getPersistentData().putBoolean("Attack1b", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("Atk1b", 0.0);
                     entity.getPersistentData().putBoolean("Attack1b", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack1c")) {
                  if (entity.getPersistentData().getDouble("Atk1c") == 0.0) {
                     entity.getPersistentData().putDouble("Atk1c", 40.0);
                  } else {
                     entity.getPersistentData().putDouble("Atk1c", entity.getPersistentData().getDouble("Atk1c") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Atk1c") == 40.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.VINE));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.VINE));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     entity.getPersistentData().putDouble("yaw", entity.getYRot());
                     entity.getPersistentData().putDouble("pitch", entity.getXRot());
                     entity.getPersistentData().putBoolean("lock", true);
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("Atk1c") == 33.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:sweep"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x, y, z, dev.deepnight.SafeSounds.resolve("simple_mobs:sweep"), SoundSource.NEUTRAL, 1.0F, 1.0F, false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk1c") == 28.0) {
                     entity.getPersistentData().putDouble("loop", 4.0);
                     entity.getPersistentData().putBoolean("sweep", true);
                  } else if (entity.getPersistentData().getDouble("Atk1c") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("Atk1c", 0.0);
                     entity.getPersistentData().putBoolean("lock", false);
                     SimpleMobsModVariables.MapVariables.get(world).aggro_tails = false;
                     SimpleMobsModVariables.MapVariables.get(world).syncData(world);
                     entity.getPersistentData().putBoolean("Attack1c", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("Atk1c", 0.0);
                     entity.getPersistentData().putBoolean("Attack1c", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack1d")) {
                  if (entity.getPersistentData().getDouble("Atk1d") == 0.0) {
                     entity.getPersistentData().putDouble("Atk1d", 35.0);
                  } else {
                     entity.getPersistentData().putDouble("Atk1d", entity.getPersistentData().getDouble("Atk1d") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Atk1d") == 35.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.CORNFLOWER));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.CORNFLOWER));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 34, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("Atk1d") == 18.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_punch"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_punch"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F,
                              false
                           );
                        }
                     }

                     _center = new Vec3(x + entity.getLookAngle().x * 1.6, y, z + entity.getLookAngle().z * 1.6);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(0.75), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof NineTailsEntity)) {
                           if ((entityiterator instanceof LivingEntity _livEnt ? _livEnt.isBlocking() : false)
                              && !world.getEntitiesOfClass(
                                    NineTailsEntity.class,
                                    AABB.ofSize(
                                       new Vec3(
                                          entityiterator.getX() + entityiterator.getLookAngle().x * 2.0,
                                          entityiterator.getY() + entityiterator.getLookAngle().y * 2.0,
                                          entityiterator.getZ() + entityiterator.getLookAngle().z * 2.0
                                       ),
                                       4.0,
                                       4.0,
                                       4.0
                                    ),
                                    e -> true
                                 )
                                 .isEmpty()) {
                              if ((entityiterator instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == Items.SHIELD) {
                                 ItemStack _ist = entityiterator instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY;
                                 if (_ist.hurt(6, net.minecraft.util.RandomSource.create(), null)) {
                                    _ist.shrink(1);
                                    _ist.setDamageValue(0);
                                 }

                                 if (entityiterator instanceof Player _player) {
                                    _player.getCooldowns()
                                       .addCooldown(
                                          (entityiterator instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem(), 100
                                       );
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
                                 if (_ist.hurt(6, net.minecraft.util.RandomSource.create(), null)) {
                                    _ist.shrink(1);
                                    _ist.setDamageValue(0);
                                 }

                                 if (entityiterator instanceof Player _player) {
                                    _player.getCooldowns()
                                       .addCooldown(
                                          (entityiterator instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem(), 100
                                       );
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
                                 .setArmorNegation(15.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 6.0F);
                           }

                           entityiterator.setDeltaMovement(
                              new Vec3((entityiterator.getX() - entity.getX()) / 2.0, 0.3, (entityiterator.getZ() - entity.getZ()) / 2.0)
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk1d") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("Atk1d", 0.0);
                     SimpleMobsModVariables.MapVariables.get(world).aggro_tails = false;
                     SimpleMobsModVariables.MapVariables.get(world).syncData(world);
                     if (Math.random() < 0.3) {
                        entity.getPersistentData().putBoolean("Attacking", true);
                        entity.getPersistentData().putBoolean("Dodge1", true);
                     }

                     entity.getPersistentData().putBoolean("Attack1d", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("Atk1d", 0.0);
                     entity.getPersistentData().putBoolean("Attack1d", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Dodge1")) {
                  if (entity.getPersistentData().getDouble("d1") == 0.0) {
                     entity.getPersistentData().putDouble("d1", 20.0);
                  } else {
                     entity.getPersistentData().putDouble("d1", entity.getPersistentData().getDouble("d1") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("d1") == 20.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.FLOWERING_AZALEA));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.FLOWERING_AZALEA));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_dodge"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_dodge"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F,
                              false
                           );
                        }
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 18, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("d1") == 15.0) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_nine_tails,sort=nearest,limit=1] {Invulnerable:1b}"
                           );
                     }

                     entity.setDeltaMovement(
                        new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 90.0F)) * 0.7, 0.4, Math.cos(Math.toRadians(entity.getYRot() + 270.0F)) * 0.7)
                     );
                  } else if (entity.getPersistentData().getDouble("d1") == 4.0) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_nine_tails,sort=nearest,limit=1] {Invulnerable:0}"
                           );
                     }
                  } else if (entity.getPersistentData().getDouble("d1") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("d1", 0.0);
                     SimpleMobsModVariables.MapVariables.get(world).aggro_tails = false;
                     SimpleMobsModVariables.MapVariables.get(world).syncData(world);
                     entity.getPersistentData().putBoolean("Dodge1", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("d1", 0.0);
                     entity.getPersistentData().putBoolean("Dodge1", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Dodge2")) {
                  if (entity.getPersistentData().getDouble("d2") == 0.0) {
                     entity.getPersistentData().putDouble("d2", 20.0);
                  } else {
                     entity.getPersistentData().putDouble("d2", entity.getPersistentData().getDouble("d2") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("d2") == 20.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.DANDELION));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.DANDELION));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_dodge"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_dodge"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F,
                              false
                           );
                        }
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 18, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("d2") == 15.0) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_nine_tails,sort=nearest,limit=1] {Invulnerable:1b}"
                           );
                     }

                     entity.setDeltaMovement(
                        new Vec3(Math.sin(Math.toRadians(entity.getYRot() - 90.0F)) * 0.7, 0.4, Math.cos(Math.toRadians(entity.getYRot() + 90.0F)) * 0.7)
                     );
                  } else if (entity.getPersistentData().getDouble("d2") == 4.0) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_nine_tails,sort=nearest,limit=1] {Invulnerable:0}"
                           );
                     }
                  } else if (entity.getPersistentData().getDouble("d2") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("d2", 0.0);
                     SimpleMobsModVariables.MapVariables.get(world).aggro_tails = false;
                     SimpleMobsModVariables.MapVariables.get(world).syncData(world);
                     entity.getPersistentData().putBoolean("Dodge2", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("d2", 0.0);
                     entity.getPersistentData().putBoolean("Dodge2", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack2a")) {
                  if (entity.getPersistentData().getDouble("Atk2a") == 0.0) {
                     entity.getPersistentData().putDouble("Atk2a", 60.0);
                  } else {
                     entity.getPersistentData().putDouble("Atk2a", entity.getPersistentData().getDouble("Atk2a") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Atk2a") == 60.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.COAL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.COAL));
                     }

                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_attack2a"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_attack2a"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F,
                              false
                           );
                        }
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 32, 1, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("Atk2a") == 28.0) {
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 28, 8, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("Atk2a") >= 20.0 && entity.getPersistentData().getDouble("Atk2a") <= 22.0) {
                     entity.getPersistentData().putDouble("i", 1.0);

                     while (entity.getPersistentData().getDouble("i") < 6.0) {
                        _center = new Vec3(
                           x + entity.getLookAngle().x * entity.getPersistentData().getDouble("i"),
                           y,
                           z + entity.getLookAngle().z * entity.getPersistentData().getDouble("i")
                        );

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.15), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (!(entityiterator instanceof NineTailsEntity)) {

                              SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                 .setInitialPosition(entity.position())
                                 .setStunType(StunType.KNOCKDOWN)
                                 .setImpact(4.0F)
                                 .setArmorNegation(16.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 14.0F);
                              if (entityiterator instanceof LivingEntity _entity) {
                                 _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 4, false, false));
                              }
                           }
                        }

                        entity.getPersistentData().putDouble("i", entity.getPersistentData().getDouble("i") + 1.0);
                     }
                  } else if (entity.getPersistentData().getDouble("Atk2a") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("Atk2a", 0.0);
                     SimpleMobsModVariables.MapVariables.get(world).aggro_tails = false;
                     SimpleMobsModVariables.MapVariables.get(world).syncData(world);
                     entity.getPersistentData().putBoolean("Attack2a", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("Atk2a", 0.0);
                     entity.getPersistentData().putBoolean("Attack2a", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack2a2")) {
                  if (entity.getPersistentData().getDouble("Atk2a2") == 0.0) {
                     entity.getPersistentData().putDouble("Atk2a2", 30.0);
                  } else {
                     entity.getPersistentData().putDouble("Atk2a2", entity.getPersistentData().getDouble("Atk2a2") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Atk2a2") == 30.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.CHARCOAL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHARCOAL));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1, false, false));
                     }

                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_attack2a2"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_attack2a2"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F,
                              false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk2a2") == 12.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 1.8, y + 1.0, z + entity.getLookAngle().z * 1.8);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(0.75), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof NineTailsEntity)) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.SHORT)
                              .setImpact(1.3F)
                              .setArmorNegation(15.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 9.0F);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk2a2") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("Atk2a2", 0.0);
                     SimpleMobsModVariables.MapVariables.get(world).aggro_tails = false;
                     SimpleMobsModVariables.MapVariables.get(world).syncData(world);
                     entity.getPersistentData().putBoolean("Attack2a2", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("Atk2a2", 0.0);
                     entity.getPersistentData().putBoolean("Attack2a2", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack2b")) {
                  if (entity.getPersistentData().getDouble("Atk2b") == 0.0) {
                     entity.getPersistentData().putDouble("Atk2b", 75.0);
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 7, false, false));
                     }
                  } else {
                     entity.getPersistentData().putDouble("Atk2b", entity.getPersistentData().getDouble("Atk2b") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Atk2b") == 75.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.IRON_NUGGET));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_NUGGET));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("Atk2b") == 67.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_attack2b"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_attack2b"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F,
                              false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk2b") == 47.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 1.8, y + 1.0, z + entity.getLookAngle().z * 1.8);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof NineTailsEntity)) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.SHORT)
                              .setImpact(0.4F)
                              .setArmorNegation(15.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 6.0F);
                           entityiterator.invulnerableTime = 0;
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk2b") == 34.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 1.8, y + 1.0, z + entity.getLookAngle().z * 1.8);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof NineTailsEntity)) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.SHORT)
                              .setImpact(0.5F)
                              .setArmorNegation(15.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 6.0F);
                           entityiterator.invulnerableTime = 0;
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk2b") == 28.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 1.8, y + 1.0, z + entity.getLookAngle().z * 1.8);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof NineTailsEntity)) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.SHORT)
                              .setImpact(0.5F)
                              .setArmorNegation(15.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 6.0F);
                           entityiterator.invulnerableTime = 0;
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk2b") == 14.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 1.8, y + 1.0, z + entity.getLookAngle().z * 1.8);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof NineTailsEntity)) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.SHORT)
                              .setImpact(0.9F)
                              .setArmorNegation(15.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 9.0F);
                           entityiterator.invulnerableTime = 0;
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk2b") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("Atk2b", 0.0);
                     SimpleMobsModVariables.MapVariables.get(world).aggro_tails = false;
                     SimpleMobsModVariables.MapVariables.get(world).syncData(world);
                     entity.getPersistentData().putBoolean("Attack2b", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("Atk2b", 0.0);
                     entity.getPersistentData().putBoolean("Attack2b", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack2c")) {
                  if (entity.getPersistentData().getDouble("Atk2c") == 0.0) {
                     entity.getPersistentData().putDouble("Atk2c", 60.0);
                  } else {
                     entity.getPersistentData().putDouble("Atk2c", entity.getPersistentData().getDouble("Atk2c") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Atk2c") <= 46.0 && entity.getPersistentData().getDouble("Atk2c") >= 28.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 1.0, y + 1.0, z + entity.getLookAngle().z * 1.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(0.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                           entity.getPersistentData().putBoolean("grab", true);
                           if (entityiterator instanceof LivingEntity _entity) {
                              _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 6, false, false));
                           }

                           if (entity instanceof LivingEntity _entity) {
                              _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 6, false, false));
                           }
                        }
                     }
                  }

                  if (entity.getPersistentData().getDouble("Atk2c") == 60.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_attack2c."),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_attack2c."),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F,
                              false
                           );
                        }
                     }

                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.GOLD_NUGGET));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLD_NUGGET));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 15, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("Atk2c") == 45.0) {
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 15, 3, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("Atk2c") == 30.0) {
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 2, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("Atk2c") == 11.0) {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof NineTailsEntity) && Math.random() <= 0.3) {
                           entity.getPersistentData().putDouble("Atk2c", 0.0);
                           entity.getPersistentData().putDouble("Atk2cCD", 120.0);
                           entity.getPersistentData().putBoolean("Attack2c", false);
                           entity.getPersistentData().putBoolean("Attack2c2", true);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk2c") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("Atk2c", 0.0);
                     entity.getPersistentData().putDouble("Atk2cCD", 100.0);
                     SimpleMobsModVariables.MapVariables.get(world).aggro_tails = false;
                     SimpleMobsModVariables.MapVariables.get(world).syncData(world);
                     entity.getPersistentData().putBoolean("Attack2c", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("Atk2c", 0.0);
                     entity.getPersistentData().putDouble("Atk2cCD", 100.0);
                     entity.getPersistentData().putBoolean("Attack2c", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack2c2")) {
                  if (entity.getPersistentData().getDouble("Atk2c2") == 0.0) {
                     entity.getPersistentData().putDouble("Atk2c2", 34.0);
                  } else {
                     entity.getPersistentData().putDouble("Atk2c2", entity.getPersistentData().getDouble("Atk2c2") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Atk2c2") == 34.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_attack2c."),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_attack2c."),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F,
                              false
                           );
                        }
                     }

                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.GHAST_TEAR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GHAST_TEAR));
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 25, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("Atk2c2") <= 26.0 && entity.getPersistentData().getDouble("Atk2c2") >= 21.0) {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof NineTailsEntity)) {
                           entityiterator.setDeltaMovement(
                              new Vec3((entityiterator.getX() - entity.getX()) * 1.5, 0.8, (entityiterator.getZ() - entity.getZ()) * 1.5)
                           );
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.LONG)
                              .setImpact(0.5F)
                              .setArmorNegation(15.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 7.0F);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk2c2") == 2.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.LILAC));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.LILAC));
                     }
                  } else if (entity.getPersistentData().getDouble("Atk2c2") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("Atk2c2", 0.0);
                     entity.getPersistentData().putBoolean("Attack2c2", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("Atk2c2", 0.0);
                     entity.getPersistentData().putBoolean("Attack2c2", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack2d")) {
                  if (entity.getPersistentData().getDouble("Atk2d") == 0.0) {
                     entity.getPersistentData().putDouble("Atk2d", 35.0);
                  } else {
                     entity.getPersistentData().putDouble("Atk2d", entity.getPersistentData().getDouble("Atk2d") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Atk2d") == 35.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.DIAMOND));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 8, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("Atk2d") == 27.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_attack2d"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_attack2d"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F,
                              false
                           );
                        }
                     }

                     _center = new Vec3(x + entity.getLookAngle().x * 1.2, y + 1.0, z + entity.getLookAngle().z * 1.2);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof NineTailsEntity)) {
                           entityiterator.setDeltaMovement(
                              new Vec3((entityiterator.getX() - entity.getX()) * 1.5, 0.8, (entityiterator.getZ() - entity.getZ()) * 1.5)
                           );
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.LONG)
                              .setImpact(1.2F)
                              .setArmorNegation(15.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 9.0F);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk2d") == 25.0) {
                     entity.setDeltaMovement(
                        new Vec3(Math.sin(Math.toRadians(entity.getYRot())) * 1.2, 0.7, Math.cos(Math.toRadians(entity.getYRot() + 180.0F)) * 1.2)
                     );
                  } else if (entity.getPersistentData().getDouble("Atk2d") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("Atk2d", 0.0);
                     SimpleMobsModVariables.MapVariables.get(world).aggro_tails = false;
                     SimpleMobsModVariables.MapVariables.get(world).syncData(world);
                     entity.getPersistentData().putBoolean("Attack2d", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("Atk2d", 0.0);
                     entity.getPersistentData().putBoolean("Attack2d", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack2f")) {
                  if (entity.getPersistentData().getDouble("Atk2f") == 0.0) {
                     entity.getPersistentData().putDouble("Atk2f", 40.0);
                  } else {
                     entity.getPersistentData().putDouble("Atk2f", entity.getPersistentData().getDouble("Atk2f") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Atk2f") == 40.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.CARROT));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CARROT));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("Atk2f") == 25.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_punch"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_punch"),
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
                              dev.deepnight.SafeSounds.resolve("item.bucket.fill_lava"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              0.5F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("item.bucket.fill_lava"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              0.5F,
                              false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk2f") <= 24.0 && entity.getPersistentData().getDouble("Atk2f") >= 21.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 1.2, y + 1.0, z + entity.getLookAngle().z * 1.2);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.75), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof NineTailsEntity)) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.LONG)
                              .setImpact(1.2F)
                              .setArmorNegation(15.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 10.0F);
                           entityiterator.setDeltaMovement(
                              new Vec3(Math.sin(Math.toRadians(entity.getYRot() - 90.0F)) * 1.2, 0.5, Math.cos(Math.toRadians(entity.getYRot() + 90.0F)) * 1.2)
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk2f") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("Atk2f", 0.0);
                     SimpleMobsModVariables.MapVariables.get(world).aggro_tails = false;
                     SimpleMobsModVariables.MapVariables.get(world).syncData(world);
                     entity.getPersistentData().putBoolean("Attack2f", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("Atk2f", 0.0);
                     entity.getPersistentData().putBoolean("Attack2f", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack2e")) {
                  if (entity.getPersistentData().getDouble("Atk2e") == 0.0) {
                     entity.getPersistentData().putDouble("Atk2e", 100.0);
                  } else {
                     entity.getPersistentData().putDouble("Atk2e", entity.getPersistentData().getDouble("Atk2e") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Atk2e") == 100.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.POTATO));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.POTATO));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("Atk2e") == 70.0) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_nine_tails,sort=nearest,limit=1] {Invulnerable:1b}"
                           );
                     }

                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 50, 1, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("Atk2e") == 40.0) {
                     if (Math.random() <= 0.5) {
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
                           public void tick_ProtailsProcedure_1(ServerTickEvent event) {
                              if (event.phase == Phase.END) {
                                 this.ticks++;
                                 if (this.ticks >= this.waitTicks) {
                                    this.run();
                                 }
                              }
                           }

                           private void run() {
                              entity.getPersistentData().putBoolean("Attack2e2", true);
                              entity.getPersistentData().putDouble("Atk2e", 0.0);
                              if (entity instanceof LivingEntity _entity) {
                                 _entity.removeAllEffects();
                              }

                              entity.getPersistentData().putBoolean("Attack2e", false);
                              MinecraftForge.EVENT_BUS.unregister(this);
                           }
                        }).start(world, Mth.nextInt(net.minecraft.util.RandomSource.create(), 0, 20));
                     }
                  } else if (entity.getPersistentData().getDouble("Atk2e") == 18.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_attack2d"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_attack2d"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F,
                              false
                           );
                        }
                     }

                     _center = new Vec3(x + entity.getLookAngle().x * 2.0, y + 1.0, z + entity.getLookAngle().z * 2.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof NineTailsEntity)) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.KNOCKDOWN)
                              .setImpact(1.5F)
                              .setArmorNegation(15.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 8.0F);
                           entityiterator.setDeltaMovement(
                              new Vec3(Math.sin(Math.toRadians(entity.getYRot())) * 0.5, 0.5, Math.cos(Math.toRadians(entity.getYRot() + 180.0F)) * 0.5)
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk2e") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_nine_tails,sort=nearest,limit=1] {Invulnerable:0}"
                           );
                     }

                     entity.getPersistentData().putDouble("Atk2e", 0.0);
                     entity.getPersistentData().putDouble("Atk2eCD", 80.0);
                     SimpleMobsModVariables.MapVariables.get(world).aggro_tails = false;
                     SimpleMobsModVariables.MapVariables.get(world).syncData(world);
                     entity.getPersistentData().putBoolean("Attack2e", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("Atk2e", 0.0);
                     entity.getPersistentData().putBoolean("Attack2e", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack2e2")) {
                  if (entity.getPersistentData().getDouble("Atk2e2") == 0.0) {
                     entity.getPersistentData().putDouble("Atk2e2", 24.0);
                  } else {
                     entity.getPersistentData().putDouble("Atk2e2", entity.getPersistentData().getDouble("Atk2e2") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Atk2e2") == 24.0) {
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 24, 6, false, false));
                     }

                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.APPLE));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.APPLE));
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_nine_tails,sort=nearest,limit=1] {Invulnerable:0}"
                           );
                     }

                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(15.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                           entityiterator.getPersistentData().putBoolean("target", true);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk2e2") == 18.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_attack2e2"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_attack2e2"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F,
                              false
                           );
                        }
                     }

                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new TailDartEntity(SimpleMobsModEntities.TAIL_DART.get(), _level);
                        entityToSpawn.moveTo(
                           x + entity.getLookAngle().x * 1.5 + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.5, 0.5),
                           y + 1.7,
                           z + entity.getLookAngle().z * 1.5 + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.5, 0.5),
                           entity.getYRot(),
                           entity.getXRot()
                        );
                        entityToSpawn.setYBodyRot(entity.getYRot());
                        entityToSpawn.setYHeadRot(entity.getYRot());
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }

                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new TailDartEntity(SimpleMobsModEntities.TAIL_DART.get(), _level);
                        entityToSpawn.moveTo(
                           x + entity.getLookAngle().x * 1.5 + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.5, 0.5),
                           y + 1.7,
                           z + entity.getLookAngle().z * 1.5 + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.5, 0.5),
                           (float)(entity.getYRot() + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -20.0, 20.0)),
                           (float)(entity.getXRot() + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -20.0, 20.0))
                        );
                        entityToSpawn.setYBodyRot((float)(entity.getYRot() + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -20.0, 20.0)));
                        entityToSpawn.setYHeadRot((float)(entity.getYRot() + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -20.0, 20.0)));
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }

                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new TailDartEntity(SimpleMobsModEntities.TAIL_DART.get(), _level);
                        entityToSpawn.moveTo(
                           x + entity.getLookAngle().x * 1.5 + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.5, 0.5),
                           y + 1.7,
                           z + entity.getLookAngle().z * 1.5 + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.5, 0.5),
                           (float)(entity.getYRot() + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -30.0, 30.0)),
                           (float)(entity.getXRot() + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -30.0, 30.0))
                        );
                        entityToSpawn.setYBodyRot((float)(entity.getYRot() + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -30.0, 30.0)));
                        entityToSpawn.setYHeadRot((float)(entity.getYRot() + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -30.0, 30.0)));
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }

                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new TailDartEntity(SimpleMobsModEntities.TAIL_DART.get(), _level);
                        entityToSpawn.moveTo(
                           x + entity.getLookAngle().x * 1.5 + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.5, 0.5),
                           y + 1.7,
                           z + entity.getLookAngle().z * 1.5 + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.5, 0.5),
                           (float)(entity.getYRot() + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -20.0, 20.0)),
                           (float)(entity.getXRot() + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -20.0, 20.0))
                        );
                        entityToSpawn.setYBodyRot((float)(entity.getYRot() + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -20.0, 20.0)));
                        entityToSpawn.setYHeadRot((float)(entity.getYRot() + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -20.0, 20.0)));
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }

                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new TailDartEntity(SimpleMobsModEntities.TAIL_DART.get(), _level);
                        entityToSpawn.moveTo(
                           x + entity.getLookAngle().x * 1.5 + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.5, 0.5),
                           y + 1.7,
                           z + entity.getLookAngle().z * 1.5 + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.5, 0.5),
                           (float)(entity.getYRot() + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -30.0, 30.0)),
                           (float)(entity.getXRot() + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -30.0, 30.0))
                        );
                        entityToSpawn.setYBodyRot((float)(entity.getYRot() + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -30.0, 30.0)));
                        entityToSpawn.setYHeadRot((float)(entity.getYRot() + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -30.0, 30.0)));
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }

                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new TailDartEntity(SimpleMobsModEntities.TAIL_DART.get(), _level);
                        entityToSpawn.moveTo(
                           x + entity.getLookAngle().x * 2.0 + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.5, 1.5),
                           y + 1.7,
                           z + entity.getLookAngle().z * 2.0 + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.5, 1.5),
                           (float)(entity.getYRot() + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -40.0, 40.0)),
                           (float)(entity.getXRot() + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -40.0, 40.0))
                        );
                        entityToSpawn.setYBodyRot((float)(entity.getYRot() + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -40.0, 40.0)));
                        entityToSpawn.setYHeadRot((float)(entity.getYRot() + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -40.0, 40.0)));
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  } else if (entity.getPersistentData().getDouble("Atk2e2") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("Atk2e2", 0.0);
                     entity.getPersistentData().putDouble("Atk2eCD", 120.0);
                     SimpleMobsModVariables.MapVariables.get(world).aggro_tails = false;
                     SimpleMobsModVariables.MapVariables.get(world).syncData(world);
                     entity.getPersistentData().putBoolean("Attack2e2", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("Atk2e2", 0.0);
                     entity.getPersistentData().putBoolean("Attack2e2", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack2g")) {
                  if (entity.getPersistentData().getDouble("atk2g") == 0.0) {
                     entity.getPersistentData().putDouble("atk2g", 91.0);
                  } else {
                     entity.getPersistentData().putDouble("atk2g", entity.getPersistentData().getDouble("atk2g") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("atk2g") == 91.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.CHAIN));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.CHAIN));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     entity.getPersistentData().putBoolean("focus", true);
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 91, 7, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("atk2g") <= 69.0 && entity.getPersistentData().getDouble("atk2g") >= 21.0) {
                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new TailDartEntity(SimpleMobsModEntities.TAIL_DART.get(), _level);
                        entityToSpawn.moveTo(
                           x + entity.getLookAngle().x * 1.0,
                           y + 1.6,
                           z + entity.getLookAngle().z * 1.0,
                           entity.getYRot() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3),
                           entity.getXRot() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3)
                        );
                        entityToSpawn.setYBodyRot(entity.getYRot() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3));
                        entityToSpawn.setYHeadRot(entity.getYRot() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3));
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL,
                                    new Vec3(x + entity.getLookAngle().x * 1.0, y + 1.7, z + entity.getLookAngle().z * 1.0),
                                    Vec2.ZERO,
                                    _level,
                                    4,
                                    "",
                                    Component.literal(""),
                                    _level.getServer(),
                                    null
                                 )
                                 .withSuppressedOutput(),
                              "/particle dust 1.000 0.100 0.100 3 ~ ~ ~ 0.1 0.1 0.1 0 10 force"
                           );
                     }
                  } else if (entity.getPersistentData().getDouble("atk2g") == 20.0) {
                     entity.getPersistentData().putBoolean("focus", false);
                  } else if (entity.getPersistentData().getDouble("atk2g") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putDouble("atk2g", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Attack2g", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putDouble("atk2g", 0.0);
                     entity.getPersistentData().putBoolean("Attack2g", false);
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
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.PODZOL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.PODZOL));
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 81, 7, false, false));
                     }

                     entity.getPersistentData().putDouble("xz", entity.getYRot());
                     entity.getPersistentData().putDouble("y", entity.getXRot());
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
                              "/data merge entity @e[type=deepnight:dawn_nine_tails,sort=nearest,limit=1] {Invulnerable:0}"
                           );
                     }

                     entity.getPersistentData().putDouble("Poise", -50.0);
                     entity.getPersistentData().putDouble("stun", 0.0);
                     entity.getPersistentData().putBoolean("Stunned", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Far")
                  && !entity.getPersistentData().getBoolean("Attacking")
                  && entity.getPersistentData().getBoolean("Tails")) {
                  if (Math.random() <= 0.35 && entity.getPersistentData().getDouble("Atk2cCD") == 0.0) {
                     entity.getPersistentData().putBoolean("Attack2c", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (Math.random() >= 0.6 && entity.getPersistentData().getDouble("Atk2eCD") == 0.0) {
                     entity.getPersistentData().putBoolean("Attack2e", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (Math.random() >= 0.6 && entity.getPersistentData().getDouble("Atk2gCD") == 0.0) {
                     entity.getPersistentData().putBoolean("Attack2g", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  }
               }

               if (entity.getPersistentData().getBoolean("Mid") && !entity.getPersistentData().getBoolean("Attacking")) {
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_nine_tails,sort=nearest,limit=1] {Invulnerable:0}"
                        );
                  }

                  if (entity.getPersistentData().getBoolean("Tails")) {
                     if (Math.random() <= 0.3) {
                        entity.getPersistentData().putBoolean("Attack2a", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (Math.random() >= 0.31 && Math.random() <= 0.38) {
                        entity.getPersistentData().putBoolean("Attack2a2", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (Math.random() >= 0.39 && Math.random() <= 0.45 && entity.getPersistentData().getDouble("Atk2eCD") == 0.0) {
                        entity.getPersistentData().putBoolean("Attack2e", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (Math.random() >= 0.71 && entity.getPersistentData().getDouble("Atk2cCD") == 0.0) {
                        entity.getPersistentData().putBoolean("Attack2c", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     }
                  } else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0F)
                        < (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1.0F) * 0.73
                     && !entity.getPersistentData().getBoolean("popped")) {
                     entity.getPersistentData().putBoolean("Tailpop", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  }
               }

               if (entity.getPersistentData().getBoolean("Close2") && !entity.getPersistentData().getBoolean("Attacking")) {
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_nine_tails,sort=nearest,limit=1] {Invulnerable:0}"
                        );
                  }

                  if (entity.getPersistentData().getBoolean("Tails")) {
                     if (Math.random() <= 0.33) {
                        entity.getPersistentData().putBoolean("Attack2b", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (Math.random() >= 0.34 && Math.random() <= 0.66) {
                        entity.getPersistentData().putBoolean("Attack2d", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (Math.random() >= 0.67) {
                        entity.getPersistentData().putBoolean("Attack2f", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     }
                  } else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0F)
                     < (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1.0F) * 0.73) {
                     if (!entity.getPersistentData().getBoolean("popped")) {
                        entity.getPersistentData().putBoolean("Tailpop", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     }
                  } else if (Math.random() <= 0.1) {
                     entity.getPersistentData().putBoolean("Dodge1", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (Math.random() >= 0.9) {
                     entity.getPersistentData().putBoolean("Dodge2", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  }
               }

               if (entity.getPersistentData().getBoolean("Close") && !entity.getPersistentData().getBoolean("Attacking")) {
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_nine_tails,sort=nearest,limit=1] {Invulnerable:0}"
                        );
                  }

                  if (entity.getPersistentData().getBoolean("Tails")) {
                     if (Math.random() <= 0.33) {
                        entity.getPersistentData().putBoolean("Attack2b", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (Math.random() >= 0.34 && Math.random() <= 0.66) {
                        entity.getPersistentData().putBoolean("Attack2d", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (Math.random() >= 0.67) {
                        entity.getPersistentData().putBoolean("Attack2f", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     }
                  } else if (Math.random() <= 0.2) {
                     entity.getPersistentData().putBoolean("Attack1a", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (Math.random() >= 0.21 && Math.random() <= 0.43) {
                     entity.getPersistentData().putBoolean("Attack1b", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (Math.random() >= 0.44 && Math.random() <= 0.65) {
                     entity.getPersistentData().putBoolean("Attack1c", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (Math.random() >= 0.65 && Math.random() <= 0.9) {
                     entity.getPersistentData().putBoolean("Attack1d", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (Math.random() >= 0.91 && Math.random() <= 0.95) {
                     entity.getPersistentData().putBoolean("Dodge1", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (Math.random() >= 0.96) {
                     entity.getPersistentData().putBoolean("Dodge2", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  }
               }
            }
         }
      }
   }
}
