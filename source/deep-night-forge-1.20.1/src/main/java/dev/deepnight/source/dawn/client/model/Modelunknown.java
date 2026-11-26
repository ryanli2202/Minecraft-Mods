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

public class Modelunknown<T extends Entity>
extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("simple_mobs", "modelunknown"), "main");
    public final ModelPart bipedHead;

    public Modelunknown(ModelPart root) {
        this.bipedHead = root.getChild("bipedHead");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition bipedHead = partdefinition.addOrReplaceChild("bipedHead", CubeListBuilder.create(), PartPose.offset(0.0f, 4.0f, 0.0f));
        PartDefinition armorHead = bipedHead.addOrReplaceChild("armorHead", CubeListBuilder.create(), PartPose.offset(7.0f, -28.0f, -10.0f));
        PartDefinition head = armorHead.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 17).addBox(-12.0f, 13.5f, 5.0f, 8.0f, 10.0f, 8.0f, new CubeDeformation(0.5f)).texOffs(0, 0).addBox(-16.0f, 16.15f, 0.0f, 16.0f, 0.0f, 17.0f, new CubeDeformation(0.0f)), PartPose.offset(0.0f, 0.0f, 0.0f));
        PartDefinition nose = head.addOrReplaceChild("nose", CubeListBuilder.create(), PartPose.offset(0.0f, -1.5f, 0.0f));
        PartDefinition nose2 = nose.addOrReplaceChild("nose2", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0f, 21.3f, 3.5f, 2.0f, 4.0f, 5.0f, new CubeDeformation(0.25f)).texOffs(0, 4).addBox(-8.0f, 20.0f, 3.5f, 0.0f, 1.0f, 5.0f, new CubeDeformation(0.0f)), PartPose.offset(0.0f, 0.0f, -3.5f));
        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.bipedHead.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }
}

