package dev.deepnight.qa;
import dev.deepnight.*;
import dev.deepnight.source.*;
import dev.deepnight.source.mobosses.entity.ai.SoulsMeleeAttackGoal;
import net.minecraft.gametest.framework.*;
import net.minecraft.nbt.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.gametest.*;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.*;

@GameTestHolder(DeepNight.ID) @PrefixGameTestTemplate(false)
public final class SourceCombatTests {
 @GameTest(template="empty") public static void dragonLordPartsShareHealthAndPreserveDamageRules(GameTestHelper t)throws Exception{
  var level=t.getLevel();
  var boss=(dev.deepnight.source.dawn.entity.DragonLordEntity)DeepNight.HOSTILES.get(Species.DAWN_DRAGON_LORD).get().create(level);
  var other=DeepNight.HOSTILES.get(Species.DAWN_DRAGON_LORD).get().create(level);
  var pos=t.absoluteVec(new net.minecraft.world.phys.Vec3(0,200,0));
  boss.setPos(pos);boss.setNoAi(true);boss.setNoGravity(true);level.addFreshEntity(boss);
  other.setPos(pos.add(2,0,0));other.setNoAi(true);level.addFreshEntity(other);
  var player=net.minecraftforge.common.util.FakePlayerFactory.get(level,new com.mojang.authlib.GameProfile(UUID.randomUUID(),"PartQA"));
  var barField=boss.getClass().getDeclaredField("bossInfo");barField.setAccessible(true);
  var bar=(net.minecraft.server.level.ServerBossEvent)barField.get(boss);
  var parts=new ArrayList<LivingEntity>();
  for(int i=2;i<=7;i++){
   var type=ForgeRegistries.ENTITY_TYPES.getValue(new net.minecraft.resources.ResourceLocation("simple_mobs","dl_multi_part_"+i));
   var part=(LivingEntity)type.create(level);part.setPos(pos.add(0,8,24));level.addFreshEntity(part);parts.add(part);
   DragonLordParts.bind(part,boss);
   float before=boss.getHealth(),partHealth=part.getHealth();
   t.assertTrue(part.hurt(boss.damageSources().playerAttack(player),30),"Part "+i+" rejected melee");
   t.assertTrue(boss.getHealth()<before&&part.getHealth()==partHealth,"Part "+i+" did not share parent health");
   t.assertTrue(boss.getLastHurtByMob()==player,"Part lost player attribution");
   float hitHealth=boss.getHealth();
   t.assertFalse(part.hurt(boss.damageSources().playerAttack(player),30),"Shared cooldown accepted a duplicate hit");
   t.assertTrue(boss.getHealth()==hitHealth&&other.getHealth()==other.getMaxHealth(),"Hit damaged another dragon or counted twice");
   SourceStun.apply(boss,100);
   for(int tick=0;tick<22;tick++)boss.tick();
   t.assertFalse(boss.getPersistentData().getBoolean("hurtcd"),"Stun froze incoming-hit cooldown: part="+i+", remaining="+boss.getPersistentData().getDouble("hurt_cd")+", ticks="+boss.tickCount+", health="+boss.getHealth()+", last="+boss.getLastDamageSource());
   t.assertTrue(Math.abs(bar.getProgress()-boss.getHealth()/boss.getMaxHealth())<0.0001f,"Part hit did not update boss bar");
  }
  var part=parts.get(0);
  boss.setInvulnerable(true);
  t.assertFalse(part.hurt(boss.damageSources().generic(),30),"Part bypassed parent invulnerability");
  t.assertFalse(boss.getPersistentData().getBoolean("hurtcd"),"Rejected hit started cooldown");
  boss.setInvulnerable(false);
  var arrow=new net.minecraft.world.entity.projectile.Arrow(level,player);
  t.assertFalse(part.hurt(boss.damageSources().arrow(arrow,player),30),"Part bypassed native arrow immunity");
  t.assertTrue(part.hurt(boss.damageSources().generic(),30),"Damage without an attacker was dropped");
  boss.discard();
  t.assertFalse(part.hurt(other.damageSources().generic(),30),"Orphan hitbox reassigned to another dragon");
  parts.forEach(Entity::discard);other.discard();t.succeed();
 }
 @GameTest(template="empty") public static void dragonLordLegacyPartsAdoptOneOwner(GameTestHelper t){
  var level=t.getLevel();var pos=t.absoluteVec(new net.minecraft.world.phys.Vec3(0,200,0));
  var boss=DeepNight.HOSTILES.get(Species.DAWN_DRAGON_LORD).get().create(level);boss.setPos(pos);level.addFreshEntity(boss);
  var type=dev.deepnight.source.dawn.init.SimpleMobsModEntities.DL_MULTI_PART_2.get();
  var part=type.create(level);part.setPos(pos.add(0,8,12));level.addFreshEntity(part);
  t.assertTrue(part.hurt(boss.damageSources().generic(),30)&&boss.getHealth()<boss.getMaxHealth(),"Legacy part failed to forward damage");
  var tag=new CompoundTag();part.saveWithoutId(tag);var loaded=type.create(level);loaded.load(tag);
  t.assertTrue(DragonLordParts.owner(loaded)==boss,"Part owner did not survive save/load");
  part.discard();loaded.discard();boss.discard();t.succeed();
 }
 @GameTest(template="empty",timeoutTicks=85) public static void herobrineDelayedRewardIsARegisteredItem(GameTestHelper t){
  var pos=t.absoluteVec(new net.minecraft.world.phys.Vec3(0,10,0));
  dev.deepnight.source.dawn.procedures.ProherobrinedeathProcedure.execute(t.getLevel(),pos.x,pos.y,pos.z);
  t.runAtTickTime(65,()->{
   var drops=t.getLevel().getEntitiesOfClass(net.minecraft.world.entity.item.ItemEntity.class,new net.minecraft.world.phys.AABB(pos,pos).inflate(20),
    item->item.getItem().is(dev.deepnight.source.dawn.init.SimpleMobsModItems.ACCURSED_SWORD.get()));
   t.assertTrue(drops.size()==1&&drops.get(0).getItem().getCount()==1,"Native Herobrine reward missing or duplicated");
   drops.forEach(Entity::discard);t.succeed();
  });
 }
 @GameTest(template="empty") public static void dragonLordEarlyDiveHitReleasesInvulnerability(GameTestHelper t){
  var level=t.getLevel();var boss=DeepNight.HOSTILES.get(Species.DAWN_DRAGON_LORD).get().create(level);
  var pos=t.absoluteVec(new net.minecraft.world.phys.Vec3(0,60,0));boss.setPos(pos);boss.setYRot(0);boss.setNoGravity(true);level.addFreshEntity(boss);
  var target=EntityType.ZOMBIE.create(level);target.setPos(pos.add(0,0,12));target.setNoAi(true);level.addFreshEntity(target);boss.setTarget(target);
  var state=boss.getPersistentData();state.putBoolean("logged",true);state.putBoolean("Attacking",true);state.putBoolean("Attack14",true);state.putDouble("atk14",110);boss.setInvulnerable(true);
  dev.deepnight.source.dawn.procedures.ProDragonLordProcedure.execute(level,pos.x,pos.y,pos.z,boss);
  t.assertTrue(boss.getPersistentData().getBoolean("Attack14b"),"Dive did not enter its early collision follow-up");
  t.assertFalse(boss.isInvulnerable(),"Early dive collision retained permanent invulnerability");boss.discard();target.discard();t.succeed();
 }
 @GameTest(template="empty") public static void everyBossHasOneUpdatingHealthBarDuringStun(GameTestHelper t)throws Exception{
  var p=net.minecraftforge.common.util.FakePlayerFactory.get(t.getLevel(),new com.mojang.authlib.GameProfile(UUID.randomUUID(),"BarQA"));
  for(int i=0;i<Species.BOSS_COUNT;i++){
   var boss=DeepNight.HOSTILES.get(Species.boss(i)).get().create(t.getLevel());boss.setPos(t.absoluteVec(new net.minecraft.world.phys.Vec3(0,20,0)));
   boss.setNoGravity(true);boss.setNoAi(true);SourceStun.apply(boss,100);boss.startSeenByPlayer(p);boss.setHealth(boss.getMaxHealth()*0.5f);boss.tick();
   int visible=0;
   for(Class<?> type=boss.getClass();type!=null;type=type.getSuperclass())for(var field:type.getDeclaredFields())
    if(net.minecraft.server.level.ServerBossEvent.class.isAssignableFrom(field.getType())){
     field.setAccessible(true);var bar=(net.minecraft.server.level.ServerBossEvent)field.get(boss);
     if(bar.getPlayers().contains(p)){visible++;t.assertTrue(Math.abs(bar.getProgress()-0.5f)<0.0001f,"Frozen bar: "+boss.species.title);}
    }
   t.assertTrue(visible==1,"Expected exactly one visible health bar: "+boss.species.title+" got "+visible);
   boss.stopSeenByPlayer(p);boss.discard();
  }
  t.succeed();
 }
 @GameTest(template="empty") public static void optionalSourceAdvancementsCannotCrashVictory(GameTestHelper t){
  var p=new BossLifecycleTests.Fighter(t.getLevel(),99);
  t.assertFalse(SourceAdvancements.award(p,"deepnight:missing_qa_advancement"),"Missing advancement unexpectedly exists");
  t.assertFalse(SourceAdvancements.award(p,"invalid advancement"),"Malformed advancement accepted");
  for(String id:List.of("simple_mobs:killelementaldeity","simple_mobs:the_last_invader")){
   t.assertTrue(SourceAdvancements.award(p,id),"Source victory advancement missing: "+id);
   var adv=p.server.getAdvancements().getAdvancement(new net.minecraft.resources.ResourceLocation(id));
   t.assertTrue(p.getAdvancements().getOrStartProgress(adv).isDone(),"Source advancement did not award: "+id);
  }
  t.succeed();
 }
 @GameTest(template="empty") public static void dragonLordBarTracksDamageHealingStunAndDeathWithoutAi(GameTestHelper t)throws Exception{
  var boss=(dev.deepnight.source.dawn.entity.DragonLordEntity)DeepNight.HOSTILES.get(Species.DAWN_DRAGON_LORD).get().create(t.getLevel());
  boss.setPos(t.absoluteVec(new net.minecraft.world.phys.Vec3(0,20,0)));boss.setNoGravity(true);boss.setNoAi(true);
  boss.getPersistentData().putBoolean("logged",true);
  var field=boss.getClass().getDeclaredField("bossInfo");field.setAccessible(true);
  var bar=(net.minecraft.server.level.ServerBossEvent)field.get(boss);
  float full=boss.getHealth();
  t.assertTrue(boss.hurt(boss.damageSources().generic(),120)&&boss.getHealth()<full,"Dragon Lord did not take damage");
  boss.tick();
  t.assertTrue(bar.getProgress()<1&&Math.abs(bar.getProgress()-boss.getHealth()/boss.getMaxHealth())<0.0001f,"NoAI froze Dragon Lord's health bar");
  boss.heal(20);boss.tick();
  t.assertTrue(Math.abs(bar.getProgress()-boss.getHealth()/boss.getMaxHealth())<0.0001f,"Bar missed healing");
  SourceStun.apply(boss,100);boss.setHealth(boss.getMaxHealth()/2);boss.tick();
  t.assertTrue(SourceStun.stunned(boss)&&Math.abs(bar.getProgress()-0.5f)<0.0001f,"Stun froze Dragon Lord's health bar");
  boss.setHealth(0);boss.tick();t.assertTrue(bar.getProgress()==0,"Dead Dragon Lord retained a nonzero health bar");
  boss.discard();t.succeed();
 }
 @GameTest(template="empty") public static void dragonLordThunderSpearAttackDoesNotCrash(GameTestHelper t){
  var boss=DeepNight.HOSTILES.get(Species.DAWN_DRAGON_LORD).get().create(t.getLevel());
  var pos=t.absoluteVec(new net.minecraft.world.phys.Vec3(0,20,0));
  boss.setPos(pos);boss.setNoGravity(true);
  var data=boss.getPersistentData();
  data.putBoolean("logged",true);data.putBoolean("Attacking",true);data.putBoolean("Attack7",true);
  // The reported crash occurs when attack 7 counts down to frame 86.
  data.putDouble("atk7",87);
  dev.deepnight.source.dawn.procedures.ProDragonLordProcedure.execute(t.getLevel(),pos.x,pos.y,pos.z,boss);
  t.assertTrue(data.getDouble("atk7")==86,"Thunder spear attack frame was not reached");
  t.assertFalse(t.getLevel().getEntitiesOfClass(dev.deepnight.source.dawn.entity.RodParticlesEntity.class,
   boss.getBoundingBox().inflate(5)).isEmpty(),"Thunder spear frame did not finish spawning its effect");
  boss.discard();t.succeed();
 }
 @GameTest(template="empty",timeoutTicks=340) public static void nativeBossesRunCombatAndPhaseTicks(GameTestHelper t){
  var bosses=new ArrayList<NightMob>();var targets=new ArrayList<net.minecraft.world.entity.monster.Zombie>();var origins=new ArrayList<net.minecraft.world.phys.Vec3>();
  var chunks=new HashSet<net.minecraft.world.level.ChunkPos>();
  for(int i=0;i<Species.BOSS_COUNT;i++){
   var pos=t.absoluteVec(new net.minecraft.world.phys.Vec3((i%6)*48,20,(i/6)*48));origins.add(pos);
   var chunk=new net.minecraft.world.level.ChunkPos(net.minecraft.core.BlockPos.containing(pos));chunks.add(chunk);t.getLevel().setChunkForced(chunk.x,chunk.z,true);
   var boss=DeepNight.HOSTILES.get(Species.boss(i)).get().create(t.getLevel());boss.setPos(pos);boss.setNoGravity(true);
   boss.finalizeSpawn(t.getLevel(),t.getLevel().getCurrentDifficultyAt(boss.blockPosition()),MobSpawnType.MOB_SUMMONED,null,null);t.getLevel().addFreshEntity(boss);bosses.add(boss);
   var target=EntityType.ZOMBIE.create(t.getLevel());target.setNoAi(true);target.setNoGravity(true);target.setPos(pos.add(2,0,0));target.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1000);target.setHealth(1000);t.getLevel().addFreshEntity(target);targets.add(target);
   boss.setTarget(target);
  }
  final int[] damageFrames={0};
  for(int tick=1;tick<=300;tick++){final int frame=tick;t.runAtTickTime(tick,()->{
   for(int i=0;i<bosses.size();i++){
    var boss=bosses.get(i);var target=targets.get(i);if(target.getHealth()<1000)damageFrames[0]++;
    target.setHealth(1000);target.setRemainingFireTicks(0);target.setPos(origins.get(i).add(2,0,0));boss.setPos(origins.get(i));boss.setHealth(boss.getMaxHealth()*(frame>150?0.25f:1));boss.setTarget(target);
   }
  });}
  t.runAtTickTime(305,()->{
   t.assertTrue(damageFrames[0]>0,"Source combat never damaged a target");
   for(var boss:bosses){t.assertTrue(boss.tickCount>200,"Source boss stopped ticking: "+boss.getType());boss.discard();}
   targets.forEach(Entity::discard);for(var chunk:chunks)t.getLevel().setChunkForced(chunk.x,chunk.z,false);
   t.succeed();
  });
 }
 @GameTest(template="empty") public static void nativeRosterAndAuxiliaryEntitiesConstruct(GameTestHelper t){
  t.assertTrue(Species.BOSS_COUNT==10,"Expected exactly ten campaign bosses");
  for(Species s:Species.values()){
   t.assertFalse(s.id.startsWith("aod_"),"Removed content registered");
   if(!s.boss())continue;
   var mob=DeepNight.HOSTILES.get(s).get().create(t.getLevel());
   t.assertTrue(mob.sourceCombat()&&mob.getClass().getName().startsWith("dev.deepnight.source."),"Generic boss factory: "+s);
   t.assertTrue(mob.getBbWidth()==s.width&&mob.getBbHeight()==s.height,"Source collision dimensions changed: "+s);
   mob.finalizeSpawn(t.getLevel(),t.getLevel().getCurrentDifficultyAt(t.absolutePos(net.minecraft.core.BlockPos.ZERO)),MobSpawnType.MOB_SUMMONED,null,null);
   mob.tick();mob.discard();
  }
  int helpers=0;
  for(var entry:ForgeRegistries.ENTITY_TYPES.getEntries())if(entry.getKey().location().getNamespace().equals("simple_mobs")||entry.getKey().location().getPath().endsWith("_aura")||entry.getKey().location().getPath().endsWith("_lightning")){
   var entity=entry.getValue().create(t.getLevel());t.assertTrue(entity!=null,"Cannot construct helper "+entry.getKey());entity.tick();entity.discard();helpers++;
  }
  t.assertTrue(helpers>100,"Source projectiles and hitbox entities absent");t.succeed();
 }
 @GameTest(template="empty") public static void sourceMeleeUsesOriginalContactAndReach(GameTestHelper t){
  var boss=DeepNight.HOSTILES.get(Species.MOBOSSES_ROYAL_GUARDIAN).get().create(t.getLevel());
  var target=EntityType.ZOMBIE.create(t.getLevel());target.getAttribute(Attributes.MAX_HEALTH).setBaseValue(200);target.setHealth(200);
  boss.setPos(2,100,2);target.setPos(3,100,2);boss.setTarget(target);
  var goal=new SoulsMeleeAttackGoal(boss,1.3,"attack_controller",()->true,3,new SoulsMeleeAttackGoal.AttackDef("skill2",90,new int[]{54},2));
  goal.tick();for(int i=0;i<53;i++)goal.tick();t.assertTrue(target.getHealth()==200,"Damage occurred before source contact tick 54");
  goal.tick();t.assertTrue(target.getHealth()<200,"Source contact did not deal damage");
  float health=target.getHealth();goal.cancel();target.invulnerableTime=0;goal.tick();target.setPos(50,100,2);for(int i=0;i<54;i++)goal.tick();
  t.assertTrue(target.getHealth()==health,"Source hitbox struck an out-of-reach target");t.succeed();
 }
 @GameTest(template="empty") public static void substituteStunStopsAndRestoresActions(GameTestHelper t){
  var attacker=EntityType.ZOMBIE.create(t.getLevel());var victim=EntityType.ZOMBIE.create(t.getLevel());
  victim.getAttribute(Attributes.MAX_HEALTH).setBaseValue(200);victim.setHealth(200);
  var damage=SourceDamage.mob(attacker).setStunType(SourceDamage.StunType.LONG).setImpact(15).setArmorNegation(20);
  t.assertTrue(victim.hurt(damage,10),"Source damage rejected");t.assertTrue(SourceStun.stunned(victim),"Source hit did not apply local stun");
  SourceStun.tick(new net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent(victim));
  t.assertTrue(victim.isNoAi(),"Stun did not pause mob AI");
  t.assertFalse(attacker.hurt(victim.damageSources().mobAttack(victim),4),"Stunned attacker can deal melee damage");
  victim.removeEffect(SourceStun.STUN.get());SourceStun.tick(new net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent(victim));
  t.assertFalse(victim.isNoAi(),"Stun permanently disabled AI");
  victim.setNoAi(true);SourceStun.apply(victim,2);SourceStun.tick(new net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent(victim));victim.removeEffect(SourceStun.STUN.get());SourceStun.tick(new net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent(victim));
  t.assertTrue(victim.isNoAi(),"Stun overwrote a pre-existing NoAI state");t.succeed();
 }
 @GameTest(template="empty") public static void removedEncountersMigrateAndCompletedBossesStayCredited(GameTestHelper t){
  UUID player=UUID.randomUUID();CompoundTag root=new CompoundTag(),row=new CompoundTag();root.putInt("RosterVersion",2);row.putUUID("Id",player);row.putBoolean("Unlocked",true);row.putInt("Stage",45);ListTag rows=new ListTag();rows.add(row);root.put("Players",rows);root.putUUID("Active",UUID.randomUUID());root.putUUID("Summoner",player);root.putInt("ActiveStage",20);
  CampaignData data=CampaignData.load(root);t.assertTrue(data.active==null&&data.player(player).stage==0,"Removed encounter blocked DawnCraft entry");
  t.assertTrue(data.player(player).defeated.contains("mobosses_demon_king"),"Existing MoBosses victories were lost");
  while(data.player(player).stage<Species.BOSS_COUNT){int stage=data.player(player).stage;UUID id=UUID.randomUUID();t.assertTrue(data.begin(player,id,"minecraft:overworld",0),"Migration locked campaign");t.assertTrue(data.credit(player,id,stage),"Cannot credit reordered boss");data.clear(id);}
  var loaded=CampaignData.load(data.save(new CompoundTag()));t.assertTrue(loaded.player(player).stage==Species.BOSS_COUNT,"Defeated IDs were not persisted");t.succeed();
 }
}
