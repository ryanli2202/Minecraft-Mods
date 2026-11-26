package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.FireGiantEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class ProleghurtProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity, float amount) {
      if (sourceentity != null) {
         boolean ok = false;
         Vec3 _center = new Vec3(x, y, z);

         for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.0), e -> true)
            .stream()
            .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
            .collect(Collectors.toList())) {
            if (entityiterator == sourceentity) {
               ok = true;
            }

            if (ok && entityiterator instanceof FireGiantEntity) {
               if (world instanceof Level _level) {
                  if (!_level.isClientSide()) {
                     _level.playSound(
                        null,
                        BlockPos.containing(x, y, z),
                        dev.deepnight.SafeSounds.resolve("simple_mobs:leghurt"),
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F
                     );
                  } else {
                     _level.playLocalSound(
                        x, y, z, dev.deepnight.SafeSounds.resolve("simple_mobs:leghurt"), SoundSource.NEUTRAL, 1.0F, 1.0F, false
                     );
                  }
               }

               DamageSource dmgSrc = entity instanceof LivingEntity ? sourceentity.damageSources().mobAttack((LivingEntity)sourceentity) : entity.damageSources().generic();
               entityiterator.hurt(dmgSrc, amount * 2.0F);
               entityiterator.getPersistentData().putBoolean("leghurt", true);
               Entity _entity = entityiterator;
               if (_entity instanceof Player _player) {
                  _player.getInventory().armor.set(0, new ItemStack(Items.LEATHER_BOOTS));
                  _player.getInventory().setChanged();
               } else if (_entity instanceof LivingEntity _living) {
                  _living.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
               }
            }
         }
      }
   }
}
