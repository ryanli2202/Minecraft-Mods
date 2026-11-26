package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.DragonFlameEntity;
import dev.deepnight.source.dawn.entity.DragonLightningBreathEntity;
import dev.deepnight.source.dawn.entity.FireDragProjEntity;
import dev.deepnight.source.dawn.entity.FireDragonAmbientEntity;
import dev.deepnight.source.dawn.entity.IceDragonAmbientEntity;
import dev.deepnight.source.dawn.entity.IceDragonBreathEntity;
import dev.deepnight.source.dawn.entity.IceDragonProjectileEntity;
import dev.deepnight.source.dawn.entity.LightningDragonAmbientEntity;
import dev.deepnight.source.dawn.entity.WindEntity;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.init.SimpleMobsModParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class Prodragmulti1Procedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         entity.getPersistentData().putBoolean("despawn", true);
         if (entity.getPersistentData().getBoolean("firedragon")) {
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(50.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator.getPersistentData().getBoolean("fdgtarget")) {
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

            _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(10.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator instanceof FireDragonAmbientEntity) {
                  Entity _ent = entity;
                  _ent.setYRot(entityiterator.getYRot());
                  _ent.setXRot(entityiterator.getXRot());
                  _ent.setYBodyRot(_ent.getYRot());
                  _ent.setYHeadRot(_ent.getYRot());
                  _ent.yRotO = _ent.getYRot();
                  _ent.xRotO = _ent.getXRot();
                  if (_ent instanceof LivingEntity _entity) {
                     _entity.yBodyRotO = _entity.getYRot();
                     _entity.yHeadRotO = _entity.getYRot();
                  }

                  _ent = entity;
                  Vec3 look = entityiterator.getViewVector(1.0F);
                  _ent.teleportTo(entityiterator.getX() + 10.0 * look.x, entityiterator.getY() + 2.0, entityiterator.getZ() + 10.0 * look.z);
                  entity.getPersistentData().putBoolean("despawn", false);
                  if (entityiterator.getPersistentData().getBoolean("fire1") && world instanceof ServerLevel _level) {
                     Vec3 lookx = entity.getLookAngle();
                     new Vec3(-lookx.z, 0.0, lookx.x);
                     Entity entityToSpawn = new DragonFlameEntity(SimpleMobsModEntities.DRAGON_FLAME.get(), _level);
                     entityToSpawn.moveTo(
                        x,
                        y,
                        z,
                        (float)(entity.getPersistentData().getDouble("yaw2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -5.0, 5.0)),
                        (float)(entity.getPersistentData().getDouble("pitch2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -5.0, 5.0))
                     );
                     entityToSpawn.setYBodyRot((float)(entity.getPersistentData().getDouble("yaw2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -5.0, 5.0)));
                     entityToSpawn.setYHeadRot((float)(entity.getPersistentData().getDouble("yaw2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -5.0, 5.0)));
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  if (entityiterator.getPersistentData().getBoolean("fire2")) {
                     entity.getPersistentData().putBoolean("ball", true);
                  }

                  if ((entityiterator.getPersistentData().getBoolean("Attack1") || entityiterator.getPersistentData().getBoolean("Attack2"))
                     && world instanceof ServerLevel _level) {
                     _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.FLAME.get(), x, y, z, 1, 0.1, 0.1, 0.1, 0.0);
                  }

                  if (entity.getPersistentData().getBoolean("ball")) {
                     if (entity.getPersistentData().getDouble("cd") == 0.0) {
                        if (world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new FireDragProjEntity(SimpleMobsModEntities.FIRE_DRAG_PROJ.get(), _level);
                           entityToSpawn.moveTo(
                              x, y, z, (float)entity.getPersistentData().getDouble("yaw2"), (float)entity.getPersistentData().getDouble("pitch2")
                           );
                           entityToSpawn.setYBodyRot((float)entity.getPersistentData().getDouble("yaw2"));
                           entityToSpawn.setYHeadRot((float)entity.getPersistentData().getDouble("yaw2"));
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
                        entity.getPersistentData().putBoolean("ball", false);
                     }
                  }
               }
            }
         } else if (entity.getPersistentData().getBoolean("icedragon")) {
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(50.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator.getPersistentData().getBoolean("idtarget")) {
                  Vec3 look = new Vec3(entity.getLookAngle().x, 0.0, entity.getLookAngle().z);
                  look = new Vec3(Math.sqrt(Math.pow(look.x, 2.0) + Math.pow(look.y, 2.0)), 0.0, Math.sqrt(Math.pow(look.z, 2.0) + Math.pow(look.y, 2.0)));
                  float distance = 12.0F;
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
                                          Math.abs(entity.getX() + look.x * distance - entityiterator.getX()),
                                          Math.abs(entity.getZ() + look.z * distance - entityiterator.getZ())
                                       )
                                       / Math.abs(entity.getY() + 0.5 - entityiterator.getY())
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
                                       / Math.abs(entity.getY() + 0.5 - entityiterator.getY())
                                 )
                              )
                        );
                  }
               }
            }

            _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(10.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator instanceof IceDragonAmbientEntity) {
                  Entity _ent = entity;
                  _ent.setYRot(entityiterator.getYRot());
                  _ent.setXRot(entityiterator.getXRot());
                  _ent.setYBodyRot(_ent.getYRot());
                  _ent.setYHeadRot(_ent.getYRot());
                  _ent.yRotO = _ent.getYRot();
                  _ent.xRotO = _ent.getXRot();
                  if (_ent instanceof LivingEntity _entity) {
                     _entity.yBodyRotO = _entity.getYRot();
                     _entity.yHeadRotO = _entity.getYRot();
                  }

                  _ent = entity;
                  Vec3 look = entityiterator.getViewVector(1.0F);
                  _ent.teleportTo(entityiterator.getX() + 12.0 * look.x, entityiterator.getY() + 4.0, entityiterator.getZ() + 12.0 * look.z);
                  entity.getPersistentData().putBoolean("despawn", false);
                  if (entityiterator.getPersistentData().getBoolean("fire1")) {
                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new IceDragonBreathEntity(SimpleMobsModEntities.ICE_DRAGON_BREATH.get(), _level);
                        entityToSpawn.moveTo(
                           x,
                           y,
                           z,
                           (float)(entity.getPersistentData().getDouble("yaw2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -5.0, 5.0)),
                           (float)(entity.getPersistentData().getDouble("pitch2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -5.0, 5.0))
                        );
                        entityToSpawn.setYBodyRot((float)(entity.getPersistentData().getDouble("yaw2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -5.0, 5.0)));
                        entityToSpawn.setYHeadRot((float)(entity.getPersistentData().getDouble("yaw2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -5.0, 5.0)));
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }

                     if (Math.random() > 0.4 && world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new WindEntity(SimpleMobsModEntities.WIND.get(), _level);
                        entityToSpawn.moveTo(
                           x, y, z, (float)entity.getPersistentData().getDouble("yaw2"), (float)entity.getPersistentData().getDouble("pitch2")
                        );
                        entityToSpawn.setYBodyRot((float)entity.getPersistentData().getDouble("yaw2"));
                        entityToSpawn.setYHeadRot((float)entity.getPersistentData().getDouble("yaw2"));
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  }

                  if (entityiterator.getPersistentData().getBoolean("fire2")) {
                     entity.getPersistentData().putBoolean("ball", true);
                  }

                  if ((entityiterator.getPersistentData().getBoolean("Attack1") || entityiterator.getPersistentData().getBoolean("Attack2"))
                     && world instanceof ServerLevel _level) {
                     _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.FROST_1.get(), x, y, z, 1, 0.1, 0.1, 0.1, 0.0);
                  }

                  if (entity.getPersistentData().getBoolean("ball")) {
                     if (entity.getPersistentData().getDouble("cd") == 0.0) {
                        if (world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new IceDragonProjectileEntity(SimpleMobsModEntities.ICE_DRAGON_PROJECTILE.get(), _level);
                           entityToSpawn.moveTo(
                              x, y, z, (float)entity.getPersistentData().getDouble("yaw2"), (float)entity.getPersistentData().getDouble("pitch2")
                           );
                           entityToSpawn.setYBodyRot((float)entity.getPersistentData().getDouble("yaw2"));
                           entityToSpawn.setYHeadRot((float)entity.getPersistentData().getDouble("yaw2"));
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
                        entity.getPersistentData().putBoolean("ball", false);
                     }
                  }
               }
            }
         } else if (entity.getPersistentData().getBoolean("lightningdragon")) {
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(50.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator.getPersistentData().getBoolean("ldtarget")) {
                  Vec3 look = new Vec3(entity.getLookAngle().x, 0.0, entity.getLookAngle().z);
                  look = new Vec3(Math.sqrt(Math.pow(look.x, 2.0) + Math.pow(look.y, 2.0)), 0.0, Math.sqrt(Math.pow(look.z, 2.0) + Math.pow(look.y, 2.0)));
                  float distance = 12.0F;
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
                                          Math.abs(entity.getX() + look.x * distance - entityiterator.getX()),
                                          Math.abs(entity.getZ() + look.z * distance - entityiterator.getZ())
                                       )
                                       / Math.abs(entity.getY() + 0.5 - entityiterator.getY())
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
                                       / Math.abs(entity.getY() + 0.5 - entityiterator.getY())
                                 )
                              )
                        );
                  }
               }
            }

            _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(10.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator instanceof LightningDragonAmbientEntity) {
                  Entity _ent = entity;
                  _ent.setYRot(entityiterator.getYRot());
                  _ent.setXRot(entityiterator.getXRot());
                  _ent.setYBodyRot(_ent.getYRot());
                  _ent.setYHeadRot(_ent.getYRot());
                  _ent.yRotO = _ent.getYRot();
                  _ent.xRotO = _ent.getXRot();
                  if (_ent instanceof LivingEntity _entity) {
                     _entity.yBodyRotO = _entity.getYRot();
                     _entity.yHeadRotO = _entity.getYRot();
                  }

                  _ent = entity;
                  Vec3 look = entityiterator.getViewVector(1.0F);
                  _ent.teleportTo(entityiterator.getX() + 12.0 * look.x, entityiterator.getY() + 4.0, entityiterator.getZ() + 12.0 * look.z);
                  entity.getPersistentData().putBoolean("despawn", false);
                  if (entityiterator.getPersistentData().getBoolean("fire1") && world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new DragonLightningBreathEntity(SimpleMobsModEntities.DRAGON_LIGHTNING_BREATH.get(), _level);
                     entityToSpawn.moveTo(
                        x,
                        y,
                        z,
                        (float)(entity.getPersistentData().getDouble("yaw2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -5.0, 5.0)),
                        (float)(entity.getPersistentData().getDouble("pitch2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -5.0, 5.0))
                     );
                     entityToSpawn.setYBodyRot((float)(entity.getPersistentData().getDouble("yaw2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -5.0, 5.0)));
                     entityToSpawn.setYHeadRot((float)(entity.getPersistentData().getDouble("yaw2") + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -5.0, 5.0)));
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  if (entityiterator.getPersistentData().getBoolean("Attack1") && world instanceof ServerLevel _level) {
                     _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.DRAGON_THUNDER_LIT.get(), x, y, z, 1, 0.1, 0.1, 0.1, 0.0);
                  }
               }
            }
         }

         if (entity.getPersistentData().getDouble("despawncd") == 0.0) {
            entity.getPersistentData().putDouble("despawncd", 20.0);
         } else {
            entity.getPersistentData().putDouble("despawncd", entity.getPersistentData().getDouble("despawncd") - 1.0);
         }

         if (entity.getPersistentData().getDouble("despawncd") == 0.0 && entity.getPersistentData().getBoolean("despawn") && !entity.level().isClientSide()) {
            entity.discard();
         }
      }
   }
}
