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

public class Modelnotch_flash5<T extends Entity>
extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("simple_mobs", "modelnotch_flash_5"), "main");
    public final ModelPart boss;
    public final ModelPart bb_main;

    public Modelnotch_flash5(ModelPart root) {
        this.boss = root.getChild("boss");
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition boss = partdefinition.addOrReplaceChild("boss", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0f, 24.0f, 0.0f, 0.0f, -3.1416f, 0.0f));
        PartDefinition bone = boss.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0f, 11.0f, 0.0f));
        PartDefinition Body = bone.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 18).addBox(-4.64f, -13.96f, -2.32f, 9.0f, 13.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, -11.88f, 0.0f, 0.2259f, -0.1744f, -0.0443f));
        PartDefinition RightArm = Body.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(52, 20).addBox(-0.22f, -0.98f, -2.32f, 4.0f, 6.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(4.86f, -11.98f, 0.0f, 0.7015f, -0.4f, -0.8685f));
        PartDefinition right_forearm = RightArm.addOrReplaceChild("right_forearm", CubeListBuilder.create().texOffs(52, 0).addBox(-2.32f, -0.08f, -2.32f, 4.0f, 6.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(2.1f, 6.06f, 0.0f, 1.117f, 0.0f, 0.1047f));
        PartDefinition log = right_forearm.addOrReplaceChild("log", CubeListBuilder.create().texOffs(0, 0).addBox(1.8498f, 1.6226f, 1.9156f, -4.0f, -4.0f, -4.0f, new CubeDeformation(2.0f)), PartPose.offsetAndRotation(-4.16f, -5.385f, -0.0474f, 1.6581f, 0.0f, 0.0f));
        PartDefinition cube_r1 = log.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(1.826f, 1.7216f, 1.8144f, -4.0f, -4.0f, -4.0f, new CubeDeformation(2.0f)), PartPose.offsetAndRotation(-0.018f, -1.1459f, 0.1014f, 0.0f, 0.0f, 0.0698f));
        PartDefinition cube_r2 = log.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(1.8306f, 1.7216f, 1.8376f, -4.0f, -4.0f, -4.0f, new CubeDeformation(2.0f)), PartPose.offsetAndRotation(-0.011f, -0.621f, 0.0842f, -0.0871f, -0.0046f, -0.0522f));
        PartDefinition LeftArm = Body.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(52, 20).addBox(-4.78f, -1.42f, -2.32f, 4.0f, 6.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-4.5f, -12.54f, 0.0f, -0.2131f, -0.1707f, 0.4583f));
        PartDefinition left_forearm = LeftArm.addOrReplaceChild("left_forearm", CubeListBuilder.create().texOffs(52, 0).addBox(-2.32f, -0.08f, -2.32f, 4.0f, 6.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-2.46f, 5.62f, 0.0f, 0.2182f, 0.0f, -0.0785f));
        PartDefinition head_real = Body.addOrReplaceChild("head_real", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0f, -14.12f, 0.0f, -0.179f, 0.1225f, 0.0634f));
        PartDefinition Head = head_real.addOrReplaceChild("Head", CubeListBuilder.create(), PartPose.offset(0.0f, 0.16f, 0.0f));
        PartDefinition Head_r1 = Head.addOrReplaceChild("Head_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-4.64f, -37.12f, -4.64f, 9.0f, 9.0f, 9.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, 27.84f, 0.0f, 0.0f, 0.0873f, 0.0f));
        PartDefinition LeftLeg = bone.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(20, 45).addBox(-2.32f, -0.18f, -2.32f, 4.0f, 6.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-3.204f, -14.84f, 3.0f, 1.9372f, 0.0291f, -1.2293f));
        PartDefinition left_lower = LeftLeg.addOrReplaceChild("left_lower", CubeListBuilder.create().texOffs(40, 38).addBox(-2.42f, 0.04f, -2.32f, 4.0f, 6.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, 6.74f, 2.0f, -1.6581f, 0.0f, 0.0f));
        PartDefinition RightLeg = bone.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(20, 45).addBox(-2.32f, 0.0f, -2.32f, 4.0f, 6.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(3.204f, -13.72f, 3.0f, 2.2925f, 0.0125f, 0.2296f));
        PartDefinition right_lower = RightLeg.addOrReplaceChild("right_lower", CubeListBuilder.create().texOffs(40, 38).addBox(-2.22f, 0.04f, -2.32f, 4.0f, 6.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, 6.32f, 0.5f, -1.6755f, 0.0f, 0.0f));
        PartDefinition fakelog = boss.addOrReplaceChild("fakelog", CubeListBuilder.create().texOffs(0, 0).addBox(1.8616f, 2.2519f, 1.8355f, -4.0f, -4.0f, -4.0f, new CubeDeformation(2.0f)), PartPose.offset(0.1488f, -17.0359f, 0.6709f));
        PartDefinition cube_r3 = fakelog.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 0).addBox(1.826f, 1.7216f, 1.8144f, -4.0f, -4.0f, -4.0f, new CubeDeformation(2.0f)), PartPose.offsetAndRotation(-0.0062f, -0.5166f, 0.0213f, 0.0f, 0.0f, 0.0698f));
        PartDefinition cube_r4 = fakelog.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 0).addBox(1.8306f, 1.7216f, 1.8376f, -4.0f, -4.0f, -4.0f, new CubeDeformation(2.0f)), PartPose.offsetAndRotation(8.0E-4f, 0.0083f, 0.0041f, -0.0871f, -0.0046f, -0.0522f));
        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0f, 24.0f, 0.0f));
        PartDefinition cube_r5 = bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(26, 8).addBox(0.0f, -5.0f, -9.0f, 0.0f, 10.0f, 10.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-5.1267f, -9.0043f, 4.0f, 0.0f, 0.0f, -0.6981f));
        PartDefinition cube_r6 = bb_main.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(26, 8).addBox(0.0f, -5.0f, -6.0f, 0.0f, 10.0f, 10.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(6.7901f, -11.648f, 5.0f, 0.0f, 0.0f, 0.7418f));
        PartDefinition cube_r7 = bb_main.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(26, 8).addBox(0.0f, -5.0f, -5.0f, 0.0f, 10.0f, 10.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(11.0f, -25.0f, -2.0f, 0.0f, 0.0f, 0.3054f));
        PartDefinition cube_r8 = bb_main.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(26, 8).addBox(0.0f, -5.0f, -6.0f, 0.0f, 10.0f, 10.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-6.0f, -25.0f, -5.0f, 0.0f, 0.0f, -0.3927f));
        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.boss.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

