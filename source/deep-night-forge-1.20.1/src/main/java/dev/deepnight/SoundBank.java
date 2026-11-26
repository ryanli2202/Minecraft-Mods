package dev.deepnight;

import com.google.gson.*;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.registries.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class SoundBank {
    public static final DeferredRegister<SoundEvent> REGISTRY=DeferredRegister.create(ForgeRegistries.SOUND_EVENTS,DeepNight.ID);
    private static final Map<String,RegistryObject<SoundEvent>> EVENTS=new HashMap<>();
    private static final JsonObject MAPPING=read("/data/deepnight/creature_sounds.json");
    static {for(String name:read("/assets/deepnight/sounds.json").keySet())EVENTS.put(name,REGISTRY.register(name,()->SoundEvent.createVariableRangeEvent(DeepNight.id(name))));}
    private static JsonObject read(String path){
        try(var stream=SoundBank.class.getResourceAsStream(path)){
            if(stream==null)throw new IOException("Missing "+path);
            return JsonParser.parseReader(new InputStreamReader(stream,StandardCharsets.UTF_8)).getAsJsonObject();
        }catch(IOException e){throw new IllegalStateException(e);}
    }
    public static SoundEvent sound(Species species,String role){
        JsonObject entry=MAPPING.getAsJsonObject(species.id);
        if(entry!=null&&entry.has(role)){var event=EVENTS.get(entry.get(role).getAsString());if(event!=null)return event.get();}
        if(role.equals("music"))return null;
        return switch(role){
            case "death"->SoundEvents.GENERIC_DEATH;case "hurt"->SoundEvents.GENERIC_HURT;
            case "attack"->SoundEvents.PLAYER_ATTACK_STRONG;case "spawn"->SoundEvents.EVOKER_PREPARE_SUMMON;
            case "step"->SoundEvents.STONE_STEP;default->null;
        };
    }
    public static int registeredCount(){return EVENTS.size();}
}
