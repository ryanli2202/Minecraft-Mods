package dev.deepnight.source.dawn.procedures;


import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.BlackholeEntity;
import dev.deepnight.source.dawn.entity.ElementalAttack1Entity;
import dev.deepnight.source.dawn.entity.ElementalAttack2Entity;
import dev.deepnight.source.dawn.entity.ElementalChainEntity;
import dev.deepnight.source.dawn.entity.ElementalDeityEntity;
import dev.deepnight.source.dawn.entity.ElementalFlash2Entity;
import dev.deepnight.source.dawn.entity.ElementalFlash3Entity;
import dev.deepnight.source.dawn.entity.ElementalGrabEntity;
import dev.deepnight.source.dawn.entity.ElementalShieldEntity;
import dev.deepnight.source.dawn.entity.NotchGrabEntity;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.network.SimpleMobsModVariables;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.resources.ResourceKey;
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
import net.minecraftforge.registries.ForgeRegistries;
import dev.deepnight.source.SourceDamage;
import dev.deepnight.source.SourceDamage.StunType;

public class ProelementalProcedure {
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

            entity.getPersistentData().putBoolean("Close", false);
            entity.getPersistentData().putBoolean("Aggro", false);
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

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(13.0), e -> true)
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

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                  entity.getPersistentData().putBoolean("Far", false);
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
               entity.getPersistentData().putDouble("Atk3cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 200, 300));
            } else if (entity.getPersistentData().getDouble("Atk3cd") > 0.0) {
               entity.getPersistentData().putDouble("Atk3cd", entity.getPersistentData().getDouble("Atk3cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Atk4cd") == 0.0 && entity.getPersistentData().getBoolean("Attack4")) {
               entity.getPersistentData().putDouble("Atk4cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 240, 340));
            } else if (entity.getPersistentData().getDouble("Atk4cd") > 0.0) {
               entity.getPersistentData().putDouble("Atk4cd", entity.getPersistentData().getDouble("Atk4cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Atk5cd") == 0.0 && entity.getPersistentData().getBoolean("Attack5")) {
               entity.getPersistentData().putDouble("Atk5cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 400, 700));
            } else if (entity.getPersistentData().getDouble("Atk5cd") > 0.0) {
               entity.getPersistentData().putDouble("Atk5cd", entity.getPersistentData().getDouble("Atk5cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Atk6cd") == 0.0 && entity.getPersistentData().getBoolean("Attack6")) {
               entity.getPersistentData().putDouble("Atk6cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 200, 360));
            } else if (entity.getPersistentData().getDouble("Atk6cd") > 0.0) {
               entity.getPersistentData().putDouble("Atk6cd", entity.getPersistentData().getDouble("Atk6cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Atk7cd") == 0.0 && entity.getPersistentData().getBoolean("Attack7")) {
               entity.getPersistentData().putDouble("Atk7cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 180, 260));
            } else if (entity.getPersistentData().getDouble("Atk7cd") > 0.0) {
               entity.getPersistentData().putDouble("Atk7cd", entity.getPersistentData().getDouble("Atk7cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Atk8cd") == 0.0 && entity.getPersistentData().getBoolean("Attack8")) {
               entity.getPersistentData().putDouble("Atk8cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 300, 400));
            } else if (entity.getPersistentData().getDouble("Atk8cd") > 0.0) {
               entity.getPersistentData().putDouble("Atk8cd", entity.getPersistentData().getDouble("Atk8cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Atk9cd") != 0.0
               || !entity.getPersistentData().getBoolean("Attack9") && !entity.getPersistentData().getBoolean("Attack10")) {
               if (entity.getPersistentData().getDouble("Atk9cd") > 0.0) {
                  entity.getPersistentData().putDouble("Atk9cd", entity.getPersistentData().getDouble("Atk9cd") - 1.0);
               }
            } else {
               entity.getPersistentData().putDouble("Atk9cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 300, 400));
            }

            if (entity.getPersistentData().getDouble("Atk11cd") == 0.0 && entity.getPersistentData().getBoolean("Attack11")) {
               entity.getPersistentData().putDouble("Atk11cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 200, 300));
            } else if (entity.getPersistentData().getDouble("Atk11cd") > 0.0) {
               entity.getPersistentData().putDouble("Atk11cd", entity.getPersistentData().getDouble("Atk11cd") - 1.0);
            }

            if (!entity.getPersistentData().getBoolean("spawned")) {
               entity.getPersistentData().putBoolean("spawned", true);
               entity.getPersistentData().putBoolean("Spawn", true);
               entity.getPersistentData().putBoolean("Attacking", true);
            }

            if (entity.getPersistentData().getBoolean("Far")) {
               if (entity.getPersistentData().getDouble("Atk2cd") == 0.0 && Math.random() > 0.7 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack2", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (entity.getPersistentData().getDouble("Atk8cd") == 0.0 && Math.random() > 0.8 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack8", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (entity.getPersistentData().getDouble("Atk9cd") == 0.0 && Math.random() > 0.9 && !entity.getPersistentData().getBoolean("Attacking")) {
                  if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0F)
                     < (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1.0F) / 2.0F) {
                     entity.getPersistentData().putBoolean("Attack10", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else {
                     entity.getPersistentData().putBoolean("Attack9", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  }
               } else if (Math.random() <= 0.1 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack1", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (Math.random() <= 0.2 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack1a", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               }
            }

            if (entity.getPersistentData().getBoolean("Mid")) {
               if (entity.getPersistentData().getDouble("Atk4cd") == 0.0 && Math.random() > 0.6 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack4", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (entity.getPersistentData().getDouble("Atk7cd") == 0.0 && Math.random() > 0.7 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack7", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (entity.getPersistentData().getDouble("Atk8cd") == 0.0 && Math.random() >= 0.8 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack8", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (entity.getPersistentData().getDouble("Atk9cd") == 0.0 && Math.random() > 0.9 && !entity.getPersistentData().getBoolean("Attacking")) {
                  if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0F)
                     < (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1.0F) / 2.0F) {
                     entity.getPersistentData().putBoolean("Attack10", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else {
                     entity.getPersistentData().putBoolean("Attack9", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  }
               } else if (entity.getPersistentData().getDouble("Atk11cd") == 0.0
                  && !entity.getPersistentData().getBoolean("Attacking")
                  && Math.random() <= 0.25) {
                  entity.getPersistentData().putBoolean("Attack11", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (Math.random() <= 0.1 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack1", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (Math.random() <= 0.2 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack1a", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               }
            }

            if (entity.getPersistentData().getBoolean("Close") && !entity.getPersistentData().getBoolean("Attacking")) {
               if (entity.getPersistentData().getDouble("Atk6cd") == 0.0 && Math.random() > 0.75 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack6", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (entity.getPersistentData().getDouble("Atk3cd") == 0.0
                  && Math.random() >= 0.5
                  && Math.random() <= 0.75
                  && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack3", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (entity.getPersistentData().getDouble("Atk5cd") == 0.0 && !entity.getPersistentData().getBoolean("Attacking") && Math.random() <= 0.25
                  )
                {
                  entity.getPersistentData().putBoolean("Attack5", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (!entity.getPersistentData().getBoolean("Attacking") && Math.random() <= 0.25) {
                  entity.getPersistentData().putBoolean("Attack11", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               }
            }

            if (entity.getPersistentData().getBoolean("Dodge")) {
               if (entity.getPersistentData().getDouble("d") == 0.0) {
                  entity.getPersistentData().putDouble("d", 10.0);
               } else {
                  entity.getPersistentData().putDouble("d", entity.getPersistentData().getDouble("d") - 1.0);
               }

               if (entity.getPersistentData().getDouble("d") == 10.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 2, false, false));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new ElementalFlash3Entity(SimpleMobsModEntities.ELEMENTAL_FLASH_3.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               } else if (entity.getPersistentData().getDouble("d") == 1.0) {
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("focus", false);
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putDouble("d", 0.0);
                  entity.getPersistentData().putBoolean("Dodge", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack9")) {
               if (entity.getPersistentData().getDouble("Atk9") == 0.0) {
                  entity.getPersistentData().putDouble("Atk9", 60.0);
               } else {
                  entity.getPersistentData().putDouble("Atk9", entity.getPersistentData().getDouble("Atk9") - 1.0);
               }

               if (entity instanceof LivingEntity _entity) {
                  _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1, 6, false, false));
               }

               if (entity.getPersistentData().getDouble("Atk9") == 60.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.GREEN_WOOL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.GREEN_WOOL));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("focus", true);
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new ElementalFlash3Entity(SimpleMobsModEntities.ELEMENTAL_FLASH_3.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
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
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_elemental_deity,sort=nearest,limit=1] {Invulnerable:1b}"
                        );
                  }
               } else if (entity.getPersistentData().getDouble("Atk9") == 52.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_elemental_deity,sort=nearest,limit=1] {Invulnerable:0}"
                        );
                  }

                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(32.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        Entity _ent = entity;
                        _ent.teleportTo(
                           entityiterator.getX() + entityiterator.getLookAngle().x * -1.5,
                           entityiterator.getY(),
                           entityiterator.getZ() + entityiterator.getLookAngle().z * -1.5
                        );
                        if (_ent instanceof ServerPlayer _serverPlayer) {
                           _serverPlayer.connection
                              .teleport(
                                 entityiterator.getX() + entityiterator.getLookAngle().x * -1.5,
                                 entityiterator.getY(),
                                 entityiterator.getZ() + entityiterator.getLookAngle().z * -1.5,
                                 _ent.getYRot(),
                                 _ent.getXRot()
                              );
                        }
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk9") == 38.0) {
                  _center = new Vec3(entity.getX() + entity.getLookAngle().x * 1.2, y, entity.getZ() + entity.getLookAngle().z * 1.2);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:notch_punch"),
                                 SoundSource.NEUTRAL,
                                 1.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:notch_punch"),
                                 SoundSource.NEUTRAL,
                                 1.0F,
                                 1.0F,
                                 false
                              );
                           }
                        }

                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(2.0F)
                           .setArmorNegation(40.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 40.0F);
                        entityiterator.setDeltaMovement(
                           new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 4.0, 0.8, Math.cos(Math.toRadians(entity.getYRot())) * 4.0)
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk9") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk9", 0.0);
                  entity.getPersistentData().putBoolean("Attack9", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack10")) {
               if (entity.getPersistentData().getDouble("Atk10") == 0.0) {
                  entity.getPersistentData().putDouble("Atk10", 100.0);
               } else {
                  entity.getPersistentData().putDouble("Atk10", entity.getPersistentData().getDouble("Atk10") - 1.0);
               }

               if (entity instanceof LivingEntity _entity) {
                  _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1, 6, false, false));
               }

               if (entity.getPersistentData().getDouble("Atk10") == 100.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.ACACIA_LEAVES));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.ACACIA_LEAVES));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("focus", true);
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new ElementalFlash3Entity(SimpleMobsModEntities.ELEMENTAL_FLASH_3.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
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
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_elemental_deity,sort=nearest,limit=1] {Invulnerable:1b}"
                        );
                  }
               } else if (entity.getPersistentData().getDouble("Atk10") == 88.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_elemental_deity,sort=nearest,limit=1] {Invulnerable:0}"
                        );
                  }

                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(32.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        Entity _ent = entity;
                        _ent.teleportTo(
                           entityiterator.getX() + entityiterator.getLookAngle().x * -1.5,
                           entityiterator.getY(),
                           entityiterator.getZ() + entityiterator.getLookAngle().z * -1.5
                        );
                        if (_ent instanceof ServerPlayer _serverPlayer) {
                           _serverPlayer.connection
                              .teleport(
                                 entityiterator.getX() + entityiterator.getLookAngle().x * -1.5,
                                 entityiterator.getY(),
                                 entityiterator.getZ() + entityiterator.getLookAngle().z * -1.5,
                                 _ent.getYRot(),
                                 _ent.getXRot()
                              );
                        }
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk10") == 58.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:elemental_attack10"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:elemental_attack10"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F,
                           false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk10") == 54.0) {
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putBoolean("noai", true);
               } else if (entity.getPersistentData().getDouble("Atk10") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("noai", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk10", 0.0);
                  entity.getPersistentData().putBoolean("Attack10", false);
               }

               if (entity.getPersistentData().getDouble("Atk10") <= 58.0 && entity.getPersistentData().getDouble("Atk10") >= 54.0) {
                  entity.setDeltaMovement(
                     new Vec3(
                        Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 0.2,
                        entity.getDeltaMovement().y(),
                        Math.cos(Math.toRadians(entity.getYRot())) * 0.2
                     )
                  );
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new ElementalFlash2Entity(SimpleMobsModEntities.ELEMENTAL_FLASH_2.get(), _level);
                     entityToSpawn.moveTo(
                        entity.getX() + entity.getLookAngle().x * 1.6 + Mth.nextInt(net.minecraft.util.RandomSource.create(), -1, 1),
                        y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.5, 2.0),
                        entity.getZ() + entity.getLookAngle().z * 1.6 + Mth.nextInt(net.minecraft.util.RandomSource.create(), -1, 1),
                        entity.getYRot(),
                        0.0F
                     );
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  _center = new Vec3(entity.getX() + entity.getLookAngle().x * 1.6, y, entity.getZ() + entity.getLookAngle().z * 1.6);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.KNOCKDOWN)
                           .setImpact(2.0F)
                           .setArmorNegation(45.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 60.0F);
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack11")) {
               if (entity.getPersistentData().getDouble("Atk11") == 0.0) {
                  entity.getPersistentData().putDouble("Atk11", 66.0);
               } else {
                  entity.getPersistentData().putDouble("Atk11", entity.getPersistentData().getDouble("Atk11") - 1.0);
               }

               if (entity instanceof LivingEntity _entity) {
                  _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1, 6, false, false));
               }

               if (entity.getPersistentData().getDouble("Atk11") == 66.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.SADDLE));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.SADDLE));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("focus", true);
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new ElementalFlash3Entity(SimpleMobsModEntities.ELEMENTAL_FLASH_3.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               } else if (entity.getPersistentData().getDouble("Atk11") == 64.0) {
                  entity.getPersistentData().putDouble("tp_x", Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, 5));
                  entity.getPersistentData().putDouble("tp_z", Mth.nextInt(net.minecraft.util.RandomSource.create(), 5, 5));
                  if (!world.getBlockState(
                        BlockPos.containing(
                           x + entity.getLookAngle().x * -8.0 + entity.getPersistentData().getDouble("tp_x"),
                           y + 1.0,
                           z + entity.getLookAngle().z * -8.0 + entity.getPersistentData().getDouble("tp_z")
                        )
                     )
                     .canOcclude()) {
                     Entity _ent = entity;
                     _ent.teleportTo(
                        x + entity.getLookAngle().x * -8.0 + entity.getPersistentData().getDouble("tp_x"),
                        y,
                        z + entity.getLookAngle().z * -8.0 + entity.getPersistentData().getDouble("tp_z")
                     );
                     if (_ent instanceof ServerPlayer _serverPlayer) {
                        _serverPlayer.connection
                           .teleport(
                              x + entity.getLookAngle().x * -8.0 + entity.getPersistentData().getDouble("tp_x"),
                              y,
                              z + entity.getLookAngle().z * -8.0 + entity.getPersistentData().getDouble("tp_z"),
                              _ent.getYRot(),
                              _ent.getXRot()
                           );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk11") == 50.0) {
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new ElementalAttack1Entity(SimpleMobsModEntities.ELEMENTAL_ATTACK_1.get(), _level);
                     entityToSpawn.moveTo(x + entity.getLookAngle().x * 1.8, y + 1.8, z + entity.getLookAngle().z * 1.8, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               } else if (entity.getPersistentData().getDouble("Atk11") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk11", 0.0);
                  entity.getPersistentData().putBoolean("Attack11", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack1")) {
               if (entity.getPersistentData().getDouble("Atk1") == 0.0) {
                  entity.getPersistentData().putDouble("Atk1", 70.0);
               } else {
                  entity.getPersistentData().putDouble("Atk1", entity.getPersistentData().getDouble("Atk1") - 1.0);
               }

               if (entity instanceof LivingEntity _entity) {
                  _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1, 6, false, false));
               }

               if (entity.getPersistentData().getDouble("Atk1") == 70.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.LEATHER_HELMET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("focus", true);
               } else if (entity.getPersistentData().getDouble("Atk1") == 68.0) {
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new ElementalAttack1Entity(SimpleMobsModEntities.ELEMENTAL_ATTACK_1.get(), _level);
                     entityToSpawn.moveTo(
                        x + entity.getLookAngle().x * Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, -15) + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3),
                        y + Mth.nextInt(net.minecraft.util.RandomSource.create(), 4, 7),
                        z + entity.getLookAngle().z * Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, -15) + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3),
                        entity.getYRot(),
                        0.0F
                     );
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0F)
                     < (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1.0F) * 0.5) {
                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new ElementalAttack1Entity(SimpleMobsModEntities.ELEMENTAL_ATTACK_1.get(), _level);
                        entityToSpawn.moveTo(
                           x + entity.getLookAngle().x * Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, -15) + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3),
                           y + Mth.nextInt(net.minecraft.util.RandomSource.create(), 4, 7),
                           z + entity.getLookAngle().z * Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, -15) + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3),
                           entity.getYRot(),
                           0.0F
                        );
                        entityToSpawn.setYBodyRot(entity.getYRot());
                        entityToSpawn.setYHeadRot(entity.getYRot());
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }

                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new ElementalAttack1Entity(SimpleMobsModEntities.ELEMENTAL_ATTACK_1.get(), _level);
                        entityToSpawn.moveTo(
                           x + entity.getLookAngle().x * Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, -15) + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3),
                           y + Mth.nextInt(net.minecraft.util.RandomSource.create(), 4, 7),
                           z + entity.getLookAngle().z * Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, -15) + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3),
                           entity.getYRot(),
                           0.0F
                        );
                        entityToSpawn.setYBodyRot(entity.getYRot());
                        entityToSpawn.setYHeadRot(entity.getYRot());
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
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

            if (entity.getPersistentData().getBoolean("Attack1a")) {
               if (entity.getPersistentData().getDouble("Atk1a") == 0.0) {
                  entity.getPersistentData().putDouble("Atk1a", 90.0);
               } else {
                  entity.getPersistentData().putDouble("Atk1a", entity.getPersistentData().getDouble("Atk1a") - 1.0);
               }

               if (entity instanceof LivingEntity _entity) {
                  _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1, 6, false, false));
               }

               if (entity.getPersistentData().getDouble("Atk1a") == 90.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.GOLD_NUGGET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLD_NUGGET));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("focus", true);
               } else if (entity.getPersistentData().getDouble("Atk1a") == 88.0) {
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new ElementalAttack1Entity(SimpleMobsModEntities.ELEMENTAL_ATTACK_1.get(), _level);
                     entityToSpawn.moveTo(
                        x + entity.getLookAngle().x * Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, -10) + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3),
                        y + Mth.nextInt(net.minecraft.util.RandomSource.create(), 4, 7),
                        z + entity.getLookAngle().z * Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, 10) + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3),
                        entity.getYRot(),
                        0.0F
                     );
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0F)
                     < (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1.0F) * 0.5) {
                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new ElementalAttack1Entity(SimpleMobsModEntities.ELEMENTAL_ATTACK_1.get(), _level);
                        entityToSpawn.moveTo(
                           x + entity.getLookAngle().x * Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, -15) + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3),
                           y + Mth.nextInt(net.minecraft.util.RandomSource.create(), 4, 7),
                           z + entity.getLookAngle().z * Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, -15) + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3),
                           entity.getYRot(),
                           0.0F
                        );
                        entityToSpawn.setYBodyRot(entity.getYRot());
                        entityToSpawn.setYHeadRot(entity.getYRot());
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }

                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new ElementalAttack1Entity(SimpleMobsModEntities.ELEMENTAL_ATTACK_1.get(), _level);
                        entityToSpawn.moveTo(
                           x + entity.getLookAngle().x * Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, -10) + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3),
                           y + Mth.nextInt(net.minecraft.util.RandomSource.create(), 4, 7),
                           z + entity.getLookAngle().z * Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, -10) + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3),
                           entity.getYRot(),
                           0.0F
                        );
                        entityToSpawn.setYBodyRot(entity.getYRot());
                        entityToSpawn.setYHeadRot(entity.getYRot());
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk1a") == 58.0) {
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new ElementalAttack1Entity(SimpleMobsModEntities.ELEMENTAL_ATTACK_1.get(), _level);
                     entityToSpawn.moveTo(
                        x + entity.getLookAngle().x * Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, -10) + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3),
                        y + Mth.nextInt(net.minecraft.util.RandomSource.create(), 4, 7),
                        z + entity.getLookAngle().z * Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, -10) + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3),
                        entity.getYRot(),
                        0.0F
                     );
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0F)
                     < (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1.0F) * 0.5) {
                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new ElementalAttack1Entity(SimpleMobsModEntities.ELEMENTAL_ATTACK_1.get(), _level);
                        entityToSpawn.moveTo(
                           x + entity.getLookAngle().x * Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, -15) + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3),
                           y + Mth.nextInt(net.minecraft.util.RandomSource.create(), 4, 7),
                           z + entity.getLookAngle().z * Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, -15) + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3),
                           entity.getYRot(),
                           0.0F
                        );
                        entityToSpawn.setYBodyRot(entity.getYRot());
                        entityToSpawn.setYHeadRot(entity.getYRot());
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }

                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new ElementalAttack1Entity(SimpleMobsModEntities.ELEMENTAL_ATTACK_1.get(), _level);
                        entityToSpawn.moveTo(
                           x + entity.getLookAngle().x * Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, -10) + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3),
                           y + Mth.nextInt(net.minecraft.util.RandomSource.create(), 4, 7),
                           z + entity.getLookAngle().z * Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, -10) + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3),
                           entity.getYRot(),
                           0.0F
                        );
                        entityToSpawn.setYBodyRot(entity.getYRot());
                        entityToSpawn.setYHeadRot(entity.getYRot());
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk1a") == 2.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk1a", 0.0);
                  entity.getPersistentData().putBoolean("Attack1a", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack8")) {
               if (entity.getPersistentData().getDouble("Atk8") == 0.0) {
                  entity.getPersistentData().putDouble("Atk8", 64.0);
               } else {
                  entity.getPersistentData().putDouble("Atk8", entity.getPersistentData().getDouble("Atk8") - 1.0);
               }

               if (entity instanceof LivingEntity _entity) {
                  _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1, 6, false, false));
               }

               if (entity.getPersistentData().getDouble("Atk8") == 64.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.YELLOW_WOOL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.YELLOW_WOOL));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("focus", true);
                  entity.getPersistentData().putDouble("i", 0.0);
                  entity.getPersistentData().putDouble("k", 12.0);
                  entity.getPersistentData().putDouble("j", 0.0);
                  entity.getPersistentData().putDouble("h", 0.0);
               } else if (entity.getPersistentData().getDouble("Atk8") <= 54.0 && entity.getPersistentData().getDouble("Atk8") >= 26.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(32.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        if (entity.getPersistentData().getDouble("i") < entity.getPersistentData().getDouble("k")) {
                           if (world instanceof ServerLevel _level) {
                              Entity entityToSpawn = new ElementalAttack1Entity(SimpleMobsModEntities.ELEMENTAL_ATTACK_1.get(), _level);
                              entityToSpawn.moveTo(
                                 entityiterator.getX()
                                    + Math.cos((Math.PI * 2) / entity.getPersistentData().getDouble("k") * entity.getPersistentData().getDouble("i"))
                                       * (9.0 - entity.getPersistentData().getDouble("j")),
                                 entityiterator.getY() + 2.0 + entity.getPersistentData().getDouble("h"),
                                 entityiterator.getZ()
                                    + Math.sin((Math.PI * 2) / entity.getPersistentData().getDouble("k") * entity.getPersistentData().getDouble("i"))
                                       * (9.0 - entity.getPersistentData().getDouble("j")),
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

                           entity.getPersistentData().putDouble("i", entity.getPersistentData().getDouble("i") + 1.0);
                        } else {
                           entity.getPersistentData().putDouble("i", 0.0);
                           entity.getPersistentData().putDouble("j", entity.getPersistentData().getDouble("j") - 3.0);
                           entity.getPersistentData().putDouble("k", entity.getPersistentData().getDouble("k") - 4.0);
                           entity.getPersistentData().putDouble("h", entity.getPersistentData().getDouble("h") + 2.0);
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

                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk8", 0.0);
                  entity.getPersistentData().putBoolean("Attack8", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack2")) {
               if (entity.getPersistentData().getDouble("Atk2") == 0.0) {
                  entity.getPersistentData().putDouble("Atk2", 90.0);
               } else {
                  entity.getPersistentData().putDouble("Atk2", entity.getPersistentData().getDouble("Atk2") - 1.0);
               }

               if (entity instanceof LivingEntity _entity) {
                  _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1, 6, false, false));
               }

               if (entity.getPersistentData().getDouble("Atk2") == 90.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.COAL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.COAL));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("focus", true);
               } else if (entity.getPersistentData().getDouble("Atk2") == 78.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(35.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null) && world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new ElementalAttack2Entity(SimpleMobsModEntities.ELEMENTAL_ATTACK_2.get(), _level);
                        entityToSpawn.moveTo(
                           entity.getX() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, 5) + entity.getLookAngle().x * 5.0,
                           entity.getY() + Mth.nextInt(net.minecraft.util.RandomSource.create(), 4, 7),
                           entity.getZ() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, 5) + entity.getLookAngle().z * 5.0,
                           entity.getYRot(),
                           0.0F
                        );
                        entityToSpawn.setYBodyRot(entity.getYRot());
                        entityToSpawn.setYHeadRot(entity.getYRot());
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
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

                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk2", 0.0);
                  entity.getPersistentData().putBoolean("Attack2", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack6")) {
               if (entity.getPersistentData().getDouble("Atk6") == 0.0) {
                  entity.getPersistentData().putDouble("Atk6", 75.0);
               } else {
                  entity.getPersistentData().putDouble("Atk6", entity.getPersistentData().getDouble("Atk6") - 1.0);
               }

               if (entity instanceof LivingEntity _entity) {
                  _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1, 6, false, false));
               }

               if (entity.getPersistentData().getDouble("Atk6") == 75.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.WHITE_CONCRETE));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.WHITE_CONCRETE));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("focus", true);
               } else if (entity.getPersistentData().getDouble("Atk6") == 65.0) {
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new ElementalAttack2Entity(SimpleMobsModEntities.ELEMENTAL_ATTACK_2.get(), _level);
                     entityToSpawn.moveTo(
                        x + entity.getLookAngle().x * Mth.nextInt(net.minecraft.util.RandomSource.create(), -1, -4) + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3),
                        y + Mth.nextInt(net.minecraft.util.RandomSource.create(), 3, 7),
                        z + entity.getLookAngle().z * Mth.nextInt(net.minecraft.util.RandomSource.create(), -1, -4) + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3),
                        entity.getYRot(),
                        0.0F
                     );
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               } else if (entity.getPersistentData().getDouble("Atk6") == 5.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk6", 0.0);
                  entity.getPersistentData().putBoolean("Attack6", false);
               }

               if (entity.getPersistentData().getDouble("Atk6") <= 63.0 && entity.getPersistentData().getDouble("Atk6") >= 30.0) {
                  entity.setDeltaMovement(
                     new Vec3(
                        Math.sin(Math.toRadians(entity.getYRot())) * 0.3,
                        entity.getDeltaMovement().y(),
                        Math.cos(Math.toRadians(entity.getYRot() + 180.0F)) * 0.3
                     )
                  );
               }
            }

            if (entity.getPersistentData().getBoolean("Attack3")) {
               if (entity.getPersistentData().getDouble("Atk3") == 0.0) {
                  entity.getPersistentData().putDouble("Atk3", 65.0);
               } else {
                  entity.getPersistentData().putDouble("Atk3", entity.getPersistentData().getDouble("Atk3") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk3") == 65.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 65, 6, false, false));
                  }

                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.CHARCOAL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHARCOAL));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("noai", true);
               } else if (entity.getPersistentData().getDouble("Atk3") == 58.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:elemental_shiield"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:elemental_shiield"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new ElementalShieldEntity(SimpleMobsModEntities.ELEMENTAL_SHIELD.get(), _level);
                     entityToSpawn.moveTo(x, y + 2.0, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
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

               if (entity.getPersistentData().getDouble("Atk3") <= 48.0 && entity.getPersistentData().getDouble("Atk3") >= 45.0) {
                  _center = new Vec3(x, y + 2.0, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof ElementalDeityEntity) && !(entityiterator instanceof ElementalShieldEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(2.0F)
                           .setArmorNegation(45.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 60.0F);
                        entityiterator.setDeltaMovement(
                           new Vec3((entityiterator.getX() - entity.getX()) * 1.4, 1.2, (entityiterator.getZ() - entity.getZ()) * 1.4)
                        );
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack4")) {
               if (entity.getPersistentData().getDouble("Atk4") == 0.0) {
                  entity.getPersistentData().putDouble("Atk4", 50.0);
               } else {
                  entity.getPersistentData().putDouble("Atk4", entity.getPersistentData().getDouble("Atk4") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk4") == 50.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 50, 6, false, false));
                  }

                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.IRON_INGOT));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_INGOT));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("focus", true);
               } else if (entity.getPersistentData().getDouble("Atk4") == 34.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(32.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        Vec3 look = entityiterator.getLookAngle();
                        Vec3 tangent = new Vec3(-look.z, 0.0, look.x);
                        if (world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new BlackholeEntity(SimpleMobsModEntities.BLACKHOLE.get(), _level);
                           entityToSpawn.moveTo(
                              entityiterator.getX() + 5.0 * tangent.x,
                              entityiterator.getY() + 2.0,
                              entityiterator.getZ() + 5.0 * tangent.z,
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
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk4") == 14.0) {
                  if (Math.random() >= 0.4
                     && (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0F)
                        < (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1.0F) * 0.5) {
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putBoolean("Attack8", true);
                     entity.getPersistentData().putDouble("Atk4", 0.0);
                     entity.getPersistentData().putBoolean("Attack4", false);
                  }
               } else if (entity.getPersistentData().getDouble("Atk4") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk4", 0.0);
                  entity.getPersistentData().putBoolean("Attack4", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack5")) {
               if (entity.getPersistentData().getDouble("Atk5") == 0.0) {
                  entity.getPersistentData().putDouble("Atk5", 90.0);
               } else {
                  entity.getPersistentData().putDouble("Atk5", entity.getPersistentData().getDouble("Atk5") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk5") == 90.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 90, 6, false, false));
                  }

                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.IRON_NUGGET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_NUGGET));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("focus", true);
               } else if (entity.getPersistentData().getDouble("Atk5") == 42.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 32, 6, false, false));
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_punch"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           0.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_punch"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           0.0F,
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
                        entity.getPersistentData().putBoolean("Attack5Grabbed", true);
                        entityiterator.getPersistentData().putBoolean("notch_grabbed", true);
                        entity.getPersistentData().putBoolean("focus", false);
                        if (entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.STUN.get(), 70, 1));
                        }

                        entity.getPersistentData().putDouble("Atk5", 0.0);
                        entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                        entity.getPersistentData().putDouble("pitch", entity.getPersistentData().getDouble("pitch2"));
                        entity.getPersistentData().putBoolean("Attack5", false);
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk5") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk5", 0.0);
                  entity.getPersistentData().putBoolean("Attack5", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack5Grabbed")) {
               if (entity.getPersistentData().getDouble("Atk5g") == 0.0) {
                  entity.getPersistentData().putDouble("Atk5g", 85.0);
               } else {
                  entity.getPersistentData().putDouble("Atk5g", entity.getPersistentData().getDouble("Atk5g") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk5g") == 85.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:elemental_grab"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:elemental_grab"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 62, 6, false, false));
                  }

                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.WHITE_WOOL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.WHITE_WOOL));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("lock", true);
                  entity.getPersistentData().putBoolean("noai", true);
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new ElementalGrabEntity(SimpleMobsModEntities.ELEMENTAL_GRAB.get(), _level);
                     entityToSpawn.moveTo(x + entity.getLookAngle().x * 1.8, y, z + entity.getLookAngle().z * 1.8, 0.0F, 0.0F);
                     entityToSpawn.setYBodyRot(0.0F);
                     entityToSpawn.setYHeadRot(0.0F);
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new NotchGrabEntity(SimpleMobsModEntities.NOTCH_GRAB.get(), _level);
                     entityToSpawn.moveTo(x + entity.getLookAngle().x * 1.8, y + 1.5, z + entity.getLookAngle().z * 1.8, 0.0F, 0.0F);
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
                           dev.deepnight.SafeSounds.resolve("simple_mobs:elemental_attack10"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:elemental_attack10"),
                           SoundSource.NEUTRAL,
                           1.0F,
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
                        _ent.teleportTo(x + entity.getLookAngle().x * 1.8, y + 1.0, z + entity.getLookAngle().z * 1.8);
                        if (_ent instanceof ServerPlayer _serverPlayer) {
                           _serverPlayer.connection
                              .teleport(x + entity.getLookAngle().x * 1.8, y + 1.0, z + entity.getLookAngle().z * 1.8, _ent.getYRot(), _ent.getXRot());
                        }
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk5g") == 65.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator instanceof NotchGrabEntity) {
                        Entity _ent = entityiterator;
                        _ent.teleportTo(x + entity.getLookAngle().x * 1.8, y + 1.5, z + entity.getLookAngle().z * 1.8);
                        if (_ent instanceof ServerPlayer _serverPlayer) {
                           _serverPlayer.connection
                              .teleport(x + entity.getLookAngle().x * 1.8, y + 1.5, z + entity.getLookAngle().z * 1.8, _ent.getYRot(), _ent.getXRot());
                        }
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk5g") == 55.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator instanceof NotchGrabEntity) {
                        Entity _ent = entityiterator;
                        _ent.teleportTo(x + entity.getLookAngle().x * 1.8, y + 2.0, z + entity.getLookAngle().z * 1.8);
                        if (_ent instanceof ServerPlayer _serverPlayer) {
                           _serverPlayer.connection
                              .teleport(x + entity.getLookAngle().x * 1.8, y + 2.0, z + entity.getLookAngle().z * 1.8, _ent.getYRot(), _ent.getXRot());
                        }
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk5g") != 45.0) {
                  if (entity.getPersistentData().getDouble("Atk5g") == 30.0) {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator instanceof NotchGrabEntity && !entityiterator.level().isClientSide()) {
                           entityiterator.discard();
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk5g") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("noai", false);
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("Atk5g", 0.0);
                     entity.getPersistentData().putBoolean("Attack5Grabbed", false);
                  }
               } else {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator.getPersistentData().getBoolean("notch_grabbed")) {
                        if (!(entityiterator instanceof Player) && !(entityiterator instanceof ServerPlayer)) {
                           entityiterator.hurt(new DamageSource(entityiterator.level().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(net.minecraft.world.damagesource.DamageTypes.GENERIC), entity), 200.0F);
                           entityiterator.getPersistentData().putBoolean("notch_grabbed", false);
                        } else {
                           if (entityiterator instanceof ServerPlayer _player && !_player.level().isClientSide()) {
                              ResourceKey<Level> destinationType = Level.NETHER;
                              if (_player.level().dimension() == destinationType) {
                                 return;
                              }

                              ServerLevel nextLevel = _player.server.getLevel(destinationType);
                              if (nextLevel != null) {
                                 _player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, 0.0F));
                                 _player.teleportTo(nextLevel, _player.getX(), _player.getY(), _player.getZ(), _player.getYRot(), _player.getXRot());
                                 _player.connection.send(new ClientboundPlayerAbilitiesPacket(_player.getAbilities()));

                                 for (MobEffectInstance _effectinstance : _player.getActiveEffects()) {
                                    _player.connection.send(new ClientboundUpdateMobEffectPacket(_player.getId(), _effectinstance));
                                 }

                                 _player.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
                              }
                           }

                           Entity _ent = entityiterator;
                           _ent.teleportTo(0.0, 100.0, 0.0);
                           if (_ent instanceof ServerPlayer _serverPlayer) {
                              _serverPlayer.connection.teleport(0.0, 100.0, 0.0, _ent.getYRot(), _ent.getXRot());
                           }

                           entityiterator.getPersistentData().putBoolean("notch_grabbed", false);
                        }
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new ElementalFlash2Entity(SimpleMobsModEntities.ELEMENTAL_FLASH_2.get(), _level);
                     entityToSpawn.moveTo(
                        x - entity.getLookAngle().x * 1.5, y + 3.0, z - entity.getLookAngle().z * 1.5, world.getRandom().nextFloat() * 360.0F, 0.0F
                     );
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack7")) {
               if (entity.getPersistentData().getDouble("Atk7") == 0.0) {
                  entity.getPersistentData().putDouble("Atk7", 50.0);
               } else {
                  entity.getPersistentData().putDouble("Atk7", entity.getPersistentData().getDouble("Atk7") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk7") == 50.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:elemental_chain"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:elemental_chain"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F,
                           false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk7") == 35.0
                  && (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0F)
                     <= (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1.0F) * 0.5
                  && world instanceof Level _level) {
                  if (!_level.isClientSide()) {
                     _level.playSound(
                        null,
                        BlockPos.containing(x, y, z),
                        dev.deepnight.SafeSounds.resolve("simple_mobs:elemental_chain"),
                        SoundSource.NEUTRAL,
                        3.0F,
                        1.0F
                     );
                  } else {
                     _level.playLocalSound(
                        x,
                        y,
                        z,
                        dev.deepnight.SafeSounds.resolve("simple_mobs:elemental_chain"),
                        SoundSource.NEUTRAL,
                        3.0F,
                        1.0F,
                        false
                     );
                  }
               }

               if (entity.getPersistentData().getDouble("Atk7") == 50.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 50, 6, false, false));
                  }

                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.RED_WOOL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.RED_WOOL));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("focus", true);
               } else if (entity.getPersistentData().getDouble("Atk7") <= 50.0 && entity.getPersistentData().getDouble("Atk7") >= 48.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(32.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null) && world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new ElementalChainEntity(SimpleMobsModEntities.ELEMENTAL_CHAIN.get(), _level);
                        entityToSpawn.moveTo(
                           entityiterator.getX() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -6, 6),
                           entityiterator.getY() + Mth.nextInt(net.minecraft.util.RandomSource.create(), 4, 7),
                           entityiterator.getZ() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -6, 6),
                           entity.getYRot(),
                           60.0F
                        );
                        entityToSpawn.setYBodyRot(entity.getYRot());
                        entityToSpawn.setYHeadRot(entity.getYRot());
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk7") <= 36.0 && entity.getPersistentData().getDouble("Atk7") >= 34.0) {
                  if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0F)
                     <= (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1.0F) * 0.5) {
                     Vec3 _centerx = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_centerx, _centerx).inflate(32.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null) && world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new ElementalChainEntity(SimpleMobsModEntities.ELEMENTAL_CHAIN.get(), _level);
                           entityToSpawn.moveTo(
                              entityiterator.getX() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -6, 6),
                              entityiterator.getY() + Mth.nextInt(net.minecraft.util.RandomSource.create(), 4, 7),
                              entityiterator.getZ() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -6, 6),
                              entity.getYRot(),
                              60.0F
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
               } else if (entity.getPersistentData().getDouble("Atk7") == 20.0) {
                  if (Math.random() >= 0.5) {
                     entity.getPersistentData().putDouble("Atk7", 0.0);
                     entity.getPersistentData().putBoolean("Attack2", true);
                     entity.getPersistentData().putBoolean("noai", false);
                     entity.getPersistentData().putBoolean("Attack7", false);
                  }
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
            }

            if (entity.getPersistentData().getBoolean("Spawn")) {
               if (entity.getPersistentData().getDouble("s") == 0.0) {
                  entity.getPersistentData().putDouble("s", 61.0);
               } else {
                  entity.getPersistentData().putDouble("s", entity.getPersistentData().getDouble("s") - 1.0);
               }

               if (entity.getPersistentData().getDouble("s") == 61.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.POPPY));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.POPPY));
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new ElementalGrabEntity(SimpleMobsModEntities.ELEMENTAL_GRAB.get(), _level);
                     entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
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
                           "/data merge entity @e[type=deepnight:dawn_elemental_deity,limit=1,sort=nearest] {NoAI:1b}"
                        );
                  }
               } else if (entity.getPersistentData().getDouble("s") == 1.0) {
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
                           "/data merge entity @e[type=deepnight:dawn_elemental_deity,limit=1,sort=nearest] {NoAI:0}"
                        );
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("s", 0.0);
                  entity.getPersistentData().putBoolean("Spawn", false);
               }
            }

            if (!entity.getPersistentData().getBoolean("Aggro")) {
               if (entity.getPersistentData().getDouble("aggrotimer") == 0.0) {
                  entity.getPersistentData().putDouble("aggrotimer", 100.0);
               } else {
                  entity.getPersistentData().putDouble("aggrotimer", entity.getPersistentData().getDouble("aggrotimer") - 1.0);
               }

               if (entity.getPersistentData().getDouble("aggrotimer") == 1.0 && entity instanceof LivingEntity _entity) {
                  _entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 80, 4, false, false));
               }
            } else {
               entity.getPersistentData().putDouble("aggrotimer", 0.0);
            }

            if ((world instanceof Level _lvl ? _lvl.dimension() : Level.OVERWORLD)
                  == ResourceKey.<Level>create(net.minecraft.core.registries.Registries.DIMENSION, new ResourceLocation("simple_mobs:distant_land"))
               && Math.sqrt(Math.pow(x - 0.0, 2.0) + Math.pow(z - 0.0, 2.0)) > 50.0
               && !entity.getPersistentData().getBoolean("Attacking")) {
               if (world instanceof ServerLevel _level) {
                  Entity entityToSpawn = new ElementalFlash3Entity(SimpleMobsModEntities.ELEMENTAL_FLASH_3.get(), _level);
                  entityToSpawn.moveTo(x, y, z, entity.getYRot(), 0.0F);
                  entityToSpawn.setYBodyRot(entity.getYRot());
                  entityToSpawn.setYHeadRot(entity.getYRot());
                  if (entityToSpawn instanceof Mob _mobToSpawn) {
                     _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                  }

                  world.addFreshEntity(entityToSpawn);
               }

               Entity _ent = entity;
               _ent.teleportTo(0.0, y, 0.0);
               if (_ent instanceof ServerPlayer _serverPlayer) {
                  _serverPlayer.connection.teleport(0.0, y, 0.0, _ent.getYRot(), _ent.getXRot());
               }
            }
         }
      }
   }
}
