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

public class Modelnotch_punch<T extends Entity>
extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("simple_mobs", "modelnotch_punch"), "main");
    public final ModelPart bone;

    public Modelnotch_punch(ModelPart root) {
        this.bone = root.getChild("bone");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.0222f, 4.7155f, -1.5177f, 0.0f, 3.1416f, 0.0f));
        PartDefinition cube_r1 = bone.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(62, 97).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 8.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-17.6249f, -12.4944f, 0.7175f, -1.5708f, 0.1745f, 0.0f));
        PartDefinition cube_r2 = bone.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(62, 116).addBox(-1.7f, -4.0f, -2.0f, 4.0f, 8.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-16.9303f, -12.4944f, 4.6568f, -1.5708f, 0.2618f, 0.0f));
        PartDefinition cube_r3 = bone.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(62, 116).addBox(17.0f, -8.0f, 12.0f, 4.0f, 8.0f, 4.0f, new CubeDeformation(0.0f)).texOffs(62, 97).addBox(17.0f, 0.0f, 12.0f, 4.0f, 8.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-9.6249f, -4.5628f, 1.7617f, -1.7037f, 0.173f, -0.023f));
        PartDefinition cube_r4 = bone.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 97).addBox(-13.0f, -7.0f, -2.0f, 4.0f, 8.0f, 4.0f, new CubeDeformation(0.0f)).texOffs(0, 116).addBox(-13.0f, -15.0f, -2.0f, 4.0f, 8.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(24.8372f, 1.4076f, -3.6788f, -1.4827f, -0.1302f, -0.0134f));
        PartDefinition cube_r5 = bone.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(21, 116).addBox(-19.0f, -8.0f, -17.0f, 4.0f, 8.0f, 4.0f, new CubeDeformation(0.0f)).texOffs(21, 97).addBox(-19.0f, 0.0f, -17.0f, 4.0f, 8.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(7.5389f, 10.6811f, -3.1077f, -1.6146f, 0.1311f, -0.0133f));
        PartDefinition cube_r6 = bone.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(41, 97).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 8.0f, 4.0f, new CubeDeformation(0.0f)).texOffs(41, 116).addBox(-2.0f, -8.0f, -2.0f, 4.0f, 8.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-3.8164f, 9.661f, 1.954f, -1.4803f, -0.0426f, -0.0341f));
        PartDefinition cube_r7 = bone.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 97).addBox(-5.0f, 3.0f, -2.0f, 4.0f, 8.0f, 4.0f, new CubeDeformation(0.0f)).texOffs(0, 116).addBox(-5.0f, -5.0f, -2.0f, 4.0f, 8.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-17.6999f, 1.1123f, 0.6226f, -1.4834f, 0.0437f, 0.0019f));
        PartDefinition cube_r8 = bone.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(21, 97).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 8.0f, 4.0f, new CubeDeformation(0.0f)).texOffs(21, 116).addBox(-2.0f, -8.0f, -2.0f, 4.0f, 8.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(13.2695f, -9.9251f, 0.5356f, -1.7009f, -0.174f, -0.0115f));
        PartDefinition cube_r9 = bone.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(21, 116).addBox(-1.2107f, -7.1404f, -2.05f, 4.0f, 8.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-11.582f, 7.4138f, -4.005f, -1.5915f, -0.0338f, 2.2084f));
        PartDefinition cube_r10 = bone.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(21, 97).addBox(-1.25f, -0.85f, -1.95f, 4.0f, 8.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-11.582f, 7.4138f, -4.005f, -1.5937f, 0.446f, 2.1979f));
        PartDefinition cube_r11 = bone.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(21, 97).addBox(-1.25f, -0.85f, -1.95f, 4.0f, 8.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(24.418f, 5.4138f, 0.995f, -1.4388f, 0.1298f, 0.4535f));
        PartDefinition cube_r12 = bone.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(21, 116).addBox(-1.2107f, -7.1404f, -2.05f, 4.0f, 8.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(24.418f, 5.4138f, 0.995f, -1.4316f, -0.346f, 0.3889f));
        PartDefinition cube_r13 = bone.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(41, 97).addBox(-5.9341f, -0.3668f, 5.3108f, 4.0f, 8.0f, 4.0f, new CubeDeformation(0.0f)).texOffs(41, 116).addBox(-5.9341f, -8.3668f, 5.3108f, 4.0f, 8.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(9.0687f, -2.4477f, 2.2989f, -1.4388f, 0.1298f, 0.0172f));
        PartDefinition cube_r14 = bone.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(0, 116).addBox(-2.0f, -8.0f, -2.0f, 4.0f, 8.0f, 4.0f, new CubeDeformation(0.0f)).texOffs(0, 97).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 8.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-1.8743f, -11.8802f, -0.2859f, -1.4399f, 0.0f, 0.0f));
        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.bone.xRot = headPitch / 57.295776f;
    }
}

