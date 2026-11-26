/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modelsentinel_flash;
import dev.deepnight.source.dawn.entity.SentinelFlashEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class SentinelFlashRenderer
extends MobRenderer<SentinelFlashEntity, Modelsentinel_flash<SentinelFlashEntity>> {
    public SentinelFlashRenderer(EntityRendererProvider.Context context) {
        super(context, new Modelsentinel_flash(context.bakeLayer(Modelsentinel_flash.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<SentinelFlashEntity, Modelsentinel_flash<SentinelFlashEntity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffers, int p_117351_, SentinelFlashEntity entity, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
                RenderType type = RenderType.eyes(this.getTextureLocation(entity));
                VertexConsumer vertexconsumer = buffers.getBuffer(type);
                ((Modelsentinel_flash)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
            }

            @Override
            protected ResourceLocation getTextureLocation(SentinelFlashEntity entity) {
                int tick = entity.tickCount;
                if (tick % 18 < 3) {
                    return new ResourceLocation("simple_mobs:textures/entities/sentinel_flash/sentinel_flash_1.png");
                }
                if (tick % 18 < 8) {
                    return new ResourceLocation("simple_mobs:textures/entities/sentinel_flash/sentinel_flash_2.png");
                }
                if (tick % 18 < 12) {
                    return new ResourceLocation("simple_mobs:textures/entities/sentinel_flash/sentinel_flash_3.png");
                }
                return new ResourceLocation("simple_mobs:textures/entities/sentinel_flash/sentinel_flash_4.png");
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(SentinelFlashEntity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/a.png");
    }
}

