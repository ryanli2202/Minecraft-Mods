package dev.travellerscompanions;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistries;

/** The installed Big Katana's player animations, with companion-safe hit selection. */
public final class CommonKatanaCombat {
    public static final String BASIC="common_weapons:ohtati3",SPECIAL="common_weapons:ohtati";
    private final CompanionEntity entity;
    private int remaining,duration,specialCooldown,combo;
    private boolean special;
    public int basicAttacks,specialAttacks,hits;
    public CommonKatanaCombat(CompanionEntity entity){this.entity=entity;}
    public boolean equipped(){return new ResourceLocation("common_weapons","test").equals(ForgeRegistries.ITEMS.getKey(entity.getMainHandItem().getItem()));}
    public boolean active(){return remaining>0;}
    public void cancel(){remaining=0;}
    public void tick(){
        if(specialCooldown>0)specialCooldown--;
        if(!active())return;
        var target=entity.getTarget();
        if(!equipped()||entity.stance()==2||target==null||!target.isAlive()||!entity.canAttack(target)){cancel();return;}
        entity.spells().aim(target);entity.getNavigation().stop();
        int elapsed=duration-(--remaining);
        if(special&&(elapsed==6||elapsed==18)){sound("zan2",elapsed==6?1:.9f);slash(10+(entity.companionLevel()-1)*.25f,4,true);}
        else if(!special&&elapsed==4){entity.playSound(SoundEvents.PLAYER_ATTACK_SWEEP,.8f,1);slash(6+(entity.companionLevel()-1)*.25f,3.2,false);}
        if(special&&elapsed==30)sound("noto",1);
    }
    public boolean start(){
        if(!equipped()||active()||entity.stance()==2||entity.getTarget()==null||!entity.canAttack(entity.getTarget()))return false;
        special=combo>=2&&specialCooldown==0;duration=special?32:12;remaining=duration;
        if(entity.level() instanceof net.minecraft.server.level.ServerLevel server)software.bernie.geckolib.animatable.GeoItem.getOrAssignId(entity.getMainHandItem(),server);
        if(special){specialCooldown=120;combo=0;specialAttacks++;sound("gatya",.7f);}else{combo++;basicAttacks++;}
        entity.interruptAttack();entity.spells().aim(entity.getTarget());
        CompanionNetwork.animate(entity,new CombatBridge.Strike(special?SPECIAL:BASIC,duration,special?6:4,special?4:3.2,1));return true;
    }
    private void slash(float damage,double range,boolean sweep){
        for(var target:entity.level().getEntitiesOfClass(LivingEntity.class,entity.getBoundingBox().inflate(range),m->m.isAlive()&&entity.canAttack(m))){
            if(!sweep&&target!=entity.getTarget()||entity.distanceToSqr(target)>range*range||!entity.getSensing().hasLineOfSight(target))continue;
            var direction=target.position().subtract(entity.position()).multiply(1,0,1).normalize();
            if(entity.getLookAngle().multiply(1,0,1).normalize().dot(direction)<.15)continue;
            if(target.hurt(entity.damageSources().mobAttack(entity),damage)){hits++;target.knockback(.35,entity.getX()-target.getX(),entity.getZ()-target.getZ());}
        }
    }
    private void sound(String name,float pitch){var sound=ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("common_weapons",name));if(sound!=null)entity.playSound(sound,.8f,pitch);}
}
