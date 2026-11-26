/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modelnotch_groundeffect1;
import dev.deepnight.source.dawn.entity.Notchgroundeffect1Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class Notchgroundeffect1Renderer
extends MobRenderer<Notchgroundeffect1Entity, Modelnotch_groundeffect1<Notchgroundeffect1Entity>> {
    public Notchgroundeffect1Renderer(EntityRendererProvider.Context context) {
        super(context, new Modelnotch_groundeffect1(context.bakeLayer(Modelnotch_groundeffect1.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<Notchgroundeffect1Entity, Modelnotch_groundeffect1<Notchgroundeffect1Entity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Notchgroundeffect1Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (entity.tickCount > 1) {
                    RenderType renderType = null;
                    int tick = entity.tickCount;
                    renderType = tick % 84 < 3 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/notch_groundeffect1/notch_groundeffect1_1.png")) : (tick % 84 < 60 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/notch_groundeffect1/notch_groundeffect1_2.png")) : (tick % 84 < 70 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/notch_groundeffect1/notch_groundeffect1_3.png")) : (tick % 84 < 75 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/notch_groundeffect1/notch_groundeffect1_4.png")) : RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/notch_groundeffect1/notch_groundeffect1_5.png")))));
                    VertexConsumer vertexconsumer = buffer.getBuffer(renderType);
                    ((Modelnotch_groundeffect1)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(Notchgroundeffect1Entity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/a.png");
    }
}

