package dev.transmogify;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(TransmogifyArmour.ID)
public final class TransmogifyArmour {
    public static final String ID = "transmogifyarmour";
    public static final Logger LOG = LogUtils.getLogger();
    public TransmogifyArmour() { TransmogNetwork.register(); }
}
