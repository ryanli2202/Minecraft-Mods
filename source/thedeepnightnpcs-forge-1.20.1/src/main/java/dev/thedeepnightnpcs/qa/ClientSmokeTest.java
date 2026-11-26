package dev.thedeepnightnpcs.qa;

import dev.thedeepnightnpcs.DeepNightNpcs;
import dev.thedeepnightnpcs.client.DialogueScreen;
import dev.thedeepnightnpcs.entity.NpcEntity;
import dev.thedeepnightnpcs.network.NpcNetwork;
import dev.thedeepnightnpcs.quest.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.*;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.glfw.GLFW;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid=DeepNightNpcs.ID,value=Dist.CLIENT)
public final class ClientSmokeTest {
    private static boolean started;
    private static int step,ticks,elapsed;
    private static int eldara,caelen,guard;
    private static int emeraldsBefore,xpBefore;
    private static long huntAt;
    private static CompletableFuture<?> pending;
    private static net.minecraft.world.phys.Vec3 guardStart;
    private static double guardTravel;
    private static void check(boolean condition,String message){ServerChecks.check(condition,message);}
    private static void task(Minecraft mc,Runnable run){pending=mc.getSingleplayerServer().submit(run);}
    private static ServerPlayer player(Minecraft mc){return mc.getSingleplayerServer().getPlayerList().getPlayer(mc.player.getUUID());}
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event) {
        if(!Boolean.getBoolean("thedeepnightnpcs.smokeTest")||event.phase!=TickEvent.Phase.END||step==99)return;
        Minecraft mc=Minecraft.getInstance();
        try {
            if(!started&&mc.screen instanceof TitleScreen&&mc.getOverlay()==null) {
                started=true;mc.options.pauseOnLostFocus=false;GLFW.glfwHideWindow(mc.getWindow().getWindow());
                mc.options.guiScale().set(2);mc.options.renderDistance().set(2);mc.options.simulationDistance().set(5);mc.resizeDisplay();
                mc.getTutorial().setStep(net.minecraft.client.tutorial.TutorialSteps.NONE);
                GameRules rules=new GameRules();rules.getRule(GameRules.RULE_DOMOBSPAWNING).set(false,null);rules.getRule(GameRules.RULE_DAYLIGHT).set(false,null);
                mc.createWorldOpenFlows().createFreshLevel("npc-qa-"+System.currentTimeMillis(),new LevelSettings("NPC QA",GameType.CREATIVE,false,Difficulty.NORMAL,true,rules,WorldDataConfiguration.DEFAULT),new WorldOptions(711,false,false),access->access.registryOrThrow(Registries.WORLD_PRESET).getHolderOrThrow(WorldPresets.FLAT).value().createWorldDimensions());return;
            }
            if(mc.player==null||mc.level==null||mc.player.tickCount<40)return;
            if(++elapsed>6000)throw new IllegalStateException("QA timed out in step "+step);
            if(pending!=null){if(!pending.isDone())return;pending.join();pending=null;ticks=0;}
            if(++ticks<25)return;
            if(step==0){
                task(mc,()->{
                    try{ServerChecks.run(mc.getSingleplayerServer());}catch(Exception e){throw new RuntimeException(e);}
                    var p=player(mc);p.teleportTo(0,-60,0);p.setYRot(0);
                    var e=DeepNightNpcs.ELDARA.get().create(p.level());e.moveTo(-1.5,-60,3,180,0);p.serverLevel().addFreshEntity(e);eldara=e.getId();
                    var c=DeepNightNpcs.CAELEN.get().create(p.level());c.moveTo(1.5,-60,3,180,0);p.serverLevel().addFreshEntity(c);caelen=c.getId();
                    var g=DeepNightNpcs.GUARD.get().create(p.level());g.moveTo(7,-60,0,90,0);p.serverLevel().addFreshEntity(g);guard=g.getId();guardStart=g.position();
                });next();return;
            }
            if(step==1){interact(mc,eldara);next();return;}
            if(step==2){
                if(!(mc.screen instanceof DialogueScreen screen))return;
                check(screen.kind.equals("eldara"),"Real right-click packet opens Eldara's wheel");
                check(screen.library.nodes.size()==163,"All 163 Eldara dialogue nodes loaded");
                check(screen.library.nodes.values().stream().filter(n->n.children().isEmpty()).count()==144,"144 authored leaf topics");
                for(var n:screen.library.nodes.values())for(String id:n.children())check(screen.library.nodes.containsKey(id),"Dialogue edge resolves");
                capture("01-eldara-root.png");screen.keyPressed(GLFW.GLFW_KEY_RIGHT,0,0);next();return;
            }
            if(step==3){capture("02-eldara-more-topics.png");((DialogueScreen)mc.screen).show("chapter_4",true);next();return;}
            if(step==4){capture("03-eldara-history-wheel.png");((DialogueScreen)mc.screen).keyPressed(GLFW.GLFW_KEY_5,0,0);next();return;}
            if(step==5){capture("04-eldara-lore.png");mc.options.guiScale().set(4);mc.resizeDisplay();next();return;}
            if(step==6){capture("05-large-gui-scale.png");mc.options.guiScale().set(2);mc.resizeDisplay();mc.screen.onClose();interact(mc,caelen);next();return;}
            if(step==7){
                if(!(mc.screen instanceof DialogueScreen screen)||!screen.kind.equals("caelen"))return;
                capture("06-caelen-root.png");screen.keyPressed(GLFW.GLFW_KEY_1,0,0);next();return;
            }
            if(step==8){capture("07-caelen-board.png");((DialogueScreen)mc.screen).keyPressed(GLFW.GLFW_KEY_1,0,0);next();return;}
            if(step==9){capture("08-contract-preview.png");((DialogueScreen)mc.screen).keyPressed(GLFW.GLFW_KEY_1,0,0);next();return;}
            if(step==10){
                task(mc,()->{
                    var p=player(mc);var r=HuntData.get(p.server).player(p.getUUID());check(r.active!=null,"GUI accept packet creates a server-side contract");
                    check(r.kills==0,"New hunt starts at zero");var original=r.active;
                    HuntService.accept(p,UUID.randomUUID().toString());check(r.active.equals(original),"Second or forged acceptance cannot replace active hunt");
                    HuntService.credit(p,"minecraft:cow");check(r.kills==0,"Wrong target never advances progress");
                    // Real LivingDeathEvent attribution through a player damage source.
                    var type=ForgeRegistries.ENTITY_TYPES.getValue(new net.minecraft.resources.ResourceLocation(r.active.target()));
                    for(int i=0;i<r.active.required();i++) {
                        var target=(LivingEntity)type.create(p.level()); target.moveTo(30,-60,30);p.serverLevel().addFreshEntity(target);
                        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.living.LivingDeathEvent(target,p.damageSources().playerAttack(p)));target.discard();
                    }
                    check(r.kills==r.active.required(),"Credited target death events complete the hunt");
                    HuntService.credit(p,r.active.target());check(r.kills==r.active.required(),"Kill count stops at the required total");
                    var restored=HuntData.load(HuntData.get(p.server).save(new CompoundTag())).player(p.getUUID());
                    check(restored.kills==r.kills&&restored.active.equals(r.active),"Completed hunt persists through save/load");
                    emeraldsBefore=p.getInventory().countItem(Items.EMERALD);xpBefore=p.totalExperience;
                    var c=(NpcEntity)p.level().getEntity(caelen);p.teleportTo(60,-60,60);
                    NpcNetwork.act(p,new NpcNetwork.Action(caelen,"claim",""));check(r.active!=null,"Out-of-range reward request rejected");p.teleportTo(0,-60,0);
                    check(!NpcNetwork.authorized(p,(NpcEntity)p.level().getEntity(eldara)),"Session is bound to the NPC actually opened");
                    NpcNetwork.open(p,c);
                });next();return;
            }
            if(step==11){
                if(!(mc.screen instanceof DialogueScreen))interact(mc,caelen);
                if(mc.screen instanceof DialogueScreen screen){screen.show("$ledger",true);NpcNetwork.CHANNEL.sendToServer(new NpcNetwork.Action(caelen,"status",""));next();}return;
            }
            if(step==12){capture("09-completed-contract.png");((DialogueScreen)mc.screen).keyPressed(GLFW.GLFW_KEY_1,0,0);next();return;}
            if(step==13){
                task(mc,()->{
                    var p=player(mc);var r=HuntData.get(p.server).player(p.getUUID());
                    check(r.active==null&&r.completed==1,"GUI claim consumes completed contract once");
                    check(p.getInventory().countItem(Items.EMERALD)>emeraldsBefore&&p.totalExperience>xpBefore,"Claim pays emeralds and XP");
                    int paid=p.getInventory().countItem(Items.EMERALD),xp=p.totalExperience;HuntService.claim(p);
                    check(paid==p.getInventory().countItem(Items.EMERALD)&&xp==p.totalExperience,"Repeated claim pays nothing");
                    HuntService.ensureOffers(p);var selected=r.offers.get(0);HuntService.accept(p,selected.id().toString());HuntService.abandon(p);
                    check(r.active==null&&r.kills==0&&paid==p.getInventory().countItem(Items.EMERALD),"Abandon clears progress with no payout");
                    var offers=List.copyOf(r.offers);HuntService.refresh(p,false); // Clock has advanced; next immediate refresh must stay stable.
                    var fresh=List.copyOf(r.offers);HuntService.refresh(p,false);check(r.offers.equals(fresh),"Refresh cooldown prevents rapid rerolling");
                    var e=(NpcEntity)p.level().getEntity(eldara);var c=(NpcEntity)p.level().getEntity(caelen);
                    check(e.getX()==-1.5&&e.getZ()==3&&c.getX()==1.5&&c.getZ()==3,"Guide and hunter remain at original positions during conversations");
                    check(!e.hurt(p.damageSources().playerAttack(p),99),"Ordinary player damage cannot kill the guide");
                    e.push(3,0,3);check(e.getX()==-1.5&&e.getZ()==3,"Guide cannot be pushed off post");
                    p.teleportTo(6,-60,-2);
                });mc.screen.onClose();next();return;
            }
            if(step==14){interact(mc,guard);next();return;}
            if(step==15){if(!(mc.screen instanceof DialogueScreen screen)||!screen.kind.equals("spire_guard"))return;capture("10-guard-wheel.png");mc.screen.onClose();next();return;}
            if(step==16){
                task(mc,()->{
                    var p=player(mc);var g=(NpcEntity)p.level().getEntity(guard);check(g.withinPatrol(g.position()),"Guard remains inside five-block patrol");
                    guardTravel=Math.max(guardTravel,g.position().distanceTo(guardStart));
                });
                if(++huntAt<30){ticks=0;return;}
                next();return;
            }
            if(step==17){
                check(guardTravel>0.2,"Guard actually roams instead of remaining frozen");
                Files.createDirectories(Path.of("../verification"));Files.writeString(Path.of("../verification/client-smoke-test.txt"),ServerChecks.REPORT.toString());
                step=99;mc.stop();
            }
        }catch(Throwable error){DeepNightNpcs.LOG.error("NPC QA FAILED at step "+step,error);try{Files.createDirectories(Path.of("../verification"));Files.writeString(Path.of("../verification/client-FAILED.txt"),"Step "+step+": "+error);}catch(Exception ignored){}step=99;mc.stop();}
    }
    private static void next(){step++;ticks=0;}
    private static void interact(Minecraft mc,int id){var entity=mc.level.getEntity(id);if(entity==null)throw new IllegalStateException("Missing client NPC "+id);mc.gameMode.interact(mc.player,entity,InteractionHand.MAIN_HAND);}
    private static void capture(String name)throws Exception{Files.createDirectories(Path.of("../verification"));try(var image=Screenshot.takeScreenshot(Minecraft.getInstance().getMainRenderTarget())){image.writeToFile(Path.of("../verification",name));}}
}
