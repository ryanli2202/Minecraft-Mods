package dev.ashenquests.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.ashenquests.*;
import net.minecraft.client.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid=Chronicles.ID,value=Dist.CLIENT)
public final class ClientEvents {
    public static final KeyMapping OPEN=new KeyMapping("key.ashenquests.open",KeyConflictContext.IN_GAME,InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_MINUS,"key.categories.ashenquests");
    @Mod.EventBusSubscriber(modid=Chronicles.ID,value=Dist.CLIENT,bus=Mod.EventBusSubscriber.Bus.MOD)
    public static final class Registration {
        @SubscribeEvent public static void keys(RegisterKeyMappingsEvent e){e.register(OPEN);}
    }
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent e) {
        if(e.phase!=TickEvent.Phase.END)return;var mc=Minecraft.getInstance();
        while(OPEN.consumeClick())if(mc.player!=null&&mc.screen==null)ClientJournal.open();
    }
    @SubscribeEvent public static void logout(ClientPlayerNetworkEvent.LoggingOut e){ClientJournal.clear();}
    public static int firstHudObjective(Quest q,QuestEngine.View v) {
        if(v.status()==QuestEngine.ACTIVE)for(int i=0;i<q.objectives().size();i++)if(v.progress()[i]<q.objectives().get(i).count())return i;
        return 0;
    }
    @SubscribeEvent public static void hud(RenderGuiEvent.Post e) {
        var mc=Minecraft.getInstance();var h=ClientJournal.hud;
        if(mc.screen!=null||mc.options.hideGui||h==null||h.quest()==null||h.view()==null)return;
        var g=e.getGuiGraphics();int x=mc.getWindow().getGuiScaledWidth()-222,y=14;var q=h.quest();var v=h.view();
        int first=firstHudObjective(q,v),rows=Math.min(3,q.objectives().size()-first);
        g.fill(x-6,y-6,x+214,y+31+rows*12,0xCE111B1B);
        g.fill(x-6,y-6,x-4,y+31+rows*12,0xFFE4BE75);
        g.drawString(mc.font,mc.font.plainSubstrByWidth(q.title(),206),x,y,0xF1D497,false);
        g.drawString(mc.font,(v.status()==QuestEngine.READY?"Return to "+q.giverName():v.status()==QuestEngine.OFFERED?"Speak to "+q.giverName():JournalScreen.status(v))+"  |  "+OPEN.getTranslatedKeyMessage().getString()+" Journal",x,y+13,0x8ACDB0,false);
        for(int row=0;row<rows;row++){
            int i=first+row;var o=q.objectives().get(i);
            String label=o.type().equals("talk")?"Discuss with "+dev.thedeepnightnpcs.NpcRoster.name(o.target().substring(o.target().indexOf(':')+1)):o.label();
            String line=v.status()==QuestEngine.CLAIMED?"Complete":v.progress()[i]+"/"+o.count()+" "+label;
            g.drawString(mc.font,mc.font.plainSubstrByWidth(line,206),x,y+29+row*12,0xE0E4DB,false);
        }
    }
}
