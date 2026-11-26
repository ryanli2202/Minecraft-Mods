package dev.travellerscompanions;

import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

public final class SpellMenu extends AbstractContainerMenu {
    public final CompanionEntity companion;
    private final ContainerData mana;
    public SpellMenu(int id,Inventory inventory,CompanionEntity companion){
        super(TravellersCompanions.SPELLS.get(),id);this.companion=companion;
        Container scrolls=companion==null?new SimpleContainer(CompanionSpells.SLOTS):companion.spellInventory;
        for(int i=0;i<CompanionSpells.SLOTS;i++)addSlot(new Slot(scrolls,i,35+i*18,35){
            @Override public int getMaxStackSize(){return 1;}
            @Override public boolean mayPlace(ItemStack stack){return CompanionSpells.accepts(stack);}
        });
        for(int row=0;row<3;row++)for(int col=0;col<9;col++)addSlot(new Slot(inventory,col+row*9+9,8+col*18,76+row*18));
        for(int col=0;col<9;col++)addSlot(new Slot(inventory,col,8+col*18,134));
        mana=inventory.player.level().isClientSide?new SimpleContainerData(2):new ContainerData(){
            public int get(int index){return companion==null?0:index==0?(int)companion.getMagicData().getMana():companion.spells().maxMana();}
            public void set(int index,int value){} public int getCount(){return 2;}
        };addDataSlots(mana);
    }
    public int mana(){return mana.get(0);}public int maxMana(){return mana.get(1);}
    @Override public boolean stillValid(Player player){return companion!=null&&companion.kind()==CharacterKind.LEONARDO&&companion.isAlive()&&!companion.isRemoved()&&companion.ownedBy(player)&&player.distanceToSqr(companion)<64;}
    @Override public ItemStack quickMoveStack(Player player,int index){
        if(index<0||index>=slots.size())return ItemStack.EMPTY;
        var slot=slots.get(index);if(!slot.hasItem())return ItemStack.EMPTY;var stack=slot.getItem();var copy=stack.copy();
        if(index<CompanionSpells.SLOTS){if(!moveItemStackTo(stack,CompanionSpells.SLOTS,slots.size(),true))return ItemStack.EMPTY;}
        else if(!CompanionSpells.accepts(stack)||!moveItemStackTo(stack,0,CompanionSpells.SLOTS,false))return ItemStack.EMPTY;
        if(stack.isEmpty())slot.set(ItemStack.EMPTY);else slot.setChanged();slot.onTake(player,stack);return copy;
    }
    @Override public void removed(Player player){super.removed(player);if(companion!=null)companion.checkpoint();}
}
