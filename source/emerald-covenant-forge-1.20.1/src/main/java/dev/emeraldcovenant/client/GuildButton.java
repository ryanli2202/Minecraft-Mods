package dev.emeraldcovenant.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

final class GuildButton extends AbstractButton {
    private final Runnable action;
    boolean selected;
    GuildButton(int x,int y,int w,int h,String text,Runnable action){super(x,y,w,h,Component.literal(text));this.action=action;}
    @Override public void onPress(){action.run();}
    @Override protected void renderWidget(GuiGraphics g,int mx,int my,float partial){
        int border=!active?0xFF39433B:selected?0xFFE0BC73:isHoveredOrFocused()?0xFFC8A76A:0xFF695939;
        int fill=!active?0xDD101A16:selected?0xFF294237:isHoveredOrFocused()?0xFF293D32:0xEE13231D;
        g.fill(getX(),getY(),getX()+width,getY()+height,border);g.fill(getX()+1,getY()+1,getX()+width-1,getY()+height-1,fill);
        var font=Minecraft.getInstance().font;
        String label=font.plainSubstrByWidth(getMessage().getString(),width-8);
        g.drawCenteredString(font,label,getX()+width/2,getY()+(height-8)/2,!active?0x788479:0xE5D8B4);
    }
    @Override protected void updateWidgetNarration(NarrationElementOutput out){defaultButtonNarrationText(out);}
}
