package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.Korok1Entity;
import dev.deepnight.source.dawn.entity.Korok2Entity;
import dev.deepnight.source.dawn.entity.Korok3Entity;
import dev.deepnight.source.dawn.entity.Korok4Entity;
import dev.deepnight.source.dawn.entity.Korok5Entity;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.init.SimpleMobsModParticleTypes;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
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

public class ProelementaldeathProcedure {
   public static void execute(LevelAccessor world, final double x, final double y, final double z) {
      if (!(world instanceof ServerLevel)) return;
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
            public void tick_ProelementaldeathProcedure_1(ServerTickEvent event) {
               if (event.phase == Phase.END) {
                  this.ticks++;
                  if (this.ticks >= this.waitTicks) {
                     this.run();
                  }
               }
            }

            private void run() {
               if (this.world instanceof Level _level) {
                  if (!_level.isClientSide()) {
                     _level.playSound(
                        null,
                        BlockPos.containing(x, y, z),
                        dev.deepnight.SafeSounds.resolve("item.totem.use"),
                        SoundSource.NEUTRAL,
                        3.0F,
                        2.0F
                     );
                  } else {
                     _level.playLocalSound(
                        x, y, z, dev.deepnight.SafeSounds.resolve("item.totem.use"), SoundSource.NEUTRAL, 3.0F, 2.0F, false
                     );
                  }
               }

               if (this.world instanceof Level _level) {
                  if (!_level.isClientSide()) {
                     _level.playSound(
                        null,
                        BlockPos.containing(x, y, z),
                        dev.deepnight.SafeSounds.resolve("entity.firework_rocket.large_blast"),
                        SoundSource.NEUTRAL,
                        3.0F,
                        1.0F
                     );
                  } else {
                     _level.playLocalSound(
                        x,
                        y,
                        z,
                        dev.deepnight.SafeSounds.resolve("entity.firework_rocket.large_blast"),
                        SoundSource.NEUTRAL,
                        3.0F,
                        1.0F,
                        false
                     );
                  }
               }

               if (this.world instanceof Level _level) {
                  if (!_level.isClientSide()) {
                     _level.playSound(
                        null,
                        BlockPos.containing(x, y, z),
                        dev.deepnight.SafeSounds.resolve("entity.lightning_bolt.thunder"),
                        SoundSource.NEUTRAL,
                        3.0F,
                        1.0F
                     );
                  } else {
                     _level.playLocalSound(
                        x,
                        y,
                        z,
                        dev.deepnight.SafeSounds.resolve("entity.lightning_bolt.thunder"),
                        SoundSource.NEUTRAL,
                        3.0F,
                        1.0F,
                        false
                     );
                  }
               }

               if (this.world instanceof ServerLevel _level) {
                  LightningBolt entityToSpawn = EntityType.LIGHTNING_BOLT.create(_level);
                  entityToSpawn.moveTo(Vec3.atBottomCenterOf(BlockPos.containing(x, y, z)));
                  entityToSpawn.setVisualOnly(true);
                  _level.addFreshEntity(entityToSpawn);
               }

               if (this.world instanceof ServerLevel _level) {
                  _level.getServer()
                     .getCommands()
                     .performPrefixedCommand(
                        new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null)
                           .withSuppressedOutput(),
                        "/tellraw @a[distance=..80] {\"text\":\"Congratulations! You have defeated the elemental deity. The world's balance is once again restored... You can right click on one of the dancing koroks to go back.\",\"bold\":true}"
                     );
               }

               if (this.world instanceof ServerLevel _level) {
                  _level.getServer()
                     .getCommands()
                     .performPrefixedCommand(
                        new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null)
                           .withSuppressedOutput(),
                        "/title @a[distance=..80] times 20 40 20"
                     );
               }

