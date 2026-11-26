package dev.emeraldcovenant;

import net.minecraft.nbt.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import java.util.*;

public final class GuildData extends SavedData {
    private final Map<UUID,Integer> members=new HashMap<>();
    public static GuildData get(MinecraftServer server){return server.overworld().getDataStorage().computeIfAbsent(GuildData::load,GuildData::new,"emeraldcovenant_guild");}
    public int mask(UUID id){return members.getOrDefault(id,0);}
    public void unlock(UUID id,int bit){members.put(id,mask(id)|bit);setDirty();}
    public static GuildData load(CompoundTag tag){var data=new GuildData();for(String key:tag.getAllKeys())try{data.members.put(UUID.fromString(key),tag.getInt(key)&15);}catch(IllegalArgumentException ignored){}return data;}
    @Override public CompoundTag save(CompoundTag tag){members.forEach((id,mask)->tag.putInt(id.toString(),mask));return tag;}
}
