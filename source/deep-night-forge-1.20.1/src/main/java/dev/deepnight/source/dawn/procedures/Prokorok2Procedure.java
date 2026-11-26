package dev.deepnight.source.dawn.procedures;

import java.util.Random;
import dev.deepnight.source.dawn.init.SimpleMobsModParticleTypes;
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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class Prokorok2Procedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         double i = 0.0;
         double j = 0.0;
         double k = 0.0;
         boolean empty = false;
         if (!world.isClientSide() && entity.getPersistentData().getBoolean("awake") && !entity.getPersistentData().getBoolean("dance")) {
            if (world.getEntitiesOfClass(Player.class, AABB.ofSize(new Vec3(x, y, z), 32.0, 32.0, 32.0), e -> true).isEmpty()
               && world.getEntitiesOfClass(ServerPlayer.class, AABB.ofSize(new Vec3(x, y, z), 32.0, 32.0, 32.0), e -> true).isEmpty()) {
               entity.getPersistentData().putBoolean("hurt", true);
            }

            if (entity.getPersistentData().getBoolean("wake")) {
               if (entity.getPersistentData().getDouble("wakecd") == 0.0) {
                  entity.getPersistentData().putDouble("wakecd", 10.0);
               } else {
                  entity.getPersistentData().putDouble("wakecd", entity.getPersistentData().getDouble("wakecd") - 1.0);
               }

               if (entity.getPersistentData().getDouble("wakecd") == 10.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.GOLD_NUGGET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLD_NUGGET));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("noai", true);
               } else if (entity.getPersistentData().getDouble("wakecd") == 4.0) {
                  if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 20, 1, false, false));
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.SNOWYCLOUD.get(), x, y, z, 4, 0.5, 0.5, 0.5, 0.3);
                  }
               } else if (entity.getPersistentData().getDouble("wakecd") == 1.0) {
                  empty = false;

                  while (!empty) {
                     i = Mth.nextInt(net.minecraft.util.RandomSource.create(), -10, 10);
                     j = Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3);
                     k = Mth.nextInt(net.minecraft.util.RandomSource.create(), -10, 10);
                     if (!world.getBlockState(BlockPos.containing(x + i, y + j, z + k)).canOcclude()
                        && world.getBlockState(BlockPos.containing(x + i, y + j - 1.0, z + k)).canOcclude()) {
                        Entity _ent = entity;
                        _ent.teleportTo(x + i, y + j, z + k);
                        if (_ent instanceof ServerPlayer _serverPlayer) {
                           _serverPlayer.connection.teleport(x + i, y + j, z + k, _ent.getYRot(), _ent.getXRot());
                        }

                        empty = true;
                     }
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("wakecd", 0.0);
                  entity.getPersistentData().putBoolean("wake", false);
               }

               if (entity.getPersistentData().getBoolean("hurt")) {
                  entity.getPersistentData().putDouble("wakecd", 0.0);
                  entity.getPersistentData().putBoolean("wake", false);
               }
            }

            if (entity.getPersistentData().getBoolean("tag")) {
               entity.getPersistentData().putDouble("tag_count", entity.getPersistentData().getDouble("tag_count") + 1.0);
               if (entity.getPersistentData().getDouble("tag_count") >= 4.0) {
                  entity.getPersistentData().putBoolean("disappear", true);
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.LEATHER_HELMET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.experience_orb.pickup"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           0.8F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.experience_orb.pickup"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           0.8F,
                           false
                        );
                     }
                  }
               } else {
                  entity.getPersistentData().putBoolean("move", true);
                  if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 20, 1, false, false));
                  }
               }

               entity.getPersistentData().putBoolean("tag", false);
            }

            if (entity.getPersistentData().getBoolean("disappear")) {
               if (entity.getPersistentData().getDouble("disappearcd") == 0.0) {
                  entity.getPersistentData().putDouble("disappearcd", 37.0);
               } else {
                  entity.getPersistentData().putDouble("disappearcd", entity.getPersistentData().getDouble("disappearcd") - 1.0);
               }

               if (entity.getPersistentData().getDouble("disappearcd") == 37.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.HAPPY_VILLAGER, x, y + 1.0, z, 4, 0.4, 0.4, 0.4, 0.1);
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("noai", false);
               } else if (entity.getPersistentData().getDouble("disappearcd") == 20.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.CHARCOAL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHARCOAL));
                  }
               } else if (entity.getPersistentData().getDouble("disappearcd") == 1.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.player.levelup"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           2.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.player.levelup"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           2.0F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.SNOWYCLOUD.get(), x, y + 1.0, z, 4, 0.4, 0.4, 0.4, 0.1);
                  }

                  if (Math.random() > 0.5) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/summon item ~ ~2 ~ {Item:{id:\"paraglider:stamina_vessel\",Count:1}}"
                           );
                     }
                  } else if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/summon item ~ ~2 ~ {Item:{id:\"paraglider:heart_container\",Count:1}}"
                        );
                  }

                  if (!entity.level().isClientSide()) {
                     entity.discard();
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("move")) {
               if (entity.getPersistentData().getDouble("movecd") == 0.0) {
                  entity.getPersistentData().putDouble("movecd", 21.0);
               } else {
                  entity.getPersistentData().putDouble("movecd", entity.getPersistentData().getDouble("movecd") - 1.0);
               }

               entity.fallDistance = 0.0F;
               if (entity.getPersistentData().getDouble("movecd") == 21.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.GOLD_INGOT));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLD_INGOT));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.experience_orb.pickup"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           0.8F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.experience_orb.pickup"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           0.8F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.HAPPY_VILLAGER, x, y + 1.0, z, 10, 0.4, 0.4, 0.4, 0.1);
                  }
               } else if (entity.getPersistentData().getDouble("movecd") == 15.0) {
                  if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 20, 1, false, false));
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.SNOWYCLOUD.get(), x, y + 1.0, z, 4, 0.4, 0.4, 0.4, 0.1);
                  }
               } else if (entity.getPersistentData().getDouble("movecd") == 1.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.item.pickup"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("entity.item.pickup"), SoundSource.NEUTRAL, 3.0F, 1.0F, false
                        );
                     }
                  }

                  empty = false;

                  while (!empty) {
                     i = Mth.nextInt(net.minecraft.util.RandomSource.create(), -10, 10);
                     j = Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3);
                     k = Mth.nextInt(net.minecraft.util.RandomSource.create(), -10, 10);
                     if (!world.getBlockState(BlockPos.containing(x + i, y + j, z + k)).canOcclude()
                        && world.getBlockState(BlockPos.containing(x + i, y + j - 1.0, z + k)).canOcclude()) {
                        Entity _ent = entity;
                        _ent.teleportTo(x + i, y + j, z + k);
                        if (_ent instanceof ServerPlayer _serverPlayer) {
                           _serverPlayer.connection.teleport(x + i, y + j, z + k, _ent.getYRot(), _ent.getXRot());
                        }

                        empty = true;
                     }
                  }

                  entity.getPersistentData().putDouble("movecd", 0.0);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("move", false);
               }

               if (entity.getPersistentData().getBoolean("hurt")) {
                  entity.getPersistentData().putDouble("movecd", 0.0);
                  entity.getPersistentData().putBoolean("flee", false);
                  entity.getPersistentData().putBoolean("move", false);
               }
            }

            if (entity.getPersistentData().getBoolean("hurt")) {
               if (entity.getPersistentData().getDouble("hurtcd") == 0.0) {
                  entity.getPersistentData().putDouble("hurtcd", 58.0);
               } else {
                  entity.getPersistentData().putDouble("hurtcd", entity.getPersistentData().getDouble("hurtcd") - 1.0);
               }

               if (entity.getPersistentData().getDouble("hurtcd") == 58.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("item.trident.return"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           2.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("item.trident.return"), SoundSource.NEUTRAL, 1.0F, 2.0F, false
                        );
                     }
                  }

                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.IRON_INGOT));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_INGOT));
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.ANGRY_VILLAGER, x, y + 0.5, z, 5, 0.4, 0.4, 0.4, 0.8);
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("noai", false);
               } else if (entity.getPersistentData().getDouble("hurtcd") == 20.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.CHARCOAL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHARCOAL));
                  }
               } else if (entity.getPersistentData().getDouble("hurtcd") == 1.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.SNOWYCLOUD.get(), x, y + 1.0, z, 4, 0.4, 0.4, 0.4, 0.1);
                  }

                  if (!entity.level().isClientSide()) {
                     entity.discard();
                  }
               }
            }
         }
      }
   }
}
