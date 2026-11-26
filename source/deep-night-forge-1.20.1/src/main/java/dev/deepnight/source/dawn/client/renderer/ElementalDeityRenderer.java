package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.ElementalDeityModel;
import dev.deepnight.source.dawn.entity.ElementalDeityEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import dev.deepnight.source.dawn.client.LegacyGeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;


public class ElementalDeityRenderer extends LegacyGeoRenderer<ElementalDeityEntity> {
   protected final GeoRenderLayer<ElementalDeityEntity> glowLayer = new GeoRenderLayer<ElementalDeityEntity>(this) {
      public void render(PoseStack poseStack, ElementalDeityEntity entity, software.bernie.geckolib.cache.object.BakedGeoModel bakedModel, RenderType originalType, MultiBufferSource buffer, VertexConsumer originalBuffer, float partialTicks, int packedLight, int packedOverlay) {
         RenderType renderType = null;
         if (entity.tickCount > 1) {
            int tick = entity.tickCount;
            if (tick % 20 < 4) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/elemental_deity/elemental_god_glow_1.png"));
            } else if (tick % 20 < 8) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/elemental_deity/elemental_god_glow_2.png"));
            } else if (tick % 20 < 12) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/elemental_deity/elemental_god_glow_3.png"));
            } else if (tick % 20 < 16) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/elemental_deity/elemental_god_glow_4.png"));
            } else {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/elemental_deity/elemental_god_glow_5.png"));
            }

            VertexConsumer ivertexbuilder = buffer.getBuffer(renderType);
            GeoModel provider = this.getGeoModel();
            this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity)),poseStack,buffer,entity,renderType,ivertexbuilder,partialTicks,15728640,OverlayTexture.NO_OVERLAY,1,1,1,1);
         }
      }
   };

   public ElementalDeityRenderer(Context context) {
      super(context, new ElementalDeityModel());
      this.addRenderLayer(this.glowLayer);
   }

   protected float getDeathMaxRotation(ElementalDeityEntity entityLivingBaseIn) {
      return 0.0F;
   }

   public void render(ElementalDeityEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
      if (entity.tickCount > 1) {
         super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
      }
   }
}
