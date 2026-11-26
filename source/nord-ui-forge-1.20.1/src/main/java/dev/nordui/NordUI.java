package dev.nordui;

import com.mojang.logging.LogUtils;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.*;
import net.minecraft.commands.Commands;
import org.slf4j.Logger;

@Mod(NordUI.ID)
public final class NordUI {
    public static final String ID = "nordui";
    public static final Logger LOG = LogUtils.getLogger();
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, ID);
    public static final RegistryObject<MenuType<VaultMenu>> VAULT = MENUS.register("vault", () -> IForgeMenuType.create(VaultMenu::client));
    public NordUI() {
        net.minecraftforge.fml.ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.SERVER,ServerConfig.SPEC);
        dev.nordui.compat.IronsCompat.register();
        MENUS.register(FMLJavaModLoadingContext.get().getModEventBus());
        VaultNetwork.register();
        MinecraftForge.EVENT_BUS.addListener(this::commands);
    }
    private void commands(RegisterCommandsEvent e) {
        e.getDispatcher().register(Commands.literal("nordvault").executes(c -> {
            VaultService.open(c.getSource().getPlayerOrException(), 0); return 1;
        }));
    }
}
