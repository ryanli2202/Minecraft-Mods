package dev.deepnightguide.client;

import com.google.gson.*;
import dev.deepnightguide.DeepNightGuide;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;

/** Reads installed data and manuals without a dependency on any content mod. No game objects off-thread. */
public final class GuideNotes {
    public static final class Note {
        public Boolean tameable,boss;
        public String icon;
        public final Set<String> categories=new LinkedHashSet<>();
        public final Map<String,LinkedHashSet<String>> sections=new LinkedHashMap<>();
        public void add(String section,String text) {
            if(text!=null&&!text.isBlank())sections.computeIfAbsent(section,k->new LinkedHashSet<>()).add(text);
        }
    }
    private final Map<String,Note> notes=new HashMap<>();
    public int sources, failures;
    private final Map<String,JsonObject> loot=new LinkedHashMap<>(), features=new LinkedHashMap<>(), placed=new LinkedHashMap<>();
    private final Map<String,String> biomeFeatures=new HashMap<>();
    private final Map<String,JsonObject> biomeTags=new HashMap<>();
    private static final Pattern FORMAT=Pattern.compile("\\$\\([^)]*\\)|§.");
    public Note get(String key) {return notes.get(key);}
    public void addWorldHabitat(String key,Set<String> biomes) {
        Note note=note(key);note.sections.remove("Current world habitats");
        note.add("Current world habitats","Final biome spawn tables in this singleplayer world: "+String.join(", ",biomes)+". Structure/event spawns are separate; dimension, light, difficulty and spawn rules still apply.");
    }
    private Note note(String key) {return notes.computeIfAbsent(key,k->new Note());}
    private void add(String key,String section,String text) {note(key).add(section,text);}
    public static GuideNotes load(List<Path> roots,Path override) {
        GuideNotes out=new GuideNotes();
        for(Path root:roots)out.readRoot(root);
        out.finishData();
        try(InputStream in=GuideNotes.class.getResourceAsStream("/assets/deepnightguide/custom_entries.json")) {
            if(in!=null)out.merge(JsonParser.parseReader(new InputStreamReader(in,StandardCharsets.UTF_8)).getAsJsonObject());
        }catch(Exception e){out.fail("custom entries",e);}
        try(InputStream in=GuideNotes.class.getResourceAsStream("/assets/deepnightguide/boss_entries.json")) {
            if(in!=null)out.merge(JsonParser.parseReader(new InputStreamReader(in,StandardCharsets.UTF_8)).getAsJsonObject());
        }catch(Exception e){out.fail("boss entries",e);}
        try(InputStream in=GuideNotes.class.getResourceAsStream("/assets/deepnightguide/deepnight_entries.json")) {
            if(in!=null)out.merge(JsonParser.parseReader(new InputStreamReader(in,StandardCharsets.UTF_8)).getAsJsonObject());
        }catch(Exception e){out.fail("Deep Night entries",e);}
        if(Files.exists(override))try(var reader=Files.newBufferedReader(override)){out.merge(JsonParser.parseReader(reader).getAsJsonObject());}catch(Exception e){out.fail(override.toString(),e);}
        out.loot.clear();out.features.clear();out.placed.clear();out.biomeFeatures.clear();out.biomeTags.clear();
        return out;
    }
    private void readRoot(Path root) {
        for(String folder:List.of("data","assets")) {
            Path start=root.resolve(folder);if(!Files.exists(start))continue;
            try(var files=Files.walk(start)) {
                files.filter(p->p.toString().endsWith(".json")).forEach(file->{
                    String path=root.relativize(file).toString().replace('\\','/');
                    boolean data=path.startsWith("data/")&&(path.contains("/loot_tables/")||path.contains("/forge/biome_modifier/")||path.contains("/worldgen/configured_feature/")||path.contains("/worldgen/placed_feature/")||path.contains("/tags/worldgen/biome/")||path.contains("/tags/items/tame")||path.contains("/tags/entity_types/tame")||path.contains("/advancements/")&&path.contains("tame"));
                    boolean manual=path.contains("/en_us/entries/")&&path.contains("patchouli_books/")||path.startsWith("assets/alexsmobs/book/animal_dictionary/");
                    if(!data&&!manual)return;
                    try {
                        if(Files.size(file)>4_000_000)return;
                        JsonElement value=JsonParser.parseString(Files.readString(file));if(!value.isJsonObject())return;
                        JsonObject json=value.getAsJsonObject();sources++;
                        if(data)readData(path,json);else readManual(root,path,json);
                    }catch(Exception e){fail(path,e);}
                });
            }catch(Exception e){fail(start.toString(),e);}
        }
    }
    private void fail(String file,Exception e) {failures++;DeepNightGuide.LOG.debug("Skipping guide source {}",file,e);}
    private static String id(String path,String marker) {
        String namespace=path.split("/")[1];return namespace+":"+path.substring(path.indexOf(marker)+marker.length(),path.length()-5);
    }
    private void readData(String path,JsonObject j) {
        if(path.contains("/tags/worldgen/biome/")){String key=id(path,"/tags/worldgen/biome/");JsonObject old=biomeTags.get(key);if(old!=null&&(!j.has("replace")||!j.get("replace").getAsBoolean())){for(JsonElement e:array(j.get("values")))old.getAsJsonArray("values").add(e);}else biomeTags.put(key,j);return;}
        if(path.contains("/loot_tables/")){loot.put(id(path,"/loot_tables/"),j);return;}
        if(path.contains("/worldgen/configured_feature/")){features.put(id(path,"/worldgen/configured_feature/"),j);return;}
        if(path.contains("/worldgen/placed_feature/")){placed.put(id(path,"/worldgen/placed_feature/"),j);return;}
        if(path.contains("/forge/biome_modifier/")) {
            String type=str(j,"type");
            if(type.equals("forge:add_spawns")) {
                JsonElement spawns=j.get("spawners");
                for(JsonElement s:array(spawns))if(s.isJsonObject()) {
                    JsonObject spawn=s.getAsJsonObject();String entity=str(spawn,"type");
                    if(!entity.isBlank())add("entity:"+entity,"Where to find","Biome selection: "+values(j.get("biomes"))+". Spawn weight "+str(spawn,"weight")+", group "+str(spawn,"minCount")+"-"+str(spawn,"maxCount")+". Shipped biome modifier; server config and spawn conditions apply.");
                }
            }
            if(type.equals("forge:add_features"))for(JsonElement f:array(j.get("features")))if(f.isJsonPrimitive())biomeFeatures.merge(f.getAsString(),values(j.get("biomes")),(a,b)->a+", "+b);
        }
        if(path.contains("/tags/items/tame")) {
            String tag=id(path,"/tags/items/");String name=tag.substring(tag.indexOf(':')+1).replaceFirst("^tame_","");
            add("entity:"+tag.split(":")[0]+":"+name,"Taming","Declared taming item tag #"+tag+": "+values(j.get("values"))+". See the mod's instructions for the required interaction.");
            note("entity:"+tag.split(":")[0]+":"+name).tameable=true;
        }
        if(path.contains("/tags/entity_types/tame"))for(JsonElement e:array(j.get("values")))if(e.isJsonPrimitive()&&!e.getAsString().startsWith("#"))note("entity:"+e.getAsString()).tameable=true;
        if(path.contains("/advancements/"))collectTames(j);
    }
    private void collectTames(JsonElement element) {
        if(element.isJsonObject()) {
            JsonObject j=element.getAsJsonObject();
            if(str(j,"trigger").equals("minecraft:tame_animal") && j.has("conditions")) {
                JsonObject c=j.getAsJsonObject("conditions");
                if(c.has("entity")&&c.get("entity").isJsonObject()) {
                    String type=str(c.getAsJsonObject("entity"),"type");if(type.contains(":"))note("entity:"+type).tameable=true;
                }
            }
            j.entrySet().forEach(e->collectTames(e.getValue()));
        }else if(element.isJsonArray())element.getAsJsonArray().forEach(this::collectTames);
    }
    private void readManual(Path root,String path,JsonObject j) throws IOException {
        if(path.startsWith("assets/alexsmobs/book/animal_dictionary/")) {
            String textFile=str(j,"text");if(textFile.isBlank())return;
            Path book=root.resolve("assets/alexsmobs/book/animal_dictionary/en_us/"+textFile);
            if(!Files.exists(book))book=root.resolve("assets/alexsmobs/book/animal_dictionary/"+textFile);
            if(!Files.exists(book))return;
            String text=clean(Files.readString(book));
            for(JsonElement render:array(j.get("entity_renders")))if(render.isJsonObject()) {
                String entity=str(render.getAsJsonObject(),"entity");
                if(!entity.isBlank())add("entity:"+entity,"Installed mod manual",text);
            }
            return;
        }
        Set<String> targets=new LinkedHashSet<>();String icon=str(j,"icon");
        if(icon.matches("[a-z0-9_]+:[a-z0-9_/]+"))targets.add("item:"+icon);
        List<String> texts=new ArrayList<>();
        for(JsonElement p:array(j.get("pages")))if(p.isJsonObject()) {
            JsonObject page=p.getAsJsonObject();String text=str(page,"text"),entity=str(page,"entity");
            if(!text.isBlank())texts.add(clean(text));
            if(!entity.isBlank())targets.add("entity:"+entity.split("\\{")[0]);
        }
        if(targets.isEmpty()||texts.isEmpty())return;
        for(String target:targets)for(String text:texts)add(target,"Installed mod manual",text);
    }
    public static String clean(String s) {
        return FORMAT.matcher(s.replace("$(br2)","\n\n").replace("$(br)","\n")).replaceAll("").replaceAll("<[^>]+>","").trim();
    }
    private void finishData() {
        // Resolve biome selectors from the installed data where their tag definitions are present.
        for(Note n:notes.values())if(n.sections.containsKey("Where to find")) {
            LinkedHashSet<String> expanded=new LinkedHashSet<>();
            for(String line:n.sections.get("Where to find")) {
                var matcher=Pattern.compile("#([a-z0-9_.-]+:[a-z0-9_./-]+)").matcher(line);
                StringBuffer buffer=new StringBuffer();
                while(matcher.find())matcher.appendReplacement(buffer,java.util.regex.Matcher.quoteReplacement(expandBiome(matcher.group(1),new HashSet<>())));
                matcher.appendTail(buffer);expanded.add(buffer.toString());
            }
            n.sections.put("Where to find",expanded);
        }
        for(var table:loot.entrySet()) {
            Set<String> items=new LinkedHashSet<>();collectLoot(table.getValue(),items,new HashSet<>());
            String source=table.getKey(),namespace=source.split(":")[0],local=source.substring(source.indexOf(':')+1);
            String label=local.startsWith("entities/")?"Mob loot: "+namespace+":"+local.substring(9):local.startsWith("blocks/")?"Block loot: "+namespace+":"+local.substring(7):"Loot table: "+source;
            for(String item:items)add("item:"+item,"Where to find",label+". Conditions, chance, tools and server overrides may apply.");
            if(local.startsWith("entities/")&&!items.isEmpty())add("entity:"+namespace+":"+local.substring(9),"Drops",String.join(", ",items)+". These are possible loot-table results, not guaranteed drops.");
        }
        for(var entry:placed.entrySet()) {
            JsonObject p=entry.getValue();JsonElement value=p.get("feature");if(value==null)continue;
            JsonObject config=value.isJsonObject()?value.getAsJsonObject():features.get(value.getAsString());
            if(config==null || !str(config,"type").endsWith(":ore"))continue;
            Set<String> blocks=new LinkedHashSet<>();collectBlockNames(config,blocks);
            String height="";
            for(JsonElement modifier:array(p.get("placement")))if(modifier.isJsonObject()&&str(modifier.getAsJsonObject(),"type").equals("minecraft:height_range"))height=" Height: "+values(modifier.getAsJsonObject().get("height"))+".";
            String biomes=biomeFeatures.get(entry.getKey());
            for(String block:blocks) {
                note("item:"+block).categories.add("ORES");
                add("item:"+block,"Ore generation","Feature "+entry.getKey()+"."+height+(biomes==null?" Biome/dimension placement is not exposed here.":" Biome selection: "+biomes+".")+" Shipped data; world generation settings may override this.");
            }
        }
    }
    private String expandBiome(String key,Set<String> seen) {
        if(!seen.add(key)||!biomeTags.containsKey(key))return "#"+key;
        List<String> names=new ArrayList<>();
        for(JsonElement e:array(biomeTags.get(key).get("values"))) {
            String value=e.isJsonObject()?str(e.getAsJsonObject(),"id"):e.getAsString();
            names.add(value.startsWith("#")?expandBiome(value.substring(1),new HashSet<>(seen)):value);
        }
        return String.join(", ",names);
    }
    private void collectLoot(JsonElement e,Set<String> items,Set<String> seen) {
        if(e.isJsonObject()) {
            JsonObject j=e.getAsJsonObject();String type=str(j,"type"),name=str(j,"name");
            if((type.equals("minecraft:item")||type.equals("item"))&&name.contains(":"))items.add(name);
            if((type.equals("minecraft:loot_table")||type.equals("loot_table"))&&seen.add(name)&&loot.containsKey(name))collectLoot(loot.get(name),items,seen);
            j.entrySet().forEach(v->collectLoot(v.getValue(),items,seen));
        } else if(e.isJsonArray())e.getAsJsonArray().forEach(v->collectLoot(v,items,seen));
    }
    private void collectBlockNames(JsonElement e,Set<String> blocks) {
        if(e.isJsonObject()) {
            JsonObject j=e.getAsJsonObject();if(j.has("state")&&j.get("state").isJsonObject()){String name=str(j.getAsJsonObject("state"),"Name");if(!name.isBlank())blocks.add(name);}
            j.entrySet().forEach(v->collectBlockNames(v.getValue(),blocks));
        }else if(e.isJsonArray())e.getAsJsonArray().forEach(v->collectBlockNames(v,blocks));
    }
    public void merge(JsonObject root) {
        for(var entry:root.entrySet()) {
            if(!entry.getValue().isJsonObject())continue;
            Note n=note(entry.getKey());JsonObject j=entry.getValue().getAsJsonObject();
            if(j.has("tameable"))n.tameable=j.get("tameable").getAsBoolean();
            if(j.has("icon"))n.icon=j.get("icon").getAsString();
            if(j.has("boss"))n.boss=j.get("boss").getAsBoolean();
            if(j.has("categories"))for(JsonElement c:j.getAsJsonArray("categories"))n.categories.add(c.getAsString());
            if(j.has("sections"))for(var section:j.getAsJsonObject("sections").entrySet()) {
                // Explicit entry sections replace inferred sections with the same heading.
                n.sections.remove(section.getKey());for(JsonElement line:array(section.getValue()))n.add(section.getKey(),line.getAsString());
            }
        }
    }
    private static String str(JsonObject j,String key) {JsonElement e=j.get(key);return e!=null&&e.isJsonPrimitive()?e.getAsString():"";}
    private static List<JsonElement> array(JsonElement e) {
        if(e==null||e.isJsonNull())return List.of();if(e.isJsonArray()){List<JsonElement> a=new ArrayList<>();e.getAsJsonArray().forEach(a::add);return a;}return List.of(e);
    }
    private static String values(JsonElement e) {
        if(e==null)return "not specified";
        if(e.isJsonPrimitive())return e.getAsString();
        if(e.isJsonArray())return String.join(", ",array(e).stream().map(GuideNotes::values).toList());
        return e.toString().replace("\"","");
    }
}
