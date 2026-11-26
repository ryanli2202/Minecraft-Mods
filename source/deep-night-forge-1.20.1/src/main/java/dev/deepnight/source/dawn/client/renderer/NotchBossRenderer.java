package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.deepnight.source.dawn.client.NotchBossModel;
import dev.deepnight.source.dawn.entity.NotchBossEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import dev.deepnight.source.dawn.client.LegacyGeoRenderer;

public class NotchBossRenderer extends LegacyGeoRenderer<NotchBossEntity> {
   public NotchBossRenderer(Context context) {
      super(context, new NotchBossModel());
      this.shadowRadius = 0.4F;
   }

   protected float getDeathMaxRotation(NotchBossEntity entityLivingBaseIn) {
      return 0.0F;
   }

   public void render(NotchBossEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
      if (entity.tickCount > 1) {
         super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
      }
   }
}
