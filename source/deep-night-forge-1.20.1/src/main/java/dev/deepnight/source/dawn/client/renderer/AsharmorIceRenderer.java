/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modelasharmor_ice;
import dev.deepnight.source.dawn.entity.AsharmorIceEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class AsharmorIceRenderer
extends MobRenderer<AsharmorIceEntity, Modelasharmor_ice<AsharmorIceEntity>> {
    public AsharmorIceRenderer(EntityRendererProvider.Context context) {
        super(context, new Modelasharmor_ice(context.bakeLayer(Modelasharmor_ice.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<AsharmorIceEntity, Modelasharmor_ice<AsharmorIceEntity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffers, int p_117351_, AsharmorIceEntity entity, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
                RenderType type = RenderType.eyes(this.getTextureLocation(entity));
                VertexConsumer vertexconsumer = buffers.getBuffer(type);
                ((Modelasharmor_ice)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
            }

            @Override
            protected ResourceLocation getTextureLocation(AsharmorIceEntity entity) {
                int tick = entity.tickCount;
                if (tick % 36 < 4) {
                    return new ResourceLocation("simple_mobs:textures/entities/asharmor_ice/asharmor_iceglow_1.png");
                }
                if (tick % 36 < 8) {
                    return new ResourceLocation("simple_mobs:textures/entities/asharmor_ice/asharmor_iceglow_2.png");
                }
                if (tick % 36 < 12) {
                    return new ResourceLocation("simple_mobs:textures/entities/asharmor_ice/asharmor_iceglow_3.png");
                }
                if (tick % 36 < 16) {
                    return new ResourceLocation("simple_mobs:textures/entities/asharmor_ice/asharmor_iceglow_4.png");
                }
                if (tick % 36 < 20) {
                    return new ResourceLocation("simple_mobs:textures/entities/asharmor_ice/asharmor_iceglow_5.png");
                }
                if (tick % 36 < 24) {
                    return new ResourceLocation("simple_mobs:textures/entities/asharmor_ice/asharmor_iceglow_6.png");
                }
                if (tick % 36 < 28) {
                    return new ResourceLocation("simple_mobs:textures/entities/asharmor_ice/asharmor_iceglow_7.png");
                }
                if (tick % 36 < 32) {
                    return new ResourceLocation("simple_mobs", "textures/entities/asharmor_ice/asharmor_ice_8.png");
                }
                return new ResourceLocation("simple_mobs:textures/entities/asharmor_ice/asharmor_iceglow_9.png");
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(AsharmorIceEntity entity) {
        int tick = entity.tickCount;
        if (tick % 36 < 4) {
            return new ResourceLocation("simple_mobs", "textures/entities/asharmor_ice/asharmor_ice_1.png");
        }
        if (tick % 36 < 8) {
            return new ResourceLocation("simple_mobs", "textures/entities/asharmor_ice/asharmor_ice_2.png");
        }
        if (tick % 36 < 12) {
            return new ResourceLocation("simple_mobs", "textures/entities/asharmor_ice/asharmor_ice_3.png");
        }
        if (tick % 36 < 16) {
            return new ResourceLocation("simple_mobs", "textures/entities/asharmor_ice/asharmor_ice_4.png");
        }
        if (tick % 36 < 20) {
            return new ResourceLocation("simple_mobs", "textures/entities/asharmor_ice/asharmor_ice_5.png");
        }
        if (tick % 36 < 24) {
            return new ResourceLocation("simple_mobs", "textures/entities/asharmor_ice/asharmor_ice_6.png");
        }
        if (tick % 36 < 28) {
            return new ResourceLocation("simple_mobs", "textures/entities/asharmor_ice/asharmor_ice_7.png");
        }
        if (tick % 36 < 28) {
            return new ResourceLocation("simple_mobs", "textures/entities/asharmor_ice/asharmor_ice_8.png");
        }
        return new ResourceLocation("simple_mobs", "textures/entities/asharmor_ice/asharmor_ice_9.png");
    }
}

