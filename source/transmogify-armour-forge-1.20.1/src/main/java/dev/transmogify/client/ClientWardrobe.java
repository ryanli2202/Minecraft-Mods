package dev.transmogify.client;

import dev.transmogify.*;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import java.util.UUID;

public final class ClientWardrobe {
    private static UUID previewPlayer;
    private static ItemStack[] preview;
    public static int renderSubstitutions;

    public static void open(TransmogNetwork.Open message) { Minecraft.getInstance().setScreen(new WardrobeScreen(message)); }
    public static void result(String error) {
        var mc = Minecraft.getInstance();
        if (mc.screen instanceof WardrobeScreen screen) {
            if (error.isEmpty()) screen.onClose(); else screen.fail(error);
        }
        if (mc.player != null) mc.player.displayClientMessage(Component.literal(error.isEmpty() ? "Armour appearance saved." : error), true);
    }
    public static void beginPreview(UUID player, ItemStack[] equipment) { previewPlayer=player; preview=equipment; }
    public static void endPreview() { preview=null; previewPlayer=null; }
    public static ItemStack rendered(ItemStack real, LivingEntity entity, EquipmentSlot slot) {
        ItemStack input=real;
        if (preview!=null && entity.getUUID().equals(previewPlayer)) {
            for(int i=0;i<4;i++) if(ArmourData.SLOTS[i]==slot) { input=preview[i]; break; }
        }
        ItemStack result=ArmourData.visual(input,slot);
        if(result!=input) renderSubstitutions++;
        return result;
    }
}
