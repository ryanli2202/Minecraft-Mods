/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modelherobrine_flash;
import dev.deepnight.source.dawn.entity.HerobrineFlashEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class HerobrineFlashRenderer
extends MobRenderer<HerobrineFlashEntity, Modelherobrine_flash<HerobrineFlashEntity>> {
    public HerobrineFlashRenderer(EntityRendererProvider.Context context) {
        super(context, new Modelherobrine_flash(context.bakeLayer(Modelherobrine_flash.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<HerobrineFlashEntity, Modelherobrine_flash<HerobrineFlashEntity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, HerobrineFlashEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (entity.tickCount > 1) {
                    RenderType renderType = null;
                    int tick = entity.tickCount;
                    renderType = tick % 15 < 2 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/herobrine_flash/herobrine_flash_1.png")) : (tick % 15 < 4 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/herobrine_flash/herobrine_flash_2.png")) : (tick % 15 < 6 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/herobrine_flash/herobrine_flash_3.png")) : (tick % 15 < 8 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/herobrine_flash/herobrine_flash_4.png")) : (tick % 15 < 10 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/herobrine_flash/herobrine_flash_5.png")) : RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/herobrine_flash/herobrine_flash_6.png"))))));
                    VertexConsumer vertexconsumer = buffer.getBuffer(renderType);
                    ((Modelherobrine_flash)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(HerobrineFlashEntity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/a.png");
    }
}

