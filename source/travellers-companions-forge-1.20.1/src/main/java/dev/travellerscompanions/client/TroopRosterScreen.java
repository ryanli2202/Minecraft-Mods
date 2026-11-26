package dev.travellerscompanions.client;

import dev.travellerscompanions.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;

public final class TroopRosterScreen extends Screen {
    private CompoundTag state;
    private int selected,x,y,w,h;
    private long received;
    private Button summon;
    private CompanionEntity preview;

    public TroopRosterScreen(CompoundTag state){super(Component.literal("Troop groups"));this.state=state;selected=Math.max(0,state.getCompound("Troops").getInt("Kind"));received=System.currentTimeMillis();}
    public void update(CompoundTag value){state=value;received=System.currentTimeMillis();rebuildWidgets();}
    private CompoundTag group(){return state.getCompound("Troops").getList("Groups",Tag.TAG_COMPOUND).getCompound(selected);}
    private long recovery(){return Math.max(0,group().getLong("Recovery")-(System.currentTimeMillis()-received));}
    @Override protected void init(){
        w=Math.min(680,width-16);h=Math.min(420,height-16);x=(width-w)/2;y=(height-h)/2;
        addRenderableWidget(Button.builder(Component.literal("Named"),b->minecraft.setScreen(new RosterScreen(state))).bounds(x+w-125,y+12,60,20).build());
        addRenderableWidget(Button.builder(Component.literal("Close"),b->onClose()).bounds(x+w-61,y+12,48,20).build());
        int tab=(w-32)/3;for(int i=0;i<TroopKind.values().length;i++){final int kind=i;var b=addRenderableWidget(Button.builder(Component.literal(TroopKind.of(i).plural),v->{selected=kind;rebuildWidgets();}).bounds(x+12+i*tab,y+57,tab-4,22).build());b.active=i!=selected;}
        summon=addRenderableWidget(Button.builder(Component.literal(group().getBoolean("Active")?"Dismiss group":"Summon four"),b->{summon.active=false;CompanionNetwork.CHANNEL.sendToServer(new CompanionNetwork.Action(group().getBoolean("Active")?15:14,selected,-1));}).bounds(x+16,y+h-38,w-32,22).build());
        if(minecraft.level!=null){preview=TravellersCompanions.COMPANION.get().create(minecraft.level);var n=new CompoundTag();n.putInt("TroopKind",selected);n.putInt("TroopSlot",0);n.putInt("Character",TroopKind.of(selected).combatKind.ordinal());n.putInt("CompanionLevel",1);preview.readAdditionalSaveData(n);preview.giveStarter();}
        buttons();
    }
    private void buttons(){summon.active=group().getBoolean("Active")||state.getCompound("Troops").getInt("Count")==0&&recovery()==0;}
    @Override public void tick(){buttons();}
    @Override public boolean isPauseScreen(){return false;}
    @Override public boolean keyPressed(int key,int scan,int mods){if(ClientEvents.ROSTER.matches(key,scan)){onClose();return true;}return super.keyPressed(key,scan,mods);}
    @Override public void render(GuiGraphics g,int mx,int my,float partial){
        renderBackground(g);g.fill(x,y,x+w,y+h,0xF21B2226);g.fill(x,y,x+w,y+2,0xFFD8BC80);
        g.drawString(font,"TROOP GROUPS",x+14,y+16,0xEAD8AE,false);
        String counts="Named: "+state.getInt("PartyCount")+" / 2   |   Troops: "+state.getCompound("Troops").getInt("Count")+" / 4";
        g.drawString(font,counts,x+14,y+37,0x94A9AE,false);
        var kind=TroopKind.of(selected);boolean large=w>=520&&h>=330;
        int contentWidth=w-(large?155:32);
        g.drawString(font,"20 health each / No leveling",x+16,y+94,0xD3C6AA,false);
        if(h>=320)g.drawWordWrap(font,Component.literal(kind.description),x+16,y+114,contentWidth,0xC4CED0);
        if(large&&preview!=null)InventoryScreen.renderEntityInInventoryFollowsMouse(g,x+w-72,y+216,51,x+w-72-mx,y+145-my,preview);
        int rowY=y+(h>=320?170:116);var members=group().getList("Members",Tag.TAG_COMPOUND);
        for(int slot=0;slot<TroopKind.SIZE;slot++){
            var member=members.getCompound(slot);long wait=Math.max(0,member.getLong("Recovery")-(System.currentTimeMillis()-received));
            String status=wait>0?"Recovering "+RosterScreen.time(wait):member.getBoolean("Active")?(member.getBoolean("Hold")?"Holding":"Following"):"Ready";
            g.drawString(font,"#"+(slot+1)+"   "+(int)Math.ceil(member.getFloat("Health"))+" / 20   "+status,x+16,rowY+slot*19,0xD1DADD,false);
        }
        if(h>=320){
            String status=group().getBoolean("Active")?"This group is travelling with you.":recovery()>0?"Group ready in "+RosterScreen.time(recovery()):state.getCompound("Troops").getInt("Count")>0?"Dismiss the active group to change troop type.":"Summon alongside your two named companions.";
            g.drawWordWrap(font,Component.literal(status),x+16,rowY+87,w-32,0xEAD8AE);
        }
        if(h>=390)g.drawWordWrap(font,Component.literal("Right-click: equipment and supplies. Sneak + right-click: follow, hold and combat stance. One group at a time."),x+16,y+h-83,w-32,0x91A6AE);
        super.render(g,mx,my,partial);
    }
}
