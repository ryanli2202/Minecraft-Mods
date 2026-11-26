/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modelherobrine_se;
import dev.deepnight.source.dawn.entity.HerobrineseEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class HerobrineseRenderer
extends MobRenderer<HerobrineseEntity, Modelherobrine_se<HerobrineseEntity>> {
    public HerobrineseRenderer(EntityRendererProvider.Context context) {
        super(context, new Modelherobrine_se(context.bakeLayer(Modelherobrine_se.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<HerobrineseEntity, Modelherobrine_se<HerobrineseEntity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, HerobrineseEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (entity.tickCount > 1) {
                    RenderType renderType = null;
                    int tick = entity.tickCount;
                    renderType = tick % 30 < 3 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/herobrine_se/herobrine_se_1.png")) : (tick % 30 < 6 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/herobrine_se/herobrine_se_2.png")) : (tick % 30 < 16 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/herobrine_se/herobrine_se_3.png")) : RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/herobrine_se/herobrine_se_4.png"))));
                    VertexConsumer vertexconsumer = buffer.getBuffer(renderType);
                    ((Modelherobrine_se)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(HerobrineseEntity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/a.png");
    }
}

