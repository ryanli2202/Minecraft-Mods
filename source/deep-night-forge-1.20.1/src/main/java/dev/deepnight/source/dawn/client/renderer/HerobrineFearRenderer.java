package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.HerobrineFearModel;
import dev.deepnight.source.dawn.entity.HerobrineFearEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import dev.deepnight.source.dawn.client.LegacyGeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;


public class HerobrineFearRenderer extends LegacyGeoRenderer<HerobrineFearEntity> {
   protected final GeoRenderLayer<HerobrineFearEntity> transparentLayer = new GeoRenderLayer<HerobrineFearEntity>(this) {
      public void render(PoseStack poseStack, HerobrineFearEntity entity, software.bernie.geckolib.cache.object.BakedGeoModel bakedModel, RenderType originalType, MultiBufferSource buffer, VertexConsumer originalBuffer, float partialTicks, int packedLight, int packedOverlay) {
         RenderType renderType = null;
         if (entity.tickCount > 1) {
            int tick = entity.tickCount;
            if (tick % 50 < 28) {
               renderType = RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/herobrine_fear/herobrine_fear_1.png"));
            } else if (tick % 50 < 32) {
               renderType = RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/herobrine_fear/herobrine_fear_2.png"));
            } else if (tick % 50 < 36) {
               renderType = RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/herobrine_fear/herobrine_fear_3.png"));
            } else {
               renderType = RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/herobrine_fear/herobrine_fear_4.png"));
            }

            VertexConsumer ivertexbuilder = buffer.getBuffer(renderType);
            GeoModel provider = this.getGeoModel();
            this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity)),poseStack,buffer,entity,renderType,ivertexbuilder,partialTicks,15728640,OverlayTexture.NO_OVERLAY,1,1,1,1);
         }
      }
   };

   public HerobrineFearRenderer(Context context) {
      super(context, new HerobrineFearModel());
      this.addRenderLayer(this.transparentLayer);
   }

   public void render(HerobrineFearEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
      if (entity.tickCount > 1) {
         super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
      }
   }
}
