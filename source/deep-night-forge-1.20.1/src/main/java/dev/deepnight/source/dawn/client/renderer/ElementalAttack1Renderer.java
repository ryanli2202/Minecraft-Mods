/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modelelemental_attack1;
import dev.deepnight.source.dawn.entity.ElementalAttack1Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class ElementalAttack1Renderer
extends MobRenderer<ElementalAttack1Entity, Modelelemental_attack1<ElementalAttack1Entity>> {
    public ElementalAttack1Renderer(EntityRendererProvider.Context context) {
        super(context, new Modelelemental_attack1(context.bakeLayer(Modelelemental_attack1.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<ElementalAttack1Entity, Modelelemental_attack1<ElementalAttack1Entity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffers, int p_117351_, ElementalAttack1Entity entity, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
                RenderType type = RenderType.eyes(this.getTextureLocation(entity));
                VertexConsumer vertexconsumer = buffers.getBuffer(type);
                ((Modelelemental_attack1)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
            }

            @Override
            protected ResourceLocation getTextureLocation(ElementalAttack1Entity entity) {
                int tick = entity.tickCount;
                if (tick % 60 < 4) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_attack1/elemental_attack1_glow_1.png");
                }
                if (tick % 60 < 8) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_attack1/elemental_attack1_glow_2.png");
                }
                if (tick % 60 < 12) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_attack1/elemental_attack1_glow_3.png");
                }
                if (tick % 60 < 16) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_attack1/elemental_attack1_glow_4.png");
                }
                if (tick % 60 < 22) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_attack1/elemental_attack1_glow_5.png");
                }
                if (tick % 60 < 28) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_attack1/elemental_attack1_glow_6.png");
                }
                if (tick % 60 < 40) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_attack1/elemental_attack1_7.png");
                }
                if (tick % 60 < 43) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_attack1/elemental_attack1_glow_8.png");
                }
                return new ResourceLocation("simple_mobs:textures/entities/elemental_attack1/elemental_attack1_9.png");
            }
        });
        this.addLayer(new RenderLayer<ElementalAttack1Entity, Modelelemental_attack1<ElementalAttack1Entity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ElementalAttack1Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (entity.tickCount > 1) {
                    RenderType renderType = null;
                    int tick = entity.tickCount;
                    renderType = tick % 60 < 4 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/elemental_attack1/elemental_attack1_transparent_1.png")) : (tick % 60 < 8 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/elemental_attack1/elemental_attack1_transparent_2.png")) : (tick % 60 < 12 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/elemental_attack1/elemental_attack1_transparent_3.png")) : (tick % 60 < 16 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/elemental_attack1/elemental_attack1_transparent_4.png")) : (tick % 60 < 22 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/elemental_attack1/elemental_attack1_transparent_5.png")) : (tick % 60 < 28 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/elemental_attack1/elemental_attack1_transparent_6.png")) : (tick % 60 < 40 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/a.png")) : (tick % 60 < 43 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/elemental_attack1/elemental_attack1_transparent_8.png")) : RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/a.png")))))))));
                    VertexConsumer vertexconsumer = buffer.getBuffer(renderType);
                    ((Modelelemental_attack1)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(ElementalAttack1Entity entity) {
        int tick = entity.tickCount;
        if (tick % 60 < 4) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_attack1/elemental_attack1_glow_1.png");
        }
        if (tick % 60 < 8) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_attack1/elemental_attack1_glow_2.png");
        }
        if (tick % 60 < 12) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_attack1/elemental_attack1_glow_3.png");
        }
        if (tick % 60 < 16) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_attack1/elemental_attack1_glow_4.png");
        }
        if (tick % 60 < 22) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_attack1/elemental_attack1_glow_5.png");
        }
        if (tick % 60 < 28) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_attack1/elemental_attack1_glow_6.png");
        }
        if (tick % 60 < 40) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_attack1/elemental_attack1_7.png");
        }
        if (tick % 60 < 43) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_attack1/elemental_attack1_glow_8.png");
        }
        return new ResourceLocation("simple_mobs:textures/entities/elemental_attack1/elemental_attack1_9.png");
    }
}

