package dev.deepnightguide.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.deepnightguide.DeepNightGuide;
import net.minecraft.client.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid=DeepNightGuide.ID,value=Dist.CLIENT)
public final class ClientEvents {
    public static final KeyMapping OPEN=new KeyMapping("key.deepnightguide.open",KeyConflictContext.IN_GAME,InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_J,"key.categories.deepnightguide");
    @Mod.EventBusSubscriber(modid=DeepNightGuide.ID,value=Dist.CLIENT,bus=Mod.EventBusSubscriber.Bus.MOD)
    public static final class Registration {
        @SubscribeEvent public static void keys(RegisterKeyMappingsEvent event) {event.register(OPEN);}
    }
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event) {
        if(event.phase!=TickEvent.Phase.END)return;
        Minecraft mc=Minecraft.getInstance();
        if(mc.level==null||mc.player==null) {
            if(GuideIndex.current!=null){GuideIndex.current.progress.save();GuideIndex.current=null;}return;
        }
        while(OPEN.consumeClick())if(mc.screen==null) {
            if(GuideIndex.current==null)GuideIndex.current=new GuideIndex();
            else GuideIndex.current.requestStats();
            mc.setScreen(new GuideScreen(GuideIndex.current));
        }
        if(GuideIndex.current!=null)GuideIndex.current.tick();
    }
    @SubscribeEvent public static void recipes(RecipesUpdatedEvent event) {if(GuideIndex.current!=null)GuideIndex.current.rebuildRecipes();}
    @SubscribeEvent public static void logout(ClientPlayerNetworkEvent.LoggingOut event) {
        if(GuideIndex.current!=null)GuideIndex.current.progress.save();GuideIndex.current=null;
    }
}
