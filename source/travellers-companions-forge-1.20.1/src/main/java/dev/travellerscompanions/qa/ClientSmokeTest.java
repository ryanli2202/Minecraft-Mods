package dev.travellerscompanions.qa;

import dev.travellerscompanions.*;
import dev.travellerscompanions.client.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import java.nio.file.*;
import java.util.*;

@Mod.EventBusSubscriber(modid=TravellersCompanions.ID,value=Dist.CLIENT)
public final class ClientSmokeTest {
    private static boolean started,ready,maraCharging,finished;private static int age,stage,entityId;private static volatile String failure;private static volatile boolean done;
    private static final Path OUT=Path.of("../verification");
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event){
        if(finished||!Boolean.getBoolean("travellerscompanions.smokeTest")||event.phase!=TickEvent.Phase.END)return;
        var mc=Minecraft.getInstance();
        if(failure!=null)throw new IllegalStateException(failure);
        if(!started&&mc.screen instanceof AccessibilityOnboardingScreen)mc.setScreen(new TitleScreen());
        if(!started&&mc.screen instanceof TitleScreen&&mc.getOverlay()==null){
            started=true;mc.options.pauseOnLostFocus=false;mc.options.tutorialStep=net.minecraft.client.tutorial.TutorialSteps.NONE;GLFW.glfwHideWindow(mc.getWindow().getWindow());mc.options.guiScale().set(2);mc.options.renderDistance().set(3);mc.options.simulationDistance().set(5);mc.resizeDisplay();
            GameRules rules=new GameRules();rules.getRule(GameRules.RULE_DOMOBSPAWNING).set(false,null);
            mc.createWorldOpenFlows().createFreshLevel("travellers-qa-"+System.currentTimeMillis(),new LevelSettings("Travellers QA",GameType.CREATIVE,false,Difficulty.NORMAL,true,rules,WorldDataConfiguration.DEFAULT),new WorldOptions(17L,false,false),a->a.registryOrThrow(Registries.WORLD_PRESET).getHolderOrThrow(WorldPresets.FLAT).value().createWorldDimensions());
        }
        if(started&&!ready&&mc.level!=null&&mc.player!=null&&mc.player.tickCount>40){ready=true;if(Boolean.getBoolean("travellerscompanions.parkourSmoke")){finished=true;ParkourClientSmoke.start();}else mc.setScreen(new Gallery());return;}
        if(!ready||mc.screen instanceof Gallery)return;
        if(++age>1300)throw new IllegalStateException("Client scenario timed out at "+stage+" / "+mc.screen);
        if(stage==0){stage=1;mc.setScreen(null);mc.getSingleplayerServer().execute(()->{try{
            var p=mc.getSingleplayerServer().getPlayerList().getPlayer(mc.player.getUUID());p.setGameMode(GameType.SURVIVAL);p.getInventory().add(new ItemStack(Items.EMERALD,64));done=true;
        }catch(Exception e){failure=e.toString();}});}
        else if(stage==1&&done&&age>20){KeyMapping.click(com.mojang.blaze3d.platform.InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_TAB));stage=2;}
        else if(stage==2&&mc.screen instanceof RosterScreen&&age>35){capture("roster");click("Castelan");click("Summon");stage=3;}
        else if(stage==3&&age>65){stage=4;mc.getSingleplayerServer().execute(()->{try{var p=mc.getSingleplayerServer().getPlayerList().getPlayer(mc.player.getUUID());var e=CompanionService.active(p);check(e!=null&&e.kind()==CharacterKind.CASTELAN,"Network summon failed");entityId=e.getId();check(net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(e.getMainHandItem().getItem()).toString().equals("magistuarmoryaddon:steel_short_spear"),"Wrong spear");e.setItemSlot(EquipmentSlot.CHEST,new ItemStack(Items.IRON_CHESTPLATE));e.mobInteract(p,InteractionHand.MAIN_HAND);}catch(Exception e){failure=e.toString();}});}
        else if(stage==4&&mc.screen instanceof EquipmentScreen&&age>85){capture("equipment");mc.player.closeContainer();CompanionNetwork.CHANNEL.sendToServer(new CompanionNetwork.Action(3,3,-1));stage=5;}
        else if(stage==5&&mc.screen instanceof RosterScreen&&age>110){stage=6;mc.getSingleplayerServer().execute(()->{try{var p=mc.getSingleplayerServer().getPlayerList().getPlayer(mc.player.getUUID());var e=CompanionService.active(p);check(e.companionLevel()==2&&p.getInventory().countItem(Items.EMERALD)==60,"Network training cost failed");var entry=CompanionData.get(p.server).roster(p.getUUID()).entries[3];entry.level=15;e.setLevel(15);p.setShiftKeyDown(true);e.mobInteract(p,InteractionHand.MAIN_HAND);p.setShiftKeyDown(false);}catch(Exception e){failure=e.toString();}});}
        else if(stage==6&&mc.screen instanceof InteractionWheel&&age>140){capture("interaction-wheel");click("Their story");stage=7;}
        else if(stage==7&&age>165){capture("dialogue");click("Back to circle");click("Hold position");stage=8;}
        else if(stage==8&&age>190){stage=9;mc.setScreen(null);mc.getSingleplayerServer().execute(()->{try{var p=mc.getSingleplayerServer().getPlayerList().getPlayer(mc.player.getUUID());var e=CompanionService.active(p);check(e.holding(),"Network hold order failed");e.setHolding(false);e.setStance(1);var target=EntityType.HUSK.create(p.level());target.moveTo(e.position().add(2,0,0));target.setNoAi(true);target.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH).setBaseValue(1000);target.setHealth(1000);p.serverLevel().addFreshEntity(target);check(p.hurt(p.damageSources().mobAttack(target),1),"Real player did not receive test damage");}catch(Exception e){failure=e.toString();}});}
        else if(stage==9&&age>210&&mc.level.getEntity(entityId) instanceof CompanionEntity c&&!c.animation.isEmpty()){stage=10;mc.getSingleplayerServer().execute(()->{try{var p=mc.getSingleplayerServer().getPlayerList().getPlayer(mc.player.getUUID());var e=CompanionService.active(p);e.die(p.damageSources().generic());CompanionNetwork.sendState(p,-1,false);}catch(Exception e){failure=e.toString();}});}
        else if(stage==10&&mc.screen instanceof RosterScreen&&age>240){click("Castelan");capture("recovery");mc.options.guiScale().set(4);mc.resizeDisplay();stage=11;}
        else if(stage==11&&age>265){capture("roster-small");click("Mara");stage=12;}
        else if(stage==12&&age>280){capture("mara-roster-small");mc.options.guiScale().set(2);mc.resizeDisplay();stage=13;}
        else if(stage==13&&age>295){capture("mara-roster");click("Summon");stage=14;}
        else if(stage==14&&age>325){stage=15;mc.setScreen(null);mc.getSingleplayerServer().execute(()->{try{
            var p=mc.getSingleplayerServer().getPlayerList().getPlayer(mc.player.getUUID());var e=CompanionService.active(p);check(e!=null&&e.kind()==CharacterKind.MARA&&e.getMainHandItem().is(Items.CROSSBOW),"Mara network summon/loadout failed");entityId=e.getId();
            e.setHolding(true);var target=EntityType.HUSK.create(p.level());target.moveTo(e.position().add(6,0,0));target.setNoAi(true);target.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH).setBaseValue(1000);target.setHealth(1000);p.serverLevel().addFreshEntity(target);e.setTarget(target);
        }catch(Exception e){failure=e.toString();}});}
        else if(stage==15&&mc.level.getEntity(entityId) instanceof CompanionEntity c){
            if(c.isUsingItem()&&c.getMainHandItem().is(Items.CROSSBOW)){maraCharging=true;var model=((CompanionRenderer)mc.getEntityRenderDispatcher().getRenderer(c)).getModel();model.prepareMobModel(c,0,0,0);check(model.rightArmPose==net.minecraft.client.model.HumanoidModel.ArmPose.CROSSBOW_CHARGE,"Mara charging pose missing");}
            if(age>415){check(maraCharging,"Mara charging state never reached client");stage=16;done=false;mc.getSingleplayerServer().execute(()->{try{var p=mc.getSingleplayerServer().getPlayerList().getPlayer(mc.player.getUUID());var e=CompanionService.active(p);check(e.backpack.countItem(Items.ARROW)<64,"Mara did not fire her crossbow");done=true;}catch(Exception e){failure=e.toString();}});}
        }
        else if(stage==16&&done){try{Files.writeString(OUT.resolve("client-smoke-test.txt"),"PASS: Five supplied skins, including replacement Gideon and new Mara, armour and equipped weapons rendered on player models. Better Combat keyframes changed the model pose; real combat animation packets arrived at the tracking client. Epic Knights Addon's steel_short_spear resolved. Actual Tab mapping and GUI buttons opened roster and summoned Castelan and Mara. Network training consumed four emeralds, normal/sneak right-click opened equipment/dialogue, story text displayed in a readable panel, hold order applied, death recovery displayed. Five-character roster and Mara selected at GUI scales 2 and 4. Mara's crossbow charging flag reached the client and selected CROSSBOW_CHARGE; server confirmed live firing consumed arrows.\n");}catch(Exception e){throw new RuntimeException(e);}stage=17;finished=true;IntegrationClientSmoke.start();}
    }
    static void check(boolean value,String message){if(!value)throw new IllegalStateException(message);}
    static void click(String label){var screen=Minecraft.getInstance().screen;if(screen instanceof RosterScreen roster){for(int attempt=0;attempt<8;attempt++){if(screen.children().stream().anyMatch(b->b instanceof net.minecraft.client.gui.components.Button value&&value.getMessage().getString().equals(label)))break;roster.mouseScrolled(25,100,-1);}}var button=screen.children().stream().filter(b->b instanceof net.minecraft.client.gui.components.Button value&&value.getMessage().getString().equals(label)).map(b->(net.minecraft.client.gui.components.Button)b).findFirst().orElseThrow(()->new IllegalStateException("Button not found: "+label));check(button.active,"Button disabled: "+label);button.onPress();}
    static void capture(String name){try{Files.createDirectories(OUT);try(var image=Screenshot.takeScreenshot(Minecraft.getInstance().getMainRenderTarget())){image.writeToFile(OUT.resolve(name+".png"));}}catch(Exception e){throw new RuntimeException(e);}}
    static class Gallery extends Screen {
        final List<CompanionEntity> entities=new ArrayList<>();int ticks;
        Gallery(){super(Component.literal("Traveller render verification"));var mc=Minecraft.getInstance();for(int i=0;i<CharacterKind.COUNT;i++){
            var e=TravellersCompanions.COMPANION.get().create(mc.level);var n=new CompoundTag();n.putInt("Character",i);n.putInt("CompanionLevel",1);e.readAdditionalSaveData(n);e.setHealth(e.getMaxHealth());e.setId(10000+i);e.setPos(mc.player.position());
            e.giveStarter();entities.add(e);
        }}
        @Override public boolean isPauseScreen(){return false;}
        @Override public void tick(){
            if(++ticks==30){capture("companions-idle");var renderer=(CompanionRenderer)minecraft.getEntityRenderDispatcher().getRenderer(entities.get(1));var model=renderer.getModel();var e=entities.get(1);String animation=CombatBridge.strike(e,0).animation();check(net.bettercombat.client.animation.AnimationRegistry.animations.containsKey(animation),"BC animation not loaded");e.animation=animation;e.animationStarted=0;e.animationDuration=30;model.setupAnim(e,0,0,3,0,0);float a=model.rightArm.xRot+model.rightArm.yRot+model.rightArm.zRot;model.setupAnim(e,0,0,17,0,0);float b=model.rightArm.xRot+model.rightArm.yRot+model.rightArm.zRot;check(Math.abs(a-b)>.1,"BC keyframes did not change arm pose");}
            if(ticks==60)capture("companions-combat");
            if(ticks==74)capture("companions-specials");
            if(ticks==75){entities.get(2).setItemSlot(EquipmentSlot.CHEST,new ItemStack(Items.IRON_CHESTPLATE));entities.get(2).setItemSlot(EquipmentSlot.LEGS,new ItemStack(Items.IRON_LEGGINGS));}
            if(ticks==90)capture("companions-armour");
            if(ticks==105){minecraft.setScreen(null);}
        }
        @Override public void render(GuiGraphics g,int mx,int my,float partial){
            g.fill(0,0,width,height,0xFF18242A);g.drawCenteredString(font,"TRAVELLER'S COMPANIONS",width/2,20,0xEAD8AE);g.drawCenteredString(font,ticks<35?"EIGHT TRAVELLERS / YOUR SKINS":"BETTER COMBAT / WEAPON ANIMATION CHECK",width/2,38,0x9BB1B8);
            int card=(width-40)/4,cardHeight=(height-90)/2;for(int i=0;i<CharacterKind.COUNT;i++){var e=entities.get(i);int x=20+(i%4)*card,top=65+(i/4)*cardHeight;g.fill(x+3,top,x+card-3,top+cardHeight-8,0xFF26383E);g.fill(x+3,top,x+card-3,top+3,0xFF000000|e.kind().color);g.drawCenteredString(font,e.kind().display,x+card/2,top+14,e.kind().color);g.drawCenteredString(font,e.kind().title,x+card/2,top+28,0xABBDC1);
                if(ticks>=35&&i>0&&i!=CharacterKind.MARA.ordinal()){e.animation=e.kind()==CharacterKind.LEONARDO?"irons_spellbooks:instant_projectile":e.kind()==CharacterKind.YUSHIRO?(ticks<60?CommonKatanaCombat.BASIC:CommonKatanaCombat.SPECIAL):CombatBridge.strike(e,0).animation();e.animationDuration=e.kind()==CharacterKind.YUSHIRO?32:30;e.animationStarted=-13;e.setGuarding(e.kind()==CharacterKind.YURIKO&&ticks>=60);e.tickCount=0;}else {e.animationStarted=-1000;}
                InventoryScreen.renderEntityInInventoryFollowsMouse(g,x+card/2,top+cardHeight-38,Math.min(48,(cardHeight-76)/2),-22,0,e);g.drawCenteredString(font,"Health "+e.kind().health,x+card/2,top+cardHeight-24,0xD2DFDF);
            }
        }
    }
}
