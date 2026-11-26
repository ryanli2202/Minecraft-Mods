package dev.deepnight.source.mobosses.entity.client.geomodel;

import dev.deepnight.source.mobosses.entity.custom.RoyalKnightEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RoyalKnightModel extends GeoModel<RoyalKnightEntity> {
   public ResourceLocation getModelResource(RoyalKnightEntity animatable) {
      return new ResourceLocation("mobosses", "geo/royal_knight.geo.json");
   }

   public ResourceLocation getTextureResource(RoyalKnightEntity animatable) {
      return new ResourceLocation("mobosses", "textures/entity/royal_knight.png");
   }

   public ResourceLocation getAnimationResource(RoyalKnightEntity animatable) {
      return new ResourceLocation("mobosses", "animations/royal_knight_animations.json");
   }
}
