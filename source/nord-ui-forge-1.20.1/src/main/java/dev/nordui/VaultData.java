package dev.nordui;

import net.minecraft.nbt.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.items.ItemStackHandler;
import java.util.*;

/** World-scoped UUID ownership survives death, dimension changes and reconnects. */
public final class VaultData extends SavedData {
    public static final int SIZE = 999;
    private final Map<UUID, ItemStackHandler> inventories = new HashMap<>();
    public static VaultData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(VaultData::load, VaultData::new, "nordui_vaults");
    }
    public ItemStackHandler inventory(UUID owner) {
        return inventories.computeIfAbsent(owner, id -> new ItemStackHandler(SIZE) {
            @Override protected void onContentsChanged(int slot) { setDirty(); }
        });
    }
    public static VaultData load(CompoundTag root) {
        VaultData data = new VaultData();
        for (Tag entry : root.getList("Players", Tag.TAG_COMPOUND)) {
            CompoundTag player = (CompoundTag) entry;
            if (!player.hasUUID("Owner")) continue;
            ItemStackHandler inv = data.inventory(player.getUUID("Owner"));
            // Read individual stacks: never trust a serialized Size to resize the 999-slot vault.
            for (Tag value : player.getList("Items", Tag.TAG_COMPOUND)) {
                CompoundTag stack = (CompoundTag) value;
                int slot = stack.getInt("Slot");
                if (slot >= 0 && slot < SIZE) inv.setStackInSlot(slot, ItemStack.of(stack));
            }
        }
        data.setDirty(false);
        return data;
    }
    @Override public CompoundTag save(CompoundTag root) {
        ListTag players = new ListTag();
        inventories.forEach((owner, inv) -> {
            CompoundTag player = new CompoundTag(); player.putUUID("Owner", owner);
            ListTag items = new ListTag();
            for (int slot = 0; slot < SIZE; slot++) if (!inv.getStackInSlot(slot).isEmpty()) {
                CompoundTag stack = inv.getStackInSlot(slot).save(new CompoundTag());
                stack.putInt("Slot", slot); items.add(stack);
            }
            player.put("Items", items); players.add(player);
        });
        root.putInt("Version", 1); root.put("Players", players); return root;
    }
    public static int occupied(ItemStackHandler inv) {
        int n = 0; for (int i = 0; i < inv.getSlots(); i++) if (!inv.getStackInSlot(i).isEmpty()) n++; return n;
    }
}
