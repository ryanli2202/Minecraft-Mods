package dev.travellerscompanions;

import java.util.*;
import net.minecraft.nbt.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;

/** Four persistent soldiers, with an independent group slot beside the named party. */
public final class TroopService {
    private TroopService() {}

    public static CompanionEntity active(ServerPlayer p,int slot){
        var r=CompanionData.get(p.server).roster(p.getUUID());var id=r.squad.get(slot);
        if(id!=null)for(var level:p.server.getAllLevels())if(level.getEntity(id) instanceof CompanionEntity e&&e.isTroop()&&e.ownedBy(p)&&e.validLease()&&!e.isRemoved())return e;
        return null;
    }

    public static long recovery(CompanionData.Roster r,int kind){
        long ready=0;for(var entry:r.troops[kind])ready=Math.max(ready,entry.readyAt);
        return Math.max(0,ready-System.currentTimeMillis());
    }

    public static String summon(ServerPlayer p,int kind){
        if(!TroopKind.valid(kind)||!p.isAlive()||p.isSpectator())return "You cannot summon a troop group now.";
        var data=CompanionData.get(p.server);var r=data.roster(p.getUUID());
        if(!r.squad.isEmpty())return "One troop group can travel with you. Dismiss your current group first.";
        if(recovery(r,kind)>0)return "This group is still recovering. All four soldiers must be ready.";
        // Reserve every position before creating any lease; failed group summons are all-or-nothing.
        var members=new ArrayList<CompanionEntity>();var reserved=new ArrayList<AABB>();
        for(int slot=0;slot<TroopKind.SIZE;slot++){
            var e=create(p,kind,slot);if(e==null||!CompanionService.teleportNear(e,p,reserved))return "Find safe, solid ground with room for all four soldiers.";
            members.add(e);reserved.add(e.getBoundingBox());
        }
        r.squadKind=kind;for(var e:members)r.squad.put(e.troopSlot(),e.getUUID());
        for(var e:members)if(!p.serverLevel().addFreshEntity(e)){
            r.squad.clear();r.squadKind=-1;for(var member:members)member.discard();data.setDirty();
            return "The group could not be summoned here.";
        }
        for(var e:members){var entry=e.troopEntry();entry.initialized=true;entry.readyAt=0;e.checkpoint();}
        data.setDirty();return "";
    }

    private static CompanionEntity create(ServerPlayer p,int kind,int slot){
        var e=TravellersCompanions.COMPANION.get().create(p.serverLevel());if(e==null)return null;
        e.configureTroop(p,kind,slot);var entry=CompanionData.get(p.server).roster(p.getUUID()).troops[kind][slot];
        if(entry.initialized)e.restore(entry.snapshot);else e.giveStarter();
        e.setHolding(false);if(entry.readyAt>0)e.setHealth(e.getMaxHealth());return e;
    }

    public static void dismiss(ServerPlayer p){
        var data=CompanionData.get(p.server);var r=data.roster(p.getUUID());
        for(int slot:List.copyOf(r.squad.keySet())){var e=active(p,slot);if(e!=null)e.dismiss();}
        r.squad.clear();r.squadKind=-1;data.setDirty();
    }

    /** Replace only the missing leases, preserving losses and soldiers ordered to hold. */
    public static void catchUp(ServerPlayer p){
        var data=CompanionData.get(p.server);var r=data.roster(p.getUUID());
        for(int slot:List.copyOf(r.squad.keySet())){
            var entry=r.troops[r.squadKind][slot];if(entry.snapshot.getBoolean("Hold")||active(p,slot)!=null)continue;
            replace(p,r.squadKind,slot);
        }
        data.setDirty();
    }

    private static void replace(ServerPlayer p,int kind,int slot){
        var r=CompanionData.get(p.server).roster(p.getUUID());var old=r.squad.get(slot);var e=create(p,kind,slot);
        if(e==null||!CompanionService.teleportNear(e,p))return;
        r.squad.put(slot,e.getUUID());
        if(!p.serverLevel().addFreshEntity(e)){r.squad.put(slot,old);return;}
        e.checkpoint();
    }

    public static void travel(ServerPlayer p){
        var data=CompanionData.get(p.server);var r=data.roster(p.getUUID());int kind=r.squadKind;
        if(!TroopKind.valid(kind))return;
        for(int slot:List.copyOf(r.squad.keySet())){
            var old=active(p,slot);if(old!=null)old.dismiss();r.squad.put(slot,UUID.randomUUID());
            // Keep the lease if the destination is unsafe; catch-up retries on safe ground.
            r.troops[kind][slot].snapshot.putBoolean("Hold",false);replace(p,kind,slot);
        }
        data.setDirty();
    }

    public static String interact(ServerPlayer p,CompanionEntity e,int action){
        if(action==6)e.setHolding(!e.holding());
        else if(action==7)e.setStance((e.stance()+1)%3);
        else return "Troops have follow/hold and combat stance orders; they do not level or gain named abilities.";
        e.checkpoint();return "";
    }

    public static CompoundTag state(ServerPlayer p){
        var r=CompanionData.get(p.server).roster(p.getUUID());var n=new CompoundTag();n.putInt("Kind",r.squadKind);n.putInt("Count",r.squad.size());
        var groups=new ListTag();for(int kind=0;kind<TroopKind.values().length;kind++){
            var group=new CompoundTag();group.putBoolean("Active",r.squadKind==kind&&!r.squad.isEmpty());group.putLong("Recovery",recovery(r,kind));var members=new ListTag();
            for(int slot=0;slot<TroopKind.SIZE;slot++){
                var saved=r.troops[kind][slot];var live=r.squadKind==kind?active(p,slot):null;var member=new CompoundTag();
                member.putBoolean("Active",r.squadKind==kind&&r.squad.containsKey(slot));member.putFloat("Health",live!=null?live.getHealth():saved.initialized?saved.snapshot.getFloat("Health"):TroopKind.HEALTH);
                member.putBoolean("Hold",live!=null?live.holding():saved.snapshot.getBoolean("Hold"));member.putInt("Stance",live!=null?live.stance():saved.snapshot.getInt("Stance"));member.putLong("Recovery",Math.max(0,saved.readyAt-System.currentTimeMillis()));members.add(member);
            }
            group.put("Members",members);groups.add(group);
        }
        n.put("Groups",groups);return n;
    }
}
