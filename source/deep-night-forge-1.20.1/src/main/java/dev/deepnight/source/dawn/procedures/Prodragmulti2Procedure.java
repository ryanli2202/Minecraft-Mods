package dev.deepnight.source.dawn.procedures;

import java.util.Comparator;
import java.util.stream.Collectors;
import dev.deepnight.source.dawn.entity.FireDragonAmbientEntity;
import dev.deepnight.source.dawn.entity.IceDragonAmbientEntity;
import dev.deepnight.source.dawn.entity.LightningDragonAmbientEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class Prodragmulti2Procedure {
   public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
      if (entity != null) {
         entity.getPersistentData().putBoolean("despawn", true);
         if (entity.getPersistentData().getBoolean("firedragon")) {
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(10.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator instanceof FireDragonAmbientEntity) {
                  Entity _ent = entity;
                  _ent.setYRot(entityiterator.getYRot());
                  _ent.setXRot(entityiterator.getXRot());
                  _ent.setYBodyRot(_ent.getYRot());
                  _ent.setYHeadRot(_ent.getYRot());
                  _ent.yRotO = _ent.getYRot();
                  _ent.xRotO = _ent.getXRot();
                  if (_ent instanceof LivingEntity _entity) {
                     _entity.yBodyRotO = _entity.getYRot();
                     _entity.yHeadRotO = _entity.getYRot();
                  }

                  _ent = entity;
                  Vec3 look = entityiterator.getViewVector(1.0F);
                  _ent.teleportTo(entityiterator.getX() + 7.0 * look.x, entityiterator.getY() + 2.0, entityiterator.getZ() + 7.0 * look.z);
                  entity.getPersistentData().putBoolean("despawn", false);
               }
            }
         } else if (entity.getPersistentData().getBoolean("icedragon")) {
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(10.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator instanceof IceDragonAmbientEntity) {
                  Entity _ent = entity;
                  _ent.setYRot(entityiterator.getYRot());
                  _ent.setXRot(entityiterator.getXRot());
                  _ent.setYBodyRot(_ent.getYRot());
                  _ent.setYHeadRot(_ent.getYRot());
                  _ent.yRotO = _ent.getYRot();
                  _ent.xRotO = _ent.getXRot();
                  if (_ent instanceof LivingEntity _entity) {
                     _entity.yBodyRotO = _entity.getYRot();
                     _entity.yHeadRotO = _entity.getYRot();
                  }

                  _ent = entity;
                  Vec3 look = entityiterator.getViewVector(1.0F);
                  _ent.teleportTo(entityiterator.getX() + 8.0 * look.x, entityiterator.getY() + 4.0, entityiterator.getZ() + 8.0 * look.z);
                  entity.getPersistentData().putBoolean("despawn", false);
               }
            }
         } else if (entity.getPersistentData().getBoolean("lightningdragon")) {
            Vec3 _center = new Vec3(x, y, z);

            for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(10.0), e -> true)
               .stream()
               .sorted(dev.deepnight.source.SourceSupport.byDistance(_center))
               .collect(Collectors.toList())) {
               if (entityiterator instanceof LightningDragonAmbientEntity) {
                  Entity _ent = entity;
                  _ent.setYRot(entityiterator.getYRot());
                  _ent.setXRot(entityiterator.getXRot());
                  _ent.setYBodyRot(_ent.getYRot());
                  _ent.setYHeadRot(_ent.getYRot());
                  _ent.yRotO = _ent.getYRot();
                  _ent.xRotO = _ent.getXRot();
                  if (_ent instanceof LivingEntity _entity) {
                     _entity.yBodyRotO = _entity.getYRot();
                     _entity.yHeadRotO = _entity.getYRot();
                  }

                  _ent = entity;
                  Vec3 look = entityiterator.getViewVector(1.0F);
                  _ent.teleportTo(entityiterator.getX() + 8.0 * look.x, entityiterator.getY() + 4.0, entityiterator.getZ() + 8.0 * look.z);
                  entity.getPersistentData().putBoolean("despawn", false);
               }
            }
         }

         if (entity.getPersistentData().getDouble("despawncd") == 0.0) {
            entity.getPersistentData().putDouble("despawncd", 20.0);
         } else {
            entity.getPersistentData().putDouble("despawncd", entity.getPersistentData().getDouble("despawncd") - 1.0);
         }

         if (entity.getPersistentData().getDouble("despawncd") == 0.0 && entity.getPersistentData().getBoolean("despawn") && !entity.level().isClientSide()) {
            entity.discard();
         }
      }
   }
}
