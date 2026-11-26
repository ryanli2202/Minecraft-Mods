package dev.nordui.qa;
import dev.nordui.NordUI;
import net.minecraft.gametest.framework.*;
import net.minecraftforge.gametest.*;
import java.nio.file.*;

@GameTestHolder(NordUI.ID)
@PrefixGameTestTemplate(false)
public final class DedicatedTests {
    @GameTest(template="empty",timeoutTicks=400)
    public static void vault(GameTestHelper helper){try{StorageChecks.run(helper.getLevel());Files.createDirectories(Path.of("../verification"));Files.writeString(Path.of("../verification/dedicated-tests.txt"),StorageChecks.REPORT);helper.succeed();}catch(Throwable e){helper.fail(e.toString());}}
}
