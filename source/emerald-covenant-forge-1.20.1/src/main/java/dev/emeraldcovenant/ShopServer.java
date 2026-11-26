package dev.emeraldcovenant;

import com.google.gson.*;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.loading.FMLPaths;
import java.nio.file.*;
import java.util.*;

public final class ShopServer {
    public static Catalogue catalogue;
    private static int revision;
    private static final Map<UUID,Session> SESSIONS=new HashMap<>();
    private static final Map<UUID,Long> LAST_OPEN=new HashMap<>();
    private static final class Session {
        final long token=new java.security.SecureRandom().nextLong();
        final int revision;long expires;int sequence;
        Session(int revision,long now){this.revision=revision;expires=now+12000;}
    }
    @SubscribeEvent public static void started(ServerStartedEvent e){catalogue=null;}
    @SubscribeEvent(priority=EventPriority.LOWEST) public static void dataReloaded(OnDatapackSyncEvent e){if(e.getPlayer()==null){catalogue=null;revision++;SESSIONS.clear();}}
    @SubscribeEvent public static void stopped(ServerStoppedEvent e){catalogue=null;SESSIONS.clear();LAST_OPEN.clear();}
    @SubscribeEvent public static void logout(PlayerEvent.PlayerLoggedOutEvent e){SESSIONS.remove(e.getEntity().getUUID());LAST_OPEN.remove(e.getEntity().getUUID());}
    public static void rebuild(){catalogue=new Catalogue();revision++;SESSIONS.clear();}
    private static long time(ServerPlayer p){return p.server.overworld().getGameTime();}
    public static void open(ServerPlayer p) {
        if(!eligible(p))return;
        long now=time(p);if(now-LAST_OPEN.getOrDefault(p.getUUID(),-100L)<10)return;LAST_OPEN.put(p.getUUID(),now);
        if(catalogue==null)rebuild();
        Session session=new Session(revision,now);SESSIONS.put(p.getUUID(),session);
        ShopNetwork.send(p,new ShopNetwork.Begin(session.token,catalogue.offers.size(),mask(p),Transactions.emeralds(p.getInventory()),String.join("; ",catalogue.warnings).substring(0,Math.min(1800,String.join("; ",catalogue.warnings).length()))));
        int size=catalogue.offers.size();
        for(int start=0;start<size;start+=32)ShopNetwork.send(p,new ShopNetwork.Chunk(session.token,List.copyOf(catalogue.offers.subList(start,Math.min(size,start+32))),start+32>=size));
        if(size==0)ShopNetwork.send(p,new ShopNetwork.Chunk(session.token,List.of(),true));
    }
    public static boolean eligible(ServerPlayer p){return p.isAlive()&&!p.isSpectator()&&p.containerMenu==p.inventoryMenu;}
    public static int mask(ServerPlayer p){return GuildData.get(p.server).mask(p.getUUID());}
    public static void buy(ServerPlayer p,long token,int sequence,String key,int quantity) {
        Session s=SESSIONS.get(p.getUUID());
        if(s==null||s.token!=token||s.revision!=revision||time(p)>s.expires){status(p,token,false,"Shop session expired. Close and reopen the shop.");return;}
        if(sequence<=s.sequence)return; // Replayed packets cannot repeat a debit.
        s.sequence=sequence;
        if(!eligible(p)){status(p,token,false,"Trading is unavailable right now.");return;}
        Offer e=catalogue.byKey.get(key);
        if(e==null){status(p,token,false,"This offer is unavailable. Reopen the shop.");return;}
        if(!e.unlocked(mask(p))){status(p,token,false,"Missing bell bearing(s): "+e.missingBearings(mask(p))+". Previously offered bearings are still unlocked.");return;}
        if(e.bearingBit()!=0&&(mask(p)&e.bearingBit())!=0){status(p,token,false,"You have already sworn this merchant's oath.");return;}
        if(e.bearingBit()!=0&&quantity!=1){status(p,token,false,"Buy one bell bearing at a time.");return;}
        Transactions.Result result=Transactions.purchase(p.getInventory(),e.stack(),quantity,e.price());
        s.expires=time(p)+12000;
        if(result.success()){p.inventoryMenu.broadcastChanges();p.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP,.6f,1.1f);}
        status(p,token,result.success(),result.message());
    }
    public static void offer(ServerPlayer p,int requestedBit) {
        if(!eligible(p))return;
        var data=GuildData.get(p.server);int before=data.mask(p.getUUID()),added=0;
        // Main inventory + offhand, never another player's or a container's slots.
        for(var group:List.of(p.getInventory().items,p.getInventory().offhand))for(ItemStack stack:group)
            if(!stack.isEmpty()&&stack.getItem() instanceof BearingItem bearing&&(requestedBit==0||requestedBit==bearing.bit)&&((before|added)&bearing.bit)==0){stack.shrink(1);added|=bearing.bit;}
        if(added!=0){data.unlock(p.getUUID(),added);p.getInventory().setChanged();p.inventoryMenu.broadcastChanges();p.playSound(SoundEvents.BELL_BLOCK,.8f,1f);}
        String message=added!=0?"Bell bearing offered. New merchant stock is permanently unlocked.":"No unoffered bell bearings in your inventory.";
        Session s=SESSIONS.get(p.getUUID());
        if(s!=null)status(p,s.token,added!=0,message);
        else p.displayClientMessage(Component.literal(message),true);
    }
    public static void action(ServerPlayer p,long token,int action) {
        Session s=SESSIONS.get(p.getUUID());if(s==null||s.token!=token)return;
        if(action==0){SESSIONS.remove(p.getUUID());return;}
        if(action==1&&eligible(p)&&time(p)<=s.expires)offer(p,0);
    }
    private static void status(ServerPlayer p,long token,boolean success,String message){ShopNetwork.send(p,new ShopNetwork.Status(token,mask(p),Transactions.emeralds(p.getInventory()),success,message));}
    @SubscribeEvent public static void commands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("covenant")
            .then(Commands.literal("shop").executes(c->{open(c.getSource().getPlayerOrException());return 1;}))
            .then(Commands.literal("reload").requires(s->s.hasPermission(2)).executes(c->{rebuild();c.getSource().sendSuccess(()->Component.literal("Covenant catalogue rebuilt: "+catalogue.offers.size()+" offers. Reopen shops to refresh."),true);return 1;}))
            .then(Commands.literal("audit").requires(s->s.hasPermission(2)).executes(c->{
                try {
                    if(catalogue==null)rebuild();
                    JsonArray rows=new JsonArray();for(Offer e:catalogue.offers){JsonObject o=new JsonObject();o.addProperty("key",e.key());o.addProperty("category",e.category().name());o.addProperty("price",e.price());o.addProperty("damage",e.damage());o.addProperty("set",e.set());o.addProperty("setProtection",e.setProtection());o.addProperty("requiredBearings",e.required());o.addProperty("nbt",e.stack().getTag()==null?"":e.stack().getTag().toString());rows.add(o);}
                    Path out=FMLPaths.GAMEDIR.get().resolve("emeraldcovenant-catalogue-audit.json");Files.writeString(out,new GsonBuilder().setPrettyPrinting().create().toJson(rows));
                    c.getSource().sendSuccess(()->Component.literal("Wrote "+rows.size()+" offers to "+out.getFileName()),false);return 1;
                }catch(Exception ex){c.getSource().sendFailure(Component.literal(ex.toString()));return 0;}
            })));
    }
}
