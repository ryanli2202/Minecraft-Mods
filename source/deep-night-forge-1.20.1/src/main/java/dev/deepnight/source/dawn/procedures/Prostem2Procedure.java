package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.SeedGunEntity;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class Prostem2Procedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
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
            entity.getPersistentData().putBoolean("Aggro", false);
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(32.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                  entity.getPersistentData().putBoolean("Aggro", true);
                  entity.getPersistentData()
                     .putDouble("atanyaw", Math.toDegrees(Math.atan2(entityiterator.getX() - entity.getX(), entityiterator.getZ() - entity.getZ())));
                  entity.getPersistentData()
                     .putDouble("distance", Math.hypot(Math.abs(entity.getX() - entityiterator.getX()), Math.abs(entity.getZ() - entityiterator.getZ())));
                  if (entity.getPersistentData().getDouble("atanyaw") < 0.0) {
                     entity.getPersistentData().putDouble("yaw2", 0.0 - entity.getPersistentData().getDouble("atanyaw"));
                  }

                  if (entity.getPersistentData().getDouble("atanyaw") >= 0.0) {
                     entity.getPersistentData().putDouble("yaw2", 360.0 - entity.getPersistentData().getDouble("atanyaw"));
                  }

                  entity.getPersistentData()
                     .putDouble(
                        "pitch2",
                        Math.toDegrees(Math.atan(entity.getPersistentData().getDouble("distance") / (entityiterator.getY() + 1.5 - (entity.getY() + 3.0))))
                     );
                  if (entity.getPersistentData().getDouble("pitch2") > 0.0) {
                     entity.getPersistentData().putDouble("pitch2", entity.getPersistentData().getDouble("pitch2") - 90.0);
                  } else if (entity.getPersistentData().getDouble("pitch2") < 0.0) {
                     entity.getPersistentData().putDouble("pitch2", entity.getPersistentData().getDouble("pitch2") - -90.0);
                  }
               }
            }

            if (!entity.getPersistentData().getBoolean("Spawned")
               && !entity.getPersistentData().getBoolean("Spawn")
               && !entity.getPersistentData().getBoolean("Attacking")) {
               entity.getPersistentData().putBoolean("Spawned", true);
               entity.getPersistentData().putBoolean("Spawn", true);
               entity.getPersistentData().putBoolean("Attacking", true);
            }

            if (entity.getPersistentData().getBoolean("Aggro") && !entity.getPersistentData().getBoolean("Attacking") && Math.random() >= 0.75) {
               entity.getPersistentData().putBoolean("Attack", true);
               entity.getPersistentData().putBoolean("Attacking", true);
            }

            if (entity.getPersistentData().getDouble("despawncd") == 0.0) {
               entity.getPersistentData().putDouble("despawncd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 180, 260));
            } else if (entity.getPersistentData().getDouble("despawncd") > 1.0) {
               entity.getPersistentData().putDouble("despawncd", entity.getPersistentData().getDouble("despawncd") - 1.0);
            }

            if (!entity.getPersistentData().getBoolean("Attacking")
               && (
                  (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0F)
                        < (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1.0F) * 0.75
                     || entity.getPersistentData().getBoolean("Despawn_Time")
                     || entity.getPersistentData().getDouble("despawncd") == 1.0
               )) {
               entity.getPersistentData().putBoolean("Despawn", true);
               entity.getPersistentData().putBoolean("Attacking", true);
            }

            if (entity.getPersistentData().getBoolean("Spawn")) {
               if (entity.getPersistentData().getDouble("spawn") == 0.0) {
                  entity.getPersistentData().putDouble("spawn", 31.0);
               } else {
                  entity.getPersistentData().putDouble("spawn", entity.getPersistentData().getDouble("spawn") - 1.0);
               }

               if (entity.getPersistentData().getDouble("spawn") == 31.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.COAL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.COAL));
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:stem_grow"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:stem_grow"),
                           SoundSource.NEUTRAL,
                           1.0F,
                           1.0F,
                           false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("spawn") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putDouble("spawn", 0.0);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("Spawn", false);
               }

               if (world instanceof ServerLevel _level) {
                  _level.getServer()
                     .getCommands()
                     .performPrefixedCommand(
                        new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null)
                           .withSuppressedOutput(),
                        "/particle minecraft:block minecraft:cactus ~ ~0.3 ~ 0 0 0 0.2 3 force"
                     );
               }

               _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (!entityiterator.getPersistentData().getBoolean("whisperer")) {
                     entityiterator.hurt(new DamageSource(entityiterator.level().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(net.minecraft.world.damagesource.DamageTypes.GENERIC), entity), 1.0F);
                     entityiterator.setDeltaMovement(
                        new Vec3((entityiterator.getX() - entity.getX()) * 2.0, 0.4, (entityiterator.getZ() - entity.getZ()) * 2.0)
                     );
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack")) {
               if (entity.getPersistentData().getDouble("Atk") == 0.0) {
                  entity.getPersistentData().putDouble("Atk", 25.0);
               } else {
                  entity.getPersistentData().putDouble("Atk", entity.getPersistentData().getDouble("Atk") - 1.0);
               }

               if (entity.getPersistentData().getDouble("Atk") == 25.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.LEATHER_HELMET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
               } else if (entity.getPersistentData().getDouble("Atk") == 11.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.shulker.ambient"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           2.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.shulker.ambient"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           2.0F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel projectileLevel) {
                     Projectile _entityToSpawn = (new Object() {
                        public Projectile getArrow(Level level, Entity shooter, float damage, int knockback) {
                           AbstractArrow entityToSpawn = new SeedGunEntity(SimpleMobsModEntities.SEED_GUN.get(), level);
                           entityToSpawn.setOwner(shooter);
                           entityToSpawn.setBaseDamage(damage);
                           entityToSpawn.setKnockback(knockback);
                           entityToSpawn.setSilent(true);
                           return entityToSpawn;
                        }
                     }).getArrow(projectileLevel, entity, 3.0F, 1);
                     _entityToSpawn.setPos(x + entity.getLookAngle().x * 3.0, y + 3.2, z + entity.getLookAngle().z * 3.0);
                     _entityToSpawn.shoot(
                        Math.sin(Math.toRadians(entity.getYRot() + 180.0F)),
                        Math.sin(Math.toRadians(0.0F - entity.getXRot())) * 1.5,
                        Math.cos(Math.toRadians(entity.getYRot())),
                        2.0F,
                        0.0F
                     );
                     projectileLevel.addFreshEntity(_entityToSpawn);
                  }
               } else if (entity.getPersistentData().getDouble("Atk") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putDouble("Atk", 0.0);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("Attack", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Despawn")) {
               if (entity.getPersistentData().getDouble("despawn") == 0.0) {
                  entity.getPersistentData().putDouble("despawn", 31.0);
               } else {
                  entity.getPersistentData().putDouble("despawn", entity.getPersistentData().getDouble("despawn") - 1.0);
               }

               if (entity.getPersistentData().getDouble("despawn") == 31.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.CHARCOAL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHARCOAL));
                  }
               } else if (entity.getPersistentData().getDouble("despawn") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  if (!entity.level().isClientSide()) {
                     entity.discard();
                  }
               }

               if (world instanceof ServerLevel _level) {
                  _level.getServer()
                     .getCommands()
                     .performPrefixedCommand(
                        new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null)
                           .withSuppressedOutput(),
                        "particle minecraft:block minecraft:cactus ~ ~0.3 ~ 0.6 0.6 0.6 0.2 3 force"
                     );
               }

               _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (!entityiterator.getPersistentData().getBoolean("whisperer")) {
                     entityiterator.hurt(new DamageSource(entityiterator.level().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(net.minecraft.world.damagesource.DamageTypes.GENERIC), entity), 1.0F);
                     entityiterator.setDeltaMovement(
                        new Vec3((entityiterator.getX() - entity.getX()) * 2.0, 0.4, (entityiterator.getZ() - entity.getZ()) * 2.0)
                     );
                  }
               }
            }

            if (!entity.getPersistentData().getBoolean("Aggro")) {
               if (entity.getPersistentData().getDouble("aggrotimer") == 0.0) {
                  entity.getPersistentData().putDouble("aggrotimer", 60.0);
               } else {
                  entity.getPersistentData().putDouble("aggrotimer", entity.getPersistentData().getDouble("aggrotimer") - 1.0);
               }

               if (entity.getPersistentData().getDouble("aggrotimer") == 1.0) {
                  entity.getPersistentData().putBoolean("Despawn_Time", true);
               }
            }
         }
      }
   }
}
