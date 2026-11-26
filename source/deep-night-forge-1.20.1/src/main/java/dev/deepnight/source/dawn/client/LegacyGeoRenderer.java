package dev.deepnight.source.dawn.client;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
/** Signature bridge for the original renderer's additional passes and beams. */
public class LegacyGeoRenderer<T extends LivingEntity & GeoEntity> extends GeoEntityRenderer<T> {
 private T sourceEntity;
 private RenderType sourceType;
 private MultiBufferSource sourceBuffers;
 private float sourceTick;
 private boolean sourceRerender;
 public LegacyGeoRenderer(EntityRendererProvider.Context context,GeoModel<T> model){super(context,model);}
 @Override protected float getDeathMaxRotation(T entityLivingBaseIn) { return 0; }
 public void renderEarly(T entity,PoseStack poses,float tick,MultiBufferSource buffers,VertexConsumer vertices,int light,int overlay,float r,float g,float b,float a){}
 public void render(BakedGeoModel model,T entity,float tick,RenderType type,PoseStack poses,MultiBufferSource buffers,VertexConsumer vertices,int light,int overlay,float r,float g,float b,float a){super.actuallyRender(poses,entity,model,type,buffers,vertices,false,tick,light,overlay,r,g,b,a);}
 @Override public void actuallyRender(PoseStack poses,T entity,BakedGeoModel model,RenderType type,MultiBufferSource buffers,VertexConsumer vertices,boolean rerender,float tick,int light,int overlay,float r,float g,float b,float a){sourceEntity=entity;sourceType=type;sourceBuffers=buffers;sourceTick=tick;sourceRerender=rerender;render(model,entity,tick,type,poses,buffers,vertices,light,overlay,r,g,b,a);}
 public void renderRecursively(software.bernie.geckolib.cache.object.GeoBone bone,PoseStack poses,VertexConsumer vertices,int light,int overlay,float r,float g,float b,float a){super.renderRecursively(poses,sourceEntity,bone,sourceType,sourceBuffers,vertices,sourceRerender,sourceTick,light,overlay,r,g,b,a);}
}
