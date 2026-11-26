package dev.travellerscompanions;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.ModList;

/** Optional, read-only access to the owner's real ParCool action. NPCs are not players. */
public final class ParCoolBridge {
    private ParCoolBridge() {}
    public static boolean installed() { return ModList.get().isLoaded("parcool"); }
    public static boolean fastRunning(LivingEntity owner) {
        return installed() && owner instanceof Player player && Loaded.fastRunning(player);
    }
    private static final class Loaded {
        static boolean fastRunning(Player player) {
            var ability = com.alrex.parcool.common.capability.Parkourability.get(player);
            return ability != null && ability.get(com.alrex.parcool.common.action.impl.FastRun.class).isDoing();
        }
    }
}
