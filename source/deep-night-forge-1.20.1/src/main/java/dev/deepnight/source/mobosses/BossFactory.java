package dev.deepnight.source.mobosses;
import dev.deepnight.*;
import dev.deepnight.source.mobosses.entity.custom.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
public final class BossFactory {
public static NightMob create(EntityType<? extends NightMob> type,Level level,Species species){return switch(species){
case MOBOSSES_DEMON_KING -> new DemonKingEntity(type,level);
case MOBOSSES_DIVINE_DRAGON -> new DivineDragonEntity(type,level);
case MOBOSSES_FOREST_GUARDIAN -> new ForestGuardianEntity(type,level);
case MOBOSSES_HEROBRINE -> new HerobrineEntity(type,level);
case MOBOSSES_NIL -> new NilEntity(type,level);
case MOBOSSES_ROYAL_GUARDIAN -> new RoyalGuardianEntity(type,level);
case MOBOSSES_ROYAL_KNIGHT -> new RoyalKnightEntity(type,level);
case MOBOSSES_DEMON_KING_MINION -> new DemonKingMinionEntity(type,level);
default -> new NightMob(type,level,species);
};}
}
