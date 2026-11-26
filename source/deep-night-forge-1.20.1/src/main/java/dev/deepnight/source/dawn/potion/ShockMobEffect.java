package dev.deepnight.source.dawn.potion;

import dev.deepnight.source.dawn.procedures.ProshockedProcedure;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class ShockMobEffect extends MobEffect {
   public ShockMobEffect() {
      super(MobEffectCategory.HARMFUL, -154);
   }

   @Override
   public String getDescriptionId() {
      return "effect.simple_mobs.shock";
   }

   @Override
   public void applyEffectTick(LivingEntity entity, int amplifier) {
      ProshockedProcedure.execute(entity.level(), entity.getX(), entity.getY(), entity.getZ(), entity);
   }

   @Override
   public boolean isDurationEffectTick(int duration, int amplifier) {
      return true;
   }
}
