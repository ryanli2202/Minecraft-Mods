package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.AirEntity;
import dev.deepnight.source.dawn.entity.FireDragonAmbientEntity;
import dev.deepnight.source.dawn.entity.IceDragMultiPart1Entity;
import dev.deepnight.source.dawn.entity.IceDragMultiPartEntity;
import dev.deepnight.source.dawn.entity.IceDragonEntity;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.network.SimpleMobsModVariables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class ProicedragonambientProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, final Entity entity) {
      if (entity != null) {
         double i = 0.0;
         double loop = 0.0;
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

         if (entity.getPersistentData().getBoolean("noai")) {
            Entity _ent = entity;
            _ent.setYRot((float)entity.getPersistentData().getDouble("yaw"));
            _ent.setXRot((float)(entity.getPersistentData().getDouble("pitch") + Mth.nextInt(net.minecraft.util.RandomSource.create(), 5, 25)));
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
            if (world.getEntitiesOfClass(IceDragMultiPartEntity.class, AABB.ofSize(new Vec3(x, y, z), 30.0, 30.0, 30.0), e -> true).isEmpty()) {
               if (world instanceof ServerLevel _level) {
                  Entity entityToSpawn = new IceDragMultiPartEntity(SimpleMobsModEntities.ICE_DRAG_MULTI_PART.get(), _level);
                  entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
                  if (entityToSpawn instanceof Mob _mobToSpawn) {
                     _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                  }

                  world.addFreshEntity(entityToSpawn);
               }
            } else if (world.getEntitiesOfClass(IceDragMultiPart1Entity.class, AABB.ofSize(new Vec3(x, y, z), 30.0, 30.0, 30.0), e -> true).isEmpty()
               && world instanceof ServerLevel _level) {
               Entity entityToSpawn = new IceDragMultiPart1Entity(SimpleMobsModEntities.ICE_DRAG_MULTI_PART_1.get(), _level);
               entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
               if (entityToSpawn instanceof Mob _mobToSpawn) {
                  _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
               }

               world.addFreshEntity(entityToSpawn);
            }

            if (entity.getPersistentData().getDouble("bbtimer") != 0.0) {
               entity.getPersistentData().putDouble("bbtimer", entity.getPersistentData().getDouble("bbtimer") - 1.0);
            } else {
               entity.getPersistentData().putDouble("bbtimer", 20.0);
               SimpleMobsModVariables.MapVariables.get(world).i = -3.0;
               SimpleMobsModVariables.MapVariables.get(world).syncData(world);
               SimpleMobsModVariables.MapVariables.get(world).j = 1.0;
               SimpleMobsModVariables.MapVariables.get(world).syncData(world);
               SimpleMobsModVariables.MapVariables.get(world).x = -3.0;
               SimpleMobsModVariables.MapVariables.get(world).syncData(world);

               while (SimpleMobsModVariables.MapVariables.get(world).i < 4.0) {
                  while (SimpleMobsModVariables.MapVariables.get(world).j < 7.0) {
                     while (SimpleMobsModVariables.MapVariables.get(world).x < 4.0) {
                        if (world.getBlockState(
                                 BlockPos.containing(
                                    x + SimpleMobsModVariables.MapVariables.get(world).i + entity.getLookAngle().x * 4.0,
                                    y + 4.0 + SimpleMobsModVariables.MapVariables.get(world).j,
                                    z + SimpleMobsModVariables.MapVariables.get(world).x + entity.getLookAngle().z * 4.0
                                 )
                              )
                              .getDestroySpeed(
                                 world,
                                 BlockPos.containing(
                                    x + SimpleMobsModVariables.MapVariables.get(world).i + entity.getLookAngle().x * 4.0,
                                    y + 4.0 + SimpleMobsModVariables.MapVariables.get(world).j,
                                    z + SimpleMobsModVariables.MapVariables.get(world).x + entity.getLookAngle().z * 4.0
                                 )
                              )
                           > -1.0F) {
                           world.destroyBlock(
                              BlockPos.containing(
                                 x + SimpleMobsModVariables.MapVariables.get(world).i + entity.getLookAngle().x * 4.0,
                                 y + 4.0 + SimpleMobsModVariables.MapVariables.get(world).j,
                                 z + SimpleMobsModVariables.MapVariables.get(world).x + entity.getLookAngle().z * 4.0
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

            if (!entity.getPersistentData().getBoolean("fall")) {
               entity.getPersistentData().putBoolean("hastarget", false);
               Vec3 _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(50.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                     Vec3 look = new Vec3(entity.getLookAngle().x, 0.0, entity.getLookAngle().z);
                     look = new Vec3(Math.sqrt(Math.pow(look.x, 2.0) + Math.pow(look.y, 2.0)), 0.0, Math.sqrt(Math.pow(look.z, 2.0) + Math.pow(look.y, 2.0)));
                     float distance = 10.0F;
                     entity.getPersistentData().putBoolean("hastarget", true);
                     entity.getPersistentData()
                        .putDouble(
                           "atanyaw",
                           Math.toDegrees(
                              Math.atan2(
                                 entityiterator.getX() - (entity.getX() + look.x * distance), entityiterator.getZ() - (entity.getZ() + look.x * distance)
                              )
                           )
                        );
                     if (entity.getPersistentData().getDouble("atanyaw") < 0.0) {
                        entity.getPersistentData().putDouble("yaw2", 0.0 - entity.getPersistentData().getDouble("atanyaw"));
                     }

                     if (entity.getPersistentData().getDouble("atanyaw") >= 0.0) {
                        entity.getPersistentData().putDouble("yaw2", 360.0 - entity.getPersistentData().getDouble("atanyaw"));
                     }

                     if (entity.getY() + 2.0 - entityiterator.getY() > 0.0) {
                        entity.getPersistentData()
                           .putDouble(
                              "pitch2",
                              90.0
                                 - Math.toDegrees(
                                    Math.atan(
                                       Math.hypot(
                                             Math.abs(entity.getX() + look.x * distance - entityiterator.getX()),
                                             Math.abs(entity.getZ() + look.z * distance - entityiterator.getZ())
                                          )
                                          / Math.abs(entity.getY() + 2.0 - entityiterator.getY())
                                    )
                                 )
                           );
                     } else {
                        entity.getPersistentData()
                           .putDouble(
                              "pitch2",
                              0.0
                                 - Math.toDegrees(
                                    Math.atan(
                                       Math.hypot(
                                             Math.abs(entity.getX() + look.x * distance - entityiterator.getX()),
                                             Math.abs(entity.getZ() + look.z * distance - entityiterator.getZ())
                                          )
                                          / Math.abs(entity.getY() + 2.0 - entityiterator.getY())
                                    )
                                 )
                           );
                     }
                  }
               }

               if (entity.getPersistentData().getBoolean("hastarget") && !entity.getPersistentData().getBoolean("Attacking") && Math.random() > 0.5) {
                  entity.getPersistentData().putBoolean("Attack1", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (entity.getPersistentData().getBoolean("hastarget") && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack2", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               }

               if (entity.getPersistentData().getBoolean("Attack1")) {
                  if (entity.getPersistentData().getDouble("atk1") == 0.0) {
                     entity.getPersistentData().putDouble("atk1", 61.0);
                  } else {
                     entity.getPersistentData().putDouble("atk1", entity.getPersistentData().getDouble("atk1") - 1.0);
                  }

                  entity.getPersistentData().putDouble("y", entity.getYRot() - entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putDouble("p", entity.getXRot() - entity.getPersistentData().getDouble("pitch2"));
                  if (entity.getPersistentData().getDouble("atk1") < 61.0 && entity.getPersistentData().getDouble("atk1") > 30.0) {
                     entity.getEntityData()
                        .set(
                           FireDragonAmbientEntity.atanyaw,
                           (float)(entity.getPersistentData().getDouble("y") * (1.0F - (float)entity.getPersistentData().getDouble("atk1") / 40.0F) * 0.04)
                        );
                     entity.getEntityData()
                        .set(
                           FireDragonAmbientEntity.pitch,
                           (float)(entity.getPersistentData().getDouble("p") * (1.0F - (float)entity.getPersistentData().getDouble("atk1") / 40.0F) * 0.03)
                        );
                  } else if (entity.getPersistentData().getDouble("atk1") < 31.0 && entity.getPersistentData().getDouble("atk1") > 1.0) {
                     entity.getEntityData()
                        .set(
                           FireDragonAmbientEntity.atanyaw,
                           (float)(entity.getPersistentData().getDouble("y") * (entity.getPersistentData().getDouble("atk1") / 40.0) * 0.04)
                        );
                     entity.getEntityData()
                        .set(
                           FireDragonAmbientEntity.pitch,
                           (float)(entity.getPersistentData().getDouble("p") * (entity.getPersistentData().getDouble("atk1") / 40.0) * 0.03)
                        );
                  }

                  float distance = 10.0F;
                  Vec3 look = entity.getViewVector(1.0F);
                  if (entity.getPersistentData().getDouble("atk1") == 61.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.LEATHER_HELMET));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                     }

                     entity.getPersistentData().putBoolean("focus", true);
                     Vec3 _centerx = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_centerx, _centerx).inflate(50.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                           entityiterator.getPersistentData().putBoolean("idtarget", true);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("atk1") == 46.0) {
                     entity.getPersistentData().putBoolean("fire1", true);
                  } else if (entity.getPersistentData().getDouble("atk1") == 8.0) {
                     entity.getPersistentData().putBoolean("fire1", false);
                  } else if (entity.getPersistentData().getDouble("atk1") == 1.0) {
                     Vec3 _centerx = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_centerx, _centerx).inflate(50.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                           entityiterator.getPersistentData().putBoolean("idtarget", false);
                        }
                     }

                     if (Math.random() > 0.3) {
                        entity.getPersistentData().putBoolean("noaggro", true);
                     }

                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putDouble("atk1", 0.0);
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Attack1", false);
                  }

                  if (entity.getPersistentData().getDouble("atk1") == 47.0 && world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_breath"),
                           SoundSource.NEUTRAL,
                           7.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_breath"),
                           SoundSource.NEUTRAL,
                           7.0F,
                           1.0F,
                           false
                        );
                     }
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack2")) {
                  if (entity.getPersistentData().getDouble("atk2") == 0.0) {
                     entity.getPersistentData().putDouble("atk2", 31.0);
                  } else {
                     entity.getPersistentData().putDouble("atk2", entity.getPersistentData().getDouble("atk2") - 1.0);
                  }

                  float distance = 10.0F;
                  Vec3 look = entity.getViewVector(1.0F);
                  entity.getPersistentData().putDouble("y", entity.getYRot() - entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putDouble("p", entity.getXRot() - entity.getPersistentData().getDouble("pitch2"));
                  if (entity.getPersistentData().getDouble("atk2") < 31.0 && entity.getPersistentData().getDouble("atk2") > 15.0) {
                     entity.getEntityData()
                        .set(
                           FireDragonAmbientEntity.atanyaw,
                           (float)(entity.getPersistentData().getDouble("y") * (1.0 - entity.getPersistentData().getDouble("atk2") / 40.0) * 0.04)
                        );
                     entity.getEntityData()
                        .set(
                           FireDragonAmbientEntity.pitch,
                           (float)(entity.getPersistentData().getDouble("p") * (1.0 - entity.getPersistentData().getDouble("atk2") / 40.0) * 0.03)
                        );
                  } else if (entity.getPersistentData().getDouble("atk2") < 16.0 && entity.getPersistentData().getDouble("atk2") > 1.0) {
                     entity.getEntityData()
                        .set(
                           FireDragonAmbientEntity.atanyaw,
                           (float)(entity.getPersistentData().getDouble("y") * (entity.getPersistentData().getDouble("atk2") / 40.0) * 0.04)
                        );
                     entity.getEntityData()
                        .set(
                           FireDragonAmbientEntity.pitch,
                           (float)(entity.getPersistentData().getDouble("p") * (entity.getPersistentData().getDouble("atk2") / 40.0) * 0.03)
                        );
                  }

                  if (entity.getPersistentData().getDouble("atk2") == 31.0) {
                     Vec3 _centerx = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_centerx, _centerx).inflate(50.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                           entityiterator.getPersistentData().putBoolean("idtarget", true);
                        }
                     }

                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.CHAINMAIL_HELMET));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
                     }

                     entity.getPersistentData().putBoolean("focus", true);
                  } else if (entity.getPersistentData().getDouble("atk2") == 15.0) {
                     entity.getPersistentData().putBoolean("fire2", true);
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("item.firecharge.use"),
                              SoundSource.NEUTRAL,
                              7.0F,
                              0.3F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("item.firecharge.use"),
                              SoundSource.NEUTRAL,
                              7.0F,
                              0.3F,
                              false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("atk2") == 1.0) {
                     if (Math.random() > 0.3) {
                        entity.getPersistentData().putBoolean("noaggro", true);
                     }

                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     Vec3 _centerx = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_centerx, _centerx).inflate(50.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                           entityiterator.getPersistentData().putBoolean("fdgtarget", false);
                        }
                     }

                     entity.getPersistentData().putBoolean("fire2", false);
                     entity.getPersistentData().putDouble("atk2", 0.0);
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Attack2", false);
                  }
               }

               if (Math.random() > 0.98 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putDouble("y", Mth.nextInt(net.minecraft.util.RandomSource.create(), -90, 90));
                  entity.getPersistentData().putDouble("p", Mth.nextInt(net.minecraft.util.RandomSource.create(), -50, 50));
                  entity.getPersistentData().putBoolean("turn", true);
               }

               if (entity.getPersistentData().getBoolean("turn")) {
                  entity.getPersistentData()
                     .putDouble(
                        "yaw2",
                        (entity.getYRot() - entity.getPersistentData().getDouble("y")) * (1.0 - entity.getPersistentData().getDouble("turntimer") / 40.0)
                     );
                  entity.getPersistentData()
                     .putDouble(
                        "pitch2",
                        (entity.getYRot() - entity.getPersistentData().getDouble("p")) * (1.0 - entity.getPersistentData().getDouble("turntimer") / 40.0)
                     );
                  if (entity.getPersistentData().getDouble("turntimer") < 40.0 && entity.getPersistentData().getDouble("turntimer") > 21.0) {
                     entity.getEntityData()
                        .set(
                           FireDragonAmbientEntity.atanyaw,
                           (float)(entity.getPersistentData().getDouble("y") * (1.0 - entity.getPersistentData().getDouble("turntimer") / 40.0) * 0.04)
                        );
                     entity.getEntityData()
                        .set(
                           FireDragonAmbientEntity.pitch,
                           (float)(entity.getPersistentData().getDouble("p") * (1.0 - entity.getPersistentData().getDouble("turntimer") / 40.0) * 0.03)
                        );
                  } else if (entity.getPersistentData().getDouble("turntimer") < 22.0 && entity.getPersistentData().getDouble("turntimer") > 1.0) {
                     entity.getEntityData()
                        .set(
                           FireDragonAmbientEntity.atanyaw,
                           (float)(entity.getPersistentData().getDouble("y") * (entity.getPersistentData().getDouble("turntimer") / 40.0) * 0.04)
                        );
                     entity.getEntityData()
                        .set(
                           FireDragonAmbientEntity.pitch,
                           (float)(entity.getPersistentData().getDouble("p") * (entity.getPersistentData().getDouble("turntimer") / 40.0) * 0.03)
                        );
                  }

                  if (entity.getPersistentData().getDouble("turntimer") == 0.0) {
                     entity.setNoGravity(true);
                     entity.getPersistentData().putDouble("turntimer", 40.0);
                     entity.getPersistentData().putBoolean("focus", true);
                  } else {
                     entity.getPersistentData().putDouble("turntimer", entity.getPersistentData().getDouble("turntimer") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("turntimer") == 1.0) {
                     entity.getPersistentData().putDouble("turntimer", 0.0);
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putBoolean("turn", false);
                  }
               }

               if (world.getBlockState(BlockPos.containing(x, y - 1.0 - entity.getPersistentData().getDouble("uptimer"), z)).canOcclude()
                  || world.getBlockState(BlockPos.containing(x, y - 1.0 - entity.getPersistentData().getDouble("uptimer"), z)).is(net.minecraft.tags.BlockTags.LEAVES)) {
                  entity.getPersistentData().putDouble("g", 0.0);
                  entity.getPersistentData().putBoolean("noai", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("glide", false);
                  entity.setDeltaMovement(
                     new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 0.3, 0.8, Math.cos(Math.toRadians(entity.getYRot())) * 0.3)
                  );
               }

               if (entity.getPersistentData().getDouble("uptimer") == 0.0) {
                  if (entity.onGround()) {
                     entity.setDeltaMovement(
                        new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 0.3, 0.8, Math.cos(Math.toRadians(entity.getYRot())) * 0.3)
                     );
                     entity.getPersistentData().putDouble("g", 0.0);
                     entity.getPersistentData().putBoolean("noai", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("glide", false);
                  }

                  entity.getPersistentData().putDouble("uptimer", 3.0);
                  if (world.getBlockState(BlockPos.containing(x, y - 2.0, z)).canOcclude()) {
                     for (; loop < 8.0; loop++) {
                        if (world instanceof ServerLevel _level) {
                           _level.sendParticles(
                              ParticleTypes.CLOUD,
                              x + Math.cos((Math.PI / 4) * loop) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 3.0, 5.0),
                              y - 1.0,
                              z + Math.sin((Math.PI / 4) * loop) * Mth.nextDouble(net.minecraft.util.RandomSource.create(), 3.0, 5.0),
                              2,
                              0.0,
                              0.1,
                              0.0,
                              0.0
                           );
                        }
                     }
                  } else if (world.getBlockState(BlockPos.containing(x, y - 1.0, z)).getBlock() == Blocks.WATER) {
                     entity.getPersistentData().putDouble("g", 0.0);
                     entity.getPersistentData().putBoolean("noai", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("glide", false);
                     entity.setDeltaMovement(
                        new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 0.25, 0.8, Math.cos(Math.toRadians(entity.getYRot())) * 0.25)
                     );
                  }
               } else {
                  entity.getPersistentData().putDouble("uptimer", entity.getPersistentData().getDouble("uptimer") - 1.0);
               }

               if (entity.getPersistentData().getDouble("boostcd") == 0.0 && entity.getPersistentData().getBoolean("boost")) {
                  entity.getPersistentData().putDouble("boostcd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 220, 270));
                  entity.getPersistentData().putBoolean("noaggro", false);
               } else if (entity.getPersistentData().getDouble("boostcd") > 0.0) {
                  entity.getPersistentData().putDouble("boostcd", entity.getPersistentData().getDouble("boostcd") - 1.0);
               }

               if (!entity.getPersistentData().getBoolean("Attacking") && Math.random() < 0.05 && entity.getPersistentData().getDouble("boostcd") == 0.0) {
                  entity.getPersistentData().putBoolean("boost", true);
                  entity.getPersistentData().putDouble("shot", 0.0);
               }

               if (entity.getPersistentData().getBoolean("boost")) {
                  entity.setDeltaMovement(
                     new Vec3(
                        Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 0.75,
                        Math.sin(Math.toRadians(0.0F - entity.getXRot())) * 0.75,
                        Math.cos(Math.toRadians(entity.getYRot())) * 0.75
                     )
                  );
                  if (entity.getPersistentData().getDouble("b") == 0.0) {
                     entity.getPersistentData().putDouble("b", Mth.nextInt(net.minecraft.util.RandomSource.create(), 120, 180));
                  } else {
                     entity.getPersistentData().putDouble("b", entity.getPersistentData().getDouble("b") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("b") == 1.0 || entity.onGround()) {
                     entity.getPersistentData().putDouble("b", 0.0);
                     entity.getPersistentData().putBoolean("boost", false);
                  }

                  if (Math.random() > 0.5 && !entity.getPersistentData().getBoolean("Attacking") && entity.getPersistentData().getDouble("b") == 5.0) {
                     entity.getPersistentData().putDouble("r", Mth.nextInt(net.minecraft.util.RandomSource.create(), 40, 120));
                     entity.getPersistentData().putBoolean("glide", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                     entity.getPersistentData().putBoolean("boost", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("glide")) {
                  if (entity.getPersistentData().getDouble("g") < entity.getPersistentData().getDouble("r")) {
                     entity.setDeltaMovement(
                        new Vec3(
                           Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 0.6,
                           Math.sin(Math.toRadians(0.0F - entity.getXRot())) * 0.9,
                           Math.cos(Math.toRadians(entity.getYRot())) * 0.6
                        )
                     );
                  }

                  if (entity.getPersistentData().getDouble("g") == 0.0) {
                     entity.getPersistentData().putDouble("g", entity.getPersistentData().getDouble("r"));
                  } else {
                     entity.getPersistentData().putDouble("g", entity.getPersistentData().getDouble("g") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("g") == entity.getPersistentData().getDouble("r")) {
                     entity.getPersistentData().putDouble("yaw", entity.getYRot());
                     entity.getPersistentData().putDouble("pitch", entity.getXRot());
                  } else if (entity.getPersistentData().getDouble("g") == entity.getPersistentData().getDouble("r") - 1.0) {
                     entity.getPersistentData().putBoolean("noai", true);
                  }

                  if (entity.getPersistentData().getDouble("g") == 1.0) {
                     entity.getPersistentData().putDouble("g", 0.0);
                     entity.getPersistentData().putBoolean("noai", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("glide", false);
                  }
               }

               if (entity.getPersistentData().getDouble("heightcheck") == 0.0) {
                  entity.getPersistentData().putDouble("heightcheck", 300.0);
               } else {
                  entity.getPersistentData().putDouble("heightcheck", entity.getPersistentData().getDouble("heightcheck") - 1.0);
               }

               if (entity.getPersistentData().getDouble("heightcheck") == 60.0
                  || entity.getPersistentData().getDouble("heightcheck") == 50.0
                  || entity.getPersistentData().getDouble("heightcheck") == 40.0
                  || entity.getPersistentData().getDouble("heightcheck") == 30.0
                  || entity.getPersistentData().getDouble("heightcheck") == 20.0) {
                  if (world.getBlockState(BlockPos.containing(x, y - entity.getPersistentData().getDouble("heightcheck"), z)).getFluidState().is(net.minecraft.tags.FluidTags.WATER)==false
                     && !world.getBlockState(BlockPos.containing(x, y - entity.getPersistentData().getDouble("heightcheck"), z)).canOcclude()) {
                     entity.getPersistentData().putDouble("aircheck", entity.getPersistentData().getDouble("aircheck") + 1.0);
                  } else {
                     entity.getPersistentData().putDouble("aircheck", 0.0);
                  }
               }

               if (entity.getPersistentData().getDouble("aircheck") == 4.0) {
                  entity.setNoGravity(false);
                  entity.getPersistentData().putDouble("aircheck", 0.0);
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
                     public void tick_ProicedragonambientProcedure_1(ServerTickEvent event) {
                        if (event.phase == Phase.END) {
                           this.ticks++;
                           if (this.ticks >= this.waitTicks) {
                              this.run();
                           }
                        }
                     }

                     private void run() {
                        entity.setNoGravity(true);
                        MinecraftForge.EVENT_BUS.unregister(this);
                     }
                  }).start(world, 30);
               }
            } else if (entity.getPersistentData().getBoolean("fall")) {
               if (entity.getPersistentData().getDouble("falltimer") == 0.0) {
                  entity.getPersistentData().putDouble("falltimer", 100.0);
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.POPPY));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.POPPY));
                  }
               } else {
                  entity.getPersistentData().putDouble("falltimer", entity.getPersistentData().getDouble("falltimer") - 1.0);
               }

               if (entity.getPersistentData().getDouble("falltimer") == 88.0) {
                  entity.setNoGravity(false);
               } else if (entity.getPersistentData().getDouble("falltimer") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putDouble("shot", 0.0);
                  entity.getPersistentData().putDouble("falltimer", 0.0);
                  entity.getPersistentData().putBoolean("fall", false);
                  entity.setNoGravity(true);
               }

               if (entity.onGround()) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:martian.attack4"),
                           SoundSource.NEUTRAL,
                           7.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:martian.attack4"),
                           SoundSource.NEUTRAL,
                           7.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new AirEntity(SimpleMobsModEntities.AIR.get(), _level);
                     entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.CLOUD, x, y, z, 30, 3.0, 3.0, 3.0, 0.1);
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 80, 3.0, 3.0, 3.0, 0.1);
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = dev.deepnight.DeepNight.PETS.get(dev.deepnight.Species.DAWN_ICE_DRAGON).get().create(_level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), entity.getXRot());
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
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
