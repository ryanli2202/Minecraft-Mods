package dev.emeraldcovenant.qa;

import dev.emeraldcovenant.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.*;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import java.nio.file.*;
import java.util.*;

public final class ShopChecks {
    public static final StringBuilder REPORT=new StringBuilder();
    public static void check(boolean ok,String message){if(!ok)throw new IllegalStateException(message);REPORT.append("PASS: ").append(message).append('\n');}
    public static void run(ServerPlayer player)throws Exception {
        ShopServer.rebuild();Catalogue c=ShopServer.catalogue;Inventory inv=player.getInventory();inv.clearContent();
        check(c!=null&&c.offers.size()>60,"Server catalogue created from live item registry: "+c.counts());
        check(c.byKey.get("item:minecraft:diamond_sword").damage()==7,"Diamond sword uses base attack damage of 7");
        check(c.byKey.get("item:minecraft:diamond_chestplate").setProtection()==20,"Diamond armour grouped into a 20-protection set");
        check(c.byKey.get("item:minecraft:iron_helmet").setProtection()==15,"Iron armour grouped into a 15-protection set");
        check((c.byKey.get("item:minecraft:iron_helmet").required()&2)!=0,"Warden bearing consistently gates an advanced armour set");
        check(c.byKey.get("item:minecraft:diamond_horse_armor").protection()==11,"Horse armour uses its actual protection value");
        check(c.byKey.get("item:minecraft:bread").price()<=3,"Food is inexpensive");
        check(ShopConfig.MAX_PRICE.get()==640,"New/legacy default maximum price resolves to 640");
        check(c.offers.stream().allMatch(e->e.price()>=1&&e.price()<=640),"Every live offer costs at most 640 emeralds per item");
        check(c.offers.stream().filter(e->e.category()==Category.FOOD).allMatch(e->e.price()<=12),"Every default food offer costs at most 12 emeralds");
        check(c.byKey.get("item:emeraldcovenant:warden_bell_bearing").price()==48&&c.byKey.get("item:emeraldcovenant:sovereign_bell_bearing").price()==128,"Bell bearings cost 48, with Sovereign at 128");
        int[] bandMin={16,72,144,288,480},bandMax={64,128,256,448,640};
        for(int tier=0;tier<5;tier++)for(Category cat:List.of(Category.WEAPONS,Category.ARMOUR,Category.SPELLS,Category.SPELLBOOKS)) {
            int low=Prices.equipment(cat,tier,0,0,0,0),high=Prices.equipment(cat,tier,1000000,1000000,Integer.MAX_VALUE,10000);
            check(low>=bandMin[tier]&&high<=bandMax[tier]&&low<=high,"Rarity band bounds survive extreme stats: "+cat+" tier "+tier);
            if(tier>0)check(low>bandMax[tier-1],"Rarer equipment always exceeds the lower rarity price band: "+cat+" tier "+tier);
        }
        check(Prices.cap(2048,640)==640&&Prices.cap(999999,2048)==999,"Legacy override and oversized config values cannot produce four-digit unit prices");
        check(Prices.cap(64000,640)==640,"High price multiplier remains below the unit cap");
        var weapons=c.offers.stream().filter(e->e.category()==Category.WEAPONS).toList();
        for(int i=1;i<weapons.size();i++)check(weapons.get(i-1).damage()>=weapons.get(i).damage(),"Weapon order "+weapons.get(i).key());
        for(var item:ForgeRegistries.ITEMS)if(item instanceof ArmorItem||item instanceof SwordItem||item instanceof ProjectileWeaponItem||item.isEdible()||SpellCompat.isBook(item))
            check(c.byKey.containsKey("item:"+ForgeRegistries.ITEMS.getKey(item)),"Registered equipment / food / spellbook included: "+ForgeRegistries.ITEMS.getKey(item));
        inv.items.set(0,new ItemStack(Items.EMERALD,10));
        var product=new ItemStack(Items.BREAD);
        check(!Transactions.purchase(inv,product,1,11).success()&&Transactions.emeralds(inv)==10,"Insufficient emeralds leaves inventory untouched");
        check(!Transactions.purchase(inv,product,-1,1).success()&&Transactions.emeralds(inv)==10,"Negative quantity rejected");
        check(!Transactions.purchase(inv,product,Integer.MAX_VALUE,999).success(),"Overflow-sized purchase rejected");
        check(!Transactions.purchase(inv,product,1,1000).success()&&Transactions.emeralds(inv)==10,"Four-digit unit price rejected without spending");
        check(Transactions.purchase(inv,product,3,2).success()&&Transactions.emeralds(inv)==4&&inv.countItem(Items.BREAD)==3,"Exact debit and quantity delivery");
        inv.clearContent();for(int i=0;i<inv.items.size();i++)inv.items.set(i,new ItemStack(Items.COBBLESTONE,64));inv.items.set(0,new ItemStack(Items.EMERALD,64));
        check(!Transactions.purchase(inv,new ItemStack(Items.DIAMOND_SWORD),1,1).success()&&Transactions.emeralds(inv)==64,"Full inventory rejects purchase without spending");
        check(Transactions.purchase(inv,new ItemStack(Items.DIAMOND_SWORD),1,64).success()&&inv.countItem(Items.DIAMOND_SWORD)==1,"Payment can free the slot used for delivery");
        inv.clearContent();inv.offhand.set(0,new ItemStack(Items.EMERALD,6));
        check(Transactions.purchase(inv,product,2,3).success()&&Transactions.emeralds(inv)==0,"Offhand emerald payment supported");
        inv.clearContent();inv.items.set(0,new ItemStack(Items.EMERALD,64));inv.items.set(1,new ItemStack(Items.BREAD,63));
        check(Transactions.purchase(inv,product,3,1).success()&&inv.items.get(1).getCount()==64&&inv.countItem(Items.BREAD)==66,"Stack merging and overflow to empty slot");
        inv.clearContent();inv.items.set(0,Covenant.BEARINGS.get(0).get().getDefaultInstance());
        ShopServer.offer(player,1);check((ShopServer.mask(player)&1)!=0&&inv.items.get(0).isEmpty(),"Bearing consumed once and unlock saved");
        inv.items.set(0,Covenant.BEARINGS.get(0).get().getDefaultInstance());ShopServer.offer(player,1);
        check(!inv.items.get(0).isEmpty(),"Duplicate offered bearing is not consumed");
        var tag=GuildData.get(player.server).save(new CompoundTag());
        check(GuildData.load(tag).mask(player.getUUID())==ShopServer.mask(player),"Bell bearing unlock survives save/load");
        UUID other=UUID.randomUUID();check(GuildData.load(tag).mask(other)==0,"Unlocks are isolated between characters");
        var rareWeapon=c.byKey.get("item:minecraft:diamond_sword").withRequired(1|8);
        check(!rareWeapon.unlocked(3)&&rareWeapon.missingBearings(3).equals("Sovereign"),"Offered Forge and Warden remain recognised; rare weapon names only missing Sovereign");
        check(rareWeapon.unlocked(3|8)&&rareWeapon.missingBearings(3|8).isEmpty(),"Adding Sovereign unlocks combined-requirement stock");
        check(rareWeapon.missingBearings(0).equals("Forgemaster + Sovereign"),"Combined requirements name every missing bearing");
        check(!rareWeapon.unlocked(8)&&rareWeapon.missingBearings(8).equals("Forgemaster"),"Sovereign does not replace the merchant bearing");
        if(ModList.get().isLoaded("irons_spellbooks")) {
            var scrolls=c.offers.stream().filter(e->e.key().startsWith("spell:")).toList();
            check(scrolls.size()>300,"Populated spell variants discovered at all levels: "+scrolls.size());
            Class<?> container=Class.forName("io.redspace.ironsspellbooks.api.spells.ISpellContainer");
            for(var e:scrolls){Object stored=container.getMethod("get",ItemStack.class).invoke(null,e.stack());Object spell=container.getMethod("getSpellAtIndex",int.class).invoke(stored,0);int level=(int)spell.getClass().getMethod("getLevel").invoke(spell);check(level==e.spellLevel(),"Scroll NBT level matches offer: "+e.key());}
            check(c.warnings.isEmpty(),"No catalogue integration warnings");
        }
        inv.clearContent();for(int i=0;i<5;i++)inv.items.set(i,new ItemStack(Items.EMERALD,64));
        player.inventoryMenu.broadcastChanges();
        Files.createDirectories(Path.of("../verification"));
        Files.writeString(Path.of("../verification/server-checks.txt"),REPORT.toString());
        var rows=new com.google.gson.JsonArray();for(Offer e:c.offers){var o=new com.google.gson.JsonObject();o.addProperty("key",e.key());o.addProperty("category",e.category().name());o.addProperty("price",e.price());o.addProperty("damage",e.damage());o.addProperty("setProtection",e.setProtection());o.addProperty("source",e.source());rows.add(o);}
        Files.writeString(Path.of("../verification/catalogue-audit.json"),new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(rows));
    }
}
