package dev.emeraldcovenant.qa;

import com.mojang.authlib.GameProfile;
import dev.emeraldcovenant.Covenant;
import net.minecraft.gametest.framework.*;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.gametest.*;
import java.nio.file.*;
import java.util.UUID;

@GameTestHolder(Covenant.ID)
@PrefixGameTestTemplate(false)
public final class DedicatedTests {
    @GameTest(template="empty",timeoutTicks=400)
    public static void dedicatedCatalogueAndTransactions(GameTestHelper helper) {
        try {
            ShopChecks.run(FakePlayerFactory.get(helper.getLevel(),new GameProfile(UUID.randomUUID(),"GuildQA")));
            Files.writeString(Path.of("../verification/dedicated-server-tests.txt"),ShopChecks.REPORT.toString());helper.succeed();
        }catch(Exception e){helper.fail(e.toString());}
    }
}
