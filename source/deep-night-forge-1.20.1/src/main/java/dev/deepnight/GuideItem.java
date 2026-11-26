package dev.deepnight;

import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

public final class GuideItem extends Item {
    public GuideItem(){super(new Properties().stacksTo(1));}
    @Override public InteractionResultHolder<ItemStack> use(Level level,Player p,InteractionHand hand){
        if(p instanceof ServerPlayer sp){
            ItemStack book=new ItemStack(Items.WRITTEN_BOOK);CompoundTag tag=book.getOrCreateTag();
            tag.putString("title","The Deep Night");tag.putString("author","The Nightbound Archive");tag.putBoolean("resolved",true);
            ListTag pages=new ListTag();
            for(String text:new String[]{
                "THE DEEP NIGHT\n\nThe Ender Dragon is only the beginning. Defeat it to receive your Boss Summon Tool.\n\nUse the tool to open the boss menu. Summon in a large, flat, open arena.",
                "THE NIGHT CAMPAIGN\n\nDefeat "+Species.BOSS_COUNT+" bosses in order.\n\nThe menu previews every boss, marks your progress, and lets you summon your next opponent or rematch a defeated boss.",
                "REMATCHES & TESTING\n\nRematches do not repeat first-clear rewards.\n\nCreative players can summon any boss. Replace active boss switches encounters without defeat credit. Defeating your next boss advances progress; future bosses do not skip stages.",
                "BATTLE RULES\n\nOnly one campaign boss may be active in the world. Your next seal opens only after its boss dies.\n\nHelpers receive credit if they have unlocked this same stage, deal damage, and survive nearby. Owned pets count as their owner's damage.",
                "READ THE ATTACKS\n\nEach boss has its own attack timing, projectiles and hitboxes. Watch its windups, movement and effects.\n\nLarge bosses can strike well beyond their main body.",
                "PHASES\n\nPhases and special moves follow each boss's own rules. Some attacks can briefly stun or freeze you.\n\nPrepare enchanted armour, healing, ranged options, and a companion.",
                "RECOVERY\n\n/deepnight reclaim restores a lost tool after the dragon unlock.\n\nUse End active encounter in the boss menu to stop a stuck fight, then retry. No credit is awarded.\n\nBosses retreat after 30 loaded seconds without a living non-spectator player within 64 blocks.",
                "PET SUMMONING\n\nDeep Night pets do not spawn naturally. Craft the Pet Summoning Tool or buy it from a level-3 cleric.\n\nCraft: ender pearl above a book; gold ingot on both sides; golden apple below. Use it, search, and choose any pet. It arrives already tamed.",
                "GROWING TOGETHER\n\nPets gain XP near their online owner and from nearby hostile kills. Levels 1-50 add 2% base health and damage per level after 1.\n\nUse the pet tool on a pet to inspect progress. Their level and XP save with the world.",
                "COMPANIONS\n\nUse a saddle, then empty-hand interact to ride on land. Golden apples breed matching adults.\n\nDrakes and the fire, ice and lightning dragon pets use elemental breath against enemies. Star Unicorns heal nearby owners. Pets defend you and ignore your other pets.",
                "FORGING\n\nEach first campaign victory awards 4 sigils and Night Essence. In a crafting table:\n S \nENE\n A \n\nS: boss sigil\nE: Night Essence\nN: netherite ingot\nA: diamond, amethyst shard, or ender pearl to choose a weapon.",
                "ENCHANTMENTS\n\nAll 14 enchantments use the normal enchanting table. Enchant books there, or obtain books through librarian trades and ordinary enchanted-book loot. Apply them with an anvil.\n\nNo extra table or altar is needed.",
                "EXPLORATION\n\nNight Goblins, Grave Ghouls, Serpent Guards, Dusk Assassins, Bone Soldiers, Hungry Mimics, Lost Shades and Voidlings drop Night Essence.\n\nCombine essence, a golden carrot and cooked beef for four Bonding Treats."
            })pages.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal(text))));
            tag.put("pages",pages);p.setItemInHand(hand,book);sp.openItemGui(book,hand);return InteractionResultHolder.success(book);
        }
        return InteractionResultHolder.success(p.getItemInHand(hand));
    }
}
