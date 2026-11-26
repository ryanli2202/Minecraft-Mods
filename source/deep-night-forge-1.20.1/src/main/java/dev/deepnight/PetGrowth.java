package dev.deepnight;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.UUID;

@Mod.EventBusSubscriber(modid=DeepNight.ID)
public final class PetGrowth {
    private static final UUID HEALTH=UUID.fromString("6200fa75-3128-46d9-9a47-f465ef42a501");
    private static final UUID DAMAGE=UUID.fromString("6200fa75-3128-46d9-9a47-f465ef42a502");
    public static final int MAX_LEVEL=50;
    public static UUID owner(LivingEntity entity){
        if(entity instanceof TamableAnimal pet&&pet.isTame())return pet.getOwnerUUID();
        if(entity instanceof AbstractHorse horse&&horse.isTamed())return horse.getOwnerUUID();return null;
    }
    public static int level(LivingEntity entity){return Math.max(1,Math.min(MAX_LEVEL,entity.getPersistentData().getInt("DeepNightPetLevel")));}
    public static int xp(LivingEntity entity){return Math.max(0,entity.getPersistentData().getInt("DeepNightPetXP"));}
    public static int required(int level){return 100+(level-1)*25;}
    public static void grant(LivingEntity pet,int amount){
        if(pet.level().isClientSide||owner(pet)==null||amount<=0)return;
        CompoundTag tag=pet.getPersistentData();int old=level(pet),level=old,xp=xp(pet)+Math.min(amount,5000);
        while(level<MAX_LEVEL&&xp>=required(level)){xp-=required(level);level++;}
        tag.putInt("DeepNightPetLevel",level);tag.putInt("DeepNightPetXP",level==MAX_LEVEL?0:xp);apply(pet);
        ServerPlayer p=pet.level().getServer().getPlayerList().getPlayer(owner(pet));
        if(level>old&&p!=null)p.displayClientMessage(net.minecraft.network.chat.Component.literal(pet.getName().getString()+" reached pet level "+level+"!"),true);
    }
    private static void modifier(LivingEntity e,Attribute key,UUID uuid,int level){
        var a=e.getAttribute(key);if(a==null)return;double amount=(level-1)*0.02;
        var old=a.getModifier(uuid);if(old!=null&&Math.abs(old.getAmount()-amount)<0.00001)return;
        if(old!=null)a.removeModifier(uuid);if(amount>0)a.addPermanentModifier(new AttributeModifier(uuid,"Deep Night pet level",amount,AttributeModifier.Operation.MULTIPLY_BASE));
    }
    public static void apply(LivingEntity pet){
        float previous=pet.getMaxHealth();modifier(pet,Attributes.MAX_HEALTH,HEALTH,level(pet));modifier(pet,Attributes.ATTACK_DAMAGE,DAMAGE,level(pet));
        if(pet.getMaxHealth()>previous)pet.heal(pet.getMaxHealth()-previous);
        if(pet instanceof Companion c)c.syncGrowth(level(pet),xp(pet));
    }
    @SubscribeEvent public static void joining(net.minecraftforge.event.entity.EntityJoinLevelEvent e){
        if(e.getLevel().isClientSide||!(e.getEntity() instanceof LivingEntity pet)||owner(pet)==null)return;
        apply(pet);
    }
    @SubscribeEvent public static void inspect(net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract e){
        if(e.getEntity() instanceof ServerPlayer p&&e.getTarget() instanceof LivingEntity pet&&PetCatalogue.hasTool(p)&&owner(pet)!=null){
            SummonTool.say(p,pet.getName().getString()+": level "+level(pet)+" / "+MAX_LEVEL+", XP "+xp(pet)+" / "+required(level(pet))+", health "+Math.round(pet.getHealth())+" / "+Math.round(pet.getMaxHealth())+". Health and damage bonus: "+((level(pet)-1)*2)+"%.");
            e.setCancellationResult(net.minecraft.world.InteractionResult.SUCCESS);e.setCanceled(true);
        }
    }
    @SubscribeEvent public static void tick(LivingEvent.LivingTickEvent e){
        LivingEntity pet=e.getEntity();if(pet.level().isClientSide||pet.tickCount%100!=0)return;
        UUID owner=owner(pet);if(owner==null)return;apply(pet);
        var p=pet.level().getServer().getPlayerList().getPlayer(owner);
        if(p!=null&&p.isAlive()&&p.level()==pet.level()&&p.distanceToSqr(pet)<1024&&pet.isAlive()){
            // Five XP per twenty seconds spent together; unloaded/offline pets do not earn XP.
            CompoundTag tag=pet.getPersistentData();int seconds=tag.getInt("DeepNightTogetherTicks")+100;
            if(seconds>=400){grant(pet,5);seconds-=400;}tag.putInt("DeepNightTogetherTicks",seconds);
        }
    }
    @SubscribeEvent public static void victory(LivingDeathEvent e){
        if(e.getEntity().level().isClientSide||e.getSource().is(net.minecraft.world.damagesource.DamageTypes.GENERIC_KILL))return;
        Entity attacker=e.getSource().getEntity();UUID id=attacker instanceof ServerPlayer p?p.getUUID():attacker instanceof LivingEntity living?owner(living):null;
        if(id==null||!(e.getEntity() instanceof net.minecraft.world.entity.monster.Enemy))return;
        if(e.getEntity() instanceof NightMob npc&&npc.species.passive())return;
        int reward=e.getEntity() instanceof NightMob n&&n.species.boss()?100:15;
        for(LivingEntity pet:e.getEntity().level().getEntitiesOfClass(LivingEntity.class,e.getEntity().getBoundingBox().inflate(32)))
            if(pet.isAlive()&&id.equals(owner(pet)))grant(pet,reward);
    }
}
