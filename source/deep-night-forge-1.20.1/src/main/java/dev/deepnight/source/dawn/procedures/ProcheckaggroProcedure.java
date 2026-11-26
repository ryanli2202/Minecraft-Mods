package dev.deepnight.source.dawn.procedures;

import dev.deepnight.DeepNight;
import dev.deepnight.source.dawn.entity.*;
import dev.deepnight.source.dawn.network.SimpleMobsModVariables;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Retained branches of the source target-change callback, adapted to Forge 1.20.1. */
@Mod.EventBusSubscriber(modid=DeepNight.ID)
public final class ProcheckaggroProcedure {
    @SubscribeEvent public static void onEntitySetsAttackTarget(LivingChangeTargetEvent event){
        var mob=event.getEntity();
        if(mob.level().isClientSide||event.getNewTarget()==null)return;
        if(mob instanceof SkeletonlordEntity||mob instanceof CapedMinotaurEntity){
            mob.getPersistentData().putBoolean("aggro",true);
        }else if(mob instanceof NineTailsEntity){
            var data=SimpleMobsModVariables.MapVariables.get(mob.level());data.aggro_tails=true;data.syncData(mob.level());
        }else if(mob instanceof CaveDwellerEntity){
            var data=SimpleMobsModVariables.MapVariables.get(mob.level());data.aggrocave=true;data.syncData(mob.level());
        }
    }
}
