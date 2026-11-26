package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.CapedMinotaurEntity;
import dev.deepnight.source.dawn.entity.FireballEntity;
import dev.deepnight.source.dawn.entity.Spell1Entity;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import dev.deepnight.source.SourceDamage;
import dev.deepnight.source.SourceDamage.StunType;

public class ProcapedminotaurProcedure {
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

         if (!world.isClientSide() && entity.isAlive()) {
            if (entity.getPersistentData().getBoolean("hurtcd")) {
               if (entity.getPersistentData().getDouble("hurt_cd") == 0.0) {
                  entity.getPersistentData().putDouble("hurt_cd", 5.0);
               } else {
                  entity.getPersistentData().putDouble("hurt_cd", entity.getPersistentData().getDouble("hurt_cd") - 1.0);
               }

               if (entity.getPersistentData().getDouble("hurt_cd") == 1.0) {
                  entity.getPersistentData().putDouble("hurt_cd", 0.0);
                  entity.getPersistentData().putBoolean("hurtcd", false);
               }
            }

            if (entity.getPersistentData().getDouble("linecheck") != 0.0) {
               entity.getPersistentData().putDouble("linecheck", entity.getPersistentData().getDouble("linecheck") - 1.0);
            } else {
               double raytrace_distance = 0.0;
               entity.getPersistentData().putBoolean("Line", false);

               for (int index0 = 0; index0 < 24; index0++) {
                  if (!world.getBlockState(
                           BlockPos.containing(
                              entity.level()
                                 .clip(
                                    new ClipContext(
                                       entity.getEyePosition(1.0F),
                                       entity.getEyePosition(1.0F).add(entity.getViewVector(1.0F).scale(raytrace_distance)),
                                       Block.COLLIDER,
                                       Fluid.NONE,
                                       entity
                                    )
                                 )
                                 .getBlockPos()
                                 .getX(),
                              entity.level()
                                 .clip(
                                    new ClipContext(
                                       entity.getEyePosition(1.0F),
                                       entity.getEyePosition(1.0F).add(entity.getViewVector(1.0F).scale(raytrace_distance)),
                                       Block.COLLIDER,
                                       Fluid.NONE,
                                       entity
                                    )
                                 )
                                 .getBlockPos()
                                 .getY(),
                              entity.level()
                                 .clip(
                                    new ClipContext(
                                       entity.getEyePosition(1.0F),
                                       entity.getEyePosition(1.0F).add(entity.getViewVector(1.0F).scale(raytrace_distance)),
                                       Block.COLLIDER,
                                       Fluid.NONE,
                                       entity
                                    )
                                 )
                                 .getBlockPos()
                                 .getZ()
                           )
                        )
                        .canOcclude()
                     || raytrace_distance < 24.0) {
                     Vec3 _center = new Vec3(
                        entity.level()
                           .clip(
                              new ClipContext(
                                 entity.getEyePosition(1.0F),
                                 entity.getEyePosition(1.0F).add(entity.getViewVector(1.0F).scale(++raytrace_distance)),
                                 Block.COLLIDER,
                                 Fluid.NONE,
                                 entity
                              )
                           )
                           .getBlockPos()
                           .getX(),
                        entity.level()
                           .clip(
                              new ClipContext(
                                 entity.getEyePosition(1.0F),
                                 entity.getEyePosition(1.0F).add(entity.getViewVector(1.0F).scale(raytrace_distance)),
                                 Block.COLLIDER,
                                 Fluid.NONE,
                                 entity
                              )
                           )
                           .getBlockPos()
                           .getY(),
                        entity.level()
                           .clip(
                              new ClipContext(
                                 entity.getEyePosition(1.0F),
                                 entity.getEyePosition(1.0F).add(entity.getViewVector(1.0F).scale(raytrace_distance)),
                                 Block.COLLIDER,
                                 Fluid.NONE,
                                 entity
                              )
                           )
                           .getBlockPos()
                           .getZ()
                     );

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(0.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                           entity.getPersistentData().putBoolean("Line", true);
                        }
                     }
                  }
               }

               entity.getPersistentData().putDouble("linecheck", 20.0);
            }

            if (entity.getPersistentData().getBoolean("Attacking")) {
               entity.getPersistentData().putBoolean("flee", false);
            } else {
               entity.getPersistentData().putBoolean("flee", true);
            }

