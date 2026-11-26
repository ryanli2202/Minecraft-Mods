/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modelelemental_shield;
import dev.deepnight.source.dawn.entity.ElementalShieldEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class ElementalShieldRenderer
extends MobRenderer<ElementalShieldEntity, Modelelemental_shield<ElementalShieldEntity>> {
    public ElementalShieldRenderer(EntityRendererProvider.Context context) {
        super(context, new Modelelemental_shield(context.bakeLayer(Modelelemental_shield.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<ElementalShieldEntity, Modelelemental_shield<ElementalShieldEntity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffers, int p_117351_, ElementalShieldEntity entity, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
                RenderType type = RenderType.eyes(this.getTextureLocation(entity));
                VertexConsumer vertexconsumer = buffers.getBuffer(type);
                ((Modelelemental_shield)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
            }

            @Override
            protected ResourceLocation getTextureLocation(ElementalShieldEntity entity) {
                int tick = entity.tickCount;
                if (tick % 40 < 4) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_shield/elemental_shield_1.png");
                }
                if (tick % 40 < 8) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_shield/elemental_shield_2.png");
                }
                if (tick % 40 < 12) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_shield/elemental_shield_3.png");
                }
                if (tick % 40 < 16) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_shield/elemental_shield_glow_4.png");
                }
                if (tick % 40 < 20) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_shield/elemental_shield_5.png");
                }
                if (tick % 40 < 24) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_shield/elemental_shield_6.png");
                }
                if (tick % 40 < 28) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_shield/elemental_shield_glow_7.png");
                }
                if (tick % 40 < 32) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_shield/elemental_shield_glow_8.png");
                }
                return new ResourceLocation("simple_mobs:textures/entities/elemental_shield/elemental_shield_9.png");
            }
        });
        this.addLayer(new RenderLayer<ElementalShieldEntity, Modelelemental_shield<ElementalShieldEntity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ElementalShieldEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (entity.tickCount > 1) {
                    RenderType renderType = null;
                    int tick = entity.tickCount;
                    renderType = tick % 40 < 12 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/a.png")) : (tick % 40 < 16 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/elemental_shield/elemental_shield_transparent_4.png")) : (tick % 40 < 20 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/a.png")) : (tick % 40 < 28 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/elemental_shield/elemental_shield_transparent_7.png")) : (tick % 40 < 32 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/elemental_shield/elemental_shield_transparent_8.png")) : RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/a.png"))))));
                    VertexConsumer vertexconsumer = buffer.getBuffer(renderType);
                    ((Modelelemental_shield)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(ElementalShieldEntity entity) {
        int tick = entity.tickCount;
        if (tick % 40 < 4) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_shield/elemental_shield_1.png");
        }
        if (tick % 40 < 8) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_shield/elemental_shield_2.png");
        }
        if (tick % 40 < 12) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_shield/elemental_shield_3.png");
        }
        if (tick % 40 < 16) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_shield/elemental_shield_glow_4.png");
        }
        if (tick % 40 < 20) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_shield/elemental_shield_5.png");
        }
        if (tick % 40 < 24) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_shield/elemental_shield_6.png");
        }
        if (tick % 40 < 28) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_shield/elemental_shield_glow_7.png");
        }
        if (tick % 40 < 32) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_shield/elemental_shield_glow_8.png");
        }
        return new ResourceLocation("simple_mobs:textures/entities/elemental_shield/elemental_shield_9.png");
    }
}

