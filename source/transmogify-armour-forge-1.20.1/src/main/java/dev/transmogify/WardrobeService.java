package dev.transmogify;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import java.util.*;

public final class WardrobeService {
    private record Session(UUID token, long opened, ItemStack[] equipment) {}
    private static final Map<ServerPlayer, Session> SESSIONS = new WeakHashMap<>();
    private WardrobeService() {}

    public static void open(ServerPlayer player) {
        TransmogNetwork.send(player, begin(player));
    }
    public static TransmogNetwork.Open begin(ServerPlayer player) {
        ItemStack[] equipped = snapshot(player);
        Session session = new Session(UUID.randomUUID(), player.level().getGameTime(), equipped);
        SESSIONS.put(player, session);
        return new TransmogNetwork.Open(session.token(), equipped);
    }
    public static ItemStack[] snapshot(ServerPlayer player) {
        ItemStack[] result = new ItemStack[4];
        for (int i = 0; i < 4; i++) result[i] = player.getItemBySlot(ArmourData.SLOTS[i]).copy();
        return result;
    }
    public static String validate(ItemStack[] equipment, String[] choices) {
        if (equipment.length != 4 || choices.length != 4) return "Invalid outfit.";
        for (int i = 0; i < 4; i++) {
            if (!choices[i].isEmpty() && !ArmourData.fits(equipment[i], ArmourData.SLOTS[i]))
                return "Equip armour in the " + ArmourData.LABELS[i].toLowerCase(Locale.ROOT) + " slot first.";
            if (!choices[i].isEmpty() && ArmourData.target(choices[i], ArmourData.SLOTS[i]) == null)
                return "That appearance is not valid for the " + ArmourData.LABELS[i].toLowerCase(Locale.ROOT) + " slot.";
        }
        return "";
    }
    public static String apply(ServerPlayer player, UUID token, String[] choices) {
        Session session = SESSIONS.remove(player);
        if (session == null || !session.token().equals(token)) return "This wardrobe session expired. Reopen it with =.";
        if (player.level().getGameTime() - session.opened() > 12000) return "This wardrobe session expired. Reopen it with =.";
        if (!player.isAlive() || player.isSpectator()) return "You cannot change armour right now.";
        ItemStack[] current = snapshot(player);
        for (int i = 0; i < 4; i++) {
            if (!ItemStack.matches(current[i], session.equipment()[i]))
                return "Your equipment changed. Reopen the wardrobe with =.";
        }
        String error = validate(current, choices);
        if (!error.isEmpty()) return error;
        // Validate the complete outfit before touching any item. Vanilla synchronizes its NBT.
        for (int i = 0; i < 4; i++) {
            ItemStack real = player.getItemBySlot(ArmourData.SLOTS[i]);
            if (ArmourData.fits(real, ArmourData.SLOTS[i])) ArmourData.set(real, choices[i]);
        }
        player.getInventory().setChanged();
        player.inventoryMenu.broadcastChanges();
        return "";
    }
}
