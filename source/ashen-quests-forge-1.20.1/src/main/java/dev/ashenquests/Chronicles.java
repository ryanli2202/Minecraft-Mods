package dev.ashenquests;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(Chronicles.ID)
public final class Chronicles {
    public static final String ID = "ashenquests";
    public static final Logger LOG = LogUtils.getLogger();
    public Chronicles() {
        QuestNetwork.register();
        MinecraftForge.EVENT_BUS.addListener((AddReloadListenerEvent e) -> e.addListener(new Campaign()));
        MinecraftForge.EVENT_BUS.register(QuestEvents.class);
    }
}
