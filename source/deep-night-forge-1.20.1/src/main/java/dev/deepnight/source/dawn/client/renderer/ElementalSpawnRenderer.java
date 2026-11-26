/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modelelemental_spawn;
import dev.deepnight.source.dawn.entity.ElementalSpawnEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class ElementalSpawnRenderer
extends MobRenderer<ElementalSpawnEntity, Modelelemental_spawn<ElementalSpawnEntity>> {
    public ElementalSpawnRenderer(EntityRendererProvider.Context context) {
        super(context, new Modelelemental_spawn(context.bakeLayer(Modelelemental_spawn.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<ElementalSpawnEntity, Modelelemental_spawn<ElementalSpawnEntity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffers, int p_117351_, ElementalSpawnEntity entity, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
                RenderType type = RenderType.eyes(this.getTextureLocation(entity));
                VertexConsumer vertexconsumer = buffers.getBuffer(type);
                ((Modelelemental_spawn)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
            }

            @Override
            protected ResourceLocation getTextureLocation(ElementalSpawnEntity entity) {
                int tick = entity.tickCount;
                if (tick % 30 < 5) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_spawn/elemental_spawn_glow_1.png");
                }
                if (tick % 30 < 10) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_spawn/elemental_spawn_glow_2.png");
                }
                if (tick % 30 < 15) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_spawn/elemental_spawn_glow_3.png");
                }
                if (tick % 30 < 20) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_spawn/elemental_spawn_glow_4.png");
                }
                return new ResourceLocation("simple_mobs:textures/entities/elemental_spawn/elemental_spawn_glow_5.png");
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(ElementalSpawnEntity entity) {
        int tick = entity.tickCount;
        if (tick % 30 < 5) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_spawn/elemental_spawn1.png");
        }
        if (tick % 30 < 10) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_spawn/elemental_spawn2.png");
        }
        if (tick % 30 < 15) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_spawn/elemental_spawn3.png");
        }
        if (tick % 30 < 20) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_spawn/elemental_spawn4.png");
        }
        return new ResourceLocation("simple_mobs:textures/entities/elemental_spawn/elemental_spawn5.png");
    }
}

