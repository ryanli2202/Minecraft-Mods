package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.FireDragonModel;
import dev.deepnight.source.dawn.entity.FireDragonEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import dev.deepnight.source.dawn.client.LegacyGeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;


public class FireDragonRenderer extends LegacyGeoRenderer<FireDragonEntity> {
   protected final RenderType glowTexture = RenderType.eyes(new ResourceLocation("simple_mobs", "textures/firedragon_eye.png"));
   protected final GeoRenderLayer<FireDragonEntity> glowLayer = new GeoRenderLayer<FireDragonEntity>(this) {
      public RenderType getRenderType(ResourceLocation texture) {
         return FireDragonRenderer.this.glowTexture;
      }

      public void render(PoseStack poseStack, FireDragonEntity entity, software.bernie.geckolib.cache.object.BakedGeoModel bakedModel, RenderType originalType, MultiBufferSource buffer, VertexConsumer originalBuffer, float partialTicks, int packedLight, int packedOverlay) {
         if (entity.tickCount > 1) {
            RenderType renderType = this.getRenderType(null);
            VertexConsumer ivertexbuilder = buffer.getBuffer(renderType);
            GeoModel provider = this.getGeoModel();
            this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity)),poseStack,buffer,entity,renderType,ivertexbuilder,partialTicks,15728640,OverlayTexture.NO_OVERLAY,1,1,1,1);
         }
      }
   };

   public FireDragonRenderer(Context context) {
      super(context, new FireDragonModel());
      this.shadowRadius = 0.0F;
      this.addRenderLayer(this.glowLayer);
   }

   protected float getDeathMaxRotation(FireDragonEntity entityLivingBaseIn) {
      return 0.0F;
   }
}
