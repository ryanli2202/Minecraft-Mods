package dev.deepnight.source.dawn.procedures;

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
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class Proea1playerProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         double i = 0.0;
         if (!entity.getPersistentData().getBoolean("clientlogged")) {
            entity.getPersistentData().putDouble("yaw", entity.getYRot());
            entity.getPersistentData().putDouble("pitch", entity.getXRot());
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

         if (!world.isClientSide() && entity.isAlive()) {
            entity.setDeltaMovement(new Vec3(0.0, 0.0, 0.0));
            if (entity.getPersistentData().getDouble("cd") == 0.0) {
               entity.getPersistentData().putDouble("cd", 47.0);
               entity.getPersistentData().putBoolean("lock", true);
               if (world instanceof ServerLevel _level) {
                  _level.getServer()
                     .getCommands()
                     .performPrefixedCommand(
                        new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null)
                           .withSuppressedOutput(),
                        "/data merge entity @e[type=simple_mobs:elemental_attack_1_player,limit=1,sort=nearest] {NoAI:1b}"
                     );
               }
            } else {
               entity.getPersistentData().putDouble("cd", entity.getPersistentData().getDouble("cd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("cd") == 6.0) {
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
                  entityToSpawn.moveTo(x, y + 1.0, z, (float)entity.getPersistentData().getDouble("yaw"), (float)entity.getPersistentData().getDouble("pitch"));
                  entityToSpawn.setYBodyRot((float)entity.getPersistentData().getDouble("yaw"));
                  entityToSpawn.setYHeadRot((float)entity.getPersistentData().getDouble("yaw"));
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
