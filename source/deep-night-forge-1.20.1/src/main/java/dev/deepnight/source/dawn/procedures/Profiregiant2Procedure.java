package dev.deepnight.source.dawn.procedures;


import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.AirEntity;
import dev.deepnight.source.dawn.entity.FireGiantEntity;
import dev.deepnight.source.dawn.entity.FlameBallEntity;
import dev.deepnight.source.dawn.entity.GrabbedEntity;
import dev.deepnight.source.dawn.entity.GroundbreakEffectBigEntity;
import dev.deepnight.source.dawn.entity.LavaBallEntity;
import dev.deepnight.source.dawn.entity.Multipartbody2Entity;
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

public class Profiregiant2Procedure {
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
               if (entity.getPersistentData().getDouble("hurt_cd") == 0.0) {
                  entity.getPersistentData().putDouble("hurt_cd", 15.0);
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

            if (!entity.getPersistentData().getBoolean("Attacking")
               && world.getEntitiesOfClass(Multipartbody2Entity.class, AABB.ofSize(new Vec3(x, y, z), 10.0, 10.0, 10.0), e -> true).isEmpty()
               && world instanceof ServerLevel _level) {
               Entity entityToSpawn = new Multipartbody2Entity(SimpleMobsModEntities.MULTIPARTBODY_2.get(), _level);
               entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
               if (entityToSpawn instanceof Mob _mobToSpawn) {
                  _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
               }

               world.addFreshEntity(entityToSpawn);
            }

            if (world instanceof ServerLevel _level) {
               _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.BURNT_FLAME.get(), x, y + 7.0, z, 1, 1.0, 1.0, 1.0, 0.1);
            }

