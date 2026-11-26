package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.DragonLordModel;
import dev.deepnight.source.dawn.entity.DragonLordEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import dev.deepnight.source.dawn.client.LegacyGeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;


public class DragonLordRenderer extends LegacyGeoRenderer<DragonLordEntity> {
   protected final GeoRenderLayer<DragonLordEntity> glowLayer = new GeoRenderLayer<DragonLordEntity>(this) {
      public void render(PoseStack poseStack, DragonLordEntity entity, software.bernie.geckolib.cache.object.BakedGeoModel bakedModel, RenderType originalType, MultiBufferSource buffer, VertexConsumer originalBuffer, float partialTicks, int packedLight, int packedOverlay) {
         RenderType renderType = null;
         if (entity.tickCount > 1) {
            int tick = entity.tickCount;
            if (tick % 6 < 2) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/dragon_lord/dragon_lord_1_glow.png"));
            } else if (tick % 6 < 3) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/dragon_lord/dragon_lord_2_glow.png"));
            } else if (tick % 6 < 4) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/dragon_lord/dragon_lord_3_glow.png"));
            } else if (tick % 6 < 5) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/dragon_lord/dragon_lord_4_glow.png"));
            } else {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/dragon_lord/dragon_lord_5_glow.png"));
            }

            VertexConsumer ivertexbuilder = buffer.getBuffer(renderType);
            GeoModel provider = this.getGeoModel();
            this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity)),poseStack,buffer,entity,renderType,ivertexbuilder,partialTicks,15728640,OverlayTexture.NO_OVERLAY,1,1,1,1);
         }
      }
   };

   public DragonLordRenderer(Context context) {
      super(context, new DragonLordModel());
      this.addRenderLayer(this.glowLayer);
   }

   protected float getDeathMaxRotation(DragonLordEntity entityLivingBaseIn) {
      return 0.0F;
   }

   public void render(DragonLordEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
      if (entity.tickCount > 1) {
         super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
      }
   }
}
