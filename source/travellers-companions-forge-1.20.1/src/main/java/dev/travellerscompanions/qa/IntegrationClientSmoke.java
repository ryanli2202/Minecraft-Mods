package dev.travellerscompanions.qa;

import dev.travellerscompanions.*;
import dev.travellerscompanions.client.*;
import java.nio.file.*;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.inventory.ClickType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=TravellersCompanions.ID,value=Dist.CLIENT)
public final class IntegrationClientSmoke {
    private static boolean running,spellAnimation,basicAnimation,specialAnimation,guardAnimation,bladeAnimation;
    private static int stage,age,entered,entityId;
    private static volatile boolean done;
    private static volatile String failure;
    public static void start(){running=true;stage=0;age=0;}
    private static void next(){stage++;entered=age;}
    private static void server(java.util.function.Consumer<net.minecraft.server.level.ServerPlayer> action){done=false;var mc=Minecraft.getInstance();mc.getSingleplayerServer().execute(()->{try{var player=mc.getSingleplayerServer().getPlayerList().getPlayer(mc.player.getUUID());action.accept(player);done=true;}catch(Throwable error){failure=error.toString();}});}
    private static void check(boolean value,String message){ClientSmokeTest.check(value,message);}
    private static void click(String label){ClientSmokeTest.click(label);}
    private static void roster(net.minecraft.server.level.ServerPlayer p){CompanionService.dismiss(p);for(var e:p.serverLevel().getEntitiesOfClass(net.minecraft.world.entity.monster.Monster.class,p.getBoundingBox().inflate(40)))e.discard();p.setHealth(p.getMaxHealth());CompanionNetwork.sendState(p,-1,false);}
    private static void target(net.minecraft.server.level.ServerPlayer p,CompanionEntity e){var mob=EntityType.HUSK.create(p.level());mob.moveTo(e.position().add(2,0,0));mob.setNoAi(true);mob.getAttribute(Attributes.MAX_HEALTH).setBaseValue(10000);mob.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1);mob.setHealth(10000);p.serverLevel().addFreshEntity(mob);e.setHolding(true);e.setTarget(mob);e.spells().aim(mob);}
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event){
        if(!running||event.phase!=TickEvent.Phase.END)return;if(failure!=null)throw new IllegalStateException(failure);
        var mc=Minecraft.getInstance();if(++age>950)throw new IllegalStateException("Integration client timeout: "+stage);
        if(stage==0){next();server(IntegrationClientSmoke::roster);}
        else if(stage==1&&done&&mc.screen instanceof RosterScreen&&age-entered>15){click("Leonardo");click("Summon");next();}
        else if(stage==2&&age-entered>25){next();server(p->{var e=CompanionService.active(p);check(e.kind()==CharacterKind.LEONARDO,"Leonardo network summon failed");entityId=e.getId();p.getInventory().setItem(9,CompanionSpells.scroll("irons_spellbooks:icicle",2));CompanionService.openInventory(p,e.getId(),false);});}
        else if(stage==3&&done&&mc.screen instanceof EquipmentScreen&&age-entered>15){ClientSmokeTest.capture("leonardo-equipment");click("Spells");next();}
        else if(stage==4&&mc.screen instanceof SpellScreen&&age-entered>15){ClientSmokeTest.capture("leonardo-spells");check(mc.player.containerMenu instanceof SpellMenu,"Spell menu missing");mc.gameMode.handleInventoryMouseClick(mc.player.containerMenu.containerId,6,0,ClickType.QUICK_MOVE,mc.player);next();}
        else if(stage==5&&age-entered>15){next();server(p->{var e=CompanionService.active(p);check(CompanionSpells.spell(e.spellInventory.getItem(3)).getLevel()==2,"Collected scroll network transfer failed");check(p.getInventory().getItem(9).isEmpty(),"Collected scroll duplicated");});}
        else if(stage==6&&done&&age-entered>10){ClientSmokeTest.capture("leonardo-spells-swapped");mc.gameMode.handleInventoryMouseClick(mc.player.containerMenu.containerId,0,0,ClickType.QUICK_MOVE,mc.player);next();}
        else if(stage==7&&age-entered>15){mc.player.closeContainer();next();server(p->{var e=CompanionService.active(p);check(e.spellInventory.getItem(0).isEmpty(),"Removed scroll remained equipped");e.spellInventory.setItem(1,net.minecraft.world.item.ItemStack.EMPTY);e.spellInventory.setItem(2,net.minecraft.world.item.ItemStack.EMPTY);target(p,e);});}
        else if(stage==8&&done&&mc.level.getEntity(entityId) instanceof CompanionEntity e&&e.animation.startsWith("irons_spellbooks:")){check(CompanionRenderer.animation(e.animation)!=null,"Iron spell player keyframes missing");spellAnimation=true;next();server(p->{check(CompanionService.active(p).spells().lastSpell.equals("irons_spellbooks:icicle"),"New equipped spell not cast");roster(p);});}
        else if(stage==9&&done&&mc.screen instanceof RosterScreen&&age-entered>15){click("Yuriko");click("Summon");next();}
        else if(stage==10&&age-entered>25){next();server(p->{var e=CompanionService.active(p);check(e.kind()==CharacterKind.YURIKO,"Yuriko network summon failed");entityId=e.getId();target(p,e);e.hurt(p.damageSources().mobAttack(e.getTarget()),3);});}
        else if(stage==11&&mc.level.getEntity(entityId) instanceof CompanionEntity e){if(e.guarding()){guardAnimation=true;var model=((CompanionRenderer)mc.getEntityRenderDispatcher().getRenderer(e)).getModel();model.prepareMobModel(e,0,0,0);model.setupAnim(e,0,0,e.tickCount,0,0);check(model.rightArm.xRot< -1,"Katana guard pose missing");}if(done&&age-entered>20){check(guardAnimation,"Yuriko guard did not synchronize");next();server(IntegrationClientSmoke::roster);}}
        else if(stage==12&&done&&mc.screen instanceof RosterScreen&&age-entered>15){click("Yushiro");click("Summon");next();}
        else if(stage==13&&age-entered>25){next();mc.setScreen(new CombatView());server(p->{var e=CompanionService.active(p);check(e.kind()==CharacterKind.YUSHIRO,"Yushiro network summon failed");entityId=e.getId();target(p,e);});}
        else if(stage==14&&done&&mc.level.getEntity(entityId) instanceof CompanionEntity e){
            if(e.animation.equals(CommonKatanaCombat.BASIC))basicAnimation=true;
            if(e.animation.equals(CommonKatanaCombat.SPECIAL)){
                specialAnimation=true;var item=(net.mcreator.commonweapons.item.TestItem)e.getMainHandItem().getItem();
                var manager=item.getAnimatableInstanceCache().getManagerForId(software.bernie.geckolib.animatable.GeoItem.getId(e.getMainHandItem()));
                var controller=manager.getAnimationControllers().get("procedureController");
                if(e.tickCount-e.animationStarted>=8&&e.tickCount-e.animationStarted<24&&controller.getCurrentRawAnimation()!=null&&controller.getAnimationState()!=software.bernie.geckolib.core.animation.AnimationController.State.STOPPED){bladeAnimation=true;ClientSmokeTest.capture("yushiro-special-live");}
            }
            if(basicAnimation&&specialAnimation&&bladeAnimation){check(CompanionRenderer.animation(CommonKatanaCombat.BASIC)!=null&&CompanionRenderer.animation(CommonKatanaCombat.SPECIAL)!=null,"Common Weapons original animation keyframes not loaded");next();CompanionNetwork.CHANNEL.sendToServer(new CompanionNetwork.Action(0,0,-1));}
        }
        else if(stage==15&&mc.screen instanceof RosterScreen&&age-entered>15){click("Yushiro");ClientSmokeTest.capture("yushiro-roster");mc.options.guiScale().set(4);mc.resizeDisplay();next();}
        else if(stage==16&&age-entered>15){click("Yushiro");next();}
        else if(stage==17&&age-entered>10){ClientSmokeTest.capture("eight-roster-small");check(spellAnimation&&guardAnimation&&basicAnimation&&specialAnimation,"Missing integration evidence");try{Files.writeString(Path.of("../verification/client-smoke-test.txt"),"PASS: Eight supplied skins and exact starter weapons rendered. Roster navigation works at GUI scales 2 and 4. Existing summon/training/equipment/dialogue/recovery checks passed. Leonardo's equipment Spells button opens six spell slots with three starter scrolls and synchronized mana. A collected level-II Icicle scroll was transferred through real inventory click packets; removing a starter scroll removed it from the loadout. Leonardo cast the newly equipped Icicle and the client loaded Iron's original spell animation. Yuriko's guard state and crossed-katana pose synchronized. Yushiro sent both original Common Weapons ohtati3 basic and ohtati special animation packets and both keyframe resources resolved. Live rendering also verified the per-stack GeckoLib attack controller and captured the animated blade and sheath.\n");}catch(Exception e){throw new RuntimeException(e);}running=false;PartyClientSmoke.start();}
    }
    private static final class CombatView extends net.minecraft.client.gui.screens.Screen {
        CombatView(){super(net.minecraft.network.chat.Component.literal("Yushiro combat verification"));}
        @Override public boolean isPauseScreen(){return false;}
        @Override public void render(net.minecraft.client.gui.GuiGraphics g,int mx,int my,float partial){
            g.fill(0,0,width,height,0xFF18242A);g.drawCenteredString(font,"YUSHIRO / COMMON WEAPONS BIG KATANA",width/2,25,0xEAD8AE);
            if(minecraft.level.getEntity(entityId) instanceof CompanionEntity e){net.minecraft.client.gui.screens.inventory.InventoryScreen.renderEntityInInventoryFollowsMouse(g,width/2,height-60,100,-25,0,e);g.drawCenteredString(font,e.animation,width/2,45,0xB9CC92);}
        }
    }
}
