package dev.deepnight.source.dawn.potion;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Consumer;
import dev.deepnight.source.dawn.procedures.PrograbProcedure;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class GrabMobEffect extends MobEffect {
   public GrabMobEffect() {
      super(MobEffectCategory.HARMFUL, -154);
   }

   @Override
   public String getDescriptionId() {
      return "effect.simple_mobs.grab";
   }

   @Override
   public void applyEffectTick(LivingEntity entity, int amplifier) {
      PrograbProcedure.execute(entity.level(), entity.getX(), entity.getY(), entity.getZ(), entity);
   }

   @Override
   public boolean isDurationEffectTick(int duration, int amplifier) {
      return true;
   }
}
