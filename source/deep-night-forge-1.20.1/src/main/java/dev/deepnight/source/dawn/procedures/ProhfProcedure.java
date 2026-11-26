package dev.deepnight.source.dawn.procedures;


import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.HerobrineEntity;
import dev.deepnight.source.dawn.entity.HerobrineFearEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import dev.deepnight.source.SourceDamage;
import dev.deepnight.source.SourceDamage.StunType;

public class ProhfProcedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         double loop = 0.0;
         double particleAmount = 0.0;
         double xRadius = 0.0;
         double zRadius = 0.0;
         if (entity.getPersistentData().getBoolean("focus")) {
            Entity _ent = entity;
            _ent.setYRot((float)entity.getPersistentData().getDouble("yaw"));
            _ent.setXRot(entity.getXRot());
            _ent.setYBodyRot(_ent.getYRot());
            _ent.setYHeadRot(_ent.getYRot());
            _ent.yRotO = _ent.getYRot();
            _ent.xRotO = _ent.getXRot();
            if (_ent instanceof LivingEntity _entity) {
               _entity.yBodyRotO = _entity.getYRot();
               _entity.yHeadRotO = _entity.getYRot();
            }
         }

         if (entity.getPersistentData().getBoolean("lock")) {
            Entity _ent = entity;
            _ent.setYRot((float)entity.getPersistentData().getDouble("yaw"));
            _ent.setXRot(entity.getXRot());
            _ent.setYBodyRot(_ent.getYRot());
            _ent.setYHeadRot(_ent.getYRot());
            _ent.yRotO = _ent.getYRot();
            _ent.xRotO = _ent.getXRot();
            if (_ent instanceof LivingEntity _entity) {
               _entity.yBodyRotO = _entity.getYRot();
               _entity.yHeadRotO = _entity.getYRot();
            }
         }

         if (!world.isClientSide()) {
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(24.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null)) {
                  entity.getPersistentData()
                     .putDouble("atanyaw", Math.toDegrees(Math.atan2(entityiterator.getX() - entity.getX(), entityiterator.getZ() - entity.getZ())));
                  if (entity.getPersistentData().getDouble("atanyaw") < 0.0) {
                     entity.getPersistentData().putDouble("yaw2", 0.0 - entity.getPersistentData().getDouble("atanyaw"));
                  }

                  if (entity.getPersistentData().getDouble("atanyaw") >= 0.0) {
                     entity.getPersistentData().putDouble("yaw2", 360.0 - entity.getPersistentData().getDouble("atanyaw"));
                  }
               }
            }

            if (entity.getPersistentData().getDouble("timer") == 28.0) {
               entity.getPersistentData().putDouble("yaw", entity.getPersistentData().getDouble("yaw2"));
               entity.getPersistentData().putBoolean("lock", true);
               entity.getPersistentData().putBoolean("noai", true);
            }

            if (entity.getPersistentData().getDouble("timer") == 0.0) {
               entity.getPersistentData().putDouble("timer", 41.0);
               entity.getPersistentData().putBoolean("focus", true);
               if (Math.random() >= 0.5) {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.LEATHER_HELMET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                  }
               } else {
                  Entity _entity = entity;
                  if (_entity instanceof Player _player) {
                     _player.getInventory().armor.set(3, new ItemStack(Items.GOLD_NUGGET));
                     _player.getInventory().setChanged();
                  } else if (_entity instanceof LivingEntity _living) {
                     _living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLD_NUGGET));
                  }
               }
            } else {
               entity.getPersistentData().putDouble("timer", entity.getPersistentData().getDouble("timer") - 1.0);
            }

            if (entity.getPersistentData().getDouble("timer") <= 22.0 && entity.getPersistentData().getDouble("timer") >= 18.0) {
               _center = new Vec3(x + entity.getLookAngle().x * 10.0, y, z + entity.getLookAngle().z * 10.0);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(10.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (!(entityiterator instanceof HerobrineFearEntity) && entityiterator instanceof LivingEntity _entity) {
                     _entity.addEffect(new MobEffectInstance((MobEffect)dev.deepnight.source.SourceStun.TREMOR.get(), 30, 2, false, false));
                  }
               }

               _center = new Vec3(x + entity.getLookAngle().x * 10.0, y, z + entity.getLookAngle().z * 10.0);

               for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(6.0), e -> true)
                  .stream()
                  .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
                  .collect(Collectors.toList())) {
                  if (!(entityiterator instanceof HerobrineFearEntity) && !(entityiterator instanceof HerobrineEntity)) {
                     SourceDamage dmgSrc = SourceDamage.mob((LivingEntity)entity)
                        .setInitialPosition(entity.position())
                        .setStunType(StunType.LONG)
                        .setImpact(3.0F)
                        .setArmorNegation(25.0F);
                     entityiterator.hurt((DamageSource)dmgSrc, 40.0F);
                  }
               }
            }

            if (entity.getPersistentData().getDouble("timer") == 1.0 && !entity.level().isClientSide()) {
               entity.discard();
            }
         }
      }
   }
}
