package dev.deepnight.source.mobosses.entity.client.renderer;

import dev.deepnight.source.mobosses.entity.client.geomodel.DivineDragonModel;
import dev.deepnight.source.mobosses.entity.custom.DivineDragonEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DivineDragonRenderer extends GeoEntityRenderer<DivineDragonEntity> {
   public DivineDragonRenderer(Context renderManager) {
      super(renderManager, new DivineDragonModel());
   }
}
