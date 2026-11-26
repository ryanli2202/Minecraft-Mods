package dev.deepnight.client;

import com.google.gson.JsonParser;
import dev.deepnight.DeepNight;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import software.bernie.geckolib.core.molang.MolangParser;

/** Populate referenced variables before GeckoLib's parallel resource loaders run.
 * GeckoLib 4.8.4 uses an unsynchronized global variable map; concurrent first
 * insertion of source query aliases can corrupt that map during resource reload.
 * Looking them up here preserves existing query values and animation semantics.
 */
final class MolangVariables {
    static void prepare(){
        try(var input=Objects.requireNonNull(DeepNight.class.getResourceAsStream("/data/deepnight/molang_variables.json"));
            var reader=new InputStreamReader(input,StandardCharsets.UTF_8)){
            for(var variable:JsonParser.parseReader(reader).getAsJsonArray())
                MolangParser.INSTANCE.getVariable(variable.getAsString());
        }catch(Exception ex){throw new IllegalStateException("Cannot initialize animation variables",ex);}
    }
}
