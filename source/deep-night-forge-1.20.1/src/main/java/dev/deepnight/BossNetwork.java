package dev.deepnight;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.SimpleChannel;
import java.util.*;
import java.util.function.Supplier;

/** The screen receives a snapshot; every action is revalidated on the server. */
public final class BossNetwork {
    public enum Action { SUMMON, ABANDON, REFRESH }
    public record Request(Action action,int stage){}
    public record State(boolean unlocked,boolean creative,int next,List<String> defeated,
                        int active,boolean canAbandon,String dimension,BlockPos position,
                        String mode,String feedback) {
        public boolean completed(Species boss){return defeated.contains(boss.id);}
        public boolean canSummon(int stage){return stage>=0&&stage<Species.BOSS_COUNT
            &&(creative||(unlocked&&active<0&&(stage==next||completed(Species.boss(stage)))));}
    }
    public record Menu(State state,boolean open,boolean close){}
    public static final SimpleChannel CHANNEL=NetworkRegistry.newSimpleChannel(DeepNight.id("bosses"),()->"1","1"::equals,"1"::equals);

    public static void register(){
        CHANNEL.registerMessage(0,Menu.class,BossNetwork::encode,BossNetwork::decode,BossNetwork::receive,Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(1,Request.class,(m,b)->{b.writeEnum(m.action());b.writeVarInt(m.stage());},
            b->new Request(b.readEnum(Action.class),b.readVarInt()),BossNetwork::request,Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }
    public static void encode(Menu menu,FriendlyByteBuf b){
        State s=menu.state();b.writeBoolean(menu.open());b.writeBoolean(menu.close());
        b.writeBoolean(s.unlocked());b.writeBoolean(s.creative());b.writeVarInt(s.next());
        b.writeVarInt(s.defeated().size());for(String id:s.defeated())b.writeUtf(id,128);
        b.writeVarInt(s.active());b.writeBoolean(s.canAbandon());b.writeUtf(s.dimension(),256);
        b.writeBlockPos(s.position());b.writeUtf(s.mode(),32);b.writeUtf(s.feedback(),512);
    }
    public static Menu decode(FriendlyByteBuf b){
        boolean open=b.readBoolean(),close=b.readBoolean(),unlocked=b.readBoolean(),creative=b.readBoolean();
        int next=b.readVarInt(),count=b.readVarInt();
        if(count<0||count>Species.BOSS_COUNT||next<0||next>Species.BOSS_COUNT)throw new IllegalArgumentException("Invalid boss progress");
        List<String> defeated=new ArrayList<>();for(int i=0;i<count;i++)defeated.add(b.readUtf(128));
        int active=b.readVarInt();if(active<-1||active>=Species.BOSS_COUNT)throw new IllegalArgumentException("Invalid active boss");
        return new Menu(new State(unlocked,creative,next,List.copyOf(defeated),active,b.readBoolean(),b.readUtf(256),b.readBlockPos(),b.readUtf(32),b.readUtf(512)),open,close);
    }
    private static void receive(Menu menu,Supplier<NetworkEvent.Context> context){
        context.get().enqueueWork(()->DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()->dev.deepnight.client.BossScreen.receive(menu)));
        context.get().setPacketHandled(true);
    }
    public static State snapshot(ServerPlayer player,String feedback){
        CampaignData data=CampaignData.get(player.server);var progress=data.player(player.getUUID());
        List<String> defeated=Arrays.stream(Species.values()).filter(Species::boss).filter(s->progress.defeated.contains(s.id)).map(s->s.id).toList();
        return new State(progress.unlocked,player.isCreative(),progress.stage,defeated,data.active==null?-1:data.activeStage,
            data.active!=null&&(player.isCreative()||player.getUUID().equals(data.summoner)||player.hasPermissions(2)),
            data.dimension,BlockPos.of(data.position),data.activeMode.name(),feedback);
    }
    public static void open(ServerPlayer player){
        if(!CampaignEvents.hasCaller(player)||player.isSpectator())return;
        SummonTool.prepareCreative(player);
        CHANNEL.send(PacketDistributor.PLAYER.with(()->player),new Menu(snapshot(player,""),true,false));
    }
    public static SummonTool.Result apply(ServerPlayer player,Request request){
        if(!CampaignEvents.hasCaller(player)||!player.isAlive()||player.isSpectator())
            return new SummonTool.Result(false,"Keep the summon tool and return to an active game mode.");
        return switch(request.action()){
            case SUMMON->SummonTool.summonSelected(player,request.stage());
            case ABANDON->CampaignEvents.abandon(player);
            case REFRESH->new SummonTool.Result(true,"");
        };
    }
    private static void request(Request request,Supplier<NetworkEvent.Context> contexts){
        var context=contexts.get();
        context.enqueueWork(()->{
            ServerPlayer player=context.getSender();if(player==null)return;
            long now=player.serverLevel().getGameTime();
            if(request.action()==Action.REFRESH){
                if(now<player.getPersistentData().getLong("DeepNightBossMenuAt"))return;
                player.getPersistentData().putLong("DeepNightBossMenuAt",now+10);
            }
            var result=apply(player,request);
            boolean close=!CampaignEvents.hasCaller(player)||player.isSpectator()||(result.success()&&request.action()==Action.SUMMON);
            CHANNEL.send(PacketDistributor.PLAYER.with(()->player),new Menu(snapshot(player,result.message()),false,close));
            if(result.success()&&request.action()==Action.SUMMON)SummonTool.say(player,result.message());
        });
        context.setPacketHandled(true);
    }
}
