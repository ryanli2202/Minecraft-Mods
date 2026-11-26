package dev.travellerscompanions.qa;

import dev.travellerscompanions.*;
import net.minecraft.gametest.framework.*;
import net.minecraft.nbt.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraftforge.gametest.*;

@GameTestHolder(TravellersCompanions.ID)
@PrefixGameTestTemplate(false)
public final class PartyGameTests {
    @GameTest(template="empty") public static void twoCompanionsAndSeparateOwners(GameTestHelper t){
        var p=CompanionGameTests.player(t);var first=CompanionGameTests.summon(t,p,0);var second=CompanionGameTests.summon(t,p,1);
        t.assertTrue(first.validLease()&&second.validLease()&&CompanionService.activeKinds(p).size()==2,"Two companions did not retain independent leases");
        t.assertFalse(CompanionService.summon(p,2).isEmpty(),"Third companion allowed");t.assertFalse(CompanionService.summon(p,1).isEmpty(),"Duplicate character allowed");
        var q=CompanionGameTests.player(t);var other=CompanionGameTests.summon(t,q,1);t.assertTrue(other!=second&&other.ownedBy(q),"Owners share roster");CompanionService.dismiss(q);
        t.assertTrue(!first.isRemoved()&&!second.isRemoved(),"Other owner's dismiss affected our party");
        CompanionService.dismiss(p,0);t.assertTrue(first.isRemoved()&&!second.isRemoved()&&second.validLease(),"Individual dismissal removed wrong companion");
        var replacement=CompanionGameTests.summon(t,p,2);t.assertTrue(replacement.validLease()&&second.validLease(),"Vacated slot could not be filled");CompanionService.dismiss(p);
        t.assertTrue(second.isRemoved()&&replacement.isRemoved()&&CompanionService.activeKinds(p).isEmpty(),"Dismiss all left an active companion");t.succeed();
    }

    @GameTest(template="empty") public static void partySaveNetworkTrainingAndRecoveryAreIndependent(GameTestHelper t){
        var p=CompanionGameTests.player(t);var first=CompanionGameTests.summon(t,p,0);var second=CompanionGameTests.summon(t,p,1);
        first.backpack.setItem(2,new ItemStack(Items.DIAMOND,7));second.backpack.setItem(2,new ItemStack(Items.GOLD_INGOT,13));first.setHealth(12);second.setHealth(19);second.setHolding(true);
        p.getInventory().add(new ItemStack(Items.EMERALD,4));t.assertTrue(CompanionService.train(p,1).isEmpty()&&second.companionLevel()==2&&first.companionLevel()==1,"Training affected wrong member");first.checkpoint();second.checkpoint();
        var data=CompanionData.get(p.server);var saved=CompanionData.load(data.save(new CompoundTag())).roster(p.getUUID());
        t.assertTrue(saved.party.size()==2&&first.getUUID().equals(saved.party.get(0))&&second.getUUID().equals(saved.party.get(1)),"Party leases did not survive save/load");
        var clone=TravellersCompanions.COMPANION.get().create(t.getLevel());clone.configure(p,1,2);clone.restore(saved.entries[1].snapshot);t.assertTrue(clone.backpack.countItem(Items.GOLD_INGOT)==13,"Second inventory lost on save/load");
        var state=CompanionNetwork.state(p);var list=state.getList("Companions",Tag.TAG_COMPOUND);
        t.assertTrue(state.getInt("PartyCount")==2&&state.getInt("PartyLimit")==2&&list.getCompound(0).getBoolean("Active")&&list.getCompound(1).getBoolean("Active")&&!list.getCompound(2).getBoolean("Active"),"Network party flags incorrect");
        t.assertTrue(list.getCompound(0).getFloat("Health")==12&&list.getCompound(1).getFloat("Health")==second.getHealth()&&list.getCompound(1).getBoolean("Hold")&&!list.getCompound(0).getBoolean("Hold"),"Network state mixed members");
        second.die(t.getLevel().damageSources().generic());t.assertTrue(first.validLease()&&!first.isRemoved()&&CompanionService.activeKinds(p).size()==1,"Member death dismissed survivor");
        t.assertTrue(data.roster(p.getUUID()).entries[1].readyAt>System.currentTimeMillis()&&data.roster(p.getUUID()).entries[0].readyAt==0,"Recovery affected wrong member");
        var replacement=CompanionGameTests.summon(t,p,2);p.setPos(p.getX(),200,p.getZ());CompanionService.dismiss(p,2);
        t.assertFalse(CompanionService.summon(p,3).isEmpty(),"Unsafe summon succeeded");t.assertTrue(first.validLease()&&CompanionService.activeKinds(p).size()==1&&replacement.isRemoved(),"Failed summon changed existing party");t.succeed();
    }

