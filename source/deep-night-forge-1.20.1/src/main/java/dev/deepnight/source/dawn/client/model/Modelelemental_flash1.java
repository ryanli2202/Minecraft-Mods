/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class Modelelemental_flash1<T extends Entity>
extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("simple_mobs", "modelelemental_flash_1"), "main");
    public final ModelPart bone;

    public Modelelemental_flash1(ModelPart root) {
        this.bone = root.getChild("bone");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offsetAndRotation(0.082f, 12.6666f, -0.5738f, 0.0f, 0.0f, 0.48f));
        PartDefinition cube_r1 = bone.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 48).addBox(-7.0f, -9.0f, -6.0f, 16.0f, 16.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.082f, -0.9666f, 2.0738f, -1.0472f, 0.0f, -2.3562f));
        PartDefinition cube_r2 = bone.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 48).addBox(-8.0f, -9.0f, -5.0f, 16.0f, 16.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.082f, -0.9666f, 2.0738f, -0.7854f, 0.0f, 0.0f));
        PartDefinition cube_r3 = bone.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 48).addBox(-8.0f, -12.0f, 5.0f, 16.0f, 16.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.082f, -0.9666f, 2.0738f, -2.4435f, 0.0f, -0.9163f));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }
}

