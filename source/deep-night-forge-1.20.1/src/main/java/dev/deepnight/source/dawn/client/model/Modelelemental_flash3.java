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

public class Modelelemental_flash3<T extends Entity>
extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("simple_mobs", "modelelemental_flash_3"), "main");
    public final ModelPart elemental_god;

    public Modelelemental_flash3(ModelPart root) {
        this.elemental_god = root.getChild("elemental_god");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition elemental_god = partdefinition.addOrReplaceChild("elemental_god", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0f, -14.0f, 0.0f, -0.0873f, 0.0f, 0.0f));
        PartDefinition cube_r1 = elemental_god.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 112).addBox(-3.0f, -4.0f, 13.0f, 16.0f, 16.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.2921f, 4.3707f, -10.7404f, 0.7258f, 0.9385f, 2.2063f));
        PartDefinition cube_r2 = elemental_god.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 112).addBox(-3.0f, -27.0f, 0.0f, 16.0f, 16.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(11.0518f, 1.8389f, 11.8995f, 2.1611f, -1.0403f, 2.4428f));
        PartDefinition cube_r3 = elemental_god.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 112).addBox(-1.0f, 3.0f, 0.0f, 16.0f, 16.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(6.1474f, -24.7065f, 11.364f, -1.5504f, 0.2436f, -0.1778f));
        PartDefinition cube_r4 = elemental_god.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 112).addBox(-8.0f, -10.0f, 0.0f, 16.0f, 16.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(9.7479f, -13.143f, 10.214f, -1.5422f, -0.8034f, -0.2033f));
        PartDefinition cube_r5 = elemental_god.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 112).addBox(-8.0f, -8.0f, 0.0f, 16.0f, 16.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(14.0f, 7.9581f, -0.0413f, -1.4875f, -0.0262f, 0.3043f));
        PartDefinition body = elemental_god.addOrReplaceChild("body", CubeListBuilder.create().texOffs(34, 36).addBox(-4.5972f, -16.8275f, -2.3935f, 9.0f, 9.0f, 4.0f, new CubeDeformation(0.0f)).texOffs(0, 27).addBox(-6.3015f, -23.8923f, -2.9935f, 13.0f, 7.0f, 6.0f, new CubeDeformation(0.0f)).texOffs(28, 11).addBox(-6.5172f, -8.4275f, -2.4935f, 13.0f, 3.0f, 5.0f, new CubeDeformation(0.0f)).texOffs(0, 0).addBox(-8.1015f, -25.0923f, -2.4935f, 16.0f, 6.0f, 5.0f, new CubeDeformation(0.0f)).texOffs(32, 21).addBox(-6.9972f, -4.8275f, -2.8935f, 14.0f, 4.0f, 6.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.0828f, 8.1875f, 1.4935f, -0.0873f, 0.0f, 0.0f));
        PartDefinition cube_r6 = body.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(20, 85).addBox(5.5866f, -6.0028f, 2.1885f, 2.0f, 4.0f, 3.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(2.4128f, -13.5462f, -3.7743f, -0.0753f, 0.4898f, 0.5193f));
        PartDefinition cube_r7 = body.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 73).addBox(3.8526f, 0.5004f, 1.2239f, 4.0f, 5.0f, 4.0f, new CubeDeformation(0.0f)).texOffs(70, 71).addBox(3.6126f, -4.1796f, 1.0239f, 4.0f, 4.0f, 5.0f, new CubeDeformation(0.0f)).texOffs(44, 49).addBox(3.0126f, -10.4196f, 0.4239f, 6.0f, 6.0f, 6.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(2.4128f, -13.5462f, -3.7743f, -0.2798f, 0.3921f, 0.0479f));
        PartDefinition cube_r8 = body.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(60, 62).addBox(-1.817f, -9.1432f, 1.2069f, 5.0f, 4.0f, 5.0f, new CubeDeformation(0.0f)).texOffs(54, 71).addBox(-1.217f, -4.4632f, 1.6069f, 4.0f, 7.0f, 4.0f, new CubeDeformation(0.0f)).texOffs(80, 45).addBox(1.1701f, 1.4909f, 1.7047f, 2.0f, 3.0f, 4.0f, new CubeDeformation(0.0f)).texOffs(0, 40).addBox(2.4901f, 3.7709f, 2.8047f, 1.0f, 3.0f, 1.0f, new CubeDeformation(0.0f)).texOffs(3, 43).addBox(2.4901f, 3.7709f, 5.2047f, 1.0f, 3.0f, 1.0f, new CubeDeformation(0.0f)).texOffs(88, 11).addBox(-1.4715f, 1.9618f, 1.3128f, 2.0f, 3.0f, 2.0f, new CubeDeformation(0.0f)).texOffs(38, 31).addBox(2.4901f, 3.7709f, 1.6047f, 1.0f, 3.0f, 1.0f, new CubeDeformation(0.0f)).texOffs(41, 0).addBox(2.4901f, 3.7709f, 4.0047f, 1.0f, 3.0f, 1.0f, new CubeDeformation(0.0f)).texOffs(0, 82).addBox(2.143f, -6.9832f, 1.9069f, 1.0f, 4.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(2.4128f, -13.5462f, -3.7743f, 0.2987f, 1.3387f, 2.0702f));
        PartDefinition cube_r9 = body.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(10, 85).addBox(-1.0219f, -9.4493f, 2.4215f, 2.0f, 4.0f, 3.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(2.4128f, -13.5462f, -3.7743f, 1.1671f, 1.0303f, 2.9889f));
        PartDefinition cube_r10 = body.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 0).addBox(3.9716f, 4.7396f, 4.1219f, 0.0f, 3.0f, 0.0f, new CubeDeformation(0.0f)).texOffs(0, 0).addBox(3.9716f, 4.7396f, 5.3219f, 0.0f, 3.0f, 0.0f, new CubeDeformation(0.0f)).texOffs(0, 0).addBox(3.9716f, 4.7396f, 2.9219f, 0.0f, 3.0f, 0.0f, new CubeDeformation(0.0f)).texOffs(0, 0).addBox(3.9716f, 4.7396f, 1.7219f, 0.0f, 3.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(2.4128f, -13.5462f, -3.7743f, 1.0454f, 1.1449f, 2.8517f));
        PartDefinition cube_r11 = body.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(0, 49).addBox(-1.8134f, 5.3243f, 1.5866f, 1.0f, 2.0f, 1.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(2.4128f, -13.5462f, -3.7743f, -0.4219f, 1.3191f, 1.3298f));
        PartDefinition cube_r12 = body.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(59, 0).addBox(2.6986f, -9.6511f, -5.963f, 6.0f, 6.0f, 6.0f, new CubeDeformation(0.0f)).texOffs(36, 67).addBox(3.2986f, -3.4111f, -5.363f, 4.0f, 4.0f, 5.0f, new CubeDeformation(0.0f)).texOffs(72, 21).addBox(3.5386f, 1.2689f, -5.163f, 4.0f, 5.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-2.8222f, -13.389f, -4.0634f, -2.9467f, 0.3972f, 3.0752f));
        PartDefinition cube_r13 = body.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(23, 40).addBox(6.2557f, -5.0062f, -4.363f, 2.0f, 4.0f, 3.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-2.8222f, -13.389f, -4.0634f, 3.1397f, 0.4401f, 2.5991f));
        PartDefinition cube_r14 = body.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(0, 64).addBox(-2.3663f, -7.7691f, -6.3107f, 5.0f, 4.0f, 5.0f, new CubeDeformation(0.0f)).texOffs(80, 80).addBox(1.5937f, -5.6091f, -5.6107f, 1.0f, 4.0f, 4.0f, new CubeDeformation(0.0f)).texOffs(80, 30).addBox(0.6553f, 2.9137f, -5.8111f, 2.0f, 3.0f, 4.0f, new CubeDeformation(0.0f)).texOffs(0, 0).addBox(1.9753f, 5.1937f, -2.3111f, 1.0f, 3.0f, 1.0f, new CubeDeformation(0.0f)).texOffs(0, 27).addBox(1.9753f, 5.1937f, -3.5111f, 1.0f, 3.0f, 1.0f, new CubeDeformation(0.0f)).texOffs(28, 11).addBox(1.9753f, 5.1937f, -4.7111f, 1.0f, 3.0f, 1.0f, new CubeDeformation(0.0f)).texOffs(37, 0).addBox(1.9753f, 5.1937f, -5.9111f, 1.0f, 3.0f, 1.0f, new CubeDeformation(0.0f)).texOffs(49, 67).addBox(-2.1165f, 2.928f, -3.5135f, 2.0f, 3.0f, 2.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-2.8222f, -14.389f, -3.0634f, 2.8077f, 1.1936f, 1.1421f));
        PartDefinition cube_r15 = body.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(68, 49).addBox(-1.7663f, -3.0891f, -5.9107f, 4.0f, 7.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-2.8222f, -13.389f, -4.0634f, 2.598f, 1.3072f, 0.9426f));
        PartDefinition cube_r16 = body.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(84, 18).addBox(-0.3068f, -7.802f, -5.1107f, 2.0f, 4.0f, 3.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-2.8222f, -13.389f, -4.0634f, 1.9689f, 0.958f, 0.2625f));
        PartDefinition cube_r17 = body.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(0, 0).addBox(4.3507f, 6.3984f, -2.2111f, 0.0f, 3.0f, 0.0f, new CubeDeformation(0.0f)).texOffs(0, 0).addBox(4.3507f, 6.3984f, -3.4111f, 0.0f, 3.0f, 0.0f, new CubeDeformation(0.0f)).texOffs(0, 0).addBox(4.3507f, 6.3984f, -4.6111f, 0.0f, 3.0f, 0.0f, new CubeDeformation(0.0f)).texOffs(0, 0).addBox(4.3507f, 6.3984f, -5.8111f, 0.0f, 3.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-2.8222f, -14.389f, -3.0634f, 2.2934f, 1.0167f, 0.5718f));
        PartDefinition cube_r18 = body.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(30, 40).addBox(-2.8455f, 6.0218f, -3.2135f, 1.0f, 2.0f, 1.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-2.8222f, -14.389f, -3.0634f, -3.0198f, 1.2126f, 1.6291f));
        PartDefinition cube_r19 = body.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(46, 80).addBox(-0.4612f, -7.2612f, -4.4049f, 3.0f, 8.0f, 2.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.5385f, -29.7111f, -3.8537f, 0.5299f, -0.7119f, -0.3655f));
        PartDefinition cube_r20 = body.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(56, 82).addBox(1.3997f, -8.4764f, -3.6469f, 5.0f, 8.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.5385f, -29.7111f, -3.8537f, 0.4565f, -0.5194f, -0.2391f));
        PartDefinition cube_r21 = body.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(30, 83).addBox(0.5074f, -7.1412f, -4.5447f, 5.0f, 8.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.5385f, -29.7111f, -3.8537f, 0.4287f, -0.4011f, -0.1766f));
        PartDefinition cube_r22 = body.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(83, 67).addBox(-6.3605f, -8.4764f, -4.1479f, 5.0f, 8.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.5385f, -29.7111f, -3.8537f, 0.4565f, 0.5194f, 0.2391f));
        PartDefinition cube_r23 = body.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(84, 37).addBox(-5.3176f, -7.1412f, -4.3744f, 5.0f, 8.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.5385f, -29.7111f, -3.8537f, 0.4287f, 0.4011f, 0.1766f));
        PartDefinition cube_r24 = body.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(68, 80).addBox(-3.1551f, -7.2612f, -3.9212f, 4.0f, 8.0f, 2.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.5385f, -29.7111f, -3.8537f, 0.5299f, 0.7119f, 0.3655f));
        PartDefinition cube_r25 = body.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(84, 52).addBox(-2.283f, -7.1412f, -4.0398f, 5.0f, 8.0f, 0.0f, new CubeDeformation(0.0f)).texOffs(16, 75).addBox(-2.283f, -7.3812f, -3.4398f, 5.0f, 8.0f, 2.0f, new CubeDeformation(0.0f)).texOffs(83, 6).addBox(-2.163f, -0.4212f, -3.0398f, 4.0f, 3.0f, 2.0f, new CubeDeformation(0.0f)).texOffs(0, 40).addBox(-3.963f, 1.0188f, -1.0398f, 8.0f, 2.0f, 7.0f, new CubeDeformation(0.0f)).texOffs(0, 11).addBox(-5.163f, -7.3812f, -1.0398f, 10.0f, 8.0f, 8.0f, new CubeDeformation(0.0f)).texOffs(38, 31).addBox(-1.563f, 3.4188f, 0.9602f, 4.0f, 1.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.5385f, -29.7111f, -3.8537f, 0.3927f, 0.0f, 0.0f));
        PartDefinition cube_r26 = body.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(41, 61).addBox(-6.9264f, -3.33f, -1.776f, 9.0f, 3.0f, 3.0f, new CubeDeformation(0.0f)).texOffs(17, 49).addBox(-1.5984f, -3.33f, 1.554f, 3.0f, 3.0f, 2.0f, new CubeDeformation(0.0f)).texOffs(60, 43).addBox(-6.9264f, -3.33f, 3.774f, 9.0f, 3.0f, 3.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(4.5247f, -24.3348f, -2.7575f, 0.0f, 0.0f, -0.7854f));
        PartDefinition cube_r27 = body.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(60, 37).addBox(-2.664f, -3.7296f, -1.665f, 9.0f, 3.0f, 3.0f, new CubeDeformation(0.0f)).texOffs(39, 49).addBox(-2.664f, -3.7296f, 1.665f, 2.0f, 3.0f, 2.0f, new CubeDeformation(0.0f)).texOffs(56, 31).addBox(-2.664f, -3.7296f, 3.885f, 9.0f, 3.0f, 3.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-4.8581f, -24.2995f, -2.7575f, 0.0f, 0.0f, 0.7854f));
        PartDefinition cube_r28 = body.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(64, 12).addBox(5.1f, 7.599f, 1.3f, 3.0f, 3.0f, 6.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(1.8703f, -16.6192f, -4.2935f, 0.0f, 0.0f, 0.7854f));
        PartDefinition right_leg = elemental_god.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(20, 64).addBox(-2.6167f, 10.868f, -1.9522f, 4.0f, 7.0f, 4.0f, new CubeDeformation(0.0f)).texOffs(22, 49).addBox(-3.0967f, 0.068f, -2.2522f, 6.0f, 10.0f, 5.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-5.1833f, 8.092f, 1.2522f, -0.0175f, 0.3054f, 0.0f));
        PartDefinition cube_r29 = right_leg.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(86, 78).addBox(-0.9072f, 19.3848f, -0.7f, 2.0f, 4.0f, 2.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-8.5262f, -9.4768f, 0.1478f, 3.1285f, 0.0416f, 2.8359f));
        PartDefinition cube_r30 = right_leg.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(77, 0).addBox(-2.6996f, -0.3267f, -1.6159f, 4.0f, 2.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.2167f, 20.6172f, -3.2153f, 1.2654f, 0.0f, 0.0f));
        PartDefinition cube_r31 = right_leg.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(30, 76).addBox(-8.28f, -1.08f, -0.6f, 4.0f, 3.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(5.7833f, 19.508f, -2.2522f, 0.8727f, 0.0f, 0.0f));
        PartDefinition left_leg = elemental_god.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(42, 0).addBox(-2.2121f, 10.3558f, -1.5637f, 4.0f, 7.0f, 4.0f, new CubeDeformation(0.0f)).texOffs(0, 49).addBox(-2.8121f, -0.4442f, -1.8637f, 6.0f, 10.0f, 5.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(5.3464f, 8.4889f, 0.7749f, -0.0151f, -0.191f, -0.138f));
        PartDefinition cube_r32 = left_leg.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(0, 11).addBox(0.72f, 26.5436f, -1.0f, 2.0f, 4.0f, 2.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(8.9778f, -17.1478f, 0.8363f, 0.0f, 0.0f, 0.3054f));
        PartDefinition cube_r33 = left_leg.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(76, 12).addBox(-2.1991f, -0.285f, -1.6502f, 4.0f, 2.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.1879f, 20.1958f, -2.8637f, 1.309f, 0.0f, 0.0f));
        PartDefinition cube_r34 = left_leg.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(75, 60).addBox(-2.28f, -1.08f, -0.6f, 4.0f, 3.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.1879f, 18.9958f, -1.8637f, 0.8727f, 0.0f, 0.0f));
        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.elemental_god.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

