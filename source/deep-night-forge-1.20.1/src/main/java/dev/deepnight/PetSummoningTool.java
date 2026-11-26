package dev.deepnight;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import java.util.List;
public final class PetSummoningTool extends Item {
    public PetSummoningTool(){super(new Properties().stacksTo(1).rarity(Rarity.RARE).fireResistant());}
    @Override public void appendHoverText(ItemStack s,Level l,List<Component> text,TooltipFlag flags){text.add(Component.literal("Choose a companion. Summoned pets are already yours."));text.add(Component.literal("Pets gain levels, health and damage by adventuring together."));}
    @Override public InteractionResultHolder<ItemStack> use(Level level,Player player,InteractionHand hand){
        if(player instanceof ServerPlayer p)PetNetwork.open(p);
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand),level.isClientSide);
    }
}
