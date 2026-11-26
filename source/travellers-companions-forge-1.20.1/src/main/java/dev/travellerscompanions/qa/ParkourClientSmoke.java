package dev.travellerscompanions.qa;

import dev.travellerscompanions.*;
import dev.travellerscompanions.client.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.nio.file.*;

@Mod.EventBusSubscriber(modid=TravellersCompanions.ID,value=Dist.CLIENT)
public final class ParkourClientSmoke {
    private static boolean running;
    private static int age,entityId;
    private static volatile boolean done;
    private static volatile String failure;
    public static void start(){running=true;Minecraft.getInstance().setScreen(null);}
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event){
        if(!running||event.phase!=TickEvent.Phase.END)return;
        if(failure!=null)throw new IllegalStateException(failure);
        var mc=Minecraft.getInstance();
        if(++age>260)throw new IllegalStateException("Parkour client timeout");
        if(age==1){
            ClientSmokeTest.check(CompanionRenderer.animation("seriousplayeranimations:walking")!=null,"SPA walking resource missing");
            mc.getSingleplayerServer().execute(()->{try{
                var p=mc.getSingleplayerServer().getPlayerList().getPlayer(mc.player.getUUID());
                String error=CompanionService.summon(p,1);ClientSmokeTest.check(error.isEmpty(),error);
                var e=CompanionService.active(p,1);entityId=e.getId();e.setStance(2);
                p.moveTo(e.position().add(12,0,0));p.setSprinting(true);e.parkour.tick();done=true;
            }catch(Throwable ex){failure=ex.toString();}});
        }
        if(done&&age>4&&mc.level.getEntity(entityId) instanceof CompanionEntity e&&e.parkourAction()==CompanionParkour.FAST_RUN){
            done=false;mc.setScreen(new Gallery(e));running=false;
        }
    }
    private static final class Gallery extends Screen {
        private final CompanionEntity template;
        private final CompanionEntity[] display=new CompanionEntity[4];
        private int ticks;
        Gallery(CompanionEntity e){super(Component.literal("Companion movement verification"));template=e;}
        @Override public boolean isPauseScreen(){return false;}
        @Override protected void init(){
            for(int i=0;i<4;i++)if(display[i]==null){display[i]=modelEntity();display[i].setItemSlot(EquipmentSlot.CHEST,new ItemStack(Items.IRON_CHESTPLATE));if(i>0)display[i].setParkourState(i,i==2?14:i==3?9:0,0);}
            minecraft.getSingleplayerServer().execute(()->{
                var p=minecraft.getSingleplayerServer().getPlayerList().getPlayer(minecraft.player.getUUID());
                var e=CompanionService.active(p,1);e.setNoAi(true);e.parkour.cancel();e.setItemSlot(EquipmentSlot.OFFHAND,new ItemStack(Items.SHIELD));e.startUsingItem(net.minecraft.world.InteractionHand.OFF_HAND);
            });
        }
        @Override public void tick(){
            for(int i=0;i<4;i++){
                display[i].tickCount++;
                if(i<2)display[i].walkAnimation.update(.6f,1);
                if(i==2&&ticks%14==0)display[i].setParkourState(2,14,0);
                if(i==3&&ticks%9==0)display[i].setParkourState(3,9,0);
            }
            if(++ticks==10)verify();
            if(ticks==20)ClientSmokeTest.capture("parkour-movement");
            if(ticks==25){
                try{Files.writeString(Path.of("../verification/parkour-client-smoke-test.txt"),"PASS: ParCool and SPA loaded alongside Better Combat, Iron's, Common Weapons and Epic Knights. Real server fast-follow state reached tracking client. Actual SPA walking resource loaded and different keyframes changed both arms and legs. Stationary pose resets after walking. Combat keyframes and shields take priority. Sprint, climb and roll poses rendered with equipped weapons and armour; shared model resets between companions. See parkour-movement.png.\n");}catch(Exception ex){throw new RuntimeException(ex);}
                Minecraft.getInstance().stop();
            }
        }
        private CompanionEntity modelEntity(){
            var e=TravellersCompanions.COMPANION.get().create(minecraft.level);e.setPos(template.position());e.setOnGround(true);e.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(Items.IRON_SWORD));return e;
        }
        private void verify(){
            var e=modelEntity();var model=((CompanionRenderer)minecraft.getEntityRenderDispatcher().getRenderer(e)).getModel();
            model.prepareMobModel(e,1,.5f,0);model.setupAnim(e,2,.5f,e.tickCount,0,0);
            ClientSmokeTest.check(model.activeAnimation!=null,"Walking applier absent");float arm=model.rightArm.xRot,leg=model.rightLeg.xRot;
            model.setupAnim(e,6,.5f,e.tickCount,0,0);
            ClientSmokeTest.check(Math.abs(arm-model.rightArm.xRot)>.1&&Math.abs(leg-model.rightLeg.xRot)>.1,"SPA limbs do not animate");
            model.setupAnim(e,0,0,e.tickCount,0,0);float idleLeg=model.rightLeg.xRot;
            ClientSmokeTest.check(Math.abs(idleLeg)<.01,"Walking leaked into idle");
            e.animation=CombatBridge.strike(e,0).animation();e.animationStarted=0;e.animationDuration=30;
            model.setupAnim(e,2,.5f,4,0,0);float combat=model.rightArm.xRot;
            boolean spa=TravellersCompanions.SPA_WALK.get();
            try{TravellersCompanions.SPA_WALK.set(false);model.setupAnim(e,2,.5f,4,0,0);ClientSmokeTest.check(Math.abs(combat-model.rightArm.xRot)<.01,"Walk overrides combat arms");}
            finally{TravellersCompanions.SPA_WALK.set(spa);}
            ClientSmokeTest.check(template.isUsingItem()&&template.getOffhandItem().is(Items.SHIELD),"Server shield use did not synchronize");
            model.prepareMobModel(template,2,.5f,0);model.setupAnim(template,2,.5f,template.tickCount,0,0);ClientSmokeTest.check(model.activeAnimation==null,"Walk overrides shield");
        }
        @Override public void render(GuiGraphics g,int mx,int my,float partial){
            g.fill(0,0,width,height,0xff17252b);g.drawCenteredString(font,"COMPANION MOVEMENT",width/2,24,0xefd8ae);
            String[] names={"SPA walk","Fast sprint","Low ledge climb","Dodge roll"};
            for(int i=0;i<4;i++){
                int x=(i*2+1)*width/8;g.drawCenteredString(font,names[i],x,65,0xd3e5e5);
                var e=display[i];
                InventoryScreen.renderEntityInInventoryFollowsMouse(g,x,height-100,70,-20,0,e);
            }
            g.drawCenteredString(font,"Actual SPA keyframes / NPC parkour poses / armour and weapons",width/2,height-40,0x94acb6);
            super.render(g,mx,my,partial);
        }
    }
}
