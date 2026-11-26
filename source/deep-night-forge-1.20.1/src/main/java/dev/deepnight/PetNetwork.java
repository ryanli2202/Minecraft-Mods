package dev.deepnight;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.SimpleChannel;
import java.util.*;
import java.util.function.Supplier;

public final class PetNetwork {
    public record Entry(String id,String name){}
    public record Menu(List<Entry> entries){}
    public record Summon(String id){}
    public static final SimpleChannel CHANNEL=NetworkRegistry.newSimpleChannel(DeepNight.id("pets"),()->"1","1"::equals,"1"::equals);
    public static void register(){
        CHANNEL.registerMessage(0,Menu.class,(m,b)->{b.writeVarInt(m.entries.size());for(Entry e:m.entries){b.writeUtf(e.id,128);b.writeUtf(e.name,256);}},b->{int n=b.readVarInt();if(n<0||n>2048)throw new IllegalArgumentException("Pet catalogue too large");List<Entry> entries=new ArrayList<>();for(int i=0;i<n;i++)entries.add(new Entry(b.readUtf(128),b.readUtf(256)));return new Menu(entries);},PetNetwork::menu,Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(1,Summon.class,(m,b)->b.writeUtf(m.id,128),b->new Summon(b.readUtf(128)),(m,c)->{var context=c.get();context.enqueueWork(()->{ServerPlayer p=context.getSender();var id=ResourceLocation.tryParse(m.id);if(p!=null&&id!=null)PetCatalogue.summon(p,id);});context.setPacketHandled(true);},Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }
    private static void menu(Menu m,Supplier<NetworkEvent.Context> c){c.get().enqueueWork(()->DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()->dev.deepnight.client.PetScreen.open(m)));c.get().setPacketHandled(true);}
    public static void open(ServerPlayer p){
        if(!PetCatalogue.hasTool(p)||p.isSpectator())return;
        var entries=PetCatalogue.all(p.serverLevel()).entrySet().stream().map(e->new Entry(e.getKey().toString(),e.getValue().getDescription().getString())).sorted(Comparator.comparing(Entry::name)).toList();
        CHANNEL.send(PacketDistributor.PLAYER.with(()->p),new Menu(entries));
    }
}
