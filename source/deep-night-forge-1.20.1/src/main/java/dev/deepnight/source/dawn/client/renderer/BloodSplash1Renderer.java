/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modelblood_splash_1;
import dev.deepnight.source.dawn.entity.BloodSplash1Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class BloodSplash1Renderer
extends MobRenderer<BloodSplash1Entity, Modelblood_splash_1<BloodSplash1Entity>> {
    public BloodSplash1Renderer(EntityRendererProvider.Context context) {
        super(context, new Modelblood_splash_1(context.bakeLayer(Modelblood_splash_1.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<BloodSplash1Entity, Modelblood_splash_1<BloodSplash1Entity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffers, int p_117351_, BloodSplash1Entity entity, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
                RenderType type = RenderType.eyes(this.getTextureLocation(entity));
                VertexConsumer vertexconsumer = buffers.getBuffer(type);
                ((Modelblood_splash_1)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
            }

            @Override
            protected ResourceLocation getTextureLocation(BloodSplash1Entity entity) {
                int tick = entity.tickCount;
                if (tick % 30 < 4) {
                    return new ResourceLocation("simple_mobs:textures/entities/blood_splash/blood_splash_1_1_glow.png");
                }
                if (tick % 30 < 8) {
                    return new ResourceLocation("simple_mobs:textures/entities/blood_splash/blood_splash_1_2_glow.png");
                }
                return new ResourceLocation("simple_mobs:textures/a.png");
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(BloodSplash1Entity entity) {
        int tick = entity.tickCount;
        if (tick % 30 < 4) {
            return new ResourceLocation("simple_mobs:textures/entities/blood_splash/blood_splash_1_1.png");
        }
        if (tick % 30 < 8) {
            return new ResourceLocation("simple_mobs:textures/entities/blood_splash/blood_splash_1_2.png");
        }
        if (tick % 30 < 12) {
            return new ResourceLocation("simple_mobs:textures/entities/blood_splash/blood_splash_1_3.png");
        }
        if (tick % 30 < 16) {
            return new ResourceLocation("simple_mobs:textures/entities/blood_splash/blood_splash_1_4.png");
        }
        if (tick % 30 < 20) {
            return new ResourceLocation("simple_mobs:textures/entities/blood_splash/blood_splash_1_5.png");
        }
        return new ResourceLocation("simple_mobs:textures/entities/blood_splash/blood_splash_1_6.png");
    }
}

