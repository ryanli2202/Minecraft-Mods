package dev.ashenquests.qa;

import dev.ashenquests.*;
import dev.thedeepnightnpcs.entity.NpcEntity;
import dev.thedeepnightnpcs.network.NpcNetwork;
import dev.thedeepnightnpcs.quest.QuestDialogueEvent;
import net.minecraft.nbt.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.*;
import net.minecraftforge.common.MinecraftForge;
import java.util.*;
import static dev.ashenquests.qa.QuestChecks.check;

/** Exercises production conversation handlers, including cross-NPC order and legacy saves. */
public final class ConversationChecks {
    private static CompoundTag state(ServerPlayer p,String id){return JournalData.child(JournalData.child(QuestEngine.journal(p),"quests"),id);}
    private static QuestDialogueEvent topics(ServerPlayer p,NpcEntity npc){var e=new QuestDialogueEvent(p,npc,new CompoundTag(),"");MinecraftForge.EVENT_BUS.post(e);return e;}
    private static QuestDialogueEvent discuss(ServerPlayer p,NpcEntity npc,String request){var e=new QuestDialogueEvent(p,npc,new CompoundTag(),request);MinecraftForge.EVENT_BUS.post(e);return e;}
    private static boolean hasTopic(ServerPlayer p,NpcEntity npc,String request){var list=topics(p,npc).state.getList("questTopics",10);for(int i=0;i<list.size();i++)if(list.getCompound(i).getString("request").equals(request))return true;return false;}
    public static void run(ServerPlayer p) {
        var original=Campaign.quests;var npcs=new LinkedHashMap<String,NpcEntity>();
        p.setPos(50,220,50);p.setNoGravity(true);p.getInventory().clearContent();
        try {
            for(String name:List.of("eldara","rowan","sera","mara","elian","iona","tamsin","fen","ysolde","caelen","jorge","spire_guard"))npcs.put(name,IntegrationChecks.spawn(p,name));
            for(var q:original.values())for(var o:q.objectives())if(o.type().equals("talk"))check(net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES.containsKey(new net.minecraft.resources.ResourceLocation(o.target())),"Conversation target registered: "+q.id()+" -> "+o.target());
            check(original.values().stream().filter(q->q.chapter().startsWith("Book IV")||q.chapter().startsWith("Book V")).count()==96,"Two sequel books add 96 main quests");
            check(original.values().stream().filter(q->q.id().startsWith("ns_")).count()==76,"Ten NPCs give 76 new side quests");
            check(original.values().stream().filter(q->q.id().startsWith("ns_")).allMatch(q->q.objectives().stream().anyMatch(o->o.type().equals("talk")&&!o.target().equals("thedeepnightnpcs:"+q.giverId()))),"Every new side quest requires speaking to another character");
            var q=original.get("rr_letter01");var rowan=npcs.get("rowan");var iona=npcs.get("iona");
            NpcNetwork.open(p,rowan);check(!hasTopic(p,rowan,q.id()+"/1"),"Unaccepted and locked story has no discussion topic");
            state(p,q.id()).putBoolean("accepted",true);
            check(!QuestConversations.available(p,rowan,q,1),"Forged acceptance cannot bypass story prerequisites");state(p,q.id()).remove("accepted");
            for(var old:original.values())if(!old.id().startsWith("rr_")&&!old.id().startsWith("wn_")&&!old.id().startsWith("ns_"))state(p,old.id()).putInt("claims",1);
            IntegrationChecks.talk(p,npcs.get("eldara"));check(QuestEngine.accept(p,q.id()).isEmpty(),"Eldara offers Book IV after the existing finale");
            NpcNetwork.open(p,rowan);check(!hasTopic(p,rowan,q.id()+"/1"),"Earlier material objective must be met before discussion");
            p.getInventory().setItem(0,new ItemStack(Items.PAPER,8));
            NpcNetwork.open(p,iona);discuss(p,iona,q.id()+"/2");check(QuestEngine.view(p,q).progress()[2]==0,"Second NPC cannot be discussed before the first report");
            var mara=npcs.get("mara");NpcNetwork.open(p,mara);discuss(p,mara,q.id()+"/1");check(QuestEngine.view(p,q).progress()[1]==0,"Another NPC cannot impersonate the required speaker");
            QuestEngine.count(p,"interact","thedeepnightnpcs:rowan",100);QuestEngine.count(p,"talk","thedeepnightnpcs:rowan",100);
            NpcNetwork.open(p,rowan);check(hasTopic(p,rowan,q.id()+"/1")&&QuestEngine.view(p,q).progress()[1]==0,"Opening dialogue and lifetime interactions grant no conversation credit");
            var response=discuss(p,rowan,q.id()+"/1");check(response.reply.contains(q.objectives().get(1).dialogue())&&QuestEngine.view(p,q).progress()[1]==1,"Quest topic records its specific authored response");
            check(!hasTopic(p,rowan,q.id()+"/1"),"Discussed topic disappears from available reports");
            discuss(p,rowan,q.id()+"/1");check(state(p,q.id()).getCompound("talks").size()==1&&QuestEngine.view(p,q).progress()[2]==0,"Replay cannot complete another conversation or duplicate progress");
            var loaded=JournalData.load(JournalData.get(p.server).save(new CompoundTag()));check(loaded.player(p.getUUID()).getCompound("quests").getCompound(q.id()).getCompound("talks").getInt("1")==1,"Quest-specific conversations survive NBT save/load");
            NpcNetwork.open(p,iona);check(hasTopic(p,iona,q.id()+"/2"),"First report unlocks the second NPC's topic");
            p.setPos(100,220,50);discuss(p,iona,q.id()+"/2");check(QuestEngine.view(p,q).progress()[2]==0,"Conversation packets fail outside NPC range");p.setPos(50,220,50);
            NpcNetwork.open(p,iona);var wall=new net.minecraft.core.BlockPos(51,221,50);p.serverLevel().setBlockAndUpdate(wall,net.minecraft.world.level.block.Blocks.STONE.defaultBlockState());discuss(p,iona,q.id()+"/2");check(QuestEngine.view(p,q).progress()[2]==0,"Blocked line of sight prevents reporting");p.serverLevel().removeBlock(wall,false);
            NpcNetwork.open(p,iona);NpcNetwork.act(p,new NpcNetwork.Action(iona.getId(),"close",""));discuss(p,iona,q.id()+"/2");check(QuestEngine.view(p,q).progress()[2]==0,"Closed dialogue cannot record a pending report");
            NpcNetwork.open(p,iona);for(String bad:List.of("",q.id()+"/abc",q.id()+"/-1",q.id()+"/99","missing/1"))discuss(p,iona,bad);
            check(QuestEngine.view(p,q).progress()[2]==0,"Malformed and out-of-bounds conversation requests are harmless");
            NpcNetwork.act(p,new NpcNetwork.Action(iona.getId(),"covenant_talk",q.id()+"/2"));check(QuestEngine.view(p,q).status()==QuestEngine.READY,"NPC network action finishes the ordered two-character report");
            IntegrationChecks.talk(p,iona);check(!QuestEngine.claim(p,q.id(),"",-1).isEmpty(),"Report recipient cannot turn in Eldara's main quest");
            IntegrationChecks.talk(p,npcs.get("eldara"));check(QuestEngine.claim(p,q.id(),"",-1).isEmpty()&&p.getInventory().countItem(Items.EMERALD)==q.emeralds(),"Return to Eldara completes the new chapter opener and pays once");
            var legacy=original.get("ac_m03");check(QuestEngine.view(p,legacy).status()==QuestEngine.CLAIMED&&state(p,legacy.id()).getCompound("talks").isEmpty(),"Legacy completions remain completed after conversation objectives are appended");
            for(String owner:List.of("rowan","sera","mara","elian","iona","tamsin","fen","ysolde","caelen","jorge")) {
                var side=original.get("ns_"+owner+"01");var giver=npcs.get(owner);p.getInventory().clearContent();
                IntegrationChecks.talk(p,npcs.get("eldara"));check(!QuestEngine.accept(p,side.id()).isEmpty(),"Eldara cannot give new "+owner+" side story");
                IntegrationChecks.talk(p,giver);check(QuestEngine.accept(p,side.id()).isEmpty(),owner+" offers their new side story in person");
                for(int k=0;k<side.objectives().size();k++) {
                    var o=side.objectives().get(k);
                    if(o.type().equals("talk")){var target=npcs.get(o.target().split(":")[1]);NpcNetwork.open(p,target);NpcNetwork.act(p,new NpcNetwork.Action(target.getId(),"covenant_talk",side.id()+"/"+k));}
                    else if(o.type().equals("kill")){var type=net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES.getValue(new net.minecraft.resources.ResourceLocation(o.target()));p.getStats().setValue(p,net.minecraft.stats.Stats.ENTITY_KILLED.get(type),o.count());}
                    else if(o.type().equals("collect")||o.type().equals("submit"))p.getInventory().add(new ItemStack(net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(new net.minecraft.resources.ResourceLocation(o.target())),o.count()));
                    else QuestEngine.count(p,o.type(),o.target(),o.count());
                }
                check(QuestEngine.view(p,side).status()==QuestEngine.READY,owner+" side story requires and records its other NPC's response");
                IntegrationChecks.talk(p,giver);check(QuestEngine.claim(p,side.id(),"",-1).isEmpty()&&QuestEngine.view(p,original.get("ns_"+owner+"02")).status()==QuestEngine.OFFERED,owner+" rewards the report and offers the next side quest");
            }
            var expanded=new LinkedHashMap<>(original);
            var repeat=QuestChecks.fixture("qa_repeat_talk",List.of(new Quest.Objective("talk","thedeepnightnpcs:rowan",1,"Report","A fresh report for this accepted contract.")),1,List.of(),List.of());expanded.put(repeat.id(),repeat);Campaign.quests=expanded;
            IntegrationChecks.talk(p,npcs.get("eldara"));QuestEngine.accept(p,repeat.id());NpcNetwork.open(p,rowan);discuss(p,rowan,repeat.id()+"/0");IntegrationChecks.talk(p,npcs.get("eldara"));check(QuestEngine.claim(p,repeat.id(),"",-1).isEmpty(),"Repeatable conversation can complete its accepted cycle");
            state(p,repeat.id()).putLong("next",0);IntegrationChecks.talk(p,npcs.get("eldara"));QuestEngine.accept(p,repeat.id());check(QuestEngine.view(p,repeat).progress()[0]==0,"Renewed acceptance clears prior-cycle conversation credit");
            NpcNetwork.open(p,rowan);check(hasTopic(p,rowan,repeat.id()+"/0"),"Repeatable report becomes discussable again after fresh acceptance");
            var invalid=QuestChecks.fixture("bad",List.of(new Quest.Objective("talk","thedeepnightnpcs:rowan",1,"Report")),0,List.of(),List.of());boolean rejected=false;try{Campaign.validate(Map.of(invalid.id(),invalid));}catch(IllegalArgumentException ex){rejected=true;}check(rejected,"Conversation definitions require authored response text");
        } finally {
            Campaign.quests=original;npcs.values().forEach(NpcEntity::discard);QuestServer.sessions.remove(p.getUUID());p.setNoGravity(false);QuestEngine.journal(p).getAllKeys().stream().toList().forEach(QuestEngine.journal(p)::remove);JournalData.get(p.server).setDirty();
        }
    }
}
