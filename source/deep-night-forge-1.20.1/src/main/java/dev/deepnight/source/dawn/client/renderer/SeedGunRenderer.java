/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.deepnight.source.dawn.client.model.Modelblossom_seed;
import dev.deepnight.source.dawn.entity.SeedGunEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SeedGunRenderer
extends EntityRenderer<SeedGunEntity> {
    private static final ResourceLocation texture = new ResourceLocation("simple_mobs:textures/entities/blossom_seed.png");
    private final Modelblossom_seed model;

    public SeedGunRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new Modelblossom_seed(context.bakeLayer(Modelblossom_seed.LAYER_LOCATION));
    }

    @Override
    public void render(SeedGunEntity entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
        VertexConsumer vb = bufferIn.getBuffer(RenderType.entityCutout(this.getTextureLocation(entityIn)));
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.yRotO, entityIn.getYRot()) - 90.0f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(90.0f + Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot())));
        this.model.renderToBuffer(poseStack, vb, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 0.0625f);
        poseStack.popPose();
        super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getTextureLocation(SeedGunEntity entity) {
        return texture;
    }
}

