package dev.deepnight.source.dawn.procedures;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;

public class ProkorokspawnProcedure {
   public static void execute(LevelAccessor world, Entity entity) {
      if (entity != null) {
         if (!world.isClientSide()
            && (world instanceof Level _lvl ? _lvl.dimension() : Level.OVERWORLD)
               == ResourceKey.<Level>create(net.minecraft.core.registries.Registries.DIMENSION, new ResourceLocation("simple_mobs:distant_land"))) {
            entity.getPersistentData().putBoolean("dance", true);
            if (Math.random() >= 0.75) {
               Entity _entity = entity;
               if (_entity instanceof Player _player) {
                  _player.getInventory().armor.set(3, new ItemStack(Blocks.WHITE_WOOL));
                  _player.getInventory().setChanged();
               } else if (_entity instanceof LivingEntity _living) {
                  _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.WHITE_WOOL));
               }
            } else if (Math.random() < 0.75 && Math.random() >= 0.5) {
               Entity _entity = entity;
               if (_entity instanceof Player _player) {
                  _player.getInventory().armor.set(3, new ItemStack(Blocks.BLACK_WOOL));
                  _player.getInventory().setChanged();
               } else if (_entity instanceof LivingEntity _living) {
                  _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.BLACK_WOOL));
               }
            } else if (Math.random() < 0.5 && Math.random() >= 0.25) {
               Entity _entity = entity;
               if (_entity instanceof Player _player) {
                  _player.getInventory().armor.set(3, new ItemStack(Blocks.RED_WOOL));
                  _player.getInventory().setChanged();
               } else if (_entity instanceof LivingEntity _living) {
                  _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.RED_WOOL));
               }
            } else {
               Entity _entity = entity;
               if (_entity instanceof Player _player) {
                  _player.getInventory().armor.set(3, new ItemStack(Blocks.BLUE_WOOL));
                  _player.getInventory().setChanged();
               } else if (_entity instanceof LivingEntity _living) {
                  _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.BLUE_WOOL));
               }
            }
         }
      }
   }
}
