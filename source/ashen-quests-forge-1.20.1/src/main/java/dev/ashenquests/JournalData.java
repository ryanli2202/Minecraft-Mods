package dev.ashenquests;

import net.minecraft.nbt.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import java.util.*;

/** Overworld SavedData is shared across dimensions and keyed by player UUID. */
public final class JournalData extends SavedData {
    private final Map<UUID, CompoundTag> players = new HashMap<>();
    public static JournalData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(JournalData::load, JournalData::new, "ashenquests_journals");
    }
    public CompoundTag player(UUID id) { return players.computeIfAbsent(id, key -> new CompoundTag()); }
    public static CompoundTag child(CompoundTag parent, String name) {
        if (!parent.contains(name, Tag.TAG_COMPOUND)) parent.put(name, new CompoundTag());
        return parent.getCompound(name);
    }
    public static JournalData load(CompoundTag tag) {
        var data = new JournalData();
        var members = tag.getCompound("players");
        for (String id : members.getAllKeys()) try { data.players.put(UUID.fromString(id), members.getCompound(id).copy()); } catch (IllegalArgumentException ignored) {}
        return data;
    }
    @Override public CompoundTag save(CompoundTag tag) {
        var members = new CompoundTag(); players.forEach((id, state) -> members.put(id.toString(), state.copy()));
        tag.putInt("version", 1); tag.put("players", members); return tag;
    }
}
