package dev.deepnight.source.dawn.procedures;

import com.google.common.collect.UnmodifiableIterator;
import java.util.Map.Entry;
import dev.deepnight.source.dawn.entity.TreeDrainParticlesEntity;
import dev.deepnight.source.dawn.init.SimpleMobsModBlocks;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.network.SimpleMobsModVariables;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class ProtreechopProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         double i = 0.0;
         double j = 0.0;
         double c = 0.0;
         double i1 = 0.0;
         double j2 = 0.0;
         double c2 = 0.0;
         boolean found = false;
         boolean found2 = false;
         boolean found1 = false;
         if (!world.isClientSide()) {
            if (entity.getPersistentData().getBoolean("tree_connected")) {
               if (world.getBlockState(
                     BlockPos.containing(
                        entity.getPersistentData().getDouble("tree_x"),
                        entity.getPersistentData().getDouble("tree_y"),
                        entity.getPersistentData().getDouble("tree_z")
                     )
                  )
                  .is(BlockTags.create(new ResourceLocation("minecraft:logs")))) {
                  world.destroyBlock(
                     BlockPos.containing(
                        entity.getPersistentData().getDouble("tree_x"),
                        entity.getPersistentData().getDouble("tree_y"),
                        entity.getPersistentData().getDouble("tree_z")
                     ),
                     false
                  );
                  entity.getPersistentData().putDouble("wood_consumed", entity.getPersistentData().getDouble("wood_consumed") + 1.0);
                  i = 1.0;
                  j = 1.0;
                  c = 1.0;

                  while (i >= -1.0) {
                     while (j >= -1.0) {
                        for (; c >= -1.0; c--) {
                           if (world.getBlockState(
                                 BlockPos.containing(
                                    entity.getPersistentData().getDouble("tree_x") + i,
                                    entity.getPersistentData().getDouble("tree_y") + j,
                                    entity.getPersistentData().getDouble("tree_z") + c
                                 )
                              )
                              .is(BlockTags.create(new ResourceLocation("minecraft:logs")))) {
                              if (!found1) {
                                 entity.getPersistentData().putDouble("tree_x_n", entity.getPersistentData().getDouble("tree_x") + i);
                                 entity.getPersistentData().putDouble("tree_y_n", entity.getPersistentData().getDouble("tree_y") + j);
                                 entity.getPersistentData().putDouble("tree_z_n", entity.getPersistentData().getDouble("tree_z") + c);
                                 found = true;
                              } else {
                                 entity.getPersistentData().putDouble("tree_x_b", entity.getPersistentData().getDouble("tree_x") + i);
                                 entity.getPersistentData().putDouble("tree_y_b", entity.getPersistentData().getDouble("tree_y") + j);
                                 entity.getPersistentData().putDouble("tree_z_b", entity.getPersistentData().getDouble("tree_z") + c);
                                 entity.getPersistentData().putBoolean("tree_b", true);
                              }
                           }
                        }

                        c = 1.0;
                        j--;
                     }

                     j = 1.0;
                     i--;
                  }
               }

               if (found) {
                  entity.getPersistentData().putDouble("tree_x", entity.getPersistentData().getDouble("tree_x_n"));
                  entity.getPersistentData().putDouble("tree_y", entity.getPersistentData().getDouble("tree_y_n"));
                  entity.getPersistentData().putDouble("tree_z", entity.getPersistentData().getDouble("tree_z_n"));
               } else if (!entity.getPersistentData().getBoolean("tree_b")) {
                  entity.getPersistentData().putBoolean("tree_connected", false);
               } else {
                  entity.getPersistentData().putDouble("tree_x", entity.getPersistentData().getDouble("tree_x_b"));
                  entity.getPersistentData().putDouble("tree_y", entity.getPersistentData().getDouble("tree_y_b"));
                  entity.getPersistentData().putDouble("tree_z", entity.getPersistentData().getDouble("tree_z_b"));
                  entity.getPersistentData().putBoolean("tree_b", false);
               }
            } else if (entity.getPersistentData().getBoolean("tree_drain")) {
               if (world.getBlockState(
                     BlockPos.containing(
                        entity.getPersistentData().getDouble("tree_x"),
                        entity.getPersistentData().getDouble("tree_y"),
                        entity.getPersistentData().getDouble("tree_z")
                     )
                  )
                  .is(BlockTags.create(new ResourceLocation("minecraft:logs")))) {
                  BlockPos _bp = BlockPos.containing(
                     entity.getPersistentData().getDouble("tree_x"),
                     entity.getPersistentData().getDouble("tree_y"),
                     entity.getPersistentData().getDouble("tree_z")
                  );
                  BlockState _bs = SimpleMobsModBlocks.DRAINED_WOOD.get().defaultBlockState();
                  BlockState _bso = world.getBlockState(_bp);
                  UnmodifiableIterator var26 = _bso.getValues().entrySet().iterator();

                  while (var26.hasNext()) {
                     Entry<Property<?>, Comparable<?>> entry = (Entry<Property<?>, Comparable<?>>)var26.next();
                     Property _property = _bs.getBlock().getStateDefinition().getProperty(entry.getKey().getName());
                     if (_property != null && _bs.getValue(_property) != null) {
                        try {
                           _bs = _bs.setValue(_property, (Comparable)entry.getValue());
                        } catch (Exception var31) {
                        }
                     }
                  }

                  world.setBlock(_bp, _bs, 3);
                  if (Math.random() >= 0.5 && world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new TreeDrainParticlesEntity(SimpleMobsModEntities.TREE_DRAIN_PARTICLES.get(), _level);
                     entityToSpawn.moveTo(
                        entity.getPersistentData().getDouble("tree_x"),
                        entity.getPersistentData().getDouble("tree_y"),
                        entity.getPersistentData().getDouble("tree_z"),
                        world.getRandom().nextFloat() * 360.0F,
                        0.0F
                     );
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  entity.getPersistentData().putDouble("wood_consumed", entity.getPersistentData().getDouble("wood_consumed") + 1.0);
                  i = 1.0;
                  j = 1.0;
                  c = 1.0;

                  while (i >= -1.0) {
                     while (j >= -1.0) {
                        for (; c >= -1.0; c--) {
                           if (world.getBlockState(
                                 BlockPos.containing(
                                    entity.getPersistentData().getDouble("tree_x") + i,
                                    entity.getPersistentData().getDouble("tree_y") + j,
                                    entity.getPersistentData().getDouble("tree_z") + c
                                 )
                              )
                              .is(BlockTags.create(new ResourceLocation("minecraft:logs")))) {
                              if (!found1) {
                                 entity.getPersistentData().putDouble("tree_x_n", entity.getPersistentData().getDouble("tree_x") + i);
                                 entity.getPersistentData().putDouble("tree_y_n", entity.getPersistentData().getDouble("tree_y") + j);
                                 entity.getPersistentData().putDouble("tree_z_n", entity.getPersistentData().getDouble("tree_z") + c);
                                 found1 = true;
                              } else {
                                 entity.getPersistentData().putDouble("tree_x_b", entity.getPersistentData().getDouble("tree_x") + i);
                                 entity.getPersistentData().putDouble("tree_y_b", entity.getPersistentData().getDouble("tree_y") + j);
                                 entity.getPersistentData().putDouble("tree_z_b", entity.getPersistentData().getDouble("tree_z") + c);
                                 entity.getPersistentData().putBoolean("tree_b", true);
                              }
                           }
                        }

                        c = 1.0;
                        j--;
                     }

                     j = 1.0;
                     i--;
                  }
               }

               if (found1) {
                  entity.getPersistentData().putDouble("tree_x", entity.getPersistentData().getDouble("tree_x_n"));
                  entity.getPersistentData().putDouble("tree_y", entity.getPersistentData().getDouble("tree_y_n"));
                  entity.getPersistentData().putDouble("tree_z", entity.getPersistentData().getDouble("tree_z_n"));
               } else if (!entity.getPersistentData().getBoolean("tree_b")) {
                  entity.getPersistentData().putBoolean("tree_drain", false);
               } else {
                  entity.getPersistentData().putDouble("tree_x", entity.getPersistentData().getDouble("tree_x_b"));
                  entity.getPersistentData().putDouble("tree_y", entity.getPersistentData().getDouble("tree_y_b"));
                  entity.getPersistentData().putDouble("tree_z", entity.getPersistentData().getDouble("tree_z_b"));
                  entity.getPersistentData().putBoolean("tree_b", false);
               }
            }

            if (entity.getPersistentData().getBoolean("tree_drain2")) {
               if (world.getBlockState(
                     BlockPos.containing(
                        entity.getPersistentData().getDouble("tree_x2"),
                        entity.getPersistentData().getDouble("tree_y2"),
                        entity.getPersistentData().getDouble("tree_z2")
                     )
                  )
                  .is(BlockTags.create(new ResourceLocation("minecraft:logs")))) {
                  BlockPos _bp = BlockPos.containing(
                     entity.getPersistentData().getDouble("tree_x2"),
                     entity.getPersistentData().getDouble("tree_y2"),
                     entity.getPersistentData().getDouble("tree_z2")
                  );
                  BlockState _bs = SimpleMobsModBlocks.DRAINED_WOOD.get().defaultBlockState();
                  BlockState _bso = world.getBlockState(_bp);
                  UnmodifiableIterator var44 = _bso.getValues().entrySet().iterator();

                  while (var44.hasNext()) {
                     Entry<Property<?>, Comparable<?>> entry = (Entry<Property<?>, Comparable<?>>)var44.next();
                     Property _property = _bs.getBlock().getStateDefinition().getProperty(entry.getKey().getName());
                     if (_property != null && _bs.getValue(_property) != null) {
                        try {
                           _bs = _bs.setValue(_property, (Comparable)entry.getValue());
                        } catch (Exception var30) {
                        }
                     }
                  }

                  world.setBlock(_bp, _bs, 3);
                  if (Math.random() >= 0.5 && world instanceof ServerLevel _level) {
                     Entity entityToSpawn = new TreeDrainParticlesEntity(SimpleMobsModEntities.TREE_DRAIN_PARTICLES.get(), _level);
                     entityToSpawn.moveTo(
                        entity.getPersistentData().getDouble("tree_x"),
                        entity.getPersistentData().getDouble("tree_y"),
                        entity.getPersistentData().getDouble("tree_z"),
                        world.getRandom().nextFloat() * 360.0F,
                        0.0F
                     );
                     if (entityToSpawn instanceof Mob _mobToSpawn) {
                        _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                     }

                     world.addFreshEntity(entityToSpawn);
                  }

                  entity.getPersistentData().putDouble("wood_consumed", entity.getPersistentData().getDouble("wood_consumed") + 1.0);
                  i1 = 1.0;
                  j2 = 1.0;
                  c2 = 1.0;

                  while (i1 >= -1.0) {
                     while (j2 >= -1.0) {
                        for (; c2 >= -1.0; c2--) {
                           if (world.getBlockState(
                                 BlockPos.containing(
                                    entity.getPersistentData().getDouble("tree_x2") + i1,
                                    entity.getPersistentData().getDouble("tree_y2") + j2,
                                    entity.getPersistentData().getDouble("tree_z2") + c2
                                 )
                              )
                              .is(BlockTags.create(new ResourceLocation("minecraft:logs")))) {
                              if (!found2) {
                                 entity.getPersistentData().putDouble("tree_x2_n", entity.getPersistentData().getDouble("tree_x2") + i1);
                                 entity.getPersistentData().putDouble("tree_y2_n", entity.getPersistentData().getDouble("tree_y2") + j2);
                                 entity.getPersistentData().putDouble("tree_z2_n", entity.getPersistentData().getDouble("tree_z2") + c2);
                                 found2 = true;
                              } else {
                                 entity.getPersistentData().putDouble("tree_x2_b", entity.getPersistentData().getDouble("tree_x2") + i1);
                                 entity.getPersistentData().putDouble("tree_y2_b", entity.getPersistentData().getDouble("tree_y2") + j2);
                                 entity.getPersistentData().putDouble("tree_z2_b", entity.getPersistentData().getDouble("tree_z2") + c2);
                                 entity.getPersistentData().putBoolean("tree_b2", true);
                              }
                           } else {
                              entity.getPersistentData().putDouble("tree_x_b2", entity.getPersistentData().getDouble("tree_x2") + i);
                              entity.getPersistentData().putDouble("tree_y_b2", entity.getPersistentData().getDouble("tree_y2") + j);
                              entity.getPersistentData().putDouble("tree_z_b2", entity.getPersistentData().getDouble("tree_z2") + c);
                              entity.getPersistentData().putBoolean("tree_b2", true);
                           }
                        }

                        c2 = 1.0;
                        j2--;
                     }

                     j2 = 1.0;
                     i1--;
                  }
               }

               if (found2) {
                  entity.getPersistentData().putDouble("tree_x2", entity.getPersistentData().getDouble("tree_x2_n"));
                  entity.getPersistentData().putDouble("tree_y2", entity.getPersistentData().getDouble("tree_y2_n"));
                  entity.getPersistentData().putDouble("tree_z2", entity.getPersistentData().getDouble("tree_z2_n"));
               } else if (!entity.getPersistentData().getBoolean("tree_b2")) {
                  entity.getPersistentData().putBoolean("tree_drain2", false);
               } else {
                  entity.getPersistentData().putDouble("tree_x2", entity.getPersistentData().getDouble("tree_x2_b"));
                  entity.getPersistentData().putDouble("tree_y2", entity.getPersistentData().getDouble("tree_y2_b"));
                  entity.getPersistentData().putDouble("tree_z2", entity.getPersistentData().getDouble("tree_z2_b"));
                  entity.getPersistentData().putBoolean("tree_b2", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Attack6")) {
               if (entity.getPersistentData().getDouble("bbtimer") == 0.0) {
                  entity.getPersistentData().putDouble("bbtimer", 80.0);
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
                              <= 2.0F) {
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
               } else {
                  entity.getPersistentData().putDouble("bbtimer", entity.getPersistentData().getDouble("bbtimer") - 1.0);
               }
            }
         }
      }
   }
}
