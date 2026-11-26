package dev.emeraldcovenant;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.*;
import java.util.*;

/** Plan on copies, then commit: insufficient money/space cannot partially debit an inventory. */
public final class Transactions {
    public record Result(boolean success,String message) {}
    public static int emeralds(Inventory inv){return inv.items.stream().filter(s->s.is(Items.EMERALD)).mapToInt(ItemStack::getCount).sum()+inv.offhand.stream().filter(s->s.is(Items.EMERALD)).mapToInt(ItemStack::getCount).sum();}
    public static Result purchase(Inventory inv,ItemStack product,int quantity,int price) {
        if(quantity<1||quantity>64||price<1||price>999||product.isEmpty())return new Result(false,"Invalid purchase.");
        long cost=(long)price*quantity;
        if(cost>emeralds(inv))return new Result(false,"You need "+cost+" emeralds for this purchase.");
        var main=new ArrayList<ItemStack>();inv.items.forEach(s->main.add(s.copy()));
        var off=new ArrayList<ItemStack>();inv.offhand.forEach(s->off.add(s.copy()));
        int debit=(int)cost;
        for(List<ItemStack> group:List.of(main,off))for(ItemStack s:group)if(s.is(Items.EMERALD)&&debit>0){int n=Math.min(debit,s.getCount());s.shrink(n);debit-=n;}
        int remaining=quantity;
        for(ItemStack s:main)if(!s.isEmpty()&&ItemStack.isSameItemSameTags(s,product)){
            int n=Math.min(remaining,Math.max(0,Math.min(s.getMaxStackSize(),inv.getMaxStackSize())-s.getCount()));s.grow(n);remaining-=n;
        }
        for(int i=0;i<main.size()&&remaining>0;i++)if(main.get(i).isEmpty()){
            int n=Math.min(remaining,Math.min(product.getMaxStackSize(),inv.getMaxStackSize()));main.set(i,product.copyWithCount(n));remaining-=n;
        }
        if(remaining>0)return new Result(false,"Make room in your inventory first. No emeralds spent.");
        for(int i=0;i<main.size();i++)inv.items.set(i,main.get(i));
        for(int i=0;i<off.size();i++)inv.offhand.set(i,off.get(i));
        inv.setChanged();
        return new Result(true,"Purchased "+quantity+" item"+(quantity==1?"":"s")+" for "+cost+" emeralds.");
    }
}
