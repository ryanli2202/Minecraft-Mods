package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.HerobrineEntity;
import dev.deepnight.source.dawn.entity.HerobrineFlashEntity;
import dev.deepnight.source.dawn.entity.Herobrinese2Entity;
import dev.deepnight.source.dawn.entity.Herobrinese3Entity;
import dev.deepnight.source.dawn.entity.HerobrineseEntity;
import dev.deepnight.source.dawn.entity.SphereEntity;
import dev.deepnight.source.dawn.entity.SwordThrownEntity;
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

public class ProherobrineProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         double i = 0.0;
         double j = 0.0;
         double k = 0.0;
         if (!world.isClientSide()) {
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

            entity.getPersistentData().putBoolean("Aggro", false);
            entity.getPersistentData().putBoolean("Far", false);
            entity.getPersistentData().putBoolean("Mid", false);
            entity.getPersistentData().putBoolean("Close", false);
            entity.getPersistentData().putDouble("yaw2", 0.0);
            entity.getPersistentData().putDouble("pitch2", 0.0);
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(64.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                  entity.getPersistentData().putBoolean("Aggro", true);
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
                        Math.toDegrees(Math.atan(entity.getPersistentData().getDouble("distance") / (entityiterator.getY() + 1.5 - (entity.getY() + 3.0))))
                     );
                  if (entity.getPersistentData().getDouble("pitch2") > 0.0) {
                     entity.getPersistentData().putDouble("pitch2", entity.getPersistentData().getDouble("pitch2") - 90.0);
                  } else if (entity.getPersistentData().getDouble("pitch2") < 0.0) {
                     entity.getPersistentData().putDouble("pitch2", entity.getPersistentData().getDouble("pitch2") - -90.0);
                  }
               }
            }

            _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(32.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                  entity.getPersistentData().putBoolean("Far", true);
                  entity.getPersistentData().putBoolean("Mid", false);
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
                  entity.getPersistentData().putBoolean("Close", false);
               }
            }

            _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                  entity.getPersistentData().putBoolean("Far", false);
                  entity.getPersistentData().putBoolean("Mid", false);
                  entity.getPersistentData().putBoolean("Close", true);
               }
            }

            if (entity.getPersistentData().getDouble("lavacheck") == 0.0) {
               entity.getPersistentData().putDouble("lavacheck", 27.0);
               entity.getPersistentData().putDouble("i", -3.0);
               entity.getPersistentData().putDouble("j", -3.0);
               entity.getPersistentData().putDouble("k", -3.0);
            } else {
               if (entity.getPersistentData().getDouble("i") <= 3.0) {
                  if (entity.getPersistentData().getDouble("j") <= 3.0) {
                     if (entity.getPersistentData().getDouble("k") <= 3.0) {
                        if (world.getBlockState(
                                 BlockPos.containing(
                                    x + entity.getPersistentData().getDouble("i"),
                                    y + entity.getPersistentData().getDouble("j"),
                                    z + entity.getPersistentData().getDouble("k")
                                 )
                              )
                              .getFluidState().is(net.minecraft.tags.FluidTags.LAVA)) {
                           entity.getPersistentData().putBoolean("lavaaround", true);
                        }

                        entity.getPersistentData().putDouble("k", entity.getPersistentData().getDouble("k") + 1.0);
                     }

                     entity.getPersistentData().putDouble("j", entity.getPersistentData().getDouble("j") + 1.0);
                  }

                  entity.getPersistentData().putDouble("i", entity.getPersistentData().getDouble("i") + 1.0);
               }

               entity.getPersistentData().putDouble("lavacheck", entity.getPersistentData().getDouble("lavacheck") - 1.0);
            }

            if (entity.getPersistentData().getBoolean("lavaaround") && !entity.getPersistentData().getBoolean("Attacking")) {
               entity.getPersistentData().putBoolean("lavapick", true);
               entity.getPersistentData().putBoolean("Attacking", true);
            }

            if (!entity.getPersistentData().getBoolean("Spawned")
               && !entity.getPersistentData().getBoolean("Spawn")
               && !entity.getPersistentData().getBoolean("Attacking")) {
               entity.getPersistentData().putBoolean("Spawned", true);
               entity.getPersistentData().putBoolean("Spawn", true);
               entity.getPersistentData().putBoolean("Attacking", true);
            }

            if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0F)
                  < (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1.0F) * 0.9
               && !entity.getPersistentData().getBoolean("Attacking")) {
               entity.getPersistentData().putBoolean("Blacken", true);
               entity.getPersistentData().putBoolean("Attacking", true);
            }

            if (entity.getPersistentData().getBoolean("Close")) {
               if (!entity.getPersistentData().getBoolean("Attacking") && entity.getPersistentData().getDouble("atk5cd") == 0.0 && Math.random() >= 0.7) {
                  entity.getPersistentData().putBoolean("Attack5", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (!entity.getPersistentData().getBoolean("Attacking") && entity.getPersistentData().getDouble("atk3cd") == 0.0 && Math.random() <= 0.3) {
                  entity.getPersistentData().putBoolean("Attack3", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (!entity.getPersistentData().getBoolean("Attacking")
                  && entity.getPersistentData().getDouble("tntcd") == 0.0
                  && Math.random() <= 0.6
                  && Math.random() >= 0.3) {
                  entity.getPersistentData().putBoolean("TNT", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (!entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack1", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               }
            } else if (entity.getPersistentData().getBoolean("Mid")) {
               if (!entity.getPersistentData().getBoolean("Attacking") && entity.getPersistentData().getDouble("atk2cd") == 0.0 && Math.random() >= 0.7) {
                  entity.getPersistentData().putBoolean("Attack2", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (!entity.getPersistentData().getBoolean("Attacking") && entity.getPersistentData().getDouble("atk6cd") == 0.0 && Math.random() <= 0.3) {
                  entity.getPersistentData().putBoolean("Attack6", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (!entity.getPersistentData().getBoolean("Attacking")
                  && entity.getPersistentData().getDouble("tntcd") == 0.0
                  && Math.random() <= 0.6
                  && Math.random() >= 0.3) {
                  entity.getPersistentData().putBoolean("TNT", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               }
            } else if (entity.getPersistentData().getBoolean("Far")) {
               if (!entity.getPersistentData().getBoolean("Attacking") && entity.getPersistentData().getDouble("atk2cd") == 0.0 && Math.random() >= 0.7) {
                  entity.getPersistentData().putBoolean("Attack2", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (!entity.getPersistentData().getBoolean("Attacking") && entity.getPersistentData().getDouble("atk4cd") == 0.0 && Math.random() <= 0.3) {
                  entity.getPersistentData().putBoolean("Attack4", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (!entity.getPersistentData().getBoolean("Attacking")
                  && entity.getPersistentData().getDouble("tntcd") == 0.0
                  && Math.random() <= 0.6
                  && Math.random() >= 0.3) {
                  entity.getPersistentData().putBoolean("TNT", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               }
            }

            if (entity.getPersistentData().getDouble("tntcd") == 0.0 && entity.getPersistentData().getBoolean("TNT")) {
               entity.getPersistentData().putDouble("tntcd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 180, 260));
            } else if (entity.getPersistentData().getDouble("tntcd") > 0.0) {
               entity.getPersistentData().putDouble("tntcd", entity.getPersistentData().getDouble("tntcd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("atk2cd") == 0.0 && entity.getPersistentData().getBoolean("Attack2")) {
               entity.getPersistentData().putDouble("atk2cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 180, 260));
            } else if (entity.getPersistentData().getDouble("atk2cd") > 0.0) {
               entity.getPersistentData().putDouble("atk2cd", entity.getPersistentData().getDouble("atk2cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("atk3cd") == 0.0 && entity.getPersistentData().getBoolean("Attack3")) {
               entity.getPersistentData().putDouble("atk3cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 180, 260));
            } else if (entity.getPersistentData().getDouble("atk3cd") > 0.0) {
               entity.getPersistentData().putDouble("atk3cd", entity.getPersistentData().getDouble("atk3cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("atk4cd") == 0.0 && entity.getPersistentData().getBoolean("Attack4")) {
               entity.getPersistentData().putDouble("atk4cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 180, 260));
            } else if (entity.getPersistentData().getDouble("atk4cd") > 0.0) {
               entity.getPersistentData().putDouble("atk4cd", entity.getPersistentData().getDouble("atk4cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("atk5cd") == 0.0 && entity.getPersistentData().getBoolean("Attack5")) {
               entity.getPersistentData().putDouble("atk5cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 180, 260));
            } else if (entity.getPersistentData().getDouble("atk5cd") > 0.0) {
               entity.getPersistentData().putDouble("atk5cd", entity.getPersistentData().getDouble("atk5cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("atk6cd") == 0.0 && entity.getPersistentData().getBoolean("Attack6")) {
               entity.getPersistentData().putDouble("atk6cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 180, 260));
            } else if (entity.getPersistentData().getDouble("atk6cd") > 0.0) {
               entity.getPersistentData().putDouble("atk6cd", entity.getPersistentData().getDouble("atk6cd") - 1.0);
            }

            if (entity.getPersistentData().getBoolean("Spawn")) {
               if (entity.getPersistentData().getDouble("spawn") == 0.0) {
                  entity.getPersistentData().putDouble("spawn", 81.0);
               } else {
                  entity.getPersistentData().putDouble("spawn", entity.getPersistentData().getDouble("spawn") - 1.0);
               }

               if (world instanceof ServerLevel _level) {
                  _level.sendParticles(ParticleTypes.SQUID_INK, x, y, z, 3, 2.0, 3.0, 2.0, 0.1);
               }

               if (world instanceof ServerLevel _level) {
                  _level.sendParticles(ParticleTypes.SOUL, x, y, z, 3, 2.0, 3.0, 2.0, 0.1);
               }

               if (entity.getPersistentData().getDouble("spawn") == 81.0) {
                  entity.getPersistentData().putBoolean("noai", true);
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.FEATHER));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.FEATHER));
                  }
               } else if (entity.getPersistentData().getDouble("spawn") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("noai", false);
                  entity.getPersistentData().putDouble("spawn", 0.0);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("Spawn", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Blacken")) {
               if (entity.getPersistentData().getDouble("btimer") == 0.0) {
                  entity.getPersistentData().putDouble("btimer", 91.0);
               } else {
                  entity.getPersistentData().putDouble("btimer", entity.getPersistentData().getDouble("btimer") - 1.0);
               }

               if (entity.getPersistentData().getDouble("btimer") == 91.0) {
                  entity.getPersistentData().putBoolean("noai", true);
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
                           x, y, z, dev.deepnight.SafeSounds.resolve("simple_mobs:poise"), SoundSource.NEUTRAL, 2.0F, 1.0F, false
                        );
                     }
                  }

                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.YELLOW_WOOL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.YELLOW_WOOL));
                  }
               } else if (entity.getPersistentData().getDouble("btimer") == 71.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.wither.hurt"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("entity.wither.hurt"), SoundSource.NEUTRAL, 2.0F, 1.0F, false
                        );
                     }
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.enderman.death"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.enderman.death"),
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
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.wither.spawn"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("entity.wither.spawn"), SoundSource.NEUTRAL, 2.0F, 0.0F, false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("btimer") == 47.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.zombie.break_wooden_door"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.zombie.break_wooden_door"),
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
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.skeleton.hurt"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           2.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("entity.skeleton.hurt"), SoundSource.NEUTRAL, 2.0F, 2.0F, false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("btimer") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  Entity _entityx = entity;
                  if (_entityx instanceof Player _player) {
                     _player.getInventory().armor.set(2, new ItemStack(Items.LEATHER_CHESTPLATE));
                     _player.getInventory().setChanged();
                  } else if (_entityx instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
                  }

                  entity.getPersistentData().putBoolean("noai", false);
                  entity.getPersistentData().putDouble("btimer", 0.0);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("Blacken", false);
                  entity.getPersistentData().putBoolean("Stage2", true);
                  entity.getPersistentData().putBoolean("Blackened", true);
               }

               if (entity.getPersistentData().getDouble("btimer") <= 66.0 && entity.getPersistentData().getDouble("btimer") >= 20.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.SQUID_INK, x, y, z, 2, 0.6, 2.0, 0.6, 0.2);
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.SOUL, x, y, z, 2, 0.6, 2.0, 0.6, 0.2);
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 2, 0.6, 32.0, 0.6, 0.2);
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack1")) {
               if (entity.getPersistentData().getDouble("atk1") == 0.0) {
                  entity.getPersistentData().putDouble("atk1", 31.0);
               } else {
                  entity.getPersistentData().putDouble("atk1", entity.getPersistentData().getDouble("atk1") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk1") == 31.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.LEATHER_HELMET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
               } else if (entity.getPersistentData().getDouble("atk1") == 21.0) {
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putBoolean("lock", true);
               } else if (entity.getPersistentData().getDouble("atk1") == 19.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.SWEEP_ATTACK, x + entity.getLookAngle().x * 1.5, y + 1.0, z + entity.getLookAngle().z * 1.5, 2, 1.0, 0.5, 1.0, 1.0
                     );
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.CRIT, x + entity.getLookAngle().x * 1.5, y + 1.0, z + entity.getLookAngle().z * 1.5, 3, 1.0, 0.5, 1.0, 1.0
                     );
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.crit"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.crit"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F,
                           false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("atk1") == 15.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        entity.getPersistentData().putBoolean("Attack1a", true);
                        entity.getPersistentData().putDouble("atk1", 0.0);
                        entity.getPersistentData().putBoolean("lock", false);
                        entity.getPersistentData().putBoolean("Attack1", false);
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
                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("Attack1", false);
               }

               if (entity.getPersistentData().getDouble("atk1") <= 19.0 && entity.getPersistentData().getDouble("atk1") >= 18.0) {
                  entity.setDeltaMovement(
                     new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 0.2, 0.0, Math.cos(Math.toRadians(entity.getYRot())) * 0.2)
                  );
                  _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.SHORT)
                           .setImpact(3.0F)
                           .setArmorNegation(20.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 25.0F);
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack1a")) {
               if (entity.getPersistentData().getDouble("atk1a") == 0.0) {
                  entity.getPersistentData().putDouble("atk1a", 31.0);
               } else {
                  entity.getPersistentData().putDouble("atk1a", entity.getPersistentData().getDouble("atk1a") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk1a") == 31.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.GOLD_NUGGET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLD_NUGGET));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
               } else if (entity.getPersistentData().getDouble("atk1a") == 21.0) {
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putBoolean("lock", true);
               } else if (entity.getPersistentData().getDouble("atk1a") == 19.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.crit"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.crit"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.SWEEP_ATTACK, x + entity.getLookAngle().x * 1.5, y + 1.0, z + entity.getLookAngle().z * 1.5, 2, 1.0, 0.5, 1.0, 1.0
                     );
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.CRIT, x + entity.getLookAngle().x * 1.5, y + 1.0, z + entity.getLookAngle().z * 1.5, 3, 1.0, 0.5, 1.0, 1.0
                     );
                  }
               } else if (entity.getPersistentData().getDouble("atk1a") == 13.0) {
                  if (Math.random() >= 0.5) {
                     _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                           entity.getPersistentData().putBoolean("Attack1", true);
                           entity.getPersistentData().putBoolean("Attack1a", false);
                           entity.getPersistentData().putBoolean("lock", false);
                           entity.getPersistentData().putDouble("atk1a", 0.0);
                        }
                     }
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
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("Attack1a", false);
               }

               if (entity.getPersistentData().getDouble("atk1a") <= 19.0 && entity.getPersistentData().getDouble("atk1a") >= 18.0) {
                  entity.setDeltaMovement(
                     new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 0.2, 0.0, Math.cos(Math.toRadians(entity.getYRot())) * 0.2)
                  );
                  _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(3.0F)
                           .setArmorNegation(20.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 25.0F);
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack2")) {
               if (entity.getPersistentData().getDouble("atk2") == 0.0) {
                  entity.getPersistentData().putDouble("atk2", 46.0);
               } else {
                  entity.getPersistentData().putDouble("atk2", entity.getPersistentData().getDouble("atk2") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk2") == 46.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.COAL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.COAL));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 15, 4, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("atk2") == 31.0) {
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new HerobrineFlashEntity(SimpleMobsModEntities.HEROBRINE_FLASH.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               } else if (entity.getPersistentData().getDouble("atk2") == 29.0) {
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putBoolean("lock", true);
                  entity.getPersistentData().putBoolean("noai", true);
               } else if (entity.getPersistentData().getDouble("atk2") == 24.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ogre_attack3"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ogre_attack3"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new Herobrinese2Entity(SimpleMobsModEntities.HEROBRINESE_2.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
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
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("noai", false);
                  entity.getPersistentData().putBoolean("Attack2", false);
               }

               if (entity.getPersistentData().getDouble("atk2") <= 31.0 && entity.getPersistentData().getDouble("atk2") >= 27.0) {
                  entity.setDeltaMovement(
                     new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 1.0, 0.0, Math.cos(Math.toRadians(entity.getYRot())) * 1.0)
                  );
                  _center = new Vec3(x + entity.getLookAngle().x * 1.0, y, z + entity.getLookAngle().z * 1.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof HerobrineEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(3.0F)
                           .setArmorNegation(20.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 30.0F);
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack3")) {
               if (entity.getPersistentData().getDouble("atk3") == 0.0) {
                  entity.getPersistentData().putDouble("atk3", 81.0);
               } else {
                  entity.getPersistentData().putDouble("atk3", entity.getPersistentData().getDouble("atk3") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk3") == 81.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.CHARCOAL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHARCOAL));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 5, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("atk3") == 66.0
                  || entity.getPersistentData().getDouble("atk3") == 53.0
                  || entity.getPersistentData().getDouble("atk3") == 27.0) {
                  entity.getPersistentData().putBoolean("focus", true);
                  entity.getPersistentData().putBoolean("lock", false);
               } else if (entity.getPersistentData().getDouble("atk3") == 31.0) {
                  if (entity.getPersistentData().getBoolean("Mid")) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.IRON_INGOT));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_INGOT));
                     }

                     entity.getPersistentData().putDouble("atk3", 0.0);
                     entity.getPersistentData().putBoolean("Attack3a", true);
                     entity.getPersistentData().putBoolean("Attack3", false);
                  }
               } else if (entity.getPersistentData().getDouble("atk3") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putDouble("atk3", 0.0);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("Attack3", false);
               }

               if (entity.getPersistentData().getDouble("atk3") == 69.0
                  || entity.getPersistentData().getDouble("atk3") == 56.0
                  || entity.getPersistentData().getDouble("atk3") == 30.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.crit"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.5F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.crit"),
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
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.crit"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.3F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.crit"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.3F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new HerobrineseEntity(SimpleMobsModEntities.HEROBRINESE.get(), _level);
                     entityToSpawn.moveTo(x + entity.getLookAngle().x * 1.5, y + 1.0, z + entity.getLookAngle().z * 1.5, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putBoolean("lock", true);
                  _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof HerobrineEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.SHORT)
                           .setImpact(3.0F)
                           .setArmorNegation(20.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 30.0F);
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack3a")) {
               if (entity.getPersistentData().getDouble("atk3a") == 0.0) {
                  entity.getPersistentData().putDouble("atk3a", 42.0);
               } else {
                  entity.getPersistentData().putDouble("atk3a", entity.getPersistentData().getDouble("atk3a") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk3a") == 42.0) {
                  entity.getPersistentData().putBoolean("focus", true);
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5, 5, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("atk3a") == 37.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 14, 5, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("atk3a") == 26.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
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
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ogre_attack3"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ogre_attack3"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new Herobrinese3Entity(SimpleMobsModEntities.HEROBRINESE_3.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               } else if (entity.getPersistentData().getDouble("atk3a") == 23.0) {
                  entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putBoolean("lock", true);
                  entity.getPersistentData().putBoolean("noai", true);
               } else if (entity.getPersistentData().getDouble("atk3a") == 2.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.RED_WOOL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.RED_WOOL));
                  }
               } else if (entity.getPersistentData().getDouble("atk3a") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putDouble("atk3a", 0.0);
                  entity.getPersistentData().putBoolean("noai", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("Attack3a", false);
               }

               if (entity.getPersistentData().getDouble("atk3a") <= 27.0 && entity.getPersistentData().getDouble("atk3a") >= 22.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof HerobrineEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(3.0F)
                           .setArmorNegation(20.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 30.0F);
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack4")) {
               if (entity.getPersistentData().getDouble("atk4") == 0.0) {
                  entity.getPersistentData().putDouble("atk4", 81.0);
               } else {
                  entity.getPersistentData().putDouble("atk4", entity.getPersistentData().getDouble("atk4") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk4") == 81.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.IRON_NUGGET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_NUGGET));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 0, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("atk4") == 68.0) {
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new SwordThrownEntity(SimpleMobsModEntities.SWORD_THROWN.get(), _level);
                     entityToSpawn.moveTo(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z, entity.getYRot(), 0.0F);
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
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_punch"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.5F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_punch"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.5F,
                           false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("atk4") == 56.0) {
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new SwordThrownEntity(SimpleMobsModEntities.SWORD_THROWN.get(), _level);
                     entityToSpawn.moveTo(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z, entity.getYRot(), 0.0F);
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
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_punch"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.5F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_punch"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.5F,
                           false
                        );
                     }
                  }

                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 56, 5, false, false));
                  }

                  entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putBoolean("lock", true);
                  entity.getPersistentData().putBoolean("noai", true);
               } else if (entity.getPersistentData().getDouble("atk4") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putDouble("atk4", 0.0);
                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("noai", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("Attack4", false);
               }

               if (entity.getPersistentData().getDouble("atk4") <= 24.0 && entity.getPersistentData().getDouble("atk4") >= 18.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("block.fire.extinguish"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           0.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("block.fire.extinguish"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           0.0F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.SQUID_INK, x, y, z, 2, 1.0, 2.0, 1.0, 0.1);
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack5")) {
               if (entity.getPersistentData().getDouble("atk5") == 0.0) {
                  entity.getPersistentData().putDouble("atk5", 46.0);
               } else {
                  entity.getPersistentData().putDouble("atk5", entity.getPersistentData().getDouble("atk5") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk5") == 46.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.WHITE_WOOL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.WHITE_WOOL));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("atk5") == 25.0) {
                  entity.getPersistentData().putBoolean("slash", true);
                  entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putBoolean("lock", true);
                  entity.getPersistentData().putBoolean("noai", true);
               } else if (entity.getPersistentData().getDouble("atk5") == 17.0) {
                  entity.getPersistentData().putBoolean("slash", false);
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 17, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("atk5") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putDouble("atk5", 0.0);
                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("noai", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("Attack5", false);
               }
            }

            if (entity.getPersistentData().getBoolean("slash")) {
               if (entity.getYRot() <= 90.0F) {
                  entity.getPersistentData().putDouble("circle_direction", 8.0);
               } else if (entity.getYRot() > 90.0F && entity.getYRot() < 180.0F) {
                  entity.getPersistentData().putDouble("circle_direction", 16.0);
               } else if (entity.getYRot() >= 270.0F) {
                  entity.getPersistentData().putDouble("circle_direction", 0.0);
               } else if (entity.getYRot() >= 180.0F && entity.getYRot() < 270.0F) {
                  entity.getPersistentData().putDouble("circle_direction", 24.0);
               }

               if (entity.getPersistentData().getDouble("loop") < 8.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL,
                                 new Vec3(
                                    x
                                       + 0.0
                                       + Math.cos(
                                             (Math.PI / 16)
                                                * (entity.getPersistentData().getDouble("loop") + entity.getPersistentData().getDouble("circle_direction"))
                                          )
                                          * 3.5,
                                    y + entity.getPersistentData().getDouble("loop") * 0.35,
                                    z
                                       + 0.0
                                       + Math.sin(
                                             (Math.PI / 16)
                                                * (entity.getPersistentData().getDouble("loop") + entity.getPersistentData().getDouble("circle_direction"))
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
                           "/particle dust_color_transition 0.000 0.000 0.000 4 0.5412 0.5412 0.5412 ~ ~ ~ 0.1 0.1 0.1 0 1 force"
                        );
                  }

                  if (entity.getPersistentData().getDouble("loop") == 7.0) {
                     entity.getPersistentData().putDouble("loop", 0.0);
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("entity.firework_rocket.large_blast"),
                              SoundSource.NEUTRAL,
                              4.0F,
                              -2.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("entity.firework_rocket.large_blast"),
                              SoundSource.NEUTRAL,
                              4.0F,
                              -2.0F,
                              false
                           );
                        }
                     }
                  } else {
                     entity.getPersistentData().putDouble("loop", entity.getPersistentData().getDouble("loop") + 1.0);
                  }
               }

               if (entity.getPersistentData().getDouble("loop") < 8.0 && world instanceof ServerLevel _level) {
                  _level.getServer()
                     .getCommands()
                     .performPrefixedCommand(
                        new CommandSourceStack(
                              CommandSource.NULL,
                              new Vec3(
                                 x
                                    + 0.0
                                    + Math.cos(
                                          (Math.PI / 16)
                                             * (entity.getPersistentData().getDouble("loop") + entity.getPersistentData().getDouble("circle_direction"))
                                       )
                                       * 3.2,
                                 y + (7.0 - entity.getPersistentData().getDouble("loop")) * 0.35,
                                 z
                                    + 0.0
                                    + Math.sin(
                                          (Math.PI / 16)
                                             * (entity.getPersistentData().getDouble("loop") + entity.getPersistentData().getDouble("circle_direction"))
                                       )
                                       * 3.0
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
                        "/particle dust_color_transition 1.000 1.000 1.000 4 0.5412 0.5412 0.5412 ~ ~ ~ 0.1 0.1 0.1 0 1 force"
                     );
               }

               _center = new Vec3(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (!(entityiterator instanceof HerobrineEntity)) {
                     entityiterator.hurt(new DamageSource(entityiterator.level().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(net.minecraft.world.damagesource.DamageTypes.GENERIC), entity), 16.0F);
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ()),
                              dev.deepnight.SafeSounds.resolve("entity.player.attack.crit"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              0.0F
                           );
                        } else {
                           _level.playLocalSound(
                              entityiterator.getX(),
                              entityiterator.getY(),
                              entityiterator.getZ(),
                              dev.deepnight.SafeSounds.resolve("entity.player.attack.crit"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              0.0F,
                              false
                           );
                        }
                     }

                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ()),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:spell_form"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              0.0F
                           );
                        } else {
                           _level.playLocalSound(
                              entityiterator.getX(),
                              entityiterator.getY(),
                              entityiterator.getZ(),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:spell_form"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              0.0F,
                              false
                           );
                        }
                     }

                     entityiterator.setDeltaMovement(
                        new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 1.35, 0.7, Math.cos(Math.toRadians(entity.getYRot())) * 1.35)
                     );
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack6")) {
               if (entity.getPersistentData().getDouble("atk6") == 0.0) {
                  entity.getPersistentData().putDouble("atk6", 66.0);
               } else {
                  entity.getPersistentData().putDouble("atk6", entity.getPersistentData().getDouble("atk6") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk6") == 66.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.WHITE_CONCRETE));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.WHITE_CONCRETE));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 66, 10, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("atk6") == 61.0) {
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new SphereEntity(SimpleMobsModEntities.SPHERE.get(), _level);
                     entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
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
                           dev.deepnight.SafeSounds.resolve("simple_mobs:electricity_ambient"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           2.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:electricity_ambient"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           2.0F,
                           false
                        );
                     }
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ogre_attack3"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ogre_attack3"),
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
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.wither.shoot"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("entity.wither.shoot"), SoundSource.NEUTRAL, 2.0F, 1.0F, false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("atk6") == 28.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.5F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.5F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new HerobrineseEntity(SimpleMobsModEntities.HEROBRINESE.get(), _level);
                     entityToSpawn.moveTo(x + entity.getLookAngle().x * 2.0, y + 1.0, z + entity.getLookAngle().z * 2.0, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               } else if (entity.getPersistentData().getDouble("atk6") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putDouble("atk6", 0.0);
                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("Attack6", false);
               }

               if (entity.getPersistentData().getDouble("atk6") >= 27.0 && entity.getPersistentData().getDouble("atk6") <= 29.0) {
                  entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putBoolean("lock", true);
                  _center = new Vec3(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof HerobrineEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(3.0F)
                           .setArmorNegation(20.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 30.0F);
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("lavapick")) {
               if (entity.getPersistentData().getDouble("lp") == 0.0) {
                  entity.getPersistentData().putDouble("lp", 26.0);
               } else {
                  entity.getPersistentData().putDouble("lp", entity.getPersistentData().getDouble("lp") - 1.0);
               }

               if (entity.getPersistentData().getDouble("lp") == 26.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.RED_CONCRETE));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.RED_CONCRETE));
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 26, 5, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("lp") != 10.0) {
                  if (entity.getPersistentData().getDouble("lp") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("lavaaround", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("lp", 0.0);
                     entity.getPersistentData().putBoolean("lavapick", false);
                  }
               } else {
                  i = -3.0;
                  j = -3.0;
                  k = -3.0;

                  while (i <= 3.0) {
                     while (j <= 3.0) {
                        for (; k <= 3.0; k++) {
                           if (world.getBlockState(BlockPos.containing(x + i, y + j, z + k)).getFluidState().is(net.minecraft.tags.FluidTags.LAVA)) {
                              world.setBlock(BlockPos.containing(x + i, y + j, z + k), Blocks.AIR.defaultBlockState(), 3);
                              entity.getPersistentData().putBoolean("haslava", true);
                           }
                        }

                        k = -3.0;
                        j++;
                     }

                     j = -3.0;
                     i++;
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("TNT")) {
               if (entity.getPersistentData().getDouble("tnt") == 0.0) {
                  entity.getPersistentData().putDouble("tnt", 26.0);
               } else {
                  entity.getPersistentData().putDouble("tnt", entity.getPersistentData().getDouble("tnt") - 1.0);
               }

               if (entity.getPersistentData().getDouble("tnt") == 26.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.YELLOW_CONCRETE));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.YELLOW_CONCRETE));
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 26, 5, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("tnt") == 9.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL,
                                 new Vec3(x + entity.getLookAngle().x, y + 1.0, z + entity.getLookAngle().z),
                                 Vec2.ZERO,
                                 _level,
                                 4,
                                 "",
                                 Component.literal(""),
                                 _level.getServer(),
                                 null
                              )
                              .withSuppressedOutput(),
                           "/summon minecraft:tnt ~ ~ ~ {Fuse:20}"
                        );
                  }
               } else if (entity.getPersistentData().getDouble("tnt") == 8.0) {
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new HerobrineFlashEntity(SimpleMobsModEntities.HEROBRINE_FLASH.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  Entity _ent = entity;
                  _ent.teleportTo(x + Mth.nextInt(net.minecraft.util.RandomSource.create(), -10, 10), y, z + Mth.nextInt(net.minecraft.util.RandomSource.create(), -10, 10));
                  if (_ent instanceof ServerPlayer _serverPlayer) {
                     _serverPlayer.connection
                        .teleport(x + Mth.nextInt(net.minecraft.util.RandomSource.create(), -10, 10), y, z + Mth.nextInt(net.minecraft.util.RandomSource.create(), -10, 10), _ent.getYRot(), _ent.getXRot());
                  }
               } else if (entity.getPersistentData().getDouble("tnt") == 6.0) {
                  i = -1.0;
                  j = 1.0;
                  k = -1.0;

                  while (i <= 1.0) {
                     while (j <= 3.0) {
                        for (; k <= 1.0; k++) {
                           if (world.getBlockState(BlockPos.containing(x + i, y + j, z + k)).canOcclude()) {
                              world.setBlock(BlockPos.containing(x + i, y + j, z + k), Blocks.AIR.defaultBlockState(), 3);
                           }
                        }

                        k = -1.0;
                        j++;
                     }

                     j = 1.0;
                     i++;
                  }
               } else if (entity.getPersistentData().getDouble("tnt") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("tnt", 0.0);
                  entity.getPersistentData().putBoolean("TNT", false);
               }
            }
         }
      }
   }
}
