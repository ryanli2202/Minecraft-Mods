package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class ProtailhurtProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
      if (entity != null && sourceentity != null) {
         if (!world.isClientSide() && entity.getPersistentData().getBoolean("Tails")) {
            Entity _entity = entity;
            if (_entity instanceof Player _player) {
               _player.getInventory().armor.set(0, new ItemStack(Items.LEATHER_BOOTS));
               _player.getInventory().setChanged();
            } else if (_entity instanceof LivingEntity _living) {
               _living.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
            }

            entity.getPersistentData().putBoolean("Consume", true);
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.5), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == sourceentity) {
                  Entity _entityx = entity;
                  if (_entityx instanceof Player _player) {
                     _player.getInventory().armor.set(0, new ItemStack(Blocks.AIR));
                     _player.getInventory().setChanged();
                  } else if (_entityx instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.FEET, new ItemStack(Blocks.AIR));
                  }

                  entity.getPersistentData().putBoolean("Consume", false);
               }
            }

            if (entity.getPersistentData().getBoolean("Consume") && world instanceof ServerLevel _level) {
               _level.getServer()
                  .getCommands()
                  .performPrefixedCommand(
                     new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null)
                        .withSuppressedOutput(),
                     "/particle dust 1.000 0.100 0.100 3 ~ ~ ~ 0.5 0.5 0.5 0 10 force"
                  );
            }

            if (!entity.getPersistentData().getBoolean("hurtcd")) {
               entity.getPersistentData().putBoolean("hurtcd", true);
               if (entity.getPersistentData().getDouble("Old HP") == 0.0) {
                  entity.getPersistentData().putDouble("Old HP", entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0);
               } else {
                  entity.getPersistentData()
                     .putDouble("DMG", entity.getPersistentData().getDouble("Old HP") - (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0F));
               }

               if (entity.getPersistentData().getDouble("DMG") > 14.0) {
                  entity.getPersistentData().putDouble("Poise", entity.getPersistentData().getDouble("Poise") + 90.0);
               } else if (entity.getPersistentData().getDouble("DMG") > 10.0) {
                  entity.getPersistentData().putDouble("Poise", entity.getPersistentData().getDouble("Poise") + 80.0);
               } else if (entity.getPersistentData().getDouble("DMG") > 4.0) {
                  entity.getPersistentData().putDouble("Poise", entity.getPersistentData().getDouble("Poise") + 75.0);
               }

               entity.getPersistentData().putDouble("Old HP", entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1.0);
            }
         }
      }
   }
}
