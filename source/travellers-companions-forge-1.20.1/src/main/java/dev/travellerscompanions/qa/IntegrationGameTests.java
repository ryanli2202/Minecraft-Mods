package dev.travellerscompanions.qa;

import dev.travellerscompanions.*;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import java.util.*;
import net.minecraft.gametest.framework.*;
import net.minecraft.nbt.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.phys.*;
import net.minecraftforge.gametest.*;
import net.minecraftforge.registries.ForgeRegistries;

@GameTestHolder(TravellersCompanions.ID)
@PrefixGameTestTemplate(false)
public final class IntegrationGameTests {
    private static CompanionEntity companion(GameTestHelper t,CharacterKind kind){
        // GameTest's fake players bypass login, where Iron normally loads its spell configuration.
        io.redspace.ironsspellbooks.api.config.SpellConfigManager.onDatapackSync(new net.minecraftforge.event.OnDatapackSyncEvent(t.getLevel().getServer().getPlayerList(),null));
        return CompanionGameTests.summon(t,CompanionGameTests.player(t),kind.ordinal());
    }
    private static LivingEntity enemy(GameTestHelper t,CompanionEntity e,double distance){var mob=t.spawn(EntityType.HUSK,7,2,7);mob.moveTo(e.position().add(distance,0,0));mob.setNoAi(true);mob.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1000);mob.setHealth(1000);e.spells().aim(mob);return mob;}
    @GameTest(template="empty") public static void newStartersUseExactModItemsAndIndependentSpells(GameTestHelper t){
        var p=CompanionGameTests.player(t);String[] ids={"irons_spellbooks:graybeard_staff","simplyswords:iron_katana","common_weapons:test"};
        for(int i=0;i<3;i++){var kind=CharacterKind.values()[5+i];var e=CompanionGameTests.summon(t,p,kind.ordinal());t.assertTrue(ForgeRegistries.ITEMS.getKey(e.getMainHandItem().getItem()).toString().equals(ids[i]),"Wrong weapon for "+kind);t.assertTrue(e.kind()==kind,"Wrong identity");
            if(kind==CharacterKind.LEONARDO)for(int slot=0;slot<3;slot++)t.assertTrue(CompanionSpells.accepts(e.spellInventory.getItem(slot)),"Missing usable starter spell");
            var saved=new CompoundTag();e.saveWithoutId(saved);var clone=TravellersCompanions.COMPANION.get().create(t.getLevel());clone.load(saved);t.assertTrue(clone.kind()==kind,"Entity save lost new identity");
            for(int tier:new int[]{1,3,8,15})t.assertFalse(kind.dialogue(tier,3).isEmpty(),"Missing dialogue");CompanionService.dismiss(p);
        }t.succeed();
    }
    @GameTest(template="empty") public static void fiveCompanionSavesGainThreeFreshEntries(GameTestHelper t){
        var old=new CompoundTag();old.putInt("Active",4);UUID lease=UUID.randomUUID();old.putUUID("Entity",lease);var entries=new ListTag();for(int i=0;i<5;i++){var entry=new CompanionData.Entry();entry.level=i+3;entry.readyAt=1000+i;entry.snapshot.putString("Marker","saved-"+i);entries.add(entry.save());}old.put("Entries",entries);
        var loaded=CompanionData.Roster.load(old);t.assertTrue(loaded.entries.length==8&&loaded.party.size()==1&&lease.equals(loaded.party.get(4)),"Migration changed Mara's lease");
        for(int i=0;i<5;i++)t.assertTrue(loaded.entries[i].level==i+3&&loaded.entries[i].readyAt==1000+i&&loaded.entries[i].snapshot.getString("Marker").equals("saved-"+i),"Old data changed");
        for(int i=5;i<8;i++)t.assertTrue(!loaded.entries[i].initialized&&loaded.entries[i].level==1,"New character inherited another character's progress");t.succeed();
    }
    @GameTest(template="empty") public static void collectedScrollsTransferWithoutDuplicationAndRejectUnsupportedItems(GameTestHelper t){
        var e=companion(t,CharacterKind.LEONARDO);var p=(net.minecraft.server.level.ServerPlayer)e.getOwner();var menu=new SpellMenu(2,p.getInventory(),e);
        t.assertFalse(menu.getSlot(0).mayPlace(new ItemStack(Items.DIAMOND)),"Non-scroll accepted");t.assertFalse(menu.getSlot(0).mayPlace(CompanionSpells.scroll("irons_spellbooks:recall",1)),"Player-only spell accepted");
        t.assertTrue(menu.stillValid(p),"Owner cannot access spells");t.assertFalse(menu.stillValid(CompanionGameTests.player(t)),"Stranger accessed spells");
        var scroll=CompanionSpells.scroll("irons_spellbooks:icicle",2);t.assertTrue(CompanionSpells.accepts(scroll),"Collected scroll rejected: "+scroll.getTag()+" / "+CompanionSpells.spell(scroll).getLevel());var original=scroll.copy();p.getInventory().setItem(9,scroll);menu.quickMoveStack(p,6);t.assertTrue(p.getInventory().getItem(9).isEmpty()&&e.spellInventory.getItem(3).getCount()==1&&CompanionSpells.spell(e.spellInventory.getItem(3)).getLevel()==2,"Collected scroll was not moved intact: source="+p.getInventory().getItem(9)+" / destination="+e.spellInventory.getItem(3).getTag()+" / level="+CompanionSpells.spell(e.spellInventory.getItem(3)).getLevel());
        menu.quickMoveStack(p,3);t.assertTrue(e.spellInventory.getItem(3).isEmpty(),"Unequipping did not remove spell");int count=0;for(int i=0;i<p.getInventory().getContainerSize();i++)if(ItemStack.isSameItemSameTags(original,p.getInventory().getItem(i)))count+=p.getInventory().getItem(i).getCount();t.assertTrue(count==1,"Scroll duplicated or lost");t.succeed();
    }
    @GameTest(template="empty",timeoutTicks=240) public static void leonardoUsesRealSpellProjectileToHurtMonster(GameTestHelper t){
        var e=companion(t,CharacterKind.LEONARDO);e.setHolding(true);e.spellInventory.clearContent();e.spellInventory.setItem(0,CompanionSpells.scroll("irons_spellbooks:magic_missile",1));var target=enemy(t,e,5);e.setTarget(target);
        t.succeedWhen(()->{t.assertTrue(e.spells().castsCompleted>0&&target.getHealth()<1000,"Real spell: casts="+e.spells().castsCompleted+" / hp="+target.getHealth()+" / target="+e.getTarget()+" / pos="+e.position()+" / victim="+target.position()+" / removed="+e.isRemoved());t.assertTrue(e.spellInventory.getItem(0).getCount()==1,"Casting consumed equipped scroll");t.assertTrue(e.spells().lastSpell.equals("irons_spellbooks:magic_missile"),"Wrong spell used");});
    }
    @GameTest(template="empty") public static void everySupportedScrollCompletesItsIronSpellLifecycle(GameTestHelper t){
        var e=companion(t,CharacterKind.LEONARDO);e.setNoAi(true);var target=enemy(t,e,2);target.getAttribute(Attributes.ARMOR).setBaseValue(10);e.setTarget(target);e.spellInventory.clearContent();
        for(String id:CompanionSpells.supportedSpells()){
            try{
                target.setHealth(1000);e.setHealth(e.getMaxHealth()/2);e.getMagicData().setMana(1000);e.spellInventory.setItem(0,CompanionSpells.scroll(id,1));int before=e.spells().castsCompleted;
                t.assertTrue(e.spells().begin(0),"Cannot start "+id+" / accepted="+CompanionSpells.accepts(e.spellInventory.getItem(0))+" / target="+e.getTarget()+" / hp="+target.getHealth()+" / eligible="+e.canAttack(target)+" / LOS="+e.getSensing().hasLineOfSight(target)+" / casting="+e.isCasting()+" / distance="+e.distanceTo(target));int timeout=e.getMagicData().getCastDuration()+2;while(e.isCasting()&&timeout-->0)e.spells().tick();
                t.assertTrue(!e.isCasting()&&e.spells().castsCompleted>before,"Spell did not finish: "+id);t.assertTrue(e.spellInventory.getItem(0).getCount()==1,"Spell consumed scroll: "+id);e.removeAllEffects();
            }catch(Exception failure){throw new IllegalStateException("Iron spell integration failed for "+id,failure);}
        }t.succeed();
    }
    @GameTest(template="empty") public static void spellLoadoutManaAndCooldownPersistAndCannotBeResetBySwapping(GameTestHelper t){
        var e=companion(t,CharacterKind.LEONARDO);var p=(net.minecraft.server.level.ServerPlayer)e.getOwner();e.spellInventory.clearContent();var scroll=CompanionSpells.scroll("irons_spellbooks:ray_of_frost",2);e.spellInventory.setItem(0,scroll);e.setTarget(enemy(t,e,4));t.assertTrue(e.spells().begin(0),"Spell did not cast: accepted="+CompanionSpells.accepts(scroll)+" / "+scroll.getTag()+" / mana="+e.getMagicData().getMana()+" / cost="+CompanionSpells.spell(scroll).getSpell().getManaCost(2)+" / target="+e.getTarget()+" / LOS="+e.getSensing().hasLineOfSight(e.getTarget()));float mana=e.getMagicData().getMana();
        e.spellInventory.setItem(0,ItemStack.EMPTY);e.spellInventory.setItem(1,scroll);t.assertFalse(e.spells().begin(1),"Moving slots reset cooldown");CompanionService.dismiss(p);var again=CompanionGameTests.summon(t,p,CharacterKind.LEONARDO.ordinal());
        t.assertTrue(again.spellInventory.getItem(0).isEmpty()&&CompanionSpells.spell(again.spellInventory.getItem(1)).getLevel()==2,"Loadout replaced by starters");t.assertTrue(again.getMagicData().getMana()==mana&&again.spells().cooldown(SpellRegistry.getSpell("irons_spellbooks:ray_of_frost"))>0,"Resummon reset mana or cooldown");t.succeed();
    }
    @GameTest(template="empty",timeoutTicks=80) public static void passiveLeonardoNeverCastsAndRemovedScrollInterruptsChannel(GameTestHelper t){
        var e=companion(t,CharacterKind.LEONARDO);e.setTarget(enemy(t,e,4));e.spellInventory.clearContent();e.spellInventory.setItem(0,CompanionSpells.scroll("irons_spellbooks:fire_breath",1));e.getMagicData().setMana(1000);t.assertTrue(e.spells().begin(0)&&e.isCasting(),"Channel did not start");e.spellInventory.setItem(0,ItemStack.EMPTY);t.assertFalse(e.isCasting(),"Removed spell continued casting");e.spellInventory.setItem(0,CompanionSpells.scroll("irons_spellbooks:magic_missile",1));e.setStance(2);int before=e.spells().castsCompleted;t.runAfterDelay(40,()->{t.assertTrue(e.spells().castsCompleted==before&&!e.isCasting(),"Passive Leonardo cast a spell");t.succeed();});
    }
    @GameTest(template="empty") public static void yurikoUsesBetterCombatKatanaMoves(GameTestHelper t){var e=companion(t,CharacterKind.YURIKO);var attributes=net.bettercombat.logic.WeaponRegistry.getAttributes(e.getMainHandItem());t.assertTrue(attributes!=null&&"katana".equals(attributes.category()),"Katana Better Combat attributes missing");var strike=CombatBridge.strike(e,0);t.assertTrue(strike.animation().startsWith("bettercombat:")&&strike.range()>2,"Katana combo not integrated");t.succeed();}
    @GameTest(template="empty") public static void yurikoParriesFrontalMeleeButHasRecoveryAndRearVulnerability(GameTestHelper t){
        var e=companion(t,CharacterKind.YURIKO);var front=enemy(t,e,2);float health=e.getHealth();e.hurt(t.getLevel().damageSources().mobAttack(front),5);t.assertTrue(e.getHealth()==health&&e.guard.blocks==1&&e.guarding(),"Front attack not blocked");
        for(int i=0;i<9;i++)e.guard.tick();e.invulnerableTime=0;e.hurt(t.getLevel().damageSources().mobAttack(front),5);t.assertTrue(e.getHealth()<health,"Recovery window grants invulnerability");
        for(int i=0;i<30;i++)e.guard.tick();front.moveTo(e.position().add(-2,0,0));e.invulnerableTime=0;health=e.getHealth();e.hurt(t.getLevel().damageSources().mobAttack(front),2);t.assertTrue(e.getHealth()<health,"Rear attack was incorrectly blocked");t.succeed();
    }
    @GameTest(template="empty",timeoutTicks=100) public static void yurikoReflectsActualEnemyArrowBackIntoShooter(GameTestHelper t){
        var e=companion(t,CharacterKind.YURIKO);e.setNoAi(true);e.setHolding(true);var target=enemy(t,e,5);float health=e.getHealth();var arrow=new Arrow(t.getLevel(),target);arrow.setPos(e.getEyePosition().add(3,0,0));arrow.shoot(-1,0,0,1.5f,0);t.getLevel().addFreshEntity(arrow);
        t.succeedWhen(()->{t.assertTrue(e.guard.deflections>0&&arrow.getOwner()==e&&e.getHealth()==health,"Enemy arrow was not deflected");t.assertTrue(target.getHealth()<1000,"Reflected arrow did not hit shooter");});
    }
    @GameTest(template="empty") public static void yurikoReflectsFireballAndPassiveStanceDisablesGuard(GameTestHelper t){
        var e=companion(t,CharacterKind.YURIKO);var target=enemy(t,e,4);var fireball=new SmallFireball(t.getLevel(),target,-1,0,0);fireball.setPos(e.getEyePosition().add(1,0,0));fireball.setDeltaMovement(-1,0,0);var event=new net.minecraftforge.event.entity.ProjectileImpactEvent(fireball,new EntityHitResult(e));net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
        t.assertTrue(event.isCanceled()&&fireball.getOwner()==e&&fireball.xPower>0&&fireball.getDeltaMovement().x>0,"Fireball not redirected");e.setStance(2);e.guard.tick();float health=e.getHealth();e.hurt(t.getLevel().damageSources().mobAttack(target),2);t.assertTrue(e.getHealth()<health,"Passive stance still parries");t.succeed();
    }
    @GameTest(template="empty",timeoutTicks=260) public static void yushiroUsesBasicAndSpecialAndNeverHitsNeutralBystanders(GameTestHelper t){
        var e=companion(t,CharacterKind.YUSHIRO);e.setHolding(true);var target=enemy(t,e,2);target.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1);e.setTarget(target);var cow=t.spawn(EntityType.COW,7,2,7);cow.moveTo(e.position().add(2,0,.5));cow.setNoAi(true);
        t.succeedWhen(()->{t.assertTrue(e.katana.basicAttacks>=2&&e.katana.specialAttacks>=1&&e.katana.hits>=3&&target.getHealth()<975,"Big Katana basic/special hits did not execute");t.assertTrue(cow.getHealth()==cow.getMaxHealth(),"Special slash damaged neutral bystander");});
    }
}
