package dev.deepnightguide.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EntityType;
import java.util.*;

public final class GuideEntry {
    public enum Category {
        ALL("All content"), ORES("Ores"), ARMOUR("Armour"), WEAPONS("Weapons"), TOOLS("Tools"), ITEMS("Items / blocks"), MOBS("Mobs"), PETS("Pets"), BOSSES("Bosses"), LORE("World & lore"),
        TAMING_LIST("Taming checklist"), WEAPON_LIST("Weapon checklist"), ARMOUR_LIST("Armour checklist"), BOSS_LIST("Boss checklist");
        public final String title;
        Category(String title) { this.title = title; }
        public boolean checklist() { return this==TAMING_LIST||this==WEAPON_LIST||this==ARMOUR_LIST||this==BOSS_LIST; }
    }
    public final ResourceLocation id;
    public final String key, name, mod;
    public final ItemStack defaultIcon;
    public ItemStack icon;
    public final EntityType<?> entityType;
    public final EnumSet<Category> categories;
    public boolean tameable,boss,inferredBoss;
    public final boolean lore;
    public final Map<String,List<String>> loreSections=new LinkedHashMap<>();
    public String bossReason="";
    public String bossProgressKey() {return "boss:"+id;}
    public String progressKey(Category context) {return boss&&(context==Category.BOSSES||context==Category.BOSS_LIST||!tameable)?bossProgressKey():key;}
    public GuideEntry(ResourceLocation id, String name, String mod, ItemStack icon, EntityType<?> type, EnumSet<Category> categories) {
        this.id=id; this.name=name; this.mod=mod; this.icon=icon; this.defaultIcon=icon; this.entityType=type; this.categories=categories;
        this.lore=categories.contains(Category.LORE);
        this.key=(lore?"lore:":type == null ? "item:" : "entity:")+id;
    }
    public boolean matches(Category category) {
        return switch(category) {
            case ALL -> true;
            case PETS, TAMING_LIST -> tameable;
            case WEAPON_LIST -> categories.contains(Category.WEAPONS);
            case ARMOUR_LIST -> categories.contains(Category.ARMOUR);
            case BOSSES, BOSS_LIST -> boss;
            default -> categories.contains(category);
        };
    }
    public boolean trackable() { return !lore&&(boss || tameable || categories.contains(Category.WEAPONS) || categories.contains(Category.ARMOUR)); }
}
