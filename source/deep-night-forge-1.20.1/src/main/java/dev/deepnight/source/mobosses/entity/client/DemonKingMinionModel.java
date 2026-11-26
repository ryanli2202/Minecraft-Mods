package dev.deepnight.source.mobosses.entity.client;

import dev.deepnight.source.mobosses.entity.custom.DemonKingMinionEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DemonKingMinionModel extends GeoModel<DemonKingMinionEntity> {
   public ResourceLocation getModelResource(DemonKingMinionEntity animatable) {
      return new ResourceLocation("mobosses", "geo/demon_king_minion.geo.json");
   }

   public ResourceLocation getTextureResource(DemonKingMinionEntity animatable) {
      return new ResourceLocation("mobosses", "textures/entity/demon_king_minion.png");
   }

   public ResourceLocation getAnimationResource(DemonKingMinionEntity animatable) {
      return new ResourceLocation("mobosses", "animations/demon_king_minion_animations.json");
   }
}
