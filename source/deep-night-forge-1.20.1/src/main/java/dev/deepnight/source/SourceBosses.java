package dev.deepnight.source;
import dev.deepnight.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
public final class SourceBosses {
public static NightMob create(EntityType<? extends NightMob> type,Level level,Species species){return switch(species){
case DAWN_CORRUPTED_OGRE -> new dev.deepnight.source.dawn.entity.CorruptedOgreEntity(type,level);
case DAWN_DRAGON_LORD -> new dev.deepnight.source.dawn.entity.DragonLordEntity(type,level);
case DAWN_ELEMENTAL_DEITY -> new dev.deepnight.source.dawn.entity.ElementalDeityEntity(type,level);
case DAWN_ENT -> new dev.deepnight.source.dawn.entity.EntEntity(type,level);
case DAWN_FIRE_DRAGON -> new dev.deepnight.source.dawn.entity.FireDragonEntity(type,level);
case DAWN_FIRE_GIANT -> new dev.deepnight.source.dawn.entity.FireGiantEntity(type,level);
case DAWN_HEROBRINE -> new dev.deepnight.source.dawn.entity.HerobrineEntity(type,level);
case DAWN_ICE_DRAGON -> new dev.deepnight.source.dawn.entity.IceDragonEntity(type,level);
case DAWN_LIGHTNING_DRAGON -> new dev.deepnight.source.dawn.entity.LightningDragonEntity(type,level);
case DAWN_MARTIAN -> new dev.deepnight.source.dawn.entity.MartianEntity(type,level);
case DAWN_MOSS_GOLEM -> new dev.deepnight.source.dawn.entity.MossGolemEntity(type,level);
case DAWN_NINE_TAILS -> new dev.deepnight.source.dawn.entity.NineTailsEntity(type,level);
case DAWN_NOTCH_BOSS -> new dev.deepnight.source.dawn.entity.NotchBossEntity(type,level);
case DAWN_SENTINEL_KNIGHT -> new dev.deepnight.source.dawn.entity.SentinelKnightEntity(type,level);
case DAWN_SKELETONLORD -> new dev.deepnight.source.dawn.entity.SkeletonlordEntity(type,level);
case MOBOSSES_DEMON_KING -> new dev.deepnight.source.mobosses.entity.custom.DemonKingEntity(type,level);
case MOBOSSES_DIVINE_DRAGON -> new dev.deepnight.source.mobosses.entity.custom.DivineDragonEntity(type,level);
case MOBOSSES_FOREST_GUARDIAN -> new dev.deepnight.source.mobosses.entity.custom.ForestGuardianEntity(type,level);
case MOBOSSES_HEROBRINE -> new dev.deepnight.source.mobosses.entity.custom.HerobrineEntity(type,level);
case MOBOSSES_NIL -> new dev.deepnight.source.mobosses.entity.custom.NilEntity(type,level);
case MOBOSSES_ROYAL_GUARDIAN -> new dev.deepnight.source.mobosses.entity.custom.RoyalGuardianEntity(type,level);
case MOBOSSES_ROYAL_KNIGHT -> new dev.deepnight.source.mobosses.entity.custom.RoyalKnightEntity(type,level);
case MOBOSSES_DEMON_KING_MINION -> new dev.deepnight.source.mobosses.entity.custom.DemonKingMinionEntity(type,level);
default -> new NightMob(type,level,species);};}
public static AttributeSupplier.Builder attributes(Species species){var builder = switch(species){
case DAWN_CORRUPTED_OGRE -> dev.deepnight.source.dawn.entity.CorruptedOgreEntity.createAttributes();
case DAWN_DRAGON_LORD -> dev.deepnight.source.dawn.entity.DragonLordEntity.createAttributes();
case DAWN_ELEMENTAL_DEITY -> dev.deepnight.source.dawn.entity.ElementalDeityEntity.createAttributes();
case DAWN_ENT -> dev.deepnight.source.dawn.entity.EntEntity.createAttributes();
case DAWN_FIRE_DRAGON -> dev.deepnight.source.dawn.entity.FireDragonEntity.createAttributes();
case DAWN_FIRE_GIANT -> dev.deepnight.source.dawn.entity.FireGiantEntity.createAttributes();
case DAWN_HEROBRINE -> dev.deepnight.source.dawn.entity.HerobrineEntity.createAttributes();
case DAWN_ICE_DRAGON -> dev.deepnight.source.dawn.entity.IceDragonEntity.createAttributes();
case DAWN_LIGHTNING_DRAGON -> dev.deepnight.source.dawn.entity.LightningDragonEntity.createAttributes();
case DAWN_MARTIAN -> dev.deepnight.source.dawn.entity.MartianEntity.createAttributes();
case DAWN_MOSS_GOLEM -> dev.deepnight.source.dawn.entity.MossGolemEntity.createAttributes();
case DAWN_NINE_TAILS -> dev.deepnight.source.dawn.entity.NineTailsEntity.createAttributes();
case DAWN_NOTCH_BOSS -> dev.deepnight.source.dawn.entity.NotchBossEntity.createAttributes();
case DAWN_SENTINEL_KNIGHT -> dev.deepnight.source.dawn.entity.SentinelKnightEntity.createAttributes();
case DAWN_SKELETONLORD -> dev.deepnight.source.dawn.entity.SkeletonlordEntity.createAttributes();
case MOBOSSES_DEMON_KING -> dev.deepnight.source.mobosses.entity.custom.DemonKingEntity.createAttributes();
case MOBOSSES_DIVINE_DRAGON -> dev.deepnight.source.mobosses.entity.custom.DivineDragonEntity.createAttributes();
case MOBOSSES_FOREST_GUARDIAN -> dev.deepnight.source.mobosses.entity.custom.ForestGuardianEntity.createAttributes();
case MOBOSSES_HEROBRINE -> dev.deepnight.source.mobosses.entity.custom.HerobrineEntity.createAttributes();
case MOBOSSES_NIL -> dev.deepnight.source.mobosses.entity.custom.NilEntity.createAttributes();
case MOBOSSES_ROYAL_GUARDIAN -> dev.deepnight.source.mobosses.entity.custom.RoyalGuardianEntity.createAttributes();
case MOBOSSES_ROYAL_KNIGHT -> dev.deepnight.source.mobosses.entity.custom.RoyalKnightEntity.createAttributes();
case MOBOSSES_DEMON_KING_MINION -> dev.deepnight.source.mobosses.entity.custom.DemonKingMinionEntity.createAttributes();
default -> NightMob.attributes(species);};
if(species.boss()||species.kind==Species.Kind.HELPER)builder.add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH,species.health);
return builder;}
public static net.minecraft.world.entity.EntityType.Builder<NightMob> builder(Species s){
var b=EntityType.Builder.<NightMob>of((type,level)->create(type,level,s),s.passive()?MobCategory.CREATURE:MobCategory.MONSTER).sized(s.width,s.height).clientTrackingRange(s.boss()?16:10);
if(!s.boss()&&!s.id.startsWith("mobosses_"))b.fireImmune();
return b;
}
}
