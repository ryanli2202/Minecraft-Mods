package dev.ashenquests.client;

import dev.ashenquests.*;
import net.minecraft.client.Minecraft;
import java.util.*;

public final class ClientJournal {
    public static final Map<String,Quest> quests=new LinkedHashMap<>();
    public static final Map<String,QuestEngine.View> views=new HashMap<>();
    public static QuestServer.Meta meta;
    public static QuestServer.Hud hud;
    public static long token;
    public static int sequence;
    public static boolean loading,pending,success;
    public static String message="";
    public static void open() {loading=true;message="";Minecraft.getInstance().setScreen(new JournalScreen());QuestNetwork.toServer(new QuestNetwork.Open());}
    public static void action(int kind,String id,String answer,int choice) {
        if(loading)return;
        if(kind==1||kind==4){if(pending)return;pending=true;}
        QuestNetwork.toServer(new QuestNetwork.Action(token,++sequence,kind,id,answer,choice));
    }
    public static void receive(QuestNetwork.Payload m) {
        if(m.kind()==5){hud=Campaign.GSON.fromJson(m.json(),QuestServer.Hud.class);return;}
        if(m.kind()==0){token=m.token();sequence=0;loading=true;pending=false;quests.clear();views.clear();meta=Campaign.GSON.fromJson(m.json(),QuestServer.Meta.class);Minecraft.getInstance().setScreen(new JournalScreen());return;}
        if(m.token()!=token)return;
        switch(m.kind()) {
            case 1 -> {Quest q=Campaign.GSON.fromJson(m.json(),Quest.class);quests.put(q.id(),q);}
            case 2 -> {loading=false;if(!(Minecraft.getInstance().screen instanceof JournalScreen))Minecraft.getInstance().setScreen(new JournalScreen());if(Minecraft.getInstance().screen instanceof JournalScreen s)s.loaded();}
            case 3 -> {for(var v:Campaign.GSON.fromJson(m.json(),QuestEngine.View[].class))views.put(v.id(),v);}
            case 4 -> {var n=Campaign.GSON.fromJson(m.json(),QuestServer.Notice.class);message=n.message();success=n.success();pending=false;}
            case 6 -> {meta=Campaign.GSON.fromJson(m.json(),QuestServer.Meta.class);if(!meta.giver().isEmpty()&&!meta.conversation()){pending=false;message="Conversation ended. Speak to the quest giver again.";success=false;}}
        }
    }
    public static void clear() {quests.clear();views.clear();meta=null;hud=null;token=0;sequence=0;loading=false;pending=false;}
}
