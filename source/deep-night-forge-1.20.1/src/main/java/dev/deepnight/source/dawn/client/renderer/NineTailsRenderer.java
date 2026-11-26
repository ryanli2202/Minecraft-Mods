package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.NineTailsModel;
import dev.deepnight.source.dawn.entity.NineTailsEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import dev.deepnight.source.dawn.client.LegacyGeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;


public class NineTailsRenderer extends LegacyGeoRenderer<NineTailsEntity> {
   protected final RenderType glowTexture = RenderType.entityTranslucent(new ResourceLocation("simple_mobs", "textures/ninetail_glow.png"));
   protected final GeoRenderLayer<NineTailsEntity> glowLayer = new GeoRenderLayer<NineTailsEntity>(this) {
      public RenderType getRenderType(ResourceLocation texture) {
         return NineTailsRenderer.this.glowTexture;
      }

      public void render(PoseStack poseStack, NineTailsEntity entity, software.bernie.geckolib.cache.object.BakedGeoModel bakedModel, RenderType originalType, MultiBufferSource buffer, VertexConsumer originalBuffer, float partialTicks, int packedLight, int packedOverlay) {
         RenderType renderType = this.getRenderType(null);
         VertexConsumer ivertexbuilder = buffer.getBuffer(renderType);
         GeoModel provider = this.getGeoModel();
         this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity)),poseStack,buffer,entity,renderType,ivertexbuilder,partialTicks,15728640,OverlayTexture.NO_OVERLAY,1,1,1,1);
      }
   };

   public NineTailsRenderer(Context context) {
      super(context, new NineTailsModel());
      this.shadowRadius = 0.8F;
      this.addRenderLayer(this.glowLayer);
   }

   protected float getDeathMaxRotation(NineTailsEntity entityLivingBaseIn) {
      return 0.0F;
   }
}
