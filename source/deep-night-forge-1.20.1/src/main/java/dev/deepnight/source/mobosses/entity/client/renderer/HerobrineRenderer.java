package dev.deepnight.source.mobosses.entity.client.renderer;

import dev.deepnight.source.mobosses.entity.client.geomodel.HerobrineModel;
import dev.deepnight.source.mobosses.entity.custom.HerobrineEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

public class HerobrineRenderer extends GeoEntityRenderer<HerobrineEntity> {
   public HerobrineRenderer(Context context) {
      super(context, new HerobrineModel());
      this.addRenderLayer(new BlockAndItemGeoLayer<HerobrineEntity>(this) {
         protected ItemStack getStackForBone(GeoBone bone, HerobrineEntity animatable) {
            return bone.getName().equals("leftHandItem") ? animatable.getItemBySlot(EquipmentSlot.MAINHAND) : ItemStack.EMPTY;
         }

         protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, HerobrineEntity animatable) {
            return ItemDisplayContext.FIXED;
         }
      });
   }
}
