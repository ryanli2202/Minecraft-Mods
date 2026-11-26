package dev.deepnight.source.mobosses.entity.client.geomodel;

import dev.deepnight.source.mobosses.entity.custom.ForestGuardianEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ForestGuardianModel extends GeoModel<ForestGuardianEntity> {
   public ResourceLocation getModelResource(ForestGuardianEntity animatable) {
      return new ResourceLocation("mobosses", "geo/forest_guardian.geo.json");
   }

   public ResourceLocation getTextureResource(ForestGuardianEntity animatable) {
      return new ResourceLocation("mobosses", "textures/entity/forest_guardian.png");
   }

   public ResourceLocation getAnimationResource(ForestGuardianEntity animatable) {
      return new ResourceLocation("mobosses", "animations/forest_guardian_animations.json");
   }
}
