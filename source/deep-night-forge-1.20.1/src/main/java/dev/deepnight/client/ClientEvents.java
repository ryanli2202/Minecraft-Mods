package dev.deepnight.client;
import dev.deepnight.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
@Mod.EventBusSubscriber(modid=DeepNight.ID,bus=Mod.EventBusSubscriber.Bus.MOD,value=Dist.CLIENT)
public final class ClientEvents {
    @SubscribeEvent public static void setup(net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent e){
        e.enqueueWork(MolangVariables::prepare);
    }
    @SubscribeEvent public static void renderers(EntityRenderersEvent.RegisterRenderers e){
        DeepNight.HOSTILES.values().forEach(t->e.registerEntityRenderer(t.get(),NightRenderer::new));
        DeepNight.PETS.values().forEach(t->e.registerEntityRenderer(t.get(),NightRenderer::new));
        dev.deepnight.source.dawn.client.SourceRenderers.renderers(e);
    }
}
