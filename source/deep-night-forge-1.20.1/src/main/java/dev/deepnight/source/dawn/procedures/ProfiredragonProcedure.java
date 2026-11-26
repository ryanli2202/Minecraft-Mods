package dev.deepnight.source.dawn.procedures;


import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.SimpleMobsMod;
import dev.deepnight.source.dawn.entity.AirEntity;
import dev.deepnight.source.dawn.entity.DragMultiPart2Entity;
import dev.deepnight.source.dawn.entity.DragMultiPart3Entity;
import dev.deepnight.source.dawn.entity.DragMultiPart4Entity;
import dev.deepnight.source.dawn.entity.DragMultiPart5Entity;
import dev.deepnight.source.dawn.entity.DragMultiPart6Entity;
import dev.deepnight.source.dawn.entity.DragonFlameEntity;
import dev.deepnight.source.dawn.entity.FireDragonAmbientEntity;
import dev.deepnight.source.dawn.entity.FireDragonGrabEntity;
import dev.deepnight.source.dawn.entity.GroundbreakEffectBigEntity;
import dev.deepnight.source.dawn.entity.SummonDragEntity;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.network.SimpleMobsModVariables;
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

public class ProfiredragonProcedure {
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

         if (!world.isClientSide()) {

            if (entity.isAlive()) {
               if (world.getEntitiesOfClass(DragMultiPart3Entity.class, AABB.ofSize(new Vec3(x, y, z), 30.0, 30.0, 30.0), e -> true).isEmpty()) {
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new DragMultiPart3Entity(SimpleMobsModEntities.DRAG_MULTI_PART_3.get(), _level);
                     entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               } else if (world.getEntitiesOfClass(DragMultiPart4Entity.class, AABB.ofSize(new Vec3(x, y, z), 30.0, 30.0, 30.0), e -> true).isEmpty()) {
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new DragMultiPart4Entity(SimpleMobsModEntities.DRAG_MULTI_PART_4.get(), _level);
                     entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               } else if (world.getEntitiesOfClass(DragMultiPart5Entity.class, AABB.ofSize(new Vec3(x, y, z), 30.0, 30.0, 30.0), e -> true).isEmpty()) {
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new DragMultiPart5Entity(SimpleMobsModEntities.DRAG_MULTI_PART_5.get(), _level);
                     entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               } else if (world.getEntitiesOfClass(DragMultiPart2Entity.class, AABB.ofSize(new Vec3(x, y, z), 30.0, 30.0, 30.0), e -> true).isEmpty()) {
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new DragMultiPart2Entity(SimpleMobsModEntities.DRAG_MULTI_PART_2.get(), _level);
                     entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               } else if (world.getEntitiesOfClass(DragMultiPart6Entity.class, AABB.ofSize(new Vec3(x, y, z), 30.0, 30.0, 30.0), e -> true).isEmpty()
                  && world instanceof ServerLevel _level) {
                  Entity entityToSpawn = new DragMultiPart6Entity(SimpleMobsModEntities.DRAG_MULTI_PART_6.get(), _level);
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
               entity.getPersistentData().putBoolean("Behind", false);
               entity.getPersistentData().putBoolean("Around", false);
               Vec3 _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(35.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                     entity.getPersistentData().putBoolean("Aggro", true);
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

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(34.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                     entity.getPersistentData().putBoolean("Far", true);
                     entity.getPersistentData().putBoolean("Mid", false);
                     entity.getPersistentData().putBoolean("Close", false);
                     entity.getPersistentData().putBoolean("Around", false);
                  }
               }

               _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(18.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                     entity.getPersistentData().putBoolean("Far", false);
                     entity.getPersistentData().putBoolean("Behind", false);
                     entity.getPersistentData().putBoolean("Around", false);
                  }
               }

               _center = new Vec3(x + entity.getLookAngle().x * 14.0, y, z + entity.getLookAngle().z * 14.0);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                     entity.getPersistentData().putBoolean("Far", false);
                     entity.getPersistentData().putBoolean("Mid", true);
                     entity.getPersistentData().putBoolean("Close", false);
                     entity.getPersistentData().putBoolean("Around", false);
                     entity.getPersistentData().putBoolean("Behind", false);
                  }
               }

