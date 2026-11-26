package dev.deepnight.source.dawn.potion;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;

public class DragonSummonedMobEffect extends MobEffect {
   public DragonSummonedMobEffect() {
      super(MobEffectCategory.NEUTRAL, -1);
   }

   @Override
   public String getDescriptionId() {
      return "effect.simple_mobs.dragon_summoned";
   }

   @Override
   public boolean isDurationEffectTick(int duration, int amplifier) {
      return true;
   }

   @Override
   public List<ItemStack> getCurativeItems() {
      return Lists.newArrayList();
   }
}
