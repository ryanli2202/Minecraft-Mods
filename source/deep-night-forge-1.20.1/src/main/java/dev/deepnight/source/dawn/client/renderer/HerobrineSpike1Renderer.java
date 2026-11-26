package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.HerobrineSpike1Model;
import dev.deepnight.source.dawn.entity.HerobrineSpike1Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import software.bernie.geckolib.model.GeoModel;
import dev.deepnight.source.dawn.client.LegacyGeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;


public class HerobrineSpike1Renderer extends LegacyGeoRenderer<HerobrineSpike1Entity> {
   protected final GeoRenderLayer<HerobrineSpike1Entity> glowLayer = new GeoRenderLayer<HerobrineSpike1Entity>(this) {
      public void render(PoseStack poseStack, HerobrineSpike1Entity entity, software.bernie.geckolib.cache.object.BakedGeoModel bakedModel, RenderType originalType, MultiBufferSource buffer, VertexConsumer originalBuffer, float partialTicks, int packedLight, int packedOverlay) {
         if (entity.tickCount > 1) {
            RenderType renderType = this.getRenderer().getRenderType(entity,this.getRenderer().getTextureLocation(entity),buffer,partialTicks);
            VertexConsumer ivertexbuilder = buffer.getBuffer(renderType);
            GeoModel provider = this.getGeoModel();
            this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity)),poseStack,buffer,entity,renderType,ivertexbuilder,partialTicks,15728640,OverlayTexture.NO_OVERLAY,1,1,1,1);
         }
      }
   };

   public HerobrineSpike1Renderer(Context context) {
      super(context, new HerobrineSpike1Model());
   }

   protected float getDeathMaxRotation(HerobrineSpike1Entity entityLivingBaseIn) {
      return 0.0F;
   }

   public void render(HerobrineSpike1Entity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
      if (entity.tickCount > 1) {
         super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
      }
   }
}
