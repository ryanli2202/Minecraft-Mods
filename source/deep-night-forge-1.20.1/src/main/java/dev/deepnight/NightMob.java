package dev.deepnight;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;
import java.util.*;

public class NightMob extends Monster implements GeoEntity,NightVisual {
    private static final EntityDataAccessor<Integer> ATTACK=SynchedEntityData.defineId(NightMob.class,EntityDataSerializers.INT);
    public final Species species;
    private final AnimatableInstanceCache cache=GeckoLibUtil.createInstanceCache(this);
    private final ServerBossEvent bar=new ServerBossEvent(Component.empty(),BossEvent.BossBarColor.PURPLE,BossEvent.BossBarOverlay.PROGRESS);
    private final Set<UUID> participants=new HashSet<>();
    private UUID owner;
    private BlockPos arena=BlockPos.ZERO;
    private int deserted;
    public NightMob(EntityType<? extends NightMob> type,Level level,Species species) {
        super(type,level); this.species=species; xpReward=species.boss()?150+species.bossStage()*25:10;
        if(!sourceCombat())setMaxUpStep(1.5f); if(species.boss()||species.kind==Species.Kind.HELPER){setPersistenceRequired();}
        bar.setName(Component.literal(species.title));
    }
    public static AttributeSupplier.Builder attributes(Species s) {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH,Math.min(1000,s.health)).add(Attributes.ATTACK_DAMAGE,s.damage)
            .add(Attributes.ARMOR,s.armor).add(Attributes.ARMOR_TOUGHNESS,s.boss()?6:0).add(Attributes.MOVEMENT_SPEED,s.speed)
            .add(Attributes.FOLLOW_RANGE,s.boss()?64:24).add(Attributes.KNOCKBACK_RESISTANCE,s.boss()?0.9:0.2);
    }
    public boolean sourceCombat(){return getClass()!=NightMob.class;}
    @Override protected void registerGoals() {
        if(sourceCombat())return;
        goalSelector.addGoal(0,new FloatGoal(this));
        goalSelector.addGoal(2,new MeleeAttackGoal(this,1,true));
        goalSelector.addGoal(4,new WaterAvoidingRandomStrollGoal(this,0.7));
        goalSelector.addGoal(5,new LookAtPlayerGoal(this,Player.class,16));
        targetSelector.addGoal(1,new HurtByTargetGoal(this));
        targetSelector.addGoal(2,new NearestAttackableTargetGoal<>(this,Player.class,10,true,false,p->species!=null&&!species.passive()));
    }
    @Override protected void defineSynchedData() {super.defineSynchedData();entityData.define(ATTACK,0);}
    public float encounterHealth(){return getHealth();}
    public void prepareDamage(float damage){}
    public void bind(ServerPlayer player) {owner=player.getUUID(); arena=blockPosition();if(!sourceCombat())playSound(SoundBank.sound(species,"spawn"),0.8f,1);}
    public BlockPos arena() {return arena;}
    public Set<UUID> participants() {return Set.copyOf(participants);}
    @Override public Species species() {return species;}
    @Override public boolean attacking(){return entityData.get(ATTACK)>0;}
    @Override public boolean removeWhenFarAway(double distance) {return !species.boss() && species.kind!=Species.Kind.HELPER && super.removeWhenFarAway(distance);}
    @Override protected boolean shouldDespawnInPeaceful() {return !species.boss()&&!species.passive();}
    @Override public boolean canBreatheUnderwater(){return species.aquatic||super.canBreatheUnderwater();}
    @Override protected net.minecraft.sounds.SoundEvent getAmbientSound(){return SoundBank.sound(species,"ambient");}
    @Override protected net.minecraft.sounds.SoundEvent getHurtSound(DamageSource source){return SoundBank.sound(species,"hurt");}
    @Override protected net.minecraft.sounds.SoundEvent getDeathSound(){return SoundBank.sound(species,"death");}
    @Override protected void playStepSound(BlockPos pos,net.minecraft.world.level.block.state.BlockState block){playSound(SoundBank.sound(species,"step"),0.25f,1);}
    @Override public net.minecraft.world.InteractionResult mobInteract(Player player,net.minecraft.world.InteractionHand hand){
        if(!species.passive())return super.mobInteract(player,hand);
        if(player instanceof ServerPlayer p){
            if(p.getItemInHand(hand).is(net.minecraft.world.item.Items.EMERALD)){
                p.getItemInHand(hand).shrink(1);CampaignEvents.give(p,new ItemStack(DeepNight.ESSENCE.get(),2));
                SummonTool.say(p,species.title+": Two Night Essence for your emerald. Safe travels.");
            }else SummonTool.say(p,species.title+": Bring me one emerald for two Night Essence. Clerics sell the Pet Summoning Tool.");
        }
        return net.minecraft.world.InteractionResult.sidedSuccess(level().isClientSide);
    }
    @Override public boolean canChangeDimensions() {return !species.boss();}
    @Override public void travel(Vec3 input){
        if(species.aquatic&&isInWater()){
            if(getTarget()!=null)setDeltaMovement(getDeltaMovement().add(getTarget().position().subtract(position()).normalize().scale(0.03)));
            moveRelative(0.04f,input);move(MoverType.SELF,getDeltaMovement());setDeltaMovement(getDeltaMovement().scale(0.9));
        }else super.travel(input);
    }
    @Override public boolean canBeLeashed(Player player) {return false;}
    @Override public boolean doHurtTarget(Entity entity) {
        if(sourceCombat())return super.doHurtTarget(entity);
        if(entity instanceof Player p && (p.isCreative()||p.isSpectator())) return false;
        boolean hit=super.doHurtTarget(entity);
        if(hit) {entityData.set(ATTACK,12);playSound(SoundBank.sound(species,"attack"),0.7f,1); if(species==Species.GRAVE_GHOUL && entity instanceof LivingEntity l) l.addEffect(new MobEffectInstance(MobEffects.HUNGER,100));}
        return hit;
    }
    public static ServerPlayer creditedPlayer(Entity entity) {
        if(entity instanceof ServerPlayer p) return p;
        if(entity instanceof TamableAnimal a && a.getOwner() instanceof ServerPlayer p) return p;
        return null;
    }
    @Override public boolean hurt(DamageSource source,float damage) {
        if(species.boss()) {
            ServerPlayer p=creditedPlayer(source.getEntity());
            if(p!=null && !p.isSpectator()) participants.add(p.getUUID());
        }
        return super.hurt(source,damage);
    }
    @Override public void tick() {
        super.tick();
        if(!(level() instanceof ServerLevel server)||!isAlive()) return;
        if(entityData.get(ATTACK)>0) entityData.set(ATTACK,entityData.get(ATTACK)-1);
        if(!species.boss()) {
            CompoundTag data=getPersistentData();
            if(data.hasUUID("DeepNightMaster") && !Objects.equals(CampaignData.get(server.getServer()).active,data.getUUID("DeepNightMaster"))) discard();
            if(!sourceCombat()&&species.ranged&&!species.passive()&&tickCount%65==0&&getTarget()!=null&&distanceToSqr(getTarget())<324&&hasLineOfSight(getTarget())){
                var t=getTarget();server.sendParticles(ParticleTypes.SOUL,t.getX(),t.getY()+1,t.getZ(),16,0.2,0.4,0.2,0.1);
                t.hurt(damageSources().indirectMagic(this,this),(float)species.damage);entityData.set(ATTACK,15);playSound(SoundBank.sound(species,"attack"),0.7f,1);
            }
            return;
        }
        CampaignData data=CampaignData.get(server.getServer());
        if(!hasSourceBossBar())bar.setProgress(getHealth()/getMaxHealth());
        if(owner==null)return;
        if(!Objects.equals(data.active,getUUID())) {discard();return;}
        if(server.getDifficulty()==Difficulty.PEACEFUL) {data.clear(getUUID());discard();return;}
        if(tickCount%40==0) {data.position=blockPosition().asLong();data.setDirty();}
        if(position().distanceToSqr(Vec3.atCenterOf(arena))>10000) {teleportTo(arena.getX()+0.5,arena.getY(),arena.getZ()+0.5);getNavigation().stop();}
        boolean fighters=server.getEntitiesOfClass(ServerPlayer.class,getBoundingBox().inflate(64),p->p.isAlive()&&!p.isSpectator()).size()>0;
        deserted=fighters?0:deserted+1;
        if(deserted>=600) {data.clear(getUUID());discard();return;}
    }
    private boolean hasSourceBossBar(){return sourceCombat()&&switch(species){
        case DAWN_FIRE_DRAGON,DAWN_ICE_DRAGON,DAWN_LIGHTNING_DRAGON,DAWN_MOSS_GOLEM,MOBOSSES_ROYAL_GUARDIAN,MOBOSSES_ROYAL_KNIGHT -> false;
        default -> true;
    };}
    @Override public void kill() {
        // Administrative kills must bypass imported shield/phase hurt overrides.
        if(species.boss()||species.kind==Species.Kind.HELPER) {setHealth(0);die(damageSources().genericKill());}
        else super.kill();
    }
    @Override public void die(DamageSource source) {
        if(!level().isClientSide && species.boss()) {
            var data=CampaignData.get(level().getServer());
            // Creative testing also supports /kill, whose damage has no attacking player.
            ServerPlayer caller=owner!=null&&level().getPlayerByUUID(owner) instanceof ServerPlayer present?present:null;
            boolean creativeCaller=caller!=null&&caller.isCreative()&&!caller.isSpectator()&&caller.isAlive()
                &&caller.level()==level()&&caller.distanceToSqr(this)<16384&&Objects.equals(data.active,getUUID());
            if(creativeCaller)participants.add(caller.getUUID());
            if((!source.is(DamageTypes.GENERIC_KILL)||creativeCaller)&&!source.is(DamageTypes.FELL_OUT_OF_WORLD)) {
                for(UUID id:participants) {
                    ServerPlayer p=level().getPlayerByUUID(id) instanceof ServerPlayer present?present:null;
                    if(p!=null && p.isAlive() && !p.isSpectator() && p.level()==level() && p.distanceToSqr(this)<16384 && data.credit(id,getUUID(),species.bossStage())) {
                        CampaignEvents.give(p,new ItemStack(DeepNight.SIGILS.get(species).get(),4));
                        int essence=4+species.bossStage();
                        while(essence>0){int count=Math.min(64,essence);CampaignEvents.give(p,new ItemStack(DeepNight.ESSENCE.get(),count));essence-=count;}
                        SummonTool.say(p,"Defeated "+species.title+"! "+(data.player(id).stage==Species.BOSS_COUNT?"The Deep Night is conquered.":"Next: "+Species.boss(data.player(id).stage).title));
                    }
                }
            }
            data.clear(getUUID()); for(ServerPlayer p:bar.getPlayers())stopMusic(p);bar.removeAllPlayers();
        }
        super.die(source);
    }
    @Override protected void dropCustomDeathLoot(DamageSource source,int looting,boolean recentHit) {
        super.dropCustomDeathLoot(source,looting,recentHit);
        if(!species.boss() && !getPersistentData().hasUUID("DeepNightMaster") && recentHit)
            spawnAtLocation(new ItemStack(DeepNight.ESSENCE.get(),1+random.nextInt(2)));
    }
    @Override public void startSeenByPlayer(ServerPlayer p) {super.startSeenByPlayer(p);if(species.boss()&&!hasSourceBossBar()){bar.addPlayer(p);var music=SoundBank.sound(species,"music");if(music!=null)p.playNotifySound(music,net.minecraft.sounds.SoundSource.MUSIC,0.4f,1);}}
    private void stopMusic(ServerPlayer p){var music=SoundBank.sound(species,"music");if(music!=null)p.connection.send(new net.minecraft.network.protocol.game.ClientboundStopSoundPacket(music.getLocation(),net.minecraft.sounds.SoundSource.MUSIC));}
    @Override public void stopSeenByPlayer(ServerPlayer p) {super.stopSeenByPlayer(p);stopMusic(p);bar.removePlayer(p);}
    @Override public void remove(RemovalReason reason){if(species!=null&&species.boss())for(ServerPlayer p:bar.getPlayers())stopMusic(p);bar.removeAllPlayers();super.remove(reason);}
    @Override public void addAdditionalSaveData(CompoundTag t) {
        super.addAdditionalSaveData(t); if(owner!=null)t.putUUID("Caller",owner);t.putLong("Arena",arena.asLong());
        t.putInt("Deserted",deserted);
        ListTag ids=new ListTag();participants.forEach(id->ids.add(NbtUtils.createUUID(id)));t.put("Participants",ids);
    }
    @Override public void readAdditionalSaveData(CompoundTag t) {
        super.readAdditionalSaveData(t);
        if(species.boss()){
            float fraction=getMaxHealth()>0?getHealth()/getMaxHealth():0;
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(species.health);
            setHealth(fraction*getMaxHealth());
        }
        owner=t.hasUUID("Caller")?t.getUUID("Caller"):null;arena=BlockPos.of(t.getLong("Arena"));
        deserted=t.getInt("Deserted");
        participants.clear(); for(Tag id:t.getList("Participants",Tag.TAG_INT_ARRAY))participants.add(NbtUtils.loadUUID(id));
    }
    @Override public void registerControllers(AnimatableManager.ControllerRegistrar c) {
        c.add(new AnimationController<NightMob>(this,"body",4,state -> state.setAndContinue(
            isDeadOrDying()?RawAnimation.begin().thenPlay("death")
            :hurtTime>0?RawAnimation.begin().thenPlay("hurt")
            :attacking()?RawAnimation.begin().thenLoop("attack")
            :tickCount<20?RawAnimation.begin().thenPlay("spawn")
            :RawAnimation.begin().thenLoop(state.isMoving()?"walk":"idle"))));
    }
    @Override public AnimatableInstanceCache getAnimatableInstanceCache(){return cache;}
}
