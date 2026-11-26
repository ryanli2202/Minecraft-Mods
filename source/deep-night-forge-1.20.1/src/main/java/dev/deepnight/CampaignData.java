package dev.deepnight;

import net.minecraft.nbt.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import java.util.*;

/** Server authority lives in overworld SavedData, never in an item or client packet. */
public final class CampaignData extends SavedData {
    public enum EncounterMode { PROGRESSION, REMATCH, TEST }
    public static final class Progress {
        public boolean unlocked, toolPending;
        public int stage;
        public final Set<String> defeated=new HashSet<>();
    }
    private final Map<UUID,Progress> players=new HashMap<>();
    public UUID active, summoner;
    public int activeStage;
    public EncounterMode activeMode=EncounterMode.PROGRESSION;
    public String dimension="minecraft:overworld";
    public long position;
    public static CampaignData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(CampaignData::load,CampaignData::new,"deepnight_campaign");
    }
    public Progress player(UUID id) { return players.computeIfAbsent(id,k->new Progress()); }
    public boolean unlock(UUID id) {
        Progress p=player(id); if(p.unlocked) return false;
        p.unlocked=true; p.toolPending=true; setDirty(); return true;
    }
    public boolean canStart(UUID id) { Progress p=player(id); return p.unlocked && p.stage<Species.BOSS_COUNT && active==null; }
    public boolean begin(UUID id, UUID boss, String dimension, long pos) {
        return beginSelected(id,boss,player(id).stage,false,dimension,pos);
    }
    public boolean canSummon(UUID id,int stage,boolean creative) {
        Progress p=player(id);
        return active==null && p.unlocked && stage>=0 && stage<Species.BOSS_COUNT
            && (stage==p.stage || p.defeated.contains(Species.boss(stage).id) || creative);
    }
    public boolean beginSelected(UUID id,UUID boss,int stage,boolean creative,String dimension,long pos) {
        if(!canSummon(id,stage,creative))return false;
        Progress p=player(id);
        activeMode=p.defeated.contains(Species.boss(stage).id)?EncounterMode.REMATCH:
            stage==p.stage?EncounterMode.PROGRESSION:EncounterMode.TEST;
        active=boss; summoner=id; activeStage=stage; this.dimension=dimension; position=pos; setDirty(); return true;
    }
    public boolean credit(UUID player, UUID boss, int stage) {
        Progress p=player(player);
        if(activeMode!=EncounterMode.PROGRESSION||!Objects.equals(active,boss)||stage!=activeStage||!p.unlocked||p.stage!=stage) return false;
        p.defeated.add(Species.boss(stage).id);
        while(p.stage<Species.BOSS_COUNT&&p.defeated.contains(Species.boss(p.stage).id))p.stage++;
        setDirty(); return true;
    }
    public void clear(UUID boss) {
        if(Objects.equals(active,boss)) {active=null; summoner=null; activeMode=EncounterMode.PROGRESSION; setDirty();}
    }
    public static CampaignData load(CompoundTag tag) {
        CampaignData data=new CampaignData();
        int version=tag.getInt("RosterVersion");
        ListTag entries=tag.getList("Players",Tag.TAG_COMPOUND);
        for(int i=0;i<entries.size();i++) {
            CompoundTag row=entries.getCompound(i); if(!row.hasUUID("Id")) continue;
            Progress p=data.player(row.getUUID("Id")); p.unlocked=row.getBoolean("Unlocked"); p.toolPending=row.getBoolean("ToolPending");
            int old=row.getInt("Stage");
            if(version>=3){
                for(Tag id:row.getList("Defeated",Tag.TAG_STRING))p.defeated.add(id.getAsString());
            }else for(int stage=0;stage<Species.BOSS_COUNT;stage++){
                Species boss=Species.boss(stage);int rank=LegacyBossRanks.rank(boss,version);
                if(rank>=0&&rank<old)p.defeated.add(boss.id);
            }
            while(p.stage<Species.BOSS_COUNT&&p.defeated.contains(Species.boss(p.stage).id))p.stage++;
        }
        int oldActive=tag.getInt("ActiveStage");
        int active=-1;
        for(int stage=0;stage<Species.BOSS_COUNT;stage++)
            if(version>=3?Species.boss(stage).id.equals(tag.getString("ActiveBoss")):LegacyBossRanks.rank(Species.boss(stage),version)==oldActive)active=stage;
        EncounterMode mode=EncounterMode.PROGRESSION;
        try {mode=EncounterMode.valueOf(tag.getString("EncounterMode"));}catch(IllegalArgumentException ignored){}
        if(active>=0&&tag.hasUUID("Active") && tag.hasUUID("Summoner") && data.validSavedEncounter(tag.getUUID("Summoner"),active,mode)) {
            data.active=tag.getUUID("Active"); data.summoner=tag.getUUID("Summoner");
            data.activeStage=active;data.activeMode=mode;
            data.dimension=tag.getString("Dimension"); data.position=tag.getLong("Position");
        }
        return data;
    }
    private boolean validSavedEncounter(UUID owner,int stage,EncounterMode mode){
        Progress p=player(owner);
        return p.unlocked && switch(mode){
            case PROGRESSION -> p.stage==stage;
            case REMATCH -> p.defeated.contains(Species.boss(stage).id);
            case TEST -> true;
        };
    }
    @Override public CompoundTag save(CompoundTag tag) {
        tag.putInt("RosterVersion",3);
        ListTag entries=new ListTag();
        players.forEach((id,p)->{CompoundTag row=new CompoundTag();row.putUUID("Id",id);row.putBoolean("Unlocked",p.unlocked);row.putBoolean("ToolPending",p.toolPending);row.putInt("Stage",p.stage);
            ListTag defeated=new ListTag();p.defeated.stream().sorted().forEach(b->defeated.add(StringTag.valueOf(b)));row.put("Defeated",defeated);entries.add(row);});
        tag.put("Players",entries);
        if(active!=null) {tag.putUUID("Active",active);tag.putUUID("Summoner",summoner);tag.putInt("ActiveStage",activeStage);tag.putString("ActiveBoss",Species.boss(activeStage).id);tag.putString("EncounterMode",activeMode.name());tag.putString("Dimension",dimension);tag.putLong("Position",position);}
        return tag;
    }
}
