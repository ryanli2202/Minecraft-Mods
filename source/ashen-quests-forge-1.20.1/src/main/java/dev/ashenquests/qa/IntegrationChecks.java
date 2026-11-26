package dev.ashenquests.qa;

import dev.ashenquests.*;
import dev.thedeepnightnpcs.*;
import dev.thedeepnightnpcs.entity.NpcEntity;
import dev.thedeepnightnpcs.network.NpcNetwork;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.*;
import java.util.*;
import static dev.ashenquests.qa.QuestChecks.check;

public final class IntegrationChecks {
    public static NpcEntity spawn(ServerPlayer p,String kind) {
        var type=switch(kind){case "eldara"->DeepNightNpcs.ELDARA;case "caelen"->DeepNightNpcs.CAELEN;case "jorge"->DeepNightNpcs.JORGE;case "spire_guard"->DeepNightNpcs.GUARD;default->DeepNightNpcs.COVENANT_NPCS.get(kind);};
        NpcEntity npc=type.get().create(p.level());npc.setPos(p.getX()+2,p.getY(),p.getZ());npc.setNoGravity(true);p.serverLevel().addFreshEntity(npc);return npc;
    }
    public static void talk(ServerPlayer p,NpcEntity npc) {
        NpcNetwork.open(p,npc);
        NpcNetwork.act(p,new NpcNetwork.Action(npc.getId(),"covenant_quests",""));
    }
    public static void run(ServerPlayer p) {
        var original=Campaign.quests;var fixtures=new LinkedHashMap<>(original);var spawned=new ArrayList<NpcEntity>();
        p.setPos(20,220,20);p.setNoGravity(true);p.getInventory().clearContent();p.getInventory().setItem(0,new ItemStack(Items.CRAFTING_TABLE));
        try {
            check(original.values().stream().filter(q->q.chapter().startsWith("Book ")).allMatch(q->q.giverId().equals("eldara")),"All 276 main quests require Eldara");
            check(original.values().stream().filter(q->q.giverId().equals("eldara")).count()==276,"Eldara owns exactly the main story");
            check(DeepNightNpcs.allNpcs().size()==12&&DeepNightNpcs.COVENANT_EGGS.size()==8,"All eight companions registered alongside four existing NPCs");
            check(original.values().stream().allMatch(q->Set.of("eldara","caelen","jorge").contains(q.giverId())||DeepNightNpcs.COVENANT_NPCS.containsKey(q.giverId())),"Every campaign quest giver resolves to an installed NPC");
            var eldara=spawn(p,"eldara");spawned.add(eldara);
            var q=original.get("ac_m01");
            check(QuestEngine.view(p,q).status()==QuestEngine.OFFERED,"Unaccepted quest cannot become ready from owned items alone");
            QuestServer.open(p);
            check(!QuestEngine.accept(p,q.id()).isEmpty()&&!QuestEngine.claim(p,q.id(),"",-1).isEmpty(),"Hotkey journal cannot accept or claim even beside Eldara");
            NpcNetwork.act(p,new NpcNetwork.Action(eldara.getId(),"covenant_quests",""));
            check(!QuestServer.canTransact(p,q),"Forged NPC action without a conversation is rejected");
            talk(p,eldara);
            check(QuestServer.canTransact(p,q),"Dialogue action opens an authorized quest conversation via Forge event");
            var session=QuestServer.sessions.get(p.getUUID());long token=session.token;
            QuestServer.action(p,new QuestNetwork.Action(token,1,4,q.id(),"",-1));
            check(QuestEngine.state(p,q.id()).getBoolean("accepted")&&QuestEngine.view(p,q).status()==QuestEngine.READY,"Network acceptance saves the quest and enables objective completion");
            var saved=JournalData.load(JournalData.get(p.server).save(new CompoundTag()));
            check(saved.player(p.getUUID()).getCompound("quests").getCompound(q.id()).getBoolean("accepted"),"Accepted state survives save/load");
            QuestServer.open(p);
            check(!QuestEngine.claim(p,q.id(),"",-1).isEmpty(),"Reopening with hotkey revokes NPC authority for an accepted quest");
            QuestServer.action(p,new QuestNetwork.Action(token,2,1,q.id(),"",-1));
            check(QuestEngine.claims(p,q.id())==0,"Old conversation token cannot claim through a new journal");
            talk(p,eldara);p.setPos(100,220,20);
            check(!QuestEngine.claim(p,q.id(),"",-1).isEmpty(),"Walking out of range blocks a pending turn-in");
            p.setPos(20,220,20);talk(p,eldara);
            var wall=new net.minecraft.core.BlockPos(21,221,20);p.serverLevel().setBlockAndUpdate(wall,net.minecraft.world.level.block.Blocks.STONE.defaultBlockState());
            check(!QuestEngine.claim(p,q.id(),"",-1).isEmpty(),"A wall blocking line of sight prevents turn-in");
            p.serverLevel().removeBlock(wall,false);talk(p,eldara);
            session=QuestServer.sessions.get(p.getUUID());session.until=QuestEngine.now(p)-1;
            check(!QuestEngine.claim(p,q.id(),"",-1).isEmpty(),"Expired conversation cannot claim");
            talk(p,eldara);session=QuestServer.sessions.get(p.getUUID());
            QuestServer.action(p,new QuestNetwork.Action(session.token,1,1,q.id(),"",-1));
            check(QuestEngine.claims(p,q.id())==1&&p.getInventory().countItem(Items.EMERALD)==3,"In-person network turn-in pays exactly once");
            QuestServer.action(p,new QuestNetwork.Action(session.token,1,1,q.id(),"",-1));
            check(p.getInventory().countItem(Items.EMERALD)==3,"Replayed turn-in sequence cannot duplicate emeralds");
            for(var companion:NpcRoster.COMPANIONS) {
                String id="qa_npc_"+companion.id();var f=new Quest(id,id,"QA","QA","QA","","minecraft:book",List.of(),List.of(new Quest.Objective("collect","minecraft:crafting_table",1,"Table")),List.of(),1,0,"Dawnstead",0,0,List.of(),companion.id());
                fixtures.put(id,f);Campaign.quests=fixtures;
                talk(p,eldara);check(!QuestEngine.accept(p,id).isEmpty(),"Eldara cannot provide "+companion.id()+"'s quest");
                var npc=spawn(p,companion.id());spawned.add(npc);talk(p,npc);
                check(QuestEngine.accept(p,id).isEmpty()&&QuestEngine.claim(p,id,"",-1).isEmpty(),companion.id()+" provides and rewards their own quest");
                check(!QuestEngine.accept(p,"ac_m02").isEmpty(),companion.id()+" cannot provide the main story");
                var npcSave=new CompoundTag();npc.addAdditionalSaveData(npcSave);npc.readAdditionalSaveData(npcSave);
                check(npc.kind().equals(companion.id())&&!npc.isGuard()&&npc.isPersistenceRequired(),companion.id()+" retains identity and persistent stationary behavior");
            }
            talk(p,eldara);session=QuestServer.sessions.get(p.getUUID());QuestServer.action(p,new QuestNetwork.Action(session.token,1,3,"","",-1));
            check(!QuestEngine.accept(p,"ac_m02").isEmpty(),"Closing the conversation immediately revokes quest authority");
            var legacy=JournalData.child(JournalData.child(QuestEngine.journal(p),"quests"),"ac_m02");legacy.putInt("claims",1);legacy.remove("accepted");
            check(QuestEngine.view(p,original.get("ac_m02")).status()==QuestEngine.CLAIMED,"Existing completed quests remain completed without new acceptance data");
            QuestServer.open(p);session=QuestServer.sessions.get(p.getUUID());QuestServer.action(p,new QuestNetwork.Action(session.token,1,2,"ac_m03","",-1));
            check(QuestEngine.journal(p).getString("pinned").equals("ac_m03"),"Hotkey journal still tracks quests without a conversation");
            talk(p,eldara);Campaign.revision++;
            check(!QuestEngine.accept(p,"ac_m03").isEmpty(),"Campaign reload invalidates an older conversation");
        } finally {
            Campaign.quests=original;spawned.forEach(NpcEntity::discard);QuestServer.sessions.remove(p.getUUID());p.setNoGravity(false);
            QuestEngine.journal(p).getAllKeys().stream().toList().forEach(QuestEngine.journal(p)::remove);JournalData.get(p.server).setDirty();
        }
    }
}
