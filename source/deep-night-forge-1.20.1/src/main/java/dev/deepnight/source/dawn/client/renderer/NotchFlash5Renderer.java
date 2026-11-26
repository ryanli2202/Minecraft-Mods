/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modelnotch_flash5;
import dev.deepnight.source.dawn.entity.NotchFlash5Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class NotchFlash5Renderer
extends MobRenderer<NotchFlash5Entity, Modelnotch_flash5<NotchFlash5Entity>> {
    public NotchFlash5Renderer(EntityRendererProvider.Context context) {
        super(context, new Modelnotch_flash5(context.bakeLayer(Modelnotch_flash5.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<NotchFlash5Entity, Modelnotch_flash5<NotchFlash5Entity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffers, int p_117351_, NotchFlash5Entity entity, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
                RenderType type = RenderType.eyes(this.getTextureLocation(entity));
                VertexConsumer vertexconsumer = buffers.getBuffer(type);
                ((Modelnotch_flash5)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
            }

            @Override
            protected ResourceLocation getTextureLocation(NotchFlash5Entity entity) {
                int tick = entity.tickCount;
                if (tick % 27 < 3) {
                    return new ResourceLocation("simple_mobs:textures/entities/notch_flash/notch_flash1.png");
                }
                if (tick % 27 < 6) {
                    return new ResourceLocation("simple_mobs:textures/entities/notch_flash/notch_flash2.png");
                }
                if (tick % 27 < 9) {
                    return new ResourceLocation("simple_mobs:textures/entities/notch_flash/notch_flash3.png");
                }
                if (tick % 27 < 12) {
                    return new ResourceLocation("simple_mobs:textures/entities/notch_flash/notch_flash4.png");
                }
                if (tick % 27 < 15) {
                    return new ResourceLocation("simple_mobs:textures/entities/notch_flash/notch_flash5.png");
                }
                return new ResourceLocation("simple_mobs:textures/entities/notch_flash/notch_flash6.png");
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(NotchFlash5Entity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/a.png");
    }
}

