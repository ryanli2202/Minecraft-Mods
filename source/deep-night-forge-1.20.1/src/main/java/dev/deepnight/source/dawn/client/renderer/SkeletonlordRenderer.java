package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.SkeletonlordModel;
import dev.deepnight.source.dawn.entity.SkeletonlordEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import dev.deepnight.source.dawn.client.LegacyGeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;


public class SkeletonlordRenderer extends LegacyGeoRenderer<SkeletonlordEntity> {
   protected final RenderType transparentTexture = RenderType.entityTranslucent(new ResourceLocation("simple_mobs", "textures/skeleton_lord_transparent.png"));
   protected final RenderType glowTexture = RenderType.eyes(new ResourceLocation("simple_mobs", "textures/skeleton_lord_glow.png"));
   protected final GeoRenderLayer<SkeletonlordEntity> glowLayer = new GeoRenderLayer<SkeletonlordEntity>(this) {
      public RenderType getRenderType(ResourceLocation texture) {
         return SkeletonlordRenderer.this.glowTexture;
      }

      public void render(PoseStack poseStack, SkeletonlordEntity entity, software.bernie.geckolib.cache.object.BakedGeoModel bakedModel, RenderType originalType, MultiBufferSource buffer, VertexConsumer originalBuffer, float partialTicks, int packedLight, int packedOverlay) {
         RenderType renderType = this.getRenderType(null);
         VertexConsumer ivertexbuilder = buffer.getBuffer(renderType);
         GeoModel provider = this.getGeoModel();
         this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity)),poseStack,buffer,entity,renderType,ivertexbuilder,partialTicks,15728640,OverlayTexture.NO_OVERLAY,1,1,1,1);
      }
   };
   protected final GeoRenderLayer<SkeletonlordEntity> transparentLayer = new GeoRenderLayer<SkeletonlordEntity>(this) {
      public RenderType getRenderType(ResourceLocation texture) {
         return SkeletonlordRenderer.this.transparentTexture;
      }

      public void render(PoseStack poseStack, SkeletonlordEntity entity, software.bernie.geckolib.cache.object.BakedGeoModel bakedModel, RenderType originalType, MultiBufferSource buffer, VertexConsumer originalBuffer, float partialTicks, int packedLight, int packedOverlay) {
         RenderType renderType = this.getRenderType(null);
         VertexConsumer ivertexbuilder = buffer.getBuffer(renderType);
         GeoModel provider = this.getGeoModel();
         this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity)),poseStack,buffer,entity,renderType,ivertexbuilder,partialTicks,15728640,OverlayTexture.NO_OVERLAY,1,1,1,1);
      }
   };

   public SkeletonlordRenderer(Context context) {
      super(context, new SkeletonlordModel());
      this.shadowRadius = 0.4F;
      this.addRenderLayer(this.transparentLayer);
      this.addRenderLayer(this.glowLayer);
   }

   protected float getDeathMaxRotation(SkeletonlordEntity entityLivingBaseIn) {
      return 0.0F;
   }
}
