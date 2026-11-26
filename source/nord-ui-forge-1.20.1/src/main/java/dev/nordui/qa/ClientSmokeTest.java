package dev.nordui.qa;

import dev.nordui.*;
import dev.nordui.client.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.world.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.ClickType;
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
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid=NordUI.ID,value=Dist.CLIENT)
public final class ClientSmokeTest {
    private static int step,ticks,total;
    private static boolean started;
    private static String world;
    private static CompletableFuture<Void> work;
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event) {
        if(!Boolean.getBoolean("nordui.smokeTest")||event.phase!=TickEvent.Phase.END||step==99)return;
        var mc=Minecraft.getInstance();
        try {
            if(++total>12000)throw new AssertionError("Client timeout at step "+step);
            if(total%200==0)NordUI.LOG.info("NORD QA step {} screen {}",step,mc.screen==null?"none":mc.screen.getClass().getSimpleName());
            if(!started&&mc.screen instanceof AccessibilityOnboardingScreen){mc.options.onboardAccessibility=false;mc.options.save();mc.setScreen(new TitleScreen());}
            if(!started&&mc.screen instanceof TitleScreen&&mc.getOverlay()==null){
                if(ticks==0){GLFW.glfwHideWindow(mc.getWindow().getWindow());mc.options.pauseOnLostFocus=false;mc.options.guiScale().set(2);mc.options.renderDistance().set(2);mc.resizeDisplay();}
                if(++ticks<40)return;ticks=0;started=true;
                GLFW.glfwHideWindow(mc.getWindow().getWindow());mc.options.pauseOnLostFocus=false;
                mc.getTutorial().setStep(net.minecraft.client.tutorial.TutorialSteps.NONE);
                capture("01-title.png");
                var rules=new GameRules();rules.getRule(GameRules.RULE_DOMOBSPAWNING).set(false,null);rules.getRule(GameRules.RULE_KEEPINVENTORY).set(true,null);
                world="nord-qa-"+System.currentTimeMillis();
                mc.createWorldOpenFlows().createFreshLevel(world,new LevelSettings("NORD QA",GameType.SURVIVAL,false,Difficulty.NORMAL,true,rules,WorldDataConfiguration.DEFAULT),new WorldOptions(42,false,false),r->r.registryOrThrow(Registries.WORLD_PRESET).getHolderOrThrow(WorldPresets.FLAT).value().createWorldDimensions());
            }
            if(step==20&&mc.level==null){step=21;mc.createWorldOpenFlows().loadLevel(new TitleScreen(),world);return;}
            if(step==50&&mc.level==null){step=51;mc.createWorldOpenFlows().loadLevel(new TitleScreen(),world);return;}
            if((step==21||step==51)&&mc.level==null&&total%30==0&&mc.screen!=null){
                if(mc.screen instanceof BackupConfirmScreen||mc.screen instanceof ConfirmScreen)mc.screen.children().stream().filter(c->c instanceof Button b&&(b.getMessage().getString().contains("Proceed")||b.getMessage().getString().contains("I know")||b.getMessage().getString().equals("Yes"))).map(c->(Button)c).findFirst().ifPresent(Button::onPress);
            }
            if(mc.player==null||mc.level==null||mc.player.tickCount<40)return;
            mc.getToasts().clear();mc.gui.getChat().clearMessages(true);GLFW.glfwSetCursorPos(mc.getWindow().getWindow(),0,0);
            if(++ticks<20)return;ticks=0;
            switch(step){
                case 0 -> {work=mc.getSingleplayerServer().submit(()->{
                    StorageChecks.run(mc.getSingleplayerServer().overworld());
                    var p=mc.getSingleplayerServer().getPlayerList().getPlayer(mc.player.getUUID());
                    p.setItemSlot(EquipmentSlot.HEAD,new ItemStack(Items.IRON_HELMET));p.setItemSlot(EquipmentSlot.CHEST,new ItemStack(Items.DIAMOND_CHESTPLATE));p.setItemSlot(EquipmentSlot.LEGS,new ItemStack(Items.IRON_LEGGINGS));p.setItemSlot(EquipmentSlot.FEET,new ItemStack(Items.IRON_BOOTS));p.setItemSlot(EquipmentSlot.OFFHAND,new ItemStack(Items.SHIELD));
                    p.getInventory().setItem(0,new ItemStack(Items.DIAMOND_SWORD));p.getInventory().setItem(1,new ItemStack(Items.BOW));p.getInventory().setItem(2,new ItemStack(Items.COOKED_BEEF,32));p.getInventory().setItem(9,new ItemStack(Items.OAK_PLANKS,64));p.getInventory().setItem(10,new ItemStack(Items.EMERALD,23));p.giveExperienceLevels(18);p.setHealth(16);
                    var data=VaultData.get(p.server);var inv=data.inventory(p.getUUID());
                    Item[] samples={Items.DIAMOND,Items.GOLD_INGOT,Items.IRON_INGOT,Items.EMERALD,Items.ENDER_PEARL,Items.AMETHYST_SHARD,Items.NETHERITE_INGOT,Items.REDSTONE,Items.LAPIS_LAZULI,Items.DIAMOND_HELMET,Items.DIAMOND_CHESTPLATE,Items.DIAMOND_LEGGINGS,Items.DIAMOND_BOOTS,Items.ENCHANTED_BOOK,Items.BLAZE_ROD,Items.GHAST_TEAR,Items.APPLE,Items.ARROW};
                    for(int i=0;i<samples.length;i++)inv.setStackInSlot(i,new ItemStack(samples[i],samples[i].getMaxStackSize()>1?16:1));
                    ItemStack last=new ItemStack(Items.NETHER_STAR);last.setHoverName(net.minecraft.network.chat.Component.literal("Last slot proof"));inv.setStackInSlot(998,last);
                });step=1;}
                case 1 -> {if(!work.isDone())return;work.join();mc.setScreen(new InventoryScreen(mc.player));step=2;}
                case 2 -> {StorageChecks.check(mc.screen instanceof NordInventoryScreen,"Vanilla inventory opening replaced by Nordic inventory");capture("02-inventory.png");((NordInventoryScreen)mc.screen).setEquipment(true);step=3;}
                case 3 -> {capture("03-equipment.png");mc.options.guiScale().set(4);mc.resizeDisplay();step=4;}
                case 4 -> {capture("04-inventory-compact.png");StorageChecks.check(mc.player.inventoryMenu.slots.stream().allMatch(s->s.x>=0&&s.y>=0&&s.x+16<308&&s.y+16<222),"All inventory slots fit compact GUI");mc.options.guiScale().set(2);mc.resizeDisplay();
                    mc.screen.children().stream().filter(c->c instanceof Button b&&b.getMessage().getString().contains("VAULT / 999")).map(c->(Button)c).findFirst().orElseThrow().onPress();step=5;}
                case 5 -> {if(!(mc.screen instanceof VaultScreen))return;StorageChecks.check(mc.player.containerMenu instanceof VaultMenu,"Inventory vault button opens server-backed menu");capture("05-vault.png");((VaultScreen)mc.screen).go(18);step=6;}
                case 6 -> {if(!(mc.screen instanceof VaultScreen v)||v.getMenu().page!=18)return;StorageChecks.check(v.getMenu().visibleSlots==27&&v.getMenu().getSlot(26).getItem().is(Items.NETHER_STAR),"Network sync reaches slot 999 on final page");capture("06-vault-last-page.png");mc.gameMode.handleInventoryMouseClick(v.getMenu().containerId,26,0,ClickType.QUICK_MOVE,mc.player);step=7;}
                case 7 -> {StorageChecks.check(mc.player.getInventory().countItem(Items.NETHER_STAR)==1&&((VaultMenu)mc.player.containerMenu).getSlot(26).getItem().isEmpty(),"Client/server shift-click retrieves slot 999 exactly once");mc.options.guiScale().set(4);mc.resizeDisplay();step=8;}
                case 8 -> {capture("07-vault-compact.png");StorageChecks.check(mc.player.containerMenu.slots.stream().allMatch(s->s.x>=0&&s.y>=0&&s.x+16<308&&s.y+16<236),"All vault and player slots fit compact GUI");mc.options.guiScale().set(2);mc.resizeDisplay();((VaultScreen)mc.screen).send(100);step=9;}
                case 9 -> {StorageChecks.check(mc.player.getInventory().getItem(9).isEmpty()&&mc.player.getInventory().getItem(0).is(Items.DIAMOND_SWORD),"Network store-backpack retains hotbar and moves backpack");mc.player.closeContainer();step=10;}
                case 10 -> {mc.setScreen(null);mc.player.setYRot(180);step=11;}
                case 11 -> {capture("08-hud.png");mc.setScreen(new PauseScreen(true));step=12;}
                case 12 -> {capture("09-pause.png");mc.setScreen(null);KeyMapping.click(ClientEvents.VAULT_KEY.getKey());step=13;}
                case 13 -> {if(!(mc.screen instanceof VaultScreen))return;StorageChecks.check(true,"V key opens vault through network");mc.player.closeContainer();work=mc.getSingleplayerServer().submit(()->mc.getSingleplayerServer().getPlayerList().getPlayer(mc.player.getUUID()).kill());step=14;}
                case 14 -> {if(!work.isDone())return;work.join();if(!mc.player.isDeadOrDying())return;mc.getConnection().send(new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.PERFORM_RESPAWN));step=15;}
                case 15 -> {if(mc.player.isDeadOrDying())return;VaultNetwork.request();step=16;}
                case 16 -> {if(!(mc.screen instanceof VaultScreen v))return;StorageChecks.check(v.getMenu().getSlot(0).getItem().is(Items.DIAMOND),"Vault retained across player death and respawn");mc.player.closeContainer();step=17;}
                case 17 -> {mc.level.disconnect();mc.clearLevel();mc.setScreen(new TitleScreen());step=20;}
                case 21 -> {VaultNetwork.request();step=22;}
                case 22 -> {if(!(mc.screen instanceof VaultScreen v))return;StorageChecks.check(v.getMenu().getSlot(0).getItem().is(Items.DIAMOND),"Vault survives real world save and reconnect");v.go(18);step=23;}
                case 23 -> {if(!(mc.screen instanceof VaultScreen v)||v.getMenu().page!=18)return;StorageChecks.check(v.getMenu().getSlot(26).getItem().isEmpty(),"Withdrawn slot remains empty after save/reconnect");mc.player.closeContainer();ClientEvents.openVanillaInventory();step=24;}
                case 24 -> {
                    StorageChecks.check(mc.screen instanceof InventoryScreen,"Recipe-book fallback opens original functional inventory");mc.player.closeContainer();
                    if(dev.nordui.compat.CuriosCompat.loaded()){
                        work=mc.getSingleplayerServer().submit(()->CuriosChecks.setup(mc.getSingleplayerServer().getPlayerList().getPlayer(mc.player.getUUID())));step=25;
                    }else step=30;
                }
                case 25 -> {if(!work.isDone())return;work.join();CuriosChecks.assertEquipped(mc.player);var s=new NordInventoryScreen(mc.player);s.equipment=true;mc.setScreen(s);step=26;}
                case 26 -> {capture("10-curios-equipment.png");StorageChecks.check(dev.nordui.compat.CuriosCompat.entries(mc.player).stream().anyMatch(e->e.item().is(Items.EMERALD)),"Equipment preview reads live Curios accessories");mc.screen.children().stream().filter(c->c instanceof Button b&&b.getMessage().getString().contains("CURIOS / ACCESSORIES")).map(c->(Button)c).findFirst().orElseThrow().onPress();step=27;}
                case 27 -> {if(!dev.nordui.compat.CuriosCompat.isScreen(mc.screen))return;StorageChecks.check(true,"Equipment button opens native synchronized Curios menu");CuriosChecks.assertEquipped(mc.player);capture("11-curios-native.png");int slot=CuriosChecks.ringSlot(mc.player);mc.gameMode.handleInventoryMouseClick(mc.player.containerMenu.containerId,slot,0,ClickType.QUICK_MOVE,mc.player);step=28;}
                case 28 -> {StorageChecks.check(dev.nordui.compat.CuriosCompat.entries(mc.player).stream().noneMatch(e->e.item().is(Items.EMERALD)),"Curios native shift-click unequips accessory");StorageChecks.check(mc.player.getInventory().countItem(Items.EMERALD)==1,"Unequipped accessory returns exactly once");mc.screen.children().stream().filter(c->c instanceof Button b&&b.getMessage().getString().contains("DEEP NIGHT EQUIPMENT")).map(c->(Button)c).findFirst().orElseThrow().onPress();step=29;}
                case 29 -> {StorageChecks.check(mc.screen instanceof NordInventoryScreen s&&s.equipment,"Curios back button returns to Nordic equipment tab");mc.player.closeContainer();step=30;}
                case 30 -> {mc.options.guiScale().set(4);mc.resizeDisplay();step=31;}
                case 31 -> {capture("12-hud-compact.png");mc.options.guiScale().set(2);mc.resizeDisplay();work=mc.getSingleplayerServer().submit(()->{var p=mc.getSingleplayerServer().getPlayerList().getPlayer(mc.player.getUUID());p.openMenu(new net.minecraft.world.SimpleMenuProvider((id,inv,player)->net.minecraft.world.inventory.ChestMenu.sixRows(id,inv,new net.minecraft.world.SimpleContainer(54)),net.minecraft.network.chat.Component.literal("Northern Cache")));});step=32;}
                case 32 -> {if(!work.isDone())return;work.join();if(!(mc.screen instanceof net.minecraft.client.gui.screens.inventory.ContainerScreen))return;capture("13-chest.png");mc.player.closeContainer();
                    if(dev.nordui.compat.IronsCompat.loaded()){
                        work=mc.getSingleplayerServer().submit(()->{var p=mc.getSingleplayerServer().getPlayerList().getPlayer(mc.player.getUUID());IronsChecks.run(p);IronsChecks.setup(p);});step=40;
                    }else finish(mc);
                }
                case 40 -> {if(!work.isDone())return;work.join();mc.setScreen(new InventoryScreen(mc.player));
                    if(dev.nordui.compat.UiCompat.loaded("jei"))StorageChecks.check(((NordInventoryScreen)mc.screen).getXSize()==308,"JEI mode reserves sidebar space in Nordic inventory");
                    StorageChecks.check(!dev.nordui.compat.UiCompat.themeScreen(new net.minecraftforge.client.gui.ModListScreen(mc.screen)),"Third-party screen subclasses keep their native styling");
                    if(dev.nordui.compat.UiCompat.rpgHud())StorageChecks.check(!ClientConfig.ownsHud(),"AUTO gives RPG-HUD ownership without canceling vanilla overlay events");
                    mc.screen.children().stream().filter(c->c instanceof Button b&&b.getMessage().getString().equals("SPELLS")).map(c->(Button)c).findFirst().orElseThrow().onPress();step=41;
                }
                case 41 -> {if(!(mc.screen instanceof dev.nordui.compat.irons.SpellScreen s))return;StorageChecks.check(s.getMenu().slots.size()==39,"Inventory spell tab opens synchronized native inscription slots");StorageChecks.check(s.getMenu().getSpellBookSlot().hasItem(),"Spell tab synchronizes equipped Curios book");s.select(0);step=42;}
                case 42 -> {var s=(dev.nordui.compat.irons.SpellScreen)mc.screen;capture("14-spells.png");StorageChecks.check(s.getMenu().getResultSlot().hasItem(),"Selecting spell synchronizes extraction preview");mc.gameMode.handleInventoryMouseClick(s.getMenu().containerId,38,0,ClickType.QUICK_MOVE,mc.player);step=43;}
                case 43 -> {var s=(dev.nordui.compat.irons.SpellScreen)mc.screen;StorageChecks.check(!s.getMenu().getResultSlot().hasItem(),"Network extraction clears output to prevent duplication");mc.gameMode.handleInventoryMouseClick(s.getMenu().containerId,0,0,ClickType.QUICK_MOVE,mc.player);step=44;}
                case 44 -> {var s=(dev.nordui.compat.irons.SpellScreen)mc.screen;StorageChecks.check(s.getMenu().getScrollSlot().hasItem(),"Shift-click moves replacement scroll into inscription input");s.send(-1);step=45;}
                case 45 -> {var s=(dev.nordui.compat.irons.SpellScreen)mc.screen;StorageChecks.check(!s.getMenu().getScrollSlot().hasItem(),"Network inscription consumes replacement scroll");mc.options.guiScale().set(4);mc.resizeDisplay();step=46;}
                case 46 -> {capture("15-spells-compact.png");StorageChecks.check(mc.player.containerMenu.slots.stream().allMatch(s->s.x>=0&&s.y>=0&&s.x+16<=308&&s.y+16<=246),"All spell and player slots fit compact screen");mc.player.closeContainer();work=mc.getSingleplayerServer().submit(()->IronsChecks.verifyReplaced(mc.getSingleplayerServer().getPlayerList().getPlayer(mc.player.getUUID())));mc.options.guiScale().set(2);mc.resizeDisplay();step=47;}
                case 47 -> {if(!work.isDone())return;work.join();capture("16-magic-hud"+(dev.nordui.compat.UiCompat.rpgHud()?"-rpg":"")+".png");mc.setScreen(new CompatibilityScreen(null));step=48;}
                case 48 -> {capture("17-compatibility.png");mc.setScreen(null);mc.level.disconnect();mc.clearLevel();mc.setScreen(new TitleScreen());step=50;}
                case 51 -> {work=mc.getSingleplayerServer().submit(()->IronsChecks.verifyReplaced(mc.getSingleplayerServer().getPlayerList().getPlayer(mc.player.getUUID())));step=52;}
                case 52 -> {if(!work.isDone())return;work.join();StorageChecks.check(true,"Edited spellbook and bound spell survive save/reconnect");finish(mc);}
            }
        }catch(Throwable ex){NordUI.LOG.error("NORD CLIENT QA FAILED at step "+step,ex);try{Files.createDirectories(Path.of("../verification"));Files.writeString(Path.of("../verification/CLIENT-FAILED.txt"),"Step "+step+"\n"+ex);}catch(Exception ignored){}step=99;mc.stop();}
    }
    private static void finish(Minecraft mc)throws Exception{
        Files.createDirectories(Path.of("../verification"));
        String suffix=dev.nordui.compat.IronsCompat.loaded()?"-irons"+(dev.nordui.compat.UiCompat.loaded("jei")?"-ui":"")+(dev.nordui.compat.UiCompat.rpgHud()?"-rpg":""):dev.nordui.compat.CuriosCompat.loaded()?"-curios":"";
        Files.writeString(Path.of("../verification/client-tests"+suffix+".txt"),StorageChecks.REPORT);step=99;mc.stop();
    }
    private static void capture(String name)throws Exception{Files.createDirectories(Path.of("../verification"));try(var image=Screenshot.takeScreenshot(Minecraft.getInstance().getMainRenderTarget())){image.writeToFile(Path.of("../verification",name));}}
}
