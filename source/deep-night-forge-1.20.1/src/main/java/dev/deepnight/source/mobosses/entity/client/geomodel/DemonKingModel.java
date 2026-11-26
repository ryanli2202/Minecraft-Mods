package dev.deepnight.source.mobosses.entity.client.geomodel;

import dev.deepnight.source.mobosses.entity.custom.DemonKingEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DemonKingModel extends GeoModel<DemonKingEntity> {
   public ResourceLocation getModelResource(DemonKingEntity animatable) {
      return new ResourceLocation("mobosses", "geo/demon_king.geo.json");
   }

   public ResourceLocation getTextureResource(DemonKingEntity animatable) {
      return new ResourceLocation("mobosses", "textures/entity/demon_king.png");
   }

   public ResourceLocation getAnimationResource(DemonKingEntity animatable) {
      return new ResourceLocation("mobosses", "animations/demon_king_animations.json");
   }
}
