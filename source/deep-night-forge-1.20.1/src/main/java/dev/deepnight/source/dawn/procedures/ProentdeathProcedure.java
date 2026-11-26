package dev.deepnight.source.dawn.procedures;

import dev.deepnight.source.dawn.entity.StaffInteractEntity;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.registries.ForgeRegistries;

public class ProentdeathProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (entity.getPersistentData().getBoolean("dead")) {
            if (entity.getPersistentData().getDouble("deathcd") == 0.0) {
               entity.getPersistentData().putDouble("deathcd", 101.0);
            } else {
               entity.getPersistentData().putDouble("deathcd", entity.getPersistentData().getDouble("deathcd") - 1.0);
            }

            if (entity.getPersistentData().getDouble("deathcd") >= 1.0
               && entity.getPersistentData().getDouble("deathcd") <= 6.0
               && world instanceof ServerLevel _level) {
               _level.sendParticles(ParticleTypes.CLOUD, x, y + 1.0, z, 50, 2.0, 2.0, 2.0, 0.3);
            }

            if (entity.getPersistentData().getDouble("deathcd") == 1.0) {
               if (world instanceof ServerLevel _level) {
                  Entity entityToSpawn = new StaffInteractEntity(SimpleMobsModEntities.STAFF_INTERACT.get(), _level);
                  entityToSpawn.moveTo(x, y, z, world.getRandom().nextFloat() * 360.0F, 0.0F);
                  if (entityToSpawn instanceof Mob _mobToSpawn) {
                     _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                  }

                  world.addFreshEntity(entityToSpawn);
               }

               entity.setCustomName(Component.literal("Staff"));
               entity.getPersistentData().putBoolean("dead", false);
               entity.getPersistentData().putBoolean("whisperer", false);
               // The staff replaces the defeated Ent after its original death animation.
               if (!entity.level().isClientSide()) entity.remove(Entity.RemovalReason.KILLED);
            } else if (entity.getPersistentData().getDouble("deathcd") == 68.0 && world instanceof Level _level) {
               if (!_level.isClientSide()) {
                  _level.playSound(
                     null,
                     BlockPos.containing(x, y, z),
                     dev.deepnight.SafeSounds.resolve("simple_mobs:small_step"),
                     SoundSource.NEUTRAL,
                     3.0F,
                     0.5F
                  );
               } else {
                  _level.playLocalSound(
                     x, y, z, dev.deepnight.SafeSounds.resolve("simple_mobs:small_step"), SoundSource.NEUTRAL, 3.0F, 0.5F, false
                  );
               }
            }
         }
      }
   }
}
