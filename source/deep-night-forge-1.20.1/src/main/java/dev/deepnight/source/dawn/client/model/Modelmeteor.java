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

public class Modelmeteor<T extends Entity>
extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("simple_mobs", "modelmeteor"), "main");
    public final ModelPart bone;

    public Modelmeteor(ModelPart root) {
        this.bone = root.getChild("bone");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(1.0f, 38.0f, -4.0f));
        PartDefinition cube_r1 = bone.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 160).addBox(-8.0f, -3.0f, -8.0f, 16.0f, 16.0f, 16.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(15.1624f, -5.7125f, -19.6184f, 0.0439f, 0.2732f, -0.1987f));
        PartDefinition cube_r2 = bone.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 160).addBox(15.0f, 2.0f, -10.0f, 16.0f, 16.0f, 16.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-1.3117f, -25.9146f, 21.8318f, 0.1596f, 0.2319f, -0.0673f));
        PartDefinition cube_r3 = bone.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 96).addBox(-13.0f, -16.0f, -16.0f, 32.0f, 32.0f, 32.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-16.2312f, -11.7156f, -5.4977f, 0.2114f, 0.3601f, -0.0392f));
        PartDefinition cube_r4 = bone.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 96).addBox(-26.0f, -24.0f, -8.0f, 32.0f, 32.0f, 32.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(21.3726f, -27.2437f, -11.0765f, 0.2987f, 0.3601f, -0.0392f));
        PartDefinition cube_r5 = bone.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-24.0f, -24.0f, -24.0f, 48.0f, 48.0f, 48.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, -24.0f, 0.0f, 0.2679f, 0.2106f, 0.0573f));
        PartDefinition cube_r6 = bone.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 160).addBox(-8.0f, -8.0f, -8.0f, 16.0f, 16.0f, 16.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-18.3495f, -43.8627f, -1.5257f, -0.0644f, 0.2009f, 0.1086f));
        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }
}

