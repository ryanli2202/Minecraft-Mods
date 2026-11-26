package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.CorruptedOgreEntity;
import dev.deepnight.source.dawn.entity.LostEyeOgreEntity;
import dev.deepnight.source.dawn.entity.LosteyeEffect2Entity;
import dev.deepnight.source.dawn.init.SimpleMobsModParticleTypes;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class Proeffect2Procedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (entity.getPersistentData().getDouble("a") == 0.0) {
            entity.getPersistentData().putDouble("a", 40.0);
         } else {
            entity.getPersistentData().putDouble("a", entity.getPersistentData().getDouble("a") - 1.0);
         }

         if (world instanceof ServerLevel _level) {
            _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.LOSTEYEPARTICLE.get(), x, y, z, 1, 0.6, 1.2, 0.6, 1.0);
         }

         if (entity.getPersistentData().getDouble("a") <= 35.0 && entity.getPersistentData().getDouble("a") >= 15.0 && world instanceof ServerLevel _level) {
            _level.getServer()
               .getCommands()
               .performPrefixedCommand(
                  new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null)
                     .withSuppressedOutput(),
                  "/particle dust 1.000 0.100 0.100 3 ~ ~ ~ 0.5 0.5 0.5 0 1 force"
               );
         }

         if (entity.getPersistentData().getDouble("a") <= 35.0 && entity.getPersistentData().getDouble("a") >= 2.0) {
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (!(entityiterator instanceof LostEyeOgreEntity)
                  && !(entityiterator instanceof CorruptedOgreEntity)
                  && !(entityiterator instanceof LosteyeEffect2Entity)) {
                  entityiterator.hurt(new DamageSource(entityiterator.level().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(net.minecraft.world.damagesource.DamageTypes.GENERIC), entity), 3.0F);
               }
            }
         } else if (entity.getPersistentData().getDouble("a") == 1.0 && !entity.level().isClientSide()) {
            entity.discard();
         }
      }
   }
}
