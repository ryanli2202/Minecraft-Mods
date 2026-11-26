package dev.nordui.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.TitleScreen;

/** Inherits vanilla title actions, narration and mod-injected widgets. Only the presentation changes. */
public final class NordTitleScreen extends TitleScreen {
    @Override protected void init(){
        super.init();int row=0,bh=height<300?17:21,spacing=height<300?2:4;
        int x=(int)(width*.64),w=Math.min(180,width-x-12),y=height<300?88:(int)(height*.44);
        int icon=0;
        for(var child:children())if(child instanceof net.minecraft.client.gui.components.Button b){
            if(b instanceof net.minecraft.client.gui.components.PlainTextButton)continue;
            if(b.getWidth()<=40){b.setX(10+icon*25);b.setY(height-40);icon++;continue;}
            b.setX(x);b.setY(y+row*(bh+spacing));b.setWidth(w);b.setHeight(bh);row++;
        }
    }
    @Override public void render(GuiGraphics g,int mx,int my,float partial) {
        g.blit(Theme.BACKGROUND,0,0,0,0,width,height,width,height);
        g.fill(0,0,width,height,0x2010171D);
        int tw=Math.min(width-28,470),th=tw/3,ty=height<300?-5:8;
        com.mojang.blaze3d.systems.RenderSystem.enableBlend();
        g.blit(Theme.TITLE,(width-tw)/2,ty,0,0,tw,th,tw,th);
        com.mojang.blaze3d.systems.RenderSystem.disableBlend();
        Theme.centered(g,"A   N O R T H E R N   O D Y S S E Y",width/2,ty+th-7,Theme.MUTED);
        for(var child:children())if(child instanceof Renderable r)r.render(g,mx,my,partial);
        Theme.text(g,"THE DEEP NIGHT  /  1.20.1",8,height-12,Theme.MUTED);
    }
}
