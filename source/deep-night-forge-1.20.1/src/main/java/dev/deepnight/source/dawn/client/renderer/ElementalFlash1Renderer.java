/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modelelemental_flash1;
import dev.deepnight.source.dawn.entity.ElementalFlash1Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class ElementalFlash1Renderer
extends MobRenderer<ElementalFlash1Entity, Modelelemental_flash1<ElementalFlash1Entity>> {
    public ElementalFlash1Renderer(EntityRendererProvider.Context context) {
        super(context, new Modelelemental_flash1(context.bakeLayer(Modelelemental_flash1.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<ElementalFlash1Entity, Modelelemental_flash1<ElementalFlash1Entity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffers, int p_117351_, ElementalFlash1Entity entity, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
                RenderType type = RenderType.eyes(this.getTextureLocation(entity));
                VertexConsumer vertexconsumer = buffers.getBuffer(type);
                ((Modelelemental_flash1)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
            }

            @Override
            protected ResourceLocation getTextureLocation(ElementalFlash1Entity entity) {
                int tick = entity.tickCount;
                if (tick % 25 < 5) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_flash1/elemental_flash1_glow_1.png");
                }
                if (tick % 25 < 10) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_flash1/elemental_flash1_glow_2.png");
                }
                if (tick % 25 < 15) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_flash1/elemental_flash1_glow_3.png");
                }
                return new ResourceLocation("simple_mobs:textures/entities/elemental_flash1/elemental_flash1_glow_4.png");
            }
        });
        this.addLayer(new RenderLayer<ElementalFlash1Entity, Modelelemental_flash1<ElementalFlash1Entity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ElementalFlash1Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (entity.tickCount > 1) {
                    RenderType renderType = null;
                    int tick = entity.tickCount;
                    renderType = tick % 25 < 5 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/elemental_flash1/elemental_flash1_transparent_1.png")) : (tick % 25 < 10 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/elemental_flash1/elemental_flash1_transparent_2.png")) : (tick % 25 < 15 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/elemental_flash1/elemental_flash1_transparent_3.png")) : RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/elemental_flash1/elemental_flash1_transparent_4.png"))));
                    VertexConsumer vertexconsumer = buffer.getBuffer(renderType);
                    ((Modelelemental_flash1)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(ElementalFlash1Entity entity) {
        int tick = entity.tickCount;
        if (tick % 25 < 5) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_flash1/elemental_flash1_glow_1.png");
        }
        if (tick % 25 < 10) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_flash1/elemental_flash1_glow_2.png");
        }
        if (tick % 25 < 15) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_flash1/elemental_flash1_glow_3.png");
        }
        return new ResourceLocation("simple_mobs:textures/entities/elemental_flash1/elemental_flash1_glow_4.png");
    }
}

