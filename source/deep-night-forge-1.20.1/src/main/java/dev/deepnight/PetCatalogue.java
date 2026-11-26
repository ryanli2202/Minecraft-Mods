package dev.deepnight;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.*;

public final class PetCatalogue {
    private static Map<ResourceLocation,EntityType<?>> pets;
    public static synchronized Map<ResourceLocation,EntityType<?>> all(ServerLevel level){
        if(pets!=null)return pets;
        var map=new TreeMap<ResourceLocation,EntityType<?>>();
        for(var entry:ForgeRegistries.ENTITY_TYPES.getEntries()){
            EntityType<?> type=entry.getValue();
            try{
                Entity test=type.create(level);
                if(test instanceof TamableAnimal || test instanceof AbstractHorse)map.put(entry.getKey().location(),type);
            }catch(RuntimeException ignored){/* An unrelated mod may require a special spawn factory. */}
        }
        pets=Collections.unmodifiableMap(map);return pets;
    }
    public static boolean hasTool(Player p){return p.getMainHandItem().is(DeepNight.PET_CALLER.get())||p.getOffhandItem().is(DeepNight.PET_CALLER.get());}
    public static boolean summon(ServerPlayer p,ResourceLocation id){
        if(!p.isAlive()||p.isSpectator()||!hasTool(p))return false;
        long now=p.level().getGameTime();if(now<p.getPersistentData().getLong("DeepNightPetSummonAt")){SummonTool.say(p,"The pet tool is recharging. Wait a few seconds.");return false;}
        EntityType<?> type=all(p.serverLevel()).get(id);if(type==null)return false;
        Entity entity=type.create(p.serverLevel());if(!(entity instanceof LivingEntity pet))return false;
        Vec3 look=p.getLookAngle().multiply(1,0,1).normalize();if(look.lengthSqr()<0.1)look=new Vec3(0,0,1);
        boolean found=false,aquatic=pet instanceof Companion c&&c.species.aquatic;
        search:for(int distance:new int[]{4,6,8})for(int angle:new int[]{0,45,-45,90,-90,180})for(int dy=2;dy>=-3;dy--){
            Vec3 point=p.position().add(look.yRot((float)Math.toRadians(angle)).scale(distance));BlockPos pos=BlockPos.containing(point).offset(0,dy,0);
            if(!p.level().hasChunkAt(pos)||!p.level().getWorldBorder().isWithinBounds(pos))continue;
            if(!p.level().getBlockState(pos.below()).isSolid()&&!(aquatic&&p.level().getFluidState(pos).is(net.minecraft.tags.FluidTags.WATER)))continue;
            pet.moveTo(pos.getX()+0.5,pos.getY(),pos.getZ()+0.5,p.getYRot()+180,0);
            if(p.level().noCollision(pet,pet.getBoundingBox().inflate(0.15,0,0.15).expandTowards(0,0.15,0))&&(aquatic||!p.level().containsAnyLiquid(pet.getBoundingBox()))){found=true;break search;}
        }
        if(!found){SummonTool.say(p,"Find open ground with enough space for this pet.");return false;}
        if(pet instanceof TamableAnimal tame){tame.tame(p);tame.setOrderedToSit(false);tame.setTarget(null);}
        else if(pet instanceof AbstractHorse horse){horse.tameWithName(p);horse.setTemper(horse.getMaxTemper());}
        if(pet instanceof AgeableMob ageable)ageable.setAge(0);
        if(pet instanceof Mob mob)mob.setPersistenceRequired();
        pet.getPersistentData().putInt("DeepNightPetLevel",1);PetGrowth.apply(pet);pet.setHealth(pet.getMaxHealth());
        if(!p.serverLevel().addFreshEntity(pet))return false;
        p.getPersistentData().putLong("DeepNightPetSummonAt",now+200);p.getCooldowns().addCooldown(DeepNight.PET_CALLER.get(),200);
        p.level().broadcastEntityEvent(pet,(byte)7);SummonTool.say(p,"Summoned "+pet.getName().getString()+", already tamed. Level 1.");return true;
    }
}
