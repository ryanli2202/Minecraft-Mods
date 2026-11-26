package dev.nordui.client;

import dev.nordui.VaultNetwork;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;

/** Vanilla inventory menu/slot identities preserve crafting, equipment rules and network semantics. */
public final class NordInventoryScreen extends AbstractContainerScreen<InventoryMenu> {
    private final Player player;
    private final int[][] original=new int[46][2];
    public boolean equipment;
    private boolean compact;
    private int bagX,bagY,step,hotbarY;
    private Button inventoryTab,equipmentTab,vault,curios,spells;
    public NordInventoryScreen(Player p) {
        super(p.inventoryMenu,p.getInventory(),Component.literal("NORD"));player=p;
        for(int i=0;i<menu.slots.size();i++){original[i][0]=menu.slots.get(i).x;original[i][1]=menu.slots.get(i).y;}
    }
    @Override protected void init() {
        compact=width<550||height<326||dev.nordui.compat.UiCompat.compactForJei();imageWidth=compact?308:530;imageHeight=compact?222:302;super.init();
        int ty=topPos+(compact?26:36),bh=compact?17:20;
        inventoryTab=addRenderableWidget(Button.builder(Component.literal("INVENTORY"),b->setEquipment(false)).bounds(leftPos+10,ty,compact?91:107,bh).build());
        equipmentTab=addRenderableWidget(Button.builder(Component.literal("EQUIPMENT"),b->setEquipment(true)).bounds(leftPos+(compact?105:123),ty,compact?91:107,bh).build());
        vault=addRenderableWidget(Button.builder(Component.literal(compact?"VAULT / 999":"PERSONAL VAULT / 999"),b->VaultNetwork.request()).bounds(leftPos+(compact?200:236),ty,compact?98:169,bh).build());
        vault.setTooltip(net.minecraft.client.gui.components.Tooltip.create(Component.literal("999 private storage slots. Put down your cursor item first.")));
        if(!compact)addRenderableWidget(Button.builder(Component.literal("RECIPES / MODS"),b->ClientEvents.openVanillaInventory()).bounds(leftPos+411,ty,107,bh).tooltip(net.minecraft.client.gui.components.Tooltip.create(Component.literal("Original inventory, recipe book and other mods' inventory buttons"))).build());
        else addRenderableWidget(Button.builder(Component.literal("R"),b->ClientEvents.openVanillaInventory()).bounds(leftPos+279,topPos+205,19,14).tooltip(net.minecraft.client.gui.components.Tooltip.create(Component.literal("Original inventory, recipe book and other mods' inventory buttons"))).build());
        if(dev.nordui.compat.CuriosCompat.loaded())curios=addRenderableWidget(Button.builder(Component.literal("CURIOS / ACCESSORIES"),b->dev.nordui.compat.CuriosCompat.open()).bounds(leftPos+(compact?187:354),topPos+(compact?183:232),compact?106:152,compact?17:20).build());
        if(dev.nordui.compat.IronsCompat.loaded()&&ClientConfig.SPELLS.get())spells=addRenderableWidget(Button.builder(Component.literal("SPELLS"),b->VaultNetwork.spells()).bounds(leftPos+(compact?197:356),topPos+(compact?8:10),compact?58:80,15).build());
        addRenderableWidget(Button.builder(Component.literal("UI"),b->minecraft.setScreen(new CompatibilityScreen(this))).bounds(leftPos+(compact?263:474),topPos+(compact?8:10),compact?32:42,15).build());
        bagX=compact?8:139;bagY=compact?132:125;step=compact?18:22;hotbarY=compact?190:209;
        for(int i=9;i<36;i++)place(i,bagX+(i-9)%9*step,bagY+(i-9)/9*step);
        for(int i=36;i<45;i++)place(i,bagX+(i-36)*step,hotbarY);
        for(int i=5;i<9;i++)place(i,compact?8:22,(compact?50:84)+(i-5)*(compact?18:24));
        place(45,compact?30:22,compact?98:188);
        for(int i=1;i<5;i++)place(i,(compact?93:139)+(i-1)%2*20,(compact?62:65)+(i-1)/2*20);
        place(0,compact?148:209,compact?72:75);
        setEquipment(equipment);
    }
    private void place(int i,int x,int y){menu.slots.get(i).x=x;menu.slots.get(i).y=y;}
    public void setEquipment(boolean value){equipment=value;if(inventoryTab!=null){inventoryTab.setMessage(Component.literal(value?"INVENTORY":"[ INVENTORY ]"));equipmentTab.setMessage(Component.literal(value?"[ EQUIPMENT ]":"EQUIPMENT"));}if(curios!=null)curios.visible=value;}
    @Override protected void containerTick(){super.containerTick();vault.active=menu.getCarried().isEmpty();if(curios!=null)curios.active=menu.getCarried().isEmpty();if(spells!=null)spells.active=menu.getCarried().isEmpty();}
    @Override public void removed(){super.removed();for(int i=0;i<menu.slots.size();i++)place(i,original[i][0],original[i][1]);}
    @Override protected void renderBg(GuiGraphics g,float partial,int mx,int my) {
        int x=leftPos,y=topPos;
        Theme.panel(g,x,y,imageWidth,imageHeight);
        Theme.text(g,"T H E  D E E P  N I G H T",x+12,y+(compact?10:14),Theme.WHITE);
        if(!compact){Theme.well(g,x+12,y+65,108,195);Theme.well(g,x+343,y+65,175,195);Theme.rule(g,x+14,y+274,502);}
        else Theme.well(g,x+181,y+49,117,156);
        for(var slot:menu.slots)Theme.slot(g,x+slot.x,y+slot.y);
        InventoryScreen.renderEntityInInventoryFollowsMouse(g,x+(compact?60:81),y+(compact?108:197),compact?25:40,
            x+(compact?60:81)-mx,y+(compact?80:150)-my,player);
        Theme.text(g,"CRAFTING",x+(compact?93:139),y+(compact?50:56),Theme.MUTED);
        Theme.text(g,">",x+(compact?137:192),y+(compact?77:80),Theme.GOLD);
        Theme.text(g,"CARRIED ITEMS",x+bagX,y+bagY-12,Theme.MUTED);
        if(!compact){Theme.text(g,"QUICK SLOTS",x+bagX,y+hotbarY-12,Theme.MUTED);Theme.text(g,"WORN GEAR",x+22,y+70,Theme.MUTED);Theme.text(g,"ARMOUR  "+player.getArmorValue(),x+22,y+226,Theme.WHITE);Theme.text(g,"LEVEL   "+player.experienceLevel,x+22,y+242,Theme.MUTED);}
        renderDetails(g,x+(compact?188:354),y+(compact?57:77),compact?104:153);
        Theme.text(g,compact?"Shift-click: move    E / Esc: close":"SHIFT + CLICK  transfer     /     E / ESC  close     /     V  vault     /     F8  visual toggle",x+12,y+imageHeight-13,Theme.MUTED);
    }
    private void renderDetails(GuiGraphics g,int x,int y,int w) {
        if(equipment){
            Theme.text(g,"EQUIPMENT",x,y,Theme.GOLD);Theme.rule(g,x,y+14,w-5);
            String[] values={"Armour: "+player.getArmorValue(),"Toughness: "+(int)player.getAttributeValue(Attributes.ARMOR_TOUGHNESS),"Base attack: "+String.format(java.util.Locale.ROOT,"%.1f",attackDamage()),"Health: "+(int)player.getHealth()+" / "+(int)player.getMaxHealth(),"Level: "+player.experienceLevel};
            for(int i=0;i<values.length;i++)Theme.text(g,values[i],x,y+24+i*17,Theme.WHITE);
            if(dev.nordui.compat.CuriosCompat.loaded()){
                var accessories=dev.nordui.compat.CuriosCompat.entries(player);
                Theme.text(g,"ACCESSORIES  /  "+accessories.size()+" slots",x,y+(compact?107:113),Theme.MUTED);
                if(!compact){int shown=0;for(var entry:accessories){if(shown==6)break;int px=x+shown*23;Theme.slot(g,px,y+132);g.renderItem(entry.item(),px,y+132);shown++;}}
            }else g.drawWordWrap(font,Theme.component(compact?"Equip armour in the slots beside your character.":"Place gear in the slots beside your character. Base attack includes your held weapon, before enchantments and effects."),x,y+(compact?107:129),w,Theme.MUTED);
            return;
        }
        ItemStack selected=hoveredSlot!=null&&!hoveredSlot.getItem().isEmpty()?hoveredSlot.getItem():player.getMainHandItem();
        Theme.text(g,"ITEM INSPECTION",x,y,Theme.GOLD);Theme.rule(g,x,y+14,w-5);
        if(selected.isEmpty()){g.drawWordWrap(font,Theme.component("Hover over an item to inspect it. Your private vault is one click away."),x,y+35,w,Theme.MUTED);return;}
        g.pose().pushPose();g.pose().translate(x+w/2-16,y+25,0);g.pose().scale(2,2,1);g.renderItem(selected,0,0);g.pose().popPose();
        g.drawWordWrap(font,selected.getHoverName(),x,y+67,w,Theme.WHITE);
        Theme.text(g,"Count: "+selected.getCount(),x,y+97,Theme.MUTED);
        if(selected.isDamageableItem())Theme.text(g,"Durability: "+(selected.getMaxDamage()-selected.getDamageValue()),x,y+112,Theme.MUTED);
        if(!compact)Theme.text(g,selected.isEnchanted()?"ENCHANTED":"",x,y+144,Theme.GOLD);
    }
    private double attackDamage(){
        double base=player.getAttributeBaseValue(Attributes.ATTACK_DAMAGE),addition=0,multiplyBase=0,multiplyTotal=1;
        for(var m:player.getMainHandItem().getAttributeModifiers(net.minecraft.world.entity.EquipmentSlot.MAINHAND).get(Attributes.ATTACK_DAMAGE)){
            switch(m.getOperation()){case ADDITION->addition+=m.getAmount();case MULTIPLY_BASE->multiplyBase+=m.getAmount();case MULTIPLY_TOTAL->multiplyTotal*=1+m.getAmount();}
        }
        return (base+addition)*(1+multiplyBase)*multiplyTotal;
    }
    @Override protected void renderLabels(GuiGraphics g,int mx,int my) {}
    @Override public void render(GuiGraphics g,int mx,int my,float partial){renderBackground(g);super.render(g,mx,my,partial);renderTooltip(g,mx,my);}
}
