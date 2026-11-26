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

public class Modelelemental_chain<T extends Entity>
extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("simple_mobs", "modelelemental_chain"), "main");
    public final ModelPart bone;

    public Modelelemental_chain(ModelPart root) {
        this.bone = root.getChild("bone");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(52, 40).addBox(-12.7953f, -13.3208f, -0.15f, 26.0f, 26.0f, 0.0f, new CubeDeformation(0.1f)), PartPose.offset(-0.2047f, 24.3208f, 0.15f));
        PartDefinition cube_r1 = bone.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 9).addBox(-79.5f, 1.9f, 1.3f, 160.0f, 10.0f, 1.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(5.2047f, -3.3208f, -80.15f, 0.0f, -1.5708f, 0.7854f));
        PartDefinition cube_r2 = bone.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 9).addBox(-80.0f, -6.2f, 5.9f, 160.0f, 10.0f, 1.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(5.2047f, -3.3208f, -80.15f, 0.0f, -1.5708f, -0.7854f));
        PartDefinition cube_r3 = bone.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 40).addBox(-13.0f, -13.0f, 0.0f, 26.0f, 26.0f, 0.0f, new CubeDeformation(0.1f)), PartPose.offsetAndRotation(0.2047f, -0.3208f, -0.05f, 0.0f, 0.0f, -0.7854f));
        return LayerDefinition.create(meshdefinition, 512, 512);
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

