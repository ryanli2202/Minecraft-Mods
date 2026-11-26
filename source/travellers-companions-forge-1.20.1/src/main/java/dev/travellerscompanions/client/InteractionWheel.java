package dev.travellerscompanions.client;

import dev.travellerscompanions.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;

/** Radial nodes remain normal focusable buttons for mouse and keyboard accessibility. */
public final class InteractionWheel extends Screen {
    private final CompoundTag state;private final int entity;private int cx,cy,r;private CompanionEntity companion;
    private String dialogue;private Button rally;private final long received=System.currentTimeMillis();private long rallyDuration;
    private static final int[] LEVELS={1,1,1,3,5,8,12,15};
    public InteractionWheel(CompoundTag state,int entity){super(Component.literal("Companion dialogue"));this.state=state;this.entity=entity;dialogue=state.getString("Dialogue");}
    @Override protected void init(){
        companion=minecraft.level.getEntity(entity) instanceof CompanionEntity e?e:null;if(companion==null){onClose();return;}
        if(companion.isTroop()){
            cx=width/2;cy=height/2;
            var entry=state.getCompound("Troops").getList("Groups",Tag.TAG_COMPOUND).getCompound(companion.troopKind().ordinal()).getList("Members",Tag.TAG_COMPOUND).getCompound(companion.troopSlot());
            addRenderableWidget(Button.builder(Component.literal(entry.getBoolean("Hold")?"Follow me":"Hold position"),b->CompanionNetwork.CHANNEL.sendToServer(new CompanionNetwork.Action(6,-1,entity))).bounds(cx-105,cy,210,22).build());
            addRenderableWidget(Button.builder(Component.literal("Stance: "+new String[]{"Aggressive","Defensive","Passive"}[entry.getInt("Stance")]),b->CompanionNetwork.CHANNEL.sendToServer(new CompanionNetwork.Action(7,-1,entity))).bounds(cx-105,cy+30,210,22).build());
            return;
        }
        cx=width/2;cy=height/2+5;r=Math.min(137,(height-95)/2);
        if(!dialogue.isEmpty()){addRenderableWidget(Button.builder(Component.literal("Back to circle"),b->{dialogue="";rebuildWidgets();}).bounds(cx-65,height/2+55,130,22).build());return;}
        var entry=state.getList("Companions",Tag.TAG_COMPOUND).getCompound(companion.kind().ordinal());
        String[] names={"Talk","Their story",entry.getBoolean("Hold")?"Follow me":"Hold position","Stance: "+new String[]{"Aggressive","Defensive","Passive"}[entry.getInt("Stance")],"Rally","Scout's insight","Field dressing","Our oath"};
        for(int i=0;i<8;i++){final int action=4+i;double angle=-Math.PI/2+i*Math.PI/4;int bx=cx+(int)(Math.cos(angle)*r)-57,by=cy+(int)(Math.sin(angle)*r)-11;boolean unlocked=entry.getInt("Level")>=LEVELS[i];String label=unlocked?names[i]:"Unlock at level "+LEVELS[i];var b=addRenderableWidget(Button.builder(Component.literal(label),button->CompanionNetwork.CHANNEL.sendToServer(new CompanionNetwork.Action(action,companion.kind().ordinal(),entity))).bounds(bx,by,114,22).build());b.active=unlocked;b.setTooltip(net.minecraft.client.gui.components.Tooltip.create(Component.literal(names[i])));if(i==4){rally=b;rallyDuration=entry.getLong("Rally");if(rallyDuration>0){b.active=false;b.setMessage(Component.literal("Rally: "+RosterScreen.time(rallyDuration)));}}}
    }
    @Override public boolean isPauseScreen(){return false;}
    @Override public void tick(){if(companion==null||companion.isRemoved()||minecraft.player.distanceToSqr(companion)>64){onClose();return;}if(rally!=null&&companion.companionLevel()>=5){long left=Math.max(0,rallyDuration-(System.currentTimeMillis()-received));rally.active=left==0;rally.setMessage(Component.literal(left==0?"Rally":"Rally: "+RosterScreen.time(left)));}}
    @Override public void render(GuiGraphics g,int mx,int my,float pt){
        renderBackground(g);g.fill(0,0,width,height,0xB0141D23);if(companion==null)return;
        if(companion.isTroop()){
            g.drawCenteredString(font,companion.displayName(),cx,cy-58,0xEAD8AE);
            g.drawCenteredString(font,"20 base health / No leveling",cx,cy-38,0xB2C0C3);
            g.drawCenteredString(font,"Esc to return to your adventure",cx,height-19,0x91A4AB);super.render(g,mx,my,pt);return;
        }
        if(!dialogue.isEmpty()){int panel=Math.min(500,width-32);g.fill(cx-panel/2-10,height/2-70,cx+panel/2+10,height/2+87,0xF5233035);g.drawCenteredString(font,companion.kind().display,cx,height/2-57,companion.kind().color);g.drawWordWrap(font,Component.literal(dialogue),cx-panel/2+10,height/2-34,panel-20,0xE1DDD2);super.render(g,mx,my,pt);return;}
        g.drawCenteredString(font,"THE TRAVELLER'S CIRCLE",cx,15,0xEAD8AE);g.drawCenteredString(font,"Choose a word. Give an order. Build a bond.",cx,30,0xA2B5BA);
        // A dotted circle and spokes establish the radial menu without opaque art over the world.
        for(int i=0;i<96;i++){double a=i*Math.PI/48;int px=cx+(int)(Math.cos(a)*r),py=cy+(int)(Math.sin(a)*r);g.fill(px,py,px+2,py+2,0xFF687875);}
        for(int i=0;i<8;i++){double a=-Math.PI/2+i*Math.PI/4;for(int d=58;d<r-48;d+=5){int px=cx+(int)(Math.cos(a)*d),py=cy+(int)(Math.sin(a)*d);g.fill(px,py,px+2,py+2,0xFF687875);}}
        g.blit(TravellersCompanions.id("textures/entity/"+companion.kind().key()+".png"),cx-16,cy-32,32,32,8,8,8,8,64,64);
        g.drawCenteredString(font,companion.kind().display,cx,cy+7,companion.kind().color);g.drawCenteredString(font,"Level "+companion.companionLevel(),cx,cy+22,0xB2C0C3);
        g.drawCenteredString(font,"Esc to return to your adventure",cx,height-19,0x91A4AB);super.render(g,mx,my,pt);
    }
}
