/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modelherobrine_se3;
import dev.deepnight.source.dawn.entity.Herobrinese3Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class Herobrinese3Renderer
extends MobRenderer<Herobrinese3Entity, Modelherobrine_se3<Herobrinese3Entity>> {
    public Herobrinese3Renderer(EntityRendererProvider.Context context) {
        super(context, new Modelherobrine_se3(context.bakeLayer(Modelherobrine_se3.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<Herobrinese3Entity, Modelherobrine_se3<Herobrinese3Entity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Herobrinese3Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (entity.tickCount > 1) {
                    RenderType renderType = null;
                    int tick = entity.tickCount;
                    renderType = tick % 30 < 3 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/herobrine_se3/herobrine_se3_1.png")) : (tick % 30 < 6 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/herobrine_se3/herobrine_se3_2.png")) : (tick % 30 < 14 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/herobrine_se3/herobrine_se3_3.png")) : (tick % 30 < 17 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/herobrine_se3/herobrine_se3_4.png")) : RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/herobrine_se3/herobrine_se3_5.png")))));
                    VertexConsumer vertexconsumer = buffer.getBuffer(renderType);
                    ((Modelherobrine_se3)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(Herobrinese3Entity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/a.png");
    }
}

