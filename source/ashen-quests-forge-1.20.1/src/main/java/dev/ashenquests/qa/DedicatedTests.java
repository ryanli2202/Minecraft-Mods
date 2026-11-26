package dev.ashenquests.qa;

import com.mojang.authlib.GameProfile;
import dev.ashenquests.Chronicles;
import net.minecraft.gametest.framework.*;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.gametest.*;
import java.nio.file.*;
import java.util.UUID;

@GameTestHolder(Chronicles.ID)
@PrefixGameTestTemplate(false)
public final class DedicatedTests {
    @GameTest(template="empty",timeoutTicks=400)
    public static void campaignAndTransactions(GameTestHelper helper){
        try{QuestChecks.run(FakePlayerFactory.get(helper.getLevel(),new GameProfile(UUID.randomUUID(),"QuestQA")));IntegrationChecks.run(FakePlayerFactory.get(helper.getLevel(),new GameProfile(UUID.randomUUID(),"IntegrationQA")));ConversationChecks.run(FakePlayerFactory.get(helper.getLevel(),new GameProfile(UUID.randomUUID(),"ConversationQA")));EyeChecks.run(FakePlayerFactory.get(helper.getLevel(),new GameProfile(UUID.randomUUID(),"EyeQA")));Files.createDirectories(Path.of("../verification"));Files.writeString(Path.of("../verification/dedicated-server-tests.txt"),QuestChecks.REPORT);helper.succeed();}catch(Exception ex){Chronicles.LOG.error("Quest tests failed",ex);helper.fail(ex.toString());}
    }
}
