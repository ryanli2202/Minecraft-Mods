package dev.transmogify.client;

import dev.transmogify.ArmourData;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.*;
import java.util.regex.Pattern;

public final class ArmourCatalogue {
    private static final Pattern PART = Pattern.compile("(^|_)(helmet|helm|hood|head|headpiece|mask|hat|crown|chestplate|chest|chestpiece|cuirass|tunic|robe|robes|leggings|legs|greaves|pants|trousers|boots|feet|shoes|sabaton|sabatons)(_|$)");
    public record Entry(String id, String name, String mod, EquipmentSlot slot, String family, ItemStack icon) {
        public boolean matches(String query) {
            String text=(name+" "+id+" "+mod).toLowerCase(Locale.ROOT);
            for(String word:query.toLowerCase(Locale.ROOT).trim().split("\\s+")) if(!text.contains(word.replace("@",""))) return false;
            return true;
        }
    }
    private final List<Entry> entries;
    public ArmourCatalogue() {
        List<Entry> found=new ArrayList<>();
        for(Item item:ForgeRegistries.ITEMS) if(item instanceof ArmorItem armour) {
            var key=ForgeRegistries.ITEMS.getKey(item);
            String path=key.getPath();
            var matcher=PART.matcher(path);
            // Only group identifiers that contain a recognizable equipment part. Ambiguous sets
            // remain individually selectable instead of mixing unrelated armour materials.
            String family=matcher.find()?key.getNamespace()+":"+matcher.replaceFirst("_").replaceAll("^_+|_+$",""):key.toString();
            String mod=ModList.get().getModContainerById(key.getNamespace()).map(c->c.getModInfo().getDisplayName()).orElse(key.getNamespace());
            ItemStack icon=item.getDefaultInstance();
            found.add(new Entry(key.toString(),icon.getHoverName().getString(),mod,armour.getEquipmentSlot(),family,icon));
        }
        found.sort(Comparator.comparing(Entry::mod).thenComparing(Entry::name).thenComparing(Entry::id));
        entries=List.copyOf(found);
    }
    public List<Entry> all() { return entries; }
    public List<Entry> search(EquipmentSlot slot,String query) { return entries.stream().filter(e->e.slot==slot && e.matches(query)).toList(); }
    public Entry get(String id) { return entries.stream().filter(e->e.id.equals(id)).findFirst().orElse(null); }
    public List<Entry> set(Entry selected) {
        List<Entry> family=entries.stream().filter(e->e.family.equals(selected.family)).toList();
        Set<EquipmentSlot> seen=new HashSet<>();
        for(Entry e:family) if(!seen.add(e.slot)) return List.of(selected);
        return family;
    }
}
