package dev.travellerscompanions;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.effect.*;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;

public final class CompanionService {
    public static java.util.List<Integer> activeKinds(ServerPlayer p){return java.util.List.copyOf(CompanionData.get(p.server).roster(p.getUUID()).party.keySet());}
    public static CompanionEntity active(ServerPlayer p){for(int kind:activeKinds(p)){var e=active(p,kind);if(e!=null)return e;}return null;}
    public static CompanionEntity active(ServerPlayer p,int kind){var id=CompanionData.get(p.server).roster(p.getUUID()).party.get(kind);if(id==null)return null;for(var level:p.server.getAllLevels()){var e=level.getEntity(id);if(e instanceof CompanionEntity c&&c.ownedBy(p)&&c.validLease()&&!c.isRemoved())return c;}return null;}
    public static String summon(ServerPlayer p,int kind){
        if(kind<0||kind>=CharacterKind.COUNT||!p.isAlive()||p.isSpectator())return "You cannot summon a companion now.";
        var data=CompanionData.get(p.server);var r=data.roster(p.getUUID());var entry=r.entries[kind];
        if(entry.readyAt>System.currentTimeMillis())return "This companion is still recovering.";
        if(r.party.containsKey(kind))return "This companion is already travelling with you.";
        if(r.party.size()>=CompanionData.PARTY_LIMIT)return "Your party is full. Dismiss a companion first; up to two can travel with you.";
        var e=TravellersCompanions.COMPANION.get().create(p.serverLevel());if(e==null)return "The companion could not be created.";
        e.configure(p,kind,entry.level);
        if(entry.initialized)e.restore(entry.snapshot);else e.giveStarter();
        // A newly summoned companion always follows, even if their last order was hold.
        e.setHolding(false);
        if(!teleportNear(e,p))return "Find safe, solid ground with room beside you.";
        r.party.put(kind,e.getUUID());
        if(!p.serverLevel().addFreshEntity(e)){r.party.remove(kind);return "There is no room to summon your companion.";}
        if(entry.readyAt>0){e.setHealth(e.getMaxHealth());entry.readyAt=0;}
        entry.initialized=true;e.checkpoint();data.setDirty();
        e.say("Ready when you are. Let's see where the road leads.",false);
        if(kind==3&&e.getMainHandItem().is(Items.TRIDENT))p.sendSystemMessage(Component.literal("Epic Knights spear unavailable: Castelan is using a trident. Set castelanStartingSpear in the common config if your pack uses a different item ID."));
        return "";
    }
    public static void dismiss(ServerPlayer p){for(int kind:activeKinds(p))dismiss(p,kind);TroopService.dismiss(p);}
    public static void dismiss(ServerPlayer p,int kind){var d=CompanionData.get(p.server);var r=d.roster(p.getUUID());var e=active(p,kind);if(e!=null)e.dismiss();r.party.remove(kind);d.setDirty();}
    public static void openInventory(ServerPlayer p,int id,boolean spells){
        if(!(p.level().getEntity(id) instanceof CompanionEntity e)||!e.ownedBy(p)||!e.validLease()||p.distanceToSqr(e)>64||!p.isAlive()||spells&&e.kind()!=CharacterKind.LEONARDO)return;
        if(spells)net.minecraftforge.network.NetworkHooks.openScreen(p,new net.minecraft.world.SimpleMenuProvider((window,inv,player)->new SpellMenu(window,inv,e),Component.literal("Leonardo - Spells")),b->b.writeVarInt(id));
        else net.minecraftforge.network.NetworkHooks.openScreen(p,new net.minecraft.world.SimpleMenuProvider((window,inv,player)->new CompanionMenu(window,inv,e),Component.literal(e.displayName()+" - Equipment")),b->b.writeVarInt(id));
    }
    public static String train(ServerPlayer p,int kind){
        if(kind<0||kind>=CharacterKind.COUNT)return "Unknown companion.";var d=CompanionData.get(p.server);var r=d.roster(p.getUUID());var e=r.entries[kind];
        if(e.level>=CompanionData.MAX_LEVEL)return "This companion has reached level 20.";
        int cost=CharacterKind.trainingCost(e.level);if(p.getInventory().countItem(Items.EMERALD)<cost)return "Training requires "+cost+" emeralds.";
        int remaining=cost;for(int i=0;i<p.getInventory().getContainerSize()&&remaining>0;i++){var s=p.getInventory().getItem(i);if(s.is(Items.EMERALD)){int n=Math.min(s.getCount(),remaining);s.shrink(n);remaining-=n;}}
        e.level++;e.bond+=3;var active=active(p,kind);if(active!=null){active.setLevel(e.level);active.checkpoint();}d.setDirty();p.getInventory().setChanged();return "";
    }
    public static boolean teleportNear(CompanionEntity e,LivingEntity owner){return teleportNear(e,owner,java.util.List.of());}
    public static boolean teleportNear(CompanionEntity e,LivingEntity owner,java.util.List<net.minecraft.world.phys.AABB> reserved){
        for(int dy:new int[]{0,1,-1,2,-2})for(int radius=2;radius<=4;radius++)for(int dx=-radius;dx<=radius;dx++)for(int dz=-radius;dz<=radius;dz++){
            if(Math.abs(dx)!=radius&&Math.abs(dz)!=radius)continue;BlockPos at=owner.blockPosition().offset(dx,dy,dz);var world=owner.level();
            if(!world.hasChunkAt(at)||!world.getWorldBorder().isWithinBounds(at)||!world.getBlockState(at.below()).isSolidRender(world,at.below())||!world.getFluidState(at).isEmpty())continue;
            if(WalkNodeEvaluator.getBlockPathTypeStatic(world,at.mutable())!=net.minecraft.world.level.pathfinder.BlockPathTypes.WALKABLE)continue;
            var box=e.getBoundingBox().move(at.getX()+.5-e.getX(),at.getY()-e.getY(),at.getZ()+.5-e.getZ());
            if(world.noCollision(e,box)&&reserved.stream().noneMatch(box::intersects)&&world.getEntitiesOfClass(LivingEntity.class,box,m->m!=e&&m.isAlive()).isEmpty()){e.moveTo(at.getX()+.5,at.getY(),at.getZ()+.5,owner.getYRot(),0);e.getNavigation().stop();return true;}
        }return false;
    }
    /** All wheel actions are checked again server side; the screen never grants privileges. */
    public static String interact(ServerPlayer p,int entityId,int action){
        if(!(p.level().getEntity(entityId) instanceof CompanionEntity e)||!e.ownedBy(p)||!e.validLease()||p.distanceToSqr(e)>64||!p.isAlive())return "Your companion is too far away.";
        if(e.isTroop())return TroopService.interact(p,e,action);
        var data=CompanionData.get(p.server);var entry=data.roster(p.getUUID()).entries[e.kind().ordinal()];
        switch(action){
            case 4->{e.say(e.kind().dialogue(entry.level,entry.dialogue++),false);entry.bond=Math.min(9999,entry.bond+1);}
            case 5->e.say(e.kind().lore,false);
            case 6->{e.setHolding(!e.holding());e.say(e.holding()?"I'll hold this position.":"Right behind you.",false);}
            case 7->{if(entry.level<3)return "Combat stances unlock at level 3.";e.setStance((e.stance()+1)%3);e.say("Understood. "+new String[]{"I'll engage nearby hostiles.","I'll fight only to defend us.","I'll keep my weapon sheathed."}[e.stance()],false);}
            case 8->{
                if(entry.level<5)return "Rally unlocks at level 5.";if(entry.rallyAt>System.currentTimeMillis())return "Rally is still recovering.";
                entry.rallyAt=System.currentTimeMillis()+120000;
                p.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,200,0));e.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,200,0));e.heal(4);e.say("Steady now. We've got each other.",false);
            }
            case 9->{
                if(entry.level<8)return "Scout's insight unlocks at level 8.";
                var enemies=p.level().getEntitiesOfClass(LivingEntity.class,e.getBoundingBox().inflate(24),m->m.isAlive()&&CompanionTargets.isMonster(m)&&e.canAttack(m));
                e.say(enemies.isEmpty()?"The next stretch looks quiet.":"I count "+enemies.size()+" hostile creatures within 24 blocks.",false);
                for(var m:enemies)m.addEffect(new MobEffectInstance(MobEffects.GLOWING,160));
            }
            case 10->{
                if(entry.level<12)return "Field dressing unlocks at level 12.";if(e.getTarget()!=null)return "Finish the fight before tending wounds.";
                int slot=-1;for(int i=0;i<27;i++)if(e.backpack.getItem(i).is(Items.BREAD)){slot=i;break;}
                if(slot<0)return "Put bread in your companion's backpack first.";if(p.getHealth()>=p.getMaxHealth()&&e.getHealth()>=e.getMaxHealth())return "Neither of you needs healing.";
                e.backpack.getItem(slot).shrink(1);p.heal(6);e.heal(6);e.checkpoint();e.say("A moment to breathe. We'll be all right.",false);
            }
            case 11->{if(entry.level<15)return "The oath unlocks at level 15.";e.say(e.kind().dialogue(20,3),false);}
            default->{return "Unknown order.";}
        }data.setDirty();return "";
    }
}
