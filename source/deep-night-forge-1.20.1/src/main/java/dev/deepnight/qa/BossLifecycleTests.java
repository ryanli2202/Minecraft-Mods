package dev.deepnight.qa;

import com.mojang.authlib.GameProfile;
import dev.deepnight.*;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.gametest.*;
import java.nio.file.*;
import java.util.*;

/** Isolated arenas exercise every native boss, then wait through delayed death callbacks. */
@GameTestHolder(DeepNight.ID) @PrefixGameTestTemplate(false)
@net.minecraftforge.fml.common.Mod.EventBusSubscriber(modid=DeepNight.ID)
public final class BossLifecycleTests {
    private static final Map<String,Object> results=new TreeMap<>();
    private static final Map<UUID,Probe> probes=new HashMap<>();
    @net.minecraftforge.eventbus.api.SubscribeEvent public static void death(net.minecraftforge.event.entity.living.LivingDeathEvent event){
        var probe=probes.get(event.getEntity().getUUID());
        if(probe!=null)probe.deathDetails=Map.of("source",event.getSource().getMsgId(),"attacker",String.valueOf(event.getSource().getEntity()),"participants",probe.boss.participants().toString(),
            "player_alive",probe.player.isAlive(),"distance",probe.player.distanceTo(probe.boss),"stage_after_credit",CampaignData.get(probe.level.getServer()).player(probe.player.getUUID()).stage);
    }
    private static void report(Species boss,Map<String,Object> result){
        results.put(boss.id,result);
        try{Files.createDirectories(Path.of("../verification"));Files.writeString(Path.of("../verification/boss-lifecycle-report.json"),
            new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(results));}
        catch(java.io.IOException ex){throw new IllegalStateException(ex);}
    }
    static final class Fighter extends net.minecraft.server.level.ServerPlayer {
        int hits;float damage;
        final Map<String,Integer> damageTypes=new TreeMap<>();
        Fighter(ServerLevel level,int stage){
            super(level.getServer(),level,new GameProfile(UUID.randomUUID(),"BossQA"+stage));
            // Real ServerPlayer semantics (Forge deliberately refuses advancements for FakePlayer).
            connection=new FakePlayer(level,getGameProfile()).connection;
        }
        @Override public boolean isInvulnerableTo(DamageSource source){return false;}
        @Override public boolean hurt(DamageSource source,float amount){
            float before=getHealth();boolean accepted=super.hurt(source,amount);
            if(accepted&&getHealth()<before){
                damageTypes.merge(source.getMsgId(),1,Integer::sum);
                if((source.getEntity()!=null&&!(source.getEntity() instanceof net.minecraft.world.entity.player.Player))
                    ||source.is(net.minecraft.world.damagesource.DamageTypes.LIGHTNING_BOLT)){
                    hits++;damage+=before-getHealth();
                }
            }
            setHealth(getMaxHealth());return accepted;
        }
        @Override public void tick(){super.tick();doTick();}
    }
    private static void run(GameTestHelper t,int stage){new Probe(t,stage).start();}
    private static final class Probe {
        final GameTestHelper t;final int stage;final Species species;final ServerLevel level;
        final Set<net.minecraft.world.level.ChunkPos> chunks=new HashSet<>();
        NightMob boss;Fighter player;Vec3 origin,deathLocation;boolean done;int acceptedHits,deathAt=-1,banishments;
        final Set<String> attackStates=new TreeSet<>();
        Map<String,Object> deathDetails=Map.of();
        Probe(GameTestHelper t,int stage){this.t=t;this.stage=stage;species=Species.boss(stage);level=t.getLevel();}
        void start(){
            level.getServer().setDifficulty(net.minecraft.world.Difficulty.NORMAL,true);
            level.setWeatherParameters(100000,0,false,false);
            level.getGameRules().getRule(net.minecraft.world.level.GameRules.RULE_DOMOBSPAWNING).set(false,level.getServer());
            level.getGameRules().getRule(net.minecraft.world.level.GameRules.RULE_DAYLIGHT).set(false,level.getServer());level.setDayTime(6000);
            report(species,Map.of("status","RUNNING","boss",species.title));
            // Source projectiles can travel hundreds of blocks; separate parallel fights accordingly.
            origin=t.absoluteVec(new Vec3(1000+(stage%6)*4096,4,1000+(stage/6)*4096));
            // The default GameTest world has terrain above the test origin; fight above that terrain.
            origin=new Vec3(origin.x,180,origin.z);
            BlockPos center=BlockPos.containing(origin);var chunk=new net.minecraft.world.level.ChunkPos(center);
            for(int x=-2;x<=2;x++)for(int z=-2;z<=2;z++){var c=new net.minecraft.world.level.ChunkPos(chunk.x+x,chunk.z+z);chunks.add(c);level.setChunkForced(c.x,c.z,true);}
            for(var entity:level.getEntitiesOfClass(Entity.class,new net.minecraft.world.phys.AABB(origin,origin).inflate(128)))if(!(entity instanceof net.minecraft.world.entity.player.Player))entity.discard();
            for(int x=-22;x<=22;x++)for(int z=-22;z<=22;z++)level.setBlock(center.offset(x,-1,z),Blocks.STONE.defaultBlockState(),2);
            player=new Fighter(level,stage);player.setGameMode(GameType.SURVIVAL);player.setNoGravity(true);player.setPos(origin.add(3,0,0));
            player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1024);player.setHealth(1024);
            try{var grace=net.minecraft.server.level.ServerPlayer.class.getDeclaredField("spawnInvulnerableTime");grace.setAccessible(true);grace.setInt(player,0);}
            catch(ReflectiveOperationException ex){throw new IllegalStateException(ex);}
            player.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(Items.NETHERITE_AXE));level.addNewPlayer(player);
            t.assertTrue(player.hurt(player.damageSources().generic(),1),"QA player cannot receive damage");player.hits=0;player.damage=0;
            var data=CampaignData.get(level.getServer());data.unlock(player.getUUID());var progress=data.player(player.getUUID());progress.toolPending=false;
            for(int i=0;i<stage;i++)progress.defeated.add(Species.boss(i).id);progress.stage=stage;
            boss=DeepNight.HOSTILES.get(species).get().create(level);boss.setPos(origin);
            boss.finalizeSpawn(level,level.getCurrentDifficultyAt(center),MobSpawnType.MOB_SUMMONED,null,null);level.addFreshEntity(boss);
            boss.startSeenByPlayer(player);boss.setTarget(player);
            probes.put(boss.getUUID(),this);
            System.out.println("BOSS AUDIT START "+species.id);
            for(int frame=1;frame<=2150;frame++){final int tick=frame;t.runAtTickTime(frame,()->step(tick));}
        }
        void step(int frame){
            if(done)return;
            try{
                t.assertTrue(Float.isFinite(boss.getHealth())&&boss.getMaxHealth()>0,"Invalid health");
                if(player.serverLevel()!=level){
                    // Elemental Deity's original grab banishes players to the Nether. Return the
                    // controlled target so the remainder of this fight and its victory can be audited.
                    banishments++;player.teleportTo(level,boss.getX()+3,boss.getY(),boss.getZ(),0,0);
                }
                // Flying bosses and their corpses need ticking chunks without a connected client ticket.
                var current=new net.minecraft.world.level.ChunkPos(boss.blockPosition());
                for(int x=-1;x<=1;x++)for(int z=-1;z<=1;z++){
                    var chunk=new net.minecraft.world.level.ChunkPos(current.x+x,current.z+z);
                    if(chunks.add(chunk))level.setChunkForced(chunk.x,chunk.z,true);
                }
                player.invulnerableTime=0;player.setHealth(player.getMaxHealth());
                if(deathAt>=0){
                    // Native victory callbacks use the location captured at death, not the moving corpse.
                    player.setPos(deathLocation.add(3,0,0));
                    if(frame-deathAt>=320){
                        t.assertTrue(boss.isRemoved(),"Corpse never completed its death animation");
                        t.assertTrue(player.hits>0,"Boss never damaged its survival target");
                        t.assertTrue(acceptedHits>0,"Boss never accepted player damage");
                        t.assertTrue(boss.tickCount>=1200,"Boss left the ticking arena before combat completed");
                        var d=CampaignData.get(level.getServer());
                        t.assertTrue(d.player(player.getUUID()).stage==stage+1,"Defeat did not unlock the next boss");
                        t.assertTrue(player.getInventory().countItem(DeepNight.SIGILS.get(species).get())==4,"First-clear sigils missing or duplicated");
                        if(species==Species.DAWN_NOTCH_BOSS){
                            var summons=level.getEntitiesOfClass(dev.deepnight.source.dawn.entity.ElementalDeityEntity.class,
                                new net.minecraft.world.phys.AABB(deathLocation,deathLocation).inflate(100));
                            t.assertTrue(summons.size()==1&&summons.get(0).isAlive(),"Architect did not summon its Elemental Deity follow-up");
                            t.assertTrue(summons.get(0).getMaxHealth()==1350,"Summoned Deity lost its health tier");
                            t.assertTrue(Species.DAWN_ELEMENTAL_DEITY.bossStage()==-1&&!DeepNight.SIGILS.containsKey(Species.DAWN_ELEMENTAL_DEITY),"Deity became a standalone campaign boss");
                            summons.forEach(net.minecraft.world.entity.Entity::discard);
                        }
                        String advancement=switch(species){case DAWN_ELEMENTAL_DEITY->"simple_mobs:killelementaldeity";case DAWN_HEROBRINE->"simple_mobs:the_last_invader";default->null;};
                        if(advancement!=null){var adv=level.getServer().getAdvancements().getAdvancement(new net.minecraft.resources.ResourceLocation(advancement));
                            t.assertTrue(adv!=null&&player.getAdvancements().getOrStartProgress(adv).isDone(),"Delayed source advancement was not awarded");}
                        finish("PASS","");t.succeed();
                    }
                    return;
                }
                t.assertFalse(boss.isRemoved(),"Live boss unexpectedly disappeared");
                t.assertTrue(boss.isAlive(),"Boss died outside the controlled player attack: "+deathDetails);
                if(boss.position().distanceToSqr(origin)>55*55||boss.getY()<origin.y-6){boss.teleportTo(origin.x,origin.y,origin.z);boss.setDeltaMovement(Vec3.ZERO);}
                double reach=frame<1200&&(frame/200)%2==0?12:1.5;
                // Alternate sides and ranges while preserving the boss's AI, attacks and movement.
                double angle=(frame/80)*Math.PI/2;player.setPos(boss.position().add(Math.cos(angle)*reach,0,Math.sin(angle)*reach));boss.setTarget(player);
                if(frame<1200){
                    // Hold each health phase long enough to exercise its attacks without ending the fight early.
                    boss.setHealth(boss.getMaxHealth()*(frame<400?1:frame<800?0.55f:0.2f));
                    for(String key:boss.getPersistentData().getAllKeys())if(key.toLowerCase(Locale.ROOT).matches("(attack|atk|phase|spawn).*")){
                        var value=boss.getPersistentData().get(key);if(!value.toString().equals("0b")&&!value.toString().equals("0.0d"))attackStates.add(key);
                    }
                }
                if(frame%25==0&&frame>=100){
                    // This controlled swing probes boss damage reception independently of player stun duration.
                    player.removeAllEffects();
                    var d=CampaignData.get(level.getServer());
                    if(d.active!=null)d.clear(d.active);
                    t.assertTrue(d.beginSelected(player.getUUID(),boss.getUUID(),stage,false,level.dimension().location().toString(),boss.blockPosition().asLong()),"Cannot track encounter");
                    float before=boss.getHealth();
                    boolean hit=boss.hurt(boss.damageSources().playerAttack(player),frame<1200?Math.min(8,boss.getMaxHealth()*0.015f):Math.max(64,boss.getMaxHealth()*0.08f));
                    if(hit&&boss.getHealth()<before)acceptedHits++;
                    if(!boss.isAlive()){deathAt=frame;deathLocation=boss.position();}
                    d.clear(boss.getUUID());
                }
                t.assertTrue(frame<1800||deathAt>=0,"Boss could not be defeated by repeated player melee damage");
            }catch(Throwable ex){finish("FAIL",ex.toString());t.fail(species.title+": "+ex.getMessage());}
        }
        void finish(String status,String error){
            done=true;
            var row=new LinkedHashMap<String,Object>();row.put("boss",species.title);row.put("status",status);row.put("player_damage_hits",acceptedHits);
            row.put("boss_damage_hits",player.hits);row.put("boss_damage",player.damage);row.put("received_damage_types",player.damageTypes);row.put("defeated_at_tick",deathAt);row.put("post_death_ticks",320);row.put("source_attack_states",attackStates);if(!error.isEmpty())row.put("error",error);
            row.put("death",deathDetails);probes.remove(boss.getUUID());
            row.put("native_entity_ticks",boss.tickCount);
            row.put("native_dimension_banishments",banishments);
            if(!error.isEmpty()){row.put("remaining_health",boss.getHealth());row.put("invulnerable",boss.isInvulnerable());row.put("effects",boss.getActiveEffects().toString());row.put("source_state",boss.getPersistentData().toString());}
            report(species,row);System.out.println("BOSS AUDIT "+status+" "+species.id+" "+row);
            boss.stopSeenByPlayer(player);boss.discard();player.discard();
            for(var entity:level.getEntitiesOfClass(Entity.class,new net.minecraft.world.phys.AABB(origin,origin).inflate(110)))if(!(entity instanceof net.minecraft.world.entity.player.Player))entity.discard();
            for(var chunk:chunks)level.setChunkForced(chunk.x,chunk.z,false);
        }
    }
    // Per-boss entries are generated below so every failure is reported independently.