               if (this.world instanceof ServerLevel _level) {
                  _level.getServer()
                     .getCommands()
                     .performPrefixedCommand(
                        new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null)
                           .withSuppressedOutput(),
                        "/title @a[distance=..80] subtitle {\"text\":\"You have defeated the corrupted deity.\",\"bold\":true,\"italic\":true}"
                     );
               }

               if (this.world instanceof ServerLevel _level) {
                  _level.getServer()
                     .getCommands()
                     .performPrefixedCommand(
                        new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null)
                           .withSuppressedOutput(),
                        "/title @a[distance=..80] title {\"text\":\"\"}"
                     );
               }

               if (this.world instanceof ServerLevel _level) {
                  _level.sendParticles(ParticleTypes.FLASH, x, y + 3.0, z, 20, 1.0, 1.0, 1.0, 0.3);
               }

               if (this.world instanceof ServerLevel _level) {
                  _level.sendParticles((SimpleParticleType)SimpleMobsModParticleTypes.SNOWYCLOUD.get(), x, y, z, 100, 3.0, 3.0, 3.0, 0.3);
               }

               Vec3 _center = new Vec3(x, y, z);

               for (Entity entityiterator : this.world
                  .getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(25.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (entityiterator instanceof ServerPlayer _player) {
                     dev.deepnight.source.SourceAdvancements.award(_player, "simple_mobs:killelementaldeity");
                  }
               }

               if (this.world instanceof ServerLevel _level) {
                  Entity entityToSpawn = new Korok1Entity(SimpleMobsModEntities.KOROK_1.get(), _level);
                  entityToSpawn.moveTo(
                     x + Mth.nextInt(net.minecraft.util.RandomSource.create(), -7, 7), y, z + Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, 5), this.world.getRandom().nextFloat() * 360.0F, 0.0F
                  );
                  if (entityToSpawn instanceof Mob _mobToSpawn) {
                     _mobToSpawn.finalizeSpawn(_level, this.world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                  }

                  this.world.addFreshEntity(entityToSpawn);
               }

               if (this.world instanceof ServerLevel _level) {
                  Entity entityToSpawn = new Korok1Entity(SimpleMobsModEntities.KOROK_1.get(), _level);
                  entityToSpawn.moveTo(
                     x + Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, 5), y, z + Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, 5), this.world.getRandom().nextFloat() * 360.0F, 0.0F
                  );
                  if (entityToSpawn instanceof Mob _mobToSpawn) {
                     _mobToSpawn.finalizeSpawn(_level, this.world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                  }

                  this.world.addFreshEntity(entityToSpawn);
               }

               if (this.world instanceof ServerLevel _level) {
                  Entity entityToSpawn = new Korok2Entity(SimpleMobsModEntities.KOROK_2.get(), _level);
                  entityToSpawn.moveTo(
                     x + Mth.nextInt(net.minecraft.util.RandomSource.create(), -7, 7), y, z + Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, 5), this.world.getRandom().nextFloat() * 360.0F, 0.0F
                  );
                  if (entityToSpawn instanceof Mob _mobToSpawn) {
                     _mobToSpawn.finalizeSpawn(_level, this.world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                  }

                  this.world.addFreshEntity(entityToSpawn);
               }

               if (this.world instanceof ServerLevel _level) {
                  Entity entityToSpawn = new Korok2Entity(SimpleMobsModEntities.KOROK_2.get(), _level);
                  entityToSpawn.moveTo(
                     x + Mth.nextInt(net.minecraft.util.RandomSource.create(), -7, 7), y, z + Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, 5), this.world.getRandom().nextFloat() * 360.0F, 0.0F
                  );
                  if (entityToSpawn instanceof Mob _mobToSpawn) {
                     _mobToSpawn.finalizeSpawn(_level, this.world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                  }

                  this.world.addFreshEntity(entityToSpawn);
               }

               if (this.world instanceof ServerLevel _level) {
                  Entity entityToSpawn = new Korok3Entity(SimpleMobsModEntities.KOROK_3.get(), _level);
                  entityToSpawn.moveTo(
                     x + Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, 5), y, z + Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, 5), this.world.getRandom().nextFloat() * 360.0F, 0.0F
                  );
                  if (entityToSpawn instanceof Mob _mobToSpawn) {
                     _mobToSpawn.finalizeSpawn(_level, this.world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                  }

                  this.world.addFreshEntity(entityToSpawn);
               }

               if (this.world instanceof ServerLevel _level) {
                  Entity entityToSpawn = new Korok3Entity(SimpleMobsModEntities.KOROK_3.get(), _level);
                  entityToSpawn.moveTo(
                     x + Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, 5), y, z + Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, 5), this.world.getRandom().nextFloat() * 360.0F, 0.0F
                  );
                  if (entityToSpawn instanceof Mob _mobToSpawn) {
                     _mobToSpawn.finalizeSpawn(_level, this.world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                  }

                  this.world.addFreshEntity(entityToSpawn);
               }

               if (this.world instanceof ServerLevel _level) {
                  Entity entityToSpawn = new Korok4Entity(SimpleMobsModEntities.KOROK_4.get(), _level);
                  entityToSpawn.moveTo(
                     x + Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, 5), y, z + Mth.nextInt(net.minecraft.util.RandomSource.create(), -7, 7), this.world.getRandom().nextFloat() * 360.0F, 0.0F
                  );
                  if (entityToSpawn instanceof Mob _mobToSpawn) {
                     _mobToSpawn.finalizeSpawn(_level, this.world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                  }

                  this.world.addFreshEntity(entityToSpawn);
               }

               if (this.world instanceof ServerLevel _level) {
                  Entity entityToSpawn = new Korok4Entity(SimpleMobsModEntities.KOROK_4.get(), _level);
                  entityToSpawn.moveTo(
                     x + Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, 5), y, z + Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, 5), this.world.getRandom().nextFloat() * 360.0F, 0.0F
                  );
                  if (entityToSpawn instanceof Mob _mobToSpawn) {
                     _mobToSpawn.finalizeSpawn(_level, this.world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                  }

                  this.world.addFreshEntity(entityToSpawn);
               }

               if (this.world instanceof ServerLevel _level) {
                  Entity entityToSpawn = new Korok5Entity(SimpleMobsModEntities.KOROK_5.get(), _level);
                  entityToSpawn.moveTo(
                     x + Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, 5), y, z + Mth.nextInt(net.minecraft.util.RandomSource.create(), -7, 7), this.world.getRandom().nextFloat() * 360.0F, 0.0F
                  );
                  if (entityToSpawn instanceof Mob _mobToSpawn) {
                     _mobToSpawn.finalizeSpawn(_level, this.world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                  }

                  this.world.addFreshEntity(entityToSpawn);
               }

               if (this.world instanceof ServerLevel _level) {
                  Entity entityToSpawn = new Korok5Entity(SimpleMobsModEntities.KOROK_5.get(), _level);
                  entityToSpawn.moveTo(
                     x + Mth.nextInt(net.minecraft.util.RandomSource.create(), -7, 7), y, z + Mth.nextInt(net.minecraft.util.RandomSource.create(), -5, 5), this.world.getRandom().nextFloat() * 360.0F, 0.0F
                  );
                  if (entityToSpawn instanceof Mob _mobToSpawn) {
                     _mobToSpawn.finalizeSpawn(_level, this.world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                  }

                  this.world.addFreshEntity(entityToSpawn);
               }

               MinecraftForge.EVENT_BUS.unregister(this);
            }
         })
         .start(world, 102);
   }
}
