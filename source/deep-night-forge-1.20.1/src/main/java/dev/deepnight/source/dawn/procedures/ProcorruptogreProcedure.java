package dev.deepnight.source.dawn.procedures;


import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.CorruptedOgreEntity;
import dev.deepnight.source.dawn.entity.LostEyeOgreEntity;
import dev.deepnight.source.dawn.entity.LosteyeEffect2Entity;
import dev.deepnight.source.dawn.entity.LosteyeEffect3Entity;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
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

public class ProcorruptogreProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (entity.getPersistentData().getBoolean("lock")) {
            Entity _ent = entity;
            _ent.setYRot((float)entity.getPersistentData().getDouble("yaw"));
            _ent.setXRot((float)entity.getPersistentData().getDouble("pitch"));
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
                  entity.getPersistentData().putDouble("hurt_cd", 11.0);
               } else {
                  entity.getPersistentData().putDouble("hurt_cd", entity.getPersistentData().getDouble("hurt_cd") - 1.0);
               }

               if (entity.getPersistentData().getDouble("hurt_cd") == 1.0) {
                  entity.getPersistentData().putDouble("hurt_cd", 0.0);
                  entity.getPersistentData().putBoolean("hurtcd", false);
               }
            }

            if (entity.getPersistentData().getBoolean("spin")) {
               entity.getPersistentData().putDouble("direction", Math.abs(entity.getYRot() / 360.0F * 360.0F) + 4.0F);
               if (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop") > 14.0) {
                  entity.getPersistentData().putDouble("direction", entity.getPersistentData().getDouble("direction") - 14.0);
               } else if (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop") < 0.0) {
                  entity.getPersistentData().putDouble("direction", entity.getPersistentData().getDouble("direction") + 14.0);
               }

               if (!(entity.getPersistentData().getDouble("loop") < 15.0)) {
                  entity.getPersistentData().putDouble("loop", 0.0);
                  entity.getPersistentData().putBoolean("spin", false);
               } else {
                  Vec3 _center = new Vec3(
                     x + Math.cos((Math.PI / 6) * (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop"))) * 2.0,
                     y + 1.0,
                     z + Math.sin((Math.PI / 6) * (entity.getPersistentData().getDouble("direction") + entity.getPersistentData().getDouble("loop"))) * 2.0
                  );

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof CorruptedOgreEntity) && !(entityiterator instanceof LostEyeOgreEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(1.5F)
                           .setArmorNegation(1.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 8.0F);
                        entityiterator.setDeltaMovement(
                           new Vec3((entityiterator.getX() - entity.getX()) / 4.0, 0.3, (entityiterator.getX() - entity.getX()) / 4.0)
                        );
                     }
                  }

                  entity.getPersistentData().putDouble("loop", entity.getPersistentData().getDouble("loop") + 1.0);
               }
            }

            entity.getPersistentData().putBoolean("Close", false);
            entity.getPersistentData().putBoolean("Far", false);
            entity.getPersistentData().putBoolean("Aggro", false);
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(24.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                  entity.getPersistentData().putBoolean("Aggro", true);
               }
            }

            _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(8.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                  entity.getPersistentData().putBoolean("Far", true);
               }
            }

            _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                  entity.getPersistentData().putBoolean("Far", false);
                  entity.getPersistentData().putBoolean("Close", true);
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

            if (entity.getPersistentData().getDouble("Poise") > 320.0 && !entity.getPersistentData().getBoolean("Stunned")) {
               entity.getPersistentData().putBoolean("Stunned", true);
            }

            if ((entity.getPersistentData().getBoolean("Attack4") || entity.getPersistentData().getBoolean("Attack4G"))
               && entity.getPersistentData().getDouble("Atk4CD") <= 0.0) {
               entity.getPersistentData().putDouble("Atk4CD", Mth.nextInt(net.minecraft.util.RandomSource.create(), 200, 300));
            } else if (entity.getPersistentData().getDouble("Atk4CD") >= 1.0) {
               entity.getPersistentData().putDouble("Atk4CD", entity.getPersistentData().getDouble("Atk4CD") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Atk7CD") <= 0.0 && entity.getPersistentData().getBoolean("Attack7")) {
               entity.getPersistentData().putDouble("Atk7CD", Mth.nextInt(net.minecraft.util.RandomSource.create(), 300, 500));
            } else if (entity.getPersistentData().getDouble("Atk7CD") >= 1.0) {
               entity.getPersistentData().putDouble("Atk7CD", entity.getPersistentData().getDouble("Atk7CD") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Atk8CD") <= 0.0 && entity.getPersistentData().getBoolean("Attack8")) {
               entity.getPersistentData().putDouble("Atk8CD", Mth.nextInt(net.minecraft.util.RandomSource.create(), 200, 400));
            } else if (entity.getPersistentData().getDouble("Atk8CD") >= 1.0) {
               entity.getPersistentData().putDouble("Atk8CD", entity.getPersistentData().getDouble("Atk8CD") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Atk9CD") <= 0.0 && entity.getPersistentData().getBoolean("Attack9")) {
               entity.getPersistentData().putDouble("Atk9CD", Mth.nextInt(net.minecraft.util.RandomSource.create(), 100, 300));
            } else if (entity.getPersistentData().getDouble("Atk9CD") >= 1.0) {
               entity.getPersistentData().putDouble("Atk9CD", entity.getPersistentData().getDouble("Atk9CD") - 1.0);
            }

            if (entity.getPersistentData().getDouble("Atk10CD") <= 0.0 && entity.getPersistentData().getBoolean("Attack10")) {
               entity.getPersistentData().putDouble("Atk10CD", Mth.nextInt(net.minecraft.util.RandomSource.create(), 200, 400));
            } else if (entity.getPersistentData().getDouble("Atk10CD") >= 1.0) {
               entity.getPersistentData().putDouble("Atk10CD", entity.getPersistentData().getDouble("Atk10CD") - 1.0);
            }

            if (entity.getPersistentData().getBoolean("Attack1")) {
               if (entity.getPersistentData().getDouble("Atk1") == 0.0) {
                  entity.getPersistentData().putDouble("Atk1", 45.0);
               } else {
                  entity.getPersistentData().putDouble("Atk1", entity.getPersistentData().getDouble("Atk1") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk1") == 45.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.LEATHER_HELMET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 41, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk1") == 29.0) {
                  entity.getPersistentData().putDouble("yaw", entity.getYRot());
                  entity.getPersistentData().putDouble("pitch", entity.getXRot());
               } else if (entity.getPersistentData().getDouble("Atk1") == 29.0) {
                  entity.getPersistentData().putBoolean("lock", true);
               } else if (entity.getPersistentData().getDouble("Atk1") == 28.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("block.anvil.land"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           -1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("block.anvil.land"), SoundSource.NEUTRAL, 1.0F, -1.0F, false
                        );
                     }
                  }

                  _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof CorruptedOgreEntity) && !(entityiterator instanceof LostEyeOgreEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.SHORT)
                           .setImpact(1.5F)
                           .setArmorNegation(1.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 6.0F);
                        if (entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2, false, false));
                        }
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk1") == 2.0) {
                  entity.getPersistentData().putBoolean("lock", false);
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }
               } else if (entity.getPersistentData().getDouble("Atk1") == 1.0) {
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk1", 0.0);
                  entity.getPersistentData().putBoolean("Attack1", false);
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  entity.getPersistentData().putDouble("Atk1", 0.0);
                  entity.getPersistentData().putBoolean("Attack1", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack2")) {
               if (entity.getPersistentData().getDouble("Atk2") == 0.0) {
                  entity.getPersistentData().putDouble("Atk2", 65.0);
               } else {
                  entity.getPersistentData().putDouble("Atk2", entity.getPersistentData().getDouble("Atk2") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk2") == 65.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.GOLD_NUGGET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLD_NUGGET));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 65, 7, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk2") == 53.0) {
                  entity.getPersistentData().putDouble("yaw", entity.getYRot());
                  entity.getPersistentData().putDouble("pitch", entity.getXRot());
               } else if (entity.getPersistentData().getDouble("Atk2") == 52.0) {
                  entity.getPersistentData().putBoolean("lock", true);
               } else if (entity.getPersistentData().getDouble("Atk2") == 42.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("block.anvil.land"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           -1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("block.anvil.land"), SoundSource.NEUTRAL, 1.0F, -1.0F, false
                        );
                     }
                  }

                  _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof CorruptedOgreEntity) && !(entityiterator instanceof LostEyeOgreEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.SHORT)
                           .setImpact(1.5F)
                           .setArmorNegation(1.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 8.0F);
                        if (entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2, false, false));
                        }
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk2") == 35.0) {
                  entity.getPersistentData().putBoolean("lock", false);
               } else if (entity.getPersistentData().getDouble("Atk2") == 28.0) {
                  entity.getPersistentData().putDouble("yaw", entity.getYRot());
                  entity.getPersistentData().putDouble("pitch", entity.getXRot());
               } else if (entity.getPersistentData().getDouble("Atk2") == 27.0) {
                  entity.getPersistentData().putBoolean("lock", true);
               } else if (entity.getPersistentData().getDouble("Atk2") == 20.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("block.anvil.land"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           -1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("block.anvil.land"), SoundSource.NEUTRAL, 1.0F, -1.0F, false
                        );
                     }
                  }

                  _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof CorruptedOgreEntity) && !(entityiterator instanceof LostEyeOgreEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(1.5F)
                           .setArmorNegation(1.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 8.0F);
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk2") == 2.0) {
                  entity.getPersistentData().putBoolean("lock", false);
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }
               } else if (entity.getPersistentData().getDouble("Atk2") == 1.0) {
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk2", 0.0);
                  entity.getPersistentData().putBoolean("Attack2", false);
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  entity.getPersistentData().putDouble("Atk2", 0.0);
                  entity.getPersistentData().putBoolean("Attack2", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack3")) {
               if (entity.getPersistentData().getDouble("Atk3") == 0.0) {
                  entity.getPersistentData().putDouble("Atk3", 25.0);
               } else {
                  entity.getPersistentData().putDouble("Atk3", entity.getPersistentData().getDouble("Atk3") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk3") == 25.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.COAL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.COAL));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 25, 7, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk3") == 20.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ogre_attack3"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ogre_attack3"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F,
                           false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk3") == 15.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof CorruptedOgreEntity) && !(entityiterator instanceof LostEyeOgreEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(6.0F)
                           .setArmorNegation(1.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 7.0F);
                        entityiterator.setDeltaMovement(
                           new Vec3((entityiterator.getX() - entity.getX()) / 4.5, 0.2, (entityiterator.getZ() - entity.getZ()) / 4.5)
                        );
                        entityiterator.setDeltaMovement(new Vec3(entityiterator.getX() - entity.getX(), 0.6, entityiterator.getZ() - entity.getZ()));
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk3") == 2.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }
               } else if (entity.getPersistentData().getDouble("Atk3") == 1.0) {
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk3", 0.0);
                  entity.getPersistentData().putBoolean("Attack3", false);
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  entity.getPersistentData().putDouble("Atk3", 0.0);
                  entity.getPersistentData().putBoolean("Attack3", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack4")) {
               if (entity.getPersistentData().getDouble("Atk4") == 0.0) {
                  entity.getPersistentData().putDouble("Atk4", 51.0);
               } else {
                  entity.getPersistentData().putDouble("Atk4", entity.getPersistentData().getDouble("Atk4") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk4") == 51.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.CHARCOAL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHARCOAL));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 11, 7, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk4") == 40.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10, 4, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk4") == 30.0) {
                  entity.getPersistentData().putDouble("yaw", entity.getYRot());
                  entity.getPersistentData().putDouble("pitch", entity.getXRot());
                  entity.getPersistentData().putBoolean("lock", true);
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 7, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk4") == 27.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("block.anvil.land"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           -3.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("block.anvil.land"), SoundSource.NEUTRAL, 2.0F, -3.0F, false
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
                           1.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.CAMPFIRE_COSY_SMOKE, x + entity.getLookAngle().x * 1.6, y, z + entity.getLookAngle().z * 1.6, 5, 2.0, 2.0, 2.0, 1.0
                     );
                  }

                  _center = new Vec3(x + entity.getLookAngle().x * 1.6, y, z + entity.getLookAngle().z * 1.6);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof CorruptedOgreEntity) && !(entityiterator instanceof LostEyeOgreEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(9.0F)
                           .setArmorNegation(1.2F);
                        entityiterator.hurt((DamageSource)dmgSrc, 10.0F);
                        if (entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 4, false, false));
                        }
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk4") == 2.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }
               } else if (entity.getPersistentData().getDouble("Atk4") == 1.0) {
                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk4", 0.0);
                  entity.getPersistentData().putBoolean("Attack4", false);
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  entity.getPersistentData().putDouble("Atk4", 0.0);
                  entity.getPersistentData().putBoolean("Attack4", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack4G")) {
               if (entity.getPersistentData().getDouble("Atk4g") == 0.0) {
                  entity.getPersistentData().putDouble("Atk4g", 51.0);
               } else {
                  entity.getPersistentData().putDouble("Atk4g", entity.getPersistentData().getDouble("Atk4g") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk4g") == 51.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.CHARCOAL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHARCOAL));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 11, 7, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk4g") == 40.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10, 8, false, false));
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new LosteyeEffect3Entity(SimpleMobsModEntities.LOSTEYE_EFFECT_3.get(), _level);
                     entityToSpawn.moveTo(x + entity.getLookAngle().x * -1.5, y, z + entity.getLookAngle().z * -1.5, entity.getYRot(), entity.getXRot());
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               } else if (entity.getPersistentData().getDouble("Atk4g") == 30.0) {
                  entity.getPersistentData().putDouble("yaw", entity.getYRot());
                  entity.getPersistentData().putDouble("pitch", entity.getXRot());
                  entity.getPersistentData().putBoolean("lock", true);
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 7, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk4g") == 27.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("block.anvil.land"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           -3.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("block.anvil.land"), SoundSource.NEUTRAL, 2.0F, -3.0F, false
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
                           dev.deepnight.SafeSounds.resolve("simple_mobs:electricity_zap"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           0.2F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:electricity_zap"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           0.2F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        (SimpleParticleType)SimpleMobsModParticleTypes.LOSTEYEPARTICLE.get(),
                        x + entity.getLookAngle().x * 1.6,
                        y,
                        z + entity.getLookAngle().z * 1.6,
                        20,
                        1.0,
                        0.5,
                        1.0,
                        0.1
                     );
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.CAMPFIRE_COSY_SMOKE, x + entity.getLookAngle().x * 1.6, y, z + entity.getLookAngle().z * 1.6, 5, 1.0, 0.5, 1.0, 0.4
                     );
                  }

                  _center = new Vec3(x + entity.getLookAngle().x * 1.6, y, z + entity.getLookAngle().z * 1.6);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof CorruptedOgreEntity) && !(entityiterator instanceof LostEyeOgreEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.KNOCKDOWN)
                           .setImpact(9.0F)
                           .setArmorNegation(1.2F);
                        entityiterator.hurt((DamageSource)dmgSrc, 14.0F);
                        if (entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 4, false, false));
                        }
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk4g") == 2.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }
               } else if (entity.getPersistentData().getDouble("Atk4g") == 1.0) {
                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk4g", 0.0);
                  entity.getPersistentData().putBoolean("Attack4G", false);
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putDouble("Atk4g", 0.0);
                  entity.getPersistentData().putBoolean("Attack4G", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack5")) {
               if (entity.getPersistentData().getDouble("Atk5") == 0.0) {
                  entity.getPersistentData().putDouble("Atk5", 71.0);
               } else {
                  entity.getPersistentData().putDouble("Atk5", entity.getPersistentData().getDouble("Atk5") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk5") == 71.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.IRON_INGOT));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_INGOT));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 1, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk5") == 36.0) {
                  entity.getPersistentData().putDouble("yaw", entity.getYRot());
                  entity.getPersistentData().putDouble("pitch", entity.getXRot());
                  entity.getPersistentData().putBoolean("lock", true);
               } else if (entity.getPersistentData().getDouble("Atk5") == 32.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 32, 7, false, false));
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("block.anvil.land"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           -3.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("block.anvil.land"), SoundSource.NEUTRAL, 2.0F, -3.0F, false
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
                           1.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.CAMPFIRE_COSY_SMOKE, x + entity.getLookAngle().x * 1.6, y, z + entity.getLookAngle().z * 1.6, 10, 1.0, 1.0, 1.0, 1.0
                     );
                  }

                  _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof CorruptedOgreEntity) && !(entityiterator instanceof LostEyeOgreEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(9.0F)
                           .setArmorNegation(0.8F);
                        entityiterator.hurt((DamageSource)dmgSrc, 14.0F);
                        entityiterator.setDeltaMovement(
                           new Vec3((entityiterator.getX() - entity.getX()) / 4.5, 0.2, (entityiterator.getZ() - entity.getZ()) / 4.5)
                        );
                        entityiterator.setDeltaMovement(new Vec3(entityiterator.getX() - entity.getX(), 0.6, entityiterator.getZ() - entity.getZ()));
                        if (entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2, false, false));
                        }

                        if (entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 60, 1, false, false));
                        }
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk5") == 2.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }
               } else if (entity.getPersistentData().getDouble("Atk5") == 1.0) {
                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk5", 0.0);
                  entity.getPersistentData().putBoolean("Attack5", false);
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putDouble("Atk5", 0.0);
                  entity.getPersistentData().putBoolean("Attack5", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack6")) {
               if (entity.getPersistentData().getDouble("Atk6") == 0.0) {
                  entity.getPersistentData().putDouble("Atk6", 42.0);
               } else {
                  entity.getPersistentData().putDouble("Atk6", entity.getPersistentData().getDouble("Atk6") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk6") == 42.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.IRON_NUGGET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_NUGGET));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  entity.getPersistentData().putDouble("loop", 1.0);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 41, 7, false, false));
                  }

                  entity.getPersistentData().putDouble("yaw", entity.getYRot());
                  entity.getPersistentData().putDouble("pitch", entity.getXRot());
                  entity.getPersistentData().putBoolean("lock", true);
               } else if (entity.getPersistentData().getDouble("Atk6") == 28.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ogre_spin"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ogre_spin"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  entity.getPersistentData().putBoolean("spin", true);
               } else if (entity.getPersistentData().getDouble("Atk6") == 24.0) {
                  entity.getPersistentData().putBoolean("spin", false);
               } else if (entity.getPersistentData().getDouble("Atk6") == 2.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.CORNFLOWER));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.CORNFLOWER));
                  }
               } else if (entity.getPersistentData().getDouble("Atk6") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk6", 0.0);
                  entity.getPersistentData().putBoolean("Attack6", false);
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("spin", false);
                  entity.getPersistentData().putDouble("Atk6", 0.0);
                  entity.getPersistentData().putBoolean("Attack6", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack7")) {
               if (entity.getPersistentData().getDouble("Atk7") == 0.0) {
                  entity.getPersistentData().putDouble("Atk7", 56.0);
               } else {
                  entity.getPersistentData().putDouble("Atk7", entity.getPersistentData().getDouble("Atk7") - 1.0);
               }

               entity.getPersistentData().putDouble("Poise", -50.0);
               if (world instanceof ServerLevel _level) {
                  _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.LOSTEYEPARTICLE.get(), x, y + 1.5, z, 1, 2.0, 2.0, 2.0, 0.2);
               }

               if (entity.getPersistentData().getDouble("Atk7") == 56.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.SUNFLOWER));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.SUNFLOWER));
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_corrupted_ogre,limit=1,sort=nearest] {NoAI:1b}"
                        );
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 56, 7, false, false));
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 56, 7, false, false));
                  }

                  entity.getPersistentData().putDouble("yaw", entity.getYRot());
                  entity.getPersistentData().putDouble("pitch", entity.getXRot());
                  entity.getPersistentData().putBoolean("lock", true);
               } else if (entity.getPersistentData().getDouble("Atk7") == 36.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("item.chorus_fruit.teleport"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.7F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("item.chorus_fruit.teleport"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.7F,
                           false
                        );
                     }
                  }

                  if (!world.getEntitiesOfClass(LostEyeOgreEntity.class, AABB.ofSize(new Vec3(x, y, z), 54.0, 54.0, 54.0), e -> true).isEmpty()) {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(27.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator instanceof LostEyeOgreEntity) {
                           Entity _ent = entityiterator;
                           _ent.teleportTo(x, y + 4.0, z);
                           if (_ent instanceof ServerPlayer _serverPlayer) {
                              _serverPlayer.connection.teleport(x, y + 4.0, z, _ent.getYRot(), _ent.getXRot());
                           }

                           if (world instanceof ServerLevel _level) {
                              _level.getServer()
                                 .getCommands()
                                 .performPrefixedCommand(
                                    new CommandSourceStack(
                                          CommandSource.NULL,
                                          new Vec3(x, y + 4.0, z),
                                          Vec2.ZERO,
                                          _level,
                                          4,
                                          "",
                                          Component.literal(""),
                                          _level.getServer(),
                                          null
                                       )
                                       .withSuppressedOutput(),
                                    "/particle dust 1.000 0.100 0.100 3 ~ ~ ~ 0.5 0.5 0.5 0 10 force"
                                 );
                           }
                        }
                     }
                  } else {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y + 4.0, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/particle dust 1.000 0.100 0.100 3 ~ ~ ~ 0.5 0.5 0.5 0 10 force"
                           );
                     }

                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new LostEyeOgreEntity(SimpleMobsModEntities.LOST_EYE_OGRE.get(), _level);
                        entityToSpawn.moveTo(x, y + 4.0, z, entity.getYRot(), entity.getXRot());
                        entityToSpawn.setYBodyRot(entity.getYRot());
                        entityToSpawn.setYHeadRot(entity.getYRot());
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk7") == 1.0) {
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
                           "/data merge entity @e[type=deepnight:dawn_corrupted_ogre,limit=1,sort=nearest] {NoAI:0}"
                        );
                  }

                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("nofollow", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("2", true);
                  entity.getPersistentData().putDouble("Atk7", 0.0);
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
                           "/data merge entity @e[type=deepnight:dawn_corrupted_ogre,limit=1,sort=nearest] {NoAI:0}"
                        );
                  }

                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("spin", false);
                  entity.getPersistentData().putDouble("Atk7", 0.0);
                  entity.getPersistentData().putBoolean("Attack7", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack8")) {
               if (entity.getPersistentData().getDouble("Atk8") == 0.0) {
                  entity.getPersistentData().putDouble("Atk8", 65.0);
               } else {
                  entity.getPersistentData().putDouble("Atk8", entity.getPersistentData().getDouble("Atk8") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk8") == 65.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.BAKED_POTATO));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.BAKED_POTATO));
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 65, 7, false, false));
                  }

                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(25.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        entity.getPersistentData().putDouble("ei_x", entityiterator.getX());
                        entity.getPersistentData().putDouble("ei_y", entityiterator.getY());
                        entity.getPersistentData().putDouble("ei_z", entityiterator.getZ());
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk8") == 36.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(25.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator instanceof LostEyeOgreEntity) {
                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("item.chorus_fruit.teleport"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 0.7F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("item.chorus_fruit.teleport"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 0.7F,
                                 false
                              );
                           }
                        }

                        entity.getPersistentData().putBoolean("nofollow", true);
                        Entity _ent = entityiterator;
                        _ent.teleportTo(
                           entity.getPersistentData().getDouble("ei_x"),
                           entity.getPersistentData().getDouble("ei_y") + 2.0,
                           entity.getPersistentData().getDouble("ei_z")
                        );
                        if (_ent instanceof ServerPlayer _serverPlayer) {
                           _serverPlayer.connection
                              .teleport(
                                 entity.getPersistentData().getDouble("ei_x"),
                                 entity.getPersistentData().getDouble("ei_y") + 2.0,
                                 entity.getPersistentData().getDouble("ei_z"),
                                 _ent.getYRot(),
                                 _ent.getXRot()
                              );
                        }

                        if (world instanceof ServerLevel _level) {
                           _level.getServer()
                              .getCommands()
                              .performPrefixedCommand(
                                 new CommandSourceStack(
                                       CommandSource.NULL,
                                       new Vec3(
                                          entity.getPersistentData().getDouble("ei_x"),
                                          entity.getPersistentData().getDouble("ei_y") + 2.0,
                                          entity.getPersistentData().getDouble("ei_z")
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
                                 "/particle dust 1.000 0.100 0.100 3 ~ ~ ~ 0.5 0.5 0.5 0 10 force"
                              );
                        }
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk8") == 14.0) {
                  entity.getPersistentData().putBoolean("fire", true);
               } else if (entity.getPersistentData().getDouble("Atk8") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("fire", false);
                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk8", 0.0);
                  entity.getPersistentData().putBoolean("Attack8", false);
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("fire", false);
                  entity.getPersistentData().putDouble("Atk8", 0.0);
                  entity.getPersistentData().putBoolean("Attack8", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack9")) {
               if (entity.getPersistentData().getDouble("Atk9") == 0.0) {
                  entity.getPersistentData().putDouble("Atk9", 75.0);
               } else {
                  entity.getPersistentData().putDouble("Atk9", entity.getPersistentData().getDouble("Atk9") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk9") == 75.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.BAMBOO));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.BAMBOO));
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
                           "/data merge entity @e[type=deepnight:dawn_corrupted_ogre,limit=1,sort=nearest] {NoAI:1b}"
                        );
                  }

                  entity.getPersistentData().putDouble("yaw", entity.getYRot());
                  entity.getPersistentData().putDouble("pitch", entity.getXRot());
                  entity.getPersistentData().putBoolean("lock", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 65, 7, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk9") < 43.0 && entity.getPersistentData().getDouble("Atk9") >= 40.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(10.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof LostEyeOgreEntity)
                        && !(entityiterator instanceof CorruptedOgreEntity)
                        && !(entityiterator instanceof LosteyeEffect2Entity)
                        && entityiterator instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 20, 1, false, false));
                     }
                  }

                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof LostEyeOgreEntity)
                        && !(entityiterator instanceof CorruptedOgreEntity)
                        && !(entityiterator instanceof LosteyeEffect2Entity)) {
                        entityiterator.setDeltaMovement(new Vec3(0.0, 0.8, 0.0));
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(2.0F)
                           .setArmorNegation(25.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 13.0F);
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.LOSTEYEPARTICLE.get(), x, y, z, 15, 1.0, 0.1, 1.0, 0.4);
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 15, 1.0, 0.1, 1.0, 1.0);
                  }
               } else if (entity.getPersistentData().getDouble("Atk9") == 1.0) {
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
                           "/data merge entity @e[type=deepnight:dawn_corrupted_ogre,limit=1,sort=nearest] {NoAI:0}"
                        );
                  }

                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk9", 0.0);
                  entity.getPersistentData().putBoolean("Attack9", false);
               }

               if (entity.getPersistentData().getDouble("Atk9") == 42.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("block.anvil.land"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("block.anvil.land"), SoundSource.NEUTRAL, 2.0F, 1.0F, false
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
                           2.0F,
                           0.6F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.dragon_fireball.explode"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.6F,
                           false
                        );
                     }
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:giant_footstep"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:giant_footstep"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F,
                           false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk9") < 41.0 && entity.getPersistentData().getDouble("Atk9") >= 37.0) {
                  for (int index0 = 0; index0 < 3; index0++) {
                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new LosteyeEffect2Entity(SimpleMobsModEntities.LOSTEYE_EFFECT_2.get(), _level);
                        entityToSpawn.moveTo(x + Mth.nextInt(net.minecraft.util.RandomSource.create(), -8, 8), y, z + Mth.nextInt(net.minecraft.util.RandomSource.create(), -8, 8), 0.0F, 0.0F);
                        entityToSpawn.setYBodyRot(0.0F);
                        entityToSpawn.setYHeadRot(0.0F);
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
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
                           "/data merge entity @e[type=deepnight:dawn_corrupted_ogre,limit=1,sort=nearest] {NoAI:0}"
                        );
                  }

                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putDouble("Atk9", 0.0);
                  entity.getPersistentData().putBoolean("Attack9", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack10")) {
               if (entity.getPersistentData().getDouble("Atk10") == 0.0) {
                  entity.getPersistentData().putDouble("Atk10", 55.0);
               } else {
                  entity.getPersistentData().putDouble("Atk10", entity.getPersistentData().getDouble("Atk10") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk10") == 55.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.BARREL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.BARREL));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 55, 7, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk10") == 37.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/data merge entity @e[type=deepnight:dawn_corrupted_ogre,limit=1,sort=nearest] {NoAI:1b}"
                        );
                  }

                  entity.getPersistentData().putDouble("yaw", entity.getYRot());
                  entity.getPersistentData().putDouble("pitch", entity.getXRot());
                  entity.getPersistentData().putBoolean("lock", true);
                  entity.getPersistentData().putBoolean("Attack10bT", false);
               } else if (entity.getPersistentData().getDouble("Atk10") == 32.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof LostEyeOgreEntity) && !(entityiterator instanceof CorruptedOgreEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.SHORT)
                           .setImpact(1.5F)
                           .setArmorNegation(1.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 8.0F);
                        entityiterator.setDeltaMovement(
                           new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 1.8, 1.3, Math.cos(Math.toRadians(entity.getYRot())) * 1.8)
                        );
                        entity.getPersistentData().putBoolean("Attack10bT", true);
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk10") == 20.0) {
                  if (entity.getPersistentData().getBoolean("Attack10bT")) {
                     entity.getPersistentData().putBoolean("Attack10b", true);
                     entity.getPersistentData().putBoolean("Attack10bT", false);
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_corrupted_ogre,limit=1,sort=nearest] {NoAI:0}"
                           );
                     }

                     entity.getPersistentData().putDouble("Atk10", 0.0);
                     entity.getPersistentData().putBoolean("Attack10", false);
                  }
               } else if (entity.getPersistentData().getDouble("Atk10") == 1.0) {
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
                           "/data merge entity @e[type=deepnight:dawn_corrupted_ogre,limit=1,sort=nearest] {NoAI:0}"
                        );
                  }

                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk10", 0.0);
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
                           "/data merge entity @e[type=deepnight:dawn_corrupted_ogre,limit=1,sort=nearest] {NoAI:0}"
                        );
                  }

                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putDouble("Atk10", 0.0);
                  entity.getPersistentData().putBoolean("Attack10", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack10b")) {
               if (entity.getPersistentData().getDouble("Atk10b") == 0.0) {
                  entity.getPersistentData().putDouble("Atk10b", 40.0);
               } else {
                  entity.getPersistentData().putDouble("Atk10b", entity.getPersistentData().getDouble("Atk10b") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk10b") == 40.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.BASALT));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.BASALT));
                  }

                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 7, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("Atk10b") == 37.0) {
                  entity.getPersistentData().putBoolean("nofollow", true);
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(25.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        entity.getPersistentData().putDouble("ei_x", entityiterator.getX());
                        entity.getPersistentData().putDouble("ei_y", entityiterator.getY() + 3.0);
                        entity.getPersistentData().putDouble("ei_z", entityiterator.getZ());
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk10b") == 34.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(25.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator instanceof LostEyeOgreEntity) {
                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("item.chorus_fruit.teleport"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 0.7F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("item.chorus_fruit.teleport"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 0.7F,
                                 false
                              );
                           }
                        }

                        Entity _ent = entityiterator;
                        _ent.teleportTo(
                           entity.getPersistentData().getDouble("ei_x"),
                           entity.getPersistentData().getDouble("ei_y"),
                           entity.getPersistentData().getDouble("ei_z")
                        );
                        if (_ent instanceof ServerPlayer _serverPlayer) {
                           _serverPlayer.connection
                              .teleport(
                                 entity.getPersistentData().getDouble("ei_x"),
                                 entity.getPersistentData().getDouble("ei_y"),
                                 entity.getPersistentData().getDouble("ei_z"),
                                 _ent.getYRot(),
                                 _ent.getXRot()
                              );
                        }

                        if (world instanceof ServerLevel _level) {
                           _level.getServer()
                              .getCommands()
                              .performPrefixedCommand(
                                 new CommandSourceStack(
                                       CommandSource.NULL,
                                       new Vec3(
                                          entity.getPersistentData().getDouble("ei_x"),
                                          entity.getPersistentData().getDouble("ei_y"),
                                          entity.getPersistentData().getDouble("ei_z")
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
                                 "/particle dust 1.000 0.100 0.100 3 ~ ~ ~ 0.5 0.5 0.5 0 10 force"
                              );
                        }
                     }
                  }
               } else if (entity.getPersistentData().getDouble("Atk10b") == 14.0) {
                  entity.getPersistentData().putBoolean("fire", true);
               } else if (entity.getPersistentData().getDouble("Atk10b") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("fire", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("Atk10b", 0.0);
                  entity.getPersistentData().putBoolean("Attack10b", false);
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("fire", false);
                  entity.getPersistentData().putDouble("Atk10b", 0.0);
                  entity.getPersistentData().putBoolean("Attack10b", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Stunned")) {
               if (entity.getPersistentData().getDouble("stun") == 0.0) {
                  entity.getPersistentData().putDouble("stun", 71.0);
               } else {
                  entity.getPersistentData().putDouble("stun", entity.getPersistentData().getDouble("stun") - 1.0);
               }

               if (entity.getPersistentData().getDouble("stun") == 71.0) {
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
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.POPPY));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.POPPY));
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 71, 7, false, false));
                  }

                  entity.getPersistentData().putDouble("xz", entity.getYRot());
                  entity.getPersistentData().putDouble("y", entity.getXRot());
               } else if (entity.getPersistentData().getDouble("stun") == 70.0) {
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

                  entity.getPersistentData().putDouble("Poise", -50.0);
                  entity.getPersistentData().putDouble("stun", 0.0);
                  entity.getPersistentData().putBoolean("Stunned", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Close")) {
               if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0F)
                     < (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1.0F) * 0.75
                  && entity.getPersistentData().getDouble("Atk7CD") == 0.0
                  && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack7", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (Math.random() > 0.2
                  && Math.random() <= 0.35
                  && entity.getPersistentData().getBoolean("2")
                  && entity.getPersistentData().getDouble("Atk9CD") == 0.0
                  && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack9", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (Math.random() > 0.35
                  && Math.random() <= 0.5
                  && entity.getPersistentData().getBoolean("2")
                  && entity.getPersistentData().getDouble("Atk10CD") == 0.0
                  && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack10", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (Math.random() >= 0.51 && Math.random() <= 0.6 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack1", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (Math.random() >= 0.61 && Math.random() <= 0.7 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack2", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (Math.random() >= 0.71 && Math.random() <= 0.8 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack3", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (Math.random() >= 0.81 && Math.random() <= 0.9 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack5", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (Math.random() >= 0.91 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack6", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               }
            }

            if (entity.getPersistentData().getBoolean("Far")) {
               if (entity instanceof LivingEntity _entity) {
                  _entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 10, 1, false, false));
               }

               if (entity.getPersistentData().getBoolean("2")) {
                  if (!entity.getPersistentData().getBoolean("Attacking") && entity.getPersistentData().getDouble("Atk7CD") == 0.0) {
                     entity.getPersistentData().putBoolean("Attack7", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (!entity.getPersistentData().getBoolean("Attacking") && entity.getPersistentData().getDouble("Atk4CD") == 0.0) {
                     entity.getPersistentData().putBoolean("Attack4G", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (!entity.getPersistentData().getBoolean("Attacking") && entity.getPersistentData().getDouble("Atk8CD") == 0.0) {
                     entity.getPersistentData().putBoolean("Attack8", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  }
               } else if (!entity.getPersistentData().getBoolean("Attacking")
                  && (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0F)
                     < (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1.0F) * 0.75
                  && entity.getPersistentData().getDouble("Atk7CD") == 0.0) {
                  entity.getPersistentData().putBoolean("Attack7", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (!entity.getPersistentData().getBoolean("Attacking") && entity.getPersistentData().getDouble("Atk4CD") == 0.0) {
                  entity.getPersistentData().putBoolean("Attack4", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               }
            }

            if (!entity.getPersistentData().getBoolean("Aggro")) {
               if (entity.getPersistentData().getDouble("idleCD") == 0.0) {
                  entity.getPersistentData().putDouble("idleCD", 400.0);
               } else {
                  entity.getPersistentData().putDouble("idleCD", entity.getPersistentData().getDouble("idleCD") - 1.0);
               }

               if (Math.random() <= 0.75 && entity.getPersistentData().getDouble("idleCD") == 1.0 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("sit", true);
               }
            }

            if (entity.getPersistentData().getBoolean("sit")) {
               if (entity.getPersistentData().getDouble("sittimer") == 0.0) {
                  entity.getPersistentData().putDouble("sittimer", 41.0);
               } else {
                  entity.getPersistentData().putDouble("sittimer", entity.getPersistentData().getDouble("sittimer") - 1.0);
               }

               if (entity.getPersistentData().getDouble("sittimer") == 41.0) {
                  entity.getPersistentData().putDouble("yaw", entity.getYRot());
                  entity.getPersistentData().putDouble("pitch", entity.getXRot());
                  entity.getPersistentData().putBoolean("lock", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.ROSE_BUSH));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.ROSE_BUSH));
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 7, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("sittimer") == 16.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("block.anvil.land"),
                           SoundSource.NEUTRAL,
                           0.5F,
                           -1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("block.anvil.land"), SoundSource.NEUTRAL, 0.5F, -1.0F, false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("sittimer") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.KELP));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.KELP));
                  }

                  entity.getPersistentData().putBoolean("sitting", true);
                  entity.getPersistentData().putBoolean("sit", false);
               }
            }

            if (entity.getPersistentData().getBoolean("sitting")) {
               if (entity instanceof LivingEntity _entity) {
                  _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 2, 7, false, false));
               }

               if (entity instanceof LivingEntity _entity) {
                  _entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2, 7, false, false));
               }

               if (entity.getPersistentData().getDouble("snore") == 0.0) {
                  entity.getPersistentData().putDouble("snore", 60.0);
                  if (Math.random() < 0.4 && world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ogre_sleep"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ogre_sleep"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F,
                           false
                        );
                     }
                  }
               } else {
                  entity.getPersistentData().putDouble("snore", entity.getPersistentData().getDouble("snore") - 1.0);
               }

               if (entity.getPersistentData().getBoolean("Aggro")) {
                  entity.getPersistentData().putBoolean("up", true);
                  entity.getPersistentData().putBoolean("sitting", false);
               } else if (entity.getPersistentData().getDouble("idleCD") == 1.0 && Math.random() <= 0.2) {
                  entity.getPersistentData().putBoolean("up", true);
                  entity.getPersistentData().putBoolean("sitting", false);
               }
            }

            if (entity.getPersistentData().getBoolean("up")) {
               if (entity.getPersistentData().getDouble("uptimer") == 0.0) {
                  entity.getPersistentData().putDouble("uptimer", 41.0);
               } else {
                  entity.getPersistentData().putDouble("uptimer", entity.getPersistentData().getDouble("uptimer") - 1.0);
               }

               if (entity.getPersistentData().getDouble("uptimer") == 41.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.LILAC));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.LILAC));
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 7, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("uptimer") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("up", false);
               }
            }

            if (entity.onGround()
               && Math.sqrt(entity.getDeltaMovement().x() * entity.getDeltaMovement().x() + entity.getDeltaMovement().z() * entity.getDeltaMovement().z())
                  > 0.05) {
               if (entity.getPersistentData().getDouble("walk") == 0.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ogre_step"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ogre_step"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  entity.getPersistentData().putDouble("walk", 15.0);
               } else {
                  entity.getPersistentData().putDouble("walk", entity.getPersistentData().getDouble("walk") - 1.0);
               }
            }
         }
      }
   }
}
