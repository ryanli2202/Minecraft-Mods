package dev.travellerscompanions;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=TravellersCompanions.ID)
public final class CompanionEvents {
    @SubscribeEvent public static void logout(PlayerEvent.PlayerLoggedOutEvent e){if(e.getEntity() instanceof ServerPlayer p){CompanionService.dismiss(p);CompanionNetwork.forget(p.getUUID());}}
    @SubscribeEvent public static void ownerDeath(LivingDeathEvent e){
        if(e.getEntity() instanceof ServerPlayer p)CompanionService.dismiss(p);
        if(e.getSource().getEntity() instanceof CompanionEntity c&&!c.isTroop()&&c.validLease()){var d=CompanionData.get(c.getServer());var entry=d.roster(c.getOwnerUUID()).entries[c.kind().ordinal()];entry.kills++;entry.bond++;d.setDirty();}
    }
    @SubscribeEvent public static void friendlyFire(net.minecraftforge.event.entity.living.LivingAttackEvent e){if(e.getSource().getEntity() instanceof CompanionEntity c&&!c.canAttack(e.getEntity()))e.setCanceled(true);}
    @SubscribeEvent public static void deflect(net.minecraftforge.event.entity.ProjectileImpactEvent event){if(!event.getProjectile().level().isClientSide&&event.getRayTraceResult() instanceof net.minecraft.world.phys.EntityHitResult hit&&hit.getEntity() instanceof CompanionEntity companion&&companion.guard.deflect(event.getProjectile()))event.setCanceled(true);}
    @SubscribeEvent public static void catchUp(net.minecraftforge.event.TickEvent.PlayerTickEvent event){
        if(event.phase!=net.minecraftforge.event.TickEvent.Phase.END||!(event.player instanceof ServerPlayer p)||p.tickCount%100!=0||!p.isAlive())return;
        TroopService.catchUp(p);
        var r=CompanionData.get(p.server).roster(p.getUUID());
        for(int kind:CompanionService.activeKinds(p)){if(r.entries[kind].snapshot.getBoolean("Hold")||CompanionService.active(p,kind)!=null)continue;CompanionService.dismiss(p,kind);CompanionService.summon(p,kind);}
    }
    @SubscribeEvent public static void travel(PlayerEvent.PlayerChangedDimensionEvent e){if(e.getEntity() instanceof ServerPlayer p){TroopService.travel(p);var kinds=CompanionService.activeKinds(p);for(int kind:kinds)CompanionService.dismiss(p,kind);for(int kind:kinds){String error=CompanionService.summon(p,kind);if(!error.isEmpty())p.sendSystemMessage(net.minecraft.network.chat.Component.literal(error));}}}
    @SubscribeEvent public static void save(LevelEvent.Save e){if(e.getLevel() instanceof net.minecraft.server.level.ServerLevel level)for(var entity:level.getAllEntities())if(entity instanceof CompanionEntity c)c.checkpoint();}
    @SubscribeEvent public static void unload(net.minecraftforge.event.entity.EntityLeaveLevelEvent e){if(e.getEntity() instanceof CompanionEntity c&&!e.getLevel().isClientSide())c.checkpoint();}
}
