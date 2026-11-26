package dev.travellerscompanions.qa;

import dev.travellerscompanions.*;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.gametest.*;

@GameTestHolder(TravellersCompanions.ID)
@PrefixGameTestTemplate(false)
public final class ParkourGameTests {
    private static CompanionEntity companion(GameTestHelper t) {
        var owner = CompanionGameTests.player(t);
        var e = CompanionGameTests.summon(t, owner, 1);
        e.setStance(1); e.moveTo(t.absolutePos(new BlockPos(5,2,7)).getCenter().add(0,-.5,0)); e.setOnGround(true);
        return e;
    }
    private static LivingEntity enemy(GameTestHelper t, CompanionEntity e) {
        var mob = t.spawn(EntityType.HUSK, 5, 2, 5); mob.setNoAi(true);
        mob.moveTo(e.position().add(0,0,-2)); return mob;
    }
    private static boolean enabled(GameTestHelper t) {
        if (ParCoolBridge.installed()) return true;
        t.succeed(); return false;
    }
    private static void successfulChance(CompanionEntity e) {
        for (long seed=0; seed<100000; seed++) {
            e.getRandom().setSeed(seed);
            if (e.getRandom().nextDouble()<TravellersCompanions.DODGE_CHANCE.get()) { e.getRandom().setSeed(seed); return; }
        }
        throw new IllegalStateException("No deterministic dodge seed");
    }
    @GameTest(template="empty") public static void optionalModsAndSprintState(GameTestHelper t) {
        var e=companion(t); var owner=e.getOwner(); owner.moveTo(e.position().add(8,0,0)); owner.setSprinting(true);
        e.parkour.tick();
        t.assertTrue((e.parkourAction()==CompanionParkour.FAST_RUN)==ParCoolBridge.installed(),"Optional ParCool sprint gating failed");
        if (ParCoolBridge.installed()) t.assertTrue(e.parkour.followSpeed()>1.1,"Sprint follow speed did not increase");
        owner.setSprinting(false); e.parkour.tick(); t.assertTrue(e.parkourAction()==0,"Fast run stuck after owner stopped");
        owner.setSprinting(true); e.setHolding(true); e.parkour.tick(); t.assertTrue(e.parkourAction()==0,"Hold started sprinting");
        t.succeed();
    }
    @GameTest(template="empty",timeoutTicks=120) public static void followsAcrossTwoBlockLedgeWithoutTeleporting(GameTestHelper t) {
        if(!enabled(t))return;
        var e=companion(t); e.setStance(2);
        for(int x=6;x<15;x++)for(int z=0;z<15;z++){t.setBlock(x,2,z,Blocks.STONE);t.setBlock(x,3,z,Blocks.STONE);}
        e.getOwner().moveTo(e.position().add(6,2,0));
        t.succeedWhen(()->{
            t.assertTrue(e.getY()>t.absolutePos(new BlockPos(0,3,0)).getY()+.9&&e.getX()>t.absolutePos(new BlockPos(6,0,0)).getX(),"Companion never climbed two-block ledge: "+e.position());
            t.assertTrue(e.distanceTo(e.getOwner())<7,"Unexpected teleport or movement");
        });
    }
    @GameTest(template="empty") public static void fastMovementAndRealParCoolActionTriggerFollowing(GameTestHelper t) {
        if(!enabled(t))return;
        var e=companion(t);var owner=(net.minecraft.world.entity.player.Player)e.getOwner();
        owner.moveTo(e.position().add(7,0,0));e.parkour.tick();
        for(int i=0;i<5;i++){owner.setPos(owner.position().add(.45,0,0));e.parkour.tick();}
        t.assertTrue(e.parkourAction()==CompanionParkour.FAST_RUN,"Measured owner speed did not trigger fast follow");
        for(int i=0;i<20;i++)e.parkour.tick();
        t.assertTrue(e.parkourAction()==0,"Fast follow stayed active after movement stopped");
        var ability=com.alrex.parcool.common.capability.Parkourability.get(owner);
        t.assertTrue(ability!=null,"ParCool capability absent");
        var fast=ability.get(com.alrex.parcool.common.action.impl.FastRun.class);
        fast.start(owner,ability,java.nio.ByteBuffer.allocate(0),null);
        try{e.parkour.tick();t.assertTrue(ParCoolBridge.fastRunning(owner)&&e.parkourAction()==CompanionParkour.FAST_RUN,"Actual ParCool action was not detected");}
        finally{fast.finish(owner);}
        t.succeed();
    }
    @GameTest(template="empty") public static void rejectsTallWallsLowCeilingsAndHazardousLedges(GameTestHelper t) {
        if(!enabled(t))return;
        var e=companion(t);
        for(int x=6;x<=8;x++)for(int y=2;y<=4;y++)for(int z=6;z<=8;z++)t.setBlock(x,y,z,Blocks.STONE);
        t.assertFalse(e.parkour.tryClimb(new Vec3(1,0,0)),"Climbed a three-block wall");
        for(int x=6;x<=8;x++)for(int z=6;z<=8;z++)t.setBlock(x,4,z,Blocks.AIR);
        t.setBlock(5,4,7,Blocks.STONE);
        for(int i=0;i<8;i++)e.parkour.tick();
        t.assertFalse(e.parkour.tryClimb(new Vec3(1,0,0)),"Climbed through low ceiling");
        t.setBlock(5,4,7,Blocks.AIR);
        for(int x=6;x<=8;x++)for(int z=6;z<=8;z++)t.setBlock(x,4,z,Blocks.FIRE);
        for(int i=0;i<8;i++)e.parkour.tick();
        t.assertFalse(e.parkour.tryClimb(new Vec3(1,0,0)),"Climbed onto fire"); t.succeed();
    }
    @GameTest(template="empty",timeoutTicks=45) public static void rollsOnAttackMovesAndKeepsCooldown(GameTestHelper t) {
        if(!enabled(t))return;
        var e=companion(t); var mob=enemy(t,e); Vec3 origin=e.position(); float health=e.getHealth();
        successfulChance(e);
        t.assertFalse(e.hurt(t.getLevel().damageSources().mobAttack(mob),4),"Eligible hit not evaded");
        t.assertTrue(e.parkourAction()==CompanionParkour.ROLL&&e.getHealth()==health,"Roll or health state wrong");
        t.assertTrue(e.hurt(t.getLevel().damageSources().generic(),1),"Roll made companion invulnerable to environment");
        t.runAfterDelay(11,()->{
            t.assertTrue(e.position().distanceTo(origin)>1.5&&!e.parkour.busy(),"Roll did not move or finish: "+e.position());
            e.invulnerableTime=0; successfulChance(e);
            t.assertFalse(e.parkour.dodge(t.getLevel().damageSources().mobAttack(mob)),"Dodge cooldown bypassed");
            t.succeed();
        });
    }
    @GameTest(template="empty") public static void rollCannotLeavePlatformOrIgnoreHoldAndPassive(GameTestHelper t) {
        if(!enabled(t))return;
        var e=companion(t); var mob=enemy(t,e);
        // Every escape direction crosses unsupported space or a wall.
        for(int x=3;x<=8;x++)for(int z=5;z<=10;z++)if(x!=5||z!=7)t.setBlock(x,1,z,Blocks.AIR);
        successfulChance(e);t.assertFalse(e.parkour.dodge(t.getLevel().damageSources().mobAttack(mob)),"Rolled over a cliff");
        for(int x=3;x<=8;x++)for(int z=5;z<=10;z++)t.setBlock(x,1,z,Blocks.STONE);
        for(int i=0;i<20;i++)e.parkour.tick();
        e.setHolding(true);successfulChance(e);t.assertFalse(e.parkour.dodge(t.getLevel().damageSources().mobAttack(mob)),"Hold allowed roll");
        e.setHolding(false);e.setStance(2);successfulChance(e);t.assertFalse(e.parkour.dodge(t.getLevel().damageSources().mobAttack(mob)),"Passive allowed roll");
        t.succeed();
    }
}
