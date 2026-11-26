package dev.deepnight.qa;

import com.mojang.authlib.GameProfile;
import dev.deepnight.*;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.gametest.*;
import java.util.*;

@GameTestHolder(DeepNight.ID) @PrefixGameTestTemplate(false)
public final class BossMenuTests {
    @GameTest(template="empty",batch="boss_menu") public static void namedAndModTaggedToolsAreRecognizedInBothHands(GameTestHelper t){
        var p=player(t,GameType.CREATIVE);
        var tool=p.getMainHandItem();
        tool.getOrCreateTag();
        t.assertFalse(p.getInventory().contains(new ItemStack(DeepNight.CALLER.get())),"Empty-tag fixture did not reproduce Fantasy Realms failure");
        t.assertTrue(CampaignEvents.hasCaller(p),"Empty-tag tool was rejected");
        tool.setHoverName(net.minecraft.network.chat.Component.literal("My Boss Tool"));
        tool.getOrCreateTag().putString("mod_added_data","regression");
        t.assertFalse(p.getInventory().contains(new ItemStack(DeepNight.CALLER.get())),"Fixture does not reproduce the old NBT-sensitive check");
        t.assertTrue(CampaignEvents.hasCaller(p),"Tagged tool was rejected");
        t.assertTrue(BossNetwork.apply(p,new BossNetwork.Request(BossNetwork.Action.REFRESH,0)).success(),"Tagged tool cannot use menu actions");
        p.setItemInHand(InteractionHand.MAIN_HAND,ItemStack.EMPTY);p.setItemInHand(InteractionHand.OFF_HAND,tool);
        t.assertTrue(CampaignEvents.hasCaller(p),"Offhand tool was rejected");
        p.setItemInHand(InteractionHand.OFF_HAND,ItemStack.EMPTY);
        var fake=new ItemStack(net.minecraft.world.item.Items.STICK);fake.setHoverName(tool.getHoverName());p.setItemInHand(InteractionHand.MAIN_HAND,fake);
        t.assertFalse(CampaignEvents.hasCaller(p),"Renamed unrelated item accepted as a tool");
        p.discard();t.succeed();
    }
    private static ServerPlayer player(GameTestHelper t,GameType mode){
        var p=FakePlayerFactory.get(t.getLevel(),new GameProfile(UUID.randomUUID(),"BossMenuQA"));
        p.setGameMode(mode);p.setPos(t.absoluteVec(new net.minecraft.world.phys.Vec3(3,2,3)));
        p.setItemInHand(InteractionHand.MAIN_HAND,new ItemStack(DeepNight.CALLER.get()));
        t.getLevel().addNewPlayer(p);return p;
    }
    private static NightMob encounter(GameTestHelper t,ServerPlayer p,int stage){
        var data=CampaignData.get(p.server);var boss=DeepNight.HOSTILES.get(Species.boss(stage)).get().create(t.getLevel());
        boss.setPos(p.position().add(2,0,0));boss.setNoAi(true);boss.bind(p);
        t.assertTrue(data.beginSelected(p.getUUID(),boss.getUUID(),stage,p.isCreative(),t.getLevel().dimension().location().toString(),boss.blockPosition().asLong()),"Cannot create test encounter");
        t.getLevel().addFreshEntity(boss);return boss;
    }
    @GameTest(template="empty",batch="boss_menu") public static void creativeDamageAndCommandKillsAdvanceAndKeepEncounterAlive(GameTestHelper t){
        var p=player(t,GameType.CREATIVE);SummonTool.prepareCreative(p);var d=CampaignData.get(p.server);
        var boss=encounter(t,p,0);CompoundTag saved=new CompoundTag();boss.saveWithoutId(saved);saved.putInt("Deserted",599);boss.load(saved);
        boss.tick();t.assertFalse(boss.isRemoved(),"Creative player was treated as an empty arena");
        CompoundTag after=new CompoundTag();boss.saveWithoutId(after);t.assertTrue(after.getInt("Deserted")==0,"Creative presence did not reset the retreat timer");
        t.assertTrue(boss.hurt(boss.damageSources().playerAttack(p),100000),"Creative damage was rejected");
        t.assertTrue(d.player(p.getUUID()).stage==1&&d.active==null,"Creative kill remained stuck on Corrupted Ogre");boss.discard();
        var next=encounter(t,p,1);next.kill();
        t.assertTrue(d.player(p.getUUID()).stage==2&&d.active==null,"Creative /kill did not advance the next encounter");next.discard();p.discard();t.succeed();
    }
    @GameTest(template="empty",batch="boss_menu") public static void rematchesPersistAndNeverRepeatProgressOrFirstClearRewards(GameTestHelper t){
        var p=player(t,GameType.SURVIVAL);var d=CampaignData.get(p.server);d.unlock(p.getUUID());d.player(p.getUUID()).toolPending=false;
        var first=encounter(t,p,0);first.hurt(first.damageSources().playerAttack(p),100000);first.discard();
        int sigils=p.getInventory().countItem(DeepNight.SIGILS.get(Species.boss(0)).get());
        t.assertTrue(sigils==4&&d.player(p.getUUID()).stage==1,"First-clear rewards missing");
        var rematch=encounter(t,p,0);
        var loaded=CampaignData.load(d.save(new CompoundTag()));
        t.assertTrue(loaded.active.equals(rematch.getUUID())&&loaded.activeMode==CampaignData.EncounterMode.REMATCH,"Rematch was lost on reload");
        rematch.hurt(rematch.damageSources().playerAttack(p),100000);
        t.assertTrue(d.player(p.getUUID()).stage==1&&p.getInventory().countItem(DeepNight.SIGILS.get(Species.boss(0)).get())==sigils,"Rematch duplicated progress/rewards");
        rematch.discard();
        var progress=d.player(p.getUUID());progress.stage=Species.BOSS_COUNT;
        for(int i=0;i<Species.BOSS_COUNT;i++)progress.defeated.add(Species.boss(i).id);
        t.assertTrue(d.canSummon(p.getUUID(),0,false)&&!d.canStart(p.getUUID()),"Completed campaign cannot rematch");
        p.discard();t.succeed();
    }
    @GameTest(template="empty",batch="boss_menu") public static void selectionPacketsCannotBypassUnlockToolOrOrder(GameTestHelper t){
        var p=player(t,GameType.SURVIVAL);var d=CampaignData.get(p.server);
        t.assertFalse(BossNetwork.apply(p,new BossNetwork.Request(BossNetwork.Action.SUMMON,0)).success(),"Survival dragon unlock bypassed");
        d.unlock(p.getUUID());
        for(int stage:new int[]{-1,1,Species.BOSS_COUNT,Integer.MAX_VALUE})
            t.assertFalse(BossNetwork.apply(p,new BossNetwork.Request(BossNetwork.Action.SUMMON,stage)).success(),"Forged selection accepted: "+stage);
        p.setItemInHand(InteractionHand.MAIN_HAND,ItemStack.EMPTY);
        t.assertFalse(BossNetwork.apply(p,new BossNetwork.Request(BossNetwork.Action.SUMMON,0)).success(),"Summoned without tool");
        p.setItemInHand(InteractionHand.MAIN_HAND,new ItemStack(DeepNight.CALLER.get()));p.setGameMode(GameType.SPECTATOR);
        t.assertFalse(BossNetwork.apply(p,new BossNetwork.Request(BossNetwork.Action.SUMMON,0)).success(),"Spectator summoned a boss");
        t.assertTrue(d.active==null&&d.player(p.getUUID()).stage==0,"Rejected request changed campaign");p.discard();t.succeed();
    }
    @GameTest(template="empty",batch="boss_menu") public static void creativeFutureSummonsAndOwnerControlsUseRealServerActions(GameTestHelper t){
        var p=player(t,GameType.CREATIVE);var other=player(t,GameType.SURVIVAL);var d=CampaignData.get(p.server);
        for(int x=-25;x<=30;x++)for(int z=-25;z<=30;z++)t.setBlock(new BlockPos(x,1,z),Blocks.STONE);
        var result=BossNetwork.apply(p,new BossNetwork.Request(BossNetwork.Action.SUMMON,Species.BOSS_COUNT-1));
        t.assertTrue(result.success(),"Creative future summon failed: "+result.message());
        t.assertTrue(d.activeStage==Species.BOSS_COUNT-1&&d.activeMode==CampaignData.EncounterMode.TEST,"Future summon became campaign progression");
        var saved=CampaignData.load(d.save(new CompoundTag()));t.assertTrue(saved.activeMode==CampaignData.EncounterMode.TEST&&saved.active!=null,"Practice encounter did not persist");
        t.assertFalse(BossNetwork.apply(other,new BossNetwork.Request(BossNetwork.Action.ABANDON,0)).success(),"Stranger ended another player's encounter");
        var original=d.active;
        t.assertFalse(BossNetwork.apply(p,new BossNetwork.Request(BossNetwork.Action.SUMMON,0)).success(),"Summon cooldown ignored");
        t.assertTrue(original.equals(d.active),"Cooldown rejection destroyed the existing fight");
        var boss=t.getLevel().getEntity(d.active);boss.kill();
        t.assertTrue(d.active==null&&d.player(p.getUUID()).stage==0,"Creative practice skipped the campaign");boss.discard();
        p.getCooldowns().removeCooldown(DeepNight.CALLER.get());
        var next=encounter(t,p,0);t.assertTrue(BossNetwork.apply(p,new BossNetwork.Request(BossNetwork.Action.ABANDON,0)).success(),"Summoner cannot end encounter");
        t.assertTrue(next.isRemoved()&&d.active==null&&d.player(p.getUUID()).stage==0,"Ending an encounter granted progression");
        p.discard();other.discard();t.succeed();
    }
    @GameTest(template="empty",batch="boss_menu") public static void menuSnapshotRoundTripsProgressAndPermissionState(GameTestHelper t){
        var p=player(t,GameType.CREATIVE);SummonTool.prepareCreative(p);var d=CampaignData.get(p.server);
        d.player(p.getUUID()).defeated.add(Species.boss(0).id);d.player(p.getUUID()).stage=1;
        var boss=encounter(t,p,0);var expected=new BossNetwork.Menu(BossNetwork.snapshot(p,"Test feedback"),true,false);
        FriendlyByteBuf buffer=new FriendlyByteBuf(Unpooled.buffer());
        try {BossNetwork.encode(expected,buffer);var decoded=BossNetwork.decode(buffer);t.assertTrue(decoded.equals(expected),"Menu packet lost state");
            t.assertTrue(decoded.state().canAbandon()&&decoded.state().canSummon(1)&&decoded.state().completed(Species.boss(0)),"Menu exposes wrong creative replacement/rematch permissions");}
        finally{buffer.release();}
        CampaignEvents.abandon(p);boss.discard();p.discard();t.succeed();
    }
    @GameTest(template="empty",batch="boss_menu") public static void creativeCanReplaceWithEveryBossWithoutCreditAndSurvivalCannot(GameTestHelper t){
        var p=player(t,GameType.CREATIVE);var owner=player(t,GameType.SURVIVAL);var d=CampaignData.get(p.server);
        d.unlock(owner.getUUID());var previous=encounter(t,owner,0);
        for(int x=-30;x<=35;x++)for(int z=-30;z<=35;z++)t.setBlock(new BlockPos(x,1,z),Blocks.STONE);
        // Replacement must also recover a fight left active by a different player.
        for(int stage=Species.BOSS_COUNT-1;stage>=0;stage--){
            p.getCooldowns().removeCooldown(DeepNight.CALLER.get());
            t.assertTrue(BossNetwork.snapshot(p,"").canSummon(stage),"Creative menu locked boss "+stage);
            var result=BossNetwork.apply(p,new BossNetwork.Request(BossNetwork.Action.SUMMON,stage));
            t.assertTrue(result.success(),"Creative replacement failed for "+Species.boss(stage).title+": "+result.message());
            t.assertTrue(previous.isRemoved()&&d.activeStage==stage&&!d.active.equals(previous.getUUID()),"Previous boss survived replacement");
            t.assertTrue(d.player(p.getUUID()).stage==0&&d.player(owner.getUUID()).stage==0,"Replacement awarded defeat credit");
            previous=(NightMob)t.getLevel().getEntity(d.active);
        }
        t.assertTrue(p.getInventory().countItem(DeepNight.SIGILS.get(Species.boss(0)).get())==0,"Replacement granted first-clear rewards");
        var active=d.active;p.getCooldowns().removeCooldown(DeepNight.CALLER.get());
        t.assertFalse(BossNetwork.apply(p,new BossNetwork.Request(BossNetwork.Action.SUMMON,-1)).success(),"Invalid replacement accepted");
        t.assertTrue(active.equals(d.active)&&!previous.isRemoved(),"Invalid selection removed existing boss");
        p.setPos(p.position().add(0,50,0));
        t.assertFalse(BossNetwork.apply(p,new BossNetwork.Request(BossNetwork.Action.SUMMON,Species.BOSS_COUNT-1)).success(),"Airborne arena incorrectly accepted");
        t.assertTrue(active.equals(d.active)&&!previous.isRemoved(),"Failed arena check removed existing boss");
        p.setGameMode(GameType.SURVIVAL);
        t.assertFalse(BossNetwork.snapshot(p,"").canSummon(0),"Survival menu allows replacement");
        t.assertFalse(BossNetwork.apply(p,new BossNetwork.Request(BossNetwork.Action.SUMMON,0)).success(),"Survival replaced an active boss");
        CampaignEvents.abandon(p);p.discard();owner.discard();t.succeed();
    }
}
