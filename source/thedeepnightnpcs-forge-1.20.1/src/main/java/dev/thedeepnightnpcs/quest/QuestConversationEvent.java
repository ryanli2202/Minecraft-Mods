package dev.thedeepnightnpcs.quest;

import dev.thedeepnightnpcs.entity.NpcEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.Event;

/** Posted on the server after a real, authorized NPC conversation requests quests. */
public final class QuestConversationEvent extends Event {
    private final ServerPlayer player;
    private final NpcEntity npc;
    public QuestConversationEvent(ServerPlayer player, NpcEntity npc) { this.player=player; this.npc=npc; }
    public ServerPlayer player() { return player; }
    public NpcEntity npc() { return npc; }
}
