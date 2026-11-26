package dev.deepnight;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import java.util.List;

public final class SummonTool extends Item {
    public record Result(boolean success,String message){}
    public SummonTool() { super(new Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()); }
    @Override public boolean isFoil(ItemStack stack) { return true; }
    @Override public void appendHoverText(ItemStack stack,Level level,List<Component> lines,TooltipFlag flag) {
        lines.add(Component.literal("Awarded after defeating the Ender Dragon."));
        lines.add(Component.literal("Use: browse bosses, summon the next boss, or start a rematch."));
        lines.add(Component.literal("Creative mode: summon any boss or replace the active boss."));
    }
    @Override public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(player instanceof ServerPlayer sp) {
            BossNetwork.open(sp);
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand),level.isClientSide);
    }
    /** Shared by server authorization and the client screen's keep-open check. */
    public static boolean hasCaller(Player player) {
        for(int slot=0;slot<player.getInventory().getContainerSize();slot++)
            if(player.getInventory().getItem(slot).is(DeepNight.CALLER.get()))return true;
        return false;
    }
    public static boolean summon(ServerPlayer player) {
        int next=CampaignData.get(player.server).player(player.getUUID()).stage;
        Result result=summonSelected(player,next);
        say(player,result.message());return result.success();
    }
    public static void prepareCreative(ServerPlayer player){
        if(player.isCreative()){
            CampaignData data=CampaignData.get(player.server);var progress=data.player(player.getUUID());
            if(!progress.unlocked){progress.unlocked=true;progress.toolPending=false;data.setDirty();}
        }
    }
    public static Result summonSelected(ServerPlayer player,int selected) {
        var level=player.serverLevel(); CampaignData data=CampaignData.get(player.server);
        if(!CampaignEvents.hasCaller(player))return new Result(false,"Keep the Boss Summon Tool in your inventory.");
        if(player.isSpectator() || !player.isAlive())return new Result(false,"Return to an active game mode before summoning.");
        prepareCreative(player);
        if(!data.player(player.getUUID()).unlocked)return new Result(false,"Defeat the Ender Dragon to unlock the campaign.");
        if(selected<0||selected>=Species.BOSS_COUNT)return new Result(false,"Unknown boss selection.");
        if(level.getDifficulty()==Difficulty.PEACEFUL)return new Result(false,"Boss battles require Easy, Normal, or Hard difficulty.");
        if(data.active!=null&&!player.isCreative())return new Result(false,"Finish or end the active encounter before summoning another boss.");
        if(!player.isCreative()&&!data.canSummon(player.getUUID(),selected,false))return new Result(false,"Defeat your next campaign boss to unlock this encounter.");
        if(player.getCooldowns().isOnCooldown(DeepNight.CALLER.get()))return new Result(false,"The summon tool is recharging.");
        Species species=Species.boss(selected);
        Vec3 look=player.getLookAngle().multiply(1,0,1).normalize();
        if(look.lengthSqr()<0.1) look=new Vec3(0,0,1);
        NightMob boss=DeepNight.HOSTILES.get(species).get().create(level);
        if(boss==null)return new Result(false,"This boss could not be created.");
        BlockPos ground=null;
        search: for(int distance:new int[]{14,18,22}) for(int angle:new int[]{0,45,-45,90,-90,180}) {
            Vec3 candidate=player.position().add(look.yRot((float)Math.toRadians(angle)).scale(distance));
            BlockPos center=BlockPos.containing(candidate);
            for(int dy=4;dy>=-6;dy--) {
                BlockPos pos=center.offset(0,dy,0);
                if(!level.hasChunkAt(pos)||!level.getWorldBorder().isWithinBounds(pos)) continue;
                if(!level.getBlockState(pos.below()).isSolid() || !level.getFluidState(pos).isEmpty()) continue;
                boss.moveTo(pos.getX()+0.5,pos.getY(),pos.getZ()+0.5,player.getYRot()+180,0);
                if(!level.noCollision(boss,boss.getBoundingBox().inflate(1,0,1).expandTowards(0,0.5,0)) || level.containsAnyLiquid(boss.getBoundingBox())) continue;
                int radius=(int)Math.ceil(species.width/2);
                for(int x=-radius;x<=radius;x++) for(int z=-radius;z<=radius;z++)
                    if(!level.getBlockState(pos.offset(x,-1,z)).isSolid()) continue search;
                ground=pos; break search;
            }
        }
        if(ground==null)return new Result(false,"Find a broad, flat arena with clear ground and at least 20 blocks of headroom nearby.");
        boss.finalizeSpawn(level,level.getCurrentDifficultyAt(ground),net.minecraft.world.entity.MobSpawnType.MOB_SUMMONED,null,null);
        boss.bind(player); boss.setTarget(player);
        if(!level.addFreshEntity(boss))return new Result(false,"The boss could not enter this arena.");
        // Validate the arena and spawn first: a failed replacement must leave the old fight intact.
        boolean replacing=data.active!=null;
        if(replacing){
            Result ended=CampaignEvents.abandon(player);
            if(!ended.success()){boss.discard();return ended;}
        }
        if(!data.beginSelected(player.getUUID(),boss.getUUID(),selected,player.isCreative(),level.dimension().location().toString(),ground.asLong())){
            boss.discard();return new Result(false,"Another encounter has already started.");
        }
        player.getCooldowns().addCooldown(DeepNight.CALLER.get(),20);
        return new Result(true,(replacing?"Replaced the active boss with ":"Summoned ")+species.title+switch(data.activeMode){
            case PROGRESSION->". Defeat it to unlock your next boss.";
            case REMATCH->" for a rematch. Campaign progress is unchanged.";
            case TEST->" for creative testing. Campaign progress is unchanged.";
        });
    }
    public static void say(ServerPlayer p,String text) {p.sendSystemMessage(Component.literal(text));}
}
