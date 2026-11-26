package dev.deepnight.qa;

import dev.deepnight.*;
import dev.deepnight.source.mobosses.entity.ai.SoulsMeleeAttackGoal;
import dev.deepnight.source.mobosses.entity.custom.*;
import net.minecraft.gametest.framework.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.gametest.*;

@GameTestHolder(DeepNight.ID) @PrefixGameTestTemplate(false)
public final class MoBossesAttackTests {
    private static Object field(Object object, String name) throws Exception {
        for(Class<?> type=object.getClass();type!=null;type=type.getSuperclass()) {
            try {var field=type.getDeclaredField(name);field.setAccessible(true);return field.get(object);}
            catch(NoSuchFieldException ignored) {}
        }
        throw new NoSuchFieldException(name);
    }

    private static BossLifecycleTests.Fighter fighter(GameTestHelper t) throws Exception {
        var player = new BossLifecycleTests.Fighter(t.getLevel(), 99);
        player.setGameMode(GameType.SURVIVAL);
        player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1024);
        player.setHealth(1024);
        var grace = net.minecraft.server.level.ServerPlayer.class.getDeclaredField("spawnInvulnerableTime");
        grace.setAccessible(true);
        grace.setInt(player, 0);
        return player;
    }

    @GameTest(template="empty")
    public static void demonKingElementalCombosMatchContactFrames(GameTestHelper t) throws Exception {
        for(int combo=0;combo<2;combo++)for(boolean dodge:new boolean[]{false,true}) {
            var boss=(DemonKingEntity)DeepNight.HOSTILES.get(Species.MOBOSSES_DEMON_KING).get().create(t.getLevel());
            var goal=(SoulsMeleeAttackGoal)field(boss,"meleeGoal");
            var choices=(SoulsMeleeAttackGoal.AttackDef[])field(goal,"attacks");
            var attack=choices[combo];java.util.Arrays.fill(choices,attack);
            var player=fighter(t);boss.setPos(0,180,0);player.setPos(1,180,0);boss.setTarget(player);
            goal.tick();if(dodge)player.setPos(100,180,0);
            for(int frame=1;frame<=attack.durationTicks;frame++){
                player.invulnerableTime=0;player.setRemainingFireTicks(0);player.removeAllEffects();player.setDeltaMovement(Vec3.ZERO);
                int hits=player.hits;goal.tick();
                int hitIndex=-1;for(int i=0;i<attack.hitTicks.length;i++)if(attack.hitTicks[i]==frame)hitIndex=i;
                boolean contact=hitIndex>=0&&!dodge;
                t.assertTrue(player.hits-hits==(contact?1:0),"Elemental attack altered hit timing or reach");
                int element=combo==0?hitIndex:(hitIndex==0?2:0);
                t.assertTrue(player.isOnFire()==(contact&&element==0),"Fire applied outside a successful fire strike");
                t.assertTrue(player.hasEffect(net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN)==(contact&&element==1),"Frost applied outside a successful frost strike");
                if(contact&&element==2)t.assertTrue(player.getDeltaMovement().horizontalDistance()>0.6,"Lightning strike lacked extra knockback");
            }
            boss.discard();player.discard();
        }
        t.succeed();
    }

    @GameTest(template="empty")
    public static void demonLightningWarnsThenStrikesOnceWithoutHittingMinions(GameTestHelper t) throws Exception {
        var level=t.getLevel();var player=fighter(t);player.setPos(t.absoluteVec(new Vec3(2,50,2)));level.addNewPlayer(player);
        var minion=DeepNight.HOSTILES.get(Species.MOBOSSES_DEMON_KING_MINION).get().create(level);
        minion.setPos(player.position());minion.setNoAi(true);level.addFreshEntity(minion);float health=minion.getHealth();
        var bolt=dev.deepnight.source.mobosses.MoBosses.DEMON_KING_LIGHTNING.get().create(level);bolt.setPos(player.position());
        for(int i=0;i<7;i++)bolt.tick();t.assertTrue(player.hits==0,"Lightning damaged during warning");
        bolt.tick();t.assertTrue(player.hits==1,"Lightning did not strike on its original impact frame");
        t.assertTrue(minion.getHealth()==health,"Demon lightning damaged its own summoned minion");
        for(int i=0;i<52;i++){player.invulnerableTime=0;bolt.tick();}
        t.assertTrue(player.hits==1&&bolt.isRemoved(),"Lightning repeated damage or failed to expire");
        minion.discard();player.discard();t.succeed();
    }

    // Match Mob's full/filtered GoalSelector cadence, rather than calling goal.tick
    // directly (which concealed the half-speed hit frames in the old test).
    private static void tick(GoalSelector selector, int frame) {
        if (frame % 2 == 0) selector.tick();
        else selector.tickRunningGoals(false);
    }

    @GameTest(template="empty")
    public static void everySharedMeleeHitDamagesSurvivalAtItsAnimationFrame(GameTestHelper t) throws Exception {
        int checked = 0;
        for (Species species : Species.values()) {
            if (species.kind==Species.Kind.REMOVED || !species.id.startsWith("mobosses_") || species == Species.MOBOSSES_NIL) continue;
            var boss = DeepNight.HOSTILES.get(species).get().create(t.getLevel());
            for (var wrapped : boss.goalSelector.getAvailableGoals()) {
                if (!(wrapped.getGoal() instanceof SoulsMeleeAttackGoal original)) continue;
                for (var attack : (SoulsMeleeAttackGoal.AttackDef[]) field(original, "attacks")) {
                    var player = fighter(t);
                    boss.setPos(0, 180, 0); player.setPos(1, 180, 0); boss.setTarget(player);
                    var goal = new SoulsMeleeAttackGoal(boss, 1, "attack_controller", () -> true, 3, attack);
                    var selector = new GoalSelector(t.getLevel()::getProfiler);
                    selector.addGoal(1, goal);
                    tick(selector, 0);
                    t.assertTrue(goal.isAttacking(), "Attack did not start: " + species.id);
                    int expectedHits = 0;
                    for (int frame = 1; frame <= attack.durationTicks; frame++) {
                        if (player.invulnerableTime > 0) player.invulnerableTime--;
                        tick(selector, frame);
                        for (int hit : attack.hitTicks) if (hit == frame) expectedHits++;
                        t.assertTrue(player.hits == expectedHits, species.id + "/" + attack.animName
                            + " frame " + frame + ": expected " + expectedHits + " health-loss hits, got " + player.hits);
                    }
                    t.assertFalse(goal.isAttacking(), "Attack outlasted its animation: " + species.id);
                    t.assertTrue(player.damage > 0, "No survival health loss: " + species.id);
                    // A target leaving the hitbox must still be able to dodge.
                    goal.cancel(); player.invulnerableTime = 0;
                    var miss = new SoulsMeleeAttackGoal(boss, 1, "attack_controller", () -> true, 3, attack);
                    selector = new GoalSelector(t.getLevel()::getProfiler); selector.addGoal(1, miss);
                    tick(selector, 0); player.setPos(100, 180, 0);
                    int before = player.hits;
                    for (int frame = 1; frame <= attack.durationTicks; frame++) tick(selector, frame);
                    t.assertTrue(player.hits == before, "Attack hit outside reach: " + species.id);
                    player.discard(); checked++;
                }
            }
            boss.discard();
        }
        t.assertTrue(checked == 10, "Missing melee variants: " + checked);
        t.succeed();
    }


    @GameTest(template="empty")
    public static void nilShoulderBashStartsDamagesAndRecharges(GameTestHelper t) throws Exception {
        var boss = (NilEntity) DeepNight.HOSTILES.get(Species.MOBOSSES_NIL).get().create(t.getLevel());
        var player = fighter(t);
        boss.setPos(0, 180, 0); player.setPos(6, 180, 0); boss.setTarget(player);
        Goal bash = boss.goalSelector.getAvailableGoals().stream().map(WrappedGoal::getGoal)
            .filter(goal -> goal.getClass().getSimpleName().equals("ShoulderBashGoal")).findFirst().orElseThrow();
        t.assertFalse(bash.canUse(), "Bash ignored its initial cooldown");
        boss.tickCount = 20;
        t.assertTrue(bash.canUse(), "Bash cooldown never expired");
        bash.start(); player.setPos(1, 180, 0);
        for (int frame = 0; frame < 15; frame++) bash.tick();
        t.assertTrue(player.hits == 1 && player.damage > 0, "Bash did not deal exactly one survival hit");
        t.assertFalse(bash.canContinueToUse(), "Bash exceeded its duration");
        bash.stop(); player.setPos(6, 180, 0); boss.tickCount = 99;
        t.assertFalse(bash.canUse(), "Bash recharged early");
        boss.tickCount = 100;
        t.assertTrue(bash.canUse(), "Bash never recharged");
        boss.discard(); player.discard(); t.succeed();
    }
}
