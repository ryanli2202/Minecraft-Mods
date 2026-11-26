package dev.deepnightguide.client;

import com.google.gson.*;
import dev.deepnightguide.DeepNightGuide;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.ForgeRegistries;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/** Authored chronicle plus a dimension atlas driven by the server's login dimension list. */
public final class GuideLore {
    public static void refresh(GuideIndex index) {
        try(InputStream in=GuideLore.class.getResourceAsStream("/assets/deepnightguide/lore.json")) {
            if(in==null)throw new IOException("Missing lore.json");
            JsonObject root=JsonParser.parseReader(new InputStreamReader(in,StandardCharsets.UTF_8)).getAsJsonObject();
            Path override=Minecraft.getInstance().gameDirectory.toPath().resolve("config/deepnightguide/lore.json");
            if(Files.exists(override))try(var reader=Files.newBufferedReader(override)) {
                JsonObject user=JsonParser.parseReader(reader).getAsJsonObject();
                for(String section:List.of("chapters","dimensions"))if(user.has(section))for(var value:user.getAsJsonObject(section).entrySet())root.getAsJsonObject(section).add(value.getKey(),value.getValue());
            }
            List<GuideEntry> result=new ArrayList<>();
            for(var chapter:root.getAsJsonObject("chapters").entrySet())if(!chapter.getValue().isJsonNull())result.add(makeEntry(new ResourceLocation("deepnightguide",chapter.getKey()),chapter.getValue().getAsJsonObject(),false));
            var connection=Minecraft.getInstance().getConnection();
            if(connection!=null)for(var dimension:connection.levels()) {
                JsonElement template=root.getAsJsonObject("dimensions").get(dimension.location().toString());
                result.add(makeEntry(dimension.location(),template!=null&&template.isJsonObject()?template.getAsJsonObject():new JsonObject(),true));
            }
            // Commit only after a complete parse, retaining the previous lore if a user file is invalid.
            index.entries.removeIf(e->e.lore);index.byKey.entrySet().removeIf(e->e.getValue().lore);
            result.forEach(index::add);
        }catch(Exception ex){DeepNightGuide.LOG.error("Could not load guide lore; previous chapters retained",ex);index.progress.error="Lore file could not be loaded; see log.";}
    }
    public static GuideEntry makeEntry(ResourceLocation id,JsonObject template,boolean dimension) {
        String name=template.has("title")?template.get("title").getAsString():readable(id.getPath());
        ResourceLocation iconId=ResourceLocation.tryParse(template.has("icon")?template.get("icon").getAsString():"minecraft:written_book");
        Item item=iconId==null?Items.WRITTEN_BOOK:ForgeRegistries.ITEMS.getValue(iconId);if(item==null||item==Items.AIR)item=Items.WRITTEN_BOOK;
        GuideEntry entry=new GuideEntry(id,name,dimension?"Dimension atlas":"Deep Night chronicle",new ItemStack(item),null,EnumSet.of(GuideEntry.Category.LORE));
        if(template.has("sections"))for(var section:template.getAsJsonObject("sections").entrySet()) {
            List<String> paragraphs=new ArrayList<>();
            if(section.getValue().isJsonArray())section.getValue().getAsJsonArray().forEach(p->paragraphs.add(p.getAsString()));else paragraphs.add(section.getValue().getAsString());
            entry.loreSections.put(section.getKey(),paragraphs);
        }
        if(entry.loreSections.isEmpty())entry.loreSections.put("Beyond the chart",List.of("A new name has appeared at the edge of the atlas. The old scribes left no reliable account of this realm. Let the first record be a careful one: mark the crossing, take provisions, and write down what you find before giving it a legend."));
        if(dimension)entry.loreSections.put("Atlas record",List.of("Dimension: "+id+". This realm is registered in your current world/server. Registration alone does not mean there is a Survival entrance. Follow the owning mod's guide for access; this book does not teleport you or unlock portals."));
        entry.loreSections.put("About the chronicle",List.of("Original Deep Night lore inspired by the Ashen Covenant campaign. Its voices, histories and place names are storytelling, not claims about an individual mod's official canon. Field notes describe gameplay separately; named story locations are not guaranteed generated structures."));
        return entry;
    }
    private static String readable(String name){StringBuilder out=new StringBuilder();for(String word:name.replace('/',' ').replace('_',' ').split(" "))if(!word.isEmpty())out.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(' ');return out.toString().trim();}
}
