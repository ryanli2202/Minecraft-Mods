package dev.deepnight.qa;

import dev.deepnight.*;
import net.minecraft.gametest.framework.*;
import net.minecraft.nbt.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.gametest.*;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.*;

@GameTestHolder(DeepNight.ID) @PrefixGameTestTemplate(false)
public final class RosterTests {
    private static final Species[] ORDER={Species.DAWN_CORRUPTED_OGRE,Species.MOBOSSES_NIL,
        Species.MOBOSSES_ROYAL_GUARDIAN,Species.DAWN_MARTIAN,Species.DAWN_SKELETONLORD,
        Species.DAWN_DRAGON_LORD,Species.MOBOSSES_DEMON_KING,Species.MOBOSSES_HEROBRINE,
        Species.DAWN_NOTCH_BOSS,Species.DAWN_HEROBRINE};
    private static final Species[] DRAGONS={Species.DAWN_LIGHTNING_DRAGON,Species.DAWN_ICE_DRAGON,Species.DAWN_FIRE_DRAGON};

    @GameTest(template="empty") public static void exactRosterAndRemovedRegistrations(GameTestHelper t){
        t.assertTrue(Species.BOSS_COUNT==ORDER.length,"Wrong boss count");
        for(int i=0;i<ORDER.length;i++)t.assertTrue(Species.boss(i)==ORDER[i],"Wrong boss at stage "+i);
        for(Species s:Species.values()){
            if(s.kind==Species.Kind.REMOVED){
                t.assertFalse(ForgeRegistries.ENTITY_TYPES.containsKey(DeepNight.id(s.id)),"Removed boss remains summonable: "+s);
                t.assertFalse(DeepNight.HOSTILES.containsKey(s),"Removed boss factory registered");
            }
            if(s.kind==Species.Kind.REMOVED||Arrays.asList(DRAGONS).contains(s))
                t.assertFalse(ForgeRegistries.ITEMS.containsKey(DeepNight.id(s.id+"_sigil")),"Removed sigil registered: "+s);
        }
        t.succeed();
    }

    @GameTest(template="empty") public static void previousRosterActiveEncountersMigrateById(GameTestHelper t){
        for(Species s:Species.values())if(s.kind==Species.Kind.REMOVED||s.boss()||Arrays.asList(DRAGONS).contains(s)){
            UUID owner=UUID.randomUUID();CompoundTag root=new CompoundTag(),row=new CompoundTag();
            root.putInt("RosterVersion",3);row.putUUID("Id",owner);row.putBoolean("Unlocked",true);
            row.putInt("Stage",22);ListTag defeated=new ListTag();
            for(Species retained:ORDER)if(retained!=s)defeated.add(StringTag.valueOf(retained.id));
            row.put("Defeated",defeated);ListTag players=new ListTag();players.add(row);root.put("Players",players);
            root.putUUID("Active",UUID.randomUUID());root.putUUID("Summoner",owner);root.putString("ActiveBoss",s.id);
            root.putInt("ActiveStage",21);root.putString("EncounterMode","PROGRESSION");
            CampaignData loaded=CampaignData.load(root);
            t.assertTrue(s.boss()?loaded.active!=null&&loaded.activeStage==s.bossStage():loaded.active==null,"Incorrect active migration: "+s);
            t.assertTrue(loaded.player(owner).stage==(s.boss()?s.bossStage():Species.BOSS_COUNT),"Defeat credit lost: "+s);
        }
        t.succeed();
    }

    @GameTest(template="empty") public static void dragonPetsMigrateAndUseElementalBreath(GameTestHelper t){
        String[] elements={"lightning","frost","fire"};
        for(int i=0;i<DRAGONS.length;i++){
            Species s=DRAGONS[i];Companion pet=DeepNight.PETS.get(s).get().create(t.getLevel());
            t.assertTrue(PetCatalogue.all(t.getLevel()).containsKey(DeepNight.id(s.id)),"Dragon missing from pet tool");
            t.assertTrue(s.dragon()&&s.breathElement().equals(elements[i]),"Wrong dragon element");
            pet.getAttribute(Attributes.MAX_HEALTH).setBaseValue(6900);pet.setHealth(6900);pet.setNoAi(true);pet.setInvulnerable(true);
            CompoundTag old=new CompoundTag();pet.saveWithoutId(old);old.remove("DeepNightCompanionVersion");pet.load(old);
            t.assertTrue(pet.getMaxHealth()==120&&!pet.isNoAi()&&!pet.isInvulnerable(),"Old boss attributes survived pet migration");
            pet.setNoAi(true);pet.setNoGravity(true);pet.setPos(0,180,0);
            var target=EntityType.ZOMBIE.create(t.getLevel());target.setNoAi(true);target.setPos(5,180,0);
            target.getAttribute(Attributes.MAX_HEALTH).setBaseValue(100);target.setHealth(100);
            pet.setTarget(target);pet.tickCount=50;pet.tick();
            t.assertTrue(target.getHealth()<100,"Dragon breath caused no damage: "+s);
            if(s==Species.DAWN_FIRE_DRAGON)t.assertTrue(target.isOnFire(),"Fire breath did not ignite");
            if(s==Species.DAWN_ICE_DRAGON)t.assertTrue(target.hasEffect(net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN),"Ice breath did not slow");
            pet.discard();target.discard();
        }
        t.succeed();
    }
}

