/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modelelemental_chain;
import dev.deepnight.source.dawn.entity.ElementalChainEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class ElementalChainRenderer
extends MobRenderer<ElementalChainEntity, Modelelemental_chain<ElementalChainEntity>> {
    public ElementalChainRenderer(EntityRendererProvider.Context context) {
        super(context, new Modelelemental_chain(context.bakeLayer(Modelelemental_chain.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<ElementalChainEntity, Modelelemental_chain<ElementalChainEntity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffers, int p_117351_, ElementalChainEntity entity, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
                RenderType type = RenderType.eyes(this.getTextureLocation(entity));
                VertexConsumer vertexconsumer = buffers.getBuffer(type);
                ((Modelelemental_chain)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
            }

            @Override
            protected ResourceLocation getTextureLocation(ElementalChainEntity entity) {
                int tick = entity.tickCount;
                if (tick % 160 < 4) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_chain/elemental_chain_1.png");
                }
                if (tick % 160 < 8) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_chain/elemental_chain_2.png");
                }
                if (tick % 160 < 38) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_chain/elemental_chain_3.png");
                }
                if (tick % 160 < 40) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_chain/elemental_chain_4.png");
                }
                if (tick % 160 < 42) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_chain/elemental_chain_5.png");
                }
                if (tick % 160 < 156) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_chain/elemental_chain_6.png");
                }
                if (tick % 160 < 158) {
                    return new ResourceLocation("simple_mobs:textures/entities/elemental_chain/elemental_chain_7.png");
                }
                return new ResourceLocation("simple_mobs:textures/entities/elemental_chain/elemental_chain_8.png");
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(ElementalChainEntity entity) {
        int tick = entity.tickCount;
        if (tick % 160 < 4) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_chain/elemental_chain_1.png");
        }
        if (tick % 160 < 8) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_chain/elemental_chain_2.png");
        }
        if (tick % 160 < 38) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_chain/elemental_chain_3.png");
        }
        if (tick % 160 < 40) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_chain/elemental_chain_4.png");
        }
        if (tick % 160 < 42) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_chain/elemental_chain_5.png");
        }
        if (tick % 160 < 156) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_chain/elemental_chain_6.png");
        }
        if (tick % 160 < 158) {
            return new ResourceLocation("simple_mobs:textures/entities/elemental_chain/elemental_chain_7.png");
        }
        return new ResourceLocation("simple_mobs:textures/entities/elemental_chain/elemental_chain_8.png");
    }
}

