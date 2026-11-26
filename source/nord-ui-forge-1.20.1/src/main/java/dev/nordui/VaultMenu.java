package dev.nordui;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.*;
import java.util.UUID;

public final class VaultMenu extends AbstractContainerMenu {
    public static final int PAGE_SIZE = 54, PAGES = (VaultData.SIZE + PAGE_SIZE - 1) / PAGE_SIZE;
    public final int page, visibleSlots;
    public final ItemStackHandler storage;
    private final VaultData data;
    private final UUID owner;
    private final DataSlot occupied = DataSlot.standalone();
    public static VaultMenu client(int id, Inventory inv, FriendlyByteBuf buf) {
        return new VaultMenu(id, inv, new ItemStackHandler(VaultData.SIZE), null, Math.max(0,Math.min(PAGES-1,buf.readVarInt())));
    }
    public VaultMenu(int id, Inventory inv, ItemStackHandler storage, VaultData data, int page) {
        super(NordUI.VAULT.get(),id);
        if (page < 0 || page >= PAGES || storage.getSlots() != VaultData.SIZE) throw new IllegalArgumentException("Invalid vault layout");
        this.page=page; this.storage=storage; this.data=data; owner=inv.player.getUUID();
        visibleSlots=Math.min(PAGE_SIZE,VaultData.SIZE-page*PAGE_SIZE);
        for(int i=0;i<visibleSlots;i++) addSlot(new SlotItemHandler(storage,page*PAGE_SIZE+i,8+(i%9)*18,38+(i/9)*18) {
            @Override public void setChanged() { super.setChanged(); if(data!=null)data.setDirty(); }
        });
        for(int y=0;y<3;y++)for(int x=0;x<9;x++)addSlot(new Slot(inv,9+y*9+x,8+x*18,166+y*18));
        for(int x=0;x<9;x++)addSlot(new Slot(inv,x,8+x*18,224));
        addDataSlot(occupied);
    }
    public int used() { return occupied.get(); }
    @Override public void broadcastChanges() {
        if(data!=null)occupied.set(VaultData.occupied(storage));
        super.broadcastChanges();
    }
    @Override public boolean stillValid(Player player) { return player.isAlive()&&!player.isSpectator()&&owner.equals(player.getUUID()); }
    @Override public boolean clickMenuButton(Player player,int action) {
        if(!(player instanceof ServerPlayer sp)||!stillValid(player)||player.containerMenu!=this||!getCarried().isEmpty())return false;
        if(action>=0&&action<PAGES) { VaultService.open(sp,action); return true; }
        if(action==100) {
            // Pack the 27 backpack slots; deliberately retain hotbar and equipped items.
            for(int slot=9;slot<36;slot++) {
                ItemStack remainder=ItemHandlerHelper.insertItemStacked(storage,player.getInventory().getItem(slot).copy(),false);
                player.getInventory().setItem(slot,remainder);
            }
            player.getInventory().setChanged(); if(data!=null)data.setDirty(); broadcastChanges(); return true;
        }
        return false;
    }
    @Override public ItemStack quickMoveStack(Player player,int index) {
        if(index<0||index>=slots.size()||!stillValid(player))return ItemStack.EMPTY;
        Slot slot=slots.get(index); if(!slot.hasItem())return ItemStack.EMPTY;
        ItemStack stack=slot.getItem(), original=stack.copy();
        if(index<visibleSlots) { if(!moveItemStackTo(stack,visibleSlots,slots.size(),true))return ItemStack.EMPTY; }
        else if(!moveItemStackTo(stack,0,visibleSlots,false))return ItemStack.EMPTY;
        if(stack.isEmpty())slot.set(ItemStack.EMPTY);else slot.setChanged();
        slot.onTake(player,stack);if(data!=null)data.setDirty();return original;
    }
    @Override public void removed(Player p) { super.removed(p);if(data!=null)data.setDirty(); }
}
