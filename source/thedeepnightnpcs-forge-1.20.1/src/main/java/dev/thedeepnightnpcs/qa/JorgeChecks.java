package dev.thedeepnightnpcs.qa;

import dev.thedeepnightnpcs.DeepNightNpcs;
import dev.thedeepnightnpcs.trade.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraftforge.registries.ForgeRegistries;
import java.nio.file.*;
import java.util.*;

public final class JorgeChecks {
    public static void catalogue() throws Exception {
        var ingots=JorgeService.ingots();var enchants=JorgeService.enchantments();
        ServerChecks.check(ingots.containsAll(List.of(Items.COPPER_INGOT,Items.IRON_INGOT,Items.GOLD_INGOT,Items.NETHERITE_INGOT)),"All four vanilla ingots discovered");
        ServerChecks.check(!ingots.contains(Items.IRON_NUGGET)&&!ingots.contains(Items.IRON_BLOCK),"Nuggets and storage blocks excluded");
        ServerChecks.check(!ingots.contains(Items.BRICK)&&!ingots.contains(Items.NETHER_BRICK),"Vanilla bricks excluded despite Forge ingot tags");
        ServerChecks.check(ingots.stream().anyMatch(i->ForgeRegistries.ITEMS.getKey(i).toString().equals("npcqa:alloy_ingot")),"Installed mod ingot discovered");
        ServerChecks.check(enchants.stream().anyMatch(e->ForgeRegistries.ENCHANTMENTS.getKey(e).toString().equals("npcqa:inscription")),"Installed mod enchantment discovered");
        ServerChecks.check(enchants.contains(Enchantments.MENDING),"Tradeable treasure enchantments available");
        DeepNightNpcs.EXCLUDED_INGOTS.set(List.of("minecraft:netherite_ingot"));DeepNightNpcs.EXCLUDED_ENCHANTMENTS.set(List.of("minecraft:mending"));
        ServerChecks.check(!JorgeService.ingots().contains(Items.NETHERITE_INGOT),"Ingot exclusion is applied");
        ServerChecks.check(!JorgeService.enchantments().contains(Enchantments.MENDING),"Enchantment exclusion is applied");
        DeepNightNpcs.EXTRA_INGOTS.set(List.of("minecraft:brick"));ServerChecks.check(JorgeService.ingots().contains(Items.BRICK),"Unconventional ingot override is applied");
        DeepNightNpcs.EXCLUDED_INGOTS.set(List.of());DeepNightNpcs.EXCLUDED_ENCHANTMENTS.set(List.of());DeepNightNpcs.EXTRA_INGOTS.set(List.of());
        Files.createDirectories(Path.of("../verification/jorge"));
        Files.writeString(Path.of("../verification/jorge/ingots.txt"),String.join("\n",ingots.stream().map(i->ForgeRegistries.ITEMS.getKey(i).toString()).toList()));
        Files.writeString(Path.of("../verification/jorge/enchantments.txt"),String.join("\n",enchants.stream().map(e->ForgeRegistries.ENCHANTMENTS.getKey(e).toString()).toList()));
    }
}