               _center = new Vec3(x + entity.getLookAngle().x * 4.0, y, z + entity.getLookAngle().z * 4.0);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                     entity.getPersistentData().putBoolean("Far", false);
                     entity.getPersistentData().putBoolean("Mid", false);
                     entity.getPersistentData().putBoolean("Close", true);
                     entity.getPersistentData().putBoolean("Around", false);
                     entity.getPersistentData().putBoolean("Behind", false);
                  }
               }

               _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.5), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                     entity.getPersistentData().putBoolean("Far", false);
                     entity.getPersistentData().putBoolean("Mid", false);
                     entity.getPersistentData().putBoolean("Close", false);
                     entity.getPersistentData().putBoolean("Around", true);
                     entity.getPersistentData().putBoolean("Behind", false);
                  }
               }

               _center = new Vec3(x + entity.getLookAngle().x * -7.0, y, z + entity.getLookAngle().z * -7.0);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.5), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
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
                  entity.getPersistentData().putDouble("atk3cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 140, 200));
               } else if (entity.getPersistentData().getDouble("atk3cd") > 0.0) {
                  entity.getPersistentData().putDouble("atk3cd", entity.getPersistentData().getDouble("atk3cd") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk6cd") == 0.0 && entity.getPersistentData().getBoolean("Attack3")) {
                  entity.getPersistentData().putDouble("atk6cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 180, 280));
               } else if (entity.getPersistentData().getDouble("atk6cd") > 0.0) {
                  entity.getPersistentData().putDouble("atk6cd", entity.getPersistentData().getDouble("atk6cd") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk9cd") == 0.0 && entity.getPersistentData().getBoolean("Attack9")) {
                  entity.getPersistentData().putDouble("atk9cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 60, 140));
               } else if (entity.getPersistentData().getDouble("atk9cd") > 0.0) {
                  entity.getPersistentData().putDouble("atk9cd", entity.getPersistentData().getDouble("atk9cd") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk10cd") == 0.0 && entity.getPersistentData().getBoolean("Attack10")) {
                  entity.getPersistentData().putDouble("atk10cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 200, 260));
               } else if (entity.getPersistentData().getDouble("atk10cd") > 0.0) {
                  entity.getPersistentData().putDouble("atk10cd", entity.getPersistentData().getDouble("atk10cd") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk11cd") == 0.0 && entity.getPersistentData().getBoolean("Attack11")) {
                  entity.getPersistentData().putDouble("atk11cd", 300.0);
               } else if (entity.getPersistentData().getDouble("atk11cd") > 0.0) {
                  entity.getPersistentData().putDouble("atk11cd", entity.getPersistentData().getDouble("atk11cd") - 1.0);
               }

               if (entity.getPersistentData().getDouble("upcd2") == 0.0 && entity.getPersistentData().getBoolean("Up")) {
                  entity.getPersistentData().putDouble("upcd2", Mth.nextInt(net.minecraft.util.RandomSource.create(), 400, 500));
               } else if (entity.getPersistentData().getDouble("upcd2") > 0.0) {
                  entity.getPersistentData().putDouble("upcd2", entity.getPersistentData().getDouble("upcd2") - 1.0);
               }

               if (entity.getPersistentData().getBoolean("hurtcd")) {
                  if (entity.getPersistentData().getDouble("hurt_cd") == 0.0) {
                     entity.getPersistentData().putDouble("hurt_cd", 6.0);
                  } else {
                     entity.getPersistentData().putDouble("hurt_cd", entity.getPersistentData().getDouble("hurt_cd") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("hurt_cd") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(0, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.FEET, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putDouble("hurt_cd", 0.0);
                     entity.getPersistentData().putBoolean("hurtcd", false);
                  }
               }

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

               if ((entity.isInLava() || entity.isInWater()) && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Up", true);
               }

               if (!entity.getPersistentData().getBoolean("air") && entity.onGround()) {
                  if (entity.getPersistentData().getBoolean("Around")) {
                     if (Math.random() <= 0.3 && !entity.getPersistentData().getBoolean("Attacking")) {
                        entity.getPersistentData().putBoolean("Attacking", true);
                        entity.getPersistentData().putBoolean("Attack1", true);
                     } else if (Math.random() >= 0.31
                        && Math.random() <= 0.6
                        && !entity.getPersistentData().getBoolean("Attacking")
                        && entity.getPersistentData().getDouble("atk6cd") == 0.0) {
                        entity.getPersistentData().putBoolean("Attack6", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (entity.getPersistentData().getDouble("upcd2") == 0.0 && !entity.getPersistentData().getBoolean("Attacking")) {
                        entity.getPersistentData().putBoolean("Up", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     }
                  } else if (entity.getPersistentData().getBoolean("Behind") && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("Attack2", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (entity.getPersistentData().getBoolean("Close") && !entity.getPersistentData().getBoolean("Attacking")) {
                     if (Math.random() <= 0.25 && !entity.getPersistentData().getBoolean("Attacking") && entity.getPersistentData().getDouble("atk3cd") == 0.0) {
                        entity.getPersistentData().putBoolean("Attacking", true);
                        entity.getPersistentData().putBoolean("Attack3", true);
                     } else if (Math.random() >= 0.26
                        && Math.random() <= 0.5
                        && !entity.getPersistentData().getBoolean("Attacking")
                        && entity.getPersistentData().getDouble("atk9cd") == 0.0) {
                        entity.getPersistentData().putBoolean("Attack9", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (Math.random() >= 0.51
                        && Math.random() <= 0.75
                        && !entity.getPersistentData().getBoolean("Attacking")
                        && entity.getPersistentData().getDouble("atk10cd") == 0.0) {
                        entity.getPersistentData().putBoolean("Attack10", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (!entity.getPersistentData().getBoolean("Attacking") && entity.getPersistentData().getDouble("atk11cd") == 0.0) {
                        entity.getPersistentData().putBoolean("Attack11", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     }
                  } else if (entity.getPersistentData().getBoolean("Mid") && !entity.getPersistentData().getBoolean("Attacking")) {
                     if (Math.random() <= 0.1 && entity.getPersistentData().getDouble("upcd2") == 0.0) {
                        entity.getPersistentData().putBoolean("Up", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     }
                  } else if (entity.getPersistentData().getBoolean("Far") && !entity.getPersistentData().getBoolean("Attacking")) {
                     if (Math.random() <= 0.5 && !entity.getPersistentData().getBoolean("Attacking")) {
                        entity.getPersistentData().putBoolean("Attack5", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     } else if (Math.random() >= 0.51 && !entity.getPersistentData().getBoolean("Attacking")) {
                        entity.getPersistentData().putBoolean("Attack4", true);
                        entity.getPersistentData().putBoolean("Attacking", true);
                     }
                  }
               } else if (entity.getPersistentData().getDouble("aircd") == 0.0 && entity.getPersistentData().getBoolean("air")) {
                  if (Math.random() <= 0.5 && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("Attack7", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (!entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("Attack8", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  }
               }

               if (entity.getPersistentData().getBoolean("Wake")) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(75.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator instanceof SummonDragEntity) {
                        entity.getPersistentData().putBoolean("Wake", false);
                        entity.getPersistentData().putBoolean("Attack7", true);
                     }
                  }
               }

               if (entity.getPersistentData().getBoolean("Wake")) {
                  if (entity.getPersistentData().getDouble("wake") == 0.0) {
                     entity.getPersistentData().putDouble("wake", 81.0);
                  } else {
                     entity.getPersistentData().putDouble("wake", entity.getPersistentData().getDouble("wake") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("wake") == 81.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.OAK_LEAVES));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.OAK_LEAVES));
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 81, 6, false, false));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     entity.getPersistentData().putBoolean("noai", true);
                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 60, 3.0, 3.0, 3.0, 0.4);
                     }
                  } else if (entity.getPersistentData().getDouble("wake") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putDouble("wake", 0.0);
                     entity.getPersistentData().putBoolean("noai", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Wake", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack1")) {
                  if (entity.getPersistentData().getDouble("atk1") == 0.0) {
                     entity.getPersistentData().putDouble("atk1", 45.0);
                  } else {
                     entity.getPersistentData().putDouble("atk1", entity.getPersistentData().getDouble("atk1") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("atk1") == 45.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.LEATHER_HELMET));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 45, 6, false, false));
                     }

                     entity.getPersistentData().putDouble("xz", entity.getYRot());
                     entity.getPersistentData().putDouble("y", entity.getXRot());
                  } else if (entity.getPersistentData().getDouble("atk1") == 44.0) {
                     entity.getPersistentData().putBoolean("lock", true);
                  } else if (entity.getPersistentData().getDouble("atk1") == 23.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                              SoundSource.NEUTRAL,
                              3.0F,
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
                              dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F,
                              false
                           );
                        }
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 30, 3.0, 3.0, 3.0, 1.0);
                     }
                  } else if (entity.getPersistentData().getDouble("atk1") < 23.0 && entity.getPersistentData().getDouble("atk1") > 18.0) {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("firedragon")) {
                           if (entityiterator instanceof LivingEntity _entity) {
                              _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 20, 0));
                           }

                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.LONG)
                              .setImpact(2.0F)
                              .setArmorNegation(40.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 16.0F);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("atk1") == 18.0) {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("firedragon")) {
                           entity.getPersistentData().putBoolean("Attack1b", true);
                           entity.getPersistentData().putDouble("atk1", 0.0);
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

                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putDouble("atk1", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Attack1", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putDouble("atk1", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Attack1", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack1b")) {
                  if (entity.getPersistentData().getDouble("atk1b") == 0.0) {
                     entity.getPersistentData().putDouble("atk1b", 41.0);
                  } else {
                     entity.getPersistentData().putDouble("atk1b", entity.getPersistentData().getDouble("atk1b") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("atk1b") == 41.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.GOLD_NUGGET));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLD_NUGGET));
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 41, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("atk1b") == 27.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                              SoundSource.NEUTRAL,
                              3.0F,
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
                              dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F,
                              false
                           );
                        }
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 30, 3.0, 3.0, 3.0, 1.0);
                     }
                  } else if (entity.getPersistentData().getDouble("atk1b") < 27.0 && entity.getPersistentData().getDouble("atk1b") > 23.0) {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("firedragon")) {
                           if (entityiterator instanceof LivingEntity _entity) {
                              _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 20, 0));
                           }

                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.LONG)
                              .setImpact(2.0F)
                              .setArmorNegation(40.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 16.0F);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("atk1b") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putDouble("atk1b", 0.0);
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Attack1b", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putDouble("atk1b", 0.0);
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Attack1b", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack2")) {
                  if (entity.getPersistentData().getDouble("atk2") == 0.0) {
                     entity.getPersistentData().putDouble("atk2", 51.0);
                  } else {
                     entity.getPersistentData().putDouble("atk2", entity.getPersistentData().getDouble("atk2") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("atk2") == 51.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.COAL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.COAL));
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 51, 7, false, false));
                     }

                     entity.getPersistentData().putDouble("xz", entity.getYRot());
                     entity.getPersistentData().putDouble("y", entity.getXRot());
                     entity.getPersistentData().putDouble("i", 10.0);
                  } else if (entity.getPersistentData().getDouble("atk2") == 50.0) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_fire_dragon,limit=1,sort=nearest] {NoAI:1b}"
                           );
                     }

                     entity.getPersistentData().putBoolean("lock", true);
                  } else if (entity.getPersistentData().getDouble("atk2") == 39.0) {
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

                     entity.getPersistentData().putBoolean("sweep", true);
                  } else if (entity.getPersistentData().getDouble("atk2") == 10.0) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_fire_dragon,limit=1,sort=nearest] {NoAI:0}"
                           );
                     }

                     entity.getPersistentData().putBoolean("lock", false);
                  } else if (entity.getPersistentData().getDouble("atk2") == 1.0) {
                     entity.getPersistentData().putBoolean("sweep", false);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putDouble("atk2", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Attack2", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_fire_dragon,limit=1,sort=nearest] {NoAI:0}"
                           );
                     }

                     entity.getPersistentData().putDouble("atk2", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Attack2", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack3")) {
                  if (entity.getPersistentData().getDouble("Atk3") <= 0.0) {
                     entity.getPersistentData().putDouble("Atk3", 101.0);
                  } else {
                     entity.getPersistentData().putDouble("Atk3", entity.getPersistentData().getDouble("Atk3") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Atk3") == 101.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.CHARCOAL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHARCOAL));
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 76, 10, false, false));
                     }

                     entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                     entity.getPersistentData().putDouble("y", entity.getXRot());
                  } else if (entity.getPersistentData().getDouble("Atk3") == 100.0) {
                     entity.getPersistentData().putBoolean("lock", true);
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_fire_dragon,limit=1,sort=nearest] {NoAI:1b}"
                           );
                     }
                  } else if (entity.getPersistentData().getDouble("Atk3") == 76.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_flame"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_flame"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F,
                              false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk3") < 76.0 && entity.getPersistentData().getDouble("Atk3") >= 22.0) {
                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new DragonFlameEntity(SimpleMobsModEntities.DRAGON_FLAME.get(), _level);
                        entityToSpawn.moveTo(
                           x + entity.getLookAngle().x * 4.0,
                           y + 9.0,
                           z + entity.getLookAngle().z * 4.0,
                           (float)Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.0, 360.0),
                           (float)(60.0 + Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.0, 30.0))
                        );
                        entityToSpawn.setYBodyRot((float)Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.0, 360.0));
                        entityToSpawn.setYHeadRot((float)Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.0, 360.0));
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  } else if (entity.getPersistentData().getDouble("Atk3") == 10.0) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_fire_dragon,limit=1,sort=nearest] {NoAI:0}"
                           );
                     }

                     entity.getPersistentData().putBoolean("lock", false);
                  } else if (entity.getPersistentData().getDouble("Atk3") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putDouble("Atk3", 0.0);
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Attack3", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_fire_dragon,limit=1,sort=nearest] {NoAI:0}"
                           );
                     }

                     entity.getPersistentData().putDouble("Atk3", 0.0);
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Attack3", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack4")) {
                  if (entity.getPersistentData().getDouble("Atk4") <= 0.0) {
                     entity.getPersistentData().putDouble("Atk4", 104.0);
                  } else {
                     entity.getPersistentData().putDouble("Atk4", entity.getPersistentData().getDouble("Atk4") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Atk4") == 104.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     entity.getPersistentData().putBoolean("focus", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.IRON_INGOT));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_INGOT));
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 104, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("Atk4") == 81.0) {
                     entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                     entity.getPersistentData().putDouble("y", entity.getPersistentData().getDouble("pitch2") + 5.0);
                     entity.getPersistentData().putDouble("turn", 25.0);
                     entity.getPersistentData().putDouble("turn1", 2.0);
                  } else if (entity.getPersistentData().getDouble("Atk4") == 80.0) {
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putBoolean("lock", true);
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_flame"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_flame"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F,
                              false
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
                              "/data merge entity @e[type=deepnight:dawn_fire_dragon,limit=1,sort=nearest] {NoAI:1b}"
                           );
                     }
                  } else if (entity.getPersistentData().getDouble("Atk4") < 80.0 && entity.getPersistentData().getDouble("Atk4") > 29.0) {
                     _center = entity.getLookAngle();
                     Vec3 tangent = new Vec3(-_center.z, 0.0, _center.x);
                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new DragonFlameEntity(SimpleMobsModEntities.DRAGON_FLAME.get(), _level);
                        entityToSpawn.moveTo(
                           x + entity.getLookAngle().x * 11.0 + (float)entity.getPersistentData().getDouble("turn1") * tangent.x,
                           y + 7.0,
                           z + entity.getLookAngle().z * 11.0 + (float)entity.getPersistentData().getDouble("turn1") * tangent.z,
                           (float)(entity.getPersistentData().getDouble("xz") + entity.getPersistentData().getDouble("turn")),
                           (float)entity.getPersistentData().getDouble("y")
                        );
                        entityToSpawn.setYBodyRot((float)(entity.getPersistentData().getDouble("xz") + entity.getPersistentData().getDouble("turn")));
                        entityToSpawn.setYHeadRot((float)(entity.getPersistentData().getDouble("xz") + entity.getPersistentData().getDouble("turn")));
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }

                     entity.getPersistentData().putDouble("turn", entity.getPersistentData().getDouble("turn") - 1.0);
                     entity.getPersistentData().putDouble("turn1", entity.getPersistentData().getDouble("turn1") - 0.1);
                  } else if (entity.getPersistentData().getDouble("Atk4") == 4.0) {
                     entity.getPersistentData().putBoolean("lock", false);
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_fire_dragon,limit=1,sort=nearest] {NoAI:0}"
                           );
                     }
                  } else if (entity.getPersistentData().getDouble("Atk4") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putDouble("Atk4", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Attack4", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_fire_dragon,limit=1,sort=nearest] {NoAI:0}"
                           );
                     }

                     entity.getPersistentData().putDouble("Atk4", 0.0);
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Attack4", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack5")) {
                  if (entity.getPersistentData().getDouble("Atk5") <= 0.0) {
                     entity.getPersistentData().putDouble("Atk5", 145.0);
                  } else {
                     entity.getPersistentData().putDouble("Atk5", entity.getPersistentData().getDouble("Atk5") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Atk5") == 145.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     entity.getPersistentData().putBoolean("focus", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.IRON_NUGGET));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_NUGGET));
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 76, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("Atk5") == 126.0) {
                     entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                     entity.getPersistentData().putDouble("y", entity.getPersistentData().getDouble("pitch2"));
                  } else if (entity.getPersistentData().getDouble("Atk5") == 125.0) {
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
                              "/data merge entity @e[type=deepnight:dawn_fire_dragon,limit=1,sort=nearest] {NoAI:1b}"
                           );
                     }

                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_flame2"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_flame2"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F,
                              false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk5") < 125.0 && entity.getPersistentData().getDouble("Atk5") > 94.0) {
                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new DragonFlameEntity(SimpleMobsModEntities.DRAGON_FLAME.get(), _level);
                        entityToSpawn.moveTo(
                           x + entity.getLookAngle().x * 12.0,
                           y + 5.5,
                           z + entity.getLookAngle().z * 12.0,
                           (float)entity.getPersistentData().getDouble("xz"),
                           (float)entity.getPersistentData().getDouble("y")
                        );
                        entityToSpawn.setYBodyRot((float)entity.getPersistentData().getDouble("xz"));
                        entityToSpawn.setYHeadRot((float)entity.getPersistentData().getDouble("xz"));
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  } else if (entity.getPersistentData().getDouble("Atk5") == 94.0) {
                     entity.getPersistentData().putBoolean("lock", false);
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_fire_dragon,limit=1,sort=nearest] {NoAI:0}"
                           );
                     }
                  } else if (entity.getPersistentData().getDouble("Atk5") == 75.0) {
                     entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                     entity.getPersistentData().putDouble("y", entity.getPersistentData().getDouble("pitch2"));
                  } else if (entity.getPersistentData().getDouble("Atk5") == 74.0) {
                     entity.getPersistentData().putBoolean("lock", true);
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_fire_dragon,limit=1,sort=nearest] {NoAI:1b}"
                           );
                     }

                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_flame2"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_flame2"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F,
                              false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk5") < 74.0 && entity.getPersistentData().getDouble("Atk5") > 50.0) {
                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new DragonFlameEntity(SimpleMobsModEntities.DRAGON_FLAME.get(), _level);
                        entityToSpawn.moveTo(
                           x + entity.getLookAngle().x * 12.0,
                           y + 5.5,
                           z + entity.getLookAngle().z * 12.0,
                           (float)entity.getPersistentData().getDouble("xz"),
                           (float)entity.getPersistentData().getDouble("y")
                        );
                        entityToSpawn.setYBodyRot((float)entity.getPersistentData().getDouble("xz"));
                        entityToSpawn.setYHeadRot((float)entity.getPersistentData().getDouble("xz"));
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  } else if (entity.getPersistentData().getDouble("Atk5") == 1.0) {
                     entity.getPersistentData().putBoolean("lock", false);
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_fire_dragon,limit=1,sort=nearest] {NoAI:0}"
                           );
                     }

                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putDouble("Atk5", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Attack5", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_fire_dragon,limit=1,sort=nearest] {NoAI:0}"
                           );
                     }

                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putDouble("Atk5", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Attack5", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack6")) {
                  if (entity.getPersistentData().getDouble("Atk6") <= 0.0) {
                     entity.getPersistentData().putDouble("Atk6", 51.0);
                  } else {
                     entity.getPersistentData().putDouble("Atk6", entity.getPersistentData().getDouble("Atk6") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Atk6") == 51.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.WHITE_WOOL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.WHITE_WOOL));
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 76, 6, false, false));
                     }

                     entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                     entity.getPersistentData().putDouble("y", entity.getXRot());
                  } else if (entity.getPersistentData().getDouble("Atk6") == 50.0) {
                     entity.getPersistentData().putBoolean("lock", true);
                     entity.getPersistentData().putBoolean("noai", true);
                  } else if (entity.getPersistentData().getDouble("Atk6") == 33.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_wing_flap"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_wing_flap"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F,
                              false
                           );
                        }
                     }

                     entity.setDeltaMovement(
                        new Vec3(Math.sin(Math.toRadians(entity.getYRot())) * 2.0, 1.6, Math.cos(Math.toRadians(entity.getYRot() + 180.0F)) * 2.0)
                     );
                  } else if (entity.getPersistentData().getDouble("Atk6") == 1.0) {
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putBoolean("noai", false);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putDouble("Atk6", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Attack6", false);
                  }

                  if (entity.getPersistentData().getDouble("Atk6") < 35.0 && entity.getPersistentData().getDouble("Atk6") >= 30.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("firedragon")) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.SHORT)
                              .setImpact(3.0F)
                              .setArmorNegation(40.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 10.0F);
                           entityiterator.setDeltaMovement(
                              new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 1.7, 0.8, Math.cos(Math.toRadians(entity.getYRot())) * 1.7)
                           );
                        }
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_fire_dragon,limit=1,sort=nearest] {NoAI:0}"
                           );
                     }

                     entity.getPersistentData().putBoolean("noai", false);
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putDouble("Atk6", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Attack6", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Up")) {
                  if (entity.getPersistentData().getDouble("up") <= 0.0) {
                     entity.getPersistentData().putDouble("up", 44.0);
                  } else {
                     entity.getPersistentData().putDouble("up", entity.getPersistentData().getDouble("up") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("up") == 44.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     entity.getPersistentData().putBoolean("air", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.WHITE_WOOL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.WHITE_WOOL));
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 76, 6, false, false));
                     }

                     entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                     entity.getPersistentData().putDouble("y", entity.getXRot());
                  } else if (entity.getPersistentData().getDouble("up") == 43.0) {
                     entity.getPersistentData().putBoolean("lock", true);
                     entity.getPersistentData().putBoolean("noai", true);
                  } else if (entity.getPersistentData().getDouble("up") == 24.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_wing_flap2"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_wing_flap2"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F,
                              false
                           );
                        }
                     }

                     entity.setDeltaMovement(new Vec3(0.0, 1.6, 0.0));
                  } else if (entity.getPersistentData().getDouble("up") == 10.0) {
                     entity.setNoGravity(true);
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putBoolean("noai", false);
                  } else if (entity.getPersistentData().getDouble("up") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putDouble("up", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Up", false);
                  }

                  if (entity.getPersistentData().getDouble("up") < 25.0 && entity.getPersistentData().getDouble("up") >= 23.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("firedragon")) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.SHORT)
                              .setImpact(3.0F)
                              .setArmorNegation(40.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 6.0F);
                           entityiterator.setDeltaMovement(
                              new Vec3(
                                 Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 0.5, 0.3, Math.cos(Math.toRadians(entity.getYRot() + 180.0F)) * 0.5
                              )
                           );
                        }
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_fire_dragon,limit=1,sort=nearest] {NoAI:0}"
                           );
                     }

                     entity.getPersistentData().putBoolean("noai", false);
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putDouble("up", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Up", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack7")) {
                  if (entity.getPersistentData().getDouble("atk7") <= 0.0) {
                     entity.getPersistentData().putDouble("atk7", 98.0);
                  } else {
                     entity.getPersistentData().putDouble("atk7", entity.getPersistentData().getDouble("atk7") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("atk7") == 98.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:dragon.roar1"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:dragon.roar1"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F,
                              false
                           );
                        }
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     entity.getPersistentData().putBoolean("focus", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.WHITE_CONCRETE));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.WHITE_CONCRETE));
                     }
                  } else if (entity.getPersistentData().getDouble("atk7") == 78.0) {
                     entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                     entity.getPersistentData().putDouble("y", entity.getXRot());
                  } else if (entity.getPersistentData().getDouble("atk7") == 77.0) {
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putBoolean("lock", true);
                  } else if (entity.getPersistentData().getDouble("atk7") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putDouble("atk7", 0.0);
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.setNoGravity(true);
                     entity.getPersistentData().putBoolean("Attack7", false);
                  }

                  if (entity.getPersistentData().getDouble("atk7") < 90.0 && entity.getPersistentData().getDouble("atk7") >= 1.0) {
                     entity.setDeltaMovement(
                        new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 1.2, -0.9, Math.cos(Math.toRadians(entity.getYRot())) * 1.2)
                     );
                  }

                  if (world.getBlockState(BlockPos.containing(x, y - 1.0, z)).canOcclude()) {
                     entity.setNoGravity(false);
                     entity.getPersistentData().putDouble("atk7", 0.0);
                     entity.getPersistentData().putBoolean("Attack7a", true);
                     entity.getPersistentData().putBoolean("Attack7", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_fire_dragon,limit=1,sort=nearest] {NoAI:0}"
                           );
                     }

                     entity.getPersistentData().putBoolean("noai", false);
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putDouble("atk7", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Attack7", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack7a")) {
                  if (entity.getPersistentData().getDouble("atk7a") <= 0.0) {
                     entity.getPersistentData().putDouble("atk7a", 62.0);
                  } else {
                     entity.getPersistentData().putDouble("atk7a", entity.getPersistentData().getDouble("atk7a") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("atk7a") == 62.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     entity.getPersistentData().putBoolean("air", false);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.RED_WOOL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.RED_WOOL));
                     }

                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new GroundbreakEffectBigEntity(SimpleMobsModEntities.GROUNDBREAK_EFFECT_BIG.get(), _level);
                        entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }

                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new AirEntity(SimpleMobsModEntities.AIR.get(), _level);
                        entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }

                     entity.getPersistentData().putBoolean("lock", true);
                     entity.getPersistentData().putBoolean("noai", true);
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:jump"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x, y, z, dev.deepnight.SafeSounds.resolve("simple_mobs:jump"), SoundSource.NEUTRAL, 3.0F, 1.0F, false
                           );
                        }
                     }

                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              0.5F
                           );
                        } else {
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
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("atk7a") == 61.0) {
                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 160, 4.0, 4.0, 4.0, 0.6);
                     }
                  } else if (entity.getPersistentData().getDouble("atk7a") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putDouble("atk7a", 0.0);
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putBoolean("noai", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Attack7a", false);
                  }

                  if (entity.getPersistentData().getDouble("atk7a") <= 61.0 && entity.getPersistentData().getDouble("atk7a") >= 56.0) {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(6.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("firedragon")) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.KNOCKDOWN)
                              .setImpact(3.0F)
                              .setArmorNegation(40.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 24.0F);
                        }
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_fire_dragon,limit=1,sort=nearest] {NoAI:0}"
                           );
                     }

                     entity.getPersistentData().putBoolean("noai", false);
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putDouble("atk7a", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Attack7a", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack8")) {
                  if (entity.getPersistentData().getDouble("atk8") <= 0.0) {
                     entity.getPersistentData().putDouble("atk8", 71.0);
                  } else {
                     entity.getPersistentData().putDouble("atk8", entity.getPersistentData().getDouble("atk8") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("atk8") == 71.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     entity.getPersistentData().putBoolean("focus", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.YELLOW_WOOL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.YELLOW_WOOL));
                     }
                  } else if (entity.getPersistentData().getDouble("atk8") == 67.0) {
                     entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                     entity.getPersistentData().putDouble("y", 0.0);
                  } else if (entity.getPersistentData().getDouble("atk8") == 66.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_flame"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_flame"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F,
                              false
                           );
                        }
                     }

                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putBoolean("lock", true);
                  } else if (entity.getPersistentData().getDouble("atk8") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.setNoGravity(false);
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putBoolean("air", false);
                     entity.getPersistentData().putDouble("atk8", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Attack8", false);
                  }

                  if (entity.getPersistentData().getDouble("atk8") <= 61.0) {
                     entity.setDeltaMovement(
                        new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 0.8, 0.0, Math.cos(Math.toRadians(entity.getYRot())) * 0.8)
                     );
                     if (entity.getPersistentData().getDouble("atk8") >= 11.0 && world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new DragonFlameEntity(SimpleMobsModEntities.DRAGON_FLAME.get(), _level);
                        entityToSpawn.moveTo(
                           x + entity.getLookAngle().x * 8.0,
                           y + 3.0,
                           z + entity.getLookAngle().z * 8.0,
                           (float)entity.getPersistentData().getDouble("yaw2"),
                           (float)(89.0 + Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.0, 1.0))
                        );
                        entityToSpawn.setYBodyRot((float)entity.getPersistentData().getDouble("yaw2"));
                        entityToSpawn.setYHeadRot((float)entity.getPersistentData().getDouble("yaw2"));
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_fire_dragon,limit=1,sort=nearest] {NoAI:0}"
                           );
                     }

                     entity.getPersistentData().putBoolean("noai", false);
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putDouble("atk8", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Attack8", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack9")) {
                  if (entity.getPersistentData().getDouble("atk9") == 0.0) {
                     entity.getPersistentData().putDouble("atk9", 51.0);
                  } else {
                     entity.getPersistentData().putDouble("atk9", entity.getPersistentData().getDouble("atk9") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("atk9") == 51.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.GREEN_WOOL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.GREEN_WOOL));
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 51, 6, false, false));
                     }

                     entity.getPersistentData().putBoolean("focus", true);
                  } else if (entity.getPersistentData().getDouble("atk9") == 40.0) {
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                     entity.getPersistentData().putDouble("y", entity.getXRot());
                     entity.getPersistentData().putDouble("s", 7.0);
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_fire_dragon,limit=1,sort=nearest] {NoAI:1b}"
                           );
                     }

                     entity.getPersistentData().putBoolean("lock", true);
                  } else if (entity.getPersistentData().getDouble("atk9") == 36.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("entity.donkey.eat"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x, y, z, dev.deepnight.SafeSounds.resolve("entity.donkey.eat"), SoundSource.NEUTRAL, 2.0F, 1.0F, false
                           );
                        }
                     }

                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("entity.dolphin.eat"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("entity.dolphin.eat"),
                              SoundSource.NEUTRAL,
                              3.0F,
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
                              dev.deepnight.SafeSounds.resolve("simple_mobs:attack1"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:attack1"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F,
                              false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("atk9") == 10.0) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_fire_dragon,limit=1,sort=nearest] {NoAI:0}"
                           );
                     }

                     entity.getPersistentData().putBoolean("lock", false);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putDouble("atk9", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Attack9", false);
                  }

                  if (entity.getPersistentData().getDouble("atk9") < 39.0 && entity.getPersistentData().getDouble("atk9") > 34.0) {
                     _center = new Vec3(
                        x + entity.getLookAngle().x * entity.getPersistentData().getDouble("s"),
                        y,
                        z + entity.getLookAngle().z * entity.getPersistentData().getDouble("s")
                     );

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("firedragon")) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.LONG)
                              .setImpact(14.0F)
                              .setArmorNegation(25.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 24.0F);
                        }
                     }

                     entity.getPersistentData().putDouble("s", entity.getPersistentData().getDouble("s") + 1.0);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_fire_dragon,limit=1,sort=nearest] {NoAI:0}"
                           );
                     }

                     entity.getPersistentData().putBoolean("noai", false);
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putDouble("atk9", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Attack9", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack10")) {
                  if (entity.getPersistentData().getDouble("atk10") == 0.0) {
                     entity.getPersistentData().putDouble("atk10", 76.0);
                  } else {
                     entity.getPersistentData().putDouble("atk10", entity.getPersistentData().getDouble("atk10") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("atk10") == 76.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.ACACIA_LEAVES));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.ACACIA_LEAVES));
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 76, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("atk10") == 49.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                              SoundSource.NEUTRAL,
                              3.0F,
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
                              dev.deepnight.SafeSounds.resolve("simple_mobs:jump"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              0.5F
                           );
                        } else {
                           _level.playLocalSound(
                              x, y, z, dev.deepnight.SafeSounds.resolve("simple_mobs:jump"), SoundSource.NEUTRAL, 3.0F, 0.5F, false
                           );
                        }
                     }

                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new AirEntity(SimpleMobsModEntities.AIR.get(), _level);
                        entityToSpawn.moveTo(
                           x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0, world.getRandom().nextFloat() * 360.0F, 0.0F
                        );
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(
                           ParticleTypes.CAMPFIRE_COSY_SMOKE, x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0, 30, 3.0, 3.0, 3.0, 1.0
                        );
                     }
                  } else if (entity.getPersistentData().getDouble("atk10") < 49.0 && entity.getPersistentData().getDouble("atk10") > 44.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("firedragon")) {
                           if (entityiterator instanceof LivingEntity _entity) {
                              _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 20, 1));
                           }

                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.KNOCKDOWN)
                              .setImpact(3.0F)
                              .setArmorNegation(40.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 28.0F);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("atk10") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putDouble("atk10", 0.0);
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Attack10", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_fire_dragon,limit=1,sort=nearest] {NoAI:0}"
                           );
                     }

                     entity.getPersistentData().putBoolean("noai", false);
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putDouble("atk10", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("attack10", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack11")) {
                  if (entity.getPersistentData().getDouble("atk11") == 0.0) {
                     entity.getPersistentData().putDouble("atk11", 101.0);
                  } else {
                     entity.getPersistentData().putDouble("atk11", entity.getPersistentData().getDouble("atk11") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("atk11") == 101.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.SADDLE));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.SADDLE));
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 101, 6, false, false));
                     }

                     entity.getPersistentData().putBoolean("focus", true);
                  } else if (entity.getPersistentData().getDouble("atk11") == 86.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_roar2"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:dragon_roar2"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F,
                              false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("atk11") <= 86.0 && entity.getPersistentData().getDouble("atk11") >= 61.0) {
                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(
                           ParticleTypes.FLAME, x + entity.getLookAngle().x * 9.0, y + 9.5, z + entity.getLookAngle().z * 11.0, 1, 0.2, 0.2, 0.2, 0.5
                        );
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(
                           ParticleTypes.FLASH, x + entity.getLookAngle().x * 9.0, y + 9.5, z + entity.getLookAngle().z * 11.0, 1, 0.2, 0.2, 0.2, 0.5
                        );
                     }
                  } else if (entity.getPersistentData().getDouble("atk11") == 54.0) {
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                     entity.getPersistentData().putDouble("y", 0.0);
                     entity.getPersistentData().putBoolean("lock", true);
                  } else if (entity.getPersistentData().getDouble("atk11") < 58.0 && entity.getPersistentData().getDouble("atk11") > 43.0) {
                     entity.setDeltaMovement(
                        new Vec3(Math.sin(Math.toRadians(entity.getYRot())) * 0.17, 0.0, Math.cos(Math.toRadians(entity.getYRot() + 180.0F)) * 0.17)
                     );
                  } else if (entity.getPersistentData().getDouble("atk11") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putDouble("atk11", 0.0);
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Attack11", false);
                  }

                  if (entity.getPersistentData().getDouble("atk11") < 36.0 && entity.getPersistentData().getDouble("atk11") > 34.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 10.0, y + 1.0, z + entity.getLookAngle().z * 10.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                           if (world instanceof ServerLevel _level) {
                              Entity entityToSpawn = new FireDragonGrabEntity(SimpleMobsModEntities.FIRE_DRAGON_GRAB.get(), _level);
                              entityToSpawn.moveTo(x + entity.getLookAngle().x * 10.0, y + 1.0, z + entity.getLookAngle().z * 10.0, 0.0F, 0.0F);
                              entityToSpawn.setYBodyRot(0.0F);
                              entityToSpawn.setYHeadRot(0.0F);
                              if (entityToSpawn instanceof Mob _mobToSpawn) {
                                 _mobToSpawn.finalizeSpawn(
                                    _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                                 );
                              }

                              world.addFreshEntity(entityToSpawn);
                           }

                           Entity _ent = entityiterator;
                           _ent.teleportTo(x + entity.getLookAngle().x * 10.0, y + 2.0, z + entity.getLookAngle().z * 10.0);
                           if (_ent instanceof ServerPlayer _serverPlayer) {
                              _serverPlayer.connection
                                 .teleport(x + entity.getLookAngle().x * 10.0, y + 2.0, z + entity.getLookAngle().z * 10.0, _ent.getYRot(), _ent.getXRot());
                           }

                           if (world instanceof Level _level) {
                              if (!_level.isClientSide()) {
                                 _level.playSound(
                                    null,
                                    BlockPos.containing(x + entity.getLookAngle().x * 10.0, y, z + entity.getLookAngle().z * 10.0),
                                    dev.deepnight.SafeSounds.resolve("entity.turtle.egg_crack"),
                                    SoundSource.NEUTRAL,
                                    2.0F,
                                    1.0F
                                 );
                              } else {
                                 _level.playLocalSound(
                                    x + entity.getLookAngle().x * 10.0,
                                    y,
                                    z + entity.getLookAngle().z * 10.0,
                                    dev.deepnight.SafeSounds.resolve("entity.turtle.egg_crack"),
                                    SoundSource.NEUTRAL,
                                    2.0F,
                                    1.0F,
                                    false
                                 );
                              }
                           }

                           entity.getPersistentData().putBoolean("Attack11G", true);
                           entity.getPersistentData().putDouble("atk11", 0.0);
                           entity.getPersistentData().putBoolean("Attack11", false);
                        }
                     }
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_fire_dragon,limit=1,sort=nearest] {NoAI:0}"
                           );
                     }

                     entity.getPersistentData().putBoolean("noai", false);
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putDouble("atk11", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Attack11", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack11G")) {
                  if (entity.getPersistentData().getDouble("atk11g") == 0.0) {
                     entity.getPersistentData().putDouble("atk11g", 91.0);
                  } else {
                     entity.getPersistentData().putDouble("atk11g", entity.getPersistentData().getDouble("atk11g") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("atk11g") == 91.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.SCUTE));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.SCUTE));
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 91, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("atk11g") == 86.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 10.0, y + 3.0, z + entity.getLookAngle().z * 10.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(15.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator instanceof FireDragonGrabEntity) {
                           Vec3 look = entity.getLookAngle();
                           Vec3 tangent = new Vec3(-look.z, 0.0, look.x);
                           Entity _ent = entityiterator;
                           _ent.teleportTo(x + entity.getLookAngle().x * 10.0 + 4.0 * tangent.x, y + 4.0, z + entity.getLookAngle().z * 10.0 + 4.0 * tangent.z);
                           if (_ent instanceof ServerPlayer _serverPlayer) {
                              _serverPlayer.connection
                                 .teleport(x + entity.getLookAngle().x * 10.0, y + 5.0, z + entity.getLookAngle().z * 10.0, _ent.getYRot(), _ent.getXRot());
                           }
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("atk11g") == 76.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 10.0, y + 5.0, z + entity.getLookAngle().z * 10.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(15.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator instanceof FireDragonGrabEntity) {
                           Vec3 look = entity.getLookAngle();
                           Vec3 tangent = new Vec3(-look.z, 0.0, look.x);
                           Entity _ent = entityiterator;
                           _ent.teleportTo(
                              x + entity.getLookAngle().x * 10.0 + -4.0 * tangent.x, y + 4.0, z + entity.getLookAngle().z * 10.0 + -4.0 * tangent.z
                           );
                           if (_ent instanceof ServerPlayer _serverPlayer) {
                              _serverPlayer.connection
                                 .teleport(x + entity.getLookAngle().x * 10.0, y + 6.0, z + entity.getLookAngle().z * 10.0, _ent.getYRot(), _ent.getXRot());
                           }
                        }

                        if (entityiterator.getPersistentData().getBoolean("draggrab")) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.SHORT)
                              .setImpact(3.0F)
                              .setArmorNegation(100.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 10.0F);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("atk11g") == 66.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 10.0, y + 5.0, z + entity.getLookAngle().z * 10.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(15.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator instanceof FireDragonGrabEntity) {
                           Vec3 look = entity.getLookAngle();
                           Vec3 tangent = new Vec3(-look.z, 0.0, look.x);
                           Entity _ent = entityiterator;
                           _ent.teleportTo(x + entity.getLookAngle().x * 10.0 + 4.0 * tangent.x, y + 4.0, z + entity.getLookAngle().z * 10.0 + 4.0 * tangent.z);
                           if (_ent instanceof ServerPlayer _serverPlayer) {
                              _serverPlayer.connection
                                 .teleport(x + entity.getLookAngle().x * 10.0, y + 4.0, z + entity.getLookAngle().z * 10.0, _ent.getYRot(), _ent.getXRot());
                           }
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("atk11g") == 54.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 10.0, y + 5.0, z + entity.getLookAngle().z * 10.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(15.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator instanceof FireDragonGrabEntity) {
                           Vec3 look = entity.getLookAngle();
                           Vec3 tangent = new Vec3(-look.z, 0.0, look.x);
                           Entity _ent = entityiterator;
                           _ent.teleportTo(
                              x + entity.getLookAngle().x * 10.0 + -4.0 * tangent.x, y + 4.0, z + entity.getLookAngle().z * 10.0 + -4.0 * tangent.z
                           );
                           if (_ent instanceof ServerPlayer _serverPlayer) {
                              _serverPlayer.connection
                                 .teleport(x + entity.getLookAngle().x * 10.0, y + 4.0, z + entity.getLookAngle().z * 10.0, _ent.getYRot(), _ent.getXRot());
                           }
                        }

                        if (entityiterator.getPersistentData().getBoolean("draggrab")) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.SHORT)
                              .setImpact(3.0F)
                              .setArmorNegation(100.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 10.0F);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("atk11g") == 47.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 10.0, y + 5.0, z + entity.getLookAngle().z * 10.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(15.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator instanceof FireDragonGrabEntity) {
                           Entity _ent = entityiterator;
                           _ent.teleportTo(x + entity.getLookAngle().x * 10.0, y + 2.0, z + entity.getLookAngle().z * 10.0);
                           if (_ent instanceof ServerPlayer _serverPlayer) {
                              _serverPlayer.connection
                                 .teleport(x + entity.getLookAngle().x * 10.0, y + 2.0, z + entity.getLookAngle().z * 10.0, _ent.getYRot(), _ent.getXRot());
                           }
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("atk11g") == 41.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 10.0, y + 5.0, z + entity.getLookAngle().z * 10.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(15.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator instanceof FireDragonGrabEntity && !entityiterator.level().isClientSide()) {
                           entityiterator.discard();
                        }

                        if (entityiterator.getPersistentData().getBoolean("draggrab")) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.KNOCKDOWN)
                              .setImpact(3.0F)
                              .setArmorNegation(100.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 10.0F);
                           entityiterator.setDeltaMovement(
                              new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 1.3, 0.7, Math.cos(Math.toRadians(entity.getYRot())) * 1.3)
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("atk11g") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putDouble("atk11g", 0.0);
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Attack11G", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     _center = new Vec3(x + entity.getLookAngle().x * 10.0, y + 5.0, z + entity.getLookAngle().z * 10.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(15.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator instanceof FireDragonGrabEntity && !entityiterator.level().isClientSide()) {
                           entityiterator.discard();
                        }

                        if (entityiterator.getPersistentData().getBoolean("draggrab")) {
                           entityiterator.getPersistentData().putBoolean("draggrab", false);
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
                              "/data merge entity @e[type=deepnight:dawn_fire_dragon,limit=1,sort=nearest] {NoAI:0}"
                           );
                     }

                     entity.getPersistentData().putBoolean("noai", false);
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putDouble("atk11g", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Attack11G", false);
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

                     if (entity.getPersistentData().getDouble("nowdirection") > 25.0) {
                        entity.getPersistentData().putDouble("nowdirection", entity.getPersistentData().getDouble("nowdirection") - 25.0);
                     } else if (entity.getPersistentData().getDouble("nowdirection") < 0.0) {
                        entity.getPersistentData().putDouble("nowdirection", entity.getPersistentData().getDouble("nowdirection") + 25.0);
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
                        x + entity.getLookAngle().x * -2.0 + Math.cos(0.25132741228718347 * entity.getPersistentData().getDouble("nowdirection")) * 8.0,
                        y,
                        z + entity.getLookAngle().z * -2.0 + Math.sin(0.25132741228718347 * entity.getPersistentData().getDouble("nowdirection")) * 8.0
                     );

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("firedragon")) {
                           if (entityiterator instanceof LivingEntity _livEnt ? _livEnt.isBlocking() : false) {
                              if ((entityiterator instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == Items.SHIELD) {
                                 ItemStack _ist = entityiterator instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY;
                                 if (_ist.hurt(30, net.minecraft.util.RandomSource.create(), null)) {
                                    _ist.shrink(1);
                                    _ist.setDamageValue(0);
                                 }

                                 if (entityiterator instanceof Player _player) {
                                    _player.getCooldowns()
                                       .addCooldown(
                                          (entityiterator instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem(), 200
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
                                 if (_ist.hurt(16, net.minecraft.util.RandomSource.create(), null)) {
                                    _ist.shrink(1);
                                    _ist.setDamageValue(0);
                                 }

                                 if (entityiterator instanceof Player _player) {
                                    _player.getCooldowns()
                                       .addCooldown(
                                          (entityiterator instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem(), 200
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
                                 .setStunType(StunType.LONG)
                                 .setImpact(3.0F)
                                 .setArmorNegation(20.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 20.0F);
                              entityiterator.setDeltaMovement(
                                 new Vec3(
                                    Math.sin(Math.toRadians(entity.getYRot() - 90.0F)) * 3.0, 0.8, Math.cos(Math.toRadians(entity.getYRot() + 90.0F)) * 3.0
                                 )
                              );
                           }
                        }
                     }

                     entity.getPersistentData().putDouble("i", entity.getPersistentData().getDouble("i") - 1.0);
                  }
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  if (entity.getPersistentData().getDouble("stun") == 0.0) {
                     entity.getPersistentData().putDouble("stun", 111.0);
                  } else {
                     entity.getPersistentData().putDouble("stun", entity.getPersistentData().getDouble("stun") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("stun") == 111.0) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_fire_dragon,limit=1,sort=nearest] {NoAI:1b}"
                           );
                     }

                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.POPPY));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.POPPY));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     entity.getPersistentData().putBoolean("noai", true);
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:poise"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x, y, z, dev.deepnight.SafeSounds.resolve("simple_mobs:poise"), SoundSource.NEUTRAL, 3.0F, 1.0F, false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("stun") == 71.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F,
                              false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("stun") == 1.0) {
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
                              "/data merge entity @e[type=deepnight:dawn_fire_dragon,limit=1,sort=nearest] {NoAI:0}"
                           );
                     }

                     entity.getPersistentData().putDouble("Poise", -100.0);
                     entity.getPersistentData().putDouble("stun", 0.0);
                     entity.getPersistentData().putBoolean("air", false);
                     entity.getPersistentData().putBoolean("noai", false);
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Stunned", false);
                  }
               }

               if (!entity.getPersistentData().getBoolean("Aggro")) {
                  if (entity.getPersistentData().getDouble("idletimer") == 0.0) {
                     entity.getPersistentData().putDouble("idletimer", 300.0);
                  } else {
                     entity.getPersistentData().putDouble("idletimer", entity.getPersistentData().getDouble("idletimer") - 1.0);
                  }

                  if (!entity.getPersistentData().getBoolean("Attacking") && entity.getPersistentData().getDouble("idletimer") == 1.0) {
                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new FireDragonAmbientEntity(SimpleMobsModEntities.FIRE_DRAGON_AMBIENT.get(), _level);
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
               } else if (entity.getPersistentData().getBoolean("Aggro") && entity.getPersistentData().getDouble("idletimer") > 0.0) {
                  entity.getPersistentData().putDouble("idletimer", 0.0);
               }
            }
         }
      }
   }
}