    @GameTest(template="empty") public static void partyTravelCatchUpAndLogoutHandleEveryMember(GameTestHelper t){
        var p=CompanionGameTests.player(t);var first=CompanionGameTests.summon(t,p,0);var second=CompanionGameTests.summon(t,p,1);second.setHolding(true);
        first.backpack.setItem(2,new ItemStack(Items.DIAMOND,7));first.checkpoint();first.discard();p.tickCount=100;
        CompanionEvents.catchUp(new net.minecraftforge.event.TickEvent.PlayerTickEvent(net.minecraftforge.event.TickEvent.Phase.END,p));
        var recovered=CompanionService.active(p,0);t.assertTrue(recovered!=null&&recovered!=first&&recovered.backpack.countItem(Items.DIAMOND)==7&&second.validLease()&&second.holding(),"Catch-up lost companion or disturbed held member");
        CompanionEvents.travel(new net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent(p,net.minecraft.world.level.Level.NETHER,p.level().dimension()));
        t.assertTrue(recovered.isRemoved()&&second.isRemoved()&&CompanionService.active(p,0)!=null&&CompanionService.active(p,1)!=null&&CompanionService.active(p,0).backpack.countItem(Items.DIAMOND)==7,"Travel did not re-create entire party with inventories");
        var currentFirst=CompanionService.active(p,0);var currentSecond=CompanionService.active(p,1);CompanionEvents.logout(new net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent(p));
        t.assertTrue(currentFirst.isRemoved()&&currentSecond.isRemoved()&&CompanionService.activeKinds(p).isEmpty(),"Logout left a party member active");t.succeed();
    }

    @GameTest(template="empty",timeoutTicks=130) public static void neutralMobsIgnoredUntilOwnerAttacks(GameTestHelper t){
        var p=CompanionGameTests.player(t);var e=CompanionGameTests.summon(t,p,1);e.setHolding(true);var enderman=t.spawn(EntityType.ENDERMAN,8,2,8);enderman.setNoAi(true);var wolf=t.spawn(EntityType.WOLF,9,2,7);wolf.setNoAi(true);var cow=t.spawn(EntityType.COW,10,2,7);cow.setNoAi(true);
        t.runAfterDelay(20,()->{t.assertTrue(e.getTarget()==null&&enderman.getHealth()==enderman.getMaxHealth()&&wolf.getHealth()==wolf.getMaxHealth()&&cow.getHealth()==cow.getMaxHealth(),"Aggressive scan targeted a neutral");e.setStance(1);p.tickCount++;p.setLastHurtByMob(wolf);});
        t.runAfterDelay(40,()->{t.assertTrue(e.getTarget()==null,"Owner defence targeted an unprovoked neutral");e.hurt(t.getLevel().damageSources().mobAttack(wolf),1);t.assertTrue(e.getTarget()==null,"Self defence targeted an unprovoked neutral");p.tickCount++;p.setLastHurtMob(cow);});
        t.runAfterDelay(60,()->{t.assertTrue(e.getTarget()==cow&&e.canAttack(cow),"Owner assist rejected passive mob");p.tickCount++;p.setLastHurtMob(enderman);});
        t.runAfterDelay(80,()->{t.assertTrue(e.getTarget()==enderman&&e.canAttack(enderman)&&!e.canAttack(cow),"Owner assist did not switch to neutral mob or left old target attackable");e.setStance(2);p.tickCount++;p.setLastHurtMob(wolf);});
        t.runAfterDelay(100,()->{t.assertTrue(e.getTarget()==null&&!e.canAttack(wolf),"Passive stance assisted owner");t.succeed();});
    }

