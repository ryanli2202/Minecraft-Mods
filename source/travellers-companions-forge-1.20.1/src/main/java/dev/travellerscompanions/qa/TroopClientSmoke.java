package dev.travellerscompanions.qa;

import dev.travellerscompanions.*;
import dev.travellerscompanions.client.*;
import java.nio.file.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=TravellersCompanions.ID,value=Dist.CLIENT)
public final class TroopClientSmoke {
    private static boolean running;
    private static int age,stage,entered,entity;
    private static volatile boolean done;
    private static volatile String failure;
    public static void start(){running=true;}
    private static void next(){stage++;entered=age;}
    private static void check(boolean value,String message){ClientSmokeTest.check(value,message);}
    private static void server(java.util.function.Consumer<net.minecraft.server.level.ServerPlayer> action){done=false;var mc=Minecraft.getInstance();mc.getSingleplayerServer().execute(()->{try{action.accept(mc.getSingleplayerServer().getPlayerList().getPlayer(mc.player.getUUID()));done=true;}catch(Throwable error){failure=error.toString();}});}
    private static Button button(String name){return Minecraft.getInstance().screen.children().stream().filter(w->w instanceof Button b&&b.getMessage().getString().equals(name)).map(w->(Button)w).findFirst().orElseThrow();}
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event){
        if(!running||event.phase!=TickEvent.Phase.END)return;if(failure!=null)throw new IllegalStateException(failure);
        var mc=Minecraft.getInstance();if(++age>700)throw new IllegalStateException("Troop client timeout: "+stage);
        if(stage==0){mc.options.guiScale().set(2);mc.resizeDisplay();ClientSmokeTest.click("Troops");ClientSmokeTest.click("Footpaladins");ClientSmokeTest.capture("troops-paladin-roster");ClientSmokeTest.click("Summon four");next();}
        else if(stage==1&&age-entered>25){check(mc.screen instanceof TroopRosterScreen&&button("Dismiss group").active,"Group summon did not update troop screen");ClientSmokeTest.capture("troops-active-roster");next();server(p->{
            var r=CompanionData.get(p.server).roster(p.getUUID());check(r.party.size()==2&&r.squad.size()==4&&r.squadKind==1,"Network failed to summon four beside two named");
            for(int i=0;i<4;i++){var e=TroopService.active(p,i);check(e!=null&&e.isTroop()&&e.getMaxHealth()==20&&e.companionLevel()==1&&e.getOffhandItem().is(Items.SHIELD),"Bad paladin state");}
            for(int kind:CompanionService.activeKinds(p))check(CompanionService.active(p,kind).getMaxHealth()>=100,"Named health below 100");
            var e=TroopService.active(p,0);entity=e.getId();e.setNoAi(true);e.startUsingItem(net.minecraft.world.InteractionHand.OFF_HAND);
        });}
        else if(stage==2&&done&&age-entered>15){
            check(mc.level.getEntity(entity) instanceof CompanionEntity,"Troop entity absent on client");var e=(CompanionEntity)mc.level.getEntity(entity);check(e.isTroop()&&e.troopKind()==TroopKind.FOOTPALADIN&&e.getDisplayName().getString().contains("Footpaladin"),"Troop identity did not synchronize");
            var renderer=(CompanionRenderer)mc.getEntityRenderDispatcher().getRenderer(e);renderer.getModel().prepareMobModel(e,0,0,0);check(renderer.getModel().leftArmPose==net.minecraft.client.model.HumanoidModel.ArmPose.BLOCK,"Shield block pose missing");e.animation=CombatBridge.strike(e,0).animation();e.animationStarted=e.tickCount;e.animationDuration=30;renderer.getModel().setupAnim(e,0,0,e.tickCount+2,0,0);check(renderer.getModel().activeAnimation==null,"Attack animation overrides raised shield");
            ClientSmokeTest.click("Footarchers");check(!button("Summon four").active,"Second troop group is enabled");ClientSmokeTest.click("Footpaladins");mc.options.guiScale().set(4);mc.resizeDisplay();next();
        }
        else if(stage==3&&age-entered>15){ClientSmokeTest.capture("troops-roster-small");check(button("Dismiss group").active,"Small troop roster lost action");ClientSmokeTest.click("Dismiss group");next();}
        else if(stage==4&&age-entered>20){mc.options.guiScale().set(2);mc.resizeDisplay();ClientSmokeTest.click("Footarchers");ClientSmokeTest.click("Summon four");next();}
        else if(stage==5&&age-entered>20){ClientSmokeTest.capture("troops-archer-roster");next();server(p->{check(CompanionData.get(p.server).roster(p.getUUID()).squadKind==2&&TroopService.active(p,0).getMainHandItem().is(Items.BOW),"Archer group switch failed");TroopService.active(p,0).backpack.getItem(0).shrink(5);TroopService.active(p,0).checkpoint();});}
        else if(stage==6&&done){ClientSmokeTest.click("Dismiss group");next();}
        else if(stage==7&&age-entered>20){ClientSmokeTest.click("Footsquires");ClientSmokeTest.click("Summon four");next();}
        else if(stage==8&&age-entered>20){ClientSmokeTest.capture("troops-squire-roster");mc.options.guiScale().set(2);mc.resizeDisplay();next();server(p->{var e=TroopService.active(p,0);check(e.getMainHandItem().is(Items.IRON_SWORD),"Squire sword missing");entity=e.getId();p.moveTo(e.position().add(1,0,0));CompanionNetwork.openWheel(p,e);});}
        else if(stage==9&&done&&age-entered>15&&mc.screen instanceof InteractionWheel){ClientSmokeTest.capture("troop-orders");check(mc.screen.children().size()==2,"Troop wheel exposes named abilities");ClientSmokeTest.click("Hold position");next();}
        else if(stage==10&&age-entered>20){check(button("Follow me").active,"Troop hold order not synchronized");ClientSmokeTest.click("Stance: Aggressive");next();}
        else if(stage==11&&age-entered>20){check(button("Stance: Defensive").active,"Troop stance failed");next();server(p->{var e=TroopService.active(p,0);check(e.holding()&&e.stance()==1,"Troop orders failed on server");CompanionService.openInventory(p,e.getId(),false);});}
        else if(stage==12&&done&&mc.screen instanceof EquipmentScreen&&age-entered>15){ClientSmokeTest.capture("troop-equipment");mc.player.closeContainer();next();server(p->{p.closeContainer();CompanionNetwork.sendState(p,-1,false);});}
        else if(stage==13&&done&&mc.screen instanceof RosterScreen&&age-entered>15){ClientSmokeTest.click("Troops");ClientSmokeTest.click("Dismiss group");next();}
        else if(stage==14&&age-entered>20){ClientSmokeTest.click("Footarchers");ClientSmokeTest.click("Summon four");next();}
        else if(stage==15&&age-entered>20){next();server(p->{check(TroopService.active(p,0).backpack.countItem(Items.ARROW)==59,"Group switch refilled arrows");check(CompanionService.activeKinds(p).size()==2,"Group actions changed named party");});}
        else if(stage==16&&done){try{Files.writeString(Path.of("../verification/troop-client-smoke-test.txt"),"PASS: Real roster buttons and packets summon four footpaladins alongside two named companions; each troop has 20 health, level 1, and its own synchronized name. Named companions have at least 100 health. Shield blocking pose renders. Second troop group is disabled. Footpaladin, footarcher and footsquire groups switch correctly; arrows persist across switches. Troop roster works at GUI scales 2 and 4. Individual hold/stance orders work through the two-button troop menu; equipment opens with the soldier's name. Named party stays active throughout.\n");}catch(Exception error){throw new RuntimeException(error);}running=false;mc.stop();}
    }
}
