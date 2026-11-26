/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modellightning_spear;
import dev.deepnight.source.dawn.entity.LightningSpearEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class LightningSpearRenderer
extends MobRenderer<LightningSpearEntity, Modellightning_spear<LightningSpearEntity>> {
    public LightningSpearRenderer(EntityRendererProvider.Context context) {
        super(context, new Modellightning_spear(context.bakeLayer(Modellightning_spear.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<LightningSpearEntity, Modellightning_spear<LightningSpearEntity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffers, int p_117351_, LightningSpearEntity entity, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
                RenderType type = RenderType.eyes(this.getTextureLocation(entity));
                VertexConsumer vertexconsumer = buffers.getBuffer(type);
                ((Modellightning_spear)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
            }

            @Override
            protected ResourceLocation getTextureLocation(LightningSpearEntity entity) {
                int tick = entity.tickCount;
                if (tick % 10 < 5) {
                    return new ResourceLocation("simple_mobs:textures/entities/lightning_spear/lightning_spear_1.png");
                }
                return new ResourceLocation("simple_mobs:textures/entities/lightning_spear/lightning_spear_2.png");
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(LightningSpearEntity entity) {
        int tick = entity.tickCount;
        if (tick % 10 < 5) {
            return new ResourceLocation("simple_mobs:textures/entities/lightning_spear/lightning_spear_1.png");
        }
        return new ResourceLocation("simple_mobs:textures/entities/lightning_spear/lightning_spear_2.png");
    }
}

