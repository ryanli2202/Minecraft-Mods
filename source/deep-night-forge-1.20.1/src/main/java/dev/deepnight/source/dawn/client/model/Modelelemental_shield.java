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

public class Modelelemental_shield<T extends Entity>
extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("simple_mobs", "modelelemental_shield"), "main");
    public final ModelPart bb_main;

    public Modelelemental_shield(ModelPart root) {
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0f, 24.0f, 0.0f));
        PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0f, -9.0f, -9.0f, 18.0f, 18.0f, 18.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(1.0f, -29.0f, 0.0f, -0.798f, -0.7977f, 0.0177f));
        PartDefinition cube_r2 = bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 226).addBox(-17.0f, -15.0f, -6.0f, 30.0f, 30.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-17.7821f, -30.1609f, -21.4753f, -1.5489f, 0.8731f, 0.3253f));
        PartDefinition cube_r3 = bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 226).addBox(-15.0f, -15.0f, 0.0f, 30.0f, 30.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(26.9457f, -31.619f, 2.9673f, 0.0726f, -0.8026f, -1.5652f));
        PartDefinition cube_r4 = bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 226).addBox(-36.0f, -15.0f, 27.0f, 30.0f, 30.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-4.5876f, -49.2539f, 30.2124f, 1.8239f, -0.2269f, -2.9855f));
        PartDefinition cube_r5 = bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 226).addBox(-15.0f, -15.0f, 0.0f, 30.0f, 30.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-19.7431f, -35.5777f, 14.1105f, 0.6336f, 0.6679f, 2.4075f));
        PartDefinition cube_r6 = bb_main.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 226).addBox(-15.0f, -23.0f, 0.0f, 30.0f, 30.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(11.3632f, -46.1246f, -25.5969f, -1.6944f, -0.7057f, -1.3043f));
        PartDefinition cube_r7 = bb_main.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 226).addBox(-15.0f, -15.0f, 0.0f, 30.0f, 30.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-3.0f, -16.0191f, -24.0249f, -1.6148f, 0.4662f, -0.6562f));
        PartDefinition cube_r8 = bb_main.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0f, -9.0f, -9.0f, 18.0f, 18.0f, 18.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(1.0f, -29.0f, 0.0f, -1.3972f, 0.3151f, -2.0764f));
        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

