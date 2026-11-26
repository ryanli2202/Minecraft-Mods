package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.ElementalProjectileEntity;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class Proea1Procedure {
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

         if (!world.isClientSide() && entity.isAlive()) {
            entity.setDeltaMovement(new Vec3(0.0, 0.0, 0.0));
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(32.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
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
                        "pitch2", Math.toDegrees(Math.atan(entity.getPersistentData().getDouble("distance") / (entityiterator.getY() - (entity.getY() + 0.8))))
                     );
                  if (entity.getPersistentData().getDouble("pitch2") > 0.0) {
                     entity.getPersistentData().putDouble("pitch2", 90.0 - entity.getPersistentData().getDouble("pitch2"));
                  } else if (entity.getPersistentData().getDouble("pitch2") < 0.0) {
                     entity.getPersistentData().putDouble("pitch2", 90.0 + entity.getPersistentData().getDouble("pitch2"));
                  }
               }
            }

            if (entity.getPersistentData().getDouble("cd") == 0.0) {
               entity.getPersistentData().putDouble("cd", 47.0);
               entity.getPersistentData().putBoolean("focus", true);
            } else {
               entity.getPersistentData().putDouble("cd", entity.getPersistentData().getDouble("cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("cd") == 6.0) {
               if (world instanceof ServerLevel _level) {
                  _level.getServer()
                     .getCommands()
                     .performPrefixedCommand(
                        new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null)
                           .withSuppressedOutput(),
                        "/data merge entity @e[type=simple_mobs:elemental_attack_1,limit=1,sort=nearest] {NoAI:1b}"
                     );
               }

               if (world instanceof Level _level) {
                  if (!_level.isClientSide()) {
                     _level.playSound(
                        null,
                        BlockPos.containing(x, y, z),
                        dev.deepnight.SafeSounds.resolve("simple_mobs:elemental_attack1"),
                        SoundSource.NEUTRAL,
                        3.0F,
                        1.0F
                     );
                  } else {
                     _level.playLocalSound(
                        x,
                        y,
                        z,
                        dev.deepnight.SafeSounds.resolve("simple_mobs:elemental_attack1"),
                        SoundSource.NEUTRAL,
                        3.0F,
                        1.0F,
                        false
                     );
                  }
               }

               if (world instanceof ServerLevel _level) {
                  Entity entityToSpawn = new ElementalProjectileEntity(SimpleMobsModEntities.ELEMENTAL_PROJECTILE.get(), _level);
                  entityToSpawn.moveTo(
                     x, y + 1.0, z, (float)entity.getPersistentData().getDouble("yaw2"), (float)entity.getPersistentData().getDouble("pitch2")
                  );
                  entityToSpawn.setYBodyRot((float)entity.getPersistentData().getDouble("yaw2"));
                  entityToSpawn.setYHeadRot((float)entity.getPersistentData().getDouble("yaw2"));
                  if (entityToSpawn instanceof Mob _mobToSpawn) {
                     _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                  }

                  world.addFreshEntity(entityToSpawn);
               }
            } else if (entity.getPersistentData().getDouble("cd") == 1.0 && !entity.level().isClientSide()) {
               entity.discard();
            }
         }
      }
   }
}
