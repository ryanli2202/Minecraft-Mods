package dev.thedeepnightnpcs.qa;

import dev.thedeepnightnpcs.DeepNightNpcs;
import dev.thedeepnightnpcs.entity.NpcEntity;
import dev.thedeepnightnpcs.quest.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.nio.file.*;
import java.util.*;

@Mod.EventBusSubscriber(modid=DeepNightNpcs.ID)
public final class ServerChecks {
    public static final StringBuilder REPORT=new StringBuilder();
    public static void check(boolean condition,String message) {if(!condition)throw new IllegalStateException(message);REPORT.append("PASS: ").append(message).append('\n');}
    public static void run(MinecraftServer server) throws Exception {
        var level=server.overworld();
        for (var type:DeepNightNpcs.allNpcs().stream().map(net.minecraftforge.registries.RegistryObject::get).toList()) {
            NpcEntity npc=type.create(level);check(npc!=null,"NPC constructs: "+type);
            check(npc.isPersistenceRequired(),"NPC persistence: "+type);
            check(!npc.checkSpawnRules(level,MobSpawnType.NATURAL),"No natural spawning: "+type);
            check(!npc.checkSpawnRules(level,MobSpawnType.CHUNK_GENERATION),"No chunk generation spawning: "+type);
            check(npc.checkSpawnRules(level,MobSpawnType.SPAWN_EGG),"Manual spawn egg allowed: "+type);
            npc.moveTo(0,-60,0); npc.tick();
            CompoundTag saved=new CompoundTag();npc.saveWithoutId(saved);NpcEntity copy=type.create(level);copy.load(saved);
            check(copy.home().equals(npc.home()),"NPC anchor saved and restored: "+type);
            if(npc.isGuard())check(npc.getMainHandItem().is(net.minecraft.world.item.Items.IRON_SWORD)&&npc.getOffhandItem().is(net.minecraft.world.item.Items.SHIELD),"Guard has sword and shield");
            npc.discard();copy.discard();
        }
        for(var biome:server.registryAccess().registryOrThrow(Registries.BIOME)) for(MobCategory category:MobCategory.values())
            for(var spawn:biome.getMobSettings().getMobs(category).unwrap())check(!ForgeRegistries.ENTITY_TYPES.getKey(spawn.type).getNamespace().equals(DeepNightNpcs.ID),"No NPC in biome spawn tables");
        HuntService.rebuild(server);var targets=HuntService.targets(server);
        check(targets.stream().anyMatch(t->t.id().equals("minecraft:zombie")),"Vanilla zombie is eligible");
        check(targets.stream().anyMatch(t->t.id().equals("minecraft:ender_dragon")),"Enemy marker includes non-MONSTER dragon");
        check(targets.stream().noneMatch(t->t.id().equals("minecraft:cow")),"Passive cow is excluded");
        check(targets.stream().noneMatch(t->t.id().startsWith(DeepNightNpcs.ID+":")),"Friendly NPCs excluded");
        check(targets.stream().anyMatch(t->t.id().equals("npcqa:odd_hostile")),"Other-mod Enemy with CREATURE category is eligible");
        DeepNightNpcs.INCLUDE.set(List.of("minecraft:cow"));DeepNightNpcs.EXCLUDE.set(List.of("minecraft:zombie"));HuntService.rebuild(server);
        check(HuntService.targets(server).stream().anyMatch(t->t.id().equals("minecraft:cow")),"Config includes unconventional targets");
        check(HuntService.targets(server).stream().noneMatch(t->t.id().equals("minecraft:zombie")),"Config excludes targets");
        DeepNightNpcs.INCLUDE.set(List.of());DeepNightNpcs.EXCLUDE.set(List.of());HuntService.rebuild(server);
        HuntData data=new HuntData();UUID first=UUID.randomUUID(),second=UUID.randomUUID();var r=data.player(first);
        r.active=new HuntData.Contract(UUID.randomUUID(),"minecraft:zombie",5,12,85,"Test");r.kills=3;r.completed=8;r.offers.add(r.active);
        var restored=HuntData.load(data.save(new CompoundTag()));check(restored.player(first).kills==3&&restored.player(first).completed==8&&restored.player(first).active.equals(r.active),"Quest progress and rewards survive NBT round trip");
        check(restored.player(second).active==null,"Player records are isolated");
        JorgeChecks.catalogue();
        Files.createDirectories(Path.of("../verification"));
        Files.writeString(Path.of("../verification/targets.txt"),String.join("\n",HuntService.targets(server).stream().map(HuntService.Target::id).toList()));
    }
    @SubscribeEvent public static void started(ServerStartedEvent event) {
        if(!Boolean.getBoolean("thedeepnightnpcs.serverTest"))return;
        try {run(event.getServer());Files.writeString(Path.of("../verification/dedicated-server.txt"),REPORT.toString());}
        catch(Throwable error){DeepNightNpcs.LOG.error("NPC server QA failed",error);try{Files.createDirectories(Path.of("../verification"));Files.writeString(Path.of("../verification/server-FAILED.txt"),error.toString());}catch(Exception ignored){}}
        event.getServer().halt(false);
    }
    @Mod.EventBusSubscriber(modid=DeepNightNpcs.ID,bus=Mod.EventBusSubscriber.Bus.MOD)
    public static final class Fixtures {
        static EntityType<Zombie> odd;
        @SubscribeEvent public static void register(RegisterEvent event) {
            if(!Boolean.getBoolean("thedeepnightnpcs.smokeTest")&&!Boolean.getBoolean("thedeepnightnpcs.serverTest")&&!Boolean.getBoolean("thedeepnightnpcs.jorgeTest"))return;
            event.register(ForgeRegistries.Keys.ENTITY_TYPES, helper->{odd=EntityType.Builder.<Zombie>of(Zombie::new,MobCategory.CREATURE).build("npcqa:odd_hostile");helper.register(new ResourceLocation("npcqa","odd_hostile"),odd);});
            event.register(ForgeRegistries.Keys.ITEMS,helper->helper.register(new ResourceLocation("npcqa","alloy_ingot"),new net.minecraft.world.item.Item(new net.minecraft.world.item.Item.Properties())));
            event.register(ForgeRegistries.Keys.ENCHANTMENTS,helper->helper.register(new ResourceLocation("npcqa","inscription"),new net.minecraft.world.item.enchantment.Enchantment(net.minecraft.world.item.enchantment.Enchantment.Rarity.UNCOMMON,net.minecraft.world.item.enchantment.EnchantmentCategory.DIGGER,new EquipmentSlot[]{EquipmentSlot.MAINHAND}){@Override public int getMaxLevel(){return 3;}}));
        }
        @SubscribeEvent public static void attributes(EntityAttributeCreationEvent event){if(odd!=null)event.put(odd,Zombie.createAttributes().build());}
    }
}
