/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.ModelLosteyeEffect3;
import dev.deepnight.source.dawn.entity.LosteyeEffect3Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class LosteyeEffect3Renderer
extends MobRenderer<LosteyeEffect3Entity, ModelLosteyeEffect3<LosteyeEffect3Entity>> {
    public LosteyeEffect3Renderer(EntityRendererProvider.Context context) {
        super(context, new ModelLosteyeEffect3(context.bakeLayer(ModelLosteyeEffect3.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<LosteyeEffect3Entity, ModelLosteyeEffect3<LosteyeEffect3Entity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffers, int p_117351_, LosteyeEffect3Entity entity, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
                RenderType type = RenderType.eyes(this.getTextureLocation(entity));
                VertexConsumer vertexconsumer = buffers.getBuffer(type);
                ((ModelLosteyeEffect3)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
            }

            @Override
            protected ResourceLocation getTextureLocation(LosteyeEffect3Entity entity) {
                int tick = entity.tickCount;
                if (tick % 22 < 4) {
                    return new ResourceLocation("simple_mobs:textures/entities/losteye_effect3/losteye_effect3_glow_1.png");
                }
                if (tick % 22 < 8) {
                    return new ResourceLocation("simple_mobs:textures/entities/losteye_effect3/losteye_effect3_glow_2.png");
                }
                if (tick % 22 < 12) {
                    return new ResourceLocation("simple_mobs:textures/entities/losteye_effect3/losteye_effect3_glow_3.png");
                }
                if (tick % 22 < 16) {
                    return new ResourceLocation("simple_mobs:textures/entities/losteye_effect3/losteye_effect3_glow_4.png");
                }
                return new ResourceLocation("simple_mobs:textures/entities/losteye_effect3/losteye_effect3_glow_5.png");
            }
        });
        this.addLayer(new RenderLayer<LosteyeEffect3Entity, ModelLosteyeEffect3<LosteyeEffect3Entity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, LosteyeEffect3Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (entity.tickCount > 1) {
                    RenderType renderType = null;
                    int tick = entity.tickCount;
                    if (tick % 22 < 4) {
                        renderType = RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/losteye_effect3/losteye_effect3_trans_1.png"));
                    }
                    if (tick % 22 < 8) {
                        renderType = RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/losteye_effect3/losteye_effect3_trans_2.png"));
                    }
                    if (tick % 22 < 12) {
                        renderType = RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/losteye_effect3/losteye_effect3_trans_3.png"));
                    }
                    renderType = tick % 22 < 16 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/losteye_effect3/losteye_effect3_trans_4.png")) : RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/losteye_effect3/losteye_effect3_trans_5.png"));
                    VertexConsumer vertexconsumer = buffer.getBuffer(renderType);
                    ((ModelLosteyeEffect3)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(LosteyeEffect3Entity entity) {
        int tick = entity.tickCount;
        if (tick % 22 < 4) {
            return new ResourceLocation("simple_mobs:textures/entities/losteye_effect3/losteye_effect3_glow_1.png");
        }
        if (tick % 22 < 8) {
            return new ResourceLocation("simple_mobs:textures/entities/losteye_effect3/losteye_effect3_glow_2.png");
        }
        if (tick % 22 < 12) {
            return new ResourceLocation("simple_mobs:textures/entities/losteye_effect3/losteye_effect3_glow_3.png");
        }
        if (tick % 22 < 16) {
            return new ResourceLocation("simple_mobs:textures/entities/losteye_effect3/losteye_effect3_glow_4.png");
        }
        return new ResourceLocation("simple_mobs:textures/entities/losteye_effect3/losteye_effect3_glow_5.png");
    }
}

