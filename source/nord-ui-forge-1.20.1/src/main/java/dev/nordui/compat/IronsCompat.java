package dev.nordui.compat;

import dev.nordui.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.ModList;

/** Keep optional classes behind a loading boundary on both physical sides. */
public final class IronsCompat {
    public static boolean loaded() { return ModList.get().isLoaded("irons_spellbooks"); }
    public static void register() { if (loaded()) dev.nordui.compat.irons.SpellMenu.register(); }
    public static void open(ServerPlayer player) { if (loaded()) dev.nordui.compat.irons.SpellMenu.open(player); }
}
