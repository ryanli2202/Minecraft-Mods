package dev.deepnight.source;

import dev.deepnight.source.dawn.entity.DragonLordEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

/** Persistent ownership for the Dragon Lord's separately spawned hitboxes. */
public final class DragonLordParts {
    private static final String OWNER = "DeepNightDragonLordOwner";

    public static void bind(Entity part, Entity dragon) {
        part.getPersistentData().putUUID(OWNER, dragon.getUUID());
    }

    public static DragonLordEntity owner(Entity part) {
        if (!(part.level() instanceof ServerLevel level)) return null;
        var data = part.getPersistentData();
        if (data.hasUUID(OWNER)) {
            var entity = level.getEntity(data.getUUID(OWNER));
            return entity instanceof DragonLordEntity dragon && dragon.isAlive() ? dragon : null;
        }
        // Adopt hitboxes saved by older releases once; never reassign an owned part.
        var dragon = level.getEntitiesOfClass(DragonLordEntity.class,
                part.getBoundingBox().inflate(32), Entity::isAlive).stream()
                .min(java.util.Comparator.comparingDouble(part::distanceToSqr)).orElse(null);
        if (dragon != null) bind(part, dragon);
        return dragon;
    }

    public static boolean belongsTo(Entity part, Entity dragon) {
        return owner(part) == dragon;
    }

    public static boolean hurt(Entity part, DamageSource source, float amount) {
        if (!part.isAlive() || amount <= 0 || source.is(net.minecraft.world.damagesource.DamageTypes.IN_WALL)) return false;
        var dragon = owner(part);
        // The parent owns health, armor, immunity, hit cooldowns and kill attribution.
        return dragon != null && dragon.hurt(source, amount);
    }
}
