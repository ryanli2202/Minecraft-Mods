package dev.deepnight.qa;

import dev.deepnight.*;
import dev.deepnight.client.BossScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.nio.file.*;
import java.util.function.Consumer;

/** Real client/server packets, actual tool use and screen buttons in a disposable world. */
@Mod.EventBusSubscriber(modid=DeepNight.ID,value=Dist.CLIENT)
public final class BossMenuClientSmoke {
    private static int step,frames,total,preview;
    private static volatile boolean serverDone;
    private static volatile String failure;
    private static volatile java.util.UUID replacedBoss,barId;
    private static volatile float expectedBar;
    private static boolean finished;
    private static int deathBoss;
    private static volatile java.util.UUID defeated;
    private static volatile long deathTick;
    private static final StringBuilder report=new StringBuilder();
    private static void require(boolean condition,String message){if(!condition)throw new IllegalStateException(message);}
    private static void next(int value){System.out.println("SUMMON MENU QA STEP "+value);step=value;frames=0;}
    private static void server(Consumer<ServerPlayer> work){
        var mc=Minecraft.getInstance();var id=mc.player.getUUID();serverDone=false;
        mc.getSingleplayerServer().execute(()->{try{work.accept(mc.getSingleplayerServer().getPlayerList().getPlayer(id));serverDone=true;}catch(Throwable ex){ex.printStackTrace();failure=ex.toString();}});
    }
    public static void start(){
        next(1);server(p->{p.setGameMode(GameType.CREATIVE);p.setItemInHand(InteractionHand.MAIN_HAND,taggedTool());p.inventoryMenu.broadcastChanges();p.connection.teleport(0.5,-60,0.5,0,0);});
    }
    private static ItemStack taggedTool(){var stack=new ItemStack(DeepNight.CALLER.get());stack.setHoverName(net.minecraft.network.chat.Component.literal("My Boss Tool"));stack.getOrCreateTag().putString("mod_added_data","regression");return stack;}
    private static void use(){Minecraft mc=Minecraft.getInstance();mc.gameMode.useItem(mc.player,InteractionHand.MAIN_HAND);}
    private static void click(String label){
        var screen=Minecraft.getInstance().screen;
        Button button=(Button)screen.children().stream().filter(c->c instanceof Button b&&b.getMessage().getString().equals(label)).findFirst().orElseThrow(()->new IllegalStateException("Button missing: "+label));
        require(button.active,"Button disabled: "+label);button.onPress();
    }
    private static void capture(String name)throws Exception{
        try(var pixels=Screenshot.takeScreenshot(Minecraft.getInstance().getMainRenderTarget())){pixels.writeToFile(Path.of("../verification/boss-menu-"+name+".png"));}
    }
    private static void finish(){
        if(finished)return;finished=true;
        try{Files.writeString(Path.of("../verification/boss-menu-client-report.txt"),(failure==null?"PASS\n":"FAIL: "+failure+"\n")+report);}catch(Exception ex){ex.printStackTrace();}
        Minecraft.getInstance().stop();
    }
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event){
        if(!Boolean.getBoolean("deepnight.bossMenuTest")||event.phase!=TickEvent.Phase.END||step==0||finished)return;
        if(failure!=null){finish();return;}
        var mc=Minecraft.getInstance();frames++;total++;if(frames%100==0)System.out.println("SUMMON MENU QA WAIT "+step+" screen="+mc.screen+" done="+serverDone+" hand="+mc.player.getMainHandItem());
        try{
            require(total<4000,"Boss menu test timed out at step "+step);
            BossScreen screen=mc.screen instanceof BossScreen b?b:null;
            switch(step){
                case 1->{if(frames>40&&serverDone&&mc.player.getMainHandItem().is(DeepNight.CALLER.get())){use();next(2);}}
                case 2->{if(screen!=null&&frames>15){require(screen.campaignState().creative()&&screen.campaignState().next()==0,"Incorrect initial menu state");capture("next");report.append("Named, mod-tagged tool opened and kept the client boss screen open through actual item use and real menu packets.\n");click("Summon boss");next(3);}}
                case 3->{if(mc.screen==null){server(p->{var data=CampaignData.get(p.server);require(data.activeStage==0&&data.active!=null,"Menu did not summon Ogre");var boss=(NightMob)p.serverLevel().getEntity(data.active);boss.hurt(boss.damageSources().playerAttack(p),100000);require(data.player(p.getUUID()).stage==1,"Creative kill did not advance");});next(4);}}
                case 4->{if(serverDone&&frames>30){report.append("Creative Ogre kill advanced to Orc Champion.\n");use();next(5);}}
                case 5->{if(screen!=null&&frames>15){require(screen.campaignState().next()==1&&screen.campaignState().completed(Species.boss(0)),"Reopened menu lost progress");capture("progress");click(Species.boss(0).title);next(6);}}
                case 6->{if(frames>12){capture("rematch");click("Summon boss");next(7);}}
                case 7->{if(mc.screen==null){server(p->{var d=CampaignData.get(p.server);require(d.activeStage==0&&d.activeMode==CampaignData.EncounterMode.REMATCH&&d.player(p.getUUID()).stage==1,"Rematch state incorrect");});next(8);}}
                case 8->{if(serverDone&&frames>30){use();next(9);}}
                case 9->{if(screen!=null&&screen.campaignState().active()==0&&frames>15){capture("active");click("End active encounter");next(10);}}
                case 10->{if(frames>3){click("Confirm end (no credit)");next(11);}}
                case 11->{if(screen!=null&&screen.campaignState().active()<0&&frames>15){require(screen.campaignState().next()==1,"Abandon changed progression");report.append("Rematch and confirmed end used real server actions without advancing.\n");screen.selectBoss(Species.BOSS_COUNT-1);next(12);}}
                case 12->{if(frames>15){capture("creative-test");click("Summon boss");next(13);}}
                case 13->{if(mc.screen==null){server(p->{var d=CampaignData.get(p.server);require(d.activeStage==Species.BOSS_COUNT-1&&d.activeMode==CampaignData.EncounterMode.TEST,"Creative future summon incorrect");replacedBoss=d.active;});next(21);}}
                case 21->{if(serverDone&&frames>30){use();next(22);}}
                case 22->{if(screen!=null&&screen.campaignState().active()==Species.BOSS_COUNT-1&&frames>15){
                    for(int i=0;i<Species.BOSS_COUNT;i++)require(screen.campaignState().canSummon(i),"Active boss locked creative selection "+i);
                    screen.selectBoss(Species.DAWN_DRAGON_LORD.bossStage());next(23);
                }}
                case 23->{if(frames>15){capture("replace");click("Replace active boss");next(24);}}
                case 24->{if(mc.screen==null){server(p->{
                    var d=CampaignData.get(p.server);require(d.activeStage==Species.DAWN_DRAGON_LORD.bossStage()&&d.activeMode==CampaignData.EncounterMode.TEST&&d.player(p.getUUID()).stage==1,"Replacement changed progression");
                    var old=p.serverLevel().getEntity(replacedBoss);require(old==null||old.isRemoved(),"Replacement retained the old boss");
                    var boss=(NightMob)p.serverLevel().getEntity(d.active);boss.setNoAi(true);
                    require(boss.hurt(boss.damageSources().generic(),600)&&boss.getHealth()>0&&boss.getHealth()<boss.getMaxHealth(),"Dragon Lord did not take nonlethal damage");
                    dev.deepnight.source.SourceStun.apply(boss,600);expectedBar=boss.getHealth()/boss.getMaxHealth();
                    try{var field=boss.getClass().getDeclaredField("bossInfo");field.setAccessible(true);barId=((net.minecraft.server.level.ServerBossEvent)field.get(boss)).getId();}
                    catch(ReflectiveOperationException ex){throw new IllegalStateException(ex);}
                });next(25);}}
                case 25->{if(serverDone&&frames>30){
                    var overlay=mc.gui.getBossOverlay();var field=overlay.getClass().getDeclaredField("events");field.setAccessible(true);
                    var bars=(java.util.Map<?,?>)field.get(overlay);var bar=(net.minecraft.world.BossEvent)bars.get(barId);
                    require(bar!=null&&Math.abs(bar.getProgress()-expectedBar)<0.005f,"Client Dragon Lord bar did not receive the damaged health during NoAI/stun");
                    capture("dragon-lord-health");report.append("Creative replacement selected Dragon Lord during an active future boss fight without defeat credit.\nDragon Lord's client health bar tracks damage during NoAI and stun.\n");
                    server(p->{require(CampaignEvents.abandon(p).success(),"Failed to clean up bar test");p.setGameMode(GameType.SURVIVAL);});next(14);
                }}
                case 14->{if(serverDone&&frames>30){use();next(15);}}
                case 15->{if(screen!=null&&frames>15){screen.selectBoss(Species.BOSS_COUNT-1);next(16);}}
                case 16->{if(frames>15){require(!screen.campaignState().creative()&&!screen.campaignState().canSummon(Species.BOSS_COUNT-1),"Survival future boss became summonable");capture("locked");report.append("Future creative test left progress unchanged; survival future bosses remain locked.\n");preview=0;screen.selectBoss(0);next(17);}}
                case 17->{if(frames>6){if(++preview<Species.BOSS_COUNT){screen.selectBoss(preview);frames=0;}else{report.append("All 10 native boss previews rendered in the menu.\n");screen.selectBoss(1);mc.options.guiScale().set(4);mc.resizeDisplay();next(18);}}}
                case 18->{if(frames>15){capture("compact");mc.options.guiScale().set(2);mc.resizeDisplay();next(19);}}
                case 19->{if(frames>10){var search=(EditBox)screen.children().stream().filter(c->c instanceof EditBox).findFirst().orElseThrow();search.setValue("guardian");next(20);}}
                case 20->{if(frames>12){capture("search");long matches=screen.children().stream().filter(c->c instanceof Button b&&b.getMessage().getString().contains("Guardian")).count();require(matches==1,"Search did not filter boss list");report.append("Search and compact window layout verified.\n");mc.setScreen(null);deathBoss=Species.DAWN_NOTCH_BOSS.bossStage();beginDeathCheck();}}
                case 30->{if(serverDone&&frames>80){server(p->{
                    var d=CampaignData.get(p.server);var boss=(NightMob)p.serverLevel().getEntity(d.active);require(boss!=null,"Death audit boss absent");
                    defeated=boss.getUUID();p.connection.teleport(boss.getX()+3,boss.getY(),boss.getZ(),0,0);
                    // Exercise the real native death routine and delayed callback with a connected client.
                    boss.kill();require(!boss.isAlive(),"Client death audit could not kill boss");deathTick=p.serverLevel().getGameTime();
                });next(31);}}
                case 31->{if(serverDone&&frames>300){server(p->{
                    require(p.serverLevel().getGameTime()-deathTick>=250,"Insufficient server death ticks");
                    var boss=p.serverLevel().getEntity(defeated);require(boss==null||boss.isRemoved(),"Client death sequence retained its boss");
                    String id="simple_mobs:the_last_invader";
                    var adv=p.server.getAdvancements().getAdvancement(new net.minecraft.resources.ResourceLocation(id));
                    require(deathBoss!=Species.DAWN_HEROBRINE.bossStage()||adv!=null&&p.getAdvancements().getOrStartProgress(adv).isDone(),"Connected player missed death advancement: "+id);
                    require(CampaignData.get(p.server).active==null,"Delayed death left encounter active");
                    if(deathBoss==Species.DAWN_HEROBRINE.bossStage()){
                        var trophy=dev.deepnight.source.dawn.init.SimpleMobsModItems.ACCURSED_SWORD.get();
                        require(p.getInventory().countItem(trophy)>0||!p.serverLevel().getEntitiesOfClass(net.minecraft.world.entity.item.ItemEntity.class,
                            p.getBoundingBox().inflate(20),item->item.getItem().is(trophy)).isEmpty(),"Herobrine's delayed trophy drop is missing");
                    }
                });next(32);}}
                case 32->{if(serverDone){report.append(Species.boss(deathBoss).title+" died, removed its corpse, cleared the encounter without a client/server crash.\n");if(deathBoss==Species.DAWN_NOTCH_BOSS.bossStage()){deathBoss=Species.DAWN_HEROBRINE.bossStage();beginDeathCheck();}else finish();}}
            }
        }catch(Throwable ex){ex.printStackTrace();failure=ex.toString();finish();}
    }
    private static void beginDeathCheck(){
        server(p->{p.setGameMode(GameType.CREATIVE);require(SummonTool.summonSelected(p,deathBoss).success(),"Failed to summon death audit boss");});next(30);
    }
}
