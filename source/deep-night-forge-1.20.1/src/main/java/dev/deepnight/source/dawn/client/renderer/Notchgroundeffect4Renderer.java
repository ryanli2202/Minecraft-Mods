/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modelnotch_groundeffect4;
import dev.deepnight.source.dawn.entity.Notchgroundeffect4Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class Notchgroundeffect4Renderer
extends MobRenderer<Notchgroundeffect4Entity, Modelnotch_groundeffect4<Notchgroundeffect4Entity>> {
    public Notchgroundeffect4Renderer(EntityRendererProvider.Context context) {
        super(context, new Modelnotch_groundeffect4(context.bakeLayer(Modelnotch_groundeffect4.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<Notchgroundeffect4Entity, Modelnotch_groundeffect4<Notchgroundeffect4Entity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffers, int p_117351_, Notchgroundeffect4Entity entity, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
                RenderType type = RenderType.eyes(this.getTextureLocation(entity));
                VertexConsumer vertexconsumer = buffers.getBuffer(type);
                ((Modelnotch_groundeffect4)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
            }

            @Override
            protected ResourceLocation getTextureLocation(Notchgroundeffect4Entity entity) {
                int tick = entity.tickCount;
                if (tick % 85 < 40) {
                    return new ResourceLocation("simple_mobs:textures/entities/a.png");
                }
                if (tick % 85 < 42) {
                    return new ResourceLocation("simple_mobs:textures/entities/notch_groundeffect4/notch_groundeffect4_glow_4.png");
                }
                if (tick % 85 < 44) {
                    return new ResourceLocation("simple_mobs:textures/entities/notch_groundeffect4/notch_groundeffect4_glow_5.png");
                }
                if (tick % 85 < 46) {
                    return new ResourceLocation("simple_mobs:textures/entities/notch_groundeffect4/notch_groundeffect4_glow_6.png");
                }
                if (tick % 85 < 48) {
                    return new ResourceLocation("simple_mobs:textures/entities/notch_groundeffect4/notch_groundeffect4_glow_7.png");
                }
                if (tick % 85 < 50) {
                    return new ResourceLocation("simple_mobs:textures/entities/notch_groundeffect4/notch_groundeffect4_glow_8.png");
                }
                return new ResourceLocation("simple_mobs:textures/entities/a.png");
            }
        });
        this.addLayer(new RenderLayer<Notchgroundeffect4Entity, Modelnotch_groundeffect4<Notchgroundeffect4Entity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Notchgroundeffect4Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (entity.tickCount > 1) {
                    RenderType renderType = null;
                    int tick = entity.tickCount;
                    renderType = tick % 85 < 3 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/notch_groundeffect4/notch_groundeffect4_1.png")) : (tick % 85 < 6 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/notch_groundeffect4/notch_groundeffect4_2.png")) : (tick % 85 < 40 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/notch_groundeffect4/notch_groundeffect4_3.png")) : (tick % 85 < 42 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/notch_groundeffect4/notch_groundeffect4_transparent_4.png")) : (tick % 85 < 44 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/notch_groundeffect4/notch_groundeffect4_transparent_5.png")) : (tick % 85 < 46 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/notch_groundeffect4/notch_groundeffect4_transparent_6.png")) : (tick % 85 < 48 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/notch_groundeffect4/notch_groundeffect4_transparent_7.png")) : (tick % 85 < 50 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/notch_groundeffect4/notch_groundeffect4_transparent_8.png")) : (tick % 85 < 70 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/notch_groundeffect4/notch_groundeffect4_9.png")) : (tick % 85 < 75 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/notch_groundeffect4/notch_groundeffect4_10.png")) : RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/notch_groundeffect4/notch_groundeffect4_11.png")))))))))));
                    VertexConsumer vertexconsumer = buffer.getBuffer(renderType);
                    ((Modelnotch_groundeffect4)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(Notchgroundeffect4Entity entity) {
        int tick = entity.tickCount;
        if (tick % 85 < 40) {
            return new ResourceLocation("simple_mobs:textures/entities/a.png");
        }
        if (tick % 85 < 42) {
            return new ResourceLocation("simple_mobs:textures/entities/notch_groundeffect4/notch_groundeffect4_glow_4.png");
        }
        if (tick % 85 < 44) {
            return new ResourceLocation("simple_mobs:textures/entities/notch_groundeffect4/notch_groundeffect4_glow_5.png");
        }
        if (tick % 85 < 46) {
            return new ResourceLocation("simple_mobs:textures/entities/notch_groundeffect4/notch_groundeffect4_glow_6.png");
        }
        if (tick % 85 < 48) {
            return new ResourceLocation("simple_mobs:textures/entities/notch_groundeffect4/notch_groundeffect4_glow_7.png");
        }
        if (tick % 85 < 50) {
            return new ResourceLocation("simple_mobs:textures/entities/notch_groundeffect4/notch_groundeffect4_glow_8.png");
        }
        return new ResourceLocation("simple_mobs:textures/entities/a.png");
    }
}

