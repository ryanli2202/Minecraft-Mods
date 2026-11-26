package dev.deepnight.source.mobosses.entity.client.geomodel;

import dev.deepnight.source.mobosses.entity.custom.HerobrineEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HerobrineModel extends GeoModel<HerobrineEntity> {
   public ResourceLocation getModelResource(HerobrineEntity animatable) {
      return new ResourceLocation("mobosses", "geo/herobrine.geo.json");
   }

   public ResourceLocation getTextureResource(HerobrineEntity animatable) {
      return new ResourceLocation("mobosses", "textures/entity/herobrine.png");
   }

   public ResourceLocation getAnimationResource(HerobrineEntity animatable) {
      return new ResourceLocation("mobosses", "animations/herobrine_animations.json");
   }
}
