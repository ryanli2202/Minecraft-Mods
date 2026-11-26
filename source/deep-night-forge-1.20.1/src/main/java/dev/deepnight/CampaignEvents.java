package dev.deepnight;

import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=DeepNight.ID)
public final class CampaignEvents {
    @SubscribeEvent public static void dragonDeath(LivingDeathEvent e) {
        if(!(e.getEntity() instanceof EnderDragon dragon)||dragon.level().isClientSide) return;
        if(e.getSource().is(net.minecraft.world.damagesource.DamageTypes.GENERIC_KILL))return;
        ServerPlayer killer=NightMob.creditedPlayer(e.getSource().getEntity());
        if(killer!=null)unlock(killer);
        // The End fight credits participating survivors on the main island, including bed kills.
        for(ServerPlayer p:dragon.level().getServer().getPlayerList().getPlayers())
            if(p.level()==dragon.level() && p.distanceToSqr(dragon)<256*256 && p.isAlive()&&!p.isCreative()&&!p.isSpectator())unlock(p);
    }
    @SubscribeEvent public static void advancement(AdvancementEvent.AdvancementEarnEvent e) {
        if(e.getEntity() instanceof ServerPlayer p && e.getAdvancement().getId().toString().equals("minecraft:end/kill_dragon"))unlock(p);
    }
    @SubscribeEvent public static void login(PlayerEvent.PlayerLoggedInEvent e) {
        if(e.getEntity() instanceof ServerPlayer p) {
            var advancement=p.server.getAdvancements().getAdvancement(new net.minecraft.resources.ResourceLocation("minecraft:end/kill_dragon"));
            if(advancement!=null&&p.getAdvancements().getOrStartProgress(advancement).isDone())unlock(p);
            deliverPending(p);
        }
    }
    @SubscribeEvent public static void tick(TickEvent.PlayerTickEvent e) {
        if(e.phase==TickEvent.Phase.END&&e.player instanceof ServerPlayer p&&p.tickCount%100==0)deliverPending(p);
    }
    public static void unlock(ServerPlayer p) {
        if(CampaignData.get(p.server).unlock(p.getUUID())) {
            SummonTool.say(p,"The dragon has fallen. The Deep Night answers. Your Boss Summon Tool is ready.");
            deliverPending(p);
            give(p,new ItemStack(DeepNight.GUIDE.get()));
        }
        deliverPending(p);
    }
    public static boolean hasCaller(ServerPlayer p) {
        return SummonTool.hasCaller(p);
    }
    public static void deliverPending(ServerPlayer p) {
        CampaignData d=CampaignData.get(p.server); CampaignData.Progress progress=d.player(p.getUUID());
        if(!progress.unlocked||!progress.toolPending)return;
        if(hasCaller(p)||p.getInventory().add(new ItemStack(DeepNight.CALLER.get()))) {
            progress.toolPending=false;d.setDirty();p.containerMenu.broadcastChanges();
        }
    }
    public static void give(ServerPlayer p,ItemStack item) {
        if(!p.getInventory().add(item)) {var entity=p.drop(item,false);if(entity!=null){entity.setTarget(p.getUUID());entity.setExtendedLifetime();}}
    }
    public static void status(ServerPlayer p) {
        CampaignData d=CampaignData.get(p.server); var progress=d.player(p.getUUID());
        if(!progress.unlocked){SummonTool.say(p,"The Deep Night is sealed. Defeat the Ender Dragon first.");return;}
        SummonTool.say(p,"The Deep Night: "+progress.stage+"/"+Species.BOSS_COUNT+" defeated. "+(progress.stage==Species.BOSS_COUNT?"Campaign complete.":"Next: "+Species.boss(progress.stage).title));
        if(d.active!=null) {BlockPos pos=BlockPos.of(d.position);SummonTool.say(p,"Active: "+Species.boss(d.activeStage).title+" at "+pos.getX()+", "+pos.getY()+", "+pos.getZ()+" in "+d.dimension+". Defeat it, or its summoner can use /deepnight abandon to retry without advancing.");}
    }
    public static SummonTool.Result abandon(ServerPlayer p){
        var d=CampaignData.get(p.server);
        if(d.active==null)return new SummonTool.Result(false,"There is no active encounter.");
        if(!p.isCreative()&&!p.getUUID().equals(d.summoner)&&!p.hasPermissions(2))return new SummonTool.Result(false,"Only the summoner, a creative player, or a server operator can end this encounter.");
        var bossId=d.active;
        for(var level:p.server.getAllLevels()){var entity=level.getEntity(bossId);if(entity!=null)entity.discard();}
        // Unloaded bosses validate this token when their chunk loads again.
        d.clear(bossId);
        return new SummonTool.Result(true,"Encounter ended. Campaign progress is unchanged.");
    }
    @SubscribeEvent public static void commands(RegisterCommandsEvent e) {
        e.getDispatcher().register(Commands.literal("deepnight")
            .executes(c->{status(c.getSource().getPlayerOrException());return 1;})
            .then(Commands.literal("status").executes(c->{status(c.getSource().getPlayerOrException());return 1;}))
            .then(Commands.literal("guide").executes(c->{give(c.getSource().getPlayerOrException(),new ItemStack(DeepNight.GUIDE.get()));return 1;}))
            .then(Commands.literal("reclaim").executes(c->{
                ServerPlayer p=c.getSource().getPlayerOrException();var d=CampaignData.get(p.server);var progress=d.player(p.getUUID());
                if(!progress.unlocked){status(p);return 0;}if(hasCaller(p)){SummonTool.say(p,"You already have a Boss Summon Tool.");return 0;}
                progress.toolPending=true;d.setDirty();deliverPending(p);
                SummonTool.say(p,progress.toolPending?"Clear one inventory slot; your tool will arrive automatically.":"Boss Summon Tool restored. Your progress is unchanged.");return 1;
            }))
            .then(Commands.literal("abandon").executes(c->{
                ServerPlayer p=c.getSource().getPlayerOrException();var result=abandon(p);
                SummonTool.say(p,result.message());return result.success()?1:0;
            })));
    }
    @SubscribeEvent public static void clericTrade(net.minecraftforge.event.village.VillagerTradesEvent e){
        if(e.getType()==net.minecraft.world.entity.npc.VillagerProfession.CLERIC)e.getTrades().get(3).add((entity,random)->new net.minecraft.world.item.trading.MerchantOffer(new ItemStack(net.minecraft.world.item.Items.EMERALD,24),new ItemStack(net.minecraft.world.item.Items.ENDER_PEARL,2),new ItemStack(DeepNight.PET_CALLER.get()),4,20,0.05f));
    }
}
