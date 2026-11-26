package dev.deepnight.source.mobosses.entity.client.geomodel;

import dev.deepnight.source.mobosses.entity.custom.DivineDragonEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DivineDragonModel extends GeoModel<DivineDragonEntity> {
   public ResourceLocation getModelResource(DivineDragonEntity animatable) {
      return new ResourceLocation("mobosses", "geo/divine_dragon.geo.json");
   }

   public ResourceLocation getTextureResource(DivineDragonEntity animatable) {
      return new ResourceLocation("mobosses", "textures/entity/divine_dragon.png");
   }

   public ResourceLocation getAnimationResource(DivineDragonEntity animatable) {
      return new ResourceLocation("mobosses", "animations/divine_dragon_animations.json");
   }
}
