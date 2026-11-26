package dev.ashenquests.client;

import dev.ashenquests.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.lwjgl.glfw.GLFW;
import java.util.*;
import java.util.List;

/** Native GUI: scalable text, separately scrollable contents and objectives, no texture dependencies. */
public final class JournalScreen extends Screen {
    private static final int INK=0xFFE2E6DB,GOLD=0xFFE1BE78,MUTED=0xFF94A69E,GREEN=0xFF85CCAB;
    private static final String[] FILTERS={"All entries","Available","Ready to turn in","Completed","Missing content"};
    private EditBox search,answer;
    private Button chapterButton,filterButton,claimButton,pinButton,choiceButton;
    private String selected="",query="",chapter="All chapters";
    private int filter,listOffset,detailOffset,choice=-1,ticks,left,right,detailWidth,maxDetail;
    private boolean factions;
    private List<Quest> visible=List.of();
    public JournalScreen(){super(Component.literal("The Ashen Covenant"));}
    @Override protected void init() {
        String response=answer==null?"":answer.getValue();
        left=Math.max(130,Math.min(228,(width-40)*34/100));right=left+32;detailWidth=width-right-20;
        search=addRenderableWidget(new EditBox(font,18,49,left-4,20,Component.literal("Search quests")));
        search.setHint(Component.literal(font.plainSubstrByWidth("Search quests...",left-16)));search.setMaxLength(120);search.setValue(query);search.setResponder(s->{query=s;listOffset=0;refresh();});
        chapterButton=button(18,74,left-4,18,"",b->cycleChapter(1));
        filterButton=button(18,97,left-4,18,FILTERS[filter],b->{filter=(filter+1)%FILTERS.length;listOffset=0;refresh();});
        button(width-151,14,65,20,"Journal",b->{factions=false;detailOffset=0;});
        button(width-81,14,63,20,"Factions",b->{factions=true;detailOffset=0;});
        answer=addRenderableWidget(new EditBox(font,right,height-65,detailWidth,18,Component.literal("Journal response")));
        answer.setMaxLength(256);answer.setValue(response);answer.setHint(Component.literal(font.plainSubstrByWidth("Enter the journal phrase...",detailWidth-12)));
        choiceButton=button(right,height-89,detailWidth,18,"Choose a response",b->{Quest q=selected();if(q!=null&&!q.choices().isEmpty())choice=(choice+1)%q.choices().size();});
        pinButton=button(right,height-40,Math.min(96,detailWidth/3),20,"Track",b->{if(selected()!=null)ClientJournal.action(2,selected,"",-1);});
        claimButton=button(right+Math.min(96,detailWidth/3)+6,height-40,detailWidth-Math.min(96,detailWidth/3)-6,20,"Claim reward",b->claim());
        refresh();
    }
    private Button button(int x,int y,int w,int h,String text,java.util.function.Consumer<Button> action){return addRenderableWidget(Button.builder(Component.literal(text),action::accept).bounds(x,y,w,h).build());}
    public Quest selected(){return ClientJournal.quests.get(selected);}
    public void select(String id){selected=id;detailOffset=0;choice=-1;if(answer!=null)answer.setValue("");ClientJournal.message="";}
    public void query(String value){search.setValue(value);}
    public void showFactions(){factions=true;detailOffset=0;}
    public int resultCount(){refresh();return visible.size();}
    public void loaded(){refresh();}
    private void cycleChapter(int direction){var chapters=new ArrayList<String>();chapters.add("All chapters");ClientJournal.quests.values().stream().map(Quest::chapter).distinct().forEach(chapters::add);int i=chapters.indexOf(chapter);chapter=chapters.get(Math.floorMod(i+direction,chapters.size()));listOffset=0;refresh();}
    public void refresh(){
        String text=query.toLowerCase(Locale.ROOT);
        visible=ClientJournal.quests.values().stream().filter(q->ClientJournal.meta==null||ClientJournal.meta.giver().isEmpty()||q.giverId().equals(ClientJournal.meta.giver())).filter(q->chapter.equals("All chapters")||q.chapter().equals(chapter)).filter(q->(q.title()+" "+q.giverName()+" "+q.story()+" "+q.section()+" "+q.id()+" "+q.objectives()).toLowerCase(Locale.ROOT).contains(text)).filter(q->{var v=ClientJournal.views.get(q.id());if(v==null)return filter==0;return switch(filter){case 1->v.status()==QuestEngine.OFFERED||v.status()==QuestEngine.ACTIVE||v.status()==QuestEngine.READY;case 2->v.status()==QuestEngine.READY;case 3->v.claims()>0;case 4->v.status()==QuestEngine.UNAVAILABLE;default->true;};}).toList();
        if(selected.isEmpty()&&!visible.isEmpty())select(visible.stream().filter(q->{var v=ClientJournal.views.get(q.id());return v!=null&&(v.status()==1||v.status()==2||v.status()==6);}).findFirst().orElse(visible.get(0)).id());
        listOffset=Math.max(0,Math.min(listOffset,Math.max(0,visible.size()-rows())));
        if(chapterButton!=null){chapterButton.setMessage(Component.literal(font.plainSubstrByWidth(chapter,left-26)+" >"));chapterButton.setTooltip(Tooltip.create(Component.literal(chapter+"\nClick for next chapter; right-click for previous.")));filterButton.setMessage(Component.literal(FILTERS[filter]));}
    }
    private int rows(){return Math.max(1,(height-151)/36);}
    public void claim(){Quest q=selected();if(q!=null){var v=ClientJournal.views.get(q.id());ClientJournal.action(v!=null&&v.status()==QuestEngine.OFFERED?4:1,q.id(),answer.getValue(),choice);}}
    @Override public void tick(){if(++ticks%40==0&&!ClientJournal.loading)ClientJournal.action(0,"","",-1);if(ticks%20==0)refresh();}
    @Override public boolean isPauseScreen(){return false;}
    @Override public void onClose(){ClientJournal.action(3,"","",-1);super.onClose();}
    @Override public boolean keyPressed(int key,int scan,int modifiers){if(key==GLFW.GLFW_KEY_ESCAPE||(ClientEvents.OPEN.matches(key,scan)&&!search.isFocused()&&!answer.isFocused())){onClose();return true;}return super.keyPressed(key,scan,modifiers);}
    @Override public boolean mouseScrolled(double x,double y,double delta){if(x<right){listOffset=Math.max(0,Math.min(Math.max(0,visible.size()-rows()),listOffset-(int)(delta*3)));}else{detailOffset=Math.max(0,Math.min(maxDetail,detailOffset-(int)(delta*28)));}return true;}
    @Override public boolean mouseClicked(double x,double y,int button){
        if(button==1&&x>=18&&x<left+14&&y>=74&&y<92){cycleChapter(-1);return true;}
        if(button==0&&x>=18&&x<left+14&&y>=123&&y<123+rows()*36){int idx=listOffset+(int)(y-123)/36;if(idx<visible.size()){select(visible.get(idx).id());factions=false;return true;}}
        return super.mouseClicked(x,y,button);
    }
    public static String status(QuestEngine.View v){return v==null?"Loading":switch(v.status()){case 0->"Locked";case 1->"In progress";case 2->"Return to quest giver";case 6->"Speak to quest giver";case 3->"Completed";case 4->"Renews in "+v.remaining()/60+"m "+v.remaining()%60+"s";default->"Missing content";};}
    @Override public void render(GuiGraphics g,int mx,int my,float partial){
        g.fill(0,0,width,height,0xF20B1315);g.fill(10,42,left+22,height-20,0xFF142223);g.fill(right-8,42,width-10,height-20,0xFF1B2929);
        g.fill(10,40,width-10,41,GOLD);g.drawString(font,width<500?"ASHEN COVENANT":"THE ASHEN COVENANT",18,14,GOLD,false);g.drawString(font,font.plainSubstrByWidth(ClientJournal.meta!=null&&!ClientJournal.meta.giver().isEmpty()?"CONVERSATION / "+ClientJournal.meta.giver().toUpperCase(Locale.ROOT):"JOURNAL / Speak to quest givers to accept and turn in",width-187),18,28,MUTED,false);
        Quest q=selected();var v=q==null?null:ClientJournal.views.get(q.id());
        boolean inPerson=q!=null&&ClientJournal.meta!=null&&ClientJournal.meta.conversation()&&q.giverId().equals(ClientJournal.meta.giver());
        boolean offered=v!=null&&v.status()==QuestEngine.OFFERED;
        boolean field=q!=null&&q.objectives().stream().anyMatch(o->o.type().equals("field"));boolean choose=q!=null&&!q.choices().isEmpty();
        answer.visible=!factions&&field&&inPerson&&!offered;answer.active=v!=null&&v.status()==QuestEngine.READY;
        choiceButton.visible=!factions&&choose&&inPerson&&!offered;choiceButton.active=v!=null&&v.status()==QuestEngine.READY;
        if(choose)choiceButton.setMessage(Component.literal(font.plainSubstrByWidth(choice<0?"Choose a response >":q.choices().get(choice).title()+" >",detailWidth-12)));
        claimButton.visible=pinButton.visible=!factions&&q!=null;
        claimButton.active=inPerson&&!ClientJournal.loading&&!ClientJournal.pending&&v!=null&&(offered||(v.status()==QuestEngine.READY&&(!field||!answer.getValue().isBlank())&&(!choose||choice>=0)));
        String actionText=ClientJournal.pending?"Please wait...":!inPerson?"Speak to "+(q==null?"quest giver":q.giverName()):offered?"Accept quest":"Turn in: "+(q==null?0:q.emeralds())+" emeralds";
        claimButton.setMessage(Component.literal(font.plainSubstrByWidth(actionText,claimButton.getWidth()-10)));
        claimButton.setTooltip(Tooltip.create(Component.literal(actionText)));
        pinButton.active=!ClientJournal.loading;pinButton.setMessage(Component.literal(ClientJournal.meta!=null&&selected.equals(ClientJournal.meta.pinned())?"Untrack":"Track"));
        if(ClientJournal.loading)g.drawString(font,"Opening your chronicle...",right,54,GOLD,false);
        else if(factions)renderFactions(g);
        else if(q!=null)renderQuest(g,q,v);
        for(int row=0;row<rows()&&row+listOffset<visible.size();row++){
            var entry=visible.get(row+listOffset);var state=ClientJournal.views.get(entry.id());int y=123+row*36;
            boolean chosen=entry.id().equals(selected);g.fill(17,y,left+15,y+33,chosen?0xFF304842:0xFF192C2C);if(chosen)g.fill(17,y,19,y+33,GOLD);
            int color=state!=null&&state.status()==QuestEngine.READY?GREEN:state!=null&&state.status()==QuestEngine.CLAIMED?MUTED:INK;
            g.drawString(font,font.plainSubstrByWidth(entry.title(),left-17),24,y+5,color,false);
            g.drawString(font,font.plainSubstrByWidth(status(state)+"  |  "+entry.emeralds()+" emeralds",left-17),24,y+19,MUTED,false);
        }
        if(visible.isEmpty()&&!ClientJournal.loading)g.drawString(font,"No matching quests",23,131,MUTED,false);
        long done=ClientJournal.views.values().stream().filter(s->s.claims()>0).count();
        g.drawString(font,font.plainSubstrByWidth(done+" / "+ClientJournal.quests.size()+(width>520?" completed  |  "+visible.size()+" shown":" done"),width>520?right-24:left),18,height-14,MUTED,false);
        super.render(g,mx,my,partial);
        if(!ClientJournal.message.isEmpty())g.drawString(font,font.plainSubstrByWidth(ClientJournal.message,detailWidth),right,height-14,ClientJournal.success?GREEN:0xFFFFAE8E,false);
    }
    private void renderQuest(GuiGraphics g,Quest q,QuestEngine.View v){
        int y=51;g.drawString(font,font.plainSubstrByWidth(q.title(),detailWidth),right,y,GOLD,false);g.drawString(font,font.plainSubstrByWidth(status(v)+"  /  "+q.faction(),detailWidth),right,y+15,GREEN,false);
        int end=height-(q.choices().isEmpty()?74:98);if(q.objectives().stream().noneMatch(o->o.type().equals("field"))&&q.choices().isEmpty())end=height-48;
        g.enableScissor(right,86,width-18,end);int cy=88-detailOffset;
        cy=paragraph(g,"QUEST GIVER: "+q.giverName(),cy,GOLD);cy=paragraph(g,"Speak in person to accept and turn in. Press - anywhere to track progress.",cy,MUTED);cy+=5;cy=paragraph(g,q.section(),cy,MUTED);cy+=5;cy=paragraph(g,q.story(),cy,INK);cy+=12;
        cy=paragraph(g,"OBJECTIVES",cy,GOLD);
        for(int i=0;i<q.objectives().size();i++){
            var o=q.objectives().get(i);boolean completed=v!=null&&(v.status()==QuestEngine.CLAIMED||v.progress()[i]>=o.count());int progress=v==null?0:v.progress()[i];
            cy=paragraph(g,(completed?"[+] ":"[ ] ")+o.label()+(o.type().equals("field")?"":"  "+(completed?o.count():progress)+" / "+o.count()),cy,completed?GREEN:INK);
            if(o.type().equals("talk")) {
                cy=paragraph(g,"Right-click the named NPC, choose Discuss a quest, then this quest's topic. Complete earlier objectives first.",cy,MUTED);
                if(progress>0&&o.dialogue()!=null)cy=paragraph(g,o.dialogue(),cy,INK);
            }
            if(!o.type().equals("field")){g.fill(right,cy, right+detailWidth,cy+3,0xFF101C1C);g.fill(right,cy,right+(int)((long)detailWidth*(completed?o.count():progress)/o.count()),cy+3,GREEN);cy+=9;}
        }
        cy+=9;cy=paragraph(g,"REWARDS",cy,GOLD);cy=paragraph(g,q.emeralds()+" emeralds  |  "+q.xp()+" XP  |  +"+q.standing()+" "+q.faction()+" standing",cy,GREEN);
        for(var reward:q.rewards()) {
            var key=new net.minecraft.resources.ResourceLocation(reward.item());boolean exists=net.minecraftforge.registries.ForgeRegistries.ITEMS.containsKey(key);
            String name=exists?net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(key).getDescription().getString():reward.item();
            cy=paragraph(g,"+ "+reward.count()+" "+name+(exists?"":" (mod absent; bonus omitted)"),cy,MUTED);
        }
        if(q.repeatable())cy=paragraph(g,"Repeatable every "+q.cooldown()/60+" minutes of world play time. Donations are consumed again; standing is not farmable.",cy,MUTED);
        if(q.objectives().stream().anyMatch(o->o.type().equals("submit")))cy=paragraph(g,"Donation items are consumed when you claim. Collection items stay yours. All rewards must fit in your inventory.",cy,MUTED);
        if(v!=null&&!v.reason().isBlank()){cy+=9;cy=paragraph(g,"REQUIREMENTS",cy,GOLD);cy=paragraph(g,v.reason(),cy,0xFFFFBC94);}
        if(!q.requires().isEmpty()){cy+=9;cy=paragraph(g,"FOLLOWS",cy,GOLD);for(String id:q.requires()){Quest dep=ClientJournal.quests.get(id);var dv=ClientJournal.views.get(id);cy=paragraph(g,(dv!=null&&dv.claims()>0?"[+] ":"[ ] ")+(dep==null?id:dep.title()),cy,MUTED);}}
        if(v!=null&&v.claims()>0){cy+=12;if(!q.aftermath().isBlank()){cy=paragraph(g,"THE NEXT PAGE",cy,GOLD);cy=paragraph(g,q.aftermath(),cy,INK);}if(v.choice()>=0&&v.choice()<q.choices().size()){cy=paragraph(g,"YOUR DECISION: "+q.choices().get(v.choice()).title(),cy,GREEN);cy=paragraph(g,q.choices().get(v.choice()).result(),cy,INK);}cy=paragraph(g,"Claimed "+v.claims()+" time(s).",cy,MUTED);}
        maxDetail=Math.max(0,cy+detailOffset-end+10);g.disableScissor();scrollbar(g,86,end,maxDetail);
    }
    private int paragraph(GuiGraphics g,String text,int y,int color){for(String line:text.split("\n",-1)){if(line.isBlank()){y+=7;continue;}for(FormattedCharSequence part:font.split(Component.literal(line),detailWidth-8)){g.drawString(font,part,right,y,color,false);y+=12;}}return y;}
    private void scrollbar(GuiGraphics g,int start,int end,int max){if(max<=0)return;int h=Math.max(10,(end-start)*(end-start)/(max+end-start));int pos=start+(end-start-h)*Math.min(detailOffset,max)/max;g.fill(width-16,start,width-14,end,0xFF101C1C);g.fill(width-16,pos,width-14,pos+h,GOLD);}
    private void renderFactions(GuiGraphics g){int end=height-29;g.enableScissor(right,49,width-18,end);int y=53-detailOffset;y=paragraph(g,"THE FREE PROVINCES",y,GOLD);y=paragraph(g,"Your first completion of a quest builds trust with its faction. Repeatable work pays emeralds without granting unlimited standing. Ranks: Ally 25, Trusted 100, Steward 250, Luminary 500.",y,INK);y+=12;if(ClientJournal.meta!=null)for(var entry:ClientJournal.meta.standing().entrySet()){y=paragraph(g,entry.getKey()+"  /  "+QuestEngine.rank(entry.getValue()),y,GOLD);y=paragraph(g,entry.getValue()+" standing",y,GREEN);String lore=switch(entry.getKey()){case "Dawnstead"->"Rowan and Tamsin keep the refuge fed, housed and free to disagree.";case "Hearthwrights"->"Mara builds workshops whose makers no longer need a royal seal.";case "Archivists"->"Sera preserves evidence, including accounts that challenge her own.";case "Free Watch"->"Elian teaches the watch to protect people and question its orders.";default->"Iona and Fen keep roads open in both directions.";};y=paragraph(g,lore,y,INK);y+=16;}maxDetail=Math.max(0,y+detailOffset-end+8);g.disableScissor();scrollbar(g,49,end,maxDetail);}
}

