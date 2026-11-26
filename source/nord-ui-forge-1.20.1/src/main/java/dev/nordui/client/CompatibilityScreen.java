package dev.nordui.client;

import dev.nordui.compat.UiCompat;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ForgeConfigSpec;

/** In-game switches, plus runtime detection so the user can see what is actually installed. */
public final class CompatibilityScreen extends Screen {
    private final Screen parent;
    private int page;
    private static final int PAGES=3;
    public CompatibilityScreen(Screen parent) { super(Component.literal("Interface compatibility")); this.parent=parent; }
    @Override protected void init() {
        int x=width/2-148,y=40;
        addRenderableWidget(Button.builder(Component.literal("<"),b->{page=(page+PAGES-1)%PAGES;rebuildWidgets();}).bounds(x,18,24,18).build());
        addRenderableWidget(Button.builder(Component.literal(">"),b->{page=(page+1)%PAGES;rebuildWidgets();}).bounds(x+272,18,24,18).build());
        if(page==0) {
            addRenderableWidget(Button.builder(Component.literal("HUD owner: "+ClientConfig.HUD_OWNER.get()),b->{var values=ClientConfig.HudOwner.values();ClientConfig.HUD_OWNER.set(values[(ClientConfig.HUD_OWNER.get().ordinal()+1)%values.length]);save();}).bounds(x,y,296,18).tooltip(net.minecraft.client.gui.components.Tooltip.create(Component.literal("AUTO uses RPG-HUD when installed. To force DEEP_NIGHT, disable RPG-HUD's rendering in its settings first. OTHER_MOD retains original HUD events."))).build());
            toggle("Deep Night inventory",ClientConfig.INVENTORY,x,y+21);toggle("Original mod screens",ClientConfig.MOD_SCREENS,x,y+42,true);
            toggle("JEI sidebar space",ClientConfig.JEI_SPACE,x,y+63);toggle("Automatic HUD spacing",ClientConfig.AUTO_SPACING,x,y+84);
            toggle("Iron's mana theme",ClientConfig.IRONS_MANA,x,y+105);toggle("Deep Night hotbar",ClientConfig.HOTBAR,x,y+126);
            toggle("Dock Iron's spells left",ClientConfig.IRONS_DOCK,x,y+147);
        } else if(page==1) {
            adjust("Compass height",ClientConfig.COMPASS_Y,x,y,8,-200,400);
            adjust("Survival bar lift",ClientConfig.HUD_LIFT,x,y+24,8,0,250);
            adjust("Hotbar horizontal",ClientConfig.HOTBAR_X,x,y+48,8,-500,500);
            adjust("Hotbar vertical",ClientConfig.HOTBAR_Y,x,y+72,8,-300,300);
            toggle("Title replacement",ClientConfig.TITLE,x,y+98);toggle("Compass",ClientConfig.COMPASS,x,y+119);toggle("Spell tab",ClientConfig.SPELLS,x,y+140);
        } else {
            var entries=UiCompat.detected();
            int rows=Math.max(1,(height-100)/21);int max=Math.max(0,(entries.size()-1)/rows);detectedPage=Math.min(detectedPage,max);
            for(int i=0;i<rows&&i+detectedPage*rows<entries.size();i++){
                var entry=entries.get(i+detectedPage*rows);
                addRenderableWidget(Button.builder(Component.literal(entry.name()+"  /  detected"),b->{}).bounds(x,y+i*21,296,18).tooltip(net.minecraft.client.gui.components.Tooltip.create(Component.literal(entry.behavior()))).build());
            }
            if(max>0)addRenderableWidget(Button.builder(Component.literal("More detected mods  "+(detectedPage+1)+" / "+(max+1)),b->{detectedPage=(detectedPage+1)%(max+1);rebuildWidgets();}).bounds(x,height-49,296,18).build());
        }
        addRenderableWidget(Button.builder(Component.literal("DONE"),b->onClose()).bounds(x,height-25,296,20).build());
    }
    private int detectedPage;
    private void toggle(String name,ForgeConfigSpec.BooleanValue value,int x,int y){toggle(name,value,x,y,false);}
    private void toggle(String name,ForgeConfigSpec.BooleanValue value,int x,int y,boolean inverted){
        addRenderableWidget(Button.builder(Component.literal(name+": "+((value.get()!=inverted)?"ON":"OFF")),b->{value.set(!value.get());save();}).bounds(x,y,296,18).build());
    }
    private void adjust(String label,ForgeConfigSpec.IntValue value,int x,int y,int step,int min,int max){
        addRenderableWidget(Button.builder(Component.literal(label+": "+value.get()+"  -"),b->{value.set(Math.max(min,value.get()-step));save();}).bounds(x,y,240,20).build());
        addRenderableWidget(Button.builder(Component.literal("+"),b->{value.set(Math.min(max,value.get()+step));save();}).bounds(x+244,y,52,20).build());
    }
    private void save(){ClientConfig.SPEC.save();rebuildWidgets();}
    @Override public void onClose(){minecraft.setScreen(parent);}
    @Override public void render(GuiGraphics g,int mx,int my,float partial){
        renderBackground(g);Theme.centered(g,new String[]{"UI COMPATIBILITY","HUD PLACEMENT","DETECTED UI MODS"}[page],width/2,23,Theme.GOLD);
        if(page==2&&UiCompat.detected().isEmpty())Theme.centered(g,"No supported optional UI mods detected",width/2,65,Theme.MUTED);
        super.render(g,mx,my,partial);
    }
}
