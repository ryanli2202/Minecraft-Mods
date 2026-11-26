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

public class Modellightning_spear<T extends Entity>
extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("simple_mobs", "modellightning_spear"), "main");
    public final ModelPart bb_main;

    public Modellightning_spear(ModelPart root) {
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0f, 24.0f, 0.0f));
        PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(104, 77).addBox(-36.5f, -88.5f, -1.0f, 74.0f, 177.0f, 2.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-2.182f, 3.5f, 2.6464f, 0.0f, -0.7854f, 0.0f));
        PartDefinition cube_r2 = bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(104, 77).addBox(-37.0f, -88.5f, -1.5f, 74.0f, 177.0f, 2.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-2.182f, 3.5f, 2.6464f, 0.0f, 0.7854f, 0.0f));
        PartDefinition cube_r3 = bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-27.5f, -41.0f, 9.5f, 55.0f, 82.0f, 1.0f, new CubeDeformation(0.0f)).mirror(false), PartPose.offsetAndRotation(23.0885f, -85.9904f, 2.0f, 0.0f, -1.5708f, 0.2618f));
        PartDefinition cube_r4 = bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 0).addBox(-27.5f, -41.0f, 9.5f, 55.0f, 82.0f, 1.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-2.5f, -86.8564f, -26.0885f, 0.2182f, 0.0f, 0.0f));
        PartDefinition cube_r5 = bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-27.5f, -41.0f, -8.5f, 55.0f, 82.0f, 1.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-2.5f, -86.3564f, 26.9545f, -0.3491f, 0.0f, 0.0f));
        PartDefinition cube_r6 = bb_main.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 0).addBox(-27.5f, -41.0f, 10.5f, 55.0f, 82.0f, 1.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-25.0885f, -85.9904f, 2.0f, 0.0f, 1.5708f, -0.2618f));
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

