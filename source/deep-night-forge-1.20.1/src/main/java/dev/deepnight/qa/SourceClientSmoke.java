package dev.deepnight.qa;
import dev.deepnight.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.GeckoLibCache;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import org.lwjgl.glfw.GLFW;
import java.nio.file.*;
import java.util.*;

@Mod.EventBusSubscriber(modid=DeepNight.ID,value=Dist.CLIENT)
public final class SourceClientSmoke {
 private static final boolean PREVIEW=Boolean.getBoolean("deepnight.bossPreview");
 private static boolean started,opened;private static int ticks;
 private static final StringBuilder report=new StringBuilder();
 private static void finish(String error){
  try{Files.createDirectories(Path.of("../verification"));Files.writeString(Path.of("../verification/"+(PREVIEW?"boss-preview-report.txt":"source-client-report.txt")),(error==null?"PASS\n":"FAIL: "+error+"\n")+report);}catch(Exception ex){ex.printStackTrace();}
  Minecraft.getInstance().stop();
 }
 @SubscribeEvent public static void tick(TickEvent.ClientTickEvent e){
  if(!Boolean.getBoolean("deepnight.smokeTest")||e.phase!=TickEvent.Phase.END)return;
  Minecraft mc=Minecraft.getInstance();
  try{
   if(++ticks>5000)throw new IllegalStateException("Client test timed out");
   if(!started&&mc.screen instanceof TitleScreen&&mc.getOverlay()==null){
    started=true;GLFW.glfwHideWindow(mc.getWindow().getWindow());mc.options.pauseOnLostFocus=false;mc.options.renderDistance().set(2);mc.options.simulationDistance().set(5);mc.options.guiScale().set(2);mc.resizeDisplay();
    var rules=new GameRules();rules.getRule(GameRules.RULE_DOMOBSPAWNING).set(false,null);
    mc.createWorldOpenFlows().createFreshLevel("source-combat-qa-"+System.currentTimeMillis(),new LevelSettings("Source Combat QA",GameType.CREATIVE,false,Difficulty.NORMAL,true,rules,WorldDataConfiguration.DEFAULT),new WorldOptions(418,false,false),access->access.registryOrThrow(Registries.WORLD_PRESET).getHolderOrThrow(WorldPresets.FLAT).value().createWorldDimensions());
   }
   if(started&&!opened&&mc.level!=null&&mc.player!=null&&mc.player.tickCount>30){opened=true;if(Boolean.getBoolean("deepnight.bossMenuTest"))BossMenuClientSmoke.start();else mc.setScreen(new Gallery());}
  }catch(Throwable ex){ex.printStackTrace();finish(ex.toString());}
 }
 private static final class Gallery extends Screen {
  private final List<Entity> entities=new ArrayList<>();private int ticks,page;private boolean done;
  Gallery(){super(Component.literal("Source boss render verification"));}
  @Override protected void init(){
   if(!entities.isEmpty())return;
   for(int i=0;i<Species.BOSS_COUNT;i++)entities.add(DeepNight.HOSTILES.get(Species.boss(i)).get().create(minecraft.level));
   if(!PREVIEW)entities.add(DeepNight.HOSTILES.get(Species.DAWN_ELEMENTAL_DEITY).get().create(minecraft.level));
   for(Species s:new Species[]{Species.DAWN_FIRE_DRAGON,Species.DAWN_ICE_DRAGON,Species.DAWN_LIGHTNING_DRAGON})entities.add(DeepNight.PETS.get(s).get().create(minecraft.level));
   if(!PREVIEW)for(var entry:ForgeRegistries.ENTITY_TYPES.getEntries())if(entry.getKey().location().getNamespace().equals("simple_mobs")||Set.of("mobosses_demon_king_lightning","mobosses_forest_guardian_aura").contains(entry.getKey().location().getPath()))entities.add(entry.getValue().create(minecraft.level));
   for(int i=0;i<entities.size();i++){
    var entity=entities.get(i);if(entity==null)throw new IllegalStateException("Null source entity");entity.setId(60000+i);entity.tickCount=50;entity.setPos(minecraft.player.position());
    if(entity instanceof Mob mob)mob.setNoAi(true);
    var renderer=minecraft.getEntityRenderDispatcher().getRenderer(entity);
    if(renderer==null)throw new IllegalStateException("Missing renderer "+entity.getType());
    if(renderer instanceof GeoEntityRenderer<?> geo&&entity instanceof GeoEntity anim){
     var model=(software.bernie.geckolib.model.GeoModel)geo.getGeoModel();var location=model.getAnimationResource(anim);var clips=GeckoLibCache.getBakedAnimations().get(location);
     if(clips==null||clips.animations().isEmpty())throw new IllegalStateException("Missing source animations: "+location);
     try(var reader=minecraft.getResourceManager().openAsReader(location)){
      var keys=net.minecraft.util.GsonHelper.parse(reader).getAsJsonObject("animations").keySet();
      if(!clips.animations().keySet().containsAll(keys))throw new IllegalStateException("Source clips failed to bake: "+location);
     }catch(java.io.IOException ex){throw new RuntimeException(ex);}
     model.getBakedModel(model.getModelResource(anim));
     var texture=model.getTextureResource(anim);if(minecraft.getResourceManager().getResource(texture).isEmpty())throw new IllegalStateException("Missing texture: "+texture);
     report.append("Loaded ").append(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType())).append(" / ").append(clips.animations().size()).append(" source clips\n");
    }
   }
  }
  @Override public boolean isPauseScreen(){return false;}
  @Override public void tick(){
   if(done)return;
   if(++ticks==15){
    try{if(page<4)try(var image=Screenshot.takeScreenshot(minecraft.getMainRenderTarget())){image.writeToFile(Path.of("../verification/"+(PREVIEW?"boss-preview-":"source-boss-gallery-")+(page+1)+".png"));}}
    catch(Exception ex){throw new RuntimeException(ex);}
    page++;ticks=0;
    if(page*6>=entities.size()){done=true;report.append("Rendered ").append(entities.size()).append(" source boss/helper entities.\n");finish(null);}
   }
  }
  @Override public void render(GuiGraphics g,int mx,int my,float partial){
   try{
    g.fill(0,0,width,height,0xff0b1423);g.drawCenteredString(font,PREVIEW?"THE DEEP NIGHT 1.2  /  BOSS GALLERY  /  "+(page+1)+" OF 4":"Original DawnCraft + Mo'Bosses renderers — page "+(page+1),width/2,12,0xffffff);
    int cw=width/3,ch=(height-40)/2;
    for(int i=0;i<6;i++){
     int index=page*6+i;if(index>=entities.size())break;Entity entity=entities.get(index);entity.tickCount++;
     int x=i%3*cw,y=35+i/3*ch;g.fill(x+4,y,x+cw-4,y+ch-4,0xff1b2940);g.enableScissor(x+5,y+1,x+cw-5,y+ch-27);
     int scale=Math.max(2,(int)(Math.min(ch-40,90)/Math.max(entity.getBbHeight(),2)));
     if(PREVIEW&&index<Species.BOSS_COUNT){
      float factor=switch(Species.boss(index)){
       case DAWN_DRAGON_LORD,DAWN_FIRE_DRAGON->0.45f;
       case DAWN_ICE_DRAGON,DAWN_LIGHTNING_DRAGON->0.55f;
       case DAWN_ENT,MOBOSSES_NIL->1.0f;
       case MOBOSSES_DIVINE_DRAGON->0.95f;
       default->1.4f;
      };scale=Math.max(2,Math.round(scale*factor));
     }
     if(entity instanceof LivingEntity living)InventoryScreen.renderEntityInInventoryFollowsMouse(g,x+cw/2,y+ch-38,scale,-45,0,living);
     else{g.pose().pushPose();g.pose().translate(x+cw/2,y+ch-38,300);g.pose().scale(scale,-scale,scale);minecraft.getEntityRenderDispatcher().render(entity,0,0,0,0,partial,g.pose(),g.bufferSource(),15728880);g.flush();g.pose().popPose();}
     g.disableScissor();
     if(PREVIEW&&index<Species.BOSS_COUNT){
      Species species=Species.boss(index);String title=species==Species.DAWN_SKELETONLORD?"Skeleton Overlord":species.title;
      g.drawCenteredString(font,(index+1)+". "+title,x+cw/2,y+ch-25,0xffffff);
      g.drawCenteredString(font,species.id.startsWith("dawn_")?"DAWNCRAFT":"MO'BOSSES",x+cw/2,y+ch-13,0xff9babca);
     }else g.drawCenteredString(font,ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).getPath(),x+cw/2,y+ch-20,0xffffff);
    }
   }catch(Throwable ex){ex.printStackTrace();done=true;finish(ex.toString());}
  }
 }
}