            entity.getPersistentData().putBoolean("Mid", false);
            Vec3 _center = new Vec3(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(12.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                  entity.getPersistentData().putBoolean("Mid", true);
               }
            }

            if (entity.getPersistentData().getDouble("Poise") > 0.0 && !entity.getPersistentData().getBoolean("Attacking")) {
               entity.getPersistentData().putDouble("Poise", entity.getPersistentData().getDouble("Poise") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Poise") > 130.0 && !entity.getPersistentData().getBoolean("Stunned")) {
               entity.getPersistentData().putBoolean("Stunned", true);
            }

            if (entity.getPersistentData().getDouble("atk1cd") == 0.0) {
               entity.getPersistentData().putDouble("atk1cd", 150.0);
            } else if (entity.getPersistentData().getDouble("atk1cd") >= 1.0) {
               entity.getPersistentData().putDouble("atk1cd", entity.getPersistentData().getDouble("atk1cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("atk2cd") == 0.0) {
               entity.getPersistentData().putDouble("atk2cd", 140.0);
            } else if (entity.getPersistentData().getDouble("atk2cd") >= 1.0) {
               entity.getPersistentData().putDouble("atk2cd", entity.getPersistentData().getDouble("atk2cd") - 1.0);
            } else if (entity.getPersistentData().getDouble("atk2cd") == 30.0) {
               _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(15.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator.getPersistentData().getBoolean("marked")) {
                     entityiterator.getPersistentData().putBoolean("marked", false);
                  }
               }
            }

            if (entity.getPersistentData().getDouble("atk3cd") == 0.0) {
               entity.getPersistentData().putDouble("atk3cd", 200.0);
            } else if (entity.getPersistentData().getDouble("atk3cd") >= 1.0) {
               entity.getPersistentData().putDouble("atk3cd", entity.getPersistentData().getDouble("atk3cd") - 1.0);
            }

            if (entity.getPersistentData().getBoolean("Attack1")) {
               if (entity.getPersistentData().getDouble("atk1") == 0.0) {
                  entity.getPersistentData().putDouble("atk1", 41.0);
               } else {
                  entity.getPersistentData().putDouble("atk1", entity.getPersistentData().getDouble("atk1") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk1") == 41.0) {
                  entity.getPersistentData().putBoolean("Attacking", true);
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.LEATHER_HELMET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 41, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("atk1") == 18.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 1.2, y, z + entity.getLookAngle().z * 1.2);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(10.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null) && world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new Spell1Entity(SimpleMobsModEntities.SPELL_1.get(), _level);
                        entityToSpawn.moveTo(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ(), 0.0F, 0.0F);
                        entityToSpawn.setYBodyRot(0.0F);
                        entityToSpawn.setYHeadRot(0.0F);
                        entityToSpawn.setDeltaMovement(0.0, 0.0, 0.0);
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
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

                  entity.getPersistentData().putDouble("atk1", 0.0);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("Attack1", false);
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  entity.getPersistentData().putBoolean("Attack1", false);
                  entity.getPersistentData().putDouble("Atk1", 0.0);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack2")) {
               if (entity.getPersistentData().getDouble("atk2") == 0.0) {
                  entity.getPersistentData().putDouble("atk2", 51.0);
               } else {
                  entity.getPersistentData().putDouble("atk2", entity.getPersistentData().getDouble("atk2") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk2") == 51.0) {
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (entity.getPersistentData().getDouble("atk2") == 31.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.IRON_INGOT));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_INGOT));
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 41, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("atk2") == 20.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 1.2, y, z + entity.getLookAngle().z * 1.2);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(15.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        entityiterator.getPersistentData().putBoolean("marked", true);
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new FireballEntity(SimpleMobsModEntities.FIREBALL.get(), _level);
                     entityToSpawn.moveTo(x + entity.getLookAngle().x * 1.2, y + 1.6, z + entity.getLookAngle().z * 1.2, 0.0F, 0.0F);
                     entityToSpawn.setYBodyRot(0.0F);
                     entityToSpawn.setYHeadRot(0.0F);
                     entityToSpawn.setDeltaMovement(0.0, 0.0, 0.0);
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
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

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  entity.getPersistentData().putBoolean("Attack2", false);
                  entity.getPersistentData().putDouble("atk2", 0.0);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack3")) {
               if (entity.getPersistentData().getDouble("atk3") == 0.0) {
                  entity.getPersistentData().putDouble("atk3", 61.0);
               } else {
                  entity.getPersistentData().putDouble("atk3", entity.getPersistentData().getDouble("atk3") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk3") == 61.0) {
                  entity.getPersistentData().putBoolean("Attacking", true);
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.COAL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.COAL));
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 15, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("atk3") == 46.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 21, 1, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("atk3") >= 24.0 && entity.getPersistentData().getDouble("atk3") <= 45.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 1.2, y, z + entity.getLookAngle().z * 1.2);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof CapedMinotaurEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.SHORT)
                           .setImpact(12.0F)
                           .setArmorNegation(3.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 8.0F);
                     }
                  }
               } else if (entity.getPersistentData().getDouble("atk3") == 3.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putDouble("atk3", 0.0);
                  entity.getPersistentData().putBoolean("Attack3", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  entity.getPersistentData().putBoolean("Attack3", false);
                  entity.getPersistentData().putDouble("atk3", 0.0);
               }
            }

            if (entity.getPersistentData().getBoolean("Stunned")) {
               if (entity.getPersistentData().getDouble("stun") == 0.0) {
                  entity.getPersistentData().putDouble("stun", 61.0);
               } else {
                  entity.getPersistentData().putDouble("stun", entity.getPersistentData().getDouble("stun") - 1.0);
               }

               if (entity.getPersistentData().getDouble("stun") == 61.0) {
                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:poise"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("simple_mobs:poise"), SoundSource.NEUTRAL, 1.0F, 1.0F, false
                        );
                     }
                  }

                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.KELP));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.KELP));
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 71, 7, false, false));
                  }

                  entity.getPersistentData().putDouble("xz", entity.getYRot());
                  entity.getPersistentData().putDouble("y", entity.getXRot());
               } else if (entity.getPersistentData().getDouble("stun") == 60.0) {
                  entity.getPersistentData().putBoolean("lock", true);
               } else if (entity.getPersistentData().getDouble("stun") == 1.0) {
                  entity.getPersistentData().putBoolean("lock", false);
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putDouble("Poise", 0.0);
                  entity.getPersistentData().putDouble("stun", 0.0);
                  entity.getPersistentData().putBoolean("Stunned", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Mid")
               && entity.getPersistentData().getBoolean("Line")
               && !entity.getPersistentData().getBoolean("Attacking")) {
               if (Math.random() <= 0.7 && entity.getPersistentData().getDouble("atk1cd") == 1.0) {
                  entity.getPersistentData().putBoolean("Attack1", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (Math.random() >= 0.4 && entity.getPersistentData().getDouble("atk2cd") == 1.0) {
                  entity.getPersistentData().putBoolean("Attack2", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (Math.random() >= 0.5 && entity.getPersistentData().getDouble("atk3cd") == 1.0) {
                  entity.getPersistentData().putBoolean("Attack3", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               }
            }

            if (!entity.getPersistentData().getBoolean("aggro")) {
               if (entity.getPersistentData().getDouble("idle2") == 0.0) {
                  entity.getPersistentData().putDouble("idle2", 400.0);
               } else {
                  entity.getPersistentData().putDouble("idle2", entity.getPersistentData().getDouble("idle2") - 1.0);
               }

               if (Math.random() >= 0.67 && entity.getPersistentData().getDouble("idle2") == 1.0) {
                  entity.getPersistentData().putBoolean("handstand", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               }
            }

            if (entity.getPersistentData().getBoolean("handstand")) {
               if (entity.getPersistentData().getDouble("idle2") == 0.0) {
                  entity.getPersistentData().putDouble("idle2", 62.0);
               } else {
                  entity.getPersistentData().putDouble("idle2", entity.getPersistentData().getDouble("idle2") - 1.0);
               }

               if (entity.getPersistentData().getDouble("idle2") == 62.0) {
                  entity.getPersistentData().putBoolean("Attacking", true);
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.POPPY));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.POPPY));
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 62, 7, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("idle2") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("idle2", 0.0);
                  entity.getPersistentData().putBoolean("handstand", false);
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  entity.getPersistentData().putBoolean("handstand", false);
                  entity.getPersistentData().putDouble("idle2", 0.0);
               }
            }
         }
      }
   }
}
