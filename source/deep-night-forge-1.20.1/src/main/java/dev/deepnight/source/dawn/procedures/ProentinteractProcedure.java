package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.EntEntity;
import dev.deepnight.source.dawn.init.SimpleMobsModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemHandlerHelper;

public class ProentinteractProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
      if (entity != null && sourceentity != null) {
         Vec3 _center = new Vec3(x, y, z);

         for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2.0), e -> true)
            .stream()
            .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
            .collect(Collectors.toList())) {
            if (entityiterator instanceof EntEntity) {
               if (sourceentity instanceof Player _player) {
                  ItemStack _setstack = new ItemStack(SimpleMobsModItems.ENT_STAFF.get());
                  _setstack.setCount(1);
                  ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
               }

               if (world instanceof ServerLevel _level) {
                  _level.sendParticles(ParticleTypes.ENCHANTED_HIT, x, y + 1.0, z, 30, 1.0, 1.0, 1.0, 0.1);
               }

               if (!entityiterator.level().isClientSide()) {
                  entityiterator.discard();
               }

               if (!entity.level().isClientSide()) {
                  entity.discard();
               }
            }
         }
      }
   }
}
