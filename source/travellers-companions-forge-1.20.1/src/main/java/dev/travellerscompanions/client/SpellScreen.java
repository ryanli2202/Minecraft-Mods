package dev.travellerscompanions.client;

import dev.travellerscompanions.*;
import io.redspace.ironsspellbooks.api.item.IScroll;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

@net.minecraftforge.fml.common.Mod.EventBusSubscriber(modid=TravellersCompanions.ID,value=net.minecraftforge.api.distmarker.Dist.CLIENT)
public final class SpellScreen extends AbstractContainerScreen<SpellMenu> {
    public SpellScreen(SpellMenu menu,Inventory inventory,Component title){super(menu,inventory,title);imageWidth=176;imageHeight=158;inventoryLabelY=64;}
    @Override protected void init(){super.init();addRenderableWidget(Button.builder(Component.literal("Gear"),b->{if(menu.companion!=null)CompanionNetwork.CHANNEL.sendToServer(new CompanionNetwork.Action(13,menu.companion.kind().ordinal(),menu.companion.getId()));}).bounds(leftPos+138,topPos+4,31,14).build());}
    @Override protected void renderBg(GuiGraphics g,float partial,int mx,int my){
        g.fill(leftPos-3,topPos-3,leftPos+179,topPos+161,0xFFBDA673);g.fill(leftPos,topPos,leftPos+176,topPos+158,0xFF232E33);
        for(var slot:menu.slots){int x=leftPos+slot.x,y=topPos+slot.y;g.fill(x-1,y-1,x+17,y+17,slot.index<6?0xFF9E95CF:0xFF71817D);g.fill(x,y,x+16,y+16,0xFF111B20);}
    }
    @Override protected void renderLabels(GuiGraphics g,int mx,int my){g.drawString(font,"Leonardo's spells",8,7,0xEAD8AE,false);g.drawString(font,"Mana "+menu.mana()+" / "+menu.maxMana(),8,22,0xA9A1EA,false);g.drawString(font,"Equip scrolls to choose spells",8,54,0xB7C6C9,false);g.drawString(font,playerInventoryTitle,8,65,0xB7C6C9,false);}
    @Override public void render(GuiGraphics g,int mx,int my,float partial){renderBackground(g);super.render(g,mx,my,partial);renderTooltip(g,mx,my);}
    @net.minecraftforge.eventbus.api.SubscribeEvent public static void spellTooltip(net.minecraftforge.event.entity.player.ItemTooltipEvent event){
        if(!(net.minecraft.client.Minecraft.getInstance().screen instanceof SpellScreen)||!(event.getItemStack().getItem() instanceof IScroll))return;
        event.getToolTip().add(Component.literal(CompanionSpells.accepts(event.getItemStack())?"Leonardo: equip in a spell slot; scroll is retained.":"Leonardo cannot cast this spell. Keep it in your inventory.").withStyle(CompanionSpells.accepts(event.getItemStack())?net.minecraft.ChatFormatting.GREEN:net.minecraft.ChatFormatting.GRAY));
    }
}
