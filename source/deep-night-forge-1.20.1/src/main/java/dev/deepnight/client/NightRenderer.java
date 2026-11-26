package dev.deepnight.client;

import dev.deepnight.*;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public final class NightRenderer<T extends LivingEntity & GeoEntity & NightVisual> extends GeoEntityRenderer<T> {
    public NightRenderer(EntityRendererProvider.Context c){super(c,new Model<>());shadowRadius=0.8f;}
    private static final class Model<E extends LivingEntity & GeoEntity & NightVisual> extends GeoModel<E>{
        @Override public ResourceLocation getModelResource(E e){return DeepNight.id("geo/"+e.species().id+".geo.json");}
        @Override public ResourceLocation getTextureResource(E e){return DeepNight.id("textures/entity/"+e.species().id+".png");}
        @Override public ResourceLocation getAnimationResource(E e){return DeepNight.id("animations/"+e.species().id+".animation.json");}
        @Override public void applyMolangQueries(E e,double animationTime){
            super.applyMolangQueries(e,animationTime);
            double tongue=1;
            // GeckoLib 4 treats v.* and variable.* as separate names; match the supplied clip.
            software.bernie.geckolib.core.molang.MolangParser.INSTANCE.setValue("v.tongue_scale",()->tongue);
            software.bernie.geckolib.core.molang.MolangParser.INSTANCE.setValue("query.head_pitch",e::getXRot);
        }
    }
    @Override public void preRender(PoseStack poses,T entity,BakedGeoModel model,MultiBufferSource buffers,VertexConsumer buffer,boolean rerender,float tick,int light,int overlay,float r,float g,float b,float a){
        scaleWidth=scaleHeight=entity.species().scale*(entity.isBaby()?0.5f:1f);
        float offset=entity.species().lift;
        poses.translate(0,offset*(entity.isBaby()?0.5f:1f),0);
        super.preRender(poses,entity,model,buffers,buffer,rerender,tick,light,overlay,r,g,b,a);
    }
}
