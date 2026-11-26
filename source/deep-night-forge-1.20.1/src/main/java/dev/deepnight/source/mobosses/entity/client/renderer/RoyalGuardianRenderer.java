package dev.deepnight.source.mobosses.entity.client.renderer;

import javax.annotation.Nullable;
import dev.deepnight.source.mobosses.entity.client.geomodel.RoyalGuardianModel;
import dev.deepnight.source.mobosses.entity.custom.RoyalGuardianEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RoyalGuardianRenderer extends GeoEntityRenderer<RoyalGuardianEntity> {
   public RoyalGuardianRenderer(Context renderManager) {
      super(renderManager, new RoyalGuardianModel());
      this.shadowRadius = 0.7F;
   }

   public RenderType getRenderType(RoyalGuardianEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
      return RenderType.entityTranslucent(texture);
   }
}
