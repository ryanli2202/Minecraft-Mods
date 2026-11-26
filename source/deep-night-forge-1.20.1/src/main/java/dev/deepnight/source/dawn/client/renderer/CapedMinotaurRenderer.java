package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.CapedMinotaurModel;
import dev.deepnight.source.dawn.entity.CapedMinotaurEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import software.bernie.geckolib.model.GeoModel;
import dev.deepnight.source.dawn.client.LegacyGeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;


public class CapedMinotaurRenderer extends LegacyGeoRenderer<CapedMinotaurEntity> {
   protected final GeoRenderLayer<CapedMinotaurEntity> glowLayer = new GeoRenderLayer<CapedMinotaurEntity>(this) {
      public void render(PoseStack poseStack, CapedMinotaurEntity entity, software.bernie.geckolib.cache.object.BakedGeoModel bakedModel, RenderType originalType, MultiBufferSource buffer, VertexConsumer originalBuffer, float partialTicks, int packedLight, int packedOverlay) {
         RenderType renderType = this.getRenderer().getRenderType(entity,this.getRenderer().getTextureLocation(entity),buffer,partialTicks);
         VertexConsumer ivertexbuilder = buffer.getBuffer(renderType);
         GeoModel provider = this.getGeoModel();
         this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity)),poseStack,buffer,entity,renderType,ivertexbuilder,partialTicks,15728640,OverlayTexture.NO_OVERLAY,1,1,1,1);
      }
   };

   public CapedMinotaurRenderer(Context context) {
      super(context, new CapedMinotaurModel());
      this.shadowRadius = 0.4F;
   }

   protected float getDeathMaxRotation(CapedMinotaurEntity entityLivingBaseIn) {
      return 0.0F;
   }
}
