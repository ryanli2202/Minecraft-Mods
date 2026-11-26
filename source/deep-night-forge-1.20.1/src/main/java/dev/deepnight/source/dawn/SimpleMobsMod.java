package dev.deepnight.source.dawn;
import java.util.function.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.SimpleChannel;
public final class SimpleMobsMod {
 public static final org.apache.logging.log4j.Logger LOGGER=org.apache.logging.log4j.LogManager.getLogger("DeepNightSource");
 public static final SimpleChannel PACKET_HANDLER=NetworkRegistry.newSimpleChannel(dev.deepnight.DeepNight.id("dawn_state"),()->"1","1"::equals,"1"::equals);
 private static int id;
 public static <T> void addNetworkMessage(Class<T> type,BiConsumer<T,FriendlyByteBuf> encoder,Function<FriendlyByteBuf,T> decoder,BiConsumer<T,Supplier<NetworkEvent.Context>> handler){PACKET_HANDLER.registerMessage(id++,type,encoder,decoder,handler,java.util.Optional.of(NetworkDirection.PLAY_TO_CLIENT));}
}
