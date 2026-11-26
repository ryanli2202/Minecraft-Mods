package dev.deepnight.source.mobosses.entity.client.renderer;

import dev.deepnight.source.mobosses.entity.client.geomodel.ForestGuardianAuraModel;
import dev.deepnight.source.mobosses.entity.custom.ForestGuardianAuraEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ForestGuardianAuraRenderer extends GeoEntityRenderer<ForestGuardianAuraEntity> {
   public ForestGuardianAuraRenderer(Context renderManager) {
      super(renderManager, new ForestGuardianAuraModel());
   }
}
