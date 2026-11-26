package dev.deepnight.source.mobosses.entity.client.renderer;

import dev.deepnight.source.mobosses.entity.client.geomodel.NilModel;
import dev.deepnight.source.mobosses.entity.custom.NilEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class NilRenderer extends GeoEntityRenderer<NilEntity> {
   public NilRenderer(Context renderManager) {
      super(renderManager, new NilModel());
      this.shadowRadius = 0.6F;
   }
}
