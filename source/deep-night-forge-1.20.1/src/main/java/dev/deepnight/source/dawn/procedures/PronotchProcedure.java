package dev.deepnight.source.dawn.procedures;

import dev.deepnight.source.dawn.entity.Notchflash1Entity;
import dev.deepnight.source.dawn.entity.Notchflash2Entity;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class PronotchProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (entity.isAlive()) {
            if (entity.getPersistentData().getBoolean("removelog")) {
               Pronotch1Procedure.execute(world, x, y, z, entity);
            } else {
               Pronotch2Procedure.execute(world, x, y, z, entity);
            }
         }

         if (!entity.getPersistentData().getBoolean("Aggro")) {
            if (entity.getPersistentData().getDouble("aggrotimer") == 0.0) {
               entity.getPersistentData().putDouble("aggrotimer", 100.0);
            } else {
               entity.getPersistentData().putDouble("aggrotimer", entity.getPersistentData().getDouble("aggrotimer") - 1.0);
            }

            if (entity.getPersistentData().getDouble("aggrotimer") == 1.0 && entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
               _entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 80, 4, false, false));
            }
         } else {
            entity.getPersistentData().putDouble("aggrotimer", 0.0);
         }

         if ((world instanceof Level _lvl ? _lvl.dimension() : Level.OVERWORLD)
               == ResourceKey.<Level>create(net.minecraft.core.registries.Registries.DIMENSION, new ResourceLocation("simple_mobs:distant_land"))
            && Math.sqrt(Math.pow(x - 0.0, 2.0) + Math.pow(z - 0.0, 2.0)) > 50.0
            && !entity.getPersistentData().getBoolean("Attacking")) {
            if (!entity.getPersistentData().getBoolean("removelog")) {
               if (world instanceof ServerLevel _level) {
                  Entity entityToSpawn = new Notchflash2Entity(SimpleMobsModEntities.NOTCHFLASH_2.get(), _level);
                  entityToSpawn.moveTo(x, y, z, entity.getYRot(), 0.0F);
                  entityToSpawn.setYBodyRot(entity.getYRot());
                  entityToSpawn.setYHeadRot(entity.getYRot());
                  if (entityToSpawn instanceof Mob _mobToSpawn) {
                     _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                  }

                  world.addFreshEntity(entityToSpawn);
               }

               Entity _ent = entity;
               _ent.teleportTo(0.0, y, 0.0);
               if (_ent instanceof ServerPlayer _serverPlayer) {
                  _serverPlayer.connection.teleport(0.0, y, 0.0, _ent.getYRot(), _ent.getXRot());
               }
            } else {
               if (world instanceof ServerLevel _level) {
                  Entity entityToSpawn = new Notchflash1Entity(SimpleMobsModEntities.NOTCHFLASH_1.get(), _level);
                  entityToSpawn.moveTo(x, y, z, entity.getYRot(), 0.0F);
                  entityToSpawn.setYBodyRot(entity.getYRot());
                  entityToSpawn.setYHeadRot(entity.getYRot());
                  if (entityToSpawn instanceof Mob _mobToSpawn) {
                     _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                  }

                  world.addFreshEntity(entityToSpawn);
               }

               Entity _ent = entity;
               _ent.teleportTo(0.0, y, 0.0);
               if (_ent instanceof ServerPlayer _serverPlayer) {
                  _serverPlayer.connection.teleport(0.0, y, 0.0, _ent.getYRot(), _ent.getXRot());
               }
            }
         }
      }
   }
}
