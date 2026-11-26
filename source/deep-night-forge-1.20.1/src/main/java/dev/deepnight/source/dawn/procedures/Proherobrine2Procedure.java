package dev.deepnight.source.dawn.procedures;


import com.google.common.collect.UnmodifiableIterator;
import java.util.Comparator;
import java.util.Random;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.AirEntity;
import dev.deepnight.source.dawn.entity.HerobrineEntity;
import dev.deepnight.source.dawn.entity.HerobrineFlashEntity;
import dev.deepnight.source.dawn.entity.HerobrineSpike1Entity;
import dev.deepnight.source.dawn.entity.HerobrineSpike2Entity;
import dev.deepnight.source.dawn.entity.Herobrinese2Entity;
import dev.deepnight.source.dawn.entity.Herobrinese3Entity;
import dev.deepnight.source.dawn.entity.SwordAutoEntity;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.init.SimpleMobsModMobEffects;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import dev.deepnight.source.SourceDamage;
import dev.deepnight.source.SourceDamage.StunType;

public class Proherobrine2Procedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         double i = 0.0;
         double j = 0.0;
         double k = 0.0;
         double xRadius = 0.0;
         double loop = 0.0;
         double zRadius = 0.0;
         double particleAmount = 0.0;
         if (!world.isClientSide()) {
            if (entity.getPersistentData().getBoolean("lock")) {
               Entity _ent = entity;
               _ent.setYRot((float)entity.getPersistentData().getDouble("yaw"));
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

            entity.getPersistentData().putBoolean("Aggro", false);
            if (entity instanceof LivingEntity _entity) {
               _entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20, 0, false, false));
            }

            entity.getPersistentData().putBoolean("Far", false);
            entity.getPersistentData().putBoolean("Far2", false);
            entity.getPersistentData().putBoolean("Mid", false);
            entity.getPersistentData().putBoolean("Close", false);
            entity.getPersistentData().putDouble("yaw2", 0.0);
            entity.getPersistentData().putDouble("pitch2", 0.0);
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(64.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                  entity.getPersistentData().putBoolean("Aggro", true);
                  entity.getPersistentData().putBoolean("Far2", true);
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

            _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(32.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                  entity.getPersistentData().putBoolean("Far", true);
                  entity.getPersistentData().putBoolean("Mid", false);
                  entity.getPersistentData().putBoolean("Close", false);
               }
            }

            _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                  entity.getPersistentData().putBoolean("Far", false);
                  entity.getPersistentData().putBoolean("Mid", true);
                  entity.getPersistentData().putBoolean("Close", false);
               }
            }

            _center = new Vec3(x + entity.getLookAngle().x * 1.0, y, z + entity.getLookAngle().z * 1.0);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                  entity.getPersistentData().putBoolean("Far", false);
                  entity.getPersistentData().putBoolean("Mid", false);
                  entity.getPersistentData().putBoolean("Close", true);
               }
            }

            if (entity.getPersistentData().getDouble("lavacheck") == 0.0) {
               entity.getPersistentData().putDouble("lavacheck", 27.0);
               entity.getPersistentData().putDouble("i", -3.0);
               entity.getPersistentData().putDouble("j", -3.0);
               entity.getPersistentData().putDouble("k", -3.0);
            } else {
               if (entity.getPersistentData().getDouble("i") <= 3.0) {
                  if (entity.getPersistentData().getDouble("j") <= 3.0) {
                     if (entity.getPersistentData().getDouble("k") <= 3.0) {
                        if (world.getBlockState(
                                 BlockPos.containing(
                                    x + entity.getPersistentData().getDouble("i"),
                                    y + entity.getPersistentData().getDouble("j"),
                                    z + entity.getPersistentData().getDouble("k")
                                 )
                              )
                              .getFluidState().is(net.minecraft.tags.FluidTags.LAVA)) {
                           entity.getPersistentData().putBoolean("lavaaround", true);
                        }

                        entity.getPersistentData().putDouble("k", entity.getPersistentData().getDouble("k") + 1.0);
                     }

                     entity.getPersistentData().putDouble("j", entity.getPersistentData().getDouble("j") + 1.0);
                  }

                  entity.getPersistentData().putDouble("i", entity.getPersistentData().getDouble("i") + 1.0);
               }

               entity.getPersistentData().putDouble("lavacheck", entity.getPersistentData().getDouble("lavacheck") - 1.0);
            }

            if (entity.getPersistentData().getBoolean("lavaaround") && !entity.getPersistentData().getBoolean("Attacking")) {
               entity.getPersistentData().putBoolean("lavapick", true);
               entity.getPersistentData().putBoolean("Attacking", true);
            }

            if (entity.getPersistentData().getBoolean("Close")) {
               if (!entity.getPersistentData().getBoolean("Attacking") && entity.getPersistentData().getDouble("atk2cd") == 0.0 && Math.random() >= 0.9) {
                  entity.getPersistentData().putBoolean("Attack2", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (!entity.getPersistentData().getBoolean("Attacking") && entity.getPersistentData().getDouble("atk4cd") == 0.0 && Math.random() <= 0.1) {
                  if (Math.random() <= 0.5) {
                     entity.getPersistentData().putBoolean("Attack4", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else {
                     entity.getPersistentData().putBoolean("Attack4a", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  }
               } else if (!entity.getPersistentData().getBoolean("Attacking")
                  && entity.getPersistentData().getDouble("tntcd") == 0.0
                  && Math.random() <= 0.2
                  && Math.random() >= 0.1) {
                  entity.getPersistentData().putBoolean("TNT", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (!entity.getPersistentData().getBoolean("Attacking")
                  && entity.getPersistentData().getDouble("atk7cd") == 0.0
                  && Math.random() <= 0.3
                  && Math.random() >= 0.2) {
                  entity.getPersistentData().putBoolean("Attack7", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (!entity.getPersistentData().getBoolean("Attacking")
                  && entity.getPersistentData().getDouble("atk8cd") == 0.0
                  && Math.random() <= 0.4
                  && Math.random() >= 0.3) {
                  entity.getPersistentData().putBoolean("Attack8", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (!entity.getPersistentData().getBoolean("Attacking")
                  && entity.getPersistentData().getDouble("atk10cd") == 0.0
                  && Math.random() <= 0.5
                  && Math.random() >= 0.4) {
                  entity.getPersistentData().putBoolean("Attack10", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (!entity.getPersistentData().getBoolean("Attacking")
                  && (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0F)
                     <= (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1.0F) * 0.4
                  && entity.getPersistentData().getDouble("eatcd") == 0.0
                  && Math.random() <= 0.6
                  && Math.random() >= 0.5) {
                  entity.getPersistentData().putBoolean("eat", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (entity.getPersistentData().getBoolean("haslava")
                  && !entity.getPersistentData().getBoolean("Attacking")
                  && Math.random() <= 0.7
                  && Math.random() >= 0.6) {
                  entity.getPersistentData().putBoolean("Spreadlava", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (!entity.getPersistentData().getBoolean("Attacking") && Math.random() <= 0.8 && Math.random() >= 0.7) {
                  entity.getPersistentData().putBoolean("Attack6", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (!entity.getPersistentData().getBoolean("Attacking")) {
                  entity.getPersistentData().putBoolean("Attack1", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               }
            } else if (entity.getPersistentData().getBoolean("Mid")) {
               if (!entity.getPersistentData().getBoolean("Attacking") && entity.getPersistentData().getDouble("atk2cd") == 0.0 && Math.random() >= 0.9) {
                  entity.getPersistentData().putBoolean("Attack2", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (!entity.getPersistentData().getBoolean("Attacking") && entity.getPersistentData().getDouble("atk4cd") == 0.0 && Math.random() <= 0.1) {
                  if (Math.random() <= 0.5) {
                     entity.getPersistentData().putBoolean("Attack4", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else {
                     entity.getPersistentData().putBoolean("Attack4a", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  }
               } else if (!entity.getPersistentData().getBoolean("Attacking") && entity.getPersistentData().getDouble("atk6acd") == 0.0 && Math.random() <= 0.1
                  )
                {
                  entity.getPersistentData().putBoolean("Attack6a", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (!entity.getPersistentData().getBoolean("Attacking")
                  && (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0F)
                     <= (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1.0F) * 0.4
                  && entity.getPersistentData().getDouble("eatcd") == 0.0
                  && Math.random() <= 0.6
                  && Math.random() >= 0.5) {
                  entity.getPersistentData().putBoolean("eat", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               }
            } else if (entity.getPersistentData().getBoolean("Far")) {
               if (!entity.getPersistentData().getBoolean("Attacking") && entity.getPersistentData().getDouble("atk4cd") == 0.0 && Math.random() >= 0.7) {
                  if (Math.random() <= 0.5) {
                     entity.getPersistentData().putBoolean("Attack4", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else {
                     entity.getPersistentData().putBoolean("Attack4a", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  }
               } else if (!entity.getPersistentData().getBoolean("Attacking") && entity.getPersistentData().getDouble("atk11cd") == 0.0 && Math.random() <= 0.3
                  )
                {
                  entity.getPersistentData().putBoolean("Attack11", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (!entity.getPersistentData().getBoolean("Attacking") && entity.getPersistentData().getDouble("atk9cd") == 0.0 && Math.random() <= 0.3) {
                  entity.getPersistentData().putBoolean("Attack9", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               } else if (!entity.getPersistentData().getBoolean("Attacking")
                  && entity.getPersistentData().getDouble("atk5cd") == 0.0
                  && Math.random() <= 0.6
                  && Math.random() >= 0.3) {
                  entity.getPersistentData().putBoolean("Attack5", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               }
            } else if (entity.getPersistentData().getBoolean("Far2")) {
               if (!entity.getPersistentData().getBoolean("Attacking")
                  && entity.getPersistentData().getDouble("atk4cd") == 0.0
                  && Math.random() <= 0.5
                  && Math.random() >= 0.3) {
                  if (Math.random() <= 0.5) {
                     entity.getPersistentData().putBoolean("Attack4", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  } else {
                     entity.getPersistentData().putBoolean("Attack4a", true);
                     entity.getPersistentData().putBoolean("Attacking", true);
                  }
               } else if (!entity.getPersistentData().getBoolean("Attacking")
                  && entity.getPersistentData().getDouble("atk11cd") == 0.0
                  && Math.random() <= 0.7
                  && Math.random() >= 0.5) {
                  entity.getPersistentData().putBoolean("Attack11", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
               }
            }

            if (entity.getPersistentData().getDouble("tntcd") == 0.0 && entity.getPersistentData().getBoolean("TNT")) {
               entity.getPersistentData().putDouble("tntcd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 180, 260));
            } else if (entity.getPersistentData().getDouble("tntcd") > 0.0) {
               entity.getPersistentData().putDouble("tntcd", entity.getPersistentData().getDouble("tntcd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("atk2cd") == 0.0 && entity.getPersistentData().getBoolean("Attack2")) {
               entity.getPersistentData().putDouble("atk2cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 180, 260));
            } else if (entity.getPersistentData().getDouble("atk2cd") > 0.0) {
               entity.getPersistentData().putDouble("atk2cd", entity.getPersistentData().getDouble("atk2cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("atk3cd") == 0.0 && entity.getPersistentData().getBoolean("Attack3")) {
               entity.getPersistentData().putDouble("atk3cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 180, 260));
            } else if (entity.getPersistentData().getDouble("atk3cd") > 0.0) {
               entity.getPersistentData().putDouble("atk3cd", entity.getPersistentData().getDouble("atk3cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("atk4cd") != 0.0
               || !entity.getPersistentData().getBoolean("Attack4") && !entity.getPersistentData().getBoolean("Attack4a")) {
               if (entity.getPersistentData().getDouble("atk4cd") > 0.0) {
                  entity.getPersistentData().putDouble("atk4cd", entity.getPersistentData().getDouble("atk4cd") - 1.0);
               }
            } else {
               entity.getPersistentData().putDouble("atk4cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 180, 260));
            }

            if (entity.getPersistentData().getDouble("atk5cd") == 0.0 && entity.getPersistentData().getBoolean("Attack5")) {
               entity.getPersistentData().putDouble("atk5cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 180, 260));
            } else if (entity.getPersistentData().getDouble("atk5cd") > 0.0) {
               entity.getPersistentData().putDouble("atk5cd", entity.getPersistentData().getDouble("atk5cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("atk6cd") == 0.0 && entity.getPersistentData().getBoolean("Attack6")) {
               entity.getPersistentData().putDouble("atk6cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 180, 260));
            } else if (entity.getPersistentData().getDouble("atk6cd") > 0.0) {
               entity.getPersistentData().putDouble("atk6cd", entity.getPersistentData().getDouble("atk6cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("atk6acd") == 0.0 && entity.getPersistentData().getBoolean("Attack6a")) {
               entity.getPersistentData().putDouble("atk6acd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 180, 260));
            } else if (entity.getPersistentData().getDouble("atk6acd") > 0.0) {
               entity.getPersistentData().putDouble("atk6acd", entity.getPersistentData().getDouble("atk6acd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("atk7cd") == 0.0 && entity.getPersistentData().getBoolean("Attack7")) {
               entity.getPersistentData().putDouble("atk7cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 180, 260));
            } else if (entity.getPersistentData().getDouble("atk7cd") > 0.0) {
               entity.getPersistentData().putDouble("atk7cd", entity.getPersistentData().getDouble("atk7cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("atk8cd") == 0.0 && entity.getPersistentData().getBoolean("Attack8")) {
               entity.getPersistentData().putDouble("atk8cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 200, 400));
            } else if (entity.getPersistentData().getDouble("atk8cd") > 0.0) {
               entity.getPersistentData().putDouble("atk8cd", entity.getPersistentData().getDouble("atk8cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("atk9cd") == 0.0 && entity.getPersistentData().getBoolean("Attack9")) {
               entity.getPersistentData().putDouble("atk9cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 200, 400));
            } else if (entity.getPersistentData().getDouble("atk9cd") > 0.0) {
               entity.getPersistentData().putDouble("atk9cd", entity.getPersistentData().getDouble("atk9cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("atk10cd") == 0.0 && entity.getPersistentData().getBoolean("Attack10")) {
               entity.getPersistentData().putDouble("atk10cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 180, 280));
            } else if (entity.getPersistentData().getDouble("atk10cd") > 0.0) {
               entity.getPersistentData().putDouble("atk10cd", entity.getPersistentData().getDouble("atk10cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("atk11cd") == 0.0 && entity.getPersistentData().getBoolean("Attack11")) {
               entity.getPersistentData().putDouble("atk11cd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 180, 280));
            } else if (entity.getPersistentData().getDouble("atk11cd") > 0.0) {
               entity.getPersistentData().putDouble("atk11cd", entity.getPersistentData().getDouble("atk11cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("eatcd") == 0.0 && entity.getPersistentData().getBoolean("eat")) {
               entity.getPersistentData().putDouble("eatcd", Mth.nextInt(net.minecraft.util.RandomSource.create(), 200, 440));
            } else if (entity.getPersistentData().getDouble("eatcd") > 0.0) {
               entity.getPersistentData().putDouble("eatcd", entity.getPersistentData().getDouble("eatcd") - 1.0);
            }

            if (entity.getPersistentData().getBoolean("Attack1")) {
               if (entity.getPersistentData().getDouble("atk1") == 0.0) {
                  entity.getPersistentData().putDouble("atk1", 36.0);
               } else {
                  entity.getPersistentData().putDouble("atk1", entity.getPersistentData().getDouble("atk1") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk1") == 36.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.GREEN_WOOL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.GREEN_WOOL));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 36, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("atk1") == 26.0) {
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putBoolean("lock", true);
               } else if (entity.getPersistentData().getDouble("atk1") == 23.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.0F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.SWEEP_ATTACK, x + entity.getLookAngle().x * 1.5, y + 1.0, z + entity.getLookAngle().z * 1.5, 2, 1.0, 0.5, 1.0, 1.0
                     );
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.CRIT, x + entity.getLookAngle().x * 1.5, y + 1.0, z + entity.getLookAngle().z * 1.5, 3, 1.0, 0.5, 1.0, 1.0
                     );
                  }
               } else if (entity.getPersistentData().getDouble("atk1") == 10.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        entity.getPersistentData().putBoolean("Attack1a", true);
                        entity.getPersistentData().putDouble("atk1", 0.0);
                        entity.getPersistentData().putBoolean("lock", false);
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

                  entity.getPersistentData().putDouble("atk1", 0.0);
                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("Attack1", false);
               }

               if (entity.getPersistentData().getDouble("atk1") <= 24.0 && entity.getPersistentData().getDouble("atk1") >= 22.0) {
                  entity.setDeltaMovement(
                     new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 0.2, 0.0, Math.cos(Math.toRadians(entity.getYRot())) * 0.2)
                  );
                  _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.SHORT)
                           .setImpact(3.0F)
                           .setArmorNegation(20.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 30.0F);
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack1a")) {
               if (entity.getPersistentData().getDouble("atk1a") == 0.0) {
                  entity.getPersistentData().putDouble("atk1a", 36.0);
               } else {
                  entity.getPersistentData().putDouble("atk1a", entity.getPersistentData().getDouble("atk1a") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk1a") == 36.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.ACACIA_LOG));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.ACACIA_LOG));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 36, 1, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("atk1a") == 34.0) {
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putBoolean("lock", true);
               } else if (entity.getPersistentData().getDouble("atk1a") == 30.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.SWEEP_ATTACK, x + entity.getLookAngle().x * 1.5, y + 1.0, z + entity.getLookAngle().z * 1.5, 2, 1.0, 0.5, 1.0, 1.0
                     );
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.CRIT, x + entity.getLookAngle().x * 1.5, y + 1.0, z + entity.getLookAngle().z * 1.5, 3, 1.0, 0.5, 1.0, 1.0
                     );
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           2.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           2.0F,
                           false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("atk1a") == 27.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           2.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           2.0F,
                           false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("atk1a") == 17.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        entity.getPersistentData().putBoolean("Attack1b", true);
                        entity.getPersistentData().putDouble("atk1a", 0.0);
                        entity.getPersistentData().putBoolean("Attack1a", false);
                        entity.getPersistentData().putBoolean("lock", false);
                     }
                  }
               } else if (entity.getPersistentData().getDouble("atk1a") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putDouble("atk1a", 0.0);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("Attack1a", false);
               }

               if (entity.getPersistentData().getDouble("atk1a") <= 30.0 && entity.getPersistentData().getDouble("atk1a") >= 29.0
                  || entity.getPersistentData().getDouble("atk1a") <= 26.0 && entity.getPersistentData().getDouble("atk1a") >= 25.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        DamageSource dmgSrc = entity instanceof LivingEntity ? entity.damageSources().mobAttack((LivingEntity)entity) : entity.damageSources().generic();
                        entityiterator.hurt(dmgSrc, 20.0F);
                        entityiterator.invulnerableTime = 0;
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack1b")) {
               if (entity.getPersistentData().getDouble("atk1b") == 0.0) {
                  entity.getPersistentData().putDouble("atk1b", 36.0);
               } else {
                  entity.getPersistentData().putDouble("atk1b", entity.getPersistentData().getDouble("atk1b") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk1b") == 36.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.BLACK_WOOL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.BLACK_WOOL));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 36, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("atk1b") == 31.0) {
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putBoolean("lock", true);
               } else if (entity.getPersistentData().getDouble("atk1b") == 29.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.0F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.SWEEP_ATTACK, x + entity.getLookAngle().x * 1.5, y + 1.0, z + entity.getLookAngle().z * 1.5, 2, 1.0, 0.5, 1.0, 1.0
                     );
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.CRIT, x + entity.getLookAngle().x * 1.5, y + 1.0, z + entity.getLookAngle().z * 1.5, 3, 1.0, 0.5, 1.0, 1.0
                     );
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

               if (entity.getPersistentData().getDouble("atk1b") <= 30.0 && entity.getPersistentData().getDouble("atk1b") >= 28.0) {
                  entity.setDeltaMovement(
                     new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 0.2, 0.0, Math.cos(Math.toRadians(entity.getYRot())) * 0.2)
                  );
                  _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(3.0F)
                           .setArmorNegation(25.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 35.0F);
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack2")) {
               if (entity.getPersistentData().getDouble("atk2") == 0.0) {
                  entity.getPersistentData().putDouble("atk2", 41.0);
               } else {
                  entity.getPersistentData().putDouble("atk2", entity.getPersistentData().getDouble("atk2") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk2") == 41.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.BROWN_WOOL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.BROWN_WOOL));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 41, 4, false, false));
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new HerobrineFlashEntity(SimpleMobsModEntities.HEROBRINE_FLASH.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               } else if (entity.getPersistentData().getDouble("atk2") == 40.0) {
                  Entity _ent = entity;
                  _ent.teleportTo(
                     x + entity.getLookAngle().x * -7.0 + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3),
                     y,
                     z + entity.getLookAngle().z * -7.0 + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3)
                  );
                  if (_ent instanceof ServerPlayer _serverPlayer) {
                     _serverPlayer.connection
                        .teleport(
                           x + entity.getLookAngle().x * -7.0 + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3),
                           y,
                           z + entity.getLookAngle().z * -7.0 + Mth.nextInt(net.minecraft.util.RandomSource.create(), -3, 3),
                           _ent.getYRot(),
                           _ent.getXRot()
                        );
                  }
               } else if (entity.getPersistentData().getDouble("atk2") == 39.0) {
                  i = -1.0;
                  j = 1.0;
                  k = -1.0;

                  while (i <= 1.0) {
                     while (j <= 3.0) {
                        for (; k <= 1.0; k++) {
                           if (world.getBlockState(BlockPos.containing(x + i, y + j, z + k)).canOcclude()) {
                              world.setBlock(BlockPos.containing(x + i, y + j, z + k), Blocks.AIR.defaultBlockState(), 3);
                           }
                        }

                        k = -1.0;
                        j++;
                     }

                     j = 1.0;
                     i++;
                  }
               } else if (entity.getPersistentData().getDouble("atk2") == 27.0) {
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putBoolean("lock", true);
                  entity.getPersistentData().putBoolean("noai", true);
               } else if (entity.getPersistentData().getDouble("atk2") == 25.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.5F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.5F,
                           false
                        );
                     }
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.polar_bear.warning"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.polar_bear.warning"),
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
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_punch"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_punch"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.0F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new Herobrinese2Entity(SimpleMobsModEntities.HEROBRINESE_2.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  entity.getPersistentData().putBoolean("noai", false);
               } else if (entity.getPersistentData().getDouble("atk2") == 19.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        entity.getPersistentData().putBoolean("Attack3", true);
                        entity.getPersistentData().putDouble("atk2", 0.0);
                        entity.getPersistentData().putBoolean("Attack2", false);
                     }
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
                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("Attack2", false);
               }

               if (entity.getPersistentData().getDouble("atk2") <= 27.0 && entity.getPersistentData().getDouble("atk2") >= 23.0) {
                  entity.setDeltaMovement(
                     new Vec3(Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 1.2, 0.0, Math.cos(Math.toRadians(entity.getYRot())) * 1.2)
                  );
                  _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof HerobrineEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(3.0F)
                           .setArmorNegation(25.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 30.0F);
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack3")) {
               if (entity.getPersistentData().getDouble("atk3") == 0.0) {
                  entity.getPersistentData().putDouble("atk3", 47.0);
               } else {
                  entity.getPersistentData().putDouble("atk3", entity.getPersistentData().getDouble("atk3") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk3") == 47.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.LIME_WOOL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.LIME_WOOL));
                  }

                  entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putBoolean("lock", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 46, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("atk3") == 38.0) {
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new Herobrinese3Entity(SimpleMobsModEntities.HEROBRINESE_3.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), 0.0F);
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
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
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
                           dev.deepnight.SafeSounds.resolve("simple_mobs:notch_punch"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:notch_punch"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F,
                           false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("atk3") == 36.0) {
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new Herobrinese3Entity(SimpleMobsModEntities.HEROBRINESE_3.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot() + 40.0F, 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot() + 40.0F);
                     entityToSpawn.setYHeadRot(entity.getYRot() + 40.0F);
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               } else if (entity.getPersistentData().getDouble("atk3") == 2.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.INK_SAC));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.INK_SAC));
                  }
               } else if (entity.getPersistentData().getDouble("atk3") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putDouble("atk3", 0.0);
                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("Attack3", false);
               }

               if (entity.getPersistentData().getDouble("atk3") <= 35.0 && entity.getPersistentData().getDouble("atk3") >= 29.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof HerobrineEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(3.0F)
                           .setArmorNegation(25.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 24.0F);
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack4")) {
               if (entity.getPersistentData().getDouble("atk4") == 0.0) {
                  entity.getPersistentData().putDouble("atk4", 61.0);
               } else {
                  entity.getPersistentData().putDouble("atk4", entity.getPersistentData().getDouble("atk4") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk4") == 61.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.GRAY_WOOL));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.GRAY_WOOL));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 61, 4, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("atk4") == 47.0) {
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new HerobrineFlashEntity(SimpleMobsModEntities.HEROBRINE_FLASH.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               } else if (entity.getPersistentData().getDouble("atk4") == 46.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(64.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        Entity _ent = entity;
                        _ent.teleportTo(
                           entityiterator.getX() + entityiterator.getLookAngle().x * 1.5,
                           entityiterator.getY(),
                           entityiterator.getZ() + entityiterator.getLookAngle().z * 1.5
                        );
                        if (_ent instanceof ServerPlayer _serverPlayer) {
                           _serverPlayer.connection
                              .teleport(
                                 entityiterator.getX() + entityiterator.getLookAngle().x * 1.5,
                                 entityiterator.getY(),
                                 entityiterator.getZ() + entityiterator.getLookAngle().z * 1.5,
                                 _ent.getYRot(),
                                 _ent.getXRot()
                              );
                        }
                     }
                  }

                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putBoolean("lock", true);
                  entity.getPersistentData().putBoolean("noai", true);
               } else if (entity.getPersistentData().getDouble("atk4") == 45.0) {
                  i = -1.0;
                  j = 1.0;
                  k = -1.0;

                  while (i <= 1.0) {
                     while (j <= 3.0) {
                        for (; k <= 1.0; k++) {
                           if (world.getBlockState(BlockPos.containing(x + i, y + j, z + k)).canOcclude()) {
                              world.setBlock(BlockPos.containing(x + i, y + j, z + k), Blocks.AIR.defaultBlockState(), 3);
                           }
                        }

                        k = -1.0;
                        j++;
                     }

                     j = 1.0;
                     i++;
                  }
               } else if (entity.getPersistentData().getDouble("atk4") == 29.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.SWEEP_ATTACK, x + entity.getLookAngle().x * 1.5, y + 1.0, z + entity.getLookAngle().z * 1.5, 2, 1.0, 0.5, 1.0, 1.0
                     );
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.CRIT, x + entity.getLookAngle().x * 1.5, y + 1.0, z + entity.getLookAngle().z * 1.5, 3, 1.0, 0.5, 1.0, 1.0
                     );
                  }
               } else if (entity.getPersistentData().getDouble("atk4") == 1.0) {
                  entity.getPersistentData().putBoolean("noai", false);
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putDouble("atk4", 0.0);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("Attack4", false);
               }

               if (entity.getPersistentData().getDouble("atk4") <= 60.0 && entity.getPersistentData().getDouble("atk4") >= 47.0) {
                  entity.setDeltaMovement(
                     new Vec3(Math.sin(Math.toRadians(entity.getYRot())) * 0.25, 0.0, Math.cos(Math.toRadians(entity.getYRot() + 180.0F)) * 0.25)
                  );
               }

               if (entity.getPersistentData().getDouble("atk4") <= 30.0 && entity.getPersistentData().getDouble("atk4") >= 29.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof HerobrineEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(3.0F)
                           .setArmorNegation(25.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 40.0F);
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack4a")) {
               if (entity.getPersistentData().getDouble("atk4a") == 0.0) {
                  entity.getPersistentData().putDouble("atk4a", 81.0);
               } else {
                  entity.getPersistentData().putDouble("atk4a", entity.getPersistentData().getDouble("atk4a") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk4a") == 81.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.BLACK_CARPET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.BLACK_CARPET));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 81, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("atk4a") == 67.0) {
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new HerobrineFlashEntity(SimpleMobsModEntities.HEROBRINE_FLASH.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               } else if (entity.getPersistentData().getDouble("atk4a") == 66.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(64.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        Entity _ent = entity;
                        _ent.teleportTo(
                           entityiterator.getX() + entityiterator.getLookAngle().x * 1.5,
                           entityiterator.getY(),
                           entityiterator.getZ() + entityiterator.getLookAngle().z * 1.5
                        );
                        if (_ent instanceof ServerPlayer _serverPlayer) {
                           _serverPlayer.connection
                              .teleport(
                                 entityiterator.getX() + entityiterator.getLookAngle().x * 1.5,
                                 entityiterator.getY(),
                                 entityiterator.getZ() + entityiterator.getLookAngle().z * 1.5,
                                 _ent.getYRot(),
                                 _ent.getXRot()
                              );
                        }
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new HerobrineFlashEntity(SimpleMobsModEntities.HEROBRINE_FLASH.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putBoolean("lock", true);
               } else if (entity.getPersistentData().getDouble("atk4a") == 65.0) {
                  i = -1.0;
                  j = 1.0;
                  k = -1.0;

                  while (i <= 1.0) {
                     while (j <= 3.0) {
                        for (; k <= 1.0; k++) {
                           if (world.getBlockState(BlockPos.containing(x + i, y + j, z + k)).canOcclude()) {
                              world.setBlock(BlockPos.containing(x + i, y + j, z + k), Blocks.AIR.defaultBlockState(), 3);
                           }
                        }

                        k = -1.0;
                        j++;
                     }

                     j = 1.0;
                     i++;
                  }
               } else if (entity.getPersistentData().getDouble("atk4a") == 51.0) {
                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("focus", true);
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new HerobrineFlashEntity(SimpleMobsModEntities.HEROBRINE_FLASH.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               } else if (entity.getPersistentData().getDouble("atk4a") == 46.0) {
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new HerobrineFlashEntity(SimpleMobsModEntities.HEROBRINE_FLASH.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(64.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        Entity _ent = entity;
                        _ent.teleportTo(
                           entityiterator.getX() + entityiterator.getLookAngle().x * -1.5,
                           entityiterator.getY(),
                           entityiterator.getZ() + entityiterator.getLookAngle().z * -1.5
                        );
                        if (_ent instanceof ServerPlayer _serverPlayer) {
                           _serverPlayer.connection
                              .teleport(
                                 entityiterator.getX() + entityiterator.getLookAngle().x * -1.5,
                                 entityiterator.getY(),
                                 entityiterator.getZ() + entityiterator.getLookAngle().z * -1.5,
                                 _ent.getYRot(),
                                 _ent.getXRot()
                              );
                        }
                     }
                  }
               } else if (entity.getPersistentData().getDouble("atk4a") == 45.0) {
                  i = -1.0;
                  j = 1.0;
                  k = -1.0;

                  while (i <= 1.0) {
                     while (j <= 3.0) {
                        for (; k <= 1.0; k++) {
                           if (world.getBlockState(BlockPos.containing(x + i, y + j, z + k)).canOcclude()) {
                              world.setBlock(BlockPos.containing(x + i, y + j, z + k), Blocks.AIR.defaultBlockState(), 3);
                           }
                        }

                        k = -1.0;
                        j++;
                     }

                     j = 1.0;
                     i++;
                  }
               } else if (entity.getPersistentData().getDouble("atk4a") == 36.0) {
                  entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putBoolean("lock", true);
                  entity.getPersistentData().putBoolean("noai", true);
               } else if (entity.getPersistentData().getDouble("atk4a") == 30.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.SWEEP_ATTACK, x + entity.getLookAngle().x * 1.5, y + 1.0, z + entity.getLookAngle().z * 1.5, 2, 1.0, 0.5, 1.0, 1.0
                     );
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.CRIT, x + entity.getLookAngle().x * -1.5, y + 1.0, z + entity.getLookAngle().z * -1.5, 3, 1.0, 0.5, 1.0, 1.0
                     );
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F,
                           false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("atk4a") == 1.0) {
                  entity.getPersistentData().putBoolean("noai", false);
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putDouble("atk4a", 0.0);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("Attack4a", false);
               }

               if (entity.getPersistentData().getDouble("atk4a") <= 80.0 && entity.getPersistentData().getDouble("atk4a") >= 67.0) {
                  entity.setDeltaMovement(
                     new Vec3(Math.sin(Math.toRadians(entity.getYRot())) * 0.25, 0.0, Math.cos(Math.toRadians(entity.getYRot() + 180.0F)) * 0.25)
                  );
               }

               if (entity.getPersistentData().getDouble("atk4a") <= 30.0 && entity.getPersistentData().getDouble("atk4a") >= 29.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof HerobrineEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(3.0F)
                           .setArmorNegation(25.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 40.0F);
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack5")) {
               if (entity.getPersistentData().getDouble("atk5") == 0.0) {
                  entity.getPersistentData().putDouble("atk5", 81.0);
               } else {
                  entity.getPersistentData().putDouble("atk5", entity.getPersistentData().getDouble("atk5") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk5") == 81.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.WHITE_CARPET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.WHITE_CARPET));
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 81, 6, false, false));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
               } else if (entity.getPersistentData().getDouble("atk5") == 60.0) {
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putBoolean("lock", true);
               } else if (entity.getPersistentData().getDouble("atk5") == 45.0) {
                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("focus", true);
               } else if (entity.getPersistentData().getDouble("atk5") == 29.0) {
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putBoolean("lock", true);
               } else if (entity.getPersistentData().getDouble("atk5") == 1.0 && Math.random() >= 0.1) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("atk5", 0.0);
                  entity.getPersistentData().putBoolean("Attack5", false);
               }

               if (entity.getPersistentData().getDouble("atk5") != 59.0 && entity.getPersistentData().getDouble("atk5") != 27.0) {
                  if (entity.getPersistentData().getDouble("atk5") != 57.0 && entity.getPersistentData().getDouble("atk5") != 25.0) {
                     if (entity.getPersistentData().getDouble("atk5") != 55.0 && entity.getPersistentData().getDouble("atk5") != 23.0) {
                        if (entity.getPersistentData().getDouble("atk5") != 53.0 && entity.getPersistentData().getDouble("atk5") != 21.0) {
                           if ((entity.getPersistentData().getDouble("atk5") == 51.0 || entity.getPersistentData().getDouble("atk5") == 19.0)
                              && world instanceof ServerLevel _level) {
                              Entity entityToSpawn = new HerobrineSpike1Entity(SimpleMobsModEntities.HEROBRINE_SPIKE_1.get(), _level);
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
                        } else if (world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new HerobrineSpike1Entity(SimpleMobsModEntities.HEROBRINE_SPIKE_1.get(), _level);
                           entityToSpawn.moveTo(x + entity.getLookAngle().x * 11.0, y, z + entity.getLookAngle().z * 11.0, entity.getYRot(), 0.0F);
                           entityToSpawn.setYBodyRot(entity.getYRot());
                           entityToSpawn.setYHeadRot(entity.getYRot());
                           if (entityToSpawn instanceof Mob _mobToSpawn) {
                              _mobToSpawn.finalizeSpawn(
                                 _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null
                              );
                           }

                           world.addFreshEntity(entityToSpawn);
                        }
                     } else if (world instanceof ServerLevel _level) {
                        Entity entityToSpawn = new HerobrineSpike1Entity(SimpleMobsModEntities.HEROBRINE_SPIKE_1.get(), _level);
                        entityToSpawn.moveTo(x + entity.getLookAngle().x * 8.0, y, z + entity.getLookAngle().z * 8.0, entity.getYRot(), 0.0F);
                        entityToSpawn.setYBodyRot(entity.getYRot());
                        entityToSpawn.setYHeadRot(entity.getYRot());
                        if (entityToSpawn instanceof Mob _mobToSpawn) {
                           _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        world.addFreshEntity(entityToSpawn);
                     }
                  } else if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new HerobrineSpike1Entity(SimpleMobsModEntities.HEROBRINE_SPIKE_1.get(), _level);
                     entityToSpawn.moveTo(x + entity.getLookAngle().x * 5.0, y, z + entity.getLookAngle().z * 5.0, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               } else if (world instanceof ServerLevel _level) {
                  Entity entityToSpawn = new HerobrineSpike1Entity(SimpleMobsModEntities.HEROBRINE_SPIKE_1.get(), _level);
                  entityToSpawn.moveTo(x + entity.getLookAngle().x * 2.0, y, z + entity.getLookAngle().z * 2.0, entity.getYRot(), 0.0F);
                  entityToSpawn.setYBodyRot(entity.getYRot());
                  entityToSpawn.setYHeadRot(entity.getYRot());
                  if (entityToSpawn instanceof Mob _mobToSpawn) {
                     _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                  }

                  world.addFreshEntity(entityToSpawn);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack6")) {
               if (entity.getPersistentData().getDouble("atk6") == 0.0) {
                  entity.getPersistentData().putDouble("atk6", 56.0);
               } else {
                  entity.getPersistentData().putDouble("atk6", entity.getPersistentData().getDouble("atk6") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk6") == 56.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.YELLOW_CARPET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.YELLOW_CARPET));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 56, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("atk6") == 51.0) {
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putBoolean("lock", true);
               } else if (entity.getPersistentData().getDouble("atk6") == 48.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.SWEEP_ATTACK, x + entity.getLookAngle().x * 1.5, y + 1.0, z + entity.getLookAngle().z * 1.5, 2, 1.0, 0.5, 1.0, 1.0
                     );
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.CRIT, x + entity.getLookAngle().x * 1.5, y + 1.0, z + entity.getLookAngle().z * 1.5, 3, 1.0, 0.5, 1.0, 1.0
                     );
                  }
               } else if (entity.getPersistentData().getDouble("atk6") == 44.0) {
                  entity.getPersistentData().putBoolean("focus", true);
                  entity.getPersistentData().putBoolean("lock", false);
               } else if (entity.getPersistentData().getDouble("atk6") == 31.0) {
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putBoolean("lock", true);
               } else if (entity.getPersistentData().getDouble("atk6") == 30.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.SWEEP_ATTACK, x + entity.getLookAngle().x * 1.5, y + 1.0, z + entity.getLookAngle().z * 1.5, 2, 1.0, 0.5, 1.0, 1.0
                     );
                  }

                  if (world instanceof ServerLevel _level) {
                     _level.sendParticles(
                        ParticleTypes.CRIT, x + entity.getLookAngle().x * 1.5, y + 1.0, z + entity.getLookAngle().z * 1.5, 3, 1.0, 0.5, 1.0, 1.0
                     );
                  }
               } else if (entity.getPersistentData().getDouble("atk6") == 21.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        entity.getPersistentData().putDouble("atk6", 0.0);
                        entity.getPersistentData().putBoolean("lock", false);
                        entity.getPersistentData().putBoolean("Attack6a", true);
                        entity.getPersistentData().putBoolean("Attack6", false);
                     }
                  }
               } else if (entity.getPersistentData().getDouble("atk6") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putDouble("atk6", 0.0);
                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("Attack6", false);
               }

               if (entity.getPersistentData().getDouble("atk6") <= 49.0 && entity.getPersistentData().getDouble("atk6") >= 48.0
                  || entity.getPersistentData().getDouble("atk6") <= 29.0 && entity.getPersistentData().getDouble("atk6") >= 28.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 1.5, y, z + entity.getLookAngle().z * 1.5);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(3.0F)
                           .setArmorNegation(25.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 30.0F);
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack6a")) {
               if (entity.getPersistentData().getDouble("atk6a") == 0.0) {
                  entity.getPersistentData().putDouble("atk6a", 91.0);
               } else {
                  entity.getPersistentData().putDouble("atk6a", entity.getPersistentData().getDouble("atk6a") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk6a") == 91.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.CYAN_CONCRETE));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.CYAN_CONCRETE));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 91, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("atk6a") == 66.0) {
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putBoolean("lock", true);
               } else if (entity.getPersistentData().getDouble("atk6a") == 60.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:notch_punch"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:notch_punch"),
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
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ogre_attack3"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ogre_attack3"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new Herobrinese2Entity(SimpleMobsModEntities.HEROBRINESE_2.get(), _level);
                     entityToSpawn.moveTo(x + entity.getLookAngle().x * 4.0, y, z + entity.getLookAngle().z * 4.0, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               } else if (entity.getPersistentData().getDouble("atk6a") == 41.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        entity.getPersistentData().putDouble("atk6a", 0.0);
                        entity.getPersistentData().putBoolean("lock", false);
                        entity.getPersistentData().putBoolean("Attack3", true);
                        entity.getPersistentData().putBoolean("Attack6a", false);
                     }
                  }
               } else if (entity.getPersistentData().getDouble("atk6a") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putDouble("atk6a", 0.0);
                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("Attack6a", false);
               }

               if (entity.getPersistentData().getDouble("atk6a") <= 60.0 && entity.getPersistentData().getDouble("atk6a") >= 45.0) {
                  for (double var35 = 1.0; var35 <= 3.0; var35++) {
                     _center = new Vec3(x + entity.getLookAngle().x * var35, y, z + entity.getLookAngle().z * var35);

                     for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
                        .stream()
                        .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                        .collect(Collectors.toList())) {
                        if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                           SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                              .setInitialPosition(entity.position())
                              .setStunType(StunType.LONG)
                              .setImpact(3.0F)
                              .setArmorNegation(25.0F);
                           entityiterator.hurt((DamageSource)dmgSrc, 30.0F);
                        }
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("lavapick")) {
               if (entity.getPersistentData().getDouble("lp") == 0.0) {
                  entity.getPersistentData().putDouble("lp", 26.0);
               } else {
                  entity.getPersistentData().putDouble("lp", entity.getPersistentData().getDouble("lp") - 1.0);
               }

               if (entity.getPersistentData().getDouble("lp") == 26.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.SEA_PICKLE));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.SEA_PICKLE));
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 26, 5, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("lp") != 10.0) {
                  if (entity.getPersistentData().getDouble("lp") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("lavaaround", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("lp", 0.0);
                     entity.getPersistentData().putBoolean("lavapick", false);
                  }
               } else {
                  i = -3.0;
                  j = -3.0;
                  k = -3.0;

                  while (i <= 3.0) {
                     while (j <= 3.0) {
                        for (; k <= 3.0; k++) {
                           if (world.getBlockState(BlockPos.containing(x + i, y + j, z + k)).getFluidState().is(net.minecraft.tags.FluidTags.LAVA)) {
                              if (world instanceof Level _level) {
                                 if (!_level.isClientSide()) {
                                    _level.playSound(
                                       null,
                                       BlockPos.containing(x, y, z),
                                       dev.deepnight.SafeSounds.resolve("item.bucket.fill_lava"),
                                       SoundSource.NEUTRAL,
                                       1.0F,
                                       1.0F
                                    );
                                 } else {
                                    _level.playLocalSound(
                                       x,
                                       y,
                                       z,
                                       dev.deepnight.SafeSounds.resolve("item.bucket.fill_lava"),
                                       SoundSource.NEUTRAL,
                                       1.0F,
                                       1.0F,
                                       false
                                    );
                                 }
                              }

                              world.setBlock(BlockPos.containing(x + i, y + j, z + k), Blocks.AIR.defaultBlockState(), 3);
                              entity.getPersistentData().putBoolean("haslava", true);
                           }
                        }

                        k = -3.0;
                        j++;
                     }

                     j = -3.0;
                     i++;
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("TNT")) {
               if (entity.getPersistentData().getDouble("tnt") == 0.0) {
                  entity.getPersistentData().putDouble("tnt", 26.0);
               } else {
                  entity.getPersistentData().putDouble("tnt", entity.getPersistentData().getDouble("tnt") - 1.0);
               }

               if (entity.getPersistentData().getDouble("tnt") == 26.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.BLACK_CONCRETE));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.BLACK_CONCRETE));
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 26, 5, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("tnt") == 9.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL,
                                 new Vec3(x + entity.getLookAngle().x, y + 1.0, z + entity.getLookAngle().z),
                                 Vec2.ZERO,
                                 _level,
                                 4,
                                 "",
                                 Component.literal(""),
                                 _level.getServer(),
                                 null
                              )
                              .withSuppressedOutput(),
                           "/summon minecraft:tnt ~ ~ ~ {Fuse:20}"
                        );
                  }
               } else if (entity.getPersistentData().getDouble("tnt") == 8.0) {
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new HerobrineFlashEntity(SimpleMobsModEntities.HEROBRINE_FLASH.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  Entity _ent = entity;
                  _ent.teleportTo(x + Mth.nextInt(net.minecraft.util.RandomSource.create(), -10, 10), y, z + Mth.nextInt(net.minecraft.util.RandomSource.create(), -10, 10));
                  if (_ent instanceof ServerPlayer _serverPlayer) {
                     _serverPlayer.connection
                        .teleport(x + Mth.nextInt(net.minecraft.util.RandomSource.create(), -10, 10), y, z + Mth.nextInt(net.minecraft.util.RandomSource.create(), -10, 10), _ent.getYRot(), _ent.getXRot());
                  }
               } else if (entity.getPersistentData().getDouble("tnt") != 6.0) {
                  if (entity.getPersistentData().getDouble("tnt") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("tnt", 0.0);
                     entity.getPersistentData().putBoolean("TNT", false);
                  }
               } else {
                  i = -1.0;
                  j = 1.0;
                  k = -1.0;

                  while (i <= 1.0) {
                     while (j <= 3.0) {
                        for (; k <= 1.0; k++) {
                           if (world.getBlockState(BlockPos.containing(x + i, y + j, z + k)).canOcclude()) {
                              world.setBlock(BlockPos.containing(x + i, y + j, z + k), Blocks.AIR.defaultBlockState(), 3);
                           }
                        }

                        k = -1.0;
                        j++;
                     }

                     j = 1.0;
                     i++;
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Attack7")) {
               if (entity.getPersistentData().getDouble("atk7") == 0.0) {
                  entity.getPersistentData().putDouble("atk7", 41.0);
               } else {
                  entity.getPersistentData().putDouble("atk7", entity.getPersistentData().getDouble("atk7") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk7") == 41.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.POPPY));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.POPPY));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 41, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("atk7") == 31.0) {
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putBoolean("lock", true);
               } else if (entity.getPersistentData().getDouble("atk7") == 28.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:notch_punch"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:notch_punch"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  _center = new Vec3(x + entity.getLookAngle().x * 1.0, y, z + entity.getLookAngle().z * 1.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        entity.getPersistentData().putBoolean("upper", true);
                        entityiterator.setDeltaMovement(new Vec3(0.0, 1.6, 0.0));
                        DamageSource dmgSrc = entity instanceof LivingEntity ? entity.damageSources().mobAttack((LivingEntity)entity) : entity.damageSources().generic();
                        entityiterator.hurt(dmgSrc, 15.0F);
                     }
                  }
               } else if (entity.getPersistentData().getDouble("atk7") == 18.0) {
                  if (entity.getPersistentData().getBoolean("upper")) {
                     entity.getPersistentData().putBoolean("upper", false);
                     entity.getPersistentData().putBoolean("Attack7a", true);
                     entity.getPersistentData().putDouble("atk7", 0.0);
                     entity.getPersistentData().putBoolean("Attack7", false);
                     entity.getPersistentData().putBoolean("lock", false);
                  }
               } else if (entity.getPersistentData().getDouble("atk7") == 6.0) {
                  entity.getPersistentData().putBoolean("lock", false);
               } else if (entity.getPersistentData().getDouble("atk7") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("atk7", 0.0);
                  entity.getPersistentData().putBoolean("Attack7", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack7a")) {
               if (entity.getPersistentData().getDouble("atk7a") == 0.0) {
                  entity.getPersistentData().putDouble("atk7a", 56.0);
               } else {
                  entity.getPersistentData().putDouble("atk7a", entity.getPersistentData().getDouble("atk7a") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk7a") == 56.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.LILAC));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.LILAC));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 56, 7, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("atk7a") == 31.0) {
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

                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new AirEntity(SimpleMobsModEntities.AIR.get(), _level);
                     entityToSpawn.moveTo(x + entity.getLookAngle().x * 1.0, y, z + entity.getLookAngle().z * 1.0, world.getRandom().nextFloat() * 360.0F, 0.0F);
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  entity.getPersistentData().putBoolean("lock", true);
                  entity.getPersistentData().putBoolean("noai", true);
                  _center = new Vec3(x + entity.getLookAngle().x * 1.0, y, z + entity.getLookAngle().z * 1.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof HerobrineEntity)) {
                        if (entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 20, 1, false, false));
                        }

                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.KNOCKDOWN)
                           .setImpact(16.0F)
                           .setArmorNegation(25.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 50.0F);
                     }
                  }
               } else if (entity.getPersistentData().getDouble("atk7a") == 6.0) {
                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("noai", false);
               } else if (entity.getPersistentData().getDouble("atk7a") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("atk7a", 0.0);
                  entity.getPersistentData().putBoolean("Attack7a", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack8")) {
               if (entity.getPersistentData().getDouble("atk8") == 0.0) {
                  entity.getPersistentData().putDouble("atk8", 51.0);
               } else {
                  entity.getPersistentData().putDouble("atk8", entity.getPersistentData().getDouble("atk8") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk8") == 51.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.LILY_OF_THE_VALLEY));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.LILY_OF_THE_VALLEY));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 51, 6, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("atk8") == 31.0) {
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putBoolean("lock", true);
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_punch"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ninetail_punch"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.0F,
                           false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("atk8") == 26.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 0.5, y, z + entity.getLookAngle().z * 0.5);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(0.5), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        entity.getPersistentData().putBoolean("Attack8a", true);
                        entity.getPersistentData().putDouble("atk8", 0.0);
                        if (entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance(SimpleMobsModMobEffects.GRAB.get(), 81, 1, false, false));
                        }

                        entity.getPersistentData().putBoolean("Attack8", false);
                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("entity.turtle.egg_crack"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("entity.turtle.egg_crack"),
                                 SoundSource.NEUTRAL,
                                 2.0F,
                                 1.0F,
                                 false
                              );
                           }
                        }
                     }
                  }
               } else if (entity.getPersistentData().getDouble("atk8") == 6.0) {
                  entity.getPersistentData().putBoolean("lock", false);
               } else if (entity.getPersistentData().getDouble("atk8") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("atk8", 0.0);
                  entity.getPersistentData().putBoolean("Attack8", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack8a")) {
               if (entity.getPersistentData().getDouble("atk8a") == 0.0) {
                  entity.getPersistentData().putDouble("atk8a", 71.0);
               } else {
                  entity.getPersistentData().putDouble("atk8a", entity.getPersistentData().getDouble("atk8a") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk8a") == 71.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.LILY_PAD));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.LILY_PAD));
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 71, 7, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("atk8a") == 51.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null, BlockPos.containing(x, y, z), dev.deepnight.SafeSounds.resolve(""), SoundSource.NEUTRAL, 2.0F, 0.0F
                        );
                     } else {
                        _level.playLocalSound(x, y, z, dev.deepnight.SafeSounds.resolve(""), SoundSource.NEUTRAL, 2.0F, 0.0F, false);
                     }
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.polar_bear.warning"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.polar_bear.warning"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           0.0F,
                           false
                        );
                     }
                  }

                  _center = new Vec3(x + entity.getLookAngle().x * 1.0, y, z + entity.getLookAngle().z * 1.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        entityiterator.hurt(new DamageSource(entityiterator.level().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(net.minecraft.world.damagesource.DamageTypes.GENERIC), entity), 1.0F);
                        if (entityiterator instanceof LivingEntity _entity) {
                           _entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 41, 0, false, false));
                        }
                     }
                  }
               } else if (entity.getPersistentData().getDouble("atk8a") == 31.0) {
                  _center = new Vec3(x + entity.getLookAngle().x * 1.0, y, z + entity.getLookAngle().z * 1.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null) && entityiterator instanceof LivingEntity _entity) {
                        _entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 400, 0, false, false));
                     }
                  }
               } else if (entity.getPersistentData().getDouble("atk8a") != 6.0) {
                  if (entity.getPersistentData().getDouble("atk8a") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("atk8a", 0.0);
                     entity.getPersistentData().putBoolean("Attack8a", false);
                  }
               } else {
                  _center = new Vec3(x + entity.getLookAngle().x * 1.0, y, z + entity.getLookAngle().z * 1.0);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        entityiterator.hurt(new DamageSource(entityiterator.level().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(net.minecraft.world.damagesource.DamageTypes.GENERIC), entity), 1.0F);
                        dev.deepnight.source.SourceStun.apply(entityiterator,40);
                     }
                  }

                  entity.getPersistentData().putBoolean("lock", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack9")) {
               if (entity.getPersistentData().getDouble("atk9") == 0.0) {
                  entity.getPersistentData().putDouble("atk9", 86.0);
               } else {
                  entity.getPersistentData().putDouble("atk9", entity.getPersistentData().getDouble("atk9") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk9") == 86.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.GLASS));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.GLASS));
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 86, 7, false, false));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
                  entity.getPersistentData().putDouble("block_x", entity.getX());
                  entity.getPersistentData().putDouble("block_y", entity.getY());
                  entity.getPersistentData().putDouble("block_z", entity.getZ());
               } else if (entity.getPersistentData().getDouble("atk9") == 80.0) {
                  entity.setDeltaMovement(new Vec3(0.0, 0.8, 0.0));
               } else if (entity.getPersistentData().getDouble("atk9") == 79.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("block.gravel.place"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("block.gravel.place"), SoundSource.NEUTRAL, 2.0F, 1.0F, false
                        );
                     }
                  }

                  world.setBlock(
                     BlockPos.containing(
                        entity.getPersistentData().getDouble("block_x"),
                        entity.getPersistentData().getDouble("block_y") + 0.0,
                        entity.getPersistentData().getDouble("block_z")
                     ),
                     Blocks.DIRT.defaultBlockState(),
                     3
                  );
               } else if (entity.getPersistentData().getDouble("atk9") == 77.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("block.gravel.place"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("block.gravel.place"), SoundSource.NEUTRAL, 2.0F, 1.0F, false
                        );
                     }
                  }

                  world.setBlock(
                     BlockPos.containing(
                        entity.getPersistentData().getDouble("block_x"),
                        entity.getPersistentData().getDouble("block_y") + 1.0,
                        entity.getPersistentData().getDouble("block_z")
                     ),
                     Blocks.DIRT.defaultBlockState(),
                     3
                  );
               } else if (entity.getPersistentData().getDouble("atk9") == 75.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("block.gravel.place"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("block.gravel.place"), SoundSource.NEUTRAL, 2.0F, 1.0F, false
                        );
                     }
                  }

                  world.setBlock(
                     BlockPos.containing(
                        entity.getPersistentData().getDouble("block_x"),
                        entity.getPersistentData().getDouble("block_y") + 2.0,
                        entity.getPersistentData().getDouble("block_z")
                     ),
                     Blocks.DIRT.defaultBlockState(),
                     3
                  );
               } else if (entity.getPersistentData().getDouble("atk9") == 58.0) {
                  entity.getPersistentData().putBoolean("focus", false);
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(64.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        entityiterator.getPersistentData().putBoolean("herobrine_mark", true);
                        if (world instanceof ServerLevel _level) {
                           Entity entityToSpawn = new SwordAutoEntity(SimpleMobsModEntities.SWORD_AUTO.get(), _level);
                           entityToSpawn.moveTo(
                              entityiterator.getX() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -2, 2),
                              entityiterator.getY() + 1.5,
                              entityiterator.getZ() + Mth.nextInt(net.minecraft.util.RandomSource.create(), -2, 2),
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
                     }
                  }
               } else if (entity.getPersistentData().getDouble("atk9") == 1.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(64.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator.getPersistentData().getBoolean("herobrine_mark")) {
                        entityiterator.getPersistentData().putBoolean("herobrine_mark", false);
                     }
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("block.gravel.break"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("block.gravel.break"), SoundSource.NEUTRAL, 2.0F, 1.0F, false
                        );
                     }
                  }

                  for (double var38 = 0.0; var38 <= 2.0; var38++) {
                     BlockPos _bp = BlockPos.containing(
                        entity.getPersistentData().getDouble("block_x"),
                        entity.getPersistentData().getDouble("block_y") + var38,
                        entity.getPersistentData().getDouble("block_z")
                     );
                     BlockState _bs = Blocks.AIR.defaultBlockState();
                     BlockState _bso = world.getBlockState(_bp);
                     UnmodifiableIterator var479 = _bso.getValues().entrySet().iterator();

                     while (var479.hasNext()) {
                        Entry<Property<?>, Comparable<?>> entry = (Entry<Property<?>, Comparable<?>>)var479.next();
                        Property _property = _bs.getBlock().getStateDefinition().getProperty(entry.getKey().getName());
                        if (_property != null && _bs.getValue(_property) != null) {
                           try {
                              _bs = _bs.setValue(_property, (Comparable)entry.getValue());
                           } catch (Exception var30) {
                           }
                        }
                     }

                     world.setBlock(_bp, _bs, 3);
                  }

                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("atk9", 0.0);
                  entity.getPersistentData().putBoolean("Attack9", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack10")) {
               if (entity.getPersistentData().getDouble("atk10") == 0.0) {
                  entity.getPersistentData().putDouble("atk10", 81.0);
               } else {
                  entity.getPersistentData().putDouble("atk10", entity.getPersistentData().getDouble("atk10") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk10") == 81.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.SAND));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.SAND));
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 81, 7, false, false));
                  }

                  entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putBoolean("lock", true);
                  entity.getPersistentData().putBoolean("noai", true);
               } else if (entity.getPersistentData().getDouble("atk10") == 39.0) {
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
                     Entity entityToSpawn = new HerobrineSpike2Entity(SimpleMobsModEntities.HEROBRINE_SPIKE_2.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               } else if (entity.getPersistentData().getDouble("atk10") == 21.0) {
                  entity.getPersistentData().putBoolean("noai", false);
                  entity.getPersistentData().putBoolean("lock", false);
               } else if (entity.getPersistentData().getDouble("atk10") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("atk10", 0.0);
                  entity.getPersistentData().putBoolean("Attack10", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack11")) {
               if (entity.getPersistentData().getDouble("atk11") == 0.0) {
                  entity.getPersistentData().putDouble("atk11", 66.0);
               } else {
                  entity.getPersistentData().putDouble("atk11", entity.getPersistentData().getDouble("atk11") - 1.0);
               }

               if (entity.getPersistentData().getDouble("atk11") == 58.0 || entity.getPersistentData().getDouble("atk11") == 50.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof HerobrineEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.SHORT)
                           .setImpact(2.0F)
                           .setArmorNegation(25.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 15.0F);
                     }
                  }
               }

               if (entity.getPersistentData().getDouble("atk11") == 40.0
                  || entity.getPersistentData().getDouble("atk11") == 35.0
                  || entity.getPersistentData().getDouble("atk11") == 30.0
                  || entity.getPersistentData().getDouble("atk11") == 25.0) {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (!(entityiterator instanceof HerobrineEntity)) {
                        SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                           .setInitialPosition(entity.position())
                           .setStunType(StunType.LONG)
                           .setImpact(3.0F)
                           .setArmorNegation(25.0F);
                        entityiterator.hurt((DamageSource)dmgSrc, 35.0F);
                     }
                  }

                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new Herobrinese3Entity(SimpleMobsModEntities.HEROBRINESE_3.get(), _level);
                     entityToSpawn.moveTo(x, y + entity.getPersistentData().getDouble("atk11") / 40.0, z, Mth.nextInt(net.minecraft.util.RandomSource.create(), 0, 360), 0.0F);
                     entityToSpawn.setYBodyRot(Mth.nextInt(net.minecraft.util.RandomSource.create(), 0, 360));
                     entityToSpawn.setYHeadRot(Mth.nextInt(net.minecraft.util.RandomSource.create(), 0, 360));
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               }

               if (entity.getPersistentData().getDouble("atk11") == 66.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.SANDSTONE));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.SANDSTONE));
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 22, 7, false, false));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
               } else if (entity.getPersistentData().getDouble("atk11") == 58.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ogre_attack3"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           0.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ogre_attack3"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           0.0F,
                           false
                        );
                     }
                  }

                  loop = 0.0;
                  particleAmount = 10.0;
                  xRadius = 1.0;
                  zRadius = 1.0;

                  while (loop < particleAmount) {
                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(
                           ParticleTypes.CLOUD,
                           x + Math.cos((Math.PI * 4) / particleAmount * loop) * xRadius,
                           y + 1.0 + loop * 0.12,
                           z + Math.sin((Math.PI * 4) / particleAmount * loop) * zRadius,
                           1,
                           0.1,
                           0.1,
                           0.1,
                           0.2
                        );
                     }

                     loop++;
                  }
               } else if (entity.getPersistentData().getDouble("atk11") == 54.0) {
                  loop = 0.0;
                  particleAmount = 10.0;
                  xRadius = 1.0;
                  zRadius = 1.0;

                  while (loop < particleAmount) {
                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(
                           ParticleTypes.CLOUD,
                           x + Math.cos((Math.PI * 3) / particleAmount * loop) * xRadius,
                           y + 2.0 - loop * 0.12,
                           z + Math.sin((Math.PI * 3) / particleAmount * loop) * zRadius,
                           1,
                           0.1,
                           0.1,
                           0.1,
                           0.2
                        );
                     }

                     loop++;
                  }
               } else if (entity.getPersistentData().getDouble("atk11") == 51.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ogre_attack3"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ogre_attack3"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  loop = 0.0;
                  particleAmount = 10.0;
                  xRadius = 1.0;
                  zRadius = 1.0;

                  while (loop < particleAmount) {
                     if (world instanceof ServerLevel _level) {
                        _level.sendParticles(
                           ParticleTypes.CLOUD,
                           x + Math.cos((Math.PI * 3) / particleAmount * loop) * xRadius,
                           y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.0, 1.0),
                           z + Math.sin((Math.PI * 3) / particleAmount * loop) * zRadius,
                           2,
                           0.4,
                           0.4,
                           0.4,
                           0.1
                        );
                     }

                     loop++;
                  }
               } else if (entity.getPersistentData().getDouble("atk11") == 43.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 22, 10, false, false));
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ogre_attack3"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("simple_mobs:ogre_attack3"),
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
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           0.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           0.0F,
                           false
                        );
                     }
                  }
               } else if (entity.getPersistentData().getDouble("atk11") == 21.0) {
                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x,
                           y,
                           z,
                           dev.deepnight.SafeSounds.resolve("entity.player.attack.sweep"),
                           SoundSource.NEUTRAL,
                           3.0F,
                           1.0F,
                           false
                        );
                     }
                  }

                  entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putBoolean("lock", true);
                  entity.getPersistentData().putBoolean("noai", true);
               } else if (entity.getPersistentData().getDouble("atk11") == 2.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.INK_SAC));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.INK_SAC));
                  }
               } else if (entity.getPersistentData().getDouble("atk11") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putBoolean("noai", false);
                  entity.getPersistentData().putDouble("atk11", 0.0);
                  entity.getPersistentData().putBoolean("Attack11", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Spreadlava")) {
               if (entity.getPersistentData().getDouble("slava") == 0.0) {
                  entity.getPersistentData().putDouble("slava", 21.0);
               } else {
                  entity.getPersistentData().putDouble("slava", entity.getPersistentData().getDouble("slava") - 1.0);
               }

               if (entity.getPersistentData().getDouble("slava") == 21.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.PACKED_ICE));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.PACKED_ICE));
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 22, 7, false, false));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
               } else if (entity.getPersistentData().getDouble("slava") != 10.0) {
                  if (entity.getPersistentData().getDouble("slava") == 1.0) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                     }

                     entity.getPersistentData().putBoolean("focus", false);
                     entity.getPersistentData().putBoolean("Attacking", false);
                     entity.getPersistentData().putDouble("slava", 0.0);
                     entity.getPersistentData().putBoolean("Spreadlava", false);
                  }
               } else {
                  _center = new Vec3(x, y, z);

                  for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.0), e -> true)
                     .stream()
                     .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                     .collect(Collectors.toList())) {
                     if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                        if (world instanceof Level _level) {
                           if (!_level.isClientSide()) {
                              _level.playSound(
                                 null,
                                 BlockPos.containing(x, y, z),
                                 dev.deepnight.SafeSounds.resolve("item.bucket.empty_lava"),
                                 SoundSource.NEUTRAL,
                                 1.0F,
                                 1.0F
                              );
                           } else {
                              _level.playLocalSound(
                                 x,
                                 y,
                                 z,
                                 dev.deepnight.SafeSounds.resolve("item.bucket.empty_lava"),
                                 SoundSource.NEUTRAL,
                                 1.0F,
                                 1.0F,
                                 false
                              );
                           }
                        }

                        world.setBlock(
                           BlockPos.containing(entityiterator.getX(), entityiterator.getY() + 1.0, entityiterator.getZ()), Blocks.LAVA.defaultBlockState(), 3
                        );
                     }
                  }

                  entity.getPersistentData().putBoolean("haslava", false);
               }
            }

            if (entity.getPersistentData().getBoolean("eat")) {
               if (entity.getPersistentData().getDouble("eattimer") == 0.0) {
                  entity.getPersistentData().putDouble("eattimer", 71.0);
               } else {
                  entity.getPersistentData().putDouble("eattimer", entity.getPersistentData().getDouble("eattimer") - 1.0);
               }

               if (entity.getPersistentData().getDouble("eattimer") == 71.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.ICE));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.ICE));
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 71, 7, false, false));
                  }

                  entity.getPersistentData().putBoolean("focus", true);
               } else if (entity.getPersistentData().getDouble("eattimer") == 69.0) {
                  if (world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new HerobrineFlashEntity(SimpleMobsModEntities.HEROBRINE_FLASH.get(), _level);
                     entityToSpawn.moveTo(x, y, z, entity.getYRot(), 0.0F);
                     entityToSpawn.setYBodyRot(entity.getYRot());
                     entityToSpawn.setYHeadRot(entity.getYRot());
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }
               } else if (entity.getPersistentData().getDouble("eattimer") == 67.0) {
                  Entity _ent = entity;
                  _ent.teleportTo(
                     x + entity.getLookAngle().x * -5.0 + Mth.nextInt(net.minecraft.util.RandomSource.create(), -4, 4),
                     y,
                     z + entity.getLookAngle().z * -5.0 + Mth.nextInt(net.minecraft.util.RandomSource.create(), -4, 4)
                  );
                  if (_ent instanceof ServerPlayer _serverPlayer) {
                     _serverPlayer.connection
                        .teleport(
                           x + entity.getLookAngle().x * -5.0 + Mth.nextInt(net.minecraft.util.RandomSource.create(), -4, 4),
                           y,
                           z + entity.getLookAngle().z * -5.0 + Mth.nextInt(net.minecraft.util.RandomSource.create(), -4, 4),
                           _ent.getYRot(),
                           _ent.getXRot()
                        );
                  }
               } else if (entity.getPersistentData().getDouble("eattimer") == 66.0) {
                  i = -1.0;
                  j = 1.0;
                  k = -1.0;

                  while (i <= 1.0) {
                     while (j <= 3.0) {
                        for (; k <= 1.0; k++) {
                           if (world.getBlockState(BlockPos.containing(x + i, y + j, z + k)).canOcclude()) {
                              world.setBlock(BlockPos.containing(x + i, y + j, z + k), Blocks.AIR.defaultBlockState(), 3);
                           }
                        }

                        k = -1.0;
                        j++;
                     }

                     j = 1.0;
                     i++;
                  }
               } else if (entity.getPersistentData().getDouble("eattimer") == 21.0) {
                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.HEAL, 1, 4, false, false));
                  }

                  if (entity instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 1, false, false));
                  }
               } else if (entity.getPersistentData().getDouble("eattimer") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("eattimer", 0.0);
                  entity.getPersistentData().putBoolean("eat", false);
               }

               if (entity.getPersistentData().getDouble("eattimer") <= 61.0
                  && entity.getPersistentData().getDouble("eattimer") >= 21.0
                  && entity.getPersistentData().getBoolean("hurt")) {
                  entity.getPersistentData().putBoolean("Stun", true);
                  entity.getPersistentData().putBoolean("focus", false);
                  entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
                  entity.getPersistentData().putBoolean("lock", true);
                  entity.getPersistentData().putDouble("eattimer", 0.0);
                  entity.getPersistentData().putBoolean("eat", false);
               }

               if (entity.getPersistentData().getDouble("eattimer") == 25.0
                  || entity.getPersistentData().getDouble("eattimer") == 30.0
                  || entity.getPersistentData().getDouble("eattimer") == 35.0
                  || entity.getPersistentData().getDouble("eattimer") == 40.0
                  || entity.getPersistentData().getDouble("eattimer") == 45.0
                  || entity.getPersistentData().getDouble("eattimer") == 50.0
                  || entity.getPersistentData().getDouble("eattimer") == 55.0) {
                  if (world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/particle minecraft:item golden_apple ^-.1 ^1.25 ^.65 .1 .1 .1 0.075 1"
                        );
                  }

                  if (world instanceof Level _level) {
                     if (!_level.isClientSide()) {
                        _level.playSound(
                           null,
                           BlockPos.containing(x, y, z),
                           dev.deepnight.SafeSounds.resolve("entity.generic.eat"),
                           SoundSource.NEUTRAL,
                           2.0F,
                           1.0F
                        );
                     } else {
                        _level.playLocalSound(
                           x, y, z, dev.deepnight.SafeSounds.resolve("entity.generic.eat"), SoundSource.NEUTRAL, 2.0F, 1.0F, false
                        );
                     }
                  }
               }
            }

            if (entity.getPersistentData().getBoolean("Stun")) {
               if (entity.getPersistentData().getDouble("stun") == 0.0) {
                  entity.getPersistentData().putDouble("stun", 61.0);
               } else {
                  entity.getPersistentData().putDouble("stun", entity.getPersistentData().getDouble("stun") - 1.0);
               }

               if (entity.getPersistentData().getDouble("stun") == 61.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.LANTERN));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.LANTERN));
                  }

                  if (entity instanceof LivingEntity _entityx) {
                     _entityx.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 61, 7, false, false));
                  }

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
               } else if (entity.getPersistentData().getDouble("stun") == 1.0) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("lock", false);
                  entity.getPersistentData().putBoolean("hurt", false);
                  entity.getPersistentData().putBoolean("Attacking", false);
                  entity.getPersistentData().putDouble("stun", 0.0);
                  entity.getPersistentData().putBoolean("Stun", false);
               }
            }
         }
      }
   }
}
