package dev.travellerscompanions;

import java.util.*;
import net.minecraft.nbt.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

/** World ledger is authoritative. Entity UUIDs are leases: stale chunk copies discard themselves. */
public final class CompanionData extends SavedData {
    public static final long RECOVERY_MILLIS=600_000L;
    public static final int MAX_LEVEL=20;
    public static final int PARTY_LIMIT=2;
    private final Map<UUID,Roster> players=new HashMap<>();
    public static class Entry {
        public int level=1,bond,kills,dialogue;
        public long readyAt,rallyAt;
        public boolean initialized;
        public CompoundTag snapshot=new CompoundTag();
        public CompoundTag save(){var n=new CompoundTag();n.putInt("Level",level);n.putInt("Bond",bond);n.putInt("Kills",kills);n.putInt("Dialogue",dialogue);n.putLong("ReadyAt",readyAt);n.putLong("RallyAt",rallyAt);n.putBoolean("Initialized",initialized);n.put("Snapshot",snapshot.copy());return n;}
        public static Entry load(CompoundTag n){var e=new Entry();e.level=Math.max(1,Math.min(MAX_LEVEL,n.getInt("Level")));e.bond=n.getInt("Bond");e.kills=n.getInt("Kills");e.dialogue=n.getInt("Dialogue");e.readyAt=n.getLong("ReadyAt");e.rallyAt=n.getLong("RallyAt");e.initialized=n.getBoolean("Initialized");e.snapshot=n.getCompound("Snapshot").copy();return e;}
    }
    public static class TroopEntry {
        public boolean initialized;
        public long readyAt;
        public CompoundTag snapshot=new CompoundTag();
        public CompoundTag save(){var n=new CompoundTag();n.putBoolean("Initialized",initialized);n.putLong("ReadyAt",readyAt);n.put("Snapshot",snapshot.copy());return n;}
        public static TroopEntry load(CompoundTag n){var e=new TroopEntry();e.initialized=n.getBoolean("Initialized");e.readyAt=n.getLong("ReadyAt");e.snapshot=n.getCompound("Snapshot").copy();return e;}
    }
    public static class Roster {
        public final Entry[] entries=java.util.stream.IntStream.range(0,CharacterKind.COUNT).mapToObj(i->new Entry()).toArray(Entry[]::new);
        public final Map<Integer,UUID> party=new LinkedHashMap<>();
        public final TroopEntry[][] troops=new TroopEntry[TroopKind.values().length][TroopKind.SIZE];
        public final Map<Integer,UUID> squad=new LinkedHashMap<>();
        public int squadKind=-1;
        public Roster(){for(int kind=0;kind<troops.length;kind++)for(int slot=0;slot<TroopKind.SIZE;slot++)troops[kind][slot]=new TroopEntry();}
        public CompoundTag save(){
            var n=new CompoundTag();var members=new ListTag();
            party.forEach((kind,entity)->{var member=new CompoundTag();member.putInt("Kind",kind);member.putUUID("Entity",entity);members.add(member);});n.put("Party",members);
            var list=new ListTag();for(var e:entries)list.add(e.save());n.put("Entries",list);
            var groups=new ListTag();for(var group:troops){var slots=new ListTag();for(var e:group)slots.add(e.save());var g=new CompoundTag();g.put("Members",slots);groups.add(g);}n.put("Troops",groups);
            n.putInt("SquadKind",squadKind);var leases=new ListTag();squad.forEach((slot,id)->{var member=new CompoundTag();member.putInt("Slot",slot);member.putUUID("Entity",id);leases.add(member);});n.put("Squad",leases);return n;
        }
        public static Roster load(CompoundTag n){
            var r=new Roster();var list=n.getList("Entries",Tag.TAG_COMPOUND);for(int i=0;i<Math.min(r.entries.length,list.size());i++)r.entries[i]=Entry.load(list.getCompound(i));
            if(n.contains("Party",Tag.TAG_LIST)){
                for(Tag raw:n.getList("Party",Tag.TAG_COMPOUND)){var member=(CompoundTag)raw;int kind=member.getInt("Kind");
                    if(member.contains("Kind",Tag.TAG_INT)&&kind>=0&&kind<r.entries.length&&member.hasUUID("Entity")&&r.party.size()<PARTY_LIMIT&&!r.party.containsKey(kind)&&!r.party.containsValue(member.getUUID("Entity")))r.party.put(kind,member.getUUID("Entity"));
                }
            }else if(n.contains("Active",Tag.TAG_INT)&&n.hasUUID("Entity")){
                int kind=n.getInt("Active");if(kind>=0&&kind<r.entries.length)r.party.put(kind,n.getUUID("Entity"));
            }
            var groups=n.getList("Troops",Tag.TAG_COMPOUND);for(int kind=0;kind<Math.min(groups.size(),r.troops.length);kind++){var slots=groups.getCompound(kind).getList("Members",Tag.TAG_COMPOUND);for(int slot=0;slot<Math.min(slots.size(),TroopKind.SIZE);slot++)r.troops[kind][slot]=TroopEntry.load(slots.getCompound(slot));}
            if(n.contains("SquadKind",Tag.TAG_INT)&&TroopKind.valid(n.getInt("SquadKind"))){
                r.squadKind=n.getInt("SquadKind");for(Tag raw:n.getList("Squad",Tag.TAG_COMPOUND)){var member=(CompoundTag)raw;int slot=member.getInt("Slot");if(member.contains("Slot",Tag.TAG_INT)&&slot>=0&&slot<TroopKind.SIZE&&member.hasUUID("Entity")&&!r.squad.containsKey(slot)&&!r.squad.containsValue(member.getUUID("Entity"))&&!r.party.containsValue(member.getUUID("Entity")))r.squad.put(slot,member.getUUID("Entity"));}
            }
            if(r.squad.isEmpty())r.squadKind=-1;
            return r;
        }
    }
    public Roster roster(UUID id){return players.computeIfAbsent(id,k->{setDirty();return new Roster();});}
    public static CompanionData get(MinecraftServer server){return server.overworld().getDataStorage().computeIfAbsent(CompanionData::load,CompanionData::new,TravellersCompanions.ID);}
    public static CompanionData load(CompoundTag n){var d=new CompanionData();for(Tag t:n.getList("Players",Tag.TAG_COMPOUND)){var p=(CompoundTag)t;if(p.hasUUID("Owner"))d.players.put(p.getUUID("Owner"),Roster.load(p));}return d;}
    @Override public CompoundTag save(CompoundTag n){var list=new ListTag();players.forEach((id,r)->{var p=r.save();p.putUUID("Owner",id);list.add(p);});n.put("Players",list);n.putInt("Schema",3);return n;}
}
