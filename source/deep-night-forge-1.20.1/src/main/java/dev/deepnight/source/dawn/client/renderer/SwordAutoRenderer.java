package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.deepnight.source.dawn.client.SwordAutoModel;
import dev.deepnight.source.dawn.entity.SwordAutoEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import dev.deepnight.source.dawn.client.LegacyGeoRenderer;

public class SwordAutoRenderer extends LegacyGeoRenderer<SwordAutoEntity> {
   public SwordAutoRenderer(Context context) {
      super(context, new SwordAutoModel());
   }

   public void render(SwordAutoEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
      if (entity.tickCount > 1) {
         super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
      }
   }
}
