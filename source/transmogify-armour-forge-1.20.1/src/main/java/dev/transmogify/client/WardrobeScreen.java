package dev.transmogify.client;

import dev.transmogify.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import java.util.*;

public final class WardrobeScreen extends Screen {
    private static final int INK=0xFFF0EDF8, MUTED=0xFFB5B1C9, ACCENT=0xFFB6A0F7, CELL=26;
    private final TransmogNetwork.Open session;
    public final ArmourCatalogue catalogue=new ArmourCatalogue();
    public final String[] choices=new String[4];
    private EditBox search;
    private String query="", status="Choose a look. Your armour keeps all its stats.";
    private int selectedSlot,page,left,top,panelWidth,panelHeight,gridX,gridY,cols,rows,previewWidth;
    private List<ArmourCatalogue.Entry> filtered=List.of();
    private Button apply,matching,previous,next;
    private final List<Button> slots=new ArrayList<>();
    private boolean pending;
    private boolean expired;

    public WardrobeScreen(TransmogNetwork.Open session) {
        super(Component.literal("Transmogify Armour")); this.session=session;
        for(int i=0;i<4;i++) choices[i]=ArmourData.fits(session.equipped()[i],ArmourData.SLOTS[i]) ? ArmourData.appearanceId(session.equipped()[i]) : "";
    }
    private Button button(String label,int x,int y,int w,Runnable click) {
        return addRenderableWidget(Button.builder(Component.literal(label),b->click.run()).bounds(x,y,w,20).build());
    }
    @Override protected void init() {
        panelWidth=Math.min(width-12,620); panelHeight=Math.min(height-12,380);
        left=(width-panelWidth)/2; top=(height-panelHeight)/2;
        previewWidth=panelWidth>=450?150:96;
        gridX=left+previewWidth+12; gridY=top+88;
        cols=Math.max(1,(panelWidth-previewWidth-24)/CELL);
        rows=Math.max(1,(panelHeight-166)/CELL);
        int available=panelWidth-previewWidth-24;
        search=addRenderableWidget(new EditBox(font,gridX,top+34,available,18,Component.literal("Search armour or mod")));
        search.setMaxLength(100); search.setHint(Component.literal("Search armour or @mod")); search.setValue(query);
        search.setResponder(value->{query=value;page=0;refresh();});
        slots.clear();
        for(int i=0;i<4;i++) { final int index=i; slots.add(button(ArmourData.LABELS[i],gridX+i*(available/4),top+60,available/4-2,()->selectSlot(index))); }
        int footer=top+panelHeight;
        previous=button("<",gridX,footer-74,22,()->changePage(-1));
        next=button(">",gridX+available-22,footer-74,22,()->changePage(1));
        matching=button("Matching set",left+10,footer-99,previewWidth-8,this::chooseSet);
        button("Original slot",left+10,footer-75,previewWidth-8,()->{choices[selectedSlot]="";status="Original appearance selected.";refresh();});
        button("Reset all",left+10,footer-51,previewWidth-8,()->{Arrays.fill(choices,"");status="All original appearances selected. Apply to save.";refresh();});
        apply=button("Apply",left+panelWidth-128,footer-26,58,this::apply);
        button("Cancel",left+panelWidth-66,footer-26,58,this::onClose);
        refresh();
    }
    public void selectSlot(int index) { selectedSlot=index; page=0; refresh(); }
    public void setSearch(String value) { search.setValue(value); }
    public void choose(String id) {
        if(!ArmourData.fits(session.equipped()[selectedSlot],ArmourData.SLOTS[selectedSlot])) {
            status="Equip armour in this slot before changing its look."; return;
        }
        var entry=catalogue.get(id);
        if(entry==null || entry.slot()!=ArmourData.SLOTS[selectedSlot]) return;
        choices[selectedSlot]=id;status=entry.name()+" selected."; refresh();
    }
    public void chooseSet() {
        var selected=catalogue.get(choices[selectedSlot]); if(selected==null)return;
        int count=0;
        for(var entry:catalogue.set(selected)) for(int i=0;i<4;i++)
            if(entry.slot()==ArmourData.SLOTS[i] && ArmourData.fits(session.equipped()[i],ArmourData.SLOTS[i])) { choices[i]=entry.id();count++; }
        status="Selected "+count+" matching piece"+(count==1?".":"s.");refresh();
    }
    private int pageCount() { return Math.max(1,(filtered.size()+cols*rows-1)/(cols*rows)); }
    private void changePage(int delta) { page=Math.max(0,Math.min(pageCount()-1,page+delta));refresh(); }
    private void refresh() {
        filtered=catalogue.search(ArmourData.SLOTS[selectedSlot],query);
        page=Math.min(page,pageCount()-1);
        if(previous==null)return;
        previous.active=page>0; next.active=page<pageCount()-1;
        for(int i=0;i<slots.size();i++) slots.get(i).active=i!=selectedSlot;
        var entry=catalogue.get(choices[selectedSlot]);
        matching.active=!pending && entry!=null && catalogue.set(entry).size()>1;
        apply.active=expired || (!pending && Arrays.stream(session.equipped()).anyMatch(s->s.getItem() instanceof net.minecraft.world.item.ArmorItem));
        apply.setMessage(Component.literal(expired?"Reopen":"Apply"));
    }
    public void apply() {
        if(expired) { TransmogNetwork.request(); return; }
        if(pending)return;
        pending=true;status="Saving appearance...";refresh();
        TransmogNetwork.CHANNEL.sendToServer(new TransmogNetwork.Apply(session.token(),choices.clone()));
    }
    public void fail(String error) { expired=true;pending=true;status=error;refresh();apply.setMessage(Component.literal("Reopen"));apply.active=true; }
    private ItemStack[] previewEquipment() {
        ItemStack[] result=new ItemStack[4];
        for(int i=0;i<4;i++) { result[i]=session.equipped()[i].copy(); if(!result[i].isEmpty())ArmourData.set(result[i],choices[i]); }
        return result;
    }
    @Override public void render(GuiGraphics g,int mouseX,int mouseY,float partial) {
        renderBackground(g);
        g.fill(left,top,left+panelWidth,top+panelHeight,0xFA171421);
        g.fill(left,top,left+panelWidth,top+2,ACCENT);
        g.drawString(font,title,left+12,top+12,INK,false);
        if(panelWidth>420) g.drawString(font,"THE WARDROBE",left+panelWidth-100,top+13,MUTED,false);
        g.fill(left+8,top+32,left+previewWidth+3,top+panelHeight-106,0xFF211D2E);
        int px=left+previewWidth/2+5, bottom=top+panelHeight-116;
        if(minecraft.player!=null && bottom>top+65) {
            int scale=Math.min(64,Math.max(24,(bottom-top-44)/2));
            ClientWardrobe.beginPreview(minecraft.player.getUUID(),previewEquipment());
            try { InventoryScreen.renderEntityInInventoryFollowsMouse(g,px,bottom,scale,Math.max(-35,Math.min(35,(px-mouseX)*0.15f)),Math.max(-12,Math.min(12,(bottom-scale*1.6f-mouseY)*0.1f)),minecraft.player); }
            finally { ClientWardrobe.endPreview(); }
        }
        g.drawCenteredString(font,"Preview",px,top+39,MUTED);
        ArmourCatalogue.Entry hovered=null;
        int start=page*cols*rows;
        for(int i=0;i<cols*rows && start+i<filtered.size();i++) {
            var entry=filtered.get(start+i); int x=gridX+(i%cols)*CELL,y=gridY+(i/cols)*CELL;
            boolean over=mouseX>=x&&mouseX<x+24&&mouseY>=y&&mouseY<y+24;
            boolean chosen=choices[selectedSlot].equals(entry.id());
            g.fill(x,y,x+24,y+24,chosen?0xFF8870BE:over?0xFF4F435F:0xFF302A3E);
            g.renderItem(entry.icon(),x+4,y+4);
            if(over)hovered=entry;
        }
        if(filtered.isEmpty()) g.drawWordWrap(font,Component.literal("No armour found. Try another search."),gridX,gridY+10,panelWidth-previewWidth-24,MUTED);
        int available=panelWidth-previewWidth-24;
        String pages=(page+1)+" / "+pageCount()+"  ("+filtered.size()+")";
        g.drawCenteredString(font,pages,gridX+available/2,top+panelHeight-68,MUTED);
        String look=choices[selectedSlot].isEmpty()?"Original appearance":Optional.ofNullable(catalogue.get(choices[selectedSlot])).map(ArmourCatalogue.Entry::name).orElse("Unavailable appearance");
        g.drawString(font,font.plainSubstrByWidth(look,available),gridX,top+panelHeight-45,ACCENT,false);
        g.drawString(font,font.plainSubstrByWidth(status,panelWidth-144),left+10,top+panelHeight-19,MUTED,false);
        super.render(g,mouseX,mouseY,partial);
        if(mouseY>=top+panelHeight-26&&mouseY<top+panelHeight&&mouseX>=left&&mouseX<left+panelWidth-132)
            g.renderComponentTooltip(font,List.of(Component.literal(status)),mouseX,mouseY);
        if(hovered!=null)g.renderComponentTooltip(font,List.of(Component.literal(hovered.name()),Component.literal(hovered.mod()).withStyle(net.minecraft.ChatFormatting.DARK_PURPLE),Component.literal(hovered.id()).withStyle(net.minecraft.ChatFormatting.GRAY),Component.literal("Click to preview").withStyle(net.minecraft.ChatFormatting.GRAY)),mouseX,mouseY);
    }
    @Override public boolean mouseClicked(double x,double y,int button) {
        if(button==0 && !pending) {
            int col=(int)(x-gridX)/CELL,row=(int)(y-gridY)/CELL;
            if(x>=gridX&&y>=gridY&&col>=0&&col<cols&&row>=0&&row<rows) {
                int index=page*cols*rows+row*cols+col;
                if(index<filtered.size()) { choose(filtered.get(index).id());return true; }
            }
        }
        return super.mouseClicked(x,y,button);
    }
    @Override public boolean mouseScrolled(double x,double y,double amount) { changePage(amount>0?-1:1);return true; }
    @Override public boolean isPauseScreen() { return false; }
    @Override public void removed() { ClientWardrobe.endPreview(); }
}
