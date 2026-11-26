package dev.deepnight;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Companion extends TamableAnimal implements GeoEntity,NightVisual {
    private static final EntityDataAccessor<Boolean> SADDLED=SynchedEntityData.defineId(Companion.class,EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ATTACK=SynchedEntityData.defineId(Companion.class,EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PET_LEVEL=SynchedEntityData.defineId(Companion.class,EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PET_XP=SynchedEntityData.defineId(Companion.class,EntityDataSerializers.INT);
    public final Species species;
    private final AnimatableInstanceCache cache=GeckoLibUtil.createInstanceCache(this);
    private int bonding;
    public Companion(EntityType<? extends Companion> type,Level level,Species species) {
        super(type,level); this.species=species;setMaxUpStep(1.2f);xpReward=8;
    }
    public static AttributeSupplier.Builder attributes(Species s) {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH,s.health).add(Attributes.ATTACK_DAMAGE,s.damage)
            .add(Attributes.ARMOR,s.armor).add(Attributes.MOVEMENT_SPEED,s.speed).add(Attributes.FOLLOW_RANGE,32).add(Attributes.KNOCKBACK_RESISTANCE,0.5);
    }
    @Override protected void defineSynchedData(){super.defineSynchedData();entityData.define(SADDLED,false);entityData.define(ATTACK,0);entityData.define(PET_LEVEL,1);entityData.define(PET_XP,0);}
    public void syncGrowth(int level,int xp){entityData.set(PET_LEVEL,level);entityData.set(PET_XP,xp);}
    public int petLevel(){return entityData.get(PET_LEVEL);}
    @Override public Component getName(){return isTame()&&!hasCustomName()?Component.literal(species.title+" [Lv. "+petLevel()+"]"):super.getName();}
    @Override public boolean canBreatheUnderwater(){return species.aquatic||species==Species.TIDE_DRAKE||super.canBreatheUnderwater();}
    @Override protected net.minecraft.sounds.SoundEvent getAmbientSound(){return SoundBank.sound(species,"ambient");}
    @Override protected net.minecraft.sounds.SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource s){return SoundBank.sound(species,"hurt");}
    @Override protected net.minecraft.sounds.SoundEvent getDeathSound(){return SoundBank.sound(species,"death");}
    @Override protected void playStepSound(BlockPos p,net.minecraft.world.level.block.state.BlockState state){playSound(SoundBank.sound(species,"step"),0.15f,1);}
    @Override protected void registerGoals() {
        goalSelector.addGoal(0,new FloatGoal(this));goalSelector.addGoal(1,new SitWhenOrderedToGoal(this));
        goalSelector.addGoal(2,new MeleeAttackGoal(this,1.15,true));goalSelector.addGoal(3,new FollowOwnerGoal(this,1.1,12,4,false));
        goalSelector.addGoal(4,new BreedGoal(this,0.9));
        goalSelector.addGoal(5,new TemptGoal(this,1,Ingredient.of(DeepNight.TREAT.get()),false));
        goalSelector.addGoal(6,new WaterAvoidingRandomStrollGoal(this,0.8));
        goalSelector.addGoal(7,new LookAtPlayerGoal(this,Player.class,10));goalSelector.addGoal(8,new RandomLookAroundGoal(this));
        targetSelector.addGoal(1,new OwnerHurtByTargetGoal(this));targetSelector.addGoal(2,new OwnerHurtTargetGoal(this));targetSelector.addGoal(3,new HurtByTargetGoal(this));
    }
    public static boolean canSpawn(EntityType<Companion> t,ServerLevelAccessor l,MobSpawnType r,BlockPos p,RandomSource random) {
        return l.getBlockState(p.below()).isSolid() && l.getFluidState(p).isEmpty() && l.getRawBrightness(p,0)>8;
    }
    @Override public Species species(){return species;}
    @Override public boolean attacking(){return entityData.get(ATTACK)>0;}
    public boolean saddled(){return entityData.get(SADDLED);}
    public void setSaddled(boolean b){entityData.set(SADDLED,b);}
    @Override public boolean isFood(ItemStack s){return isTame()&&s.is(Items.GOLDEN_APPLE);}
    @Override public boolean canMate(Animal other){return other instanceof Companion c && c.species==species && isTame()&&c.isTame()&&super.canMate(other);}
    @Override public AgeableMob getBreedOffspring(ServerLevel l,AgeableMob other){
        Companion c=DeepNight.PETS.get(species).get().create(l);
        if(c!=null&&getOwnerUUID()!=null){c.setOwnerUUID(getOwnerUUID());c.setTame(true);}return c;
    }
    @Override public InteractionResult mobInteract(Player p,InteractionHand hand) {
        ItemStack item=p.getItemInHand(hand);
        if(item.is(DeepNight.TREAT.get())) {
            if(isTame()&&!isOwnedBy(p)) return InteractionResult.PASS;
            if(!level().isClientSide) {
                if(!p.getAbilities().instabuild)item.shrink(1);
                if(!isTame()) {
                    if(++bonding>=3){tame(p);setTarget(null);getNavigation().stop();level().broadcastEntityEvent(this,(byte)7);}
                    else{level().broadcastEntityEvent(this,(byte)6);p.displayClientMessage(Component.literal("Trust: "+bonding+"/3"),true);}
                } else {heal(16);if(isBaby())ageUp(60);}
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        if(isOwnedBy(p)) {
            if(item.is(Items.GOLDEN_APPLE))return super.mobInteract(p,hand);
            if(item.is(Items.SADDLE)&&!saddled()&&!isBaby()){
                if(!level().isClientSide){setSaddled(true);if(!p.getAbilities().instabuild)item.shrink(1);}
                return InteractionResult.sidedSuccess(level().isClientSide);
            }
            if(item.isEmpty()) {
                if(!level().isClientSide) {
                    if(p.isShiftKeyDown()){setOrderedToSit(!isOrderedToSit());getNavigation().stop();setTarget(null);p.displayClientMessage(Component.literal(isOrderedToSit()?"Staying here.":"Following you."),true);}
                    else if(saddled()&&!isBaby()){setOrderedToSit(false);p.startRiding(this);}
                    else p.displayClientMessage(Component.literal("Sneak + interact to sit/follow. Equip a saddle to ride."),true);
                }
                return InteractionResult.sidedSuccess(level().isClientSide);
            }
        }
        return super.mobInteract(p,hand);
    }
    public boolean canHarm(LivingEntity t) {
        if(t==this||t==getOwner()||isAlliedTo(t)||hasPassenger(t))return false;
        if(t instanceof TamableAnimal a && getOwnerUUID()!=null&&getOwnerUUID().equals(a.getOwnerUUID()))return false;
        if(t instanceof Player p && (p.isCreative()||p.isSpectator()||getOwner() instanceof Player owner&&!owner.canHarmPlayer(p)))return false;
        return true;
    }
    @Override public boolean wantsToAttack(LivingEntity t,LivingEntity owner){return canHarm(t)&&super.wantsToAttack(t,owner);}
    @Override public boolean doHurtTarget(Entity t){
        if(t instanceof LivingEntity l && !canHarm(l))return false;
        boolean hit=super.doHurtTarget(t);if(hit){entityData.set(ATTACK,12);playSound(SoundBank.sound(species,"attack"),0.6f,1);}return hit;
    }
    @Override public void tick(){
        super.tick();if(!(level() instanceof ServerLevel l))return;
        if(attacking())entityData.set(ATTACK,entityData.get(ATTACK)-1);
        if(isOrderedToSit()||isBaby())return;
        LivingEntity target=getTarget();
        if(species.dragon()&&tickCount%50==0&&target!=null&&target.isAlive()&&canHarm(target)&&distanceToSqr(target)<196&&hasLineOfSight(target)){
            Vec3 start=getEyePosition(),direction=target.getEyePosition().subtract(start);
            var particle=switch(species.breathElement()){
                case "fire"->ParticleTypes.FLAME;case "water"->ParticleTypes.SPLASH;
                case "frost"->ParticleTypes.SNOWFLAKE;case "lightning"->ParticleTypes.ELECTRIC_SPARK;
                case "earth"->ParticleTypes.HAPPY_VILLAGER;case "wind"->ParticleTypes.CLOUD;default->ParticleTypes.SOUL;
            };
            for(int i=0;i<30;i++){Vec3 at=start.add(direction.scale(i/30.0));l.sendParticles(particle,at.x,at.y,at.z,1,0.08,0.08,0.08,0);}
            if(target.hurt(damageSources().mobAttack(this),6*(1+0.02f*(petLevel()-1)))){
                if(species.breathElement().equals("fire"))target.setSecondsOnFire(3);
                if(species.breathElement().equals("frost")||species.breathElement().equals("earth"))
                    target.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN,40,0));
                if(species.breathElement().equals("wind"))target.knockback(0.6,-direction.x,-direction.z);
            }
            entityData.set(ATTACK,15);
        }
        if(species==Species.STAR_UNICORN&&isTame()&&tickCount%200==0&&getOwner()!=null&&distanceToSqr(getOwner())<100)getOwner().heal(2);
    }
    @Override public LivingEntity getControllingPassenger(){return getFirstPassenger() instanceof Player p&&isOwnedBy(p)&&saddled()&&!isBaby()?p:null;}
    @Override public void travel(Vec3 input){
        if(species.aquatic&&isInWater()&&!isOrderedToSit()){
            LivingEntity follow=getTarget()!=null?getTarget():getOwner();
            if(follow!=null&&distanceToSqr(follow)>9)setDeltaMovement(getDeltaMovement().add(follow.position().subtract(position()).normalize().scale(0.025)));
            moveRelative(0.04f,input);move(MoverType.SELF,getDeltaMovement());setDeltaMovement(getDeltaMovement().scale(0.9));
        }else super.travel(input);
    }
    @Override protected boolean canAddPassenger(Entity e){return getPassengers().isEmpty()&&e instanceof Player p&&isOwnedBy(p)&&saddled()&&!isBaby();}
    @Override protected void tickRidden(Player p,Vec3 input){super.tickRidden(p,input);setRot(p.getYRot(),p.getXRot()*0.3f);yBodyRot=getYRot();yHeadRot=getYRot();getNavigation().stop();setTarget(null);}
    @Override protected Vec3 getRiddenInput(Player p,Vec3 input){return new Vec3(p.xxa*0.5,0,p.zza<0?p.zza*0.25:p.zza);}
    @Override protected float getRiddenSpeed(Player p){return (float)species.speed*1.2f;}
    @Override public double getPassengersRidingOffset(){return getBbHeight()*0.75;}
    @Override public void addAdditionalSaveData(CompoundTag t){super.addAdditionalSaveData(t);t.putBoolean("Saddled",saddled());t.putInt("Bonding",bonding);t.putInt("DeepNightCompanionVersion",1);}
    @Override public void readAdditionalSaveData(CompoundTag t){super.readAdditionalSaveData(t);
        if((species==Species.DAWN_FIRE_DRAGON||species==Species.DAWN_ICE_DRAGON||species==Species.DAWN_LIGHTNING_DRAGON)&&!t.contains("DeepNightCompanionVersion")){
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(species.health);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(species.damage);
            getAttribute(Attributes.ARMOR).setBaseValue(species.armor);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(species.speed);
            setInvulnerable(false);setNoAi(false);setNoGravity(false);setTarget(null);
        }
        setSaddled(t.getBoolean("Saddled"));bonding=t.getInt("Bonding");if(!level().isClientSide){PetGrowth.apply(this);setHealth(Math.min(getMaxHealth(),t.getFloat("Health")));}}
    @Override public void registerControllers(AnimatableManager.ControllerRegistrar c){c.add(new AnimationController<>(this,"body",4,s->s.setAndContinue(isDeadOrDying()?RawAnimation.begin().thenPlay("death"):hurtTime>0?RawAnimation.begin().thenPlay("hurt"):RawAnimation.begin().thenLoop(attacking()?"attack":isOrderedToSit()?"sit":s.isMoving()?"walk":"idle"))));}
    @Override public AnimatableInstanceCache getAnimatableInstanceCache(){return cache;}
}
