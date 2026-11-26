package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.FlameBallModel;
import dev.deepnight.source.dawn.entity.FlameBallEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import dev.deepnight.source.dawn.client.LegacyGeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;


public class FlameBallRenderer extends LegacyGeoRenderer<FlameBallEntity> {
   protected final RenderType glowTexture = RenderType.eyes(new ResourceLocation("simple_mobs", "textures/entities/lava_ball_glow.png"));
   protected final GeoRenderLayer<FlameBallEntity> glowLayer = new GeoRenderLayer<FlameBallEntity>(this) {
      public RenderType getRenderType(ResourceLocation texture) {
         return FlameBallRenderer.this.glowTexture;
      }

      public void render(PoseStack poseStack, FlameBallEntity entity, software.bernie.geckolib.cache.object.BakedGeoModel bakedModel, RenderType originalType, MultiBufferSource buffer, VertexConsumer originalBuffer, float partialTicks, int packedLight, int packedOverlay) {
         if (entity.tickCount > 1) {
            RenderType renderType = this.getRenderType(null);
            VertexConsumer ivertexbuilder = buffer.getBuffer(renderType);
            GeoModel provider = this.getGeoModel();
            this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity)),poseStack,buffer,entity,renderType,ivertexbuilder,partialTicks,15728640,OverlayTexture.NO_OVERLAY,1,1,1,1);
         }
      }
   };

   public FlameBallRenderer(Context context) {
      super(context, new FlameBallModel());
      this.shadowRadius = 0.4F;
      this.addRenderLayer(this.glowLayer);
   }

   public void render(FlameBallEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
      if (entity.tickCount > 1) {
         super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
      }
   }
}
