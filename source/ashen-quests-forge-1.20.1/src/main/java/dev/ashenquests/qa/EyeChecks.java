package dev.ashenquests.qa;

import dev.ashenquests.*;
import dev.thedeepnightnpcs.entity.NpcEntity;
import dev.thedeepnightnpcs.network.NpcNetwork;
import dev.thedeepnightnpcs.quest.QuestDialogueEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.*;
import static dev.ashenquests.qa.QuestChecks.check;

public final class EyeChecks {
    private static void complete(ServerPlayer p,String id) {
        JournalData.child(JournalData.child(QuestEngine.journal(p),"quests"),id).putInt("claims",1);
    }
    public static void run(ServerPlayer p) {
        var original=Campaign.quests; var npcs=new HashMap<String,NpcEntity>();
        p.setPos(80,220,80);p.setNoGravity(true);
        try {
            var keys=original.values().stream().filter(q->q.id().startsWith("ac_key")).toList();
            check(keys.size()==16&&keys.stream().map(q->q.rewards().get(0).item()).distinct().count()==16,"Sixteen unique one-time eye trial rewards");
            check(keys.stream().noneMatch(q->q.repeatable()||q.objectives().stream().anyMatch(o->o.target().startsWith("endrem:"))),"Eye trials never require the eye they award");
            check(original.get("ac_m35").requires().contains("ac_key12")&&original.get("ac_m35").objectives().get(0).target().equals("thedeepnightnpcs:spire_guard"),"Spire preparation requires twelve trials and the guard instead of Eye Spy");
            for(var q:original.values())if(!q.id().startsWith("ac_key")&&!q.id().equals("ac_m35"))complete(p,q.id());
            check(QuestEngine.claims(p,"ac_key01")==0&&QuestEngine.claims(p,"ac_eye01")==1,"Legacy discovery completion does not consume new eye reward entitlement");
            var sera=IntegrationChecks.spawn(p,"sera");npcs.put("sera",sera);
            var first=keys.get(0);
            var missing=new Quest("qa_eye_missing","Missing eye","QA","QA","QA","QA","minecraft:book",List.of(),List.of(QuestChecks.o("submit","minecraft:paper",16)),List.of(new Quest.Reward("endrem:missing_test_eye",1)),4,0,"Archivists",0,0,List.of(),"sera");
            var map=new LinkedHashMap<>(original);map.put(missing.id(),missing);Campaign.quests=map;
            IntegrationChecks.talk(p,sera);p.getInventory().clearContent();p.getInventory().setItem(0,new ItemStack(Items.PAPER,16));
            check(QuestEngine.view(p,missing).status()==QuestEngine.UNAVAILABLE&&!QuestEngine.claim(p,missing.id(),"",-1).isEmpty()&&p.getInventory().countItem(Items.PAPER)==16&&QuestEngine.claims(p,missing.id())==0,"Missing eye reward blocks claims without consuming donations or completion");
            Campaign.quests=original;
            if(!ForgeRegistries.ITEMS.containsKey(new ResourceLocation(first.rewards().get(0).item()))) {
                check(QuestEngine.view(p,first).status()==QuestEngine.UNAVAILABLE,"Absent End Remastered safely disables eye trials");
                return;
            }
            for(int i=0;i<keys.size();i++) {
                var q=keys.get(i);var npc=npcs.computeIfAbsent(q.giverId(),name->IntegrationChecks.spawn(p,name));
                IntegrationChecks.talk(p,npc);
                check(QuestEngine.accept(p,q.id()).isEmpty(),"Story milestone and previous trial unlock "+q.id());
                if(i<11)check(QuestEngine.view(p,original.get("ac_m35")).status()==QuestEngine.LOCKED,"Spire preparation stays locked before twelve eye claims: "+q.id());
                if(i+1<keys.size())check(QuestEngine.view(p,keys.get(i+1)).status()==QuestEngine.LOCKED,"Next eye cannot be taken early: "+q.id());
                p.getInventory().clearContent();var o=q.objectives().get(0);var target=new ResourceLocation(o.target());
                switch(o.type()) {
                    case "collect","submit" -> p.getInventory().setItem(0,new ItemStack(ForgeRegistries.ITEMS.getValue(target),o.count()));
                    case "kill" -> p.getStats().setValue(p,Stats.ENTITY_KILLED.get(ForgeRegistries.ENTITY_TYPES.getValue(target)),o.count());
                    default -> QuestEngine.count(p,o.type(),o.target(),o.count());
                }
                NpcNetwork.open(p,npc);MinecraftForge.EVENT_BUS.post(new QuestDialogueEvent(p,npc,new net.minecraft.nbt.CompoundTag(),q.id()+"/1"));
                IntegrationChecks.talk(p,npc);
                var eye=ForgeRegistries.ITEMS.getValue(new ResourceLocation(q.rewards().get(0).item()));
                check(QuestEngine.claim(p,q.id(),"",-1).isEmpty()&&p.getInventory().countItem(eye)==1,"NPC turn-in grants exactly one real End Remastered eye: "+q.id());
                check(!QuestEngine.claim(p,q.id(),"",-1).isEmpty()&&p.getInventory().countItem(eye)==1,"Eye reward cannot be claimed twice: "+q.id());
                if(i==11)check(QuestEngine.view(p,original.get("ac_m35")).status()==QuestEngine.OFFERED,"Twelve distinct eye claims unlock Spire preparation without optional trials");
            }
            var eldara=IntegrationChecks.spawn(p,"eldara");npcs.put("eldara",eldara);IntegrationChecks.talk(p,eldara);
            check(QuestEngine.accept(p,"ac_m35").isEmpty(),"Eldara accepts Spire portal preparation without stronghold advancement");
            var guard=IntegrationChecks.spawn(p,"spire_guard");npcs.put("spire_guard",guard);NpcNetwork.open(p,guard);
            MinecraftForge.EVENT_BUS.post(new QuestDialogueEvent(p,guard,new net.minecraft.nbt.CompoundTag(),"ac_m35/0"));
            IntegrationChecks.talk(p,eldara);
            check(QuestEngine.claim(p,"ac_m35","",-1).isEmpty(),"Spire Guard conversation completes portal preparation through Eldara");
        } finally {Campaign.quests=original; npcs.values().forEach(NpcEntity::discard);}
    }
}
