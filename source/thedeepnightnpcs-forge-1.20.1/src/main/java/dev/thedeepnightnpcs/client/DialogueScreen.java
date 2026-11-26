package dev.thedeepnightnpcs.client;

import dev.thedeepnightnpcs.entity.NpcEntity;
import dev.thedeepnightnpcs.network.NpcNetwork;
import dev.thedeepnightnpcs.quest.HuntData;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.glfw.GLFW;
import java.util.*;

public final class DialogueScreen extends Screen {
    public final int entityId;
    public final String kind;
    public final DialogueLibrary library;
    private NpcNetwork.View view;
    private String nodeId = "root", heading = "", body = "";
    private final Deque<String> history = new ArrayDeque<>();
    private List<Choice> choices = new ArrayList<>();
    private List<FormattedCharSequence> lines = List.of();
    private int page, scroll, selected = -1;
    private float scale = 1;
    private double ox, oy;
    private record Choice(String label, Runnable select) {}
    private static final int BG = 0xf20d111e, PANEL = 0xe9182133, GOLD = 0xffe2c28a, TEXT = 0xffe7e5e0, MUTED = 0xff9caebc;
    private static final Map<Integer,List<List<int[]>>> SECTORS = new HashMap<>();
    private static List<List<int[]>> sectors(int count) {
        return SECTORS.computeIfAbsent(count, size -> {
            List<List<int[]>> result=new ArrayList<>();
            for (int i=0;i<size;i++) {
                List<int[]> spans=new ArrayList<>(); double middle=-Math.PI/2+2*Math.PI*i/size, half=Math.PI/size;
                for (int row=-156;row<=156;row++) {
                    int start=1000;
                    for (int col=-156;col<=157;col++) {
                        double radius=col*col+row*row, a=Math.atan2(row,col)-middle; a=Math.atan2(Math.sin(a),Math.cos(a));
                        boolean inside=col<157 && radius>=64*64 && radius<=156*156 && Math.abs(a)<half-0.025;
                        if (inside && start==1000) start=col;
                        if (!inside && start!=1000) {spans.add(new int[]{start,row,col});start=1000;}
                    }
                }
                result.add(spans);
            }
            return result;
        });
    }
    public DialogueScreen(NpcNetwork.View view) {
        super(Component.literal("The Deep Night")); this.view=view; entityId=view.entityId(); kind=view.kind(); library=DialogueLibrary.load(kind);
    }
    @Override protected void init() { layout(); show(nodeId, false); }
    private void layout() { scale=Math.min(width/800F,height/450F); ox=(width-800*scale)/2; oy=(height-450*scale)/2; }
    public void update(NpcNetwork.View packet) {
        view=packet;
        if (nodeId.startsWith("$")) {
            if (nodeId.equals("$board")) board();
            else if(nodeId.equals("$quest_topics")) questTopics();
            else if(nodeId.equals("$ingots")||nodeId.equals("$books")) shop(nodeId.equals("$books"));
            else ledger();
        }
        if (!packet.message().isBlank()) { body=packet.message()+"\n\n"+body; wrap(); }
    }
    public void show(String id, boolean remember) {
        if (remember) history.push(nodeId);
        nodeId=id; page=0; scroll=0; selected=-1;
        if (id.equals("$board")) { board(); return; }
        if (id.equals("$ledger")) { ledger(); return; }
        if (id.equals("$quest_topics")) { questTopics(); return; }
        if (id.equals("$ingots")||id.equals("$books")) {shop(id.equals("$books"));return;}
        var node=library.nodes.getOrDefault(id,library.nodes.get("root"));
        heading=node.title(); body=node.text(); choices=new ArrayList<>();
        if (id.equals("root") && view.state().getBoolean("covenantQuests")) {
            choices.add(new Choice(kind.equals("eldara") ? "Main quests" : "Covenant quests", () -> send("covenant_quests", "")));
        }
        if (id.equals("root") && !view.state().getList("questTopics",10).isEmpty()) {
            choices.add(new Choice("Discuss a quest", () -> show("$quest_topics",true)));
        }
        if (kind.equals("caelen") && id.equals("root")) {
            choices.add(new Choice("Available hunts", ()->show("$board",true)));
            choices.add(new Choice("My contract", ()->{ show("$ledger",true); send("status",""); }));
        }
        if(kind.equals("jorge")&&id.equals("root")) {
            choices.add(new Choice("Buy ingots",()->show("$ingots",true)));
            choices.add(new Choice("Buy books",()->show("$books",true)));
            choices.add(new Choice("Enchant gear",()->send("jorge_enchant","")));
        }
        for (String child:node.children()) choices.add(new Choice(library.nodes.get(child).title(),()->show(child,true)));
        wrap();
    }
    private void questTopics() {
        nodeId="$quest_topics";heading="Reports and questions";page=0;scroll=0;choices=new ArrayList<>();
        body="Choose the assignment you want to discuss. These conversations belong to quests you have accepted. Complete earlier objectives first when a report is not yet available.";
        var topics=view.state().getList("questTopics",10);
        for(int i=0;i<topics.size();i++) {
            var topic=topics.getCompound(i);String request=topic.getString("request");
            choices.add(new Choice(topic.getString("title"),()->send("covenant_talk",request)));
            body+="\n\n"+topic.getString("title")+"\n"+topic.getString("subject");
        }
        if(topics.isEmpty())body="We have discussed every report available here. Check your journal for the next person to visit, or return to your quest giver when all objectives are complete.";
        wrap();
    }
    private HuntData.Record record() { return HuntData.Record.load(view.state()); }
    private void shop(boolean books) {
        nodeId=books?"$books":"$ingots";heading=books?"The enchanted-book shelf":"The ingot counter";page=0;scroll=0;
        MerchantOffers offers=new MerchantOffers(view.state().getCompound(books?"books":"ingots"));
        StringBuilder text=new StringBuilder(books?"A good inscription is worth keeping. Each book has its enchantment and level listed before purchase.\n\n":"Choose the metal that suits your next project. Each offer shows the exact ingot, quantity, and emerald price.\n\n");
        for(var offer:offers) {
            String name=offer.getResult().getHoverName().getString();
            if(books)name=String.join(", ",EnchantmentHelper.getEnchantments(offer.getResult()).entrySet().stream().map(e->e.getKey().getFullname(e.getValue()).getString()).toList());
            text.append(offer.getResult().getCount()).append(" x ").append(name).append("\n").append(offer.getCostA().getCount()).append(" emeralds | ").append(Math.max(0,offer.getMaxUses()-offer.getUses())).append(" trades left\n\n");
        }
        text.append("Refresh available in ").append(view.state().getInt(books?"bookSeconds":"ingotSeconds")).append(" seconds at last update.\nClose the shop and speak to me again to refresh this stock. Refreshing is free and refills the new offers.");
        if(offers.isEmpty())text.insert(0,"There is no eligible stock in this world.\n\n");
        body=text.toString();choices=new ArrayList<>();
        if(!offers.isEmpty())choices.add(new Choice("Browse stock",()->send(books?"jorge_books":"jorge_ingots","")));
        choices.add(new Choice("Refresh stock",()->send(books?"refresh_books":"refresh_ingots","")));
        choices.add(new Choice(books?"Ingot counter":"Book shelf",()->show(books?"$ingots":"$books",true)));
        wrap();
    }
    private String mobName(String target) {
        var type=ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(target));
        return type==null ? target : type.getDescription().getString();
    }
    private String contractText(HuntData.Contract c) {
        return c.style()+"\nTarget: "+mobName(c.target())+"\nRegistry: "+c.target()+"\nRequired kills: "+c.required()+"\nReward: "+c.emeralds()+" emeralds + "+c.xp()+" XP\n\n"+
            "The scouts report a threat to the roads. Track this creature in its own territory and reduce its numbers. You choose where to hunt; this contract does not summon targets or reveal their location.\n\nReadiness comes first. You can abandon a hunt without penalty, but partial kills earn no reward.";
    }
    private void board() {
        nodeId="$board"; heading="The hunting board"; scroll=0; page=0;
        var r=record();
        body="I have reports from across the frontier. Choose a contract to read its target and payment before committing.\n\n"+
            "Eligible hostile types: "+view.state().getInt("targetCount")+"\nCompleted contracts: "+r.completed+
            "\nNew reports: "+view.state().getInt("refreshSeconds")+" seconds at last update\n\n"+
            (r.active==null ? "You have room for one active contract." : "You already have an active contract. Finish or abandon it before taking another.");
        choices=new ArrayList<>();
        for (var c:r.offers) choices.add(new Choice(mobName(c.target()),()->offer(c)));
        choices.add(new Choice("New reports",()->send("refresh","")));
        choices.add(new Choice("My contract",()->show("$ledger",true))); wrap();
    }
    private void offer(HuntData.Contract c) {
        heading=c.style(); body=contractText(c); scroll=0; choices=new ArrayList<>();
        if (record().active==null) choices.add(new Choice("Accept hunt",()->send("accept",c.id().toString())));
        choices.add(new Choice("Other reports",this::board)); wrap();
    }
    private void ledger() {
        nodeId="$ledger"; heading="Your hunting ledger"; scroll=0; page=0; choices=new ArrayList<>(); var r=record();
        if (r.active==null) { body="Your ledger is clear. Read the board when you are ready for another hunt.\n\nCompleted contracts: "+r.completed; }
        else {
            body="Progress: "+r.kills+" / "+r.active.required()+"\n\n"+contractText(r.active);
            if (r.kills>=r.active.required()) choices.add(new Choice("Claim reward",()->send("claim","")));
            choices.add(new Choice("Abandon hunt",()->{
                heading="Leave this contract?"; body="Abandoning removes all progress on this hunt. You receive no payment and can immediately accept a different report.";
                choices=new ArrayList<>(List.of(new Choice("Yes, abandon",()->send("abandon","")),new Choice("Keep hunting",this::ledger))); scroll=0; wrap();
            }));
        }
        choices.add(new Choice("Available hunts",()->show("$board",true))); wrap();
    }
    private void wrap() {
        if (font==null) return;
        List<FormattedCharSequence> result=new ArrayList<>();
        for (String paragraph:body.split("\n",-1)) {
            if (paragraph.isEmpty()) result.add(FormattedCharSequence.EMPTY);
            else result.addAll(font.split(Component.literal(paragraph),362));
        }
        lines=result; scroll=Math.min(scroll,Math.max(0,lines.size()-20));
    }
    private void send(String action,String value) { NpcNetwork.CHANNEL.sendToServer(new NpcNetwork.Action(entityId,action,value)); }
    private void back() { if (!history.isEmpty()) show(history.pop(),false); else show("root",false); }
    private int pages() { return Math.max(1,(choices.size()+7)/8); }
    private List<Choice> visible() { return choices.subList(Math.min(page*8,choices.size()),Math.min(page*8+8,choices.size())); }
    private double mx(double x) { return (x-ox)/scale; }
    private double my(double y) { return (y-oy)/scale; }
    private int hovered(double x,double y) {
        double dx=x-198,dy=y-224,d=Math.sqrt(dx*dx+dy*dy); int count=visible().size();
        if (count==0 || d<62 || d>157) return -1;
        double angle=(Math.atan2(dy,dx)+Math.PI/2+Math.PI/count+Math.PI*2)%(Math.PI*2);
        return Math.min(count-1,(int)(angle/(Math.PI*2/count)));
    }
    @Override public void render(GuiGraphics g,int mouseX,int mouseY,float partial) {
        renderBackground(g); layout(); double x=mx(mouseX),y=my(mouseY); int hover=hovered(x,y);
        g.pose().pushPose(); g.pose().translate(ox,oy,0); g.pose().scale(scale,scale,1);
        g.fill(8,8,792,442,BG); g.fill(8,8,792,10,GOLD);
        g.drawString(font,"THE DEEP NIGHT  /  "+dev.thedeepnightnpcs.NpcRoster.title(kind).toUpperCase(Locale.ROOT),24,25,GOLD,false);
        g.drawString(font,"A conversation with "+dev.thedeepnightnpcs.NpcRoster.name(kind),24,43,MUTED,false);
        g.fill(385,65,776,390,PANEL); g.drawString(font,font.plainSubstrByWidth(heading,355),399,79,GOLD,false); g.fill(399,95,762,96,0xff48515b);
        for (int i=0;i<20 && i+scroll<lines.size();i++) g.drawString(font,lines.get(i+scroll),399,108+i*13,TEXT,false);
        if (lines.size()>20) {
            int thumb=Math.max(12,260*20/lines.size()), top=108+(260-thumb)*scroll/Math.max(1,lines.size()-20);
            g.fill(767,108,769,368,0xff303a49); g.fill(767,top,769,top+thumb,GOLD);
            g.drawString(font,"Scroll to read  "+(scroll+1)+" / "+lines.size(),400,376,MUTED,false);
        }
        List<Choice> shown=visible(); int count=shown.size();
        if (count>0) for (int i=0;i<count;i++) {
            double middle=-Math.PI/2+2*Math.PI*i/count, half=Math.PI/count;
            int color=i==hover || i==selected ? 0xff725e43 : 0xff253148;
            for (int[] span:sectors(count).get(i)) g.fill(198+span[0],224+span[1],198+span[2],225+span[1],color);
            int tx=198+(int)(Math.cos(middle)*112),ty=224+(int)(Math.sin(middle)*112);
            var labels=font.split(Component.literal((i+1)+". "+shown.get(i).label()),80);
            for (int j=0;j<Math.min(3,labels.size());j++) g.drawCenteredString(font,labels.get(j),tx,ty-5*labels.size()+j*10,TEXT);
        }
        if (minecraft.level!=null && minecraft.level.getEntity(entityId) instanceof NpcEntity npc) {
            InventoryScreen.renderEntityInInventoryFollowsMouse(g,198,268,59,0,0,npc);
        }
        if (count==0) g.drawCenteredString(font,"Take a moment to listen.",198,318,MUTED);
        g.drawCenteredString(font,dev.thedeepnightnpcs.NpcRoster.name(kind),198,288,GOLD);
        if (pages()>1) {
            button(g,55,378,140,399,"< Previous",x,y); button(g,257,378,342,399,"Next >",x,y);
            g.drawCenteredString(font,(page+1)+" / "+pages(),198,385,GOLD);
        }
        button(g,24,411,110,432,"Back",x,y); button(g,118,411,204,432,"Topics",x,y); button(g,686,411,776,432,"Farewell",x,y);
        g.drawCenteredString(font,"1-8 choose  |  Left/Right pages  |  Backspace back  |  Esc close",444,417,MUTED);
        g.pose().popPose();
    }
    private void button(GuiGraphics g,int x,int y,int right,int bottom,String label,double mx,double my) {
        g.fill(x,y,right,bottom,mx>=x&&mx<=right&&my>=y&&my<=bottom?0xff635740:0xff273347); g.drawCenteredString(font,label,(x+right)/2,y+6,TEXT);
    }
    private void choose(int index) {
        if (index<0 || index>=visible().size()) return;
        minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK,0.9F)); visible().get(index).select().run();
    }
    @Override public boolean mouseClicked(double x,double y,int button) {
        x=mx(x); y=my(y);
        if (button==1) { back(); return true; }
        if (button!=0) return false;
        if (y>=411&&y<=432) {
            if (x>=24&&x<=110) back(); else if (x>=118&&x<=204) {history.clear();show("root",false);} else if(x>=686&&x<=776) onClose(); return true;
        }
        if (y>=378&&y<=399&&pages()>1) { if(x>=55&&x<=140)page=(page+pages()-1)%pages(); if(x>=257&&x<=342)page=(page+1)%pages(); return true; }
        choose(hovered(x,y)); return true;
    }
    @Override public boolean mouseScrolled(double x,double y,double delta) {
        if (mx(x)>385) scroll=Math.max(0,Math.min(Math.max(0,lines.size()-20),scroll-(int)(delta*3)));
        else if (pages()>1) page=(page+(delta<0?1:pages()-1))%pages(); return true;
    }
    @Override public boolean keyPressed(int key,int scan,int mods) {
        if (key>=GLFW.GLFW_KEY_1&&key<=GLFW.GLFW_KEY_8) {choose(key-GLFW.GLFW_KEY_1);return true;}
        if (key==GLFW.GLFW_KEY_BACKSPACE) {back();return true;}
        if (key==GLFW.GLFW_KEY_RIGHT||key==GLFW.GLFW_KEY_LEFT) {page=(page+(key==GLFW.GLFW_KEY_RIGHT?1:pages()-1))%pages();return true;}
        if (key==GLFW.GLFW_KEY_DOWN||key==GLFW.GLFW_KEY_UP) {scroll=Math.max(0,Math.min(Math.max(0,lines.size()-20),scroll+(key==GLFW.GLFW_KEY_DOWN?3:-3)));return true;}
        return super.keyPressed(key,scan,mods);
    }
    @Override public void tick() {
        if (minecraft.player==null || minecraft.level==null || !(minecraft.level.getEntity(entityId) instanceof NpcEntity npc) || !npc.isAlive() || !minecraft.player.isAlive() || minecraft.player.distanceToSqr(npc)>64) onClose();
    }
    @Override public boolean isPauseScreen() { return false; }
    @Override public void onClose() { if(minecraft.player!=null)send("close",""); super.onClose(); }
}
