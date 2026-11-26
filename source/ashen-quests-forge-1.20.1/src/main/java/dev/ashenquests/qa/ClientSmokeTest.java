package dev.ashenquests.qa;

import dev.ashenquests.*;
import dev.ashenquests.client.*;
import dev.thedeepnightnpcs.*;
import dev.thedeepnightnpcs.client.DialogueScreen;
import net.minecraft.client.*;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import java.nio.file.*;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid=Chronicles.ID,value=Dist.CLIENT)
public final class ClientSmokeTest {
    private static boolean started;
    private static int step,ticks,overall,eldara,portrait;
    private static final int[] companions=new int[8];
    private static CompletableFuture<Void> work;
    private static JournalScreen screen;
    private static long claimToken;
    private static int claimSequence;
    private static void next(int target){step=target;ticks=0;}
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent e){
        if(!Boolean.getBoolean("ashenquests.smokeTest")||e.phase!=TickEvent.Phase.END||step==99)return;
        var mc=Minecraft.getInstance();
        try{
            if(++overall>10000)throw new IllegalStateException("Timeout at step "+step);
            if(!started&&mc.screen instanceof TitleScreen&&mc.getOverlay()==null){
                started=true;mc.options.pauseOnLostFocus=false;GLFW.glfwHideWindow(mc.getWindow().getWindow());mc.options.guiScale().set(2);mc.options.renderDistance().set(2);mc.resizeDisplay();
                mc.getTutorial().setStep(net.minecraft.client.tutorial.TutorialSteps.NONE);var rules=new GameRules();rules.getRule(GameRules.RULE_DOMOBSPAWNING).set(false,null);
                mc.createWorldOpenFlows().createFreshLevel("ashen-npc-qa-"+System.currentTimeMillis(),new LevelSettings("Covenant NPC QA",GameType.SURVIVAL,false,Difficulty.PEACEFUL,true,rules,WorldDataConfiguration.DEFAULT),new WorldOptions(91,false,false),a->a.registryOrThrow(Registries.WORLD_PRESET).getHolderOrThrow(WorldPresets.FLAT).value().createWorldDimensions());
            }
            if(mc.player==null||mc.level==null||mc.player.tickCount<40)return;
            if(step==0){
                QuestChecks.REPORT.setLength(0);work=mc.getSingleplayerServer().submit(()->{
                    var p=mc.getSingleplayerServer().getPlayerList().getPlayer(mc.player.getUUID());var level=p.serverLevel();
                    for(int x=-5;x<=18;x++)for(int z=-5;z<=15;z++)level.setBlockAndUpdate(new BlockPos(x,80,z),Blocks.STONE_BRICKS.defaultBlockState());
                    p.teleportTo(0.5,81,0.5);p.getInventory().clearContent();p.getInventory().setItem(0,new ItemStack(Items.CRAFTING_TABLE));
                    var npc=DeepNightNpcs.ELDARA.get().create(level);npc.moveTo(2.5,81,0.5);level.addFreshEntity(npc);eldara=npc.getId();
                    for(int i=0;i<8;i++){npc=DeepNightNpcs.COVENANT_NPCS.get(NpcRoster.COMPANIONS.get(i).id()).get().create(level);npc.moveTo(4.5+3*(i%4),81,4.5+4*(i/4));level.addFreshEntity(npc);companions[i]=npc.getId();}
                    p.getInventory().setChanged();p.containerMenu.broadcastChanges();
                });next(1);return;
            }
            if(++ticks<25)return;
            if(step==1){if(!work.isDone())return;work.join();QuestChecks.check(ClientEvents.OPEN.getKey().getValue()==GLFW.GLFW_KEY_MINUS,"Default quest key is minus");KeyMapping.click(ClientEvents.OPEN.getKey());next(2);return;}
            if(step==2){if(!(mc.screen instanceof JournalScreen s)||ClientJournal.loading)return;screen=s;QuestChecks.check(ClientJournal.quests.size()==573&&ClientJournal.views.size()==573,"Minus key opens all 573 definitions and current progress");QuestChecks.check(!ClientJournal.meta.conversation(),"Hotkey opens tracking mode");screen.select("ac_m01");screen.claim();next(3);return;}
            if(step==3){if(ClientJournal.pending)return;QuestChecks.check(!ClientJournal.success&&mc.player.getInventory().countItem(Items.EMERALD)==0,"Real network acceptance from hotkey journal is rejected");capture("integration-01-tracking-only.png");ClientJournal.action(2,"ac_m01","",-1);next(4);return;}
            if(step==4){QuestChecks.check(ClientJournal.meta.pinned().equals("ac_m01"),"Hotkey journal can pin an offered quest to locate its giver");screen.onClose();next(5);return;}
            if(step==5){QuestChecks.check(ClientJournal.hud!=null&&ClientJournal.hud.quest().id().equals("ac_m01"),"Tracked HUD receives server quest progress");capture("integration-02-hud.png");mc.gameMode.interact(mc.player,mc.level.getEntity(eldara),InteractionHand.MAIN_HAND);next(6);return;}
            if(step==6){if(!(mc.screen instanceof DialogueScreen d))return;capture("integration-03-eldara-dialogue.png");d.keyPressed(GLFW.GLFW_KEY_1,0,0);next(7);return;}
            if(step==7){if(!(mc.screen instanceof JournalScreen s)||ClientJournal.loading)return;screen=s;QuestChecks.check(ClientJournal.meta.conversation()&&ClientJournal.meta.giver().equals("eldara")&&screen.resultCount()==276,"Eldara dialogue exposes exactly 276 main quests");screen.select("ac_m01");capture("integration-04-eldara-offer.png");screen.claim();next(8);return;}
            if(step==8){if(ClientJournal.pending)return;QuestChecks.check(ClientJournal.success&&ClientJournal.views.get("ac_m01").status()==QuestEngine.READY&&mc.player.getInventory().countItem(Items.EMERALD)==0,"Acceptance is separate from reward turn-in");screen.claim();claimToken=ClientJournal.token;claimSequence=ClientJournal.sequence;next(9);return;}
            if(step==9){if(ClientJournal.pending)return;QuestChecks.check(ClientJournal.success&&mc.player.getInventory().countItem(Items.EMERALD)==3,"In-person network claim delivers exact emerald reward");QuestNetwork.toServer(new QuestNetwork.Action(claimToken,claimSequence,1,"ac_m01","",-1));next(10);return;}
            if(step==10){QuestChecks.check(mc.player.getInventory().countItem(Items.EMERALD)==3,"Replayed claim packet cannot duplicate rewards");screen.select("ac_m02");screen.claim();next(11);return;}
            if(step==11){if(ClientJournal.pending)return;QuestChecks.check(ClientJournal.success&&ClientJournal.views.get("ac_m02").status()!=QuestEngine.OFFERED,"The next main quest must be accepted through Eldara");ClientJournal.action(2,"ac_m02","",-1);capture("integration-05-next-main-quest.png");next(12);return;}
            if(step==12){screen.onClose();next(13);return;}
            if(step==13){KeyMapping.click(ClientEvents.OPEN.getKey());next(14);return;}
            if(step==14){if(ClientJournal.loading||!(mc.screen instanceof JournalScreen s))return;screen=s;QuestChecks.check(ClientJournal.views.get("ac_m01").claims()==1&&!ClientJournal.meta.conversation(),"Reopening retains completed progress without conversation authority");screen.select("ac_m02");mc.options.guiScale().set(4);mc.resizeDisplay();next(15);return;}
            if(step==15){capture("integration-06-large-gui.png");mc.options.guiScale().set(2);mc.resizeDisplay();screen.onClose();next(20);return;}
            if(step==20){if(portrait==8){next(30);return;}work=mc.getSingleplayerServer().submit(()->{var p=mc.getSingleplayerServer().getPlayerList().getPlayer(mc.player.getUUID());var npc=p.level().getEntity(companions[portrait]);p.teleportTo(npc.getX()-2,npc.getY(),npc.getZ());});next(21);return;}
            if(step==21){if(!work.isDone())return;work.join();mc.gameMode.interact(mc.player,mc.level.getEntity(companions[portrait]),InteractionHand.MAIN_HAND);next(22);return;}
            if(step==22){if(!(mc.screen instanceof DialogueScreen d))return;String kind=NpcRoster.COMPANIONS.get(portrait).id();QuestChecks.check(d.kind.equals(kind)&&d.library.nodes.containsKey("work"),"Supplied skin and authored dialogue loaded for "+kind);capture("integration-npc-"+kind+".png");d.keyPressed(GLFW.GLFW_KEY_1,0,0);next(23);return;}
            if(step==23){if(ClientJournal.loading||!(mc.screen instanceof JournalScreen s))return;String kind=NpcRoster.COMPANIONS.get(portrait).id();QuestChecks.check(ClientJournal.meta.giver().equals(kind)&&ClientJournal.meta.conversation()&&s.resultCount()>0,"Conversation opens "+kind+"'s assigned quests");capture("integration-quests-"+kind+".png");s.onClose();portrait++;next(20);}
            if(step==30){work=mc.getSingleplayerServer().submit(()->{var p=mc.getSingleplayerServer().getPlayerList().getPlayer(mc.player.getUUID());JournalData.child(JournalData.child(QuestEngine.journal(p),"quests"),"ac_m06").putInt("claims",1);p.getInventory().add(new ItemStack(Items.IRON_INGOT,4));var npc=p.level().getEntity(companions[0]);p.teleportTo(npc.getX()-2,npc.getY(),npc.getZ());p.containerMenu.broadcastChanges();});next(31);return;}
            if(step==31){if(!work.isDone())return;work.join();mc.gameMode.interact(mc.player,mc.level.getEntity(companions[0]),InteractionHand.MAIN_HAND);next(32);return;}
            if(step==32){if(!(mc.screen instanceof DialogueScreen d))return;d.keyPressed(GLFW.GLFW_KEY_1,0,0);next(33);return;}
            if(step==33){if(ClientJournal.loading||!(mc.screen instanceof JournalScreen s))return;screen=s;screen.select("ns_rowan01");screen.claim();next(34);return;}
            if(step==34){if(ClientJournal.pending)return;QuestChecks.check(ClientJournal.success&&ClientJournal.views.get("ns_rowan01").status()==QuestEngine.ACTIVE,"Real client accepts Rowan side quest but held materials do not complete its conversation");ClientJournal.action(2,"ns_rowan01","",-1);next(35);return;}
            if(step==35){screen.onClose();work=mc.getSingleplayerServer().submit(()->{var p=mc.getSingleplayerServer().getPlayerList().getPlayer(mc.player.getUUID());var npc=p.level().getEntity(companions[2]);p.teleportTo(npc.getX()-2,npc.getY(),npc.getZ());});next(36);return;}
            if(step==36){if(!work.isDone())return;work.join();mc.gameMode.interact(mc.player,mc.level.getEntity(companions[2]),InteractionHand.MAIN_HAND);next(37);return;}
            if(step==37){if(!(mc.screen instanceof DialogueScreen d))return;QuestChecks.check(ClientJournal.hud.view().progress()[1]==0,"Opening Mara's ordinary dialogue does not finish Rowan's quest");d.keyPressed(GLFW.GLFW_KEY_2,0,0);next(38);return;}
            if(step==38){if(!(mc.screen instanceof DialogueScreen d))return;var field=DialogueScreen.class.getDeclaredField("body");field.setAccessible(true);QuestChecks.check(((String)field.get(d)).contains("The Lock on the Common Room"),"Discuss a quest opens the accepted assignment's full topic title");capture("conversation-01-topic.png");d.keyPressed(GLFW.GLFW_KEY_1,0,0);next(39);return;}
            if(step==39){if(!(mc.screen instanceof DialogueScreen d))return;var field=DialogueScreen.class.getDeclaredField("body");field.setAccessible(true);String response=(String)field.get(d);QuestChecks.check(response.contains("The lock is sound")&&response.contains("Conversation recorded"),"Mara's authored response arrives over the actual NPC network");QuestChecks.check(ClientJournal.hud.view().status()==QuestEngine.READY&&ClientJournal.hud.view().progress()[1]==1,"Discussion immediately updates the pinned quest HUD");capture("conversation-02-response.png");d.onClose();next(40);return;}
            if(step==40){KeyMapping.click(ClientEvents.OPEN.getKey());next(41);return;}
            if(step==41){if(ClientJournal.loading||!(mc.screen instanceof JournalScreen s))return;screen=s;screen.select("ns_rowan01");QuestChecks.check(ClientJournal.views.get("ns_rowan01").status()==QuestEngine.READY&&!ClientJournal.meta.conversation(),"Hotkey retains the conversation result in tracking mode");QuestChecks.check(ClientJournal.quests.get("ns_rowan01").objectives().get(1).dialogue().contains("The lock is sound"),"The journal receives the persistent conversation transcript");screen.claim();next(42);return;}
            if(step==42){if(ClientJournal.pending)return;QuestChecks.check(!ClientJournal.success,"Completed conversation still cannot be rewarded through the hotkey");capture("conversation-03-journal.png");screen.onClose();work=mc.getSingleplayerServer().submit(()->{var p=mc.getSingleplayerServer().getPlayerList().getPlayer(mc.player.getUUID());var npc=p.level().getEntity(companions[0]);p.teleportTo(npc.getX()-2,npc.getY(),npc.getZ());});next(43);return;}
            if(step==43){if(!work.isDone())return;work.join();mc.gameMode.interact(mc.player,mc.level.getEntity(companions[0]),InteractionHand.MAIN_HAND);next(44);return;}
            if(step==44){if(!(mc.screen instanceof DialogueScreen d))return;d.keyPressed(GLFW.GLFW_KEY_1,0,0);next(45);return;}
            if(step==45){if(ClientJournal.loading||!(mc.screen instanceof JournalScreen s))return;screen=s;screen.select("ns_rowan01");screen.claim();next(46);return;}
            if(step==46){if(ClientJournal.pending)return;QuestChecks.check(ClientJournal.success&&ClientJournal.views.get("ns_rowan01").claims()==1&&mc.player.getInventory().countItem(Items.EMERALD)==12,"Returning to Rowan pays the exact side-story reward");QuestChecks.check(ClientJournal.views.get("ns_rowan02").status()==QuestEngine.OFFERED,"Completing the report offers Rowan's next side quest");capture("conversation-04-completed.png");screen.onClose();work=mc.getSingleplayerServer().submit(()->{var p=mc.getSingleplayerServer().getPlayerList().getPlayer(mc.player.getUUID());var quests=JournalData.child(QuestEngine.journal(p),"quests");for(var q:Campaign.quests.values())if(q.chapter().startsWith("Book ")&&!q.id().equals("wn_spring05"))JournalData.child(quests,q.id()).putInt("claims",1);var state=JournalData.child(quests,"wn_spring05");state.putBoolean("accepted",true);var talks=JournalData.child(state,"talks");talks.putInt("2",1);talks.putInt("3",1);p.getInventory().clearContent();p.getInventory().add(new ItemStack(Items.BREAD,32));p.getInventory().add(new ItemStack(Items.COOKED_COD,24));QuestEngine.journal(p).putString("pinned","wn_spring05");QuestServer.sendHud(p);});next(47);return;}
            if(step==47){if(!work.isDone())return;work.join();var hud=ClientJournal.hud;QuestChecks.check(hud.quest().id().equals("wn_spring05")&&ClientEvents.firstHudObjective(hud.quest(),hud.view())==4,"Long quest HUD advances past completed tasks to its fifth objective");capture("conversation-05-next-npc-hud.png");Files.writeString(Path.of("../verification/client-smoke-test.txt"),QuestChecks.REPORT);next(99);mc.stop();return;}
        }catch(Throwable ex){Chronicles.LOG.error("ASHEN CLIENT QA FAILED step "+step,ex);try{Files.createDirectories(Path.of("../verification"));Files.writeString(Path.of("../verification/integration-FAILED.txt"),"Step "+step+"\n"+ex);}catch(Exception ignored){}step=99;mc.stop();}
    }
    private static void capture(String name)throws Exception{Files.createDirectories(Path.of("../verification"));try(var img=Screenshot.takeScreenshot(Minecraft.getInstance().getMainRenderTarget())){img.writeToFile(Path.of("../verification",name));}}
}
