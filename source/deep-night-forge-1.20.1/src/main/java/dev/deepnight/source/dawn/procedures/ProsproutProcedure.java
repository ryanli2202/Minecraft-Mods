package dev.deepnight.source.dawn.procedures;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class ProsproutProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         if (world instanceof ServerLevel _level) {
            _level.getServer()
               .getCommands()
               .performPrefixedCommand(
                  new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null)
                     .withSuppressedOutput(),
                  "/particle minecraft:block minecraft:dirt ~ ~ ~ 0 0 0 0.2 3 force"
               );
         }

         if (entity.onGround()) {
            if (world.getBlockState(BlockPos.containing(x, y - 1.0, z)).is(BlockTags.create(new ResourceLocation("minecraft:dirt")))) {
               world.setBlock(BlockPos.containing(x, y, z), Blocks.OAK_SAPLING.defaultBlockState(), 3);
            }

            if (!entity.level().isClientSide()) {
               entity.discard();
            }
         }
      }
   }
}
