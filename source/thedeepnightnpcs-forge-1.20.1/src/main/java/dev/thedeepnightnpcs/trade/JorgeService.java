package dev.thedeepnightnpcs.trade;

import dev.thedeepnightnpcs.DeepNightNpcs;
import dev.thedeepnightnpcs.entity.NpcEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.item.trading.*;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.*;

public final class JorgeService {
    public static final int REFRESH_TICKS=1200;
    public static List<Item> ingots() {
        return ForgeRegistries.ITEMS.getValues().stream().filter(item->{
            var id=ForgeRegistries.ITEMS.getKey(item);
            if(item==Items.AIR || id==null || DeepNightNpcs.EXCLUDED_INGOTS.get().contains(id.toString()))return false;
            boolean extra=DeepNightNpcs.EXTRA_INGOTS.get().contains(id.toString());
            // Forge also tags ordinary bricks as ingots; the armskeeper stocks metals by default.
            if((item==Items.BRICK||item==Items.NETHER_BRICK)&&!extra)return false;
            return id.getPath().endsWith("_ingot") || extra ||
                item.builtInRegistryHolder().tags().anyMatch(tag->{var key=tag.location();return (key.getNamespace().equals("forge")||key.getNamespace().equals("c")) && (key.getPath().equals("ingots")||key.getPath().startsWith("ingots/"));});
        }).sorted(Comparator.comparing(item->ForgeRegistries.ITEMS.getKey(item).toString())).toList();
    }
    public static List<Enchantment> enchantments() {
        return ForgeRegistries.ENCHANTMENTS.getValues().stream().filter(e->e.isTradeable() && e.getMaxLevel()>=1 &&
            !DeepNightNpcs.EXCLUDED_ENCHANTMENTS.get().contains(ForgeRegistries.ENCHANTMENTS.getKey(e).toString()))
            .sorted(Comparator.comparing(e->ForgeRegistries.ENCHANTMENTS.getKey(e).toString())).toList();
    }
    private static long now(ServerPlayer player){return player.server.overworld().getGameTime();}
    public static JorgeStock.Stock stock(ServerPlayer player) {
        JorgeStock data=JorgeStock.get(player.server);var s=data.player(player.getUUID());
        if(s.ingots==null){s.ingots=rollIngots(player);s.nextIngots=now(player)+REFRESH_TICKS;data.setDirty();}
        if(s.books==null){s.books=rollBooks(player);s.nextBooks=now(player)+REFRESH_TICKS;data.setDirty();}
        // Remove offers invalidated by a pack change, without resetting their surviving neighbours.
        var validIngots=new HashSet<>(ingots());var validEnchants=new HashSet<>(enchantments());
        if(s.ingots.removeIf(o->o.getResult().isEmpty()||!validIngots.contains(o.getResult().getItem())))data.setDirty();
        if(s.books.removeIf(o->{var es=EnchantmentHelper.getEnchantments(o.getResult());return !o.getResult().is(Items.ENCHANTED_BOOK)||es.isEmpty()||!validEnchants.containsAll(es.keySet());}))data.setDirty();
        return s;
    }
    private static MerchantOffers rollIngots(ServerPlayer p) {
        List<Item> items=new ArrayList<>(ingots());Collections.shuffle(items,new Random(p.getRandom().nextLong()));
        MerchantOffers offers=new MerchantOffers();
        for(Item item:items.subList(0,Math.min(3,items.size()))) {
            int units=item==Items.NETHERITE_INGOT?1:1+p.getRandom().nextInt(4);
            int each=item==Items.COPPER_INGOT?1:item==Items.IRON_INGOT?2:item==Items.GOLD_INGOT?4:item==Items.NETHERITE_INGOT?32:6;
            offers.add(new MerchantOffer(new ItemStack(Items.EMERALD,Math.min(64,units*each)),new ItemStack(item,units),8,0,0));
        }
        return offers;
    }
    private static MerchantOffers rollBooks(ServerPlayer p) {
        List<Enchantment> enchants=new ArrayList<>(enchantments());Collections.shuffle(enchants,new Random(p.getRandom().nextLong()));
        MerchantOffers offers=new MerchantOffers();
        for(Enchantment enchantment:enchants.subList(0,Math.min(5,enchants.size()))) {
            int max=Math.min(255,Math.max(1,enchantment.getMaxLevel())),min=Math.min(max,Math.max(1,enchantment.getMinLevel()));
            int level=min+p.getRandom().nextInt(max-min+1);
            int price=Math.min(64,(6+level*4+p.getRandom().nextInt(7))*(enchantment.isTreasureOnly()?2:1));
            offers.add(new MerchantOffer(new ItemStack(Items.EMERALD,price),EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment,level)),3,0,0));
        }
        return offers;
    }
    public static String refresh(ServerPlayer p,boolean books) {
        var s=stock(p);long remaining=(books?s.nextBooks:s.nextIngots)-now(p);
        if(remaining>0)return "My "+(books?"book":"ingot")+" shipment is still on its way. Try again in "+((remaining+19)/20)+" seconds.";
        if(books){s.books=rollBooks(p);s.nextBooks=now(p)+REFRESH_TICKS;}
        else{s.ingots=rollIngots(p);s.nextIngots=now(p)+REFRESH_TICKS;}
        JorgeStock.get(p.server).setDirty();return "Fresh "+(books?"books":"ingots")+", fresh possibilities. The new stock is ready to browse.";
    }
    public static CompoundTag snapshot(ServerPlayer p) {
        var s=stock(p);var n=s.save();
        n.putInt("ingotSeconds",(int)Math.max(0,(s.nextIngots-now(p)+19)/20));
        n.putInt("bookSeconds",(int)Math.max(0,(s.nextBooks-now(p)+19)/20));
        n.putInt("ingotTypes",ingots().size());n.putInt("enchantmentTypes",enchantments().size());return n;
    }
    public static boolean nearby(Player player,NpcEntity npc) {
        return npc.isAlive()&&npc.kind().equals("jorge")&&player.isAlive()&&!player.isSpectator()&&player.level()==npc.level()&&player.distanceToSqr(npc)<=64&&player.hasLineOfSight(npc);
    }
    public static String trade(ServerPlayer p,NpcEntity npc,boolean books) {
        if(!nearby(p,npc))return "Come closer to the counter.";
        if(npc.getTradingPlayer()!=null && npc.getTradingPlayer()!=p) {
            Player other=npc.getTradingPlayer();
            if(other.isAlive()&&!other.isRemoved()&&other.containerMenu instanceof MerchantMenu)return "I am serving another customer. I will be with you when they close the shop.";
            npc.setTradingPlayer(null);
        }
        var offers=books?stock(p).books:stock(p).ingots;
        if(offers.isEmpty())return "There are no eligible "+(books?"enchantments":"ingots")+" in this world's stock list.";
        p.closeContainer();npc.setTradingPlayer(p);npc.overrideOffers(offers);
        var opened=p.openMenu(new SimpleMenuProvider((id,inv,player)->new MerchantMenu(id,inv,npc){
            @Override public boolean stillValid(Player viewer){return super.stillValid(viewer)&&nearby(viewer,npc);}
        },Component.literal(books?"Jorge - Enchanted Books":"Jorge - Ingots")));
        if(opened.isEmpty()){npc.setTradingPlayer(null);return "The shop could not be opened. Try speaking to me again.";}
        p.sendMerchantOffers(opened.getAsInt(),offers,1,0,false,false);return null;
    }
    public static String enchant(ServerPlayer p,NpcEntity npc) {
        if(!nearby(p,npc))return "Come closer to the workbench.";
        p.openMenu(new SimpleMenuProvider((id,inv,player)->new EnchantmentMenu(id,inv,ContainerLevelAccess.create(npc.level(),npc.blockPosition())){
            @Override public boolean stillValid(Player viewer){return nearby(viewer,npc);}
            @Override public boolean clickMenuButton(Player viewer,int button){return button>=0&&button<3&&stillValid(viewer)&&super.clickMenuButton(viewer,button);}
        },Component.literal("Jorge - Enchanting")));
        return null;
    }
}
