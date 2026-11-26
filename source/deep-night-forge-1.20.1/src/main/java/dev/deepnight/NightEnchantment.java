package dev.deepnight;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.*;

public final class NightEnchantment extends Enchantment {
    public enum Type {
        BERSERK(3,EnchantmentCategory.WEAPON), SPIDER_BITE(3,EnchantmentCategory.WEAPON),
        RADIANCE(3,EnchantmentCategory.WEAPON), FROZEN_TOUCH(3,EnchantmentCategory.WEAPON),
        SOUL_FIRE(3,EnchantmentCategory.WEAPON), LIFE_REAPER(3,EnchantmentCategory.WEAPON),
        BOSS_SLAYER(5,EnchantmentCategory.WEAPON), RETRIBUTION(3,EnchantmentCategory.ARMOR_CHEST),
        VITALITY(5,EnchantmentCategory.ARMOR_CHEST), AGILITY(3,EnchantmentCategory.ARMOR_FEET),
        LEAPING(2,EnchantmentCategory.ARMOR_FEET), LAST_STAND(3,EnchantmentCategory.ARMOR_CHEST),
        WISDOM(3,EnchantmentCategory.DIGGER), DEEP_GUARD(3,EnchantmentCategory.ARMOR);
        public final int max;public final EnchantmentCategory category;
        Type(int max,EnchantmentCategory category){this.max=max;this.category=category;}
        public String id(){return name().toLowerCase(java.util.Locale.ROOT);}
    }
    public final Type type;
    private static EquipmentSlot[] slots(Type type){
        if(type.category==EnchantmentCategory.ARMOR_CHEST)return new EquipmentSlot[]{EquipmentSlot.CHEST};
        if(type.category==EnchantmentCategory.ARMOR_FEET)return new EquipmentSlot[]{EquipmentSlot.FEET};
        if(type.category==EnchantmentCategory.ARMOR)return new EquipmentSlot[]{EquipmentSlot.HEAD,EquipmentSlot.CHEST,EquipmentSlot.LEGS,EquipmentSlot.FEET};
        return new EquipmentSlot[]{EquipmentSlot.MAINHAND};
    }
    public NightEnchantment(Type type){super(Rarity.RARE,type.category,slots(type));this.type=type;}
    @Override public int getMaxLevel(){return type.max;}
    @Override public int getMinCost(int level){return 8+(level-1)*6;}
    @Override public int getMaxCost(int level){return getMinCost(level)+35;}
    @Override public boolean isTreasureOnly(){return false;}
    @Override public boolean isDiscoverable(){return true;}
    @Override public boolean isTradeable(){return true;}
    @Override protected boolean checkCompatibility(Enchantment other){
        if(!super.checkCompatibility(other))return false;
        if(other instanceof NightEnchantment n){
            if((type==Type.LIFE_REAPER&&n.type==Type.RADIANCE)||(type==Type.RADIANCE&&n.type==Type.LIFE_REAPER))return false;
            if((type==Type.FROZEN_TOUCH&&n.type==Type.SOUL_FIRE)||(type==Type.SOUL_FIRE&&n.type==Type.FROZEN_TOUCH))return false;
        }
        return true;
    }
}
