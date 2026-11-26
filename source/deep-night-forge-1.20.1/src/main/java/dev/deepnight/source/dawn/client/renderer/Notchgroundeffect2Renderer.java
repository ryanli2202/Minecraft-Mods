/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modelnotch_groundeffect2;
import dev.deepnight.source.dawn.entity.Notchgroundeffect2Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class Notchgroundeffect2Renderer
extends MobRenderer<Notchgroundeffect2Entity, Modelnotch_groundeffect2<Notchgroundeffect2Entity>> {
    public Notchgroundeffect2Renderer(EntityRendererProvider.Context context) {
        super(context, new Modelnotch_groundeffect2(context.bakeLayer(Modelnotch_groundeffect2.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<Notchgroundeffect2Entity, Modelnotch_groundeffect2<Notchgroundeffect2Entity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Notchgroundeffect2Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (entity.tickCount > 1) {
                    RenderType renderType = null;
                    int tick = entity.tickCount;
                    renderType = tick % 84 < 3 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/notch_groundeffect2/notch_groundeffect2_1.png")) : (tick % 84 < 60 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/notch_groundeffect2/notch_groundeffect2_2.png")) : (tick % 84 < 70 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/notch_groundeffect2/notch_groundeffect2_3.png")) : (tick % 84 < 75 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/notch_groundeffect2/notch_groundeffect2_4.png")) : RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/notch_groundeffect2/notch_groundeffect2_5.png")))));
                    VertexConsumer vertexconsumer = buffer.getBuffer(renderType);
                    ((Modelnotch_groundeffect2)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(Notchgroundeffect2Entity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/a.png");
    }
}

