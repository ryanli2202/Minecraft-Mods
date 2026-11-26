/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modelthunder_effect_4;
import dev.deepnight.source.dawn.entity.Thundereffect4Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class Thundereffect4Renderer
extends MobRenderer<Thundereffect4Entity, Modelthunder_effect_4<Thundereffect4Entity>> {
    public Thundereffect4Renderer(EntityRendererProvider.Context context) {
        super(context, new Modelthunder_effect_4(context.bakeLayer(Modelthunder_effect_4.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<Thundereffect4Entity, Modelthunder_effect_4<Thundereffect4Entity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffers, int p_117351_, Thundereffect4Entity entity, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
                RenderType type = RenderType.eyes(this.getTextureLocation(entity));
                VertexConsumer vertexconsumer = buffers.getBuffer(type);
                ((Modelthunder_effect_4)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
            }

            @Override
            protected ResourceLocation getTextureLocation(Thundereffect4Entity entity) {
                int tick = entity.tickCount;
                if (tick % 80 < 8) {
                    return new ResourceLocation("simple_mobs:textures/entities/thunder_effect_4/thunder_effect_4_1.png");
                }
                if (tick % 80 < 30) {
                    return new ResourceLocation("simple_mobs:textures/entities/thunder_effect_4/thunder_effect_4_2.png");
                }
                if (tick % 80 < 33) {
                    return new ResourceLocation("simple_mobs:textures/entities/thunder_effect_4/thunder_effect_4_3.png");
                }
                return new ResourceLocation("simple_mobs:textures/entities/a.png");
            }
        });
        this.addLayer(new RenderLayer<Thundereffect4Entity, Modelthunder_effect_4<Thundereffect4Entity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Thundereffect4Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (entity.tickCount > 1) {
                    RenderType renderType = null;
                    int tick = entity.tickCount;
                    renderType = tick % 80 < 8 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/a.png")) : (tick % 80 < 30 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/thunder_effect_4/thunder_effect_4_2_trans.png")) : (tick % 80 < 33 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/thunder_effect_4/thunder_effect_4_3_trans.png")) : (tick % 80 < 36 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/thunder_effect_4/thunder_effect_4_4.png")) : RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/thunder_effect_4/thunder_effect_4_5.png")))));
                    VertexConsumer vertexconsumer = buffer.getBuffer(renderType);
                    ((Modelthunder_effect_4)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(Thundereffect4Entity entity) {
        int tick = entity.tickCount;
        if (tick % 80 < 8) {
            return new ResourceLocation("simple_mobs:textures/entities/thunder_effect_4/thunder_effect_4_1.png");
        }
        if (tick % 80 < 30) {
            return new ResourceLocation("simple_mobs:textures/entities/thunder_effect_4/thunder_effect_4_2.png");
        }
        if (tick % 80 < 33) {
            return new ResourceLocation("simple_mobs:textures/entities/thunder_effect_4/thunder_effect_4_3.png");
        }
        return new ResourceLocation("simple_mobs:textures/entities/a.png");
    }
}

