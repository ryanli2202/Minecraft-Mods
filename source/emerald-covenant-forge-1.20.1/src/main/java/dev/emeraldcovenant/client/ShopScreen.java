package dev.emeraldcovenant.client;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.emeraldcovenant.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import org.lwjgl.glfw.GLFW;
import java.util.*;

public final class ShopScreen extends Screen {
    private static final int W=720,H=460,GOLD=0xE3C587,INK=0xE2DDD0,MUTED=0x97AA9D,GREEN=0x8DE4AE;
    private static final ResourceLocation FRAME=new ResourceLocation(Covenant.ID,"textures/gui/shop_frame.png"),PORTRAITS=new ResourceLocation(Covenant.ID,"textures/gui/merchants.png");
    private Category category=Category.WEAPONS;
    private List<Offer> filtered=List.of();
    private Offer selected;
    private String query="";
    private int offset,quantity=1;
    private boolean reverse,unlockedOnly;
    private double scale,originX,originY;
    private EditBox search;
    private GuildButton buy,sort,availability,previous,next,minus,plus;
    private final List<GuildButton> tabs=new ArrayList<>();
    public ShopScreen(){super(Component.literal("The Emerald Covenant"));}
    @Override protected void init() {
        scale=Math.min(1.25,Math.min((width-8.0)/W,(height-8.0)/H));originX=(width-W*scale)/2;originY=(height-H*scale)/2;
        tabs.clear();
        for(var c:Category.values()){
            var tab=button(174+c.ordinal()*87,73,84,24,c.label,()->setCategory(c));tabs.add(tab);
        }
        search=new EditBox(font,177,113,260,18,Component.literal("Search items or @modid"));search.setMaxLength(100);search.setHint(Component.literal("Search items or @modid..."));search.setValue(query);search.setResponder(q->{query=q;offset=0;refresh();});addRenderableWidget(search);
        sort=button(447,110,116,24,"",()->{reverse=!reverse;offset=0;refresh();});
        availability=button(569,110,126,24,"All stock",()->{unlockedOnly=!unlockedOnly;offset=0;refresh();});
        previous=button(175,379,30,20,"<",()->{offset=Math.max(0,offset-6);});
        next=button(471,379,30,20,">",()->{offset=Math.min(Math.max(0,filtered.size()-6),offset+6);});
        minus=button(526,340,26,21,"-",()->quantity=Math.max(1,quantity-1));
        plus=button(658,340,26,21,"+",()->quantity=Math.min(selected!=null&&selected.bearingBit()!=0?1:64,quantity+1));
        buy=button(526,379,158,26,"Purchase",()->{if(selected!=null)ClientShop.buy(selected,quantity);});
        button(26,384,126,25,"Offer bearings",()->ShopNetwork.toServer(new ShopNetwork.Action(ClientShop.token,1)));
        button(651,29,24,20,"X",this::onClose);
        refresh();
    }
    private GuildButton button(int x,int y,int w,int h,String label,Runnable action){return addRenderableWidget(new GuildButton(x,y,w,h,label,action));}
    public void setCategory(Category c){category=c;offset=0;selected=null;quantity=1;refresh();}
    public void setQuery(String q){query=q;if(search!=null)search.setValue(q);refresh();}
    public void selectKey(String key){selected=filtered.stream().filter(e->e.key().equals(key)).findFirst().orElse(null);if(selected!=null)offset=Math.max(0,filtered.indexOf(selected)-2);}
    public int resultCount(){return filtered.size();}
    public Offer selected(){return selected;}
    public void refresh() {
        String[] words=query.trim().toLowerCase(Locale.ROOT).split("\\s+");
        var list=new ArrayList<Offer>();
        for(Offer e:ClientShop.offers) {
            if(e.category()!=category||(unlockedOnly&&!e.unlocked(ClientShop.mask)))continue;
            String text=(safeName(e)+" "+e.key()+" "+e.source()).toLowerCase(Locale.ROOT);boolean match=true;
            for(String word:words)if(!word.isEmpty()&&!(word.startsWith("@")?e.source().toLowerCase(Locale.ROOT).contains(word.substring(1)):text.contains(word))){match=false;break;}
            if(match)list.add(e);
        }
        list.sort(reverse?Catalogue.comparator().reversed():Catalogue.comparator());filtered=list;
        if(selected==null||!filtered.contains(selected)){selected=filtered.isEmpty()?null:filtered.get(0);quantity=1;}
        offset=Math.min(offset,Math.max(0,filtered.size()-6));
        for(int i=0;i<tabs.size();i++)tabs.get(i).selected=i==category.ordinal();
        if(sort!=null)sort.setMessage(Component.literal(reverse?"Order: reversed":"Order: default"));
        if(availability!=null)availability.setMessage(Component.literal(unlockedOnly?"Unlocked only":"All stock"));
    }
    private static String safeName(Offer e){try{return e.stack().getHoverName().getString();}catch(Exception ex){return e.key();}}
    @Override public boolean isPauseScreen(){return false;}
    @Override public void tick(){search.tick();if(minecraft.player==null||!minecraft.player.isAlive())onClose();}
    @Override public void onClose(){ShopNetwork.toServer(new ShopNetwork.Action(ClientShop.token,0));super.onClose();}
    @Override public void render(GuiGraphics g,int mouseX,int mouseY,float partial) {
        renderBackground(g);int mx=(int)((mouseX-originX)/scale),my=(int)((mouseY-originY)/scale);
        g.pose().pushPose();g.pose().translate(originX,originY,0);g.pose().scale((float)scale,(float)scale,1);
        RenderSystem.enableBlend();g.blit(FRAME,0,0,W,H,0,0,384,256,384,256);
        g.drawString(font,"THE EMERALD COVENANT",44,29,GOLD,false);
        g.drawString(font,"A guild of wandering merchants",44,45,MUTED,false);
        g.renderItem(Items.EMERALD.getDefaultInstance(),520,28);
        int wallet=minecraft.player==null?ClientShop.emeralds:Transactions.emeralds(minecraft.player.getInventory());
        g.drawString(font,wallet+" emeralds",540,32,GREEN,false);
        g.fill(163,74,164,410,0x885E674D);
        int p=category.portrait,px=(p%2)*64,py=(p/2)*64;
        g.fill(25,76,154,205,0xFFA48A51);g.blit(PORTRAITS,27,78,125,125,px,py,64,64,128,128);
        g.drawString(font,category.merchant,26,218,GOLD,false);
        wrapped(g,category.title,26,233,129,MUTED,2);
        String quote=switch(category){
            case WEAPONS->"A keen edge is a promise. Choose the steel that will carry your name.";
            case ARMOUR->"Walk into the dark wearing something worthy of the return.";
            case SPELLS,SPELLBOOKS->"Every spell has a price. Wisdom is knowing when to cast it.";
            case FOOD->"Sit a moment, traveller. A warm meal need not cost a fortune.";
            case BEARINGS->"Buy a bearing here. Then click Offer bearings, or hold it and right-click, to unlock its stock forever.";
        };
        wrapped(g,quote,26,265,127,INK,7);
        g.drawString(font,"GUILD OATHS",26,337,GOLD,false);
        String[] oath={"Forge","Ward","Lore","Crown"};
        for(int i=0;i<4;i++)g.drawString(font,((ClientShop.mask&(1<<i))!=0?"+ ":"- ")+oath[i],26+(i%2)*67,351+(i/2)*13,(ClientShop.mask&(1<<i))!=0?GREEN:MUTED,false);
        String sortLabel=category.sort;
        if(reverse)sortLabel=sortLabel.contains("highest")?sortLabel.replace("highest","lowest"):sortLabel.replace("lowest","highest");
        g.drawString(font,sortLabel,176,142,MUTED,false);
        Offer hover=null;
        for(int row=0;row<6;row++) {
            int index=offset+row;if(index>=filtered.size())break;Offer e=filtered.get(index);int y=156+row*36;
            boolean hit=mx>=175&&mx<502&&my>=y&&my<y+33,chosen=selected!=null&&e.key().equals(selected.key());
            g.fill(175,y,502,y+33,chosen?0xD0394A36:hit?0xD026382E:0xC0101D19);
            g.fill(175,y,177,y+33,chosen?0xFFD8B96F:0xFF405442);
            g.renderItem(e.stack(),184,y+8);
            String name=font.plainSubstrByWidth(safeName(e),211);
            g.drawString(font,name,207,y+5,e.unlocked(ClientShop.mask)?INK:0xA89F90,false);
            String stat=switch(category){case WEAPONS->e.damage()==0?"Ranged / variable damage":fmt(e.damage())+" attack damage";case ARMOUR->fmt(e.setProtection())+" set protection  /  "+fmt(e.protection())+" piece";case SPELLS->e.spellLevel()>0?"Spell level "+e.spellLevel():"Spell item";case SPELLBOOKS->rarity(e.rarity());case FOOD->"Provisions";case BEARINGS->(ClientShop.mask&e.bearingBit())!=0?"Oath sworn":"Permanent unlock";};
            if(!e.unlocked(ClientShop.mask))stat="Locked  /  "+stat;
            g.drawString(font,font.plainSubstrByWidth(stat,214),207,y+19,MUTED,false);
            String cost=String.valueOf(e.price());g.drawString(font,cost,484-font.width(cost),y+12,GREEN,false);
            if(hit)hover=e;
        }
        if(filtered.isEmpty())wrapped(g,ClientShop.loading?"Opening the merchant ledger...":"No wares match your search. Try another merchant or search term.",189,190,294,MUTED,4);
        previous.active=offset>0;next.active=offset+6<filtered.size();
        g.drawCenteredString(font,(filtered.isEmpty()?"0":(offset+1)+"-"+Math.min(offset+6,filtered.size()))+" of "+filtered.size()+" wares",338,385,MUTED);
        g.fill(514,154,695,409,0xD00C1713);g.fill(514,154,515,409,0xFF72633F);
        if(selected!=null)details(g,wallet);
        else buy.active=false;
        plus.active=selected!=null&&quantity<(selected.bearingBit()!=0?1:64);minus.active=selected!=null&&quantity>1;
        wrapped(g,ClientShop.message,176,416,511,ClientShop.success?MUTED:0xE5AA91,2);
        super.render(g,mx,my,partial);
        g.pose().popPose();
        if(hover!=null&&mx<205)try{g.renderTooltip(font,hover.stack(),mouseX,mouseY);}catch(Exception ignored){}
    }
    private void details(GuiGraphics g,int wallet) {
        Offer e=selected;
        g.pose().pushPose();g.pose().translate(587,168,0);g.pose().scale(2,2,1);g.renderItem(e.stack(),0,0);g.pose().popPose();
        wrapped(g,safeName(e),526,209,158,GOLD,3);
        g.drawString(font,rarity(e.rarity()),526,247,rarityColor(e.rarity()),false);
        g.drawString(font,font.plainSubstrByWidth(e.source(),158),526,260,MUTED,false);
        String detail=switch(e.category()){
            case WEAPONS -> e.damage()==0?"Ranged damage varies by ammunition and charge.":fmt(e.damage())+" base attack damage";
            case ARMOUR -> fmt(e.setProtection())+" total set protection\n"+fmt(e.protection())+" protection on this piece";
            case SPELLS -> e.spellLevel()>0?"Level "+e.spellLevel()+" spell scroll":"Spell item";
            case SPELLBOOKS -> "A vessel for your magic.";
            case FOOD -> "Affordable provisions for your journey.";
            case BEARINGS -> switch(e.bearingBit()){case 1->"Unlocks advanced weapons.";case 2->"Unlocks advanced armour.";case 4->"Unlocks advanced spells & books.";default->"Unlocks rare & legendary stock.";};
        };
        wrapped(g,detail,526,277,158,INK,3);
        String lock=e.unlocked(ClientShop.mask)?"Available":"Needs: "+e.missingBearings(ClientShop.mask);
        wrapped(g,lock,526,313,158,e.unlocked(ClientShop.mask)?GREEN:0xD9AD87,2);
        g.drawCenteredString(font,"Quantity: "+quantity,605,347,INK);
        long total=(long)e.price()*quantity;g.drawCenteredString(font,total+" emeralds",605,366,GREEN);
        boolean sworn=e.bearingBit()!=0&&(ClientShop.mask&e.bearingBit())!=0;
        int missingCount=Integer.bitCount(e.required()&~ClientShop.mask);
        String lockedLabel=missingCount==1?"Needs "+e.missingBearings(ClientShop.mask):"Needs "+missingCount+" bearings";
        buy.setMessage(Component.literal(ClientShop.pending?"Trading...":sworn?"Oath sworn":!e.unlocked(ClientShop.mask)?lockedLabel:wallet<total?"Need more emeralds":"Purchase"));
        buy.setTooltip(e.unlocked(ClientShop.mask)?null:net.minecraft.client.gui.components.Tooltip.create(Component.literal("Still required: "+e.missingBearings(ClientShop.mask)+" bell bearing(s). Previously offered bearings remain unlocked.")));
        buy.active=!ClientShop.pending&&!ClientShop.loading&&e.unlocked(ClientShop.mask)&&wallet>=total&&!sworn;
    }
    private void wrapped(GuiGraphics g,String text,int x,int y,int w,int color,int max){var lines=font.split(Component.literal(text),w);for(int i=0;i<Math.min(max,lines.size());i++)g.drawString(font,lines.get(i),x,y+i*11,color,false);}
    private static String fmt(double n){return Math.abs(n-Math.rint(n))<.01?String.valueOf((int)n):String.format(Locale.ROOT,"%.1f",n);}
    private static String rarity(int r){return new String[]{"Common","Uncommon","Rare","Epic","Legendary"}[Math.max(0,Math.min(4,r))];}
    private static int rarityColor(int r){return new int[]{INK,GREEN,0x89B8EC,0xC99BF1,GOLD}[Math.max(0,Math.min(4,r))];}
    @Override public boolean mouseClicked(double x,double y,int button) {
        double mx=(x-originX)/scale,my=(y-originY)/scale;
        if(button==0&&mx>=175&&mx<502&&my>=156&&my<372){int row=(int)(my-156)/36,index=offset+row;if(index<filtered.size()){selected=filtered.get(index);quantity=1;return true;}}
        return super.mouseClicked(mx,my,button);
    }
    @Override public boolean mouseReleased(double x,double y,int button){return super.mouseReleased((x-originX)/scale,(y-originY)/scale,button);}
    @Override public boolean mouseDragged(double x,double y,int button,double dx,double dy){return super.mouseDragged((x-originX)/scale,(y-originY)/scale,button,dx/scale,dy/scale);}
    @Override public void mouseMoved(double x,double y){super.mouseMoved((x-originX)/scale,(y-originY)/scale);}
    @Override public boolean mouseScrolled(double x,double y,double delta){double mx=(x-originX)/scale;if(mx>=174&&mx<=504){offset=Math.max(0,Math.min(Math.max(0,filtered.size()-6),offset-(int)Math.signum(delta)*3));return true;}return super.mouseScrolled((x-originX)/scale,(y-originY)/scale,delta);}
    @Override public boolean keyPressed(int key,int scan,int modifiers) {
        if(key==GLFW.GLFW_KEY_ESCAPE){onClose();return true;}
        if(!search.isFocused()&&ClientEvents.OPEN.matches(key,scan)){onClose();return true;}
        if(!search.isFocused()&&(key==GLFW.GLFW_KEY_DOWN||key==GLFW.GLFW_KEY_UP)&&!filtered.isEmpty()){
            int n=Math.max(0,Math.min(filtered.size()-1,filtered.indexOf(selected)+(key==GLFW.GLFW_KEY_DOWN?1:-1)));selected=filtered.get(n);quantity=1;
            if(n<offset)offset=n;if(n>=offset+6)offset=n-5;return true;
        }
        return super.keyPressed(key,scan,modifiers);
    }
}
