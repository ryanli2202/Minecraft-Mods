package dev.nordui.qa;

import dev.nordui.ServerConfig;
import dev.nordui.compat.irons.SpellMenu;
import io.redspace.ironsspellbooks.api.events.InscribeSpellEvent;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import static dev.nordui.qa.StorageChecks.check;

public final class IronsChecks {
    public static ItemStack scroll(AbstractSpell spell){var s=new ItemStack(ItemRegistry.SCROLL.get());ISpellContainer.createScrollContainer(spell,1,s);return s;}
    public static ItemStack book(){
        var stack=new ItemStack(ItemRegistry.GOLD_SPELL_BOOK.get());
        var spells=ISpellContainer.create(8,true,true).mutableCopy();
        spells.addSpellAtIndex(SpellRegistry.FIREBOLT_SPELL.get(),1,0,false);
        spells.addSpellAtIndex(SpellRegistry.HEAL_SPELL.get(),1,1,true);
        ISpellContainer.set(stack,spells.toImmutable());stack.getOrCreateTag().putString("test:preserved","runes");return stack;
    }
    public static void setup(ServerPlayer p){
        var book=book();var spells=ISpellContainer.get(book).mutableCopy();
        spells.addSpellAtIndex(SpellRegistry.FIREBALL_SPELL.get(),1,2,false);
        spells.addSpellAtIndex(SpellRegistry.TELEPORT_SPELL.get(),1,3,false);
        spells.addSpellAtIndex(SpellRegistry.MAGIC_MISSILE_SPELL.get(),1,4,false);
        spells.addSpellAtIndex(SpellRegistry.GUIDING_BOLT_SPELL.get(),1,5,false);
        ISpellContainer.set(book,spells.toImmutable());
        Utils.setPlayerSpellbookStack(p,book);p.getInventory().setItem(9,scroll(SpellRegistry.ICE_SPIKES_SPELL.get()));
        p.getInventory().setItem(10,scroll(SpellRegistry.LIGHTNING_BOLT_SPELL.get()));
        p.getInventory().setItem(0,new ItemStack(ItemRegistry.GRAYBEARD_STAFF.get()));
    }
    public static void run(ServerPlayer p){
        var previousBook=Utils.getPlayerSpellbookStack(p);previousBook=previousBook==null?ItemStack.EMPTY:previousBook.copy();
        var book=book();Utils.setPlayerSpellbookStack(p,book);
        var menu=new SpellMenu(70,p.getInventory(),null);p.containerMenu=menu;
        check(menu.getSpellBookSlot().getItem()==book,"Spell tab loads the actual Curios spellbook");
        check(!menu.clickMenuButton(p,999999)&&!menu.clickMenuButton(p,-2),"Forged spell selection/action IDs rejected");
        menu.clickMenuButton(p,1);check(menu.getResultSlot().getItem().isEmpty(),"Bound spells never produce extraction scrolls");
        menu.getScrollSlot().set(scroll(SpellRegistry.ICE_SPIKES_SPELL.get()));
        check(!menu.clickMenuButton(p,-1),"Cannot overwrite a bound spell");
        menu.clickMenuButton(p,0);check(!menu.getResultSlot().getItem().isEmpty(),"Removable spell exposes native extraction result");
        check(!menu.clickMenuButton(p,-1),"Occupied spells must be extracted before replacement");
        menu.clicked(38,0,ClickType.PICKUP,p);
        check(!menu.getCarried().isEmpty()&&ISpellContainer.get(menu.getCarried()).getSpellAtIndex(0).getSpell()==SpellRegistry.FIREBOLT_SPELL.get(),"Extraction gives the original spell scroll");
        check(menu.spells().getSpellAtIndex(0).equals(SpellData.EMPTY)&&menu.getResultSlot().getItem().isEmpty(),"Extraction removes spell and invalidates result immediately");
        check(!menu.clickMenuButton(p,2),"Held cursor blocks spell actions");menu.setCarried(ItemStack.EMPTY);
        check(menu.clickMenuButton(p,-1)&&menu.spells().getSpellAtIndex(0).getSpell()==SpellRegistry.ICE_SPIKES_SPELL.get()&&menu.getScrollSlot().getItem().isEmpty(),"Replacement consumes exactly one scroll and changes actual book");
        menu.getScrollSlot().set(scroll(SpellRegistry.ICE_SPIKES_SPELL.get()));menu.clickMenuButton(p,2);
        check(!menu.clickMenuButton(p,-1)&&menu.getScrollSlot().getItem().getCount()==1,"Duplicate spell rejected without consuming scroll");
        menu.getScrollSlot().set(scroll(SpellRegistry.LIGHTNING_BOLT_SPELL.get()));
        var cancel=new CancelInscription();MinecraftForge.EVENT_BUS.register(cancel);
        try{check(!menu.clickMenuButton(p,-1)&&menu.spells().getSpellAtIndex(2).equals(SpellData.EMPTY)&&menu.getScrollSlot().hasItem(),"Addon InscribeSpellEvent cancellation respected");}finally{MinecraftForge.EVENT_BUS.unregister(cancel);}
        int scrolls=p.getInventory().countItem(ItemRegistry.SCROLL.get());menu.removed(p);menu.removed(p);
        check(p.getInventory().countItem(ItemRegistry.SCROLL.get())==scrolls+1,"Closing returns unspent scroll exactly once");
        check(Utils.getPlayerSpellbookStack(p).getOrCreateTag().getString("test:preserved").equals("runes"),"Closing preserves equipped book and custom NBT");
        p.containerMenu=p.inventoryMenu;
        ServerConfig.REQUIRE_TABLE.set(true);SpellMenu.open(p);check(p.containerMenu==p.inventoryMenu,"Server table requirement blocks remote inscription");ServerConfig.REQUIRE_TABLE.set(false);
        ServerConfig.SPELL_TAB.set(false);SpellMenu.open(p);check(p.containerMenu==p.inventoryMenu,"Server can disable portable spell tab");ServerConfig.SPELL_TAB.set(true);
        Utils.setPlayerSpellbookStack(p,previousBook);
    }
    public static void verifyReplaced(ServerPlayer p){
        var book=Utils.getPlayerSpellbookStack(p);
        check(book!=null&&ISpellContainer.get(book).getSpellAtIndex(0).getSpell()==SpellRegistry.ICE_SPIKES_SPELL.get(),"Network replacement updates real equipped Curios book");
        check(ISpellContainer.get(book).getSpellAtIndex(1).isLocked(),"Network edits preserve bound spell");
        check(book.getOrCreateTag().getString("test:preserved").equals("runes"),"Network edits preserve other-mod book NBT");
    }
    public static final class CancelInscription {@SubscribeEvent public void cancel(InscribeSpellEvent e){e.setCanceled(true);}}
}
