package dev.nordui.compat.irons;

import dev.nordui.*;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.gui.inscription_table.InscriptionTableMenu;
import io.redspace.ironsspellbooks.item.Scroll;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.RegistryObject;

/** Native inscription owns all items, Curios synchronization and InscribeSpellEvent hooks. */
public final class SpellMenu extends InscriptionTableMenu {
    public static RegistryObject<MenuType<SpellMenu>> TYPE;
    private final Player owner;
    private final BlockPos table;
    private int selection = -1;
    private boolean closed;
    public static void register() {
        TYPE = NordUI.MENUS.register("spells", () -> IForgeMenuType.create((id, inv, data) -> new SpellMenu(id, inv, null)));
    }
    public SpellMenu(int id, Inventory inventory, BlockPos table) {
        // NULL access skips native close cleanup. A real context is necessary even for portable menus.
        super(id, inventory, ContainerLevelAccess.create(inventory.player.level(), inventory.player.blockPosition()));
        this.owner = inventory.player;
        this.table = table;
    }
    @Override public MenuType<?> getType() { return TYPE.get(); }
    public static void open(ServerPlayer p) {
        if (!ServerConfig.SPELL_TAB.get()) { p.displayClientMessage(Component.literal("The spell tab is disabled by this server."),true); return; }
        if (!p.isAlive() || p.isSpectator() || p.containerMenu != p.inventoryMenu || !p.containerMenu.getCarried().isEmpty()) return;
        if (MagicData.getPlayerMagicData(p).isCasting()) { p.displayClientMessage(Component.literal("Finish casting before editing your spellbook."), true); return; }
        BlockPos table = null;
        if (ServerConfig.REQUIRE_TABLE.get()) {
            for (BlockPos pos : BlockPos.betweenClosed(p.blockPosition().offset(-8,-8,-8), p.blockPosition().offset(8,8,8))) {
                if (p.distanceToSqr(pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5) <= 64 && p.level().getBlockState(pos).is(BlockRegistry.INSCRIPTION_TABLE_BLOCK.get())) { table = pos.immutable(); break; }
            }
            if (table == null) { p.displayClientMessage(Component.literal("This server requires an inscription table within 8 blocks."), true); return; }
        }
        BlockPos found = table;
        NetworkHooks.openScreen(p, new SimpleMenuProvider((id,inv,player) -> new SpellMenu(id,inv,found), Component.literal("Spells")));
    }
    @Override public boolean stillValid(Player p) {
        return p == owner && p.isAlive() && !p.isSpectator() && (p.level().isClientSide || (ServerConfig.SPELL_TAB.get() && (!ServerConfig.REQUIRE_TABLE.get() || table != null && p.distanceToSqr(table.getX()+0.5,table.getY()+0.5,table.getZ()+0.5)<=64 && p.level().getBlockState(table).is(BlockRegistry.INSCRIPTION_TABLE_BLOCK.get()))));
    }
    public ISpellContainer spells() {
        var stack = getSpellBookSlot().getItem();
        return ISpellContainer.isSpellContainer(stack) ? ISpellContainer.get(stack) : ISpellContainer.create(0,false,false);
    }
    @Override public boolean clickMenuButton(Player p, int id) {
        if (!stillValid(p) || !getCarried().isEmpty() || MagicData.getPlayerMagicData(p).isCasting()) return false;
        int count = spells().getMaxSpellCount();
        if (id >= 0) {
            if (id >= count) return false;
            selection = id;
            return super.clickMenuButton(p,id);
        }
        if (id != -1 || !canInscribe(selection)) return false;
        boolean result = super.clickMenuButton(p,-1);
        super.setSelectedSpell(selection);
        broadcastChanges();
        return result;
    }
    public boolean canInscribe(int index) {
        var book = spells();
        var scroll = getScrollSlot().getItem();
        if (index < 0 || index >= book.getMaxSpellCount() || !book.getSpellAtIndex(index).equals(SpellData.EMPTY) || !(scroll.getItem() instanceof Scroll) || !ISpellContainer.isSpellContainer(scroll)) return false;
        var spell = ISpellContainer.get(scroll).getSpellAtIndex(0);
        return !spell.equals(SpellData.EMPTY) && book.mutableCopy().addSpellAtIndex(spell.getSpell(),spell.getLevel(),index,false);
    }
    @Override public void doInscription(int index) { if (canInscribe(index)) super.doInscription(index); }
    @Override public ItemStack quickMoveStack(Player p, int index) {
        if (index < 0 || index >= slots.size()) return ItemStack.EMPTY;
        ItemStack result = super.quickMoveStack(p,index);
        refreshSelection();
        return result;
    }
    @Override public void clicked(int slot, int button, ClickType type, Player p) {
        if (!stillValid(p)) return;
        refreshSelection();
        super.clicked(slot,button,type,p);
        refreshSelection();
    }
    private void refreshSelection() {
        if (selection >= spells().getMaxSpellCount()) selection = -1;
        super.setSelectedSpell(selection);
    }
    @Override public void removed(Player p) { if (!closed) { closed = true; super.removed(p); } }
}
