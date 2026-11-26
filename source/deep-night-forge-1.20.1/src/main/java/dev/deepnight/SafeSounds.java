package dev.deepnight;

import com.mojang.logging.LogUtils;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.registries.ForgeRegistries;

/** Sound IDs from imported or new combat code must be resolved here. */
public final class SafeSounds {
    private static final Set<String> WARNED = ConcurrentHashMap.newKeySet();

    private SafeSounds() {}

    public static SoundEvent resolve(String name) {
        // Imported effect entities use empty names to mean intentionally silent.
        if (name == null || name.isBlank()) return SoundEvents.EMPTY;
        ResourceLocation id = ResourceLocation.tryParse(name);
        SoundEvent sound = id == null ? null : ForgeRegistries.SOUND_EVENTS.getValue(id);
        if (sound != null) return sound;
        if (WARNED.add(name)) {
            LogUtils.getLogger().warn("Deep Night sound '{}' is invalid or unregistered; using silence", name);
        }
        return SoundEvents.EMPTY;
    }
}
