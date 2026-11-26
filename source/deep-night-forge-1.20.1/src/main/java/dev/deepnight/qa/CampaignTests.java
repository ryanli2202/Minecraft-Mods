package dev.deepnight.qa;

import com.mojang.authlib.GameProfile;
import dev.deepnight.*;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.gametest.*;
import java.util.*;

@GameTestHolder(DeepNight.ID)
@PrefixGameTestTemplate(false)
public final class CampaignTests {
    private static ServerPlayer player(GameTestHelper t){
        var p=FakePlayerFactory.get(t.getLevel(),new GameProfile(UUID.randomUUID(),"NightQA"));
        p.setGameMode(GameType.SURVIVAL);p.setPos(t.absoluteVec(new net.minecraft.world.phys.Vec3(3,2,3)));t.getLevel().addNewPlayer(p);return p;
    }
    @GameTest(template="empty")
    public static void progressionIsStrictPersistentAndReplaySafe(GameTestHelper t){
        CampaignData d=new CampaignData();UUID p=UUID.randomUUID(),helper=UUID.randomUUID(),boss=UUID.randomUUID();
        t.assertFalse(d.canStart(p),"Pre-dragon player can summon");
        t.assertFalse(d.begin(p,boss,"minecraft:overworld",0),"Locked player opened an encounter");
        t.assertTrue(d.unlock(p),"Dragon unlock failed");t.assertFalse(d.unlock(p),"Dragon unlock repeated");
        for(int stage=0;stage<Species.BOSS_COUNT;stage++){
            boss=UUID.randomUUID();t.assertTrue(d.begin(p,boss,"minecraft:the_end",123),"Could not start next boss");
            t.assertFalse(d.begin(p,UUID.randomUUID(),"minecraft:the_end",123),"Duplicate boss allowed");
            t.assertFalse(d.credit(p,boss,stage+1),"Skipped a boss");
            t.assertFalse(d.credit(p,UUID.randomUUID(),stage),"Forged boss UUID accepted");
            t.assertFalse(d.credit(helper,boss,stage),"Unqualified helper advanced");
            d=CampaignData.load(d.save(new CompoundTag()));
            t.assertTrue(d.active.equals(boss)&&d.player(p).stage==stage,"Save/reload lost encounter or stage");
            t.assertTrue(d.credit(p,boss,stage),"Valid defeat rejected");
            t.assertFalse(d.credit(p,boss,stage),"Same kill rewarded twice");d.clear(boss);
        }
        t.assertFalse(d.canStart(p),"Campaign restarted after completion");t.succeed();
    }
    @GameTest(template="empty")
    public static void abandoningDoesNotAdvanceAndInvalidatesOldBoss(GameTestHelper t){
        CampaignData d=new CampaignData();UUID p=UUID.randomUUID(),boss=UUID.randomUUID();d.unlock(p);d.begin(p,boss,"minecraft:overworld",0);
        d.clear(boss);t.assertTrue(d.player(p).stage==0&&d.canStart(p),"Abandon advanced or locked campaign");
        UUID retry=UUID.randomUUID();d.begin(p,retry,"minecraft:overworld",0);
        t.assertFalse(d.credit(p,boss,0),"Abandoned boss can still grant progress");
        d.clear(boss);t.assertTrue(d.active.equals(retry),"Old boss cleared new encounter");t.succeed();
    }
    @GameTest(template="empty")
    public static void dragonRewardRequiresUnlockAndSurvivesFullInventory(GameTestHelper t){
        ServerPlayer p=player(t);var d=CampaignData.get(p.server);
        CampaignEvents.deliverPending(p);t.assertFalse(CampaignEvents.hasCaller(p),"Unqualified player got tool");
        for(int i=0;i<36;i++)p.getInventory().setItem(i,new ItemStack(Items.COBBLESTONE,64));
        d.unlock(p.getUUID());CampaignEvents.deliverPending(p);
        t.assertTrue(d.player(p.getUUID()).toolPending,"Full inventory silently lost tool");
        p.getInventory().setItem(0,ItemStack.EMPTY);CampaignEvents.deliverPending(p);
        t.assertTrue(CampaignEvents.hasCaller(p)&&!d.player(p.getUUID()).toolPending,"Deferred delivery failed");
        CampaignEvents.unlock(p);long count=p.getInventory().items.stream().filter(s->s.is(DeepNight.CALLER.get())).count();
        t.assertTrue(count==1,"Repeated dragon award duplicated tool");t.succeed();
    }
    @GameTest(template="empty")
    public static void petsTameHealProtectOwnershipAndSave(GameTestHelper t){
        ServerPlayer owner=player(t),stranger=player(t);
        for(Species s:Species.values())if(s.kind==Species.Kind.PET){
            Companion pet=DeepNight.PETS.get(s).get().create(t.getLevel());
            owner.setItemInHand(InteractionHand.MAIN_HAND,new ItemStack(DeepNight.TREAT.get(),4));
            for(int i=0;i<3;i++)pet.mobInteract(owner,InteractionHand.MAIN_HAND);
            t.assertTrue(pet.isOwnedBy(owner)&&owner.getMainHandItem().getCount()==1,"Three treats did not tame "+s);
            pet.setHealth(10);pet.mobInteract(owner,InteractionHand.MAIN_HAND);t.assertTrue(pet.getHealth()==26,"Treat healing failed");
            t.assertFalse(pet.canHarm(owner),"Pet can attack owner");
            pet.setSaddled(true);pet.setOrderedToSit(true);
            Companion loaded=DeepNight.PETS.get(s).get().create(t.getLevel());CompoundTag tag=new CompoundTag();pet.saveWithoutId(tag);loaded.load(tag);
            t.assertTrue(loaded.isOwnedBy(owner)&&loaded.saddled()&&loaded.isOrderedToSit(),"Pet state did not persist");
            t.assertFalse(stranger.startRiding(loaded),"Stranger can ride pet");
            Companion sibling=DeepNight.PETS.get(s).get().create(t.getLevel());sibling.tame(owner);
            t.assertFalse(loaded.canHarm(sibling),"Same owner's pets can hurt one another");
        }t.succeed();
    }
    @GameTest(template="empty")
    public static void nativeTableBooksAndAnvilCompatibility(GameTestHelper t){
        var candidates=EnchantmentHelper.getAvailableEnchantmentResults(40,new ItemStack(Items.BOOK),false);
        for(var entry:DeepNight.ENCHANTS.entrySet()){
            Enchantment e=entry.getValue().get();
            t.assertTrue(candidates.stream().anyMatch(c->c.enchantment==e),"Missing from vanilla book enchanting pool: "+entry.getKey());
            t.assertTrue(e.isTradeable()&&e.isDiscoverable()&&!e.isTreasureOnly()&&e.isAllowedOnBooks(),"Book acquisition disabled");
            ItemStack book=EnchantedBookItem.createForEnchantment(new EnchantmentInstance(e,e.getMaxLevel()));
            t.assertTrue(EnchantmentHelper.getEnchantments(book).get(e)==e.getMaxLevel(),"Book stored wrong enchantment");
        }
        var fire=DeepNight.ENCHANTS.get(NightEnchantment.Type.SOUL_FIRE).get();var frost=DeepNight.ENCHANTS.get(NightEnchantment.Type.FROZEN_TOUCH).get();
        t.assertFalse(fire.isCompatibleWith(frost),"Conflicting elements can combine");
        t.assertTrue(fire.canEnchant(new ItemStack(DeepNight.WEAPONS.get("nightfall").get())),"New weapons cannot enchant");
        t.assertFalse(fire.canEnchant(new ItemStack(Items.DIAMOND_CHESTPLATE)),"Weapon enchantment accepts chestplate");t.succeed();
    }
    @GameTest(template="empty")
    public static void allContentRecipesAndSpawnBiomesLoad(GameTestHelper t){
        double health=200,damage=0;
        for(Species s:Species.values()){
            if(s.kind==Species.Kind.REMOVED)continue;
            LivingEntity e=s.kind==Species.Kind.PET?DeepNight.PETS.get(s).get().create(t.getLevel()):DeepNight.HOSTILES.get(s).get().create(t.getLevel());
            t.assertTrue(e!=null&&e.getMaxHealth()==(s.boss()||s.kind==Species.Kind.HELPER?s.health:Math.min(s.id.startsWith("mobosses_")?1024:1000,s.health)),"Invalid source attributes "+s);
        }
        for(Weapon w:Weapon.values())t.assertTrue(t.getLevel().getRecipeManager().byKey(DeepNight.id(w.id())).isPresent(),"Recipe missing: "+w);
        t.assertTrue(t.getLevel().getRecipeManager().getRecipes().stream().noneMatch(r->r.getResultItem(t.getLevel().registryAccess()).is(DeepNight.CALLER.get())),"Summon tool is craftable");
        var biomes=t.getLevel().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.BIOME);
        for(Species s:Species.values()){
            if(s.kind==Species.Kind.REMOVED)continue;
            var type=s.kind==Species.Kind.PET?DeepNight.PETS.get(s).get():DeepNight.HOSTILES.get(s).get();
            boolean present=biomes.stream().anyMatch(b->b.getMobSettings().getMobs(type.getCategory()).unwrap().stream().anyMatch(spawn->spawn.type==type));
            t.assertTrue(present==(!s.boss()&&s.kind!=Species.Kind.PET&&s.kind!=Species.Kind.HELPER),"Wrong natural spawning for "+s);
        }
        t.assertTrue(t.getLevel().getRecipeManager().byKey(DeepNight.id("pet_summoning_tool")).isPresent(),"Pet tool recipe missing");
        t.assertTrue(SoundBank.registeredCount()>0,"Supplied sound events were not registered");t.succeed();
    }
    @GameTest(template="empty")
    public static void petCatalogueOwnershipCooldownAndPersistentGrowth(GameTestHelper t){
        ServerPlayer p=player(t);
        for(int x=-7;x<=14;x++)for(int z=-7;z<=14;z++)t.setBlock(new BlockPos(x,1,z),Blocks.STONE);
        t.assertFalse(PetCatalogue.summon(p,DeepNight.id("ember_drake")),"Pet summoned without the tool");
        p.setItemInHand(InteractionHand.MAIN_HAND,new ItemStack(DeepNight.PET_CALLER.get()));
        t.assertFalse(PetCatalogue.summon(p,new net.minecraft.resources.ResourceLocation("minecraft:ender_dragon")),"Forged non-pet catalogue entry accepted");
        var catalogue=PetCatalogue.all(t.getLevel());
        for(Species s:Species.values())if(s.kind==Species.Kind.PET){
            t.assertTrue(catalogue.containsKey(DeepNight.id(s.id)),"Pet absent from catalogue: "+s);
            p.getPersistentData().putLong("DeepNightPetSummonAt",0);
            t.assertTrue(PetCatalogue.summon(p,DeepNight.id(s.id)),"Pet could not summon: "+s);
            Companion pet=t.getLevel().getEntitiesOfClass(Companion.class,p.getBoundingBox().inflate(20),c->c.species==s&&c.isOwnedBy(p)).get(0);
            t.assertTrue(pet.isTame()&&pet.getHealth()==pet.getMaxHealth(),"Summoned pet was not owned and healthy");
            t.assertFalse(PetCatalogue.summon(p,DeepNight.id(s.id)),"Pet tool cooldown bypassed");
            PetGrowth.grant(pet,100);t.assertTrue(PetGrowth.level(pet)==2&&pet.getMaxHealth()>s.health,"Pet level did not raise health");
            t.assertTrue(pet.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE)>s.damage,"Pet level did not raise damage");
            float health=pet.getMaxHealth();PetGrowth.apply(pet);t.assertTrue(pet.getMaxHealth()==health,"Repeated growth stacked modifiers");
            pet.setHealth(35);CompoundTag tag=new CompoundTag();pet.saveWithoutId(tag);Companion loaded=DeepNight.PETS.get(s).get().create(t.getLevel());loaded.load(tag);PetGrowth.apply(loaded);
            t.assertTrue(loaded.getHealth()==35,"Reload changed an injured pet's health");
            t.assertTrue(loaded.isOwnedBy(p)&&PetGrowth.level(loaded)==2&&loaded.getMaxHealth()==health,"Pet growth did not survive save/reload");
            for(int i=0;i<10;i++)PetGrowth.grant(loaded,5000);
            t.assertTrue(PetGrowth.level(loaded)==50&&PetGrowth.xp(loaded)==0,"Pet level cap failed");pet.discard();
        }
        t.assertTrue(catalogue.containsKey(new net.minecraft.resources.ResourceLocation("minecraft:wolf"))&&catalogue.containsKey(new net.minecraft.resources.ResourceLocation("minecraft:horse")),"Vanilla pets absent from catalogue");
        t.succeed();
    }
}
