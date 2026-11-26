package dev.ashenquests;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.SimpleChannel;

public final class QuestNetwork {
    public static final SimpleChannel CHANNEL=NetworkRegistry.newSimpleChannel(new ResourceLocation(Chronicles.ID,"journal"),()->"3","3"::equals,"3"::equals);
    public record Open() {}
    public record Action(long token,int sequence,int kind,String id,String answer,int choice) {}
    public record Payload(long token,int kind,String json) {}
    public static void register() {
        CHANNEL.messageBuilder(Open.class,0,NetworkDirection.PLAY_TO_SERVER).encoder((m,b)->{}).decoder(b->new Open()).consumerMainThread((m,c)->{var p=c.get().getSender();if(p!=null)QuestServer.open(p);c.get().setPacketHandled(true);}).add();
        CHANNEL.messageBuilder(Action.class,1,NetworkDirection.PLAY_TO_SERVER).encoder((m,b)->{b.writeLong(m.token);b.writeVarInt(m.sequence);b.writeVarInt(m.kind);b.writeUtf(m.id,100);b.writeUtf(m.answer,256);b.writeVarInt(m.choice);})
            .decoder(b->new Action(b.readLong(),b.readVarInt(),b.readVarInt(),b.readUtf(100),b.readUtf(256),b.readVarInt()))
            .consumerMainThread((m,c)->{var p=c.get().getSender();if(p!=null)QuestServer.action(p,m);c.get().setPacketHandled(true);}).add();
        CHANNEL.messageBuilder(Payload.class,2,NetworkDirection.PLAY_TO_CLIENT).encoder((m,b)->{b.writeLong(m.token);b.writeVarInt(m.kind);b.writeUtf(m.json,32000);})
            .decoder(b->new Payload(b.readLong(),b.readVarInt(),b.readUtf(32000)))
            .consumerMainThread((m,c)->{DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()->dev.ashenquests.client.ClientJournal.receive(m));c.get().setPacketHandled(true);}).add();
    }
    public static void send(ServerPlayer p,long token,int kind,Object object) { CHANNEL.send(PacketDistributor.PLAYER.with(()->p),new Payload(token,kind,Campaign.GSON.toJson(object))); }
    public static void toServer(Object message) { CHANNEL.sendToServer(message); }
}
