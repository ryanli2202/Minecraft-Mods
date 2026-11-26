package dev.deepnight.source.mobosses.entity.client.renderer;

import dev.deepnight.source.mobosses.entity.client.geomodel.RoyalKnightModel;
import dev.deepnight.source.mobosses.entity.custom.RoyalKnightEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RoyalKnightRenderer extends GeoEntityRenderer<RoyalKnightEntity> {
   public RoyalKnightRenderer(Context renderManager) {
      super(renderManager, new RoyalKnightModel());
      this.shadowRadius = 0.5F;
   }
}
