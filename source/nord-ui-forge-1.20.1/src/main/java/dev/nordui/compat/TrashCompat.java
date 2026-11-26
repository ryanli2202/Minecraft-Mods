package dev.nordui.compat;

import dev.nordui.client.*;
import net.blay09.mods.trashslot.api.TrashSlotAPI;
import net.blay09.mods.trashslot.client.gui.layout.SimpleGuiContainerLayout;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

/** Register moving container boundaries; TrashSlot retains deletion, undo and JEI exclusion behavior. */
public final class TrashCompat {
    public static void register(){
        var layout=new SimpleGuiContainerLayout(){
            @Override public int getDefaultSlotX(AbstractContainerScreen<?> screen){return screen.getXSize()/2+2;}
            @Override public int getDefaultSlotY(AbstractContainerScreen<?> screen){return screen.getYSize()/2-28;}
        };
        layout.enableDefaultCollision().enableDefaultSnaps().setEnabledByDefault();
        TrashSlotAPI.registerLayout(NordInventoryScreen.class,layout);
        TrashSlotAPI.registerLayout(VaultScreen.class,layout);
        if(IronsCompat.loaded())TrashSlotAPI.registerLayout(dev.nordui.compat.irons.SpellScreen.class,layout);
    }
}
