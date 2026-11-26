package dev.deepnight.qa;

import dev.deepnight.*;
import net.minecraft.gametest.framework.*;
import net.minecraft.sounds.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.gametest.*;
import net.minecraftforge.registries.ForgeRegistries;

@GameTestHolder(DeepNight.ID)
@PrefixGameTestTemplate(false)
public final class SoundSafetyTests {
    @GameTest(template="empty")
    public static void missingAndMalformedSoundsCanPlayWithoutCrashing(GameTestHelper t) {
        for (String id : new String[]{null, "", " ", "INVALID SOUND!", "deepnight:missing_future_boss_sound"}) {
            SoundEvent sound = SafeSounds.resolve(id);
            t.assertTrue(sound == SoundEvents.EMPTY, "Expected silence for " + id);
            t.getLevel().playSound(null, t.absolutePos(net.minecraft.core.BlockPos.ZERO), sound,
                SoundSource.HOSTILE, 1, 1);
        }
        var existing = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("simple_mobs:thunderr_spear"));
        t.assertTrue(existing != null && SafeSounds.resolve("simple_mobs:thunderr_spear") == existing,
            "Valid boss sound must be preserved");
        t.assertTrue(SafeSounds.resolve("entity.cow.hurt") == SoundEvents.COW_HURT,
            "Vanilla sound default namespace must be preserved");
        t.succeed();
    }

    @GameTest(template="empty")
    public static void bundledSoundDefinitionsAreRegistered(GameTestHelper t) throws Exception {
        for (String namespace : new String[]{"deepnight", "simple_mobs", "mobosses"}) {
            try (var stream = SoundSafetyTests.class.getResourceAsStream("/assets/" + namespace + "/sounds.json")) {
                t.assertTrue(stream != null, "Missing sound catalog " + namespace);
                var json = com.google.gson.JsonParser.parseReader(new java.io.InputStreamReader(
                    stream, java.nio.charset.StandardCharsets.UTF_8)).getAsJsonObject();
                for (String name : json.keySet()) {
                    var id = new ResourceLocation(namespace, name);
                    t.assertTrue(ForgeRegistries.SOUND_EVENTS.getValue(id) != null, "Unregistered sound " + id);
                }
            }
        }
        t.succeed();
    }
}
