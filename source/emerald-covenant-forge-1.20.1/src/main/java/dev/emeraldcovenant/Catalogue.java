package dev.emeraldcovenant;

import com.google.gson.*;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;

/** Server-owned catalogue. No item, price, NBT or unlock data supplied by a buyer is trusted. */
public final class Catalogue {
    public final List<Offer> offers;
    public final Map<String,Offer> byKey;
    public final List<String> warnings;
    private final Map<String,Offer> building=new TreeMap<>();
    private JsonObject overrides=new JsonObject();
    private final List<String> problems=new ArrayList<>();
    private static final Pattern WEAPON=Pattern.compile("(^|_)(sword|greatsword|longsword|shortsword|blade|dagger|katana|rapier|saber|sabre|scimitar|cutlass|cleaver|scythe|sickle|glaive|halberd|spear|pike|lance|trident|axe|greataxe|battleaxe|mace|hammer|warhammer|club|flail|fist|gauntlet|glove|claw|bow|longbow|crossbow|gun|musket|pistol|rifle|cannon|staff|stave|wand|scepter|sceptre|shield|chakram|boomerang|whip|quarterstaff)(_|$)");
    private static final Pattern PIECE=Pattern.compile("(^|_)(helmet|helm|head|headpiece|hood|hat|crown|mask|chestplate|chest|chestpiece|tunic|robe|robes|cuirass|breastplate|leggings|legs|pants|greaves|boots|feet|shoes|sabatons)(_|$)");

    public Catalogue() {
        readOverrides();
        for (Item item:ForgeRegistries.ITEMS) {
            ResourceLocation id=ForgeRegistries.ITEMS.getKey(item);
            if(item==Items.AIR||id==null)continue;
            try {
                ItemStack stack=item.getDefaultInstance().copy();
                Category category=classify(stack,id);
                JsonObject custom=override("item:"+id,id.toString());
                if(custom.has("category"))category=Category.valueOf(custom.get("category").getAsString().toUpperCase(Locale.ROOT));
                if(category!=null)add("item:"+id,stack,category,0,-1,id.getNamespace());
            }catch(Exception ex){warn("Skipped "+id+": "+ex.getMessage());}
        }
        SpellCompat.addSpells(this);
        if(overrides.has("extraOffers"))for(JsonElement e:overrides.getAsJsonArray("extraOffers"))try {
            JsonObject o=e.getAsJsonObject();String key="custom:"+o.get("key").getAsString();
            ResourceLocation id=new ResourceLocation(o.get("item").getAsString());
            if(!ForgeRegistries.ITEMS.containsKey(id))continue;
            ItemStack stack=ForgeRegistries.ITEMS.getValue(id).getDefaultInstance();
            if(o.has("nbt"))stack.setTag(TagParser.parseTag(o.get("nbt").getAsString()));
            overrides.add(key,o);
            add(key,stack,Category.valueOf(o.get("category").getAsString().toUpperCase(Locale.ROOT)),0,-1,id.getNamespace());
        }catch(Exception ex){warn("Invalid extra offer: "+ex.getMessage());}
        Map<String,Map<Integer,Double>> sets=new HashMap<>();
        for(Offer e:building.values())if(e.category()==Category.ARMOUR)
            sets.computeIfAbsent(e.set(), k->new HashMap<>()).merge(e.slot(),e.protection(),Math::max);
        List<Offer> finished=new ArrayList<>();
        for(Offer e:building.values()) {
            if(e.category()==Category.ARMOUR) {
                double total=sets.get(e.set()).values().stream().mapToDouble(Double::doubleValue).sum();
                JsonObject o=override(e.key(),ForgeRegistries.ITEMS.getKey(e.stack().getItem()).toString());
                total=number(o,"setProtection",total);
                e=e.withSetProtection(total);
                if(ShopConfig.REQUIRE_BEARINGS.get()&&!o.has("requiredBearings")&&total>=15)
                    e=e.withRequired(e.required()|2|(total>=24?8:0));
            }
            finished.add(e);
        }
        finished.sort(comparator());
        offers=List.copyOf(finished);var map=new HashMap<String,Offer>();offers.forEach(e->map.put(e.key(),e));byKey=Map.copyOf(map);
        warnings=List.copyOf(problems);
        Covenant.LOG.info("Emerald Covenant indexed {} offers: {}", offers.size(), counts());
    }

