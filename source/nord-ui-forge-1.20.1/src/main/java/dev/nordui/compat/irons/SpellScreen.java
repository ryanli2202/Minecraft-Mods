package dev.nordui.compat.irons;

import dev.nordui.client.*;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import java.util.ArrayList;
import java.util.List;

public final class SpellScreen extends AbstractContainerScreen<SpellMenu> {
    private int page,selected=-1;
    private final List<Button> spells=new ArrayList<>();
    private Button inscribe,previous,next,back;
    public SpellScreen(SpellMenu menu,Inventory inventory,Component title){super(menu,inventory,title);imageWidth=308;imageHeight=236;}
    @Override protected void init(){
        super.init();spells.clear();
        back=addRenderableWidget(Button.builder(Component.literal("< INVENTORY"),b->{minecraft.player.closeContainer();minecraft.setScreen(new NordInventoryScreen(minecraft.player));}).bounds(leftPos+10,topPos+26,101,18).build());
        addRenderableWidget(Button.builder(Component.literal("[ SPELLS ]"),b->{}).bounds(leftPos+115,topPos+26,105,18).build());
        previous=addRenderableWidget(Button.builder(Component.literal("<"),b->{page--;refresh();}).bounds(leftPos+224,topPos+26,34,18).build());
        next=addRenderableWidget(Button.builder(Component.literal(">"),b->{page++;refresh();}).bounds(leftPos+262,topPos+26,34,18).build());
        for(int i=0;i<8;i++){
            final int cell=i;
            spells.add(addRenderableWidget(Button.builder(Component.empty(),b->select(page*8+cell)).bounds(leftPos+110+(i%2)*94,topPos+54+(i/2)*18,90,17).build()));
        }
        inscribe=addRenderableWidget(Button.builder(Component.literal("INSCRIBE SCROLL"),b->send(-1)).bounds(leftPos+110,topPos+128,184,18).build());
        place(36,16,56);place(37,16,88);place(38,16,120);
        for(int i=0;i<27;i++)place(i,73+i%9*18,150+i/9*18);
        for(int i=27;i<36;i++)place(i,73+(i-27)*18,208);
        refresh();
    }
    private void place(int i,int x,int y){menu.getSlot(i).x=x;menu.getSlot(i).y=y;}
    public void select(int index){selected=index;send(index);refresh();}
    public void send(int id){minecraft.gameMode.handleInventoryButtonClick(menu.containerId,id);}
    @Override protected void containerTick(){super.containerTick();refresh();}
    private void refresh(){
        var container=menu.spells();int count=container.getMaxSpellCount();page=Math.max(0,Math.min(page,Math.max(0,(count-1)/8)));
        if(selected>=count)selected=-1;
        boolean emptyCursor=menu.getCarried().isEmpty();
        previous.active=page>0;next.active=(page+1)*8<count;back.active=emptyCursor;
        for(int i=0;i<spells.size();i++){
            int index=page*8+i;var button=spells.get(i);button.active=index<count&&emptyCursor;
            var spell=index<count?container.getSpellAtIndex(index):SpellData.EMPTY;
            String name=index>=count?"--":spell.equals(SpellData.EMPTY)?"Empty":spell.getSpell().getDisplayName(minecraft.player).getString();
            String prefix=(index==selected?"> ":"")+(index+1)+" ";
            button.setMessage(Theme.component(prefix+font.plainSubstrByWidth(name,65-font.width(prefix))));
            button.setTooltip(Tooltip.create(Component.literal(index>=count?"Insert a spellbook to reveal its slots.":name+(spell.equals(SpellData.EMPTY)?" — ready for inscription":" — level "+spell.getLevel()+(spell.canRemove()?". Select, then take the extracted scroll on the left.":". Bound spell: cannot be removed.")))));
        }
        inscribe.active=emptyCursor&&menu.canInscribe(selected);
        inscribe.setTooltip(Tooltip.create(Component.literal("Put a scroll in SCROLL, select an empty spell slot, then inscribe. To replace a spell, first take its EXTRACT scroll. Bound spells stay locked.")));
    }
    @Override protected void renderBg(GuiGraphics g,float partial,int mx,int my){
        Theme.panel(g,leftPos,topPos,imageWidth,imageHeight);Theme.text(g,"T H E  D E E P  N I G H T  /  M A G I C",leftPos+12,topPos+11,Theme.WHITE);
        for(var slot:menu.slots)Theme.slot(g,leftPos+slot.x,topPos+slot.y);
        Theme.text(g,"BOOK",leftPos+40,topPos+60,Theme.GOLD);Theme.text(g,"SCROLL",leftPos+40,topPos+92,Theme.MUTED);Theme.text(g,"EXTRACT",leftPos+40,topPos+124,Theme.MUTED);
        Theme.text(g,"CARRIED",leftPos+12,topPos+154,Theme.MUTED);
        Theme.text(g,"ITEMS",leftPos+12,topPos+166,Theme.MUTED);
        Theme.text(g,"Select > extract > inscribe replacement",leftPos+12,topPos+226,Theme.MUTED);
    }
    @Override protected void renderLabels(GuiGraphics g,int mx,int my){}
    @Override public void render(GuiGraphics g,int mx,int my,float partial){renderBackground(g);super.render(g,mx,my,partial);renderTooltip(g,mx,my);}
}
