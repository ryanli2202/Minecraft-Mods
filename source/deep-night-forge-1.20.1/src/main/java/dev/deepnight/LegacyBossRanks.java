package dev.deepnight;
final class LegacyBossRanks {
static int rank(Species s,int version){return switch(s){
case DAWN_CORRUPTED_OGRE -> version==2?-1:84;
case DAWN_DRAGON_LORD -> version==2?-1:85;
case DAWN_ELEMENTAL_DEITY -> version==2?-1:86;
case DAWN_ENT -> version==2?-1:87;
case DAWN_FIRE_DRAGON -> version==2?-1:88;
case DAWN_FIRE_GIANT -> version==2?-1:89;
case DAWN_HEROBRINE -> version==2?-1:92;
case DAWN_ICE_DRAGON -> version==2?-1:93;
case DAWN_LIGHTNING_DRAGON -> version==2?-1:95;
case DAWN_MARTIAN -> version==2?-1:97;
case DAWN_MOSS_GOLEM -> version==2?-1:99;
case DAWN_NINE_TAILS -> version==2?-1:100;
case DAWN_NOTCH_BOSS -> version==2?-1:101;
case DAWN_SENTINEL_KNIGHT -> version==2?-1:105;
case DAWN_SKELETONLORD -> version==2?-1:106;
case MOBOSSES_DEMON_KING -> version==2?43:107;
case MOBOSSES_DIVINE_DRAGON -> version==2?44:108;
case MOBOSSES_FOREST_GUARDIAN -> version==2?45:109;
case MOBOSSES_HEROBRINE -> version==2?46:110;
case MOBOSSES_NIL -> version==2?47:111;
case MOBOSSES_ROYAL_GUARDIAN -> version==2?48:112;
case MOBOSSES_ROYAL_KNIGHT -> version==2?49:113;
default -> -1;};}
}
