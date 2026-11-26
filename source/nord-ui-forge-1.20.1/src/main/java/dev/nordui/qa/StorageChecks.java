package dev.nordui.qa;

import com.mojang.authlib.GameProfile;
import dev.nordui.*;
import net.minecraft.nbt.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.util.FakePlayerFactory;
import java.util.UUID;

public final class StorageChecks {
    public static final StringBuilder REPORT=new StringBuilder();
    public static void check(boolean result,String description){if(!result)throw new AssertionError(description);REPORT.append("PASS ").append(description).append('\n');NordUI.LOG.info("NORD QA PASS: {}",description);}
    public static void run(ServerLevel level){
        var a=FakePlayerFactory.get(level,new GameProfile(UUID.randomUUID(),"NordQA"));
        var b=FakePlayerFactory.get(level,new GameProfile(UUID.randomUUID(),"NordOther"));
        VaultData data=new VaultData();var inv=data.inventory(a.getUUID());
        check(inv.getSlots()==999,"Exactly 999 persistent slots per player");
        ItemStack sword=new ItemStack(Items.DIAMOND_SWORD);sword.setDamageValue(123);sword.enchant(Enchantments.SHARPNESS,5);sword.setHoverName(net.minecraft.network.chat.Component.literal("Frost oath"));sword.getOrCreateTag().putString("othermod:custom_data","retained");
        inv.setStackInSlot(998,sword.copy());
        check(data.isDirty(),"Storage changes mark SavedData dirty");
        var loaded=VaultData.load(data.save(new CompoundTag()));
        check(ItemStack.matches(sword,loaded.inventory(a.getUUID()).getStackInSlot(998)),"Last slot round-trips exact count, name, enchantments, durability and modded NBT");
        check(loaded.inventory(b.getUUID()).getStackInSlot(998).isEmpty(),"Different players cannot share the same vault");
        int total=0;
        for(int page=0;page<VaultMenu.PAGES;page++){var menu=new VaultMenu(1,a.getInventory(),inv,data,page);total+=menu.visibleSlots;check(menu.slots.size()==menu.visibleSlots+36,"Page "+(page+1)+" contains only legal vault and player slots");}
        check(total==999,"All pages cover exactly 999 slots without overlap or overflow");
        var last=new VaultMenu(1,a.getInventory(),inv,data,18);a.containerMenu=last;
        check(last.visibleSlots==27,"Final page exposes exactly 27 slots");
        check(!last.stillValid(b),"Menu rejects another player");
        check(!last.clickMenuButton(a,-1)&&!last.clickMenuButton(a,19)&&!last.clickMenuButton(a,101),"Invalid page and action IDs rejected");
        a.getInventory().clearContent();
        check(!last.quickMoveStack(a,26).isEmpty(),"Shift-click retrieves final slot");
        check(inv.getStackInSlot(998).isEmpty(),"Retrieved item removed from vault");
        check(a.getInventory().countItem(Items.DIAMOND_SWORD)==1,"Shift-click does not duplicate item");
        check(last.quickMoveStack(a,26).isEmpty(),"Repeated shift-click cannot retrieve item twice");
        a.getInventory().clearContent();a.getInventory().setItem(9,sword.copy());
        check(!last.quickMoveStack(a,27).isEmpty(),"Shift-click deposits into final page");
        check(ItemStack.matches(inv.getStackInSlot(972),sword),"Deposit retains full item data");
        last.setCarried(new ItemStack(Items.EMERALD));
        check(!last.clickMenuButton(a,100)&&!last.clickMenuButton(a,0),"Cursor item blocks deposits and page switching");last.setCarried(ItemStack.EMPTY);
        for(int i=0;i<999;i++)inv.setStackInSlot(i,new ItemStack(Items.COBBLESTONE,64));
        a.getInventory().setItem(9,new ItemStack(Items.DIAMOND,16));last.clickMenuButton(a,100);
        check(a.getInventory().getItem(9).getCount()==16,"Full vault leaves backpack items untouched");
        inv.setStackInSlot(998,ItemStack.EMPTY);last.clickMenuButton(a,100);
        check(a.getInventory().getItem(9).isEmpty()&&inv.getStackInSlot(998).getCount()==16,"Deposit scans all 999 slots including final slot");
        a.getInventory().setItem(0,new ItemStack(Items.EMERALD,7));last.clickMenuButton(a,100);
        check(a.getInventory().getItem(0).getCount()==7,"Store-backpack preserves hotbar");
        check(VaultData.occupied(inv)==999,"Occupancy count reports all 999 used slots");
        check(last.quickMoveStack(a,-1).isEmpty()&&last.quickMoveStack(a,9999).isEmpty(),"Out-of-range slot indices rejected");
        var corrupt=data.save(new CompoundTag());var player=(CompoundTag)corrupt.getList("Players",10).get(0);var entries=player.getList("Items",10);var invalid=new ItemStack(Items.NETHER_STAR).save(new CompoundTag());invalid.putInt("Slot",999);entries.add(invalid);
        check(VaultData.load(corrupt).inventory(a.getUUID()).getSlots()==999,"Out-of-range serialized slots cannot expand storage");
        a.containerMenu=a.inventoryMenu;
    }
}
