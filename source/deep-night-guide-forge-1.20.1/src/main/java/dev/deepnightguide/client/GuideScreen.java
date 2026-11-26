package dev.deepnightguide.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.glfw.GLFW;
import java.util.*;
import static dev.deepnightguide.client.GuideEntry.Category.*;

/** Scaled book canvas keeps the complete UI usable at Minecraft's larger GUI scales. */
public final class GuideScreen extends Screen {
    private static final int BG=0xff0b101b,PANEL=0xff121c2c,CARD=0xff1b2940,INK=0xffdde6f4,MUTED=0xff92a5bd,GOLD=0xffe7c787,TEAL=0xff85d9c0;
    private final GuideIndex index;
    private GuideEntry.Category category=ALL;
    private GuideEntry selected;
    private final List<GuideEntry> filtered=new ArrayList<>();
    private final List<Hit> hits=new ArrayList<>();
    private final Deque<GuideEntry> history=new ArrayDeque<>();
    private EditBox search;
    private String query="",modFilter="";
    private List<String> mods;
    private int listScroll,detailScroll,detailHeight,recipePage,age,completionFilter;
    private int matchingTotal,matchingCompleted;
    private int w,h,detailX=358,detailW;
    private float scale;
    private ItemStack hovered=ItemStack.EMPTY;
    private String hint="";
    private int mx,my;
    private boolean inBody;
    private record Hit(int x,int y,int width,int height,Runnable action) {boolean contains(double px,double py){return px>=x&&py>=y&&px<x+width&&py<y+height;}}
    public GuideScreen(GuideIndex index){super(Component.literal("The Deep Night Guide"));this.index=index;}
    @Override protected void init() {
        scale=Math.min(1f,Math.min(width/760f,height/460f));w=Math.round(width/scale);h=Math.round(height/scale);detailW=w-detailX-24;
        search=new EditBox(font,142,55,196,20,Component.literal("Search guide"));
        search.setMaxLength(150);search.setValue(query);search.setHint(Component.literal("Search name, ID, @mod"));
        search.setResponder(text->{query=text;listScroll=0;filter();});
        mods=index.entries.stream().map(e->e.id.getNamespace()).distinct().sorted().toList();
        filter();
    }
    private void filter() {
        if(selected!=null&&selected.lore)selected=index.byKey.get(selected.key);
        filtered.clear();matchingTotal=0;matchingCompleted=0;String q=query.toLowerCase(Locale.ROOT).trim();
        for(GuideEntry e:index.entries) {
            if(!e.matches(category) || !modFilter.isEmpty()&&!e.id.getNamespace().equals(modFilter))continue;
            boolean match=true;
            for(String term:q.split("\\s+"))if(!term.isEmpty()) {
                String hay=term.startsWith("@")?e.id.getNamespace()+" "+e.mod:e.name+" "+e.id+" "+e.mod;
                if(!hay.toLowerCase(Locale.ROOT).contains(term.startsWith("@")?term.substring(1):term)){match=false;break;}
            }
            if(!match)continue;
            matchingTotal++;if(done(e))matchingCompleted++;
            if(category.checklist()&&(completionFilter==1&&done(e)||completionFilter==2&&!done(e)))continue;
            filtered.add(e);
        }
        listScroll=Math.max(0,Math.min(listScroll,Math.max(0,filtered.size()-visibleRows())));
        if(selected==null&&!filtered.isEmpty())select(filtered.get(0),false);
    }
    private int visibleRows(){return Math.max(1,(h-160)/34);}
    private void select(GuideEntry entry,boolean remember) {
        if(remember&&selected!=null&&selected!=entry)history.push(selected);
        selected=entry;detailScroll=0;recipePage=0;
    }
    public void selectKey(String key) {GuideEntry e=index.byKey.get(key);if(e!=null)select(e,true);}
    public void setCategory(GuideEntry.Category c) {category=c;listScroll=0;filter();if(!filtered.isEmpty())select(filtered.get(0),false);else selected=null;}
    private boolean done(GuideEntry e){return index.progress.done(e.progressKey(category));}
    public void setCompletionFilter(int value){completionFilter=Math.floorMod(value,3);filter();}
    public void setQuery(String q){search.setValue(q);}
    public int resultCount(){return filtered.size();}
    @Override public boolean isPauseScreen(){return false;}
    @Override public void tick(){search.tick();if(++age%20==0)filter();}
    @Override public void removed(){index.progress.save();}
    @Override public void render(GuiGraphics g,int mouseX,int mouseY,float partialTick) {
        mx=Math.round(mouseX/scale);my=Math.round(mouseY/scale);hits.clear();hovered=ItemStack.EMPTY;hint="";
        g.pose().pushPose();g.pose().scale(scale,scale,1);
        g.fill(0,0,w,h,BG);g.fill(0,0,w,43,0xff101a2b);
        text(g,"THE DEEP NIGHT GUIDE",18,13,GOLD);text(g,"FIELD NOTES  /  COLLECTIONS  /  DISCOVERY",265,14,MUTED);
        button(g,w-62,9,44,22,"Close",this::onClose);
        g.fill(12,51,130,h-32,PANEL);text(g,"EXPLORE",23,65,MUTED);
        int y=85;
        int navStep=Math.min(27,(h-180)/GuideEntry.Category.values().length);
        for(GuideEntry.Category c:GuideEntry.Category.values()) {
            if(c==TAMING_LIST){y+=15;text(g,"COLLECTIONS",23,y,MUTED);y+=19;}
            final var next=c;
            if(category==c)g.fill(18,y-3,124,y+navStep-4,CARD);
            text(g,c.title,23,y+3,category==c?GOLD:INK);
            hits.add(new Hit(18,y-3,106,navStep-1,()->setCategory(next)));y+=navStep;
        }
        button(g,20,h-62,101,20,"Reload notes",()->{index.reloadNotes();index.rebuildRecipes();});
        g.fill(136,51,345,h-32,PANEL);
        search.render(g,mx,my,partialTick);
        String modLabel=modFilter.isBlank()?"All mods":modFilter;
        button(g,142,81,196,19,"Mod: "+font.plainSubstrByWidth(modLabel,160),()->{
            int p=mods.indexOf(modFilter)+1;modFilter=p>=mods.size()?"":mods.get(p);listScroll=0;filter();
        });
        if(mx>=142&&mx<=338&&my>=81&&my<=100)hint="Click to cycle mods. Search @modid for a specific mod.";
        String count=filtered.size()+" entries";
        if(category.checklist()) {
            count=matchingCompleted+" / "+matchingTotal+(category==BOSS_LIST?" defeated":" collected");
            button(g,264,105,74,17,switch(completionFilter){case 1->"Missing";case 2->"Done";default->"All";},()->{completionFilter=(completionFilter+1)%3;filter();});
        }
        text(g,count,144,109,MUTED);
        int rows=visibleRows();
        for(int i=0;i<rows&&i+listScroll<filtered.size();i++) {
            GuideEntry e=filtered.get(i+listScroll);int rowY=128+i*34;
            g.fill(142,rowY,338,rowY+31,e==selected?0xff2b3a52:CARD);
            icon(g,e.icon,148,rowY+7);
            text(g,font.plainSubstrByWidth(e.name,145),171,rowY+6,INK);
            text(g,font.plainSubstrByWidth(e.mod,126),171,rowY+18,MUTED);
            if(e.trackable())text(g,done(e)?"+":"-",327,rowY+19,done(e)?TEAL:MUTED);
            hits.add(new Hit(142,rowY,196,31,()->select(e,true)));
        }
        if(filtered.isEmpty())wrapped(g,"No matches. Try a name, registry ID, or @modid.",148,142,182,MUTED);
        scrollbar(g,341,128,h-40,listScroll,Math.max(1,filtered.size()),rows);
        g.fill(detailX-7,51,w-12,h-32,PANEL);
        if(selected!=null)renderDetails(g);else wrapped(g,"Your guide will fill with content from the installed modpack.",detailX+8,70,detailW,INK);
        text(g,font.plainSubstrByWidth(index.progress.error.isEmpty()?index.status:index.progress.error,w-35),18,h-20,MUTED);
        if(!hovered.isEmpty()) {
            List<Component> lines=hovered.getTooltipLines(minecraft.player,minecraft.options.advancedItemTooltips?TooltipFlag.Default.ADVANCED:TooltipFlag.Default.NORMAL);
            tooltip(g,lines.stream().map(Component::getString).toList());
        }else if(!hint.isBlank())tooltip(g,List.of(hint));
        g.pose().popPose();
    }
    private void renderDetails(GuiGraphics g) {
        int x=detailX+8;
        icon(g,selected.icon,x,66);
        text(g,font.plainSubstrByWidth(selected.name,detailW-38),x+26,66,GOLD);
        text(g,font.plainSubstrByWidth(selected.mod,detailW-38),x+26,80,MUTED);
        button(g,x,99,detailW-4,18,font.plainSubstrByWidth(selected.id.toString(),detailW-15),()->minecraft.keyboardHandler.setClipboard(selected.id.toString()));
        if(mx>=x&&mx<x+detailW&&my>=99&&my<117)hint="Click to copy the registry ID.";
        int btnY=123;
        boolean bossContext=selected.boss&&selected.progressKey(category).equals(selected.bossProgressKey());
        if(selected.trackable())button(g,x,btnY,Math.min(190,detailW-80),20,(done(selected)?"[+] ":"[ ] ")+(bossContext?(done(selected)?"Boss defeated":"Mark boss defeated"):(done(selected)?"Collected / tamed":"Mark collected / tamed")),()->{index.progress.toggle(selected.progressKey(category));filter();});
        if(!history.isEmpty())button(g,w-82,btnY,54,20,"Back",()->select(history.pop(),false));
        int top=153,bottom=h-43;
        g.enableScissor(Math.round((detailX)*scale),Math.round(top*scale),Math.round((w-20)*scale),Math.round(bottom*scale));
        int start=top+4-detailScroll;int y=start;inBody=true;
        GuideNotes.Note note=index.notes.get(selected.key);
        if(selected.lore) {
            for(var section:selected.loreSections.entrySet()) {
                y=heading(g,section.getKey(),x,y+5);
                for(String line:section.getValue())y=paragraph(g,line,x,y);
            }
            finishBody(g,start,y,top,bottom);return;
        }
        if(selected.entityType!=null) {
            y=heading(g,"Creature notes",x,y);
            y=paragraph(g,"Group: "+selected.entityType.getCategory().getName()+". "+(selected.tameable?"Included in Pets and the taming checklist.":note!=null&&Boolean.FALSE.equals(note.tameable)?"This creature cannot be tamed.":"Taming has not been confirmed for this entry."),x,y);
            if(selected.entityType.getCategory()==MobCategory.MISC)y=paragraph(g,"This registry also includes technical entities such as projectiles, vehicles and effects.",x,y);
            if(selected.boss) {
                y=heading(g,"Boss encounter",x,y+5);
                y=paragraph(g,"Tracked in Bosses and the Boss checklist. Defeating this entity is separate from taming it. "+selected.bossReason,x,y);
                if(selected.tameable) {
                    String otherKey=bossContext?selected.key:selected.bossProgressKey();
                    bodyButton(g,x,y,200,20,(index.progress.done(otherKey)?"[+] ":"[ ] ")+(bossContext?"Tamed / recruited":"Boss defeated"),top,bottom,()->{index.progress.toggle(otherKey);filter();});y+=27;
                }
            }
        }else {
            y=heading(g,"Crafting & processing",x,y);
            if(JeiBridge.recipes!=null) {
                bodyButton(g,x,y,124,20,"All recipes (JEI)",top,bottom,()->JeiBridge.recipes.accept(selected.icon));
                bodyButton(g,x+130,y,100,20,"Uses (JEI)",top,bottom,()->JeiBridge.uses.accept(selected.icon));
                y+=28;
            }
            List<Recipe<?>> recipes=index.recipes.getOrDefault(selected.icon.getItem(),List.of());
            if(recipes.isEmpty())y=paragraph(g,"No recipe with a static item result is exposed by this world's recipe manager. Check acquisition notes below. Special machine, fluid, brewing or scripted recipes may require the mod's own guide or JEI.",x,y);
            else {
                recipePage=Math.floorMod(recipePage,recipes.size());Recipe<?> recipe=recipes.get(recipePage);
                try {y=recipe(g,recipe,x,y,top,bottom);}
                catch(Exception ex){y=paragraph(g,"This recipe needs its mod's custom display. Use All recipes (JEI) when available. Recipe: "+recipe.getId(),x,y);}
                int controlY=y;
                bodyButton(g,x,controlY,37,18,"<",top,bottom,()->{recipePage=Math.floorMod(recipePage-1,recipes.size());});
                bodyButton(g,x+43,controlY,37,18,">",top,bottom,()->{recipePage=(recipePage+1)%recipes.size();});
                text(g,"Recipe "+(recipePage+1)+" / "+recipes.size(),x+88,controlY+5,MUTED);y+=28;
            }
            if(selected.icon.getMaxDamage()>0)y=paragraph(g,"Durability: "+selected.icon.getMaxDamage(),x,y);
            if(selected.icon.getItem() instanceof ArmorItem armour)y=paragraph(g,"Armour: "+armour.getDefense()+"   Toughness: "+armour.getToughness()+"   Slot: "+armour.getEquipmentSlot().getName(),x,y);
        }
        if(note!=null)for(var section:note.sections.entrySet()) {
            y=heading(g,section.getKey(),x,y+5);
            for(String line:section.getValue())y=paragraph(g,line,x,y);
        }
        if(note==null || !note.sections.containsKey("Where to find")) {
            y=heading(g,"Where to find",x,y+5);
            y=paragraph(g,selected.entityType==null?"No additional verified location is available for this entry. Recipes and imported loot sources are shown when the mod exposes them.":"Locations may be controlled by biome tags, structures, events or server configuration. Check any habitat or installed manual notes above.",x,y);
        }
        if(selected.tameable&&(note==null||!note.sections.containsKey("Taming"))) {
            y=heading(g,"Taming",x,y+5);
            y=paragraph(g,"This creature supports ownership or has a declared taming entry. Its exact taming steps may appear in the installed manual above; otherwise they are not documented here. Food for breeding is not automatically assumed to be taming food.",x,y);
        }
        if(selected.trackable()) {
            y=heading(g,"Your checklist",x,y+5);
            y=paragraph(g,selected.boss?"Boss defeats are checked from your personal entity-kill statistics when the guide opens, and periodically while open. Only kills credited to you count automatically. Mark victories manually if a boss uses custom or team mechanics. Boss progress is independent of taming. Unticking suppresses automatic re-checking.":"Use the checkbox to record progress manually. Weapons and armour are recorded when seen in your inventory. Owned standard pets and mounts are recorded when loaded nearby. Custom ownership systems can be checked manually. Unticking an entry keeps it unticked until you check it again.",x,y);
        }
        y=heading(g,"About this page",x,y+5);
        y=paragraph(g,"The catalog and recipes come from the running game. Location and manual notes describe installed mod data; datapacks, KubeJS and server settings can change acquisition. Reload notes after editing the guide's config. This guide does not reveal nearby coordinates or grant items.",x,y);
        finishBody(g,start,y,top,bottom);
    }
    private void finishBody(GuiGraphics g,int start,int y,int top,int bottom) {
        detailHeight=y-start;
        g.disableScissor();inBody=false;
        detailScroll=Math.min(detailScroll,Math.max(0,detailHeight-(bottom-top)));
        scrollbar(g,w-19,top,bottom,detailScroll,detailHeight,bottom-top);
    }
    private int recipe(GuiGraphics g,Recipe<?> recipe,int x,int y,int top,int bottom) {
        ResourceLocation type=ForgeRegistries.RECIPE_TYPES.getKey(recipe.getType());
        y=paragraph(g,type==null?recipe.getType().toString():type.toString(),x,y);
        boolean shaped=recipe instanceof ShapedRecipe;
        List<Ingredient> ingredients=recipe.getIngredients();
        int recipeWidth=shaped?((ShapedRecipe)recipe).getWidth():3;
        int cols=shaped?Math.max(3,recipeWidth):3;
        cols=Math.max(1,Math.min(cols,8));int rows=Math.max(1,(ingredients.size()+cols-1)/cols);
        if(shaped)rows=Math.max(3,((ShapedRecipe)recipe).getHeight());
        for(int i=0;i<rows*cols;i++) {
            int sx=x+(i%cols)*23,sy=y+(i/cols)*23;
            g.fill(sx,sy,sx+21,sy+21,0xff09111d);
            int ingredientIndex=shaped?(i%cols<recipeWidth?(i/cols)*recipeWidth+i%cols:-1):i;
            if(ingredientIndex>=0&&ingredientIndex<ingredients.size()) {
                ItemStack[] choices=ingredients.get(ingredientIndex).getItems();
                if(choices.length>0) {
                    ItemStack stack=choices[(age/30)%choices.length];
                    icon(g,stack,sx+3,sy+3);
                    if(sy>=top&&sy+21<=bottom)hits.add(new Hit(sx,sy,21,21,()->selectKey("item:"+ForgeRegistries.ITEMS.getKey(stack.getItem()))));
                }
            }
        }
        int outputX=x+cols*23+30;
        if(outputX+25<x+detailW){text(g,">",outputX-18,y+7,GOLD);icon(g,recipe.getResultItem(minecraft.level.registryAccess()),outputX,y+3);}
        y+=rows*23+7;
        String method=shaped?"Crafting table - keep this pattern.":recipe instanceof ShapelessRecipe?"Shapeless crafting - ingredients may go in any slots.":recipe instanceof AbstractCookingRecipe c?"Cooking time: "+(c.getCookingTime()/20f)+" seconds. XP: "+c.getExperience()+". Use the matching station; fuel is required for furnace-type recipes.":"Ingredients exposed by the recipe. Use its matching station; custom machines can have additional requirements.";
        y=paragraph(g,method,x,y);
        y=paragraph(g,"Alternatives cycle in each slot. Click an ingredient for its page.",x,y);
        return y;
    }
    private void icon(GuiGraphics g,ItemStack stack,int x,int y) {
        try{g.renderItem(stack,x,y);g.renderItemDecorations(font,stack,x,y);}catch(Exception ignored){text(g,"?",x+4,y+4,GOLD);}
        if(mx>=x&&mx<x+16&&my>=y&&my<y+16&&my>=51&&my<h-32&&(!inBody||my>=153&&my<h-43))hovered=stack;
    }
    private void text(GuiGraphics g,String value,int x,int y,int color){g.drawString(font,value,x,y,color,false);}
    private int heading(GuiGraphics g,String s,int x,int y){text(g,s.toUpperCase(Locale.ROOT),x,y,GOLD);return y+17;}
    private int paragraph(GuiGraphics g,String s,int x,int y){return wrapped(g,s,x,y,detailW-12,INK)+9;}
    private int wrapped(GuiGraphics g,String s,int x,int y,int width,int color) {
        for(FormattedCharSequence line:font.split(Component.literal(s),Math.max(30,width))){g.drawString(font,line,x,y,color,false);y+=12;}return y;
    }
    private void button(GuiGraphics g,int x,int y,int width,int height,String label,Runnable action) {
        boolean hover=mx>=x&&mx<x+width&&my>=y&&my<y+height;
        g.fill(x,y,x+width,y+height,hover?0xff344966:CARD);text(g,font.plainSubstrByWidth(label,width-8),x+4,y+(height-8)/2,hover?GOLD:INK);
        hits.add(new Hit(x,y,width,height,action));
    }
    private void bodyButton(GuiGraphics g,int x,int y,int width,int height,String label,int top,int bottom,Runnable action) {
        if(y>=top&&y+height<=bottom)button(g,x,y,width,height,label,action);
    }
    private void scrollbar(GuiGraphics g,int x,int top,int bottom,int offset,int total,int visible) {
        if(total<=visible)return;int span=bottom-top,thumb=Math.max(12,span*visible/Math.max(1,total));
        int pos=top+(span-thumb)*offset/Math.max(1,total-visible);g.fill(x,top,x+2,bottom,0xff223149);g.fill(x,pos,x+2,pos+thumb,GOLD);
    }
    private void tooltip(GuiGraphics g,List<String> lines) {
        int tw=Math.min(280,w-30);List<FormattedCharSequence> wrap=new ArrayList<>();for(String s:lines)wrap.addAll(font.split(Component.literal(s),tw-14));
        int th=wrap.size()*11+12;int x=Math.min(mx+13,w-tw-5),y=Math.max(5,Math.min(my+13,h-th-5));
        g.pose().pushPose();g.pose().translate(0,0,500);g.fill(x,y,x+tw,y+th,0xff080c15);g.renderOutline(x,y,tw,th,0xff657c9c);
        for(var line:wrap){g.drawString(font,line,x+7,y+6,INK,false);y+=11;}g.pose().popPose();
    }
    @Override public boolean mouseClicked(double x,double y,int button) {
        x/=scale;y/=scale;
        if(button==0&&search.mouseClicked(x,y,button)){search.setFocused(true);return true;}search.setFocused(false);
        if(button==0)for(int i=hits.size()-1;i>=0;i--)if(hits.get(i).contains(x,y)){hits.get(i).action.run();return true;}
        return false;
    }
    @Override public boolean mouseScrolled(double x,double y,double delta) {
        if(x/scale<350){listScroll=Math.max(0,Math.min(Math.max(0,filtered.size()-visibleRows()),listScroll-(int)(delta*3)));}
        else detailScroll=Math.max(0,Math.min(Math.max(0,detailHeight-(h-196)),detailScroll-(int)(delta*32)));return true;
    }
    @Override public boolean charTyped(char c,int modifiers){return search.charTyped(c,modifiers);}
    @Override public boolean keyPressed(int key,int scan,int modifiers) {
        if(key==GLFW.GLFW_KEY_ESCAPE){onClose();return true;}
        if(hasControlDown()&&key==GLFW.GLFW_KEY_F){search.setFocused(true);search.setHighlightPos(0);search.setCursorPosition(search.getValue().length());return true;}
        if(search.isFocused()&&search.keyPressed(key,scan,modifiers))return true;
        if(!search.isFocused()&&ClientEvents.OPEN.matches(key,scan)){onClose();return true;}
        if(key==GLFW.GLFW_KEY_DOWN||key==GLFW.GLFW_KEY_UP) {
            int next=filtered.indexOf(selected)+(key==GLFW.GLFW_KEY_DOWN?1:-1);
            if(next>=0&&next<filtered.size()){select(filtered.get(next),false);listScroll=Math.max(0,Math.min(next,Math.max(listScroll,next-visibleRows()+1)));}return true;
        }
        if(key==GLFW.GLFW_KEY_PAGE_DOWN||key==GLFW.GLFW_KEY_PAGE_UP){detailScroll=Math.max(0,Math.min(Math.max(0,detailHeight-(h-196)),detailScroll+(key==GLFW.GLFW_KEY_PAGE_DOWN?180:-180)));return true;}
        return super.keyPressed(key,scan,modifiers);
    }
}
