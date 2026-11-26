package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.SkeletonlordEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class ProSpell3Procedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         double xRadius = 0.0;
         double loop = 0.0;
         double zRadius = 0.0;
         double particleAmount = 0.0;
         if (entity.getPersistentData().getDouble("cd") == 0.0) {
            entity.getPersistentData().putDouble("cd", 50.0);
            entity.getPersistentData().putDouble("pa", 8.0);
            entity.getPersistentData().putDouble("loop", 0.0);
            entity.getPersistentData().putDouble("r", 2.5);
            entity.getPersistentData().putDouble("dir", Mth.nextInt(net.minecraft.util.RandomSource.create(), 0, 8));
            if (world instanceof Level _level) {
               if (!_level.isClientSide()) {
                  _level.playSound(
                     null,
                     BlockPos.containing(x, y, z),
                     dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_spell"),
                     SoundSource.NEUTRAL,
                     2.0F,
                     1.0F
                  );
               } else {
                  _level.playLocalSound(
                     x,
                     y,
                     z,
                     dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_spell"),
                     SoundSource.NEUTRAL,
                     2.0F,
                     1.0F,
                     false
                  );
               }
            }
         } else {
            if (entity.getPersistentData().getDouble("dir") < entity.getPersistentData().getDouble("pa")) {
               if (world instanceof ServerLevel _level) {
                  _level.sendParticles(
                     ParticleTypes.ENCHANTED_HIT,
                     x
                        + Math.cos((Math.PI * 2) / entity.getPersistentData().getDouble("dir") * entity.getPersistentData().getDouble("loop"))
                           * entity.getPersistentData().getDouble("r"),
                     y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.5, 2.0),
                     z
                        + Math.sin((Math.PI * 2) / entity.getPersistentData().getDouble("dir") * entity.getPersistentData().getDouble("loop"))
                           * entity.getPersistentData().getDouble("r"),
                     1,
                     2.0,
                     2.0,
                     2.0,
                     0.1
                  );
               }

               if (world instanceof ServerLevel _level) {
                  _level.sendParticles(
                     ParticleTypes.CRIT,
                     x
                        + Math.cos((Math.PI * 2) / entity.getPersistentData().getDouble("dir") * entity.getPersistentData().getDouble("loop"))
                           * entity.getPersistentData().getDouble("r"),
                     y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.5, 2.0),
                     z
                        + Math.sin((Math.PI * 2) / entity.getPersistentData().getDouble("dir") * entity.getPersistentData().getDouble("loop"))
                           * entity.getPersistentData().getDouble("r"),
                     1,
                     0.5,
                     0.5,
                     0.5,
                     0.1
                  );
               }
            } else {
               entity.getPersistentData().putDouble("dir", 0.0);
            }

            if (entity.getPersistentData().getDouble("loop") < entity.getPersistentData().getDouble("pa")) {
               if (world instanceof ServerLevel _level) {
                  _level.sendParticles(
                     ParticleTypes.REVERSE_PORTAL,
                     x
                        + Math.cos((Math.PI * 2) / entity.getPersistentData().getDouble("pa") * entity.getPersistentData().getDouble("loop"))
                           * entity.getPersistentData().getDouble("r"),
                     y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.5, 2.0),
                     z
                        + Math.sin((Math.PI * 2) / entity.getPersistentData().getDouble("pa") * entity.getPersistentData().getDouble("loop"))
                           * entity.getPersistentData().getDouble("r"),
                     2,
                     1.0,
                     1.0,
                     1.0,
                     0.1
                  );
               }

               if (world instanceof ServerLevel _level) {
                  _level.sendParticles(
                     ParticleTypes.PORTAL,
                     x
                        + Math.cos((Math.PI * 2) / entity.getPersistentData().getDouble("pa") * entity.getPersistentData().getDouble("loop"))
                           * entity.getPersistentData().getDouble("r"),
                     y + Mth.nextDouble(net.minecraft.util.RandomSource.create(), 0.5, 2.0),
                     z
                        + Math.sin((Math.PI * 2) / entity.getPersistentData().getDouble("pa") * entity.getPersistentData().getDouble("loop"))
                           * entity.getPersistentData().getDouble("r"),
                     2,
                     0.5,
                     0.5,
                     0.5,
                     0.1
                  );
               }

               entity.getPersistentData().putDouble("loop", entity.getPersistentData().getDouble("loop") + 1.0);
            } else {
               entity.getPersistentData().putDouble("loop", 0.0);
            }

            entity.getPersistentData().putDouble("cd", entity.getPersistentData().getDouble("cd") - 1.0);
         }

         if (entity.getPersistentData().getDouble("cd") < 24.0 && world instanceof ServerLevel _level) {
            _level.sendParticles(ParticleTypes.CLOUD, x, y, z, 1, 2.0, 2.0, 2.0, 1.0);
         }

         if (entity.getPersistentData().getDouble("cd") == 32.0) {
            if (world instanceof Level _level) {
               if (!_level.isClientSide()) {
                  _level.playSound(
                     null,
                     BlockPos.containing(x, y, z),
                     dev.deepnight.SafeSounds.resolve("entity.enderman.teleport"),
                     SoundSource.NEUTRAL,
                     2.0F,
                     0.5F
                  );
               } else {
                  _level.playLocalSound(
                     x, y, z, dev.deepnight.SafeSounds.resolve("entity.enderman.teleport"), SoundSource.NEUTRAL, 2.0F, 0.5F, false
                  );
               }
            }

            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(100.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator instanceof SkeletonlordEntity) {
                  entity.getPersistentData().putDouble("x", entityiterator.getX() + 1.5 * entity.getLookAngle().x);
                  entity.getPersistentData().putDouble("y", entityiterator.getY());
                  entity.getPersistentData().putDouble("z", entityiterator.getZ() + 1.5 * entity.getLookAngle().z);
               }
            }
         } else if (entity.getPersistentData().getDouble("cd") == 31.0) {
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (!world.getEntitiesOfClass(SkeletonlordEntity.class, AABB.ofSize(new Vec3(x, y, z), 200.0, 200.0, 200.0), e -> true).isEmpty()) {
                  Entity _ent = entityiterator;
                  _ent.teleportTo(
                     entity.getPersistentData().getDouble("x"), entity.getPersistentData().getDouble("y"), entity.getPersistentData().getDouble("z")
                  );
                  if (_ent instanceof ServerPlayer _serverPlayer) {
                     _serverPlayer.connection
                        .teleport(
                           entity.getPersistentData().getDouble("x"),
                           entity.getPersistentData().getDouble("y"),
                           entity.getPersistentData().getDouble("z"),
                           _ent.getYRot(),
                           _ent.getXRot()
                        );
                  }
               }
            }
         } else if (entity.getPersistentData().getDouble("cd") == 1.0 && !entity.level().isClientSide()) {
            entity.discard();
         }
      }
   }
}
