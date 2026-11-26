package dev.emeraldcovenant;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public record Offer(String key, ItemStack stack, Category category, int price, int rarity,
                    double damage, double protection, double setProtection, String set,
                    int slot, int spellLevel, int required, int bearingBit, String source) {
    public boolean unlocked(int mask) { return (mask & required)==required; }
    public String missingBearings(int mask) {
        var names=new java.util.ArrayList<String>();
        for(int i=0;i<Covenant.BEARING_NAMES.length;i++)
            if((required & ~mask & (1<<i))!=0)names.add(Covenant.BEARING_NAMES[i]);
        return String.join(" + ",names);
    }
    public void write(FriendlyByteBuf b) {
        b.writeUtf(key,512);b.writeItemStack(stack,false);b.writeEnum(category);b.writeVarInt(price);b.writeVarInt(rarity);
        b.writeDouble(damage);b.writeDouble(protection);b.writeDouble(setProtection);b.writeUtf(set,256);
        b.writeVarInt(slot);b.writeVarInt(spellLevel);b.writeVarInt(required);b.writeVarInt(bearingBit);b.writeUtf(source,256);
    }
    public static Offer read(FriendlyByteBuf b) {
        return new Offer(b.readUtf(512),b.readItem(),b.readEnum(Category.class),b.readVarInt(),b.readVarInt(),
            b.readDouble(),b.readDouble(),b.readDouble(),b.readUtf(256),b.readVarInt(),b.readVarInt(),b.readVarInt(),b.readVarInt(),b.readUtf(256));
    }
    public Offer withSetProtection(double total) {
        return new Offer(key,stack,category,price,rarity,damage,protection,total,set,slot,spellLevel,required,bearingBit,source);
    }
    public Offer withRequired(int mask) {
        return new Offer(key,stack,category,price,rarity,damage,protection,setProtection,set,slot,spellLevel,mask,bearingBit,source);
    }
}
