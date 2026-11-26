package dev.nordui.compat;

import dev.nordui.client.ClientConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.fml.ModList;
import java.util.*;

public final class UiCompat {
    public record Integration(String id,String name,String behavior) {}
    public static final List<Integration> INTEGRATIONS=List.of(
        new Integration("irons_spellbooks","Iron's Spells","Spells tab; native spell wheel, casting and cooldowns; themed mana"),
        new Integration("curios","Curios","Native accessory slots and validators; equipment tab shortcut"),
        new Integration("rpghud","RPG-HUD","AUTO gives this mod ownership of health, XP and hotbar"),
        new Integration("jade","Jade","Native tooltips retained; compass height can be adjusted in F7"),
        new Integration("jei","JEI","Compact containers reserve sidebar room; native slot tooltips"),
        new Integration("trashslot","TrashSlot","Native trash slot beside inventory, vault and spells; native deletion/undo"),
        new Integration("parcool","ParCool","Lift survival bars; leave native stamina and actions intact"),
        new Integration("equipmentcompare","Equipment Compare","Native item-tooltip events are retained"),
        new Integration("iceberg","Iceberg","Native tooltip pipeline retained"),
        new Integration("whatdurability","What Durability","Native overlay retained; hotbar and bar offsets available"),
        new Integration("travelers_titles","Traveler's Titles","Native announcements retained"),
        new Integration("immersivemessages","Immersive Messages","Native text and announcements retained"),
        new Integration("immersivedamageindicators","Damage Indicators","Native combat indicators retained"),
        new Integration("xaeroworldmap","Xaero's World Map","Custom map screen and buttons left intact"),
        new Integration("waystones","Waystones","Native travel screen left intact"),
        new Integration("explorerscompass","Explorer's Compass","Native navigation screen/overlay retained"),
        new Integration("puffish_skills","Pufferfish Skills","Native skill screen retained; inventory fallback for injected buttons"),
        new Integration("puffish_attributes","Pufferfish Attributes","Attribute tooltips and native screens retained"),
        new Integration("transmog","Transmog","Native cosmetic screen retained; inventory fallback for shortcuts"),
        new Integration("rpg_attribute_system","RPG Attribute System","Native attribute UI retained; inventory fallback"),
        new Integration("everlastingabilities","Everlasting Abilities","Native ability screen retained"),
        new Integration("patchouli","Patchouli","Book layouts, navigation and controls left intact"),
        new Integration("easy_npc","Easy NPC","Dialogue and editor screens left intact"),
        new Integration("customnpcs","Custom NPCs","Dialogue, quests and editor screens left intact"),
        new Integration("boundless","Boundless","Quest screens left intact"),
        new Integration("sdmshoprework","SDM Shop","Shop screens and custom controls left intact"),
        new Integration("sdm_ui_lib","SDM UI Library","Custom layouts and controls left intact"),
        new Integration("ftblibrary","FTB Library","Custom screens left intact"),
        new Integration("minecolonies","MineColonies","Colony screens and BlockUI controls left intact"),
        new Integration("easyanvils","Easy Anvils","Custom container screen left intact"),
        new Integration("easymagic","Easy Magic","Custom container screen left intact"),
        new Integration("ironchest","Iron Chests","Custom container layouts left intact"),
        new Integration("ironfurnaces","Iron Furnaces","Custom container layouts left intact"),
        new Integration("smoothgui","Smooth GUI","Screen animation events retained; inventory replacement toggle available"),
        new Integration("bettercombat","Better Combat","Crosshair and combat controls retained")
    );
    public static boolean loaded(String id) { return ModList.get().isLoaded(id); }
    public static boolean rpgHud() { return loaded("rpghud") || loaded("rpg_hud"); }
    public static boolean compactForJei() { return ClientConfig.JEI_SPACE.get() && loaded("jei"); }
    public static int compassY() { return 12 + ClientConfig.COMPASS_Y.get(); }
    public static int hudLift() { return ClientConfig.HUD_LIFT.get() + (ClientConfig.AUTO_SPACING.get() && loaded("parcool") ? 20 : 0); }
    public static boolean themeScreen(Screen s) {
        if(s==null)return false;
        String name=s.getClass().getName();
        return name.startsWith("dev.nordui.") || name.startsWith("net.minecraft.") || ClientConfig.MOD_SCREENS.get();
    }
    public static List<Integration> detected() { return INTEGRATIONS.stream().filter(i->loaded(i.id()) || i.id().equals("rpghud")&&rpgHud()).toList(); }
}
