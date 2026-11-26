package dev.deepnight.source.dawn.procedures;

import java.util.Random;
import dev.deepnight.source.dawn.entity.Korok5BalloonEntity;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
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

public class Prokorok5Procedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (!world.isClientSide() && entity.getPersistentData().getBoolean("awake") && !entity.getPersistentData().getBoolean("dance")) {
            if (world.getEntitiesOfClass(Player.class, AABB.ofSize(new Vec3(x, y, z), 50.0, 50.0, 50.0), e -> true).isEmpty()
               && world.getEntitiesOfClass(ServerPlayer.class, AABB.ofSize(new Vec3(x, y, z), 50.0, 50.0, 50.0), e -> true).isEmpty()) {
               entity.getPersistentData().putBoolean("hurt", true);
            }

            if (entity.getPersistentData().getBoolean("wake")) {
               entity.fallDistance = 0.0F;
               if (entity.getPersistentData().getDouble("wakecd") == 0.0) {
                  entity.getPersistentData().putDouble("wakecd", 15.0);
               } else {
                  entity.getPersistentData().putDouble("wakecd", entity.getPersistentData().getDouble("wakecd") - 1.0);
               }

               if (entity.getPersistentData().getDouble("wakecd") == 15.0) {
                  entity.setDeltaMovement(new Vec3(Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.5, 1.5), 0.45, Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.5, 1.5)));
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.LEATHER_HELMET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                  }

                  entity.setNoGravity(true);
                  entity.getPersistentData().putBoolean("noai", false);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (entity.getPersistentData().getDouble("wakecd") == 1.0) {
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new Korok5BalloonEntity(SimpleMobsModEntities.KOROK_5_BALLOON.get(), _level);
                     entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  entity.getPersistentData().putDouble("wakecd", 0.0);
                  entity.getPersistentData().putBoolean("fly", true);
                  entity.getPersistentData().putBoolean("wake", false);
               }

               if (entity.getPersistentData().getBoolean("hurt")) {
                  entity.getPersistentData().putDouble("wakecd", 0.0);
                  entity.getPersistentData().putBoolean("noai", true);
                  entity.getPersistentData().putBoolean("wake", false);
               }
            }

            if (entity.getPersistentData().getBoolean("fly")) {
               if (entity.getPersistentData().getDouble("flycd") == 0.0) {
                  entity.getPersistentData().putDouble("flycd", 300.0);
                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.SNOWYCLOUD.get(), x, y + 1.0, z, 4, 0.4, 0.4, 0.4, 0.1);
                  }
               } else {
                  entity.getPersistentData().putDouble("flycd", entity.getPersistentData().getDouble("flycd") - 1.0);
               }

               entity.fallDistance = 0.0F;
               if (Math.random() >= 0.95) {
                  entity.setDeltaMovement(
                     new Vec3(Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.16, 0.16), Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.01, 0.05), Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.16, 0.16))
                  );
               }

               if (world.getEntitiesOfClass(Korok5BalloonEntity.class, AABB.ofSize(new Vec3(x, y, z), 5.0, 5.0, 5.0), e -> true).isEmpty()) {
                  entity.setNoGravity(false);
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.GOLD_INGOT));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLD_INGOT));
                  }

                  entity.getPersistentData().putDouble("flycd", 0.0);
                  entity.getPersistentData().putBoolean("noai", true);
                  entity.getPersistentData().putBoolean("fall", true);
                  entity.getPersistentData().putBoolean("fly", false);
               }

               if (entity.getPersistentData().getDouble("flycd") == 1.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.SNOWYCLOUD.get(), x, y + 1.0, z, 4, 0.4, 0.4, 0.4, 0.1);
                  }

                  entity.getPersistentData().putDouble("flycd", 0.0);
                  entity.getPersistentData().putBoolean("noai", true);
                  entity.getPersistentData().putBoolean("hurt", true);
                  entity.getPersistentData().putBoolean("fly", false);
               }

               if (entity.getPersistentData().getBoolean("hurt")) {
                  entity.getPersistentData().putDouble("flycd", 0.0);
                  entity.getPersistentData().putBoolean("noai", true);
                  entity.getPersistentData().putBoolean("fly", false);
               }
            }

            if (entity.getPersistentData().getBoolean("fall")) {
               entity.fallDistance = 0.0F;
               if (entity.onGround()) {
                  entity.getPersistentData().putDouble("fallcd", 0.0);
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.experience_orb.pickup"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.experience_orb.pickup"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.HAPPY_VILLAGER, x, y + 1.0, z, 5, 0.4, 0.4, 0.4, 0.1);
                  }

                  entity.getPersistentData().putBoolean("disappear", true);
                  entity.getPersistentData().putBoolean("fall", false);
               }

               if (entity.getPersistentData().getDouble("fallcd") == 0.0) {
                  entity.getPersistentData().putDouble("fallcd", 51.0);
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(2, new ItemStack(Items.LEATHER_CHESTPLATE));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
                  }
               } else {
                  entity.getPersistentData().putDouble("fallcd", entity.getPersistentData().getDouble("fallcd") - 1.0);
               }

               if (entity.getPersistentData().getDouble("fallcd") == 1.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.experience_orb.pickup"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.experience_orb.pickup"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  entity.getPersistentData().putDouble("fallcd", 0.0);
                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.HAPPY_VILLAGER, x, y + 1.0, z, 5, 0.4, 0.4, 0.4, 0.1);
                  }

                  entity.getPersistentData().putBoolean("disappear", true);
                  entity.getPersistentData().putBoolean("fall", false);
               }
            }

            if (entity.getPersistentData().getBoolean("disappear")) {
               if (entity.getPersistentData().getDouble("disappearcd") == 0.0) {
                  entity.getPersistentData().putDouble("disappearcd", 53.0);
               } else {
                  entity.getPersistentData().putDouble("disappearcd", entity.getPersistentData().getDouble("disappearcd") - 1.0);
               }

               if (entity.getPersistentData().getDouble("disappearcd") == 53.0) {
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (entity.getPersistentData().getDouble("disappearcd") == 43.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.COAL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.COAL));
                  }
               } else if (entity.getPersistentData().getDouble("disappearcd") == 23.0) {
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

            if (entity.getPersistentData().getBoolean("hurt")) {
               if (entity.getPersistentData().getDouble("hurtcd") == 0.0) {
                  entity.getPersistentData().putDouble("hurtcd", 33.0);
               } else {
                  entity.getPersistentData().putDouble("hurtcd", entity.getPersistentData().getDouble("hurtcd") - 1.0);
               }

               if (entity.getPersistentData().getDouble("hurtcd") == 33.0) {
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
               } else if (entity.getPersistentData().getDouble("hurtcd") == 1.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.item.pickup"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.5F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("entity.item.pickup"), SoundSource.NEUTRAL, 2.0F, 0.5F, false
                        );
                     }
                  }

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
