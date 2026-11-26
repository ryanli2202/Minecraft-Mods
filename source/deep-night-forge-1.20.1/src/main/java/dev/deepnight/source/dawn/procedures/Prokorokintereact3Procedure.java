package dev.deepnight.source.dawn.procedures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class Prokorokintereact3Procedure {
   public static void execute(LevelAccessor world, Entity entity, Entity sourceentity) {
      if (entity != null && sourceentity != null) {
         if (!entity.getPersistentData().getBoolean("awake")) {
            entity.getPersistentData().putBoolean("awake", true);
            entity.getPersistentData().putBoolean("wake", true);
            entity.getPersistentData().putBoolean("Attacking", true);
         }

         if (entity.getPersistentData().getBoolean("open")) {
            if (entity.getPersistentData().getBoolean("correct")) {
               entity.getPersistentData().putBoolean("tag", true);
               entity.getPersistentData().putBoolean("Attacking", true);
            } else {
               entity.getPersistentData().putBoolean("wrong_tag", true);
               entity.getPersistentData().putBoolean("Attacking", true);
            }
         }

         if ((world instanceof Level _lvl ? _lvl.dimension() : Level.OVERWORLD)
               == ResourceKey.<Level>create(net.minecraft.core.registries.Registries.DIMENSION, new ResourceLocation("simple_mobs:distant_land"))
            && sourceentity instanceof ServerPlayer _player
            && !_player.level().isClientSide()) {
            ResourceKey<Level> destinationType = Level.OVERWORLD;
            if (_player.level().dimension() == destinationType) {
               return;
            }

            ServerLevel nextLevel = _player.server.getLevel(destinationType);
            if (nextLevel != null) {
               _player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, 0.0F));
               _player.teleportTo(nextLevel, _player.getX(), _player.getY(), _player.getZ(), _player.getYRot(), _player.getXRot());
               _player.connection.send(new ClientboundPlayerAbilitiesPacket(_player.getAbilities()));

               for (MobEffectInstance _effectinstance : _player.getActiveEffects()) {
                  _player.connection.send(new ClientboundUpdateMobEffectPacket(_player.getId(), _effectinstance));
               }

               _player.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
            }
         }
      }
   }
}
