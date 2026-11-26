package dev.nordui.qa;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import top.theillusivec4.curios.api.CuriosApi;

public final class CuriosChecks {
    public static void setup(Player player){
        var inv=CuriosApi.getCuriosInventory(player).resolve().orElseThrow();
        StorageChecks.check(inv.getStacksHandler("ring").orElseThrow().getSlots()==2,"Curios provides configured dynamic ring slots");
        ItemStack ring=new ItemStack(Items.EMERALD);ring.setHoverName(net.minecraft.network.chat.Component.literal("Northern Signet"));ring.getOrCreateTag().putString("nordui_qa","preserved");
        inv.setEquippedCurio("ring",0,ring);
    }
    public static void assertEquipped(Player player){
        var stack=CuriosApi.getCuriosInventory(player).resolve().orElseThrow().getStacksHandler("ring").orElseThrow().getStacks().getStackInSlot(0);
        StorageChecks.check(stack.is(Items.EMERALD)&&stack.getOrCreateTag().getString("nordui_qa").equals("preserved"),"Curios accessory and custom data remain intact");
    }
    public static int ringSlot(Player p){
        for(int i=0;i<p.containerMenu.slots.size();i++)if(p.containerMenu.slots.get(i) instanceof top.theillusivec4.curios.common.inventory.CurioSlot s&&s.getIdentifier().equals("ring")&&!s.isCosmetic()&&s.hasItem())return i;
        throw new AssertionError("No visible equipped ring slot");
    }
}
