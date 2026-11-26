package dev.deepnight.client;

import dev.deepnight.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import java.util.*;

/** Boss catalogue and live campaign controls. Rendering never advances an encounter. */
public final class BossScreen extends Screen {
    private BossNetwork.State state;
    private final Map<Integer,LivingEntity> previews=new HashMap<>();
    private EditBox search;
    private String query="",feedback="";
    private int selected,page,age,pendingTicks;
    private boolean pending,confirmEnd;
    private int x,y,w,h,listX,listW,detailX,detailW,rows;

    public BossScreen(BossNetwork.State state){
        super(Component.literal("The Deep Night"));this.state=state;
        selected=Math.min(state.next(),Species.BOSS_COUNT-1);
    }
    public BossNetwork.State campaignState(){return state;}
    public static void receive(BossNetwork.Menu menu){
        Minecraft mc=Minecraft.getInstance();
        if(menu.close()){if(mc.screen instanceof BossScreen)mc.setScreen(null);return;}
        if(menu.open())mc.setScreen(new BossScreen(menu.state()));
        else if(mc.screen instanceof BossScreen screen)screen.update(menu.state());
    }
    public void update(BossNetwork.State next){
        if(state.active()!=next.active())confirmEnd=false;
        state=next;pending=false;pendingTicks=0;
        if(!next.feedback().isBlank())feedback=next.feedback();
        rebuild();
    }
    public void selectBoss(int stage){
        selected=Math.max(0,Math.min(Species.BOSS_COUNT-1,stage));query="";feedback="";
        if(search!=null){search.setValue("");page=selected/rows;rebuild();}
    }
    @Override public boolean isPauseScreen(){return false;}
    @Override protected void init(){
        w=Math.min(760,width-20);h=height-16;x=(width-w)/2;y=8;
        listX=x+12;listW=(w-38)*44/100;detailX=listX+listW+16;detailW=x+w-12-detailX;
        rows=Math.max(1,(h-165)/30);
        search=new EditBox(font,listX,y+54,listW,20,Component.literal("Search bosses"));
        search.setMaxLength(80);search.setHint(Component.literal("Search bosses..."));search.setValue(query);
        search.setResponder(value->{query=value;page=0;rebuild();});
        page=selected/rows;rebuild();
    }
    private List<Integer> filtered(){
        String q=query.toLowerCase(Locale.ROOT);List<Integer> result=new ArrayList<>();
        for(int i=0;i<Species.BOSS_COUNT;i++){
            Species s=Species.boss(i);
            if((s.title+" "+s.id+" "+(s.id.startsWith("dawn_")?"dawncraft":"mo'bosses")).toLowerCase(Locale.ROOT).contains(q))result.add(i);
        }
        return result;
    }
    private void rebuild(){
        if(search==null)return;
        var choices=filtered();int pages=Math.max(1,(choices.size()+rows-1)/rows);page=Math.max(0,Math.min(page,pages-1));
        clearWidgets();addRenderableWidget(search);
        for(int i=0;i<rows&&page*rows+i<choices.size();i++)addRenderableWidget(new BossRow(listX,y+82+i*30,listW,28,choices.get(page*rows+i)));
        Button previous=addRenderableWidget(Button.builder(Component.literal("<"),b->{page--;rebuild();}).bounds(listX,y+h-68,28,20).build());previous.active=page>0;
        Button next=addRenderableWidget(Button.builder(Component.literal(">"),b->{page++;rebuild();}).bounds(listX+listW-28,y+h-68,28,20).build());next.active=page+1<pages;
        Button chooseNext=addRenderableWidget(Button.builder(Component.literal(state.next()<Species.BOSS_COUNT?"Select next boss":"Campaign complete"),b->selectBoss(state.next())).bounds(listX,y+h-40,listW,22).build());chooseNext.active=state.next()<Species.BOSS_COUNT;
        String action=state.creative()?(state.active()>=0?"Replace active boss":"Summon boss"):
            state.active()>=0?"Encounter active":state.completed(Species.boss(selected))?"Resummon boss":selected==state.next()?"Summon next boss":"Locked";
        Button summon=addRenderableWidget(Button.builder(Component.literal(pending?"Please wait...":action),b->send(BossNetwork.Action.SUMMON)).bounds(detailX,y+h-40,detailW,22).build());
        summon.active=!pending&&state.canSummon(selected);
        if(state.active()>=0&&state.canAbandon()){
            Button end=addRenderableWidget(Button.builder(Component.literal(confirmEnd?"Confirm end (no credit)":"End active encounter"),b->{
                if(confirmEnd)send(BossNetwork.Action.ABANDON);else{confirmEnd=true;rebuild();}
            }).bounds(detailX,y+h-66,detailW,20).build());end.active=!pending;
        }
        addRenderableWidget(Button.builder(Component.literal("X"),b->onClose()).bounds(x+w-30,y+10,18,18).build());
    }
    private void send(BossNetwork.Action action){
        pending=true;pendingTicks=0;feedback="";rebuild();
        BossNetwork.CHANNEL.sendToServer(new BossNetwork.Request(action,selected));
    }
    @Override public void tick(){
        search.tick();age++;
        if(minecraft.player==null||!SummonTool.hasCaller(minecraft.player)){onClose();return;}
        if(pending&&++pendingTicks>=60){pending=false;feedback="The server did not respond. Try again.";rebuild();}
        if(age%40==0&&!pending)BossNetwork.CHANNEL.sendToServer(new BossNetwork.Request(BossNetwork.Action.REFRESH,selected));
    }
    @Override public boolean mouseScrolled(double mx,double my,double amount){
        if(mx>=listX&&mx<listX+listW){page+=amount>0?-1:1;rebuild();return true;}return super.mouseScrolled(mx,my,amount);
    }
    private String status(int stage){
        if(stage==state.active())return "ACTIVE";
        if(state.completed(Species.boss(stage)))return "DEFEATED";
        if(!state.unlocked())return "LOCKED";
        if(stage==state.next())return "NEXT BOSS";
        return state.creative()?"AVAILABLE":"LOCKED";
    }
    private int statusColor(int stage){
        if(stage==state.active())return 0xffecb36c;
        if(state.completed(Species.boss(stage)))return 0xff8bd2b0;
        if(stage==state.next()&&state.unlocked())return 0xffdfc3ff;
        return 0xff9caac2;
    }
    private int wrapped(GuiGraphics g,String text,int left,int top,int maxWidth,int color,int maxLines){
        var lines=font.split(Component.literal(text),maxWidth);int count=0;
        for(var line:lines){if(count++>=maxLines)break;g.drawString(font,line,left,top,color,false);top+=10;}return top;
    }
    @Override public void render(GuiGraphics g,int mx,int my,float partial){
        renderBackground(g);g.fill(x,y,x+w,y+h,0xf20c1423);g.fill(x,y,x+w,y+2,0xffae8bcf);
        g.drawString(font,"THE DEEP NIGHT",x+12,y+12,0xffeadbff,false);
        g.drawString(font,state.defeated().size()+" / "+Species.BOSS_COUNT+" bosses defeated"+(state.creative()?"  |  Creative testing":""),x+12,y+29,0xffb0bfd4,false);
        int progressWidth=w-24;g.fill(x+12,y+44,x+12+progressWidth,y+46,0xff253249);
        g.fill(x+12,y+44,x+12+progressWidth*state.defeated().size()/Species.BOSS_COUNT,y+46,0xffa78bcc);
        g.fill(detailX-7,y+54,detailX-6,y+h-16,0xff2b3a53);
        Species boss=Species.boss(selected);
        g.drawString(font,boss.id.startsWith("dawn_")?"DAWNCRAFT":"MO'BOSSES",detailX,y+55,0xffaebcce,false);
        int titleBottom=wrapped(g,(selected+1)+". "+boss.title,detailX,y+70,detailW,0xfff3edff,3);
        g.drawString(font,status(selected),detailX,titleBottom+3,statusColor(selected),false);
        int infoTop=y+h-(state.active()>=0?130:104);
        int previewTop=titleBottom+19,previewBottom=infoTop-7;
        if(previewBottom-previewTop>28)renderPreview(g,boss,previewTop,previewBottom,mx,my);
        String text;
        if(!feedback.isBlank())text=feedback;
        else if(state.active()>=0)text="Active: "+Species.boss(state.active()).title+"\n"+state.position().getX()+", "+state.position().getY()+", "+state.position().getZ()+"  "+state.dimension().replace("minecraft:","")+"\n"+(state.creative()?"Select any boss to replace this encounter without defeat credit.":state.canAbandon()?"Finish the fight or end it to summon again.":"The summoner can end this encounter.");
        else if(!state.unlocked())text="Defeat the Ender Dragon to unlock this campaign.";
        else if(state.completed(boss))text="Rematch unlocked. Your progress and first-clear rewards will not repeat.";
        else if(selected==state.next())text="Your next challenge. Defeat this boss to unlock the next encounter.";
        else if(state.creative())text="Creative mode: summon any boss. Future bosses do not skip campaign progression.";
        else text="Defeat earlier bosses to unlock this encounter. You can preview every boss here.";
        wrapped(g,text,detailX,infoTop,detailW,feedback.isBlank()?0xffb8c7dc:0xffffc38a,5);
        var choices=filtered();g.drawCenteredString(font,choices.isEmpty()?"No matches":(page+1)+" / "+Math.max(1,(choices.size()+rows-1)/rows),listX+listW/2,y+h-62,0xffaebdd1);
        super.render(g,mx,my,partial);
    }
    private void renderPreview(GuiGraphics g,Species boss,int top,int bottom,int mx,int my){
        if(minecraft.level==null)return;
        LivingEntity entity=previews.computeIfAbsent(selected,i->{var mob=DeepNight.HOSTILES.get(boss).get().create(minecraft.level);mob.setId(70000+i);mob.setNoAi(true);return mob;});
        entity.tickCount=age+50;
        float factor=switch(boss){
            case DAWN_DRAGON_LORD,DAWN_FIRE_DRAGON->0.45f;
            case DAWN_ICE_DRAGON,DAWN_LIGHTNING_DRAGON->0.55f;
            case DAWN_ENT,MOBOSSES_NIL->1;
            case MOBOSSES_DIVINE_DRAGON->0.95f;
            default->1.4f;
        };
        float room=Math.min((bottom-top)*0.65f,detailW*0.42f);
        int scale=Math.max(1,Math.round(room/Math.max(entity.getBbHeight(),2)*factor));
        g.enableScissor(detailX,top,detailX+detailW,bottom);
        InventoryScreen.renderEntityInInventoryFollowsMouse(g,detailX+detailW/2,bottom-5,scale,-35,0,entity);
        g.disableScissor();
    }
    private final class BossRow extends Button {
        private final int stage;
        BossRow(int left,int top,int width,int height,int stage){
            super(left,top,width,height,Component.literal(Species.boss(stage).title),b->{selected=stage;feedback="";rebuild();},DEFAULT_NARRATION);this.stage=stage;
        }
        @Override protected void renderWidget(GuiGraphics g,int mx,int my,float partial){
            boolean chosen=selected==stage;g.fill(getX(),getY(),getX()+getWidth(),getY()+getHeight(),chosen?0xff34415b:isHoveredOrFocused()?0xff28374e:0xff1a2639);
            if(chosen)g.fill(getX(),getY(),getX()+3,getY()+getHeight(),0xffc3a1ed);
            String title=(stage+1)+". "+Species.boss(stage).title;
            if(font.width(title)>getWidth()-15)title=font.plainSubstrByWidth(title,getWidth()-24)+"...";
            g.drawString(font,title,getX()+8,getY()+4,0xfff1ecf8,false);
            g.drawString(font,status(stage),getX()+8,getY()+16,statusColor(stage),false);
        }
    }
}
