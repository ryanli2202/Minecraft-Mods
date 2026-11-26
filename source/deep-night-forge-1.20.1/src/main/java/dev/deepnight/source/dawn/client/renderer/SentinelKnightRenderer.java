package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import javax.annotation.Nullable;
import dev.deepnight.source.dawn.client.SentinelKnightModel;
import dev.deepnight.source.dawn.entity.SentinelKnightEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import dev.deepnight.source.dawn.client.LegacyGeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;


public class SentinelKnightRenderer extends LegacyGeoRenderer<SentinelKnightEntity> {
   protected final RenderType glowTexture = RenderType.eyes(new ResourceLocation("simple_mobs", "textures/sentinelknight_glow.png"));
   protected final GeoRenderLayer<SentinelKnightEntity> glowLayer = new GeoRenderLayer<SentinelKnightEntity>(this) {
      public RenderType getRenderType(ResourceLocation texture) {
         return SentinelKnightRenderer.this.glowTexture;
      }

      public void render(PoseStack poseStack, SentinelKnightEntity entity, software.bernie.geckolib.cache.object.BakedGeoModel bakedModel, RenderType originalType, MultiBufferSource buffer, VertexConsumer originalBuffer, float partialTicks, int packedLight, int packedOverlay) {
         RenderType renderType = this.getRenderType(null);
         VertexConsumer ivertexbuilder = buffer.getBuffer(renderType);
         GeoModel provider = this.getGeoModel();
         this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity)),poseStack,buffer,entity,renderType,ivertexbuilder,partialTicks,15728640,OverlayTexture.NO_OVERLAY,1,1,1,1);
      }
   };
   protected final GeoRenderLayer<SentinelKnightEntity> transparentLayer = new GeoRenderLayer<SentinelKnightEntity>(this) {
      public void render(PoseStack poseStack, SentinelKnightEntity entity, software.bernie.geckolib.cache.object.BakedGeoModel bakedModel, RenderType originalType, MultiBufferSource buffer, VertexConsumer originalBuffer, float partialTicks, int packedLight, int packedOverlay) {
         RenderType renderType = this.getRenderer().getRenderType(entity,this.getRenderer().getTextureLocation(entity),buffer,partialTicks);
         VertexConsumer ivertexbuilder = buffer.getBuffer(renderType);
         GeoModel provider = this.getGeoModel();
         this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity)),poseStack,buffer,entity,renderType,ivertexbuilder,partialTicks,15728640,OverlayTexture.NO_OVERLAY,1,1,1,1);
      }
   };

   public void render(
      BakedGeoModel model,
      SentinelKnightEntity animatable,
      float partialTicks,
      RenderType type,
      PoseStack matrixStackIn,
      @Nullable MultiBufferSource renderTypeBuffer,
      @Nullable VertexConsumer vertexBuilder,
      int packedLightIn,
      int packedOverlayIn,
      float red,
      float green,
      float blue,
      float alpha
   ) {
      this.renderEarly(animatable, matrixStackIn, partialTicks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
      if (renderTypeBuffer != null) {
         vertexBuilder = renderTypeBuffer.getBuffer(type);
      }

      this.renderRecursively(
         (GeoBone)model.topLevelBones().get(0),
         matrixStackIn,
         vertexBuilder,
         packedLightIn,
         OverlayTexture.pack(0, OverlayTexture.v(animatable.hurtTime > 0 && animatable.getPersistentData().getBoolean("Blocking"))),
         red,
         green,
         blue,
         alpha
      );
   }

   public SentinelKnightRenderer(Context context) {
      super(context, new SentinelKnightModel());
      this.shadowRadius = 1.0F;
      this.addRenderLayer(this.glowLayer);
   }

   protected float getDeathMaxRotation(SentinelKnightEntity entityLivingBaseIn) {
      return 0.0F;
   }
}
