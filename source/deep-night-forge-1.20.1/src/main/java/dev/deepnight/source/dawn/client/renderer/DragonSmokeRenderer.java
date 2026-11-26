/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modeldragon_smoke;
import dev.deepnight.source.dawn.entity.DragonSmokeEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class DragonSmokeRenderer
extends MobRenderer<DragonSmokeEntity, Modeldragon_smoke<DragonSmokeEntity>> {
    public DragonSmokeRenderer(EntityRendererProvider.Context context) {
        super(context, new Modeldragon_smoke(context.bakeLayer(Modeldragon_smoke.LAYER_LOCATION)), 0.0f);
        this.addLayer(new RenderLayer<DragonSmokeEntity, Modeldragon_smoke<DragonSmokeEntity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, DragonSmokeEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (entity.tickCount > 1) {
                    RenderType renderType = null;
                    renderType = RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/dragon_smoke.png"));
                    VertexConsumer vertexconsumer = buffer.getBuffer(renderType);
                    ((Modeldragon_smoke)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(DragonSmokeEntity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/a.png");
    }
}

