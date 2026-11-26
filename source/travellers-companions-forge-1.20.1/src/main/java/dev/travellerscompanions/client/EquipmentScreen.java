package dev.travellerscompanions.client;

import dev.travellerscompanions.CompanionMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public final class EquipmentScreen extends AbstractContainerScreen<CompanionMenu> {
    public EquipmentScreen(CompanionMenu menu,Inventory inv,Component title){super(menu,inv,title);imageWidth=176;imageHeight=220;inventoryLabelY=125;titleLabelY=6;}
    @Override protected void init(){super.init();if(menu.companion!=null&&menu.companion.kind()==dev.travellerscompanions.CharacterKind.LEONARDO)addRenderableWidget(net.minecraft.client.gui.components.Button.builder(Component.literal("Spells"),b->dev.travellerscompanions.CompanionNetwork.CHANNEL.sendToServer(new dev.travellerscompanions.CompanionNetwork.Action(12,menu.companion.kind().ordinal(),menu.companion.getId()))).bounds(leftPos+117,topPos+48,51,14).build());}
    @Override protected void renderBg(GuiGraphics g,float partial,int mx,int my){
        g.fill(leftPos-3,topPos-3,leftPos+179,topPos+223,0xFFBDA673);g.fill(leftPos,topPos,leftPos+176,topPos+220,0xFF232E33);
        for(var slot:menu.slots){int x=leftPos+slot.x,y=topPos+slot.y;g.fill(x-1,y-1,x+17,y+17,0xFF71817D);g.fill(x,y,x+16,y+16,0xFF111B20);}
        String[] labels={"Head","Chest","Legs","Feet","Hand","Off"};for(int i=0;i<6;i++)g.drawString(font,labels[i],leftPos+7+i*27,topPos+17,0xB7C6C9,false);
        g.drawString(font,menu.companion!=null&&menu.companion.kind()==dev.travellerscompanions.CharacterKind.LEONARDO?"Backpack":"Backpack / food & arrows",leftPos+8,topPos+52,0xB7C6C9,false);
    }
    @Override protected void renderLabels(GuiGraphics g,int mx,int my){g.drawString(font,title,8,6,0xEAD8AE,false);g.drawString(font,playerInventoryTitle,8,125,0xB7C6C9,false);}
    @Override public void render(GuiGraphics g,int mx,int my,float partial){renderBackground(g);super.render(g,mx,my,partial);renderTooltip(g,mx,my);}
}
