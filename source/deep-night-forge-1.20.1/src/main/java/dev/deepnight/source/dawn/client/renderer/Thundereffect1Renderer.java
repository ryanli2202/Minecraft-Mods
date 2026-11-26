/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modelthunder_effect_1;
import dev.deepnight.source.dawn.entity.Thundereffect1Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class Thundereffect1Renderer
extends MobRenderer<Thundereffect1Entity, Modelthunder_effect_1<Thundereffect1Entity>> {
    public Thundereffect1Renderer(EntityRendererProvider.Context context) {
        super(context, new Modelthunder_effect_1(context.bakeLayer(Modelthunder_effect_1.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<Thundereffect1Entity, Modelthunder_effect_1<Thundereffect1Entity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffers, int p_117351_, Thundereffect1Entity entity, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
                RenderType type = RenderType.eyes(this.getTextureLocation(entity));
                VertexConsumer vertexconsumer = buffers.getBuffer(type);
                ((Modelthunder_effect_1)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
            }

            @Override
            protected ResourceLocation getTextureLocation(Thundereffect1Entity entity) {
                int tick = entity.tickCount;
                if (tick % 30 < 3) {
                    return new ResourceLocation("simple_mobs:textures/entities/thunder_effect1/thunder_effect1_glow_1.png");
                }
                if (tick % 30 < 6) {
                    return new ResourceLocation("simple_mobs:textures/entities/thunder_effect1/thunder_effect1_glow_2.png");
                }
                if (tick % 30 < 9) {
                    return new ResourceLocation("simple_mobs:textures/entities/thunder_effect1/thunder_effect1_glow_3.png");
                }
                if (tick % 30 < 12) {
                    return new ResourceLocation("simple_mobs:textures/entities/thunder_effect1/thunder_effect1_glow_4.png");
                }
                if (tick % 30 < 15) {
                    return new ResourceLocation("simple_mobs:textures/entities/thunder_effect1/thunder_effect1_5.png");
                }
                return new ResourceLocation("simple_mobs:textures/entities/thunder_effect1/thunder_effect1_6.png");
            }
        });
        this.addLayer(new RenderLayer<Thundereffect1Entity, Modelthunder_effect_1<Thundereffect1Entity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Thundereffect1Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (entity.tickCount > 1) {
                    RenderType renderType = null;
                    int tick = entity.tickCount;
                    renderType = tick % 30 < 3 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/thunder_effect1/thunder_effect1_transparent_1.png")) : (tick % 30 < 6 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/thunder_effect1/thunder_effect1_transparent_2.png")) : (tick % 30 < 9 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/thunder_effect1/thunder_effect1_transparent_3.png")) : (tick % 30 < 12 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/thunder_effect1/thunder_effect1_transparent_4.png")) : RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/a.png")))));
                    VertexConsumer vertexconsumer = buffer.getBuffer(renderType);
                    ((Modelthunder_effect_1)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(Thundereffect1Entity entity) {
        int tick = entity.tickCount;
        if (tick % 30 < 3) {
            return new ResourceLocation("simple_mobs:textures/entities/thunder_effect1/thunder_effect1_glow_1.png");
        }
        if (tick % 30 < 6) {
            return new ResourceLocation("simple_mobs:textures/entities/thunder_effect1/thunder_effect1_glow_2.png");
        }
        if (tick % 30 < 9) {
            return new ResourceLocation("simple_mobs:textures/entities/thunder_effect1/thunder_effect1_glow_3.png");
        }
        if (tick % 30 < 12) {
            return new ResourceLocation("simple_mobs:textures/entities/thunder_effect1/thunder_effect1_glow_4.png");
        }
        if (tick % 30 < 15) {
            return new ResourceLocation("simple_mobs:textures/entities/thunder_effect1/thunder_effect1_5.png");
        }
        return new ResourceLocation("simple_mobs:textures/entities/thunder_effect1/thunder_effect1_6.png");
    }
}

