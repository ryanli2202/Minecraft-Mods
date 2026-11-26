package dev.emeraldcovenant.client;

import dev.emeraldcovenant.*;
import net.minecraft.client.Minecraft;
import java.util.*;

public final class ClientShop {
    public static long token;
    public static int mask,emeralds,expected,sequence;
    public static boolean loading,pending;
    public static String message="",warning="";
    public static boolean success=true;
    public static final List<Offer> offers=new ArrayList<>();
    public static void begin(ShopNetwork.Begin m) {
        var mc=Minecraft.getInstance();if(mc.player==null)return;
        token=m.token();mask=m.mask();emeralds=m.emeralds();expected=m.count();sequence=0;loading=true;pending=false;offers.clear();warning=m.warning();message="The merchants are preparing their wares...";
        if(!(mc.screen instanceof ShopScreen))mc.setScreen(new ShopScreen());
    }
    public static void chunk(ShopNetwork.Chunk m) {
        if(m.token()!=token||!loading)return;
        offers.addAll(m.offers());
        if(m.last()) {
            loading=false;success=offers.size()==expected;
            message=success?"Choose a merchant. Select an item to inspect and purchase.":"Catalogue incomplete. Please reopen the shop.";
            if(!warning.isBlank())message="Some items could not be indexed. See the server log / catalogue audit.";
            if(Minecraft.getInstance().screen instanceof ShopScreen s)s.refresh();
        }
    }
    public static void status(ShopNetwork.Status m) {
        if(m.token()!=token)return;
        mask=m.mask();emeralds=m.emeralds();success=m.success();message=m.message();pending=false;
        if(Minecraft.getInstance().screen instanceof ShopScreen s)s.refresh();
    }
    public static void buy(Offer offer,int quantity) {
        if(pending||loading)return;pending=true;message="Completing your trade...";
        ShopNetwork.toServer(new ShopNetwork.Buy(token,++sequence,offer.key(),quantity));
    }
    public static void clear(){offers.clear();token=0;loading=false;pending=false;message="";}
}
