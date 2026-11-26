package dev.deepnight.source.mobosses.entity.client.renderer;

import dev.deepnight.source.mobosses.entity.custom.DemonKingLightningEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DemonKingLightningRenderer extends GeoEntityRenderer<DemonKingLightningEntity> {
   public DemonKingLightningRenderer(Context renderManager) {
      super(renderManager, new GeoModel<DemonKingLightningEntity>() {
         public ResourceLocation getModelResource(DemonKingLightningEntity animatable) {
            return new ResourceLocation("mobosses", "geo/demon_king_lightning.geo.json");
         }

         public ResourceLocation getTextureResource(DemonKingLightningEntity animatable) {
            return new ResourceLocation("mobosses", "textures/entity/demon_king_lightning.png");
         }

         public ResourceLocation getAnimationResource(DemonKingLightningEntity animatable) {
            return new ResourceLocation("mobosses", "animations/demon_king_lightning_animations.json");
         }
      });
   }
}