    private void readOverrides() {
        Path path=FMLPaths.CONFIGDIR.get().resolve("emeraldcovenant-catalogue.json");
        try {
            if(Files.exists(path))overrides=JsonParser.parseString(Files.readString(path)).getAsJsonObject();
            else Files.writeString(path,"{\n  \"extraOffers\": []\n}\n");
        }catch(Exception e){warn("Cannot read catalogue overrides: "+e.getMessage());}
    }
    private JsonObject override(String key,String item) {
        JsonObject result=new JsonObject();
        if(overrides.has(item)&&overrides.get(item).isJsonObject())overrides.getAsJsonObject(item).entrySet().forEach(e->result.add(e.getKey(),e.getValue()));
        if(overrides.has(key)&&overrides.get(key).isJsonObject())overrides.getAsJsonObject(key).entrySet().forEach(e->result.add(e.getKey(),e.getValue()));
        return result;
    }
    public void warn(String message){problems.add(message);Covenant.LOG.warn(message);}
    public void add(String key,ItemStack stack,Category category,int spellLevel,int suppliedRarity,String source) {
        var id=ForgeRegistries.ITEMS.getKey(stack.getItem());
        JsonObject o=override(key,id.toString());
        if(o.has("disabled")&&o.get("disabled").getAsBoolean())return;
        if(o.has("nbt"))try {stack.setTag(TagParser.parseTag(o.get("nbt").getAsString()));}catch(Exception ex){warn("Invalid NBT for "+key);return;}
        stack.setCount(1);
        if(o.has("category"))category=Category.valueOf(o.get("category").getAsString().toUpperCase(Locale.ROOT));
        int rarity=(int)number(o,"rarity",suppliedRarity<0?stack.getRarity().ordinal():suppliedRarity);
        rarity=Math.max(0,Math.min(4,rarity));
        double damage=attribute(stack,EquipmentSlot.MAINHAND,Attributes.ATTACK_DAMAGE,1);
        if(damage==1 && (stack.getItem() instanceof ProjectileWeaponItem || id.getPath().matches(".*(bow|gun|musket|pistol|rifle).*")))damage=0;
        damage=number(o,"attackDamage",damage);
        EquipmentSlot slot=stack.getItem() instanceof ArmorItem armor?armor.getEquipmentSlot():EquipmentSlot.CHEST;
        if(!(stack.getItem() instanceof ArmorItem))for(var s:EquipmentSlot.values())if(s.getType()==EquipmentSlot.Type.ARMOR && attribute(stack,s,Attributes.ARMOR,0)>0){slot=s;break;}
        double protection=number(o,"protection",stack.getItem() instanceof HorseArmorItem horse?horse.getProtection():attribute(stack,slot,Attributes.ARMOR,0));
        String set=id.getNamespace()+":"+PIECE.matcher(id.getPath()).replaceAll("_").replaceAll("^_+|_+$","");
        if(o.has("armorSet"))set=o.get("armorSet").getAsString();
        int bit=stack.getItem() instanceof BearingItem b?b.bit:0;
        int required=0;
        if(ShopConfig.REQUIRE_BEARINGS.get()&&category.bit!=0) {
            if(rarity>0||damage>6||protection>=5||spellLevel>2||category==Category.SPELLBOOKS)required|=category.bit;
            if(rarity>=2||damage>=12||spellLevel>=6)required|=8;
        }
        if(o.has("requiredBearings"))required=o.get("requiredBearings").getAsInt()&15;
        double base=category==Category.BEARINGS?(bit==8?128:48)
            :Prices.equipment(category,rarity,damage,protection,stack.getMaxDamage(),spellLevel);
        if(category==Category.FOOD) {
            var food=stack.getFoodProperties(null);
            base=food==null?1:Math.max(1,Math.ceil(food.getNutrition()/4.0));
            // Even rare meals cost much less than equipment. Effects add a small premium.
            if(food!=null&&!food.getEffects().isEmpty())base+=2;
            base=Math.min(12,base*(1+rarity*.5));
        }
        double requestedPrice=number(o,"price",base*ShopConfig.PRICE_MULTIPLIER.get());
        if(o.has("price")&&requestedPrice<1)throw new IllegalArgumentException("Price must be positive for "+key);
        int price=Prices.cap(requestedPrice,ShopConfig.MAX_PRICE.get());
        building.put(key,new Offer(key,stack.copy(),category,price,rarity,damage,protection,0,set,slot.getIndex(),spellLevel,required,bit,source));
    }
    public static double attribute(ItemStack stack,EquipmentSlot slot,Attribute attribute,double base) {
        double add=0,totalBase=0,multiply=1;
        for(AttributeModifier m:stack.getAttributeModifiers(slot).get(attribute))switch(m.getOperation()) {
            case ADDITION -> add+=m.getAmount();
            case MULTIPLY_BASE -> totalBase+=m.getAmount();
            case MULTIPLY_TOTAL -> multiply*=1+m.getAmount();
        }
        double value=(base+add)*(1+totalBase)*multiply;
        return Double.isFinite(value)?Math.max(0,value):0;
    }
    private static double number(JsonObject o,String key,double fallback){double d=o.has(key)?o.get(key).getAsDouble():fallback;return Double.isFinite(d)?d:fallback;}
    public static Category classify(ItemStack stack,ResourceLocation id) {
        Item item=stack.getItem();String name=id.getPath();
        if(item instanceof BearingItem)return Category.BEARINGS;
        for(Category c:Category.values())if(stack.getTags().anyMatch(t->t.location().equals(new ResourceLocation(Covenant.ID,c.name().toLowerCase(Locale.ROOT)))))return c;
        if(SpellCompat.isBook(item)||name.matches(".*(spell_?book|grimoire|tome|codex).*"))return Category.SPELLBOOKS;
        if(id.toString().equals("irons_spellbooks:scroll"))return null; // Add populated variants through the spell registry.
        if(SpellCompat.isScroll(item)||name.matches(".*(spell_scroll|scroll_of_|spell_rune|spell_gem).*"))return Category.SPELLS;
        if(item instanceof ArmorItem || item instanceof HorseArmorItem)return Category.ARMOUR;
        for(var slot:EquipmentSlot.values())if(slot.getType()==EquipmentSlot.Type.ARMOR&&attribute(stack,slot,Attributes.ARMOR,0)>0)return Category.ARMOUR;
        if(item.isEdible())return Category.FOOD;
        if(item instanceof SwordItem||item instanceof AxeItem||item instanceof TridentItem||item instanceof ProjectileWeaponItem||item instanceof ShieldItem)return Category.WEAPONS;
        if(stack.getTags().anyMatch(t->t.location().getPath().matches(".*(weapons|swords|bows|spears|daggers|shields|staves).*")))return Category.WEAPONS;
        if(!(item instanceof DiggerItem)&&!(item instanceof HoeItem)&&attribute(stack,EquipmentSlot.MAINHAND,Attributes.ATTACK_DAMAGE,1)>1)return Category.WEAPONS;
        if(!(item instanceof BlockItem)&&!(item instanceof SpawnEggItem)&&WEAPON.matcher(name).find()&&!name.matches(".*(fragment|shard|head|handle|mold|pattern|upgrade|template|part|bullet|ammo|mount|stand|display).*"))return Category.WEAPONS;
        return null;
    }
    public static Comparator<Offer> comparator() {
        return Comparator.comparing(Offer::category).thenComparingDouble(e->switch(e.category()) {
            case WEAPONS -> -e.damage();case ARMOUR -> -e.setProtection();case SPELLS -> -e.spellLevel();case SPELLBOOKS -> -e.rarity();default -> e.price();
        }).thenComparing(e->e.category()==Category.ARMOUR?e.set():"").thenComparingInt(e->e.category()==Category.ARMOUR?-e.slot():0).thenComparing(Offer::key);
    }
    public Map<Category,Long> counts(){var m=new EnumMap<Category,Long>(Category.class);for(var c:Category.values())m.put(c,offers.stream().filter(e->e.category()==c).count());return m;}
}
