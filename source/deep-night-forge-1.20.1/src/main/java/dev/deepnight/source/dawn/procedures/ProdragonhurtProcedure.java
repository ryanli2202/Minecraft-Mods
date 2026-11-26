package dev.deepnight.source.dawn.procedures;

import dev.deepnight.source.dawn.entity.DragMultiPart2Entity;
import dev.deepnight.source.dawn.entity.IceDragMultiPart2Entity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;

public class ProdragonhurtProcedure {
   public static void execute(LevelAccessor world, Entity entity, Entity sourceentity) {
      if (entity != null && sourceentity != null) {
         if (!world.isClientSide() && !entity.getPersistentData().getBoolean("hurtcd")) {
            Entity _entity = entity;
            if (_entity instanceof Player _player) {
               _player.getInventory().armor.set(0, new ItemStack(Blocks.DIRT));
               _player.getInventory().setChanged();
            } else if (_entity instanceof LivingEntity _living) {
               _living.setItemSlot(EquipmentSlot.FEET, new ItemStack(Blocks.DIRT));
            }

            entity.getPersistentData().putBoolean("hurtcd", true);
            if (entity.getPersistentData().getDouble("Old HP") == 0.0) {
               entity.getPersistentData().putDouble("Old HP", entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0);
            } else {
               entity.getPersistentData()
                  .putDouble("DMG", entity.getPersistentData().getDouble("Old HP") - (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0F));
            }

            if (!(sourceentity instanceof DragMultiPart2Entity) && !(sourceentity instanceof IceDragMultiPart2Entity)) {
               if (entity.getPersistentData().getDouble("DMG") > 14.0) {
                  entity.getPersistentData().putDouble("Poise", entity.getPersistentData().getDouble("Poise") + 100.0);
               } else if (entity.getPersistentData().getDouble("DMG") > 10.0) {
                  entity.getPersistentData().putDouble("Poise", entity.getPersistentData().getDouble("Poise") + 60.0);
               } else if (entity.getPersistentData().getDouble("DMG") > 6.0) {
                  entity.getPersistentData().putDouble("Poise", entity.getPersistentData().getDouble("Poise") + 50.0);
               } else if (entity.getPersistentData().getDouble("DMG") > 4.0) {
                  entity.getPersistentData().putDouble("Poise", entity.getPersistentData().getDouble("Poise") + 40.0);
               }
            } else if (entity.getPersistentData().getDouble("DMG") > 14.0) {
               entity.getPersistentData().putDouble("Poise", entity.getPersistentData().getDouble("Poise") + 140.0);
            } else if (entity.getPersistentData().getDouble("DMG") > 10.0) {
               entity.getPersistentData().putDouble("Poise", entity.getPersistentData().getDouble("Poise") + 80.0);
            } else if (entity.getPersistentData().getDouble("DMG") > 6.0) {
               entity.getPersistentData().putDouble("Poise", entity.getPersistentData().getDouble("Poise") + 70.0);
            } else if (entity.getPersistentData().getDouble("DMG") > 4.0) {
               entity.getPersistentData().putDouble("Poise", entity.getPersistentData().getDouble("Poise") + 60.0);
            }

            entity.getPersistentData().putDouble("Old HP", entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0);
         }
      }
   }
}
