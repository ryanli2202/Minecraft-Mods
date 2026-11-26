package dev.nordui.compat.irons;

import dev.nordui.client.*;
import dev.nordui.compat.UiCompat;
import io.redspace.ironsspellbooks.gui.overlays.ManaBarOverlay;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.util.Mth;

public final class IronsClient {
    public static boolean dockSpells(net.minecraftforge.client.event.RenderGuiOverlayEvent.Pre event){
        if(!ClientConfig.IRONS_DOCK.get()||io.redspace.ironsspellbooks.config.ClientConfigs.SPELL_BAR_ANCHOR.get()!=io.redspace.ironsspellbooks.gui.overlays.SpellBarOverlay.Anchor.Hotbar)return false;
        var mc=Minecraft.getInstance();int w=mc.getWindow().getGuiScaledWidth(),h=mc.getWindow().getGuiScaledHeight();
        var g=event.getGuiGraphics();g.pose().pushPose();
        try{
            int nativeX=w/2-Math.max(110,w/4),nativeY=h-Math.max(55,h/8);
            g.pose().translate(Math.min(80,w/5)-nativeX,h/2-nativeY,0);
            // Delegate the original renderer, retaining its spell selection, fade and cooldown data.
            io.redspace.ironsspellbooks.gui.overlays.SpellBarOverlay.instance.render((net.minecraftforge.client.gui.overlay.ForgeGui)mc.gui,g,event.getPartialTick(),w,h);
        }finally{g.pose().popPose();}
        return true;
    }
    public static void register(){MenuScreens.register(SpellMenu.TYPE.get(),SpellScreen::new);}
    public static boolean themedMana(){return ClientConfig.ownsHud()&&ClientConfig.IRONS_MANA.get();}
    public static void mana(GuiGraphics g,int w,int h){
        var p=Minecraft.getInstance().player;
        if(p==null||!themedMana()||!ManaBarOverlay.shouldShowManaBar(p))return;
        int width=Math.min(142,(w-48)/3),y=Math.max(45,h-53-UiCompat.hudLift());
        int mana=ClientMagicData.getPlayerMana(),max=(int)p.getAttributeValue(AttributeRegistry.MAX_MANA.get());
        Theme.ornateBar(g,8,y,width,(float)mana/Math.max(1,max),Theme.XP);
        Theme.text(g,"MANA "+Mth.ceil(mana)+" / "+max,19,y-8,Theme.WHITE);
    }
}
