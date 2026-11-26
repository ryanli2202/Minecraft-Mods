package dev.thedeepnightnpcs.client;

import com.google.gson.*;
import dev.thedeepnightnpcs.DeepNightNpcs;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import java.util.*;

public final class DialogueLibrary {
    public record Node(String id, String title, String text, List<String> children) {}
    public final Map<String, Node> nodes = new LinkedHashMap<>();
    public static DialogueLibrary load(String npc) {
        DialogueLibrary library = new DialogueLibrary();
        try (var reader = Minecraft.getInstance().getResourceManager().openAsReader(new ResourceLocation(DeepNightNpcs.ID, "dialogue/" + npc + ".json"))) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            for (var entry : root.entrySet()) {
                JsonObject n = entry.getValue().getAsJsonObject(); List<String> children = new ArrayList<>();
                if (n.has("children")) for (JsonElement child : n.getAsJsonArray("children")) children.add(child.getAsString());
                library.nodes.put(entry.getKey(), new Node(entry.getKey(), n.get("title").getAsString(), n.get("text").getAsString(), List.copyOf(children)));
            }
            if (!library.nodes.containsKey("root")) throw new IllegalArgumentException("Missing dialogue root");
            for (Node node : library.nodes.values()) for (String child : node.children()) if (!library.nodes.containsKey(child)) throw new IllegalArgumentException("Missing dialogue node: " + child);
        } catch (Exception error) {
            DeepNightNpcs.LOG.error("Failed to load {} dialogue", npc, error);
            library.nodes.clear(); library.nodes.put("root", new Node("root", "A quiet moment", "My archive could not be opened. Check the dialogue resource pack and the game log.", List.of()));
        }
        return library;
    }
}
