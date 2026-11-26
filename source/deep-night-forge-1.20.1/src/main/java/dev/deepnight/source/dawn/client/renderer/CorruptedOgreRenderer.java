package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.CorruptedOgreModel;
import dev.deepnight.source.dawn.entity.CorruptedOgreEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import dev.deepnight.source.dawn.client.LegacyGeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;


public class CorruptedOgreRenderer extends LegacyGeoRenderer<CorruptedOgreEntity> {
   protected final RenderType glowTexture = RenderType.eyes(new ResourceLocation("simple_mobs", "textures/ogre_glow.png"));
   protected final GeoRenderLayer<CorruptedOgreEntity> glowLayer = new GeoRenderLayer<CorruptedOgreEntity>(this) {
      public RenderType getRenderType(ResourceLocation texture) {
         return CorruptedOgreRenderer.this.glowTexture;
      }

      public void render(PoseStack poseStack, CorruptedOgreEntity entity, software.bernie.geckolib.cache.object.BakedGeoModel bakedModel, RenderType originalType, MultiBufferSource buffer, VertexConsumer originalBuffer, float partialTicks, int packedLight, int packedOverlay) {
         RenderType renderType = this.getRenderType(null);
         VertexConsumer ivertexbuilder = buffer.getBuffer(renderType);
         GeoModel provider = this.getGeoModel();
         this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity)),poseStack,buffer,entity,renderType,ivertexbuilder,partialTicks,15728640,OverlayTexture.NO_OVERLAY,1,1,1,1);
      }
   };
   protected final GeoRenderLayer<CorruptedOgreEntity> transparentLayer = new GeoRenderLayer<CorruptedOgreEntity>(this) {
      public void render(PoseStack poseStack, CorruptedOgreEntity entity, software.bernie.geckolib.cache.object.BakedGeoModel bakedModel, RenderType originalType, MultiBufferSource buffer, VertexConsumer originalBuffer, float partialTicks, int packedLight, int packedOverlay) {
         RenderType renderType = this.getRenderer().getRenderType(entity,this.getRenderer().getTextureLocation(entity),buffer,partialTicks);
         VertexConsumer ivertexbuilder = buffer.getBuffer(renderType);
         GeoModel provider = this.getGeoModel();
         this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity)),poseStack,buffer,entity,renderType,ivertexbuilder,partialTicks,15728640,OverlayTexture.NO_OVERLAY,1,1,1,1);
      }
   };

   public CorruptedOgreRenderer(Context context) {
      super(context, new CorruptedOgreModel());
      this.shadowRadius = 1.0F;
      this.addRenderLayer(this.glowLayer);
   }

   protected float getDeathMaxRotation(CorruptedOgreEntity entityLivingBaseIn) {
      return 0.0F;
   }
}
