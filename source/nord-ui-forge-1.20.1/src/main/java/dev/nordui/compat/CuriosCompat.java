package dev.nordui.compat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import dev.nordui.client.Theme;
import java.util.*;

/** The bridge is resolved only when Curios is present; no optional types leak into common code. */
public final class CuriosCompat {
    public record Entry(String slot,ItemStack item) {}
    public static boolean loaded(){return ModList.get().isLoaded("curios");}
    public static List<Entry> entries(Player player){return loaded()?Bridge.entries(player):List.of();}
    public static void open(){if(loaded()&&Minecraft.getInstance().player.containerMenu.getCarried().isEmpty())Bridge.open();}
    public static boolean isScreen(Object screen){return loaded()&&Bridge.isScreen(screen);}
    public static void frame(AbstractContainerScreen<?> s,GuiGraphics g){
        // Keep Curios' page controls, cosmetic switches and empty-slot icons visible.
        int x=s.getGuiLeft(),y=s.getGuiTop();
        int minX=0,sideBottom=0;for(var slot:s.getMenu().slots)if(slot.isActive()&&slot.x<0){minX=Math.min(minX,slot.x-5);sideBottom=Math.max(sideBottom,slot.y+21);}
        g.fill(x,y,x+s.getXSize(),y+s.getYSize(),0xAF071119);
        if(minX<0)g.fill(x+minX,y,x,y+sideBottom,0xAF071119);
        Theme.frame(g,x-2,y-2,s.getXSize()+4,s.getYSize()+4,0xFFACAFA6);
        for(var slot:s.getMenu().slots)if(slot.isActive())Theme.slot(g,x+slot.x,y+slot.y);
        // The body was drawn in Curios' background before this event. Redraw it undimmed.
        var p=Minecraft.getInstance().player;
        if(p!=null)net.minecraft.client.gui.screens.inventory.InventoryScreen.renderEntityInInventoryFollowsMouse(g,x+51,y+75,30,0,0,p);
    }
    private static final class Bridge {
        static List<Entry> entries(Player player){
            List<Entry> result=new ArrayList<>();
            top.theillusivec4.curios.api.CuriosApi.getCuriosInventory(player).ifPresent(handler->handler.getCurios().forEach((id,slots)->{
                if(!slots.isVisible())return;
                for(int i=0;i<slots.getSlots();i++)result.add(new Entry(id+(slots.getSlots()>1?" "+(i+1):""),slots.getStacks().getStackInSlot(i).copy()));
            }));return result;
        }
        static void open(){top.theillusivec4.curios.common.network.NetworkHandler.INSTANCE.sendToServer(new top.theillusivec4.curios.common.network.client.CPacketOpenCurios(ItemStack.EMPTY));}
        static boolean isScreen(Object screen){return screen instanceof top.theillusivec4.curios.api.client.ICuriosScreen;}
    }
}
