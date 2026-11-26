package dev.thedeepnightnpcs.quest;

import dev.thedeepnightnpcs.entity.NpcEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.Event;

/** Optional quest integration. Empty request collects topics; a request discusses one. */
public final class QuestDialogueEvent extends Event {
    public final ServerPlayer player;
    public final NpcEntity npc;
    public final CompoundTag state;
    public final String request;
    public String reply="";
    public QuestDialogueEvent(ServerPlayer player,NpcEntity npc,CompoundTag state,String request) {
        this.player=player;this.npc=npc;this.state=state;this.request=request;
    }
}
