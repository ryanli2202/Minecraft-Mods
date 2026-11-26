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

public class Modelelemental_spawn<T extends Entity>
extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("simple_mobs", "modelelemental_spawn"), "main");
    public final ModelPart bb_main;

    public Modelelemental_spawn(ModelPart root) {
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-59.0f, -100.0f, 0.0f, 120.0f, 90.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offset(0.0f, 24.0f, 0.0f));
        PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-60.0f, -45.0f, 0.0f, 120.0f, 90.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(1.0f, -55.0f, 0.0f, 0.0f, 1.5708f, 0.0f));
        PartDefinition cube_r2 = bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 440).addBox(-35.0f, -45.0f, -25.0f, 72.0f, 72.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-15.0f, -36.0f, 4.0f, 0.0f, 1.5708f, -0.7418f));
        PartDefinition cube_r3 = bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 440).addBox(-36.0f, -45.0f, 25.0f, 72.0f, 72.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(16.0f, -36.0f, 4.0f, 0.0f, 1.5708f, 0.7418f));
        PartDefinition cube_r4 = bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 440).addBox(-36.0f, -45.0f, 25.0f, 72.0f, 72.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-3.0f, -36.0f, 15.0f, -0.7418f, 0.0f, 0.0f));
        PartDefinition cube_r5 = bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 440).addBox(-36.0f, -54.0f, -20.0f, 72.0f, 72.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-3.0f, -25.8661f, -12.0592f, 0.7418f, 0.0f, 0.0f));
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

