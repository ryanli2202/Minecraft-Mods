/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modelclaw_slash;
import dev.deepnight.source.dawn.entity.ClawMarkEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class ClawMarkRenderer
extends MobRenderer<ClawMarkEntity, Modelclaw_slash<ClawMarkEntity>> {
    public ClawMarkRenderer(EntityRendererProvider.Context context) {
        super(context, new Modelclaw_slash(context.bakeLayer(Modelclaw_slash.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<ClawMarkEntity, Modelclaw_slash<ClawMarkEntity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffers, int p_117351_, ClawMarkEntity entity, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
                RenderType type = RenderType.eyes(this.getTextureLocation(entity));
                VertexConsumer vertexconsumer = buffers.getBuffer(type);
                ((Modelclaw_slash)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
            }

            @Override
            protected ResourceLocation getTextureLocation(ClawMarkEntity entity) {
                int tick = entity.tickCount;
                if (tick % 25 < 4) {
                    return new ResourceLocation("simple_mobs:textures/entities/claw_mark1/claw_mark1_1.png");
                }
                if (tick % 25 < 8) {
                    return new ResourceLocation("simple_mobs:textures/entities/claw_mark1/claw_mark1_2.png");
                }
                return new ResourceLocation("simple_mobs:textures/entities/claw_mark1/claw_mark1_3.png");
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(ClawMarkEntity entity) {
        int tick = entity.tickCount;
        if (tick % 25 < 4) {
            return new ResourceLocation("simple_mobs:textures/entities/claw_mark1/claw_mark1_1.png");
        }
        if (tick % 25 < 8) {
            return new ResourceLocation("simple_mobs:textures/entities/claw_mark1/claw_mark1_2.png");
        }
        return new ResourceLocation("simple_mobs:textures/entities/claw_mark1/claw_mark1_3.png");
    }
}