@GameTest(template="empty",batch="boss_lifecycle",timeoutTicks=2200) public static void dawn_corrupted_ogre(GameTestHelper t){run(t,Species.DAWN_CORRUPTED_OGRE.bossStage());}
@GameTest(template="empty",batch="boss_lifecycle",timeoutTicks=2200) public static void dawn_dragon_lord(GameTestHelper t){run(t,Species.DAWN_DRAGON_LORD.bossStage());}
@GameTest(template="empty",batch="boss_lifecycle",timeoutTicks=2200) public static void dawn_herobrine(GameTestHelper t){run(t,Species.DAWN_HEROBRINE.bossStage());}
@GameTest(template="empty",batch="boss_lifecycle",timeoutTicks=2200) public static void dawn_martian(GameTestHelper t){run(t,Species.DAWN_MARTIAN.bossStage());}
@GameTest(template="empty",batch="boss_lifecycle",timeoutTicks=2200) public static void dawn_notch_boss(GameTestHelper t){run(t,Species.DAWN_NOTCH_BOSS.bossStage());}
@GameTest(template="empty",batch="boss_lifecycle",timeoutTicks=2200) public static void dawn_skeletonlord(GameTestHelper t){run(t,Species.DAWN_SKELETONLORD.bossStage());}
@GameTest(template="empty",batch="boss_lifecycle",timeoutTicks=2200) public static void mobosses_demon_king(GameTestHelper t){run(t,Species.MOBOSSES_DEMON_KING.bossStage());}
@GameTest(template="empty",batch="boss_lifecycle",timeoutTicks=2200) public static void mobosses_herobrine(GameTestHelper t){run(t,Species.MOBOSSES_HEROBRINE.bossStage());}
@GameTest(template="empty",batch="boss_lifecycle",timeoutTicks=2200) public static void mobosses_nil(GameTestHelper t){run(t,Species.MOBOSSES_NIL.bossStage());}
@GameTest(template="empty",batch="boss_lifecycle",timeoutTicks=2200) public static void mobosses_royal_guardian(GameTestHelper t){run(t,Species.MOBOSSES_ROYAL_GUARDIAN.bossStage());}
}
