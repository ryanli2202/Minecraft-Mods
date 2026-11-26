package dev.emeraldcovenant;

public enum Category {
    WEAPONS("Weapons", "Garrick", "The Forgemaster", 0, 1, "Attack damage, highest first"),
    ARMOUR("Armour", "Seren", "The Iron Warden", 1, 2, "Set protection, highest first"),
    SPELLS("Spells", "Orin", "Keeper of the Archive", 2, 4, "Spell level, highest first"),
    SPELLBOOKS("Spellbooks", "Orin", "Keeper of the Archive", 2, 4, "Rarity, highest first"),
    FOOD("Provisions", "Merri", "The Hearthkeeper", 3, 0, "Price, lowest first"),
    BEARINGS("Bell bearings", "The Covenant", "Oaths that open new doors", 2, 0, "Permanent merchant unlocks");
    public final String label, merchant, title, sort;
    public final int portrait, bit;
    Category(String label,String merchant,String title,int portrait,int bit,String sort) {
        this.label=label;this.merchant=merchant;this.title=title;this.portrait=portrait;this.bit=bit;this.sort=sort;
    }
}
