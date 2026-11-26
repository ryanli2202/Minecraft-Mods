package dev.transmogify;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.ForgeRegistries;

/** The only persistent change is our namespaced cosmetic tag on the real item. */
public final class ArmourData {
    public static final String TAG = "transmogifyarmour:appearance";
    public static final EquipmentSlot[] SLOTS = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    public static final String[] LABELS = {"Head", "Chest", "Legs", "Feet"};
    private ArmourData() {}

    public static boolean fits(ItemStack stack, EquipmentSlot slot) {
        return !stack.isEmpty() && stack.getItem() instanceof ArmorItem armour && armour.getEquipmentSlot() == slot;
    }
    public static Item target(String id, EquipmentSlot slot) {
        ResourceLocation key = ResourceLocation.tryParse(id);
        if (key == null || !ForgeRegistries.ITEMS.containsKey(key)) return null;
        Item item = ForgeRegistries.ITEMS.getValue(key);
        return item instanceof ArmorItem armour && armour.getEquipmentSlot() == slot ? item : null;
    }
    public static String id(Item item) { return ForgeRegistries.ITEMS.getKey(item).toString(); }
    public static String appearanceId(ItemStack stack) {
        return stack.hasTag() ? stack.getTag().getString(TAG) : "";
    }
    public static void set(ItemStack stack, String id) {
        if (id.isEmpty()) {
            stack.removeTagKey(TAG);
        } else {
            stack.getOrCreateTag().putString(TAG, id);
        }
    }
    public static ItemStack visual(ItemStack real, EquipmentSlot slot) {
        if (!fits(real, slot)) return real;
        Item item = target(appearanceId(real), slot);
        if (item == null) return real;
        ItemStack result = item.getDefaultInstance().copy();
        // Keep glint, dye and trim, but never transfer gameplay capabilities or other mods' NBT.
        CompoundTag data = real.getTag();
        if (data != null) {
            for (String key : new String[]{"Enchantments", "Trim"})
                if (data.contains(key)) result.getOrCreateTag().put(key, data.get(key).copy());
            if (item instanceof DyeableLeatherItem && data.getCompound("display").contains("color"))
                result.getOrCreateTagElement("display").putInt("color", data.getCompound("display").getInt("color"));
        }
        return result;
    }
}
