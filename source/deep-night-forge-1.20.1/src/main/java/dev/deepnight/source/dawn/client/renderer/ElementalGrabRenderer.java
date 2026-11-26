/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modelelemental_grab;
import dev.deepnight.source.dawn.entity.ElementalGrabEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class ElementalGrabRenderer
extends MobRenderer<ElementalGrabEntity, Modelelemental_grab<ElementalGrabEntity>> {
    public ElementalGrabRenderer(EntityRendererProvider.Context context) {
        super(context, new Modelelemental_grab(context.bakeLayer(Modelelemental_grab.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<ElementalGrabEntity, Modelelemental_grab<ElementalGrabEntity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffers, int p_117351_, ElementalGrabEntity entity, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
                RenderType type = RenderType.eyes(this.getTextureLocation(entity));
                VertexConsumer vertexconsumer = buffers.getBuffer(type);
                ((Modelelemental_grab)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
            }

            @Override
            protected ResourceLocation getTextureLocation(ElementalGrabEntity entity) {
                int tick = entity.tickCount;
                if (tick % 16 < 4) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_grab/elemental_grab_1.png");
                }
                if (tick % 16 < 8) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_grab/elemental_grab_2.png");
                }
                if (tick % 16 < 12) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_grab/elemental_grab_3.png");
                }
                return new ResourceLocation("simple_mobs:textures/entities/elemental_grab/elemental_grab_4.png");
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(ElementalGrabEntity entity) {
        int tick = entity.tickCount;
        if (tick % 16 < 4) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_grab/elemental_grab_1.png");
        }
        if (tick % 16 < 8) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_grab/elemental_grab_2.png");
        }
        if (tick % 16 < 12) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_grab/elemental_grab_3.png");
        }
        return new ResourceLocation("simple_mobs:textures/entities/elemental_grab/elemental_grab_4.png");
    }
}

