package dev.travellerscompanions;

import io.redspace.ironsspellbooks.api.item.IScroll;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import java.util.*;
import net.minecraft.nbt.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

/** Calls Iron's real mob-casting lifecycle; scrolls are removable equipment, not copied spells. */
public final class CompanionSpells {
    public static final int SLOTS=6;
    public enum Role { ATTACK, HEAL, BUFF }
    public record Rule(Role role,double range) {}
    private static final Map<String,Rule> RULES=new LinkedHashMap<>();
    static {
        register(Role.ATTACK,24,"magic_missile","magic_arrow","firebolt","fire_arrow","icicle","snowball","poison_arrow","acid_orb","blood_needles","lightning_lance","guiding_bolt","wither_skull","fireball","magma_bomb","lob_creeper","ball_lightning","ray_of_frost");
        register(Role.ATTACK,8,"fire_breath","cone_of_cold","poison_breath","electrocute");
        register(Role.ATTACK,4,"blood_slash","flaming_strike","frostwave","heat_surge");
        register(Role.HEAL,0,"heal","greater_heal");
        register(Role.BUFF,0,"oakskin","fortify");
    }
    private static void register(Role role,double range,String... names){for(String name:names)RULES.put("irons_spellbooks:"+name,new Rule(role,range));}
    public static Set<String> supportedSpells(){return Collections.unmodifiableSet(RULES.keySet());}
    public static Rule rule(AbstractSpell spell){return RULES.get(spell.getSpellId());}
    public static SpellData spell(ItemStack stack){
        if(stack.isEmpty()||!(stack.getItem() instanceof IScroll)||!ISpellContainer.isSpellContainer(stack))return SpellData.EMPTY;
        var container=ISpellContainer.get(stack);
        return container.getActiveSpellCount()==1?container.getActiveSpells().get(0).spellData():SpellData.EMPTY;
    }
    public static boolean accepts(ItemStack stack){var data=spell(stack);return data!=SpellData.EMPTY&&rule(data.getSpell())!=null&&data.getSpell().isEnabled()&&data.getLevel()>=1&&data.getLevel()<=data.getSpell().getMaxLevel();}
    public static ItemStack scroll(String id,int level){
        var item=ForgeRegistries.ITEMS.getValue(new ResourceLocation("irons_spellbooks","scroll"));
        var stack=new ItemStack(item==null?Items.AIR:item);
        ISpellContainer.createScrollContainer(SpellRegistry.getSpell(id),level,stack);return stack;
    }

