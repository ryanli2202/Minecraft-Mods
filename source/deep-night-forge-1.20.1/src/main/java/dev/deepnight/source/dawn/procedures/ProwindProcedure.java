package dev.deepnight.source.dawn.procedures;

import java.util.Random;
import dev.deepnight.source.dawn.entity.WindEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;

import net.minecraft.world.phys.Vec3;

public class ProwindProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (!entity.getPersistentData().getBoolean("clientlogged")) {
            entity.getPersistentData().putDouble("yaw", entity.getYRot());
            entity.getPersistentData().putDouble("pitch", entity.getXRot());
            entity.getEntityData().set(WindEntity.pitch, (float)entity.getPersistentData().getDouble("pitch"));
            entity.getPersistentData().putBoolean("clientlogged", true);
         }

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

         if (!world.isClientSide()) {
            entity.setDeltaMovement(
               new Vec3(
                  Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180.0)) * 1.0,
                  Math.sin(Math.toRadians(0.0 - entity.getPersistentData().getDouble("pitch"))) * 1.5,
                  Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * 1.0
               )
            );
            if (entity.getPersistentData().getDouble("despawn") == 0.0) {
               entity.getPersistentData().putDouble("despawn", 100.0);
               entity.getPersistentData().putDouble("yaw", entity.getYRot());
               entity.getPersistentData().putDouble("pitch", entity.getXRot());
            } else {
               entity.getPersistentData().putDouble("despawn", entity.getPersistentData().getDouble("despawn") - 1.0);
            }

            if (entity.getPersistentData().getDouble("despawn") == 100.0) {
               entity.getPersistentData().putBoolean("lock", true);
            } else if (entity.getPersistentData().getDouble("despawn") == 1.0 && !entity.level().isClientSide()) {
               entity.discard();
            }

            entity.getPersistentData().putDouble("i", Mth.nextInt(net.minecraft.util.RandomSource.create(), -1, 1));
            entity.getPersistentData().putDouble("j", Mth.nextInt(net.minecraft.util.RandomSource.create(), -1, 1));
            entity.getPersistentData().putDouble("k", Mth.nextInt(net.minecraft.util.RandomSource.create(), -1, 1));
            if (world.getBlockState(
                  BlockPos.containing(
                     x + entity.getPersistentData().getDouble("i"),
                     y + entity.getPersistentData().getDouble("j"),
                     z + entity.getPersistentData().getDouble("k")
                  )
               )
               .canOcclude()) {
               if (!entity.level().isClientSide()) {
                  entity.discard();
               }
            } else if (world.getBlockState(
                        BlockPos.containing(
                           x + entity.getPersistentData().getDouble("i"),
                           y + entity.getPersistentData().getDouble("j"),
                           z + entity.getPersistentData().getDouble("k")
                        )
                     )
                     .is(net.minecraft.tags.BlockTags.LEAVES)
               && !entity.level().isClientSide()) {
               entity.discard();
            }
         }
      }
   }
}
