package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.Spell3Model;
import dev.deepnight.source.dawn.entity.Spell3Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import dev.deepnight.source.dawn.client.LegacyGeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;


public class Spell3Renderer extends LegacyGeoRenderer<Spell3Entity> {
   protected final GeoRenderLayer<Spell3Entity> glowLayer = new GeoRenderLayer<Spell3Entity>(this) {
      public void render(PoseStack poseStack, Spell3Entity entity, software.bernie.geckolib.cache.object.BakedGeoModel bakedModel, RenderType originalType, MultiBufferSource buffer, VertexConsumer originalBuffer, float partialTicks, int packedLight, int packedOverlay) {
         RenderType renderType = null;
         if (entity.tickCount > 1) {
            int tick = entity.tickCount;
            if (tick % 55 < 34) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/spell3/spell3_1.png"));
            } else if (tick % 55 < 36) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/spell3/spell3_2.png"));
            } else if (tick % 55 < 38) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/spell3/spell3_3.png"));
            } else if (tick % 55 < 40) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/spell3/spell3_4.png"));
            } else if (tick % 55 < 42) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/spell3/spell3_5.png"));
            } else if (tick % 55 < 44) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/spell3/spell3_6.png"));
            } else if (tick % 55 < 46) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/spell3/spell3_7.png"));
            } else if (tick % 55 < 48) {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/spell3/spell3_8.png"));
            } else {
               renderType = RenderType.eyes(new ResourceLocation("simple_mobs:textures/a.png"));
            }

            VertexConsumer ivertexbuilder = buffer.getBuffer(renderType);
            GeoModel provider = this.getGeoModel();
            this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity)),poseStack,buffer,entity,renderType,ivertexbuilder,partialTicks,15728640,OverlayTexture.NO_OVERLAY,1,1,1,1);
         }
      }
   };

   public Spell3Renderer(Context context) {
      super(context, new Spell3Model());
      this.addRenderLayer(this.glowLayer);
   }

   public void render(Spell3Entity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
      if (entity.tickCount > 1) {
         super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
      }
   }
}
