/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modelelemental_flash2;
import dev.deepnight.source.dawn.entity.ElementalFlash2Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class ElementalFlash2Renderer
extends MobRenderer<ElementalFlash2Entity, Modelelemental_flash2<ElementalFlash2Entity>> {
    public ElementalFlash2Renderer(EntityRendererProvider.Context context) {
        super(context, new Modelelemental_flash2(context.bakeLayer(Modelelemental_flash2.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<ElementalFlash2Entity, Modelelemental_flash2<ElementalFlash2Entity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffers, int p_117351_, ElementalFlash2Entity entity, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
                RenderType type = RenderType.eyes(this.getTextureLocation(entity));
                VertexConsumer vertexconsumer = buffers.getBuffer(type);
                ((Modelelemental_flash2)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
            }

            @Override
            protected ResourceLocation getTextureLocation(ElementalFlash2Entity entity) {
                int tick = entity.tickCount;
                if (tick % 25 < 5) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_flash2/elemental_flash2_1.png");
                }
                if (tick % 25 < 10) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_flash2/elemental_flash2_2.png");
                }
                if (tick % 25 < 15) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_flash2/elemental_flash2_3.png");
                }
                return new ResourceLocation("simple_mobs:textures/entities/elemental_flash2/elemental_flash2_4.png");
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(ElementalFlash2Entity entity) {
        int tick = entity.tickCount;
        if (tick % 25 < 5) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_flash2/elemental_flash2_1.png");
        }
        if (tick % 25 < 10) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_flash2/elemental_flash2_2.png");
        }
        if (tick % 25 < 15) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_flash2/elemental_flash2_3.png");
        }
        return new ResourceLocation("simple_mobs:textures/entities/elemental_flash2/elemental_flash2_4.png");
    }
}

