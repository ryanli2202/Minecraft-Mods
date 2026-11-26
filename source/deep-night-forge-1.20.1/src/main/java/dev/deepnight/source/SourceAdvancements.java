package dev.deepnight.source;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import java.util.HashSet;
import java.util.Set;

/** Optional source advancements must never abort a boss's delayed victory routine. */
public final class SourceAdvancements {
    private static final Set<String> missing=new HashSet<>();
    public static boolean award(ServerPlayer player,String name){
        var id=ResourceLocation.tryParse(name);
        var advancement=id==null?null:player.server.getAdvancements().getAdvancement(id);
        if(advancement==null){
            if(missing.add(name))dev.deepnight.source.dawn.SimpleMobsMod.LOGGER.warn("Skipping unavailable boss advancement {}",name);
            return false;
        }
        var progress=player.getAdvancements().getOrStartProgress(advancement);
        var criteria=new java.util.ArrayList<String>();progress.getRemainingCriteria().forEach(criteria::add);
        for(String criterion:criteria)player.getAdvancements().award(advancement,criterion);
        return true;
    }
}
