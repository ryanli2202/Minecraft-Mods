/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modelsentinel_slash;
import dev.deepnight.source.dawn.entity.SentinelSlashEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class SentinelSlashRenderer
extends MobRenderer<SentinelSlashEntity, Modelsentinel_slash<SentinelSlashEntity>> {
    public SentinelSlashRenderer(EntityRendererProvider.Context context) {
        super(context, new Modelsentinel_slash(context.bakeLayer(Modelsentinel_slash.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<SentinelSlashEntity, Modelsentinel_slash<SentinelSlashEntity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffers, int p_117351_, SentinelSlashEntity entity, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
                RenderType type = RenderType.eyes(this.getTextureLocation(entity));
                VertexConsumer vertexconsumer = buffers.getBuffer(type);
                ((Modelsentinel_slash)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
            }

            @Override
            protected ResourceLocation getTextureLocation(SentinelSlashEntity entity) {
                int tick = entity.tickCount;
                if (tick % 25 < 3) {
                    return new ResourceLocation("simple_mobs:textures/entities/sentinel_slash/sentinel_slash_1.png");
                }
                if (tick % 25 < 6) {
                    return new ResourceLocation("simple_mobs:textures/entities/sentinel_slash/sentinel_slash_2.png");
                }
                if (tick % 25 < 9) {
                    return new ResourceLocation("simple_mobs:textures/entities/sentinel_slash/sentinel_slash_1.png");
                }
                if (tick % 25 < 12) {
                    return new ResourceLocation("simple_mobs:textures/entities/sentinel_slash/sentinel_slash_1.png");
                }
                if (tick % 25 < 14) {
                    return new ResourceLocation("simple_mobs:textures/entities/sentinel_slash/sentinel_slash_3.png");
                }
                if (tick % 25 < 16) {
                    return new ResourceLocation("simple_mobs:textures/entities/sentinel_slash/sentinel_slash_4.png");
                }
                return new ResourceLocation("simple_mobs:textures/entities/sentinel_slash/sentinel_slash_5.png");
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(SentinelSlashEntity entity) {
        return new ResourceLocation("simple_mobs:textures/a.png");
    }
}

