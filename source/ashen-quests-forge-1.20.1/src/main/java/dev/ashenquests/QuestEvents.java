package dev.ashenquests;

import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public final class QuestEvents {
    @SubscribeEvent public static void discussion(dev.thedeepnightnpcs.quest.QuestDialogueEvent e) { QuestConversations.handle(e); }
    @SubscribeEvent public static void conversation(dev.thedeepnightnpcs.quest.QuestConversationEvent e) { QuestServer.openConversation(e.player(),e.npc()); }
    @SubscribeEvent public static void craft(PlayerEvent.ItemCraftedEvent e) { if(e.getEntity() instanceof ServerPlayer p)QuestEngine.count(p,"craft",ForgeRegistries.ITEMS.getKey(e.getCrafting().getItem()).toString(),e.getCrafting().getCount()); }
    @SubscribeEvent public static void smelt(PlayerEvent.ItemSmeltedEvent e) { if(e.getEntity() instanceof ServerPlayer p)QuestEngine.count(p,"smelt",ForgeRegistries.ITEMS.getKey(e.getSmelting().getItem()).toString(),e.getSmelting().getCount()); }
    @SubscribeEvent public static void tame(AnimalTameEvent e) { if(!e.isCanceled()&&e.getTamer() instanceof ServerPlayer p)QuestEngine.count(p,"tame",ForgeRegistries.ENTITY_TYPES.getKey(e.getAnimal().getType()).toString(),1); }
    @SubscribeEvent public static void interact(PlayerInteractEvent.EntityInteract e) { if(!e.isCanceled()&&e.getEntity() instanceof ServerPlayer p)QuestEngine.count(p,"interact",ForgeRegistries.ENTITY_TYPES.getKey(e.getTarget().getType()).toString(),1); }
    @SubscribeEvent public static void fish(ItemFishedEvent e) { if(!e.isCanceled()&&e.getEntity() instanceof ServerPlayer p)for(var drop:e.getDrops())QuestEngine.count(p,"fish",ForgeRegistries.ITEMS.getKey(drop.getItem()).toString(),drop.getCount()); }
    @SubscribeEvent public static void tick(TickEvent.PlayerTickEvent e) {
        if(e.phase!=TickEvent.Phase.END||!(e.player instanceof ServerPlayer p)||p.tickCount%20!=0) return;
        var totals=QuestEngine.journal(p).getCompound("totals");String dim=p.level().dimension().location().toString();
        if(totals.getInt("visit/"+dim)==0)QuestEngine.count(p,"visit",dim,1);
        p.level().getBiome(p.blockPosition()).unwrapKey().ifPresent(key->{String biome=key.location().toString();if(totals.getInt("biome/"+biome)==0)QuestEngine.count(p,"biome",biome,1);});
        QuestServer.tick(p);
    }
    @SubscribeEvent public static void login(PlayerEvent.PlayerLoggedInEvent e) { if(e.getEntity() instanceof ServerPlayer p)p.sendSystemMessage(Component.literal("The Ashen Covenant: speak to Eldara for main quests. Speak to the named quest giver to accept and turn in side quests. Press - to track your progress.")); }
    @SubscribeEvent public static void logout(PlayerEvent.PlayerLoggedOutEvent e) { QuestServer.sessions.remove(e.getEntity().getUUID()); }
    @SubscribeEvent public static void stop(ServerStoppedEvent e) { QuestServer.sessions.clear(); }
    @SubscribeEvent public static void commands(RegisterCommandsEvent e) {
        e.getDispatcher().register(Commands.literal("ashenquests").executes(c->{QuestServer.open(c.getSource().getPlayerOrException());return 1;})
            .then(Commands.literal("audit").requires(s->s.hasPermission(2)).executes(c->{var p=c.getSource().getPlayerOrException();long missing=Campaign.quests.values().stream().filter(q->!QuestEngine.missing(p,q,new java.util.HashSet<>()).isEmpty()).count();c.getSource().sendSuccess(()->Component.literal(Campaign.quests.size()+" quests; "+missing+" need missing content or prerequisites. See journal Requirements."),false);return 1;})));
    }
}
