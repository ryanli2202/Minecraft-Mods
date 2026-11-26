package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.SphereModel;
import dev.deepnight.source.dawn.entity.SphereEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import dev.deepnight.source.dawn.client.LegacyGeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;


public class SphereRenderer extends LegacyGeoRenderer<SphereEntity> {
   protected final RenderType transparentTexture = RenderType.entityTranslucent(new ResourceLocation("simple_mobs", "textures/sphere.png"));
   protected final GeoRenderLayer<SphereEntity> transparentLayer = new GeoRenderLayer<SphereEntity>(this) {
      public RenderType getRenderType(ResourceLocation texture) {
         return SphereRenderer.this.transparentTexture;
      }

      public void render(PoseStack poseStack, SphereEntity entity, software.bernie.geckolib.cache.object.BakedGeoModel bakedModel, RenderType originalType, MultiBufferSource buffer, VertexConsumer originalBuffer, float partialTicks, int packedLight, int packedOverlay) {
         if (entity.tickCount > 1) {
            RenderType renderType = this.getRenderType(null);
            VertexConsumer ivertexbuilder = buffer.getBuffer(renderType);
            GeoModel provider = this.getGeoModel();
            this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity)),poseStack,buffer,entity,renderType,ivertexbuilder,partialTicks,15728640,OverlayTexture.NO_OVERLAY,1,1,1,1);
         }
      }
   };

   public SphereRenderer(Context context) {
      super(context, new SphereModel());
      this.shadowRadius = 0.4F;
      this.addRenderLayer(this.transparentLayer);
   }

   protected float getDeathMaxRotation(SphereEntity entityLivingBaseIn) {
      return 0.0F;
   }

   public void render(SphereEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
      if (entity.tickCount > 1) {
         super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
      }
   }
}
