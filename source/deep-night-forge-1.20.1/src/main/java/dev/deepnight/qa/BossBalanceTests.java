package dev.deepnight.qa;

import dev.deepnight.*;
import net.minecraft.gametest.framework.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.gametest.GameTestHolder;

@GameTestHolder("deepnight")
@net.minecraftforge.gametest.PrefixGameTestTemplate(false)
public final class BossBalanceTests {
    @GameTest(template="empty")
    public static void rankedHealthAppliesToSpawnsAndOldSaves(GameTestHelper t) {
        double previous=0;
        for(int i=0;i<Species.BOSS_COUNT;i++) {
            Species s=Species.boss(i);
            var boss=DeepNight.HOSTILES.get(s).get().create(t.getLevel());
            t.assertTrue(boss.getMaxHealth()==s.health && boss.getHealth()==s.health,"Health capped or spawn not full: "+s);
            t.assertTrue(s.health>previous,"Health does not rise with progression: "+s);
            previous=s.health;
            boss.getAttribute(Attributes.MAX_HEALTH).setBaseValue(400);
            boss.setHealth(100);
            CompoundTag saved=new CompoundTag();boss.saveWithoutId(saved);
            var restored=DeepNight.HOSTILES.get(s).get().create(t.getLevel());restored.load(saved);
            t.assertTrue(restored.getMaxHealth()==s.health && Math.abs(restored.getHealth()-s.health/4)<0.01,"Old save lost health fraction: "+s);
            boss.discard();restored.discard();
        }
        t.assertTrue(Species.DAWN_DRAGON_LORD.bossStage()==Species.DAWN_SKELETONLORD.bossStage()+1,"Dragon Lord order");
        Species[] finale={Species.MOBOSSES_DEMON_KING,Species.MOBOSSES_HEROBRINE,Species.DAWN_NOTCH_BOSS,Species.DAWN_HEROBRINE};
        for(int i=0;i<4;i++)t.assertTrue(Species.boss(Species.BOSS_COUNT-4+i)==finale[i],"Finale order");
        t.succeed();
    }

    @GameTest(template="empty")
    public static void meleeAndMagicScaleOnceAndOrdinaryMobsDoNot(GameTestHelper t) {
        var victim=EntityType.ZOMBIE.create(t.getLevel());
        var boss=DeepNight.HOSTILES.get(Species.MOBOSSES_ROYAL_GUARDIAN).get().create(t.getLevel());
        var aura=new dev.deepnight.source.mobosses.entity.custom.ForestGuardianAuraEntity(
            dev.deepnight.source.mobosses.MoBosses.FOREST_GUARDIAN_AURA.get(),t.getLevel());
        DamageSource[] sources={boss.damageSources().mobAttack(boss),boss.damageSources().indirectMagic(boss,boss),aura.damageSources().indirectMagic(aura,aura)};
        for(var source:sources){
            var event=new LivingHurtEvent(victim,source,10);BossBalance.damage(event);
            t.assertTrue(event.getAmount()==30,"Boss damage was not tripled exactly once");
            victim.getAttribute(Attributes.MAX_HEALTH).setBaseValue(100);
            victim.getAttribute(Attributes.ARMOR).setBaseValue(0);
            victim.setHealth(100);victim.invulnerableTime=0;
            t.assertTrue(victim.hurt(source,10) && victim.getHealth()==70,"Actual health loss was not tripled");
        }
        var event=new LivingHurtEvent(victim,victim.damageSources().mobAttack(victim),10);BossBalance.damage(event);
        t.assertTrue(event.getAmount()==10,"Ordinary mob damage changed");
        victim.discard();boss.discard();aura.discard();t.succeed();
    }

    @GameTest(template="empty")
    public static void oldCompletedIdsSurviveReordering(GameTestHelper t) {
        var owner=java.util.UUID.randomUUID();
        CampaignData data=new CampaignData();data.unlock(owner);
        // The first two bosses in 1.3.3 were Ogre and Dragon Lord.
        data.player(owner).defeated.add(Species.DAWN_CORRUPTED_OGRE.id);
        data.player(owner).defeated.add(Species.DAWN_DRAGON_LORD.id);
        data=CampaignData.load(data.save(new CompoundTag()));
        t.assertTrue(Species.boss(data.player(owner).stage)==Species.MOBOSSES_NIL,"Reorder skipped unbeaten boss");
        while(data.player(owner).stage<Species.DAWN_DRAGON_LORD.bossStage()) {
            int stage=data.player(owner).stage;var boss=java.util.UUID.randomUUID();
            t.assertTrue(data.begin(owner,boss,"minecraft:overworld",0),"Cannot continue migrated campaign");
            t.assertTrue(data.credit(owner,boss,stage),"Cannot credit migrated campaign");data.clear(boss);
        }
        t.assertTrue(Species.boss(data.player(owner).stage)==Species.MOBOSSES_DEMON_KING,"Previously beaten Dragon Lord was not skipped");
        t.succeed();
    }
}
