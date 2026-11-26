package dev.deepnightguide.qa;

import dev.deepnightguide.client.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.function.BiConsumer;

/** Actual content-mod integration checks, with no compile-time dependency on that mod. */
public final class DeepNightChecks {
    public static void validate(Minecraft mc,GuideScreen screen,BiConsumer<Boolean,String> check)throws Exception {
        var index=GuideIndex.current;
        int bosses=0,pets=0,others=0,weapons=0;
        for(var e:index.entries) {
            if(!e.id.getNamespace().equals("deepnight"))continue;
            if(e.entityType!=null){
                var entity=e.entityType.create(mc.level);
                Object species=entity.getClass().getMethod("species").invoke(entity);
                String kind=species.getClass().getField("kind").get(species).toString();
                boolean boss=kind.equals("BOSS"),pet=kind.equals("PET");
                check.accept(e.boss==boss,"Deep Night boss classification matches live Species: "+e.id);
                check.accept(BossDiscovery.declaresBossBar(entity)==boss,"Shared NightMob boss-bar detection: "+e.id);
                check.accept(e.tameable==pet&&e.matches(GuideEntry.Category.PETS)==pet,"Deep Night pet classification: "+e.id);
                var note=index.notes.get(e.key);
                check.accept(note!=null&&note.sections.containsKey("Where to find"),"Deep Night acquisition notes: "+e.id);
                if(boss){
                    bosses++;
                    check.accept(ForgeRegistries.ITEMS.getKey(e.icon.getItem()).equals(new ResourceLocation("deepnight",e.id.getPath()+"_sigil")),"Boss uses its own sigil icon: "+e.id);
                    check.accept(note.sections.containsKey("Combat")&&note.sections.containsKey("Victory and rewards"),"Boss combat and campaign instructions: "+e.id);
                }else{
                    if(pet)pets++;else others++;
                    check.accept(e.icon.getItem() instanceof SpawnEggItem,"Actual Deep Night creature icon: "+e.id);
                    if(pet)check.accept(note.sections.containsKey("Taming")&&note.sections.containsKey("Growth"),"Pet taming and levelling instructions: "+e.id);
                }
            }else if(e.matches(GuideEntry.Category.WEAPONS)){
                weapons++;
                check.accept(index.recipes.containsKey(e.icon.getItem()),"Live weapon crafting recipe: "+e.id);
                check.accept(index.notes.get(e.key).sections.containsKey("Crafting"),"Weapon crafting and progression notes: "+e.id);
            }
        }
        check.accept(bosses==41&&pets==31&&others==105&&weapons==25,"Complete Deep Night coverage: 41 bosses, 31 pets, 105 ordinary/neutral mobs, 25 weapons");
        screen.setQuery("@deepnight");screen.setCategory(GuideEntry.Category.BOSSES);check.accept(screen.resultCount()==41,"Bosses browser contains exactly 41 Deep Night bosses");
        screen.setCategory(GuideEntry.Category.PETS);check.accept(screen.resultCount()==31,"Pets browser contains all 31 Deep Night pets");
        screen.setCategory(GuideEntry.Category.TAMING_LIST);check.accept(screen.resultCount()==31,"Taming checklist contains all 31 Deep Night pets");
        screen.setCategory(GuideEntry.Category.WEAPON_LIST);check.accept(screen.resultCount()==25,"Weapon checklist contains all 25 Deep Night weapons");
        screen.setCategory(GuideEntry.Category.MOBS);check.accept(screen.resultCount()==177,"Mobs browser contains all 177 Deep Night creatures");
        check.accept(index.notes.failures==0,"Deep Night guide sources parsed without failures");
        check.accept(!index.byKey.get("item:deepnight:pet_summoning_tool").matches(GuideEntry.Category.WEAPONS),"Pet Summoning Tool is a tool, not a weapon");
    }
    public static void seedProgress(Minecraft mc) {
        var playerId=mc.player.getUUID();var server=mc.getSingleplayerServer();
        server.execute(()->{
            var player=server.getPlayerList().getPlayer(playerId);
            var pet=(TamableAnimal)ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation("deepnight","ember_drake")).create(player.serverLevel());
            pet.moveTo(player.getX()+3,player.getY(),player.getZ(),0,0);pet.tame(player);pet.setOrderedToSit(true);player.serverLevel().addFreshEntity(pet);
            player.getInventory().add(ForgeRegistries.ITEMS.getValue(new ResourceLocation("deepnight","nightfall")).getDefaultInstance());player.containerMenu.broadcastChanges();
            player.awardStat(Stats.ENTITY_KILLED.get(ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation("deepnight","ashen_knight"))));
        });
    }
}
