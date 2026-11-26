package dev.thedeepnightnpcs;

import com.mojang.logging.LogUtils;
import dev.thedeepnightnpcs.entity.NpcEntity;
import dev.thedeepnightnpcs.network.NpcNetwork;
import dev.thedeepnightnpcs.quest.HuntService;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.*;
import org.slf4j.Logger;
import java.util.List;

@Mod(DeepNightNpcs.ID)
public final class DeepNightNpcs {
    public static final String ID = "thedeepnightnpcs";
    public static final Logger LOG = LogUtils.getLogger();
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB, ID);
    public static final RegistryObject<EntityType<NpcEntity>> ELDARA = npc("eldara");
    public static final RegistryObject<EntityType<NpcEntity>> CAELEN = npc("caelen");
    public static final RegistryObject<EntityType<NpcEntity>> GUARD = npc("spire_guard");
    public static final RegistryObject<EntityType<NpcEntity>> JORGE = npc("jorge");
    public static final RegistryObject<Item> ELDARA_EGG = egg("eldara", ELDARA, 0x573876, 0xf5db97);
    public static final RegistryObject<Item> CAELEN_EGG = egg("caelen", CAELEN, 0x253535, 0xb28354);
    public static final RegistryObject<Item> GUARD_EGG = egg("spire_guard", GUARD, 0x6b5360, 0xd9b291);
    public static final RegistryObject<Item> JORGE_EGG = egg("jorge", JORGE, 0x46392b, 0xc0b19b);
    public static final java.util.Map<String, RegistryObject<EntityType<NpcEntity>>> COVENANT_NPCS = new java.util.LinkedHashMap<>();
    public static final java.util.List<RegistryObject<Item>> COVENANT_EGGS = new java.util.ArrayList<>();
    static {
        for (var entry : NpcRoster.COMPANIONS) {
            var type = npc(entry.id()); COVENANT_NPCS.put(entry.id(), type);
            COVENANT_EGGS.add(egg(entry.id(), type, entry.coat(), entry.trim()));
        }
    }
    public static final RegistryObject<CreativeModeTab> TAB = TABS.register("npcs", () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup.thedeepnightnpcs")).icon(() -> ELDARA_EGG.get().getDefaultInstance())
        .displayItems((parameters, output) -> { output.accept(ELDARA_EGG.get()); output.accept(CAELEN_EGG.get()); output.accept(GUARD_EGG.get()); output.accept(JORGE_EGG.get()); COVENANT_EGGS.forEach(egg -> output.accept(egg.get())); }).build());
    public static final ForgeConfigSpec CONFIG;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> INCLUDE, EXCLUDE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> EXTRA_INGOTS, EXCLUDED_INGOTS, EXCLUDED_ENCHANTMENTS;
    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        INCLUDE = builder.comment("Additional installed living entity IDs to include when a mod does not mark its hostile mobs as monsters. Also supports entity-type tag thedeepnightnpcs:hunt_targets.")
            .defineListAllowEmpty(List.of("additionalHostileMobs"), List::of, value -> value instanceof String s && net.minecraft.resources.ResourceLocation.tryParse(s) != null);
        EXCLUDE = builder.comment("Entity IDs never offered by Caelen, including unavailable encounters. Also supports entity-type tag thedeepnightnpcs:excluded_hunt_targets.")
            .defineListAllowEmpty(List.of("excludedMobs"), List::of, value -> value instanceof String s && net.minecraft.resources.ResourceLocation.tryParse(s) != null);
        builder.push("jorge");
        EXTRA_INGOTS = builder.comment("Extra item IDs to treat as ingots; tagged ingots and *_ingot items are detected automatically.")
            .defineListAllowEmpty(List.of("additionalIngots"), List::of, value -> value instanceof String s && net.minecraft.resources.ResourceLocation.tryParse(s) != null);
        EXCLUDED_INGOTS = builder.comment("Item IDs excluded from Jorge's ingot shop. Use this to exclude progression-critical materials.")
            .defineListAllowEmpty(List.of("excludedIngots"), List::of, value -> value instanceof String s && net.minecraft.resources.ResourceLocation.tryParse(s) != null);
        EXCLUDED_ENCHANTMENTS = builder.comment("Enchantment IDs excluded from Jorge's book shop. Only tradeable enchantments are selected.")
            .defineListAllowEmpty(List.of("excludedEnchantments"), List::of, value -> value instanceof String s && net.minecraft.resources.ResourceLocation.tryParse(s) != null);
        builder.pop();
        CONFIG = builder.build();
    }
    public DeepNightNpcs() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ENTITIES.register(bus); ITEMS.register(bus); TABS.register(bus);
        bus.addListener(this::attributes);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CONFIG);
        NpcNetwork.register();
    }
    private void attributes(EntityAttributeCreationEvent event) {
        for (var type : allNpcs()) event.put(type.get(), NpcEntity.attributes().build());
    }
    public static List<RegistryObject<EntityType<NpcEntity>>> allNpcs() {
        var types = new java.util.ArrayList<>(List.of(ELDARA, CAELEN, GUARD, JORGE));
        types.addAll(COVENANT_NPCS.values()); return types;
    }
    private static RegistryObject<EntityType<NpcEntity>> npc(String name) {
        return ENTITIES.register(name, () -> EntityType.Builder.of(NpcEntity::new, MobCategory.MISC).sized(0.6F, 1.95F)
            .clientTrackingRange(10).fireImmune().build(ID + ":" + name));
    }
    private static RegistryObject<Item> egg(String name, RegistryObject<EntityType<NpcEntity>> type, int base, int spots) {
        return ITEMS.register(name + "_spawn_egg", () -> new net.minecraftforge.common.ForgeSpawnEggItem(type, base, spots, new Item.Properties()));
    }
}
