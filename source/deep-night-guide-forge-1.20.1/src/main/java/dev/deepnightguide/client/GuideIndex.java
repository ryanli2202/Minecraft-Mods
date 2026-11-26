package dev.deepnightguide.client;

import dev.deepnightguide.DeepNightGuide;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.stats.Stats;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import static dev.deepnightguide.client.GuideEntry.Category.*;

public final class GuideIndex {
    public static GuideIndex current;
    public final List<GuideEntry> entries=new ArrayList<>();
    public final Map<String,GuideEntry> byKey=new HashMap<>();
    public final Map<Item,List<Recipe<?>>> recipes=new HashMap<>();
    public final ChecklistStore progress;
    public GuideNotes notes=new GuideNotes();
    public String status="Reading installed mod data...";
    private CompletableFuture<GuideNotes> loading;
    private final ArrayDeque<GuideEntry> inspect=new ArrayDeque<>();
    private int ticks;
    public GuideIndex() {
        Minecraft mc=Minecraft.getInstance();
        String world=mc.getSingleplayerServer()!=null ? mc.getSingleplayerServer().getWorldPath(LevelResource.ROOT).toAbsolutePath().normalize().toString() : mc.getCurrentServer()!=null ? mc.getCurrentServer().ip : "remote";
        progress=new ChecklistStore(mc.gameDirectory.toPath().resolve("config/deepnightguide/progress"),world+"|"+mc.player.getUUID());
        Map<String,String> names=new HashMap<>();
        ModList.get().getMods().forEach(m->names.put(m.getModId(),m.getDisplayName()));
        for(Item item:ForgeRegistries.ITEMS) {
            if(item==Items.AIR)continue;
            ResourceLocation id=ForgeRegistries.ITEMS.getKey(item);
            var categories=EnumSet.of(ITEMS);
            String path=id.getPath();
            if(item instanceof ArmorItem || hasTag(item,"armors") || path.matches(".*(?:_helmet|_chestplate|_leggings|_boots)$"))categories.add(ARMOUR);
            if(item instanceof SwordItem || item instanceof ProjectileWeaponItem || item instanceof TridentItem || item instanceof AxeItem || hasTag(item,"weapons") || path.matches(".*(?:_sword|_spear|_staff|_dagger|_katana|_hammer|_halberd|_glaive|_scythe|_mace|_saber|_bow|_crossbow|_greatsword)$"))categories.add(WEAPONS);
            if(item instanceof DiggerItem || item instanceof ShearsItem || item instanceof FishingRodItem || item instanceof FlintAndSteelItem || hasTag(item,"tools"))categories.add(TOOLS);
            if(hasTag(item,"ores") || item instanceof BlockItem b && b.getBlock() instanceof net.minecraft.world.level.block.DropExperienceBlock && path.contains("ore") || path.endsWith("_ore") || item instanceof BlockItem b && b.getBlock()==Blocks.ANCIENT_DEBRIS)categories.add(ORES);
            ItemStack stack=new ItemStack(item);
            add(new GuideEntry(id,stack.getHoverName().getString(),names.getOrDefault(id.getNamespace(),id.getNamespace()),stack,null,categories));
        }
        // Block states without inventory items are included as entries too.
        ForgeRegistries.BLOCKS.forEach(block->{
            if(block==Blocks.AIR || block.asItem()!=Items.AIR)return;
            ResourceLocation id=ForgeRegistries.BLOCKS.getKey(block);
            GuideEntry e=new GuideEntry(id,block.getName().getString(),names.getOrDefault(id.getNamespace(),id.getNamespace()),new ItemStack(Items.STRUCTURE_VOID),null,EnumSet.of(ITEMS));
            if(!byKey.containsKey(e.key))add(e);
        });
        for(EntityType<?> type:ForgeRegistries.ENTITY_TYPES) {
            if(type==EntityType.PLAYER)continue;
            ResourceLocation id=ForgeRegistries.ENTITY_TYPES.getKey(type);
            SpawnEggItem egg=net.minecraftforge.common.ForgeSpawnEggItem.fromEntityType(type);
            String name=type.getDescription().getString();
            if(id.getNamespace().equals("fantasycraft")&&id.getPath().matches("hydra_[1-5]")) {
                String heads=id.getPath().substring(6);name+=" ("+heads+(heads.equals("1")?" head)":" heads)");
            }
            GuideEntry e=new GuideEntry(id,name,names.getOrDefault(id.getNamespace(),id.getNamespace()),new ItemStack(egg==null?Items.SPYGLASS:egg),type,EnumSet.of(MOBS));
            e.inferredBoss=BossDiscovery.tagged(type);e.boss=e.inferredBoss;
            if(e.boss)e.bossReason="Vanilla boss or an explicit boss tag.";
            add(e);inspect.add(e);
        }
        entries.sort(Comparator.comparing((GuideEntry e)->e.name.toLowerCase(Locale.ROOT)).thenComparing(e->e.key));
        rebuildRecipes();reloadNotes();requestStats();
    }
    void add(GuideEntry e) { entries.add(e);byKey.put(e.key,e); }
    private boolean hasTag(Item item,String path) { return item.builtInRegistryHolder().tags().anyMatch(t->t.location().getPath().equals(path)||t.location().getPath().startsWith(path+"/")); }
    public void rebuildRecipes() {
        recipes.clear();Minecraft mc=Minecraft.getInstance();if(mc.level==null)return;
        for(Recipe<?> recipe:mc.level.getRecipeManager().getRecipes())try {
            ItemStack result=recipe.getResultItem(mc.level.registryAccess());
            if(!result.isEmpty())recipes.computeIfAbsent(result.getItem(),k->new ArrayList<>()).add(recipe);
        }catch(Exception e){DeepNightGuide.LOG.debug("Unsupported guide recipe {}",recipe.getId(),e);}
        recipes.values().forEach(list->list.sort(Comparator.comparing(r->r.getId().toString())));
    }
    public void reloadNotes() {
        if(loading!=null && !loading.isDone())return;
        status="Reading installed mod data...";
        List<Path> roots=ModList.get().getModFiles().stream().map(m->m.getFile().findResource("")).toList();
        Path overrides=Minecraft.getInstance().gameDirectory.toPath().resolve("config/deepnightguide/entries.json");
        loading=CompletableFuture.supplyAsync(()->GuideNotes.load(roots,overrides));
    }
    public void tick() {
        if(loading!=null && loading.isDone()) {
            try {notes=loading.join();status="Loaded "+entries.size()+" entries | "+notes.sources+" data files";}
            catch(Exception e){status="Some guide data could not load; see log.";DeepNightGuide.LOG.error(status,e);}
            loading=null;
            for(GuideEntry e:entries) {
                GuideNotes.Note n=notes.get(e.key);e.icon=e.defaultIcon;
                if(n!=null){
                    if(n.tameable!=null)e.tameable=n.tameable;
                    for(String c:n.categories)try{e.categories.add(GuideEntry.Category.valueOf(c));}catch(Exception ignored){}
                    if(n.icon!=null){
                        ResourceLocation iconId=ResourceLocation.tryParse(n.icon);
                        Item icon=iconId==null?null:ForgeRegistries.ITEMS.getValue(iconId);
                        if(icon!=null&&icon!=Items.AIR)e.icon=new ItemStack(icon);
                    }
                }
                BossDiscovery.apply(e,n);
            }
            GuideLore.refresh(this);
            entries.sort(Comparator.comparing((GuideEntry e)->e.name.toLowerCase(Locale.ROOT)).thenComparing(e->e.key));
            status="Loaded "+entries.size()+" entries | "+notes.sources+" data files";
            addWorldHabitats();
        }
        Minecraft mc=Minecraft.getInstance();if(mc.level==null || mc.player==null)return;
        for(int i=0;i<4&&!inspect.isEmpty();i++) {
            GuideEntry e=inspect.poll();
            // Do not construct arbitrary technical/projectile entities merely to classify them.
            if(e.entityType.getCategory()==MobCategory.MISC)continue;
            try { Entity mob=e.entityType.create(mc.level);
                GuideNotes.Note note=notes.get(e.key);
                if(note==null || note.tameable==null)e.tameable=mob instanceof TamableAnimal || mob instanceof AbstractHorse;
                if(mob!=null&&BossDiscovery.declaresBossBar(mob)){e.inferredBoss=true;e.bossReason="Entity class declares a boss health bar.";}
                BossDiscovery.apply(e,note);
            }catch(Exception ex){DeepNightGuide.LOG.debug("Cannot inspect {}",e.key,ex);}
        }
        ticks++;
        if(ticks%200==0&&mc.screen instanceof GuideScreen)requestStats();
        if(ticks%40!=0)return;
        for(GuideEntry e:entries)if(e.boss&&e.entityType!=null&&mc.player.getStats().getValue(Stats.ENTITY_KILLED,e.entityType)>0)progress.observe(e.bossProgressKey());
        for(ItemStack stack:mc.player.getInventory().items)observeItem(stack);
        for(ItemStack stack:mc.player.getInventory().armor)observeItem(stack);
        for(ItemStack stack:mc.player.getInventory().offhand)observeItem(stack);
        for(Entity entity:mc.level.entitiesForRendering()) {
            UUID owner=entity instanceof TamableAnimal pet&&pet.isTame()?pet.getOwnerUUID():entity instanceof AbstractHorse horse&&horse.isTamed()?horse.getOwnerUUID():null;
            if(mc.player.getUUID().equals(owner)) {
                String key="entity:"+ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
                GuideEntry e=byKey.get(key);if(e!=null){e.tameable=true;progress.observe(key);}
            }
        }
        progress.save();
    }
    public void requestStats() {
        var connection=Minecraft.getInstance().getConnection();
        if(connection!=null)connection.send(new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.REQUEST_STATS));
    }
    private void addWorldHabitats() {
        Minecraft mc=Minecraft.getInstance();var server=mc.getSingleplayerServer();if(server==null)return;
        server.execute(()->{
            Map<String,Set<String>> habitats=new TreeMap<>();
            var biomes=server.registryAccess().registryOrThrow(Registries.BIOME);
            for(var biome:biomes.entrySet())for(MobCategory group:MobCategory.values())for(var spawn:biome.getValue().getMobSettings().getMobs(group).unwrap()) {
                ResourceLocation entityId=ForgeRegistries.ENTITY_TYPES.getKey(spawn.type);
                if(entityId!=null)habitats.computeIfAbsent("entity:"+entityId,k->new TreeSet<>()).add(biome.getKey().location().toString());
            }
            mc.execute(()->{if(current!=this)return;for(var entry:habitats.entrySet())notes.addWorldHabitat(entry.getKey(),entry.getValue());});
        });
    }
    private void observeItem(ItemStack stack) {
        if(stack.isEmpty())return;
        GuideEntry e=byKey.get("item:"+ForgeRegistries.ITEMS.getKey(stack.getItem()));
        if(e!=null && (e.categories.contains(WEAPONS)||e.categories.contains(ARMOUR)))progress.observe(e.key);
    }
}
