package dev.ashenquests;

import dev.thedeepnightnpcs.entity.NpcEntity;
import dev.thedeepnightnpcs.network.NpcNetwork;
import dev.thedeepnightnpcs.quest.QuestDialogueEvent;
import net.minecraft.nbt.*;
import net.minecraft.server.level.ServerPlayer;

/** Conversation progress belongs to this accepted quest, never to lifetime clicks. */
public final class QuestConversations {
    public static boolean available(ServerPlayer p,NpcEntity npc,Quest q,int index) {
        if(index<0||index>=q.objectives().size())return false;
        var state=QuestEngine.state(p,q.id());var o=q.objectives().get(index);
        if(!state.getBoolean("accepted")||!o.type().equals("talk")||!o.target().equals("thedeepnightnpcs:"+npc.kind())||state.getCompound("talks").getInt(""+index)>0)return false;
        if(!NpcNetwork.authorized(p,npc)||p.containerMenu!=p.inventoryMenu)return false;
        var view=QuestEngine.view(p,q);
        if(view.status()!=QuestEngine.ACTIVE&&view.status()!=QuestEngine.READY)return false;
        for(int i=0;i<index;i++)if(!q.objectives().get(i).type().equals("field")&&view.progress()[i]<q.objectives().get(i).count())return false;
        return true;
    }
    public static void handle(QuestDialogueEvent event) {
        var p=event.player;var npc=event.npc;
        if(event.request.isEmpty()) {
            var topics=new ListTag();
            for(Quest q:Campaign.quests.values())for(int i=0;i<q.objectives().size();i++)if(available(p,npc,q,i)) {
                var topic=new CompoundTag();topic.putString("title",q.title());topic.putString("subject",q.objectives().get(i).label());topic.putString("request",q.id()+"/"+i);topics.add(topic);
            }
            event.state.put("questTopics",topics);return;
        }
        int split=event.request.lastIndexOf('/');if(split<1)return;
        Quest q=Campaign.quests.get(event.request.substring(0,split));if(q==null)return;
        int index;try{index=Integer.parseInt(event.request.substring(split+1));}catch(NumberFormatException ex){return;}
        if(!available(p,npc,q,index)){event.reply="We cannot discuss that report yet. Accept the quest and complete its earlier objectives first.";return;}
        var data=JournalData.get(p.server);var state=JournalData.child(JournalData.child(data.player(p.getUUID()),"quests"),q.id());
        JournalData.child(state,"talks").putInt(""+index,1);data.setDirty();
        event.reply=q.title()+"\n\n"+q.objectives().get(index).dialogue()+"\n\nConversation recorded. Check your journal for the next step.";
        QuestServer.sendHud(p);
    }
}
