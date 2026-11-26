package dev.transmogify.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.transmogify.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.*;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid=TransmogifyArmour.ID,value=Dist.CLIENT)
public final class ClientEvents {
    public static final KeyMapping OPEN = new KeyMapping("key.transmogifyarmour.open",KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_EQUAL,"key.categories.transmogifyarmour");
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent e) {
        if(e.phase!=TickEvent.Phase.END) return;
        var mc=Minecraft.getInstance();
        while(OPEN.consumeClick()) if(mc.player!=null && mc.screen==null) TransmogNetwork.request();
    }
    @SubscribeEvent public static void logout(ClientPlayerNetworkEvent.LoggingOut e) { ClientWardrobe.endPreview(); }
    @SubscribeEvent public static void tooltip(ItemTooltipEvent e) {
        if(e.getItemStack().getItem() instanceof net.minecraft.world.item.ArmorItem armour) {
            String id=ArmourData.appearanceId(e.getItemStack());
            if(!id.isEmpty()) {
                var item=ArmourData.target(id,armour.getEquipmentSlot());
                e.getToolTip().add(Component.literal("Appearance: ").append(item==null?Component.literal(id+" (unavailable)"):item.getDescription()).withStyle(ChatFormatting.LIGHT_PURPLE));
            }
        }
    }
    @Mod.EventBusSubscriber(modid=TransmogifyArmour.ID,value=Dist.CLIENT,bus=Mod.EventBusSubscriber.Bus.MOD)
    public static final class Registration {
        @SubscribeEvent public static void keys(RegisterKeyMappingsEvent e) { e.register(OPEN); }
    }
}
