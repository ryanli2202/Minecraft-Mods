package dev.emeraldcovenant.qa;

import dev.emeraldcovenant.*;
import dev.emeraldcovenant.client.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.Difficulty;
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
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid=Covenant.ID,value=Dist.CLIENT)
public final class ClientSmokeTest {
    private static boolean started;
    private static int step,ticks,overall;
    private static CompletableFuture<Void> serverChecks;
    private static int walletBefore;
    private static ShopScreen screen;
    private static Offer scrollPurchase;
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event) {
        if(!Boolean.getBoolean("emeraldcovenant.smokeTest")||event.phase!=TickEvent.Phase.END||step==99)return;
        Minecraft mc=Minecraft.getInstance();
        try {
            if(++overall>12000)throw new IllegalStateException("QA timed out at step "+step);
            if(!started&&mc.screen instanceof TitleScreen&&mc.getOverlay()==null) {
                started=true;mc.options.pauseOnLostFocus=false;GLFW.glfwHideWindow(mc.getWindow().getWindow());
                mc.options.guiScale().set(2);mc.options.renderDistance().set(2);mc.options.simulationDistance().set(5);mc.resizeDisplay();
                mc.getTutorial().setStep(net.minecraft.client.tutorial.TutorialSteps.NONE);
                GameRules rules=new GameRules();rules.getRule(GameRules.RULE_DOMOBSPAWNING).set(false,null);
                mc.createWorldOpenFlows().createFreshLevel("emerald-covenant-qa-"+System.currentTimeMillis(),new LevelSettings("Covenant QA",GameType.SURVIVAL,false,Difficulty.PEACEFUL,true,rules,WorldDataConfiguration.DEFAULT),new WorldOptions(91,false,false),a->a.registryOrThrow(Registries.WORLD_PRESET).getHolderOrThrow(WorldPresets.FLAT).value().createWorldDimensions());
            }
            if(mc.level==null||mc.player==null||mc.player.tickCount<40)return;
            if(step==0){var server=mc.getSingleplayerServer();serverChecks=server.submit(()->{try{ShopChecks.run(server.getPlayerList().getPlayer(mc.player.getUUID()));}catch(Exception e){throw new RuntimeException(e);}});step=1;return;}
            if(step==1){if(!serverChecks.isDone())return;serverChecks.join();KeyMapping.click(ClientEvents.OPEN.getKey());step=2;ticks=0;return;}
            if(step==2){if(!(mc.screen instanceof ShopScreen s)||ClientShop.loading)return;screen=s;ShopChecks.check(ClientShop.offers.size()==ShopServer.catalogue.offers.size(),"H key opens shop and all chunked offers arrive");screen.setQuery("@minecraft diamond sword");ShopChecks.check(screen.resultCount()==1,"Combined name and mod search");screen.setQuery("");screen.selectKey("item:minecraft:diamond_sword");step=3;ticks=0;return;}
            if(++ticks<25)return;
            if(step==3){capture("01-weapons.png");walletBefore=Transactions.emeralds(mc.player.getInventory());ClientShop.buy(screen.selected(),1);step=4;ticks=0;return;}
            if(step==4){if(ClientShop.pending)return;ShopChecks.check(ClientShop.success,"Real client-to-server purchase succeeds");ShopChecks.check(Transactions.emeralds(mc.player.getInventory())==walletBefore-screen.selected().price(),"Network purchase debits exact server price");ShopChecks.check(mc.player.getInventory().countItem(Items.DIAMOND_SWORD)==1,"Network purchase delivers sword");ShopNetwork.toServer(new ShopNetwork.Buy(ClientShop.token,ClientShop.sequence,screen.selected().key(),1));step=5;ticks=0;return;}
            if(step==5){ShopChecks.check(mc.player.getInventory().countItem(Items.DIAMOND_SWORD)==1,"Duplicate purchase packet does not buy twice");screen.setCategory(Category.ARMOUR);screen.selectKey("item:minecraft:diamond_chestplate");ClientShop.buy(screen.selected(),1);step=6;ticks=0;return;}
            if(step==6){if(ClientShop.pending)return;ShopChecks.check(!ClientShop.success&&mc.player.getInventory().countItem(Items.DIAMOND_CHESTPLATE)==0,"Locked armour rejected by server");capture("02-armour.png");screen.setCategory(Category.BEARINGS);screen.selectKey("item:emeraldcovenant:warden_bell_bearing");ClientShop.buy(screen.selected(),1);step=7;ticks=0;return;}
            if(step==7){if(ClientShop.pending)return;ShopChecks.check(ClientShop.success,"Bearing purchased through network");ShopNetwork.toServer(new ShopNetwork.Action(ClientShop.token,1));step=8;ticks=0;return;}
            if(step==8){ShopChecks.check((ClientShop.mask&2)!=0,"Offer-bearing network action unlocks armour");capture("03-bell-bearings.png");screen.setCategory(Category.FOOD);screen.selectKey("item:minecraft:bread");step=9;ticks=0;return;}
            if(step==9){capture("04-provisions.png");screen.setCategory(Category.SPELLS);step=10;ticks=0;return;}
            if(step==10){capture("05-spells.png");scrollPurchase=ClientShop.offers.stream().filter(e->e.category()==Category.SPELLS&&e.key().startsWith("spell:")&&e.unlocked(ClientShop.mask)&&e.price()<100).findFirst().orElse(null);if(scrollPurchase!=null)ClientShop.buy(scrollPurchase,1);screen.setCategory(Category.SPELLBOOKS);step=11;ticks=0;return;}
            if(step==11){if(ClientShop.pending)return;if(scrollPurchase!=null){ShopChecks.check(ClientShop.success,"Populated spell scroll purchased through real network");ShopChecks.check(mc.player.getInventory().items.stream().anyMatch(s->ItemStack.isSameItemSameTags(s,scrollPurchase.stack())),"Purchased scroll preserves exact spell NBT");}capture("06-spellbooks.png");mc.options.guiScale().set(4);mc.resizeDisplay();screen.setCategory(Category.WEAPONS);step=12;ticks=0;return;}
            if(step==12){capture("07-large-gui-scale.png");screen.onClose();ShopChecks.check(mc.screen==null,"Escape/close exits shop");Files.writeString(Path.of("../verification/client-smoke-test.txt"),ShopChecks.REPORT.toString());step=99;mc.stop();}
        }catch(Throwable e){Covenant.LOG.error("COVENANT QA FAILED at step "+step,e);try{Files.createDirectories(Path.of("../verification"));Files.writeString(Path.of("../verification/FAILED.txt"),"Step "+step+"\n"+e);}catch(Exception ignored){}step=99;mc.stop();}
    }
    private static void capture(String name)throws Exception{Files.createDirectories(Path.of("../verification"));try(var img=Screenshot.takeScreenshot(Minecraft.getInstance().getMainRenderTarget())){img.writeToFile(Path.of("../verification",name));}}
}
