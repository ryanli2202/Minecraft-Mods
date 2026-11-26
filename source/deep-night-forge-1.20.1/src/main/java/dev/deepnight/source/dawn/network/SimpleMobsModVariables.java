package dev.deepnight.source.dawn.network;

import java.util.function.Supplier;
import dev.deepnight.source.dawn.SimpleMobsMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.NetworkEvent.Context;

@EventBusSubscriber(bus = Bus.MOD)
public class SimpleMobsModVariables {
   @SubscribeEvent
   public static void init(FMLCommonSetupEvent event) {
      SimpleMobsMod.addNetworkMessage(
         SimpleMobsModVariables.SavedDataSyncMessage.class,
         SimpleMobsModVariables.SavedDataSyncMessage::buffer,
         SimpleMobsModVariables.SavedDataSyncMessage::new,
         SimpleMobsModVariables.SavedDataSyncMessage::handler
      );
   }

   @EventBusSubscriber
   public static class EventBusVariableHandlers {
      @SubscribeEvent
      public static void onPlayerLoggedIn(PlayerLoggedInEvent event) {
         if (!event.getEntity().level().isClientSide()) {
            SavedData mapdata = SimpleMobsModVariables.MapVariables.get(event.getEntity().level());
            SavedData worlddata = SimpleMobsModVariables.WorldVariables.get(event.getEntity().level());
            if (mapdata != null) {
               SimpleMobsMod.PACKET_HANDLER
                  .send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)event.getEntity()), new SimpleMobsModVariables.SavedDataSyncMessage(0, mapdata));
            }

