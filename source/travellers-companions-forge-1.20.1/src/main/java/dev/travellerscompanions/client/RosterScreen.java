package dev.travellerscompanions.client;

import dev.travellerscompanions.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;

public final class RosterScreen extends Screen {
    private CompoundTag state;private int selected;private long received;private CompanionEntity preview;
    private int x,y,w,h,rail,step,offset,visible;private Button summon,train;
    public RosterScreen(CompoundTag state){super(Component.literal("Traveller's Companions"));this.state=state;selected=CharacterKind.of(state.getInt("Active")).ordinal();received=System.currentTimeMillis();}
    public void update(CompoundTag value){state=value;received=System.currentTimeMillis();rebuildWidgets();}
    private CompoundTag entry(){return state.getList("Companions",Tag.TAG_COMPOUND).getCompound(selected);}
    private long recovery(){return Math.max(0,entry().getLong("Recovery")-(System.currentTimeMillis()-received));}
    @Override protected void init(){
        w=Math.min(680,width-16);h=Math.min(420,height-16);x=(width-w)/2;y=(height-h)/2;rail=Math.min(160,w/3);
        visible=Math.min(CharacterKind.COUNT,Math.max(1,(h-100)/28));offset=Math.max(0,Math.min(offset,CharacterKind.COUNT-visible));step=Math.min(43,(h-100)/visible);
        for(int i=offset;i<offset+visible;i++){final int kind=i;var c=CharacterKind.of(i);addRenderableWidget(Button.builder(Component.literal(c.display),b->{selected=kind;rebuildWidgets();}).bounds(x+12,y+63+(i-offset)*step,rail-24,22).build());}
        if(visible<CharacterKind.COUNT){
            var previous=addRenderableWidget(Button.builder(Component.literal("Prev"),b->{offset=Math.max(0,offset-visible);rebuildWidgets();}).bounds(x+12,y+h-27,(rail-28)/2,18).build());previous.active=offset>0;
            var next=addRenderableWidget(Button.builder(Component.literal("Next"),b->{offset=Math.min(CharacterKind.COUNT-visible,offset+visible);rebuildWidgets();}).bounds(x+16+(rail-28)/2,y+h-27,(rail-28)/2,18).build());next.active=offset+visible<CharacterKind.COUNT;
        }
        int dx=x+rail+16,dw=w-rail-32;
        summon=addRenderableWidget(Button.builder(Component.literal(entry().getBoolean("Active")?"Dismiss":"Summon"),b->send(entry().getBoolean("Active")?2:1)).bounds(dx,y+h-63,dw/2-4,22).build());
        train=addRenderableWidget(Button.builder(Component.literal(dw<330?"Train ("+CharacterKind.trainingCost(entry().getInt("Level"))+")":"Train: "+CharacterKind.trainingCost(entry().getInt("Level"))+" emeralds"),b->send(3)).bounds(dx+dw/2+4,y+h-63,dw/2-4,22).build());
        addRenderableWidget(Button.builder(Component.literal("Troops"),b->minecraft.setScreen(new TroopRosterScreen(state))).bounds(x+w-125,y+12,60,20).build());
        addRenderableWidget(Button.builder(Component.literal("Close"),b->onClose()).bounds(x+w-61,y+12,48,20).build());
        if(minecraft.level!=null){preview=TravellersCompanions.COMPANION.get().create(minecraft.level);var n=new CompoundTag();n.putInt("Character",selected);n.putInt("CompanionLevel",entry().getInt("Level"));preview.readAdditionalSaveData(n);preview.setHealth(preview.getMaxHealth());preview.giveStarter();}
        buttons();
    }
    @Override public boolean mouseScrolled(double mx,double my,double delta){if(mx>=x&&mx<x+rail&&my>=y+43&&my<y+h&&visible<CharacterKind.COUNT){offset=Math.max(0,Math.min(CharacterKind.COUNT-visible,offset+(delta<0?1:-1)));rebuildWidgets();return true;}return super.mouseScrolled(mx,my,delta);}
    private void send(int action){summon.active=false;train.active=false;CompanionNetwork.CHANNEL.sendToServer(new CompanionNetwork.Action(action,selected,-1));}
    private void buttons(){int level=entry().getInt("Level");summon.active=entry().getBoolean("Active")||(state.getInt("PartyCount")<state.getInt("PartyLimit")&&recovery()==0);train.active=level<20&&state.getInt("Emeralds")>=CharacterKind.trainingCost(level);if(level>=20)train.setMessage(Component.literal("Maximum level"));}
    @Override public void tick(){buttons();}
    @Override public boolean isPauseScreen(){return false;}
    @Override public boolean keyPressed(int key,int scan,int mods){if(ClientEvents.ROSTER.matches(key,scan)){onClose();return true;}return super.keyPressed(key,scan,mods);}
    public static String time(long millis){long s=(millis+999)/1000;return (s/60)+":"+String.format(java.util.Locale.ROOT,"%02d",s%60);}
    @Override public void render(GuiGraphics g,int mx,int my,float partial){
        renderBackground(g);g.fill(x,y,x+w,y+h,0xF21B2226);g.fill(x,y,x+w,y+2,0xFFD8BC80);g.fill(x,y+43,x+rail,y+h,0xFF242F33);g.drawString(font,w<460?"COMPANIONS":"TRAVELLER'S COMPANIONS",x+14,y+16,0xEAD8AE,false);
        g.drawString(font,"Travelling party: "+state.getInt("PartyCount")+" / "+state.getInt("PartyLimit"),x+14,y+34,0x94A9AE,false);
        var list=state.getList("Companions",Tag.TAG_COMPOUND);
        for(int i=offset;i<offset+visible;i++){var c=CharacterKind.of(i);int by=y+63+(i-offset)*step;if(i==selected)g.fill(x+4,by-4,x+7,by+Math.min(step-3,40),0xFF000000|c.color);var e=list.getCompound(i);if(e.getBoolean("Active"))g.fill(x+rail-9,by+8,x+rail-5,by+12,0xFFD8BC80);if(step>=43)g.drawString(font,"Level "+e.getInt("Level")+(e.getBoolean("Active")&&rail>=150?" / Active":""),x+16,by+27,0xB1BFC0,false);}
        var c=CharacterKind.of(selected);var e=entry();int dx=x+rail+16,dw=w-rail-32;boolean large=dw>320&&h>=330;
        g.drawString(font,dw<270?c.display:c.display+" / "+c.title,dx,y+60,c.color,false);g.drawString(font,"LEVEL "+e.getInt("Level")+" / 20",dx,y+80,0xD3C6AA,false);
        if(large&&preview!=null)InventoryScreen.renderEntityInInventoryFollowsMouse(g,x+w-70,y+183,48,(x+w-70-mx),y+110-my,preview);
        if(h>=320)g.drawWordWrap(font,Component.literal(c.lore),dx,y+105,large?dw-130:dw,0xC4CED0);
        int infoY=y+(h>=320?192:105);float hp=e.getFloat("Health"),max=c.maxHealth(e.getInt("Level"));
        g.drawString(font,"Health "+(int)Math.ceil(hp)+" / "+(int)max+(dw>=320?"   |   Bond "+e.getInt("Bond")+"   |   Victories "+e.getInt("Kills"):""),dx,infoY,0xD1DADD,false);
        g.fill(dx,infoY+15,dx+dw,infoY+21,0xFF394247);g.fill(dx,infoY+15,dx+(int)(dw*Math.min(1,hp/max)),infoY+21,0xFF000000|c.color);
        String status=recovery()>0?"Recovering - ready in "+time(recovery()):e.getBoolean("Active")?(e.getBoolean("Hold")?"Holding position":"Travelling with you"):"Ready to travel";
        g.drawString(font,status,dx,infoY+34,0xEAD8AE,false);
        if(h>=355)g.drawWordWrap(font,Component.literal("Next milestones: 3 Stances / 5 Rally / 8 Scout / 12 Field dressing / 15 Oath"),dx,infoY+55,dw,0x91A6AE);
        if(h>=390)g.drawWordWrap(font,Component.literal("Right-click: gear & backpack. Sneak + right-click: dialogue wheel. Equip armour, weapons and spare arrows."),dx,y+h-103,dw,0xA6B6BA);
        g.drawString(font,"Emeralds: "+state.getInt("Emeralds")+(dw>=360?"  |  Training: +2 health, +0.25 attack":""),dx,y+h-28,0x99ACB0,false);
        super.render(g,mx,my,partial);
    }
}
