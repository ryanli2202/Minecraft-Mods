package dev.deepnight.source;

import dev.deepnight.DeepNight;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;

@Mod.EventBusSubscriber(modid=DeepNight.ID)
public final class SourceStun {
    public static final DeferredRegister<MobEffect> EFFECTS=DeferredRegister.create(ForgeRegistries.MOB_EFFECTS,DeepNight.ID);
    public static final RegistryObject<MobEffect> STUN=EFFECTS.register("source_stun",()->new MobEffect(MobEffectCategory.HARMFUL,0x8178aa){}
        .addAttributeModifier(Attributes.MOVEMENT_SPEED,"e69cb170-e08d-49db-bc89-36e3d295ac07",-1,AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> TREMOR=EFFECTS.register("source_tremor",()->new MobEffect(MobEffectCategory.HARMFUL,0x9e886a){});
    public static final RegistryObject<MobEffect> FROZEN=EFFECTS.register("source_frozen",()->new MobEffect(MobEffectCategory.HARMFUL,0x9ad3ed){}
        .addAttributeModifier(Attributes.MOVEMENT_SPEED,"c599d9c3-769a-439f-a433-7d0fa2695d4c",-1,AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static boolean stunned(Entity entity){return entity instanceof LivingEntity living&&(living.hasEffect(STUN.get())||living.hasEffect(FROZEN.get()));}
    public static void apply(Entity entity,int ticks){
        if(entity instanceof LivingEntity living&&!entity.level().isClientSide&&!(entity instanceof Player p&&(p.isCreative()||p.isSpectator()))) {
            living.addEffect(new MobEffectInstance(STUN.get(),ticks,0,false,true));living.stopUsingItem();
            if(living instanceof Mob mob)mob.getNavigation().stop();
        }
    }
    @SubscribeEvent public static void damage(LivingHurtEvent event){
        if(event.getSource() instanceof SourceDamage source&&source.armorNegation>0){
            var victim=event.getEntity();float amount=event.getAmount(),fraction=Math.min(1,source.armorNegation/100);
            float armored=CombatRules.getDamageAfterAbsorb(amount,victim.getArmorValue(),(float)victim.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
            event.setAmount(amount*fraction+armored*(1-fraction));
        }
    }
    @SubscribeEvent public static void hit(LivingDamageEvent event){
        if(event.getAmount()>0&&event.getSource() instanceof SourceDamage source){
            int ticks=switch(source.stun){case NONE->0;case SHORT->8;case LONG->24;case HOLD->20;case KNOCKDOWN,FALL->40;case NEUTRALIZE->60;};
            if(ticks>0)apply(event.getEntity(),ticks+Math.min(10,(int)(source.impact/3)));
        }
    }
    @SubscribeEvent public static void tick(LivingEvent.LivingTickEvent event){
        LivingEntity entity=event.getEntity();
        if(stunned(entity)){
            var velocity=entity.getDeltaMovement();entity.setDeltaMovement(0,velocity.y,0);
            if(entity instanceof Mob mob){
                if(!mob.getPersistentData().contains("DeepNightStunAI"))mob.getPersistentData().putBoolean("DeepNightStunAI",mob.isNoAi());
                mob.setNoAi(true);mob.getNavigation().stop();
            }
        }else if(entity instanceof Mob mob&&mob.getPersistentData().contains("DeepNightStunAI")){
            mob.setNoAi(mob.getPersistentData().getBoolean("DeepNightStunAI"));mob.getPersistentData().remove("DeepNightStunAI");
        }
    }
    @SubscribeEvent public static void attack(LivingAttackEvent event){if(stunned(event.getSource().getEntity()))event.setCanceled(true);}
    @SubscribeEvent public static void swing(AttackEntityEvent event){if(stunned(event.getEntity()))event.setCanceled(true);}
    @SubscribeEvent public static void use(LivingEntityUseItemEvent.Start event){if(stunned(event.getEntity()))event.setCanceled(true);}
    @SubscribeEvent public static void jump(LivingEvent.LivingJumpEvent event){if(stunned(event.getEntity()))event.getEntity().setDeltaMovement(0,0,0);}
    public static void groundBreak(Entity entity,double radius){
        if(entity.level() instanceof ServerLevel level){
            var state=level.getBlockState(entity.blockPosition().below());
            level.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK,state),entity.getX(),entity.getY()+0.2,entity.getZ(),(int)(radius*30),radius,0.2,radius,0.15);
        }
    }
}
