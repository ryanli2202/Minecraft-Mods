package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.HerobrineEntity;
import dev.deepnight.source.dawn.entity.HerobrineSpike1Entity;
import dev.deepnight.source.dawn.entity.HerobrineSpike2Entity;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import dev.deepnight.source.SourceDamage;
import dev.deepnight.source.SourceDamage.StunType;

public class ProspikeProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (entity instanceof HerobrineSpike1Entity) {
            if (entity.getPersistentData().getDouble("timer") == 0.0) {
               if (world instanceof ServerLevel _level) {
                  _level.getServer()
                     .getCommands()
                     .performPrefixedCommand(
                        new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null)
                           .withSuppressedOutput(),
                        "/particle minecraft:block minecraft:bedrock ~ ~ ~ 3 .2 3 0 500"
                     );
               }

               if (world instanceof Level _level) {
                  if (!_level.isClientSide()) {
                     _level.playSound(
                        null,
                        BlockPos.containing(x, y, z),
                        dev.deepnight.SafeSounds.resolve("simple_mobs:sentinielground"),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F
                     );
                  } else {
                     _level.playLocalSound(
                        x,
                        y,
                        z,
                        dev.deepnight.SafeSounds.resolve("simple_mobs:sentinielground"),
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

            if (entity.getPersistentData().getDouble("timer") <= 35.0) {
               Vec3 _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (!(entityiterator instanceof HerobrineEntity) && entityiterator != entity) {
                     SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                        .setInitialPosition(entity.position())
                        .setStunType(StunType.LONG)
                        .setImpact(3.0F)
                        .setArmorNegation(25.0F);
                     entityiterator.hurt((DamageSource)dmgSrc, 30.0F);
                  }
               }
            }

            if (entity.getPersistentData().getDouble("timer") == 1.0) {
               if (world instanceof Level _level) {
                  if (!_level.isClientSide()) {
                     _level.playSound(
                        null,
                        BlockPos.containing(x, y, z),
                        dev.deepnight.SafeSounds.resolve("simple_mobs:sentinielground"),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F
                     );
                  } else {
                     _level.playLocalSound(
                        x,
                        y,
                        z,
                        dev.deepnight.SafeSounds.resolve("simple_mobs:sentinielground"),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F,
                        false
                     );
                  }
               }

               if (!entity.level().isClientSide()) {
                  entity.discard();
               }

               if (world instanceof ServerLevel _level) {
                  _level.getServer()
                     .getCommands()
                     .performPrefixedCommand(
                        new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null)
                           .withSuppressedOutput(),
                        "/particle minecraft:block minecraft:bedrock ~ ~ ~ 3 .2 3 0 500"
                     );
               }
            }
         } else if (entity instanceof HerobrineSpike2Entity) {
            if (entity.getPersistentData().getDouble("timer") == 0.0) {
               if (world instanceof ServerLevel _level) {
                  _level.getServer()
                     .getCommands()
                     .performPrefixedCommand(
                        new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null)
                           .withSuppressedOutput(),
                        "/particle minecraft:block minecraft:bedrock ~ ~ ~ 3 .2 3 0 500"
                     );
               }

               if (world instanceof Level _level) {
                  if (!_level.isClientSide()) {
                     _level.playSound(
                        null,
                        BlockPos.containing(x, y, z),
                        dev.deepnight.SafeSounds.resolve("simple_mobs:sentinielground"),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F
                     );
                  } else {
                     _level.playLocalSound(
                        x,
                        y,
                        z,
                        dev.deepnight.SafeSounds.resolve("simple_mobs:sentinielground"),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F,
                        false
                     );
                  }
               }

               entity.getPersistentData().putDouble("timer", 51.0);
            } else {
               entity.getPersistentData().putDouble("timer", entity.getPersistentData().getDouble("timer") - 1.0);
            }

            if (entity.getPersistentData().getDouble("timer") <= 42.0) {
               Vec3 _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.5), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (!(entityiterator instanceof HerobrineEntity) && entityiterator != entity) {
                     SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                        .setInitialPosition(entity.position())
                        .setStunType(StunType.LONG)
                        .setImpact(3.0F)
                        .setArmorNegation(25.0F);
                     entityiterator.hurt((DamageSource)dmgSrc, 20.0F);
                  }
               }
            } else if (entity.getPersistentData().getDouble("timer") <= 47.0) {
               Vec3 _center = new Vec3(x, y, z);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (!(entityiterator instanceof HerobrineEntity) && entityiterator != entity) {
                     SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                        .setInitialPosition(entity.position())
                        .setStunType(StunType.LONG)
                        .setImpact(3.0F)
                        .setArmorNegation(25.0F);
                     entityiterator.hurt((DamageSource)dmgSrc, 60.0F);
                  }
               }
            }

            if (entity.getPersistentData().getDouble("timer") == 1.0) {
               if (world instanceof Level _level) {
                  if (!_level.isClientSide()) {
                     _level.playSound(
                        null,
                        BlockPos.containing(x, y, z),
                        dev.deepnight.SafeSounds.resolve("simple_mobs:sentinielground"),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F
                     );
                  } else {
                     _level.playLocalSound(
                        x,
                        y,
                        z,
                        dev.deepnight.SafeSounds.resolve("simple_mobs:sentinielground"),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F,
                        false
                     );
                  }
               }

               if (!entity.level().isClientSide()) {
                  entity.discard();
               }

               if (world instanceof ServerLevel _level) {
                  _level.getServer()
                     .getCommands()
                     .performPrefixedCommand(
                        new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null)
                           .withSuppressedOutput(),
                        "/particle minecraft:block minecraft:bedrock ~ ~ ~ 3 .2 3 0 500"
                     );
               }
            }
         }
      }
   }
}
