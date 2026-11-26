package dev.travellerscompanions;

import java.util.*;
import javax.annotation.Nullable;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

public final class CompanionEntity extends TamableAnimal implements io.redspace.ironsspellbooks.api.entity.IMagicEntity {
    private static final EntityDataAccessor<Integer> KIND=SynchedEntityData.defineId(CompanionEntity.class,EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TROOP=SynchedEntityData.defineId(CompanionEntity.class,EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TROOP_SLOT=SynchedEntityData.defineId(CompanionEntity.class,EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LEVEL=SynchedEntityData.defineId(CompanionEntity.class,EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> STANCE=SynchedEntityData.defineId(CompanionEntity.class,EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> HOLD=SynchedEntityData.defineId(CompanionEntity.class,EntityDataSerializers.BOOLEAN);
    public final SimpleContainer backpack=new SimpleContainer(27){@Override public void setChanged(){super.setChanged();if(!level().isClientSide)checkpoint();}};
    public final SimpleContainer spellInventory=new SimpleContainer(CompanionSpells.SLOTS){@Override public void setChanged(){super.setChanged();if(!level().isClientSide){spells().loadoutChanged();checkpoint();}}};
    private CompanionSpells spellcasting;
    public final KatanaGuard guard=new KatanaGuard(this);
    public final CommonKatanaCombat katana=new CommonKatanaCombat(this);
    public final CompanionParkour parkour=new CompanionParkour(this);
    private static final EntityDataAccessor<Integer> PARKOUR=SynchedEntityData.defineId(CompanionEntity.class,EntityDataSerializers.INT);
    private static final EntityDataAccessor<Long> PARKOUR_START=SynchedEntityData.defineId(CompanionEntity.class,EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Integer> PARKOUR_DURATION=SynchedEntityData.defineId(CompanionEntity.class,EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> PARKOUR_YAW=SynchedEntityData.defineId(CompanionEntity.class,EntityDataSerializers.FLOAT);
    public int parkourAction(){return entityData.get(PARKOUR);}
    public int parkourDuration(){return entityData.get(PARKOUR_DURATION);}
    public float parkourYaw(){return entityData.get(PARKOUR_YAW);}
    public float parkourElapsed(float partial){return Math.max(0,level().getGameTime()-entityData.get(PARKOUR_START)+partial);}
    public void setParkourState(int action,int duration,float yaw){entityData.set(PARKOUR_START,level().getGameTime());entityData.set(PARKOUR_DURATION,duration);entityData.set(PARKOUR_YAW,yaw);entityData.set(PARKOUR,action);}
    public boolean attackPending(){return pending!=null;}
    @Override public void travel(Vec3 movement){if(parkour.busy()){setDeltaMovement(Vec3.ZERO);return;}super.travel(movement);}
    private static final EntityDataAccessor<Boolean> GUARD=SynchedEntityData.defineId(CompanionEntity.class,EntityDataSerializers.BOOLEAN);
    public CompanionSpells spells(){if(spellcasting==null)spellcasting=new CompanionSpells(this);return spellcasting;}
    public boolean guarding(){return entityData.get(GUARD);}
    public void setGuarding(boolean value){entityData.set(GUARD,value);}
    public void interruptAttack(){pending=null;windup=0;stopUsingItem();}
    @Override public io.redspace.ironsspellbooks.api.magic.MagicData getMagicData(){return spells().magic();}
    @Override public void setSyncedSpellData(io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData data){if(level().isClientSide)spells().clientSync(data);}
    @Override public boolean isCasting(){return getMagicData().isCasting();}
    @Override public void initiateCastSpell(io.redspace.ironsspellbooks.api.spells.AbstractSpell spell,int level){spells().initiate(spell,level);}
    @Override public void cancelCast(){if(!level().isClientSide)spells().cancel(true);}
    @Override public void castComplete(){if(!level().isClientSide)spells().cancel(false);}
    @Override public void notifyDangerousProjectile(Projectile projectile){}
    @Override public boolean setTeleportLocationBehindTarget(int distance){return false;}
    @Override public void setBurningDashDirectionData(){}
    @Override public boolean isDrinkingPotion(){return false;}
    @Override public boolean getHasUsedSingleAttack(){return false;}
    @Override public void setHasUsedSingleAttack(boolean value){}
    @Override public void startDrinkingPotion(){}
    private boolean restoring,exiting;
    private int combatTicks,attackCooldown,windup,combo;
    private int ownerHurtStamp=-1,ownerHitStamp=-1;
    private CombatBridge.Strike pending;
    private UUID pendingTarget;
    private UUID ownerAttackTarget;
    private long nextBark;
    // Client animation state is data only; no client classes are loaded on a dedicated server.
    public String animation="";public int animationDuration=20,animationStarted=-1000;
    public String lastDialogue="";
    public CompanionEntity(EntityType<? extends TamableAnimal> type,Level level){super(type,level);setPersistenceRequired();setCanPickUpLoot(false);}
    public static AttributeSupplier.Builder attributes(){return Mob.createMobAttributes().add(Attributes.MAX_HEALTH,100).add(Attributes.MOVEMENT_SPEED,.31).add(Attributes.ATTACK_DAMAGE,1).add(Attributes.ATTACK_KNOCKBACK,.2).add(Attributes.FOLLOW_RANGE,24).add(Attributes.KNOCKBACK_RESISTANCE,.1);}
    @Override protected void defineSynchedData(){super.defineSynchedData();entityData.define(KIND,0);entityData.define(TROOP,-1);entityData.define(TROOP_SLOT,-1);entityData.define(LEVEL,1);entityData.define(STANCE,0);entityData.define(HOLD,false);entityData.define(GUARD,false);entityData.define(PARKOUR,0);entityData.define(PARKOUR_START,0L);entityData.define(PARKOUR_DURATION,0);entityData.define(PARKOUR_YAW,0f);}
    @Override protected void registerGoals(){goalSelector.addGoal(0,new FloatGoal(this));goalSelector.addGoal(1,new TravelGoal());goalSelector.addGoal(6,new LookAtPlayerGoal(this,Player.class,8));goalSelector.addGoal(7,new RandomLookAroundGoal(this));}
    public CharacterKind kind(){return CharacterKind.of(entityData.get(KIND));}
    public boolean isTroop(){return TroopKind.valid(entityData.get(TROOP));}
    public TroopKind troopKind(){return TroopKind.of(entityData.get(TROOP));}
    public int troopSlot(){return entityData.get(TROOP_SLOT);}
    public String displayName(){return isTroop()?troopKind().display+" "+(troopSlot()+1):kind().display;}
    public String skinKey(){return isTroop()?switch(troopKind()){case FOOTSQUIRE->"footsquire";case FOOTPALADIN->"footpaladin";case FOOTARCHER->"footarcher";}:kind().key();}
    public CompanionData.TroopEntry troopEntry(){return CompanionData.get(getServer()).roster(getOwnerUUID()).troops[troopKind().ordinal()][troopSlot()];}
    public int companionLevel(){return entityData.get(LEVEL);}
    public int stance(){return entityData.get(STANCE);}
    public boolean holding(){return entityData.get(HOLD);}
    public void setHolding(boolean value){parkour.cancel();entityData.set(HOLD,value);getNavigation().stop();setTarget(null);checkpoint();}
    public void setStance(int value){parkour.cancel();interruptAttack();cancelCast();katana.cancel();entityData.set(STANCE,Math.max(0,Math.min(2,value)));setTarget(null);checkpoint();}
    public boolean ownedBy(Player p){return p.getUUID().equals(getOwnerUUID());}
    public void configure(ServerPlayer owner,int kind,int level){setOwnerUUID(owner.getUUID());setTame(true);entityData.set(KIND,kind);setCustomName(Component.literal(displayName()));setCustomNameVisible(true);setLevel(level);}
    public void configureTroop(ServerPlayer owner,int kind,int slot){entityData.set(TROOP,kind);entityData.set(TROOP_SLOT,slot);configure(owner,TroopKind.of(kind).combatKind.ordinal(),1);}
    public void setLevel(int level){level=isTroop()?1:Math.max(1,Math.min(CompanionData.MAX_LEVEL,level));float oldMax=getMaxHealth();entityData.set(LEVEL,level);getAttribute(Attributes.MAX_HEALTH).setBaseValue(isTroop()?TroopKind.HEALTH:kind().maxHealth(level));getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(1+(level-1)*.25);setHealth(Math.max(1,Math.min(getMaxHealth(),getHealth()+getMaxHealth()-oldMax)));}
    public void giveStarter(){
        restoring=true;
        switch(kind()){
            case LEONARDO->{setItemSlot(EquipmentSlot.MAINHAND,starterItem("irons_spellbooks:graybeard_staff",Items.STICK));spells().starter();}
            case YURIKO->setItemSlot(EquipmentSlot.MAINHAND,starterItem("simplyswords:iron_katana",Items.IRON_SWORD));
            case YUSHIRO->{setItemSlot(EquipmentSlot.MAINHAND,starterItem("common_weapons:test",Items.IRON_SWORD));if(level() instanceof ServerLevel server)software.bernie.geckolib.animatable.GeoItem.getOrAssignId(getMainHandItem(),server);}
            case MARA->{setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(Items.CROSSBOW));backpack.setItem(0,new ItemStack(Items.ARROW,64));}
            case VARYS->{setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(Items.BOW));backpack.setItem(0,new ItemStack(Items.ARROW,64));}
            case RAESHEL->setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(Items.IRON_SWORD));
            case GIDEON->{setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(Items.IRON_SWORD));setItemSlot(EquipmentSlot.OFFHAND,new ItemStack(Items.SHIELD));}
            case CASTELAN->{var id=net.minecraft.resources.ResourceLocation.tryParse(TravellersCompanions.SPEAR.get());var item=id==null?Items.AIR:ForgeRegistries.ITEMS.getValue(id);if(item==null||item==Items.AIR)item=ForgeRegistries.ITEMS.getValue(new net.minecraft.resources.ResourceLocation("magistuarmory","iron_pike"));setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(item==null||item==Items.AIR?Items.TRIDENT:item));}
        }
        if(isTroop()&&troopKind()==TroopKind.FOOTPALADIN)setItemSlot(EquipmentSlot.OFFHAND,new ItemStack(Items.SHIELD));
        backpack.setItem(8,new ItemStack(Items.BREAD,4));setHealth(getMaxHealth());restoring=false;
    }
    public static ItemStack starterItem(String id,Item fallback){var item=ForgeRegistries.ITEMS.getValue(new net.minecraft.resources.ResourceLocation(id));return new ItemStack(item==null||item==Items.AIR?fallback:item);}
    public CompoundTag snapshot(){var n=new CompoundTag();n.putFloat("Health",getHealth());n.putFloat("MaxHealth",getMaxHealth());n.putInt("Stance",stance());n.putBoolean("Hold",holding());var cargo=new ListTag();for(int i=0;i<27;i++){var item=new CompoundTag();item.putInt("Slot",i);backpack.getItem(i).save(item);cargo.add(item);}n.put("Cargo",cargo);var gear=new CompoundTag();for(var slot:EquipmentSlot.values())gear.put(slot.getName(),getItemBySlot(slot).save(new CompoundTag()));n.put("Gear",gear);var spells=new ListTag();for(int i=0;i<CompanionSpells.SLOTS;i++){var item=spellInventory.getItem(i).save(new CompoundTag());item.putInt("Slot",i);spells.add(item);}n.put("Spells",spells);if(kind()==CharacterKind.LEONARDO)n.put("Magic",spells().save());return n;}
    public void restore(CompoundTag n){restoring=true;spellInventory.clearContent();for(Tag raw:n.getList("Spells",Tag.TAG_COMPOUND)){var item=(CompoundTag)raw;int i=item.getInt("Slot");if(i>=0&&i<CompanionSpells.SLOTS)spellInventory.setItem(i,ItemStack.of(item));}if(kind()==CharacterKind.LEONARDO)spells().load(n.getCompound("Magic"));entityData.set(STANCE,Math.max(0,Math.min(2,n.getInt("Stance"))));entityData.set(HOLD,n.getBoolean("Hold"));for(Tag raw:n.getList("Cargo",Tag.TAG_COMPOUND)){var t=(CompoundTag)raw;int slot=t.getInt("Slot");if(slot>=0&&slot<27)backpack.setItem(slot,ItemStack.of(t));}for(var s:EquipmentSlot.values())setItemSlot(s,ItemStack.of(n.getCompound("Gear").getCompound(s.getName())));setHealth(isTroop()?Math.max(1,Math.min(getMaxHealth(),n.getFloat("Health"))):kind().restoredHealth(n,companionLevel()));restoring=false;}
    public boolean validLease(){if(getServer()==null||getOwnerUUID()==null)return false;var r=CompanionData.get(getServer()).roster(getOwnerUUID());return isTroop()?r.squadKind==troopKind().ordinal()&&troopSlot()>=0&&troopSlot()<TroopKind.SIZE&&getUUID().equals(r.squad.get(troopSlot())):getUUID().equals(r.party.get(kind().ordinal()));}
    public void checkpoint(){if(restoring||exiting||level().isClientSide||!validLease())return;var d=CompanionData.get(getServer());if(isTroop())troopEntry().snapshot=snapshot();else d.roster(getOwnerUUID()).entries[kind().ordinal()].snapshot=snapshot();d.setDirty();}
    @Override public void addAdditionalSaveData(CompoundTag n){super.addAdditionalSaveData(n);n.putInt("TroopKind",isTroop()?troopKind().ordinal():-1);n.putInt("TroopSlot",troopSlot());n.putInt("Character",kind().ordinal());n.putInt("CompanionLevel",companionLevel());n.put("CompanionState",snapshot());}
    @Override public void readAdditionalSaveData(CompoundTag n){super.readAdditionalSaveData(n);entityData.set(TROOP,n.contains("TroopKind")&&TroopKind.valid(n.getInt("TroopKind"))?n.getInt("TroopKind"):-1);entityData.set(TROOP_SLOT,n.contains("TroopSlot")?n.getInt("TroopSlot"):-1);entityData.set(KIND,Math.max(0,Math.min(CharacterKind.COUNT-1,n.getInt("Character"))));setLevel(Math.max(1,Math.min(20,n.getInt("CompanionLevel"))));restore(n.getCompound("CompanionState"));}
    @Override public boolean isFood(ItemStack stack){return false;}
    @Nullable @Override public AgeableMob getBreedOffspring(ServerLevel l,AgeableMob other){return null;}
    @Override public boolean removeWhenFarAway(double d){return false;}
    @Override public boolean canBeLeashed(Player p){return false;}
    @Override public boolean canAttack(LivingEntity target){return (CompanionTargets.isMonster(target)||CompanionTargets.isOwnerAttackTarget(target)&&target.getUUID().equals(ownerAttackTarget))&&super.canAttack(target);}
    @Override public void setTarget(@Nullable LivingEntity target){if(target!=null&&(!canAttack(target)||stance()==2))return;boolean engage=getTarget()==null&&target!=null;super.setTarget(target);if(target==null||!target.getUUID().equals(ownerAttackTarget))ownerAttackTarget=null;if(engage&&!level().isClientSide)bark(kind().combat);}
    private void assistOwner(LivingEntity target){
        if(stance()==2||!CompanionTargets.isOwnerAttackTarget(target)||!target.isAlive()||!super.canAttack(target))return;
        ownerAttackTarget=target.getUUID();setTarget(target);
    }
    @Override public InteractionResult mobInteract(Player p,InteractionHand hand){
        if(!ownedBy(p))return InteractionResult.PASS;
        if(!level().isClientSide&&p instanceof ServerPlayer sp&&hand==InteractionHand.MAIN_HAND){
            if(p.isShiftKeyDown())CompanionNetwork.openWheel(sp,this);
            else NetworkHooks.openScreen(sp,new SimpleMenuProvider((id,inv,player)->new CompanionMenu(id,inv,this),Component.literal(displayName()+" - Equipment")),b->b.writeVarInt(getId()));
        }return InteractionResult.sidedSuccess(level().isClientSide);
    }
    public void bark(String[] lines){if(isTroop()||getServer()==null||System.currentTimeMillis()<nextBark)return;nextBark=System.currentTimeMillis()+12000;say(lines[random.nextInt(lines.length)],true);}
    public void say(String line,boolean broadcast){if(!broadcast)lastDialogue=line;var text=Component.literal("<"+displayName()+"> "+line).withStyle(net.minecraft.ChatFormatting.GRAY);if(broadcast&&TravellersCompanions.PUBLIC_CHAT.get())getServer().getPlayerList().broadcastSystemMessage(text,false);else if(getOwner() instanceof ServerPlayer p)p.sendSystemMessage(text);}
    @Override public boolean hurt(DamageSource source,float amount){
        if(source.getEntity() instanceof Player||source.getEntity() instanceof CompanionEntity)return false;
        if(!level().isClientSide&&guard.block(source))return false;
        if(!level().isClientSide&&amount>0&&parkour.dodge(source))return false;
        boolean result=super.hurt(source,amount);
        if(result&&!level().isClientSide&&!isRemoved()){combatTicks=100;bark(kind().hurt);if(source.getEntity() instanceof LivingEntity attacker&&canAttack(attacker))setTarget(attacker);checkpoint();}return result;
    }
    @Override protected void hurtArmor(DamageSource source,float amount){if(amount<=0)return;int wear=Math.max(1,(int)(amount/4));for(var slot:EquipmentSlot.values())if(slot.getType()==EquipmentSlot.Type.ARMOR){var item=getItemBySlot(slot);if(!(source.is(net.minecraft.tags.DamageTypeTags.IS_FIRE)&&item.getItem().isFireResistant()))item.hurtAndBreak(wear,this,e->e.broadcastBreakEvent(slot));}}
    @Override protected void hurtCurrentlyUsedShield(float amount){if(amount<3)return;var hand=getUsedItemHand();getUseItem().hurtAndBreak(1+(int)amount,this,e->e.broadcastBreakEvent(hand));if(getUseItem().isEmpty())stopUsingItem();}
    @Override public void die(DamageSource source){if(level().isClientSide)return;cancelCast();katana.cancel();if(validLease()){setHealth(getMaxHealth());checkpoint();var d=CompanionData.get(getServer());var r=d.roster(getOwnerUUID());if(isTroop())troopEntry().readyAt=System.currentTimeMillis()+CompanionData.RECOVERY_MILLIS;else r.entries[kind().ordinal()].readyAt=System.currentTimeMillis()+CompanionData.RECOVERY_MILLIS;releaseLease();d.setDirty();say("I must retreat. Give me ten minutes, and I'll be ready again.",true);}exiting=true;discard();}
    @Override public void tick(){
        super.tick();if(level().isClientSide||exiting)return;
        if(!validLease()){discard();return;}
        int savedLevel=isTroop()?1:CompanionData.get(getServer()).roster(getOwnerUUID()).entries[kind().ordinal()].level;if(savedLevel!=companionLevel())setLevel(savedLevel);
        if(getOwner()==null&&tickCount>100){checkpoint();var d=CompanionData.get(getServer());var r=d.roster(getOwnerUUID());releaseLease();d.setDirty();exiting=true;discard();return;}
        parkour.tick();guard.tick();katana.tick();spells().tick();
        if(combatTicks>0)combatTicks--;
        if(tickCount%10==0)selectTarget();
        if(tickCount%100==0&&combatTicks==0&&getTarget()==null&&TravellersCompanions.FOOD.get()&&getHealth()<getMaxHealth()){
            for(int i=0;i<27;i++){var s=backpack.getItem(i);var food=s.getFoodProperties(this);if(food!=null&&!s.is(Items.ROTTEN_FLESH)&&!s.is(Items.SPIDER_EYE)&&!s.is(Items.POISONOUS_POTATO)&&!s.is(Items.PUFFERFISH)&&!s.is(Items.CHORUS_FRUIT)){heal(food.getNutrition()*2);ItemStack remainder=s.getCraftingRemainingItem();s.shrink(1);if(!remainder.isEmpty()&&!backpack.addItem(remainder).isEmpty())spawnAtLocation(remainder);playSound(SoundEvents.GENERIC_EAT,.7f,1);break;}}
        }
        if(tickCount%20==0)checkpoint();
    }
    private void selectTarget(){
        if(stance()==2){setTarget(null);return;}
        var target=getTarget();if(target!=null&&(!target.isAlive()||!canAttack(target)||distanceToSqr(target)>900||(getOwner()!=null&&getOwner().distanceToSqr(target)>1600)))setTarget(null);
        var owner=getOwner();if(owner!=null){
            if(owner.getLastHurtByMobTimestamp()!=ownerHurtStamp){ownerHurtStamp=owner.getLastHurtByMobTimestamp();if(canAttack(owner.getLastHurtByMob()))setTarget(owner.getLastHurtByMob());}
            if(owner.getLastHurtMobTimestamp()!=ownerHitStamp){ownerHitStamp=owner.getLastHurtMobTimestamp();assistOwner(owner.getLastHurtMob());}
        }
        if(getTarget()==null&&stance()==0){level().getEntitiesOfClass(LivingEntity.class,getBoundingBox().inflate(16),m->m.isAlive()&&CompanionTargets.isMonster(m)&&canAttack(m)&&getSensing().hasLineOfSight(m)).stream().min(Comparator.comparingDouble(this::distanceToSqr)).ifPresent(this::setTarget);}
    }
    private void releaseLease(){var r=CompanionData.get(getServer()).roster(getOwnerUUID());if(isTroop()){r.squad.remove(troopSlot());if(r.squad.isEmpty())r.squadKind=-1;}else r.party.remove(kind().ordinal());}
    public void dismiss(){cancelCast();katana.cancel();checkpoint();exiting=true;discard();}
    private boolean ranged(){return getMainHandItem().getItem() instanceof BowItem||getMainHandItem().getItem() instanceof CrossbowItem;}
    private ItemStack ammo(){for(int i=0;i<27;i++)if(backpack.getItem(i).getItem() instanceof ArrowItem)return backpack.getItem(i);return ItemStack.EMPTY;}
    private void shoot(LivingEntity target){
        var ammo=ammo();if(ammo.isEmpty()||!canAttack(target))return;boolean crossbow=getMainHandItem().getItem() instanceof CrossbowItem;var arrow=((ArrowItem)ammo.getItem()).createArrow(level(),ammo,this);arrow.setBaseDamage(2+companionLevel()*.18);arrow.pickup=AbstractArrow.Pickup.DISALLOWED;arrow.setShotFromCrossbow(crossbow);
        double x=target.getX()-getX(),z=target.getZ()-getZ(),y=target.getY(.45)-arrow.getY();arrow.shoot(x,y+Math.sqrt(x*x+z*z)*.16,z,crossbow?3.15f:1.8f,Math.max(1,6-companionLevel()/4f));
        level().addFreshEntity(arrow);ammo.shrink(1);getMainHandItem().hurtAndBreak(1,this,e->e.broadcastBreakEvent(EquipmentSlot.MAINHAND));playSound(crossbow?SoundEvents.CROSSBOW_SHOOT:SoundEvents.SKELETON_SHOOT,1,.9f);checkpoint();
    }
    class TravelGoal extends Goal {
        TravelGoal(){setFlags(EnumSet.of(Flag.MOVE,Flag.LOOK));}
        @Override public boolean canUse(){return getOwner()!=null||getTarget()!=null;}
        @Override public boolean requiresUpdateEveryTick(){return true;}
        @Override public void stop(){getNavigation().stop();stopUsingItem();}
        @Override public void tick(){
            if(attackCooldown>0)attackCooldown--;
            if(parkour.busy()){getNavigation().stop();return;}
            var target=getTarget();
            if(kind()==CharacterKind.LEONARDO){
                pending=null;stopUsingItem();if(isCasting()){getNavigation().stop();return;}
                if(target!=null&&target.isAlive()&&canAttack(target)&&stance()!=2){double range=spells().preferredRange();if(distanceToSqr(target)>range*range||!getSensing().hasLineOfSight(target)){if(!holding())parkour.moveTo(target,1.1);}else getNavigation().stop();return;}
            }
            if(katana.active()||guarding()){getNavigation().stop();return;}
            if(pending!=null){
                if(--windup<=0){var strike=pending;pending=null;stopUsingItem();
                    if(target!=null&&target.getUUID().equals(pendingTarget)&&target.isAlive()&&canAttack(target)&&distanceTo(target)<=strike.range()+.5&&getSensing().hasLineOfSight(target)){
                        var a=getAttribute(Attributes.ATTACK_DAMAGE);var bonus=new AttributeModifier(UUID.fromString("098b79f9-c2b3-4710-a521-293580bc4e01"),"Companion combo",strike.multiplier()-1,AttributeModifier.Operation.MULTIPLY_TOTAL);a.addTransientModifier(bonus);try{doHurtTarget(target);}finally{a.removeModifier(bonus);}getMainHandItem().hurtAndBreak(1,CompanionEntity.this,e->e.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                        checkpoint();
                    }
                }
            }
            if(target!=null&&target.isAlive()&&canAttack(target)&&stance()!=2){
                combatTicks=100;getLookControl().setLookAt(target,30,30);
                if(katana.equipped()){if(distanceToSqr(target)>10||!getSensing().hasLineOfSight(target)){if(!holding())parkour.moveTo(target,1.1);}else{getNavigation().stop();katana.start();}return;}
                if(ranged()&&!ammo().isEmpty()){
                    if(distanceToSqr(target)>144||!getSensing().hasLineOfSight(target)){if(!holding())parkour.moveTo(target,1.1);}else getNavigation().stop();
                    boolean crossbow=getMainHandItem().getItem() instanceof CrossbowItem;
                    int charge=crossbow?CrossbowItem.getChargeDuration(getMainHandItem()):15;
                    if(attackCooldown<=charge&&!isUsingItem()){startUsingItem(InteractionHand.MAIN_HAND);if(crossbow)playSound(SoundEvents.CROSSBOW_LOADING_START,.7f,1);}
                    if(attackCooldown<=0&&isUsingItem()&&getTicksUsingItem()>=charge&&getSensing().hasLineOfSight(target)&&distanceToSqr(target)<324){stopUsingItem();shoot(target);attackCooldown=Math.max(18,35-companionLevel()/2);}return;
                }
                var strike=CombatBridge.strike(CompanionEntity.this,combo);
                if(distanceTo(target)>strike.range()||!getSensing().hasLineOfSight(target)){if(!holding())parkour.moveTo(target,1.15);}else{
                    getNavigation().stop();if(attackCooldown<=0&&pending==null){stopUsingItem();pending=strike;pendingTarget=target.getUUID();windup=strike.windup();attackCooldown=strike.duration();combo++;CompanionNetwork.animate(CompanionEntity.this,strike);swing(InteractionHand.MAIN_HAND);}
                }
                if(pending==null&&getOffhandItem().getItem() instanceof ShieldItem&&attackCooldown>4&&!isUsingItem())startUsingItem(InteractionHand.OFF_HAND);
            }else{
                pending=null;stopUsingItem();var owner=getOwner();if(owner==null||holding()){getNavigation().stop();return;}
                double distance=distanceToSqr(owner);if(distance>400&&tickCount%20==0&&TravellersCompanions.TELEPORT.get())CompanionService.teleportNear(CompanionEntity.this,owner);
                else if(distance>9&&tickCount%5==0)parkour.moveTo(owner,parkour.followSpeed());else if(distance<6)getNavigation().stop();
            }
        }
    }
}
