package dev.deepnightguide.client;

import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import java.util.*;

/** Conservative evidence-based detection; ordinary high-health mobs are not assumed to be bosses. */
public final class BossDiscovery {
    private static final Set<String> TAGS=Set.of("forge:bosses","c:bosses","minecraft:bosses");
    public static boolean tagged(EntityType<?> type) {
        return type==EntityType.WITHER||type==EntityType.ENDER_DRAGON||type.builtInRegistryHolder().tags().anyMatch(t->TAGS.contains(t.location().toString()));
    }
    public static boolean declaresBossBar(Entity entity) {
        // Deep Night shares NightMob and its bar across bosses, enemies and neutral NPCs.
        if(entity.getClass().getName().startsWith("dev.deepnight."))try {
            Object species=entity.getClass().getMethod("species").invoke(entity);
            return Boolean.TRUE.equals(species.getClass().getMethod("boss").invoke(species));
        }catch(ReflectiveOperationException ignored){return false;}
        // Mowzie's base class allocates bossInfo even for ordinary mobs; its explicit predicate is authoritative.
        if(entity.getClass().getName().startsWith("com.bobmowzie.mowziesmobs."))try {
            return Boolean.TRUE.equals(entity.getClass().getMethod("hasBossBar").invoke(entity));
        }catch(ReflectiveOperationException ignored){return false;}
        for(Class<?> type=entity.getClass();type!=null&&type!=Entity.class;type=type.getSuperclass()) {
            try {
                for(var field:type.getDeclaredFields())if(BossEvent.class.isAssignableFrom(field.getType()))return true;
            }catch(LinkageError|SecurityException ignored) {return false;}
        }
        return false;
    }
    public static void apply(GuideEntry entry,GuideNotes.Note note) {
        entry.boss=entry.inferredBoss;
        if(note!=null) {
            if(note.categories.contains("BOSSES"))entry.boss=true;
            if(note.boss!=null){entry.boss=note.boss;if(entry.boss)entry.bossReason="Confirmed encounter in the guide's source-verified notes.";}
        }
    }
}
