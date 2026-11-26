package dev.travellerscompanions;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.*;

@Mod(TravellersCompanions.ID)
public final class TravellersCompanions {
    public static final String ID="travellerscompanions";
    public static final DeferredRegister<EntityType<?>> ENTITIES=DeferredRegister.create(ForgeRegistries.ENTITY_TYPES,ID);
    public static final DeferredRegister<MenuType<?>> MENUS=DeferredRegister.create(ForgeRegistries.MENU_TYPES,ID);
    public static final RegistryObject<EntityType<CompanionEntity>> COMPANION=ENTITIES.register("companion",()->EntityType.Builder.of(CompanionEntity::new,MobCategory.CREATURE).sized(.6f,1.8f).clientTrackingRange(10).updateInterval(2).build(ID+":companion"));
    public static final RegistryObject<MenuType<CompanionMenu>> EQUIPMENT=MENUS.register("equipment",()->IForgeMenuType.create((id,inv,data)->new CompanionMenu(id,inv,(CompanionEntity)inv.player.level().getEntity(data.readVarInt()))));
    public static final RegistryObject<MenuType<SpellMenu>> SPELLS=MENUS.register("spells",()->IForgeMenuType.create((id,inv,data)->new SpellMenu(id,inv,(CompanionEntity)inv.player.level().getEntity(data.readVarInt()))));
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.BooleanValue PUBLIC_CHAT, TELEPORT, FOOD;
    public static final ForgeConfigSpec.BooleanValue PARKOUR, SPA_WALK;
    public static final ForgeConfigSpec.DoubleValue DODGE_CHANCE;
    public static final ForgeConfigSpec.IntValue DODGE_COOLDOWN;
    public static final ForgeConfigSpec.ConfigValue<String> SPEAR;
    static {
        var b=new ForgeConfigSpec.Builder();
        PUBLIC_CHAT=b.comment("Broadcast combat barks to server chat. Disabled: owner only.").define("publicCombatChat",true);
        TELEPORT=b.comment("Catch up when more than 20 blocks from the owner; only safe ground.").define("teleportToOwner",true);
        FOOD=b.comment("Eat food from the backpack when injured and out of combat.").define("eatBackpackFood",true);
        PARKOUR=b.comment("With ParCool installed, fast-follow the owner, climb low ledges and occasionally dodge attacks.").define("parcoolCompanionMovement",true);
        SPA_WALK=b.comment("Use Serious Player Animations walking keyframes when installed on the client.").define("seriousPlayerWalking",true);
        DODGE_CHANCE=b.comment("Chance to roll away from an eligible attack when a safe route exists.").defineInRange("dodgeRollChance",.22,0,1);
        DODGE_COOLDOWN=b.comment("Minimum ticks between successful companion rolls (20 ticks = 1 second).").defineInRange("dodgeRollCooldownTicks",100,20,1200);
        SPEAR=b.comment("Epic Knights Addon starting spear registry ID. Missing item falls back to Epic Knights iron pike, then a trident.").define("castelanStartingSpear","magistuarmoryaddon:steel_short_spear");
        SPEC=b.build();
    }
    public TravellersCompanions(){
        var bus=FMLJavaModLoadingContext.get().getModEventBus();ENTITIES.register(bus);MENUS.register(bus);
        bus.addListener((EntityAttributeCreationEvent e)->e.put(COMPANION.get(),CompanionEntity.attributes().build()));
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON,SPEC);
        CompanionNetwork.init();
    }
    public static ResourceLocation id(String path){return new ResourceLocation(ID,path);}
}
