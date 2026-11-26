/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modelnotch_flash3;
import dev.deepnight.source.dawn.entity.Notchflash3Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class Notchflash3Renderer
extends MobRenderer<Notchflash3Entity, Modelnotch_flash3<Notchflash3Entity>> {
    public Notchflash3Renderer(EntityRendererProvider.Context context) {
        super(context, new Modelnotch_flash3(context.bakeLayer(Modelnotch_flash3.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<Notchflash3Entity, Modelnotch_flash3<Notchflash3Entity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffers, int p_117351_, Notchflash3Entity entity, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
                RenderType type = RenderType.eyes(this.getTextureLocation(entity));
                VertexConsumer vertexconsumer = buffers.getBuffer(type);
                ((Modelnotch_flash3)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
            }

            @Override
            protected ResourceLocation getTextureLocation(Notchflash3Entity entity) {
                int tick = entity.tickCount;
                if (tick % 25 < 3) {
                    return new ResourceLocation("simple_mobs:textures/entities/notch_flash3/notch_flash3_glow_1.png");
                }
                if (tick % 25 < 6) {
                    return new ResourceLocation("simple_mobs:textures/entities/notch_flash3/notch_flash3_glow_2.png");
                }
                if (tick % 25 < 9) {
                    return new ResourceLocation("simple_mobs:textures/entities/notch_flash3/notch_flash3_glow_3.png");
                }
                return new ResourceLocation("simple_mobs:textures/entities/notch_flash3/notch_flash3_glow_4.png");
            }
        });
        this.addLayer(new RenderLayer<Notchflash3Entity, Modelnotch_flash3<Notchflash3Entity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Notchflash3Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (entity.tickCount > 1) {
                    RenderType renderType = null;
                    int tick = entity.tickCount;
                    renderType = tick % 25 < 3 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/notch_flash3/notch_flash3_transparent_1.png")) : (tick % 25 < 6 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/notch_flash3/notch_flash3_transparent_2.png")) : (tick % 25 < 9 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/notch_flash3/notch_flash3_transparent_3.png")) : RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/notch_flash3/notch_flash3_transparent_4.png"))));
                    VertexConsumer vertexconsumer = buffer.getBuffer(renderType);
                    ((Modelnotch_flash3)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(Notchflash3Entity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/a.png");
    }
}

