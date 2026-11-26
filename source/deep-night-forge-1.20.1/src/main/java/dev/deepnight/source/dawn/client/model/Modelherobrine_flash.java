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

public class Modelherobrine_flash<T extends Entity>
extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("simple_mobs", "modelherobrine_flash"), "main");
    public final ModelPart bone;

    public Modelherobrine_flash(ModelPart root) {
        this.bone = root.getChild("bone");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0f, 20.9f, 0.0f));
        PartDefinition cube_r1 = bone.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 88).addBox(-11.0f, -19.5f, -0.5f, 22.0f, 39.0f, 1.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, -16.4f, -0.5f, 0.0f, 0.0f, -0.2182f));
        PartDefinition Body = bone.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 20).addBox(-4.8f, -14.3f, -2.4f, 9.6f, 14.4f, 4.8f, new CubeDeformation(0.0f)), PartPose.offset(0.0f, -11.4f, 0.0f));
        PartDefinition RightArm = Body.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(40, 44).addBox(-4.6f, -1.3f, -2.4f, 4.8f, 7.2f, 4.8f, new CubeDeformation(0.0f)), PartPose.offset(-5.0f, -13.0f, 0.0f));
        PartDefinition right_fore = RightArm.addOrReplaceChild("right_fore", CubeListBuilder.create().texOffs(30, 32).addBox(-2.4f, -0.6f, -2.4f, 4.8f, 7.2f, 4.8f, new CubeDeformation(0.0f)), PartPose.offset(-2.2f, 6.5f, 0.0f));
        PartDefinition LeftArm = Body.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(20, 44).addBox(-0.2f, -2.3f, -2.4f, 4.8f, 7.2f, 4.8f, new CubeDeformation(0.0f)), PartPose.offset(5.0f, -12.0f, 0.0f));
        PartDefinition left_fore = LeftArm.addOrReplaceChild("left_fore", CubeListBuilder.create().texOffs(30, 20).addBox(-2.4f, -0.6f, -2.4f, 4.8f, 7.2f, 4.8f, new CubeDeformation(0.0f)), PartPose.offset(2.2f, 5.5f, 0.0f));
        PartDefinition Head = Body.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.2f, -6.9f, -4.8f, 10.0f, 10.0f, 10.0f, new CubeDeformation(0.0f)), PartPose.offset(0.0f, -17.4f, 0.0f));
        PartDefinition LeftLeg = bone.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(40, 0).addBox(-2.4f, 3.1f, -2.4f, 4.8f, 7.2f, 4.8f, new CubeDeformation(0.0f)), PartPose.offset(2.28f, -14.4f, 0.0f));
        PartDefinition left_lower = LeftLeg.addOrReplaceChild("left_lower", CubeListBuilder.create().texOffs(50, 24).addBox(-2.4f, 0.4f, -2.4f, 4.8f, 7.2f, 4.8f, new CubeDeformation(0.0f)), PartPose.offset(0.0f, 9.9f, 0.0f));
        PartDefinition RightLeg = bone.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(0, 39).addBox(-2.4f, 3.1f, -2.4f, 4.8f, 7.2f, 4.8f, new CubeDeformation(0.0f)), PartPose.offset(-2.28f, -14.4f, 0.0f));
        PartDefinition right_lower = RightLeg.addOrReplaceChild("right_lower", CubeListBuilder.create().texOffs(45, 12).addBox(-2.4f, 0.4f, -2.4f, 4.8f, 7.2f, 4.8f, new CubeDeformation(0.0f)), PartPose.offset(0.0f, 9.9f, 0.0f));
        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

