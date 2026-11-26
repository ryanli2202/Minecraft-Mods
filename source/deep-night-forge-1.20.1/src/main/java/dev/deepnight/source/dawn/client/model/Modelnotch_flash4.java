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

public class Modelnotch_flash4<T extends Entity>
extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("simple_mobs", "modelnotch_flash_4"), "main");
    public final ModelPart bone;

    public Modelnotch_flash4(ModelPart root) {
        this.bone = root.getChild("bone");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 74).addBox(-8.0f, 8.0f, -73.0f, 4.0f, 4.0f, 70.0f, new CubeDeformation(-0.4f)).texOffs(0, 148).addBox(-4.0f, 11.0f, -73.0f, 3.0f, 3.0f, 70.0f, new CubeDeformation(-0.4f)).texOffs(78, 78).addBox(3.6f, 11.1f, -73.0f, 3.0f, 3.0f, 70.0f, new CubeDeformation(-0.4f)).texOffs(78, 4).addBox(-11.3f, 6.3f, -73.0f, 3.0f, 3.0f, 70.0f, new CubeDeformation(-0.4f)), PartPose.offset(2.9f, -6.0f, 7.0f));
        PartDefinition cube_r1 = bone.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.7f, 0.3f, -35.0f, 4.0f, 4.0f, 70.0f, new CubeDeformation(-0.4f)), PartPose.offsetAndRotation(3.0f, 12.0f, -38.0f, 0.0f, 0.0f, 0.4363f));
        PartDefinition cube_r2 = bone.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(81, 154).addBox(-30.0f, -23.0f, -8.0f, 60.0f, 60.0f, 0.0f, new CubeDeformation(0.4f)), PartPose.offsetAndRotation(-2.9f, 0.0f, 0.0f, -0.2182f, 0.0f, 0.0f));
        return LayerDefinition.create(meshdefinition, 256, 256);
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

