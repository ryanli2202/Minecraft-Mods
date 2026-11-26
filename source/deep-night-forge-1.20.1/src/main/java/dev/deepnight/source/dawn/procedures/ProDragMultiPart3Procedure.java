package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.DragonFlameEntity;
import dev.deepnight.source.dawn.entity.DragonLordEntity;
import dev.deepnight.source.dawn.entity.FireDragProjEntity;
import dev.deepnight.source.dawn.entity.FireDragonEntity;
import dev.deepnight.source.dawn.entity.IceDragonEntity;
import dev.deepnight.source.dawn.entity.LightningDragonEntity;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ProDragMultiPart3Procedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (entity.getPersistentData().getBoolean("lock")) {
            Entity _ent = entity;
            _ent.setYRot((float)entity.getPersistentData().getDouble("xz"));
            _ent.setXRot((float)entity.getPersistentData().getDouble("y"));
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
            entity.getPersistentData().putBoolean("despawn", true);
            if (entity.getPersistentData().getBoolean("firedragon")) {
               Vec3 _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(10.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator instanceof FireDragonEntity) {
                     entity.getPersistentData().putBoolean("despawn", false);
                     Entity _ent = entity;
                     Vec3 look = entityiterator.getViewVector(1.0F);
                     _ent.teleportTo(entityiterator.getX() + 5.0 * look.x, entityiterator.getY() + 4.0, entityiterator.getZ() + 5.0 * look.z);
                  }
               }
            } else if (entity.getPersistentData().getBoolean("icedragon")) {
               Vec3 _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(10.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator instanceof IceDragonEntity) {
                     entity.getPersistentData().putBoolean("despawn", false);
                     Entity _ent = entity;
                     Vec3 look = entityiterator.getViewVector(1.0F);
                     _ent.teleportTo(entityiterator.getX() + 7.0 * look.x, entityiterator.getY() + 6.0, entityiterator.getZ() + 7.0 * look.z);
                  }
               }
            } else if (entity.getPersistentData().getBoolean("lightningdragon")) {
               Vec3 _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(10.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator instanceof LightningDragonEntity) {
                     entity.getPersistentData().putBoolean("despawn", false);
                     Entity _ent = entity;
                     Vec3 look = entityiterator.getViewVector(1.0F);
                     _ent.teleportTo(entityiterator.getX() + 7.0 * look.x, entityiterator.getY() + 6.0, entityiterator.getZ() + 7.0 * look.z);
                  }
               }
            } else if (entity.getPersistentData().getBoolean("dragon_lord")) {
               Vec3 _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(100.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator.getPersistentData().getBoolean("dltarget")) {
                     Vec3 look = new Vec3(entity.getLookAngle().x, 0.0, entity.getLookAngle().z);
                     look = new Vec3(Math.sqrt(Math.pow(look.x, 2.0) + Math.pow(look.y, 2.0)), 0.0, Math.sqrt(Math.pow(look.z, 2.0) + Math.pow(look.y, 2.0)));
                     float distance = 10.0F;
                     entity.getPersistentData().putBoolean("hastarget", true);
                     entity.getPersistentData()
                        .putDouble("atanyaw", Math.toDegrees(Math.atan2(entityiterator.getX() - entity.getX(), entityiterator.getZ() - entity.getZ())));
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
                                             Math.abs(entity.getX() + look.x - entityiterator.getX()), Math.abs(entity.getZ() + look.z - entityiterator.getZ())
                                          )
                                          / Math.abs(entity.getY() - 2.0 - entityiterator.getY())
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
                                             Math.abs(entity.getX() + look.x - entityiterator.getX()), Math.abs(entity.getZ() + look.z - entityiterator.getZ())
                                          )
                                          / Math.abs(entity.getY() - 2.0 - entityiterator.getY())
                                    )
                                 )
                           );
                     }
                  }
               }

               _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(25.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator instanceof DragonLordEntity && dev.deepnight.source.DragonLordParts.belongsTo(entity, entityiterator)) {
                     entity.getPersistentData().putBoolean("despawn", false);
                     Entity _ent = entity;
                     Vec3 look = entityiterator.getViewVector(1.0F);
                     _ent.teleportTo(entityiterator.getX() + 12.0 * look.x, entityiterator.getY() + 8.0, entityiterator.getZ() + 12.0 * look.z);
                     _ent = entity;
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

                     if (entityiterator.getPersistentData().getBoolean("fire1")) {
                        if (Math.random() <= 0.5 && world instanceof ServerLevel _level) {
                           Vec3 lookx = entity.getLookAngle();
                           Vec3 tangent = new Vec3(-lookx.z, 0.0, lookx.x);
                           Entity entityToSpawn = new DragonFlameEntity(SimpleMobsModEntities.DRAGON_FLAME.get(), _level);
                           entityToSpawn.moveTo(
                              x + 4.0 * tangent.x,
                              y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
                              z + 4.0 * tangent.z,
                              (float)(entity.getPersistentData().getDouble("yaw2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -20.0, 20.0)),
                              (float)(entity.getPersistentData().getDouble("pitch2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -10.0, 10.0))
                           );
                           entityToSpawn.setYBodyRot((float)(entity.getPersistentData().getDouble("yaw2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -20.0, 20.0)));
                           entityToSpawn.setYHeadRot((float)(entity.getPersistentData().getDouble("yaw2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -20.0, 20.0)));
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }

                        if (Math.random() <= 0.5 && world instanceof ServerLevel _level) {
                           Vec3 lookx = entity.getLookAngle();
                           new Vec3(-lookx.z, 0.0, lookx.x);
                           Entity entityToSpawn = new DragonFlameEntity(SimpleMobsModEntities.DRAGON_FLAME.get(), _level);
                           entityToSpawn.moveTo(
                              x,
                              y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
                              z,
                              (float)(entity.getPersistentData().getDouble("yaw2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -20.0, 20.0)),
                              (float)(entity.getPersistentData().getDouble("pitch2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -10.0, 10.0))
                           );
                           entityToSpawn.setYBodyRot((float)(entity.getPersistentData().getDouble("yaw2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -20.0, 20.0)));
                           entityToSpawn.setYHeadRot((float)(entity.getPersistentData().getDouble("yaw2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -20.0, 20.0)));
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }

                        if (Math.random() <= 0.5 && world instanceof ServerLevel _level) {
                           Vec3 lookx = entity.getLookAngle();
                           Vec3 tangent = new Vec3(-lookx.z, 0.0, lookx.x);
                           Entity entityToSpawn = new DragonFlameEntity(SimpleMobsModEntities.DRAGON_FLAME.get(), _level);
                           entityToSpawn.moveTo(
                              x + -4.0 * tangent.x,
                              y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
                              z + -4.0 * tangent.z,
                              (float)(entity.getPersistentData().getDouble("yaw2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -20.0, 20.0)),
                              (float)(entity.getPersistentData().getDouble("pitch2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -10.0, 10.0))
                           );
                           entityToSpawn.setYBodyRot((float)(entity.getPersistentData().getDouble("yaw2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -20.0, 20.0)));
                           entityToSpawn.setYHeadRot((float)(entity.getPersistentData().getDouble("yaw2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -20.0, 20.0)));
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }
                     }

                     if (entityiterator.getPersistentData().getBoolean("fire5")) {
                        entity.getPersistentData().putBoolean("locking", true);
                        if (world instanceof ServerLevel _level) {
                           Vec3 lookx = entity.getLookAngle();
                           Vec3 tangent = new Vec3(-lookx.z, 0.0, lookx.x);
                           Entity entityToSpawn = new DragonFlameEntity(SimpleMobsModEntities.DRAGON_FLAME.get(), _level);
                           entityToSpawn.moveTo(
                              x + 7.0 * tangent.x,
                              y - 1.0,
                              z + 7.0 * tangent.z,
                              (float)(entity.getPersistentData().getDouble("xz") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -20.0, 20.0)),
                              (float)(entity.getPersistentData().getDouble("pitch2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -10.0, 10.0))
                           );
                           entityToSpawn.setYBodyRot((float)(entity.getPersistentData().getDouble("xz") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -20.0, 20.0)));
                           entityToSpawn.setYHeadRot((float)(entity.getPersistentData().getDouble("xz") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -20.0, 20.0)));
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }

                        if (world instanceof ServerLevel _level) {
                           Vec3 lookx = entity.getLookAngle();
                           Vec3 tangent = new Vec3(-lookx.z, 0.0, lookx.x);
                           Entity entityToSpawn = new DragonFlameEntity(SimpleMobsModEntities.DRAGON_FLAME.get(), _level);
                           entityToSpawn.moveTo(
                              x + -7.0 * tangent.x,
                              y - 1.0,
                              z + -7.0 * tangent.z,
                              (float)(entity.getPersistentData().getDouble("xz") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -20.0, 20.0)),
                              (float)(entity.getPersistentData().getDouble("pitch2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -10.0, 10.0))
                           );
                           entityToSpawn.setYBodyRot((float)(entity.getPersistentData().getDouble("xz") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -20.0, 20.0)));
                           entityToSpawn.setYHeadRot((float)(entity.getPersistentData().getDouble("xz") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -20.0, 20.0)));
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }
                     }

                     if (entity.getPersistentData().getBoolean("locking")) {
                        if (entity.getPersistentData().getDouble("locking_tick") == 0.0) {
                           entity.getPersistentData().putDouble("locking_tick", 64.0);
                        } else {
                           entity.getPersistentData().putDouble("locking_tick", entity.getPersistentData().getDouble("locking_tick") - 1.0);
                        }

                        if (entity.getPersistentData().getDouble("locking_tick") == 64.0) {
                           entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                        } else if (entity.getPersistentData().getDouble("locking_tick") == 63.0) {
                           entity.getPersistentData().putBoolean("lock", true);
                        } else if (entity.getPersistentData().getDouble("locking_tick") == 1.0) {
                           entity.getPersistentData().putBoolean("lock", false);
                           entity.getPersistentData().putDouble("locking_tick", 0.0);
                           entity.getPersistentData().putBoolean("locking", false);
                        }
                     }

                     if (entityiterator.getPersistentData().getBoolean("fire2")) {
                        entity.getPersistentData().putBoolean("ball1", true);
                     }

                     if (entityiterator.getPersistentData().getBoolean("fire3")) {
                        entity.getPersistentData().putBoolean("ball2", true);
                     }

                     if (entityiterator.getPersistentData().getBoolean("fire4")) {
                        entity.getPersistentData().putBoolean("ball3", true);
                     }

                     if (entity.getPersistentData().getBoolean("ball1")) {
                        if (entity.getPersistentData().getDouble("cd") == 0.0) {
                           if (world instanceof ServerLevel _level) {
                              Entity entityToSpawn = new FireDragProjEntity(SimpleMobsModEntities.FIRE_DRAG_PROJ.get(), _level);
                              entityToSpawn.moveTo(
                                 x,
                                 y,
                                 z,
                                 (float)(entity.getPersistentData().getDouble("yaw2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -10.0, 10.0)),
                                 (float)(entity.getPersistentData().getDouble("pitch2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -5.0, 5.0))
                              );
                              entityToSpawn.setYBodyRot((float)(entity.getPersistentData().getDouble("yaw2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -10.0, 10.0)));
                              entityToSpawn.setYHeadRot((float)(entity.getPersistentData().getDouble("yaw2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -10.0, 10.0)));
                              if (entityToSpawn instanceof Mob _mobToSpawn) {
                                 _mobToSpawn.finalizeSpawn(
                                    _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                                 );
                              }

                              world.addFreshEntity(entityToSpawn);
                           }

                           entity.getPersistentData().putDouble("cd", 15.0);
                        } else {
                           entity.getPersistentData().putDouble("cd", entity.getPersistentData().getDouble("cd") - 1.0);
                        }

                        if (entity.getPersistentData().getDouble("cd") == 1.0) {
                           entity.getPersistentData().putDouble("cd", 0.0);
                           entity.getPersistentData().putBoolean("ball1", false);
                        }
                     }

                     if (entity.getPersistentData().getBoolean("ball2")) {
                        if (entity.getPersistentData().getDouble("cd1") == 0.0) {
                           if (world instanceof ServerLevel _level) {
                              Vec3 lookx = entity.getLookAngle();
                              Vec3 tangent = new Vec3(-lookx.z, 0.0, lookx.x);
                              Entity entityToSpawn = new FireDragProjEntity(SimpleMobsModEntities.FIRE_DRAG_PROJ.get(), _level);
                              entityToSpawn.moveTo(
                                 x + 4.0 * tangent.x,
                                 y,
                                 z + 4.0 * tangent.z,
                                 (float)(entity.getPersistentData().getDouble("yaw2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -10.0, 10.0)),
                                 (float)(entity.getPersistentData().getDouble("pitch2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -5.0, 5.0))
                              );
                              entityToSpawn.setYBodyRot((float)(entity.getPersistentData().getDouble("yaw2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -10.0, 10.0)));
                              entityToSpawn.setYHeadRot((float)(entity.getPersistentData().getDouble("yaw2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -10.0, 10.0)));
                              if (entityToSpawn instanceof Mob _mobToSpawn) {
                                 _mobToSpawn.finalizeSpawn(
                                    _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                                 );
                              }

                              world.addFreshEntity(entityToSpawn);
                           }

                           entity.getPersistentData().putDouble("cd1", 15.0);
                        } else {
                           entity.getPersistentData().putDouble("cd1", entity.getPersistentData().getDouble("cd1") - 1.0);
                        }

                        if (entity.getPersistentData().getDouble("cd1") == 1.0) {
                           entity.getPersistentData().putDouble("cd1", 0.0);
                           entity.getPersistentData().putBoolean("ball2", false);
                        }
                     }

                     if (entity.getPersistentData().getBoolean("ball3")) {
                        if (entity.getPersistentData().getDouble("cd2") == 0.0) {
                           if (world instanceof ServerLevel _level) {
                              Vec3 lookx = entity.getLookAngle();
                              Vec3 tangent = new Vec3(-lookx.z, 0.0, lookx.x);
                              Entity entityToSpawn = new FireDragProjEntity(SimpleMobsModEntities.FIRE_DRAG_PROJ.get(), _level);
                              entityToSpawn.moveTo(
                                 x + -4.0 * tangent.x,
                                 y,
                                 z + -4.0 * tangent.z,
                                 (float)(entity.getPersistentData().getDouble("yaw2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -10.0, 10.0)),
                                 (float)(entity.getPersistentData().getDouble("pitch2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -5.0, 5.0))
                              );
                              entityToSpawn.setYBodyRot((float)(entity.getPersistentData().getDouble("yaw2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -10.0, 10.0)));
                              entityToSpawn.setYHeadRot((float)(entity.getPersistentData().getDouble("yaw2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -10.0, 10.0)));
                              if (entityToSpawn instanceof Mob _mobToSpawn) {
                                 _mobToSpawn.finalizeSpawn(
                                    _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                                 );
                              }

                              world.addFreshEntity(entityToSpawn);
                           }

                           entity.getPersistentData().putDouble("cd2", 15.0);
                        } else {
                           entity.getPersistentData().putDouble("cd2", entity.getPersistentData().getDouble("cd2") - 1.0);
                        }

                        if (entity.getPersistentData().getDouble("cd2") == 1.0) {
                           entity.getPersistentData().putDouble("cd2", 0.0);
                           entity.getPersistentData().putBoolean("ball3", false);
                        }
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("despawn") && !entity.level().isClientSide()) {
               entity.discard();
            }
         }
      }
   }
}
