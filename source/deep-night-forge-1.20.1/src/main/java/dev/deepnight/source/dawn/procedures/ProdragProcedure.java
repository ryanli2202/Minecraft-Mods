package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.DraggingSpellEntity;
import dev.deepnight.source.dawn.entity.GhostprojectileEntity;
import dev.deepnight.source.dawn.entity.SkeletonlordEntity;
import dev.deepnight.source.dawn.entity.Spell1Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class ProdragProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         double loop = 0.0;
         double particleAmount = 0.0;
         double xRadius = 0.0;
         double zRadius = 0.0;
         Vec3 _center = new Vec3(x, y, z);

         for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(20.0), e -> true)
            .stream()
            .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
            .collect(Collectors.toList())) {
            if (!(entityiterator instanceof Spell1Entity)
               && !(entityiterator instanceof GhostprojectileEntity)
               && !(entityiterator instanceof DraggingSpellEntity)
               && !(entityiterator instanceof SkeletonlordEntity)
               && !(false)
               && !(false)
               && !(false)) {
               if (entity.getPersistentData().getDouble("Move") == 0.0) {
                  entityiterator.setDeltaMovement(new Vec3((entity.getX() - entityiterator.getX()) / 8.8, 0.0, (entity.getZ() - entityiterator.getZ()) / 8.8));
                  entity.getPersistentData().putDouble("Move", 3.0);
               } else {
                  entity.getPersistentData().putDouble("Move", entity.getPersistentData().getDouble("Move") - 1.0);
               }
            }
         }

         if (entity.getPersistentData().getDouble("timer") == 0.0) {
            if (world instanceof Level _level) {
               if (!_level.isClientSide()) {
                  _level.playSound(
                     null,
                     BlockPos.containing(x, y, z),
                     dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_drag"),
                     SoundSource.NEUTRAL,
                     1.0F,
                     1.0F
                  );
               } else {
                  _level.playLocalSound(
                     x,
                     y,
                     z,
                     dev.deepnight.SafeSounds.resolve("simple_mobs:skeletonlord_drag"),
                     SoundSource.NEUTRAL,
                     1.0F,
                     1.0F,
                     false
                  );
               }
            }

            entity.getPersistentData().putDouble("timer", 41.0);
         } else {
            entity.getPersistentData().putDouble("timer", entity.getPersistentData().getDouble("timer") - 1.0);
         }

         if (entity.getPersistentData().getDouble("timer") == 1.0 && !entity.level().isClientSide()) {
            entity.discard();
         }
      }
   }
}
