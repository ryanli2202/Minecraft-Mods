package dev.deepnight.source;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

/** Source move damage metadata, independent of an animation/combat overhaul. */
public final class SourceDamage extends DamageSource {
    public enum StunType { NONE, SHORT, LONG, HOLD, KNOCKDOWN, NEUTRALIZE, FALL }
    public StunType stun=StunType.NONE;
    public float impact, armorNegation;
    private Vec3 origin;
    private SourceDamage(LivingEntity attacker) {super(attacker.damageSources().mobAttack(attacker).typeHolder(),attacker);}
    public static SourceDamage mob(LivingEntity attacker){return new SourceDamage(attacker);}
    public SourceDamage setInitialPosition(Vec3 position){origin=position;return this;}
    public SourceDamage setStunType(StunType value){stun=value;return this;}
    public SourceDamage setImpact(float value){impact=value;return this;}
    public SourceDamage setArmorNegation(float value){armorNegation=value;return this;}
    @Override public Vec3 getSourcePosition(){return origin!=null?origin:super.getSourcePosition();}
    @Override public boolean is(TagKey<DamageType> tag){return tag==DamageTypeTags.BYPASSES_ARMOR&&armorNegation>0||super.is(tag);}
}
