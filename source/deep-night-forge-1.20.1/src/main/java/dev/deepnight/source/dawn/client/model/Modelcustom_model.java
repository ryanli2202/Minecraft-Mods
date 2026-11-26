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

public class Modelcustom_model<T extends Entity>
extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("simple_mobs", "modelcustom_model"), "main");
    public final ModelPart boss;
    public final ModelPart bb_main;

    public Modelcustom_model(ModelPart root) {
        this.boss = root.getChild("boss");
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition boss = partdefinition.addOrReplaceChild("boss", CubeListBuilder.create(), PartPose.offset(0.0f, 24.0f, 0.0f));
        PartDefinition bone = boss.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 7.0E-4f, -0.0431f, -0.0113f));
        PartDefinition Body = bone.addOrReplaceChild("Body", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0f, -13.88f, 0.0f, 0.0737f, 0.1257f, 0.0449f));
        PartDefinition Body_r1 = Body.addOrReplaceChild("Body_r1", CubeListBuilder.create().texOffs(0, 40).addBox(0.5406f, 3.5665f, -21.1882f, 9.0f, 14.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(5.6596f, -20.2669f, -14.0774f, -3.0051f, 0.0916f, 3.0811f));
        PartDefinition RightArm = Body.addOrReplaceChild("RightArm", CubeListBuilder.create(), PartPose.offsetAndRotation(4.86f, -12.98f, 0.0f, 0.6363f, 0.7934f, -0.8491f));
        PartDefinition RightArm_r1 = RightArm.addOrReplaceChild("RightArm_r1", CubeListBuilder.create().texOffs(106, 63).addBox(15.445f, 0.9749f, -17.4479f, 4.0f, 7.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(14.2414f, -6.7156f, -1.9987f, -0.696f, -0.8834f, -2.7006f));
        PartDefinition right_forearm = RightArm.addOrReplaceChild("right_forearm", CubeListBuilder.create(), PartPose.offsetAndRotation(2.1f, 6.06f, 0.0f, 0.5672f, 0.0f, 0.0f));
        PartDefinition RightArm_r2 = right_forearm.addOrReplaceChild("RightArm_r2", CubeListBuilder.create().texOffs(76, 106).addBox(15.445f, -1.4548f, -19.4067f, 4.0f, 7.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(12.1414f, -11.8487f, 5.1787f, 0.8072f, -0.9229f, 2.8268f));
        PartDefinition LeftArm = Body.addOrReplaceChild("LeftArm", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.5f, -12.54f, 0.0f, -0.0442f, 0.2423f, 0.2248f));
        PartDefinition LeftArm_r1 = LeftArm.addOrReplaceChild("LeftArm_r1", CubeListBuilder.create().texOffs(106, 63).addBox(1.4878f, 4.1133f, -19.9777f, 4.0f, 7.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(11.3203f, -9.2703f, -12.1247f, 3.0486f, 0.065f, 2.604f));
        PartDefinition left_forearm = LeftArm.addOrReplaceChild("left_forearm", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.46f, 5.62f, 0.0f, 0.0f, 0.0f, -0.0785f));
        PartDefinition LeftArm_r2 = left_forearm.addOrReplaceChild("LeftArm_r2", CubeListBuilder.create().texOffs(76, 106).addBox(0.5978f, 11.3376f, -19.9777f, 4.0f, 7.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(14.9061f, -13.7632f, -12.1247f, 3.0539f, 0.0721f, 2.7609f));
        PartDefinition log = left_forearm.addOrReplaceChild("log", CubeListBuilder.create(), PartPose.offsetAndRotation(19.5982f, -26.2642f, -14.5932f, -1.3263f, -0.3143f, -1.9895f));
        PartDefinition cube_r1 = log.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(90, 0).addBox(-12.707f, -3.6782f, 11.9796f, 12.0f, 22.0f, 12.0f, new CubeDeformation(2.0f)), PartPose.offsetAndRotation(-8.2845f, -7.3996f, -7.81f, 3.135f, -0.2936f, 0.7199f));
        PartDefinition cube_r2 = log.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(3, 60).addBox(-16.2178f, 41.686f, 6.0268f, 15.0f, 35.0f, 15.0f, new CubeDeformation(2.2f)), PartPose.offsetAndRotation(-8.2845f, -7.3996f, -7.81f, -3.1411f, -0.286f, 0.847f));
        PartDefinition cube_r3 = log.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(64, 60).addBox(-15.6746f, 14.4591f, 11.7862f, 14.0f, 32.0f, 14.0f, new CubeDeformation(2.2f)), PartPose.offsetAndRotation(-8.2845f, -7.3996f, -7.81f, 3.0112f, -0.286f, 0.847f));
        PartDefinition head_real = Body.addOrReplaceChild("head_real", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0f, -14.12f, 0.0f, -0.173f, -0.1161f, -0.1569f));
        PartDefinition Head = head_real.addOrReplaceChild("Head", CubeListBuilder.create(), PartPose.offset(0.0f, 0.16f, 0.0f));
        PartDefinition Head_r1 = Head.addOrReplaceChild("Head_r1", CubeListBuilder.create().texOffs(90, 34).addBox(-2.2849f, -1.3324f, -22.8908f, 9.0f, 9.0f, 9.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(4.8755f, -2.7314f, -15.4145f, 2.9209f, 0.1163f, -2.8916f));
        PartDefinition LeftLeg = bone.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.204f, -13.74f, 0.0f, -0.1215f, 0.0133f, 0.1738f));
        PartDefinition LeftLeg_r1 = LeftLeg.addOrReplaceChild("LeftLeg_r1", CubeListBuilder.create().texOffs(60, 106).addBox(1.1272f, 20.7508f, -17.8819f, 4.0f, 7.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(3.5334f, -17.9125f, -18.378f, 2.887f, 0.0462f, 2.8084f));
        PartDefinition left_lower = LeftLeg.addOrReplaceChild("left_lower", CubeListBuilder.create(), PartPose.offset(0.0f, 6.74f, 0.0f));
        PartDefinition LeftLeg_r2 = left_lower.addOrReplaceChild("LeftLeg_r2", CubeListBuilder.create().texOffs(0, 58).addBox(1.0272f, 27.7108f, -17.8819f, 4.0f, 7.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(3.5334f, -24.6525f, -18.378f, 2.887f, 0.0462f, 2.8084f));
        PartDefinition RightLeg = bone.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offsetAndRotation(2.204f, -13.92f, 0.0f, 0.2033f, 0.196f, -0.1149f));
        PartDefinition RightLeg_r1 = RightLeg.addOrReplaceChild("RightLeg_r1", CubeListBuilder.create().texOffs(60, 106).addBox(3.2372f, 15.458f, -23.1603f, 4.0f, 7.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(7.5318f, -21.195f, -10.8807f, -2.6813f, 0.0289f, -2.8807f));
        PartDefinition right_lower = RightLeg.addOrReplaceChild("right_lower", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0f, 6.92f, 0.0f, -0.1309f, 0.0f, 0.0f));
        PartDefinition RightLeg_r2 = right_lower.addOrReplaceChild("RightLeg_r2", CubeListBuilder.create().texOffs(0, 58).addBox(3.3372f, 24.9464f, -20.0559f, 4.0f, 7.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(7.5318f, -26.4543f, -14.4573f, -2.9389f, 0.0623f, -2.8865f));
        PartDefinition fakelog = boss.addOrReplaceChild("fakelog", CubeListBuilder.create(), PartPose.offset(0.1488f, -17.0359f, 0.6709f));
        PartDefinition cube_r4 = fakelog.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 0).addBox(-1.791f, 17.3776f, 18.437f, -4.0f, -4.0f, -4.0f, new CubeDeformation(2.0f)), PartPose.offsetAndRotation(-6.3059f, -15.8822f, 16.3987f, 3.1355f, 0.0871f, 3.0715f));
        PartDefinition cube_r5 = fakelog.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-3.609f, 15.839f, 19.7647f, -4.0f, -4.0f, -4.0f, new CubeDeformation(2.0f)), PartPose.offsetAndRotation(-6.3059f, -15.8822f, 16.3987f, 3.059f, 0.0917f, -3.0892f));
        PartDefinition cube_r6 = fakelog.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 0).addBox(-2.8325f, 18.1341f, 18.4368f, -4.0f, -4.0f, -4.0f, new CubeDeformation(2.0f)), PartPose.offsetAndRotation(-6.3059f, -15.8822f, 16.3987f, -3.1416f, 0.0873f, 3.1416f));
        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0f, 24.0f, 0.0f));
        PartDefinition cube_r7 = bb_main.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(-8, 0).addBox(-34.2292f, 10.5508f, -5.9718f, 30.0f, 0.0f, 30.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-6.1571f, -32.9181f, 17.0696f, 2.053f, -1.3137f, -2.4736f));
        PartDefinition cube_r8 = bb_main.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(-10, 0).mirror().addBox(6.1066f, -2.1567f, -20.3068f, 10.0f, 0.0f, 10.0f, new CubeDeformation(0.0f)).mirror(false), PartPose.offsetAndRotation(-6.1571f, -32.9181f, 17.0696f, 1.1489f, -0.2371f, 2.5744f));
        PartDefinition cube_r9 = bb_main.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(-10, 0).addBox(-19.5509f, 15.1821f, 8.979f, 10.0f, 0.0f, 10.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-6.1571f, -32.9181f, 17.0696f, -3.1159f, -0.0775f, 2.503f));
        PartDefinition cube_r10 = bb_main.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(-8, 0).addBox(-18.0968f, -27.733f, 4.5132f, 30.0f, 0.0f, 30.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-6.1571f, -32.9181f, 17.0696f, -1.9925f, -0.6858f, 2.2866f));
        PartDefinition cube_r11 = bb_main.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(-10, 0).mirror().addBox(4.5479f, 17.4932f, 17.5649f, 10.0f, 0.0f, 10.0f, new CubeDeformation(0.0f)).mirror(false), PartPose.offsetAndRotation(-6.1571f, -32.9181f, 17.0696f, 2.9362f, -0.2947f, 2.6551f));
        PartDefinition cube_r12 = bb_main.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(-10, 0).addBox(-26.0324f, 12.7067f, 14.5649f, 10.0f, 0.0f, 10.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-6.1571f, -32.9181f, 17.0696f, 2.7902f, -0.0674f, -2.8613f));
        PartDefinition cube_r13 = bb_main.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(-8, 0).addBox(-3.4223f, -5.6218f, -4.4936f, 30.0f, 0.0f, 30.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-6.1571f, -32.9181f, 17.0696f, -1.5502f, 0.477f, -1.5508f));
        return LayerDefinition.create(meshdefinition, 256, 256);
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

