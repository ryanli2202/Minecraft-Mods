package dev.deepnight.source.mobosses.entity.client.renderer;

import dev.deepnight.source.mobosses.entity.client.geomodel.ForestGuardianModel;
import dev.deepnight.source.mobosses.entity.custom.ForestGuardianEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ForestGuardianRenderer extends GeoEntityRenderer<ForestGuardianEntity> {
   public ForestGuardianRenderer(Context renderManager) {
      super(renderManager, new ForestGuardianModel());
   }
}
