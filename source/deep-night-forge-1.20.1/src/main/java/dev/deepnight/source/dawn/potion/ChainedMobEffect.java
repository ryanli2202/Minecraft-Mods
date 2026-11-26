package dev.deepnight.source.dawn.potion;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Consumer;
import dev.deepnight.source.dawn.procedures.ProchainedProcedure;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class ChainedMobEffect extends MobEffect {
   public ChainedMobEffect() {
      super(MobEffectCategory.NEUTRAL, -3355648);
   }

   @Override
   public String getDescriptionId() {
      return "effect.simple_mobs.chained";
   }

   @Override
   public void applyEffectTick(LivingEntity entity, int amplifier) {
      ProchainedProcedure.execute(entity);
   }

   @Override
   public boolean isDurationEffectTick(int duration, int amplifier) {
      return true;
   }
}
