package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
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

public class Prokorok4Procedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         boolean empty = false;
         double i = 0.0;
         double j = 0.0;
         double k = 0.0;
         double xRadius = 0.0;
         double loop = 0.0;
         double zRadius = 0.0;
         double particleAmount = 0.0;
         if (!world.isClientSide() && entity.getPersistentData().getBoolean("awake") && !entity.getPersistentData().getBoolean("dance")) {
            if (world.getEntitiesOfClass(Player.class, AABB.ofSize(new Vec3(x, y, z), 16.0, 16.0, 16.0), e -> true).isEmpty()
               && world.getEntitiesOfClass(ServerPlayer.class, AABB.ofSize(new Vec3(x, y, z), 16.0, 16.0, 16.0), e -> true).isEmpty()) {
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
                     _player.getInventory().armor.set(3, new ItemStack(Items.LEATHER_HELMET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                  }

                  _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(2, new ItemStack(Items.LEATHER_CHESTPLATE));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("noai", true);
               } else if (entity.getPersistentData().getDouble("wakecd") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.IRON_NUGGET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_NUGGET));
                  }

                  entity.getPersistentData().putBoolean("chant", true);
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
               if (entity.getPersistentData().getDouble("tagcd") == 0.0) {
                  entity.getPersistentData().putDouble("tagcd", 40.0);
                  entity.getPersistentData().putBoolean("chant", false);
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.HAPPY_VILLAGER, x, y, z, 5, 0.4, 0.4, 0.4, 0.4);
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.experience_orb.pickup"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.8F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.experience_orb.pickup"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.8F,
                           false
                        );
                     }
                  }

                  entity.getPersistentData().putDouble("tag_count", entity.getPersistentData().getDouble("tag_count") + 1.0);
                  if (entity.getPersistentData().getDouble("tag_count") >= 4.0) {
                     entity.getPersistentData().putBoolean("disappear", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                     entity.getPersistentData().putBoolean("chant", false);
                     _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.LEATHER_HELMET));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                     }

                     entity.getPersistentData().putBoolean("tag", false);
                  }
               } else {
                  entity.getPersistentData().putDouble("tagcd", entity.getPersistentData().getDouble("tagcd") - 1.0);
               }

               if (entity.getPersistentData().getDouble("tagcd") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.IRON_NUGGET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_NUGGET));
                  }

                  entity.getPersistentData().putDouble("tagcd", 0.0);
                  entity.getPersistentData().putBoolean("chant", true);
                  entity.getPersistentData().putBoolean("tag", false);
               }
            }

            if (entity.getPersistentData().getBoolean("wrong_tag")) {
               if (entity.getPersistentData().getDouble("tagcd") == 0.0) {
                  entity.getPersistentData().putDouble("tagcd", 40.0);
                  entity.getPersistentData().putBoolean("chant", false);
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.ANGRY_VILLAGER, x, y, z, 5, 0.4, 0.4, 0.4, 0.4);
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("block.bell.use"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("block.bell.use"), SoundSource.NEUTRAL, 2.0F, 0.0F, false
                        );
                     }
                  }

                  entity.getPersistentData().putDouble("wtag_count", entity.getPersistentData().getDouble("wtag_count") + 1.0);
                  if (entity.getPersistentData().getDouble("wtag_count") >= 2.0) {
                     entity.getPersistentData().putBoolean("hurt", true);
                     entity.getPersistentData().putBoolean("chant", false);
                     entity.getPersistentData().putBoolean("wrong_tag", false);
                  }
               } else {
                  entity.getPersistentData().putDouble("tagcd", entity.getPersistentData().getDouble("tagcd") - 1.0);
               }

               if (entity.getPersistentData().getDouble("tagcd") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.IRON_NUGGET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_NUGGET));
                  }

                  entity.getPersistentData().putDouble("tagcd", 0.0);
                  entity.getPersistentData().putBoolean("chant", true);
                  entity.getPersistentData().putBoolean("wrong_tag", false);
               }
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

            if (entity.getPersistentData().getBoolean("chant")) {
               if (entity.getPersistentData().getDouble("chantcd3") == 0.0) {
                  entity.getPersistentData().putDouble("chantcd3", 100.0);
                  entity.getPersistentData().putDouble("chantcd4", 0.0);
                  if (Math.random() > 0.5) {
                     entity.getPersistentData().putBoolean("3", true);
                     entity.getPersistentData().putBoolean("4", false);
                  } else {
                     entity.getPersistentData().putBoolean("3", false);
                     entity.getPersistentData().putBoolean("4", true);
                  }

                  if (Math.random() > 0.5) {
                     entity.getPersistentData().putBoolean("red", true);
                     entity.getPersistentData().putBoolean("green", false);
                  } else {
                     entity.getPersistentData().putBoolean("red", false);
                     entity.getPersistentData().putBoolean("green", true);
                  }

                  entity.getPersistentData().putDouble("radius2", Mth.nextInt(net.minecraft.util.RandomSource.create(), 2, 8));
               } else {
                  entity.getPersistentData().putDouble("chantcd3", entity.getPersistentData().getDouble("chantcd3") - 1.0);
                  entity.getPersistentData().putDouble("chantcd4", entity.getPersistentData().getDouble("chantcd4") + 1.0);
               }

               if (entity.getPersistentData().getBoolean("3")) {
                  entity.getPersistentData().putDouble("chantcd", entity.getPersistentData().getDouble("chantcd3"));
               } else if (entity.getPersistentData().getBoolean("4")) {
                  entity.getPersistentData().putDouble("chantcd", entity.getPersistentData().getDouble("chantcd4"));
               }

               if (entity.getPersistentData().getDouble("chantcd1") == 0.0) {
                  entity.getPersistentData().putDouble("chantcd1", 80.0);
                  entity.getPersistentData().putDouble("chantcd2", 0.0);
                  entity.getPersistentData().putDouble("radius1", Mth.nextInt(net.minecraft.util.RandomSource.create(), 2, 8));
                  if (Math.random() > 0.5) {
                     entity.getPersistentData().putBoolean("1", true);
                     entity.getPersistentData().putBoolean("2", false);
                  } else {
                     entity.getPersistentData().putBoolean("1", false);
                     entity.getPersistentData().putBoolean("2", true);
                  }
               } else {
                  entity.getPersistentData().putDouble("chantcd1", entity.getPersistentData().getDouble("chantcd1") - 1.0);
                  entity.getPersistentData().putDouble("chantcd2", entity.getPersistentData().getDouble("chantcd2") + 1.0);
               }

               if (entity.getPersistentData().getBoolean("1")) {
                  entity.getPersistentData().putDouble("2chantcd", entity.getPersistentData().getDouble("chantcd1"));
               } else if (entity.getPersistentData().getBoolean("2")) {
                  entity.getPersistentData().putDouble("2chantcd", entity.getPersistentData().getDouble("chantcd2"));
               }

               if (entity.getPersistentData().getBoolean("green")) {
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL,
                                 new Vec3(
                                    x
                                       + Math.cos((Math.PI / 15) * entity.getPersistentData().getDouble("chantcd"))
                                          * entity.getPersistentData().getDouble("radius1"),
                                    y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), 1.0, 2.0),
                                    z
                                       + Math.sin((Math.PI / 15) * entity.getPersistentData().getDouble("chantcd"))
                                          * entity.getPersistentData().getDouble("radius1")
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
                           "/particle minecraft:dust 0.1 0.8 0 3 ~ ~ ~ 0.1 0.1 0.1 0 1 force"
                        );
                  }

                  Vec3 _center = new Vec3(
                     x + Math.cos((Math.PI / 15) * entity.getPersistentData().getDouble("chantcd")) * entity.getPersistentData().getDouble("radius1"),
                     y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), 1.0, 2.0),
                     z + Math.sin((Math.PI / 15) * entity.getPersistentData().getDouble("chantcd")) * entity.getPersistentData().getDouble("radius1")
                  );

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(0.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator instanceof Player || entityiterator instanceof ServerPlayer) {
                        entity.getPersistentData().putBoolean("tag", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
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
                                       + Math.cos((Math.PI / 15) * entity.getPersistentData().getDouble("2chantcd"))
                                          * entity.getPersistentData().getDouble("radius2"),
                                    y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), 1.0, 2.0),
                                    z
                                       + Math.sin((Math.PI / 15) * entity.getPersistentData().getDouble("2chantcd"))
                                          * entity.getPersistentData().getDouble("radius2")
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
                           "/particle minecraft:dust 1 0 0 3 ~ ~ ~ 0.1 0.1 0.1 0 1 force"
                        );
                  }

                  _center = new Vec3(
                     x + Math.cos((Math.PI / 15) * entity.getPersistentData().getDouble("2chantcd")) * entity.getPersistentData().getDouble("radius2"),
                     y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), 1.0, 2.0),
                     z + Math.sin((Math.PI / 15) * entity.getPersistentData().getDouble("2chantcd")) * entity.getPersistentData().getDouble("radius2")
                  );

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(0.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator instanceof Player || entityiterator instanceof ServerPlayer) {
                        entity.getPersistentData().putBoolean("wrong_tag", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     }
                  }
               }

               if (entity.getPersistentData().getBoolean("red")) {
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL,
                                 new Vec3(
                                    x
                                       + Math.cos((Math.PI / 15) * entity.getPersistentData().getDouble("2chantcd"))
                                          * entity.getPersistentData().getDouble("radius2"),
                                    y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), 1.0, 2.0),
                                    z
                                       + Math.sin((Math.PI / 15) * entity.getPersistentData().getDouble("2chantcd"))
                                          * entity.getPersistentData().getDouble("radius2")
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
                           "/particle minecraft:dust 1 0 0 3 ~ ~ ~ 0.1 0.1 0.1 0 1 force"
                        );
                  }

                  Vec3 _center = new Vec3(
                     x + Math.cos((Math.PI / 15) * entity.getPersistentData().getDouble("2chantcd")) * entity.getPersistentData().getDouble("radius2"),
                     y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), 1.0, 2.0),
                     z + Math.sin((Math.PI / 15) * entity.getPersistentData().getDouble("2chantcd")) * entity.getPersistentData().getDouble("radius2")
                  );

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(0.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator instanceof Player || entityiterator instanceof ServerPlayer) {
                        entity.getPersistentData().putBoolean("wrong_tag", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
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
                                       + Math.cos((Math.PI / 15) * entity.getPersistentData().getDouble("chantcd"))
                                          * entity.getPersistentData().getDouble("radius1"),
                                    y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), 1.0, 2.0),
                                    z
                                       + Math.sin((Math.PI / 15) * entity.getPersistentData().getDouble("chantcd"))
                                          * entity.getPersistentData().getDouble("radius1")
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
                           "/particle minecraft:dust 0.1 0.8 0 3 ~ ~ ~ 0.1 0.1 0.1 0 1 force"
                        );
                  }

                  _center = new Vec3(
                     x + Math.cos((Math.PI / 15) * entity.getPersistentData().getDouble("chantcd")) * entity.getPersistentData().getDouble("radius1"),
                     y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), 1.0, 2.0),
                     z + Math.sin((Math.PI / 15) * entity.getPersistentData().getDouble("chantcd")) * entity.getPersistentData().getDouble("radius1")
                  );

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(0.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator instanceof Player || entityiterator instanceof ServerPlayer) {
                        entity.getPersistentData().putBoolean("tag", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     }
                  }
               }

               if (entity.getPersistentData().getBoolean("hurt")) {
                  entity.getPersistentData().putDouble("chantcd", 0.0);
                  entity.getPersistentData().putBoolean("chant", false);
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
