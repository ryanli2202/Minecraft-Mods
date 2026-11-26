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

public class ModelLosteyeEffect3<T extends Entity>
extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("simple_mobs", "model_losteye_effect_3"), "main");
    public final ModelPart bb_main;

    public ModelLosteyeEffect3(ModelPart root) {
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0f, 24.0f, 0.0f));
        PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 96).addBox(-16.0f, -21.0f, 12.0f, 32.0f, 32.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-6.4899f, -33.3823f, 20.4344f, -2.138f, 0.0f, 1.5708f));
        PartDefinition cube_r2 = bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 96).addBox(-16.0f, -11.0f, -4.0f, 32.0f, 32.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(13.4796f, -33.3823f, 21.8302f, -0.7854f, 0.0f, 1.5708f));
        PartDefinition cube_r3 = bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 96).addBox(-16.0f, -33.5f, 14.0f, 32.0f, 32.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-2.0f, -37.3823f, 14.1477f, -2.3998f, 0.0f, 0.0f));
        PartDefinition cube_r4 = bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 96).addBox(-16.0f, -31.0f, -13.0f, 32.0f, 32.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-2.0f, -33.3823f, 14.1477f, -0.7854f, 0.0f, 0.0f));
        PartDefinition cube_r5 = bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-32.0f, -32.0f, 0.0f, 64.0f, 64.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-3.0f, -32.2767f, 3.6628f, -0.0436f, 0.0f, 0.0f));
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

