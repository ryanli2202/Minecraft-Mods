/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.ModelLosteyeEffect1;
import dev.deepnight.source.dawn.entity.LosteyeEffect1Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class LosteyeEffect1Renderer
extends MobRenderer<LosteyeEffect1Entity, ModelLosteyeEffect1<LosteyeEffect1Entity>> {
    public LosteyeEffect1Renderer(EntityRendererProvider.Context context) {
        super(context, new ModelLosteyeEffect1(context.bakeLayer(ModelLosteyeEffect1.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<LosteyeEffect1Entity, ModelLosteyeEffect1<LosteyeEffect1Entity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffers, int p_117351_, LosteyeEffect1Entity entity, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
                RenderType type = RenderType.eyes(this.getTextureLocation(entity));
                VertexConsumer vertexconsumer = buffers.getBuffer(type);
                ((ModelLosteyeEffect1)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
            }

            @Override
            protected ResourceLocation getTextureLocation(LosteyeEffect1Entity entity) {
                int tick = entity.tickCount;
                if (tick % 27 < 5) {
                    return new ResourceLocation("simple_mobs:textures/entities/losteye_effect1/losteye_effect_glow_1.png");
                }
                if (tick % 27 < 10) {
                    return new ResourceLocation("simple_mobs:textures/entities/losteye_effect1/losteye_effect_glow_2.png");
                }
                if (tick % 27 < 15) {
                    return new ResourceLocation("simple_mobs:textures/entities/losteye_effect1/losteye_effect_glow_3.png");
                }
                if (tick % 27 < 20) {
                    return new ResourceLocation("simple_mobs:textures/entities/losteye_effect1/losteye_effect_glow_4.png");
                }
                return new ResourceLocation("simple_mobs:textures/entities/losteye_effect1/losteye_effect_glow_5.png");
            }
        });
        this.addLayer(new RenderLayer<LosteyeEffect1Entity, ModelLosteyeEffect1<LosteyeEffect1Entity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, LosteyeEffect1Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (entity.tickCount > 1) {
                    RenderType renderType = null;
                    int tick = entity.tickCount;
                    renderType = tick % 27 < 5 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/losteye_effect1/losteye_effect_trans_1.png")) : (tick % 27 < 10 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/losteye_effect1/losteye_effect_trans_2.png")) : (tick % 27 < 15 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/losteye_effect1/losteye_effect_trans_3.png")) : (tick % 27 < 20 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/losteye_effect1/losteye_effect_trans_4.png")) : RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/losteye_effect1/losteye_effect_trans_5.png")))));
                    VertexConsumer vertexconsumer = buffer.getBuffer(renderType);
                    ((ModelLosteyeEffect1)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(LosteyeEffect1Entity entity) {
        int tick = entity.tickCount;
        if (tick % 27 < 5) {
            return new ResourceLocation("simple_mobs:textures/entities/losteye_effect1/losteye_effect_glow_1.png");
        }
        if (tick % 27 < 10) {
            return new ResourceLocation("simple_mobs:textures/entities/losteye_effect1/losteye_effect_glow_2.png");
        }
        if (tick % 27 < 15) {
            return new ResourceLocation("simple_mobs:textures/entities/losteye_effect1/losteye_effect_glow_3.png");
        }
        if (tick % 27 < 20) {
            return new ResourceLocation("simple_mobs:textures/entities/losteye_effect1/losteye_effect_glow_4.png");
        }
        return new ResourceLocation("simple_mobs:textures/entities/losteye_effect1/losteye_effect_glow_5.png");
    }
}

