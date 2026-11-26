package dev.nordui;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraftforge.network.NetworkHooks;

public final class VaultService {
    public static void open(ServerPlayer player, int page) {
        if (!player.isAlive() || player.isSpectator() || page < 0 || page >= VaultMenu.PAGES) return;
        if (player.containerMenu != player.inventoryMenu && !(player.containerMenu instanceof VaultMenu)) return;
        // Do not switch a container underneath an item on the cursor.
        if (!player.containerMenu.getCarried().isEmpty()) return;
        var data = VaultData.get(player.server);
        NetworkHooks.openScreen(player, new SimpleMenuProvider((id, inv, p) ->
            new VaultMenu(id, inv, data.inventory(player.getUUID()), data, page),
            Component.translatable("container.nordui.vault")), buf -> buf.writeVarInt(page));
    }
}
