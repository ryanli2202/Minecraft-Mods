package dev.deepnight.source.mobosses.entity.client.renderer;

import dev.deepnight.source.mobosses.entity.client.geomodel.DemonKingModel;
import dev.deepnight.source.mobosses.entity.custom.DemonKingEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DemonKingRenderer extends GeoEntityRenderer<DemonKingEntity> {
   public DemonKingRenderer(Context renderManager) {
      super(renderManager, new DemonKingModel());
   }
}
