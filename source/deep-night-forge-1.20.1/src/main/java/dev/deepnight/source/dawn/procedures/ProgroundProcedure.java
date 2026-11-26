package dev.deepnight.source.dawn.procedures;


import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.SentinelKnightEntity;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import dev.deepnight.source.SourceDamage;
import dev.deepnight.source.SourceDamage.StunType;

public class ProgroundProcedure {
   public static void execute(LevelAccessor world, final double x, final double y, final double z, final Entity entity) {
      if (entity != null) {
         Vec3 _center = new Vec3(x + entity.getLookAngle().x * 3.0, y, z + entity.getLookAngle().z * 3.0);

         for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.0), e -> true)
            .stream()
            .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
            .collect(Collectors.toList())) {
            if (!entityiterator.getPersistentData().getBoolean("spikesowner") && !(entityiterator instanceof SentinelKnightEntity) && entityiterator != entity) {
               if ((entityiterator instanceof Player || entityiterator instanceof ServerPlayer) && entityiterator instanceof LivingEntity _entity) {
                  _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 20, 1, false, false));
               }

               SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                  .setInitialPosition(entity.position())
                  .setStunType(StunType.LONG)
                  .setImpact(1.0F)
                  .setArmorNegation(5.0F);
               entityiterator.hurt((DamageSource)dmgSrc, 10.0F);
            }
         }

         if (world instanceof ServerLevel _level) {
            _level.getServer()
               .getCommands()
               .performPrefixedCommand(
                  new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null)
                     .withSuppressedOutput(),
                  "/particle minecraft:block minecraft:stone ~ ~ ~ 1.6 .1 1.6 0 500"
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
                  x, y, z, dev.deepnight.SafeSounds.resolve("simple_mobs:sentinielground"), SoundSource.NEUTRAL, 1.0F, 1.0F, false
               );
            }
         }

         (new Object() {
               private int ticks = 0;
               private float waitTicks;
               private LevelAccessor world;

               public void start(LevelAccessor world, int waitTicks) {
                  this.waitTicks = waitTicks;
                  MinecraftForge.EVENT_BUS.register(this);
                  this.world = world;
               }

               @SubscribeEvent
               public void tick_ProgroundProcedure_1(ServerTickEvent event) {
                  if (event.phase == Phase.END) {
                     this.ticks++;
                     if (this.ticks >= this.waitTicks) {
                        this.run();
                     }
                  }
               }

               private void run() {
                  if (!entity.level().isClientSide()) {
                     entity.discard();
                  }

                  if (this.world instanceof ServerLevel _level) {
                     _level.getServer()
                        .getCommands()
                        .performPrefixedCommand(
                           new CommandSourceStack(
                                 CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null
                              )
                              .withSuppressedOutput(),
                           "/particle minecraft:block minecraft:stone ~ ~ ~ 1.6 .1 1.6 0 500"
                        );
                  }

                  MinecraftForge.EVENT_BUS.unregister(this);
               }
            })
            .start(world, 30);
      }
   }
}
