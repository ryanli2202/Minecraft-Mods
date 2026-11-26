package dev.emeraldcovenant;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import java.util.List;

public final class BearingItem extends Item {
    public final int bit;
    public BearingItem(int bit) { super(new Properties().stacksTo(1).rarity(bit==8?Rarity.EPIC:Rarity.RARE)); this.bit=bit; }
    @Override public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        var stack=player.getItemInHand(hand);
        if (!level.isClientSide && player instanceof ServerPlayer server) ShopServer.offer(server,bit);
        return InteractionResultHolder.sidedSuccess(stack,level.isClientSide);
    }
    @Override public void appendHoverText(ItemStack stack, Level level, List<Component> lines, TooltipFlag flag) {
        lines.add(Component.literal("Right-click or offer in the shop to unlock stock.").withStyle(net.minecraft.ChatFormatting.GRAY));
        lines.add(Component.literal(switch(bit) {
            case 1 -> "Forgemaster: advanced weapons";
            case 2 -> "Warden: advanced armour";
            case 4 -> "Archivist: advanced spells and spellbooks";
            default -> "Sovereign: rare and legendary wares";
        }).withStyle(net.minecraft.ChatFormatting.GOLD));
        lines.add(Component.literal(bit==8?"Also requires the stock's merchant bearing.":"Rare and powerful stock also needs Sovereign.").withStyle(net.minecraft.ChatFormatting.GRAY));
        lines.add(Component.literal("Permanent for this character in this world.").withStyle(net.minecraft.ChatFormatting.DARK_GRAY));
    }
}
