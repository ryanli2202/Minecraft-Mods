package dev.travellerscompanions.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.travellerscompanions.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid=TravellersCompanions.ID,value=Dist.CLIENT)
public final class ClientEvents {
    public static final KeyMapping ROSTER=new KeyMapping("key.travellerscompanions.roster",KeyConflictContext.IN_GAME,InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_TAB,"key.categories.travellerscompanions");
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent e){if(e.phase!=TickEvent.Phase.END)return;var mc=Minecraft.getInstance();while(ROSTER.consumeClick())if(mc.player!=null&&mc.screen==null)CompanionNetwork.CHANNEL.sendToServer(new CompanionNetwork.Action(0,0,-1));}
    public static void receive(CompanionNetwork.State state){var mc=Minecraft.getInstance();if(mc.level==null||state.tag()==null)return;if(state.wheel())mc.setScreen(new InteractionWheel(state.tag(),state.entity()));else if(mc.screen instanceof TroopRosterScreen s)s.update(state.tag());else if(mc.screen instanceof RosterScreen s)s.update(state.tag());else mc.setScreen(new RosterScreen(state.tag()));}
    public static void animate(CompanionNetwork.Animation a){var mc=Minecraft.getInstance();if(mc.level!=null&&mc.level.getEntity(a.entity()) instanceof CompanionEntity e){e.animation=a.name();e.animationDuration=a.ticks();e.animationStarted=e.tickCount;}}
    @Mod.EventBusSubscriber(modid=TravellersCompanions.ID,value=Dist.CLIENT,bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {
        @SubscribeEvent public static void keys(RegisterKeyMappingsEvent e){e.register(ROSTER);}
        @SubscribeEvent public static void renderers(EntityRenderersEvent.RegisterRenderers e){e.registerEntityRenderer(TravellersCompanions.COMPANION.get(),CompanionRenderer::new);}
        @SubscribeEvent public static void setup(net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent e){e.enqueueWork(()->{MenuScreens.register(TravellersCompanions.EQUIPMENT.get(),EquipmentScreen::new);MenuScreens.register(TravellersCompanions.SPELLS.get(),SpellScreen::new);});}
    }
}
