/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.ModelLosteyeEffect2;
import dev.deepnight.source.dawn.entity.LosteyeEffect2Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class LosteyeEffect2Renderer
extends MobRenderer<LosteyeEffect2Entity, ModelLosteyeEffect2<LosteyeEffect2Entity>> {
    public LosteyeEffect2Renderer(EntityRendererProvider.Context context) {
        super(context, new ModelLosteyeEffect2(context.bakeLayer(ModelLosteyeEffect2.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<LosteyeEffect2Entity, ModelLosteyeEffect2<LosteyeEffect2Entity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffers, int p_117351_, LosteyeEffect2Entity entity, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
                RenderType type = RenderType.eyes(this.getTextureLocation(entity));
                VertexConsumer vertexconsumer = buffers.getBuffer(type);
                ((ModelLosteyeEffect2)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
            }

            @Override
            protected ResourceLocation getTextureLocation(LosteyeEffect2Entity entity) {
                int tick = entity.tickCount;
                if (tick % 45 < 10) {
                    return new ResourceLocation("simple_mobs:textures/entities/losteye_effect2/losteye_effect2_glow_1.png");
                }
                if (tick % 45 < 15) {
                    return new ResourceLocation("simple_mobs:textures/entities/losteye_effect2/losteye_effect2_glow_2.png");
                }
                if (tick % 45 < 20) {
                    return new ResourceLocation("simple_mobs:textures/entities/losteye_effect2/losteye_effect2_glow_3.png");
                }
                if (tick % 45 < 25) {
                    return new ResourceLocation("simple_mobs:textures/entities/losteye_effect2/losteye_effect2_glow_4.png");
                }
                if (tick % 45 < 30) {
                    return new ResourceLocation("simple_mobs:textures/entities/losteye_effect2/losteye_effect2_glow_5.png");
                }
                return new ResourceLocation("simple_mobs:textures/entities/losteye_effect2/losteye_effect2_glow_6.png");
            }
        });
        this.addLayer(new RenderLayer<LosteyeEffect2Entity, ModelLosteyeEffect2<LosteyeEffect2Entity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, LosteyeEffect2Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (entity.tickCount > 1) {
                    RenderType renderType = null;
                    int tick = entity.tickCount;
                    renderType = tick % 45 < 10 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/losteye_effect2/losteye_effect2_trans_1.png")) : (tick % 45 < 15 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/losteye_effect2/losteye_effect2_trans_2.png")) : (tick % 45 < 20 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/losteye_effect2/losteye_effect2_trans_3.png")) : (tick % 45 < 25 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/losteye_effect2/losteye_effect2_trans_4.png")) : (tick % 45 < 30 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/losteye_effect2/losteye_effect2_trans_5.png")) : RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/losteye_effect2/losteye_effect2_trans_6.png"))))));
                    VertexConsumer vertexconsumer = buffer.getBuffer(renderType);
                    ((ModelLosteyeEffect2)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(LosteyeEffect2Entity entity) {
        int tick = entity.tickCount;
        if (tick % 45 < 10) {
            return new ResourceLocation("simple_mobs:textures/entities/losteye_effect2/losteye_effect2_glow_1.png");
        }
        if (tick % 45 < 15) {
            return new ResourceLocation("simple_mobs:textures/entities/losteye_effect2/losteye_effect2_glow_2.png");
        }
        if (tick % 45 < 20) {
            return new ResourceLocation("simple_mobs:textures/entities/losteye_effect2/losteye_effect2_glow_3.png");
        }
        if (tick % 45 < 25) {
            return new ResourceLocation("simple_mobs:textures/entities/losteye_effect2/losteye_effect2_glow_4.png");
        }
        if (tick % 45 < 30) {
            return new ResourceLocation("simple_mobs:textures/entities/losteye_effect2/losteye_effect2_glow_5.png");
        }
        return new ResourceLocation("simple_mobs:textures/entities/losteye_effect2/losteye_effect2_glow_6.png");
    }
}

