package dev.deepnight.source.dawn.init;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
public final class SimpleMobsModItems {
public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, "simple_mobs");
public static final RegistryObject<Item> ACCURSED_SWORD=REGISTRY.register("accursed_sword",()->new Item(new Item.Properties()));
public static final RegistryObject<net.minecraft.world.item.Item> DRAGON_LORD_ESSENCE=REGISTRY.register("dragon_lord_essence",()->new net.minecraft.world.item.Item(new net.minecraft.world.item.Item.Properties()));
public static final RegistryObject<net.minecraft.world.item.Item> DWELLER_ARMOR_FRAGMENT=REGISTRY.register("dweller_armor_fragment",()->new net.minecraft.world.item.Item(new net.minecraft.world.item.Item.Properties()));
public static final RegistryObject<net.minecraft.world.item.Item> ELEMENTAL_GAUNTLET=REGISTRY.register("elemental_gauntlet",()->new net.minecraft.world.item.Item(new net.minecraft.world.item.Item.Properties()));
public static final RegistryObject<net.minecraft.world.item.Item> ENT_STAFF=REGISTRY.register("ent_staff",()->new net.minecraft.world.item.Item(new net.minecraft.world.item.Item.Properties()));
public static final RegistryObject<net.minecraft.world.item.Item> FIREDRAGON_HEART=REGISTRY.register("firedragon_heart",()->new net.minecraft.world.item.Item(new net.minecraft.world.item.Item.Properties()));
public static final RegistryObject<net.minecraft.world.item.Item> ICEDRAGON_HEART=REGISTRY.register("icedragon_heart",()->new net.minecraft.world.item.Item(new net.minecraft.world.item.Item.Properties()));
public static final RegistryObject<net.minecraft.world.item.Item> LIGHTNINGDRAGON_HEART=REGISTRY.register("lightningdragon_heart",()->new net.minecraft.world.item.Item(new net.minecraft.world.item.Item.Properties()));

}
