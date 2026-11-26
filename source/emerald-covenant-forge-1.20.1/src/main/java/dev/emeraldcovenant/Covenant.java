package dev.emeraldcovenant;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.*;
import org.slf4j.Logger;
import java.util.*;

@Mod(Covenant.ID)
public final class Covenant {
    public static final String ID = "emeraldcovenant";
    public static final Logger LOG = LogUtils.getLogger();
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ID);
    public static final List<RegistryObject<Item>> BEARINGS = new ArrayList<>();
    public static final String[] BEARING_IDS = {"forgemaster_bell_bearing", "warden_bell_bearing", "archivist_bell_bearing", "sovereign_bell_bearing"};
    public static final String[] BEARING_NAMES = {"Forgemaster", "Warden", "Archivist", "Sovereign"};
    static {
        for (int n=0;n<4;n++) {
            final int bit=1<<n;
            BEARINGS.add(ITEMS.register(BEARING_IDS[n], () -> new BearingItem(bit)));
        }
    }
    public Covenant() {
        var bus=FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
        bus.addListener(this::creative);
        bus.addListener(ShopConfig::loaded);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ShopConfig.SPEC);
        ShopNetwork.register();
        MinecraftForge.EVENT_BUS.register(ShopServer.class);
    }
    private void creative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey()==CreativeModeTabs.TOOLS_AND_UTILITIES) BEARINGS.forEach(event::accept);
    }
}
