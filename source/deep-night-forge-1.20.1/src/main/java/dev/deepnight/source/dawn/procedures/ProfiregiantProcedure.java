package dev.deepnight.source.dawn.procedures;


import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.AirEntity;
import dev.deepnight.source.dawn.entity.FireGiantEntity;
import dev.deepnight.source.dawn.entity.GrabbedEntity;
import dev.deepnight.source.dawn.entity.GroundbreakEffectBigEntity;
import dev.deepnight.source.dawn.entity.LavaBallEntity;
import dev.deepnight.source.dawn.entity.MultiPartLegEntity;
import dev.deepnight.source.dawn.entity.Multipartbody2Entity;
import dev.deepnight.source.dawn.entity.MultipartbodyEntity;
import dev.deepnight.source.dawn.entity.Projectile2Entity;
import dev.deepnight.source.dawn.entity.RumbleEntity;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.init.SimpleMobsModParticleTypes;
import dev.deepnight.source.dawn.network.SimpleMobsModVariables;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
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

public class ProfiregiantProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (entity.getPersistentData().getBoolean("lock")) {
            Entity _ent = entity;
            _ent.setYRot((float)entity.getPersistentData().getDouble("xz"));
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

         if (!world.isClientSide()) {
            if (entity.getPersistentData().getBoolean("hurtcd")) {
               if (entity.getPersistentData().getDouble("hurt_cd") != 0.0) {
                  entity.getPersistentData().putDouble("hurt_cd", entity.getPersistentData().getDouble("hurt_cd") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("hurt_cd", 15.0);
               }

               if (entity.getPersistentData().getDouble("hurt_cd") == 1.0) {
                  Entity _entity = entity;
                  if (!(_entity instanceof Player _player)) {
                     if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.FEET, new ItemStack(Blocks.AIR));
                     }
                  } else {
                     _player.getInventory().armor.set(0, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  }

                  entity.getPersistentData().putDouble("hurt_cd", 0.0);
                  entity.getPersistentData().putBoolean("hurtcd", false);
               }
            }

            if (!entity.getPersistentData().getBoolean("Attacking")) {
               if (world.getEntitiesOfClass(MultiPartLegEntity.class, AABB.ofSize(new Vec3(x, y, z), 10.0, 10.0, 10.0), e -> true).isEmpty()
                  && world instanceof ServerLevel _level) {
                  Entity entityToSpawn = new MultiPartLegEntity(SimpleMobsModEntities.MULTI_PART_LEG.get(), _level);
                  entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
                  if (entityToSpawn instanceof Mob _mobToSpawn) {
                     _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                  }

                  world.addFreshEntity(entityToSpawn);
               }

               if (world.getEntitiesOfClass(MultipartbodyEntity.class, AABB.ofSize(new Vec3(x, y, z), 10.0, 10.0, 10.0), e -> true).isEmpty()
                  && world instanceof ServerLevel _level) {
                  Entity entityToSpawn = new MultipartbodyEntity(SimpleMobsModEntities.MULTIPARTBODY.get(), _level);
                  entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
                  if (entityToSpawn instanceof Mob _mobToSpawn) {
                     _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                  }

                  world.addFreshEntity(entityToSpawn);
               }
            }

            if (!entity.getPersistentData().getBoolean("firstjoin")) {
               entity.getPersistentData().putBoolean("Attacking", true);
               Entity _entity = entity;
               if (!(_entity instanceof Player _player)) {
                  if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.PACKED_ICE));
                  }
               } else {
                  _player.getInventory().armor.set(3, new ItemStack(Blocks.PACKED_ICE));
                  _player.getInventory().setChanged();
               }

               entity.fallDistance = 0.0F;
               if (entity.onGround()) {
                  entity.getPersistentData().putBoolean("landing", true);
                  entity.getPersistentData().putBoolean("firstjoin", true);
               }
            }

            if (entity.getPersistentData().getBoolean("landing")) {
               if (entity.getPersistentData().getDouble("landingCD") != 0.0) {
                  entity.getPersistentData().putDouble("landingCD", entity.getPersistentData().getDouble("landingCD") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("landingCD", 61.0);
               }

               if (entity.getPersistentData().getDouble("landingCD") == 61.0) {
                  Entity _entity = entity;
                  if (!(_entity instanceof Player _player)) {
                     if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.ICE));
                     }
                  } else {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.ICE));
                     _player.getInventory().setChanged();
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new AirEntity(SimpleMobsModEntities.AIR.get(), _level);
                     entityToSpawn.moveTo(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0, 0.0F, 0.0F);
                     entityToSpawn.setYBodyRot(0.0F);
                     entityToSpawn.setYHeadRot(0.0F);
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new RumbleEntity(SimpleMobsModEntities.RUMBLE.get(), _level);
                     entityToSpawn.moveTo(x, y, z, 0.0F, 0.0F);
                     entityToSpawn.setYBodyRot(0.0F);
                     entityToSpawn.setYHeadRot(0.0F);
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  for (int index0 = 0; index0 < 3; index0++) {
                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new RumbleEntity(SimpleMobsModEntities.RUMBLE.get(), _level);
                        entityToSpawn.moveTo(x + Mth.nextInt(net.minecraft.util.RandomSource.create(), -10, 10), y, z + Mth.nextInt(net.minecraft.util.RandomSource.create(), 10, 10), 0.0F, 0.0F);
                        entityToSpawn.setYBodyRot(0.0F);
                        entityToSpawn.setYHeadRot(0.0F);
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  }
               } else if (entity.getPersistentData().getDouble("landingCD") != 60.0) {
                  if (entity.getPersistentData().getDouble("landingCD") == 59.0) {
                     if (world instanceof Level _level) {
                        if (_level.isClientSide()) {
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
                        } else {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
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
                              dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F,
                              false
                           );
                        } else {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        }
                     }

                     if (world instanceof Level _level) {
                        if (_level.isClientSide()) {
                           _level.playLocalSound(
                              x, y, z, dev.deepnight.SafeSounds.resolve("simple_mobs:jump"), SoundSource.NEUTRAL, 3.0F, 0.5F, false
                           );
                        } else {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:jump"),
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
                              dev.deepnight.SafeSounds.resolve("simple_mobs:sentinielground"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F,
                              false
                           );
                        } else {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:sentinielground"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("landingCD") <= 58.0 && entity.getPersistentData().getDouble("landingCD") >= 53.0) {
                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 2, 10.0, 1.0, 10.0, 0.4);
                     }

                     Vec3 _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(20.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("firegiant") && entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 40, 2, false, false));
                        }
                     }

                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(10.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("firegiant")) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.LONG)
                              .setImpact(14.0F)
                              .setArmorNegation(30.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 38.0F);
                           entityiterator.setDeltaMovement(
                              new Vec3((entityiterator.getX() - entity.getX()) * 0.2, 0.6, (entityiterator.getZ() - entity.getZ()) * 0.2)
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("landingCD") != 52.0) {
                     if (entity.getPersistentData().getDouble("landingCD") != 15.0) {
                        if (entity.getPersistentData().getDouble("landingCD") == 1.0) {
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
                           entity.getPersistentData().putBoolean("landing", false);
                        }
                     } else {
                        entity.getPersistentData().putBoolean("noai", false);
                     }
                  } else {
                     entity.getPersistentData().putBoolean("noai", true);
                  }
               } else {
                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 50, 5.0, 1.0, 5.0, 0.4);
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.CLOUD, x, y, z, 50, 5.0, 1.0, 5.0, 0.4);
                  }
               }
            }

            if (entity.onGround()
               && !entity.getPersistentData().getBoolean("Attacking")
               && Math.sqrt(entity.getDeltaMovement().x() * entity.getDeltaMovement().x() + entity.getDeltaMovement().z() * entity.getDeltaMovement().z())
                  > 0.02) {
               if (entity.getPersistentData().getDouble("Walk") != 0.0) {
                  entity.getPersistentData().putDouble("Walk", entity.getPersistentData().getDouble("Walk") - 1.0);
               } else {
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

                  Vec3 _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if ((entityiterator instanceof Player || entityiterator instanceof ServerPlayer) && entityiterator instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 20, 1, false, false));
                     }
                  }

                  entity.getPersistentData().putDouble("Walk", 40.0);
               }
            }

            if (entity.getPersistentData().getBoolean("bb")) {
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
                                       x + SimpleMobsModVariables.MapVariables.get(world).i + entity.getLookAngle().x * 1.5,
                                       y + SimpleMobsModVariables.MapVariables.get(world).j,
                                       z + SimpleMobsModVariables.MapVariables.get(world).x + entity.getLookAngle().z * 1.5
                                    )
                                 )
                                 .getDestroySpeed(
                                    world,
                                    BlockPos.containing(
                                       x + SimpleMobsModVariables.MapVariables.get(world).i + entity.getLookAngle().x * 1.5,
                                       y + SimpleMobsModVariables.MapVariables.get(world).j,
                                       z + SimpleMobsModVariables.MapVariables.get(world).x + entity.getLookAngle().z * 1.5
                                    )
                                 )
                              <= 5.0F) {
                              world.destroyBlock(
                                 BlockPos.containing(
                                    x + SimpleMobsModVariables.MapVariables.get(world).i + entity.getLookAngle().x * 1.5,
                                    y + SimpleMobsModVariables.MapVariables.get(world).j,
                                    z + SimpleMobsModVariables.MapVariables.get(world).x + entity.getLookAngle().z * 1.5
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
            }

            entity.getPersistentData().putBoolean("Aggro", false);
            entity.getPersistentData().putBoolean("Far", false);
            entity.getPersistentData().putBoolean("Mid", false);
            entity.getPersistentData().putBoolean("Close", false);
            entity.getPersistentData().putBoolean("Around", false);
            if ((!(entity instanceof LivingEntity _livEnt) ? -1.0F : _livEnt.getHealth())
                  < (!(entity instanceof LivingEntity _livEnt) ? -1.0F : _livEnt.getMaxHealth()) * 0.5
               && !entity.getPersistentData().getBoolean("Done")) {
               entity.getPersistentData().putBoolean("Drop", true);
               entity.getPersistentData().putBoolean("Done", true);
            }

            entity.getPersistentData().putBoolean("warn", false);
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(45.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator instanceof ServerPlayer || entityiterator instanceof Player) {
                  entity.getPersistentData().putBoolean("warn", true);
               }
            }

            _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(39.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator instanceof ServerPlayer || entityiterator instanceof Player) {
                  entity.getPersistentData().putBoolean("warn", false);
               }
            }

            if (entity.getPersistentData().getBoolean("warn")) {
               _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(45.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if ((entityiterator instanceof ServerPlayer || entityiterator instanceof Player)
                     && entityiterator instanceof Player _player
                     && !_player.level().isClientSide()) {
                     _player.displayClientMessage(Component.translatable("message.fire_giant.1"), true);
                  }
               }
            }

            _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(39.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (!(entity instanceof Mob _mobEnt) ? null : _mobEnt.getTarget())) {
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
                        Math.toDegrees(
                           Math.atan(
                              Math.hypot(Math.abs(entity.getX() - entityiterator.getX()), Math.abs(entity.getZ() - entityiterator.getZ()))
                                 / Math.abs(entity.getY() + 4.0 - entityiterator.getY())
                           )
                        )
                     );
               }
            }

            _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(32.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (!(entity instanceof Mob _mobEnt) ? null : _mobEnt.getTarget())) {
                  entity.getPersistentData().putBoolean("Far", true);
                  entity.getPersistentData().putBoolean("Mid", false);
                  entity.getPersistentData().putBoolean("Close", false);
                  entity.getPersistentData().putBoolean("Around", false);
               }
            }

            _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(20.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (!(entity instanceof Mob _mobEnt) ? null : _mobEnt.getTarget())) {
                  entity.getPersistentData().putBoolean("Far", false);
               }
            }

            _center = new Vec3(x + entity.getLookAngle().x * 18.0, y, z + entity.getLookAngle().z * 18.0);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (!(entity instanceof Mob _mobEnt) ? null : _mobEnt.getTarget())) {
                  entity.getPersistentData().putBoolean("Far", false);
                  entity.getPersistentData().putBoolean("Mid", true);
                  entity.getPersistentData().putBoolean("Close", false);
                  entity.getPersistentData().putBoolean("Around", false);
               }
            }

            _center = new Vec3(x + entity.getLookAngle().x * 3.0, y, z + entity.getLookAngle().z * 3.0);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (!(entity instanceof Mob _mobEnt) ? null : _mobEnt.getTarget())) {
                  entity.getPersistentData().putBoolean("Far", false);
                  entity.getPersistentData().putBoolean("Mid", false);
                  entity.getPersistentData().putBoolean("Close", true);
                  entity.getPersistentData().putBoolean("Around", false);
               }
            }

            _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.5), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (!(entity instanceof Mob _mobEnt) ? null : _mobEnt.getTarget())) {
                  entity.getPersistentData().putBoolean("Far", false);
                  entity.getPersistentData().putBoolean("Mid", false);
                  entity.getPersistentData().putBoolean("Close", false);
                  entity.getPersistentData().putBoolean("Around", true);
               }
            }

            if (entity.getPersistentData().getDouble("Atk1CD") == 0.0 && entity.getPersistentData().getBoolean("Attack1")) {
               entity.getPersistentData().putDouble("Atk1CD", 200.0);
            } else if (entity.getPersistentData().getDouble("Atk1CD") > 0.0) {
               entity.getPersistentData().putDouble("Atk1CD", entity.getPersistentData().getDouble("Atk1CD") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Atk2CD") == 0.0 && entity.getPersistentData().getBoolean("Attack2")) {
               entity.getPersistentData().putDouble("Atk2CD", 120.0);
            } else if (entity.getPersistentData().getDouble("Atk2CD") > 0.0) {
               entity.getPersistentData().putDouble("Atk2CD", entity.getPersistentData().getDouble("Atk2CD") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Atk3CD") == 0.0 && entity.getPersistentData().getBoolean("Attack3")) {
               entity.getPersistentData().putDouble("Atk3CD", 240.0);
            } else if (entity.getPersistentData().getDouble("Atk3CD") > 0.0) {
               entity.getPersistentData().putDouble("Atk3CD", entity.getPersistentData().getDouble("Atk3CD") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Atk4CD") == 0.0 && entity.getPersistentData().getBoolean("Attack4")) {
               entity.getPersistentData().putDouble("Atk4CD", 300.0);
            } else if (entity.getPersistentData().getDouble("Atk5CD") > 0.0) {
               entity.getPersistentData().putDouble("Atk4CD", entity.getPersistentData().getDouble("Atk4CD") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Atk5CD") == 0.0 && entity.getPersistentData().getBoolean("Attack5")) {
               entity.getPersistentData().putDouble("Atk5CD", 200.0);
            } else if (entity.getPersistentData().getDouble("Atk5CD") > 0.0) {
               entity.getPersistentData().putDouble("Atk5CD", entity.getPersistentData().getDouble("Atk5CD") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Atk6CD") == 0.0 && entity.getPersistentData().getBoolean("Attack6")) {
               entity.getPersistentData().putDouble("Atk6CD", 400.0);
            } else if (entity.getPersistentData().getDouble("Atk6CD") > 0.0) {
               entity.getPersistentData().putDouble("Atk6CD", entity.getPersistentData().getDouble("Atk6CD") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Atk7CD") == 0.0 && entity.getPersistentData().getBoolean("Attack7")) {
               entity.getPersistentData().putDouble("Atk7CD", 380.0);
            } else if (entity.getPersistentData().getDouble("Atk7CD") > 0.0) {
               entity.getPersistentData().putDouble("Atk7CD", entity.getPersistentData().getDouble("Atk7CD") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Atk8CD") == 0.0 && entity.getPersistentData().getBoolean("Attack8")) {
               entity.getPersistentData().putDouble("Atk8CD", 360.0);
            } else if (entity.getPersistentData().getDouble("Atk8CD") > 0.0) {
               entity.getPersistentData().putDouble("Atk8CD", entity.getPersistentData().getDouble("Atk8CD") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Atk9CD") == 0.0 && entity.getPersistentData().getBoolean("Attack9")) {
               entity.getPersistentData().putDouble("Atk9CD", 350.0);
            } else if (entity.getPersistentData().getDouble("Atk9CD") > 0.0) {
               entity.getPersistentData().putDouble("Atk9CD", entity.getPersistentData().getDouble("Atk9CD") - 1.0);
            }

            if ((!(entity instanceof LivingEntity _livEnt) ? -1.0F : _livEnt.getHealth())
                  < (!(entity instanceof LivingEntity _livEnt) ? -1.0F : _livEnt.getMaxHealth()) * 0.43
               && !entity.getPersistentData().getBoolean("Attacking")) {
               entity.getPersistentData().putBoolean("Unchained", true);
               entity.getPersistentData().putBoolean("Attacking", true);
            }

            if (!entity.getPersistentData().getBoolean("Around")) {
               if (!entity.getPersistentData().getBoolean("Close")) {
                  if (entity.getPersistentData().getBoolean("Mid")
                     && !entity.getPersistentData().getBoolean("Attacking")
                     && entity.getPersistentData().getDouble("Atk5CD") == 0.0) {
                     entity.getPersistentData().putBoolean("Attack5", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (entity.getPersistentData().getBoolean("Far")
                     && !entity.getPersistentData().getBoolean("Attacking")
                     && entity.getPersistentData().getDouble("Atk7CD") == 0.0) {
                     entity.getPersistentData().putBoolean("Attack7", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  }
               } else if (Math.random() <= 0.25 && !entity.getPersistentData().getBoolean("Attacking") && entity.getPersistentData().getDouble("Atk2CD") == 0.0
                  )
                {
                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putBoolean("Attack2", true);
               } else if (Math.random() >= 0.26
                  && Math.random() <= 0.5
                  && !entity.getPersistentData().getBoolean("Attacking")
                  && entity.getPersistentData().getDouble("Atk3CD") == 0.0) {
                  entity.getPersistentData().putBoolean("Attack3", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (entity.getPersistentData().getDouble("Atk9CD") == 0.0
                  && Math.random() >= 0.51
                  && Math.random() <= 0.75
                  && (!(entity instanceof LivingEntity _livEnt) ? -1.0F : _livEnt.getHealth())
                     < (!(entity instanceof LivingEntity _livEnt) ? -1.0F : _livEnt.getMaxHealth()) * 0.7
                  && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack9", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (!entity.getPersistentData().getBoolean("Attacking") && entity.getPersistentData().getDouble("Atk6CD") == 0.0) {
                  entity.getPersistentData().putBoolean("Attack6", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               }
            } else if (Math.random() <= 0.3 && !entity.getPersistentData().getBoolean("Attacking") && entity.getPersistentData().getDouble("Atk1CD") == 0.0) {
               entity.getPersistentData().putBoolean("Attacking", true);
               entity.getPersistentData().putBoolean("Attack1", true);
            } else if (Math.random() > 0.3
               && Math.random() <= 0.6
               && !entity.getPersistentData().getBoolean("Attacking")
               && entity.getPersistentData().getDouble("Atk4CD") == 0.0) {
               entity.getPersistentData().putBoolean("Attacking", true);
               entity.getPersistentData().putBoolean("Attack4", true);
            } else if (Math.random() > 0.6
               && Math.random() <= 0.9
               && !entity.getPersistentData().getBoolean("Attacking")
               && entity.getPersistentData().getDouble("Atk8CD") == 0.0) {
               entity.getPersistentData().putBoolean("Attack8", true);
               entity.getPersistentData().putBoolean("Attacking", true);
            } else if (!entity.getPersistentData().getBoolean("Attacking")
               && entity.getPersistentData().getDouble("Atk9CD") == 0.0
               && (!(entity instanceof LivingEntity _livEnt) ? -1.0F : _livEnt.getHealth())
                  < (!(entity instanceof LivingEntity _livEnt) ? -1.0F : _livEnt.getMaxHealth()) * 0.7) {
               entity.getPersistentData().putBoolean("Attack9", true);
               entity.getPersistentData().putBoolean("Attacking", true);
            }

            if (entity.getPersistentData().getBoolean("Attack1")) {
               if (entity.getPersistentData().getDouble("atk1") != 0.0) {
                  entity.getPersistentData().putDouble("atk1", entity.getPersistentData().getDouble("atk1") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("atk1", 111.0);
               }

               if (entity.getPersistentData().getDouble("atk1") == 111.0) {
                  Entity _entity = entity;
                  if (!(_entity instanceof Player _player)) {
                     if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                     }
                  } else {
                     _player.getInventory().armor.set(3, new ItemStack(Items.LEATHER_HELMET));
                     _player.getInventory().setChanged();
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 111, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("atk1") == 110.0) {
                  if (world instanceof Level _level) {
                     if (_level.isClientSide()) {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.ambient2"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.8F,
                           false
                        );
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.ambient2"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.8F
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("atk1") != 52.0) {
                  if (entity.getPersistentData().getDouble("atk1") < 52.0 && entity.getPersistentData().getDouble("atk1") > 47.0) {
                     _center = new Vec3(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(10.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entity.getPersistentData().getBoolean("firegiant") && entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 30, 1));
                        }
                     }

                     _center = new Vec3(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(8.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("firegiant")) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.LONG)
                              .setImpact(5.0F)
                              .setArmorNegation(30.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 34.0F);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("atk1") != 31.0) {
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

                        entity.getPersistentData().putDouble("atk1", 0.0);
                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putBoolean("Attack1", false);
                     }
                  } else {
                     _center = new Vec3(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator == (!(entity instanceof Mob _mobEnt) ? null : _mobEnt.getTarget())) {
                           entity.getPersistentData().putBoolean("Attack1b", true);
                           entity.getPersistentData().putDouble("atk1", 0.0);
                           entity.getPersistentData().putBoolean("Attack1", false);
                        }
                     }
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
                           1.0F,
                           false
                        );
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
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
                           dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F,
                           false
                        );
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new RumbleEntity(SimpleMobsModEntities.RUMBLE.get(), _level);
                     entityToSpawn.moveTo(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z, entity.getYRot(), entity.getXRot());
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  for (int index4 = 0; index4 < 2; index4++) {
                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new RumbleEntity(SimpleMobsModEntities.RUMBLE.get(), _level);
                        entityToSpawn.moveTo(
                           x + entity.getLookAngle().x + Mth.nextInt(net.minecraft.util.RandomSource.create(), -4, 4),
                           y,
                           z + entity.getLookAngle().z + Mth.nextInt(net.minecraft.util.RandomSource.create(), -4, 4),
                           entity.getYRot(),
                           entity.getXRot()
                        );
                        entityToSpawn.setYBodyRot(entity.getYRot());
                        entityToSpawn.setYHeadRot(entity.getYRot());
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 30, 3.0, 3.0, 3.0, 1.0);
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack1b")) {
               if (entity.getPersistentData().getDouble("atk1b") != 0.0) {
                  entity.getPersistentData().putDouble("atk1b", entity.getPersistentData().getDouble("atk1b") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("atk1b", 91.0);
               }

               if (entity.getPersistentData().getDouble("atk1b") == 91.0) {
                  Entity _entity = entity;
                  if (!(_entity instanceof Player _player)) {
                     if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLD_NUGGET));
                     }
                  } else {
                     _player.getInventory().armor.set(3, new ItemStack(Items.GOLD_NUGGET));
                     _player.getInventory().setChanged();
                  }
               } else if (entity.getPersistentData().getDouble("atk1b") == 90.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 90, 6, false, false));
                  }

                  if (world instanceof Level _level) {
                     if (_level.isClientSide()) {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.ambient"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           0.5F,
                           false
                        );
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.ambient"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           0.5F
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("atk1b") != 56.0) {
                  if (entity.getPersistentData().getDouble("atk1b") < 56.0 && entity.getPersistentData().getDouble("atk1b") > 50.0) {
                     _center = new Vec3(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(10.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("firegiant") && entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 30, 1));
                        }
                     }

                     _center = new Vec3(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(8.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("firegiant")) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.LONG)
                              .setImpact(5.0F)
                              .setArmorNegation(35.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 34.0F);
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
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Attack1b", false);
                  }
               } else {
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new RumbleEntity(SimpleMobsModEntities.RUMBLE.get(), _level);
                     entityToSpawn.moveTo(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z, entity.getYRot(), entity.getXRot());
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  for (int index5 = 0; index5 < 2; index5++) {
                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new RumbleEntity(SimpleMobsModEntities.RUMBLE.get(), _level);
                        entityToSpawn.moveTo(
                           x + entity.getLookAngle().x + Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, 5),
                           y,
                           z + entity.getLookAngle().z + Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, 5),
                           entity.getYRot(),
                           entity.getXRot()
                        );
                        entityToSpawn.setYBodyRot(entity.getYRot());
                        entityToSpawn.setYHeadRot(entity.getYRot());
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  }

                  if (world instanceof Level _level) {
                     if (_level.isClientSide()) {
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
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
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
                           dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F,
                           false
                        );
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 30, 3.0, 3.0, 3.0, 1.0);
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack2")) {
               if (!(entity.getPersistentData().getDouble("Atk2") <= 0.0)) {
                  entity.getPersistentData().putDouble("Atk2", entity.getPersistentData().getDouble("Atk2") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("Atk2", 76.0);
               }

               if (entity.getPersistentData().getDouble("Atk2") != 76.0) {
                  if (entity.getPersistentData().getDouble("Atk2") != 64.0) {
                     if (entity.getPersistentData().getDouble("Atk2") != 56.0) {
                        if (entity.getPersistentData().getDouble("Atk2") != 55.0) {
                           if (entity.getPersistentData().getDouble("Atk2") < 45.0 && entity.getPersistentData().getDouble("Atk2") >= 40.0) {
                              _center = entity.getLookAngle();
                              Vec3 tangent = new Vec3(-_center.z, 0.0, _center.x);
                              Vec3 _centerx = new Vec3(x + entity.getLookAngle().x + 0.6 * tangent.x, y, z + entity.getLookAngle().z + 0.6 * tangent.z);

                              for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_centerx, _centerx).inflate(5.0), e -> true)
                                 .stream()
                                 .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                                 .collect(Collectors.toList())) {
                                 if (!entityiterator.getPersistentData().getBoolean("firegiant")) {
                                    SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                       .setInitialPosition(entity.position())
                                       .setStunType(StunType.LONG)
                                       .setImpact(5.0F)
                                       .setArmorNegation(30.0F);
                                    entityiterator.hurt((DamageSource)dmgSrc, 24.0F);
                                    entityiterator.setDeltaMovement(
                                       new Vec3((entityiterator.getX() - entity.getX()) / 3.0, 0.7, (entityiterator.getZ() - entity.getZ()) / 3.0)
                                    );
                                 }
                              }
                           } else if (entity.getPersistentData().getDouble("Atk2") == 1.0) {
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
                                       "/data merge entity @e[type=deepnight:dawn_fire_giant,limit=1,sort=nearest] {NoAI:0}"
                                    );
                              }

                              entity.getPersistentData().putBoolean("Attacking", false);
                              entity.getPersistentData().putBoolean("lock", false);
                              entity.getPersistentData().putDouble("Atk2", 0.0);
                              entity.getPersistentData().putBoolean("Attack2", false);
                           }
                        } else {
                           entity.getPersistentData().putBoolean("lock", true);
                           if (world instanceof ServerLevel _level) {
                              _level.getServer()
                                 .getCommands()
                                 .performPrefixedCommand(
                                    new CommandSourceStack(
                                          CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                       )
                                       .withSuppressedOutput(),
                                    "/data merge entity @e[type=deepnight:dawn_fire_giant,limit=1,sort=nearest] {NoAI:1b}"
                                 );
                           }
                        }
                     } else {
                        entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                        entity.getPersistentData().putDouble("y", entity.getXRot());
                     }
                  } else if (world instanceof Level _level) {
                     if (_level.isClientSide()) {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.sweep"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F,
                           false
                        );
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.sweep"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     }
                  }
               } else {
                  entity.getPersistentData().putBoolean("Attacking", true);
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
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 76, 10, false, false));
                  }

                  if (world instanceof Level _level) {
                     if (_level.isClientSide()) {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.ambient2"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.5F,
                           false
                        );
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.ambient2"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.5F
                        );
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack3")) {
               if (!(entity.getPersistentData().getDouble("Atk3") <= 0.0)) {
                  entity.getPersistentData().putDouble("Atk3", entity.getPersistentData().getDouble("Atk3") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("Atk3", 121.0);
               }

               if (entity.getPersistentData().getDouble("Atk3") != 121.0) {
                  if (entity.getPersistentData().getDouble("Atk3") != 120.0) {
                     if (entity.getPersistentData().getDouble("Atk3") != 78.0) {
                        if (entity.getPersistentData().getDouble("Atk3") != 77.0) {
                           if (entity.getPersistentData().getDouble("Atk3") != 67.0) {
                              if (entity.getPersistentData().getDouble("Atk3") != 21.0) {
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

                                    entity.getPersistentData().putBoolean("noai", false);
                                    entity.getPersistentData().putBoolean("Attacking", false);
                                    entity.getPersistentData().putBoolean("focus", false);
                                    entity.getPersistentData().putDouble("Atk3", 0.0);
                                    entity.getPersistentData().putBoolean("Attack3", false);
                                 }
                              } else {
                                 entity.getPersistentData().putBoolean("noai", false);
                              }
                           } else {
                              if (entity instanceof LivingEntity _entity) {
                                 _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 67, 10, false, false));
                              }

                              if (world instanceof Level _level) {
                                 if (_level.isClientSide()) {
                                    _level.playLocalSound(
                                       x + entity.getLookAngle().x * 2.0,
                                       y,
                                       z + entity.getLookAngle().z * 2.0,
                                       dev.deepnight.SafeSounds.resolve("simple_mobs:cave_golem.hurt"),
                                       SoundSource.NEUTRAL,
                                       3.0F,
                                       1.0F,
                                       false
                                    );
                                 } else {
                                    _level.playSound(
                                       null,
                                       BlockPos.containing(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0),
                                       dev.deepnight.SafeSounds.resolve("simple_mobs:cave_golem.hurt"),
                                       SoundSource.NEUTRAL,
                                       3.0F,
                                       1.0F
                                    );
                                 }
                              }

                              entity.getPersistentData().putBoolean("sweep", true);
                              entity.getPersistentData().putBoolean("noai", true);
                           }
                        } else if (world instanceof Level _level) {
                           if (_level.isClientSide()) {
                              _level.playLocalSound(
                                 x + entity.getLookAngle().x * 2.0,
                                 y,
                                 z + entity.getLookAngle().z * 2.0,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:giant_skele.sweep"),
                                 SoundSource.NEUTRAL,
                                 3.0F,
                                 1.0F,
                                 false
                              );
                           } else {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:giant_skele.sweep"),
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

                        _center = new Vec3(x, y, z);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5.0), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (!(entityiterator instanceof FireGiantEntity) && entityiterator instanceof LivingEntity _entity) {
                              _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 20, 1, false, false));
                           }
                        }
                     }
                  } else {
                     entity.getPersistentData().putBoolean("focus", true);
                     entity.getPersistentData().putDouble("i", 12.0);
                  }
               } else {
                  entity.getPersistentData().putBoolean("Attacking", true);
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
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 10, false, false));
                  }
               }

               if (entity.getPersistentData().getDouble("Atk3") <= 90.0 && entity.getPersistentData().getDouble("Atk3") >= 71.0) {
                  entity.setDeltaMovement(
                     new Vec3(Math.sin(Math.toRadians(entity.getYRot())) / 3.0, 0.0, Math.cos(Math.toRadians(entity.getYRot() + 180.0F)) / 3.0)
                  );
               }
            }

            if (entity.getPersistentData().getBoolean("sweep")) {
               if (!(entity.getPersistentData().getDouble("i") > 0.0)) {
                  entity.getPersistentData().putBoolean("sweep", false);
               } else {
                  entity.getPersistentData().putDouble("direction", Math.abs(entity.getYRot()) / 360.0F * 26.0F);
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
                     Entity entityToSpawn = new RumbleEntity(SimpleMobsModEntities.RUMBLE.get(), _level);
                     entityToSpawn.moveTo(
                        x + Math.cos(0.25132741228718347 * entity.getPersistentData().getDouble("nowdirection")) * 8.0,
                        y,
                        z + Math.sin(0.25132741228718347 * entity.getPersistentData().getDouble("nowdirection")) * 8.0,
                        (float)Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.0, 360.0),
                        0.0F
                     );
                     entityToSpawn.setYBodyRot((float)Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.0, 360.0));
                     entityToSpawn.setYHeadRot((float)Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.0, 360.0));
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  _center = new Vec3(
                     x + Math.cos(0.25132741228718347 * entity.getPersistentData().getDouble("nowdirection")) * 8.0,
                     y,
                     z + Math.sin(0.25132741228718347 * entity.getPersistentData().getDouble("nowdirection")) * 8.0
                  );

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!entityiterator.getPersistentData().getBoolean("firegiant")) {
                        if (!(entityiterator instanceof LivingEntity _livEnt) ? true : !_livEnt.isBlocking()) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.KNOCKDOWN)
                              .setImpact(14.0F)
                              .setArmorNegation(30.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 36.0F);
                           entityiterator.setDeltaMovement(
                              new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 90.0F)) * 3.0, 0.7, Math.cos(Math.toRadians(entity.getYRot() + 240.0F)) * 3.0)
                           );
                        } else if ((!(entityiterator instanceof LivingEntity _livEnt) ? ItemStack.EMPTY : _livEnt.getMainHandItem()).getItem() != Items.SHIELD) {
                           ItemStack _ist = !(entityiterator instanceof LivingEntity _livEnt) ? ItemStack.EMPTY : _livEnt.getOffhandItem();
                           if (_ist.hurt(16, net.minecraft.util.RandomSource.create(), null)) {
                              _ist.shrink(1);
                              _ist.setDamageValue(0);
                           }

                           if (entityiterator instanceof Player _player) {
                              _player.getCooldowns()
                                 .addCooldown((!(entityiterator instanceof LivingEntity _livEnt) ? ItemStack.EMPTY : _livEnt.getOffhandItem()).getItem(), 100);
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
                                 .addCooldown((!(entityiterator instanceof LivingEntity _livEnt) ? ItemStack.EMPTY : _livEnt.getMainHandItem()).getItem(), 100);
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

            if (!entity.getPersistentData().getBoolean("Aggro") && !entity.getPersistentData().getBoolean("Attacking")) {
               if (entity.getPersistentData().getDouble("idletimer") != 0.0) {
                  entity.getPersistentData().putDouble("idletimer", entity.getPersistentData().getDouble("idletimer") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("idletimer", 200.0);
               }

               if (!entity.getPersistentData().getBoolean("Attacking") && entity.getPersistentData().getDouble("idletimer") == 1.0) {
                  if (!entity.level().isClientSide()) {
                     entity.discard();
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.CLOUD, x, y, z, 300, 6.0, 16.0, 6.0, 1.0);
                  }
               }
            } else if (entity.getPersistentData().getBoolean("Aggro") && entity.getPersistentData().getDouble("idletimer") > 0.0) {
               entity.getPersistentData().putDouble("idletimer", 0.0);
            }

            if (entity.getPersistentData().getBoolean("Attack4")) {
               if (!(entity.getPersistentData().getDouble("Atk4") <= 0.0)) {
                  entity.getPersistentData().putDouble("Atk4", entity.getPersistentData().getDouble("Atk4") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("Atk4", 92.0);
               }

               if (entity.getPersistentData().getDouble("Atk4") != 92.0) {
                  if (entity.getPersistentData().getDouble("Atk4") != 81.0) {
                     if (entity.getPersistentData().getDouble("Atk4") != 75.0) {
                        if (entity.getPersistentData().getDouble("Atk4") <= 70.0 && entity.getPersistentData().getDouble("Atk4") >= 36.0) {
                           _center = new Vec3(x, y, z);

                           for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(8.0), e -> true)
                              .stream()
                              .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                              .collect(Collectors.toList())) {
                              if (!entityiterator.getPersistentData().getBoolean("firegiant") && entityiterator instanceof LivingEntity _entity) {
                                 _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 40, 1));
                              }
                           }

                           _center = new Vec3(x, y, z);

                           for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.5), e -> true)
                              .stream()
                              .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                              .collect(Collectors.toList())) {
                              if (!entityiterator.getPersistentData().getBoolean("firegiant")) {
                                 SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                    .setInitialPosition(entity.position())
                                    .setStunType(StunType.KNOCKDOWN)
                                    .setImpact(12.0F)
                                    .setArmorNegation(30.0F);
                                 entityiterator.hurt((DamageSource)dmgSrc, 36.0F);
                                 entityiterator.setDeltaMovement(
                                    new Vec3(
                                       Math.sin(Math.toRadians(entity.getYRot() + 90.0F)) * 3.0, 0.7, Math.cos(Math.toRadians(entity.getYRot() + 270.0F)) * 3.0
                                    )
                                 );
                              }
                           }
                        } else if (entity.getPersistentData().getDouble("Atk4") != 21.0) {
                           if (entity.getPersistentData().getDouble("Atk4") != 2.0) {
                              if (entity.getPersistentData().getDouble("Atk4") == 1.0) {
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
                                 entity.getPersistentData().putDouble("Atk4", 0.0);
                                 entity.getPersistentData().putBoolean("Attack4", false);
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
                           }
                        } else {
                           entity.getPersistentData().putBoolean("lock", false);
                           entity.getPersistentData().putBoolean("noai", false);
                           entity.getPersistentData().putBoolean("bb", false);
                        }
                     } else if (world instanceof Level _level) {
                        if (_level.isClientSide()) {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant_roll"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F,
                              false
                           );
                        } else {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant_roll"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        }
                     }
                  } else {
                     entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                     entity.getPersistentData().putDouble("y", entity.getXRot());
                     entity.getPersistentData().putBoolean("lock", true);
                     entity.getPersistentData().putBoolean("bb", true);
                     entity.getPersistentData().putBoolean("noai", true);
                  }
               } else {
                  entity.getPersistentData().putBoolean("Attacking", true);
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
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 1, false, false));
                  }
               }

               if (entity.getPersistentData().getDouble("Atk4") <= 77.0 && entity.getPersistentData().getDouble("Atk4") >= 36.0) {
                  entity.setDeltaMovement(
                     new Vec3(
                        Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 0.35,
                        entity.getDeltaMovement().y(),
                        Math.cos(Math.toRadians(entity.getYRot())) * 0.35
                     )
                  );
               }

               if ((entity.getPersistentData().getDouble("Atk4") == 44.0 || entity.getPersistentData().getDouble("Atk4") == 27.0)
                  && world instanceof Level _level) {
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
            }

            if (entity.getPersistentData().getBoolean("Attack5")) {
               if (!(entity.getPersistentData().getDouble("Atk5") <= 0.0)) {
                  entity.getPersistentData().putDouble("Atk5", entity.getPersistentData().getDouble("Atk5") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("Atk5", 128.0);
               }

               if (entity.getPersistentData().getDouble("Atk5") != 128.0) {
                  if (entity.getPersistentData().getDouble("Atk5") == 99.0) {
                     entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                  } else if (entity.getPersistentData().getDouble("Atk5") != 98.0) {
                     if (entity.getPersistentData().getDouble("Atk5") == 89.0) {
                        if (world instanceof Level _level) {
                           if (_level.isClientSide()) {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("entity.dragon_fireball.explode"),
                                 SoundSource.NEUTRAL,
                                 3.0F,
                                 0.0F,
                                 false
                              );
                           } else {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("entity.dragon_fireball.explode"),
                                 SoundSource.NEUTRAL,
                                 3.0F,
                                 0.0F
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
                                 1.0F,
                                 false
                              );
                           } else {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                                 SoundSource.NEUTRAL,
                                 3.0F,
                                 1.0F
                              );
                           }
                        }

                        if (world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new GroundbreakEffectBigEntity(SimpleMobsModEntities.GROUNDBREAK_EFFECT_BIG.get(), _level);
                           entityToSpawn.moveTo(
                              x + entity.getLookAngle().x * 18.0, y, z + entity.getLookAngle().z * 18.0, world.getRandom().nextFloat() * 360.0F, 0.0F
                           );
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }

                        if (world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new RumbleEntity(SimpleMobsModEntities.RUMBLE.get(), _level);
                           entityToSpawn.moveTo(x + entity.getLookAngle().x * 18.0, y, z + entity.getLookAngle().z * 18.0, entity.getYRot(), 0.0F);
                           entityToSpawn.setYBodyRot(entity.getYRot());
                           entityToSpawn.setYHeadRot(entity.getYRot());
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }

                        entity.getPersistentData().putBoolean("noai", true);
                     } else if (entity.getPersistentData().getDouble("Atk5") <= 88.0 && entity.getPersistentData().getDouble("Atk5") >= 82.0) {
                        _center = new Vec3(x + entity.getLookAngle().x * 18.0, y, z + entity.getLookAngle().z * 18.0);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.5), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (!entityiterator.getPersistentData().getBoolean("firegiant") && entityiterator instanceof LivingEntity _entity) {
                              _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 40, 1));
                           }
                        }

                        _center = new Vec3(x + entity.getLookAngle().x * 18.0, y, z + entity.getLookAngle().z * 18.0);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5.0), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (!entityiterator.getPersistentData().getBoolean("firegiant")) {
                              SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                 .setInitialPosition(entity.position())
                                 .setStunType(StunType.LONG)
                                 .setImpact(14.0F)
                                 .setArmorNegation(30.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 38.0F);
                           }
                        }
                     } else if (entity.getPersistentData().getDouble("Atk5") != 31.0) {
                        if (entity.getPersistentData().getDouble("Atk5") == 8.0) {
                           entity.getPersistentData().putBoolean("lock", false);
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
                           entity.getPersistentData().putDouble("Atk5", 0.0);
                           entity.getPersistentData().putBoolean("Attack5", false);
                        }
                     } else {
                        entity.getPersistentData().putBoolean("noai", false);
                     }
                  } else {
                     entity.getPersistentData().putBoolean("lock", true);
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
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 128, 10, false, false));
                  }

                  if (world instanceof Level _level) {
                     if (_level.isClientSide()) {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.ambient"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           0.0F,
                           false
                        );
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.ambient"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           0.0F
                        );
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack6")) {
               if (!(entity.getPersistentData().getDouble("Atk6") <= 0.0)) {
                  entity.getPersistentData().putDouble("Atk6", entity.getPersistentData().getDouble("Atk6") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("Atk6", 113.0);
               }

               if (entity.getPersistentData().getDouble("Atk6") != 113.0) {
                  if (entity.getPersistentData().getDouble("Atk6") != 112.0) {
                     if (entity.getPersistentData().getDouble("Atk6") != 85.0) {
                        if (entity.getPersistentData().getDouble("Atk6") != 67.0) {
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

                                 entity.getPersistentData().putBoolean("Attacking", false);
                                 entity.getPersistentData().putBoolean("lock", false);
                                 entity.getPersistentData().putDouble("Atk6", 0.0);
                                 entity.getPersistentData().putBoolean("Attack6", false);
                              }
                           } else if (world instanceof ServerLevel _level) {
                              _level.getServer()
                                 .getCommands()
                                 .performPrefixedCommand(
                                    new CommandSourceStack(
                                          CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                       )
                                       .withSuppressedOutput(),
                                    "/data merge entity @e[type=deepnight:dawn_fire_giant,limit=1,sort=nearest] {NoAI:0}"
                                 );
                           }
                        } else {
                           _center = new Vec3(x + entity.getLookAngle().x * 3.0, y, z + entity.getLookAngle().z * 3.0);

                           for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                              .stream()
                              .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                              .collect(Collectors.toList())) {
                              if (entityiterator == (!(entity instanceof Mob _mobEnt) ? null : _mobEnt.getTarget())) {
                                 if (world instanceof Level _level) {
                                    if (_level.isClientSide()) {
                                       _level.playLocalSound(
                                          x,
                                          y,
                                          z,
                                          dev.deepnight.SafeSounds.resolve("entity.turtle.egg_crack"),
                                          SoundSource.NEUTRAL,
                                          1.0F,
                                          1.0F,
                                          false
                                       );
                                    } else {
                                       _level.playSound(
                                          null,
                                          BlockPos.containing(x, y, z),
                                          dev.deepnight.SafeSounds.resolve("entity.turtle.egg_crack"),
                                          SoundSource.NEUTRAL,
                                          1.0F,
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
                                          dev.deepnight.SafeSounds.resolve("item.axe.strip"),
                                          SoundSource.NEUTRAL,
                                          1.0F,
                                          1.0F,
                                          false
                                       );
                                    } else {
                                       _level.playSound(
                                          null,
                                          BlockPos.containing(x, y, z),
                                          dev.deepnight.SafeSounds.resolve("item.axe.strip"),
                                          SoundSource.NEUTRAL,
                                          1.0F,
                                          1.0F
                                       );
                                    }
                                 }

                                 if (world instanceof ServerLevel _level) {
                                    Entity entityToSpawn = new GrabbedEntity(SimpleMobsModEntities.GRABBED.get(), _level);
                                    entityToSpawn.moveTo(x + entity.getLookAngle().x * 3.0, y + 2.0, z + entity.getLookAngle().z * 3.0, 0.0F, 0.0F);
                                    entityToSpawn.setYBodyRot(0.0F);
                                    entityToSpawn.setYHeadRot(0.0F);
                                    if (entityToSpawn instanceof Mob _mobToSpawn) {
                                       _mobToSpawn.finalizeSpawn(
                                          _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                                       );
                                    }

                                    world.addFreshEntity(entityToSpawn);
                                 }

                                 entityiterator.getPersistentData().putBoolean("grabbed", true);
                                 entity.getPersistentData().putBoolean("Attack6G", true);
                                 entity.getPersistentData().putDouble("Atk6", 0.0);
                                 entity.getPersistentData().putBoolean("Attack6", false);
                              }
                           }
                        }
                     } else if (world instanceof Level _level) {
                        if (_level.isClientSide()) {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.sweep"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F,
                              false
                           );
                        } else {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.sweep"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        }
                     }
                  } else {
                     entity.getPersistentData().putBoolean("lock", true);
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_fire_giant,limit=1,sort=nearest] {NoAI:1b}"
                           );
                     }

                     if (world instanceof Level _level) {
                        if (_level.isClientSide()) {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.ambient"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F,
                              false
                           );
                        } else {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.ambient"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        }
                     }
                  }
               } else {
                  entity.getPersistentData().putBoolean("Attacking", true);
                  Entity _entity = entity;
                  if (!(_entity instanceof Player _player)) {
                     if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.WHITE_CONCRETE));
                     }
                  } else {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.WHITE_CONCRETE));
                     _player.getInventory().setChanged();
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 113, 10, false, false));
                  }

                  entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putDouble("y", entity.getXRot());
               }
            }

            if (entity.getPersistentData().getBoolean("Attack6G")) {
               if (!(entity.getPersistentData().getDouble("Atk6g") <= 0.0)) {
                  entity.getPersistentData().putDouble("Atk6g", entity.getPersistentData().getDouble("Atk6g") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("Atk6g", 121.0);
               }

               if (entity.getPersistentData().getDouble("Atk6g") != 121.0) {
                  if (entity.getPersistentData().getDouble("Atk6g") != 117.0) {
                     if (entity.getPersistentData().getDouble("Atk6g") != 111.0) {
                        if (entity.getPersistentData().getDouble("Atk6g") != 101.0) {
                           if (entity.getPersistentData().getDouble("Atk6g") != 91.0) {
                              if (entity.getPersistentData().getDouble("Atk6g") != 81.0) {
                                 if (entity.getPersistentData().getDouble("Atk6g") != 71.0) {
                                    if (entity.getPersistentData().getDouble("Atk6g") != 66.0) {
                                       if (entity.getPersistentData().getDouble("Atk6g") != 10.0) {
                                          if (entity.getPersistentData().getDouble("Atk6g") == 1.0) {
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
                                             entity.getPersistentData().putBoolean("lock", false);
                                             entity.getPersistentData().putDouble("Atk6g", 0.0);
                                             entity.getPersistentData().putBoolean("Attack6G", false);
                                          }
                                       } else if (world instanceof ServerLevel _level) {
                                          _level.getServer()
                                             .getCommands()
                                             .performPrefixedCommand(
                                                new CommandSourceStack(
                                                      CommandSource.NULL,
                                                      new Vec3(x, y, z),
                                                      Vec2.ZERO,
                                                      _level,
                                                      4,
                                                      "",
                                                      Component.literal(""),
                                                      _level.getServer(),
                                                      null
                                                   )
                                                   .withSuppressedOutput(),
                                                "/data merge entity @e[type=deepnight:dawn_fire_giant,limit=1,sort=nearest] {NoAI:0}"
                                             );
                                       }
                                    } else {
                                       _center = new Vec3(x, y, z);

                                       for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(20.0), e -> true)
                                          .stream()
                                          .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                                          .collect(Collectors.toList())) {
                                          if (entityiterator.getPersistentData().getBoolean("grabbed")) {
                                             entityiterator.getPersistentData().putBoolean("grabbed", false);
                                          }

                                          if (entityiterator instanceof GrabbedEntity) {
                                             Entity _ent = entityiterator;
                                             _ent.teleportTo(x + entity.getLookAngle().x * 6.0, y + 15.0, z + entity.getLookAngle().z * 6.0);
                                             if (_ent instanceof ServerPlayer _serverPlayer) {
                                                _serverPlayer.connection
                                                   .teleport(
                                                      x + entity.getLookAngle().x * 6.0,
                                                      y + 15.0,
                                                      z + entity.getLookAngle().z * 6.0,
                                                      _ent.getYRot(),
                                                      _ent.getXRot()
                                                   );
                                             }
                                          }

                                          if (entityiterator instanceof GrabbedEntity && !entityiterator.level().isClientSide()) {
                                             entityiterator.discard();
                                          }
                                       }
                                    }
                                 } else {
                                    _center = new Vec3(x, y, z);

                                    for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(20.0), e -> true)
                                       .stream()
                                       .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                                       .collect(Collectors.toList())) {
                                       if (entityiterator instanceof GrabbedEntity) {
                                          Entity _ent = entityiterator;
                                          _ent.teleportTo(x + entity.getLookAngle().x * 6.0, y + 12.5, z + entity.getLookAngle().z * 6.0);
                                          if (_ent instanceof ServerPlayer _serverPlayer) {
                                             _serverPlayer.connection
                                                .teleport(
                                                   x + entity.getLookAngle().x * 6.0,
                                                   y + 12.5,
                                                   z + entity.getLookAngle().z * 6.0,
                                                   _ent.getYRot(),
                                                   _ent.getXRot()
                                                );
                                          }
                                       }
                                    }
                                 }
                              } else {
                                 _center = new Vec3(x, y, z);

                                 for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(20.0), e -> true)
                                    .stream()
                                    .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                                    .collect(Collectors.toList())) {
                                    if (entityiterator instanceof GrabbedEntity) {
                                       Entity _ent = entityiterator;
                                       _ent.teleportTo(x + entity.getLookAngle().x * 8.0, y + 11.7, z + entity.getLookAngle().z * 8.0);
                                       if (_ent instanceof ServerPlayer _serverPlayer) {
                                          _serverPlayer.connection
                                             .teleport(
                                                x + entity.getLookAngle().x * 8.0, y + 11.7, z + entity.getLookAngle().z * 8.0, _ent.getYRot(), _ent.getXRot()
                                             );
                                       }
                                    }
                                 }
                              }
                           } else {
                              _center = new Vec3(x, y, z);

                              for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(20.0), e -> true)
                                 .stream()
                                 .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                                 .collect(Collectors.toList())) {
                                 if (entityiterator instanceof GrabbedEntity) {
                                    Entity _ent = entityiterator;
                                    _ent.teleportTo(x + entity.getLookAngle().x * 10.0, y + 11.0, z + entity.getLookAngle().z * 10.0);
                                    if (_ent instanceof ServerPlayer _serverPlayer) {
                                       _serverPlayer.connection
                                          .teleport(
                                             x + entity.getLookAngle().x * 10.0, y + 11.0, z + entity.getLookAngle().z * 10.0, _ent.getYRot(), _ent.getXRot()
                                          );
                                    }
                                 }
                              }
                           }
                        } else {
                           _center = new Vec3(x, y, z);

                           for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(20.0), e -> true)
                              .stream()
                              .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                              .collect(Collectors.toList())) {
                              if (entityiterator instanceof GrabbedEntity) {
                                 Entity _ent = entityiterator;
                                 _ent.teleportTo(x + entity.getLookAngle().x * 12.0, y + 10.0, z + entity.getLookAngle().z * 12.0);
                                 if (_ent instanceof ServerPlayer _serverPlayer) {
                                    _serverPlayer.connection
                                       .teleport(
                                          x + entity.getLookAngle().x * 12.0, y + 10.0, z + entity.getLookAngle().z * 12.0, _ent.getYRot(), _ent.getXRot()
                                       );
                                 }
                              }
                           }
                        }
                     } else {
                        _center = new Vec3(x, y, z);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(20.0), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (entityiterator instanceof GrabbedEntity) {
                              Entity _ent = entityiterator;
                              _ent.teleportTo(x + entity.getLookAngle().x * 9.0, y + 8.0, z + entity.getLookAngle().z * 9.0);
                              if (_ent instanceof ServerPlayer _serverPlayer) {
                                 _serverPlayer.connection
                                    .teleport(x + entity.getLookAngle().x * 9.0, y + 8.0, z + entity.getLookAngle().z * 9.0, _ent.getYRot(), _ent.getXRot());
                              }
                           }
                        }
                     }
                  } else {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(20.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator.getPersistentData().getBoolean("grabbed")) {
                           Entity _ent = entityiterator;
                           _ent.teleportTo(x + entity.getLookAngle().x * 8.0, y + 4.0, z + entity.getLookAngle().z * 8.0);
                           if (_ent instanceof ServerPlayer _serverPlayer) {
                              _serverPlayer.connection
                                 .teleport(x + entity.getLookAngle().x * 8.0, y + 4.0, z + entity.getLookAngle().z * 8.0, _ent.getYRot(), _ent.getXRot());
                           }
                        }

                        if (entityiterator instanceof GrabbedEntity) {
                           Entity _ent = entityiterator;
                           _ent.teleportTo(x + entity.getLookAngle().x * 8.0, y + 6.0, z + entity.getLookAngle().z * 8.0);
                           if (_ent instanceof ServerPlayer _serverPlayer) {
                              _serverPlayer.connection
                                 .teleport(x + entity.getLookAngle().x * 8.0, y + 6.0, z + entity.getLookAngle().z * 8.0, _ent.getYRot(), _ent.getXRot());
                           }
                        }
                     }
                  }
               } else {
                  entity.getPersistentData().putBoolean("Attacking", true);
                  Entity _entity = entity;
                  if (!(_entity instanceof Player _player)) {
                     if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.RED_WOOL));
                     }
                  } else {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.RED_WOOL));
                     _player.getInventory().setChanged();
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 121, 10, false, false));
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack7")) {
               if (!(entity.getPersistentData().getDouble("Atk7") <= 0.0)) {
                  entity.getPersistentData().putDouble("Atk7", entity.getPersistentData().getDouble("Atk7") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("Atk7", 166.0);
               }

               if (entity.getPersistentData().getDouble("Atk7") != 166.0) {
                  if (entity.getPersistentData().getDouble("Atk7") != 154.0) {
                     if (entity.getPersistentData().getDouble("Atk7") != 82.0) {
                        if (entity.getPersistentData().getDouble("Atk7") != 81.0) {
                           if (entity.getPersistentData().getDouble("Atk7") != 74.0) {
                              if (entity.getPersistentData().getDouble("Atk7") != 15.0) {
                                 if (entity.getPersistentData().getDouble("Atk7") == 1.0) {
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
                                    entity.getPersistentData().putBoolean("lock", false);
                                    entity.getPersistentData().putDouble("Atk7", 0.0);
                                    entity.getPersistentData().putBoolean("Attack7", false);
                                 }
                              } else {
                                 entity.getPersistentData().putBoolean("noai", false);
                              }
                           } else {
                              if (world instanceof Level _level) {
                                 if (_level.isClientSide()) {
                                    _level.playLocalSound(
                                       x + entity.getLookAngle().x * 14.0,
                                       y,
                                       z + entity.getLookAngle().z * 14.0,
                                       dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                                       SoundSource.NEUTRAL,
                                       2.0F,
                                       1.0F,
                                       false
                                    );
                                 } else {
                                    _level.playSound(
                                       null,
                                       BlockPos.containing(x + entity.getLookAngle().x * 14.0, y, z + entity.getLookAngle().z * 14.0),
                                       dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                                       SoundSource.NEUTRAL,
                                       2.0F,
                                       1.0F
                                    );
                                 }
                              }

                              if (world instanceof Level _level) {
                                 if (_level.isClientSide()) {
                                    _level.playLocalSound(
                                       x + entity.getLookAngle().x * 14.0,
                                       y,
                                       z + entity.getLookAngle().z * 14.0,
                                       dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                                       SoundSource.NEUTRAL,
                                       2.0F,
                                       1.0F,
                                       false
                                    );
                                 } else {
                                    _level.playSound(
                                       null,
                                       BlockPos.containing(x + entity.getLookAngle().x * 14.0, y, z + entity.getLookAngle().z * 14.0),
                                       dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                                       SoundSource.NEUTRAL,
                                       2.0F,
                                       1.0F
                                    );
                                 }
                              }

                              if (world instanceof Level _level) {
                                 if (_level.isClientSide()) {
                                    _level.playLocalSound(
                                       x + entity.getLookAngle().x * 14.0,
                                       y,
                                       z + entity.getLookAngle().z * 14.0,
                                       dev.deepnight.SafeSounds.resolve("simple_mobs:sentinielground"),
                                       SoundSource.NEUTRAL,
                                       2.0F,
                                       1.0F,
                                       false
                                    );
                                 } else {
                                    _level.playSound(
                                       null,
                                       BlockPos.containing(x + entity.getLookAngle().x * 14.0, y, z + entity.getLookAngle().z * 14.0),
                                       dev.deepnight.SafeSounds.resolve("simple_mobs:sentinielground"),
                                       SoundSource.NEUTRAL,
                                       2.0F,
                                       1.0F
                                    );
                                 }
                              }

                              if (world instanceof ServerLevel _level) {
                                 Entity entityToSpawn = new GroundbreakEffectBigEntity(SimpleMobsModEntities.GROUNDBREAK_EFFECT_BIG.get(), _level);
                                 entityToSpawn.moveTo(
                                    x + entity.getLookAngle().x * 14.0, y, z + entity.getLookAngle().z * 14.0, world.getRandom().nextFloat() * 360.0F, 0.0F
                                 );
                                 if (entityToSpawn instanceof Mob _mobToSpawn) {
                                    _mobToSpawn.finalizeSpawn(
                                       _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                                    );
                                 }

                                 world.addFreshEntity(entityToSpawn);
                              }

                              if (world instanceof ServerLevel _level) {
                                 Entity entityToSpawn = new AirEntity(SimpleMobsModEntities.AIR.get(), _level);
                                 entityToSpawn.moveTo(x + entity.getLookAngle().x * 14.0, y, z + entity.getLookAngle().z * 14.0, entity.getYRot(), 0.0F);
                                 entityToSpawn.setYBodyRot(entity.getYRot());
                                 entityToSpawn.setYHeadRot(entity.getYRot());
                                 if (entityToSpawn instanceof Mob _mobToSpawn) {
                                    _mobToSpawn.finalizeSpawn(
                                       _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                                    );
                                 }

                                 world.addFreshEntity(entityToSpawn);
                              }

                              if (world instanceof ServerLevel _level) {
                                 Entity entityToSpawn = new RumbleEntity(SimpleMobsModEntities.RUMBLE.get(), _level);
                                 entityToSpawn.moveTo(x + entity.getLookAngle().x * 14.0, y, z + entity.getLookAngle().z * 14.0, entity.getYRot(), 0.0F);
                                 entityToSpawn.setYBodyRot(entity.getYRot());
                                 entityToSpawn.setYHeadRot(entity.getYRot());
                                 if (entityToSpawn instanceof Mob _mobToSpawn) {
                                    _mobToSpawn.finalizeSpawn(
                                       _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                                    );
                                 }

                                 world.addFreshEntity(entityToSpawn);
                              }

                              _center = new Vec3(x + entity.getLookAngle().x * 14.0, y, z + entity.getLookAngle().z * 14.0);

                              for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.5), e -> true)
                                 .stream()
                                 .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                                 .collect(Collectors.toList())) {
                                 if (!entityiterator.getPersistentData().getBoolean("firegiant") && entityiterator instanceof LivingEntity _entity) {
                                    _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 40, 1, false, false));
                                 }
                              }

                              _center = new Vec3(x + entity.getLookAngle().x * 14.0, y, z + entity.getLookAngle().z * 14.0);

                              for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5.0), e -> true)
                                 .stream()
                                 .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                                 .collect(Collectors.toList())) {
                                 if (!entityiterator.getPersistentData().getBoolean("firegiant")) {
                                    SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                       .setInitialPosition(entity.position())
                                       .setStunType(StunType.KNOCKDOWN)
                                       .setImpact(5.0F)
                                       .setArmorNegation(30.0F);
                                    entityiterator.hurt((DamageSource)dmgSrc, 42.0F);
                                 }
                              }
                           }
                        } else {
                           entity.getPersistentData().putBoolean("lock", true);
                           entity.getPersistentData().putBoolean("noai", true);
                        }
                     } else {
                        if (world instanceof Level _level) {
                           if (_level.isClientSide()) {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.ambient"),
                                 SoundSource.NEUTRAL,
                                 1.0F,
                                 1.0F,
                                 false
                              );
                           } else {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.ambient"),
                                 SoundSource.NEUTRAL,
                                 1.0F,
                                 1.0F
                              );
                           }
                        }

                        if (entity instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 86, 10, false, false));
                        }

                        entity.getPersistentData().putBoolean("focus", false);
                        entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                     }
                  } else {
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 72, 1, false, false));
                     }

                     entity.getPersistentData().putBoolean("focus", true);
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

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 12, 10, false, false));
                  }
               }

               if (entity.getPersistentData().getDouble("Atk7") == 134.0
                  || entity.getPersistentData().getDouble("Atk7") == 114.0
                  || entity.getPersistentData().getDouble("Atk7") == 98.0
                  || entity.getPersistentData().getDouble("Atk7") == 88.0) {
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

                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(8.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!entityiterator.getPersistentData().getBoolean("firegiant") && entityiterator instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 30, 1, false, false));
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("leghurt")) {
               if (!(entity.getPersistentData().getDouble("l") <= 0.0)) {
                  entity.getPersistentData().putDouble("l", entity.getPersistentData().getDouble("l") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("l", 25.0);
               }

               if (entity.getPersistentData().getDouble("l") != 25.0) {
                  if (entity.getPersistentData().getDouble("l") == 1.0) {
                     Entity _entity = entity;
                     if (!(_entity instanceof Player _player)) {
                        if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.FEET, new ItemStack(Blocks.AIR));
                        }
                     } else {
                        _player.getInventory().armor.set(0, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     }

                     entity.getPersistentData().putDouble("l", 0.0);
                     entity.getPersistentData().putBoolean("leghurt", false);
                  }
               } else {
                  Entity _entity = entity;
                  if (!(_entity instanceof Player _player)) {
                     if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
                     }
                  } else {
                     _player.getInventory().armor.set(0, new ItemStack(Items.LEATHER_BOOTS));
                     _player.getInventory().setChanged();
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack8")) {
               if (!(entity.getPersistentData().getDouble("Atk8") <= 0.0)) {
                  entity.getPersistentData().putDouble("Atk8", entity.getPersistentData().getDouble("Atk8") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("Atk8", 151.0);
               }

               if (entity.getPersistentData().getDouble("Atk8") != 151.0) {
                  if (entity.getPersistentData().getDouble("Atk8") != 111.0) {
                     if (entity.getPersistentData().getDouble("Atk8") != 103.0) {
                        if (entity.getPersistentData().getDouble("Atk8") != 41.0) {
                           if (entity.getPersistentData().getDouble("Atk8") == 1.0) {
                              Entity _entity = entity;
                              if (!(_entity instanceof Player _player)) {
                                 if (_entity instanceof LivingEntity _living) {
                                    _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                                 }
                              } else {
                                 _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                                 _player.getInventory().setChanged();
                              }

                              entity.getPersistentData().putBoolean("noai", false);
                              entity.getPersistentData().putBoolean("Attacking", false);
                              entity.getPersistentData().putDouble("Atk8", 0.0);
                              entity.getPersistentData().putBoolean("Attack8", false);
                           }
                        } else {
                           entity.getPersistentData().putBoolean("focus", false);
                           entity.getPersistentData().putBoolean("noai", true);
                        }
                     } else if (world instanceof Level _level) {
                        if (_level.isClientSide()) {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.flame"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F,
                              false
                           );
                        } else {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.flame"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        }
                     }
                  } else {
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 50, 1, false, false));
                     }

                     entity.getPersistentData().putBoolean("noai", false);
                     entity.getPersistentData().putBoolean("focus", true);
                  }
               } else {
                  entity.getPersistentData().putBoolean("Attacking", true);
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
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 10, false, false));
                  }

                  entity.getPersistentData().putBoolean("noai", true);
               }

               if (entity.getPersistentData().getDouble("Atk8") <= 101.0
                  && entity.getPersistentData().getDouble("Atk8") >= 48.0
                  && world instanceof ServerLevel _level) {
                  Entity entityToSpawn = new Projectile2Entity(SimpleMobsModEntities.PROJECTILE_2.get(), _level);
                  entityToSpawn.moveTo(x + entity.getLookAngle().x * 3.0, y + 8.0, z + entity.getLookAngle().z * 3.0, Mth.nextInt(net.minecraft.util.RandomSource.create(), 0, 360), 90.0F);
                  entityToSpawn.setYBodyRot(Mth.nextInt(net.minecraft.util.RandomSource.create(), 0, 360));
                  entityToSpawn.setYHeadRot(Mth.nextInt(net.minecraft.util.RandomSource.create(), 0, 360));
                  if (entityToSpawn instanceof Mob _mobToSpawn) {
                     _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                  }

                  world.addFreshEntity(entityToSpawn);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack9")) {
               if (!(entity.getPersistentData().getDouble("Atk9") <= 0.0)) {
                  entity.getPersistentData().putDouble("Atk9", entity.getPersistentData().getDouble("Atk9") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("Atk9", 161.0);
               }

               if (entity.getPersistentData().getDouble("Atk9") != 161.0) {
                  if (entity.getPersistentData().getDouble("Atk9") != 142.0) {
                     if (entity.getPersistentData().getDouble("Atk9") != 141.0) {
                        if (entity.getPersistentData().getDouble("Atk9") != 135.0) {
                           if (entity.getPersistentData().getDouble("Atk9") != 107.0) {
                              if (entity.getPersistentData().getDouble("Atk9") != 71.0) {
                                 if (entity.getPersistentData().getDouble("Atk9") != 37.0) {
                                    if (entity.getPersistentData().getDouble("Atk9") == 7.0) {
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
                                       entity.getPersistentData().putBoolean("lock", false);
                                       entity.getPersistentData().putDouble("Atk9", 0.0);
                                       entity.getPersistentData().putBoolean("Attack9", false);
                                    }
                                 } else if (world instanceof ServerLevel _level) {
                                    _level.getServer()
                                       .getCommands()
                                       .performPrefixedCommand(
                                          new CommandSourceStack(
                                                CommandSource.NULL,
                                                new Vec3(x, y, z),
                                                Vec2.ZERO,
                                                _level,
                                                4,
                                                "",
                                                Component.literal(""),
                                                _level.getServer(),
                                                null
                                             )
                                             .withSuppressedOutput(),
                                          "/data merge entity @e[type=deepnight:dawn_fire_giant,limit=1,sort=nearest] {NoAI:0}"
                                       );
                                 }
                              } else {
                                 if (world instanceof Level _level) {
                                    if (_level.isClientSide()) {
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
                                    } else {
                                       _level.playSound(
                                          null,
                                          BlockPos.containing(x, y, z),
                                          dev.deepnight.SafeSounds.resolve("entity.zombie.break_wooden_door"),
                                          SoundSource.NEUTRAL,
                                          2.0F,
                                          0.0F
                                       );
                                    }
                                 }

                                 if (world instanceof Level _level) {
                                    if (_level.isClientSide()) {
                                       _level.playLocalSound(
                                          x,
                                          y,
                                          z,
                                          dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                                          SoundSource.NEUTRAL,
                                          2.0F,
                                          0.0F,
                                          false
                                       );
                                    } else {
                                       _level.playSound(
                                          null,
                                          BlockPos.containing(x, y, z),
                                          dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                                          SoundSource.NEUTRAL,
                                          2.0F,
                                          0.0F
                                       );
                                    }
                                 }

                                 if (world instanceof Level _level) {
                                    if (_level.isClientSide()) {
                                       _level.playLocalSound(
                                          x,
                                          y,
                                          z,
                                          dev.deepnight.SafeSounds.resolve("block.anvil.land"),
                                          SoundSource.NEUTRAL,
                                          2.0F,
                                          0.0F,
                                          false
                                       );
                                    } else {
                                       _level.playSound(
                                          null,
                                          BlockPos.containing(x, y, z),
                                          dev.deepnight.SafeSounds.resolve("block.anvil.land"),
                                          SoundSource.NEUTRAL,
                                          2.0F,
                                          0.0F
                                       );
                                    }
                                 }

                                 _center = new Vec3(x + entity.getLookAngle().x * 9.0, y, z + entity.getLookAngle().z * 9.0);

                                 for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(20.0), e -> true)
                                    .stream()
                                    .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                                    .collect(Collectors.toList())) {
                                    if (!entityiterator.getPersistentData().getBoolean("firegiant")) {
                                       SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                          .setInitialPosition(entity.position())
                                          .setStunType(StunType.KNOCKDOWN)
                                          .setImpact(14.0F)
                                          .setArmorNegation(30.0F);
                                       entityiterator.hurt((DamageSource)dmgSrc, 40.0F);
                                       entityiterator.setDeltaMovement(new Vec3(0.0, 1.0, 0.0));
                                    }
                                 }

                                 if (world instanceof ServerLevel _level) {
                                    Entity entityToSpawn = new RumbleEntity(SimpleMobsModEntities.RUMBLE.get(), _level);
                                    entityToSpawn.moveTo(
                                       x + entity.getLookAngle().x * 7.0, y, z + entity.getLookAngle().z * 7.0, world.getRandom().nextFloat() * 360.0F, 0.0F
                                    );
                                    if (entityToSpawn instanceof Mob _mobToSpawn) {
                                       _mobToSpawn.finalizeSpawn(
                                          _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                                       );
                                    }

                                    world.addFreshEntity(entityToSpawn);
                                 }

                                 if (world instanceof ServerLevel _level) {
                                    Entity entityToSpawn = new RumbleEntity(SimpleMobsModEntities.RUMBLE.get(), _level);
                                    entityToSpawn.moveTo(
                                       x + entity.getLookAngle().x * 8.0, y, z + entity.getLookAngle().z * 8.0, world.getRandom().nextFloat() * 360.0F, 0.0F
                                    );
                                    if (entityToSpawn instanceof Mob _mobToSpawn) {
                                       _mobToSpawn.finalizeSpawn(
                                          _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                                       );
                                    }

                                    world.addFreshEntity(entityToSpawn);
                                 }

                                 if (world instanceof ServerLevel _level) {
                                    _level.sendParticles(
                                       ParticleTypes.FLAME, x + entity.getLookAngle().x * 9.0, y, z + entity.getLookAngle().z * 9.0, 20, 40.0, 2.0, 40.0, 0.5
                                    );
                                 }

                                 if (world instanceof ServerLevel _level) {
                                    _level.sendParticles(
                                       (SimpleParticleType)SimpleMobsModParticleTypes.FLAME.get(),
                                       x + entity.getLookAngle().x * 9.0,
                                       y,
                                       z + entity.getLookAngle().z * 9.0,
                                       100,
                                       40.0,
                                       2.0,
                                       40.0,
                                       0.5
                                    );
                                 }

                                 if (world instanceof ServerLevel _level) {
                                    _level.sendParticles(
                                       (SimpleParticleType)SimpleMobsModParticleTypes.BURNT_FLAME.get(),
                                       x + entity.getLookAngle().x * 9.0,
                                       y,
                                       z + entity.getLookAngle().z * 9.0,
                                       100,
                                       40.0,
                                       2.0,
                                       40.0,
                                       0.5
                                    );
                                 }

                                 for (int index6 = 0; index6 < 14; index6++) {
                                    if (Math.random() >= 0.2 && world instanceof ServerLevel _level) {
                                       Entity entityToSpawn = new RumbleEntity(SimpleMobsModEntities.RUMBLE.get(), _level);
                                       entityToSpawn.moveTo(
                                          x + entity.getLookAngle().x * 9.0 + Mth.nextInt(net.minecraft.util.RandomSource.create(), -10, 10),
                                          y,
                                          z + entity.getLookAngle().z * 9.0 + Mth.nextInt(net.minecraft.util.RandomSource.create(), -10, 10),
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
                           } else if (world instanceof Level _level) {
                              if (_level.isClientSide()) {
                                 _level.playLocalSound(
                                    x,
                                    y,
                                    z,
                                    dev.deepnight.SafeSounds.resolve("simple_mobs:giant_skele.dwell"),
                                    SoundSource.NEUTRAL,
                                    4.0F,
                                    1.0F,
                                    false
                                 );
                              } else {
                                 _level.playSound(
                                    null,
                                    BlockPos.containing(x, y, z),
                                    dev.deepnight.SafeSounds.resolve("simple_mobs:giant_skele.dwell"),
                                    SoundSource.NEUTRAL,
                                    4.0F,
                                    1.0F
                                 );
                              }
                           }
                        } else {
                           if (world instanceof Level _level) {
                              if (_level.isClientSide()) {
                                 _level.playLocalSound(
                                    x + entity.getLookAngle().x * 9.0,
                                    y,
                                    z + entity.getLookAngle().z * 9.0,
                                    dev.deepnight.SafeSounds.resolve("entity.dragon_fireball.explode"),
                                    SoundSource.NEUTRAL,
                                    2.0F,
                                    0.5F,
                                    false
                                 );
                              } else {
                                 _level.playSound(
                                    null,
                                    BlockPos.containing(x + entity.getLookAngle().x * 9.0, y, z + entity.getLookAngle().z * 9.0),
                                    dev.deepnight.SafeSounds.resolve("entity.dragon_fireball.explode"),
                                    SoundSource.NEUTRAL,
                                    2.0F,
                                    0.5F
                                 );
                              }
                           }

                           if (world instanceof Level _level) {
                              if (_level.isClientSide()) {
                                 _level.playLocalSound(
                                    x + entity.getLookAngle().x * 9.0,
                                    y,
                                    z + entity.getLookAngle().z * 9.0,
                                    dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                                    SoundSource.NEUTRAL,
                                    2.0F,
                                    1.0F,
                                    false
                                 );
                              } else {
                                 _level.playSound(
                                    null,
                                    BlockPos.containing(x + entity.getLookAngle().x * 9.0, y, z + entity.getLookAngle().z * 9.0),
                                    dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                                    SoundSource.NEUTRAL,
                                    2.0F,
                                    1.0F
                                 );
                              }
                           }

                           if (world instanceof Level _level) {
                              if (_level.isClientSide()) {
                                 _level.playLocalSound(
                                    x + entity.getLookAngle().x * 9.0,
                                    y,
                                    z + entity.getLookAngle().z * 9.0,
                                    dev.deepnight.SafeSounds.resolve("entity.zombie.break_wooden_door"),
                                    SoundSource.NEUTRAL,
                                    2.0F,
                                    1.0F,
                                    false
                                 );
                              } else {
                                 _level.playSound(
                                    null,
                                    BlockPos.containing(x + entity.getLookAngle().x * 9.0, y, z + entity.getLookAngle().z * 9.0),
                                    dev.deepnight.SafeSounds.resolve("entity.zombie.break_wooden_door"),
                                    SoundSource.NEUTRAL,
                                    2.0F,
                                    1.0F
                                 );
                              }
                           }

                           if (world instanceof ServerLevel _level) {
                              Entity entityToSpawn = new RumbleEntity(SimpleMobsModEntities.RUMBLE.get(), _level);
                              entityToSpawn.moveTo(
                                 x + entity.getLookAngle().x * Mth.nextInt(net.minecraft.util.RandomSource.create(), 7, 11),
                                 y,
                                 z + entity.getLookAngle().z * Mth.nextInt(net.minecraft.util.RandomSource.create(), 7, 11),
                                 entity.getYRot(),
                                 0.0F
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

                           if (world instanceof ServerLevel _level) {
                              Entity entityToSpawn = new RumbleEntity(SimpleMobsModEntities.RUMBLE.get(), _level);
                              entityToSpawn.moveTo(
                                 x + entity.getLookAngle().x * Mth.nextInt(net.minecraft.util.RandomSource.create(), 7, 11),
                                 y,
                                 z + entity.getLookAngle().z * Mth.nextInt(net.minecraft.util.RandomSource.create(), 7, 11),
                                 entity.getYRot(),
                                 0.0F
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

                           _center = new Vec3(x + entity.getLookAngle().x * 7.0, y, z + entity.getLookAngle().z * 7.0);

                           for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(10.0), e -> true)
                              .stream()
                              .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                              .collect(Collectors.toList())) {
                              if (!entityiterator.getPersistentData().getBoolean("firegiant") && entityiterator instanceof LivingEntity _entity) {
                                 _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 30, 1, false, false));
                              }
                           }

                           _center = new Vec3(x + entity.getLookAngle().x * 7.0, y, z + entity.getLookAngle().z * 7.0);

                           for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.0), e -> true)
                              .stream()
                              .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                              .collect(Collectors.toList())) {
                              if (!entityiterator.getPersistentData().getBoolean("firegiant")) {
                                 SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                    .setInitialPosition(entity.position())
                                    .setStunType(StunType.LONG)
                                    .setImpact(5.0F)
                                    .setArmorNegation(30.0F);
                                 entityiterator.hurt((DamageSource)dmgSrc, 18.0F);
                              }
                           }
                        }
                     } else {
                        entity.getPersistentData().putBoolean("lock", true);
                        if (world instanceof ServerLevel _level) {
                           _level.getServer()
                              .getCommands()
                              .performPrefixedCommand(
                                 new CommandSourceStack(
                                       CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                    )
                                    .withSuppressedOutput(),
                                 "/data merge entity @e[type=deepnight:dawn_fire_giant,limit=1,sort=nearest] {NoAI:1b}"
                              );
                        }
                     }
                  } else {
                     entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                  }
               } else {
                  entity.getPersistentData().putBoolean("Attacking", true);
                  Entity _entity = entity;
                  if (!(_entity instanceof Player _player)) {
                     if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.ACACIA_LOG));
                     }
                  } else {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.ACACIA_LOG));
                     _player.getInventory().setChanged();
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 161, 10, false, false));
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 120, 2, false, false));
                  }

                  if (world instanceof Level _level) {
                     if (_level.isClientSide()) {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.ambient"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F,
                           false
                        );
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.ambient"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F
                        );
                     }
                  }
               }

               if (entity.getPersistentData().getDouble("Atk9") <= 111.0 && entity.getPersistentData().getDouble("Atk9") >= 100.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 9.0, y, z + entity.getLookAngle().z * 9.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(20.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!entityiterator.getPersistentData().getBoolean("firegiant") && entityiterator instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 20, 1, false, false));
                     }
                  }

                  if (Math.random() >= 0.3 && world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        (SimpleParticleType)SimpleMobsModParticleTypes.BURNT_FLAME.get(),
                        x + entity.getLookAngle().x * 9.0 + Mth.nextInt(net.minecraft.util.RandomSource.create(), -20, 20),
                        y,
                        z + entity.getLookAngle().z * 9.0 + Mth.nextInt(net.minecraft.util.RandomSource.create(), -20, 20),
                        20,
                        10.0,
                        1.0,
                        10.0,
                        1.0
                     );
                  }
               }

               if (entity.getPersistentData().getDouble("Atk9") <= 100.0 && entity.getPersistentData().getDouble("Atk9") >= 81.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 9.0, y, z + entity.getLookAngle().z * 9.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(20.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!entityiterator.getPersistentData().getBoolean("firegiant") && entityiterator instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 10, 1, false, false));
                     }
                  }

                  if (Math.random() >= 0.1) {
                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(
                           ParticleTypes.LAVA,
                           x + entity.getLookAngle().x * 9.0 + Mth.nextInt(net.minecraft.util.RandomSource.create(), -40, 40),
                           y,
                           z + entity.getLookAngle().z * 9.0 + Mth.nextInt(net.minecraft.util.RandomSource.create(), -40, 40),
                           20,
                           10.0,
                           1.0,
                           10.0,
                           1.0
                        );
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(
                           (SimpleParticleType)SimpleMobsModParticleTypes.BURNT_FLAME.get(),
                           x + entity.getLookAngle().x * 9.0 + Mth.nextInt(net.minecraft.util.RandomSource.create(), -40, 40),
                           y,
                           z + entity.getLookAngle().z * 9.0 + Mth.nextInt(net.minecraft.util.RandomSource.create(), -40, 40),
                           20,
                           10.0,
                           1.0,
                           10.0,
                           1.0
                        );
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Unchained")) {
               if (entity instanceof LivingEntity _entity) {
                  _entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 5, 6, false, false));
               }

               if (!(entity.getPersistentData().getDouble("ucd") <= 0.0)) {
                  entity.getPersistentData().putDouble("ucd", entity.getPersistentData().getDouble("ucd") - 1.0);
               } else {
                  entity.getPersistentData().putDouble("ucd", 467.0);
               }

               if (entity.getPersistentData().getDouble("ucd") != 467.0) {
                  if (entity.getPersistentData().getDouble("ucd") != 457.0) {
                     if (entity.getPersistentData().getDouble("ucd") != 433.0) {
                        if (entity.getPersistentData().getDouble("ucd") != 425.0) {
                           if (entity.getPersistentData().getDouble("ucd") != 385.0) {
                              if (entity.getPersistentData().getDouble("ucd") != 282.0) {
                                 if (entity.getPersistentData().getDouble("ucd") != 166.0) {
                                    if (entity.getPersistentData().getDouble("ucd") != 130.0) {
                                       if (entity.getPersistentData().getDouble("ucd") != 105.0) {
                                          if (entity.getPersistentData().getDouble("ucd") != 77.0) {
                                             if (entity.getPersistentData().getDouble("ucd") != 20.0) {
                                                if (entity.getPersistentData().getDouble("ucd") != 10.0) {
                                                   if (entity.getPersistentData().getDouble("ucd") == 1.0) {
                                                      Entity _entity = entity;
                                                      if (!(_entity instanceof Player _player)) {
                                                         if (_entity instanceof LivingEntity _living) {
                                                            _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                                                         }
                                                      } else {
                                                         _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                                                         _player.getInventory().setChanged();
                                                      }

                                                      entity.getPersistentData().putBoolean("Stage2", true);
                                                      entity.getPersistentData().putBoolean("Attacking", false);
                                                      entity.getPersistentData().putDouble("Atk8", 0.0);
                                                      entity.getPersistentData().putBoolean("Attack8", false);
                                                   }
                                                } else {
                                                   Entity _entity = entity;
                                                   if (!(_entity instanceof Player _player)) {
                                                      if (_entity instanceof LivingEntity _living) {
                                                         _living.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
                                                      }
                                                   } else {
                                                      _player.getInventory().armor.set(2, new ItemStack(Items.LEATHER_CHESTPLATE));
                                                      _player.getInventory().setChanged();
                                                   }
                                                }
                                             } else if (world instanceof ServerLevel _level) {
                                                _level.getServer()
                                                   .getCommands()
                                                   .performPrefixedCommand(
                                                      new CommandSourceStack(
                                                            CommandSource.NULL,
                                                            new Vec3(x, y, z),
                                                            Vec2.ZERO,
                                                            _level,
                                                            4,
                                                            "",
                                                            Component.literal(""),
                                                            _level.getServer(),
                                                            null
                                                         )
                                                         .withSuppressedOutput(),
                                                      "/data merge entity @e[type=deepnight:dawn_fire_giant,limit=1,sort=nearest] {NoAI:0}"
                                                   );
                                             }
                                          } else {
                                             if (world instanceof ServerLevel _level) {
                                                _level.getServer()
                                                   .getCommands()
                                                   .performPrefixedCommand(
                                                      new CommandSourceStack(
                                                            CommandSource.NULL,
                                                            new Vec3(x + entity.getLookAngle().x * -2.5, y + 11.0, z + entity.getLookAngle().z * -2.5),
                                                            Vec2.ZERO,
                                                            _level,
                                                            4,
                                                            "",
                                                            Component.literal(""),
                                                            _level.getServer(),
                                                            null
                                                         )
                                                         .withSuppressedOutput(),
                                                      "/particle simple_mobs:flame ~ ~ ~ 3 3 3 0.1 100 normal"
                                                   );
                                             }

                                             if (world instanceof ServerLevel _level) {
                                                _level.getServer()
                                                   .getCommands()
                                                   .performPrefixedCommand(
                                                      new CommandSourceStack(
                                                            CommandSource.NULL,
                                                            new Vec3(x + entity.getLookAngle().x * -2.5, y + 11.0, z + entity.getLookAngle().z * -2.5),
                                                            Vec2.ZERO,
                                                            _level,
                                                            4,
                                                            "",
                                                            Component.literal(""),
                                                            _level.getServer(),
                                                            null
                                                         )
                                                         .withSuppressedOutput(),
                                                      "/particle minecraft:flash ~ ~ ~ 3 3 3 0.1 30 normal"
                                                   );
                                             }

                                             if (world instanceof ServerLevel _level) {
                                                _level.getServer()
                                                   .getCommands()
                                                   .performPrefixedCommand(
                                                      new CommandSourceStack(
                                                            CommandSource.NULL,
                                                            new Vec3(x + entity.getLookAngle().x * -2.5, y + 14.0, z + entity.getLookAngle().z * -2.5),
                                                            Vec2.ZERO,
                                                            _level,
                                                            4,
                                                            "",
                                                            Component.literal(""),
                                                            _level.getServer(),
                                                            null
                                                         )
                                                         .withSuppressedOutput(),
                                                      "/particle simple_mobs:burnt_flame ~ ~ ~ 3 3 3 0.1 150 normal"
                                                   );
                                             }

                                             if (world instanceof Level _level) {
                                                if (_level.isClientSide()) {
                                                   _level.playLocalSound(
                                                      x,
                                                      y,
                                                      z,
                                                      dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammer"),
                                                      SoundSource.NEUTRAL,
                                                      3.0F,
                                                      1.0F,
                                                      false
                                                   );
                                                } else {
                                                   _level.playSound(
                                                      null,
                                                      BlockPos.containing(x, y, z),
                                                      dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammer"),
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
                                                      dev.deepnight.SafeSounds.resolve("simple_mobs:jump"),
                                                      SoundSource.NEUTRAL,
                                                      3.0F,
                                                      1.0F,
                                                      false
                                                   );
                                                } else {
                                                   _level.playSound(
                                                      null,
                                                      BlockPos.containing(x, y, z),
                                                      dev.deepnight.SafeSounds.resolve("simple_mobs:jump"),
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
                                                      dev.deepnight.SafeSounds.resolve("entity.dragon_fireball.explode"),
                                                      SoundSource.NEUTRAL,
                                                      3.0F,
                                                      0.0F,
                                                      false
                                                   );
                                                } else {
                                                   _level.playSound(
                                                      null,
                                                      BlockPos.containing(x, y, z),
                                                      dev.deepnight.SafeSounds.resolve("entity.dragon_fireball.explode"),
                                                      SoundSource.NEUTRAL,
                                                      3.0F,
                                                      0.0F
                                                   );
                                                }
                                             }

                                             _center = new Vec3(x, y, z);

                                             for (Entity entityiterator : world.getEntitiesOfClass(
                                                   Entity.class, new AABB(_center, _center).inflate(30.0), e -> true
                                                )
                                                .stream()
                                                .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                                                .collect(Collectors.toList())) {
                                                if (!(entityiterator instanceof FireGiantEntity) && entityiterator instanceof LivingEntity _entity) {
                                                   _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 60, 2, false, false));
                                                }
                                             }

                                             _center = new Vec3(x, y, z);

                                             for (Entity entityiterator : world.getEntitiesOfClass(
                                                   Entity.class, new AABB(_center, _center).inflate(7.0), e -> true
                                                )
                                                .stream()
                                                .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                                                .collect(Collectors.toList())) {
                                                if (!entity.getPersistentData().getBoolean("firegiant")) {
                                                   if (entityiterator instanceof LivingEntity _entity) {
                                                      _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 60, 3, false, false));
                                                   }

                                                   SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                                      .setInitialPosition(entity.position())
                                                      .setStunType(StunType.LONG)
                                                      .setImpact(6.0F)
                                                      .setArmorNegation(30.0F);
                                                   entityiterator.hurt((DamageSource)dmgSrc, 6.0F);
                                                }
                                             }

                                             for (int index12 = 0; index12 < 15; index12++) {
                                                if (world instanceof ServerLevel _level) {
                                                   Entity entityToSpawn = new LavaBallEntity(SimpleMobsModEntities.LAVA_BALL.get(), _level);
                                                   entityToSpawn.moveTo(
                                                      x + entity.getLookAngle().x * -2.5, y + 7.0, z + entity.getLookAngle().z * -2.5, 0.0F, 0.0F
                                                   );
                                                   entityToSpawn.setYBodyRot(0.0F);
                                                   entityToSpawn.setYHeadRot(0.0F);
                                                   entityToSpawn.setDeltaMovement(
                                                      Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
                                                      1.4 + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.3, 0.3),
                                                      Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0)
                                                   );
                                                   if (entityToSpawn instanceof Mob _mobToSpawn) {
                                                      _mobToSpawn.finalizeSpawn(
                                                         _level,
                                                         world.getCurrentDifficultyAt(entityToSpawn.blockPosition()),
                                                         MobSpawnType.MOB_SUMMONED,
                                                         null,
                                                         null
                                                      );
                                                   }

                                                   world.addFreshEntity(entityToSpawn);
                                                }
                                             }

                                             for (int index13 = 0; index13 < 15; index13++) {
                                                if (world instanceof ServerLevel _level) {
                                                   Entity entityToSpawn = new LavaBallEntity(SimpleMobsModEntities.LAVA_BALL.get(), _level);
                                                   entityToSpawn.moveTo(
                                                      x + entity.getLookAngle().x * -2.5, y + 7.0, z + entity.getLookAngle().z * -2.5, 0.0F, 0.0F
                                                   );
                                                   entityToSpawn.setYBodyRot(0.0F);
                                                   entityToSpawn.setYHeadRot(0.0F);
                                                   entityToSpawn.setDeltaMovement(
                                                      Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.8, 1.8),
                                                      1.8 + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.3, 0.3),
                                                      Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.8, 1.8)
                                                   );
                                                   if (entityToSpawn instanceof Mob _mobToSpawn) {
                                                      _mobToSpawn.finalizeSpawn(
                                                         _level,
                                                         world.getCurrentDifficultyAt(entityToSpawn.blockPosition()),
                                                         MobSpawnType.MOB_SUMMONED,
                                                         null,
                                                         null
                                                      );
                                                   }

                                                   world.addFreshEntity(entityToSpawn);
                                                }
                                             }
                                          }
                                       } else {
                                          if (world instanceof ServerLevel _level) {
                                             _level.getServer()
                                                .getCommands()
                                                .performPrefixedCommand(
                                                   new CommandSourceStack(
                                                         CommandSource.NULL,
                                                         new Vec3(x + entity.getLookAngle().x * -2.5, y + 11.0, z + entity.getLookAngle().z * -2.5),
                                                         Vec2.ZERO,
                                                         _level,
                                                         4,
                                                         "",
                                                         Component.literal(""),
                                                         _level.getServer(),
                                                         null
                                                      )
                                                      .withSuppressedOutput(),
                                                   "/particle simple_mobs:flame ~ ~ ~ 2 3 2 0.1 50 normal"
                                                );
                                          }

                                          if (world instanceof ServerLevel _level) {
                                             _level.getServer()
                                                .getCommands()
                                                .performPrefixedCommand(
                                                   new CommandSourceStack(
                                                         CommandSource.NULL,
                                                         new Vec3(x + entity.getLookAngle().x * -2.5, y + 11.0, z + entity.getLookAngle().z * -2.5),
                                                         Vec2.ZERO,
                                                         _level,
                                                         4,
                                                         "",
                                                         Component.literal(""),
                                                         _level.getServer(),
                                                         null
                                                      )
                                                      .withSuppressedOutput(),
                                                   "/particle minecraft:flash ~ ~ ~ 2 2 2 0.1 20 normal"
                                                );
                                          }

                                          if (world instanceof ServerLevel _level) {
                                             _level.getServer()
                                                .getCommands()
                                                .performPrefixedCommand(
                                                   new CommandSourceStack(
                                                         CommandSource.NULL,
                                                         new Vec3(x + entity.getLookAngle().x * -2.5, y + 14.0, z + entity.getLookAngle().z * -2.5),
                                                         Vec2.ZERO,
                                                         _level,
                                                         4,
                                                         "",
                                                         Component.literal(""),
                                                         _level.getServer(),
                                                         null
                                                      )
                                                      .withSuppressedOutput(),
                                                   "/particle simple_mobs:burnt_flame ~ ~ ~ 2 3 2 0.1 100 normal"
                                                );
                                          }

                                          if (world instanceof Level _level) {
                                             if (_level.isClientSide()) {
                                                _level.playLocalSound(
                                                   x,
                                                   y,
                                                   z,
                                                   dev.deepnight.SafeSounds.resolve("entity.dragon_fireball.explode"),
                                                   SoundSource.NEUTRAL,
                                                   3.0F,
                                                   0.0F,
                                                   false
                                                );
                                             } else {
                                                _level.playSound(
                                                   null,
                                                   BlockPos.containing(x, y, z),
                                                   dev.deepnight.SafeSounds.resolve("entity.dragon_fireball.explode"),
                                                   SoundSource.NEUTRAL,
                                                   3.0F,
                                                   0.0F
                                                );
                                             }
                                          }

                                          if (world instanceof Level _level) {
                                             if (_level.isClientSide()) {
                                                _level.playLocalSound(
                                                   x,
                                                   y,
                                                   z,
                                                   dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammer"),
                                                   SoundSource.NEUTRAL,
                                                   3.0F,
                                                   1.0F,
                                                   false
                                                );
                                             } else {
                                                _level.playSound(
                                                   null,
                                                   BlockPos.containing(x, y, z),
                                                   dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammer"),
                                                   SoundSource.NEUTRAL,
                                                   3.0F,
                                                   1.0F
                                                );
                                             }
                                          }

                                          _center = new Vec3(x, y, z);

                                          for (Entity entityiterator : world.getEntitiesOfClass(
                                                Entity.class, new AABB(_center, _center).inflate(30.0), e -> true
                                             )
                                             .stream()
                                             .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                                             .collect(Collectors.toList())) {
                                             if (!(entityiterator instanceof FireGiantEntity) && entityiterator instanceof LivingEntity _entity) {
                                                _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 60, 1, false, false));
                                             }
                                          }

                                          _center = new Vec3(x, y, z);

                                          for (Entity entityiterator : world.getEntitiesOfClass(
                                                Entity.class, new AABB(_center, _center).inflate(7.0), e -> true
                                             )
                                             .stream()
                                             .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                                             .collect(Collectors.toList())) {
                                             if (!entity.getPersistentData().getBoolean("firegiant")) {
                                                if (entityiterator instanceof LivingEntity _entity) {
                                                   _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 60, 2, false, false));
                                                }

                                                SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                                   .setInitialPosition(entity.position())
                                                   .setStunType(StunType.LONG)
                                                   .setImpact(6.0F)
                                                   .setArmorNegation(30.0F);
                                                entityiterator.hurt((DamageSource)dmgSrc, 6.0F);
                                             }
                                          }

                                          for (int index9 = 0; index9 < 7; index9++) {
                                             if (world instanceof ServerLevel _level) {
                                                Entity entityToSpawn = new LavaBallEntity(SimpleMobsModEntities.LAVA_BALL.get(), _level);
                                                entityToSpawn.moveTo(
                                                   x + entity.getLookAngle().x * -2.5, y + 7.0, z + entity.getLookAngle().z * -2.5, 0.0F, 0.0F
                                                );
                                                entityToSpawn.setYBodyRot(0.0F);
                                                entityToSpawn.setYHeadRot(0.0F);
                                                entityToSpawn.setDeltaMovement(
                                                   Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
                                                   1.5 + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.3, 0.3),
                                                   Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0)
                                                );
                                                if (entityToSpawn instanceof Mob _mobToSpawn) {
                                                   _mobToSpawn.finalizeSpawn(
                                                      _level,
                                                      world.getCurrentDifficultyAt(entityToSpawn.blockPosition()),
                                                      MobSpawnType.MOB_SUMMONED,
                                                      null,
                                                      null
                                                   );
                                                }

                                                world.addFreshEntity(entityToSpawn);
                                             }
                                          }

                                          for (int index10 = 0; index10 < 3; index10++) {
                                             if (world instanceof ServerLevel _level) {
                                                Entity entityToSpawn = new LavaBallEntity(SimpleMobsModEntities.LAVA_BALL.get(), _level);
                                                entityToSpawn.moveTo(
                                                   x + entity.getLookAngle().x * -2.5, y + 7.0, z + entity.getLookAngle().z * -2.5, 0.0F, 0.0F
                                                );
                                                entityToSpawn.setYBodyRot(0.0F);
                                                entityToSpawn.setYHeadRot(0.0F);
                                                entityToSpawn.setDeltaMovement(
                                                   Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.2, 1.2),
                                                   1.7 + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.3, 0.3),
                                                   Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.2, 1.2)
                                                );
                                                if (entityToSpawn instanceof Mob _mobToSpawn) {
                                                   _mobToSpawn.finalizeSpawn(
                                                      _level,
                                                      world.getCurrentDifficultyAt(entityToSpawn.blockPosition()),
                                                      MobSpawnType.MOB_SUMMONED,
                                                      null,
                                                      null
                                                   );
                                                }

                                                world.addFreshEntity(entityToSpawn);
                                             }
                                          }

                                          for (int index11 = 0; index11 < 4; index11++) {
                                             if (world instanceof ServerLevel _level) {
                                                Entity entityToSpawn = new LavaBallEntity(SimpleMobsModEntities.LAVA_BALL.get(), _level);
                                                entityToSpawn.moveTo(
                                                   x + entity.getLookAngle().x * -2.5, y + 7.0, z + entity.getLookAngle().z * -2.5, 0.0F, 0.0F
                                                );
                                                entityToSpawn.setYBodyRot(0.0F);
                                                entityToSpawn.setYHeadRot(0.0F);
                                                entityToSpawn.setDeltaMovement(
                                                   Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.5, 1.5),
                                                   1.7 + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.3, 0.3),
                                                   Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.5, 1.5)
                                                );
                                                if (entityToSpawn instanceof Mob _mobToSpawn) {
                                                   _mobToSpawn.finalizeSpawn(
                                                      _level,
                                                      world.getCurrentDifficultyAt(entityToSpawn.blockPosition()),
                                                      MobSpawnType.MOB_SUMMONED,
                                                      null,
                                                      null
                                                   );
                                                }

                                                world.addFreshEntity(entityToSpawn);
                                             }
                                          }
                                       }
                                    } else {
                                       if (world instanceof ServerLevel _level) {
                                          _level.getServer()
                                             .getCommands()
                                             .performPrefixedCommand(
                                                new CommandSourceStack(
                                                      CommandSource.NULL,
                                                      new Vec3(x + entity.getLookAngle().x * -2.5, y + 11.0, z + entity.getLookAngle().z * -2.5),
                                                      Vec2.ZERO,
                                                      _level,
                                                      4,
                                                      "",
                                                      Component.literal(""),
                                                      _level.getServer(),
                                                      null
                                                   )
                                                   .withSuppressedOutput(),
                                                "/particle simple_mobs:flame ~ ~ ~ 2 3 2 0.1 50 normal"
                                             );
                                       }

                                       if (world instanceof ServerLevel _level) {
                                          _level.getServer()
                                             .getCommands()
                                             .performPrefixedCommand(
                                                new CommandSourceStack(
                                                      CommandSource.NULL,
                                                      new Vec3(x + entity.getLookAngle().x * -2.5, y + 11.0, z + entity.getLookAngle().z * -2.5),
                                                      Vec2.ZERO,
                                                      _level,
                                                      4,
                                                      "",
                                                      Component.literal(""),
                                                      _level.getServer(),
                                                      null
                                                   )
                                                   .withSuppressedOutput(),
                                                "/particle minecraft:flash ~ ~ ~ 2 2 2 0.1 20 normal"
                                             );
                                       }

                                       if (world instanceof ServerLevel _level) {
                                          _level.getServer()
                                             .getCommands()
                                             .performPrefixedCommand(
                                                new CommandSourceStack(
                                                      CommandSource.NULL,
                                                      new Vec3(x + entity.getLookAngle().x * -2.5, y + 14.0, z + entity.getLookAngle().z * -2.5),
                                                      Vec2.ZERO,
                                                      _level,
                                                      4,
                                                      "",
                                                      Component.literal(""),
                                                      _level.getServer(),
                                                      null
                                                   )
                                                   .withSuppressedOutput(),
                                                "/particle simple_mobs:burnt_flame ~ ~ ~ 2 2 2 0.1 100 normal"
                                             );
                                       }

                                       if (world instanceof Level _level) {
                                          if (_level.isClientSide()) {
                                             _level.playLocalSound(
                                                x,
                                                y,
                                                z,
                                                dev.deepnight.SafeSounds.resolve("entity.dragon_fireball.explode"),
                                                SoundSource.NEUTRAL,
                                                3.0F,
                                                0.0F,
                                                false
                                             );
                                          } else {
                                             _level.playSound(
                                                null,
                                                BlockPos.containing(x, y, z),
                                                dev.deepnight.SafeSounds.resolve("entity.dragon_fireball.explode"),
                                                SoundSource.NEUTRAL,
                                                3.0F,
                                                0.0F
                                             );
                                          }
                                       }

                                       if (world instanceof Level _level) {
                                          if (_level.isClientSide()) {
                                             _level.playLocalSound(
                                                x,
                                                y,
                                                z,
                                                dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammer"),
                                                SoundSource.NEUTRAL,
                                                3.0F,
                                                1.0F,
                                                false
                                             );
                                          } else {
                                             _level.playSound(
                                                null,
                                                BlockPos.containing(x, y, z),
                                                dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammer"),
                                                SoundSource.NEUTRAL,
                                                3.0F,
                                                1.0F
                                             );
                                          }
                                       }

                                       _center = new Vec3(x, y, z);

                                       for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(30.0), e -> true)
                                          .stream()
                                          .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                                          .collect(Collectors.toList())) {
                                          if (!entityiterator.getPersistentData().getBoolean("firegiant") && entityiterator instanceof LivingEntity _entity) {
                                             _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 60, 1, false, false));
                                          }
                                       }

                                       _center = new Vec3(x, y, z);

                                       for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.0), e -> true)
                                          .stream()
                                          .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                                          .collect(Collectors.toList())) {
                                          if (!entityiterator.getPersistentData().getBoolean("firegiant")) {
                                             if (entityiterator instanceof LivingEntity _entity) {
                                                _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 60, 1, false, false));
                                             }

                                             SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                                .setInitialPosition(entity.position())
                                                .setStunType(StunType.LONG)
                                                .setImpact(14.0F)
                                                .setArmorNegation(30.0F);
                                             entityiterator.hurt((DamageSource)dmgSrc, 6.0F);
                                          }
                                       }

                                       for (int index7 = 0; index7 < 7; index7++) {
                                          if (world instanceof ServerLevel _level) {
                                             Entity entityToSpawn = new LavaBallEntity(SimpleMobsModEntities.LAVA_BALL.get(), _level);
                                             entityToSpawn.moveTo(x + entity.getLookAngle().x * -2.5, y + 7.0, z + entity.getLookAngle().z * -2.5, 0.0F, 0.0F);
                                             entityToSpawn.setYBodyRot(0.0F);
                                             entityToSpawn.setYHeadRot(0.0F);
                                             entityToSpawn.setDeltaMovement(
                                                Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0),
                                                1.5 + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.3, 0.3),
                                                Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0)
                                             );
                                             if (entityToSpawn instanceof Mob _mobToSpawn) {
                                                _mobToSpawn.finalizeSpawn(
                                                   _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                                                );
                                             }

                                             world.addFreshEntity(entityToSpawn);
                                          }
                                       }

                                       for (int index8 = 0; index8 < 5; index8++) {
                                          if (world instanceof ServerLevel _level) {
                                             Entity entityToSpawn = new LavaBallEntity(SimpleMobsModEntities.LAVA_BALL.get(), _level);
                                             entityToSpawn.moveTo(x + entity.getLookAngle().x * -2.5, y + 7.0, z + entity.getLookAngle().z * -2.5, 0.0F, 0.0F);
                                             entityToSpawn.setYBodyRot(0.0F);
                                             entityToSpawn.setYHeadRot(0.0F);
                                             entityToSpawn.setDeltaMovement(
                                                Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.5, 1.5),
                                                1.7 + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.3, 0.3),
                                                Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.5, 1.5)
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
                                 } else {
                                    if (world instanceof Level _level) {
                                       if (_level.isClientSide()) {
                                          _level.playLocalSound(
                                             x,
                                             y,
                                             z,
                                             dev.deepnight.SafeSounds.resolve("item.totem.use"),
                                             SoundSource.NEUTRAL,
                                             2.0F,
                                             0.5F,
                                             false
                                          );
                                       } else {
                                          _level.playSound(
                                             null,
                                             BlockPos.containing(x, y, z),
                                             dev.deepnight.SafeSounds.resolve("item.totem.use"),
                                             SoundSource.NEUTRAL,
                                             2.0F,
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
                                             dev.deepnight.SafeSounds.resolve("entity.zombie_villager.cure"),
                                             SoundSource.NEUTRAL,
                                             2.0F,
                                             0.5F,
                                             false
                                          );
                                       } else {
                                          _level.playSound(
                                             null,
                                             BlockPos.containing(x, y, z),
                                             dev.deepnight.SafeSounds.resolve("entity.zombie_villager.cure"),
                                             SoundSource.NEUTRAL,
                                             2.0F,
                                             0.5F
                                          );
                                       }
                                    }
                                 }
                              } else if (world instanceof Level _level) {
                                 if (_level.isClientSide()) {
                                    _level.playLocalSound(
                                       x,
                                       y,
                                       z,
                                       dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.ambient2"),
                                       SoundSource.NEUTRAL,
                                       2.0F,
                                       1.0F,
                                       false
                                    );
                                 } else {
                                    _level.playSound(
                                       null,
                                       BlockPos.containing(x, y, z),
                                       dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.ambient2"),
                                       SoundSource.NEUTRAL,
                                       2.0F,
                                       1.0F
                                    );
                                 }
                              }
                           } else if (world instanceof Level _level) {
                              if (_level.isClientSide()) {
                                 _level.playLocalSound(
                                    x,
                                    y,
                                    z,
                                    dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.groan"),
                                    SoundSource.NEUTRAL,
                                    3.0F,
                                    1.0F,
                                    false
                                 );
                              } else {
                                 _level.playSound(
                                    null,
                                    BlockPos.containing(x, y, z),
                                    dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.groan"),
                                    SoundSource.NEUTRAL,
                                    3.0F,
                                    1.0F
                                 );
                              }
                           }
                        } else {
                           _center = new Vec3(x, y, z);

                           for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5.0), e -> true)
                              .stream()
                              .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                              .collect(Collectors.toList())) {
                              if (entityiterator instanceof MultipartbodyEntity) {
                                 if (!entityiterator.level().isClientSide()) {
                                    entityiterator.discard();
                                 }

                                 if (world instanceof ServerLevel _level) {
                                    Entity entityToSpawn = new Multipartbody2Entity(SimpleMobsModEntities.MULTIPARTBODY_2.get(), _level);
                                    entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
                                    if (entityToSpawn instanceof Mob _mobToSpawn) {
                                       _mobToSpawn.finalizeSpawn(
                                          _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                                       );
                                    }

                                    world.addFreshEntity(entityToSpawn);
                                 }
                              }
                           }

                           if (world instanceof Level _level) {
                              if (_level.isClientSide()) {
                                 _level.playLocalSound(
                                    x,
                                    y,
                                    z,
                                    dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                                    SoundSource.NEUTRAL,
                                    3.0F,
                                    0.0F,
                                    false
                                 );
                              } else {
                                 _level.playSound(
                                    null,
                                    BlockPos.containing(x, y, z),
                                    dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                                    SoundSource.NEUTRAL,
                                    3.0F,
                                    0.0F
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

                           _center = new Vec3(x, y, z);

                           for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(10.0), e -> true)
                              .stream()
                              .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                              .collect(Collectors.toList())) {
                              if (!entityiterator.getPersistentData().getBoolean("firegiant") && entityiterator instanceof LivingEntity _entity) {
                                 _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 50, 1, false, false));
                              }
                           }
                        }
                     } else if (world instanceof Level _level) {
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
                  } else if (world instanceof Level _level) {
                     if (_level.isClientSide()) {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.zombie.break_wooden_door"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F,
                           false
                        );
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.zombie.break_wooden_door"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     }
                  }
               } else {
                  entity.getPersistentData().putBoolean("Attacking", true);
                  Entity _entity = entity;
                  if (!(_entity instanceof Player _player)) {
                     if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.BLACK_WOOL));
                     }
                  } else {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.BLACK_WOOL));
                     _player.getInventory().setChanged();
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 467, 10, false, false));
                  }

                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator instanceof MultiPartLegEntity && !entityiterator.level().isClientSide()) {
                        entityiterator.discard();
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
                           "/data merge entity @e[type=deepnight:dawn_fire_giant,limit=1,sort=nearest] {NoAI:1b}"
                        );
                  }

                  if (world instanceof Level _level) {
                     if (_level.isClientSide()) {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.ambient"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F,
                           false
                        );
                     } else {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.ambient"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     }
                  }
               }
            }
         }
      }
   }
}
