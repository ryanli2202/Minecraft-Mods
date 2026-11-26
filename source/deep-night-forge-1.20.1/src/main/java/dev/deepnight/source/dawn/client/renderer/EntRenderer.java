package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import javax.annotation.Nullable;
import dev.deepnight.source.dawn.client.EntModel;
import dev.deepnight.source.dawn.entity.EntEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import dev.deepnight.source.dawn.client.LegacyGeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;


public class EntRenderer extends LegacyGeoRenderer<EntEntity> {
   protected final GeoRenderLayer<EntEntity> glowLayer = new GeoRenderLayer<EntEntity>(this) {
      public void render(PoseStack poseStack, EntEntity entity, software.bernie.geckolib.cache.object.BakedGeoModel bakedModel, RenderType originalType, MultiBufferSource buffer, VertexConsumer originalBuffer, float partialTicks, int packedLight, int packedOverlay) {
         RenderType renderType = null;
         if (entity.getDisplayName().getString().equals("Staff")) {
            renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/a.png"));
         } else {
            renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/ent_glow.png"));
         }

         VertexConsumer ivertexbuilder = buffer.getBuffer(renderType);
         GeoModel provider = this.getGeoModel();
         this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity)),poseStack,buffer,entity,renderType,ivertexbuilder,partialTicks,15728640,OverlayTexture.NO_OVERLAY,1,1,1,1);
      }
   };
   protected final GeoRenderLayer<EntEntity> transparentLayer = new GeoRenderLayer<EntEntity>(this) {
      public void render(PoseStack poseStack, EntEntity entity, software.bernie.geckolib.cache.object.BakedGeoModel bakedModel, RenderType originalType, MultiBufferSource buffer, VertexConsumer originalBuffer, float partialTicks, int packedLight, int packedOverlay) {
         RenderType renderType = this.getRenderer().getRenderType(entity,this.getRenderer().getTextureLocation(entity),buffer,partialTicks);
         VertexConsumer ivertexbuilder = buffer.getBuffer(renderType);
         GeoModel provider = this.getGeoModel();
         this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity)),poseStack,buffer,entity,renderType,ivertexbuilder,partialTicks,15728640,OverlayTexture.NO_OVERLAY,1,1,1,1);
      }
   };

   public void render(
      BakedGeoModel model,
      EntEntity animatable,
      float partialTicks,
      RenderType type,
      PoseStack matrixStackIn,
      @Nullable MultiBufferSource renderTypeBuffer,
      @Nullable VertexConsumer vertexBuilder,
      int packedLightIn,
      int packedOverlayIn,
      float red,
      float green,
      float blue,
      float alpha
   ) {
      this.renderEarly(animatable, matrixStackIn, partialTicks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
      if (renderTypeBuffer != null) {
         vertexBuilder = renderTypeBuffer.getBuffer(type);
      }

      this.renderRecursively(
         (GeoBone)model.topLevelBones().get(0),
         matrixStackIn,
         vertexBuilder,
         packedLightIn,
         OverlayTexture.pack(0, OverlayTexture.v(animatable.hurtTime > 0 && animatable.getPersistentData().getBoolean("Blocking"))),
         red,
         green,
         blue,
         alpha
      );
   }

   public boolean shouldShowName(EntEntity p114504) {
      return false;
   }

   public EntRenderer(Context context) {
      super(context, new EntModel());
      this.shadowRadius = 1.0F;
      this.addRenderLayer(this.glowLayer);
   }

   protected float getDeathMaxRotation(EntEntity entityLivingBaseIn) {
      return 0.0F;
   }
}
