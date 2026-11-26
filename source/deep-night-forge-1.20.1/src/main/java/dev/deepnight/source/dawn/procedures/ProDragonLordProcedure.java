package dev.deepnight.source.dawn.procedures;


import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.DLMultiPart2Entity;
import dev.deepnight.source.dawn.entity.DLMultiPart3Entity;
import dev.deepnight.source.dawn.entity.DLMultiPart4Entity;
import dev.deepnight.source.dawn.entity.DLMultiPart5Entity;
import dev.deepnight.source.dawn.entity.DLMultiPart6Entity;
import dev.deepnight.source.dawn.entity.DLMultiPart7Entity;
import dev.deepnight.source.dawn.entity.DragonSmokeEntity;
import dev.deepnight.source.dawn.entity.GroundbreakEffectBigEntity;
import dev.deepnight.source.dawn.entity.LightningSpearEntity;
import dev.deepnight.source.dawn.entity.MeteorAttackEntity;
import dev.deepnight.source.dawn.entity.Notchgroundeffect3Entity;
import dev.deepnight.source.dawn.entity.RodParticlesEntity;
import dev.deepnight.source.dawn.entity.Thundereffect2Entity;
import dev.deepnight.source.dawn.entity.Thundereffect3Entity;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.init.SimpleMobsModMobEffects;
import dev.deepnight.source.dawn.init.SimpleMobsModParticleTypes;
import dev.deepnight.source.dawn.network.SimpleMobsModVariables;
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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
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

public class ProDragonLordProcedure {
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
            if (!entity.getPersistentData().getBoolean("logged")) {
               entity.getPersistentData().putBoolean("logged", true);
               entity.getPersistentData().putBoolean("Spawn", true);
               entity.getPersistentData().putBoolean("Attacking", true);
            }

