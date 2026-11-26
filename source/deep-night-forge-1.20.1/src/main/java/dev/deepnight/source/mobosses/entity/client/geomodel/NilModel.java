package dev.deepnight.source.mobosses.entity.client.geomodel;

import dev.deepnight.source.mobosses.entity.custom.NilEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class NilModel extends GeoModel<NilEntity> {
   public ResourceLocation getModelResource(NilEntity animatable) {
      return new ResourceLocation("mobosses", "geo/nil.geo.json");
   }

   public ResourceLocation getTextureResource(NilEntity animatable) {
      return new ResourceLocation("mobosses", "textures/entity/nil.png");
   }

   public ResourceLocation getAnimationResource(NilEntity animatable) {
      return new ResourceLocation("mobosses", "animations/nil_animations.json");
   }
}
