package dev.deepnightguide.client;

import com.google.gson.*;
import dev.deepnightguide.DeepNightGuide;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.*;

/** Atomic, world/server + player scoped progress. Manual unticks suppress automatic re-ticking. */
public final class ChecklistStore {
    private final Path file;
    private final Set<String> completed = new HashSet<>(), suppressed = new HashSet<>();
    private boolean dirty;
    public String error = "";
    public ChecklistStore(Path root, String identity) {
        try {
            String hash=HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(identity.getBytes(StandardCharsets.UTF_8)));
            file=root.resolve(hash+".json");
        } catch(Exception e) { throw new IllegalStateException(e); }
        if(Files.exists(file)) try {
            JsonObject json=JsonParser.parseString(Files.readString(file)).getAsJsonObject();
            readSet(json,"completed",completed); readSet(json,"suppressed",suppressed);
        } catch(Exception e) {
            error="Progress file could not be read; original preserved. See log.";
            DeepNightGuide.LOG.error("Could not read guide progress {}",file,e);
            try { Files.copy(file,file.resolveSibling(file.getFileName()+".corrupt-"+System.currentTimeMillis())); }
            catch(Exception backup) { error="Progress backup failed; saving disabled."; }
        }
    }
    private static void readSet(JsonObject json,String key,Set<String> result) {
        if(json.has(key))json.getAsJsonArray(key).forEach(v->result.add(v.getAsString()));
    }
    public boolean done(String key) { return completed.contains(key); }
    public void toggle(String key) {
        if(completed.remove(key))suppressed.add(key);else {completed.add(key);suppressed.remove(key);}
        dirty=true;save();
    }
    public void observe(String key) { if(!suppressed.contains(key) && completed.add(key))dirty=true; }
    public void save() {
        if(!dirty || error.contains("disabled"))return;
        try {
            Files.createDirectories(file.getParent());
            JsonObject json=new JsonObject();json.addProperty("version",1);
            json.add("completed",new Gson().toJsonTree(new TreeSet<>(completed)));
            json.add("suppressed",new Gson().toJsonTree(new TreeSet<>(suppressed)));
            Path tmp=file.resolveSibling(file.getFileName()+".tmp");
            Files.writeString(tmp,new GsonBuilder().setPrettyPrinting().create().toJson(json));
            try {Files.move(tmp,file,StandardCopyOption.REPLACE_EXISTING,StandardCopyOption.ATOMIC_MOVE);}
            catch(AtomicMoveNotSupportedException e){Files.move(tmp,file,StandardCopyOption.REPLACE_EXISTING);}
            dirty=false;error="";
        } catch(Exception e) {error="Checklist could not be saved. See log.";DeepNightGuide.LOG.error(error,e);}
    }
}
