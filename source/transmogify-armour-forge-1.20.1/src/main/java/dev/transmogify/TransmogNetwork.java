package dev.transmogify;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.SimpleChannel;
import java.util.UUID;

public final class TransmogNetwork {
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(TransmogifyArmour.ID, "wardrobe"), () -> "1", "1"::equals, "1"::equals);
    public record Request() {}
    public record Open(UUID token, ItemStack[] equipped) {}
    public record Apply(UUID token, String[] choices) {}
    public record Result(String error) {}

    public static void register() {
        CHANNEL.messageBuilder(Request.class, 0, NetworkDirection.PLAY_TO_SERVER)
            .encoder((m,b) -> {}).decoder(b -> new Request()).consumerMainThread((m,c) -> {
                var p=c.get().getSender(); if(p!=null && p.isAlive() && !p.isSpectator()) WardrobeService.open(p);
                c.get().setPacketHandled(true);
            }).add();
        CHANNEL.messageBuilder(Open.class, 1, NetworkDirection.PLAY_TO_CLIENT)
            .encoder((m,b) -> { b.writeUUID(m.token); for(var s:m.equipped) b.writeItem(s); })
            .decoder(b -> new Open(b.readUUID(), new ItemStack[]{b.readItem(),b.readItem(),b.readItem(),b.readItem()}))
            .consumerMainThread((m,c) -> {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> dev.transmogify.client.ClientWardrobe.open(m));
                c.get().setPacketHandled(true);
            }).add();
        CHANNEL.messageBuilder(Apply.class, 2, NetworkDirection.PLAY_TO_SERVER)
            .encoder((m,b) -> { b.writeUUID(m.token); for(String id:m.choices) b.writeUtf(id,256); })
            .decoder(b -> new Apply(b.readUUID(),new String[]{b.readUtf(256),b.readUtf(256),b.readUtf(256),b.readUtf(256)}))
            .consumerMainThread((m,c) -> {
                var p=c.get().getSender(); if(p!=null) send(p,new Result(WardrobeService.apply(p,m.token,m.choices)));
                c.get().setPacketHandled(true);
            }).add();
        CHANNEL.messageBuilder(Result.class, 3, NetworkDirection.PLAY_TO_CLIENT)
            .encoder((m,b) -> b.writeUtf(m.error,512)).decoder(b -> new Result(b.readUtf(512)))
            .consumerMainThread((m,c) -> {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> dev.transmogify.client.ClientWardrobe.result(m.error));
                c.get().setPacketHandled(true);
            }).add();
    }
    public static void send(ServerPlayer player,Object message) { CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),message); }
    public static void request() { CHANNEL.sendToServer(new Request()); }
}
