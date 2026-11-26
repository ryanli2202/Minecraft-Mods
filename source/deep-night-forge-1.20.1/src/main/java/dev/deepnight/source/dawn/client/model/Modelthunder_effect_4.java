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

public class Modelthunder_effect_4<T extends Entity>
extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("simple_mobs", "modelthunder_effect_4"), "main");
    public final ModelPart bb_main;

    public Modelthunder_effect_4(ModelPart root) {
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0f, 24.0f, 0.0f));
        PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(-112, 0).addBox(-50.0f, -9.0f, -41.0f, 112.0f, 0.0f, 112.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(74.0f, -9.0f, 0.0f, 0.0f, -1.6581f, -0.2618f));
        PartDefinition cube_r2 = bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(-112, 0).addBox(-52.0f, -5.0f, -44.0f, 112.0f, 0.0f, 112.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-61.2702f, -14.1392f, 0.0f, 0.0f, 1.3526f, 0.2618f));
        PartDefinition cube_r3 = bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(-112, 0).addBox(-56.0f, -8.0f, -50.0f, 112.0f, 0.0f, 112.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, -9.0f, 71.0f, 2.8798f, 0.0f, 3.1416f));
        PartDefinition cube_r4 = bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(-112, 0).addBox(-56.0f, -12.0f, -39.0f, 112.0f, 0.0f, 112.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, -9.0f, -71.0f, -0.2618f, 0.0f, 0.0f));
        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }
}

