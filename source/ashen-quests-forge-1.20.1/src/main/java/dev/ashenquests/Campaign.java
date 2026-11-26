package dev.ashenquests;

import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.*;
import net.minecraft.util.profiling.ProfilerFiller;
import java.util.*;

/** A reload is atomic: invalid definitions never replace the working campaign. */
public final class Campaign extends SimpleJsonResourceReloadListener {
    public static final Gson GSON = new Gson();
    public static Map<String, Quest> quests = Map.of();
    public static int revision;
    public static final Set<String> TYPES = Set.of("collect","submit","kill","achieve","effect","field","craft","smelt","visit","biome","interact","tame","fish","talk");
    public Campaign() { super(GSON, "ashen_quests"); }
    @Override protected void apply(Map<ResourceLocation, JsonElement> entries, ResourceManager manager, ProfilerFiller profiler) {
        try {
            var next = new LinkedHashMap<String, Quest>();
            entries.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(e -> {
                for (JsonElement row : e.getValue().getAsJsonObject().getAsJsonArray("quests")) {
                    Quest q = GSON.fromJson(row, Quest.class);
                    if (next.putIfAbsent(q.id(), q) != null) throw new IllegalArgumentException("Duplicate quest " + q.id());
                }
            });
            validate(next);
            quests = Collections.unmodifiableMap(next); revision++;
            Chronicles.LOG.info("Loaded {} Ashen quests", quests.size());
        } catch (Exception ex) {
            Chronicles.LOG.error("Invalid Ashen campaign; keeping previous definitions", ex);
            if (quests.isEmpty()) throw new IllegalStateException("No valid Ashen campaign", ex);
        }
    }
    public static void validate(Map<String, Quest> next) {
        if (next.isEmpty() || next.size() > 2000) throw new IllegalArgumentException("Campaign must contain 1..2000 quests");
        for (Quest q : next.values()) {
            if (!Set.of("eldara","rowan","sera","mara","elian","iona","tamsin","fen","ysolde","caelen","jorge").contains(q.giverId())) throw new IllegalArgumentException("Unknown quest giver: " + q.id());
            if (q.id() == null || !q.id().matches("[a-z0-9_]{1,100}") || q.title() == null || q.story() == null || q.chapter() == null || q.section() == null || q.aftermath() == null || q.icon() == null || q.faction() == null || q.requires() == null || q.objectives() == null || q.rewards() == null || q.choices() == null) throw new IllegalArgumentException("Missing quest field");
            if (GSON.toJson(q).length() > 24000 || q.emeralds() < 1 || q.emeralds() > 640 || q.xp() < 0 || q.xp() > 10000 || q.standing() < 0 || q.standing() > 100 || q.cooldown() < 0 || q.cooldown() > 604800 || q.objectives().isEmpty() || q.objectives().size() > 12 || q.choices().size() > 3) throw new IllegalArgumentException("Invalid limits: " + q.id());
            for (var o : q.objectives()) {
                if (!TYPES.contains(o.type()) || o.target() == null || o.label() == null || o.target().length() > 256 || o.count() < 1 || o.count() > 100000) throw new IllegalArgumentException("Invalid objective: " + q.id());
                if (!o.type().equals("field") && ResourceLocation.tryParse(o.target()) == null) throw new IllegalArgumentException("Invalid target: " + q.id());
                if(o.type().equals("talk")&&(o.count()!=1||o.dialogue()==null||o.dialogue().isBlank()||o.dialogue().length()>2000||!o.target().matches("thedeepnightnpcs:(eldara|rowan|sera|mara|elian|iona|tamsin|fen|ysolde|caelen|jorge|spire_guard)")))throw new IllegalArgumentException("Invalid quest conversation: "+q.id());
                if (q.repeatable() && Set.of("achieve","effect","field","visit","biome").contains(o.type())) throw new IllegalArgumentException("Repeatable objective cannot be permanent: " + q.id());
            }
            for (var r : q.rewards()) if (ResourceLocation.tryParse(r.item()) == null || r.count() < 1 || r.count() > 256) throw new IllegalArgumentException("Invalid reward: " + q.id());
            if (q.rewards().size() > 12) throw new IllegalArgumentException("Too many rewards");
            for (String dep : q.requires()) if (!next.containsKey(dep)) throw new IllegalArgumentException("Unknown prerequisite " + dep);
        }
        var complete = new HashSet<String>();
        for (String id : next.keySet()) visit(id, next, new HashSet<>(), complete);
    }
    private static void visit(String id, Map<String, Quest> next, Set<String> path, Set<String> complete) {
        if (complete.contains(id)) return;
        if (!path.add(id)) throw new IllegalArgumentException("Dependency cycle at " + id);
        for (String dep : next.get(id).requires()) visit(dep, next, path, complete);
        path.remove(id); complete.add(id);
    }
}

