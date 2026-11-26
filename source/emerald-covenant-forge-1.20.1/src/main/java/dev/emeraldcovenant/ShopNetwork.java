package dev.emeraldcovenant;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.SimpleChannel;
import java.util.*;
import java.util.function.*;

public final class ShopNetwork {
    public static final SimpleChannel CHANNEL=NetworkRegistry.newSimpleChannel(new ResourceLocation(Covenant.ID,"shop"),()->"1","1"::equals,"1"::equals);
    public record Open() {}
    public record Begin(long token,int count,int mask,int emeralds,String warning) {}
    public record Chunk(long token,List<Offer> offers,boolean last) {}
    public record Buy(long token,int sequence,String key,int quantity) {}
    public record Action(long token,int action) {}
    public record Status(long token,int mask,int emeralds,boolean success,String message) {}
    public static void register() {
        CHANNEL.messageBuilder(Open.class,0,NetworkDirection.PLAY_TO_SERVER).encoder((m,b)->{}).decoder(b->new Open()).consumerMainThread((m,c)->{var p=c.get().getSender();if(p!=null)ShopServer.open(p);c.get().setPacketHandled(true);}).add();
        CHANNEL.messageBuilder(Begin.class,1,NetworkDirection.PLAY_TO_CLIENT).encoder((m,b)->{b.writeLong(m.token);b.writeVarInt(m.count);b.writeVarInt(m.mask);b.writeVarInt(m.emeralds);b.writeUtf(m.warning,2048);})
            .decoder(b->new Begin(b.readLong(),b.readVarInt(),b.readVarInt(),b.readVarInt(),b.readUtf(2048))).consumerMainThread((m,c)->client(c,()->()->dev.emeraldcovenant.client.ClientShop.begin(m))).add();
        CHANNEL.messageBuilder(Chunk.class,2,NetworkDirection.PLAY_TO_CLIENT).encoder((m,b)->{b.writeLong(m.token);b.writeVarInt(m.offers.size());m.offers.forEach(e->e.write(b));b.writeBoolean(m.last);})
            .decoder(b->{long token=b.readLong();int n=b.readVarInt();if(n<0||n>32)throw new IllegalArgumentException("Invalid chunk");var list=new ArrayList<Offer>();for(int i=0;i<n;i++)list.add(Offer.read(b));return new Chunk(token,list,b.readBoolean());})
            .consumerMainThread((m,c)->client(c,()->()->dev.emeraldcovenant.client.ClientShop.chunk(m))).add();
        CHANNEL.messageBuilder(Buy.class,3,NetworkDirection.PLAY_TO_SERVER).encoder((m,b)->{b.writeLong(m.token);b.writeVarInt(m.sequence);b.writeUtf(m.key,512);b.writeVarInt(m.quantity);})
            .decoder(b->new Buy(b.readLong(),b.readVarInt(),b.readUtf(512),b.readVarInt())).consumerMainThread((m,c)->{var p=c.get().getSender();if(p!=null)ShopServer.buy(p,m.token,m.sequence,m.key,m.quantity);c.get().setPacketHandled(true);}).add();
        CHANNEL.messageBuilder(Action.class,4,NetworkDirection.PLAY_TO_SERVER).encoder((m,b)->{b.writeLong(m.token);b.writeVarInt(m.action);}).decoder(b->new Action(b.readLong(),b.readVarInt()))
            .consumerMainThread((m,c)->{var p=c.get().getSender();if(p!=null)ShopServer.action(p,m.token,m.action);c.get().setPacketHandled(true);}).add();
        CHANNEL.messageBuilder(Status.class,5,NetworkDirection.PLAY_TO_CLIENT).encoder((m,b)->{b.writeLong(m.token);b.writeVarInt(m.mask);b.writeVarInt(m.emeralds);b.writeBoolean(m.success);b.writeUtf(m.message,2048);})
            .decoder(b->new Status(b.readLong(),b.readVarInt(),b.readVarInt(),b.readBoolean(),b.readUtf(2048))).consumerMainThread((m,c)->client(c,()->()->dev.emeraldcovenant.client.ClientShop.status(m))).add();
    }
    private static void client(Supplier<NetworkEvent.Context> context,Supplier<Runnable> work){DistExecutor.unsafeRunWhenOn(Dist.CLIENT,work);context.get().setPacketHandled(true);}
    public static void send(ServerPlayer player,Object message){CHANNEL.send(PacketDistributor.PLAYER.with(()->player),message);}
    public static void toServer(Object message){CHANNEL.sendToServer(message);}
}
