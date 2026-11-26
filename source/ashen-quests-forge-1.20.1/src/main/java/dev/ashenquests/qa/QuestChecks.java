package dev.ashenquests.qa;

import dev.ashenquests.*;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import java.util.*;

public final class QuestChecks {
    public static final StringBuilder REPORT=new StringBuilder();
    public static void check(boolean value,String description){if(!value)throw new IllegalStateException(description);REPORT.append("PASS: ").append(description).append('\n');}
    public static Quest fixture(String id,List<Quest.Objective> objectives,int cooldown,List<String> deps,List<Quest.Choice> choices){return new Quest(id,id,"QA","QA","QA","Afterwards","minecraft:book",deps,objectives,List.of(),4,0,"Dawnstead",10,cooldown,choices,"eldara");}
    public static Quest.Objective o(String type,String target,int count){return new Quest.Objective(type,target,count,target);}
    private static String claim(ServerPlayer p,String id,String answer,int choice) {
        Quest q=Campaign.quests.get(id);
        if(q!=null&&QuestEngine.view(p,q).status()==QuestEngine.OFFERED)QuestEngine.accept(p,id);
        return QuestEngine.claim(p,id,answer,choice);
    }
    public static void run(ServerPlayer p) throws Exception {
        var testNpc=dev.thedeepnightnpcs.DeepNightNpcs.ELDARA.get().create(p.level());
        p.setPos(0,220,0);p.setNoGravity(true);testNpc.setPos(2,220,0);testNpc.setNoGravity(true);p.serverLevel().addFreshEntity(testNpc);
        dev.thedeepnightnpcs.network.NpcNetwork.open(p,testNpc);QuestServer.openConversation(p,testNpc);
        check(QuestServer.canTransact(p,Campaign.quests.get("ac_m01")),"Eldara conversation authorizes main quest transactions");
        check(Campaign.quests.size()==573,"All 573 campaign entries load on the actual Forge server");
        Campaign.validate(Campaign.quests);check(true,"Full dependency graph is acyclic and all definitions meet limits");
        check(Campaign.quests.values().stream().allMatch(q->q.emeralds()>0),"Every quest guarantees emerald rewards");
        check(Campaign.quests.values().stream().filter(Quest::repeatable).count()==22,"22 repeatable contracts");
        check(Campaign.quests.values().stream().filter(q->!q.choices().isEmpty()).count()==6,"Six persistent narrative decisions");
        for(Quest q:Campaign.quests.values())for(var o:q.objectives())if(o.target().startsWith("minecraft:")) {
            String missing=QuestEngine.missing(p,fixture("qa_registry",List.of(o),0,List.of(),List.of()),new HashSet<>());
            if(!missing.isEmpty())throw new IllegalStateException(q.id()+": "+missing);
        }
        check(true,"Every vanilla item, entity, effect, advancement, biome and dimension target exists in live registries");
        var original=Campaign.quests;
        try {
            var expanded=new LinkedHashMap<>(original);
            expanded.put("qa_submit",fixture("qa_submit",List.of(o("submit","minecraft:bread",4)),0,List.of(),List.of()));
            expanded.put("qa_overlap",fixture("qa_overlap",List.of(o("submit","minecraft:bread",4),o("submit","minecraft:bread",4)),0,List.of(),List.of()));
            expanded.put("qa_repeat",fixture("qa_repeat",List.of(o("submit","minecraft:bread",4)),10,List.of(),List.of()));
            expanded.put("qa_kill",fixture("qa_kill",List.of(o("kill","minecraft:zombie",2)),10,List.of(),List.of()));
            expanded.put("qa_field",fixture("qa_field",List.of(o("field","keep the light",1)),0,List.of(),List.of(new Quest.Choice("Listen","The voices remain."),new Quest.Choice("Rest","The voices rest."))));
            expanded.put("qa_missing",fixture("qa_missing",List.of(o("collect","absent_mod:relic",1)),0,List.of(),List.of()));
            expanded.put("qa_child",fixture("qa_child",List.of(o("collect","minecraft:stick",1)),0,List.of("qa_missing"),List.of()));
            Campaign.quests=expanded;
            var inv=p.getInventory();inv.clearContent();
            check(!claim(p,"ac_m02","",-1).isEmpty(),"Locked story quest cannot be claimed early");
            inv.items.set(0,new ItemStack(Items.CRAFTING_TABLE));
            check(claim(p,"ac_m01","",-1).isEmpty(),"First story quest can be completed");
            check(inv.countItem(Items.EMERALD)==3&&inv.countItem(Items.CRAFTING_TABLE)==1,"Emeralds delivered; held collection item is preserved");
            int before=inv.countItem(Items.EMERALD);
            check(!claim(p,"ac_m01","",-1).isEmpty()&&inv.countItem(Items.EMERALD)==before,"Duplicate claim cannot duplicate emeralds");
            inv.clearContent();inv.items.set(0,new ItemStack(Items.BREAD,4));
            check(claim(p,"qa_submit","",-1).isEmpty()&&inv.countItem(Items.BREAD)==0&&inv.countItem(Items.EMERALD)==4,"Donation consumes exact items and pays emeralds atomically");
            inv.clearContent();inv.items.set(0,new ItemStack(Items.BREAD,4));
            check(!claim(p,"qa_overlap","",-1).isEmpty()&&inv.countItem(Items.BREAD)==4,"Overlapping donation objectives cannot reuse the same items");
            inv.items.set(0,new ItemStack(Items.BREAD,8));check(claim(p,"qa_overlap","",-1).isEmpty()&&inv.countItem(Items.BREAD)==0,"Aggregated donations succeed with sufficient items");
            inv.clearContent();for(int i=0;i<36;i++)inv.items.set(i,new ItemStack(Items.COBBLESTONE,64));inv.items.set(0,new ItemStack(Items.BREAD,64));
            check(!claim(p,"qa_repeat","",-1).isEmpty()&&inv.countItem(Items.BREAD)==64,"Full inventory rejects claim without consuming a donation");
            inv.items.set(0,new ItemStack(Items.BREAD,4));
            check(claim(p,"qa_repeat","",-1).isEmpty()&&inv.countItem(Items.EMERALD)==4,"Donation can free the reward slot");
            check(!claim(p,"qa_repeat","",-1).isEmpty(),"Repeatable cooldown rejects an immediate second claim");
            var rstate=JournalData.child(JournalData.child(QuestEngine.journal(p),"quests"),"qa_repeat");rstate.putLong("next",0);
            check(!claim(p,"qa_repeat","",-1).isEmpty(),"Expired repeatable still requires fresh donated supplies");
            inv.clearContent();inv.offhand.set(0,new ItemStack(Items.BREAD,4));int rep=QuestEngine.journal(p).getCompound("standing").getInt("Dawnstead");
            check(claim(p,"qa_repeat","",-1).isEmpty()&&inv.offhand.get(0).isEmpty(),"Offhand donations are supported");
            check(QuestEngine.journal(p).getCompound("standing").getInt("Dawnstead")==rep,"Repeated contracts cannot farm faction standing");
            check(!claim(p,"qa_field","wrong",0).isEmpty(),"Incorrect journal phrase rejected");
            check(!claim(p,"qa_field","keep the light",5).isEmpty(),"Out-of-range story choice rejected");
            check(claim(p,"qa_field","  KEEP THE LIGHT ",1).isEmpty(),"Valid phrase accepts harmless case and whitespace differences");
            check(QuestEngine.state(p,"qa_field").getInt("choice")==1,"Story choice persists with completion");
            check(QuestEngine.view(p,expanded.get("qa_missing")).status()==QuestEngine.UNAVAILABLE,"Missing mod targets are diagnosed explicitly");
            check(QuestEngine.view(p,expanded.get("qa_child")).reason().contains("absent_mod:relic"),"Missing target explanation propagates through prerequisites");
            int kills=p.getStats().getValue(Stats.ENTITY_KILLED.get(EntityType.ZOMBIE));p.getStats().setValue(p,Stats.ENTITY_KILLED.get(EntityType.ZOMBIE),kills+2);
            check(QuestEngine.total(p,o("kill","minecraft:zombie",1))==kills+2,"Kill objectives read persistent Minecraft statistics");
            inv.clearContent();check(claim(p,"qa_kill","",-1).isEmpty(),"Kill quest claims from server statistics");
            JournalData.child(JournalData.child(QuestEngine.journal(p),"quests"),"qa_kill").putLong("next",0);
            check(QuestEngine.view(p,expanded.get("qa_kill")).progress()[0]==0,"Repeatable kills reset against a saved baseline");
            p.getStats().setValue(p,Stats.ENTITY_KILLED.get(EntityType.ZOMBIE),kills+4);check(claim(p,"qa_kill","",-1).isEmpty(),"Fresh kills satisfy the next repeat cycle");
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerEvent.ItemCraftedEvent(p,new ItemStack(Items.NOTE_BLOCK,2),new net.minecraft.world.SimpleContainer(9)));
            check(QuestEngine.total(p,o("craft","minecraft:note_block",2))==2,"Registered crafting event accumulates output quantities");
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerEvent.ItemSmeltedEvent(p,new ItemStack(Items.COPPER_INGOT,24)));
            check(QuestEngine.total(p,o("smelt","minecraft:copper_ingot",24))==24,"Registered smelting event accumulates output quantities");
            var wolf=new net.minecraft.world.entity.animal.Wolf(EntityType.WOLF,p.level());
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.living.AnimalTameEvent(wolf,p));
            check(QuestEngine.total(p,o("tame","minecraft:wolf",1))==1,"Registered taming event records the correct animal");
            var villager=new net.minecraft.world.entity.npc.Villager(EntityType.VILLAGER,p.level());
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract(p,net.minecraft.world.InteractionHand.MAIN_HAND,villager));
            check(QuestEngine.total(p,o("interact","minecraft:villager",1))==1,"Registered interaction event records a conversation");
            p.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE,200));check(QuestEngine.total(p,o("effect","minecraft:fire_resistance",1))==1,"Active potion effects are checked on server");
            var advancement=p.server.getAdvancements().getAdvancement(new ResourceLocation("minecraft:story/root"));for(String criterion:advancement.getCriteria().keySet())p.getAdvancements().getOrStartProgress(advancement).grantProgress(criterion);
            check(QuestEngine.total(p,o("achieve","minecraft:story/root",1))==1,"Advancement objective reads real advancement progress");
            var saved=JournalData.get(p.server).save(new CompoundTag());var restored=JournalData.load(saved);
            check(restored.player(p.getUUID()).getCompound("quests").getCompound("qa_field").getInt("choice")==1,"Completion and decision survive NBT save/load");
            check(restored.player(p.getUUID()).getCompound("totals").getInt("smelt/minecraft:copper_ingot")==24,"Objective counters survive NBT save/load");
            check(restored.player(UUID.randomUUID()).isEmpty(),"Different player UUID has independent quest progress");
            var cycle=new LinkedHashMap<String,Quest>();cycle.put("a",fixture("a",List.of(o("collect","minecraft:stick",1)),0,List.of("b"),List.of()));cycle.put("b",fixture("b",List.of(o("collect","minecraft:stick",1)),0,List.of("a"),List.of()));boolean rejected=false;try{Campaign.validate(cycle);}catch(IllegalArgumentException ex){rejected=true;}check(rejected,"Dependency cycles rejected before publication");
        } finally {Campaign.quests=original;}
        // Clear only this QA character so the network smoke test starts at the first real quest.
        QuestEngine.journal(p).getAllKeys().stream().toList().forEach(QuestEngine.journal(p)::remove);JournalData.get(p.server).setDirty();p.getInventory().clearContent();p.getInventory().items.set(0,new ItemStack(Items.CRAFTING_TABLE));testNpc.discard();QuestServer.sessions.remove(p.getUUID());p.setNoGravity(false);
    }
}
