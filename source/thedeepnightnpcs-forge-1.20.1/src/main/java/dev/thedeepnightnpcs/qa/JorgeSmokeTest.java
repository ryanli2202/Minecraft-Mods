package dev.thedeepnightnpcs.qa;

import dev.thedeepnightnpcs.DeepNightNpcs;
import dev.thedeepnightnpcs.client.DialogueScreen;
import dev.thedeepnightnpcs.entity.NpcEntity;
import dev.thedeepnightnpcs.network.NpcNetwork;
import dev.thedeepnightnpcs.trade.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ServerboundSelectTradePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid=DeepNightNpcs.ID,value=Dist.CLIENT)
public final class JorgeSmokeTest {
    private static int step,ticks,elapsed,jorge;
    private static boolean started;
    private static CompletableFuture<?> pending;
    private static int price,units,usesBefore,emeraldsBefore,booksBefore,levelBefore;
    private static Item bought;
    private static String ingotStock;
    private static void check(boolean condition,String message){ServerChecks.check(condition,message);}
    private static ServerPlayer player(Minecraft mc){return mc.getSingleplayerServer().getPlayerList().getPlayer(mc.player.getUUID());}
    private static NpcEntity npc(Minecraft mc){return (NpcEntity)player(mc).level().getEntity(jorge);}
    private static void task(Minecraft mc,Runnable run){pending=mc.getSingleplayerServer().submit(run);}
    private static void next(){step++;ticks=0;}
    private static void interact(Minecraft mc){mc.gameMode.interact(mc.player,mc.level.getEntity(jorge),InteractionHand.MAIN_HAND);}
    private static void key(Minecraft mc,int key){mc.screen.keyPressed(key,0,0);}
    private static void close(Minecraft mc){mc.screen.onClose();}
    private static void capture(String name)throws Exception {
        Files.createDirectories(Path.of("../verification/jorge"));
        try(var image=Screenshot.takeScreenshot(Minecraft.getInstance().getMainRenderTarget())){image.writeToFile(Path.of("../verification/jorge",name));}
    }
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event) {
        if(!Boolean.getBoolean("thedeepnightnpcs.jorgeTest")||event.phase!=TickEvent.Phase.END||step==99)return;
        Minecraft mc=Minecraft.getInstance();
        try {
            if(!started&&mc.screen instanceof TitleScreen&&mc.getOverlay()==null) {
                started=true;GLFW.glfwHideWindow(mc.getWindow().getWindow());mc.options.pauseOnLostFocus=false;
                mc.options.guiScale().set(2);mc.options.renderDistance().set(2);mc.options.simulationDistance().set(5);mc.resizeDisplay();
                mc.getTutorial().setStep(net.minecraft.client.tutorial.TutorialSteps.NONE);
                GameRules rules=new GameRules();rules.getRule(GameRules.RULE_DOMOBSPAWNING).set(false,null);
                mc.createWorldOpenFlows().createFreshLevel("jorge-qa-"+System.currentTimeMillis(),new LevelSettings("Jorge QA",GameType.SURVIVAL,false,Difficulty.NORMAL,true,rules,WorldDataConfiguration.DEFAULT),new WorldOptions(214,false,false),access->access.registryOrThrow(Registries.WORLD_PRESET).getHolderOrThrow(WorldPresets.FLAT).value().createWorldDimensions());return;
            }
            if(mc.level==null||mc.player==null||mc.player.tickCount<40)return;
            if(++elapsed>3600)throw new IllegalStateException("Timed out in step "+step);
            if(pending!=null){if(!pending.isDone())return;pending.join();pending=null;ticks=0;}
            if(++ticks<20)return;
            switch(step) {
                case 0 -> {
                    task(mc,()->{
                        try{ServerChecks.run(mc.getSingleplayerServer());}catch(Exception e){throw new RuntimeException(e);}
                        var p=player(mc);p.teleportTo(0.5,-60,1.5);p.setYRot(180);
                        var j=DeepNightNpcs.JORGE.get().create(p.level());j.moveTo(0.5,-60,0.5,0,0);p.serverLevel().addFreshEntity(j);jorge=j.getId();
                        for(int i=0;i<5;i++)p.getInventory().setItem(i,new ItemStack(Items.EMERALD,64));
                        p.getInventory().setItem(5,new ItemStack(Items.DIAMOND_PICKAXE));p.getInventory().setItem(6,new ItemStack(Items.LAPIS_LAZULI,12));p.giveExperienceLevels(35);
                        var s=JorgeService.stock(p);check(s.ingots.size()==3&&s.books.size()==5,"Three ingot offers and five books generated");
                        check(s.ingots.stream().map(o->o.getResult().getItem()).distinct().count()==3,"Ingot offers use distinct types");
                        check(s.books.stream().allMatch(o->o.getResult().is(Items.ENCHANTED_BOOK)&&!EnchantmentHelper.getEnchantments(o.getResult()).isEmpty()),"Book results contain real enchantment data");
                        for(var offer:s.books)for(var enchant:EnchantmentHelper.getEnchantments(offer.getResult()).entrySet())check(enchant.getValue()>=enchant.getKey().getMinLevel()&&enchant.getValue()<=enchant.getKey().getMaxLevel(),"Book level is within enchantment's supported range");
                        ingotStock=s.ingots.createTag().toString();JorgeService.refresh(p,false);check(ingotStock.equals(s.ingots.createTag().toString()),"Early refresh preserves stock and cooldown");
                        check(JorgeStock.get(p.server).player(UUID.randomUUID()).ingots==null,"Another player has independent stock");
                    });next();
                }
                case 1 -> {interact(mc);next();}
                case 2 -> {
                    if(!(mc.screen instanceof DialogueScreen d)||!d.kind.equals("jorge"))return;
                    check(d.library.nodes.size()==6,"Jorge's five conversation topics and greeting load");capture("01-jorge-wheel.png");key(mc,GLFW.GLFW_KEY_1);next();
                }
                case 3 -> {capture("02-ingot-stock-wheel.png");key(mc,GLFW.GLFW_KEY_1);next();}
                case 4 -> {
                    if(!(mc.screen instanceof MerchantScreen))return;
                    capture("03-ingot-shop.png");check(mc.player.containerMenu instanceof MerchantMenu,"Wheel opens native merchant screen");
                    task(mc,()->{var p=player(mc);var m=(MerchantMenu)p.containerMenu;var o=m.getOffers().get(0);price=o.getCostA().getCount();units=o.getResult().getCount();bought=o.getResult().getItem();usesBefore=o.getUses();emeraldsBefore=p.getInventory().countItem(Items.EMERALD);});next();
                }
                case 5 -> {mc.getConnection().send(new ServerboundSelectTradePacket(0));next();}
                case 6 -> {
                    // Click the real result slot once, then put the cursor stack into a free inventory slot.
                    mc.gameMode.handleInventoryMouseClick(mc.player.containerMenu.containerId,2,0,ClickType.PICKUP,mc.player);next();
                }
                case 7 -> {mc.gameMode.handleInventoryMouseClick(mc.player.containerMenu.containerId,10,0,ClickType.PICKUP,mc.player);close(mc);next();}
                case 8 -> {
                    task(mc,()->{
                        var p=player(mc);var s=JorgeService.stock(p);
                        check(p.getInventory().countItem(Items.EMERALD)==emeraldsBefore-price,"Purchase charges exact emerald price");
                        check(p.getInventory().countItem(bought)==units,"Purchase grants exact ingot quantity");
                        check(s.ingots.get(0).getUses()==usesBefore+1,"Purchase consumes one offer use");
                        check(npc(mc).getTradingPlayer()==null,"Closing merchant screen releases the counter");
                        var restored=JorgeStock.load(JorgeStock.get(p.server).save(new CompoundTag())).player(p.getUUID());
                        check(restored.ingots.createTag().equals(s.ingots.createTag())&&restored.books.createTag().equals(s.books.createTag()),"Stock, enchantments, prices and uses persist through save/load");
                        check(restored.nextIngots==s.nextIngots&&restored.nextBooks==s.nextBooks,"Restock timers persist");
                    });interact(mc);next();
                }
                case 9 -> {if(!(mc.screen instanceof DialogueScreen))return;key(mc,GLFW.GLFW_KEY_2);next();}
                case 10 -> {capture("04-book-stock-wheel.png");key(mc,GLFW.GLFW_KEY_1);next();}
                case 11 -> {
                    if(!(mc.screen instanceof MerchantScreen))return;
                    capture("05-book-shop.png");
                    task(mc,()->{var p=player(mc);booksBefore=p.getInventory().countItem(Items.ENCHANTED_BOOK);emeraldsBefore=p.getInventory().countItem(Items.EMERALD);price=((MerchantMenu)p.containerMenu).getOffers().get(0).getCostA().getCount();});
                    mc.getConnection().send(new ServerboundSelectTradePacket(0));next();
                }
                case 12 -> {mc.gameMode.handleInventoryMouseClick(mc.player.containerMenu.containerId,2,0,ClickType.PICKUP,mc.player);next();}
                case 13 -> {mc.gameMode.handleInventoryMouseClick(mc.player.containerMenu.containerId,11,0,ClickType.PICKUP,mc.player);close(mc);next();}
                case 14 -> {
                    task(mc,()->{
                        var p=player(mc);check(p.getInventory().countItem(Items.ENCHANTED_BOOK)==booksBefore+1,"Real book purchase succeeds");
                        check(p.getInventory().countItem(Items.EMERALD)==emeraldsBefore-price,"Book purchase charges exact emerald price");
                        var s=JorgeService.stock(p);var original=s.ingots.createTag();s.nextBooks=0;JorgeService.refresh(p,true);
                        check(s.books.stream().allMatch(o->o.getUses()==0),"Book refresh replenishes uses");check(original.equals(s.ingots.createTag()),"Refreshing books leaves ingot stock unchanged");
                        s.nextIngots=0;JorgeService.refresh(p,false);check(s.ingots.stream().allMatch(o->o.getUses()==0),"Ingot refresh replenishes uses");
                        for(int i=0;i<p.getInventory().items.size();i++)if(p.getInventory().getItem(i).is(Items.EMERALD))p.getInventory().setItem(i,ItemStack.EMPTY);
                    });interact(mc);next();
                }
                case 15 -> {if(!(mc.screen instanceof DialogueScreen))return;key(mc,GLFW.GLFW_KEY_1);next();}
                case 16 -> {key(mc,GLFW.GLFW_KEY_1);next();}
                case 17 -> {if(!(mc.screen instanceof MerchantScreen))return;mc.getConnection().send(new ServerboundSelectTradePacket(0));next();}
                case 18 -> {
                    task(mc,()->{
                        var p=player(mc);var m=(MerchantMenu)p.containerMenu;
                        check(m.getSlot(2).getItem().isEmpty(),"No emeralds means no purchasable output");
                        var stockBefore=JorgeService.stock(p).ingots.createTag();NpcNetwork.act(p,new NpcNetwork.Action(jorge,"refresh_ingots",""));
                        check(stockBefore.equals(JorgeService.stock(p).ingots.createTag()),"Refresh packet cannot replace stock while trading");
                        m.getOffers().get(0).setToOutOfStock();m.getSlot(0).set(new ItemStack(Items.EMERALD,64));
                        check(m.getSlot(2).getItem().isEmpty(),"Sold-out offers cannot be bought even with emeralds");
                        p.teleportTo(30,-60,30);check(!m.stillValid(p),"Shop invalidates when player leaves Jorge");p.closeContainer();
                        check(p.getInventory().countItem(Items.EMERALD)==64,"Closing shop returns unused payment emeralds");p.teleportTo(0.5,-60,1.5);
                        // Shelves around Jorge, leaving the full one-block gap clear.
                        BlockPos base=npc(mc).blockPosition();
                        for(int x=-2;x<=2;x++)for(int z=-2;z<=2;z++)if(Math.abs(x)==2||Math.abs(z)==2)p.serverLevel().setBlockAndUpdate(base.offset(x,0,z),Blocks.BOOKSHELF.defaultBlockState());
                    });next();
                }
                case 19 -> {interact(mc);next();}
                case 20 -> {if(!(mc.screen instanceof DialogueScreen))return;key(mc,GLFW.GLFW_KEY_3);next();}
                case 21 -> {
                    if(!(mc.screen instanceof EnchantmentScreen))return;
                    task(mc,()->{
                        var p=player(mc);var m=(EnchantmentMenu)p.containerMenu;
                        // Move real items from player inventory into the native menu's inputs.
                        for(int i=0;i<p.getInventory().items.size();i++)if(p.getInventory().getItem(i).is(Items.DIAMOND_PICKAXE)){m.getSlot(0).set(p.getInventory().removeItem(i,1));break;}
                        for(int i=0;i<p.getInventory().items.size();i++)if(p.getInventory().getItem(i).is(Items.LAPIS_LAZULI)){m.getSlot(1).set(p.getInventory().removeItem(i,3));break;}
                        m.broadcastChanges();levelBefore=p.experienceLevel;
                        check(m.costs[2]==30,"Bookshelves around Jorge enable the level-thirty offer");
                        check(!m.clickMenuButton(p,-1)&&!m.clickMenuButton(p,3),"Invalid enchantment option indices are rejected");
                    });next();
                }
                case 22 -> {capture("06-enchanting.png");mc.gameMode.handleInventoryButtonClick(mc.player.containerMenu.containerId,2);next();}
                case 23 -> {
                    task(mc,()->{
                        var p=player(mc);var m=(EnchantmentMenu)p.containerMenu;
                        check(m.getSlot(0).getItem().isEnchanted(),"Native enchant packet enchants the tool");
                        check(m.getSlot(1).getItem().isEmpty(),"Third enchanting offer spends three lapis");
                        check(p.experienceLevel==levelBefore-3,"Third enchanting offer spends three levels");
                    });next();
                }
                case 24 -> {capture("07-enchanted-tool.png");close(mc);next();}
                case 25 -> {
                    task(mc,()->{
                        var p=player(mc);check(p.getInventory().items.stream().anyMatch(i->i.is(Items.DIAMOND_PICKAXE)&&i.isEnchanted()),"Closing enchanting returns the tool to inventory");
                        check(p.getInventory().countItem(Items.LAPIS_LAZULI)==9,"Unspent lapis remains intact");
                        check(npc(mc).getX()==0.5&&npc(mc).getZ()==0.5,"Jorge stays at his placement during all services");
                        NpcNetwork.open(p,npc(mc));p.teleportTo(30,-60,30);NpcNetwork.act(p,new NpcNetwork.Action(jorge,"jorge_enchant",""));
                        check(p.containerMenu==p.inventoryMenu,"Out-of-range service request rejected");
                    });next();
                }
                case 26 -> {
                    Files.createDirectories(Path.of("../verification/jorge"));Files.writeString(Path.of("../verification/jorge/client-test.txt"),ServerChecks.REPORT.toString());step=99;mc.stop();
                }
            }
        }catch(Throwable error){DeepNightNpcs.LOG.error("Jorge QA failed at step "+step,error);try{Files.createDirectories(Path.of("../verification/jorge"));Files.writeString(Path.of("../verification/jorge/FAILED.txt"),"Step "+step+": "+error);}catch(Exception ignored){}step=99;mc.stop();}
    }
}
