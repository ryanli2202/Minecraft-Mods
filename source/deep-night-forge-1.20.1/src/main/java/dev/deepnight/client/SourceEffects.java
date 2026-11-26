package dev.deepnight.client;
import dev.deepnight.source.SourceStun;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
@Mod.EventBusSubscriber(modid="deepnight",value=Dist.CLIENT)
public final class SourceEffects {
 @SubscribeEvent public static void input(MovementInputUpdateEvent e){if(SourceStun.stunned(e.getEntity())){var input=e.getInput();input.forwardImpulse=0;input.leftImpulse=0;input.jumping=false;}}
 @SubscribeEvent public static void camera(ViewportEvent.ComputeCameraAngles e){
  var player=Minecraft.getInstance().player;if(player==null||!player.hasEffect(SourceStun.TREMOR.get()))return;
  var effect=player.getEffect(SourceStun.TREMOR.get());double time=player.tickCount+e.getPartialTick();float power=Math.min(3,0.5f+effect.getAmplifier()*0.4f);
  e.setPitch(e.getPitch()+(float)Math.sin(time*2.7)*power);e.setRoll(e.getRoll()+(float)Math.cos(time*2.1)*power);
 }
}
