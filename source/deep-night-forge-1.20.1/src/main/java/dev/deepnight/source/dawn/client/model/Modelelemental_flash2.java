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

public class Modelelemental_flash2<T extends Entity>
extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("simple_mobs", "modelelemental_flash_2"), "main");
    public final ModelPart bb_main;

    public Modelelemental_flash2(ModelPart root) {
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-10.0f, -20.0f, -8.0f, 20.0f, 20.0f, 0.0f, new CubeDeformation(0.1f)), PartPose.offset(0.0f, 24.0f, 0.0f));
        PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 108).addBox(-9.0f, -10.0f, 3.0f, 20.0f, 20.0f, 0.0f, new CubeDeformation(0.1f)), PartPose.offsetAndRotation(-0.2181f, -4.1f, 1.5952f, 3.0146f, -1.2191f, 1.6901f));
        PartDefinition cube_r2 = bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 108).addBox(-8.0f, -10.0f, -2.0f, 20.0f, 20.0f, 0.0f, new CubeDeformation(0.1f)), PartPose.offsetAndRotation(-0.2181f, -16.1f, 1.5952f, 0.0097f, -1.1349f, -1.5611f));
        PartDefinition cube_r3 = bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 108).addBox(-5.5f, -10.0f, 5.7f, 20.0f, 20.0f, 0.0f, new CubeDeformation(0.1f)), PartPose.offsetAndRotation(0.0f, -10.0f, -1.0f, 0.0f, -1.8326f, 0.0f));
        PartDefinition cube_r4 = bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 108).addBox(-5.7f, -10.0f, -5.8f, 20.0f, 20.0f, 0.0f, new CubeDeformation(0.1f)), PartPose.offsetAndRotation(0.0f, -10.0f, -1.0f, 0.0f, -1.309f, 0.0f));
        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

