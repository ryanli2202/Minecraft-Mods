package dev.ashenquests;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.*;

public final class QuestEngine {
    public static final int LOCKED=0, ACTIVE=1, READY=2, CLAIMED=3, COOLDOWN=4, UNAVAILABLE=5, OFFERED=6;
    public record View(String id, int status, int[] progress, int claims, long remaining, String reason, int choice) {}
    public static CompoundTag journal(ServerPlayer p) { return JournalData.get(p.server).player(p.getUUID()); }
    public static CompoundTag state(ServerPlayer p, String id) { return journal(p).getCompound("quests").getCompound(id); }
    public static int claims(ServerPlayer p, String id) { return state(p,id).getInt("claims"); }
    public static long now(ServerPlayer p) { return p.server.overworld().getGameTime(); }
    public static void count(ServerPlayer p, String type, String target, int amount) {
        var data=JournalData.get(p.server); var totals=JournalData.child(data.player(p.getUUID()),"totals");
        String key=type+"/"+target;
        totals.putInt(key,(int)Math.min(Integer.MAX_VALUE,(long)totals.getInt(key)+amount)); data.setDirty();
    }
    public static int total(ServerPlayer p, Quest.Objective o) {
        var id=ResourceLocation.tryParse(o.target());
        return switch(o.type()) {
            case "collect","submit" -> inventoryCount(p, o.target());
            case "kill" -> ForgeRegistries.ENTITY_TYPES.containsKey(id) ? p.getStats().getValue(Stats.ENTITY_KILLED.get(ForgeRegistries.ENTITY_TYPES.getValue(id))) : 0;
            case "achieve" -> { var a=p.server.getAdvancements().getAdvancement(id); yield a!=null&&p.getAdvancements().getOrStartProgress(a).isDone()?1:0; }
            case "effect" -> ForgeRegistries.MOB_EFFECTS.containsKey(id)&&p.hasEffect(ForgeRegistries.MOB_EFFECTS.getValue(id))?1:0;
            case "field" -> 0;
            default -> journal(p).getCompound("totals").getInt(o.type()+"/"+o.target());
        };
    }
    private static int inventoryCount(ServerPlayer p,String target) {
        var id=ResourceLocation.tryParse(target); if (!ForgeRegistries.ITEMS.containsKey(id)) return 0;
        Item item=ForgeRegistries.ITEMS.getValue(id); int n=0;
        for(int i=0;i<p.getInventory().getContainerSize();i++) if(p.getInventory().getItem(i).is(item)) n+=p.getInventory().getItem(i).getCount();
        return n;
    }
    public static boolean cumulative(Quest.Objective o) { return Set.of("kill","craft","smelt","interact","tame","fish").contains(o.type()); }
    public static String missing(ServerPlayer p,Quest q,Set<String> visited) {
        if(!visited.add(q.id())) return "";
        // Progression keys must never be silently skipped like optional bonus loot.
        for(var reward:q.rewards()) if(reward.item().startsWith("endrem:")
                && !ForgeRegistries.ITEMS.containsKey(new ResourceLocation(reward.item())))
            return "Missing required eye reward: "+reward.item()+" (install End Remastered)";
        for(var o:q.objectives()) {
            var id=ResourceLocation.tryParse(o.target());
            boolean valid=switch(o.type()) {
                case "collect","submit","craft","smelt","fish" -> ForgeRegistries.ITEMS.containsKey(id);
                case "kill","interact","tame","talk" -> ForgeRegistries.ENTITY_TYPES.containsKey(id);
                case "effect" -> ForgeRegistries.MOB_EFFECTS.containsKey(id);
                case "achieve" -> p.server.getAdvancements().getAdvancement(id)!=null;
                case "biome" -> p.server.registryAccess().registryOrThrow(Registries.BIOME).containsKey(id);
                case "visit" -> p.server.levelKeys().stream().anyMatch(k->k.location().equals(id));
                default -> true;
            };
            if(!valid) return "Missing "+o.type()+" target: "+o.target();
        }
        for(String dep:q.requires()) if(claims(p,dep)==0) {
            String m=missing(p,Campaign.quests.get(dep),visited);
            if(!m.isEmpty()) return m.startsWith("Prerequisite needs ")?m:"Prerequisite needs "+m;
        }
        return "";
    }
    public static View view(ServerPlayer p,Quest q) {
        var s=state(p,q.id()); int n=s.getInt("claims"); int[] progress=new int[q.objectives().size()]; boolean ready=true;
        for(int i=0;i<progress.length;i++) {
            var o=q.objectives().get(i); int raw=o.type().equals("talk")?s.getCompound("talks").getInt(""+i):total(p,o);
            if(q.repeatable()&&cumulative(o)) raw-=s.getCompound("baseline").getInt(""+i);
            progress[i]=Math.max(0,Math.min(o.count(),raw));
            if(!o.type().equals("field")&&progress[i]<o.count()) ready=false;
        }
        int choice=s.contains("choice")?s.getInt("choice"):-1;
        if(n>0&&!q.repeatable()) return new View(q.id(),CLAIMED,progress,n,0,"",choice);
        long remaining=n==0?0:Math.max(0,s.getLong("next")-now(p));
        if(remaining>0) return new View(q.id(),COOLDOWN,progress,n,(remaining+19)/20,"World play time until renewal",choice);
        String missing=missing(p,q,new HashSet<>());
        if(!missing.isEmpty()) return new View(q.id(),UNAVAILABLE,progress,n,0,missing,choice);
        var deps=q.requires().stream().filter(id->claims(p,id)==0).map(id->Campaign.quests.get(id).title()).toList();
        if(!deps.isEmpty()) return new View(q.id(),LOCKED,progress,n,0,"Claim first: "+String.join("; ",deps),choice);
        if(!s.getBoolean("accepted")) return new View(q.id(),OFFERED,progress,n,0,"Speak to "+q.giverName()+" to accept this quest.",choice);
        return new View(q.id(),ready?READY:ACTIVE,progress,n,0,"",choice);
    }
    public static String accept(ServerPlayer p,String id) {
        Quest q=Campaign.quests.get(id); if(q==null)return "Unknown quest.";
        if(!QuestServer.canTransact(p,q))return "Speak to "+q.giverName()+" in person to accept this quest.";
        View v=view(p,q); if(v.status()!=OFFERED)return v.reason().isEmpty()?"This quest cannot be accepted yet.":v.reason();
        var data=JournalData.get(p.server);
        var accepted=JournalData.child(JournalData.child(data.player(p.getUUID()),"quests"),id);
        accepted.putBoolean("accepted",true);accepted.remove("talks");
        data.setDirty(); return "";
    }
    /** Validates against current server state; preview values from the client are never trusted. */
    public static String claim(ServerPlayer p,String id,String answer,int choice) {
        Quest q=Campaign.quests.get(id); if(q==null) return "Unknown quest.";
        if(!QuestServer.canTransact(p,q))return "Return to "+q.giverName()+" to turn in this quest.";
        View view=view(p,q); if(view.status()!=READY) return view.reason().isEmpty()?"This quest is not ready to claim.":view.reason();
        for(var o:q.objectives()) if(o.type().equals("field")&&!o.target().strip().equalsIgnoreCase(answer.strip())) return "Copy the journal phrase into the answer field.";
        if(!q.choices().isEmpty()&&(choice<0||choice>=q.choices().size())) return "Choose a response before signing this entry.";
        var inv=p.getInventory(); var slots=new ArrayList<ItemStack>();
        for(int i=0;i<inv.getContainerSize();i++) slots.add(inv.getItem(i).copy());
        // Aggregate duplicate donations so two objectives cannot spend the same stack twice.
        var donations=new LinkedHashMap<Item,Integer>();
        for(var o:q.objectives()) if(o.type().equals("submit")) donations.merge(ForgeRegistries.ITEMS.getValue(new ResourceLocation(o.target())),o.count(),Integer::sum);
        for(var donation:donations.entrySet()) {
            int needed=donation.getValue();
            for(var stack:slots) if(stack.is(donation.getKey())) {int take=Math.min(needed,stack.getCount()); stack.shrink(take);needed-=take;}
            if(needed>0) return "Not enough donation items. Nothing was consumed.";
        }
        var rewards=new ArrayList<ItemStack>(); rewards.add(new ItemStack(Items.EMERALD,q.emeralds()));
        for(var r:q.rewards()) {var key=new ResourceLocation(r.item()); if(ForgeRegistries.ITEMS.containsKey(key))rewards.add(new ItemStack(ForgeRegistries.ITEMS.getValue(key),r.count()));}
        for(var reward:rewards) if(!insert(slots,reward)) return "Make room in your inventory for all rewards. Nothing was consumed.";
        for(int i=0;i<slots.size();i++) inv.setItem(i,slots.get(i));
        var data=JournalData.get(p.server); var journal=data.player(p.getUUID());
        var s=JournalData.child(JournalData.child(journal,"quests"),id);
        int old=s.getInt("claims"); s.putInt("claims",old==Integer.MAX_VALUE?old:old+1);s.putLong("next",now(p)+q.cooldown()*20L);
        s.putBoolean("accepted",false);
        if(!q.choices().isEmpty())s.putInt("choice",choice);
        var baseline=new CompoundTag();for(int i=0;i<q.objectives().size();i++)if(cumulative(q.objectives().get(i)))baseline.putInt(""+i,total(p,q.objectives().get(i)));s.put("baseline",baseline);
        if(old==0) {var standing=JournalData.child(journal,"standing");standing.putInt(q.faction(),standing.getInt(q.faction())+q.standing());}
        data.setDirty(); inv.setChanged(); p.containerMenu.broadcastChanges();p.giveExperiencePoints(q.xp());
        p.sendSystemMessage(Component.literal("Quest complete: "+q.title()+"  +"+q.emeralds()+" emeralds"));
        return "";
    }
    private static boolean insert(List<ItemStack> slots,ItemStack reward) {
        for(int i=0;i<36;i++) {var slot=slots.get(i);if(!slot.isEmpty()&&ItemStack.isSameItemSameTags(slot,reward)){int moved=Math.min(reward.getCount(),slot.getMaxStackSize()-slot.getCount());slot.grow(moved);reward.shrink(moved);}}
        for(int i=0;i<36&&!reward.isEmpty();i++)if(slots.get(i).isEmpty()){int moved=Math.min(reward.getCount(),reward.getMaxStackSize());slots.set(i,reward.copyWithCount(moved));reward.shrink(moved);}
        return reward.isEmpty();
    }
    public static String rank(int standing) { return standing>=500?"Luminary":standing>=250?"Steward":standing>=100?"Trusted":standing>=25?"Ally":"Newcomer"; }
}
