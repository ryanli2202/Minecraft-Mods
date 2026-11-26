package dev.travellerscompanions.qa;

import dev.travellerscompanions.*;
import java.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.*;
import net.minecraft.nbt.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.gametest.*;

@GameTestHolder(TravellersCompanions.ID)
@PrefixGameTestTemplate(false)
public final class TroopGameTests {
    private static List<CompanionEntity> summon(GameTestHelper t,ServerPlayer p,int kind){
        String error=TroopService.summon(p,kind);t.assertTrue(error.isEmpty(),"Group summon: "+error);
        var troops=new ArrayList<CompanionEntity>();for(int i=0;i<TroopKind.SIZE;i++){var e=TroopService.active(p,i);t.assertTrue(e!=null,"Missing soldier "+i);troops.add(e);}return troops;
    }
    private static LivingEntity enemy(GameTestHelper t,CompanionEntity e,double distance){
        var target=t.spawn(EntityType.HUSK,9,2,7);target.moveTo(e.position().add(distance,0,0));target.setNoAi(true);target.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1000);target.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1);target.setHealth(1000);return target;
    }

    @GameTest(template="empty") public static void healthUpgradePreservesDamageAndNeverRepeats(GameTestHelper t){
        var p=CompanionGameTests.player(t);
        for(var kind:CharacterKind.values()){
            var e=CompanionGameTests.summon(t,p,kind.ordinal());t.assertTrue(e.getMaxHealth()==100&&e.getHealth()==100,"Named companion does not start at 100");
            var old=e.snapshot();old.remove("MaxHealth");old.putFloat("Health",kind.legacyHealth-7);e.restore(old);
            t.assertTrue(e.getHealth()==93,"Legacy damage deficit not preserved");e.checkpoint();CompanionService.dismiss(p,kind.ordinal());
            e=CompanionGameTests.summon(t,p,kind.ordinal());t.assertTrue(e.getHealth()==93,"Resummon healed twice");
            p.getInventory().add(new ItemStack(Items.EMERALD,4));t.assertTrue(CompanionService.train(p,kind.ordinal()).isEmpty()&&e.getMaxHealth()==102&&e.getHealth()==95,"Training lost health progression");
            var n=new CompoundTag();e.saveWithoutId(n);var clone=TravellersCompanions.COMPANION.get().create(t.getLevel());clone.load(n);t.assertTrue(clone.getMaxHealth()==102&&clone.getHealth()==95,"Reload changed health");CompanionService.dismiss(p);
        }t.succeed();
    }

    @GameTest(template="empty") public static void everyGroupSpawnsFourBesideTwoNamedWithNoLeveling(GameTestHelper t){
        var p=CompanionGameTests.player(t);var first=CompanionGameTests.summon(t,p,0);var second=CompanionGameTests.summon(t,p,2);
        for(var kind:TroopKind.values()){
            var troops=summon(t,p,kind.ordinal());t.assertTrue(first.validLease()&&second.validLease()&&CompanionService.activeKinds(p).size()==2,"Troops consumed named slots");
            for(var e:troops){t.assertTrue(e.validLease()&&e.isTroop()&&e.isTame()&&e.ownedBy(p)&&e.troopKind()==kind&&e.getMaxHealth()==20&&e.getHealth()==20,"Wrong troop identity or health");e.setLevel(20);t.assertTrue(e.companionLevel()==1&&e.getMaxHealth()==20&&e.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue()==1,"Troop leveled up");
                for(var other:troops)if(e!=other)t.assertFalse(e.getBoundingBox().intersects(other.getBoundingBox()),"Group members overlap at spawn");
                if(kind==TroopKind.FOOTSQUIRE)t.assertTrue(e.getMainHandItem().is(Items.IRON_SWORD),"Sword absent");
                if(kind==TroopKind.FOOTPALADIN)t.assertTrue(e.getOffhandItem().is(Items.SHIELD)&&!e.getMainHandItem().isEmpty()&&!e.getMainHandItem().is(Items.IRON_SWORD),"Spear/shield absent");
                if(kind==TroopKind.FOOTARCHER)t.assertTrue(e.getMainHandItem().is(Items.BOW)&&e.backpack.countItem(Items.ARROW)==64,"Bow/arrows absent");
            }
            for(int other=0;other<3;other++)t.assertFalse(TroopService.summon(p,other).isEmpty(),"Second group allowed");
            t.assertFalse(CompanionService.summon(p,1).isEmpty(),"Third named companion allowed");TroopService.dismiss(p);
        }t.succeed();
    }

    @GameTest(template="empty") public static void groupSaveNetworkAndInventoryStaySeparate(GameTestHelper t){
        var p=CompanionGameTests.player(t);var named=CompanionGameTests.summon(t,p,0);var troops=summon(t,p,2);
        for(int i=0;i<4;i++){var e=troops.get(i);e.setHealth(10+i);e.backpack.getItem(0).shrink(10+i);e.backpack.setItem(2,new ItemStack(Items.DIAMOND,i+1));e.checkpoint();}
        var data=CompanionData.get(p.server);var saved=CompanionData.load(data.save(new CompoundTag())).roster(p.getUUID());
        t.assertTrue(saved.party.size()==1&&saved.squad.size()==4&&saved.squadKind==2,"Group leases not saved");
        var state=CompanionNetwork.state(p);t.assertTrue(state.getInt("PartyCount")==1&&state.getCompound("Troops").getInt("Count")==4,"Network confused party counts");
        for(int i=0;i<4;i++){var n=new CompoundTag();troops.get(i).saveWithoutId(n);var clone=TravellersCompanions.COMPANION.get().create(t.getLevel());clone.load(n);t.assertTrue(clone.isTroop()&&clone.troopSlot()==i&&clone.getHealth()==10+i&&clone.getMaxHealth()==20,"Entity identity or health lost");}
        TroopService.dismiss(p);troops=summon(t,p,2);
        for(int i=0;i<4;i++){var e=troops.get(i);t.assertTrue(e.getHealth()==10+i&&e.backpack.countItem(Items.ARROW)==54-i&&e.backpack.countItem(Items.DIAMOND)==i+1,"Troop items or health reset");}
        t.assertTrue(named.validLease()&&named.backpack.countItem(Items.ARROW)==64&&named.getHealth()==100,"Troops changed named state");t.succeed();
    }

    @GameTest(template="empty") public static void casualtiesKeepSurvivorsAndRecoveryCannotBeBypassed(GameTestHelper t){
        var p=CompanionGameTests.player(t);var named=CompanionGameTests.summon(t,p,1);var troops=summon(t,p,0);var r=CompanionData.get(p.server).roster(p.getUUID());
        troops.get(1).backpack.setItem(3,new ItemStack(Items.DIAMOND,3));troops.get(1).hurt(t.getLevel().damageSources().generic(),1000);
        t.assertTrue(troops.get(1).isRemoved()&&r.squad.size()==3&&named.validLease()&&r.entries[1].readyAt==0,"Casualty affected other members");
        t.assertTrue(r.troops[0][1].readyAt>System.currentTimeMillis()+599000,"Troop recovery missing");
        p.tickCount=100;CompanionEvents.catchUp(new net.minecraftforge.event.TickEvent.PlayerTickEvent(net.minecraftforge.event.TickEvent.Phase.END,p));t.assertTrue(r.squad.size()==3,"Catch-up resurrected dead soldier");
        TroopService.dismiss(p);t.assertFalse(TroopService.summon(p,0).isEmpty(),"Dismiss bypassed recovery");
        summon(t,p,2);TroopService.dismiss(p);t.assertFalse(TroopService.summon(p,0).isEmpty(),"Switching type bypassed recovery");
        r.troops[0][1].readyAt=System.currentTimeMillis()-1;troops=summon(t,p,0);t.assertTrue(troops.get(1).getHealth()==20&&troops.get(1).backpack.countItem(Items.DIAMOND)==3,"Recovery lost equipment");t.succeed();
    }

    @GameTest(template="empty",timeoutTicks=40) public static void catchUpRejectsStaleCopiesAndTravelPreservesCasualties(GameTestHelper t){
        var p=CompanionGameTests.player(t);var troops=summon(t,p,1);var first=troops.get(0);first.backpack.setItem(3,new ItemStack(Items.GOLD_INGOT,7));first.checkpoint();var old=new CompoundTag();first.saveWithoutId(old);first.discard();troops.get(2).setHolding(true);
        p.tickCount=100;CompanionEvents.catchUp(new net.minecraftforge.event.TickEvent.PlayerTickEvent(net.minecraftforge.event.TickEvent.Phase.END,p));var replacement=TroopService.active(p,0);
        t.assertTrue(replacement!=first&&replacement.backpack.countItem(Items.GOLD_INGOT)==7&&troops.get(2).holding(),"Catch-up changed supplies or hold");
        var stale=TravellersCompanions.COMPANION.get().create(t.getLevel());stale.load(old);t.getLevel().addFreshEntity(stale);
        troops.get(1).die(t.getLevel().damageSources().generic());TroopService.travel(p);
        t.assertTrue(CompanionData.get(p.server).roster(p.getUUID()).squad.size()==3&&TroopService.active(p,1)==null&&TroopService.active(p,0).backpack.countItem(Items.GOLD_INGOT)==7&&!TroopService.active(p,2).holding(),"Travel reset group or resurrected casualty");
        t.runAfterDelay(4,()->{t.assertTrue(stale.isRemoved(),"Stale troop survived");CompanionEvents.logout(new net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent(p));t.assertTrue(CompanionData.get(p.server).roster(p.getUUID()).squad.isEmpty(),"Logout left troops active");t.succeed();});
    }

    @GameTest(template="empty") public static void unsafeGroupSummonIsAtomic(GameTestHelper t){
        var p=CompanionGameTests.player(t);p.setPos(p.getX(),200,p.getZ());var at=p.blockPosition();
        for(int i=0;i<3;i++)t.getLevel().setBlockAndUpdate(at.offset(-2,-1,i-1),Blocks.STONE.defaultBlockState());
        t.assertFalse(TroopService.summon(p,0).isEmpty(),"Partial group allowed with only three safe positions");var r=CompanionData.get(p.server).roster(p.getUUID());
        t.assertTrue(r.squad.isEmpty()&&r.squadKind==-1,"Failed group left leases");for(var e:r.troops[0])t.assertTrue(!e.initialized&&e.snapshot.isEmpty(),"Failed summon consumed starter state");t.succeed();
    }

    @GameTest(template="empty") public static void troopOrdersPermissionsAndOwnerDeath(GameTestHelper t){
        var p=CompanionGameTests.player(t);var q=CompanionGameTests.player(t);var e=summon(t,p,0).get(0);var other=summon(t,q,0).get(0);
        t.assertFalse(CompanionService.interact(q,e.getId(),6).isEmpty(),"Stranger commanded troop");t.assertFalse(new CompanionMenu(0,p.getInventory(),e).stillValid(q),"Stranger opened troop gear");
        t.assertTrue(CompanionService.interact(p,e.getId(),6).isEmpty()&&e.holding(),"Hold order failed");t.assertTrue(CompanionService.interact(p,e.getId(),7).isEmpty()&&e.stance()==1,"Troop stance locked behind levels");
        for(int action:new int[]{4,5,8,9,10,11})t.assertFalse(CompanionService.interact(p,e.getId(),action).isEmpty(),"Troop gained named ability");
        CompanionEvents.ownerDeath(new net.minecraftforge.event.entity.living.LivingDeathEvent(p,t.getLevel().damageSources().generic()));t.assertTrue(e.isRemoved()&&other.validLease(),"Owner death affected wrong troop group");t.succeed();
    }

    @GameTest(template="empty",timeoutTicks=220) public static void footsquiresFightWithoutAdvancingNamedProgress(GameTestHelper t){
        var p=CompanionGameTests.player(t);var troops=summon(t,p,0);var e=troops.get(0);for(var other:troops)if(other!=e)other.setStance(2);var target=enemy(t,e,2);e.setTarget(target);
        t.succeedWhen(()->{t.assertTrue(target.getHealth()<1000&&e.getMainHandItem().getDamageValue()>0,"Footsquire never attacked");target.hurt(t.getLevel().damageSources().mobAttack(e),10000);var entry=CompanionData.get(p.server).roster(p.getUUID()).entries[1];t.assertTrue(entry.level==1&&entry.kills==0&&entry.bond==0&&e.companionLevel()==1,"Troop kill advanced named progression");});
    }

    @GameTest(template="empty",timeoutTicks=220) public static void footarchersFireRealArrowsAndProtectTheParty(GameTestHelper t){
        var p=CompanionGameTests.player(t);var named=CompanionGameTests.summon(t,p,1);named.setStance(2);var troops=summon(t,p,2);var e=troops.get(0);for(var other:troops)if(other!=e)other.setStance(2);var target=enemy(t,e,5);e.setTarget(target);e.setHolding(true);e.setTarget(target);p.moveTo(e.position().add(-3,0,0));
        named.setHolding(true);named.moveTo(e.position().add(0,0,4));for(int i=1;i<4;i++){troops.get(i).setHolding(true);troops.get(i).moveTo(e.position().add(i,0,4));}
        var source=t.getLevel().damageSources().arrow(new net.minecraft.world.entity.projectile.Arrow(t.getLevel(),e),e);
        for(var friend:List.of(p,named,troops.get(1))){var event=new net.minecraftforge.event.entity.living.LivingAttackEvent(friend,source,8);net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);t.assertTrue(event.isCanceled(),"Troop arrow can hurt party");}
        t.succeedWhen(()->t.assertTrue(e.backpack.countItem(Items.ARROW)<64&&target.getHealth()<1000,"Footarcher: ammo="+e.backpack.countItem(Items.ARROW)+" target hp="+target.getHealth()+" target="+e.getTarget()+" pos="+e.position()+" victim="+target.position()));
    }

    @GameTest(template="empty",timeoutTicks=160) public static void footpaladinsRaiseShieldsBlockFrontAndRemainVulnerableBehind(GameTestHelper t){
        var p=CompanionGameTests.player(t);var troops=summon(t,p,1);var e=troops.get(0);for(var other:troops)if(other!=e)other.setStance(2);var target=enemy(t,e,2);e.setHolding(true);e.setTarget(target);e.backpack.clearContent();
        t.succeedWhen(()->{
            t.assertTrue(e.isBlocking()&&e.getMainHandItem().getDamageValue()>0,"Paladin did not alternate spear attacks and shield guard");
            var front=e.position().add(e.getLookAngle().multiply(1,0,1).normalize().scale(2));target.moveTo(front);float before=e.getHealth();int wear=e.getOffhandItem().getDamageValue();
            e.hurt(t.getLevel().damageSources().mobAttack(target),4);t.assertTrue(e.getHealth()==before&&e.getOffhandItem().getDamageValue()>wear,"Frontal hit was not blocked");
            target.moveTo(e.position().subtract(front.subtract(e.position())));e.invulnerableTime=0;e.hurt(t.getLevel().damageSources().mobAttack(target),4);t.assertTrue(e.getHealth()<before,"Rear hit incorrectly blocked");
            e.setStance(2);t.assertFalse(e.isBlocking(),"Passive stance kept shield raised");
        });
    }
}
