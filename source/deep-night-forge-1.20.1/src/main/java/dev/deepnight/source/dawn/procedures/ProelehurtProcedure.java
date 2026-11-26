package dev.deepnight.source.dawn.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;

public class ProelehurtProcedure {
   public static void execute(LevelAccessor world, Entity entity) {
      if (entity != null) {
         if (!world.isClientSide()) {
            if (entity.getPersistentData().getBoolean("Close") && !entity.getPersistentData().getBoolean("Attacking")) {
               if (Math.random() > 0.2) {
                  entity.getPersistentData().putBoolean("Dodge", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (Math.random() > 0.5) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.PAPER));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.PAPER));
                     }
                  } else {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.CARROT));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CARROT));
                     }
                  }
               }
            } else if (entity.getPersistentData().getBoolean("Mid") && !entity.getPersistentData().getBoolean("Attacking")) {
               if (Math.random() > 0.1) {
                  entity.getPersistentData().putBoolean("Dodge", true);
                  entity.getPersistentData().putBoolean("Attacking", true);
                  if (Math.random() > 0.5) {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.PAPER));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.PAPER));
                     }
                  } else {
                     Entity _entity = entity;
                     if (_entity instanceof Player _player) {
                        _player.getInventory().armor.set(3, new ItemStack(Items.CARROT));
                        _player.getInventory().setChanged();
                     } else if (_entity instanceof LivingEntity _living) {
                        _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CARROT));
                     }
                  }
               }
            } else if (!entity.getPersistentData().getBoolean("Attacking")) {
               entity.getPersistentData().putBoolean("Dodge", true);
               entity.getPersistentData().putBoolean("Attacking", true);
               if (Math.random() > 0.5) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.PAPER));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.PAPER));
                  }
               } else {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.CARROT));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CARROT));
                  }
               }
            }

            if (!entity.getPersistentData().getBoolean("hurtcd")) {
               entity.getPersistentData().putBoolean("hurtcd", true);
            }
         }
      }
   }
}
