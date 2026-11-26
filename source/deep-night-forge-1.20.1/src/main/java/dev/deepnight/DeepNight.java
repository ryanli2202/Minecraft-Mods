package dev.deepnight;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.entity.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.*;
import software.bernie.geckolib.GeckoLib;
import java.util.*;

@Mod(DeepNight.ID)
public final class DeepNight {
    public static final String ID="deepnight";
    public static final DeferredRegister<Item> ITEMS=DeferredRegister.create(ForgeRegistries.ITEMS,ID);
    public static final DeferredRegister<EntityType<?>> ENTITIES=DeferredRegister.create(ForgeRegistries.ENTITY_TYPES,ID);
    public static final DeferredRegister<Enchantment> ENCHANTMENTS=DeferredRegister.create(ForgeRegistries.ENCHANTMENTS,ID);
    public static final DeferredRegister<CreativeModeTab> TABS=DeferredRegister.create(Registries.CREATIVE_MODE_TAB,ID);
    public static final Map<Species,RegistryObject<EntityType<NightMob>>> HOSTILES=new EnumMap<>(Species.class);
    public static final Map<Species,RegistryObject<EntityType<Companion>>> PETS=new EnumMap<>(Species.class);
    public static final Map<Species,RegistryObject<Item>> SIGILS=new EnumMap<>(Species.class);
    public static final Map<String,RegistryObject<Item>> WEAPONS=new LinkedHashMap<>();
    public static final Map<NightEnchantment.Type,RegistryObject<Enchantment>> ENCHANTS=new EnumMap<>(NightEnchantment.Type.class);
    public static final RegistryObject<Item> CALLER=ITEMS.register("boss_summon_tool",SummonTool::new);
    public static final RegistryObject<Item> TREAT=ITEMS.register("bonding_treat",()->new Item(new Item.Properties()));
    public static final RegistryObject<Item> ESSENCE=ITEMS.register("night_essence",()->new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> GUIDE=ITEMS.register("field_guide",GuideItem::new);
    public static final RegistryObject<Item> PET_CALLER=ITEMS.register("pet_summoning_tool",PetSummoningTool::new);
    static {
        for(Species s:Species.values()) {
            if(s.kind==Species.Kind.REMOVED)continue;
            if(s.kind==Species.Kind.PET) {
                var type=ENTITIES.register(s.id,()->EntityType.Builder.<Companion>of((t,l)->new Companion(t,l,s),MobCategory.CREATURE)
                    .sized(s.width,s.height).clientTrackingRange(12).build(ID+":"+s.id));
                PETS.put(s,type);
                ITEMS.register(s.id+"_spawn_egg",()->new ForgeSpawnEggItem(type,0x253044,0x70cbd3,new Item.Properties()));
            } else {
                var type=ENTITIES.register(s.id,()->dev.deepnight.source.SourceBosses.builder(s).build(ID+":"+s.id));
                HOSTILES.put(s,type);
                if(s.boss()) SIGILS.put(s,ITEMS.register(s.id+"_sigil",()->new Item(new Item.Properties().rarity(Rarity.EPIC).fireResistant())));
                else if(s.kind!=Species.Kind.HELPER) ITEMS.register(s.id+"_spawn_egg",()->new ForgeSpawnEggItem(type,0x171424,0xa15174,new Item.Properties()));
            }
        }
        for(Weapon weapon:Weapon.values()) {
            WEAPONS.put(weapon.id(),ITEMS.register(weapon.id(),()->new NightWeapon(weapon)));
        }
        for(var t:NightEnchantment.Type.values()) ENCHANTS.put(t,ENCHANTMENTS.register(t.id(),()->new NightEnchantment(t)));
        TABS.register("deep_night",()->CreativeModeTab.builder().title(Component.literal("The Deep Night"))
            .icon(()->CALLER.get().getDefaultInstance()).displayItems((p,o)->{
                ITEMS.getEntries().forEach(i->o.accept(i.get()));
                ENCHANTS.values().forEach(e->o.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(e.get(),e.get().getMaxLevel()))));
            }).build());
    }
    public DeepNight() {
        GeckoLib.initialize();
        BossBalance.raiseHealthLimit();
        IEventBus bus=FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus); ENTITIES.register(bus); ENCHANTMENTS.register(bus); TABS.register(bus);
        SoundBank.REGISTRY.register(bus);PetNetwork.register();BossNetwork.register();
        dev.deepnight.source.SourceStun.EFFECTS.register(bus);
        dev.deepnight.source.mobosses.MoBosses.register(bus);
        dev.deepnight.source.dawn.DawnRuntime.register(bus);
        bus.addListener(this::attributes); bus.addListener(this::placements);
    }
    private void attributes(EntityAttributeCreationEvent e) {
        HOSTILES.forEach((s,t)->e.put(t.get(),dev.deepnight.source.SourceBosses.attributes(s).build()));
        PETS.forEach((s,t)->e.put(t.get(),Companion.attributes(s).build()));
    }
    private void placements(SpawnPlacementRegisterEvent e) {
        HOSTILES.forEach((s,t)->{if(!s.boss()&&s.kind!=Species.Kind.HELPER) e.register(t.get(),s.aquatic?SpawnPlacements.Type.IN_WATER:SpawnPlacements.Type.ON_GROUND,Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            (type,level,reason,pos,random)->s.aquatic?level.getFluidState(pos).is(net.minecraft.tags.FluidTags.WATER):s.passive()?level.getBlockState(pos.below()).isSolid()&&level.getRawBrightness(pos,0)>8:NightMob.checkMonsterSpawnRules(type,level,reason,pos,random),SpawnPlacementRegisterEvent.Operation.REPLACE);});
    }
    public static ResourceLocation id(String path) { return new ResourceLocation(ID,path); }
}
