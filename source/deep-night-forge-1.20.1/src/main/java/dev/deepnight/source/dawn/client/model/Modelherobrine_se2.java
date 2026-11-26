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

public class Modelherobrine_se2<T extends Entity>
extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("simple_mobs", "modelherobrine_se_2"), "main");
    public final ModelPart bb_main;

    public Modelherobrine_se2(ModelPart root) {
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0f, 24.0f, 0.0f));
        PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 32).addBox(-0.6f, -16.0f, -1.8f, 0.0f, 32.0f, 32.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, -28.0f, -47.0f, 0.0f, -0.48f, -1.5708f));
        PartDefinition cube_r2 = bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 32).addBox(1.0f, -16.0f, -1.4f, 0.0f, 32.0f, 32.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, -28.0f, -47.0f, 0.0f, 0.4625f, -1.5708f));
        PartDefinition cube_r3 = bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 32).addBox(0.0f, -15.1f, -0.4437f, 0.0f, 32.0f, 32.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.364f, -27.9f, -47.9497f, 0.0f, -0.5236f, 0.0f));
        PartDefinition cube_r4 = bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 32).addBox(0.2121f, -15.0f, -0.2315f, 0.0f, 32.0f, 32.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.064f, -28.0f, -47.9497f, 0.0f, 0.5236f, 0.0f));
        PartDefinition cube_r5 = bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 0).addBox(0.0f, -8.0f, -48.0f, 0.0f, 16.0f, 96.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, -27.0f, 0.0f, 0.0f, 0.0f, 0.6545f));
        PartDefinition cube_r6 = bb_main.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 0).addBox(0.0f, -8.0f, -48.0f, 0.0f, 16.0f, 96.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, -27.0f, 0.0f, 0.0f, 0.0f, -0.6545f));
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

