package dev.deepnight;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.UUID;
import static dev.deepnight.NightEnchantment.Type.*;

@Mod.EventBusSubscriber(modid=DeepNight.ID)
public final class EnchantmentEvents {
    private static final UUID VITALITY_ID=UUID.fromString("f3b84eb1-3f8c-4eaa-a3f4-e8cebd280d51");
    private static final UUID AGILITY_ID=UUID.fromString("f3b84eb1-3f8c-4eaa-a3f4-e8cebd280d52");
    private static final ThreadLocal<Boolean> REFLECTING=ThreadLocal.withInitial(()->false);
    public static int level(LivingEntity e,NightEnchantment.Type type){return EnchantmentHelper.getEnchantmentLevel(DeepNight.ENCHANTS.get(type).get(),e);}
    public static int weapon(LivingEntity e,NightEnchantment.Type type){return EnchantmentHelper.getItemEnchantmentLevel(DeepNight.ENCHANTS.get(type).get(),e.getMainHandItem());}
    @SubscribeEvent public static void hurt(LivingHurtEvent e){
        if(e.getEntity().level().isClientSide||REFLECTING.get())return;
        LivingEntity target=e.getEntity();
        if(e.getSource().getEntity() instanceof LivingEntity attacker && e.getSource().getDirectEntity()==attacker){
            int berserk=weapon(attacker,BERSERK);if(attacker.getHealth()<attacker.getMaxHealth()*0.4)e.setAmount(e.getAmount()*(1+berserk*0.12f));
            int slayer=weapon(attacker,BOSS_SLAYER);if(target instanceof NightMob n&&n.species.boss()||target instanceof net.minecraft.world.entity.boss.enderdragon.EnderDragon||target instanceof net.minecraft.world.entity.boss.wither.WitherBoss)e.setAmount(e.getAmount()*(1+slayer*0.07f));
        }
        int guard=level(target,DEEP_GUARD);
        if(e.getSource().getEntity() instanceof NightMob || e.getSource().is(DamageTypes.WITHER))e.setAmount(e.getAmount()*(1-Math.min(0.45f,guard*0.08f)));
        int stand=level(target,LAST_STAND);
        CompoundTag data=target.getPersistentData();long time=target.level().getGameTime();
        if(stand>0&&target.getHealth()<target.getMaxHealth()*0.35&&time>=data.getLong("DeepNightLastStand")){
            e.setAmount(e.getAmount()*(1-stand*0.15f));target.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,60+stand*20,0));data.putLong("DeepNightLastStand",time+600);
        }
    }
    @SubscribeEvent public static void damaged(LivingDamageEvent e){
        if(e.getEntity().level().isClientSide||e.getAmount()<=0||REFLECTING.get())return;
        LivingEntity target=e.getEntity();
        if(target instanceof NightMob boss&&boss.species.boss()&&!e.getSource().is(DamageTypes.GENERIC_KILL)&&!e.getSource().is(DamageTypes.FELL_OUT_OF_WORLD))boss.prepareDamage(e.getAmount());
        if(e.getSource().getEntity() instanceof LivingEntity a && e.getSource().getDirectEntity()==a){
            long now=a.level().getGameTime();CompoundTag data=a.getPersistentData();
            if(now>=data.getLong("DeepNightOnHit")){
                data.putLong("DeepNightOnHit",now+20);
                int poison=weapon(a,SPIDER_BITE), frost=weapon(a,FROZEN_TOUCH), fire=weapon(a,SOUL_FIRE), heal=weapon(a,RADIANCE);
                if(poison>0)target.addEffect(new MobEffectInstance(MobEffects.POISON,40+poison*20,0));
                if(frost>0)target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,40+frost*20,Math.min(1,frost-1)));
                if(fire>0){target.setSecondsOnFire(fire*2);target.addEffect(new MobEffectInstance(MobEffects.WITHER,20+fire*20,0));}
                if(heal>0 && now>=data.getLong("DeepNightRadiance")){a.heal(heal*0.75f);data.putLong("DeepNightRadiance",now+80);}
            }
            int reflect=level(target,RETRIBUTION);
            if(reflect>0){try{REFLECTING.set(true);a.hurt(target.damageSources().thorns(target),reflect*1.25f);}finally{REFLECTING.set(false);}}
        }
    }
    @SubscribeEvent public static void kill(LivingDeathEvent e){
        if(!e.getEntity().level().isClientSide&&e.getSource().getEntity() instanceof LivingEntity a){
            int rank=weapon(a,LIFE_REAPER);if(rank>0)a.heal(rank*1.5f);
        }
    }
    @SubscribeEvent public static void experience(net.minecraftforge.event.level.BlockEvent.BreakEvent e){
        int rank=weapon(e.getPlayer(),WISDOM);if(rank>0&&e.getExpToDrop()>0)e.setExpToDrop((int)Math.ceil(e.getExpToDrop()*(1+rank*0.25)));
    }
    private static void modifier(LivingEntity e,Attribute attribute,UUID id,double amount,AttributeModifier.Operation op){
        AttributeInstance a=e.getAttribute(attribute);if(a==null)return;AttributeModifier old=a.getModifier(id);
        if(old!=null && old.getAmount()==amount)return;
        if(old!=null)a.removeModifier(id);
        if(amount!=0)a.addTransientModifier(new AttributeModifier(id,"deepnight enchantment",amount,op));
    }
    @SubscribeEvent public static void tick(TickEvent.PlayerTickEvent e){
        if(e.phase!=TickEvent.Phase.END||e.player.level().isClientSide||e.player.tickCount%20!=0)return;
        var p=e.player;modifier(p,Attributes.MAX_HEALTH,VITALITY_ID,level(p,VITALITY)*2,AttributeModifier.Operation.ADDITION);
        modifier(p,Attributes.MOVEMENT_SPEED,AGILITY_ID,level(p,AGILITY)*0.05,AttributeModifier.Operation.MULTIPLY_TOTAL);
        if(p.getHealth()>p.getMaxHealth())p.setHealth(p.getMaxHealth());
        int leap=level(p,LEAPING);if(leap>0)p.addEffect(new MobEffectInstance(MobEffects.JUMP,40,leap-1,true,false));
    }
}
