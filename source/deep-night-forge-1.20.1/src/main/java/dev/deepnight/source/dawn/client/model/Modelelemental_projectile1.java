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

public class Modelelemental_projectile1<T extends Entity>
extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("simple_mobs", "modelelemental_projectile_1"), "main");
    public final ModelPart bone;

    public Modelelemental_projectile1(ModelPart root) {
        this.bone = root.getChild("bone");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(7, 7).addBox(-1.434f, -1.4301f, -3.4505f, 3.0f, 3.0f, 17.0f, new CubeDeformation(-0.5f)), PartPose.offset(-0.066f, 22.4301f, -9.5495f));
        PartDefinition cube_r1 = bone.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 44).addBox(-3.4f, -10.0f, 1.7f, 5.0f, 20.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(3.1372f, -1.609f, 5.1825f, -1.4418f, -0.0258f, 0.6537f));
        PartDefinition cube_r2 = bone.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 44).addBox(-2.5f, -10.0f, 0.0f, 5.0f, 20.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(1.3526f, 0.7976f, 5.0416f, -1.6738f, -0.05f, -0.9617f));
        PartDefinition cube_r3 = bone.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 44).addBox(-1.9f, -10.0f, -0.7f, 5.0f, 20.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.8671f, -0.0301f, 4.8474f, -1.4993f, -0.05f, -0.9617f));
        PartDefinition cube_r4 = bone.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 44).addBox(-2.5f, -22.9f, 2.4f, 5.0f, 20.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-1.6604f, -0.0301f, -2.1484f, -1.4835f, 0.0f, -1.5708f));
        PartDefinition cube_r5 = bone.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5f, -2.9f, -0.6f, 5.0f, 5.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-1.6604f, -0.0301f, -2.1484f, -0.7854f, 0.0f, -1.5708f));
        PartDefinition cube_r6 = bone.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 44).addBox(-2.5f, -10.0f, 2.0f, 5.0f, 20.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(2.4688f, -0.0301f, 10.7628f, -1.4835f, 0.0f, 1.5708f));
        PartDefinition cube_r7 = bone.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5f, -2.9f, -0.4f, 5.0f, 5.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(1.8044f, -0.0301f, -2.2191f, -0.7418f, 0.0f, 1.5708f));
        PartDefinition cube_r8 = bone.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 44).addBox(-2.5f, -13.0f, 4.1f, 5.0f, 20.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.034f, -4.2276f, 7.8633f, -1.6144f, 0.0f, 0.0f));
        PartDefinition cube_r9 = bone.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 44).addBox(-2.5f, -13.0f, 4.0f, 5.0f, 20.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.034f, -4.2276f, 7.8633f, -1.4835f, 0.0f, 0.0f));
        PartDefinition cube_r10 = bone.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5f, -3.5f, 0.0f, 5.0f, 5.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.034f, -1.0301f, -3.2505f, -0.7854f, 0.0f, 0.0f));
        PartDefinition cube_r11 = bone.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5f, -2.6f, 0.0f, 5.0f, 5.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.034f, 1.6569f, -2.5433f, -2.3562f, 0.0f, 0.0f));
        return LayerDefinition.create(meshdefinition, 64, 64);
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

