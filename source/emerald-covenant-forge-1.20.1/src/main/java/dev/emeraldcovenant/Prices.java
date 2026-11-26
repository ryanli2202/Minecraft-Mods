package dev.emeraldcovenant;

/** Disjoint rarity bands keep prices affordable without flattening all valuable items to one cap. */
public final class Prices {
    private static final int[] MIN={16,72,144,288,480};
    private static final int[] MAX={64,128,256,448,640};
    private Prices() {}
    public static int equipment(Category category,int rarity,double damage,double protection,int durability,int spellLevel) {
        int tier=Math.max(0,Math.min(4,rarity));
        double strength=switch(category) {
            case WEAPONS -> Math.max(0,damage-2)*4+Math.min(12,Math.max(0,durability)/250.0);
            case ARMOUR -> Math.max(0,protection)*5+Math.min(16,Math.max(0,durability)/80.0);
            case SPELLS -> Math.max(0,spellLevel)*10.0;
            case SPELLBOOKS -> 24+Math.min(24,Math.max(0,durability)/100.0);
            default -> 0;
        };
        if(Double.isNaN(strength))strength=0;
        return MIN[tier]+(int)Math.ceil(Math.min(MAX[tier]-MIN[tier],strength));
    }
    /** Applies to generated prices AND overrides, including legacy four-digit overrides. */
    public static int cap(double requested,int configuredMaximum) {
        int limit=Math.max(1,Math.min(999,configuredMaximum));
        if(Double.isNaN(requested))return 1;
        return (int)Math.max(1,Math.min(limit,Math.ceil(requested)));
    }
}
