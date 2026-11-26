package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.LightningDragonModel;
import dev.deepnight.source.dawn.entity.LightningDragonEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import dev.deepnight.source.dawn.client.LegacyGeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;


public class LightningDragonRenderer extends LegacyGeoRenderer<LightningDragonEntity> {
   protected final RenderType glowTexture = RenderType.eyes(new ResourceLocation("simple_mobs", "textures/lightningdragon_eye.png"));
   protected final GeoRenderLayer<LightningDragonEntity> glowLayer = new GeoRenderLayer<LightningDragonEntity>(this) {
      public RenderType getRenderType(ResourceLocation texture) {
         return LightningDragonRenderer.this.glowTexture;
      }

      public void render(PoseStack poseStack, LightningDragonEntity entity, software.bernie.geckolib.cache.object.BakedGeoModel bakedModel, RenderType originalType, MultiBufferSource buffer, VertexConsumer originalBuffer, float partialTicks, int packedLight, int packedOverlay) {
         if (entity.tickCount > 1) {
            RenderType renderType = this.getRenderType(null);
            VertexConsumer ivertexbuilder = buffer.getBuffer(renderType);
            GeoModel provider = this.getGeoModel();
            this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity)),poseStack,buffer,entity,renderType,ivertexbuilder,partialTicks,15728640,OverlayTexture.NO_OVERLAY,1,1,1,1);
         }
      }
   };

   public LightningDragonRenderer(Context context) {
      super(context, new LightningDragonModel());
      this.shadowRadius = 0.0F;
      this.addRenderLayer(this.glowLayer);
   }

   protected float getDeathMaxRotation(LightningDragonEntity entityLivingBaseIn) {
      return 0.0F;
   }
}
