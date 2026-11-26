package dev.nordui.client;

import dev.nordui.VaultMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import java.util.ArrayList;
import java.util.List;

public final class VaultScreen extends AbstractContainerScreen<VaultMenu> {
    private boolean compact;
    private int gridX,gridY,step,bagY,hotbarY,side;
    private EditBox pageInput;
    private final List<Button> transfers=new ArrayList<>();
    public VaultScreen(VaultMenu menu,Inventory inv,Component title){super(menu,inv,title);}
    @Override protected void init() {
        compact=width<530||height<326||dev.nordui.compat.UiCompat.compactForJei();imageWidth=compact?308:500;imageHeight=compact?236:302;super.init();
        gridX=compact?10:20;gridY=compact?34:56;step=compact?18:20;bagY=compact?154:186;hotbarY=compact?212:260;side=compact?188:228;
        for(int i=0;i<menu.visibleSlots;i++)place(i,gridX+i%9*step,gridY+i/9*step);
        for(int i=0;i<27;i++)place(menu.visibleSlots+i,gridX+i%9*step,bagY+i/9*step);
        for(int i=0;i<9;i++)place(menu.visibleSlots+27+i,gridX+i*step,hotbarY);
        transfers.clear();int buttonWidth=compact?48:115,y=topPos+(compact?56:154);
        var prev=button("< PREV",leftPos+side,y,buttonWidth,()->go(menu.page-1));prev.active=menu.page>0;
        var next=button("NEXT >",leftPos+side+buttonWidth+6,y,buttonWidth,()->go(menu.page+1));next.active=menu.page<VaultMenu.PAGES-1;
        transfers.add(prev);transfers.add(next);
        int py=y+25;
        pageInput=addRenderableWidget(new EditBox(font,leftPos+side,py,compact?49:76,18,Component.literal("Vault page 1 to 19")));
        pageInput.setMaxLength(2);pageInput.setFilter(s->s.matches("[0-9]*"));pageInput.setValue(Integer.toString(menu.page+1));
        pageInput.setTextColor(Theme.WHITE);
        transfers.add(button("GO",leftPos+side+(compact?55:83),py,compact?47:153,()->{try{go(Integer.parseInt(pageInput.getValue())-1);}catch(NumberFormatException ignored){}}));
        var deposit=button(compact?"STORE BACKPACK":"STORE BACKPACK IN VAULT",leftPos+side,topPos+(compact?151:211),compact?102:236,()->send(100));
        deposit.setTooltip(Tooltip.create(Component.literal("Store the 27 backpack slots across all 999 vault slots. Keeps hotbar and equipment. A full vault leaves remaining items in your backpack.")));transfers.add(deposit);
        button("< INVENTORY",leftPos+side,topPos+(compact?182:238),compact?102:236,()->{minecraft.player.closeContainer();minecraft.setScreen(new NordInventoryScreen(minecraft.player));});
    }
    private Button button(String text,int x,int y,int w,Runnable action){return addRenderableWidget(Button.builder(Component.literal(text),b->action.run()).bounds(x,y,w,18).build());}
    private void place(int i,int x,int y){menu.slots.get(i).x=x;menu.slots.get(i).y=y;}
    public void go(int page){if(page>=0&&page<VaultMenu.PAGES&&page!=menu.page)send(page);}
    public void send(int action){if(menu.getCarried().isEmpty())minecraft.gameMode.handleInventoryButtonClick(menu.containerId,action);}
    @Override protected void containerTick(){super.containerTick();boolean empty=menu.getCarried().isEmpty();for(var b:transfers)b.active=empty;transfers.get(0).active=empty&&menu.page>0;transfers.get(1).active=empty&&menu.page<VaultMenu.PAGES-1;}
    @Override public boolean keyPressed(int key,int scan,int mods){if(pageInput.isFocused()&&key==257){try{go(Integer.parseInt(pageInput.getValue())-1);}catch(NumberFormatException ignored){}return true;}return super.keyPressed(key,scan,mods);}
    @Override protected void renderBg(GuiGraphics g,float partial,int mx,int my) {
        int x=leftPos,y=topPos;Theme.panel(g,x,y,imageWidth,imageHeight);
        Theme.text(g,"P E R S O N A L   V A U L T",x+12,y+11,Theme.WHITE);
        Theme.text(g,"999 SLOTS",x+imageWidth-68,y+11,Theme.GOLD);
        Theme.rule(g,x+12,y+27,imageWidth-25);
        for(int i=0;i<54;i++) {
            int sx=x+gridX+i%9*step,sy=y+gridY+i/9*step;
            if(i<menu.visibleSlots)Theme.slot(g,sx,sy);
            else {g.fill(sx,sy,sx+16,sy+16,0x60101417);Theme.centered(g,"-",sx+8,sy+5,0x3C4446);}
        }
        for(int i=menu.visibleSlots;i<menu.slots.size();i++)Theme.slot(g,x+menu.slots.get(i).x,y+menu.slots.get(i).y);
        Theme.text(g,"YOUR BACKPACK",x+gridX,y+bagY-12,Theme.MUTED);
        if(!compact)Theme.text(g,"QUICK SLOTS",x+gridX,y+hotbarY-12,Theme.MUTED);
        int sx=x+side,sy=y+(compact?39:49);
        Theme.text(g,"PAGE "+(menu.page+1)+" / "+VaultMenu.PAGES,sx,sy,Theme.GOLD);
        if(!compact){
            Theme.text(g,"A place for every journey.",sx,sy+24,Theme.WHITE);
            g.drawWordWrap(font,Theme.component("Private to your character. Saved with this world. Retained after death."),sx,sy+42,230,Theme.MUTED);
            Theme.bar(g,sx,sy+81,236,menu.used()/999f,0xFF927D53);
            Theme.text(g,menu.used()+" occupied  /  "+(999-menu.used())+" free",sx,sy+93,Theme.MUTED);
        } else {
            Theme.text(g,menu.used()+" / 999 used",sx,y+113,Theme.MUTED);
            Theme.bar(g,sx,y+127,102,menu.used()/999f,0xFF927D53);
        }
        String footer=menu.getCarried().isEmpty()?(compact?"Shift-click: transfer    Esc: close":"SHIFT + CLICK  transfer     /     18 x 54 + 27 = 999 SLOTS     /     ESC  close"):"Put down your cursor item to change pages.";
        if(!compact)Theme.text(g,footer,x+12,y+imageHeight-11,Theme.MUTED);
        else if(menu.getCarried().isEmpty()){Theme.text(g,"Shift-click: move",sx,y+209,Theme.MUTED);Theme.text(g,"Esc: close",sx,y+221,Theme.MUTED);}
        else g.drawWordWrap(font,Component.literal("Put down cursor item."),sx,y+207,102,Theme.MUTED);
    }
    @Override protected void renderLabels(GuiGraphics g,int mx,int my) {}
    @Override public void render(GuiGraphics g,int mx,int my,float partial){renderBackground(g);super.render(g,mx,my,partial);renderTooltip(g,mx,my);}
}
