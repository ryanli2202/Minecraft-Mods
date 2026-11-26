package dev.thedeepnightnpcs.quest;

import dev.thedeepnightnpcs.DeepNightNpcs;
import dev.thedeepnightnpcs.entity.NpcEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.*;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.server.*;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.*;

@Mod.EventBusSubscriber(modid = DeepNightNpcs.ID)
public final class HuntService {
    public record Target(String id, double health) {}
    public static final TagKey<EntityType<?>> INCLUDED = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(DeepNightNpcs.ID, "hunt_targets"));
    public static final TagKey<EntityType<?>> EXCLUDED = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(DeepNightNpcs.ID, "excluded_hunt_targets"));
    private static List<Target> targets = List.of();
    private static MinecraftServer indexedServer;
    public static List<Target> targets(MinecraftServer server) {
        if (indexedServer != server) rebuild(server);
        return targets;
    }
    public static void rebuild(MinecraftServer server) {
        List<Target> found = new ArrayList<>();
        for (var type : ForgeRegistries.ENTITY_TYPES) {
            ResourceLocation key = ForgeRegistries.ENTITY_TYPES.getKey(type);
            if (key == null || key.getNamespace().equals(DeepNightNpcs.ID) || type.is(EXCLUDED) || DeepNightNpcs.EXCLUDE.get().contains(key.toString())) continue;
            Entity entity = null;
            try {
                // Detached instances are inspected once per server/reload, never added to the world.
                entity = type.create(server.overworld());
                if (!(entity instanceof LivingEntity living) || entity instanceof NpcEntity) continue;
                boolean hostile = type.getCategory() == MobCategory.MONSTER || entity instanceof Enemy;
                if (hostile || type.is(INCLUDED) || DeepNightNpcs.INCLUDE.get().contains(key.toString())) found.add(new Target(key.toString(), living.getMaxHealth()));
            } catch (Exception | LinkageError error) {
                DeepNightNpcs.LOG.warn("Cannot inspect hunt target {}: {}", key, error.toString());
            } finally { if (entity != null) entity.discard(); }
        }
        found.sort(Comparator.comparing(Target::id)); targets = List.copyOf(found); indexedServer = server;
        DeepNightNpcs.LOG.info("Caelen indexed {} hostile entity types from the installed registry", targets.size());
    }
    @SubscribeEvent public static void tags(TagsUpdatedEvent event) { if (event.getUpdateCause() == TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD) indexedServer = null; }
    @SubscribeEvent public static void stopped(ServerStoppedEvent event) { indexedServer = null; targets = List.of(); }
    public static void ensureOffers(ServerPlayer player) {
        var record = data(player).player(player.getUUID());
        boolean removed = record.offers.removeIf(c -> !available(player, c.target()));
        if (removed) data(player).setDirty();
        if (record.offers.isEmpty()) refresh(player, true);
    }
    private static HuntData data(ServerPlayer player) { return HuntData.get(player.server); }
    private static boolean available(ServerPlayer player, String target) { return targets(player.server).stream().anyMatch(t -> t.id().equals(target)); }
    public static String refresh(ServerPlayer player, boolean initial) {
        HuntData data = data(player); var r = data.player(player.getUUID()); long now = player.server.overworld().getGameTime();
        if (!initial && now < r.nextRefresh) return "The scouts are still returning. Try again in " + ((r.nextRefresh-now+19)/20) + " seconds.";
        List<Target> candidates = new ArrayList<>(targets(player.server));
        if (r.active != null) candidates.removeIf(t -> t.id().equals(r.active.target()));
        Collections.shuffle(candidates, new Random(player.getRandom().nextLong())); r.offers.clear();
        String[] styles = {"Roadwarden", "Night watch", "Deep hunt", "Scouting report", "Spire bounty", "Frontier patrol"};
        for (int i=0; i<Math.min(3, candidates.size()); i++) {
            Target target = candidates.get(i); int count = target.health() >= 100 ? 1 + player.getRandom().nextInt(2) : 3 + player.getRandom().nextInt(8);
            int difficulty = target.health() >= 100 ? 10 : target.health() >= 40 ? 4 : 2;
            r.offers.add(new HuntData.Contract(UUID.randomUUID(), target.id(), count, Math.min(48, 3+count*difficulty), 25+count*difficulty*5, styles[player.getRandom().nextInt(styles.length)]));
        }
        r.nextRefresh = now+1200; data.setDirty();
        return r.offers.isEmpty() ? "No eligible targets are registered. Ask the keeper of this world to check the hunt exclusions." : "Three reports, three possible roads. Read the target before you accept.";
    }
    public static String accept(ServerPlayer player, String id) {
        HuntData data = data(player); var r = data.player(player.getUUID());
        if (r.active != null) return "Finish or abandon your current contract first.";
        var match = r.offers.stream().filter(c -> c.id().toString().equals(id)).findFirst();
        if (match.isEmpty() || !available(player, match.get().target())) return "That report is no longer available. Read the current board.";
        r.active = match.get(); r.offers.remove(r.active); r.kills = 0; data.setDirty();
        return "Contract accepted. Only your credited kills from this moment count. Return to me when the work is done.";
    }
    public static String abandon(ServerPlayer player) {
        HuntData data = data(player); var r = data.player(player.getUUID());
        r.active = null; r.kills = 0; data.setDirty(); return "The contract is closed without payment. A living hunter can choose another road.";
    }
    public static String claim(ServerPlayer player) {
        HuntData data = data(player); var r = data.player(player.getUUID());
        if (r.active == null || r.kills < r.active.required()) return "The contract is not complete yet. Check your hunting ledger.";
        var completed = r.active;
        // Consume first; repeated packets cannot pay twice.
        r.active = null; r.kills = 0; r.completed++; data.setDirty();
        ItemStack reward = new ItemStack(Items.EMERALD, completed.emeralds());
        if (!player.getInventory().add(reward)) player.drop(reward, false);
        player.giveExperiencePoints(completed.xp());
        return "Well hunted. " + completed.emeralds() + " emeralds and " + completed.xp() + " experience are yours. The roads are a little safer.";
    }
    @SubscribeEvent public static void death(LivingDeathEvent event) {
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) return;
        credit(player, ForgeRegistries.ENTITY_TYPES.getKey(event.getEntity().getType()).toString());
    }
    public static void credit(ServerPlayer player, String type) {
        HuntData data = data(player); var r = data.player(player.getUUID());
        if (r.active == null || !r.active.target().equals(type) || r.kills >= r.active.required()) return;
        r.kills++; data.setDirty();
        player.displayClientMessage(Component.literal("Caelen's hunt: " + r.kills + "/" + r.active.required() + (r.kills == r.active.required() ? " - Return to Caelen for your reward" : "")), true);
    }
    public static CompoundTag snapshot(ServerPlayer player) {
        ensureOffers(player); var n = data(player).player(player.getUUID()).save();
        n.putInt("targetCount", targets(player.server).size());
        n.putInt("refreshSeconds", (int)Math.max(0, (n.getLong("nextRefresh")-player.server.overworld().getGameTime()+19)/20)); return n;
    }
}
