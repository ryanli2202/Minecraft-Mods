package dev.deepnight;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import java.util.List;

public final class NightWeapon extends SwordItem {
    public final int rank;
    public NightWeapon(Weapon weapon) {
        super(Tiers.NETHERITE,5+weapon.rank,weapon.speed,new Properties().durability(2400+weapon.rank*240).fireResistant().rarity(Rarity.EPIC));
        this.rank=weapon.rank;
    }
    @Override public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if(rank==5) target.setSecondsOnFire(4);
        return super.hurtEnemy(stack,target,attacker);
    }
    @Override public void appendHoverText(ItemStack stack,Level level,List<Component> lines,TooltipFlag flag) {
        lines.add(Component.literal("Forged with the sigil of "+Species.boss(rank).title+"."));
        lines.add(Component.literal("Better Combat moveset included."));
    }
}
