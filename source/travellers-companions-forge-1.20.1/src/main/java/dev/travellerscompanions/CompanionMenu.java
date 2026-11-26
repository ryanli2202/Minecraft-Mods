package dev.travellerscompanions;

import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;

public final class CompanionMenu extends AbstractContainerMenu {
    public static final EquipmentSlot[] GEAR={EquipmentSlot.HEAD,EquipmentSlot.CHEST,EquipmentSlot.LEGS,EquipmentSlot.FEET,EquipmentSlot.MAINHAND,EquipmentSlot.OFFHAND};
    public final CompanionEntity companion;
    public CompanionMenu(int id,Inventory inv,CompanionEntity companion){
        super(TravellersCompanions.EQUIPMENT.get(),id);this.companion=companion;
        Container gear=new SimpleContainer(6){
            @Override public ItemStack getItem(int i){return companion==null?ItemStack.EMPTY:companion.getItemBySlot(GEAR[i]);}
            @Override public void setItem(int i,ItemStack s){if(companion!=null){companion.setItemSlot(GEAR[i],s);setChanged();}}
            @Override public ItemStack removeItem(int i,int count){var s=getItem(i).split(count);setChanged();return s;}
            @Override public ItemStack removeItemNoUpdate(int i){var s=getItem(i);setItem(i,ItemStack.EMPTY);return s;}
            @Override public void setChanged(){if(companion!=null)companion.checkpoint();}
        };
        for(int i=0;i<6;i++){final int index=i;addSlot(new Slot(gear,i,8+i*27,27){
            @Override public int getMaxStackSize(){return index<4?1:64;}
            @Override public boolean mayPlace(ItemStack s){return index>=4||s.canEquip(GEAR[index],companion);}
        });}
        Container cargo=companion==null?new SimpleContainer(27):companion.backpack;
        for(int y=0;y<3;y++)for(int x=0;x<9;x++)addSlot(new Slot(cargo,x+y*9,8+x*18,65+y*18));
        for(int y=0;y<3;y++)for(int x=0;x<9;x++)addSlot(new Slot(inv,x+y*9+9,8+x*18,137+y*18));
        for(int x=0;x<9;x++)addSlot(new Slot(inv,x,8+x*18,195));
    }
    @Override public boolean stillValid(Player p){return companion!=null&&companion.isAlive()&&!companion.isRemoved()&&companion.ownedBy(p)&&p.distanceToSqr(companion)<64;}
    @Override public ItemStack quickMoveStack(Player p,int index){
        if(index<0||index>=slots.size())return ItemStack.EMPTY;var slot=slots.get(index);if(!slot.hasItem())return ItemStack.EMPTY;var stack=slot.getItem();var original=stack.copy();
        if(index<33){if(!moveItemStackTo(stack,33,69,true))return ItemStack.EMPTY;}
        else {int equipment=-1;for(int i=0;i<4;i++)if(stack.canEquip(GEAR[i],companion)&&!slots.get(i).hasItem()){equipment=i;break;}if(equipment>=0){if(!moveItemStackTo(stack,equipment,equipment+1,false))return ItemStack.EMPTY;}else if(!moveItemStackTo(stack,6,33,false))return ItemStack.EMPTY;}
        if(stack.isEmpty())slot.set(ItemStack.EMPTY);else slot.setChanged();slot.onTake(p,stack);return original;
    }
    @Override public void removed(Player p){super.removed(p);if(companion!=null)companion.checkpoint();}
}
