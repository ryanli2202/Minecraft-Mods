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

public class Modelthunder_effect_1<T extends Entity>
extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("simple_mobs", "modelthunder_effect_1"), "main");
    public final ModelPart bb_main;

    public Modelthunder_effect_1(ModelPart root) {
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-80.0f, -159.0f, 0.0f, 160.0f, 160.0f, 0.0f, new CubeDeformation(0.1f)), PartPose.offset(0.0f, 24.0f, 0.0f));
        PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 372).addBox(-95.0f, -100.0f, 0.0f, 140.0f, 140.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-24.4f, -27.0f, -45.0f, -0.7854f, 3.1416f, 0.0f));
        PartDefinition cube_r2 = bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 372).addBox(-95.0f, -100.0f, 0.0f, 140.0f, 140.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-24.4f, -27.0f, 44.0f, 0.7854f, 3.1416f, 0.0f));
        PartDefinition cube_r3 = bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 372).addBox(-95.0f, -100.0f, 0.0f, 140.0f, 140.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(39.6f, -27.0f, -23.0f, -0.7854f, 1.5708f, 0.0f));
        PartDefinition cube_r4 = bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 372).addBox(-95.0f, -100.0f, 0.0f, 140.0f, 140.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-39.4f, -27.0f, -23.0f, 0.7854f, 1.5708f, 0.0f));
        PartDefinition cube_r5 = bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-80.0f, -80.0f, 0.0f, 160.0f, 160.0f, 0.0f, new CubeDeformation(0.1f)), PartPose.offsetAndRotation(0.0f, -79.0f, 0.0f, 0.0f, -1.4835f, 0.0f));
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

