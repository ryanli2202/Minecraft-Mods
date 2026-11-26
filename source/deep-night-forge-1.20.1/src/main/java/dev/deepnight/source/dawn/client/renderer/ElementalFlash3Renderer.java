/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modelelemental_flash3;
import dev.deepnight.source.dawn.entity.ElementalFlash3Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class ElementalFlash3Renderer
extends MobRenderer<ElementalFlash3Entity, Modelelemental_flash3<ElementalFlash3Entity>> {
    public ElementalFlash3Renderer(EntityRendererProvider.Context context) {
        super(context, new Modelelemental_flash3(context.bakeLayer(Modelelemental_flash3.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<ElementalFlash3Entity, Modelelemental_flash3<ElementalFlash3Entity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffers, int p_117351_, ElementalFlash3Entity entity, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
                RenderType type = RenderType.eyes(this.getTextureLocation(entity));
                VertexConsumer vertexconsumer = buffers.getBuffer(type);
                ((Modelelemental_flash3)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
            }

            @Override
            protected ResourceLocation getTextureLocation(ElementalFlash3Entity entity) {
                int tick = entity.tickCount;
                if (tick % 25 < 4) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_flash3/elemental_flash3_1.png");
                }
                if (tick % 25 < 8) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_flash3/elemental_flash3_2.png");
                }
                if (tick % 25 < 12) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_flash3/elemental_flash3_3.png");
                }
                if (tick % 25 < 16) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_flash3/elemental_flash3_4.png");
                }
                return new ResourceLocation("simple_mobs:textures/entities/elemental_flash3/elemental_flash3_5.png");
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(ElementalFlash3Entity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/a.png");
    }
}

