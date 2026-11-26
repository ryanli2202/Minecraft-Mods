package dev.nordui.client;

import dev.nordui.NordUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public final class Theme {
    public static final int WHITE=0xE5E5DE, MUTED=0x949A9A, GOLD=0xD7C395, EDGE=0xFF687172;
    public static final ResourceLocation SLATE=new ResourceLocation(NordUI.ID,"textures/gui/slate.png");
    public static final ResourceLocation ORNAMENT=new ResourceLocation(NordUI.ID,"textures/gui/ornament.png");
    public static final ResourceLocation SLOT=new ResourceLocation(NordUI.ID,"textures/gui/slot.png");
    public static final ResourceLocation MARKER=new ResourceLocation(NordUI.ID,"textures/gui/marker.png");
    public static final ResourceLocation FONT=new ResourceLocation(NordUI.ID,"ui");
    public static final ResourceLocation BACKGROUND=new ResourceLocation(NordUI.ID,"textures/gui/main_menu.png");
    public static final ResourceLocation TITLE=new ResourceLocation(NordUI.ID,"textures/gui/the_deep_night.png");
    public static final ResourceLocation BAR_FRAME=new ResourceLocation(NordUI.ID,"textures/gui/bar_frame.png");
    public static final ResourceLocation HEALTH=new ResourceLocation(NordUI.ID,"textures/gui/health_fill.png");
    public static final ResourceLocation FOOD=new ResourceLocation(NordUI.ID,"textures/gui/stamina_fill.png");
    public static final ResourceLocation XP=new ResourceLocation(NordUI.ID,"textures/gui/experience_fill.png");
    public static final ResourceLocation QUICK_SLOT=new ResourceLocation(NordUI.ID,"textures/gui/quick_slot.png");
    public static final ResourceLocation QUICK_SELECTED=new ResourceLocation(NordUI.ID,"textures/gui/quick_selected.png");
    public static Component component(String text){return Component.literal(text).withStyle(s->s.withFont(FONT));}
    public static void panel(GuiGraphics g,int x,int y,int w,int h) {
        g.fill(x-1,y-1,x+w+1,y+h+1,0xB0101518);
        g.blit(SLATE,x,y,0,0,w,h,w,h);
        g.fill(x,y,x+w,y+h,0x35101418); frame(g,x,y,w,h,EDGE);
        g.hLine(x+12,x+w-13,y+2,0xFF989B91);
    }
    public static void frame(GuiGraphics g,int x,int y,int w,int h,int color) {
        g.hLine(x,x+w-1,y,color);g.hLine(x,x+w-1,y+h-1,color);
        g.vLine(x,y,y+h-1,color);g.vLine(x+w-1,y,y+h-1,color);
    }
    public static void well(GuiGraphics g,int x,int y,int w,int h) {
        g.fill(x,y,x+w,y+h,0x9C080C0F);frame(g,x,y,w,h,0x804F585A);
    }
    public static void slot(GuiGraphics g,int x,int y) { g.blit(SLOT,x-1,y-1,0,0,18,18,18,18); }
    public static void text(GuiGraphics g,String value,int x,int y,int color) { g.drawString(Minecraft.getInstance().font,component(value),x,y,color,false); }
    public static void centered(GuiGraphics g,String value,int x,int y,int color) { g.drawCenteredString(Minecraft.getInstance().font,component(value),x,y,color); }
    public static void rule(GuiGraphics g,int x,int y,int w) {
        g.hLine(x,x+w,y,0xFF626B6A);
        g.blit(ORNAMENT,x+w/2-16,y-4,0,0,32,9,32,9);
    }
    public static void button(AbstractButton b,GuiGraphics g) {
        int x=b.getX(), y=b.getY(), w=b.getWidth(), h=b.getHeight();
        boolean focus=b.isHoveredOrFocused();
        if(Minecraft.getInstance().screen instanceof NordTitleScreen){
            if(focus&&b.active){g.fill(x,y,x+w,y+h,0x4025333D);text(g,">",x+3,y+(h-9)/2,GOLD);}
            g.drawString(Minecraft.getInstance().font,b.getMessage().copy().withStyle(s->s.withFont(FONT)),x+16,y+(h-9)/2,b.active?(focus?GOLD:WHITE):MUTED,false);return;
        }
        g.fill(x,y,x+w,y+h,focus?0xEE293237:0xE010151A);
        frame(g,x,y,w,h,b.active?(focus?0xFFDBD1B8:0xFF616B6D):0xFF303739);
        if(focus&&b.active){g.fill(x+3,y+3,x+5,y+h-3,0xFFD7C395);}
        g.enableScissor(x+5,y,x+w-5,y+h);
        g.drawCenteredString(Minecraft.getInstance().font,b.getMessage().copy().withStyle(s->s.withFont(FONT)),x+w/2,y+(h-9)/2,b.active?(focus?GOLD:WHITE):0x626969);
        g.disableScissor();
    }
    public static void bar(GuiGraphics g,int x,int y,int w,float ratio,int color) {
        g.fill(x,y,x+w,y+7,0xE2080B0F);frame(g,x,y,w,7,0xCC989B91);
        int filled=(int)((w-4)*Mth.clamp(ratio,0,1));
        g.fill(x+2,y+2,x+2+filled,y+5,color);g.fill(x+2,y+2,x+2+filled,y+3,0x60FFFFFF);
    }
    public static void ornateBar(GuiGraphics g,int x,int y,int w,float ratio,ResourceLocation fill){
        int pad=Math.max(4,w*24/256),inner=w-2*pad,amount=Math.round(inner*Mth.clamp(ratio,0,1));
        g.fill(x+pad,y+6,x+w-pad,y+11,0xCD080B10);
        if(amount>0){g.enableScissor(x+pad,y+6,x+pad+amount,y+11);g.blit(fill,x+pad,y+6,0,0,inner,5,inner,5);g.disableScissor();}
        com.mojang.blaze3d.systems.RenderSystem.enableBlend();g.blit(BAR_FRAME,x,y,0,0,w,17,w,17);com.mojang.blaze3d.systems.RenderSystem.disableBlend();
    }
}
