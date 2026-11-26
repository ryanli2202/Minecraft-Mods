package dev.deepnight.source.mobosses.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.deepnight.source.mobosses.entity.client.DemonKingMinionModel;
import dev.deepnight.source.mobosses.entity.custom.DemonKingMinionEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DemonKingMinionRenderer extends GeoEntityRenderer<DemonKingMinionEntity> {
   public DemonKingMinionRenderer(Context renderManager) {
      super(renderManager, new DemonKingMinionModel());
   }

   public void render(DemonKingMinionEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
      poseStack.pushPose();
      poseStack.translate(0.0, 1.0, 0.0);
      super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
      poseStack.popPose();
   }
}
