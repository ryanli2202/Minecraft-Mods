package dev.transmogify.qa;

import dev.transmogify.*;
import dev.transmogify.client.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import org.lwjgl.glfw.GLFW;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid=TransmogifyArmour.ID,value=Dist.CLIENT)
public final class ClientSmokeTest {
    private static int step,ticks,total,index,renderCount;
    private static boolean started;
    private static CompletableFuture<Void> work;
    private static WardrobeScreen screen;
    private static String world;
    private static List<String> modSamples;
    private static final String[] DIAMOND={"minecraft:diamond_helmet","minecraft:diamond_chestplate","minecraft:diamond_leggings","minecraft:diamond_boots"};
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event) {
        if(!Boolean.getBoolean("transmogifyarmour.smokeTest")||event.phase!=TickEvent.Phase.END||step==99)return;
        var mc=Minecraft.getInstance();
        try {
            if(++total>10000)throw new AssertionError("Client timeout at step "+step);
            if(total%100==0)TransmogifyArmour.LOG.info("QA step {} screen {}",step,mc.screen==null?"none":mc.screen.getClass().getSimpleName());
            if(!started && mc.screen instanceof net.minecraft.client.gui.screens.AccessibilityOnboardingScreen) {
                mc.options.onboardAccessibility=false;mc.options.save();mc.setScreen(new TitleScreen());
            }
            if(!started&&mc.screen instanceof TitleScreen&&mc.getOverlay()==null) {
                started=true;GLFW.glfwHideWindow(mc.getWindow().getWindow());mc.options.pauseOnLostFocus=false;
                mc.options.guiScale().set(2);mc.options.renderDistance().set(2);mc.resizeDisplay();
                mc.getTutorial().setStep(net.minecraft.client.tutorial.TutorialSteps.NONE);
                var rules=new GameRules();rules.getRule(GameRules.RULE_DOMOBSPAWNING).set(false,null);rules.getRule(GameRules.RULE_KEEPINVENTORY).set(true,null);
                world="transmog-qa-"+System.currentTimeMillis();
                mc.createWorldOpenFlows().createFreshLevel(world,new LevelSettings("Transmog QA",GameType.SURVIVAL,false,Difficulty.PEACEFUL,true,rules,WorldDataConfiguration.DEFAULT),new WorldOptions(42,false,false),r->r.registryOrThrow(Registries.WORLD_PRESET).getHolderOrThrow(WorldPresets.FLAT).value().createWorldDimensions());
            }
            if(step==13&&mc.level==null){step=14;mc.createWorldOpenFlows().loadLevel(new TitleScreen(),world);return;}
            if(step==14&&mc.level==null&&total%40==0&&mc.screen!=null) {
                if(mc.screen instanceof net.minecraft.client.gui.screens.BackupConfirmScreen||mc.screen instanceof net.minecraft.client.gui.screens.ConfirmScreen)
                    mc.screen.children().stream().filter(c->c instanceof Button b&&(b.getMessage().getString().equals("Proceed")||b.getMessage().getString().contains("I know")||b.getMessage().getString().equals("Yes"))).map(c->(Button)c).findFirst().ifPresent(Button::onPress);
            }
            if(mc.player==null||mc.level==null||mc.player.tickCount<40)return;
            if(++ticks<12)return;ticks=0;
            if(step==0) {
                work=mc.getSingleplayerServer().submit(()->{
                    ArmourChecks.run(mc.getSingleplayerServer().overworld());
                    var player=mc.getSingleplayerServer().getPlayerList().getPlayer(mc.player.getUUID());
                    Item[] iron={Items.IRON_HELMET,Items.IRON_CHESTPLATE,Items.IRON_LEGGINGS,Items.IRON_BOOTS};
                    for(int i=0;i<4;i++)player.setItemSlot(ArmourData.SLOTS[i],new ItemStack(iron[i]));
                });step=1;return;
            }
            if(step==1) { if(!work.isDone())return;work.join();KeyMapping.click(ClientEvents.OPEN.getKey());step=2;return; }
            if(step==2) {
                if(!(mc.screen instanceof WardrobeScreen s))return;screen=s;
                ArmourChecks.check(ClientEvents.OPEN.getKey().getValue()==GLFW.GLFW_KEY_EQUAL,"Equals key opens wardrobe through real network");
                screen.setSearch("diamond");screen.choose(DIAMOND[0]);screen.chooseSet();
                ArmourChecks.check(Arrays.equals(screen.choices,DIAMOND),"Matching set chooses all four diamond pieces");
                ArmourChecks.check(screen.catalogue.search(EquipmentSlot.HEAD,"@minecraft diamond").size()==1,"Search filters by mod and name");
                screen.setSearch("");mc.getToasts().clear();renderCount=ClientWardrobe.renderSubstitutions;step=3;return;
            }
            if(step==3) {
                ArmourChecks.check(ClientWardrobe.renderSubstitutions>renderCount,"Live armour render mixin executes for preview");
                ArmourChecks.check(ArmourData.appearanceId(mc.player.getItemBySlot(EquipmentSlot.HEAD)).isEmpty(),"Preview does not mutate real equipment");
                capture("01-wardrobe.png");screen.apply();step=4;return;
            }
            if(step==4) {
                if(mc.screen!=null)return;
                for(int i=0;i<4;i++)ArmourChecks.check(ArmourData.appearanceId(mc.player.getItemBySlot(ArmourData.SLOTS[i])).equals(DIAMOND[i]),"Network applies and synchronizes slot "+i);
                ArmourChecks.check(mc.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.IRON_CHESTPLATE),"Applied appearance keeps original iron armour");
                KeyMapping.click(ClientEvents.OPEN.getKey());step=5;return;
            }
            if(step==5) {
                if(!(mc.screen instanceof WardrobeScreen s))return;screen=s;
                mc.options.guiScale().set(4);mc.resizeDisplay();step=6;return;
            }
            if(step==6) {
                capture("02-compact.png");mc.options.guiScale().set(2);mc.resizeDisplay();
                modSamples=screen.catalogue.all().stream().filter(e->e.slot()==EquipmentSlot.HEAD&&!e.id().startsWith("minecraft:")).collect(java.util.stream.Collectors.toMap(e->e.id().split(":")[0],ArmourCatalogue.Entry::id,(a,b)->a,TreeMap::new)).values().stream().toList();
                step=7;return;
            }
            if(step==7) {
                if(index<modSamples.size()){screen.selectSlot(0);screen.choose(modSamples.get(index));screen.chooseSet();renderCount=ClientWardrobe.renderSubstitutions;step=8;return;}
                screen.selectSlot(0);screen.choose(DIAMOND[0]);screen.chooseSet();screen.apply();step=9;return;
            }
            if(step==8) {
                ArmourChecks.check(ClientWardrobe.renderSubstitutions>renderCount,"Modded appearance rendered: "+modSamples.get(index));
                capture("03-mod-"+modSamples.get(index).split(":")[0]+".png");index++;step=7;return;
            }
            if(step==9) { if(mc.screen!=null)return;work=mc.getSingleplayerServer().submit(()->mc.getSingleplayerServer().getPlayerList().getPlayer(mc.player.getUUID()).kill());step=10;return; }
            if(step==10) { if(!work.isDone())return;work.join();if(!mc.player.isDeadOrDying())return;mc.getConnection().send(new net.minecraft.network.protocol.game.ServerboundClientCommandPacket(net.minecraft.network.protocol.game.ServerboundClientCommandPacket.Action.PERFORM_RESPAWN));step=11;return; }
            if(step==11) { if(mc.player.isDeadOrDying())return;ArmourChecks.check(ArmourData.appearanceId(mc.player.getItemBySlot(EquipmentSlot.HEAD)).equals(DIAMOND[0]),"Death with keepInventory preserves appearance");step=12;return; }
            if(step==12) { mc.level.disconnect();mc.clearLevel();mc.setScreen(new TitleScreen());step=13;return; }
            if(step==14) {
                ArmourChecks.check(ArmourData.appearanceId(mc.player.getItemBySlot(EquipmentSlot.HEAD)).equals(DIAMOND[0]),"World save and reconnect preserve appearance");
                KeyMapping.click(ClientEvents.OPEN.getKey());step=15;return;
            }
            if(step==15) { if(!(mc.screen instanceof WardrobeScreen s))return;Arrays.fill(s.choices,"");s.apply();step=16;return; }
            if(step==16) {
                if(mc.screen!=null)return;ArmourChecks.check(ArmourData.appearanceId(mc.player.getItemBySlot(EquipmentSlot.HEAD)).isEmpty(),"Reset works through actual client/server packets");
                Files.createDirectories(Path.of("../verification"));Files.writeString(Path.of("../verification/client-"+(modSamples.isEmpty()?"vanilla":"compat")+".txt"),ArmourChecks.REPORT);step=99;mc.stop();
            }
        }catch(Throwable ex){TransmogifyArmour.LOG.error("TRANSMOG CLIENT QA FAILED at step "+step,ex);try{Files.createDirectories(Path.of("../verification"));Files.writeString(Path.of("../verification/CLIENT-FAILED.txt"),"Step "+step+"\n"+ex);}catch(Exception ignored){}step=99;mc.stop();}
    }
    private static void capture(String name)throws Exception { Files.createDirectories(Path.of("../verification"));try(var image=Screenshot.takeScreenshot(Minecraft.getInstance().getMainRenderTarget())){image.writeToFile(Path.of("../verification",name));} }
}
