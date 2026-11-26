package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.ElementalAttack2Model;
import dev.deepnight.source.dawn.entity.ElementalAttack2Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import dev.deepnight.source.dawn.client.LegacyGeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;


public class ElementalAttack2Renderer extends LegacyGeoRenderer<ElementalAttack2Entity> {
   protected final GeoRenderLayer<ElementalAttack2Entity> glowLayer = new GeoRenderLayer<ElementalAttack2Entity>(this) {
      public void render(PoseStack poseStack, ElementalAttack2Entity entity, software.bernie.geckolib.cache.object.BakedGeoModel bakedModel, RenderType originalType, MultiBufferSource buffer, VertexConsumer originalBuffer, float partialTicks, int packedLight, int packedOverlay) {
         RenderType renderType = null;
         if (entity.tickCount > 1) {
            int tick = entity.tickCount;
            if (tick % 105 < 5) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_glow_1.png"));
            } else if (tick % 105 < 10) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_glow_2.png"));
            } else if (tick % 105 < 15) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_glow_3.png"));
            } else if (tick % 105 < 20) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_glow_4.png"));
            } else if (tick % 105 < 24) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_glow_5.png"));
            } else if (tick % 105 < 28) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_glow_6.png"));
            } else if (tick % 105 < 32) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_glow_7.png"));
            } else if (tick % 105 < 36) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_8.png"));
            } else if (tick % 105 < 40) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_9.png"));
            } else if (tick % 105 < 44) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_10.png"));
            } else if (tick % 105 < 48) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_9.png"));
            } else if (tick % 105 < 52) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_10.png"));
            } else if (tick % 105 < 56) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_9.png"));
            } else if (tick % 105 < 60) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_10.png"));
            } else if (tick % 105 < 64) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_9.png"));
            } else if (tick % 105 < 68) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_10.png"));
            } else if (tick % 105 < 72) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_9.png"));
            } else if (tick % 105 < 76) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_10.png"));
            } else if (tick % 105 < 80) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_9.png"));
            } else if (tick % 105 < 85) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_10.png"));
            } else if (tick % 105 < 90) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_9.png"));
            } else if (tick % 105 < 93) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_11.png"));
            } else {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_12.png"));
            }

            VertexConsumer ivertexbuilder = buffer.getBuffer(renderType);
            GeoModel provider = this.getGeoModel();
            this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity)),poseStack,buffer,entity,renderType,ivertexbuilder,partialTicks,15728640,OverlayTexture.NO_OVERLAY,1,1,1,1);
         }
      }
   };
   protected final GeoRenderLayer<ElementalAttack2Entity> transparentLayer = new GeoRenderLayer<ElementalAttack2Entity>(this) {
      public void render(PoseStack poseStack, ElementalAttack2Entity entity, software.bernie.geckolib.cache.object.BakedGeoModel bakedModel, RenderType originalType, MultiBufferSource buffer, VertexConsumer originalBuffer, float partialTicks, int packedLight, int packedOverlay) {
         RenderType renderType = null;
         if (entity.tickCount > 1) {
            int tick = entity.tickCount;
            if (tick % 105 < 36) {
               renderType = RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/a.png"));
            } else if (tick % 105 < 40) {
               renderType = RenderType.entityTranslucent(
                  new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_transparent_9.png")
               );
            } else if (tick % 105 < 44) {
               renderType = RenderType.entityTranslucent(
                  new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_transparent_10.png")
               );
            } else if (tick % 105 < 48) {
               renderType = RenderType.entityTranslucent(
                  new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_transparent_9.png")
               );
            } else if (tick % 105 < 52) {
               renderType = RenderType.entityTranslucent(
                  new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_transparent_10.png")
               );
            } else if (tick % 105 < 56) {
               renderType = RenderType.entityTranslucent(
                  new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_transparent_9.png")
               );
            } else if (tick % 105 < 60) {
               renderType = RenderType.entityTranslucent(
                  new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_transparent_10.png")
               );
            } else if (tick % 105 < 64) {
               renderType = RenderType.entityTranslucent(
                  new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_transparent_9.png")
               );
            } else if (tick % 105 < 68) {
               renderType = RenderType.entityTranslucent(
                  new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_transparent_10.png")
               );
            } else if (tick % 105 < 72) {
               renderType = RenderType.entityTranslucent(
                  new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_transparent_9.png")
               );
            } else if (tick % 105 < 76) {
               renderType = RenderType.entityTranslucent(
                  new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_transparent_10.png")
               );
            } else if (tick % 105 < 80) {
               renderType = RenderType.entityTranslucent(
                  new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_transparent_9.png")
               );
            } else if (tick % 105 < 85) {
               renderType = RenderType.entityTranslucent(
                  new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_transparent_10.png")
               );
            } else if (tick % 105 < 90) {
               renderType = RenderType.entityTranslucent(
                  new ResourceLocation("simple_mobs:textures/entities/elemental_attack2/elemental_attack2_transparent_9.png")
               );
            } else {
               renderType = RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/a.png"));
            }

            VertexConsumer ivertexbuilder = buffer.getBuffer(renderType);
            GeoModel provider = this.getGeoModel();
            this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity)),poseStack,buffer,entity,renderType,ivertexbuilder,partialTicks,15728640,OverlayTexture.NO_OVERLAY,1,1,1,1);
         }
      }
   };

   public ElementalAttack2Renderer(Context context) {
      super(context, new ElementalAttack2Model());
      this.addRenderLayer(this.glowLayer);
      this.addRenderLayer(this.transparentLayer);
   }

   public void render(ElementalAttack2Entity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
      if (entity.tickCount > 1) {
         super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
      }
   }
}
