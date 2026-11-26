package dev.deepnight.source.mobosses.entity.client.geomodel;

import dev.deepnight.source.mobosses.entity.custom.ForestGuardianAuraEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ForestGuardianAuraModel extends GeoModel<ForestGuardianAuraEntity> {
   public ResourceLocation getModelResource(ForestGuardianAuraEntity animatable) {
      return new ResourceLocation("mobosses", "geo/forest_guardian_aura.geo.json");
   }

   public ResourceLocation getTextureResource(ForestGuardianAuraEntity animatable) {
      return new ResourceLocation("mobosses", "textures/entity/forest_guardian_aura.png");
   }

   public ResourceLocation getAnimationResource(ForestGuardianAuraEntity animatable) {
      return new ResourceLocation("mobosses", "animations/forest_guardian_aura_animations.json");
   }
}
