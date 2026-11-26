package dev.travellerscompanions;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

/** A frontal guard with recovery between parries; it never makes Yuriko invulnerable. */
public final class KatanaGuard {
    private final CompanionEntity entity;
    private int guardTicks,cooldown;
    public int blocks,deflections;
    public KatanaGuard(CompanionEntity entity){this.entity=entity;}
    public boolean hasKatana(){var id=ForgeRegistries.ITEMS.getKey(entity.getMainHandItem().getItem());return id!=null&&id.getPath().contains("katana");}
    private boolean available(){return entity.kind()==CharacterKind.YURIKO&&entity.stance()!=2&&entity.isAlive()&&!entity.parkour.busy()&&hasKatana();}
    public void tick(){
        if(cooldown>0)cooldown--;
        if(guardTicks>0)guardTicks--;
        if(!available())guardTicks=0;
        entity.setGuarding(guardTicks>0);
    }
    private boolean inFront(Vec3 source){var delta=source.subtract(entity.position()).multiply(1,0,1);var look=entity.getLookAngle().multiply(1,0,1);return delta.lengthSqr()<.001||look.normalize().dot(delta.normalize())>.15;}
    private boolean prepare(LivingEntity attacker,Vec3 source){
        if(!available()||!entity.canAttack(attacker)||!inFront(source))return false;
        if(guardTicks<=0){if(cooldown>0)return false;guardTicks=8;cooldown=28;entity.interruptAttack();entity.setGuarding(true);}
        return true;
    }
    private void impact(){entity.playSound(SoundEvents.SHIELD_BLOCK,.8f,1.35f);entity.getMainHandItem().hurtAndBreak(1,entity,e->e.broadcastBreakEvent(EquipmentSlot.MAINHAND));entity.checkpoint();}
    public boolean block(DamageSource source){
        if(source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)||source.is(DamageTypeTags.BYPASSES_SHIELD)||source.is(DamageTypeTags.IS_EXPLOSION)||!(source.getEntity() instanceof LivingEntity attacker))return false;
        if(source.getDirectEntity() instanceof Projectile projectile)return deflect(projectile);
        if(!prepare(attacker,attacker.position()))return false;
        blocks++;impact();attacker.knockback(.45,entity.getX()-attacker.getX(),entity.getZ()-attacker.getZ());entity.setTarget(attacker);return true;
    }
    public boolean deflect(Projectile projectile){
        if(!(projectile.getOwner() instanceof LivingEntity attacker)||!prepare(attacker,projectile.position()))return false;
        Vec3 direction=attacker.getEyePosition().subtract(entity.getEyePosition()).normalize();if(direction.lengthSqr()<.01)direction=entity.getLookAngle();
        float speed=(float)Math.max(1.5,Math.min(4,projectile.getDeltaMovement().length()));
        projectile.setOwner(entity);projectile.setPos(entity.getEyePosition().add(direction.scale(1.2)));projectile.shoot(direction.x,direction.y,direction.z,speed,0);
        if(projectile instanceof AbstractHurtingProjectile fireball){fireball.xPower=direction.x*.1;fireball.yPower=direction.y*.1;fireball.zPower=direction.z*.1;}
        if(projectile instanceof AbstractArrow arrow)arrow.pickup=AbstractArrow.Pickup.DISALLOWED;
        projectile.hurtMarked=true;deflections++;impact();entity.setTarget(attacker);return true;
    }
}
