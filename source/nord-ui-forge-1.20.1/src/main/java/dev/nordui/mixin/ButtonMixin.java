package dev.nordui.mixin;

import dev.nordui.client.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Explicit official + SRG selectors keep the 1.20.1 dev and production targets identical. */
@Mixin(AbstractButton.class)
public abstract class ButtonMixin {
    @Inject(method={"renderWidget","m_87963_"},at=@At("HEAD"),cancellable=true,remap=false)
    private void nordui$button(GuiGraphics g,int mx,int my,float partial,CallbackInfo ci) {
        if(ClientConfig.active()&&ClientConfig.BUTTONS.get()&&dev.nordui.compat.UiCompat.themeScreen(net.minecraft.client.Minecraft.getInstance().screen)) {
            Theme.button((AbstractButton)(Object)this,g);ci.cancel();
        }
    }
}