    private final CompanionEntity entity;
    private final MagicData magic=new MagicData(true);
    private final Map<String,Long> cooldowns=new HashMap<>();
    private SpellData casting;
    private int castingSlot=-1,nextSlot,recovery;
    private ItemStack castingScroll=ItemStack.EMPTY;
    private UUID targetId;
    public int castsCompleted;
    public String lastSpell="";
    public CompanionSpells(CompanionEntity entity){this.entity=entity;magic.setSyncedData(new SyncedSpellData(entity));magic.setMana(maxMana());}
    public MagicData magic(){return magic;}
    public int maxMana(){return 150+10*(Math.max(1,entity.companionLevel())-1);}
    public long cooldown(AbstractSpell spell){return Math.max(0,cooldowns.getOrDefault(spell.getSpellId(),0L)-System.currentTimeMillis());}
    public void starter(){entity.spellInventory.setItem(0,scroll("irons_spellbooks:magic_missile",1));entity.spellInventory.setItem(1,scroll("irons_spellbooks:firebolt",1));entity.spellInventory.setItem(2,scroll("irons_spellbooks:heal",1));magic.setMana(maxMana());}
    public CompoundTag save(){var n=new CompoundTag();n.putFloat("Mana",magic.getMana());var timers=new CompoundTag();cooldowns.forEach(timers::putLong);n.put("Cooldowns",timers);return n;}
    public void load(CompoundTag n){magic.setMana(n.contains("Mana")?Mth.clamp(n.getFloat("Mana"),0,maxMana()):maxMana());cooldowns.clear();var timers=n.getCompound("Cooldowns");for(var id:timers.getAllKeys())if(timers.getLong(id)>System.currentTimeMillis())cooldowns.put(id,timers.getLong(id));}
    public void loadoutChanged(){if(casting!=null&&!sameScroll())cancel(true);}
    private boolean sameScroll(){return castingSlot>=0&&ItemStack.isSameItemSameTags(castingScroll,entity.spellInventory.getItem(castingSlot));}
    private boolean hasTarget(){var target=entity.getTarget();return target!=null&&target.isAlive()&&entity.canAttack(target);}
    public void tick(){
        if(entity.kind()!=CharacterKind.LEONARDO||entity.level().isClientSide)return;
        if(entity.parkour.busy())return;
        if(recovery>0)recovery--;
        magic.setMana(Math.min(maxMana(),magic.getMana()+.25f));
        if(casting!=null){
            var spell=casting.getSpell();var rule=rule(spell);
            if(entity.stance()==2||!sameScroll()||!spell.isEnabled()||rule.role()==Role.ATTACK&&(!hasTarget()||!entity.getTarget().getUUID().equals(targetId)||!entity.getSensing().hasLineOfSight(entity.getTarget())||entity.distanceToSqr(entity.getTarget())>rule.range()*rule.range()||spell.shouldAIStopCasting(casting.getLevel(),entity,entity.getTarget()))){cancel(true);return;}
            if(rule.role()==Role.ATTACK)aim(entity.getTarget());
            magic.handleCastDuration();spell.onServerCastTick(entity.level(),casting.getLevel(),entity,magic);
            if(spell.getCastType()==CastType.CONTINUOUS&&magic.getCastDurationRemaining()>0&&(magic.getCastDurationRemaining()+1)%10==0)cast();
            if(magic.getCastDurationRemaining()<=0){if(spell.getCastType()!=CastType.CONTINUOUS)cast();cancel(false);}
            return;
        }
        if(entity.stance()==2||recovery>0)return;
        // Healing takes precedence, including while travelling between fights.
        if(entity.getHealth()<entity.getMaxHealth()*.7f&&choose(Role.HEAL))return;
        if(!hasTarget())return;
        if(choose(Role.BUFF))return;
        choose(Role.ATTACK);
    }
    private boolean choose(Role role){
        for(int offset=0;offset<SLOTS;offset++){
            int slot=(nextSlot+offset)%SLOTS;var stack=entity.spellInventory.getItem(slot);if(!accepts(stack))continue;
            var data=spell(stack);var rule=rule(data.getSpell());if(rule.role()!=role)continue;
            if(role==Role.ATTACK&&(entity.distanceToSqr(entity.getTarget())>rule.range()*rule.range()||!entity.getSensing().hasLineOfSight(entity.getTarget())))continue;
            if(begin(slot)){nextSlot=(slot+1)%SLOTS;return true;}
        }return false;
    }
    public boolean begin(int slot){
        if(entity.level().isClientSide||entity.kind()!=CharacterKind.LEONARDO||entity.stance()==2||casting!=null||slot<0||slot>=SLOTS)return false;
        var stack=entity.spellInventory.getItem(slot);if(!accepts(stack))return false;
        var data=spell(stack);var spell=data.getSpell();var rule=rule(spell);int level=data.getLevel();
        if(rule.role()==Role.ATTACK&&(!hasTarget()||entity.distanceToSqr(entity.getTarget())>rule.range()*rule.range()||!entity.getSensing().hasLineOfSight(entity.getTarget())))return false;
        if(cooldown(spell)>0||magic.getMana()<spell.getManaCost(level)||rule.role()==Role.ATTACK&&spell.shouldAIStopCasting(level,entity,entity.getTarget()))return false;
        if(rule.role()==Role.ATTACK)aim(entity.getTarget());
        if(!spell.checkPreCastConditions(entity.level(),level,entity,magic)){magic.resetAdditionalCastData();return false;}
        casting=data;castingSlot=slot;castingScroll=stack.copy();targetId=hasTarget()?entity.getTarget().getUUID():null;
        magic.setMana(magic.getMana()-spell.getManaCost(level));cooldowns.put(spell.getSpellId(),System.currentTimeMillis()+Math.max(20,spell.getSpellCooldown())*50L);
        magic.initiateCast(spell,level,Math.max(0,spell.getEffectiveCastTime(level,entity)),CastSource.MOB,"mainhand");
        spell.onServerPreCast(entity.level(),level,entity,magic);
        animate(spell.getCastStartAnimation(),Math.max(10,magic.getCastDuration()));
        if(spell.getCastType()==CastType.INSTANT){cast();cancel(false);}
        entity.checkpoint();return true;
    }
    public void initiate(AbstractSpell spell,int level){for(int i=0;i<SLOTS;i++){var data=spell(entity.spellInventory.getItem(i));if(data.getSpell()==spell&&data.getLevel()==level){begin(i);return;}}}
    private void cast(){var spell=casting.getSpell();spell.onCast(entity.level(),casting.getLevel(),entity,CastSource.MOB,magic);castsCompleted++;lastSpell=spell.getSpellId();}
    public void cancel(boolean cancelled){
        if(casting==null)return;
        var data=casting;casting=null;castingSlot=-1;castingScroll=ItemStack.EMPTY;targetId=null;recovery=12;
        data.getSpell().onServerCastComplete(entity.level(),data.getLevel(),entity,magic,cancelled);
        if(cancelled)CompanionNetwork.animate(entity,new CombatBridge.Strike("",1,0,0,0));else animate(data.getSpell().getCastFinishAnimation(),12);
    }
    public void clientSync(SyncedSpellData data){magic.setSyncedData(data);}
    private void animate(AnimationHolder holder,int ticks){holder.getForPlayer().ifPresent(id->CompanionNetwork.animate(entity,new CombatBridge.Strike(id.toString(),ticks,0,0,0)));}
    public void aim(LivingEntity target){
        var delta=target.getEyePosition().subtract(entity.getEyePosition());float yaw=(float)(Math.atan2(delta.z,delta.x)*180/Math.PI)-90;float pitch=(float)(-Math.atan2(delta.y,Math.sqrt(delta.x*delta.x+delta.z*delta.z))*180/Math.PI);
        entity.setYRot(yaw);entity.setYHeadRot(yaw);entity.setXRot(pitch);entity.getLookControl().setLookAt(target,180,180);
    }
    public double preferredRange(){double range=0;for(int i=0;i<SLOTS;i++){var stack=entity.spellInventory.getItem(i);if(accepts(stack)){var rule=rule(spell(stack).getSpell());if(rule.role()==Role.ATTACK)range=Math.max(range,rule.range());}}return Math.max(2,Math.min(12,range*.7));}
}
