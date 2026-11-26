package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.GroundSpikeEntity;
import dev.deepnight.source.dawn.entity.SentinelFlashEntity;
import dev.deepnight.source.dawn.entity.SentinelKnightEntity;
import dev.deepnight.source.dawn.entity.SentinelSlashEntity;
import dev.deepnight.source.dawn.entity.SwordProjectileEntity;
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

public class ProsentinelProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (entity.isAlive()) {
            double i = 0.0;
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
               if (entity.getPersistentData().getDouble("Poise") > 0.0 && !entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putDouble("Poise", entity.getPersistentData().getDouble("Poise") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Poise") > 560.0 && !entity.getPersistentData().getBoolean("Stunned")) {
                  entity.getPersistentData().putBoolean("Stunned", true);
               }

               entity.getPersistentData().putBoolean("Aggro", false);
               entity.getPersistentData().putBoolean("Far", false);
               entity.getPersistentData().putBoolean("Far2", false);
               entity.getPersistentData().putBoolean("Mid", false);
               entity.getPersistentData().putBoolean("Close", false);
               entity.getPersistentData().putBoolean("Height", false);
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

               Vec3 _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(60.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                     if (Math.abs(entityiterator.getY() - entity.getY()) >= 2.0) {
                        entity.getPersistentData().putBoolean("Height", true);
                     }

                     entity.getPersistentData().putBoolean("Aggro", true);
                     entity.getPersistentData().putBoolean("Far2", true);
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
                                       / Math.abs(entity.getY() - entityiterator.getY())
                                 )
                              )
                        );
                  }
               }

               _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(12.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                     entity.getPersistentData().putBoolean("Far", true);
                     entity.getPersistentData().putBoolean("Far2", false);
                     entity.getPersistentData().putBoolean("Mid", false);
                     entity.getPersistentData().putBoolean("Close", false);
                  }
               }

               _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                     entity.getPersistentData().putBoolean("Far", false);
                     entity.getPersistentData().putBoolean("Mid", true);
                     entity.getPersistentData().putBoolean("Close", false);
                  }
               }

               _center = new Vec3(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                     entity.getPersistentData().putBoolean("Far", false);
                     entity.getPersistentData().putBoolean("Mid", false);
                     entity.getPersistentData().putBoolean("Close", true);
                  }
               }

               if (!entity.getPersistentData().getBoolean("Blocking")
                  && !entity.getPersistentData().getBoolean("Aggro")
                  && !entity.getPersistentData().getBoolean("Attacking")
                  && !entity.getPersistentData().getBoolean("Shieldoff")) {
                  if (entity.getPersistentData().getDouble("guardCD") == 0.0) {
                     entity.getPersistentData().putDouble("guardCD", 300.0);
                  } else {
                     entity.getPersistentData().putDouble("guardCD", entity.getPersistentData().getDouble("guardCD") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("guardCD") == 1.0) {
                     entity.getPersistentData().putBoolean("Guarda", true);
                  }
               }

               if (entity.getPersistentData().getBoolean("Guarda")) {
                  if (entity.getPersistentData().getDouble("guardatimer") == 0.0) {
                     entity.getPersistentData().putDouble("guardatimer", 51.0);
                  } else {
                     entity.getPersistentData().putDouble("guardatimer", entity.getPersistentData().getDouble("guardatimer") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("guardatimer") == 51.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.SAND));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.SAND));
                     }

                     entity.getPersistentData().putDouble("yaw", entity.getYRot());
                     entity.getPersistentData().putDouble("pitch", entity.getXRot());
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 51, 8, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("guardatimer") == 50.0) {
                     entity.getPersistentData().putBoolean("lock", true);
                  } else if (entity.getPersistentData().getDouble("guardatimer") == 1.0) {
                     entity.getPersistentData().putBoolean("Guardb", true);
                     entity.getPersistentData().putDouble("guardatimer", 0.0);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.VINE));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.VINE));
                     }

                     entity.getPersistentData().putBoolean("Guarda", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Guardb")) {
                  if (entity.getPersistentData().getDouble("guardbtimer") == 0.0) {
                     entity.getPersistentData().putDouble("guardbtimer", 10.0);
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 8, false, false));
                     }

                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.VINE));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.VINE));
                     }
                  } else {
                     entity.getPersistentData().putDouble("guardbtimer", entity.getPersistentData().getDouble("guardbtimer") - 1.0);
                  }

                  if (entity.getPersistentData().getBoolean("Aggro")) {
                     entity.getPersistentData().putBoolean("Guardc", true);
                     entity.getPersistentData().putBoolean("Guardb", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Guardc")) {
                  if (entity.getPersistentData().getDouble("guardctimer") == 0.0) {
                     entity.getPersistentData().putDouble("guardctimer", 31.0);
                  } else {
                     entity.getPersistentData().putDouble("guardctimer", entity.getPersistentData().getDouble("guardctimer") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("guardctimer") == 31.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.CACTUS));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.CACTUS));
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 31, 8, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("guardctimer") == 1.0) {
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putDouble("guardctimer", 0.0);
                     entity.getPersistentData().putBoolean("Guardc", false);
                  }
               }

               if (!entity.getPersistentData().getBoolean("Shieldoff")
                  && entity.getPersistentData().getBoolean("Aggro")
                  && !entity.getPersistentData().getBoolean("Blocking")
                  && !entity.getPersistentData().getBoolean("Attacking")) {
                  if (entity.getPersistentData().getDouble("blockCD") == 0.0) {
                     entity.getPersistentData().putDouble("blockCD", 60.0);
                  } else {
                     entity.getPersistentData().putDouble("blockCD", entity.getPersistentData().getDouble("blockCD") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("blockCD") == 1.0) {
                     entity.getPersistentData().putBoolean("Block", true);
                  }
               }

               if (entity.getPersistentData().getBoolean("Block")) {
                  if (entity.getPersistentData().getDouble("blocktimer") == 0.0) {
                     entity.getPersistentData().putDouble("blocktimer", 11.0);
                  } else {
                     entity.getPersistentData().putDouble("blocktimer", entity.getPersistentData().getDouble("blocktimer") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("blocktimer") == 11.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.LILY_PAD));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.LILY_PAD));
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 11, 8, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("blocktimer") == 1.0) {
                     entity.getPersistentData().putBoolean("Shieldon", true);
                     entity.getPersistentData().putBoolean("Shieldoff", false);
                     entity.getPersistentData().putBoolean("Blocking", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     Entity _entityx = entity;
                     if (_entityx instanceof Player _player) {
                        _player.getInventory().armor.set(2, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
                        _player.getInventory().setChanged();
                     } else if (_entityx instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Block", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Blocking")) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 4, 1, false, false));
                  }

                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 4, 3, false, false));
                  }
               }

               if (!entity.getPersistentData().getBoolean("Shieldoff")
                  && !entity.getPersistentData().getBoolean("Aggro")
                  && entity.getPersistentData().getBoolean("Blocking")
                  && !entity.getPersistentData().getBoolean("Attacking")) {
                  if (entity.getPersistentData().getDouble("unblockCD") == 0.0) {
                     entity.getPersistentData().putDouble("unblockCD", 100.0);
                  } else {
                     entity.getPersistentData().putDouble("unblockCD", entity.getPersistentData().getDouble("unblockCD") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("unblockCD") == 1.0) {
                     entity.getPersistentData().putBoolean("Unblock", true);
                  }
               }

               if (entity.getPersistentData().getBoolean("Unblock")) {
                  if (entity.getPersistentData().getDouble("unblocktimer") == 0.0) {
                     entity.getPersistentData().putDouble("unblocktimer", 11.0);
                  } else {
                     entity.getPersistentData().putDouble("unblocktimer", entity.getPersistentData().getDouble("unblocktimer") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("unblocktimer") == 11.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.GLASS));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.GLASS));
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 11, 8, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("unblocktimer") == 1.0) {
                     entity.getPersistentData().putBoolean("Shieldon", true);
                     entity.getPersistentData().putBoolean("Shieldoff", false);
                     entity.getPersistentData().putBoolean("Blocking", false);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     Entity _entityx = entity;
                     if (_entityx instanceof Player _player) {
                        _player.getInventory().armor.set(2, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entityx instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Unblock", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Aggro")
                  && entity.getPersistentData().getBoolean("Shieldon")
                  && !entity.getPersistentData().getBoolean("Shieldoff")
                  && !entity.getPersistentData().getBoolean("Attacking")) {
                  if (entity.getPersistentData().getDouble("swordCD") == 0.0) {
                     entity.getPersistentData().putDouble("swordCD", 200.0);
                  } else {
                     entity.getPersistentData().putDouble("swordCD", entity.getPersistentData().getDouble("swordCD") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("swordCD") == 1.0 && Math.random() > 0.5) {
                     entity.getPersistentData().putBoolean("Sword", true);
                  }
               }

               if (entity.getPersistentData().getBoolean("Sword")) {
                  if (entity.getPersistentData().getDouble("swordtimer") == 0.0) {
                     entity.getPersistentData().putDouble("swordtimer", 16.0);
                  } else {
                     entity.getPersistentData().putDouble("swordtimer", entity.getPersistentData().getDouble("swordtimer") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("swordtimer") == 16.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.LILY_OF_THE_VALLEY));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.LILY_OF_THE_VALLEY));
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 16, 8, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("swordtimer") == 1.0) {
                     entity.getPersistentData().putBoolean("Shieldoff", true);
                     entity.getPersistentData().putBoolean("Blocking", false);
                     entity.getPersistentData().putBoolean("Shieldon", false);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     Entity _entityx = entity;
                     if (_entityx instanceof Player _player) {
                        _player.getInventory().armor.set(2, new ItemStack(Items.LEATHER_CHESTPLATE));
                        _player.getInventory().setChanged();
                     } else if (_entityx instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
                     }

                     entity.getPersistentData().putDouble("swordtimer", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Sword", false);
                  }
               }

               if (!entity.getPersistentData().getBoolean("Shieldon")
                  && entity.getPersistentData().getBoolean("Shieldoff")
                  && !entity.getPersistentData().getBoolean("Attacking")) {
                  if (entity.getPersistentData().getDouble("OffCD") == 0.0) {
                     entity.getPersistentData().putDouble("OffCD", 180.0);
                  } else {
                     entity.getPersistentData().putDouble("OffCD", entity.getPersistentData().getDouble("OffCD") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("OffCD") == 1.0 && Math.random() > 0.5) {
                     entity.getPersistentData().putBoolean("Off", true);
                  }
               }

               if (entity.getPersistentData().getBoolean("Off")) {
                  if (entity.getPersistentData().getDouble("offtimer") == 0.0) {
                     entity.getPersistentData().putDouble("offtimer", 16.0);
                  } else {
                     entity.getPersistentData().putDouble("offtimer", entity.getPersistentData().getDouble("offtimer") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("offtimer") == 16.0) {
                     entity.getPersistentData().putBoolean("Attacking", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.LILAC));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.LILAC));
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 16, 8, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("offtimer") == 1.0) {
                     entity.getPersistentData().putBoolean("Shieldoff", false);
                     entity.getPersistentData().putBoolean("Blocking", false);
                     entity.getPersistentData().putBoolean("Shieldon", true);
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     Entity _entityx = entity;
                     if (_entityx instanceof Player _player) {
                        _player.getInventory().armor.set(2, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entityx instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putDouble("offtimer", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("Off", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Mid")
                  && !entity.getPersistentData().getBoolean("Attacking")
                  && entity.getPersistentData().getBoolean("Blocking")) {
                  if (Math.random() >= 0.5 && entity.getPersistentData().getDouble("bAtk2CD") == 0.0 && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("b_attack2", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (Math.random() < 0.5
                     && entity.getPersistentData().getDouble("bAtk4CD") == 0.0
                     && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("b_attack4", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  }
               }

               if (entity.getPersistentData().getBoolean("Close")
                  && !entity.getPersistentData().getBoolean("Attacking")
                  && entity.getPersistentData().getBoolean("Blocking")) {
                  if (Math.random() >= 0.66 && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("b_attack1", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (Math.random() >= 0.33
                     && Math.random() < 0.66
                     && entity.getPersistentData().getDouble("bAtk3CD") == 0.0
                     && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("b_attack3", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (Math.random() < 0.33 && !entity.getPersistentData().getBoolean("Attacking")) {
                     entity.getPersistentData().putBoolean("b_attack5", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  }
               }

               if (entity.getPersistentData().getBoolean("spin")) {
                  entity.getPersistentData().putDouble("direction", Math.abs(entity.getYRot()) / 360.0F * 12.0F);
                  if (entity.getPersistentData().getDouble("direction") + i >= 12.0) {
                     entity.getPersistentData().putDouble("direction", entity.getPersistentData().getDouble("direction") - 12.0);
                  }

                  if (i < 12.0) {
                     _center = new Vec3(
                        x + Math.cos((Math.PI / 6) * (i + entity.getPersistentData().getDouble("direction"))) * 2.0,
                        y,
                        z + Math.sin((Math.PI / 6) * (i + entity.getPersistentData().getDouble("direction"))) * 2.0
                     );

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof SentinelKnightEntity)) {
                           if (entityiterator instanceof LivingEntity _livEnt ? _livEnt.isBlocking() : false) {
                              if ((entityiterator instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == Items.SHIELD) {
                                 ItemStack _ist = entityiterator instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY;
                                 if (_ist.hurt(16, net.minecraft.util.RandomSource.create(), null)) {
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
                                 .setImpact(15.0F)
                                 .setArmorNegation(4.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 18.0F);
                              entityiterator.setDeltaMovement(
                                 new Vec3((entityiterator.getX() - entity.getX()) / 3.0, 0.4, (entityiterator.getZ() - entity.getZ()) / 3.0)
                              );
                           }
                        }
                     }

                     i++;
                  }
               }

               if (entity.getPersistentData().getDouble("bAtk2CD") == 0.0 && entity.getPersistentData().getBoolean("b_attack2")) {
                  entity.getPersistentData().putDouble("bAtk2CD", 200.0);
               } else if (entity.getPersistentData().getDouble("bAtk2CD") > 0.0) {
                  entity.getPersistentData().putDouble("bAtk2CD", entity.getPersistentData().getDouble("bAtk2CD") - 1.0);
               }

               if (entity.getPersistentData().getDouble("bAtk3CD") == 0.0 && entity.getPersistentData().getBoolean("b_attack3")) {
                  entity.getPersistentData().putDouble("bAtk3CD", 160.0);
               } else if (entity.getPersistentData().getDouble("bAtk3CD") > 0.0) {
                  entity.getPersistentData().putDouble("bAtk3CD", entity.getPersistentData().getDouble("bAtk3CD") - 1.0);
               }

               if (entity.getPersistentData().getDouble("bAtk4CD") == 0.0 && entity.getPersistentData().getBoolean("b_attack4")) {
                  entity.getPersistentData().putDouble("bAtk4CD", 200.0);
               } else if (entity.getPersistentData().getDouble("bAtk4CD") > 0.0) {
                  entity.getPersistentData().putDouble("bAtk4CD", entity.getPersistentData().getDouble("bAtk4CD") - 1.0);
               }

               if (entity.getPersistentData().getDouble("sAtk2CD") == 0.0 && entity.getPersistentData().getBoolean("s_attack2")) {
                  entity.getPersistentData().putDouble("sAtk2CD", 180.0);
               } else if (entity.getPersistentData().getDouble("sAtk2CD") > 0.0) {
                  entity.getPersistentData().putDouble("sAtk2CD", entity.getPersistentData().getDouble("sAtk2CD") - 1.0);
               }

               if (entity.getPersistentData().getDouble("sAtk3CD") == 0.0 && entity.getPersistentData().getBoolean("s_attack3")) {
                  entity.getPersistentData().putDouble("sAtk3CD", 140.0);
               } else if (entity.getPersistentData().getDouble("sAtk3CD") > 0.0) {
                  entity.getPersistentData().putDouble("sAtk3CD", entity.getPersistentData().getDouble("sAtk3CD") - 1.0);
               }

               if (entity.getPersistentData().getDouble("sAtk4CD") == 0.0 && entity.getPersistentData().getBoolean("s_attack4")) {
                  entity.getPersistentData().putDouble("sAtk4CD", Mth.nextInt(net.minecraft.util.RandomSource.create(), 200, 350));
               } else if (entity.getPersistentData().getDouble("sAtk4CD") > 0.0) {
                  entity.getPersistentData().putDouble("sAtk4CD", entity.getPersistentData().getDouble("sAtk4CD") - 1.0);
               }

               if (entity.getPersistentData().getDouble("sAtk5CD") == 0.0 && entity.getPersistentData().getBoolean("s_attack5")) {
                  entity.getPersistentData().putDouble("sAtk5CD", Mth.nextInt(net.minecraft.util.RandomSource.create(), 200, 300));
               } else if (entity.getPersistentData().getDouble("sAtk5CD") > 0.0) {
                  entity.getPersistentData().putDouble("sAtk5CD", entity.getPersistentData().getDouble("sAtk5CD") - 1.0);
               }

               if (entity.getPersistentData().getDouble("sAtk6CD") == 0.0 && entity.getPersistentData().getBoolean("s_attack6")) {
                  entity.getPersistentData().putDouble("sAtk6CD", Mth.nextInt(net.minecraft.util.RandomSource.create(), 360, 500));
               } else if (entity.getPersistentData().getDouble("sAtk6CD") > 0.0) {
                  entity.getPersistentData().putDouble("sAtk6CD", entity.getPersistentData().getDouble("sAtk6CD") - 1.0);
               }

               if (entity.getPersistentData().getDouble("sAtk7CD") == 0.0 && entity.getPersistentData().getBoolean("s_attack7")) {
                  entity.getPersistentData().putDouble("sAtk7CD", Mth.nextInt(net.minecraft.util.RandomSource.create(), 140, 240));
               } else if (entity.getPersistentData().getDouble("sAtk7CD") > 0.0) {
                  entity.getPersistentData().putDouble("sAtk7CD", entity.getPersistentData().getDouble("sAtk7CD") - 1.0);
               }

               if (entity.getPersistentData().getDouble("sAtk8CD") == 0.0 && entity.getPersistentData().getBoolean("s_attack8")) {
                  entity.getPersistentData().putDouble("sAtk8CD", Mth.nextInt(net.minecraft.util.RandomSource.create(), 140, 250));
               } else if (entity.getPersistentData().getDouble("sAtk8CD") > 0.0) {
                  entity.getPersistentData().putDouble("sAtk8CD", entity.getPersistentData().getDouble("sAtk8CD") - 1.0);
               }

               if (entity.getPersistentData().getBoolean("b_attack1")) {
                  if (entity.getPersistentData().getDouble("bAtk1") == 0.0) {
                     entity.getPersistentData().putDouble("bAtk1", 26.0);
                  } else {
                     entity.getPersistentData().putDouble("bAtk1", entity.getPersistentData().getDouble("bAtk1") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("bAtk1") == 26.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.LEATHER_HELMET));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 26, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("bAtk1") == 14.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              3.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              3.0F,
                              false
                           );
                        }
                     }

                     for (double var19 = 0.0; var19 < 3.0; var19++) {
                        _center = new Vec3(x + entity.getLookAngle().x * var19, y + 1.0, z + entity.getLookAngle().z * var19);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.0), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (!(entityiterator instanceof SentinelKnightEntity)) {
                              SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                 .setInitialPosition(entity.position())
                                 .setStunType(StunType.SHORT)
                                 .setImpact(15.0F)
                                 .setArmorNegation(4.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 18.0F);
                           }
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("bAtk1") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("bAtk1", 0.0);
                     entity.getPersistentData().putBoolean("b_attack1", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putBoolean("b_attack1", false);
                     entity.getPersistentData().putDouble("bAtk1", 0.0);
                  }
               }

               if (entity.getPersistentData().getBoolean("b_attack2")) {
                  if (entity.getPersistentData().getDouble("bAtk2") == 0.0) {
                     entity.getPersistentData().putDouble("bAtk2", 71.0);
                  } else {
                     entity.getPersistentData().putDouble("bAtk2", entity.getPersistentData().getDouble("bAtk2") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("bAtk2") == 71.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.GOLD_NUGGET));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLD_NUGGET));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 12, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("bAtk2") == 59.0) {
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 33, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("bAtk2") == 26.0) {
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 26, 6, false, false));
                     }

                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              -1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              -1.0F,
                              false
                           );
                        }
                     }

                     _center = new Vec3(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof SentinelKnightEntity)) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.KNOCKDOWN)
                              .setImpact(15.0F)
                              .setArmorNegation(6.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 20.0F);
                           entityiterator.setDeltaMovement(
                              new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 90.0F)), 0.7, Math.cos(Math.toRadians(entity.getYRot() + 270.0F)))
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("bAtk2") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("bAtk2", 0.0);
                     entity.getPersistentData().putBoolean("b_attack2", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putBoolean("b_attack2", false);
                     entity.getPersistentData().putDouble("bAtk2", 0.0);
                  }
               }

               if (entity.getPersistentData().getBoolean("b_attack3")) {
                  if (entity.getPersistentData().getDouble("bAtk3") == 0.0) {
                     entity.getPersistentData().putDouble("bAtk3", 31.0);
                  } else {
                     entity.getPersistentData().putDouble("bAtk3", entity.getPersistentData().getDouble("bAtk3") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("bAtk3") == 31.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.COAL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.COAL));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 31, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("bAtk3") == 19.0) {
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

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof SentinelKnightEntity)) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.LONG)
                              .setImpact(15.0F)
                              .setArmorNegation(6.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 14.0F);
                           if (entityiterator instanceof LivingEntity _entity) {
                              _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 3, false, false));
                           }
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("bAtk3") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("bAtk3", 0.0);
                     entity.getPersistentData().putBoolean("b_attack3", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putBoolean("b_attack3", false);
                     entity.getPersistentData().putDouble("bAtk3", 0.0);
                  }
               }

               if (entity.getPersistentData().getBoolean("b_attack4")) {
                  if (entity.getPersistentData().getDouble("bAtk4") == 0.0) {
                     entity.getPersistentData().putDouble("bAtk4", 61.0);
                  } else {
                     entity.getPersistentData().putDouble("bAtk4", entity.getPersistentData().getDouble("bAtk4") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("bAtk4") == 61.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.CHARCOAL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHARCOAL));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 6, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("bAtk4") == 55.0) {
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 33, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("bAtk4") == 25.0) {
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

                     entity.getPersistentData().putBoolean("Blocking", false);
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 25, 6, false, false));
                     }

                     _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof SentinelKnightEntity)) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.LONG)
                              .setImpact(15.0F)
                              .setArmorNegation(2.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 24.0F);
                           entityiterator.setDeltaMovement(
                              new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 90.0F)) * 0.3, 0.8, Math.cos(Math.toRadians(entity.getYRot() + 270.0F)) * 0.3)
                           );
                           if (entityiterator instanceof LivingEntity _entity) {
                              _entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80, 2, false, false));
                           }
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("bAtk4") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Blocking", true);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("bAtk4", 0.0);
                     entity.getPersistentData().putBoolean("b_attack4", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putBoolean("b_attack4", false);
                     entity.getPersistentData().putDouble("bAtk4", 0.0);
                  }
               }

               if (entity.getPersistentData().getBoolean("b_attack5")) {
                  if (entity.getPersistentData().getDouble("bAtk5") == 0.0) {
                     entity.getPersistentData().putDouble("bAtk5", 31.0);
                  } else {
                     entity.getPersistentData().putDouble("bAtk5", entity.getPersistentData().getDouble("bAtk5") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("bAtk5") == 31.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.IRON_INGOT));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_INGOT));
                     }

                     entity.getPersistentData().putBoolean("focus", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 7, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("bAtk5") == 24.0) {
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 12, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("bAtk5") == 20.0) {
                     entity.getPersistentData().putBoolean("Blocking", false);
                     _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof SentinelKnightEntity)) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.SHORT)
                              .setImpact(6.0F)
                              .setArmorNegation(6.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 18.0F);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("bAtk5") == 14.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 2.5, y, z + entity.getLookAngle().z * 2.5);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                           entity.getPersistentData().putBoolean("b_attack5b", true);
                           entity.getPersistentData().putBoolean("b_attack5", false);
                           entity.getPersistentData().putDouble("bAtk5", 0.0);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("bAtk5") == 12.0) {
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 12, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("bAtk5") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Blocking", true);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("bAtk5", 0.0);
                     entity.getPersistentData().putBoolean("b_attack5", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putBoolean("b_attack5", false);
                     entity.getPersistentData().putDouble("bAtk5", 0.0);
                  }
               }

               if (entity.getPersistentData().getBoolean("b_attack5b")) {
                  if (entity.getPersistentData().getDouble("bAtk5b") == 0.0) {
                     entity.getPersistentData().putDouble("bAtk5b", 44.0);
                  } else {
                     entity.getPersistentData().putDouble("bAtk5b", entity.getPersistentData().getDouble("bAtk5b") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("bAtk5b") == 44.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.IRON_NUGGET));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_NUGGET));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 15, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("bAtk5b") == 32.0) {
                     entity.getPersistentData().putDouble("yaw", entity.getYRot());
                     entity.getPersistentData().putDouble("pitch", entity.getXRot());
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putBoolean("lock", true);
                  } else if (entity.getPersistentData().getDouble("bAtk5b") == 29.0) {
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 7, 8, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("bAtk5b") == 22.0) {
                     for (double var20 = 1.0; var20 < 4.0; var20++) {
                        _center = new Vec3(x + entity.getLookAngle().x * var20, y, z + entity.getLookAngle().z * var20);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (!(entityiterator instanceof SentinelKnightEntity)) {
                              SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                 .setInitialPosition(entity.position())
                                 .setStunType(StunType.SHORT)
                                 .setImpact(12.0F)
                                 .setArmorNegation(6.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 18.0F);
                           }
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("bAtk5b") == 23.0) {
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 8, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("bAtk5b") == 15.0) {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(6.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                           entity.getPersistentData().putBoolean("b_attack5c", true);
                           entity.getPersistentData().putDouble("bAtk5b", 0.0);
                           entity.getPersistentData().putBoolean("b_attack5b", false);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("bAtk5b") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putBoolean("Blocking", true);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("bAtk5b", 0.0);
                     entity.getPersistentData().putBoolean("b_attack5b", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putBoolean("b_attack5b", false);
                     entity.getPersistentData().putDouble("bAtk5b", 0.0);
                  }
               }

               if (entity.getPersistentData().getBoolean("b_attack5c")) {
                  if (entity.getPersistentData().getDouble("bAtk5c") == 0.0) {
                     entity.getPersistentData().putDouble("bAtk5c", 57.0);
                  } else {
                     entity.getPersistentData().putDouble("bAtk5c", entity.getPersistentData().getDouble("bAtk5c") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("bAtk5c") == 57.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.WHITE_WOOL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.WHITE_WOOL));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("bAtk5c") == 52.0) {
                     i = 0.0;
                     entity.getPersistentData().putBoolean("spin", true);
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
                  } else if (entity.getPersistentData().getDouble("bAtk5c") == 39.0) {
                     entity.getPersistentData().putBoolean("spin", false);
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 6, 2, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("bAtk5c") == 33.0) {
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 8, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("bAtk5c") == 26.0) {
                     entity.getPersistentData().putDouble("pitch", entity.getXRot());
                     entity.getPersistentData().putDouble("yaw", entity.getYRot());
                  } else if (entity.getPersistentData().getDouble("bAtk5c") == 25.0) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_sentinel_knight,limit=1,sort=nearest] {NoAI:1b}"
                           );
                     }

                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
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
                              dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              -1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              -1.0F,
                              false
                           );
                        }
                     }

                     _center = new Vec3(x + entity.getLookAngle().x * 1.5, y + 1.0, z + entity.getLookAngle().z * 1.5);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof SentinelKnightEntity)) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.SHORT)
                              .setImpact(11.0F)
                              .setArmorNegation(3.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 20.0F);
                        }
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(
                           ParticleTypes.SWEEP_ATTACK, x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5, 10, 0.0, 2.0, 0.0, 0.2
                        );
                     }

                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 24, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("bAtk5c") == 2.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.WHITE_CONCRETE));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.WHITE_CONCRETE));
                     }
                  } else if (entity.getPersistentData().getDouble("bAtk5c") == 1.0) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_sentinel_knight,limit=1,sort=nearest] {NoAI:0}"
                           );
                     }

                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     Entity _entityx = entity;
                     if (_entityx instanceof Player _player) {
                        _player.getInventory().armor.set(2, new ItemStack(Items.LEATHER_CHESTPLATE));
                        _player.getInventory().setChanged();
                     } else if (_entityx instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
                     }

                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putBoolean("Blocking", false);
                     entity.getPersistentData().putBoolean("Shieldoff", true);
                     entity.getPersistentData().putBoolean("Shieldon", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("bAtk5c", 0.0);
                     entity.getPersistentData().putBoolean("b_attack5c", false);
                  }

                  if (entity.getPersistentData().getBoolean("Stunned")) {
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putBoolean("b_attack5c", false);
                     entity.getPersistentData().putDouble("bAtk5c", 0.0);
                  }
               }

               if (entity.getPersistentData().getBoolean("Stunned")) {
                  if (entity.getPersistentData().getDouble("stuntimer") == 0.0) {
                     entity.getPersistentData().putDouble("stuntimer", 61.0);
                  } else {
                     entity.getPersistentData().putDouble("stuntimer", entity.getPersistentData().getDouble("stuntimer") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("stuntimer") == 61.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.POPPY));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.POPPY));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     entity.getPersistentData().putBoolean("Blocking", false);
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 61, 6, false, false));
                     }

                     entity.getPersistentData().putDouble("pitch", entity.getXRot());
                     entity.getPersistentData().putDouble("yaw", entity.getYRot());
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:poise"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x, y, z, dev.deepnight.SafeSounds.resolve("simple_mobs:poise"), SoundSource.NEUTRAL, 2.0F, 1.0F, false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("stuntimer") == 60.0) {
                     entity.getPersistentData().putBoolean("lock", true);
                  } else if (entity.getPersistentData().getDouble("stuntimer") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putBoolean("Blocking", true);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("stuntimer", 0.0);
                     entity.getPersistentData().putDouble("Poise", -90.0);
                     entity.getPersistentData().putBoolean("Stunned", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Shieldoff") && entity instanceof LivingEntity _entity) {
                  _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2, 2, false, false));
               }

               if (entity.getPersistentData().getDouble("sAtk7CD") == 0.0
                  && entity.getPersistentData().getBoolean("Far2")
                  && !entity.getPersistentData().getBoolean("Attacking")
                  && entity.getPersistentData().getBoolean("Shieldoff")) {
                  entity.getPersistentData().putBoolean("s_attack7", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               }

               if (entity.getPersistentData().getBoolean("Far")
                  && !entity.getPersistentData().getBoolean("Attacking")
                  && entity.getPersistentData().getBoolean("Shieldoff")) {
                  if (Math.random() >= 0.5 && entity.getPersistentData().getDouble("sAtk5CD") == 0.0) {
                     entity.getPersistentData().putBoolean("s_attack5", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (Math.random() < 0.5 && entity.getPersistentData().getDouble("sAtk4CD") == 0.0) {
                     entity.getPersistentData().putBoolean("s_attack4", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (entity.getPersistentData().getDouble("sAtk7CD") == 0.0 && entity.getPersistentData().getBoolean("Height")) {
                     entity.getPersistentData().putBoolean("s_attack7", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  }
               }

               if (entity.getPersistentData().getBoolean("Mid")
                  && !entity.getPersistentData().getBoolean("Attacking")
                  && entity.getPersistentData().getBoolean("Shieldoff")) {
                  if (entity.getPersistentData().getDouble("sAtk2CD") == 0.0) {
                     entity.getPersistentData().putBoolean("s_attack2", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (entity.getPersistentData().getDouble("sAtk8CD") == 0.0) {
                     entity.getPersistentData().putBoolean("s_attack8", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (entity.getPersistentData().getDouble("sAtk7CD") == 0.0 && entity.getPersistentData().getBoolean("Height")) {
                     entity.getPersistentData().putBoolean("s_attack7", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  }
               }

               if (entity.getPersistentData().getBoolean("Close")
                  && !entity.getPersistentData().getBoolean("Attacking")
                  && entity.getPersistentData().getBoolean("Shieldoff")) {
                  if (Math.random() >= 0.6) {
                     entity.getPersistentData().putBoolean("s_attack1", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (Math.random() <= 0.3 && entity.getPersistentData().getDouble("sAtk3CD") == 0.0) {
                     entity.getPersistentData().putBoolean("s_attack3", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else if (Math.random() > 0.3 && Math.random() < 0.6 && entity.getPersistentData().getDouble("sAtk6CD") == 0.0) {
                     entity.getPersistentData().putBoolean("s_attack6", true);
                     entity.getPersistentData().putDouble("sAtk6CD", 0.0);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  }
               }

               if (entity.getPersistentData().getBoolean("s_attack1")) {
                  if (entity.getPersistentData().getDouble("sAtk1") == 0.0) {
                     entity.getPersistentData().putDouble("sAtk1", 26.0);
                  } else {
                     entity.getPersistentData().putDouble("sAtk1", entity.getPersistentData().getDouble("sAtk1") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("sAtk1") == 26.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.RED_WOOL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.RED_WOOL));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 6, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("sAtk1") == 20.0) {
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 5, 7, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("sAtk1") == 17.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              -2.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              -2.0F,
                              false
                           );
                        }
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(
                           ParticleTypes.SWEEP_ATTACK, x + entity.getLookAngle().x * 2.0, y + 1.3, z + entity.getLookAngle().z * 2.0, 10, 2.0, 0.0, 2.0, 0.1
                        );
                     }

                     _center = new Vec3(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof SentinelKnightEntity)) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.SHORT)
                              .setImpact(12.0F)
                              .setArmorNegation(6.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 20.0F);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("sAtk1") == 14.0) {
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 14, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("sAtk1") == 10.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 3.0, y, z + entity.getLookAngle().z * 3.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof SentinelKnightEntity)) {
                           entity.getPersistentData().putBoolean("s_attack1b", true);
                           entity.getPersistentData().putBoolean("s_attack1", false);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("sAtk1") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("sAtk1", 0.0);
                     entity.getPersistentData().putBoolean("s_attack1", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("s_attack1b")) {
                  if (entity.getPersistentData().getDouble("sAtk1b") == 0.0) {
                     entity.getPersistentData().putDouble("sAtk1b", 26.0);
                  } else {
                     entity.getPersistentData().putDouble("sAtk1b", entity.getPersistentData().getDouble("sAtk1b") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("sAtk1b") == 26.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.YELLOW_WOOL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.YELLOW_WOOL));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 6, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("sAtk1b") == 20.0) {
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 5, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("sAtk1b") == 17.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              -3.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              -3.0F,
                              false
                           );
                        }
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(
                           ParticleTypes.SWEEP_ATTACK, x + entity.getLookAngle().x * 2.0, y + 1.3, z + entity.getLookAngle().z * 2.0, 10, 2.0, 0.0, 2.0, 0.1
                        );
                     }

                     _center = new Vec3(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof SentinelKnightEntity)) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.SHORT)
                              .setImpact(13.0F)
                              .setArmorNegation(6.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 20.0F);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("sAtk1b") == 14.0) {
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 14, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("sAtk1b") == 10.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 3.0, y, z + entity.getLookAngle().z * 3.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof SentinelKnightEntity)) {
                           entity.getPersistentData().putBoolean("s_attack1c", true);
                           entity.getPersistentData().putBoolean("s_attack1b", false);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("sAtk1b") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("sAtk1b", 0.0);
                     entity.getPersistentData().putBoolean("s_attack1b", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("s_attack1c")) {
                  if (entity.getPersistentData().getDouble("sAtk1c") == 0.0) {
                     entity.getPersistentData().putDouble("sAtk1c", 61.0);
                  } else {
                     entity.getPersistentData().putDouble("sAtk1c", entity.getPersistentData().getDouble("sAtk1c") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("sAtk1c") == 61.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.GREEN_WOOL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.GREEN_WOOL));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 6, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("sAtk1c") == 33.0) {
                     entity.getPersistentData().putDouble("pitch", entity.getXRot());
                     entity.getPersistentData().putDouble("yaw", entity.getYRot());
                  } else if (entity.getPersistentData().getDouble("sAtk1c") == 32.0) {
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 32, 6, false, false));
                     }

                     entity.getPersistentData().putBoolean("lock", true);
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              -3.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                              SoundSource.NEUTRAL,
                              1.0F,
                              -3.0F,
                              false
                           );
                        }
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(
                           ParticleTypes.SWEEP_ATTACK, x + entity.getLookAngle().x * 2.0, y + 1.3, z + entity.getLookAngle().z * 2.0, 10, 0.0, 1.0, 0.0, 0.1
                        );
                     }

                     _center = new Vec3(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof SentinelKnightEntity)) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.LONG)
                              .setImpact(13.0F)
                              .setArmorNegation(6.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 20.0F);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("sAtk1c") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putDouble("sAtk1c", 0.0);
                     entity.getPersistentData().putBoolean("s_attack1c", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("s_attack2")) {
                  if (entity.getPersistentData().getDouble("sAtk2") == 0.0) {
                     entity.getPersistentData().putDouble("sAtk2", 71.0);
                  } else {
                     entity.getPersistentData().putDouble("sAtk2", entity.getPersistentData().getDouble("sAtk2") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("sAtk2") == 71.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.BLACK_WOOL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.BLACK_WOOL));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 9, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("sAtk2") == 62.0) {
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 27, 5, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("sAtk2") != 26.0) {
                     if (entity.getPersistentData().getDouble("sAtk2") == 1.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                        }

                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putDouble("sAtk2", 0.0);
                        entity.getPersistentData().putBoolean("s_attack2", false);
                     }
                  } else {
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 26, 6, false, false));
                     }

                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              -3.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              -3.0F,
                              false
                           );
                        }
                     }

                     for (double var22 = 1.0; var22 < 4.0; var22++) {
                        _center = new Vec3(x + entity.getLookAngle().x * var22, y, z + entity.getLookAngle().z * var22);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (!(entityiterator instanceof SentinelKnightEntity)) {
                              SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                 .setInitialPosition(entity.position())
                                 .setStunType(StunType.SHORT)
                                 .setImpact(13.0F)
                                 .setArmorNegation(7.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 25.0F);
                           }
                        }
                     }
                  }
               }

               if (entity.getPersistentData().getBoolean("s_attack3")) {
                  if (entity.getPersistentData().getDouble("sAtk3") == 0.0) {
                     entity.getPersistentData().putDouble("sAtk3", 36.0);
                  } else {
                     entity.getPersistentData().putDouble("sAtk3", entity.getPersistentData().getDouble("sAtk3") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("sAtk3") == 36.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.BROWN_WOOL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.BROWN_WOOL));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 36, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("sAtk3") == 28.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("block.anvil.break"),
                              SoundSource.NEUTRAL,
                              3.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x, y, z, dev.deepnight.SafeSounds.resolve("block.anvil.break"), SoundSource.NEUTRAL, 3.0F, 1.0F, false
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("sAtk3") == 25.0) {
                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new GroundSpikeEntity(SimpleMobsModEntities.GROUND_SPIKE.get(), _level);
                        entityToSpawn.moveTo(x + entity.getLookAngle().x * 1.2, y, z + entity.getLookAngle().z * 1.2, entity.getYRot(), entity.getXRot());
                        entityToSpawn.setYBodyRot(entity.getYRot());
                        entityToSpawn.setYHeadRot(entity.getYRot());
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  } else if (entity.getPersistentData().getDouble("sAtk3") == 17.0) {
                     _center = new Vec3(x + entity.getLookAngle().x * 3.0, y, z + entity.getLookAngle().z * 3.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof SentinelKnightEntity)) {
                           entity.getPersistentData().putBoolean("s_attack3b", true);
                           entity.getPersistentData().putBoolean("s_attack3", false);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("sAtk3") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("sAtk3", 0.0);
                     entity.getPersistentData().putBoolean("s_attack3", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("s_attack3b")) {
                  if (entity.getPersistentData().getDouble("sAtk3b") == 0.0) {
                     entity.getPersistentData().putDouble("sAtk3b", 41.0);
                  } else {
                     entity.getPersistentData().putDouble("sAtk3b", entity.getPersistentData().getDouble("sAtk3b") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("sAtk3b") == 41.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.LIME_WOOL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.LIME_WOOL));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 7, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("sAtk3b") == 32.0) {
                     if (entity instanceof LivingEntity _entity) {
                        _entity.removeAllEffects();
                     }

                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 11, 12, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("sAtk3b") == 21.0) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_sentinel_knight,limit=1,sort=nearest] {NoAI:1b}"
                           );
                     }

                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 6, false, false));
                     }

                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              -4.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              -4.0F,
                              false
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
                              -4.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("entity.generic.explode"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              -4.0F,
                              false
                           );
                        }
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(
                           ParticleTypes.SMOKE, x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0, 5, 0.0, 1.0, 0.0, 0.1
                        );
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(
                           ParticleTypes.SWEEP_ATTACK, x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0, 10, 0.0, 1.0, 0.0, 0.1
                        );
                     }

                     _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof SentinelKnightEntity)) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.KNOCKDOWN)
                              .setImpact(13.0F)
                              .setArmorNegation(8.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 30.0F);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("sAtk3b") == 1.0) {
                     if (world instanceof ServerLevel _level) {
                        _level.getServer()
                           .getCommands()
                           .performPrefixedCommand(
                              new CommandSourceStack(
                                    CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                                 )
                                 .withSuppressedOutput(),
                              "/data merge entity @e[type=deepnight:dawn_sentinel_knight,limit=1,sort=nearest] {NoAI:0}"
                           );
                     }

                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("sAtk3b", 0.0);
                     entity.getPersistentData().putBoolean("s_attack3b", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("s_attack4")) {
                  if (entity.getPersistentData().getDouble("sAtk4") == 0.0) {
                     entity.getPersistentData().putDouble("sAtk4", 51.0);
                  } else {
                     entity.getPersistentData().putDouble("sAtk4", entity.getPersistentData().getDouble("sAtk4") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("sAtk4") == 51.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.GRAY_WOOL));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.GRAY_WOOL));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 51, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("sAtk4") != 22.0) {
                     if (entity.getPersistentData().getDouble("sAtk4") == 1.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                        }

                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putDouble("sAtk4", 0.0);
                        entity.getPersistentData().putBoolean("s_attack4", false);
                     }
                  } else {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(20.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                           entityiterator.getPersistentData().putBoolean("marked", true);
                        }
                     }

                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new SwordProjectileEntity(SimpleMobsModEntities.SWORD_PROJECTILE.get(), _level);
                        entityToSpawn.moveTo(x + entity.getLookAngle().x * 1.2, y, z + entity.getLookAngle().z * 1.2, entity.getYRot(), entity.getXRot());
                        entityToSpawn.setYBodyRot(entity.getYRot());
                        entityToSpawn.setYHeadRot(entity.getYRot());
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  }
               }

               if (entity.getPersistentData().getBoolean("s_attack5")) {
                  if (entity.getPersistentData().getDouble("sAtk5") == 0.0) {
                     entity.getPersistentData().putDouble("sAtk5", 51.0);
                  } else {
                     entity.getPersistentData().putDouble("sAtk5", entity.getPersistentData().getDouble("sAtk5") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("sAtk5") == 51.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.BLACK_CARPET));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.BLACK_CARPET));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("sAtk5") == 41.0) {
                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 16, 16, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("sAtk5") != 22.0) {
                     if (entity.getPersistentData().getDouble("sAtk5") == 1.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                        }

                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putDouble("sAtk5", 0.0);
                        entity.getPersistentData().putBoolean("s_attack5", false);
                     }
                  } else {
                     _center = new Vec3(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (!(entityiterator instanceof SentinelKnightEntity)) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.LONG)
                              .setImpact(13.0F)
                              .setArmorNegation(2.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 24.0F);
                        }
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(
                           ParticleTypes.SWEEP_ATTACK, x + entity.getLookAngle().x * 1.5, y + 1.0, z + entity.getLookAngle().z * 1.5, 20, 2.0, 2.0, 2.0, 0.2
                        );
                     }

                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 22, 8, false, false));
                     }
                  }
               }

               if (entity.getPersistentData().getBoolean("s_attack6")) {
                  if (entity.getPersistentData().getDouble("sAtk6") == 0.0) {
                     entity.getPersistentData().putDouble("sAtk6", 41.0);
                  } else {
                     entity.getPersistentData().putDouble("sAtk6", entity.getPersistentData().getDouble("sAtk6") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("sAtk6") == 41.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.WHITE_CARPET));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.WHITE_CARPET));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 41, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("sAtk6") == 27.0) {
                     entity.getPersistentData().putBoolean("Parry", true);
                  } else if (entity.getPersistentData().getDouble("sAtk6") == 12.0) {
                     entity.getPersistentData().putBoolean("Parry", false);
                  } else if (entity.getPersistentData().getDouble("sAtk6") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     Entity _entityx = entity;
                     if (_entityx instanceof Player _player) {
                        _player.getInventory().armor.set(2, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entityx instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Shieldon", true);
                     entity.getPersistentData().putBoolean("Shieldoff", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("sAtk6", 0.0);
                     entity.getPersistentData().putBoolean("s_attack6", false);
                  }

                  if (entity.getPersistentData().getBoolean("Parried")) {
                     entity.getPersistentData().putDouble("sAtk6", 0.0);
                     entity.getPersistentData().putBoolean("Parry", false);
                     entity.getPersistentData().putBoolean("s_attack6", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("s_attack7")) {
                  if (entity.getPersistentData().getDouble("sAtk7") == 0.0) {
                     entity.getPersistentData().putDouble("sAtk7", 82.0);
                     entity.getPersistentData().putDouble("i", 0.0);
                  } else {
                     entity.getPersistentData().putDouble("sAtk7", entity.getPersistentData().getDouble("sAtk7") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("sAtk7") <= 64.0 && entity.getPersistentData().getDouble("sAtk7") >= 54.0) {
                     entity.getPersistentData().putDouble("i", entity.getPersistentData().getDouble("i") + 1.0);
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(80.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                           if (world instanceof ServerLevel _level) {
                              _level.sendParticles(
                                 ParticleTypes.EXPLOSION_EMITTER,
                                 x + (entityiterator.getX() - entity.getX()) * (entity.getPersistentData().getDouble("i") / 11.0),
                                 y + 1.5,
                                 z + (entityiterator.getZ() - entity.getZ()) * (entity.getPersistentData().getDouble("i") / 11.0),
                                 1,
                                 1.5,
                                 1.5,
                                 1.5,
                                 1.0
                              );
                           }

                           if (world instanceof ServerLevel _level) {
                              _level.sendParticles(
                                 (SimpleParticleType)SimpleMobsModParticleTypes.SPARKLE.get(),
                                 x + (entityiterator.getX() - entity.getX()) * (entity.getPersistentData().getDouble("i") / 11.0),
                                 y + 1.5,
                                 z + (entityiterator.getZ() - entity.getZ()) * (entity.getPersistentData().getDouble("i") / 11.0),
                                 10,
                                 1.5,
                                 1.5,
                                 1.5,
                                 1.0
                              );
                           }
                        }
                     }
                  }

                  if (entity.getPersistentData().getDouble("sAtk7") == 82.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.TORCH));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.TORCH));
                     }

                     entity.getPersistentData().putBoolean("focus", true);
                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 81, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("sAtk7") != 52.0) {
                     if (entity.getPersistentData().getDouble("sAtk7") == 51.0) {
                        if (world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new SentinelFlashEntity(SimpleMobsModEntities.SENTINEL_FLASH.get(), _level);
                           entityToSpawn.moveTo(x, y, z, entity.getYRot(), 0.0F);
                           entityToSpawn.setYBodyRot(entity.getYRot());
                           entityToSpawn.setYHeadRot(entity.getYRot());
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }

                        _center = new Vec3(x, y, z);

                        for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.5), e -> true)
                           .stream()
                           .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                           .collect(Collectors.toList())) {
                           if (!(entityiterator instanceof SentinelKnightEntity) && !(entityiterator instanceof SentinelFlashEntity)) {
                              SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                                 .setInitialPosition(entity.position())
                                 .setStunType(StunType.LONG)
                                 .setImpact(15.0F)
                                 .setArmorNegation(4.0F);
                              entityiterator.hurt((DamageSource)dmgSrc, 14.0F);
                              entityiterator.setDeltaMovement(new Vec3(entityiterator.getX() - entity.getX(), 0.2, entityiterator.getZ() - entity.getZ()));
                           }
                        }
                     } else if (entity.getPersistentData().getDouble("sAtk7") == 2.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.WHITE_CONCRETE));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.WHITE_CONCRETE));
                        }
                     } else if (entity.getPersistentData().getDouble("sAtk7") == 1.0) {
                        Entity _entity = entity;
                        if (_entity instanceof Player _player) {
                           _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                           _player.getInventory().setChanged();
                        } else if (_entity instanceof LivingEntity _living) {
                           _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                        }

                        entity.getPersistentData().putBoolean("Attacking", false);
                        entity.getPersistentData().putBoolean("noai", false);
                        entity.getPersistentData().putBoolean("lock", false);
                        entity.getPersistentData().putDouble("sAtk7", 0.0);
                        entity.getPersistentData().putBoolean("s_attack7", false);
                     }
                  } else {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(80.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                           Entity _ent = entity;
                           _ent.teleportTo(
                              entityiterator.getX() + entityiterator.getLookAngle().x * 1.6,
                              entityiterator.getY() + 1.0,
                              entityiterator.getZ() + entityiterator.getLookAngle().z * 1.6
                           );
                           if (_ent instanceof ServerPlayer _serverPlayer) {
                              _serverPlayer.connection
                                 .teleport(
                                    entityiterator.getX() + entityiterator.getLookAngle().x * 1.6,
                                    entityiterator.getY() + 1.0,
                                    entityiterator.getZ() + entityiterator.getLookAngle().z * 1.6,
                                    _ent.getYRot(),
                                    _ent.getXRot()
                                 );
                           }
                        }
                     }

                     entity.getPersistentData().putDouble("yaw", entity.getYRot());
                     entity.getPersistentData().putDouble("pitch", 0.0);
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putBoolean("lock", true);
                     entity.getPersistentData().putBoolean("noai", true);
                  }
               }

               if (entity.getPersistentData().getBoolean("s_attack8")) {
                  if (entity.getPersistentData().getDouble("sAtk8") == 0.0) {
                     entity.getPersistentData().putDouble("sAtk8", 71.0);
                  } else {
                     entity.getPersistentData().putDouble("sAtk8", entity.getPersistentData().getDouble("sAtk8") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("sAtk8") == 71.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.YELLOW_CONCRETE));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.YELLOW_CONCRETE));
                     }

                     entity.getPersistentData().putBoolean("focus", true);
                  } else if (entity.getPersistentData().getDouble("sAtk8") == 44.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("entity.blaze.shoot"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              2.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("entity.blaze.shoot"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              2.0F,
                              false
                           );
                        }
                     }

                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("entity.player.attack.crit"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("entity.player.attack.crit"),
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
                              dev.deepnight.SafeSounds.resolve("block.amethyst_cluster.step"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("block.amethyst_cluster.step"),
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
                              dev.deepnight.SafeSounds.resolve("block.amethyst_block.break"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              0.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("block.amethyst_block.break"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              0.0F,
                              false
                           );
                        }
                     }

                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new SentinelSlashEntity(SimpleMobsModEntities.SENTINEL_SLASH.get(), _level);
                        entityToSpawn.moveTo(x + entity.getLookAngle().x * 1.0, y + 1.5, z + entity.getLookAngle().z * 1.0, entity.getYRot(), entity.getXRot());
                        entityToSpawn.setYBodyRot(entity.getYRot());
                        entityToSpawn.setYHeadRot(entity.getYRot());
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(
                           (SimpleParticleType)SimpleMobsModParticleTypes.SPARKLE.get(),
                           x + entity.getLookAngle().x * 1.0,
                           y + 1.5,
                           z + entity.getLookAngle().z * 1.0,
                           30,
                           2.0,
                           2.0,
                           2.0,
                           0.1
                        );
                     }

                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 44, 6, false, false));
                     }

                     entity.getPersistentData().putDouble("yaw", entity.getYRot());
                     entity.getPersistentData().putDouble("pitch", entity.getXRot());
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putBoolean("lock", true);
                  } else if (entity.getPersistentData().getDouble("sAtk8") == 31.0) {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                           entity.getPersistentData().putBoolean("s_attack8a", true);
                           entity.getPersistentData().putDouble("sAtk8", 0.0);
                           entity.getPersistentData().putBoolean("s_attack8", false);
                           entity.getPersistentData().putBoolean("lock", false);
                           entity.getPersistentData().putBoolean("focus", true);
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("sAtk8") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("noai", false);
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putDouble("sAtk8", 0.0);
                     entity.getPersistentData().putBoolean("s_attack8", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("s_attack8a")) {
                  if (entity.getPersistentData().getDouble("sAtk8a") == 0.0) {
                     entity.getPersistentData().putDouble("sAtk8a", 47.0);
                  } else {
                     entity.getPersistentData().putDouble("sAtk8a", entity.getPersistentData().getDouble("sAtk8a") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("sAtk8a") == 47.0) {
                     if (entity instanceof LivingEntity _entity) {
                        _entity.removeAllEffects();
                     }

                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.BLACK_CONCRETE));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.BLACK_CONCRETE));
                     }

                     entity.getPersistentData().putBoolean("focus", true);
                  } else if (entity.getPersistentData().getDouble("sAtk8a") == 31.0) {
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("entity.blaze.shoot"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              2.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("entity.blaze.shoot"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              2.0F,
                              false
                           );
                        }
                     }

                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("entity.player.attack.crit"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("entity.player.attack.crit"),
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
                              dev.deepnight.SafeSounds.resolve("block.amethyst_cluster.step"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("block.amethyst_cluster.step"),
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
                              dev.deepnight.SafeSounds.resolve("block.amethyst_block.break"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              0.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x,
                              y,
                              z,
                              dev.deepnight.SafeSounds.resolve("block.amethyst_block.break"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              0.0F,
                              false
                           );
                        }
                     }

                     if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new SentinelSlashEntity(SimpleMobsModEntities.SENTINEL_SLASH.get(), _level);
                        entityToSpawn.moveTo(x + entity.getLookAngle().x * 1.0, y + 1.5, z + entity.getLookAngle().z * 1.0, entity.getYRot(), entity.getXRot());
                        entityToSpawn.setYBodyRot(entity.getYRot());
                        entityToSpawn.setYHeadRot(entity.getYRot());
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }

                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(
                           (SimpleParticleType)SimpleMobsModParticleTypes.SPARKLE.get(),
                           x + entity.getLookAngle().x * 1.0,
                           y + 1.5,
                           z + entity.getLookAngle().z * 1.0,
                           30,
                           2.0,
                           2.0,
                           2.0,
                           0.1
                        );
                     }

                     if (entity instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 44, 6, false, false));
                     }

                     entity.getPersistentData().putDouble("yaw", entity.getYRot());
                     entity.getPersistentData().putDouble("pitch", 0.0);
                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putBoolean("lock", true);
                  } else if (entity.getPersistentData().getDouble("sAtk8a") == 2.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.WHITE_CONCRETE));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.WHITE_CONCRETE));
                     }
                  } else if (entity.getPersistentData().getDouble("sAtk8a") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putBoolean("noai", false);
                     entity.getPersistentData().putBoolean("lock", false);
                     entity.getPersistentData().putDouble("sAtk8a", 0.0);
                     entity.getPersistentData().putBoolean("s_attack8a", false);
                  }
               }

               if (entity.getPersistentData().getBoolean("Parried")) {
                  if (entity.getPersistentData().getDouble("parriedtimer") == 0.0) {
                     entity.getPersistentData().putDouble("parriedtimer", 41.0);
                  } else {
                     entity.getPersistentData().putDouble("parriedtimer", entity.getPersistentData().getDouble("parriedtimer") - 1.0);
                  }

                  if (entity.getPersistentData().getDouble("parriedtimer") == 41.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.YELLOW_CARPET));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.YELLOW_CARPET));
                     }

                     entity.getPersistentData().putBoolean("Attacking", true);
                     if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                           _level.playSound(
                              null,
                              BlockPos.containing(x, y, z),
                              dev.deepnight.SafeSounds.resolve("simple_mobs:poise"),
                              SoundSource.NEUTRAL,
                              2.0F,
                              1.0F
                           );
                        } else {
                           _level.playLocalSound(
                              x, y, z, dev.deepnight.SafeSounds.resolve("simple_mobs:poise"), SoundSource.NEUTRAL, 2.0F, 1.0F, false
                           );
                        }
                     }

                     if (entity instanceof LivingEntity _entityx) {
                        _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 41, 6, false, false));
                     }
                  } else if (entity.getPersistentData().getDouble("parriedtimer") == 16.0) {
                     _center = new Vec3(x, y, z);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.5), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.SHORT)
                              .setImpact(13.0F)
                              .setArmorNegation(14.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 16.0F);
                           entityiterator.setDeltaMovement(
                              new Vec3((entityiterator.getX() - entity.getX()) * 0.7, 0.7, (entityiterator.getZ() - entity.getZ()) * 0.7)
                           );
                        }
                     }
                  } else if (entity.getPersistentData().getDouble("parriedtimer") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     Entity _entityx = entity;
                     if (_entityx instanceof Player _player) {
                        _player.getInventory().armor.set(2, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entityx instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Shieldon", true);
                     entity.getPersistentData().putBoolean("Shieldoff", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("parriedtimer", 0.0);
                     entity.getPersistentData().putBoolean("Parried", false);
                  }
               }
            }
         }
      }
   }
}
