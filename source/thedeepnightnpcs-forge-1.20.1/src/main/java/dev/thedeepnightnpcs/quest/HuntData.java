package dev.thedeepnightnpcs.quest;

import net.minecraft.nbt.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import java.util.*;

public final class HuntData extends SavedData {
    public record Contract(UUID id, String target, int required, int emeralds, int xp, String style) {
        public CompoundTag save() {
            CompoundTag n = new CompoundTag(); n.putUUID("id", id); n.putString("target", target); n.putInt("required", required);
            n.putInt("emeralds", emeralds); n.putInt("xp", xp); n.putString("style", style); return n;
        }
        public static Contract load(CompoundTag n) {
            return new Contract(n.getUUID("id"), n.getString("target"), Math.max(1, n.getInt("required")),
                Math.max(0, n.getInt("emeralds")), Math.max(0, n.getInt("xp")), n.getString("style"));
        }
    }
    public static final class Record {
        public final List<Contract> offers = new ArrayList<>();
        public Contract active;
        public int kills, completed;
        public long nextRefresh;
        public CompoundTag save() {
            CompoundTag n = new CompoundTag(); ListTag list = new ListTag(); offers.forEach(c -> list.add(c.save())); n.put("offers", list);
            if (active != null) n.put("active", active.save());
            n.putInt("kills", kills); n.putInt("completed", completed); n.putLong("nextRefresh", nextRefresh); return n;
        }
        public static Record load(CompoundTag n) {
            Record r = new Record();
            for (Tag t : n.getList("offers", Tag.TAG_COMPOUND)) r.offers.add(Contract.load((CompoundTag)t));
            if (n.contains("active")) r.active = Contract.load(n.getCompound("active"));
            r.kills = Math.max(0, n.getInt("kills")); r.completed = Math.max(0, n.getInt("completed")); r.nextRefresh = n.getLong("nextRefresh"); return r;
        }
    }
    private final Map<UUID, Record> records = new HashMap<>();
    public static HuntData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(HuntData::load, HuntData::new, "thedeepnightnpcs_hunts");
    }
    public Record player(UUID id) {
        return records.computeIfAbsent(id, key -> { setDirty(); return new Record(); });
    }
    public static HuntData load(CompoundTag n) {
        HuntData data = new HuntData();
        for (String key : n.getAllKeys()) {
            try { data.records.put(UUID.fromString(key), Record.load(n.getCompound(key))); }
            catch (RuntimeException error) { dev.thedeepnightnpcs.DeepNightNpcs.LOG.warn("Skipping invalid hunt record {}", key, error); }
        }
        return data;
    }
    @Override public CompoundTag save(CompoundTag n) { records.forEach((id,r) -> n.put(id.toString(), r.save())); return n; }
}
