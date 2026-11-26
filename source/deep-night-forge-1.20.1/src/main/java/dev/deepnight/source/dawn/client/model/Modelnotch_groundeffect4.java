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

public class Modelnotch_groundeffect4<T extends Entity>
extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("simple_mobs", "modelnotch_groundeffect_4"), "main");
    public final ModelPart bb_main;

    public Modelnotch_groundeffect4(ModelPart root) {
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-50.0f, -0.1f, -50.0f, 100.0f, 0.0f, 100.0f, new CubeDeformation(0.0f)).texOffs(0, 0).addBox(-10.0f, -26.0f, -10.0f, 20.0f, 26.0f, 20.0f, new CubeDeformation(0.0f)), PartPose.offset(0.0f, 24.0f, 0.0f));
        PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(-30, 482).addBox(-15.0f, 0.0f, -15.0f, 30.0f, 0.0f, 30.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-21.8178f, -11.6303f, -12.195f, 1.6539f, -0.3031f, -0.5668f));
        PartDefinition cube_r2 = bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(-30, 482).addBox(-19.0f, 0.0f, -17.0f, 30.0f, 0.0f, 30.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(20.7078f, -11.0001f, -24.5695f, 2.0194f, 0.5545f, 0.6969f));
        PartDefinition cube_r3 = bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(-30, 482).addBox(-21.0f, 0.0f, -21.0f, 30.0f, 0.0f, 30.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(25.2574f, -13.2203f, 24.3341f, 0.7724f, -0.8312f, 0.9542f));
        PartDefinition cube_r4 = bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(-30, 482).addBox(-18.0f, 0.0f, -16.0f, 30.0f, 0.0f, 30.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-16.4612f, -11.9011f, 21.7274f, -0.5455f, -0.8731f, 1.8525f));
        return LayerDefinition.create(meshdefinition, 512, 512);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

