/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modelnotch_flash4;
import dev.deepnight.source.dawn.entity.Notchflash4Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class Notchflash4Renderer
extends MobRenderer<Notchflash4Entity, Modelnotch_flash4<Notchflash4Entity>> {
    public Notchflash4Renderer(EntityRendererProvider.Context context) {
        super(context, new Modelnotch_flash4(context.bakeLayer(Modelnotch_flash4.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<Notchflash4Entity, Modelnotch_flash4<Notchflash4Entity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffers, int p_117351_, Notchflash4Entity entity, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
                RenderType type = RenderType.eyes(this.getTextureLocation(entity));
                VertexConsumer vertexconsumer = buffers.getBuffer(type);
                ((Modelnotch_flash4)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
            }

            @Override
            protected ResourceLocation getTextureLocation(Notchflash4Entity entity) {
                int tick = entity.tickCount;
                if (tick % 30 < 5) {
                    return new ResourceLocation("simple_mobs:textures/entities/notch_flash4/notch_flash4_1.png");
                }
                if (tick % 30 < 10) {
                    return new ResourceLocation("simple_mobs:textures/entities/notch_flash4/notch_flash4_2.png");
                }
                if (tick % 30 < 15) {
                    return new ResourceLocation("simple_mobs:textures/entities/notch_flash4/notch_flash4_3.png");
                }
                if (tick % 30 < 20) {
                    return new ResourceLocation("simple_mobs:textures/entities/notch_flash4/notch_flash4_4.png");
                }
                return new ResourceLocation("simple_mobs:textures/entities/notch_flash4/notch_flash4_5.png");
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(Notchflash4Entity entity) {
        int tick = entity.tickCount;
        if (tick % 30 < 5) {
            return new ResourceLocation("simple_mobs:textures/entities/notch_flash4/notch_flash4_1.png");
        }
        if (tick % 30 < 10) {
            return new ResourceLocation("simple_mobs:textures/entities/notch_flash4/notch_flash4_2.png");
        }
        if (tick % 30 < 15) {
            return new ResourceLocation("simple_mobs:textures/entities/notch_flash4/notch_flash4_3.png");
        }
        if (tick % 30 < 20) {
            return new ResourceLocation("simple_mobs:textures/entities/notch_flash4/notch_flash4_4.png");
        }
        return new ResourceLocation("simple_mobs:textures/entities/notch_flash4/notch_flash4_5.png");
    }
}

