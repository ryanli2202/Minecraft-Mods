/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modelthunder_effect3;
import dev.deepnight.source.dawn.entity.Thundereffect3Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class Thundereffect3Renderer
extends MobRenderer<Thundereffect3Entity, Modelthunder_effect3<Thundereffect3Entity>> {
    public Thundereffect3Renderer(EntityRendererProvider.Context context) {
        super(context, new Modelthunder_effect3(context.bakeLayer(Modelthunder_effect3.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<Thundereffect3Entity, Modelthunder_effect3<Thundereffect3Entity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffers, int p_117351_, Thundereffect3Entity entity, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
                RenderType type = RenderType.eyes(this.getTextureLocation(entity));
                VertexConsumer vertexconsumer = buffers.getBuffer(type);
                ((Modelthunder_effect3)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
            }

            @Override
            protected ResourceLocation getTextureLocation(Thundereffect3Entity entity) {
                int tick = entity.tickCount;
                if (tick % 70 < 4) {
                    return new ResourceLocation("simple_mobs:textures/entities/thunder_effect3/thunder_effect3_1.png");
                }
                if (tick % 70 < 8) {
                    return new ResourceLocation("simple_mobs:textures/entities/thunder_effect3/thunder_effect3_2.png");
                }
                if (tick % 70 < 20) {
                    return new ResourceLocation("simple_mobs:textures/a.png");
                }
                if (tick % 70 < 24) {
                    return new ResourceLocation("simple_mobs:textures/entities/thunder_effect3/thunder_effect3_2.png");
                }
                if (tick % 70 < 28) {
                    return new ResourceLocation("simple_mobs:textures/entities/thunder_effect3/thunder_effect3_1.png");
                }
                if (tick % 70 < 30) {
                    return new ResourceLocation("simple_mobs:textures/a.png");
                }
                if (tick % 70 < 40) {
                    return new ResourceLocation("simple_mobs:textures/entities/thunder_effect3/thunder_effect3_2.png");
                }
                if (tick % 70 < 44) {
                    return new ResourceLocation("simple_mobs:textures/entities/thunder_effect3/thunder_effect3_3.png");
                }
                if (tick % 70 < 47) {
                    return new ResourceLocation("simple_mobs:textures/entities/thunder_effect3/thunder_effect3_4.png");
                }
                if (tick % 70 < 51) {
                    return new ResourceLocation("simple_mobs:textures/entities/thunder_effect3/thunder_effect3_5.png");
                }
                if (tick % 70 < 55) {
                    return new ResourceLocation("simple_mobs:textures/entities/thunder_effect3/thunder_effect3_6.png");
                }
                return new ResourceLocation("simple_mobs:textures/entities/thunder_effect3/thunder_effect3_7.png");
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(Thundereffect3Entity entity) {
        int tick = entity.tickCount;
        if (tick % 70 < 4) {
            return new ResourceLocation("simple_mobs:textures/entities/thunder_effect3/thunder_effect3_1.png");
        }
        if (tick % 70 < 8) {
            return new ResourceLocation("simple_mobs:textures/entities/thunder_effect3/thunder_effect3_2.png");
        }
        if (tick % 70 < 20) {
            return new ResourceLocation("simple_mobs:textures/a.png");
        }
        if (tick % 70 < 24) {
            return new ResourceLocation("simple_mobs:textures/entities/thunder_effect3/thunder_effect3_2.png");
        }
        if (tick % 70 < 28) {
            return new ResourceLocation("simple_mobs:textures/entities/thunder_effect3/thunder_effect3_1.png");
        }
        if (tick % 70 < 30) {
            return new ResourceLocation("simple_mobs:textures/a.png");
        }
        if (tick % 70 < 40) {
            return new ResourceLocation("simple_mobs:textures/entities/thunder_effect3/thunder_effect3_2.png");
        }
        if (tick % 70 < 44) {
            return new ResourceLocation("simple_mobs:textures/entities/thunder_effect3/thunder_effect3_3.png");
        }
        if (tick % 70 < 47) {
            return new ResourceLocation("simple_mobs:textures/entities/thunder_effect3/thunder_effect3_4.png");
        }
        if (tick % 70 < 51) {
            return new ResourceLocation("simple_mobs:textures/entities/thunder_effect3/thunder_effect3_5.png");
        }
        if (tick % 70 < 55) {
            return new ResourceLocation("simple_mobs:textures/entities/thunder_effect3/thunder_effect3_6.png");
        }
        return new ResourceLocation("simple_mobs:textures/entities/thunder_effect3/thunder_effect3_7.png");
    }
}

