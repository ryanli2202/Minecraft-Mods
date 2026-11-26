package dev.emeraldcovenant;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import java.lang.reflect.*;
import java.util.*;

/** Optional bridge: API names are inspected reflectively, with no mandatory spell-mod dependency. */
public final class SpellCompat {
    private static final String API="io.redspace.ironsspellbooks.api.";
    public static boolean isBook(Item item){return instance(API+"item.ISpellbook",item);}
    public static boolean isScroll(Item item){return instance(API+"item.IScroll",item);}
    private static boolean instance(String type,Object item) {
        if(!ModList.get().isLoaded("irons_spellbooks"))return false;
        try{return Class.forName(type).isInstance(item);}catch(ReflectiveOperationException e){return false;}
    }
    public static void addSpells(Catalogue catalogue) {
        if(!ModList.get().isLoaded("irons_spellbooks"))return;
        try {
            Class<?> spellClass=Class.forName(API+"spells.AbstractSpell");
            Class<?> container=Class.forName(API+"spells.ISpellContainer");
            Method create=container.getMethod("createScrollContainer",spellClass,int.class,ItemStack.class);
            Collection<?> spells=(Collection<?>)Class.forName(API+"registry.SpellRegistry").getMethod("getEnabledSpells").invoke(null);
            Item scroll=ForgeRegistries.ITEMS.getValue(new ResourceLocation("irons_spellbooks","scroll"));
            if(scroll==null||scroll==Items.AIR)throw new IllegalStateException("Scroll item unavailable");
            int added=0;
            for(Object spell:spells)try {
                String id=(String)spellClass.getMethod("getSpellId").invoke(spell);
                int max=(int)spellClass.getMethod("getMaxLevel").invoke(spell);
                int min=(int)spellClass.getMethod("getMinLevel").invoke(spell);
                if(max<min||max>10000)throw new IllegalStateException("Invalid spell levels: "+id);
                for(int level=Math.max(1,min);level<=max;level++) {
                    ItemStack stack=new ItemStack(scroll);
                    create.invoke(null,spell,level,stack);
                    Enum<?> rarity=(Enum<?>)spellClass.getMethod("getRarity",int.class).invoke(spell,level);
                    catalogue.add("spell:"+id+":"+level,stack,Category.SPELLS,level,rarity.ordinal(),new ResourceLocation(id).getNamespace());added++;
                }
            }catch(Exception ex){catalogue.warn("Spell could not be indexed: "+ex);}
            Covenant.LOG.info("Created {} populated Iron's spell scroll offers (including registered add-ons)",added);
        }catch(Exception ex){catalogue.warn("Iron's spell integration failed: "+ex+". Check the installed API version.");}
    }
}
