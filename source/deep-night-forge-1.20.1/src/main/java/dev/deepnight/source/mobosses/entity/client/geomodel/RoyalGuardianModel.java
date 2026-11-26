package dev.deepnight.source.mobosses.entity.client.geomodel;

import dev.deepnight.source.mobosses.entity.custom.RoyalGuardianEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RoyalGuardianModel extends GeoModel<RoyalGuardianEntity> {
   public ResourceLocation getModelResource(RoyalGuardianEntity animatable) {
      return new ResourceLocation("mobosses", "geo/royal_guardian.geo.json");
   }

   public ResourceLocation getTextureResource(RoyalGuardianEntity animatable) {
      return new ResourceLocation("mobosses", "textures/entity/royal_guardian.png");
   }

   public ResourceLocation getAnimationResource(RoyalGuardianEntity animatable) {
      return new ResourceLocation("mobosses", "animations/royal_guardian_animations.json");
   }
}
