package dev.deepnight.client;
import dev.deepnight.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import java.util.*;
public final class PetScreen extends Screen {
    private final List<PetNetwork.Entry> entries;private EditBox search;private int page;private String query="";
    public PetScreen(PetNetwork.Menu menu){super(Component.literal("Summon a companion"));entries=menu.entries();}
    public static void open(PetNetwork.Menu menu){Minecraft.getInstance().setScreen(new PetScreen(menu));}
    @Override public boolean isPauseScreen(){return false;}
    @Override protected void init(){
        int x=width/2-150;
        search=new EditBox(font,x,48,300,20,Component.literal("Search pets"));search.setMaxLength(80);search.setHint(Component.literal("Search pets..."));search.setValue(query);
        search.setResponder(text->{query=text;page=0;rebuild();});addRenderableWidget(search);rebuild();
    }
    private List<PetNetwork.Entry> filtered(){String q=query.toLowerCase(Locale.ROOT);return entries.stream().filter(e->(e.name()+" "+e.id()).toLowerCase(Locale.ROOT).contains(q)).toList();}
    private int rows(){return Math.max(2,Math.min(9,(height-130)/25));}
    private void rebuild(){
        if(search==null)return;var list=filtered();int rows=rows();page=Math.max(0,Math.min(page,Math.max(0,(list.size()-1)/rows)));
        clearWidgets();addRenderableWidget(search);int x=width/2-150;
        for(int i=0;i<rows;i++){int index=page*rows+i;if(index>=list.size())break;var pet=list.get(index);
            addRenderableWidget(Button.builder(Component.literal(pet.name()),b->{PetNetwork.CHANNEL.sendToServer(new PetNetwork.Summon(pet.id()));onClose();}).bounds(x,80+i*25,300,22).build());}
        addRenderableWidget(Button.builder(Component.literal("Previous"),b->{page--;rebuild();}).bounds(x,height-37,94,20).build());
        addRenderableWidget(Button.builder(Component.literal("Close"),b->onClose()).bounds(x+103,height-37,94,20).build());
        addRenderableWidget(Button.builder(Component.literal("Next"),b->{page++;rebuild();}).bounds(x+206,height-37,94,20).build());
    }
    @Override public void render(GuiGraphics g,int mx,int my,float dt){
        renderBackground(g);g.fill(width/2-165,10,width/2+165,height-8,0xee121a2b);
        g.drawCenteredString(font,title,width/2,19,0xd7c4ff);g.drawCenteredString(font,"Already tamed • Level up together • 10-second recharge",width/2,34,0x9dbdc7);
        super.render(g,mx,my,dt);g.drawCenteredString(font,(page+1)+" / "+Math.max(1,(filtered().size()+rows()-1)/rows()),width/2,height-50,0xa5b1c5);
    }
}
