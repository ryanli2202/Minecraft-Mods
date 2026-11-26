package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.deepnight.source.dawn.client.Korok5Model;
import dev.deepnight.source.dawn.entity.Korok5Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import dev.deepnight.source.dawn.client.LegacyGeoRenderer;

public class Korok5Renderer extends LegacyGeoRenderer<Korok5Entity> {
   public Korok5Renderer(Context context) {
      super(context, new Korok5Model());
      this.shadowRadius = 0.4F;
   }

   protected float getDeathMaxRotation(Korok5Entity entityLivingBaseIn) {
      return 0.0F;
   }

   public void render(Korok5Entity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
      if (entity.tickCount > 1) {
         super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
      }
   }
}
