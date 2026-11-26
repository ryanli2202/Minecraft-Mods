/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modelnotch_punch;
import dev.deepnight.source.dawn.entity.NotchPunchEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class NotchPunchRenderer
extends MobRenderer<NotchPunchEntity, Modelnotch_punch<NotchPunchEntity>> {
    public NotchPunchRenderer(EntityRendererProvider.Context context) {
        super(context, new Modelnotch_punch(context.bakeLayer(Modelnotch_punch.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<NotchPunchEntity, Modelnotch_punch<NotchPunchEntity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, NotchPunchEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (entity.tickCount > 1) {
                    RenderType renderType = null;
                    int tick = entity.tickCount;
                    renderType = tick % 8 < 2 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/notch_punch/notch_punch1.png")) : (tick % 8 < 4 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/notch_punch/notch_punch2.png")) : (tick % 8 < 6 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/notch_punch/notch_punch3.png")) : RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/notch_punch/notch_punch4.png"))));
                    VertexConsumer vertexconsumer = buffer.getBuffer(renderType);
                    ((Modelnotch_punch)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(NotchPunchEntity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/a.png");
    }
}

