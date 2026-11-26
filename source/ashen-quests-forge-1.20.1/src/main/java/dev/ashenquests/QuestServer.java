package dev.ashenquests;

import dev.thedeepnightnpcs.entity.NpcEntity;
import dev.thedeepnightnpcs.network.NpcNetwork;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import java.util.*;

public final class QuestServer {
    public static final Map<UUID,Session> sessions=new HashMap<>();
    public static final class Session {
        public final long token=new Random().nextLong();
        public final int revision=Campaign.revision;
        public final UUID npc;
        public final ResourceKey<Level> dimension;
        public final String giver;
        public final long opened;
        public long until,lastAction;
        public int sequence;
        Session(ServerPlayer player,NpcEntity npc) {
            this.npc=npc==null?null:npc.getUUID();dimension=player.level().dimension();giver=npc==null?"":npc.kind();
            opened=QuestEngine.now(player);until=opened+100;lastAction=opened-5;
        }
    }
    public record Meta(int count,String pinned,Map<String,Integer> standing,String giver,boolean conversation) {}
    public record Notice(boolean success,String message) {}
    public record Hud(Quest quest,QuestEngine.View view) {}
    public static void open(ServerPlayer p) { open(p,null); }
    public static void openConversation(ServerPlayer p,NpcEntity npc) {
        if(NpcNetwork.authorized(p,npc))open(p,npc);
    }
    private static void open(ServerPlayer p,NpcEntity npc) {
        if(!p.isAlive()||p.isSpectator()||p.containerMenu!=p.inventoryMenu)return;
        Session previous=sessions.get(p.getUUID());
        if(npc==null&&previous!=null&&previous.npc==null&&previous.revision==Campaign.revision&&QuestEngine.now(p)-previous.opened<10) {
            previous.until=QuestEngine.now(p)+100;QuestNetwork.send(p,previous.token,2,"");return;
        }
        // Opening the hotkey journal always revokes any previous NPC authority.
        var s=new Session(p,npc);sessions.put(p.getUUID(),s);
        QuestNetwork.send(p,s.token,0,meta(p));
        for(Quest q:Campaign.quests.values())QuestNetwork.send(p,s.token,1,q);
        sync(p,s);QuestNetwork.send(p,s.token,2,"");
    }
    public static boolean validConversation(ServerPlayer p,Session s) {
        if(s==null||s.npc==null||s.revision!=Campaign.revision||s.until<QuestEngine.now(p)||!s.dimension.equals(p.level().dimension()))return false;
        var entity=p.serverLevel().getEntity(s.npc);
        return entity instanceof NpcEntity npc && npc.kind().equals(s.giver) && NpcNetwork.authorized(p,npc) && p.containerMenu==p.inventoryMenu;
    }
    public static boolean canTransact(ServerPlayer p,Quest q) {
        Session s=sessions.get(p.getUUID());return validConversation(p,s)&&s.giver.equals(q.giverId());
    }
    public static Meta meta(ServerPlayer p) {
        var journal=QuestEngine.journal(p);var values=journal.getCompound("standing");var standings=new LinkedHashMap<String,Integer>();
        for(String faction:List.of("Dawnstead","Hearthwrights","Archivists","Free Watch","Wayfarers"))standings.put(faction,values.getInt(faction));
        Session s=sessions.get(p.getUUID());
        return new Meta(Campaign.quests.size(),journal.getString("pinned"),standings,s==null?"":s.giver,validConversation(p,s));
    }
    public static void sync(ServerPlayer p,Session s) {
        var batch=new ArrayList<QuestEngine.View>();
        for(var q:Campaign.quests.values()) {batch.add(QuestEngine.view(p,q));if(batch.size()==10){QuestNetwork.send(p,s.token,3,batch);batch.clear();}}
        if(!batch.isEmpty())QuestNetwork.send(p,s.token,3,batch);
        QuestNetwork.send(p,s.token,6,meta(p));
    }
    public static void action(ServerPlayer p,QuestNetwork.Action m) {
        var s=sessions.get(p.getUUID());long now=QuestEngine.now(p);
        if(s==null||s.token!=m.token()||s.revision!=Campaign.revision||m.sequence()!=s.sequence+1||m.kind()<0||m.kind()>4)return;
        s.sequence=m.sequence();
        if(m.kind()==3) {sessions.remove(p.getUUID());return;}
        if(s.until<now) {QuestNetwork.send(p,s.token,4,new Notice(false,"This conversation has ended. Reopen the journal or speak to the quest giver."));QuestNetwork.send(p,s.token,6,meta(p));return;}
        if(m.kind()==0) {
            if(s.npc==null||validConversation(p,s))s.until=now+100;
            else {s.until=now-1;QuestNetwork.send(p,s.token,6,meta(p));}
            return;
        }
        if(now-s.lastAction<4) {QuestNetwork.send(p,s.token,4,new Notice(false,"Please wait a moment before the next action."));return;}
        s.lastAction=now;
        if(m.kind()==1||m.kind()==4) {
            String error=m.kind()==4?QuestEngine.accept(p,m.id()):QuestEngine.claim(p,m.id(),m.answer(),m.choice());
            QuestNetwork.send(p,s.token,4,new Notice(error.isEmpty(),error.isEmpty()?(m.kind()==4?"Quest accepted. Use - to track your progress.":"Reward claimed. Speak to me about your next quest."):error));
        } else if(m.kind()==2&&Campaign.quests.containsKey(m.id())) {
            var data=JournalData.get(p.server);var journal=data.player(p.getUUID());journal.putString("pinned",journal.getString("pinned").equals(m.id())?"":m.id());data.setDirty();
        }
        sync(p,s);sendHud(p);
    }
    public static void tick(ServerPlayer p) {
        var s=sessions.get(p.getUUID());
        if(s!=null&&s.until>=QuestEngine.now(p)) {
            if(s.revision!=Campaign.revision)open(p);
            else if(s.npc!=null&&!validConversation(p,s)) {s.until=QuestEngine.now(p)-1;QuestNetwork.send(p,s.token,6,meta(p));}
            else sync(p,s);
        }
        if(p.tickCount%40==0)sendHud(p);
    }
    public static void sendHud(ServerPlayer p) {
        String id=QuestEngine.journal(p).getString("pinned");Quest q=Campaign.quests.get(id);
        QuestNetwork.send(p,0,5,new Hud(q,q==null?null:QuestEngine.view(p,q)));
    }
}
