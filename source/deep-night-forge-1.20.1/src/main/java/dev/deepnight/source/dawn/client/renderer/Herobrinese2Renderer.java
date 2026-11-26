/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modelherobrine_se2;
import dev.deepnight.source.dawn.entity.Herobrinese2Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class Herobrinese2Renderer
extends MobRenderer<Herobrinese2Entity, Modelherobrine_se2<Herobrinese2Entity>> {
    public Herobrinese2Renderer(EntityRendererProvider.Context context) {
        super(context, new Modelherobrine_se2(context.bakeLayer(Modelherobrine_se2.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<Herobrinese2Entity, Modelherobrine_se2<Herobrinese2Entity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Herobrinese2Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (entity.tickCount > 1) {
                    RenderType renderType = null;
                    int tick = entity.tickCount;
                    renderType = tick % 40 < 4 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/herobrine_se2/herobrine_se2_1.png")) : (tick % 40 < 8 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/herobrine_se2/herobrine_se2_2.png")) : (tick % 40 < 12 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/herobrine_se2/herobrine_se2_3.png")) : (tick % 40 < 29 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/herobrine_se2/herobrine_se2_4.png")) : (tick % 40 < 32 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/herobrine_se2/herobrine_se2_5.png")) : RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/herobrine_se2/herobrine_se2_6.png"))))));
                    VertexConsumer vertexconsumer = buffer.getBuffer(renderType);
                    ((Modelherobrine_se2)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(Herobrinese2Entity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/a.png");
    }
}