            if (entity.isAlive()) {
               if (entity instanceof LivingEntity _entity) {
                  _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5, 10, false, false));
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
               if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0F)
                     < (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1.0F) * 0.5
                  && !entity.getPersistentData().getBoolean("Done")) {
                  entity.getPersistentData().putBoolean("Drop", true);
                  entity.getPersistentData().putBoolean("Done", true);
               }

               Vec3 _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(32.0), e -> true)
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
                           Math.toDegrees(
                                 Math.atan(
                                    Math.abs(entity.getY() + 7.0 - entityiterator.getY())
                                       / Math.hypot(Math.abs(entity.getX() - entityiterator.getX()), Math.abs(entity.getZ() - entityiterator.getZ()))
                                 )
                              )
                              * 0.8
                        );
                  }
               }

               _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(32.0), e -> true)
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

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(8.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                     entity.getPersistentData().putBoolean("Far", false);
                     entity.getPersistentData().putBoolean("Around", true);
                  }
               }

               _center = new Vec3(x + entity.getLookAngle().x * 13.0, y, z + entity.getLookAngle().z * 13.0);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                     entity.getPersistentData().putBoolean("Far", false);
                     entity.getPersistentData().putBoolean("Mid", true);
                     entity.getPersistentData().putBoolean("Close", false);
                     entity.getPersistentData().putBoolean("Around", false);
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
                  }
               }

               entity.getPersistentData().putBoolean("warn", false);
               _center = new Vec3(x, y, z);

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

               if (entity.getPersistentData().getDouble("atk2cd") == 0.0 && entity.getPersistentData().getBoolean("Attack2")) {
                  entity.getPersistentData().putDouble("atk2cd", 120.0);
               } else if (entity.getPersistentData().getDouble("atk2cd") > 0.0) {
                  entity.getPersistentData().putDouble("atk2cd", entity.getPersistentData().getDouble("atk2cd") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk3cd") == 0.0 && entity.getPersistentData().getBoolean("Attack3")) {
                  entity.getPersistentData().putDouble("atk3cd", 220.0);
               } else if (entity.getPersistentData().getDouble("atk3cd") > 0.0) {
                  entity.getPersistentData().putDouble("atk3cd", entity.getPersistentData().getDouble("atk3cd") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk4cd") == 0.0 && entity.getPersistentData().getBoolean("Attack4")) {
                  entity.getPersistentData().putDouble("atk4cd", 200.0);
               } else if (entity.getPersistentData().getDouble("atk4cd") > 0.0) {
                  entity.getPersistentData().putDouble("atk4cd", entity.getPersistentData().getDouble("atk4cd") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk5cd") == 0.0 && entity.getPersistentData().getBoolean("Attack5")) {
                  entity.getPersistentData().putDouble("atk5cd", 800 + Mth.nextInt(net.minecraft.util.RandomSource.create(), 0, 300));
               } else if (entity.getPersistentData().getDouble("atk5cd") > 0.0) {
                  entity.getPersistentData().putDouble("atk5cd", entity.getPersistentData().getDouble("atk5cd") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk6cd") == 0.0 && entity.getPersistentData().getBoolean("Attack6")) {
                  entity.getPersistentData().putDouble("atk6cd", 400.0);
               } else if (entity.getPersistentData().getDouble("atk6cd") > 0.0) {
                  entity.getPersistentData().putDouble("atk6cd", entity.getPersistentData().getDouble("atk6cd") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk7cd") == 1.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(30.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entity.getPersistentData().getBoolean("flametarget")) {
                        entityiterator.getPersistentData().putBoolean("flametarget", false);
                     }
                  }
               }

               if (entity.getPersistentData().getDouble("atk7cd") == 0.0 && entity.getPersistentData().getBoolean("Attack7")) {
                  entity.getPersistentData().putDouble("atk7cd", 200.0);
               } else if (entity.getPersistentData().getDouble("atk7cd") > 0.0) {
                  entity.getPersistentData().putDouble("atk7cd", entity.getPersistentData().getDouble("atk7cd") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk8cd") == 0.0 && entity.getPersistentData().getBoolean("Attack8")) {
                  entity.getPersistentData().putDouble("atk8cd", 300.0);
               } else if (entity.getPersistentData().getDouble("atk8cd") > 0.0) {
                  entity.getPersistentData().putDouble("atk8cd", entity.getPersistentData().getDouble("atk8cd") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk9cd") == 0.0 && entity.getPersistentData().getBoolean("Attack9")) {
                  entity.getPersistentData().putDouble("atk9cd", 340.0);
               } else if (entity.getPersistentData().getDouble("atk9cd") > 0.0) {
                  entity.getPersistentData().putDouble("atk9cd", entity.getPersistentData().getDouble("atk9cd") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk11cd") == 0.0 && entity.getPersistentData().getBoolean("Attack11")) {
                  entity.getPersistentData().putDouble("atk11cd", 200.0);
               } else if (entity.getPersistentData().getDouble("atk11cd") > 0.0) {
                  entity.getPersistentData().putDouble("atk11cd", entity.getPersistentData().getDouble("atk11cd") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk10cd") == 0.0 && entity.getPersistentData().getBoolean("Attack10")) {
                  entity.getPersistentData().putDouble("atk10cd", 260.0);
               } else if (entity.getPersistentData().getDouble("atk10cd") > 0.0) {
                  entity.getPersistentData().putDouble("atk10cd", entity.getPersistentData().getDouble("atk10cd") - 1.0);
               }

               if (entity.getPersistentData().getBoolean("Around")) {
                  if (Math.random() <= 0.4 && entity.getPersistentData().getDouble("atk11cd") == 0.0 && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     entity.getPersistentData().putBoolean("Attack11", true);
                  } else if (entity.getPersistentData().getDouble("atk4cd") == 0.0
                     && Math.random() >= 0.4
                     && Math.random() <= 0.8
                     && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     entity.getPersistentData().putBoolean("Attack4", true);
                  } else if (Math.random() > 0.8
                     && entity.getPersistentData().getDouble("atk5cd") == 0.0
                     && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("Attack5", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  }
               } else if (entity.getPersistentData().getBoolean("Close")) {
                  if (Math.random() <= 0.2 && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     entity.getPersistentData().putBoolean("Attack1", true);
                  } else if (Math.random() >= 0.21 && Math.random() <= 0.41 && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("Attack2", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (Math.random() >= 0.41
                     && Math.random() <= 0.61
                     && entity.getPersistentData().getDouble("atk10cd") == 0.0
                     && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("Attack10", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (entity.getPersistentData().getDouble("atk9cd") == 0.0 && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("Attack9", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  }
               } else if (entity.getPersistentData().getBoolean("Mid") && !entity.getPersistentData().getBoolean("Attacking")) {
                  if (entity.getPersistentData().getDouble("atk5cd") == 0.0 && Math.random() <= 0.33 && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     entity.getPersistentData().putBoolean("Attack5", true);
                  } else if (Math.random() > 0.66
                     && entity.getPersistentData().getDouble("atk6cd") == 0.0
                     && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("Attack6", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  }
               } else if (entity.getPersistentData().getBoolean("Far") && !entity.getPersistentData().getBoolean("Attacking")) {
                  if (entity.getPersistentData().getDouble("atk7cd") == 0.0 && Math.random() <= 0.5 && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     entity.getPersistentData().putBoolean("Attack7", true);
                  } else if (entity.getPersistentData().getDouble("atk8cd") == 0.0 && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("Attack8", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (entity.getPersistentData().getDouble("atk3cd") == 0.0 && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("Attack3", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack2")) {
                  if (entity.getPersistentData().getDouble("atk2") == 0.0) {
                     entity.getPersistentData().putDouble("atk2", 62.0);
                  } else {
                     entity.getPersistentData().putDouble("atk2", entity.getPersistentData().getDouble("atk2") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("atk2") == 62.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.GRAY_WOOL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.GRAY_WOOL));
                     }

                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.ambient"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F
                           );
                        } else {
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
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("atk2") == 41.0) {
                     entity.getPersistentData().putBoolean("noai", true);
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
                  } else if (entity.getPersistentData().getDouble("atk2") < 41.0 && entity.getPersistentData().getDouble("atk2") > 35.0) {
                     _center = new Vec3(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(8.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entity.getPersistentData().getBoolean("firegiant") && entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 30, 1));
                        }
                     }

                     _center = new Vec3(x + entity.getLookAngle().x, y, z + entity.getLookAngle().z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(6.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("firegiant")) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.LONG)
                              .setImpact(4.0F)
                              .setArmorNegation(30.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 35.0F);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("atk2") == 11.0) {
                     entity.getPersistentData().putBoolean("noai", false);
                  } else if (entity.getPersistentData().getDouble("atk2") == 1.0) {
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
               }

               if (entity.getPersistentData().getBoolean("Attack10")) {
                  if (entity.getPersistentData().getDouble("atk10") == 0.0) {
                     entity.getPersistentData().putDouble("atk10", 119.0);
                  } else {
                     entity.getPersistentData().putDouble("atk10", entity.getPersistentData().getDouble("atk10") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("atk10") == 119.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.SANDSTONE));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.SANDSTONE));
                     }

                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.ambient2"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              0.6F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.ambient2"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              0.6F,
                              false
                           );
                        }
                     }

                     entity.getPersistentData().putBoolean("focus", true);
                  } else if (entity.getPersistentData().getDouble("atk10") == 101.0) {
                     entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                  } else if (entity.getPersistentData().getDouble("atk10") == 100.0) {
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putBoolean("noai", true);
                  } else if (entity.getPersistentData().getDouble("atk10") == 92.0) {
                     for (int index3 = 0; index3 < 4; index3++) {
                        if (world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new RumbleEntity(SimpleMobsModEntities.RUMBLE.get(), _level);
                           entityToSpawn.moveTo(
                              x + entity.getLookAngle().x * 4.0 + Mth.nextInt(net.minecraft.util.RandomSource.create(), -2, 2),
                              y,
                              z + entity.getLookAngle().z * 4.0 + Mth.nextInt(net.minecraft.util.RandomSource.create(), -2, 2),
                              entity.getYRot(),
                              entity.getXRot()
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

                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new GroundbreakEffectBigEntity(SimpleMobsModEntities.GROUNDBREAK_EFFECT_BIG.get(), _level);
                        entityToSpawn.moveTo(
                           x + entity.getLookAngle().x * 4.0, y, z + entity.getLookAngle().z * 4.0, world.getRandom().nextFloat() * 360.0F, 0.0F
                        );
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
                              dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              0.4F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              0.4F,
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

                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(
                           ParticleTypes.CAMPFIRE_COSY_SMOKE, x + entity.getLookAngle().x * 4.0, y, z + entity.getLookAngle().z * 4.0, 30, 3.0, 3.0, 3.0, 1.0
                        );
                     }
                  } else if (entity.getPersistentData().getDouble("atk10") < 92.0 && entity.getPersistentData().getDouble("atk10") > 87.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 3.0, y, z + entity.getLookAngle().z * 3.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(12.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entity.getPersistentData().getBoolean("firegiant") && entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 40, 1));
                        }
                     }

                     _center = new Vec3(x + entity.getLookAngle().x * 3.0, y, z + entity.getLookAngle().z * 3.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("firegiant")) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.LONG)
                              .setImpact(4.0F)
                              .setArmorNegation(30.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 26.0F);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("atk10") == 65.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.fire"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.fire"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F,
                              false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("atk10") == 60.0) {
                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(ParticleTypes.FLASH, x, y + 1.0, z, 20, 5.0, 5.0, 5.0, 0.0);
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.FLAME.get(), x, y + 1.0, z, 100, 6.0, 3.0, 6.0, 1.0);
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.FLAME.get(), x, y + 1.0, z, 100, 6.0, 2.0, 6.0, 1.0);
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.BURNT_FLAME.get(), x, y + 1.0, z, 100, 7.0, 2.0, 7.0, 1.0);
                     }
                  } else if (entity.getPersistentData().getDouble("atk10") < 60.0 && entity.getPersistentData().getDouble("atk10") > 55.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * -4.0, y, z + entity.getLookAngle().z * -4.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(10.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("firegiant")) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.LONG)
                              .setImpact(4.0F)
                              .setArmorNegation(30.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 40.0F);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("atk10") == 15.0) {
                     entity.getPersistentData().putBoolean("noai", false);
                  } else if (entity.getPersistentData().getDouble("atk10") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putDouble("atk10", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Attack10", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack1")) {
                  if (entity.getPersistentData().getDouble("Atk1") <= 0.0) {
                     entity.getPersistentData().putDouble("Atk1", 81.0);
                  } else {
                     entity.getPersistentData().putDouble("Atk1", entity.getPersistentData().getDouble("Atk1") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Atk1") == 81.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.BROWN_WOOL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.BROWN_WOOL));
                     }

                     entity.getPersistentData().putBoolean("focus", true);
                  } else if (entity.getPersistentData().getDouble("Atk1") == 77.0) {
                     entity.getPersistentData().putDouble("i", 12.0);
                  } else if (entity.getPersistentData().getDouble("Atk1") == 76.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.sweep"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x + entity.getLookAngle().x * 2.0,
                              y,
                              z + entity.getLookAngle().z * 2.0,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.sweep"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F,
                              false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk1") == 55.0) {
                     entity.getPersistentData().putBoolean("focus", false);
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
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:cave_golem.hurt"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        } else {
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
                        }
                     }

                     entity.getPersistentData().putBoolean("sweep", true);
                  } else if (entity.getPersistentData().getDouble("Atk1") == 41.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(12.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null) && Math.random() > 0.5) {
                           entity.getPersistentData().putBoolean("Attack1b", true);
                           entity.getPersistentData().putDouble("Atk1", 0.0);
                           entity.getPersistentData().putBoolean("Attack1", false);
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

                     entity.getPersistentData().putBoolean("noai", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("Atk1", 0.0);
                     entity.getPersistentData().putBoolean("Attack1", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack1b")) {
                  if (entity.getPersistentData().getDouble("Atk1b") <= 0.0) {
                     entity.getPersistentData().putDouble("Atk1b", 62.0);
                  } else {
                     entity.getPersistentData().putDouble("Atk1b", entity.getPersistentData().getDouble("Atk1b") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Atk1b") == 62.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.LIME_WOOL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.LIME_WOOL));
                     }

                     entity.getPersistentData().putBoolean("focus", true);
                  } else if (entity.getPersistentData().getDouble("Atk1b") == 60.0) {
                     entity.getPersistentData().putDouble("i", 0.0);
                  } else if (entity.getPersistentData().getDouble("Atk1b") == 53.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.sweep"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x + entity.getLookAngle().x * 2.0,
                              y,
                              z + entity.getLookAngle().z * 2.0,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.sweep"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F,
                              false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk1b") == 40.0) {
                     entity.getPersistentData().putBoolean("noai", true);
                     entity.getPersistentData().putBoolean("focus", false);
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:cave_golem.hurt"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        } else {
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
                        }
                     }

                     entity.getPersistentData().putBoolean("1sweep", true);
                  } else if (entity.getPersistentData().getDouble("Atk1b") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("noai", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("Atk1b", 0.0);
                     entity.getPersistentData().putBoolean("Attack1b", false);
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

                     if (entity.getPersistentData().getDouble("nowdirection") > 25.0) {
                        entity.getPersistentData().putDouble("nowdirection", entity.getPersistentData().getDouble("nowdirection") - 25.0);
                     } else if (entity.getPersistentData().getDouble("nowdirection") < 0.0) {
                        entity.getPersistentData().putDouble("nowdirection", entity.getPersistentData().getDouble("nowdirection") + 25.0);
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
                                          (entityiterator instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem(), 100
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
                                          (entityiterator instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem(), 100
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
                                 .setImpact(4.0F)
                                 .setArmorNegation(30.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 26.0F);
                              entityiterator.setDeltaMovement(
                                 new Vec3(
                                    Math.sin(Math.toRadians(entity.getYRot() + 90.0F)) * 3.0, 0.7, Math.cos(Math.toRadians(entity.getYRot() + 240.0F)) * 3.0
                                 )
                              );
                           }
                        }
                     }

                     entity.getPersistentData().putDouble("i", entity.getPersistentData().getDouble("i") - 1.0);
                  }
               }

               if (entity.getPersistentData().getBoolean("1sweep")) {
                  if (!(entity.getPersistentData().getDouble("i") < 12.0)) {
                     entity.getPersistentData().putBoolean("1sweep", false);
                  } else {
                     entity.getPersistentData().putDouble("direction", Math.abs(entity.getYRot()) / 360.0F * 26.0F);
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
                                          (entityiterator instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem(), 100
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
                                          (entityiterator instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem(), 100
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
                                 .setImpact(4.0F)
                                 .setArmorNegation(30.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 26.0F);
                           }
                        }
                     }

                     entity.getPersistentData().putDouble("i", entity.getPersistentData().getDouble("i") + 1.0);
                  }
               }

               if (!entity.getPersistentData().getBoolean("Aggro") && !entity.getPersistentData().getBoolean("Attacking")) {
                  if (entity.getPersistentData().getDouble("idletimer") == 0.0) {
                     entity.getPersistentData().putDouble("idletimer", 200.0);
                  } else {
                     entity.getPersistentData().putDouble("idletimer", entity.getPersistentData().getDouble("idletimer") - 1.0);
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

               if (entity.getPersistentData().getBoolean("Attack3")) {
                  if (entity.getPersistentData().getDouble("Atk3") <= 0.0) {
                     entity.getPersistentData().putDouble("Atk3", 120.0);
                  } else {
                     entity.getPersistentData().putDouble("Atk3", entity.getPersistentData().getDouble("Atk3") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Atk3") == 120.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.BLACK_CARPET));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.BLACK_CARPET));
                     }

                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.ambient2"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F
                           );
                        } else {
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
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk3") == 103.0) {
                     entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                     entity.getPersistentData().putDouble("y", entity.getXRot());
                     entity.getPersistentData().putBoolean("lock", true);
                     entity.getPersistentData().putBoolean("bb", true);
                     entity.getPersistentData().putBoolean("noai", true);
                  } else if (entity.getPersistentData().getDouble("Atk3") == 102.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant_roll"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant_roll"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F,
                              false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk3") <= 96.0 && entity.getPersistentData().getDouble("Atk3") >= 72.0) {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(11.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("firegiant") && entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 50, 1));
                        }
                     }

                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("firegiant")) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.LONG)
                              .setImpact(0.5F)
                              .setArmorNegation(10.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 14.0F);
                           entityiterator.setDeltaMovement(
                              new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 90.0F)) * 3.0, 0.7, Math.cos(Math.toRadians(entity.getYRot() + 270.0F)) * 3.0)
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk3") == 69.0) {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("firegiant")) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.LONG)
                              .setImpact(8.0F)
                              .setArmorNegation(30.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 24.0F);
                        }
                     }

                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                              SoundSource.NEUTRAL,
                              2.0F,
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
                              2.0F,
                              1.0F
                           );
                        } else {
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
                        }
                     }

                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new RumbleEntity(SimpleMobsModEntities.RUMBLE.get(), _level);
                        entityToSpawn.moveTo(
                           x + entity.getLookAngle().x * 5.0, y, z + entity.getLookAngle().z * 5.0, world.getRandom().nextFloat() * 360.0F, 0.0F
                        );
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  } else if (entity.getPersistentData().getDouble("Atk3") == 21.0) {
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putBoolean("noai", false);
                     entity.getPersistentData().putBoolean("bb", false);
                  } else if (entity.getPersistentData().getDouble("Atk3") == 2.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.POPPY));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.POPPY));
                     }
                  } else if (entity.getPersistentData().getDouble("Atk3") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("Atk3", 0.0);
                     entity.getPersistentData().putBoolean("Attack3", false);
                  }

                  if (entity.getPersistentData().getDouble("Atk3") <= 100.0 && entity.getPersistentData().getDouble("Atk3") >= 70.0) {
                     entity.setDeltaMovement(
                        new Vec3(
                           Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 0.45,
                           entity.getDeltaMovement().y(),
                           Math.cos(Math.toRadians(entity.getYRot())) * 0.45
                        )
                     );
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack4")) {
                  if (entity.getPersistentData().getDouble("Atk4") <= 0.0) {
                     entity.getPersistentData().putDouble("Atk4", 101.0);
                  } else {
                     entity.getPersistentData().putDouble("Atk4", entity.getPersistentData().getDouble("Atk4") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Atk4") == 101.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.WHITE_CARPET));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.WHITE_CARPET));
                     }

                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.ambient2"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F
                           );
                        } else {
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
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk4") == 87.0) {
                     entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                     entity.getPersistentData().putDouble("y", entity.getXRot());
                     entity.getPersistentData().putBoolean("lock", true);
                     entity.getPersistentData().putBoolean("bb", true);
                     entity.getPersistentData().putBoolean("noai", true);
                  } else if (entity.getPersistentData().getDouble("Atk4") == 86.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant_roll"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant_roll"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F,
                              false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk4") < 86.0 && entity.getPersistentData().getDouble("Atk4") >= 53.0) {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(10.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("firegiant") && entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 50, 1));
                        }
                     }

                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("firegiant")) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.LONG)
                              .setImpact(0.5F)
                              .setArmorNegation(10.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 16.0F);
                           entityiterator.setDeltaMovement(
                              new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 90.0F)) * 3.0, 0.7, Math.cos(Math.toRadians(entity.getYRot() + 270.0F)) * 3.0)
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk4") == 50.0) {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("firegiant")) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.LONG)
                              .setImpact(12.0F)
                              .setArmorNegation(30.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 28.0F);
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
                        Entity entityToSpawn = new RumbleEntity(SimpleMobsModEntities.RUMBLE.get(), _level);
                        entityToSpawn.moveTo(
                           x + entity.getLookAngle().x * 5.0, y, z + entity.getLookAngle().z * 5.0, world.getRandom().nextFloat() * 360.0F, 0.0F
                        );
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  } else if (entity.getPersistentData().getDouble("Atk4") == 21.0) {
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putBoolean("noai", false);
                     entity.getPersistentData().putBoolean("bb", false);
                  } else if (entity.getPersistentData().getDouble("Atk4") == 2.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.POPPY));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.POPPY));
                     }
                  } else if (entity.getPersistentData().getDouble("Atk4") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("Atk4", 0.0);
                     entity.getPersistentData().putBoolean("Attack4", false);
                  }

                  if (entity.getPersistentData().getDouble("Atk4") <= 85.0 && entity.getPersistentData().getDouble("Atk4") >= 58.0) {
                     entity.setDeltaMovement(
                        new Vec3(
                           Math.sin(Math.toRadians(entity.getYRot() + 90.0F)) * 0.45,
                           entity.getDeltaMovement().y(),
                           Math.cos(Math.toRadians(entity.getYRot() + 270.0F)) * 0.45
                        )
                     );
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack6")) {
                  if (entity.getPersistentData().getDouble("Atk6") <= 0.0) {
                     entity.getPersistentData().putDouble("Atk6", 82.0);
                  } else {
                     entity.getPersistentData().putDouble("Atk6", entity.getPersistentData().getDouble("Atk6") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Atk6") == 82.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.LILAC));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.LILAC));
                     }
                  } else if (entity.getPersistentData().getDouble("Atk6") == 70.0) {
                     entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                  } else if (entity.getPersistentData().getDouble("Atk6") == 69.0) {
                     entity.getPersistentData().putBoolean("lock", true);
                  } else if (entity.getPersistentData().getDouble("Atk6") == 54.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("entity.dragon_fireball.explode"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              0.0F
                           );
                        } else {
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
                        Entity entityToSpawn = new RumbleEntity(SimpleMobsModEntities.RUMBLE.get(), _level);
                        entityToSpawn.moveTo(x + entity.getLookAngle().x * 15.0, y, z + entity.getLookAngle().z * 15.0, entity.getYRot(), 0.0F);
                        entityToSpawn.setYBodyRot(entity.getYRot());
                        entityToSpawn.setYHeadRot(entity.getYRot());
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }

                     entity.getPersistentData().putBoolean("noai", true);
                  } else if (entity.getPersistentData().getDouble("Atk6") <= 53.0 && entity.getPersistentData().getDouble("Atk6") >= 48.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 15.0, y, z + entity.getLookAngle().z * 15.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("firegiant") && entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 40, 1));
                        }
                     }

                     _center = new Vec3(x + entity.getLookAngle().x * 15.0, y, z + entity.getLookAngle().z * 15.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("firegiant")) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.KNOCKDOWN)
                              .setImpact(4.0F)
                              .setArmorNegation(30.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 34.0F);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk6") == 21.0) {
                     entity.getPersistentData().putBoolean("noai", false);
                  } else if (entity.getPersistentData().getDouble("Atk6") == 1.0) {
                     entity.getPersistentData().putBoolean("lock", false);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("Atk6", 0.0);
                     entity.getPersistentData().putBoolean("Attack6", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack7")) {
                  if (entity.getPersistentData().getDouble("Atk7") <= 0.0) {
                     entity.getPersistentData().putDouble("Atk7", 132.0);
                  } else {
                     entity.getPersistentData().putDouble("Atk7", entity.getPersistentData().getDouble("Atk7") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Atk7") == 132.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.LILY_OF_THE_VALLEY));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.LILY_OF_THE_VALLEY));
                     }
                  } else if (entity.getPersistentData().getDouble("Atk7") == 130.0) {
                     entity.getPersistentData().putBoolean("focus", true);
                  } else if (entity.getPersistentData().getDouble("Atk7") == 45.0) {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(30.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                           entityiterator.getPersistentData().putBoolean("flametarget", true);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk7") == 40.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("item.firecharge.use"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              0.5F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("item.firecharge.use"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              0.5F,
                              false
                           );
                        }
                     }

                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new FlameBallEntity(SimpleMobsModEntities.FLAME_BALL.get(), _level);
                        entityToSpawn.moveTo(x + entity.getLookAngle().x * 7.0, y + 5.0, z + entity.getLookAngle().z * 7.0, 0.0F, 0.0F);
                        entityToSpawn.setYBodyRot(0.0F);
                        entityToSpawn.setYHeadRot(0.0F);
                        entityToSpawn.setDeltaMovement(0.0, 0.5, 0.0);
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  } else if (entity.getPersistentData().getDouble("Atk7") == 21.0) {
                     entity.getPersistentData().putBoolean("focus", false);
                  } else if (entity.getPersistentData().getDouble("Atk7") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("Atk7", 0.0);
                     entity.getPersistentData().putBoolean("Attack7", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack9")) {
                  if (entity.getPersistentData().getDouble("Atk9") <= 0.0) {
                     entity.getPersistentData().putDouble("Atk9", 85.0);
                  } else {
                     entity.getPersistentData().putDouble("Atk9", entity.getPersistentData().getDouble("Atk9") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Atk9") < 67.0 && entity.getPersistentData().getDouble("Atk9") > 50.0) {
                     entity.setDeltaMovement(
                        new Vec3(Math.sin(Math.toRadians(entity.getYRot())) * 0.2, 0.0, Math.cos(Math.toRadians(entity.getYRot() + 180.0F)) * 0.2)
                     );
                  }

                  if (entity.getPersistentData().getDouble("Atk9") == 85.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.GLASS));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.GLASS));
                     }

                     entity.getPersistentData().putBoolean("focus", true);
                  } else if (entity.getPersistentData().getDouble("Atk9") == 55.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.sweep"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F
                           );
                        } else {
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
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk9") == 38.0) {
                     entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                  } else if (entity.getPersistentData().getDouble("Atk9") == 37.0) {
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
                              "/data merge entity @e[type=deepnight:dawn_fire_giant,limit=1,sort=nearest] {NoAI:1b}"
                           );
                     }
                  } else if (entity.getPersistentData().getDouble("Atk9") == 35.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 6.0, y, z + entity.getLookAngle().z * 6.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                           if (world instanceof Level _level) {
                              if (!_level.isClientSide()) {
                                 _level.playSound(
                                    null,
                                    BlockPos.containing(x, y, z),
                                    dev.deepnight.SafeSounds.resolve("entity.turtle.egg_crack"),
                                    SoundSource.NEUTRAL,
                                    1.0F,
                                    1.0F
                                 );
                              } else {
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
                              }
                           }

                           if (world instanceof Level _level) {
                              if (!_level.isClientSide()) {
                                 _level.playSound(
                                    null,
                                    BlockPos.containing(x, y, z),
                                    dev.deepnight.SafeSounds.resolve("item.axe.strip"),
                                    SoundSource.NEUTRAL,
                                    1.0F,
                                    1.0F
                                 );
                              } else {
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
                              }
                           }

                           Entity _ent = entityiterator;
                           _ent.teleportTo(x + entity.getLookAngle().x * 12.0, y + 2.0, z + entity.getLookAngle().z * 12.0);
                           if (_ent instanceof ServerPlayer _serverPlayer) {
                              _serverPlayer.connection
                                 .teleport(x + entity.getLookAngle().x * 12.0, y + 2.0, z + entity.getLookAngle().z * 12.0, _ent.getYRot(), _ent.getXRot());
                           }

                           if (world instanceof ServerLevel _level) {
                              Entity entityToSpawn = new GrabbedEntity(SimpleMobsModEntities.GRABBED.get(), _level);
                              entityToSpawn.moveTo(x + entity.getLookAngle().x * 12.0, y + 2.0, z + entity.getLookAngle().z * 12.0, 0.0F, 0.0F);
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
                           entity.getPersistentData().putBoolean("Attack9G", true);
                           entity.getPersistentData().putDouble("Atk9", 0.0);
                           entity.getPersistentData().putBoolean("Attack9", false);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk9") == 10.0) {
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

                     entity.getPersistentData().putBoolean("lock", false);
                  } else if (entity.getPersistentData().getDouble("Atk9") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("Atk9", 0.0);
                     entity.getPersistentData().putBoolean("Attack9", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack9G")) {
                  if (entity.getPersistentData().getDouble("Atk9g") <= 0.0) {
                     entity.getPersistentData().putDouble("Atk9g", 81.0);
                  } else {
                     entity.getPersistentData().putDouble("Atk9g", entity.getPersistentData().getDouble("Atk9g") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Atk9g") == 81.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.SAND));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.SAND));
                     }
                  } else if (entity.getPersistentData().getDouble("Atk9g") == 81.0) {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(15.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator instanceof GrabbedEntity) {
                           Entity _ent = entityiterator;
                           _ent.teleportTo(x + entity.getLookAngle().x * 12.0, y + 5.0, z + entity.getLookAngle().z * 12.0);
                           if (_ent instanceof ServerPlayer _serverPlayer) {
                              _serverPlayer.connection
                                 .teleport(x + entity.getLookAngle().x * 12.0, y + 5.0, z + entity.getLookAngle().z * 12.0, _ent.getYRot(), _ent.getXRot());
                           }
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk9g") == 74.0) {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(15.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator instanceof GrabbedEntity) {
                           Entity _ent = entityiterator;
                           _ent.teleportTo(x + entity.getLookAngle().x * 11.0, y + 6.0, z + entity.getLookAngle().z * 11.0);
                           if (_ent instanceof ServerPlayer _serverPlayer) {
                              _serverPlayer.connection
                                 .teleport(x + entity.getLookAngle().x * 11.0, y + 6.0, z + entity.getLookAngle().z * 11.0, _ent.getYRot(), _ent.getXRot());
                           }
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk9g") == 69.0) {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(15.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator instanceof GrabbedEntity) {
                           Entity _ent = entityiterator;
                           _ent.teleportTo(x + entity.getLookAngle().x * 10.0, y + 6.0, z + entity.getLookAngle().z * 10.0);
                           if (_ent instanceof ServerPlayer _serverPlayer) {
                              _serverPlayer.connection
                                 .teleport(x + entity.getLookAngle().x * 10.0, y + 6.0, z + entity.getLookAngle().z * 10.0, _ent.getYRot(), _ent.getXRot());
                           }
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk9g") == 64.0) {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(15.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator instanceof GrabbedEntity) {
                           Entity _ent = entityiterator;
                           _ent.teleportTo(x + entity.getLookAngle().x * 10.0, y + 7.0, z + entity.getLookAngle().z * 10.0);
                           if (_ent instanceof ServerPlayer _serverPlayer) {
                              _serverPlayer.connection
                                 .teleport(x + entity.getLookAngle().x * 10.0, y + 7.0, z + entity.getLookAngle().z * 10.0, _ent.getYRot(), _ent.getXRot());
                           }
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk9g") == 59.0) {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(15.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator instanceof GrabbedEntity) {
                           Entity _ent = entityiterator;
                           _ent.teleportTo(x + entity.getLookAngle().x * 9.0, y + 8.0, z + entity.getLookAngle().z * 10.0);
                           if (_ent instanceof ServerPlayer _serverPlayer) {
                              _serverPlayer.connection
                                 .teleport(x + entity.getLookAngle().x * 9.0, y + 8.0, z + entity.getLookAngle().z * 10.0, _ent.getYRot(), _ent.getXRot());
                           }
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk9g") == 51.0) {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(15.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator.getPersistentData().getBoolean("grabbed")) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.LONG)
                              .setImpact(6.0F)
                              .setArmorNegation(30.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 30.0F);
                           if (world instanceof Level _level) {
                              if (!_level.isClientSide()) {
                                 _level.playSound(
                                    null,
                                    BlockPos.containing(x, y, z),
                                    dev.deepnight.SafeSounds.resolve("entity.generic.eat"),
                                    SoundSource.NEUTRAL,
                                    2.0F,
                                    0.0F
                                 );
                              } else {
                                 _level.playLocalSound(
                                    x,
                                    y,
                                    z,
                                    dev.deepnight.SafeSounds.resolve("entity.generic.eat"),
                                    SoundSource.NEUTRAL,
                                    2.0F,
                                    0.0F,
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
                                    2.0F,
                                    0.0F
                                 );
                              } else {
                                 _level.playLocalSound(
                                    x,
                                    y,
                                    z,
                                    dev.deepnight.SafeSounds.resolve("simple_mobs:attack1"),
                                    SoundSource.NEUTRAL,
                                    2.0F,
                                    0.0F,
                                    false
                                 );
                              }
                           }

                           entityiterator.getPersistentData().putBoolean("grabbed", false);
                        }

                        if (entityiterator instanceof GrabbedEntity && !entityiterator.level().isClientSide()) {
                           entityiterator.discard();
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk9g") == 20.0) {
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
                  } else if (entity.getPersistentData().getDouble("Atk9g") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("Atk9g", 0.0);
                     entity.getPersistentData().putBoolean("Attack9G", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Attack8")) {
                  if (entity.getPersistentData().getDouble("Atk8") <= 0.0) {
                     entity.getPersistentData().putDouble("Atk8", 82.0);
                  } else {
                     entity.getPersistentData().putDouble("Atk8", entity.getPersistentData().getDouble("Atk8") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Atk8") == 82.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.LILY_PAD));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.LILY_PAD));
                     }

                     entity.getPersistentData().putBoolean("focus", true);
                  } else if (entity.getPersistentData().getDouble("Atk8") == 57.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.shoot"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:fire_giant.shoot"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F,
                              false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk8") <= 56.0 && entity.getPersistentData().getDouble("Atk8") >= 22.0) {
                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new Projectile2Entity(SimpleMobsModEntities.PROJECTILE_2.get(), _level);
                        entityToSpawn.moveTo(
                           x + entity.getLookAngle().x * 1.0,
                           y + 7.0,
                           z + entity.getLookAngle().z * 1.0,
                           (float)entity.getPersistentData().getDouble("yaw2"),
                           (float)entity.getPersistentData().getDouble("pitch2")
                        );
                        entityToSpawn.setYBodyRot((float)entity.getPersistentData().getDouble("yaw2"));
                        entityToSpawn.setYHeadRot((float)entity.getPersistentData().getDouble("yaw2"));
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
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

               if (entity.getPersistentData().getBoolean("Attack5")) {
                  if (entity.getPersistentData().getDouble("Atk5") <= 0.0) {
                     entity.getPersistentData().putDouble("Atk5", 187.0);
                  } else {
                     entity.getPersistentData().putDouble("Atk5", entity.getPersistentData().getDouble("Atk5") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Atk5") == 187.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.YELLOW_CARPET));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.YELLOW_CARPET));
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
                  } else if (entity.getPersistentData().getDouble("Atk5") == 125.0) {
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
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("entity.dragon_fireball.explode"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              0.0F
                           );
                        } else {
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
                        }
                     }

                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammer"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        } else {
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
                        }
                     }

                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(30.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("firegiant") && entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 50, 1, false, false));
                        }
                     }

                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("firegiant")) {
                           if (entityiterator instanceof LivingEntity _entity) {
                              _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 50, 2, false, false));
                           }

                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.LONG)
                              .setImpact(4.0F)
                              .setArmorNegation(30.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 6.0F);
                        }
                     }

                     for (int index4 = 0; index4 < 7; index4++) {
                        if (world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new LavaBallEntity(SimpleMobsModEntities.LAVA_BALL.get(), _level);
                           entityToSpawn.moveTo(x + entity.getLookAngle().x * -2.5, y + 7.0, z + entity.getLookAngle().z * -2.5, 0.0F, 0.0F);
                           entityToSpawn.setYBodyRot(0.0F);
                           entityToSpawn.setYHeadRot(0.0F);
                           entityToSpawn.setDeltaMovement(
                              Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0), 1.5 + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.3, 0.3), Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0)
                           );
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }
                     }

                     for (int index5 = 0; index5 < 5; index5++) {
                        if (world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new LavaBallEntity(SimpleMobsModEntities.LAVA_BALL.get(), _level);
                           entityToSpawn.moveTo(x + entity.getLookAngle().x * -2.5, y + 7.0, z + entity.getLookAngle().z * -2.5, 0.0F, 0.0F);
                           entityToSpawn.setYBodyRot(0.0F);
                           entityToSpawn.setYHeadRot(0.0F);
                           entityToSpawn.setDeltaMovement(
                              Mth.nextDouble(net.minecraft.util.RandomSource.create(), -2.0, 2.0), 1.8 + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.3, 0.3), Mth.nextDouble(net.minecraft.util.RandomSource.create(), -2.0, 2.0)
                           );
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk5") == 98.0) {
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
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("entity.dragon_fireball.explode"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              0.0F
                           );
                        } else {
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
                        }
                     }

                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammer"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        } else {
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
                        }
                     }

                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(30.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof FireGiantEntity) && entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 50, 1, false, false));
                        }
                     }

                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entity.getPersistentData().getBoolean("firegiant") && entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 50, 2, false, false));
                        }
                     }

                     for (int index6 = 0; index6 < 7; index6++) {
                        if (world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new LavaBallEntity(SimpleMobsModEntities.LAVA_BALL.get(), _level);
                           entityToSpawn.moveTo(x + entity.getLookAngle().x * -2.5, y + 7.0, z + entity.getLookAngle().z * -2.5, 0.0F, 0.0F);
                           entityToSpawn.setYBodyRot(0.0F);
                           entityToSpawn.setYHeadRot(0.0F);
                           entityToSpawn.setDeltaMovement(
                              Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0), 1.5 + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.3, 0.3), Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0)
                           );
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }
                     }

                     for (int index7 = 0; index7 < 7; index7++) {
                        if (world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new LavaBallEntity(SimpleMobsModEntities.LAVA_BALL.get(), _level);
                           entityToSpawn.moveTo(x + entity.getLookAngle().x * -2.5, y + 7.0, z + entity.getLookAngle().z * -2.5, 0.0F, 0.0F);
                           entityToSpawn.setYBodyRot(0.0F);
                           entityToSpawn.setYHeadRot(0.0F);
                           entityToSpawn.setDeltaMovement(
                              Mth.nextDouble(net.minecraft.util.RandomSource.create(), -2.0, 2.0), 1.8 + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.3, 0.3), Mth.nextDouble(net.minecraft.util.RandomSource.create(), -2.0, 2.0)
                           );
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk5") != 72.0) {
                     if (entity.getPersistentData().getDouble("Atk5") == 20.0) {
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
                     } else if (entity.getPersistentData().getDouble("Atk5") == 10.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(2, new ItemStack(Items.LEATHER_CHESTPLATE));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
                        }
                     } else if (entity.getPersistentData().getDouble("Atk5") == 1.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                        }

                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putDouble("Atk5", 0.0);
                        entity.getPersistentData().putBoolean("Attack5", false);
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
                              "/particle minecraft:flash ~ ~ ~ 3 3 3 0.1 50 normal"
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
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_hammer"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        } else {
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
                              dev.deepnight.SafeSounds.resolve("entity.dragon_fireball.explode"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              0.0F
                           );
                        } else {
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
                        }
                     }

                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(30.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof FireGiantEntity) && entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 50, 1, false, false));
                        }
                     }

                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entity.getPersistentData().getBoolean("firegiant") && entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 50, 2, false, false));
                        }
                     }

                     for (int index8 = 0; index8 < 10; index8++) {
                        if (world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new LavaBallEntity(SimpleMobsModEntities.LAVA_BALL.get(), _level);
                           entityToSpawn.moveTo(x + entity.getLookAngle().x * -2.5, y + 7.0, z + entity.getLookAngle().z * -2.5, 0.0F, 0.0F);
                           entityToSpawn.setYBodyRot(0.0F);
                           entityToSpawn.setYHeadRot(0.0F);
                           entityToSpawn.setDeltaMovement(
                              Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0), 1.5 + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.3, 0.3), Mth.nextDouble(net.minecraft.util.RandomSource.create(), -1.0, 1.0)
                           );
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }
                     }

                     for (int index9 = 0; index9 < 15; index9++) {
                        if (world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new LavaBallEntity(SimpleMobsModEntities.LAVA_BALL.get(), _level);
                           entityToSpawn.moveTo(x + entity.getLookAngle().x * -2.5, y + 7.0, z + entity.getLookAngle().z * -2.5, 0.0F, 0.0F);
                           entityToSpawn.setYBodyRot(0.0F);
                           entityToSpawn.setYHeadRot(0.0F);
                           entityToSpawn.setDeltaMovement(
                              Mth.nextDouble(net.minecraft.util.RandomSource.create(), -2.0, 2.0), 1.8 + Mth.nextDouble(net.minecraft.util.RandomSource.create(), -0.3, 0.3), Mth.nextDouble(net.minecraft.util.RandomSource.create(), -2.0, 2.0)
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
               }

               if (entity.getPersistentData().getBoolean("Attack11")) {
                  if (entity.getPersistentData().getDouble("Atk11") == 0.0) {
                     entity.getPersistentData().putDouble("Atk11", 81.0);
                  } else {
                     entity.getPersistentData().putDouble("Atk11", entity.getPersistentData().getDouble("Atk11") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("Atk11") == 81.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.SEA_PICKLE));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.SEA_PICKLE));
                     }

                     entity.getPersistentData().putBoolean("focus", true);
                  } else if (entity.getPersistentData().getDouble("Atk11") == 75.0) {
                     entity.getPersistentData().putDouble("xz", entity.getPersistentData().getDouble("yaw2"));
                  } else if (entity.getPersistentData().getDouble("Atk11") == 74.0) {
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
                              "/data merge entity @e[type=deepnight:dawn_fire_giant,limit=1,sort=nearest] {NoAI:1b}"
                           );
                     }
                  } else if (entity.getPersistentData().getDouble("Atk11") == 64.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x + entity.getLookAngle().x * 6.0, y, z + entity.getLookAngle().z * 6.0),
                              dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              0.4F
                           );
                        } else {
                           _level.playLocalSound(
                              x + entity.getLookAngle().x * 6.0,
                              y,
                              z + entity.getLookAngle().z * 6.0,
                              dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              0.4F,
                              false
                           );
                        }
                     }

                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x + entity.getLookAngle().x * 6.0, y, z + entity.getLookAngle().z * 6.0),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:jump"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x + entity.getLookAngle().x * 6.0,
                              y,
                              z + entity.getLookAngle().z * 6.0,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:jump"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F,
                              false
                           );
                        }
                     }

                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x + entity.getLookAngle().x * 6.0, y, z + entity.getLookAngle().z * 6.0),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x + entity.getLookAngle().x * 6.0,
                              y,
                              z + entity.getLookAngle().z * 6.0,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F,
                              false
                           );
                        }
                     }

                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new RumbleEntity(SimpleMobsModEntities.RUMBLE.get(), _level);
                        entityToSpawn.moveTo(x + entity.getLookAngle().x * 6.0, y, z + entity.getLookAngle().z * 6.0, 0.0F, 0.0F);
                        entityToSpawn.setYBodyRot(0.0F);
                        entityToSpawn.setYHeadRot(0.0F);
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }

                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new AirEntity(SimpleMobsModEntities.AIR.get(), _level);
                        entityToSpawn.moveTo(x + entity.getLookAngle().x * 6.0, y, z + entity.getLookAngle().z * 6.0, 0.0F, 0.0F);
                        entityToSpawn.setYBodyRot(0.0F);
                        entityToSpawn.setYHeadRot(0.0F);
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  } else if (entity.getPersistentData().getDouble("Atk11") < 64.0 && entity.getPersistentData().getDouble("Atk11") > 60.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 3.0, y, z + entity.getLookAngle().z * 3.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(12.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("firegiant")) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.LONG)
                              .setImpact(4.0F)
                              .setArmorNegation(30.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 30.0F);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk11") == 58.0) {
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

                     entity.getPersistentData().putBoolean("focus", true);
                     entity.getPersistentData().putBoolean("lock", false);
                  } else if (entity.getPersistentData().getDouble("Atk11") == 53.0) {
                     entity.getPersistentData().putBoolean("focus", false);
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
                  } else if (entity.getPersistentData().getDouble("Atk11") == 42.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x + entity.getLookAngle().x * 6.0, y, z + entity.getLookAngle().z * 6.0),
                              dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              0.4F
                           );
                        } else {
                           _level.playLocalSound(
                              x + entity.getLookAngle().x * 6.0,
                              y,
                              z + entity.getLookAngle().z * 6.0,
                              dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              0.4F,
                              false
                           );
                        }
                     }

                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x + entity.getLookAngle().x * 6.0, y, z + entity.getLookAngle().z * 6.0),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:jump"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x + entity.getLookAngle().x * 6.0,
                              y,
                              z + entity.getLookAngle().z * 6.0,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:jump"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F,
                              false
                           );
                        }
                     }

                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x + entity.getLookAngle().x * 6.0, y, z + entity.getLookAngle().z * 6.0),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x + entity.getLookAngle().x * 6.0,
                              y,
                              z + entity.getLookAngle().z * 6.0,
                              dev.deepnight.SafeSounds.resolve("simple_mobs:firegiant_footstep"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F,
                              false
                           );
                        }
                     }

                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new RumbleEntity(SimpleMobsModEntities.RUMBLE.get(), _level);
                        entityToSpawn.moveTo(x + entity.getLookAngle().x * 6.0, y, z + entity.getLookAngle().z * 6.0, 0.0F, 0.0F);
                        entityToSpawn.setYBodyRot(0.0F);
                        entityToSpawn.setYHeadRot(0.0F);
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }

                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new AirEntity(SimpleMobsModEntities.AIR.get(), _level);
                        entityToSpawn.moveTo(x + entity.getLookAngle().x * 6.0, y, z + entity.getLookAngle().z * 6.0, 0.0F, 0.0F);
                        entityToSpawn.setYBodyRot(0.0F);
                        entityToSpawn.setYHeadRot(0.0F);
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  } else if (entity.getPersistentData().getDouble("Atk11") < 42.0 && entity.getPersistentData().getDouble("Atk11") > 38.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 3.0, y, z + entity.getLookAngle().z * 3.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(12.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!entityiterator.getPersistentData().getBoolean("firegiant")) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.LONG)
                              .setImpact(4.0F)
                              .setArmorNegation(30.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 36.0F);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("Atk11") == 21.0) {
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
                  } else if (entity.getPersistentData().getDouble("Atk11") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("Atk11", 0.0);
                     entity.getPersistentData().putBoolean("Attack11", false);
                  }
               }
            }
         }
      }
   }
}