            if (!world.getEntitiesOfClass(DLMultiPart2Entity.class, AABB.ofSize(new Vec3(x, y, z), 50.0, 50.0, 50.0), e -> dev.deepnight.source.DragonLordParts.belongsTo(e, entity)).isEmpty()) {
               if (!world.getEntitiesOfClass(DLMultiPart3Entity.class, AABB.ofSize(new Vec3(x, y, z), 40.0, 40.0, 40.0), e -> dev.deepnight.source.DragonLordParts.belongsTo(e, entity)).isEmpty()) {
                  if (!world.getEntitiesOfClass(DLMultiPart4Entity.class, AABB.ofSize(new Vec3(x, y, z), 40.0, 40.0, 40.0), e -> dev.deepnight.source.DragonLordParts.belongsTo(e, entity)).isEmpty()) {
                     if (!world.getEntitiesOfClass(DLMultiPart5Entity.class, AABB.ofSize(new Vec3(x, y, z), 20.0, 20.0, 20.0), e -> dev.deepnight.source.DragonLordParts.belongsTo(e, entity)).isEmpty()) {
                        if (!world.getEntitiesOfClass(DLMultiPart6Entity.class, AABB.ofSize(new Vec3(x, y, z), 15.0, 15.0, 15.0), e -> dev.deepnight.source.DragonLordParts.belongsTo(e, entity)).isEmpty()) {
                           if (world.getEntitiesOfClass(DLMultiPart7Entity.class, AABB.ofSize(new Vec3(x, y, z), 40.0, 40.0, 40.0), e -> dev.deepnight.source.DragonLordParts.belongsTo(e, entity)).isEmpty()
                              && world instanceof ServerLevel _level) {
                              Entity entityToSpawn = new DLMultiPart7Entity(SimpleMobsModEntities.DL_MULTI_PART_7.get(), _level);
                              dev.deepnight.source.DragonLordParts.bind(entityToSpawn, entity);
                              entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
                              if (entityToSpawn instanceof Mob _mobToSpawn) {
                                 _mobToSpawn.finalizeSpawn(
                                    _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                                 );
                              }

                              world.addFreshEntity(entityToSpawn);
                           }
                        } else if (world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new DLMultiPart6Entity(SimpleMobsModEntities.DL_MULTI_PART_6.get(), _level);
                           dev.deepnight.source.DragonLordParts.bind(entityToSpawn, entity);
                           entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }
                     } else if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new DLMultiPart5Entity(SimpleMobsModEntities.DL_MULTI_PART_5.get(), _level);
                        dev.deepnight.source.DragonLordParts.bind(entityToSpawn, entity);
                        entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  } else if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new DLMultiPart4Entity(SimpleMobsModEntities.DL_MULTI_PART_4.get(), _level);
                     dev.deepnight.source.DragonLordParts.bind(entityToSpawn, entity);
                     entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               } else if (world instanceof ServerLevel _level) {
                  Entity entityToSpawn = new DLMultiPart3Entity(SimpleMobsModEntities.DL_MULTI_PART_3.get(), _level);
                  dev.deepnight.source.DragonLordParts.bind(entityToSpawn, entity);
                  entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
                  if (entityToSpawn instanceof Mob _mobToSpawn) {
                     _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                  }

                  world.addFreshEntity(entityToSpawn);
               }
            } else if (world instanceof ServerLevel _level) {
               Entity entityToSpawn = new DLMultiPart2Entity(SimpleMobsModEntities.DL_MULTI_PART_2.get(), _level);
               dev.deepnight.source.DragonLordParts.bind(entityToSpawn, entity);
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
               SimpleMobsModVariables.MapVariables.get(world).i = -5.0;
               SimpleMobsModVariables.MapVariables.get(world).syncData(world);
               SimpleMobsModVariables.MapVariables.get(world).j = 1.0;
               SimpleMobsModVariables.MapVariables.get(world).syncData(world);
               SimpleMobsModVariables.MapVariables.get(world).x = -5.0;
               SimpleMobsModVariables.MapVariables.get(world).syncData(world);

               while (SimpleMobsModVariables.MapVariables.get(world).i < 6.0) {
                  while (SimpleMobsModVariables.MapVariables.get(world).j < 8.0) {
                     while (SimpleMobsModVariables.MapVariables.get(world).x < 6.0) {
                        if (world.getBlockState(
                                    BlockPos.containing(
                                       x + SimpleMobsModVariables.MapVariables.get(world).i + entity.getLookAngle().x * 3.0,
                                       y + SimpleMobsModVariables.MapVariables.get(world).j,
                                       z + SimpleMobsModVariables.MapVariables.get(world).x + entity.getLookAngle().z * 3.0
                                    )
                                 )
                                 .getDestroySpeed(
                                    world,
                                    BlockPos.containing(
                                       x + SimpleMobsModVariables.MapVariables.get(world).i + entity.getLookAngle().x * 3.0,
                                       y + SimpleMobsModVariables.MapVariables.get(world).j,
                                       z + SimpleMobsModVariables.MapVariables.get(world).x + entity.getLookAngle().z * 3.0
                                    )
                                 )
                              >= 0.0F
                           && world.getBlockState(
                                    BlockPos.containing(
                                       x + SimpleMobsModVariables.MapVariables.get(world).i + entity.getLookAngle().x * 3.0,
                                       y + SimpleMobsModVariables.MapVariables.get(world).j,
                                       z + SimpleMobsModVariables.MapVariables.get(world).x + entity.getLookAngle().z * 3.0
                                    )
                                 )
                                 .getDestroySpeed(
                                    world,
                                    BlockPos.containing(
                                       x + SimpleMobsModVariables.MapVariables.get(world).i + entity.getLookAngle().x * 3.0,
                                       y + SimpleMobsModVariables.MapVariables.get(world).j,
                                       z + SimpleMobsModVariables.MapVariables.get(world).x + entity.getLookAngle().z * 3.0
                                    )
                                 )
                              <= 5.0F) {
                           world.destroyBlock(
                              BlockPos.containing(
                                 x + SimpleMobsModVariables.MapVariables.get(world).i + entity.getLookAngle().x * 3.0,
                                 y + SimpleMobsModVariables.MapVariables.get(world).j,
                                 z + SimpleMobsModVariables.MapVariables.get(world).x + entity.getLookAngle().z * 3.0
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

            entity.getPersistentData().putBoolean("Aggro", false);
            entity.getPersistentData().putBoolean("Far", false);
            entity.getPersistentData().putBoolean("Mid", false);
            entity.getPersistentData().putBoolean("Close", false);
            entity.getPersistentData().putBoolean("Around", false);
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(100.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (!(entity instanceof Mob _mobEnt) ? null : _mobEnt.getTarget())) {
                  entity.getPersistentData().putBoolean("Aggro", true);
                  entity.getPersistentData().putBoolean("Far", true);
                  entity.getPersistentData().putBoolean("Mid", false);
                  entity.getPersistentData().putBoolean("Close", false);
                  entity.getPersistentData().putBoolean("Around", false);
                  entity.getPersistentData()
                     .putDouble("atanyaw", Math.toDegrees(Math.atan2(entityiterator.getX() - entity.getX(), entityiterator.getZ() - entity.getZ())));
                  if (entity.getPersistentData().getDouble("atanyaw") < 0.0) {
                     entity.getPersistentData().putDouble("yaw2", 0.0 - entity.getPersistentData().getDouble("atanyaw"));
                  }

                  if (entity.getPersistentData().getDouble("atanyaw") >= 0.0) {
                     entity.getPersistentData().putDouble("yaw2", 360.0 - entity.getPersistentData().getDouble("atanyaw"));
                  }

                  entity.getPersistentData()
                     .putDouble(
                        "pitch2",
                        90.0
                           - Math.toDegrees(
                              Math.atan(
                                 Math.hypot(Math.abs(entity.getX() - entityiterator.getX()), Math.abs(entity.getZ() - entityiterator.getZ()))
                                    / Math.abs(entity.getY() + 3.0 - entityiterator.getY())
                              )
                           )
                     );
               }
            }

            _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(19.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (!(entity instanceof Mob _mobEnt) ? null : _mobEnt.getTarget())) {
                  entity.getPersistentData().putBoolean("Far", false);
                  entity.getPersistentData().putBoolean("Mid", true);
                  entity.getPersistentData().putBoolean("Close", false);
                  entity.getPersistentData().putBoolean("Around", false);
                  entity.getPersistentData().putBoolean("Behind", false);
               }
            }

            _center = new Vec3(x + entity.getLookAngle().x * 17.0, y, z + entity.getLookAngle().z * 17.0);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(8.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (!(entity instanceof Mob _mobEnt) ? null : _mobEnt.getTarget())) {
                  entity.getPersistentData().putBoolean("Far", false);
                  entity.getPersistentData().putBoolean("Mid", false);
                  entity.getPersistentData().putBoolean("Close", true);
                  entity.getPersistentData().putBoolean("Around", false);
                  entity.getPersistentData().putBoolean("Behind", false);
               }
            }

            _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.5), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (!(entity instanceof Mob _mobEnt) ? null : _mobEnt.getTarget())) {
                  entity.getPersistentData().putBoolean("Far", false);
                  entity.getPersistentData().putBoolean("Mid", false);
                  entity.getPersistentData().putBoolean("Close", false);
                  entity.getPersistentData().putBoolean("Around", true);
                  entity.getPersistentData().putBoolean("Behind", false);
               }
            }

            _center = new Vec3(x + entity.getLookAngle().x * -9.0, y, z + entity.getLookAngle().z * -9.0);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.5), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (!(entity instanceof Mob _mobEnt) ? null : _mobEnt.getTarget())) {
                  entity.getPersistentData().putBoolean("Far", false);
                  entity.getPersistentData().putBoolean("Mid", false);
                  entity.getPersistentData().putBoolean("Close", false);
                  entity.getPersistentData().putBoolean("Around", false);
                  entity.getPersistentData().putBoolean("Behind", true);
               }
            }

            if (entity.getPersistentData().getDouble("atk2cd") == 0.0 && entity.getPersistentData().getBoolean("Attack2")) {
               entity.getPersistentData().putDouble("atk2cd", 120.0);
            } else if (entity.getPersistentData().getDouble("atk2cd") > 0.0) {
               entity.getPersistentData().putDouble("atk2cd", entity.getPersistentData().getDouble("atk2cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("upcd") == 0.0 && entity.getPersistentData().getBoolean("Up")) {
               entity.getPersistentData().putDouble("upcd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 40, 70));
            } else if (entity.getPersistentData().getDouble("upcd") > 0.0) {
               entity.getPersistentData().putDouble("upcd", entity.getPersistentData().getDouble("upcd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("atk3cd") == 0.0 && entity.getPersistentData().getBoolean("Attack3")) {
               entity.getPersistentData().putDouble("atk3cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 400, 600));
            } else if (entity.getPersistentData().getDouble("atk3cd") > 0.0) {
               entity.getPersistentData().putDouble("atk3cd", entity.getPersistentData().getDouble("atk3cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("atk4cd") == 0.0 && entity.getPersistentData().getBoolean("Attack4")) {
               entity.getPersistentData().putDouble("atk4cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 400, 600));
            } else if (entity.getPersistentData().getDouble("atk4cd") > 0.0) {
               entity.getPersistentData().putDouble("atk4cd", entity.getPersistentData().getDouble("atk4cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("atk5cd") == 0.0 && entity.getPersistentData().getBoolean("Attack5")) {
               entity.getPersistentData().putDouble("atk5cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 200, 280));
            } else if (entity.getPersistentData().getDouble("atk5cd") > 0.0) {
               entity.getPersistentData().putDouble("atk5cd", entity.getPersistentData().getDouble("atk5cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("atk6cd") == 0.0 && entity.getPersistentData().getBoolean("Attack3")) {
               entity.getPersistentData().putDouble("atk6cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 200, 300));
            } else if (entity.getPersistentData().getDouble("atk6cd") > 0.0) {
               entity.getPersistentData().putDouble("atk6cd", entity.getPersistentData().getDouble("atk6cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("atk9cd") == 0.0 && entity.getPersistentData().getBoolean("Attack9")) {
               entity.getPersistentData().putDouble("atk9cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 60, 140));
            } else if (entity.getPersistentData().getDouble("atk9cd") > 0.0) {
               entity.getPersistentData().putDouble("atk9cd", entity.getPersistentData().getDouble("atk9cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("atk10cd") != 0.0
               || !entity.getPersistentData().getBoolean("Attack10") && !entity.getPersistentData().getBoolean("Attack10alt")) {
               if (entity.getPersistentData().getDouble("atk10cd") > 0.0) {
                  entity.getPersistentData().putDouble("atk10cd", entity.getPersistentData().getDouble("atk10cd") - 1.0);
               }
            } else {
               entity.getPersistentData().putDouble("atk10cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 100, 160));
            }

            if (entity.getPersistentData().getDouble("atk11cd") == 0.0 && entity.getPersistentData().getBoolean("Attack11")) {
               entity.getPersistentData().putDouble("atk11cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 500, 800));
            } else if (entity.getPersistentData().getDouble("atk11cd") > 0.0) {
               entity.getPersistentData().putDouble("atk11cd", entity.getPersistentData().getDouble("atk11cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("atk12cd") == 0.0 && entity.getPersistentData().getBoolean("Attack12")) {
               entity.getPersistentData().putDouble("atk12cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 200, 400));
            } else if (entity.getPersistentData().getDouble("atk12cd") > 0.0) {
               entity.getPersistentData().putDouble("atk12cd", entity.getPersistentData().getDouble("atk12cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("atk13cd") == 0.0 && entity.getPersistentData().getBoolean("Attack13")) {
               entity.getPersistentData().putDouble("atk13cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 100, 300));
            } else if (entity.getPersistentData().getDouble("atk13cd") > 0.0) {
               entity.getPersistentData().putDouble("atk13cd", entity.getPersistentData().getDouble("atk13cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("atk14cd") == 0.0 && entity.getPersistentData().getBoolean("Attack14")) {
               entity.getPersistentData().putDouble("atk14cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 300, 460));
            } else if (entity.getPersistentData().getDouble("atk14cd") > 0.0) {
               entity.getPersistentData().putDouble("atk14cd", entity.getPersistentData().getDouble("atk14cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("upcd2") == 0.0 && entity.getPersistentData().getBoolean("Up")) {
               entity.getPersistentData().putDouble("upcd2", Mth.nextInt(net.minecraft.util.RandomSource.create(), 400, 500));
            } else if (entity.getPersistentData().getDouble("upcd2") > 0.0) {
               entity.getPersistentData().putDouble("upcd2", entity.getPersistentData().getDouble("upcd2") - 1.0);
            }

            if (!entity.getPersistentData().getBoolean("air") && entity.onGround()) {
               if (!entity.getPersistentData().getBoolean("Around")) {
                  if (entity.getPersistentData().getBoolean("Behind") && !entity.getPersistentData().getBoolean("Attacking")) {
                     if ((!(entity instanceof LivingEntity _livEnt) ? -1.0F : _livEnt.getHealth())
                              / (!(entity instanceof LivingEntity _livEnt) ? -1.0F : _livEnt.getMaxHealth())
                           <= 0.5
                        && Math.random() >= 0.5
                        && !entity.getPersistentData().getBoolean("Attacking")
                        && entity.getPersistentData().getDouble("atk13cd") == 0.0) {
                        entity.getPersistentData().putBoolean("Attack13", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if ((!(entity instanceof LivingEntity _livEnt) ? -1.0F : _livEnt.getHealth())
                              / (!(entity instanceof LivingEntity _livEnt) ? -1.0F : _livEnt.getMaxHealth())
                           <= 0.5
                        && Math.random() <= 0.4
                        && !entity.getPersistentData().getBoolean("Attacking")
                        && entity.getPersistentData().getDouble("atk14cd") == 0.0) {
                        entity.getPersistentData().putBoolean("Attack14", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (!entity.getPersistentData().getBoolean("Attacking")) {
                        entity.getPersistentData().putBoolean("Attack2", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     }
                  } else if (entity.getPersistentData().getBoolean("Close") && !entity.getPersistentData().getBoolean("Attacking")) {
                     if ((!(entity instanceof LivingEntity _livEnt) ? -1.0F : _livEnt.getHealth())
                              / (!(entity instanceof LivingEntity _livEnt) ? -1.0F : _livEnt.getMaxHealth())
                           <= 0.5
                        && Math.random() >= 0.7
                        && !entity.getPersistentData().getBoolean("Attacking")
                        && entity.getPersistentData().getDouble("atk13cd") == 0.0) {
                        entity.getPersistentData().putBoolean("Attack13", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if ((!(entity instanceof LivingEntity _livEnt) ? -1.0F : _livEnt.getHealth())
                              / (!(entity instanceof LivingEntity _livEnt) ? -1.0F : _livEnt.getMaxHealth())
                           <= 0.5
                        && Math.random() <= 0.3
                        && !entity.getPersistentData().getBoolean("Attacking")
                        && entity.getPersistentData().getDouble("atk14cd") == 0.0) {
                        entity.getPersistentData().putBoolean("Attack14", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (Math.random() <= 0.25
                        && !entity.getPersistentData().getBoolean("Attacking")
                        && entity.getPersistentData().getDouble("atk10cd") == 0.0) {
                        entity.getPersistentData().putBoolean("Attack10", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (Math.random() >= 0.26
                        && Math.random() <= 0.5
                        && !entity.getPersistentData().getBoolean("Attacking")
                        && entity.getPersistentData().getDouble("atk10cd") == 0.0) {
                        entity.getPersistentData().putBoolean("Attack10alt", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (Math.random() >= 0.51
                        && Math.random() <= 0.75
                        && !entity.getPersistentData().getBoolean("Attacking")
                        && entity.getPersistentData().getDouble("atk9cd") == 0.0) {
                        entity.getPersistentData().putBoolean("Attack9", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (Math.random() >= 0.75
                        && !entity.getPersistentData().getBoolean("Attacking")
                        && entity.getPersistentData().getDouble("atk3cd") == 0.0) {
                        entity.getPersistentData().putBoolean("Attack3", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (entity.getPersistentData().getDouble("upcd2") == 0.0 && !entity.getPersistentData().getBoolean("Attacking")) {
                        entity.getPersistentData().putBoolean("Up", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     }
                  } else if (entity.getPersistentData().getBoolean("Mid") && !entity.getPersistentData().getBoolean("Attacking")) {
                     if ((!(entity instanceof LivingEntity _livEnt) ? -1.0F : _livEnt.getHealth())
                              / (!(entity instanceof LivingEntity _livEnt) ? -1.0F : _livEnt.getMaxHealth())
                           <= 0.5
                        && Math.random() <= 0.8
                        && !entity.getPersistentData().getBoolean("Attacking")
                        && entity.getPersistentData().getDouble("atk13cd") == 0.0) {
                        entity.getPersistentData().putBoolean("Attack13", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (!entity.getPersistentData().getBoolean("Attacking")
                        && (!(entity instanceof LivingEntity _livEnt) ? -1.0F : _livEnt.getHealth())
                              / (!(entity instanceof LivingEntity _livEnt) ? -1.0F : _livEnt.getMaxHealth())
                           <= 0.5
                        && Math.random() <= 0.8
                        && entity.getPersistentData().getDouble("atk11cd") == 0.0) {
                        entity.getPersistentData().putBoolean("Attack11a", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (!entity.getPersistentData().getBoolean("Attacking")
                        && Math.random() <= 0.3
                        && entity.getPersistentData().getDouble("atk11cd") == 0.0) {
                        entity.getPersistentData().putBoolean("Attack11", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (Math.random() >= 0.6
                        && !entity.getPersistentData().getBoolean("Attacking")
                        && entity.getPersistentData().getDouble("atk9cd") == 0.0) {
                        entity.getPersistentData().putBoolean("Attack9", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     }
                  } else if (entity.getPersistentData().getBoolean("Far") && !entity.getPersistentData().getBoolean("Attacking")) {
                     if ((!(entity instanceof LivingEntity _livEnt) ? -1.0F : _livEnt.getHealth())
                              / (!(entity instanceof LivingEntity _livEnt) ? -1.0F : _livEnt.getMaxHealth())
                           <= 0.5
                        && Math.random() <= 0.8
                        && !entity.getPersistentData().getBoolean("Attacking")
                        && entity.getPersistentData().getDouble("atk13cd") == 0.0) {
                        entity.getPersistentData().putBoolean("Attack13", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (Math.random() >= 0.6
                        && !entity.getPersistentData().getBoolean("Attacking")
                        && entity.getPersistentData().getDouble("atk4cd") == 0.0) {
                        entity.getPersistentData().putBoolean("Attack4", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (Math.random() >= 0.6
                        && !entity.getPersistentData().getBoolean("Attacking")
                        && entity.getPersistentData().getDouble("atk5cd") == 0.0) {
                        entity.getPersistentData().putBoolean("Attack5", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if ((!(entity instanceof LivingEntity _livEnt) ? -1.0F : _livEnt.getHealth())
                              / (!(entity instanceof LivingEntity _livEnt) ? -1.0F : _livEnt.getMaxHealth())
                           <= 0.5
                        && Math.random() <= 0.7
                        && entity.getPersistentData().getDouble("atk11cd") == 0.0
                        && !entity.getPersistentData().getBoolean("Attacking")) {
                        entity.getPersistentData().putBoolean("Attack11a", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (Math.random() <= 0.1
                        && entity.getPersistentData().getDouble("atk11cd") == 0.0
                        && !entity.getPersistentData().getBoolean("Attacking")) {
                        entity.getPersistentData().putBoolean("Attack11", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (entity.getPersistentData().getDouble("upcd2") == 0.0 && !entity.getPersistentData().getBoolean("Attacking")) {
                        entity.getPersistentData().putBoolean("Up", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     }
                  }
               } else if ((!(entity instanceof LivingEntity _livEnt) ? -1.0F : _livEnt.getHealth())
                        / (!(entity instanceof LivingEntity _livEnt) ? -1.0F : _livEnt.getMaxHealth())
                     <= 0.5
                  && Math.random() >= 0.6
                  && !entity.getPersistentData().getBoolean("Attacking")
                  && entity.getPersistentData().getDouble("atk13cd") == 0.0) {
                  entity.getPersistentData().putBoolean("Attack13", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if ((!(entity instanceof LivingEntity _livEnt) ? -1.0F : _livEnt.getHealth())
                        / (!(entity instanceof LivingEntity _livEnt) ? -1.0F : _livEnt.getMaxHealth())
                     <= 0.5
                  && Math.random() <= 0.4
                  && !entity.getPersistentData().getBoolean("Attacking")
                  && entity.getPersistentData().getDouble("atk14cd") == 0.0) {
                  entity.getPersistentData().putBoolean("Attack14", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (Math.random() <= 0.3 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack1", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (Math.random() >= 0.31
                  && Math.random() <= 0.6
                  && !entity.getPersistentData().getBoolean("Attacking")
                  && entity.getPersistentData().getDouble("atk3cd") == 0.0) {
                  entity.getPersistentData().putBoolean("Attack3", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (Math.random() >= 0.61
                  && Math.random() <= 0.8
                  && !entity.getPersistentData().getBoolean("Attacking")
                  && entity.getPersistentData().getDouble("atk6cd") == 0.0) {
                  entity.getPersistentData().putBoolean("Attack6", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (entity.getPersistentData().getDouble("upcd2") == 0.0 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Up", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (entity.getPersistentData().getDouble("atk11cd") == 0.0 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack11", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (entity.getPersistentData().getDouble("atk12cd") == 0.0 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack12", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               }
            } else if (entity.getPersistentData().getDouble("aircd") == 0.0 && entity.getPersistentData().getBoolean("air")) {
               if (Math.random() <= 0.5 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack7", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (!entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack8", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("atk8done", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Spawn")) {
               if (entity.getPersistentData().getDouble("l") != 0.0) {
                  entity.getPersistentData().putDouble("l", entity.getPersistentData().getDouble("l") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("l", 60.0);
               }

               if (entity.getPersistentData().getDouble("l") != 60.0) {
                  if (entity.getPersistentData().getDouble("l") == 1.0) {
                     Entity _entity = entity;
                     if (!(_entity instanceof Player _player)) {
                        if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                        }
                     } else {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("l", 0.0);
                     entity.getPersistentData().putBoolean("Spawn", false);
                  }
               } else {
                  Entity _entity = entity;
                  if (!(_entity instanceof Player _player)) {
                     if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.TORCH));
                     }
                  } else {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.TORCH));
                     _player.getInventory().setChanged();
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack1")) {
               if (entity.getPersistentData().getDouble("atk1") != 0.0) {
                  entity.getPersistentData().putDouble("atk1", entity.getPersistentData().getDouble("atk1") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("atk1", 71.0);
               }

               if (entity.getPersistentData().getDouble("atk1") == 71.0) {
                  Entity _entity = entity;
                  if (!(_entity instanceof Player _player)) {
                     if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                     }
                  } else {
                     _player.getInventory().armor.set(3, new ItemStack(Items.LEATHER_HELMET));
                     _player.getInventory().setChanged();
                  }

                  entity.getPersistentData().putDouble("xz", entity.getYRot());
                  entity.getPersistentData().putDouble("y", entity.getXRot());
               } else if (entity.getPersistentData().getDouble("atk1") != 65.0) {
                  if (entity.getPersistentData().getDouble("atk1") == 42.0) {
                     if (world instanceof Level _level) {
                        if (_level.isClientSide()) {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              0.5F,
                              false
                           );
                        } else {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              0.5F
                           );
                        }
                     }

                     if (world instanceof Level _level) {
                        if (_level.isClientSide()) {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              0.5F,
                              false
                           );
                        } else {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              0.5F
                           );
                        }
                     }

                     if (world instanceof Level _level) {
                        if (_level.isClientSide()) {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:notch_sweep"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F,
                              false
                           );
                        } else {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:notch_sweep"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        }
                     }

                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new Notchgroundeffect3Entity(SimpleMobsModEntities.NOTCHGROUNDEFFECT_3.get(), _level);
                        entityToSpawn.moveTo(
                           x - entity.getLookAngle().x * 2.0, y, z - entity.getLookAngle().z * 2.0, world.getRandom().nextFloat() * 360.0F, 0.0F
                        );
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(
                           (SimpleParticleType)SimpleMobsModParticleTypes.EXPLOSION.get(),
                           x - entity.getLookAngle().x * 2.0,
                           y,
                           z - entity.getLookAngle().z * 2.0,
                           40,
                           3.0,
                           3.0,
                           3.0,
                           1.0
                        );
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(
                           ParticleTypes.CAMPFIRE_COSY_SMOKE, x - entity.getLookAngle().x * 2.0, y, z - entity.getLookAngle().z * 2.0, 40, 3.0, 3.0, 3.0, 1.0
                        );
                     }

                     if (world instanceof ServerLevel _level) {
                        Vec3 look = entity.getLookAngle();
                        Vec3 tangent = new Vec3(-look.z, 0.0, look.x);
                        Entity entityToSpawn = new GroundbreakEffectBigEntity(SimpleMobsModEntities.GROUNDBREAK_EFFECT_BIG.get(), _level);
                        entityToSpawn.moveTo(
                           x - entity.getLookAngle().x * 2.0 + -3.0 * tangent.x,
                           y,
                           z - entity.getLookAngle().z * 2.0 + -3.0 * tangent.z,
                           world.getRandom().nextFloat() * 360.0F,
                           0.0F
                        );
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  } else if (entity.getPersistentData().getDouble("atk1") < 42.0 && entity.getPersistentData().getDouble("atk1") > 39.0) {
                     _center = new Vec3(x - entity.getLookAngle().x * 2.0, y, z - entity.getLookAngle().z * 2.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("dragon_lord")) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.LONG)
                              .setImpact(2.0F)
                              .setArmorNegation(25.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 24.0F);
                        }
                     }

                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(12.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if ((entityiterator instanceof Player || entityiterator instanceof ServerPlayer) && entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 20, 1, false, false));
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("atk1") != 27.0) {
                     if (entity.getPersistentData().getDouble("atk1") == 1.0) {
                        Entity _entity = entity;
                        if (!(_entity instanceof Player _player)) {
                           if (_entity instanceof LivingEntity _living) {
                              _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                           }
                        } else {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                           _player.getInventory().setChanged();
                        }

                        entity.getPersistentData().putBoolean("lock", false);
                        entity.getPersistentData().putBoolean("noai", false);
                        entity.getPersistentData().putDouble("atk1", 0.0);
                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putBoolean("Attack1", false);
                     }
                  } else {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("dragon_lord")) {
                           entity.getPersistentData().putBoolean("Attack1b", true);
                           entity.getPersistentData().putDouble("atk1", 0.0);
                           entity.getPersistentData().putBoolean("Attack1", false);
                        }
                     }
                  }
               } else {
                  entity.getPersistentData().putBoolean("lock", true);
                  entity.getPersistentData().putBoolean("noai", true);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack1b")) {
               if (entity.getPersistentData().getDouble("atk1b") != 0.0) {
                  entity.getPersistentData().putDouble("atk1b", entity.getPersistentData().getDouble("atk1b") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("atk1b", 51.0);
               }

               if (entity.getPersistentData().getDouble("atk1b") != 51.0) {
                  if (entity.getPersistentData().getDouble("atk1b") != 34.0) {
                     if (entity.getPersistentData().getDouble("atk1b") < 34.0 && entity.getPersistentData().getDouble("atk1b") > 30.0) {
                        _center = new Vec3(x, y, z);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.5), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (!entityiterator.getPersistentData().getBoolean("dragon_lord")) {
                              if (entityiterator instanceof LivingEntity _entity) {
                                 _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 20, 0));
                              }

                              SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                 .setInitialPosition(entity.position())
                                 .setStunType(StunType.LONG)
                                 .setImpact(2.0F)
                                 .setArmorNegation(25.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 24.0F);
                           }
                        }

                        _center = new Vec3(x, y, z);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(12.5), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if ((entityiterator instanceof Player || entityiterator instanceof ServerPlayer) && entityiterator instanceof LivingEntity _entity) {
                              _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 20, 1, false, false));
                           }
                        }
                     } else if (entity.getPersistentData().getDouble("atk1b") == 1.0) {
                        Entity _entity = entity;
                        if (!(_entity instanceof Player _player)) {
                           if (_entity instanceof LivingEntity _living) {
                              _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                           }
                        } else {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                           _player.getInventory().setChanged();
                        }

                        entity.getPersistentData().putDouble("atk1b", 0.0);
                        if (world instanceof ServerLevel _level) {
                           _level.getServer()
                              .getCommands()
                              .performPrefixedCommand(
                                 new CommandSourceStack(
                                       CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                    )
                                    .withSuppressedOutput(),
                                 "/data merge entity @e[type=deepnight:dawn_dragon_lord,limit=1,sort=nearest] {NoAI:0}"
                              );
                        }

                        entity.getPersistentData().putBoolean("lock", false);
                        entity.getPersistentData().putBoolean("noai", false);
                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putBoolean("Attack1b", false);
                     }
                  } else {
                     if (world instanceof Level _level) {
                        if (_level.isClientSide()) {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              0.5F,
                              false
                           );
                        } else {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              0.5F
                           );
                        }
                     }

                     if (world instanceof Level _level) {
                        if (_level.isClientSide()) {
                           _level.playLocalSound(
                              x + entity.getLookAngle().x,
                              y,
                              z + entity.getLookAngle().z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              0.5F,
                              false
                           );
                        } else {
                           _level.playSound(
                              null,
                              BlockPos.containing(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              0.5F
                           );
                        }
                     }

                     if (world instanceof ServerLevel _level) {
                        Vec3 look = entity.getLookAngle();
                        Vec3 tangent = new Vec3(-look.z, 0.0, look.x);
                        Entity entityToSpawn = new GroundbreakEffectBigEntity(SimpleMobsModEntities.GROUNDBREAK_EFFECT_BIG.get(), _level);
                        entityToSpawn.moveTo(
                           x - entity.getLookAngle().x * 1.0 + 3.0 * tangent.x,
                           y,
                           z - entity.getLookAngle().z * 1.0 + 3.0 * tangent.z,
                           world.getRandom().nextFloat() * 360.0F,
                           0.0F
                        );
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(
                           (SimpleParticleType)SimpleMobsModParticleTypes.EXPLOSION.get(),
                           x + entity.getLookAngle().x,
                           y,
                           z + entity.getLookAngle().z,
                           40,
                           3.0,
                           3.0,
                           3.0,
                           1.0
                        );
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(
                           ParticleTypes.CAMPFIRE_COSY_SMOKE, x + entity.getLookAngle().x, y, z + entity.getLookAngle().z, 40, 3.0, 3.0, 3.0, 1.0
                        );
                     }
                  }
               } else {
                  Entity _entity = entity;
                  if (!(_entity instanceof Player _player)) {
                     if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLD_NUGGET));
                     }
                  } else {
                     _player.getInventory().armor.set(3, new ItemStack(Items.GOLD_NUGGET));
                     _player.getInventory().setChanged();
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 41, 6, false, false));
                  }
               }

               if (entity.getPersistentData().getDouble("atk1b") < 50.0 && entity.getPersistentData().getDouble("atk1b") > 40.0) {
                  entity.setDeltaMovement(
                     new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 0.5, 0.0, Math.cos(Math.toRadians(entity.getYRot())) * 0.5)
                  );
               }
            }

            if (entity.getPersistentData().getBoolean("Attack2")) {
               if (entity.getPersistentData().getDouble("atk2") != 0.0) {
                  entity.getPersistentData().putDouble("atk2", entity.getPersistentData().getDouble("atk2") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("atk2", 51.0);
               }

               if (entity.getPersistentData().getDouble("atk2") != 51.0) {
                  if (entity.getPersistentData().getDouble("atk2") != 50.0) {
                     if (entity.getPersistentData().getDouble("atk2") != 39.0) {
                        if (entity.getPersistentData().getDouble("atk2") != 10.0) {
                           if (entity.getPersistentData().getDouble("atk2") == 1.0) {
                              entity.getPersistentData().putBoolean("sweep", false);
                              Entity _entity = entity;
                              if (!(_entity instanceof Player _player)) {
                                 if (_entity instanceof LivingEntity _living) {
                                    _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                                 }
                              } else {
                                 _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                                 _player.getInventory().setChanged();
                              }

                              entity.getPersistentData().putDouble("atk2", 0.0);
                              entity.getPersistentData().putBoolean("Attacking", false);
                              entity.getPersistentData().putBoolean("Attack2", false);
                           }
                        } else {
                           if (world instanceof ServerLevel _level) {
                              _level.getServer()
                                 .getCommands()
                                 .performPrefixedCommand(
                                    new CommandSourceStack(
                                          CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                       )
                                       .withSuppressedOutput(),
                                    "/data merge entity @e[type=deepnight:dawn_dragon_lord,limit=1,sort=nearest] {NoAI:0}"
                                 );
                           }

                           entity.getPersistentData().putBoolean("lock", false);
                        }
                     } else {
                        if (world instanceof Level _level) {
                           if (_level.isClientSide()) {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:sweep"),
                                 SoundSource.NEUTRAL,
                                 1.0F,
                                 1.0F,
                                 false
                              );
                           } else {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:sweep"),
                                 SoundSource.NEUTRAL,
                                 1.0F,
                                 1.0F
                              );
                           }
                        }

                        entity.getPersistentData().putBoolean("sweep", true);
                     }
                  } else {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_dragon_lord,limit=1,sort=nearest] {NoAI:1b}"
                           );
                     }

                     entity.getPersistentData().putBoolean("lock", true);
                  }
               } else {
                  Entity _entity = entity;
                  if (!(_entity instanceof Player _player)) {
                     if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.COAL));
                     }
                  } else {
                     _player.getInventory().armor.set(3, new ItemStack(Items.COAL));
                     _player.getInventory().setChanged();
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 51, 7, false, false));
                  }

                  entity.getPersistentData().putDouble("xz", entity.getYRot());
                  entity.getPersistentData().putDouble("y", entity.getXRot());
                  entity.getPersistentData().putDouble("i", 10.0);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack3")) {
               if (!(entity.getPersistentData().getDouble("Atk3") <= 0.0)) {
                  entity.getPersistentData().putDouble("Atk3", entity.getPersistentData().getDouble("Atk3") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("Atk3", 157.0);
               }

               if (entity.getPersistentData().getDouble("Atk3") == 157.0) {
                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("focus", true);
                  Entity _entity = entity;
                  if (!(_entity instanceof Player _player)) {
                     if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHARCOAL));
                     }
                  } else {
                     _player.getInventory().armor.set(3, new ItemStack(Items.CHARCOAL));
                     _player.getInventory().setChanged();
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 104, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk3") == 137.0) {
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putDouble("y", 0.0);
                  entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putBoolean("lock", true);
                  entity.getPersistentData().putBoolean("noai", true);
               } else if (entity.getPersistentData().getDouble("Atk3") == 132.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_dragon_lord,limit=1,sort=nearest] {NoAI:1b}"
                        );
                  }
               } else if (entity.getPersistentData().getDouble("Atk3") == 127.0) {
                  Vec3 look = entity.getLookAngle();
                  Vec3 tangent = new Vec3(-look.z, 0.0, look.x);
                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        (SimpleParticleType)SimpleMobsModParticleTypes.THUNDER_FLASH_1.get(),
                        entity.getX() + entity.getLookAngle().x * 8.0 + 8.0 * tangent.x,
                        y + 8.0,
                        entity.getZ() + entity.getLookAngle().z * 8.0 + 8.0 * tangent.z,
                        3,
                        0.4,
                        4.0,
                        0.4,
                        0.0
                     );
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.FLASH,
                        entity.getX() + entity.getLookAngle().x * 8.0 + 8.0 * tangent.x,
                        y + 8.0,
                        entity.getZ() + entity.getLookAngle().z * 8.0 + 8.0 * tangent.z,
                        3,
                        0.4,
                        4.0,
                        0.4,
                        0.0
                     );
                  }
               } else if (entity.getPersistentData().getDouble("Atk3") == 110.0) {
                  if (world instanceof Level _level) {
                     if (_level.isClientSide()) {
                        _level.playLocalSound(
                           x + entity.getLookAngle().x * 10.0,
                           y,
                           z + entity.getLookAngle().z * 10.0,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:spell_form"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F,
                           false
                        );
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x + entity.getLookAngle().x * 10.0, y, z + entity.getLookAngle().z * 10.0),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:spell_form"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F
                        );
                     }
                  }

                  if (world instanceof Level _level) {
                     if (_level.isClientSide()) {
                        _level.playLocalSound(
                           x + entity.getLookAngle().x * 10.0,
                           y,
                           z + entity.getLookAngle().z * 10.0,
                           dev.deepnight.SafeSounds.resolve("item.totem.use"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F,
                           false
                        );
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x + entity.getLookAngle().x * 10.0, y, z + entity.getLookAngle().z * 10.0),
                           dev.deepnight.SafeSounds.resolve("item.totem.use"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F
                        );
                     }
                  }

                  if (world instanceof Level _level) {
                     if (_level.isClientSide()) {
                        _level.playLocalSound(
                           x + entity.getLookAngle().x * 10.0,
                           y,
                           z + entity.getLookAngle().z * 10.0,
                           dev.deepnight.SafeSounds.resolve("entity.lightning_bolt.thunder"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F,
                           false
                        );
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x + entity.getLookAngle().x * 10.0, y, z + entity.getLookAngle().z * 10.0),
                           dev.deepnight.SafeSounds.resolve("entity.lightning_bolt.thunder"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk3") != 107.0) {
                  if (entity.getPersistentData().getDouble("Atk3") != 4.0) {
                     if (entity.getPersistentData().getDouble("Atk3") == 1.0) {
                        Entity _entity = entity;
                        if (!(_entity instanceof Player _player)) {
                           if (_entity instanceof LivingEntity _living) {
                              _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                           }
                        } else {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                           _player.getInventory().setChanged();
                        }

                        entity.getPersistentData().putDouble("Atk3", 0.0);
                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putBoolean("Attack3", false);
                     }
                  } else {
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putBoolean("noai", false);
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_dragon_lord,limit=1,sort=nearest] {NoAI:0}"
                           );
                     }
                  }
               } else {
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new Thundereffect2Entity(SimpleMobsModEntities.THUNDEREFFECT_2.get(), _level);
                     entityToSpawn.moveTo(
                        x + entity.getLookAngle().x * 10.0, y, z + entity.getLookAngle().z * 10.0, world.getRandom().nextFloat() * 360.0F, 0.0F
                     );
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  _center = new Vec3(x + entity.getLookAngle().x * 10.0, y, z + entity.getLookAngle().z * 10.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(6.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!entityiterator.getPersistentData().getBoolean("dragon_lord")) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.SHORT)
                           .setImpact(2.0F)
                           .setArmorNegation(25.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 30.0F);
                     }
                  }

                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(13.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if ((entityiterator instanceof Player || entityiterator instanceof ServerPlayer) && entityiterator instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 40, 1, false, false));
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack4")) {
               if (!(entity.getPersistentData().getDouble("Atk4") <= 0.0)) {
                  entity.getPersistentData().putDouble("Atk4", entity.getPersistentData().getDouble("Atk4") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("Atk4", 106.0);
               }

               if (entity.getPersistentData().getDouble("Atk4") == 106.0) {
                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("focus", true);
                  Entity _entity = entity;
                  if (!(_entity instanceof Player _player)) {
                     if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_INGOT));
                     }
                  } else {
                     _player.getInventory().armor.set(3, new ItemStack(Items.IRON_INGOT));
                     _player.getInventory().setChanged();
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 104, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk4") == 87.0) {
                  entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putDouble("y", entity.getPersistentData().getDouble("pitch2") + 5.0);
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(75.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (!(entity instanceof Mob _mobEnt) ? null : _mobEnt.getTarget())) {
                        entityiterator.getPersistentData().putBoolean("dltarget", true);
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk4") == 86.0) {
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putBoolean("lock", true);
                  if (world instanceof Level _level) {
                     if (_level.isClientSide()) {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_flame"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           0.5F,
                           false
                        );
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_flame"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           0.5F
                        );
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
                           "/data merge entity @e[type=deepnight:dawn_dragon_lord,limit=1,sort=nearest] {NoAI:1b}"
                        );
                  }
               } else if (entity.getPersistentData().getDouble("Atk4") == 83.0) {
                  entity.getPersistentData().putBoolean("fire1", true);
               } else if (entity.getPersistentData().getDouble("Atk4") == 20.0) {
                  entity.getPersistentData().putBoolean("fire1", false);
               } else if (entity.getPersistentData().getDouble("Atk4") == 6.0) {
                  entity.getPersistentData().putBoolean("lock", false);
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_dragon_lord,limit=1,sort=nearest] {NoAI:0}"
                        );
                  }
               } else if (entity.getPersistentData().getDouble("Atk4") == 1.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(75.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (!(entity instanceof Mob _mobEnt) ? null : _mobEnt.getTarget())) {
                        entityiterator.getPersistentData().putBoolean("dltarget", false);
                     }
                  }

                  Entity _entity = entity;
                  if (!(_entity instanceof Player _player)) {
                     if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }
                  } else {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  }

                  entity.getPersistentData().putDouble("Atk4", 0.0);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("Attack4", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack5")) {
               if (entity.getPersistentData().getDouble("atk5") != 0.0) {
                  entity.getPersistentData().putDouble("atk5", entity.getPersistentData().getDouble("atk5") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("atk5", 106.0);
               }

               if (entity.getPersistentData().getDouble("atk5") != 106.0) {
                  if (entity.getPersistentData().getDouble("atk5") != 86.0) {
                     if (entity.getPersistentData().getDouble("atk5") != 85.0) {
                        if (entity.getPersistentData().getDouble("atk5") != 72.0) {
                           if (entity.getPersistentData().getDouble("atk5") != 31.0) {
                              if (entity.getPersistentData().getDouble("atk5") == 1.0) {
                                 Entity _entity = entity;
                                 if (!(_entity instanceof Player _player)) {
                                    if (_entity instanceof LivingEntity _living) {
                                       _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                                    }
                                 } else {
                                    _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                                    _player.getInventory().setChanged();
                                 }

                                 entity.getPersistentData().putBoolean("lock", false);
                                 entity.getPersistentData().putBoolean("noai", false);
                                 entity.getPersistentData().putDouble("atk5", 0.0);
                                 entity.getPersistentData().putBoolean("Attacking", false);
                                 entity.getPersistentData().putBoolean("Attack5", false);
                              }
                           } else {
                              entity.getPersistentData().putBoolean("Spear", true);
                           }
                        } else {
                           if (world instanceof Level _level) {
                              if (_level.isClientSide()) {
                                 _level.playLocalSound(
                                    x,
                                    y,
                                    z,
                                    dev.deepnight.SafeSounds.resolve("simple_mobs:spell_form"),
                                    SoundSource.NEUTRAL,
                                    4.0F,
                                    0.3F,
                                    false
                                 );
                              } else {
                                 _level.playSound(
                                    null,
                                    BlockPos.containing(x, y, z),
                                    dev.deepnight.SafeSounds.resolve("simple_mobs:spell_form"),
                                    SoundSource.NEUTRAL,
                                    4.0F,
                                    0.3F
                                 );
                              }
                           }

                           if (world instanceof Level _level) {
                              if (_level.isClientSide()) {
                                 _level.playLocalSound(
                                    x,
                                    y,
                                    z,
                                    dev.deepnight.SafeSounds.resolve("simple_mobs:electricity_charge"),
                                    SoundSource.NEUTRAL,
                                    4.0F,
                                    0.5F,
                                    false
                                 );
                              } else {
                                 _level.playSound(
                                    null,
                                    BlockPos.containing(x, y, z),
                                    dev.deepnight.SafeSounds.resolve("simple_mobs:electricity_charge"),
                                    SoundSource.NEUTRAL,
                                    4.0F,
                                    0.5F
                                 );
                              }
                           }

                           if (world instanceof Level _level) {
                              if (_level.isClientSide()) {
                                 _level.playLocalSound(
                                    x,
                                    y,
                                    z,
                                    dev.deepnight.SafeSounds.resolve("simple_mobs:notch_sweep"),
                                    SoundSource.NEUTRAL,
                                    4.0F,
                                    1.0F,
                                    false
                                 );
                              } else {
                                 _level.playSound(
                                    null,
                                    BlockPos.containing(x, y, z),
                                    dev.deepnight.SafeSounds.resolve("simple_mobs:notch_sweep"),
                                    SoundSource.NEUTRAL,
                                    4.0F,
                                    1.0F
                                 );
                              }
                           }

                           Vec3 look = entity.getLookAngle();
                           Vec3 tangent = new Vec3(-look.z, 0.0, look.x);
                           if (world instanceof ServerLevel _level) {
                              _level.sendParticles(
                                 (SimpleParticleType)SimpleMobsModParticleTypes.THUNDER_FLASH_1.get(),
                                 entity.getX() + entity.getLookAngle().x * -5.0 + 8.0 * tangent.x,
                                 y + 8.0,
                                 entity.getZ() + entity.getLookAngle().z * -5.0 + 8.0 * tangent.z,
                                 3,
                                 0.4,
                                 4.0,
                                 0.4,
                                 0.0
                              );
                           }

                           if (world instanceof ServerLevel _level) {
                              _level.sendParticles(
                                 ParticleTypes.FLASH,
                                 entity.getX() + entity.getLookAngle().x * -5.0 + 8.0 * tangent.x,
                                 y + 8.0,
                                 entity.getZ() + entity.getLookAngle().z * -5.0 + 8.0 * tangent.z,
                                 3,
                                 0.4,
                                 4.0,
                                 0.4,
                                 0.0
                              );
                           }
                        }
                     } else {
                        entity.getPersistentData().putBoolean("lock", true);
                        entity.getPersistentData().putBoolean("noai", true);
                     }
                  } else {
                     entity.getPersistentData().putDouble("xz", entity.getYRot());
                     entity.getPersistentData().putDouble("y", entity.getXRot());
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(120.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator == (!(entity instanceof Mob _mobEnt) ? null : _mobEnt.getTarget())) {
                           entityiterator.getPersistentData().putBoolean("dltarget", true);
                        }
                     }
                  }
               } else {
                  Entity _entity = entity;
                  if (!(_entity instanceof Player _player)) {
                     if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_NUGGET));
                     }
                  } else {
                     _player.getInventory().armor.set(3, new ItemStack(Items.IRON_NUGGET));
                     _player.getInventory().setChanged();
                  }

                  entity.getPersistentData().putBoolean("focus", true);
               }
            }

            if (entity.getPersistentData().getBoolean("Spear")) {
               if (entity.getPersistentData().getDouble("spear") != 0.0) {
                  entity.getPersistentData().putDouble("spear", entity.getPersistentData().getDouble("spear") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("spear", 61.0);
               }

               if (entity.getPersistentData().getDouble("spear") == 61.0
                  || entity.getPersistentData().getDouble("spear") == 51.0
                  || entity.getPersistentData().getDouble("spear") == 41.0
                  || entity.getPersistentData().getDouble("spear") == 31.0
                  || entity.getPersistentData().getDouble("spear") == 21.0
                  || entity.getPersistentData().getDouble("spear") == 11.0
                  || entity.getPersistentData().getDouble("spear") == 1.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(120.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator.getPersistentData().getBoolean("dltarget") && world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new LightningSpearEntity(SimpleMobsModEntities.LIGHTNING_SPEAR.get(), _level);
                        entityToSpawn.moveTo(
                           entityiterator.getX() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -10, 10),
                           entityiterator.getY() + 80.0,
                           entityiterator.getZ() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -10, 10),
                           world.getRandom().nextFloat() * 360.0F,
                           0.0F
                        );
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  }
               }

               if (entity.getPersistentData().getDouble("spear") == 1.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(120.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator.getPersistentData().getBoolean("dltarget")
                        || entityiterator == (!(entity instanceof Mob _mobEnt) ? null : _mobEnt.getTarget())) {
                        entityiterator.getPersistentData().putBoolean("dltarget", false);
                     }
                  }

                  entity.getPersistentData().putDouble("spear", 0.0);
                  entity.getPersistentData().putBoolean("Spear", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack6")) {
               if (!(entity.getPersistentData().getDouble("Atk6") <= 0.0)) {
                  entity.getPersistentData().putDouble("Atk6", entity.getPersistentData().getDouble("Atk6") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("Atk6", 51.0);
               }

               if (entity.getPersistentData().getDouble("Atk6") != 51.0) {
                  if (entity.getPersistentData().getDouble("Atk6") != 50.0) {
                     if (entity.getPersistentData().getDouble("Atk6") != 33.0) {
                        if (entity.getPersistentData().getDouble("Atk6") != 10.0) {
                           if (entity.getPersistentData().getDouble("Atk6") == 1.0) {
                              Entity _entity = entity;
                              if (!(_entity instanceof Player _player)) {
                                 if (_entity instanceof LivingEntity _living) {
                                    _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                                 }
                              } else {
                                 _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                                 _player.getInventory().setChanged();
                              }

                              entity.getPersistentData().putDouble("Atk6", 0.0);
                              entity.getPersistentData().putBoolean("Attacking", false);
                              entity.getPersistentData().putBoolean("Attack6", false);
                           }
                        } else {
                           entity.getPersistentData().putBoolean("lock", false);
                           entity.getPersistentData().putBoolean("noai", false);
                        }
                     } else {
                        if (world instanceof Level _level) {
                           if (_level.isClientSide()) {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_wing_flap3"),
                                 SoundSource.NEUTRAL,
                                 3.0F,
                                 1.0F,
                                 false
                              );
                           } else {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_wing_flap3"),
                                 SoundSource.NEUTRAL,
                                 3.0F,
                                 1.0F
                              );
                           }
                        }

                        entity.setDeltaMovement(
                           new Vec3(Math.sin(Math.toRadians(entity.getYRot())) * 3.0, 1.6, Math.cos(Math.toRadians(entity.getYRot() + 180.0F)) * 3.0)
                        );
                     }
                  } else {
                     entity.getPersistentData().putBoolean("lock", true);
                     entity.getPersistentData().putBoolean("noai", true);
                  }
               } else {
                  entity.getPersistentData().putBoolean("Attacking", true);
                  Entity _entity = entity;
                  if (!(_entity instanceof Player _player)) {
                     if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.WHITE_WOOL));
                     }
                  } else {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.WHITE_WOOL));
                     _player.getInventory().setChanged();
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 76, 6, false, false));
                  }

                  entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putDouble("y", entity.getXRot());
               }

               if (entity.getPersistentData().getDouble("Atk6") < 35.0 && entity.getPersistentData().getDouble("Atk6") >= 30.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!entityiterator.getPersistentData().getBoolean("dragon_lord")) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(2.0F)
                           .setArmorNegation(15.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 20.0F);
                        entityiterator.setDeltaMovement(
                           new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 1.7, 0.7, Math.cos(Math.toRadians(entity.getYRot())) * 1.7)
                        );
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Up")) {
               if (!(entity.getPersistentData().getDouble("up") <= 0.0)) {
                  entity.getPersistentData().putDouble("up", entity.getPersistentData().getDouble("up") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("up", 44.0);
               }

               if (entity.getPersistentData().getDouble("up") != 44.0) {
                  if (entity.getPersistentData().getDouble("up") != 43.0) {
                     if (entity.getPersistentData().getDouble("up") != 24.0) {
                        if (entity.getPersistentData().getDouble("up") != 10.0) {
                           if (entity.getPersistentData().getDouble("up") == 1.0) {
                              Entity _entity = entity;
                              if (!(_entity instanceof Player _player)) {
                                 if (_entity instanceof LivingEntity _living) {
                                    _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                                 }
                              } else {
                                 _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                                 _player.getInventory().setChanged();
                              }

                              entity.getPersistentData().putDouble("up", 0.0);
                              entity.getPersistentData().putBoolean("Attacking", false);
                              entity.getPersistentData().putBoolean("Up", false);
                           }
                        } else {
                           entity.setNoGravity(true);
                           entity.getPersistentData().putBoolean("lock", false);
                           entity.getPersistentData().putBoolean("noai", false);
                        }
                     } else {
                        if (world instanceof Level _level) {
                           if (_level.isClientSide()) {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_wing_flap3"),
                                 SoundSource.NEUTRAL,
                                 3.0F,
                                 1.0F,
                                 false
                              );
                           } else {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_wing_flap3"),
                                 SoundSource.NEUTRAL,
                                 3.0F,
                                 1.0F
                              );
                           }
                        }

                        entity.setDeltaMovement(
                           new Vec3(
                              Math.sin(Math.toRadians(entity.getYRot())) * 6.0,
                              2.4,
                              Math.cos(Math.toRadians(entity.getYRot() + Mth.nextInt(net.minecraft.util.RandomSource.create(), 150, 220))) * 6.0
                           )
                        );
                     }
                  } else {
                     entity.getPersistentData().putBoolean("lock", true);
                     entity.getPersistentData().putBoolean("noai", true);
                  }
               } else {
                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("air", true);
                  Entity _entity = entity;
                  if (!(_entity instanceof Player _player)) {
                     if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.WHITE_WOOL));
                     }
                  } else {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.WHITE_WOOL));
                     _player.getInventory().setChanged();
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 76, 6, false, false));
                  }

                  entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putDouble("y", entity.getXRot());
               }

               if (entity.getPersistentData().getDouble("up") < 25.0 && entity.getPersistentData().getDouble("up") >= 23.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!entityiterator.getPersistentData().getBoolean("dragon_lord")) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(2.0F)
                           .setArmorNegation(15.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 20.0F);
                        entityiterator.setDeltaMovement(
                           new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 3.0, 0.6, Math.cos(Math.toRadians(entity.getYRot())) * 3.0)
                        );
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack7")) {
               if (!(entity.getPersistentData().getDouble("atk7") <= 0.0)) {
                  entity.getPersistentData().putDouble("atk7", entity.getPersistentData().getDouble("atk7") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("atk7", 131.0);
               }

               if (entity.getPersistentData().getDouble("atk7") != 131.0) {
                  if (entity.getPersistentData().getDouble("atk7") != 116.0) {
                     if (entity.getPersistentData().getDouble("atk7") != 102.0) {
                        if (entity.getPersistentData().getDouble("atk7") != 101.0) {
                           if (entity.getPersistentData().getDouble("atk7") != 56.0) {
                              if (entity.getPersistentData().getDouble("atk7") != 2.0) {
                                 if (entity.getPersistentData().getDouble("atk7") == 1.0) {
                                    Entity _entity = entity;
                                    if (!(_entity instanceof Player _player)) {
                                       if (_entity instanceof LivingEntity _living) {
                                          _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                                       }
                                    } else {
                                       _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                                       _player.getInventory().setChanged();
                                    }

                                    entity.getPersistentData().putDouble("atk7", 0.0);
                                    entity.getPersistentData().putBoolean("lock", false);
                                    entity.getPersistentData().putBoolean("air", false);
                                    entity.getPersistentData().putBoolean("Attacking", false);
                                    entity.getPersistentData().putBoolean("Attack7", false);
                                 }
                              } else {
                                 Entity _entity = entity;
                                 if (!(_entity instanceof Player _player)) {
                                    if (_entity instanceof LivingEntity _living) {
                                       _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.RED_WOOL));
                                    }
                                 } else {
                                    _player.getInventory().armor.set(3, new ItemStack(Blocks.RED_WOOL));
                                    _player.getInventory().setChanged();
                                 }
                              }
                           } else {
                              entity.setNoGravity(false);
                           }
                        } else {
                           entity.getPersistentData().putBoolean("focus", false);
                           entity.getPersistentData().putBoolean("lock", true);
                        }
                     } else {
                        entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                        entity.getPersistentData().putDouble("y", entity.getXRot());
                     }
                  } else {
                     if (world instanceof Level _level) {
                        if (_level.isClientSide()) {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:spell_form"),
                              SoundSource.NEUTRAL,
                              5.0F,
                              0.5F,
                              false
                           );
                        } else {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:spell_form"),
                              SoundSource.NEUTRAL,
                              5.0F,
                              0.5F
                           );
                        }
                     }

                     if (world instanceof Level _level) {
                        if (_level.isClientSide()) {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammerform"),
                              SoundSource.NEUTRAL,
                              5.0F,
                              1.0F,
                              false
                           );
                        } else {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammerform"),
                              SoundSource.NEUTRAL,
                              5.0F,
                              1.0F
                           );
                        }
                     }

                     if (world instanceof ServerLevel _level) {
                        LightningBolt entityToSpawn = EntityType.LIGHTNING_BOLT.create(_level);
                        entityToSpawn.moveTo(Vec3.atBottomCenterOf(BlockPos.containing(x + entity.getLookAngle().x * -4.0, y, z + entity.getLookAngle().z * -4.0)));
                        entityToSpawn.setVisualOnly(true);
                        _level.addFreshEntity(entityToSpawn);
                     }
                  }
               } else {
                  if (world instanceof Level _level) {
                     if (_level.isClientSide()) {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:dragon.roar1"),
                           SoundSource.NEUTRAL,
                           5.0F,
                           1.0F,
                           false
                        );
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:dragon.roar1"),
                           SoundSource.NEUTRAL,
                           5.0F,
                           1.0F
                        );
                     }
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("focus", true);
                  Entity _entity = entity;
                  if (!(_entity instanceof Player _player)) {
                     if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.WHITE_CONCRETE));
                     }
                  } else {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.WHITE_CONCRETE));
                     _player.getInventory().setChanged();
                  }
               }

               if (entity.getPersistentData().getDouble("atk7") != 86.0) {
                  if (entity.getPersistentData().getDouble("atk7") != 60.0) {
                     if (entity.getPersistentData().getDouble("atk7") == 49.0) {
                        _center = new Vec3(x, y, z);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5.0), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (entityiterator instanceof RodParticlesEntity && !entityiterator.level().isClientSide()) {
                              entityiterator.discard();
                           }
                        }

                        if (world instanceof ServerLevel _level) {
                           _level.sendParticles(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 50, 3.0, 3.0, 3.0, 1.0);
                        }

                        if (world instanceof ServerLevel _level) {
                           _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.THUNDER_FLASH_2.get(), x, y, z, 60, 9.0, 9.0, 9.0, 0.3);
                        }
                     }
                  } else if (world instanceof Level _level) {
                     if (_level.isClientSide()) {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammerform"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F,
                           false
                        );
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammerform"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F
                        );
                     }
                  }
               } else {
                  if (world instanceof Level _level) {
                     if (_level.isClientSide()) {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:thunderr_spear"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F,
                           false
                        );
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:thunderr_spear"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new RodParticlesEntity(SimpleMobsModEntities.ROD_PARTICLES.get(), _level);
                     entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               }

               if (entity.getPersistentData().getDouble("atk7") < 111.0
                  && entity.getPersistentData().getDouble("atk7") >= 51.0
                  && entity.onGround()
                  && world instanceof ServerLevel _level) {
                  _level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 10, 6.0, 1.0, 6.0, 0.3);
               }

               if (entity.getPersistentData().getDouble("atk7") < 101.0 && entity.getPersistentData().getDouble("atk7") >= 49.0) {
                  _center = new Vec3(x, y + 2.0, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(8.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (!(entity instanceof Mob _mobEnt) ? null : _mobEnt.getTarget())) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(16.0F)
                           .setArmorNegation(50.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 45.0F);
                     }
                  }

                  if (entity.getPersistentData().getDouble("atk7") >= 61.0) {
                     entity.setDeltaMovement(
                        new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 1.8, -1.9, Math.cos(Math.toRadians(entity.getYRot())) * 1.8)
                     );
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack8")) {
               if (!(entity.getPersistentData().getDouble("atk8") <= 0.0)) {
                  entity.getPersistentData().putDouble("atk8", entity.getPersistentData().getDouble("atk8") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("atk8", 76.0);
               }

               if (entity.getPersistentData().getDouble("atk8") != 76.0) {
                  if (entity.getPersistentData().getDouble("atk8") != 59.0) {
                     if (entity.getPersistentData().getDouble("atk8") != 48.0) {
                        if (entity.getPersistentData().getDouble("atk8") != 39.0) {
                           if (entity.getPersistentData().getDouble("atk8") != 13.0) {
                              if (entity.getPersistentData().getDouble("atk8") == 1.0) {
                                 entity.getPersistentData().putBoolean("fire4", false);
                                 _center = new Vec3(x, y, z);

                                 for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(100.0), e -> true)
                                    .stream()
                                    .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                                    .collect(Collectors.toList())) {
                                    if (entityiterator == (!(entity instanceof Mob _mobEnt) ? null : _mobEnt.getTarget())) {
                                       entityiterator.getPersistentData().putBoolean("dltarget", false);
                                    }
                                 }

                                 if (Math.random() >= 0.5 || entity.getPersistentData().getBoolean("atk8done")) {
                                    Entity _entity = entity;
                                    if (!(_entity instanceof Player _player)) {
                                       if (_entity instanceof LivingEntity _living) {
                                          _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                                       }
                                    } else {
                                       _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                                       _player.getInventory().setChanged();
                                    }

                                    entity.setNoGravity(false);
                                    entity.getPersistentData().putBoolean("air", false);
                                    entity.getPersistentData().putDouble("atk8", 0.0);
                                    entity.getPersistentData().putBoolean("Attacking", false);
                                    entity.getPersistentData().putBoolean("Attack8", false);
                                 }

                                 entity.getPersistentData().putBoolean("atk8done", true);
                              }
                           } else {
                              if (world instanceof Level _level) {
                                 if (_level.isClientSide()) {
                                    _level.playLocalSound(
                                       x,
                                       y,
                                       z,
                                       dev.deepnight.SafeSounds.resolve("entity.ender_dragon.shoot"),
                                       SoundSource.NEUTRAL,
                                       4.0F,
                                       0.8F,
                                       false
                                    );
                                 } else {
                                    _level.playSound(
                                       null,
                                       BlockPos.containing(x, y, z),
                                       dev.deepnight.SafeSounds.resolve("entity.ender_dragon.shoot"),
                                       SoundSource.NEUTRAL,
                                       4.0F,
                                       0.8F
                                    );
                                 }
                              }

                              entity.getPersistentData().putBoolean("fire4", true);
                           }
                        } else {
                           entity.getPersistentData().putBoolean("fire3", false);
                        }
                     } else {
                        if (world instanceof Level _level) {
                           if (_level.isClientSide()) {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("entity.ender_dragon.shoot"),
                                 SoundSource.NEUTRAL,
                                 4.0F,
                                 0.8F,
                                 false
                              );
                           } else {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("entity.ender_dragon.shoot"),
                                 SoundSource.NEUTRAL,
                                 4.0F,
                                 0.8F
                              );
                           }
                        }

                        entity.getPersistentData().putBoolean("fire2", false);
                        entity.getPersistentData().putBoolean("fire3", true);
                     }
                  } else {
                     if (world instanceof Level _level) {
                        if (_level.isClientSide()) {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("entity.ender_dragon.shoot"),
                              SoundSource.NEUTRAL,
                              4.0F,
                              0.8F,
                              false
                           );
                        } else {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("entity.ender_dragon.shoot"),
                              SoundSource.NEUTRAL,
                              4.0F,
                              0.8F
                           );
                        }
                     }

                     entity.getPersistentData().putBoolean("fire2", true);
                  }
               } else {
                  entity.getPersistentData().putBoolean("Attacking", true);
                  Entity _entity = entity;
                  if (!(_entity instanceof Player _player)) {
                     if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.YELLOW_WOOL));
                     }
                  } else {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.YELLOW_WOOL));
                     _player.getInventory().setChanged();
                  }

                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(100.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (!(entity instanceof Mob _mobEnt) ? null : _mobEnt.getTarget())) {
                        entityiterator.getPersistentData().putBoolean("dltarget", true);
                     }
                  }
               }

               if (entity.getPersistentData().getDouble("atk8") <= 61.0 && entity.getPersistentData().getDouble("atk8") >= 21.0) {
                  entity.setDeltaMovement(
                     new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 0.8, 0.0, Math.cos(Math.toRadians(entity.getYRot())) * 0.8)
                  );
               }
            }

            if (entity.getPersistentData().getBoolean("Attack9")) {
               if (entity.getPersistentData().getDouble("atk9") != 0.0) {
                  entity.getPersistentData().putDouble("atk9", entity.getPersistentData().getDouble("atk9") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("atk9", 56.0);
               }

               if (entity.getPersistentData().getDouble("atk9") != 56.0) {
                  if (entity.getPersistentData().getDouble("atk9") != 49.0) {
                     if (entity.getPersistentData().getDouble("atk9") != 40.0) {
                        if (entity.getPersistentData().getDouble("atk9") < 39.0 && entity.getPersistentData().getDouble("atk9") > 35.0) {
                           entity.setDeltaMovement(
                              new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 0.8, 0.0, Math.cos(Math.toRadians(entity.getYRot())) * 0.8)
                           );
                        } else if (entity.getPersistentData().getDouble("atk9") == 1.0) {
                           entity.getPersistentData().putBoolean("lock", false);
                           entity.getPersistentData().putBoolean("noai", false);
                           Entity _entity = entity;
                           if (!(_entity instanceof Player _player)) {
                              if (_entity instanceof LivingEntity _living) {
                                 _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                              }
                           } else {
                              _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                              _player.getInventory().setChanged();
                           }

                           entity.getPersistentData().putDouble("atk9", 0.0);
                           entity.getPersistentData().putBoolean("Attacking", false);
                           entity.getPersistentData().putBoolean("Attack9", false);
                        }
                     } else {
                        entity.getPersistentData().putBoolean("focus", false);
                        entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                        entity.getPersistentData().putDouble("y", 0.0);
                        if (world instanceof Level _level) {
                           if (_level.isClientSide()) {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F,
                                 false
                              );
                           } else {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F
                              );
                           }
                        }

                        entity.getPersistentData().putBoolean("lock", true);
                        entity.getPersistentData().putBoolean("noai", true);
                     }
                  } else if (world instanceof Level _level) {
                     if (_level.isClientSide()) {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:spell_form"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F,
                           false
                        );
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:spell_form"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     }
                  }
               } else {
                  Entity _entity = entity;
                  if (!(_entity instanceof Player _player)) {
                     if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.GREEN_WOOL));
                     }
                  } else {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.GREEN_WOOL));
                     _player.getInventory().setChanged();
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 81, 6, false, false));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
               }

               if (entity.getPersistentData().getDouble("atk9") == 37.0) {
                  if (world instanceof Level _level) {
                     if (_level.isClientSide()) {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           0.5F,
                           false
                        );
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           0.5F
                        );
                     }
                  }

                  if (world instanceof Level _level) {
                     if (_level.isClientSide()) {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:electric_1"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F,
                           false
                        );
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:electric_1"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F
                        );
                     }
                  }

                  if (world instanceof Level _level) {
                     if (_level.isClientSide()) {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammerform"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F,
                           false
                        );
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammerform"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F
                        );
                     }
                  }
               }

               if (entity.getPersistentData().getDouble("atk9") < 38.0 && entity.getPersistentData().getDouble("atk9") > 32.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        (SimpleParticleType)SimpleMobsModParticleTypes.THUNDER_FLASH_1.get(),
                        x + entity.getLookAngle().x * 17.0,
                        y,
                        z + entity.getLookAngle().z * 17.0,
                        2,
                        2.0,
                        2.0,
                        2.0,
                        1.0
                     );
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        (SimpleParticleType)SimpleMobsModParticleTypes.THUNDER_FLASH_2.get(),
                        x + entity.getLookAngle().x * 17.0,
                        y,
                        z + entity.getLookAngle().z * 17.0,
                        2,
                        2.0,
                        2.0,
                        2.0,
                        1.0
                     );
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.CAMPFIRE_COSY_SMOKE, x + entity.getLookAngle().x * 17.0, y, z + entity.getLookAngle().z * 17.0, 1, 3.0, 3.0, 3.0, 1.0
                     );
                  }

                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if ((entityiterator instanceof Player || entityiterator instanceof ServerPlayer) && entityiterator instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 20, 0, false, false));
                     }
                  }

                  _center = new Vec3(x + entity.getLookAngle().x * 17.0, y, z + entity.getLookAngle().z * 17.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!entityiterator.getPersistentData().getBoolean("dragon_lord")) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.KNOCKDOWN)
                           .setImpact(2.0F)
                           .setArmorNegation(50.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 40.0F);
                     }
                  }

                  entity.getPersistentData().putDouble("s", entity.getPersistentData().getDouble("s") + 1.0);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack10")) {
               if (entity.getPersistentData().getDouble("atk10") != 0.0) {
                  entity.getPersistentData().putDouble("atk10", entity.getPersistentData().getDouble("atk10") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("atk10", 85.0);
               }

               if (entity.getPersistentData().getDouble("atk10") != 85.0) {
                  if (entity.getPersistentData().getDouble("atk10") != 48.0) {
                     if (entity.getPersistentData().getDouble("atk10") < 44.0 && entity.getPersistentData().getDouble("atk10") > 39.0) {
                        _center = new Vec3(x + entity.getLookAngle().x * 15.0, y, z + entity.getLookAngle().z * 15.0);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(6.0), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (!entityiterator.getPersistentData().getBoolean("dragon_lord")) {
                              SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                 .setInitialPosition(entity.position())
                                 .setStunType(StunType.LONG)
                                 .setImpact(2.0F)
                                 .setArmorNegation(50.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 45.0F);
                           }
                        }
                     } else if (entity.getPersistentData().getDouble("atk10") != 31.0) {
                        if (entity.getPersistentData().getDouble("atk10") != 10.0) {
                           if (entity.getPersistentData().getDouble("atk10") == 1.0) {
                              Entity _entity = entity;
                              if (!(_entity instanceof Player _player)) {
                                 if (_entity instanceof LivingEntity _living) {
                                    _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                                 }
                              } else {
                                 _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                                 _player.getInventory().setChanged();
                              }

                              entity.getPersistentData().putDouble("atk10", 0.0);
                              entity.getPersistentData().putBoolean("lock", false);
                              entity.getPersistentData().putBoolean("Attacking", false);
                              entity.getPersistentData().putBoolean("Attack10", false);
                           }
                        } else if (world instanceof ServerLevel _level) {
                           _level.getServer()
                              .getCommands()
                              .performPrefixedCommand(
                                 new CommandSourceStack(
                                       CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                    )
                                    .withSuppressedOutput(),
                                 "/data merge entity @e[type=deepnight:dawn_dragon_lord,limit=1,sort=nearest] {NoAI:0}"
                              );
                        }
                     } else {
                        _center = new Vec3(x + entity.getLookAngle().x * 16.0, y, z + entity.getLookAngle().z * 16.0);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.5), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (!entityiterator.getPersistentData().getBoolean("dragon_lord")) {
                              if (world instanceof ServerLevel _level) {
                                 _level.getServer()
                                    .getCommands()
                                    .performPrefixedCommand(
                                       new CommandSourceStack(
                                             CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                          )
                                          .withSuppressedOutput(),
                                       "/data merge entity @e[type=deepnight:dawn_dragon_lord,limit=1,sort=nearest] {NoAI:0}"
                                    );
                              }

                              entity.getPersistentData().putDouble("atk10", 0.0);
                              entity.getPersistentData().putBoolean("Attack10b", true);
                              entity.getPersistentData().putBoolean("lock", false);
                              entity.getPersistentData().putBoolean("Attack10", false);
                           }
                        }
                     }
                  } else {
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                     entity.getPersistentData().putDouble("y", 0.0);
                     entity.getPersistentData().putBoolean("lock", true);
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_dragon_lord,limit=1,sort=nearest] {NoAI:1b}"
                           );
                     }

                     if (world instanceof Level _level) {
                        if (_level.isClientSide()) {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:lightning_dragon_sweep"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F,
                              false
                           );
                        } else {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:lightning_dragon_sweep"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F
                           );
                        }
                     }

                     if (world instanceof Level _level) {
                        if (_level.isClientSide()) {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:electric_1"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F,
                              false
                           );
                        } else {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:electric_1"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F
                           );
                        }
                     }
                  }
               } else {
                  Entity _entity = entity;
                  if (!(_entity instanceof Player _player)) {
                     if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.ACACIA_LEAVES));
                     }
                  } else {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.ACACIA_LEAVES));
                     _player.getInventory().setChanged();
                  }

                  entity.getPersistentData().putBoolean("focus", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 85, 6, false, false));
                  }
               }

               if (entity.getPersistentData().getDouble("atk10") != 65.0) {
                  if (entity.getPersistentData().getDouble("atk10") <= 53.0 && entity.getPersistentData().getDouble("atk10") >= 47.0) {
                     _center = entity.getLookAngle();
                     new Vec3(-_center.z, 0.0, _center.x);
                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(
                           (SimpleParticleType)SimpleMobsModParticleTypes.THUNDER_FLASH_1.get(),
                           x + entity.getLookAngle().x * 17.0,
                           y + entity.getPersistentData().getDouble("j"),
                           z + entity.getLookAngle().z * 17.0,
                           1,
                           0.1,
                           0.5,
                           0.1,
                           1.0
                        );
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(
                           (SimpleParticleType)SimpleMobsModParticleTypes.THUNDER_FLASH_2.get(),
                           x + entity.getLookAngle().x * 17.0,
                           y + entity.getPersistentData().getDouble("j"),
                           z + entity.getLookAngle().z * 17.0,
                           2,
                           0.1,
                           0.5,
                           0.1,
                           1.0
                        );
                     }

                     entity.getPersistentData().putDouble("i", entity.getPersistentData().getDouble("i") - 1.5);
                     entity.getPersistentData().putDouble("j", entity.getPersistentData().getDouble("j") + 1.0);
                  }
               } else {
                  if (world instanceof Level _level) {
                     if (_level.isClientSide()) {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:spell_form"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F,
                           false
                        );
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:spell_form"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        (SimpleParticleType)SimpleMobsModParticleTypes.THUNDER_FLASH_2.get(),
                        x + entity.getLookAngle().x * 2.0,
                        y,
                        z + entity.getLookAngle().z * 2.0,
                        5,
                        1.0,
                        1.0,
                        1.0,
                        0.1
                     );
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.FLASH, x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0, 5, 3.0, 3.0, 3.0, 1.0);
                  }

                  entity.getPersistentData().putDouble("i", 11.0);
                  entity.getPersistentData().putDouble("j", -3.0);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack10alt")) {
               if (entity.getPersistentData().getDouble("atk10alt") != 0.0) {
                  entity.getPersistentData().putDouble("atk10alt", entity.getPersistentData().getDouble("atk10alt") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("atk10alt", 65.0);
               }

               if (entity.getPersistentData().getDouble("atk10alt") != 65.0) {
                  if (entity.getPersistentData().getDouble("atk10alt") != 48.0) {
                     if (entity.getPersistentData().getDouble("atk10alt") < 44.0 && entity.getPersistentData().getDouble("atk10alt") > 39.0) {
                        _center = new Vec3(x + entity.getLookAngle().x * 14.0, y, z + entity.getLookAngle().z * 14.0);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.5), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (!entityiterator.getPersistentData().getBoolean("dragon_lord")) {
                              SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                 .setInitialPosition(entity.position())
                                 .setStunType(StunType.LONG)
                                 .setImpact(2.0F)
                                 .setArmorNegation(30.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 30.0F);
                           }
                        }
                     } else if (entity.getPersistentData().getDouble("atk10alt") != 31.0) {
                        if (entity.getPersistentData().getDouble("atk10alt") != 10.0) {
                           if (entity.getPersistentData().getDouble("atk10alt") == 1.0) {
                              Entity _entity = entity;
                              if (!(_entity instanceof Player _player)) {
                                 if (_entity instanceof LivingEntity _living) {
                                    _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                                 }
                              } else {
                                 _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                                 _player.getInventory().setChanged();
                              }

                              entity.getPersistentData().putDouble("atk10alt", 0.0);
                              entity.getPersistentData().putBoolean("lock", false);
                              entity.getPersistentData().putBoolean("Attacking", false);
                              entity.getPersistentData().putBoolean("Attack10alt", false);
                           }
                        } else if (world instanceof ServerLevel _level) {
                           _level.getServer()
                              .getCommands()
                              .performPrefixedCommand(
                                 new CommandSourceStack(
                                       CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                    )
                                    .withSuppressedOutput(),
                                 "/data merge entity @e[type=deepnight:dawn_dragon_lord,limit=1,sort=nearest] {NoAI:0}"
                              );
                        }
                     } else {
                        _center = new Vec3(x + entity.getLookAngle().x * 16.0, y, z + entity.getLookAngle().z * 16.0);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.5), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (!entityiterator.getPersistentData().getBoolean("dragon_lord")) {
                              if (world instanceof ServerLevel _level) {
                                 _level.getServer()
                                    .getCommands()
                                    .performPrefixedCommand(
                                       new CommandSourceStack(
                                             CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                          )
                                          .withSuppressedOutput(),
                                       "/data merge entity @e[type=deepnight:dawn_dragon_lord,limit=1,sort=nearest] {NoAI:0}"
                                    );
                              }

                              entity.getPersistentData().putDouble("atk10alt", 0.0);
                              entity.getPersistentData().putBoolean("lock", false);
                              entity.getPersistentData().putBoolean("Attack10alt", false);
                              entity.getPersistentData().putBoolean("Attack10b", true);
                           }
                        }
                     }
                  } else {
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                     entity.getPersistentData().putDouble("y", 0.0);
                     entity.getPersistentData().putBoolean("lock", true);
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_dragon_lord,limit=1,sort=nearest] {NoAI:1b}"
                           );
                     }

                     if (world instanceof Level _level) {
                        if (_level.isClientSide()) {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:lightning_dragon_sweep"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F,
                              false
                           );
                        } else {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:lightning_dragon_sweep"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F
                           );
                        }
                     }
                  }
               } else {
                  Entity _entity = entity;
                  if (!(_entity instanceof Player _player)) {
                     if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.OAK_LEAVES));
                     }
                  } else {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.OAK_LEAVES));
                     _player.getInventory().setChanged();
                  }

                  entity.getPersistentData().putBoolean("focus", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 85, 6, false, false));
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack10b")) {
               if (entity.getPersistentData().getDouble("atk10b") != 0.0) {
                  entity.getPersistentData().putDouble("atk10b", entity.getPersistentData().getDouble("atk10b") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("atk10b", 71.0);
               }

               if (entity.getPersistentData().getDouble("atk10b") != 71.0) {
                  if (entity.getPersistentData().getDouble("atk10b") != 63.0) {
                     if (entity.getPersistentData().getDouble("atk10b") != 56.0) {
                        if (entity.getPersistentData().getDouble("atk10b") < 52.0 && entity.getPersistentData().getDouble("atk10b") > 47.0) {
                           _center = new Vec3(x + entity.getLookAngle().x * 15.0, y, z + entity.getLookAngle().z * 15.0);

                           for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.0), e -> true)
                              .stream()
                              .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                              .collect(Collectors.toList())) {
                              if (!entityiterator.getPersistentData().getBoolean("dragon_lord")) {
                                 SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                    .setInitialPosition(entity.position())
                                    .setStunType(StunType.LONG)
                                    .setImpact(2.0F)
                                    .setArmorNegation(30.0F);
                                 entityiterator.hurt((DamageSource)dmgSrc, 30.0F);
                              }
                           }
                        } else if (entity.getPersistentData().getDouble("atk10b") != 10.0) {
                           if (entity.getPersistentData().getDouble("atk10b") == 1.0) {
                              Entity _entity = entity;
                              if (!(_entity instanceof Player _player)) {
                                 if (_entity instanceof LivingEntity _living) {
                                    _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                                 }
                              } else {
                                 _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                                 _player.getInventory().setChanged();
                              }

                              entity.getPersistentData().putDouble("atk10b", 0.0);
                              entity.getPersistentData().putBoolean("lock", false);
                              entity.getPersistentData().putBoolean("Attacking", false);
                              entity.getPersistentData().putBoolean("Attack10b", false);
                           }
                        } else if (world instanceof ServerLevel _level) {
                           _level.getServer()
                              .getCommands()
                              .performPrefixedCommand(
                                 new CommandSourceStack(
                                       CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                    )
                                    .withSuppressedOutput(),
                                 "/data merge entity @e[type=deepnight:dawn_dragon_lord,limit=1,sort=nearest] {NoAI:0}"
                              );
                        }
                     } else if (world instanceof Level _level) {
                        if (_level.isClientSide()) {
                           _level.playLocalSound(
                              x + entity.getLookAngle().x * 16.0,
                              y,
                              z + entity.getLookAngle().z * 16.0,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:lightning_dragon_sweep"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F,
                              false
                           );
                        } else {
                           _level.playSound(
                              null,
                              BlockPos.containing(x + entity.getLookAngle().x * 16.0, y, z + entity.getLookAngle().z * 16.0),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:lightning_dragon_sweep"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F
                           );
                        }
                     }
                  } else {
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putBoolean("lock", true);
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_dragon_lord,limit=1,sort=nearest] {NoAI:1b}"
                           );
                     }
                  }
               } else {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 71, 6, false, false));
                  }

                  Entity _entity = entity;
                  if (!(_entity instanceof Player _player)) {
                     if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.CHAIN));
                     }
                  } else {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.CHAIN));
                     _player.getInventory().setChanged();
                  }

                  entity.getPersistentData().putBoolean("focus", true);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack11")) {
               if (entity.getPersistentData().getDouble("atk11") != 0.0) {
                  entity.getPersistentData().putDouble("atk11", entity.getPersistentData().getDouble("atk11") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("atk11", 186.0);
                  entity.getPersistentData().putDouble("t_s", 0.0);
               }

               if (entity.getPersistentData().getDouble("atk11") != 186.0) {
                  if (entity.getPersistentData().getDouble("atk11") != 168.0) {
                     if (entity.getPersistentData().getDouble("atk11") != 166.0) {
                        if (entity.getPersistentData().getDouble("atk11") == 1.0) {
                           Entity _entity = entity;
                           if (!(_entity instanceof Player _player)) {
                              if (_entity instanceof LivingEntity _living) {
                                 _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                              }
                           } else {
                              _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                              _player.getInventory().setChanged();
                           }

                           if (world instanceof ServerLevel _level) {
                              _level.getServer()
                                 .getCommands()
                                 .performPrefixedCommand(
                                    new CommandSourceStack(
                                          CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                       )
                                       .withSuppressedOutput(),
                                    "/data merge entity @e[type=deepnight:dawn_dragon_lord,limit=1,sort=nearest] {NoAI:0}"
                                 );
                           }

                           entity.getPersistentData().putDouble("atk11", 0.0);
                           entity.getPersistentData().putBoolean("lock", false);
                           entity.getPersistentData().putBoolean("Attacking", false);
                           entity.getPersistentData().putBoolean("Attack11", false);
                        }
                     } else if (world instanceof Level _level) {
                        if (_level.isClientSide()) {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_lord_roar"),
                              SoundSource.NEUTRAL,
                              4.0F,
                              1.0F,
                              false
                           );
                        } else {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_lord_roar"),
                              SoundSource.NEUTRAL,
                              4.0F,
                              1.0F
                           );
                        }
                     }
                  } else {
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                     entity.getPersistentData().putDouble("y", 0.0);
                     entity.getPersistentData().putBoolean("lock", true);
                  }
               } else {
                  Entity _entity = entity;
                  if (!(_entity instanceof Player _player)) {
                     if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.SADDLE));
                     }
                  } else {
                     _player.getInventory().armor.set(3, new ItemStack(Items.SADDLE));
                     _player.getInventory().setChanged();
                  }

                  entity.getPersistentData().putBoolean("focus", true);
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_dragon_lord,limit=1,sort=nearest] {NoAI:1b}"
                        );
                  }
               }

               if (entity.getPersistentData().getDouble("atk11") < 140.0 && entity.getPersistentData().getDouble("atk11") > 61.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(50.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if ((entityiterator instanceof Player || entityiterator instanceof ServerPlayer) && entityiterator instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 20, 0, false, false));
                     }
                  }

                  if (Math.random() >= 0.6) {
                     _center = new Vec3(x + entity.getLookAngle().x * 7.0, y, z + entity.getLookAngle().z * 7.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("dragon_lord")) {
                           entityiterator.hurt(new DamageSource(entityiterator.level().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(net.minecraft.world.damagesource.DamageTypes.GENERIC), entity), 2.0F);
                           if (entityiterator instanceof LivingEntity _entity) {
                              _entity.addEffect(new MobEffectInstance(SimpleMobsModMobEffects.SHOCK.get(), 2, 0, false, false));
                           }
                        }
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(
                           ParticleTypes.FLASH,
                           x + Mth.nextInt(net.minecraft.util.RandomSource.create(), -20, 20) + entity.getLookAngle().x * 7.0,
                           y,
                           z + Mth.nextInt(net.minecraft.util.RandomSource.create(), -20, 20) + entity.getLookAngle().z * 7.0,
                           1,
                           5.0,
                           7.0,
                           5.0,
                           0.0
                        );
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(
                           (SimpleParticleType)SimpleMobsModParticleTypes.DRAGON_THUNDER_LIT_2.get(),
                           x + Mth.nextInt(net.minecraft.util.RandomSource.create(), -20, 20) + entity.getLookAngle().x * 7.0,
                           y,
                           z + Mth.nextInt(net.minecraft.util.RandomSource.create(), -20, 20) + entity.getLookAngle().z * 7.0,
                           5,
                           10.0,
                           7.0,
                           10.0,
                           0.0
                        );
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(
                           (SimpleParticleType)SimpleMobsModParticleTypes.THUNDER_FLASH_2.get(),
                           x + Mth.nextInt(net.minecraft.util.RandomSource.create(), -20, 20) + entity.getLookAngle().x * 7.0,
                           y,
                           z + Mth.nextInt(net.minecraft.util.RandomSource.create(), -20, 20) + entity.getLookAngle().z * 7.0,
                           5,
                           10.0,
                           7.0,
                           10.0,
                           0.0
                        );
                     }
                  }

                  if (Math.random() >= 0.9 && entity.getPersistentData().getDouble("t_s") <= 20.0) {
                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new Thundereffect3Entity(SimpleMobsModEntities.THUNDEREFFECT_3.get(), _level);
                        entityToSpawn.moveTo(x + Mth.nextInt(net.minecraft.util.RandomSource.create(), -20, 20), y, z + Mth.nextInt(net.minecraft.util.RandomSource.create(), -20, 20), 0.0F, 0.0F);
                        entityToSpawn.setYBodyRot(0.0F);
                        entityToSpawn.setYHeadRot(0.0F);
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }

                     entity.getPersistentData().putDouble("t_s", entity.getPersistentData().getDouble("t_s") + 1.0);
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack11a")) {
               if (entity.getPersistentData().getDouble("atk11a") != 0.0) {
                  entity.getPersistentData().putDouble("atk11a", entity.getPersistentData().getDouble("atk11a") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("atk11a", 186.0);
               }

               if (entity.getPersistentData().getDouble("atk11a") != 186.0) {
                  if (entity.getPersistentData().getDouble("atk11a") != 168.0) {
                     if (entity.getPersistentData().getDouble("atk11a") != 166.0) {
                        if (entity.getPersistentData().getDouble("atk11a") == 1.0) {
                           Entity _entity = entity;
                           if (!(_entity instanceof Player _player)) {
                              if (_entity instanceof LivingEntity _living) {
                                 _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                              }
                           } else {
                              _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                              _player.getInventory().setChanged();
                           }

                           if (world instanceof ServerLevel _level) {
                              _level.getServer()
                                 .getCommands()
                                 .performPrefixedCommand(
                                    new CommandSourceStack(
                                          CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                       )
                                       .withSuppressedOutput(),
                                    "/data merge entity @e[type=deepnight:dawn_dragon_lord,limit=1,sort=nearest] {NoAI:0}"
                                 );
                           }

                           entity.getPersistentData().putDouble("atk11a", 0.0);
                           entity.getPersistentData().putBoolean("lock", false);
                           entity.getPersistentData().putBoolean("Attacking", false);
                           entity.getPersistentData().putBoolean("Attack11a", false);
                        }
                     } else {
                        if (world instanceof Level _level) {
                           if (_level.isClientSide()) {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_lord_roar"),
                                 SoundSource.NEUTRAL,
                                 3.0F,
                                 1.0F,
                                 false
                              );
                           } else {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_lord_roar"),
                                 SoundSource.NEUTRAL,
                                 3.0F,
                                 1.0F
                              );
                           }
                        }

                        _center = new Vec3(x, y, z);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(70.0), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (world instanceof Level _level) {
                              if (_level.isClientSide()) {
                                 _level.playLocalSound(
                                    x,
                                    y,
                                    z,
                                    dev.deepnight.SafeSounds.resolve("simple_mobs:earthquake"),
                                    SoundSource.NEUTRAL,
                                    1.0F,
                                    1.0F,
                                    false
                                 );
                              } else {
                                 _level.playSound(
                                    null,
                                    BlockPos.containing(x, y, z),
                                    dev.deepnight.SafeSounds.resolve("simple_mobs:earthquake"),
                                    SoundSource.NEUTRAL,
                                    1.0F,
                                    1.0F
                                 );
                              }
                           }
                        }
                     }
                  } else {
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                     entity.getPersistentData().putDouble("y", 0.0);
                     entity.getPersistentData().putBoolean("lock", true);
                  }
               } else {
                  Entity _entity = entity;
                  if (!(_entity instanceof Player _player)) {
                     if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.SADDLE));
                     }
                  } else {
                     _player.getInventory().armor.set(3, new ItemStack(Items.SADDLE));
                     _player.getInventory().setChanged();
                  }

                  entity.getPersistentData().putBoolean("focus", true);
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_dragon_lord,limit=1,sort=nearest] {NoAI:1b}"
                        );
                  }
               }

               if (entity.getPersistentData().getDouble("atk11a") < 144.0 && entity.getPersistentData().getDouble("atk11a") > 41.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(8.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if ((entityiterator instanceof Player || entityiterator instanceof ServerPlayer) && entityiterator instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 20, 0, false, false));
                     }
                  }

                  if (Math.random() >= 0.6) {
                     _center = new Vec3(x + entity.getLookAngle().x * 7.0, y, z + entity.getLookAngle().z * 7.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("dragon_lord")) {
                           entityiterator.hurt(new DamageSource(entityiterator.level().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(net.minecraft.world.damagesource.DamageTypes.GENERIC), entity), 2.0F);
                           if (entityiterator instanceof LivingEntity _entity) {
                              _entity.addEffect(new MobEffectInstance(SimpleMobsModMobEffects.SHOCK.get(), 2, 0, false, false));
                           }
                        }
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(
                           ParticleTypes.FLASH,
                           x + Mth.nextInt(net.minecraft.util.RandomSource.create(), -20, 20) + entity.getLookAngle().x * 7.0,
                           y,
                           z + Mth.nextInt(net.minecraft.util.RandomSource.create(), -20, 20) + entity.getLookAngle().z * 7.0,
                           1,
                           7.0,
                           7.0,
                           7.0,
                           0.0
                        );
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(
                           (SimpleParticleType)SimpleMobsModParticleTypes.DRAGON_THUNDER_LIT_2.get(),
                           x + Mth.nextInt(net.minecraft.util.RandomSource.create(), -20, 20) + entity.getLookAngle().x * 7.0,
                           y,
                           z + Mth.nextInt(net.minecraft.util.RandomSource.create(), -20, 20) + entity.getLookAngle().z * 7.0,
                           5,
                           10.0,
                           7.0,
                           10.0,
                           0.0
                        );
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(
                           (SimpleParticleType)SimpleMobsModParticleTypes.THUNDER_FLASH_2.get(),
                           x + Mth.nextInt(net.minecraft.util.RandomSource.create(), -20, 20) + entity.getLookAngle().x * 7.0,
                           y,
                           z + Mth.nextInt(net.minecraft.util.RandomSource.create(), -20, 20) + entity.getLookAngle().z * 7.0,
                           5,
                           10.0,
                           7.0,
                           10.0,
                           0.0
                        );
                     }

                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new MeteorAttackEntity(SimpleMobsModEntities.METEOR_ATTACK.get(), _level);
                        entityToSpawn.moveTo(
                           x + Mth.nextInt(net.minecraft.util.RandomSource.create(), -20, 20) + entity.getLookAngle().x * Mth.nextInt(net.minecraft.util.RandomSource.create(), -40, -10),
                           y + 70.0,
                           z + Mth.nextInt(net.minecraft.util.RandomSource.create(), -50, 50) + entity.getLookAngle().z * Mth.nextInt(net.minecraft.util.RandomSource.create(), -40, -10),
                           entity.getYRot() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -50, 50),
                           0.0F
                        );
                        entityToSpawn.setYBodyRot(entity.getYRot() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -50, 50));
                        entityToSpawn.setYHeadRot(entity.getYRot() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -50, 50));
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("sweep")) {
               if (!(entity.getPersistentData().getDouble("i") > 3.0)) {
                  entity.getPersistentData().putBoolean("sweep", false);
               } else {
                  entity.getPersistentData().putDouble("revdir", entity.getYRot() + 180.0F);
                  if (entity.getPersistentData().getDouble("revdir") > 360.0) {
                     entity.getPersistentData().putDouble("revdir", entity.getPersistentData().getDouble("revdir") - 360.0);
                  }

                  entity.getPersistentData().putDouble("direction", Math.abs(entity.getPersistentData().getDouble("revdir")) / 360.0 * 26.0);
                  entity.getPersistentData()
                     .putDouble("nowdirection", entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("i"));
                  if (entity.getPersistentData().getDouble("direction") >= 24.0 && entity.getPersistentData().getDouble("direction") <= 25.0) {
                     entity.getPersistentData().putDouble("nowdirection", 0.0 + entity.getPersistentData().getDouble("i"));
                  }

                  if (!(entity.getPersistentData().getDouble("nowdirection") > 25.0)) {
                     if (entity.getPersistentData().getDouble("nowdirection") < 0.0) {
                        entity.getPersistentData().putDouble("nowdirection", entity.getPersistentData().getDouble("nowdirection") + 25.0);
                     }
                  } else {
                     entity.getPersistentData().putDouble("nowdirection", entity.getPersistentData().getDouble("nowdirection") - 25.0);
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.CLOUD,
                        x + entity.getLookAngle().x * -2.0 + Math.cos(0.25132741228718347 * entity.getPersistentData().getDouble("nowdirection")) * 8.0,
                        y,
                        z + entity.getLookAngle().z * -2.0 + Math.sin(0.25132741228718347 * entity.getPersistentData().getDouble("nowdirection")) * 8.0,
                        10,
                        1.0,
                        1.0,
                        1.0,
                        0.6
                     );
                  }

                  _center = new Vec3(
                     x + entity.getLookAngle().x * -5.0 + Math.cos(0.25132741228718347 * entity.getPersistentData().getDouble("nowdirection")) * 8.0,
                     y,
                     z + entity.getLookAngle().z * -5.0 + Math.sin(0.25132741228718347 * entity.getPersistentData().getDouble("nowdirection")) * 8.0
                  );

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(6.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!entityiterator.getPersistentData().getBoolean("dragon_lord")) {
                        if (!(entityiterator instanceof LivingEntity _livEnt) ? true : !_livEnt.isBlocking()) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.SHORT)
                              .setImpact(0.5F)
                              .setArmorNegation(30.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 30.0F);
                           entityiterator.setDeltaMovement(
                              new Vec3(Math.sin(Math.toRadians(entity.getYRot() - 90.0F)) * 3.0, 0.8, Math.cos(Math.toRadians(entity.getYRot() + 90.0F)) * 3.0)
                           );
                        } else if ((!(entityiterator instanceof LivingEntity _livEnt) ? ItemStack.EMPTY : _livEnt.getMainHandItem()).getItem() != Items.SHIELD) {
                           ItemStack _ist = !(entityiterator instanceof LivingEntity _livEnt) ? ItemStack.EMPTY : _livEnt.getOffhandItem();
                           if (_ist.hurt(16, net.minecraft.util.RandomSource.create(), null)) {
                              _ist.shrink(1);
                              _ist.setDamageValue(0);
                           }

                           if (entityiterator instanceof Player _player) {
                              _player.getCooldowns()
                                 .addCooldown((!(entityiterator instanceof LivingEntity _livEnt) ? ItemStack.EMPTY : _livEnt.getOffhandItem()).getItem(), 200);
                           }

                           if (world instanceof Level _level) {
                              if (_level.isClientSide()) {
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
                              } else {
                                 _level.playSound(
                                    null,
                                    BlockPos.containing(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ()),
                                    dev.deepnight.SafeSounds.resolve("item.shield.break"),
                                    SoundSource.NEUTRAL,
                                    1.0F,
                                    1.0F
                                 );
                              }
                           }
                        } else {
                           ItemStack _ist = !(entityiterator instanceof LivingEntity _livEnt) ? ItemStack.EMPTY : _livEnt.getMainHandItem();
                           if (_ist.hurt(30, net.minecraft.util.RandomSource.create(), null)) {
                              _ist.shrink(1);
                              _ist.setDamageValue(0);
                           }

                           if (entityiterator instanceof Player _player) {
                              _player.getCooldowns()
                                 .addCooldown((!(entityiterator instanceof LivingEntity _livEnt) ? ItemStack.EMPTY : _livEnt.getMainHandItem()).getItem(), 200);
                           }

                           if (world instanceof Level _level) {
                              if (_level.isClientSide()) {
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
                              } else {
                                 _level.playSound(
                                    null,
                                    BlockPos.containing(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ()),
                                    dev.deepnight.SafeSounds.resolve("item.shield.break"),
                                    SoundSource.NEUTRAL,
                                    1.0F,
                                    1.0F
                                 );
                              }
                           }
                        }
                     }
                  }

                  entity.getPersistentData().putDouble("i", entity.getPersistentData().getDouble("i") - 1.0);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack12")) {
               if (entity.getPersistentData().getDouble("atk12") != 0.0) {
                  entity.getPersistentData().putDouble("atk12", entity.getPersistentData().getDouble("atk12") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("atk12", 96.0);
               }

               if (entity.getPersistentData().getDouble("atk12") != 96.0) {
                  if (entity.getPersistentData().getDouble("atk12") != 95.0) {
                     if (entity.getPersistentData().getDouble("atk12") != 82.0) {
                        if (entity.getPersistentData().getDouble("atk12") != 20.0) {
                           if (entity.getPersistentData().getDouble("atk12") != 6.0) {
                              if (entity.getPersistentData().getDouble("atk12") == 1.0) {
                                 Entity _entity = entity;
                                 if (!(_entity instanceof Player _player)) {
                                    if (_entity instanceof LivingEntity _living) {
                                       _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                                    }
                                 } else {
                                    _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                                    _player.getInventory().setChanged();
                                 }

                                 entity.getPersistentData().putDouble("atk12", 1.0);
                                 entity.getPersistentData().putBoolean("Attacking", false);
                                 entity.getPersistentData().putBoolean("Attack12", false);
                              }
                           } else {
                              entity.getPersistentData().putBoolean("lock", false);
                           }
                        } else {
                           entity.getPersistentData().putBoolean("fire5", false);
                        }
                     } else {
                        entity.getPersistentData().putBoolean("fire5", true);
                        if (world instanceof Level _level) {
                           if (_level.isClientSide()) {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_flame"),
                                 SoundSource.NEUTRAL,
                                 3.0F,
                                 0.7F,
                                 false
                              );
                           } else {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_flame"),
                                 SoundSource.NEUTRAL,
                                 3.0F,
                                 0.7F
                              );
                           }
                        }
                     }
                  } else {
                     entity.getPersistentData().putBoolean("lock", true);
                  }
               } else {
                  Entity _entity = entity;
                  if (!(_entity instanceof Player _player)) {
                     if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.POPPY));
                     }
                  } else {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.POPPY));
                     _player.getInventory().setChanged();
                  }

                  entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putDouble("y", entity.getXRot());
               }
            }

            if (entity.getPersistentData().getBoolean("Attack13")) {
               if (entity.getPersistentData().getDouble("atk13") != 0.0) {
                  entity.getPersistentData().putDouble("atk13", entity.getPersistentData().getDouble("atk13") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("atk13", 61.0);
               }

               if (entity.getPersistentData().getDouble("atk13") != 61.0) {
                  if (entity.getPersistentData().getDouble("atk13") != 56.0) {
                     if (entity.getPersistentData().getDouble("atk13") != 49.0) {
                        if (entity.getPersistentData().getDouble("atk13") < 40.0 && entity.getPersistentData().getDouble("atk13") > 36.0) {
                           _center = new Vec3(x + entity.getLookAngle().x * 16.0, y, z + entity.getLookAngle().z * 16.0);

                           for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5.0), e -> true)
                              .stream()
                              .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                              .collect(Collectors.toList())) {
                              if (!entityiterator.getPersistentData().getBoolean("dragon_lord")) {
                                 SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                    .setInitialPosition(entity.position())
                                    .setStunType(StunType.LONG)
                                    .setImpact(2.0F)
                                    .setArmorNegation(50.0F);
                                 entityiterator.hurt((DamageSource)dmgSrc, 40.0F);
                              }
                           }
                        } else if (entity.getPersistentData().getDouble("atk13") != 7.0) {
                           if (entity.getPersistentData().getDouble("atk13") == 1.0) {
                              Entity _entity = entity;
                              if (!(_entity instanceof Player _player)) {
                                 if (_entity instanceof LivingEntity _living) {
                                    _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                                 }
                              } else {
                                 _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                                 _player.getInventory().setChanged();
                              }

                              entity.getPersistentData().putDouble("atk13", 0.0);
                              entity.getPersistentData().putBoolean("Attacking", false);
                              entity.getPersistentData().putBoolean("Attack13", false);
                           }
                        } else {
                           if (world instanceof ServerLevel _level) {
                              _level.getServer()
                                 .getCommands()
                                 .performPrefixedCommand(
                                    new CommandSourceStack(
                                          CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                       )
                                       .withSuppressedOutput(),
                                    "/data merge entity @e[type=deepnight:dawn_dragon_lord,limit=1,sort=nearest] {NoAI:0}"
                                 );
                           }

                           entity.getPersistentData().putBoolean("lock", false);
                        }
                     } else {
                        entity.getPersistentData().putBoolean("focus", false);
                        entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                        entity.getPersistentData().putDouble("y", 0.0);
                        entity.getPersistentData().putBoolean("lock", true);
                        if (world instanceof ServerLevel _level) {
                           _level.sendParticles(
                              ParticleTypes.CAMPFIRE_SIGNAL_SMOKE,
                              x + entity.getLookAngle().x * 10.0,
                              y,
                              z + entity.getLookAngle().z * 10.0,
                              50,
                              8.0,
                              5.0,
                              8.0,
                              1.0
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
                                 "/data merge entity @e[type=deepnight:dawn_dragon_lord,limit=1,sort=nearest] {NoAI:1b}"
                              );
                        }

                        if (world instanceof Level _level) {
                           if (_level.isClientSide()) {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:lightning_dragon_sweep"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F,
                                 false
                              );
                           } else {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:lightning_dragon_sweep"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F
                              );
                           }
                        }

                        if (world instanceof Level _level) {
                           if (_level.isClientSide()) {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:electric_1"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F,
                                 false
                              );
                           } else {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:electric_1"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F
                              );
                           }
                        }
                     }
                  } else {
                     entity.getPersistentData().putBoolean("focus", true);
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_dragon_lord,sort=nearest,limit=1] {Invulnerable:0}"
                           );
                     }

                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(129.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator == (!(entity instanceof Mob _mobEnt) ? null : _mobEnt.getTarget())) {
                           Entity _ent = entity;
                           _ent.teleportTo(
                              entityiterator.getX() + entityiterator.getLookAngle().x * -14.0,
                              entityiterator.getY(),
                              entityiterator.getZ() + entityiterator.getLookAngle().z * -14.0
                           );
                           if (_ent instanceof ServerPlayer _serverPlayer) {
                              _serverPlayer.connection
                                 .teleport(
                                    entityiterator.getX() + entityiterator.getLookAngle().x * -14.0,
                                    entityiterator.getY(),
                                    entityiterator.getZ() + entityiterator.getLookAngle().z * -14.0,
                                    _ent.getYRot(),
                                    _ent.getXRot()
                                 );
                           }
                        }
                     }
                  }
               } else {
                  Entity _entity = entity;
                  if (!(_entity instanceof Player _player)) {
                     if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.PAPER));
                     }
                  } else {
                     _player.getInventory().armor.set(3, new ItemStack(Items.PAPER));
                     _player.getInventory().setChanged();
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 14, 1, false, false));
                  }

                  entity.getPersistentData().putDouble("i", 11.0);
                  entity.getPersistentData().putDouble("j", -3.0);
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_dragon_lord,sort=nearest,limit=1] {Invulnerable:1b}"
                        );
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.CAMPFIRE_COSY_SMOKE,
                        x + entity.getLookAngle().x * 8.0,
                        y + 7.0,
                        entity.getZ() + entity.getLookAngle().z * 8.0,
                        200,
                        14.0,
                        8.0,
                        14.0,
                        1.0
                     );
                  }

                  if (world instanceof Level _level) {
                     if (_level.isClientSide()) {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammerform"),
                           SoundSource.NEUTRAL,
                           4.0F,
                           0.7F,
                           false
                        );
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammerform"),
                           SoundSource.NEUTRAL,
                           4.0F,
                           0.7F
                        );
                     }
                  }

                  if (world instanceof Level _level) {
                     if (_level.isClientSide()) {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.evoker.prepare_summon"),
                           SoundSource.NEUTRAL,
                           4.0F,
                           0.7F,
                           false
                        );
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.evoker.prepare_summon"),
                           SoundSource.NEUTRAL,
                           4.0F,
                           0.7F
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new DragonSmokeEntity(SimpleMobsModEntities.DRAGON_SMOKE.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), entity.getXRot());
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
               }

               if (entity.getPersistentData().getDouble("atk13") <= 41.0 && entity.getPersistentData().getDouble("atk13") >= 35.0) {
                  _center = entity.getLookAngle();
                  new Vec3(-_center.z, 0.0, _center.x);
                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        (SimpleParticleType)SimpleMobsModParticleTypes.THUNDER_FLASH_1.get(),
                        x + entity.getLookAngle().x * 17.0,
                        y + entity.getPersistentData().getDouble("j"),
                        z + entity.getLookAngle().z * 17.0,
                        1,
                        0.1,
                        0.5,
                        0.1,
                        1.0
                     );
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        (SimpleParticleType)SimpleMobsModParticleTypes.THUNDER_FLASH_2.get(),
                        x + entity.getLookAngle().x * 17.0,
                        y + entity.getPersistentData().getDouble("j"),
                        z + entity.getLookAngle().z * 17.0,
                        2,
                        0.1,
                        0.5,
                        0.1,
                        1.0
                     );
                  }

                  entity.getPersistentData().putDouble("i", entity.getPersistentData().getDouble("i") - 1.5);
                  entity.getPersistentData().putDouble("j", entity.getPersistentData().getDouble("j") + 1.0);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack14")) {
               if (entity.getPersistentData().getDouble("atk14") != 0.0) {
                  entity.getPersistentData().putDouble("atk14", entity.getPersistentData().getDouble("atk14") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("atk14", 161.0);
               }

               if (entity.getPersistentData().getDouble("atk14") != 161.0) {
                  if (entity.getPersistentData().getDouble("atk14") != 98.0) {
                     if (entity.getPersistentData().getDouble("atk14") != 97.0) {
                        if (entity.getPersistentData().getDouble("atk14") != 46.0) {
                           if (entity.getPersistentData().getDouble("atk14") != 6.0) {
                              if (entity.getPersistentData().getDouble("atk14") == 1.0) {
                                 Entity _entity = entity;
                                 if (!(_entity instanceof Player _player)) {
                                    if (_entity instanceof LivingEntity _living) {
                                       _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                                    }
                                 } else {
                                    _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                                    _player.getInventory().setChanged();
                                 }

                                 entity.getPersistentData().putDouble("atk14", 0.0);
                                 entity.getPersistentData().putBoolean("Attacking", false);
                                 entity.getPersistentData().putBoolean("Attack14", false);
                              }
                           } else {
                              entity.getPersistentData().putBoolean("noai", false);
                           }
                        } else {
                           entity.setNoGravity(false);
                           if (world instanceof ServerLevel _level) {
                              _level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 100, 6.0, 2.0, 6.0, 0.3);
                           }

                           entity.getPersistentData().putBoolean("noai", true);
                        }
                     } else {
                        entity.getPersistentData().putBoolean("focus", false);
                        entity.getPersistentData().putBoolean("lock", true);
                        if (world instanceof Level _level) {
                           if (_level.isClientSide()) {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:dragon.roar1"),
                                 SoundSource.NEUTRAL,
                                 3.0F,
                                 0.5F,
                                 false
                              );
                           } else {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:dragon.roar1"),
                                 SoundSource.NEUTRAL,
                                 3.0F,
                                 0.5F
                              );
                           }
                        }
                     }
                  } else {
                     entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                     entity.getPersistentData().putDouble("y", entity.getXRot());
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_dragon_lord,sort=nearest,limit=1] {Invulnerable:0}"
                           );
                     }

                     entity.getPersistentData().putBoolean("focus", false);
                  }
               } else {
                  Entity _entity = entity;
                  if (!(_entity instanceof Player _player)) {
                     if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CARROT));
                     }
                  } else {
                     _player.getInventory().armor.set(3, new ItemStack(Items.CARROT));
                     _player.getInventory().setChanged();
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 14, 1, false, false));
                  }

                  if (world instanceof Level _level) {
                     if (_level.isClientSide()) {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammerform"),
                           SoundSource.NEUTRAL,
                           4.0F,
                           0.7F,
                           false
                        );
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammerform"),
                           SoundSource.NEUTRAL,
                           4.0F,
                           0.7F
                        );
                     }
                  }

                  if (world instanceof Level _level) {
                     if (_level.isClientSide()) {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.evoker.prepare_summon"),
                           SoundSource.NEUTRAL,
                           4.0F,
                           0.7F,
                           false
                        );
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.evoker.prepare_summon"),
                           SoundSource.NEUTRAL,
                           4.0F,
                           0.7F
                        );
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
                           "/data merge entity @e[type=deepnight:dawn_dragon_lord,sort=nearest,limit=1] {Invulnerable:1b}"
                        );
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new DragonSmokeEntity(SimpleMobsModEntities.DRAGON_SMOKE.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), entity.getXRot());
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.CAMPFIRE_COSY_SMOKE,
                        x + entity.getLookAngle().x * 8.0,
                        y + 7.0,
                        entity.getZ() + entity.getLookAngle().z * 8.0,
                        200,
                        14.0,
                        8.0,
                        14.0,
                        1.0
                     );
                  }

                  entity.setNoGravity(true);
                  entity.setDeltaMovement(
                     new Vec3(
                        Math.sin(Math.toRadians(Mth.nextInt(net.minecraft.util.RandomSource.create(), -360, 360))) * 7.0,
                        1.1,
                        Math.cos(Math.toRadians(Mth.nextInt(net.minecraft.util.RandomSource.create(), -360, 360))) * 7.0
                     )
                  );
                  entity.getPersistentData().putBoolean("focus", true);
               }

               if (entity.getPersistentData().getDouble("atk14") != 141.0) {
                  if (entity.getPersistentData().getDouble("atk14") == 100.0) {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator instanceof RodParticlesEntity && !entityiterator.level().isClientSide()) {
                           entityiterator.discard();
                        }
                     }
                  }
               } else {
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new RodParticlesEntity(SimpleMobsModEntities.ROD_PARTICLES.get(), _level);
                     entityToSpawn.moveTo(x, y + 1.0, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  if (world instanceof ServerLevel _level) {
                     LightningBolt entityToSpawn = EntityType.LIGHTNING_BOLT.create(_level);
                     entityToSpawn.moveTo(Vec3.atBottomCenterOf(BlockPos.containing(x, y, z)));
                     entityToSpawn.setVisualOnly(true);
                     _level.addFreshEntity(entityToSpawn);
                  }
               }

               if (entity.getPersistentData().getDouble("atk14") < 100.0 && entity.getPersistentData().getDouble("atk14") >= 42.0) {
                  entity.setDeltaMovement(
                     new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 1.8, -1.9, Math.cos(Math.toRadians(entity.getYRot())) * 1.8)
                  );
               }

               if (entity.getPersistentData().getDouble("atk14") > 46.0 && entity.getPersistentData().getDouble("atk14") < 110.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 12.0, y, z + entity.getLookAngle().z * 12.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(6.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (!(entity instanceof Mob _mobEnt) ? null : _mobEnt.getTarget())) {
                        entity.setNoGravity(false);
                        if (world instanceof ServerLevel _level) {
                           _level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 10, 6.0, 1.0, 6.0, 0.3);
                        }

                        entity.getPersistentData().putDouble("atk14", 0.0);
                        entity.getPersistentData().putBoolean("Attack14b", true);
                        entity.getPersistentData().putBoolean("Attack14", false);
                        // An early dive collision skips attack 14's frame-98 immunity release.
                        entity.setInvulnerable(false);
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack14b")) {
               if (entity.getPersistentData().getDouble("atk14b") != 0.0) {
                  entity.getPersistentData().putDouble("atk14b", entity.getPersistentData().getDouble("atk14b") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("i", 10.0);
                  entity.getPersistentData().putDouble("atk14b", 62.0);
               }

               if (entity.getPersistentData().getDouble("atk14b") != 62.0) {
                  if (entity.getPersistentData().getDouble("atk14b") != 6.0) {
                     if (entity.getPersistentData().getDouble("atk14b") == 1.0) {
                        Entity _entity = entity;
                        if (!(_entity instanceof Player _player)) {
                           if (_entity instanceof LivingEntity _living) {
                              _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                           }
                        } else {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                           _player.getInventory().setChanged();
                        }

                        entity.getPersistentData().putDouble("atk14b", 0.0);
                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putBoolean("Attack14b", false);
                     }
                  } else {
                     entity.getPersistentData().putBoolean("lock", false);
                  }
               } else {
                  Entity _entity = entity;
                  if (!(_entity instanceof Player _player)) {
                     if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_CARROT));
                     }
                  } else {
                     _player.getInventory().armor.set(3, new ItemStack(Items.GOLDEN_CARROT));
                     _player.getInventory().setChanged();
                  }

                  if (world instanceof Level _level) {
                     if (_level.isClientSide()) {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:lightning_dragon_sweep"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F,
                           false
                        );
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:lightning_dragon_sweep"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F
                        );
                     }
                  }

                  if (world instanceof Level _level) {
                     if (_level.isClientSide()) {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.lightning_bolt.thunder"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F,
                           false
                        );
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.lightning_bolt.thunder"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F
                        );
                     }
                  }

                  if (world instanceof Level _level) {
                     if (_level.isClientSide()) {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:electric_1"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F,
                           false
                        );
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:electric_1"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F
                        );
                     }
                  }

                  if (world instanceof Level _level) {
                     if (_level.isClientSide()) {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:spell_form"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           0.4F,
                           false
                        );
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:spell_form"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           0.4F
                        );
                     }
                  }
               }

               if (entity.getPersistentData().getDouble("atk14b") <= 58.0 && entity.getPersistentData().getDouble("atk14b") >= 53.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.EXPLOSION_EMITTER, x + entity.getLookAngle().x * 8.0, y, z + entity.getLookAngle().z * 8.0, 2, 8.0, 5.0, 8.0, 0.3
                     );
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        (SimpleParticleType)SimpleMobsModParticleTypes.EXPLOSION.get(),
                        x + entity.getLookAngle().x * 8.0,
                        y,
                        z + entity.getLookAngle().z * 8.0,
                        2,
                        8.0,
                        5.0,
                        8.0,
                        0.3
                     );
                  }

                  entity.getPersistentData().putDouble("i", entity.getPersistentData().getDouble("i") - 3.0);
                  _center = entity.getLookAngle();
                  Vec3 tangent = new Vec3(-_center.z, 0.0, _center.x);
                  Vec3 _centerx = new Vec3(
                     x + entity.getLookAngle().x * 14.0 + entity.getPersistentData().getDouble("i") * tangent.x,
                     y,
                     z + entity.getLookAngle().z * 14.0 + entity.getPersistentData().getDouble("i") * tangent.z
                  );

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_centerx, _centerx).inflate(8.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!entityiterator.getPersistentData().getBoolean("dragon_lord")) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.KNOCKDOWN)
                           .setImpact(2.0F)
                           .setArmorNegation(45.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 50.0F);
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        (SimpleParticleType)SimpleMobsModParticleTypes.THUNDER_FLASH_1.get(),
                        x + entity.getLookAngle().x * 14.0 + entity.getPersistentData().getDouble("i") * tangent.x,
                        y,
                        z + entity.getLookAngle().z * 14.0 + entity.getPersistentData().getDouble("i") * tangent.z,
                        4,
                        1.0,
                        0.3,
                        1.0,
                        0.3
                     );
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        (SimpleParticleType)SimpleMobsModParticleTypes.THUNDER_FLASH_2.get(),
                        x + entity.getLookAngle().x * 14.0 + entity.getPersistentData().getDouble("i") * tangent.x,
                        y,
                        z + entity.getLookAngle().z * 14.0 + entity.getPersistentData().getDouble("i") * tangent.z,
                        4,
                        1.0,
                        0.3,
                        1.0,
                        0.3
                     );
                  }
               }
            }

            if (!entity.getPersistentData().getBoolean("Attacking") && !entity.getPersistentData().getBoolean("Aggro")) {
               if (entity.getPersistentData().getDouble("idletimer") != 0.0) {
                  entity.getPersistentData().putDouble("idletimer", entity.getPersistentData().getDouble("idletimer") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("idletimer", 100.0);
               }

               if (entity.getPersistentData().getDouble("idletimer") == 1.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 3, false, false));
                  }

                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 4, false, false));
                  }
               }
            } else if (entity.getPersistentData().getBoolean("Aggro") && entity.getPersistentData().getDouble("idletimer") > 0.0) {
               entity.getPersistentData().putDouble("idletimer", 0.0);
            }
         }
      }
   }
}
