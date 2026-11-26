package dev.thedeepnightnpcs.trade;

import dev.thedeepnightnpcs.DeepNightNpcs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.saveddata.SavedData;
import java.util.*;

/** Stock is world- and player-specific; neither reconnecting nor visiting another Jorge resets it. */
public final class JorgeStock extends SavedData {
    public static final class Stock {
        public MerchantOffers ingots, books;
        public long nextIngots, nextBooks;
        public CompoundTag save() {
            CompoundTag n=new CompoundTag();
            if(ingots!=null)n.put("ingots",ingots.createTag());
            if(books!=null)n.put("books",books.createTag());
            n.putLong("nextIngots",nextIngots);n.putLong("nextBooks",nextBooks);return n;
        }
        public static Stock load(CompoundTag n) {
            Stock s=new Stock();
            if(n.contains("ingots"))s.ingots=new MerchantOffers(n.getCompound("ingots"));
            if(n.contains("books"))s.books=new MerchantOffers(n.getCompound("books"));
            s.nextIngots=n.getLong("nextIngots");s.nextBooks=n.getLong("nextBooks");return s;
        }
    }
    private final Map<UUID,Stock> players=new HashMap<>();
    public static JorgeStock get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(JorgeStock::load,JorgeStock::new,"thedeepnightnpcs_jorge_stock");
    }
    public Stock player(UUID player) {return players.computeIfAbsent(player,id->{setDirty();return new Stock();});}
    public static JorgeStock load(CompoundTag n) {
        JorgeStock data=new JorgeStock();
        for(String id:n.getAllKeys())try{data.players.put(UUID.fromString(id),Stock.load(n.getCompound(id)));}
        catch(RuntimeException error){DeepNightNpcs.LOG.warn("Skipping invalid Jorge stock record {}",id,error);}
        return data;
    }
    @Override public CompoundTag save(CompoundTag n){players.forEach((id,stock)->n.put(id.toString(),stock.save()));return n;}
}
