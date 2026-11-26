package dev.travellerscompanions;

import java.util.*;
import java.util.function.Supplier;
import net.minecraft.nbt.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.SimpleChannel;

public final class CompanionNetwork {
    public static final SimpleChannel CHANNEL=NetworkRegistry.newSimpleChannel(TravellersCompanions.id("main"),()->"6","6"::equals,"6"::equals);
    private static final Map<UUID,Long> LAST=new HashMap<>();
    public record Action(int action,int kind,int entity){}
    public record State(CompoundTag tag,int entity,boolean wheel){}
    public record Animation(int entity,String name,int ticks){}
    public static void init(){
        CHANNEL.registerMessage(0,Action.class,(m,b)->{b.writeVarInt(m.action);b.writeVarInt(m.kind);b.writeVarInt(m.entity);},b->new Action(b.readVarInt(),b.readVarInt(),b.readVarInt()),CompanionNetwork::handle,Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(1,State.class,(m,b)->{b.writeNbt(m.tag);b.writeVarInt(m.entity);b.writeBoolean(m.wheel);},b->new State(b.readNbt(),b.readVarInt(),b.readBoolean()),(m,c)->{c.get().enqueueWork(()->dev.travellerscompanions.client.ClientEvents.receive(m));c.get().setPacketHandled(true);},Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(2,Animation.class,(m,b)->{b.writeVarInt(m.entity);b.writeUtf(m.name,256);b.writeVarInt(m.ticks);},b->new Animation(b.readVarInt(),b.readUtf(256),b.readVarInt()),(m,c)->{c.get().enqueueWork(()->dev.travellerscompanions.client.ClientEvents.animate(m));c.get().setPacketHandled(true);},Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }
    public static void forget(UUID id){LAST.remove(id);}
    private static void handle(Action m,Supplier<NetworkEvent.Context> context){var ctx=context.get();ctx.enqueueWork(()->{
        var p=ctx.getSender();if(p==null||!p.isAlive()||p.isSpectator())return;
        long now=System.currentTimeMillis();if(now-LAST.getOrDefault(p.getUUID(),0L)<120)return;LAST.put(p.getUUID(),now);
        if(m.action==12||m.action==13){CompanionService.openInventory(p,m.entity,m.action==12);return;}
        String error="";
        switch(m.action){case 0->{}case 1->error=CompanionService.summon(p,m.kind);case 2->CompanionService.dismiss(p,m.kind);case 3->error=CompanionService.train(p,m.kind);case 14->error=TroopService.summon(p,m.kind);case 15->{var r=CompanionData.get(p.server).roster(p.getUUID());if(r.squadKind==m.kind)TroopService.dismiss(p);}default->error=CompanionService.interact(p,m.entity,m.action);}
        if(!error.isEmpty())p.sendSystemMessage(Component.literal(error).withStyle(net.minecraft.ChatFormatting.GOLD));
        if(m.action<=3||m.action==14||m.action==15)sendState(p,-1,false);else if(p.level().getEntity(m.entity) instanceof CompanionEntity e&&e.ownedBy(p)&&p.distanceToSqr(e)<=64){var tag=state(p);if(error.isEmpty()&&(m.action==4||m.action==5||m.action==9||m.action==11))tag.putString("Dialogue",e.lastDialogue);CHANNEL.send(PacketDistributor.PLAYER.with(()->p),new State(tag,m.entity,true));}
    });ctx.setPacketHandled(true);}
    public static void openWheel(ServerPlayer p,CompanionEntity e){sendState(p,e.getId(),true);}
    public static CompoundTag state(ServerPlayer p){
        var r=CompanionData.get(p.server).roster(p.getUUID());var n=new CompoundTag();n.putInt("Active",r.party.keySet().stream().findFirst().orElse(-1));n.putInt("PartyCount",r.party.size());n.putInt("PartyLimit",CompanionData.PARTY_LIMIT);n.putInt("Emeralds",p.getInventory().countItem(net.minecraft.world.item.Items.EMERALD));n.putLong("ServerTime",System.currentTimeMillis());var list=new ListTag();
        for(int i=0;i<r.entries.length;i++){var e=r.entries[i];var live=CompanionService.active(p,i);var c=new CompoundTag();c.putBoolean("Active",r.party.containsKey(i));c.putInt("Level",e.level);c.putInt("Bond",e.bond);c.putInt("Kills",e.kills);c.putLong("Recovery",Math.max(0,e.readyAt-System.currentTimeMillis()));c.putLong("Rally",Math.max(0,e.rallyAt-System.currentTimeMillis()));c.putFloat("Health",live!=null?live.getHealth():e.initialized?CharacterKind.of(i).restoredHealth(e.snapshot,e.level):CharacterKind.of(i).maxHealth(e.level));c.putInt("Stance",live!=null?live.stance():e.snapshot.getInt("Stance"));c.putBoolean("Hold",live!=null?live.holding():e.snapshot.getBoolean("Hold"));list.add(c);}n.put("Companions",list);n.put("Troops",TroopService.state(p));return n;
    }
    public static void sendState(ServerPlayer p,int entity,boolean wheel){CHANNEL.send(PacketDistributor.PLAYER.with(()->p),new State(state(p),entity,wheel));}
    public static void animate(CompanionEntity e,CombatBridge.Strike strike){CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(()->e),new Animation(e.getId(),strike.animation(),strike.duration()));}
}
