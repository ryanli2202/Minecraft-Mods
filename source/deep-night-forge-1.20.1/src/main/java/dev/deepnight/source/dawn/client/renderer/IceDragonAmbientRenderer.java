package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.IceDragonAmbientModel;
import dev.deepnight.source.dawn.entity.IceDragonAmbientEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import dev.deepnight.source.dawn.client.LegacyGeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;


public class IceDragonAmbientRenderer extends LegacyGeoRenderer<IceDragonAmbientEntity> {
   protected final RenderType glowTexture = RenderType.eyes(new ResourceLocation("simple_mobs", "textures/icedragon_eye.png"));
   protected final GeoRenderLayer<IceDragonAmbientEntity> glowLayer = new GeoRenderLayer<IceDragonAmbientEntity>(this) {
      public RenderType getRenderType(ResourceLocation texture) {
         return IceDragonAmbientRenderer.this.glowTexture;
      }

      public void render(PoseStack poseStack, IceDragonAmbientEntity entity, software.bernie.geckolib.cache.object.BakedGeoModel bakedModel, RenderType originalType, MultiBufferSource buffer, VertexConsumer originalBuffer, float partialTicks, int packedLight, int packedOverlay) {
         if (entity.tickCount > 1) {
            RenderType renderType = this.getRenderType(null);
            VertexConsumer ivertexbuilder = buffer.getBuffer(renderType);
            GeoModel provider = this.getGeoModel();
            this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity)),poseStack,buffer,entity,renderType,ivertexbuilder,partialTicks,15728640,OverlayTexture.NO_OVERLAY,1,1,1,1);
         }
      }
   };

   public IceDragonAmbientRenderer(Context context) {
      super(context, new IceDragonAmbientModel());
      this.shadowRadius = 3.0F;
   }

   protected float getDeathMaxRotation(IceDragonAmbientEntity entityLivingBaseIn) {
      return 0.0F;
   }
}
