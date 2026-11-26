package dev.transmogify.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.transmogify.client.ClientWardrobe;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

/** Changes the rendering input before Forge resolves both custom armour models and textures.
 * Named + SRG selectors are pinned to Minecraft 1.20.1; no inventory or gameplay hooks. */
@Mixin(HumanoidArmorLayer.class)
public abstract class ArmourLayerMixin {
    @ModifyVariable(method={"renderArmorPiece","m_117118_"}, at=@At("STORE"), ordinal=0, remap=false)
    private ItemStack transmogifyAppearance(ItemStack original, PoseStack pose, MultiBufferSource buffers,
            LivingEntity entity, EquipmentSlot slot, int light, HumanoidModel<?> model) {
        return ClientWardrobe.rendered(original,entity,slot);
    }
}
