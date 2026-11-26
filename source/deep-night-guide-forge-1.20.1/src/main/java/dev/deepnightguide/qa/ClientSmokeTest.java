package dev.deepnightguide.qa;

import dev.deepnightguide.DeepNightGuide;
import dev.deepnightguide.client.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.Difficulty;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.*;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.glfw.GLFW;
import java.nio.file.*;

@Mod.EventBusSubscriber(modid=DeepNightGuide.ID,value=Dist.CLIENT)
public final class ClientSmokeTest {
    private static boolean started;
    private static int step,ticks;
    private static GuideScreen guide;
    private static final StringBuilder report=new StringBuilder();
    private static void check(boolean test,String message){if(!test)throw new IllegalStateException(message);report.append("PASS: ").append(message).append('\n');}
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event) {
        if(!Boolean.getBoolean("deepnightguide.smokeTest")||event.phase!=TickEvent.Phase.END)return;
        Minecraft mc=Minecraft.getInstance();
        try {
            if(!started&&mc.screen instanceof TitleScreen&&mc.getOverlay()==null) {
                started=true;mc.options.pauseOnLostFocus=false;GLFW.glfwHideWindow(mc.getWindow().getWindow());
                mc.options.guiScale().set(2);mc.options.renderDistance().set(2);mc.options.simulationDistance().set(5);mc.resizeDisplay();
                mc.getTutorial().setStep(net.minecraft.client.tutorial.TutorialSteps.NONE);
                GameRules rules=new GameRules();rules.getRule(GameRules.RULE_DOMOBSPAWNING).set(false,null);
                mc.createWorldOpenFlows().createFreshLevel("deep-night-guide-qa-"+System.currentTimeMillis(),new LevelSettings("Guide QA",GameType.CREATIVE,false,Difficulty.PEACEFUL,true,rules,WorldDataConfiguration.DEFAULT),new WorldOptions(77,false,false),access->access.registryOrThrow(Registries.WORLD_PRESET).getHolderOrThrow(WorldPresets.FLAT).value().createWorldDimensions());
            }
            if(mc.level==null||mc.player==null||mc.player.tickCount<50)return;
            if(step==0) {
                // Exercise the real key binding's consumeClick event path.
                KeyMapping.click(ClientEvents.OPEN.getKey());step++;return;
            }
            if(step==1) {
                if(!(mc.screen instanceof GuideScreen))return;
                guide=(GuideScreen)mc.screen;
                check(GuideIndex.current!=null,"Keybind opens guide in a world");
                GuideIndex index=GuideIndex.current;
                for(var item:ForgeRegistries.ITEMS)if(item!=Items.AIR)check(index.byKey.containsKey("item:"+ForgeRegistries.ITEMS.getKey(item)),"Indexed item "+ForgeRegistries.ITEMS.getKey(item));
                check(index.recipes.containsKey(Items.DIAMOND_SWORD),"Live diamond sword crafting recipe");
                check(index.byKey.get("item:minecraft:diamond_ore").matches(GuideEntry.Category.ORES),"Ore classification");
                check(index.byKey.get("item:minecraft:iron_chestplate").matches(GuideEntry.Category.ARMOUR),"Armour classification");
                guide.setQuery("@minecraft diamond sword");check(guide.resultCount()==1,"Combined mod and name search");guide.setQuery("");guide.selectKey("item:minecraft:diamond_sword");
                step++;ticks=0;return;
            }
            if(++ticks<50)return;
            if(step==2) {
                if(GuideIndex.current.status.startsWith("Reading")){ticks=40;return;}
                capture("01-recipes.png");
                ChecklistStore store=new ChecklistStore(Path.of("../verification/progress-test"),"test-world-A");
                String key="item:minecraft:diamond_sword";if(store.done(key))store.toggle(key);store.toggle(key);store.save();
                check(new ChecklistStore(Path.of("../verification/progress-test"),"test-world-A").done(key),"Checklist persists to disk");
                store.toggle(key);store.observe(key);store.save();check(!store.done(key),"Manual untick suppresses automatic collection");
                check(!new ChecklistStore(Path.of("../verification/progress-test"),"test-world-B").done(key),"World progress isolation");
                guide.setCategory(GuideEntry.Category.TAMING_LIST);guide.selectKey("entity:minecraft:wolf");
                check(GuideIndex.current.notes.get("entity:minecraft:wolf")!=null,"Verified taming notes loaded");
                step++;ticks=0;return;
            }
            if(step==3) {capture("02-taming.png");guide.setCategory(GuideEntry.Category.ARMOUR_LIST);guide.selectKey("item:minecraft:diamond_chestplate");step++;ticks=0;return;}
            if(step==4) {
                capture("03-armour-checklist.png");
                // Every installed mod's data scanner is exercised separately without loading that pack's gameplay classes.
                if(System.getProperty("deepnightguide.packMods")!=null) {
                    var roots=new java.util.ArrayList<Path>();var systems=new java.util.ArrayList<java.nio.file.FileSystem>();
                    try(var files=Files.list(Path.of(System.getProperty("deepnightguide.packMods")))) {
                        for(Path jar:files.filter(p->p.toString().endsWith(".jar")).toList()) {
                            var fs=FileSystems.newFileSystem(jar);systems.add(fs);roots.add(fs.getPath("/"));
                        }
                        var notes=GuideNotes.load(roots,Path.of("nonexistent-qa-override.json"));
                        check(notes.sources>1000,"Imported pack data: "+notes.sources+" files; skipped: "+notes.failures);
                        var crow=notes.get("entity:alexsmobs:crow");
                        check(crow!=null&&crow.sections.containsKey("Installed mod manual"),"Alex's Mobs installed manual imported");
                    }finally{for(var fs:systems)fs.close();}
                }
                if(GuideIndex.current.byKey.containsKey("entity:dragonfire:ace")) {
                    check(GuideIndex.current.entries.stream().filter(e->e.entityType!=null&&e.id.getNamespace().equals("dragonfire")&&e.tameable).count()==27,"All 27 Dragonfire breeds have taming entries");
                    check(GuideIndex.current.byKey.get("entity:dragonfire:ace").icon.getItem() instanceof net.minecraft.world.item.SpawnEggItem,"Forge mob uses its actual spawn egg icon");
                    check(GuideIndex.current.entries.stream().filter(e->e.entityType!=null&&e.id.getNamespace().equals("fantasycraft")&&e.tameable).count()==18,"Exactly 18 tameable FantasyCraft species");
                    check(GuideIndex.current.byKey.get("item:ld_eb:earth_glove").matches(GuideEntry.Category.WEAPONS),"Earth Glove classified as a weapon");
                    check(JeiBridge.recipes!=null,"Optional JEI plugin initialized");
                    guide.setCategory(GuideEntry.Category.TAMING_LIST);guide.selectKey("entity:dragonfire:ace");step++;ticks=0;return;
                }
                step=7;ticks=0;return;
            }
            if(step==5){capture("04-custom-dragon.png");JeiBridge.recipes.accept(Items.DIAMOND_SWORD.getDefaultInstance());step++;ticks=0;return;}
            if(step==6){check(mc.screen!=guide,"JEI opens output recipes from guide");capture("05-jei-recipes.png");step=7;ticks=0;return;}
            if(step==7) {
                var index=GuideIndex.current;
                check(index.byKey.get("entity:minecraft:wither").boss,"Wither classified as a boss");
                check(index.byKey.get("entity:minecraft:ender_dragon").boss,"Ender Dragon classified as a boss");
                check(!index.byKey.get("entity:minecraft:cow").boss,"Ordinary mobs excluded from boss category");
                if(index.byKey.containsKey("entity:fantasycraft:surtr"))check(index.byKey.get("entity:fantasycraft:surtr").boss,"Custom FantasyCraft major encounter included");
                check(index.entries.stream().filter(e->e.lore).count()==8+mc.getConnection().levels().size(),"Eight lore chapters plus exactly the live dimensions");
                for(var dimension:mc.getConnection().levels())check(index.byKey.containsKey("lore:"+dimension.location()),"Dimension atlas includes "+dimension.location());
                check(!index.byKey.containsKey("lore:the_afterdark:afterdark"),"Uninstalled dimension omitted from atlas");
                check(!index.byKey.containsKey("entity:cataclysm:ignis"),"Uninstalled boss does not create ghost entries");
                var generic=GuideLore.makeEntry(new net.minecraft.resources.ResourceLocation("qa","unmapped_realm"),new com.google.gson.JsonObject(),true);
                check(generic.loreSections.containsKey("Beyond the chart"),"Unknown dimensions receive an atlas fallback");
                ChecklistStore separate=new ChecklistStore(Path.of("../verification/progress-test"),"boss-taming-separation-"+System.nanoTime());
                separate.observe("entity:test:dragon");separate.save();
                check(!separate.done("boss:test:dragon"),"Taming does not complete boss defeat");
                separate.observe("boss:test:dragon");separate.save();
                check(separate.done("entity:test:dragon")&&separate.done("boss:test:dragon"),"Both tracking states can coexist");
                mc.setScreen(guide);guide.setQuery("");guide.setCategory(GuideEntry.Category.BOSS_LIST);guide.selectKey("entity:minecraft:wither");
                mc.getSingleplayerServer().execute(()->mc.getSingleplayerServer().getPlayerList().getPlayer(mc.player.getUUID()).awardStat(net.minecraft.stats.Stats.ENTITY_KILLED.get(net.minecraft.world.entity.EntityType.WITHER)));
                step=8;ticks=0;return;
            }
            if(step==8) {GuideIndex.current.requestStats();step=9;ticks=0;return;}
            if(step==9) {
                if(!GuideIndex.current.progress.done("boss:minecraft:wither")){if(ticks>400)throw new IllegalStateException("Kill stats did not complete boss checklist");return;}
                check(true,"Server-credited kill statistic automatically completes boss checklist");
                guide.setQuery("wither");guide.setCompletionFilter(2);check(guide.resultCount()==1,"Boss Done filter includes defeated Wither");
                guide.setCompletionFilter(1);check(guide.resultCount()==0,"Boss Missing filter excludes defeated Wither");guide.setCompletionFilter(0);guide.setQuery("");
                capture("06-boss-checklist.png");
                GuideIndex.current.progress.toggle("boss:minecraft:wither");step=10;ticks=0;return;
            }
            if(step==10) {
                check(!GuideIndex.current.progress.done("boss:minecraft:wither"),"Manual boss untick survives further positive kill statistics");
                guide.setCategory(GuideEntry.Category.LORE);guide.selectKey("lore:minecraft:overworld");step=11;ticks=0;return;
            }
            if(step==11) {capture("07-world-lore.png");mc.options.guiScale().set(4);mc.resizeDisplay();guide.selectKey("lore:deepnightguide:01_the_deep_night");step=12;ticks=0;return;}
            if(step==12) {
                capture("08-lore-large-gui-scale.png");mc.options.guiScale().set(2);mc.resizeDisplay();
                if(GuideIndex.current.byKey.containsKey("entity:deepnight:ashen_knight")) {
                    DeepNightChecks.validate(mc,guide,ClientSmokeTest::check);
                    guide.setQuery("@deepnight");guide.setCategory(GuideEntry.Category.BOSS_LIST);guide.selectKey("entity:deepnight:the_deep_night");
                    step=13;ticks=0;return;
                }
                check(GuideIndex.current.entries.stream().noneMatch(e->e.id.getNamespace().equals("deepnight")),"Absent Deep Night mod creates no catalog entries");
                finish(mc);
            }
            if(step==13){capture("09-deepnight-bosses.png");guide.setCategory(GuideEntry.Category.PETS);guide.selectKey("entity:deepnight:ember_drake");step=14;ticks=0;return;}
            if(step==14){capture("10-deepnight-pets.png");guide.setCategory(GuideEntry.Category.WEAPONS);guide.selectKey("item:deepnight:nightfall");step=15;ticks=0;return;}
            if(step==15){
                capture("11-deepnight-weapons.png");DeepNightChecks.seedProgress(mc);step=16;ticks=0;return;
            }
            if(step==16){
                GuideIndex.current.requestStats();
                var p=GuideIndex.current.progress;
                if(!p.done("entity:deepnight:ember_drake")||!p.done("item:deepnight:nightfall")||!p.done("boss:deepnight:ashen_knight")){
                    if(ticks>400)throw new IllegalStateException("Deep Night automatic tracking did not synchronize");return;
                }
                check(true,"Deep Night pet ownership, weapon inventory and credited boss kill automatically tracked");
                guide.setCategory(GuideEntry.Category.BOSS_LIST);guide.setCompletionFilter(2);check(guide.resultCount()==1,"Deep Night defeated boss filter");
                guide.setCompletionFilter(1);check(guide.resultCount()==40,"Deep Night missing boss filter");guide.setCompletionFilter(0);
                mc.options.guiScale().set(4);mc.resizeDisplay();guide.setCategory(GuideEntry.Category.PETS);guide.selectKey("entity:deepnight:ember_drake");step=17;ticks=0;return;
            }
            if(step==17){capture("12-deepnight-large-gui-scale.png");Files.writeString(Path.of("../verification/deepnight-smoke-test.txt"),report.toString());finish(mc);}
        }catch(Throwable ex) {
            DeepNightGuide.LOG.error("GUIDE QA FAILED",ex);
            try{Files.createDirectories(Path.of("../verification"));Files.writeString(Path.of("../verification/FAILED.txt"),ex.toString());}catch(Exception ignored){}
            mc.stop();step=99;
        }
    }
    private static void finish(Minecraft mc)throws Exception{Files.writeString(Path.of("../verification/client-smoke-test.txt"),report.toString());step=99;mc.stop();}
    private static void capture(String name)throws Exception {
        Files.createDirectories(Path.of("../verification"));
        try(var image=Screenshot.takeScreenshot(Minecraft.getInstance().getMainRenderTarget())){image.writeToFile(Path.of("../verification",name));}
    }
}