            if (worlddata != null) {
               SimpleMobsMod.PACKET_HANDLER
                  .send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)event.getEntity()), new SimpleMobsModVariables.SavedDataSyncMessage(1, worlddata));
            }
         }
      }

      @SubscribeEvent
      public static void onPlayerChangedDimension(PlayerChangedDimensionEvent event) {
         if (!event.getEntity().level().isClientSide()) {
            SavedData worlddata = SimpleMobsModVariables.WorldVariables.get(event.getEntity().level());
            if (worlddata != null) {
               SimpleMobsMod.PACKET_HANDLER
                  .send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)event.getEntity()), new SimpleMobsModVariables.SavedDataSyncMessage(1, worlddata));
            }
         }
      }
   }

   public static class MapVariables extends SavedData {
      public static final String DATA_NAME = "simple_mobs_mapvars";
      public boolean Aggro = false;
      public boolean knight_attack_1 = false;
      public boolean knight_attack_2 = false;
      public boolean samurai_attack_1 = false;
      public boolean samurai_attack_2 = false;
      public boolean Aggro_sam = false;
      public double dist_eye = 1.6;
      public boolean sam_atk_prep = false;
      public double loop = 0.0;
      public double loop_part = 8.0;
      public double samurai_head_rotation_x = 0.0;
      public double samurai_head_rotation_z = 0.0;
      public double samurai_head_rotation_y = 0.0;
      public double samradius = 1.0;
      public boolean samurai_attack_3 = false;
      public double sam_old_x = 0.0;
      public double sam_old_y = 0.0;
      public double sam_old_z = 0.0;
      public boolean sam_atk3_pt1 = false;
      public boolean sam_atk3_pt2 = false;
      public boolean samurai_attack_4 = false;
      public boolean sam_atk_4_pt2 = false;
      public boolean knight_attack_3 = false;
      public boolean knight_atk_3_pt1 = false;
      public boolean knight_atk_3_pt2 = false;
      public double i = 1.0;
      public double j = 1.0;
      public boolean collide = false;
      public double x = 1.0;
      public double circle_direction = 0.0;
      public double r = 0.0;
      public boolean r_decrease = false;
      public boolean ball_damage = false;
      public boolean ball_explosion = false;
      public boolean knight_attack_4 = false;
      public boolean knight_attack_4_done = false;
      public double loop2 = 0.0;
      public boolean aggronin = false;
      public double arrow_direction = 0.0;
      public boolean GiantAggro = false;
      public boolean aggrocave = false;
      public boolean damageblock = false;
      public boolean aggrosand = false;
      public boolean aggroogre = false;
      public boolean aggro_tails = false;
      public boolean flame = false;
      public boolean palace_spawn = false;
      public double ghost_x = 0.0;
      public double ghost_y = 0.0;
      public double ghost_z = 0.0;
      static SimpleMobsModVariables.MapVariables clientSide = new SimpleMobsModVariables.MapVariables();

      public static SimpleMobsModVariables.MapVariables load(CompoundTag tag) {
         SimpleMobsModVariables.MapVariables data = new SimpleMobsModVariables.MapVariables();
         data.read(tag);
         return data;
      }

      public void read(CompoundTag nbt) {
         this.Aggro = nbt.getBoolean("Aggro");
         this.knight_attack_1 = nbt.getBoolean("knight_attack_1");
         this.knight_attack_2 = nbt.getBoolean("knight_attack_2");
         this.samurai_attack_1 = nbt.getBoolean("samurai_attack_1");
         this.samurai_attack_2 = nbt.getBoolean("samurai_attack_2");
         this.Aggro_sam = nbt.getBoolean("Aggro_sam");
         this.dist_eye = nbt.getDouble("dist_eye");
         this.sam_atk_prep = nbt.getBoolean("sam_atk_prep");
         this.loop = nbt.getDouble("loop");
         this.loop_part = nbt.getDouble("loop_part");
         this.samurai_head_rotation_x = nbt.getDouble("samurai_head_rotation_x");
         this.samurai_head_rotation_z = nbt.getDouble("samurai_head_rotation_z");
         this.samurai_head_rotation_y = nbt.getDouble("samurai_head_rotation_y");
         this.samradius = nbt.getDouble("samradius");
         this.samurai_attack_3 = nbt.getBoolean("samurai_attack_3");
         this.sam_old_x = nbt.getDouble("sam_old_x");
         this.sam_old_y = nbt.getDouble("sam_old_y");
         this.sam_old_z = nbt.getDouble("sam_old_z");
         this.sam_atk3_pt1 = nbt.getBoolean("sam_atk3_pt1");
         this.sam_atk3_pt2 = nbt.getBoolean("sam_atk3_pt2");
         this.samurai_attack_4 = nbt.getBoolean("samurai_attack_4");
         this.sam_atk_4_pt2 = nbt.getBoolean("sam_atk_4_pt2");
         this.knight_attack_3 = nbt.getBoolean("knight_attack_3");
         this.knight_atk_3_pt1 = nbt.getBoolean("knight_atk_3_pt1");
         this.knight_atk_3_pt2 = nbt.getBoolean("knight_atk_3_pt2");
         this.i = nbt.getDouble("i");
         this.j = nbt.getDouble("j");
         this.collide = nbt.getBoolean("collide");
         this.x = nbt.getDouble("x");
         this.circle_direction = nbt.getDouble("circle_direction");
         this.r = nbt.getDouble("r");
         this.r_decrease = nbt.getBoolean("r_decrease");
         this.ball_damage = nbt.getBoolean("ball_damage");
         this.ball_explosion = nbt.getBoolean("ball_explosion");
         this.knight_attack_4 = nbt.getBoolean("knight_attack_4");
         this.knight_attack_4_done = nbt.getBoolean("knight_attack_4_done");
         this.loop2 = nbt.getDouble("loop2");
         this.aggronin = nbt.getBoolean("aggronin");
         this.arrow_direction = nbt.getDouble("arrow_direction");
         this.GiantAggro = nbt.getBoolean("GiantAggro");
         this.aggrocave = nbt.getBoolean("aggrocave");
         this.damageblock = nbt.getBoolean("damageblock");
         this.aggrosand = nbt.getBoolean("aggrosand");
         this.aggroogre = nbt.getBoolean("aggroogre");
         this.aggro_tails = nbt.getBoolean("aggro_tails");
         this.flame = nbt.getBoolean("flame");
         this.palace_spawn = nbt.getBoolean("palace_spawn");
         this.ghost_x = nbt.getDouble("ghost_x");
         this.ghost_y = nbt.getDouble("ghost_y");
         this.ghost_z = nbt.getDouble("ghost_z");
      }

      @Override
      public CompoundTag save(CompoundTag nbt) {
         nbt.putBoolean("Aggro", this.Aggro);
         nbt.putBoolean("knight_attack_1", this.knight_attack_1);
         nbt.putBoolean("knight_attack_2", this.knight_attack_2);
         nbt.putBoolean("samurai_attack_1", this.samurai_attack_1);
         nbt.putBoolean("samurai_attack_2", this.samurai_attack_2);
         nbt.putBoolean("Aggro_sam", this.Aggro_sam);
         nbt.putDouble("dist_eye", this.dist_eye);
         nbt.putBoolean("sam_atk_prep", this.sam_atk_prep);
         nbt.putDouble("loop", this.loop);
         nbt.putDouble("loop_part", this.loop_part);
         nbt.putDouble("samurai_head_rotation_x", this.samurai_head_rotation_x);
         nbt.putDouble("samurai_head_rotation_z", this.samurai_head_rotation_z);
         nbt.putDouble("samurai_head_rotation_y", this.samurai_head_rotation_y);
         nbt.putDouble("samradius", this.samradius);
         nbt.putBoolean("samurai_attack_3", this.samurai_attack_3);
         nbt.putDouble("sam_old_x", this.sam_old_x);
         nbt.putDouble("sam_old_y", this.sam_old_y);
         nbt.putDouble("sam_old_z", this.sam_old_z);
         nbt.putBoolean("sam_atk3_pt1", this.sam_atk3_pt1);
         nbt.putBoolean("sam_atk3_pt2", this.sam_atk3_pt2);
         nbt.putBoolean("samurai_attack_4", this.samurai_attack_4);
         nbt.putBoolean("sam_atk_4_pt2", this.sam_atk_4_pt2);
         nbt.putBoolean("knight_attack_3", this.knight_attack_3);
         nbt.putBoolean("knight_atk_3_pt1", this.knight_atk_3_pt1);
         nbt.putBoolean("knight_atk_3_pt2", this.knight_atk_3_pt2);
         nbt.putDouble("i", this.i);
         nbt.putDouble("j", this.j);
         nbt.putBoolean("collide", this.collide);
         nbt.putDouble("x", this.x);
         nbt.putDouble("circle_direction", this.circle_direction);
         nbt.putDouble("r", this.r);
         nbt.putBoolean("r_decrease", this.r_decrease);
         nbt.putBoolean("ball_damage", this.ball_damage);
         nbt.putBoolean("ball_explosion", this.ball_explosion);
         nbt.putBoolean("knight_attack_4", this.knight_attack_4);
         nbt.putBoolean("knight_attack_4_done", this.knight_attack_4_done);
         nbt.putDouble("loop2", this.loop2);
         nbt.putBoolean("aggronin", this.aggronin);
         nbt.putDouble("arrow_direction", this.arrow_direction);
         nbt.putBoolean("GiantAggro", this.GiantAggro);
         nbt.putBoolean("aggrocave", this.aggrocave);
         nbt.putBoolean("damageblock", this.damageblock);
         nbt.putBoolean("aggrosand", this.aggrosand);
         nbt.putBoolean("aggroogre", this.aggroogre);
         nbt.putBoolean("aggro_tails", this.aggro_tails);
         nbt.putBoolean("flame", this.flame);
         nbt.putBoolean("palace_spawn", this.palace_spawn);
         nbt.putDouble("ghost_x", this.ghost_x);
         nbt.putDouble("ghost_y", this.ghost_y);
         nbt.putDouble("ghost_z", this.ghost_z);
         return nbt;
      }

      public void syncData(LevelAccessor world) {
         this.setDirty();
         if (world instanceof Level && !world.isClientSide()) {
            SimpleMobsMod.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new SimpleMobsModVariables.SavedDataSyncMessage(0, this));
         }
      }

      public static SimpleMobsModVariables.MapVariables get(LevelAccessor world) {
         return world instanceof ServerLevelAccessor serverLevelAcc
            ? serverLevelAcc.getLevel()
               .getServer()
               .getLevel(Level.OVERWORLD)
               .getDataStorage()
               .computeIfAbsent(e -> load(e), SimpleMobsModVariables.MapVariables::new, "simple_mobs_mapvars")
            : clientSide;
      }
   }

   public static class SavedDataSyncMessage {
      public int type;
      public SavedData data;

      public SavedDataSyncMessage(FriendlyByteBuf buffer) {
         this.type = buffer.readInt();
         this.data = this.type == 0 ? new SimpleMobsModVariables.MapVariables() : new SimpleMobsModVariables.WorldVariables();
         if (this.data instanceof SimpleMobsModVariables.MapVariables _mapvars) {
            _mapvars.read(buffer.readNbt());
         } else if (this.data instanceof SimpleMobsModVariables.WorldVariables _worldvars) {
            _worldvars.read(buffer.readNbt());
         }
      }

      public SavedDataSyncMessage(int type, SavedData data) {
         this.type = type;
         this.data = data;
      }

      public static void buffer(SimpleMobsModVariables.SavedDataSyncMessage message, FriendlyByteBuf buffer) {
         buffer.writeInt(message.type);
         buffer.writeNbt(message.data.save(new CompoundTag()));
      }

      public static void handler(SimpleMobsModVariables.SavedDataSyncMessage message, Supplier<Context> contextSupplier) {
         Context context = contextSupplier.get();
         context.enqueueWork(() -> {
            if (!context.getDirection().getReceptionSide().isServer()) {
               if (message.type == 0) {
                  SimpleMobsModVariables.MapVariables.clientSide = (SimpleMobsModVariables.MapVariables)message.data;
               } else {
                  SimpleMobsModVariables.WorldVariables.clientSide = (SimpleMobsModVariables.WorldVariables)message.data;
               }
            }
         });
         context.setPacketHandled(true);
      }
   }

   public static class WorldVariables extends SavedData {
      public static final String DATA_NAME = "simple_mobs_worldvars";
      static SimpleMobsModVariables.WorldVariables clientSide = new SimpleMobsModVariables.WorldVariables();

      public static SimpleMobsModVariables.WorldVariables load(CompoundTag tag) {
         SimpleMobsModVariables.WorldVariables data = new SimpleMobsModVariables.WorldVariables();
         data.read(tag);
         return data;
      }

      public void read(CompoundTag nbt) {
      }

      @Override
      public CompoundTag save(CompoundTag nbt) {
         return nbt;
      }

      public void syncData(LevelAccessor world) {
         this.setDirty();
         if (world instanceof Level level && !level.isClientSide()) {
            SimpleMobsMod.PACKET_HANDLER.send(PacketDistributor.DIMENSION.with(level::dimension), new SimpleMobsModVariables.SavedDataSyncMessage(1, this));
         }
      }

      public static SimpleMobsModVariables.WorldVariables get(LevelAccessor world) {
         return world instanceof ServerLevel level
            ? level.getDataStorage().computeIfAbsent(e -> load(e), SimpleMobsModVariables.WorldVariables::new, "simple_mobs_worldvars")
            : clientSide;
      }
   }
}
