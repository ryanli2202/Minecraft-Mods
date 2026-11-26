package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.deepnight.source.dawn.client.HerobrineSpike2Model;
import dev.deepnight.source.dawn.entity.HerobrineSpike2Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import dev.deepnight.source.dawn.client.LegacyGeoRenderer;

public class HerobrineSpike2Renderer extends LegacyGeoRenderer<HerobrineSpike2Entity> {
   public HerobrineSpike2Renderer(Context context) {
      super(context, new HerobrineSpike2Model());
   }

   protected float getDeathMaxRotation(HerobrineSpike2Entity entityLivingBaseIn) {
      return 0.0F;
   }

   public void render(HerobrineSpike2Entity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
      if (entity.tickCount > 1) {
         super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
      }
   }
}
