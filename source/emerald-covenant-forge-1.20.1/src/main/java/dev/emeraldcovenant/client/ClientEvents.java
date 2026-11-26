package dev.emeraldcovenant.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.emeraldcovenant.*;
import net.minecraft.client.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid=Covenant.ID,value=Dist.CLIENT)
public final class ClientEvents {
    public static final KeyMapping OPEN=new KeyMapping("key.emeraldcovenant.open",KeyConflictContext.IN_GAME,InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_H,"key.categories.emeraldcovenant");
    @Mod.EventBusSubscriber(modid=Covenant.ID,value=Dist.CLIENT,bus=Mod.EventBusSubscriber.Bus.MOD)
    public static final class Registration {
        @SubscribeEvent public static void keys(RegisterKeyMappingsEvent event){event.register(OPEN);}
        @SubscribeEvent public static void colors(RegisterColorHandlersEvent.Item event){event.register((stack,tint)->{
            if(tint!=0||!(stack.getItem() instanceof BearingItem b))return -1;
            return switch(b.bit){case 1->0xFFD090;case 2->0xB5D6FF;case 4->0xDAB0FF;default->0x91FFBC;};
        },Covenant.BEARINGS.stream().map(v->v.get()).toArray(net.minecraft.world.item.Item[]::new));}
    }
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event) {
        if(event.phase!=TickEvent.Phase.END)return;var mc=Minecraft.getInstance();
        while(OPEN.consumeClick())if(mc.player!=null&&mc.screen==null&&!mc.player.isSpectator())ShopNetwork.toServer(new ShopNetwork.Open());
    }
    @SubscribeEvent public static void logout(ClientPlayerNetworkEvent.LoggingOut event){ClientShop.clear();}
}
