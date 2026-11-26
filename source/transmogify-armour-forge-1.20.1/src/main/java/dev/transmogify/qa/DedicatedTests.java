package dev.transmogify.qa;
import dev.transmogify.TransmogifyArmour;
import net.minecraft.gametest.framework.*;
import net.minecraftforge.gametest.*;
import java.nio.file.*;

@GameTestHolder(TransmogifyArmour.ID)
@PrefixGameTestTemplate(false)
public final class DedicatedTests {
    @GameTest(template="empty",timeoutTicks=400)
    public static void cosmetics(GameTestHelper helper) {
        try {
            ArmourChecks.run(helper.getLevel());
            Files.createDirectories(Path.of("../verification"));
            Files.writeString(Path.of("../verification/dedicated-tests.txt"),ArmourChecks.REPORT);
            helper.succeed();
        } catch(Throwable ex) { helper.fail(ex.toString()); }
    }
}