    private static <T extends Mob> T target(GameTestHelper t,EntityType<T> type,CompanionEntity e,double distance){
        var mob=t.spawn(type,7,2,7);mob.setNoAi(true);mob.moveTo(e.position().add(distance,0,0));mob.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1000);mob.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1);mob.setHealth(1000);return mob;
    }

    @GameTest(template="empty",timeoutTicks=180) public static void bothCompanionsDamageOwnersPassiveTarget(GameTestHelper t){
        var p=CompanionGameTests.player(t);var first=CompanionGameTests.summon(t,p,1);var second=CompanionGameTests.summon(t,p,2);first.setStance(1);second.setStance(1);
        var cow=target(t,EntityType.COW,first,2);var bystander=target(t,EntityType.COW,first,3);p.attack(cow);
        t.succeedWhen(()->{t.assertTrue(first.getTarget()==cow&&second.getTarget()==cow,"Both party members must assist an actual player attack");t.assertTrue(first.getMainHandItem().getDamageValue()>0&&second.getMainHandItem().getDamageValue()>0&&cow.getHealth()<990,"Both melee companions must damage passive target");t.assertTrue(bystander.getHealth()==1000&&!first.canAttack(bystander)&&!second.canAttack(bystander),"Untouched passive bystander became attackable");});
    }

    @GameTest(template="empty",timeoutTicks=220) public static void crossbowDamagesOwnersNeutralTarget(GameTestHelper t){
        var p=CompanionGameTests.player(t);var e=CompanionGameTests.summon(t,p,4);e.setHolding(true);e.setStance(1);var wolf=target(t,EntityType.WOLF,e,5);p.moveTo(e.position().add(-3,0,0));p.attack(wolf);
        // Verify an actual companion hit after the owner's attack. The old fixed 990
        // threshold required several random-spread hits on a small wolf hitbox.
        float afterOwnerAttack=wolf.getHealth();
        t.succeedWhen(()->{t.assertTrue(e.getTarget()==wolf&&e.backpack.countItem(Items.ARROW)<64&&wolf.getHealth()<afterOwnerAttack,"Crossbow did not damage owner-attacked neutral mob: health="+wolf.getHealth()+" baseline="+afterOwnerAttack+" ammo="+e.backpack.countItem(Items.ARROW));});
    }

    @GameTest(template="empty",timeoutTicks=220) public static void spellsDamageOwnersPassiveTarget(GameTestHelper t){
        var p=CompanionGameTests.player(t);io.redspace.ironsspellbooks.api.config.SpellConfigManager.onDatapackSync(new net.minecraftforge.event.OnDatapackSyncEvent(p.server.getPlayerList(),null));
        var e=CompanionGameTests.summon(t,p,5);e.setHolding(true);e.setStance(1);e.spellInventory.clearContent();e.spellInventory.setItem(0,CompanionSpells.scroll("irons_spellbooks:magic_missile",1));var cow=target(t,EntityType.COW,e,5);p.attack(cow);
        t.succeedWhen(()->{t.assertTrue(e.getTarget()==cow&&e.spells().castsCompleted>0&&cow.getHealth()<990,"Spell did not damage owner-attacked passive mob");});
    }

    @GameTest(template="empty",timeoutTicks=260) public static void katanaDamagesOwnersNeutralTargetButProtectsBystanders(GameTestHelper t){
        var p=CompanionGameTests.player(t);var e=CompanionGameTests.summon(t,p,7);e.setHolding(true);var wolf=target(t,EntityType.WOLF,e,2);var cow=target(t,EntityType.COW,e,2);cow.moveTo(cow.position().add(0,0,.7));p.attack(wolf);
        t.succeedWhen(()->{t.assertTrue(e.getTarget()==wolf&&e.katana.basicAttacks>=2&&e.katana.specialAttacks>=1&&wolf.getHealth()<975,"Katana did not complete basic and special hits on neutral target");t.assertTrue(cow.getHealth()==1000,"Katana sweep damaged passive bystander");});
    }
}
