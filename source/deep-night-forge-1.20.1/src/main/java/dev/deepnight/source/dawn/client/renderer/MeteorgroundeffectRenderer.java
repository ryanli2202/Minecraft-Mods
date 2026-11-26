/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.model.Modelnotch_groundeffect3;
import dev.deepnight.source.dawn.entity.MeteorgroundeffectEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class MeteorgroundeffectRenderer
extends MobRenderer<MeteorgroundeffectEntity, Modelnotch_groundeffect3<MeteorgroundeffectEntity>> {
    public MeteorgroundeffectRenderer(EntityRendererProvider.Context context) {
        super(context, new Modelnotch_groundeffect3(context.bakeLayer(Modelnotch_groundeffect3.LAYER_LOCATION)), 0.5f);
        this.addLayer(new RenderLayer<MeteorgroundeffectEntity, Modelnotch_groundeffect3<MeteorgroundeffectEntity>>((RenderLayerParent)this){

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, MeteorgroundeffectEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (entity.tickCount > 1) {
                    RenderType renderType = null;
                    int tick = entity.tickCount;
                    renderType = tick % 84 < 3 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/meteor_ground/meteor_ground_1.png")) : (tick % 84 < 8 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/meteor_ground/meteor_ground_2.png")) : (tick % 84 < 60 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/meteor_ground/meteor_ground_3.png")) : (tick % 84 < 70 ? RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/meteor_ground/meteor_ground_4.png")) : RenderType.entityTranslucent(new ResourceLocation("simple_mobs:textures/entities/meteor_ground/meteor_ground_5.png")))));
                    VertexConsumer vertexconsumer = buffer.getBuffer(renderType);
                    ((Modelnotch_groundeffect3)this.getParentModel()).renderToBuffer(poseStack, vertexconsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(MeteorgroundeffectEntity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/a.png");
    }
}

