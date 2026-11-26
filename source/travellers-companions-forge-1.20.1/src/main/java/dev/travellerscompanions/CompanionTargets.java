package dev.travellerscompanions;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;

/** Unprovoked aggression stays monster-only; owner-directed combat can include other mobs. */
public final class CompanionTargets {
    private CompanionTargets() {}

    public static boolean isOwnerAttackTarget(LivingEntity target) {
        return target instanceof Mob && !(target instanceof CompanionEntity);
    }

    public static boolean isMonster(LivingEntity target) {
        if (target == null || target instanceof Player || target instanceof CompanionEntity
                || target instanceof NeutralMob || target instanceof Piglin
                || target instanceof TamableAnimal pet && pet.isTame()) return false;
        // Enemy includes monsters such as slimes, ghasts and phantoms that do not extend Monster.
        return target instanceof Enemy || target.getType().getCategory() == MobCategory.MONSTER;
    }
}
