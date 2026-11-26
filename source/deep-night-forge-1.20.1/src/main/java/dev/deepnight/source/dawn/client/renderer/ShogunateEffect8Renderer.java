/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modelshogunate_effect_8;
import dev.deepnight.source.dawn.entity.ShogunateEffect8Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class ShogunateEffect8Renderer
extends MobRenderer<ShogunateEffect8Entity, Modelshogunate_effect_8<ShogunateEffect8Entity>> {
    public ShogunateEffect8Renderer(EntityRendererProvider.Context context) {
        super(context, new Modelshogunate_effect_8(context.bakeLayer(Modelshogunate_effect_8.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<ShogunateEffect8Entity, Modelshogunate_effect_8<ShogunateEffect8Entity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ShogunateEffect8Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (entity.tickCount > 1) {
                    RenderType renderType = null;
                    int tick = entity.tickCount;
                    renderType = tick % 60 < 2 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/shogunate_effect6/shogunate_effect_6_1.png")) : (tick % 60 < 3 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/shogunate_effect6/shogunate_effect_6_2.png")) : (tick % 60 < 4 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/shogunate_effect6/shogunate_effect_6_3.png")) : (tick % 60 < 5 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/shogunate_effect6/shogunate_effect_6_4.png")) : (tick % 60 < 6 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/shogunate_effect6/shogunate_effect_6_5.png")) : (tick % 60 < 7 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/shogunate_effect6/shogunate_effect_6_6.png")) : (tick % 60 < 8 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/shogunate_effect6/shogunate_effect_6_7.png")) : (tick % 60 < 9 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/shogunate_effect6/shogunate_effect_6_8.png")) : (tick % 60 < 10 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/shogunate_effect6/shogunate_effect_6_9.png")) : (tick % 60 < 11 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/shogunate_effect6/shogunate_effect_6_10.png")) : (tick % 60 < 12 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/shogunate_effect6/shogunate_effect_6_11.png")) : (tick % 60 < 13 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/shogunate_effect6/shogunate_effect_6_12.png")) : (tick % 60 < 14 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/shogunate_effect6/shogunate_effect_6_13.png")) : (tick % 60 < 15 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/shogunate_effect6/shogunate_effect_6_14.png")) : (tick % 60 < 16 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/shogunate_effect6/shogunate_effect_6_15.png")) : RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/a.png"))))))))))))))));
                    VertexConsumer vertexconsumer = buffer.getBuffer(renderType);
                    ((Modelshogunate_effect_8)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(ShogunateEffect8Entity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/a.png");
    }
}

