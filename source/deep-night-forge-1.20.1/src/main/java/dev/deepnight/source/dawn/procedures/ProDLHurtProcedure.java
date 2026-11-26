package dev.deepnight.source.dawn.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;

public class ProDLHurtProcedure {
   public static void execute(LevelAccessor world, Entity entity) {
      if (entity != null) {
         if (!world.isClientSide() && !entity.getPersistentData().getBoolean("hurtcd")) {
            Entity _entity = entity;
            if (_entity instanceof Player _player) {
               _player.getInventory().armor.set(0, new ItemStack(Blocks.DIRT));
               _player.getInventory().setChanged();
            } else if (_entity instanceof LivingEntity _living) {
               _living.setItemSlot(EquipmentSlot.FEET, new ItemStack(Blocks.DIRT));
            }

            entity.getPersistentData().putBoolean("hurtcd", true);
         }
      }
   }
}
