package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.BlackholeEntity;
import dev.deepnight.source.dawn.entity.ElementalAttack1Entity;
import dev.deepnight.source.dawn.entity.ElementalAttack2Entity;
import dev.deepnight.source.dawn.entity.ElementalChainEntity;
import dev.deepnight.source.dawn.entity.ElementalDeityEntity;
import dev.deepnight.source.dawn.entity.ElementalProjectileEntity;
import dev.deepnight.source.dawn.entity.ElementalShieldEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class ProblackholeProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         double loop = 0.0;
         double particleAmount = 0.0;
         double xRadius = 0.0;
         double zRadius = 0.0;
         if (world instanceof ServerLevel _level) {
            _level.sendParticles(ParticleTypes.SQUID_INK, x, y, z, 1, 2.0, 2.0, 2.0, 1.0);
         }

         if (world instanceof ServerLevel _level) {
            _level.sendParticles(ParticleTypes.REVERSE_PORTAL, x, y, z, 1, 2.0, 2.0, 2.0, 1.0);
         }

         Vec3 _center = new Vec3(x, y, z);

         for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.0), e -> true)
            .stream()
            .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
            .collect(Collectors.toList())) {
            entityiterator.hurt(new DamageSource(entityiterator.level().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(net.minecraft.world.damagesource.DamageTypes.GENERIC), entity), 3.0F);
         }

         _center = new Vec3(x, y, z);

         for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(15.0), e -> true)
            .stream()
            .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
            .collect(Collectors.toList())) {
            if (!(entityiterator instanceof BlackholeEntity)
               && !(entityiterator instanceof ElementalDeityEntity)
               && !(entityiterator instanceof ElementalAttack1Entity)
               && !(entityiterator instanceof ElementalAttack2Entity)
               && !(entityiterator instanceof ElementalChainEntity)
               && !(entityiterator instanceof ElementalProjectileEntity)
               && !(entityiterator instanceof ElementalShieldEntity)) {
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
                     dev.deepnight.SafeSounds.resolve("simple_mobs:blackhole"),
                     SoundSource.NEUTRAL,
                     3.0F,
                     1.0F
                  );
               } else {
                  _level.playLocalSound(
                     x, y, z, dev.deepnight.SafeSounds.resolve("simple_mobs:blackhole"), SoundSource.NEUTRAL, 3.0F, 1.0F, false
                  );
               }
            }

            entity.getPersistentData().putDouble("timer", 81.0);
         } else {
            entity.getPersistentData().putDouble("timer", entity.getPersistentData().getDouble("timer") - 1.0);
         }

         if (entity.getPersistentData().getDouble("timer") == 1.0 && !entity.level().isClientSide()) {
            entity.discard();
         }
      }
   }
}
