package dev.deepnight.source.dawn.entity;

import java.util.Random;
import dev.deepnight.source.dawn.init.SimpleMobsModEntities;
import dev.deepnight.source.dawn.procedures.ProseedProcedure;
import dev.deepnight.source.dawn.procedures.ProseedshotProcedure;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages.SpawnEntity;
import net.minecraftforge.registries.ForgeRegistries;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class SeedGunEntity extends AbstractArrow implements ItemSupplier {
   public SeedGunEntity(SpawnEntity packet, Level world) {
      super(SimpleMobsModEntities.SEED_GUN.get(), world);
   }

   public SeedGunEntity(EntityType<? extends SeedGunEntity> type, Level world) {
      super(type, world);
   }

   public SeedGunEntity(EntityType<? extends SeedGunEntity> type, double x, double y, double z, Level world) {
      super(type, x, y, z, world);
   }

   public SeedGunEntity(EntityType<? extends SeedGunEntity> type, LivingEntity entity, Level world) {
      super(type, entity, world);
   }

   @Override
   public Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public ItemStack getItem() {
      return ItemStack.EMPTY;
   }

   @Override
   protected ItemStack getPickupItem() {
      return ItemStack.EMPTY;
   }

   @Override
   protected void doPostHurtEffects(LivingEntity entity) {
      super.doPostHurtEffects(entity);
      entity.setArrowCount(entity.getArrowCount() - 1);
   }

   @Override
   public void onHitEntity(EntityHitResult entityHitResult) {
      super.onHitEntity(entityHitResult);
      ProseedshotProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ());
   }

   @Override
   public void onHitBlock(BlockHitResult blockHitResult) {
      super.onHitBlock(blockHitResult);
      ProseedshotProcedure.execute(this.level(), blockHitResult.getBlockPos().getX(), blockHitResult.getBlockPos().getY(), blockHitResult.getBlockPos().getZ());
   }

   @Override
   public void tick() {
      super.tick();
      ProseedProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ());
      if (this.inGround) {
         this.discard();
      }
   }

   public static SeedGunEntity shoot(Level world, LivingEntity entity, net.minecraft.util.RandomSource random, float power, double damage, int knockback) {
      SeedGunEntity entityarrow = new SeedGunEntity(SimpleMobsModEntities.SEED_GUN.get(), entity, world);
      entityarrow.shoot(entity.getViewVector(1.0F).x, entity.getViewVector(1.0F).y, entity.getViewVector(1.0F).z, power * 2.0F, 0.0F);
      entityarrow.setSilent(true);
      entityarrow.setCritArrow(false);
      entityarrow.setBaseDamage(damage);
      entityarrow.setKnockback(knockback);
      world.addFreshEntity(entityarrow);
      world.playSound(
         null,
         entity.getX(),
         entity.getY(),
         entity.getZ(),
         dev.deepnight.SafeSounds.resolve("block.spore_blossom.break"),
         SoundSource.PLAYERS,
         1.0F,
         1.0F / (random.nextFloat() * 0.5F + 1.0F) + power / 2.0F
      );
      return entityarrow;
   }

   public static SeedGunEntity shoot(LivingEntity entity, LivingEntity target) {
      SeedGunEntity entityarrow = new SeedGunEntity(SimpleMobsModEntities.SEED_GUN.get(), entity, entity.level());
      double dx = target.getX() - entity.getX();
      double dy = target.getY() + target.getEyeHeight() - 1.1;
      double dz = target.getZ() - entity.getZ();
      entityarrow.shoot(dx, dy - entityarrow.getY() + Math.hypot(dx, dz) * 0.2F, dz, 4.0F, 12.0F);
      entityarrow.setSilent(true);
      entityarrow.setBaseDamage(3.0);
      entityarrow.setKnockback(1);
      entityarrow.setCritArrow(false);
      entity.level().addFreshEntity(entityarrow);
      entity.level()
         .playSound(
            null,
            entity.getX(),
            entity.getY(),
            entity.getZ(),
            dev.deepnight.SafeSounds.resolve("block.spore_blossom.break"),
            SoundSource.PLAYERS,
            1.0F,
            1.0F / (net.minecraft.util.RandomSource.create().nextFloat() * 0.5F + 1.0F)
         );
      return entityarrow;
   }
}
