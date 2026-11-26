package dev.transmogify.qa;

import com.mojang.authlib.GameProfile;
import dev.transmogify.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.*;

public final class ArmourChecks {
    public static final StringBuilder REPORT=new StringBuilder();
    public static void check(boolean condition,String message) {
        if(!condition)throw new AssertionError(message);
        REPORT.append("PASS ").append(message).append('\n');TransmogifyArmour.LOG.info("TRANSMOG QA PASS: {}",message);
    }
    public static void run(ServerLevel level) {
        var player=FakePlayerFactory.get(level,new GameProfile(UUID.randomUUID(),"TransmogQA"));
        ItemStack real=new ItemStack(Items.DIAMOND_CHESTPLATE);real.setDamageValue(77);
        real.enchant(Enchantments.ALL_DAMAGE_PROTECTION,4);
        real.getOrCreateTag().putString("anothermod:keep_me","preserved");
        player.setItemSlot(EquipmentSlot.CHEST,real);
        ItemStack original=real.copy();
        String[] choices={"","minecraft:golden_chestplate","",""};
        var session=WardrobeService.begin(player);
        check(WardrobeService.apply(player,session.token(),choices).isEmpty(),"Valid cosmetic change accepted");
        check(real.getItem()==Items.DIAMOND_CHESTPLATE&&real.getDamageValue()==77,"Real item and durability unchanged");
        check(real.getEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION)==4,"Enchantments preserved");
        check(real.getTag().getString("anothermod:keep_me").equals("preserved"),"Unrelated mod NBT preserved");
        check(real.getAttributeModifiers(EquipmentSlot.CHEST).equals(original.getAttributeModifiers(EquipmentSlot.CHEST)),"Armour attributes unchanged");
        check(ArmourData.visual(real,EquipmentSlot.CHEST).is(Items.GOLDEN_CHESTPLATE),"Renderer receives target armour");
        check(ArmourData.visual(real,EquipmentSlot.CHEST).hasFoil(),"Enchantment glint retained");
        ItemStack saved=ItemStack.of(real.save(new CompoundTag()));
        check(ArmourData.appearanceId(saved).equals(choices[1]),"Appearance persists through item NBT save/load");
        check(!WardrobeService.apply(player,session.token(),choices).isEmpty(),"Replayed session rejected");
        session=WardrobeService.begin(player);
        String[] bad={"","minecraft:diamond_helmet","",""};
        check(!WardrobeService.apply(player,session.token(),bad).isEmpty(),"Wrong equipment slot rejected");
        check(ArmourData.appearanceId(real).equals(choices[1]),"Rejected outfit makes no partial change");
        session=WardrobeService.begin(player);real.setDamageValue(78);
        check(!WardrobeService.apply(player,session.token(),choices).isEmpty(),"Equipment changes during menu rejected");
        session=WardrobeService.begin(player);
        check(!WardrobeService.apply(player,session.token(),new String[]{"minecraft:diamond_helmet","","",""}).isEmpty(),"Empty equipped slot cannot gain armour");
        check(!WardrobeService.validate(WardrobeService.snapshot(player),new String[]{"","minecraft:diamond","",""}).isEmpty(),"Non-armour item rejected");
        check(!WardrobeService.validate(WardrobeService.snapshot(player),new String[]{"","missing_mod:chestplate","",""}).isEmpty(),"Missing registry item rejected");
        check(!WardrobeService.validate(WardrobeService.snapshot(player),new String[]{"","bad id!!","",""}).isEmpty(),"Malformed registry identifier rejected");
        ArmourData.set(real,"removedmod:chestplate");
        check(ArmourData.visual(real,EquipmentSlot.CHEST)==real,"Removed mod falls back to original armour");
        real.setDamageValue(77);session=WardrobeService.begin(player);
        check(WardrobeService.apply(player,session.token(),new String[]{"","","",""}).isEmpty(),"Reset accepted");
        check(ItemStack.matches(real,original),"Reset restores original item NBT exactly");
        int count=0;
        for(Item item:ForgeRegistries.ITEMS) if(item instanceof ArmorItem armour) {
            var target=new ItemStack(item);var slot=armour.getEquipmentSlot();
            ArmourData.set(target,ArmourData.id(item));
            check(ArmourData.visual(target,slot).getItem()==item,"Registry armour resolves: "+ArmourData.id(item));count++;
        }
        check(count>=25,"All vanilla armour registered ("+count+" total pieces)");
    }
}
