package dev.travellerscompanions.qa;

import dev.travellerscompanions.*;
import dev.travellerscompanions.client.RosterScreen;
import java.nio.file.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=TravellersCompanions.ID,value=Dist.CLIENT)
public final class PartyClientSmoke {
    private static boolean running;
    private static int stage,age,entered;
    private static volatile boolean done;
    private static volatile String failure;
    public static void start(){running=true;}
    private static void next(){stage++;entered=age;}
    private static void server(java.util.function.Consumer<net.minecraft.server.level.ServerPlayer> action){done=false;var mc=Minecraft.getInstance();mc.getSingleplayerServer().execute(()->{try{action.accept(mc.getSingleplayerServer().getPlayerList().getPlayer(mc.player.getUUID()));done=true;}catch(Throwable error){failure=error.toString();}});}
    private static void check(boolean value,String message){ClientSmokeTest.check(value,message);}
    private static Button button(String name){return Minecraft.getInstance().screen.children().stream().filter(w->w instanceof Button b&&b.getMessage().getString().equals(name)).map(w->(Button)w).findFirst().orElseThrow();}
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event){
        if(!running||event.phase!=TickEvent.Phase.END)return;if(failure!=null)throw new IllegalStateException(failure);
        var mc=Minecraft.getInstance();if(++age>500)throw new IllegalStateException("Party client timeout: "+stage);
        if(stage==0){mc.options.guiScale().set(2);mc.resizeDisplay();next();server(p->{CompanionService.dismiss(p);for(var mob:p.serverLevel().getEntitiesOfClass(net.minecraft.world.entity.monster.Monster.class,p.getBoundingBox().inflate(40)))mob.discard();CompanionNetwork.sendState(p,-1,false);});}
        else if(stage==1&&done&&mc.screen instanceof RosterScreen&&age-entered>15){ClientSmokeTest.click("Raeshel");ClientSmokeTest.click("Summon");next();}
        else if(stage==2&&age-entered>20){ClientSmokeTest.click("Mara");ClientSmokeTest.click("Summon");next();}
        else if(stage==3&&age-entered>20){check(button("Dismiss").active,"Second member has no dismiss button");ClientSmokeTest.capture("party-two-roster");next();server(p->{check(CompanionService.active(p,1)!=null&&CompanionService.active(p,4)!=null&&CompanionService.activeKinds(p).size()==2,"Network did not summon two companions");});}
        else if(stage==4&&done&&age-entered>10){ClientSmokeTest.click("Varys");check(!button("Summon").active,"Full party allows third summon");ClientSmokeTest.click("Raeshel");check(button("Dismiss").active,"First member has no dismiss button");ClientSmokeTest.click("Dismiss");next();}
        else if(stage==5&&age-entered>20){check(button("Summon").active,"Dismissed member cannot be resummoned");ClientSmokeTest.click("Mara");check(button("Dismiss").active,"Dismissing first member removed second");next();server(p->{check(CompanionService.active(p,1)==null&&CompanionService.active(p,4)!=null&&CompanionService.activeKinds(p).size()==1,"Network dismissal affected wrong member");});}
        else if(stage==6&&done&&age-entered>10){ClientSmokeTest.click("Varys");ClientSmokeTest.click("Summon");next();}
        else if(stage==7&&age-entered>20){mc.options.guiScale().set(4);mc.resizeDisplay();next();}
        else if(stage==8&&age-entered>15){ClientSmokeTest.click("Mara");check(button("Dismiss").active,"Small roster lost second party member");ClientSmokeTest.capture("party-two-roster-small");next();server(p->{check(CompanionService.active(p,0)!=null&&CompanionService.active(p,4)!=null&&CompanionService.activeKinds(p).size()==2,"Replacement summon failed");});}
        else if(stage==9&&done){try{Files.writeString(Path.of("../verification/party-client-smoke-test.txt"),"PASS: Real roster buttons and client/server packets summoned Raeshel and Mara together, displayed Dismiss for both, disabled a third summon, dismissed only Raeshel, and filled the free slot with Varys. Both active members remain selectable at GUI scales 2 and 4. Screenshots: party-two-roster.png and party-two-roster-small.png.\n");}catch(Exception error){throw new RuntimeException(error);}running=false;TroopClientSmoke.start();}
    }
}
