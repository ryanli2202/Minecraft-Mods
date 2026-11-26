/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modelasharmor_fire;
import dev.deepnight.source.dawn.entity.AsharmorFireEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class AsharmorFireRenderer
extends MobRenderer<AsharmorFireEntity, Modelasharmor_fire<AsharmorFireEntity>> {
    public AsharmorFireRenderer(EntityRendererProvider.Context context) {
        super(context, new Modelasharmor_fire(context.bakeLayer(Modelasharmor_fire.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<AsharmorFireEntity, Modelasharmor_fire<AsharmorFireEntity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffers, int p_117351_, AsharmorFireEntity entity, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
                RenderType type = RenderType.eyes(this.getTextureLocation(entity));
                VertexConsumer vertexconsumer = buffers.getBuffer(type);
                ((Modelasharmor_fire)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
            }

            @Override
            protected ResourceLocation getTextureLocation(AsharmorFireEntity entity) {
                int tick = entity.tickCount;
                if (tick % 36 < 4) {
                    return new ResourceLocation("simple_mobs:textures/entities/asharmor_fire/asharmor_fireglow_1.png");
                }
                if (tick % 36 < 8) {
                    return new ResourceLocation("simple_mobs:textures/entities/asharmor_fire/asharmor_fireglow_2.png");
                }
                if (tick % 36 < 12) {
                    return new ResourceLocation("simple_mobs:textures/entities/asharmor_fire/asharmor_fireglow_3.png");
                }
                if (tick % 36 < 16) {
                    return new ResourceLocation("simple_mobs:textures/entities/asharmor_fire/asharmor_fireglow_4.png");
                }
                if (tick % 36 < 20) {
                    return new ResourceLocation("simple_mobs:textures/entities/asharmor_fire/asharmor_fireglow_5.png");
                }
                if (tick % 36 < 24) {
                    return new ResourceLocation("simple_mobs:textures/entities/asharmor_fire/asharmor_fireglow_6.png");
                }
                if (tick % 36 < 28) {
                    return new ResourceLocation("simple_mobs:textures/entities/asharmor_fire/asharmor_fireglow_7.png");
                }
                if (tick % 36 < 32) {
                    return new ResourceLocation("simple_mobs", "textures/entities/asharmor_fire/asharmor_fire_8.png");
                }
                return new ResourceLocation("simple_mobs:textures/entities/asharmor_fire/asharmor_fireglow_9.png");
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(AsharmorFireEntity entity) {
        int tick = entity.tickCount;
        if (tick % 36 < 4) {
            return new ResourceLocation("simple_mobs", "textures/entities/asharmor_fire/asharmor_fire_1.png");
        }
        if (tick % 36 < 8) {
            return new ResourceLocation("simple_mobs", "textures/entities/asharmor_fire/asharmor_fire_2.png");
        }
        if (tick % 36 < 12) {
            return new ResourceLocation("simple_mobs", "textures/entities/asharmor_fire/asharmor_fire_3.png");
        }
        if (tick % 36 < 16) {
            return new ResourceLocation("simple_mobs", "textures/entities/asharmor_fire/asharmor_fire_4.png");
        }
        if (tick % 36 < 20) {
            return new ResourceLocation("simple_mobs", "textures/entities/asharmor_fire/asharmor_fire_5.png");
        }
        if (tick % 36 < 24) {
            return new ResourceLocation("simple_mobs", "textures/entities/asharmor_fire/asharmor_fire_6.png");
        }
        if (tick % 36 < 28) {
            return new ResourceLocation("simple_mobs", "textures/entities/asharmor_fire/asharmor_fire_7.png");
        }
        if (tick % 36 < 28) {
            return new ResourceLocation("simple_mobs", "textures/entities/asharmor_fire/asharmor_fire_8.png");
        }
        return new ResourceLocation("simple_mobs", "textures/entities/asharmor_fire/asharmor_fire_9.png");
    }
}

