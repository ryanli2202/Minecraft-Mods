package dev.nordui.client;

import dev.nordui.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid=NordUI.ID,value=Dist.CLIENT)
public final class ClientEvents {
    public static final KeyMapping VAULT_KEY=new KeyMapping("key.nordui.vault",GLFW.GLFW_KEY_V,"key.categories.nordui");
    public static final KeyMapping TOGGLE_KEY=new KeyMapping("key.nordui.appearance",GLFW.GLFW_KEY_F8,"key.categories.nordui");
    public static final KeyMapping COMPAT_KEY=new KeyMapping("key.nordui.compatibility",GLFW.GLFW_KEY_F7,"key.categories.nordui");
    private static boolean vanillaOnce;
    public static void openVanillaInventory(){vanillaOnce=true;Minecraft.getInstance().setScreen(new InventoryScreen(Minecraft.getInstance().player));}
    @SubscribeEvent public static void screen(ScreenEvent.Opening e) {
        if(vanillaOnce&&e.getNewScreen() instanceof InventoryScreen){vanillaOnce=false;return;}
        var mc=Minecraft.getInstance();
        if(ClientConfig.active()&&ClientConfig.TITLE.get()&&e.getNewScreen()!=null&&e.getNewScreen().getClass()==TitleScreen.class)e.setNewScreen(new NordTitleScreen());
        if(ClientConfig.active()&&ClientConfig.INVENTORY.get()&&mc.player!=null&&!mc.player.isCreative()&&e.getNewScreen()!=null&&e.getNewScreen().getClass()==InventoryScreen.class)
            e.setNewScreen(new NordInventoryScreen(mc.player));
    }
    @SubscribeEvent public static void background(ScreenEvent.BackgroundRendered e) {
        if(!ClientConfig.active()||!dev.nordui.compat.UiCompat.themeScreen(e.getScreen())||e.getScreen() instanceof AbstractContainerScreen<?>||e.getScreen() instanceof TitleScreen)return;
        var s=e.getScreen();var g=e.getGuiGraphics();
        g.blit(Theme.SLATE,0,0,0,0,s.width,s.height,s.width,s.height);g.fill(0,0,s.width,s.height,0x5510171D);
    }
    private static boolean storageScreen(AbstractContainerScreen<?> s){return ClientConfig.active()&&ClientConfig.CONTAINERS.get()&&(s.getClass()==ContainerScreen.class||s.getClass()==ShulkerBoxScreen.class||s.getClass()==HopperScreen.class||s.getClass()==DispenserScreen.class);}
    @SubscribeEvent public static void containerBackground(ContainerScreenEvent.Render.Background e){
        var s=e.getContainerScreen();var g=e.getGuiGraphics();
        if(ClientConfig.active()&&dev.nordui.compat.CuriosCompat.isScreen(s)){dev.nordui.compat.CuriosCompat.frame(s,g);return;}
        if(!storageScreen(s))return;
        Theme.panel(g,s.getGuiLeft(),s.getGuiTop(),s.getXSize(),s.getYSize());
        for(var slot:s.getMenu().slots)if(slot.isActive())Theme.slot(g,s.getGuiLeft()+slot.x,s.getGuiTop()+slot.y);
    }
    @SubscribeEvent public static void containerForeground(ContainerScreenEvent.Render.Foreground e){
        var s=e.getContainerScreen();var g=e.getGuiGraphics();
        if(ClientConfig.active()&&dev.nordui.compat.CuriosCompat.isScreen(s)){
            g.drawString(Minecraft.getInstance().font,Component.translatable("container.crafting"),97,6,Theme.WHITE,false);
            return;
        }
        if(!storageScreen(s))return;
        g.drawString(Minecraft.getInstance().font,s.getTitle(),8,6,Theme.WHITE,false);
        g.drawString(Minecraft.getInstance().font,Component.translatable("container.inventory"),8,s.getYSize()-94,Theme.MUTED,false);
    }
    @SubscribeEvent public static void init(ScreenEvent.Init.Post e) {
        if(ClientConfig.active()&&dev.nordui.compat.CuriosCompat.isScreen(e.getScreen())){
            var s=e.getScreen();e.addListener(Button.builder(Component.literal("< DEEP NIGHT EQUIPMENT"),b->{var mc=Minecraft.getInstance();mc.player.closeContainer();var screen=new NordInventoryScreen(mc.player);screen.equipment=true;mc.setScreen(screen);}).bounds(s.width/2-90,6,180,18).build());
        }
        if(e.getScreen() instanceof InventoryScreen||e.getScreen() instanceof CreativeModeInventoryScreen) {
            var screen=e.getScreen();
            e.addListener(Button.builder(Component.literal("VAULT / 999"),b->VaultNetwork.request()).bounds(screen.width/2-150,6,110,18).build());
            if(dev.nordui.compat.IronsCompat.loaded()&&ClientConfig.SPELLS.get())e.addListener(Button.builder(Component.literal("SPELLS"),b->VaultNetwork.spells()).bounds(screen.width/2-36,6,80,18).build());
            e.addListener(Button.builder(Component.literal("UI OPTIONS"),b->Minecraft.getInstance().setScreen(new CompatibilityScreen(screen))).bounds(screen.width/2+48,6,102,18).build());
        }
    }
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent e) {
        if(e.phase!=TickEvent.Phase.END)return;var mc=Minecraft.getInstance();
        while(TOGGLE_KEY.consumeClick()) {
            ClientConfig.ENABLED.set(!ClientConfig.active());ClientConfig.SPEC.save();
            if(mc.player!=null)mc.player.displayClientMessage(Component.literal("NORD interface "+(ClientConfig.active()?"enabled":"disabled")),true);
        }
        while(VAULT_KEY.consumeClick())if(mc.player!=null&&mc.screen==null&&!mc.player.isSpectator())VaultNetwork.request();
        while(COMPAT_KEY.consumeClick())if(mc.screen==null)mc.setScreen(new CompatibilityScreen(null));
    }
    @SubscribeEvent(priority=net.minecraftforge.eventbus.api.EventPriority.LOWEST) public static void overlay(RenderGuiOverlayEvent.Pre e) {
        var mc=Minecraft.getInstance();if(!ClientConfig.ownsHud()||mc.player==null||mc.player.isSpectator())return;
        var id=e.getOverlay().id();
        if(dev.nordui.compat.IronsCompat.loaded()&&id.toString().equals("irons_spellbooks:spell_bar")&&dev.nordui.compat.irons.IronsClient.dockSpells(e)){e.setCanceled(true);return;}
        if(dev.nordui.compat.IronsCompat.loaded()&&ClientConfig.IRONS_MANA.get()&&id.toString().equals("irons_spellbooks:mana_overlay")){e.setCanceled(true);return;}
        if(id.equals(VanillaGuiOverlay.HOTBAR.id())&&!ClientConfig.HOTBAR.get())return;
        if(id.equals(VanillaGuiOverlay.PLAYER_HEALTH.id())||id.equals(VanillaGuiOverlay.FOOD_LEVEL.id())||id.equals(VanillaGuiOverlay.EXPERIENCE_BAR.id())||id.equals(VanillaGuiOverlay.ARMOR_LEVEL.id())||id.equals(VanillaGuiOverlay.AIR_LEVEL.id())||id.equals(VanillaGuiOverlay.HOTBAR.id())||id.equals(VanillaGuiOverlay.MOUNT_HEALTH.id())||id.equals(VanillaGuiOverlay.JUMP_BAR.id()))e.setCanceled(true);
    }
    @Mod.EventBusSubscriber(modid=NordUI.ID,value=Dist.CLIENT,bus=Mod.EventBusSubscriber.Bus.MOD)
    public static final class ModEvents {
        static {
            ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT,ClientConfig.SPEC);
            ModLoadingContext.get().registerExtensionPoint(net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory.class,()->new net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory((mc,parent)->new CompatibilityScreen(parent)));
        }
        @SubscribeEvent public static void setup(FMLClientSetupEvent e){e.enqueueWork(()->{MenuScreens.register(NordUI.VAULT.get(),VaultScreen::new);if(dev.nordui.compat.IronsCompat.loaded())dev.nordui.compat.irons.IronsClient.register();if(dev.nordui.compat.UiCompat.loaded("trashslot"))dev.nordui.compat.TrashCompat.register();});}
        @SubscribeEvent public static void keys(RegisterKeyMappingsEvent e){e.register(VAULT_KEY);e.register(TOGGLE_KEY);e.register(COMPAT_KEY);}
        @SubscribeEvent public static void overlays(RegisterGuiOverlaysEvent e){e.registerAboveAll("northern_hud",NordHud::render);}
    }
}
