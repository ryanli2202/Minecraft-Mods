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

public class Modelsword_thrown<T extends Entity>
extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("simple_mobs", "modelsword_thrown"), "main");
    public final ModelPart sword;

    public Modelsword_thrown(ModelPart root) {
        this.sword = root.getChild("sword");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition sword = partdefinition.addOrReplaceChild("sword", CubeListBuilder.create().texOffs(12, 16).addBox(-0.55f, -1.6477f, 0.3909f, 1.0f, 4.0f, 1.0f, new CubeDeformation(0.0f)).texOffs(16, 16).addBox(-0.55f, -8.6477f, -7.6091f, 1.0f, 4.0f, 1.0f, new CubeDeformation(0.0f)).texOffs(0, 0).addBox(-0.55f, 4.3523f, 4.3909f, 1.0f, 3.0f, 3.0f, new CubeDeformation(0.0f)).texOffs(16, 4).addBox(-0.55f, 3.3523f, 3.3909f, 1.0f, 1.0f, 2.0f, new CubeDeformation(0.0f)).texOffs(18, 2).addBox(-0.55f, 2.3523f, 3.3909f, 1.0f, 1.0f, 1.0f, new CubeDeformation(0.0f)).texOffs(10, 0).addBox(-0.55f, -2.6477f, -0.6091f, 1.0f, 5.0f, 1.0f, new CubeDeformation(0.0f)).texOffs(6, 8).addBox(-0.55f, -3.6477f, -1.6091f, 1.0f, 5.0f, 1.0f, new CubeDeformation(0.0f)).texOffs(10, 10).addBox(-0.55f, -4.6477f, -2.6091f, 1.0f, 5.0f, 1.0f, new CubeDeformation(0.0f)).texOffs(0, 12).addBox(-0.55f, -5.6477f, -3.6091f, 1.0f, 5.0f, 1.0f, new CubeDeformation(0.0f)).texOffs(4, 14).addBox(-0.55f, -6.6477f, -4.6091f, 1.0f, 5.0f, 1.0f, new CubeDeformation(0.0f)).texOffs(14, 10).addBox(-0.55f, -7.6477f, -5.6091f, 1.0f, 5.0f, 1.0f, new CubeDeformation(0.0f)).texOffs(14, 0).addBox(-0.55f, -8.6477f, -6.6091f, 1.0f, 5.0f, 1.0f, new CubeDeformation(0.0f)).texOffs(17, 7).addBox(-0.55f, -8.6477f, -8.6091f, 1.0f, 3.0f, 1.0f, new CubeDeformation(0.0f)).texOffs(18, 13).addBox(-0.55f, 2.3523f, -1.6091f, 1.0f, 1.0f, 1.0f, new CubeDeformation(0.0f)).texOffs(6, 4).addBox(-0.55f, 2.3523f, -0.6091f, 1.0f, 2.0f, 2.0f, new CubeDeformation(0.0f)).texOffs(10, 6).addBox(-0.55f, 3.3523f, -2.6091f, 1.0f, 2.0f, 2.0f, new CubeDeformation(0.0f)).texOffs(18, 11).addBox(-0.55f, -1.6477f, 2.3909f, 1.0f, 1.0f, 1.0f, new CubeDeformation(0.0f)).texOffs(0, 6).addBox(-0.55f, -0.6477f, 1.3909f, 1.0f, 4.0f, 2.0f, new CubeDeformation(0.0f)).texOffs(8, 16).addBox(-0.55f, -2.6477f, 3.3909f, 1.0f, 4.0f, 1.0f, new CubeDeformation(0.0f)).texOffs(5, 0).addBox(-0.55f, -2.6477f, 4.3909f, 1.0f, 2.0f, 1.0f, new CubeDeformation(0.0f)).texOffs(0, 18).addBox(-0.55f, 4.3523f, 3.3909f, 1.0f, 1.0f, 1.0f, new CubeDeformation(0.0f)).texOffs(18, 0).addBox(-0.55f, 3.3523f, 2.3909f, 1.0f, 1.0f, 1.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.6f, 23.9686f, 1.1541f, 0.7854f, 0.0f, -1.5708f));
        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.sword.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

