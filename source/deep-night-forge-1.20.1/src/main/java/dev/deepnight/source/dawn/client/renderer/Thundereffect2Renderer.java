package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.Thundereffect2Model;
import dev.deepnight.source.dawn.entity.Thundereffect2Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import dev.deepnight.source.dawn.client.LegacyGeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;


public class Thundereffect2Renderer extends LegacyGeoRenderer<Thundereffect2Entity> {
   protected final GeoRenderLayer<Thundereffect2Entity> glowLayer = new GeoRenderLayer<Thundereffect2Entity>(this) {
      public void render(PoseStack poseStack, Thundereffect2Entity entity, software.bernie.geckolib.cache.object.BakedGeoModel bakedModel, RenderType originalType, MultiBufferSource buffer, VertexConsumer originalBuffer, float partialTicks, int packedLight, int packedOverlay) {
         RenderType renderType = null;
         if (entity.tickCount > 1) {
            int tick = entity.tickCount;
            if (tick % 75 < 4) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/thunder_effect2/thunder_effect2_glow_1.png"));
            } else if (tick % 75 < 8) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/thunder_effect2/thunder_effect2_glow_2.png"));
            } else if (tick % 75 < 16) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/thunder_effect2/thunder_effect2_glow_3.png"));
            } else if (tick % 75 < 20) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/thunder_effect2/thunder_effect2_glow_4.png"));
            } else if (tick % 75 < 24) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/thunder_effect2/thunder_effect2_glow_5.png"));
            } else if (tick % 75 < 51) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/thunder_effect2/thunder_effect2_glow_3.png"));
            } else if (tick % 75 < 53) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/thunder_effect2/thunder_effect2_glow_6.png"));
            } else if (tick % 75 < 55) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/thunder_effect2/thunder_effect2_glow_7.png"));
            } else if (tick % 75 < 57) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/thunder_effect2/thunder_effect2_glow_8.png"));
            } else if (tick % 75 < 60) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/thunder_effect2/thunder_effect2_glow_9.png"));
            } else if (tick % 75 < 63) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/thunder_effect2/thunder_effect2_glow_10.png"));
            } else {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/a.png"));
            }

            VertexConsumer ivertexbuilder = buffer.getBuffer(renderType);
            GeoModel provider = this.getGeoModel();
            this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity)),poseStack,buffer,entity,renderType,ivertexbuilder,partialTicks,15728640,OverlayTexture.NO_OVERLAY,1,1,1,1);
         }
      }
   };
   protected final GeoRenderLayer<Thundereffect2Entity> transparentLayer = new GeoRenderLayer<Thundereffect2Entity>(this) {
      public void render(PoseStack poseStack, Thundereffect2Entity entity, software.bernie.geckolib.cache.object.BakedGeoModel bakedModel, RenderType originalType, MultiBufferSource buffer, VertexConsumer originalBuffer, float partialTicks, int packedLight, int packedOverlay) {
         RenderType renderType = null;
         if (entity.tickCount > 1) {
            int tick = entity.tickCount;
            if (tick % 60 < 4) {
               renderType = RenderType.entityTranslucent(
                  new ResourceLocation("simple_mobs:textures/entities/thunder_effect2/thunder_effect2_transparent_1.png")
               );
            } else if (tick % 75 < 8) {
               renderType = RenderType.entityTranslucent(
                  new ResourceLocation("simple_mobs:textures/entities/thunder_effect2/thunder_effect2_transparent_2.png")
               );
            } else if (tick % 75 < 16) {
               renderType = RenderType.entityTranslucent(
                  new ResourceLocation("simple_mobs:textures/entities/thunder_effect2/thunder_effect2_transparent_3.png")
               );
            } else if (tick % 75 < 20) {
               renderType = RenderType.entityTranslucent(
                  new ResourceLocation("simple_mobs:textures/entities/thunder_effect2/thunder_effect2_transparent_4.png")
               );
            } else if (tick % 75 < 24) {
               renderType = RenderType.entityTranslucent(
                  new ResourceLocation("simple_mobs:textures/entities/thunder_effect2/thunder_effect2_transparent_5.png")
               );
            } else if (tick % 75 < 51) {
               renderType = RenderType.entityTranslucent(
                  new ResourceLocation("simple_mobs:textures/entities/thunder_effect2/thunder_effect2_transparent_3.png")
               );
            } else if (tick % 75 < 53) {
               renderType = RenderType.entityTranslucent(
                  new ResourceLocation("simple_mobs:textures/entities/thunder_effect2/thunder_effect2_transparent_6.png")
               );
            } else if (tick % 75 < 55) {
               renderType = RenderType.entityTranslucent(
                  new ResourceLocation("simple_mobs:textures/entities/thunder_effect2/thunder_effect2_transparent_7.png")
               );
            } else if (tick % 75 < 57) {
               renderType = RenderType.entityTranslucent(
                  new ResourceLocation("simple_mobs:textures/entities/thunder_effect2/thunder_effect2_transparent_8.png")
               );
            } else if (tick % 75 < 60) {
               renderType = RenderType.entityTranslucent(
                  new ResourceLocation("simple_mobs:textures/entities/thunder_effect2/thunder_effect2_transparent_9.png")
               );
            } else if (tick % 75 < 63) {
               renderType = RenderType.entityTranslucent(
                  new ResourceLocation("simple_mobs:textures/entities/thunder_effect2/thunder_effect2_transparent_10.png")
               );
            } else {
               renderType = RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/a.png"));
            }

            VertexConsumer ivertexbuilder = buffer.getBuffer(renderType);
            GeoModel provider = this.getGeoModel();
            this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity)),poseStack,buffer,entity,renderType,ivertexbuilder,partialTicks,15728640,OverlayTexture.NO_OVERLAY,1,1,1,1);
         }
      }
   };

   public Thundereffect2Renderer(Context context) {
      super(context, new Thundereffect2Model());
      this.addRenderLayer(this.glowLayer);
      this.addRenderLayer(this.transparentLayer);
   }

   public void render(Thundereffect2Entity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
      if (entity.tickCount > 1) {
         super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
      }
   }
}
