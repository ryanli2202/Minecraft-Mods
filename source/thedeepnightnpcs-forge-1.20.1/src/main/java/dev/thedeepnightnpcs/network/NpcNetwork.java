package dev.thedeepnightnpcs.network;

import dev.thedeepnightnpcs.DeepNightNpcs;
import dev.thedeepnightnpcs.entity.NpcEntity;
import dev.thedeepnightnpcs.quest.HuntService;
import dev.thedeepnightnpcs.trade.JorgeService;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.*;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = DeepNightNpcs.ID)
public final class NpcNetwork {
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(DeepNightNpcs.ID, "dialogue"), () -> "4", "4"::equals, "4"::equals);
    private record Session(UUID npc, long expires, long lastAction) {}
    private static final Map<UUID, Session> sessions = new HashMap<>();
    public record View(int entityId, String kind, CompoundTag state, String message) {
        void encode(FriendlyByteBuf b) { b.writeVarInt(entityId); b.writeUtf(kind, 32); b.writeNbt(state); b.writeUtf(message, 4096); }
        static View decode(FriendlyByteBuf b) { return new View(b.readVarInt(), b.readUtf(32), b.readNbt(), b.readUtf(4096)); }
        static void handle(View packet, Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> dev.thedeepnightnpcs.client.ClientEvents.open(packet)));
            context.get().setPacketHandled(true);
        }
    }
    public record Action(int entityId, String action, String value) {
        void encode(FriendlyByteBuf b) { b.writeVarInt(entityId); b.writeUtf(action, 24); b.writeUtf(value, 128); }
        static Action decode(FriendlyByteBuf b) { return new Action(b.readVarInt(), b.readUtf(24), b.readUtf(128)); }
        static void handle(Action packet, Supplier<NetworkEvent.Context> context) {
            ServerPlayer player = context.get().getSender();
            context.get().enqueueWork(() -> { if (player != null) act(player, packet); }); context.get().setPacketHandled(true);
        }
    }
    public static void register() {
        CHANNEL.registerMessage(0, View.class, View::encode, View::decode, View::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(1, Action.class, Action::encode, Action::decode, Action::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }
    public static void open(ServerPlayer player, NpcEntity npc) {
        if (!player.isAlive() || player.isSpectator() || player.containerMenu != player.inventoryMenu || player.distanceToSqr(npc) > 64 || !player.hasLineOfSight(npc)) return;
        long now = player.server.overworld().getGameTime();
        sessions.put(player.getUUID(), new Session(npc.getUUID(), now+6000, now-5)); send(player, npc, "");
    }
    private static void send(ServerPlayer player, NpcEntity npc, String message) {
        CompoundTag state=switch(npc.kind()) {case "caelen" -> HuntService.snapshot(player);case "jorge" -> JorgeService.snapshot(player);default -> new CompoundTag();};
        state.putBoolean("covenantQuests", net.minecraftforge.fml.ModList.get().isLoaded("ashenquests") && !npc.isGuard());
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new dev.thedeepnightnpcs.quest.QuestDialogueEvent(player,npc,state,""));
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new View(npc.getId(), npc.kind(), state, message));
    }
    public static boolean authorized(ServerPlayer player, NpcEntity npc) {
        Session session = sessions.get(player.getUUID()); long now = player.server.overworld().getGameTime();
        return session != null && session.npc.equals(npc.getUUID()) && session.expires >= now && npc.isAlive() && player.isAlive()
            && !player.isSpectator() && npc.level() == player.level() && player.distanceToSqr(npc) <= 64 && player.hasLineOfSight(npc);
    }
    public static void act(ServerPlayer player, Action packet) {
        if (!(player.level().getEntity(packet.entityId) instanceof NpcEntity npc) || !authorized(player, npc)) return;
        if (packet.action.equals("close")) { sessions.remove(player.getUUID()); return; }
        Session session = sessions.get(player.getUUID()); long now = player.server.overworld().getGameTime();
        if (now-session.lastAction < 5 || player.containerMenu != player.inventoryMenu) return;
        sessions.put(player.getUUID(), new Session(npc.getUUID(), now+6000, now));
        if(packet.action.equals("covenant_talk")) {
            var event=new dev.thedeepnightnpcs.quest.QuestDialogueEvent(player,npc,new CompoundTag(),packet.value);
            if(!packet.value.isBlank())net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
            send(player,npc,event.reply.isBlank()?"That conversation is no longer available. Check your journal for the next step.":event.reply);
            return;
        }
        if (packet.action.equals("covenant_quests")) {
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new dev.thedeepnightnpcs.quest.QuestConversationEvent(player, npc));
            return;
        }
        if (npc.kind().equals("jorge")) {
            String message=switch(packet.action) {
                case "jorge_ingots" -> JorgeService.trade(player,npc,false);
                case "jorge_books" -> JorgeService.trade(player,npc,true);
                case "jorge_enchant" -> JorgeService.enchant(player,npc);
                case "refresh_ingots" -> JorgeService.refresh(player,false);
                case "refresh_books" -> JorgeService.refresh(player,true);
                default -> "Tell me which service you need.";
            };
            if(message!=null)send(player,npc,message);
            else sessions.remove(player.getUUID());
            return;
        }
        if(!npc.kind().equals("caelen"))return;
        String message = switch (packet.action) {
            case "refresh" -> HuntService.refresh(player, false);
            case "accept" -> HuntService.accept(player, packet.value);
            case "abandon" -> HuntService.abandon(player);
            case "claim" -> HuntService.claim(player);
            case "status" -> "The ledger is current. Take your time; a contract has no deadline.";
            default -> null;
        };
        if (message != null) send(player, npc, message);
    }
    @SubscribeEvent public static void logout(PlayerEvent.PlayerLoggedOutEvent event) { sessions.remove(event.getEntity().getUUID()); }
    @SubscribeEvent public static void stop(ServerStoppedEvent event) { sessions.clear(); }
}
