package dev.nordui;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.SimpleChannel;

public final class VaultNetwork {
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(NordUI.ID,"vault"), () -> "2", "2"::equals, "2"::equals);
    public record Open() {}
    public record OpenSpells() {}
    public static void register() {
        CHANNEL.messageBuilder(Open.class, 0, NetworkDirection.PLAY_TO_SERVER)
            .encoder((m,b) -> {}).decoder(b -> new Open()).consumerMainThread((m,c) -> {
                var p = c.get().getSender(); if (p != null) VaultService.open(p, 0);
                c.get().setPacketHandled(true);
            }).add();
        CHANNEL.messageBuilder(OpenSpells.class, 1, NetworkDirection.PLAY_TO_SERVER)
            .encoder((m,b)->{}).decoder(b->new OpenSpells()).consumerMainThread((m,c)->{
                var p=c.get().getSender();if(p!=null)dev.nordui.compat.IronsCompat.open(p);
                c.get().setPacketHandled(true);
            }).add();
    }
    public static void request() { CHANNEL.sendToServer(new Open()); }
    public static void spells() { CHANNEL.sendToServer(new OpenSpells()); }
}
