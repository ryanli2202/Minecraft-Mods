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

public class Modeldragon_smoke<T extends Entity>
extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("simple_mobs", "modeldragon_smoke"), "main");
    public final ModelPart bone;

    public Modeldragon_smoke(ModelPart root) {
        this.bone = root.getChild("bone");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0f, -92.5f, 0.0f));
        PartDefinition body = bone.addOrReplaceChild("body", CubeListBuilder.create().texOffs(9, 74).addBox(-41.872f, -24.5618f, -181.5951f, 82.08f, 66.69f, 97.47f, new CubeDeformation(0.0f)).texOffs(43, 0).addBox(-18.7651f, -14.155f, 40.868f, 36.0f, 36.0f, 67.5f, new CubeDeformation(0.0f)).texOffs(43, 0).addBox(-30.0151f, -23.155f, -84.682f, 58.5f, 56.25f, 72.0f, new CubeDeformation(0.0f)).texOffs(211, 220).addBox(-25.5151f, -18.655f, -12.682f, 49.5f, 47.25f, 54.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.287f, -14.0f, 67.232f, -0.0873f, 0.0f, 0.0f));
        PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(322, 257).addBox(-9.0f, 15.0f, -28.75f, 18.0f, 18.0f, 49.5f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-1.6651f, 3.845f, 191.618f, -0.1329f, -0.173f, 0.023f));
        PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(159, 103).addBox(-13.5f, -13.5f, -0.25f, 27.0f, 27.0f, 58.5f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.7651f, 3.845f, 108.618f, -0.3491f, 0.0f, 0.0f));
        PartDefinition cube_r3 = body.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(-52, 381).addBox(-18.4542f, 8.563f, 4.7137f, 148.5f, 0.0f, 125.25f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(48.4945f, -16.1559f, -142.8024f, -0.0063f, -0.0152f, 0.3927f));
        PartDefinition cube_r4 = body.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(-117, 199).addBox(-74.25f, -26.0f, -62.625f, 148.5f, 0.0f, 125.25f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-103.8812f, -7.6599f, -74.5527f, 0.005f, -0.0157f, 2.8361f));
        PartDefinition front_left = body.addOrReplaceChild("front_left", CubeListBuilder.create(), PartPose.offsetAndRotation(28.7374f, -1.0f, -152.5649f, 0.2618f, -0.2618f, -0.0873f));
        PartDefinition cube_r5 = front_left.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(43, 95).addBox(-17.45f, -12.95f, -29.0f, 34.9f, 43.9f, 34.0f, new CubeDeformation(-2.0f)), PartPose.offsetAndRotation(7.9052f, 38.4573f, 5.1248f, 0.4382f, -0.0531f, -0.0693f));
        PartDefinition cube_r6 = front_left.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(43, 274).addBox(-18.0f, -19.5f, -10.5f, 36.0f, 54.0f, 36.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(5.3781f, 11.8221f, -5.5808f, -0.3472f, -0.0531f, -0.0693f));
        PartDefinition front_left_lower = front_left.addOrReplaceChild("front_left_lower", CubeListBuilder.create(), PartPose.offsetAndRotation(9.7997f, 67.7712f, -0.1664f, -0.2182f, 0.0f, 0.0f));
        PartDefinition cube_r7 = front_left_lower.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(175, 199).addBox(-4.5f, -27.375f, -6.75f, 13.5f, 46.5f, 13.5f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-2.5025f, 56.855f, 3.8907f, -0.3052f, 0.0131f, -0.0893f));
        PartDefinition left_palm = front_left_lower.addOrReplaceChild("left_palm", CubeListBuilder.create(), PartPose.offset(3.2057f, 72.022f, -0.576f));
        PartDefinition cube_r8 = left_palm.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(165, 95).addBox(-9.0f, -4.5f, -14.625f, 18.0f, 9.0f, 22.5f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-1.7571f, 4.5307f, -0.9595f, -0.0421f, 0.0168f, -0.0021f));
        PartDefinition lightning_claw = left_palm.addOrReplaceChild("lightning_claw", CubeListBuilder.create(), PartPose.offset(2.0546f, -6.1953f, -77.2914f));
        PartDefinition front_right = body.addOrReplaceChild("front_right", CubeListBuilder.create(), PartPose.offsetAndRotation(-26.4612f, -3.5f, -146.9988f, 0.4363f, 0.2618f, 0.0f));
        PartDefinition cube_r9 = front_right.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(177, 0).addBox(-19.4143f, -4.5293f, -24.8188f, 34.9f, 43.9f, 34.0f, new CubeDeformation(-2.0f)), PartPose.offsetAndRotation(2.5798f, 34.9573f, -7.2864f, 0.4382f, -0.0531f, -0.0693f));
        PartDefinition front_right_lower = front_right.addOrReplaceChild("front_right_lower", CubeListBuilder.create(), PartPose.offsetAndRotation(2.8251f, 74.3784f, 2.0958f, -0.3491f, 0.0f, 0.0f));
        PartDefinition cube_r10 = front_right_lower.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(358, 46).addBox(-7.0632f, -24.0067f, 1.8718f, 13.5f, 46.5f, 13.5f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.8533f, 46.7478f, -10.7826f, -0.3052f, 0.0131f, -0.0893f));
        PartDefinition cube_r11 = front_right_lower.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(169, 274).addBox(29.6958f, -8.2285f, 5.1382f, 4.5f, 20.25f, 13.5f, new CubeDeformation(0.0f)).texOffs(43, 227).addBox(7.1958f, -8.2285f, 0.6382f, 22.5f, 36.0f, 22.5f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-20.001f, 5.4241f, -13.0706f, 0.1327f, -0.0531f, -0.0693f));
        PartDefinition neck1_a1 = body.addOrReplaceChild("neck1_a1", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.287f, -37.0f, -158.7006f, -0.3491f, 0.0f, 0.0f));
        PartDefinition neck1 = neck1_a1.addOrReplaceChild("neck1", CubeListBuilder.create(), PartPose.offset(0.0f, 27.0f, -22.0f));
        PartDefinition cube_r12 = neck1.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(247, 405).addBox(-24.75f, -15.75f, -24.75f, 49.5f, 40.5f, 49.5f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.4781f, 10.095f, -9.7315f, 0.1745f, 0.0f, 0.0f));
        PartDefinition neck2_a1 = neck1.addOrReplaceChild("neck2_a1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0f, 12.5956f, -32.2794f, 0.2182f, 0.0f, 0.0f));
        PartDefinition neck2 = neck2_a1.addOrReplaceChild("neck2", CubeListBuilder.create(), PartPose.offset(0.0f, -6.0f, 0.0f));
        PartDefinition neck3_a1 = neck2.addOrReplaceChild("neck3_a1", CubeListBuilder.create(), PartPose.offset(-0.45f, 4.9508f, -57.1392f));
        PartDefinition neck3 = neck3_a1.addOrReplaceChild("neck3", CubeListBuilder.create(), PartPose.offset(0.0f, -1.0f, 5.0f));
        PartDefinition cube_r13 = neck3.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(333, 136).addBox(-19.125f, -13.5f, -22.5f, 38.25f, 33.75f, 29.25f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.4781f, 3.4191f, -5.5609f, -0.0873f, 0.0f, 0.0f));
        PartDefinition head = neck3.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.141f, 3.165f, -20.5947f, 0.5672f, 0.0f, 0.0f));
        PartDefinition cube_r14 = head.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(301, 321).addBox(-2.2897f, 8.3353f, -10.462f, 4.5f, 9.0f, 22.5f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(23.86f, -1.6233f, 5.1586f, -0.2357f, 0.6529f, -0.0375f));
        PartDefinition cube_r15 = head.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(308, 175).addBox(-2.025f, 8.4949f, -10.7822f, 4.5f, 9.0f, 22.5f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-24.6408f, -1.7037f, 4.6633f, -0.1856f, -0.7426f, -0.0405f));
        PartDefinition cube_r16 = head.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(231, 199).addBox(-2.025f, -4.5474f, -11.6013f, 4.5f, 9.0f, 24.75f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-24.6408f, -1.7037f, 4.6633f, -0.0547f, -0.7426f, -0.0405f));
        PartDefinition cube_r17 = head.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(411, 199).addBox(-2.2897f, -4.6639f, -11.263f, 4.5f, 9.0f, 24.75f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(23.86f, -1.6233f, 5.1586f, -0.1048f, 0.6529f, -0.0375f));
        PartDefinition upperjaw_a1 = head.addOrReplaceChild("upperjaw_a1", CubeListBuilder.create(), PartPose.offset(0.141f, -4.1051f, -9.4641f));
        PartDefinition upperjaw = upperjaw_a1.addOrReplaceChild("upperjaw", CubeListBuilder.create(), PartPose.offset(0.0f, 4.0f, -4.0f));
        PartDefinition cube_r18 = upperjaw.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(321, 413).addBox(-14.5f, 7.25f, -13.75f, 29.0f, 8.0f, 29.0f, new CubeDeformation(-1.0f)).texOffs(200, 20).addBox(-14.5f, -9.25f, -13.75f, 29.0f, 18.5f, 27.5f, new CubeDeformation(-1.0f)), PartPose.offsetAndRotation(-0.6281f, -4.0892f, -26.3028f, -0.0873f, 0.0f, 0.0f));
        PartDefinition cube_r19 = upperjaw.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(43, 170).addBox(-18.0f, -6.6656f, -44.7888f, 36.0f, 36.0f, 21.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.4781f, -7.8908f, 30.7479f, -0.0873f, 0.0f, 0.0f));
        PartDefinition cube_r20 = upperjaw.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(87, 176).addBox(-16.9171f, -19.9948f, -3.3496f, 11.3f, 21.8f, 11.3f, new CubeDeformation(-0.7f)), PartPose.offsetAndRotation(-33.7982f, -19.5976f, 23.9259f, -0.4046f, 0.2161f, 0.2581f));
        PartDefinition jaw_a1 = head.addOrReplaceChild("jaw_a1", CubeListBuilder.create(), PartPose.offset(0.141f, -19.8365f, -28.6336f));
        PartDefinition jaw = jaw_a1.addOrReplaceChild("jaw", CubeListBuilder.create(), PartPose.offset(0.0f, -0.75f, 1.5f));
        PartDefinition cube_r21 = jaw.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(365, 220).addBox(-14.0f, -2.15f, -14.0f, 28.0f, 7.0f, 28.0f, new CubeDeformation(-2.0f)), PartPose.offsetAndRotation(-0.6281f, 24.0719f, -12.154f, -0.0873f, 0.0f, 0.0f));
        PartDefinition cube_r22 = jaw.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(297, 60).addBox(-13.5135f, -6.5288f, -77.7345f, 27.027f, 11.43f, 23.562f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.4781f, 39.0427f, 51.6645f, -0.0873f, 0.0f, 0.0f));
        PartDefinition neck2_a4 = body.addOrReplaceChild("neck2_a4", CubeListBuilder.create(), PartPose.offsetAndRotation(-31.787f, 4.0f, -160.2006f, -0.3205f, 0.3271f, -0.2829f));
        PartDefinition cube_r23 = neck2_a4.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(186, 147).addBox(-24.75f, -15.75f, -24.75f, 49.5f, 40.5f, 49.5f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.4781f, -0.905f, -28.7315f, 0.1745f, 0.0f, 0.0f));
        PartDefinition neck2_a2 = neck2_a4.addOrReplaceChild("neck2_a2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0f, 7.5956f, -47.2794f, 0.2176f, -0.1504f, -0.1957f));
        PartDefinition neck2_2 = neck2_a2.addOrReplaceChild("neck2_2", CubeListBuilder.create().texOffs(98, 249).addBox(-20.7281f, -16.905f, -57.2f, 40.5f, 36.0f, 58.5f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, -1.0f, 0.0f, 0.2182f, 0.0f, 0.0f));
        PartDefinition neck3_a2 = neck2_2.addOrReplaceChild("neck3_a2", CubeListBuilder.create(), PartPose.offset(-0.45f, -5.0492f, -56.1392f));
        PartDefinition head2 = neck3_a2.addOrReplaceChild("head2", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.141f, -3.835f, -25.5947f, 0.4692f, 0.1166f, -0.1848f));
        PartDefinition cube_r24 = head2.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(198, 0).addBox(-2.2897f, 8.3353f, -10.462f, 4.5f, 9.0f, 22.5f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(23.86f, 3.3767f, 10.1586f, -0.2357f, 0.6529f, -0.0375f));
        PartDefinition cube_r25 = head2.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(423, 95).addBox(-2.025f, 8.4949f, -10.7822f, 4.5f, 9.0f, 22.5f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-24.6408f, 3.2963f, 9.6633f, -0.1856f, -0.7426f, -0.0405f));
        PartDefinition cube_r26 = head2.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(325, 0).addBox(-2.025f, -4.5474f, -11.6013f, 4.5f, 9.0f, 24.75f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-24.6408f, 3.2963f, 9.6633f, -0.0547f, -0.7426f, -0.0405f));
        PartDefinition cube_r27 = head2.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(356, 161).addBox(-14.0f, -2.15f, -14.0f, 28.0f, 7.0f, 28.0f, new CubeDeformation(-2.0f)), PartPose.offsetAndRotation(-0.4871f, 8.4854f, -34.2877f, -0.0873f, 0.0f, 0.0f));
        PartDefinition cube_r28 = head2.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(69, 173).addBox(-13.5135f, -6.5288f, -77.7345f, 27.027f, 11.43f, 23.562f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.3371f, 23.4562f, 29.5309f, -0.0873f, 0.0f, 0.0f));
        PartDefinition cube_r29 = head2.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(454, 182).addBox(-2.5388f, 2.1895f, -3.5908f, 13.5f, 22.5f, 13.5f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-33.6572f, -14.7027f, 15.4618f, -0.975f, -0.2926f, -0.1931f));
        PartDefinition cube_r30 = head2.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(397, 396).addBox(-12.4418f, -11.6923f, -4.2449f, 12.0f, 19.5f, 12.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-33.6572f, -14.7027f, 15.4618f, 0.1232f, -0.7704f, -1.7132f));
        PartDefinition cube_r31 = head2.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(149, 189).addBox(-14.1157f, -12.1025f, -4.1449f, 11.8f, 19.3f, 11.8f, new CubeDeformation(-0.2f)), PartPose.offsetAndRotation(-33.6572f, -14.7027f, 15.4618f, -0.7302f, -0.2979f, -0.2833f));
        PartDefinition cube_r32 = head2.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(43, 176).addBox(-16.9171f, -19.9948f, -3.3496f, 11.3f, 21.8f, 11.3f, new CubeDeformation(-0.7f)), PartPose.offsetAndRotation(-33.6572f, -14.7027f, 15.4618f, -0.4046f, 0.2161f, 0.2581f));
        PartDefinition cube_r33 = head2.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(43, 113).addBox(-18.0f, -6.6656f, -44.7888f, 36.0f, 36.0f, 21.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.3371f, -2.9959f, 22.2838f, -0.0873f, 0.0f, 0.0f));
        PartDefinition cube_r34 = head2.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(166, 0).addBox(-14.5f, -9.25f, -13.75f, 29.0f, 18.5f, 27.5f, new CubeDeformation(-1.0f)).texOffs(267, 66).addBox(-14.5f, 7.25f, -13.75f, 29.0f, 8.0f, 29.0f, new CubeDeformation(-1.0f)), PartPose.offsetAndRotation(-0.4871f, 0.8057f, -34.7669f, -0.0873f, 0.0f, 0.0f));
        PartDefinition cube_r35 = head2.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(314, 29).addBox(-2.2897f, -4.6639f, -11.263f, 4.5f, 9.0f, 24.75f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(23.86f, 3.3767f, 10.1586f, -0.1048f, 0.6529f, -0.0375f));
        PartDefinition neck3_a4 = body.addOrReplaceChild("neck3_a4", CubeListBuilder.create(), PartPose.offsetAndRotation(32.713f, 4.0f, -157.2006f, -0.2284f, -0.298f, 0.0681f));
        PartDefinition neck3_1 = neck3_a4.addOrReplaceChild("neck3_1", CubeListBuilder.create(), PartPose.offset(0.0f, 2.0f, 0.0f));
        PartDefinition neck2_a3 = neck3_1.addOrReplaceChild("neck2_a3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0f, 7.5956f, -50.2794f, 0.3927f, 0.0f, 0.0f));
        PartDefinition neck3_2 = neck2_a3.addOrReplaceChild("neck3_2", CubeListBuilder.create().texOffs(77, 0).addBox(-20.7281f, -17.905f, -57.2f, 40.5f, 36.0f, 58.5f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.3491f));
        PartDefinition neck3_a3 = neck3_2.addOrReplaceChild("neck3_a3", CubeListBuilder.create(), PartPose.offset(-0.45f, -1.0492f, -65.1392f));
        PartDefinition neck3_3 = neck3_a3.addOrReplaceChild("neck3_3", CubeListBuilder.create(), PartPose.offset(0.0f, -1.0f, 3.0f));
        PartDefinition cube_r36 = neck3_3.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(199, 136).addBox(-19.125f, -13.5f, -22.5f, 38.25f, 33.75f, 29.25f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.4781f, -2.5809f, 0.4391f, -0.0873f, 0.0f, 0.0f));
        PartDefinition head3 = neck3_3.addOrReplaceChild("head3", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.141f, -2.835f, -18.5947f, 0.3927f, 0.5672f, 0.0f));
        PartDefinition cube_r37 = head3.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(212, 189).addBox(-2.025f, 8.4949f, -10.7822f, 4.5f, 9.0f, 22.5f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-24.6408f, -1.7037f, 8.6633f, -0.1856f, -0.7426f, -0.0405f));
        PartDefinition cube_r38 = head3.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(291, 199).addBox(-2.25f, -4.5f, -12.375f, 4.5f, 9.0f, 24.75f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-6.4343f, -20.4554f, 8.9928f, -0.2618f, -0.6545f, 1.5708f));
        PartDefinition cube_r39 = head3.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(351, 199).addBox(-2.25f, -4.5f, -12.375f, 4.5f, 9.0f, 24.75f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(8.5657f, -20.4554f, 8.9928f, 0.3054f, -0.6545f, 1.5708f));
        PartDefinition cube_r40 = head3.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(271, 150).addBox(-2.025f, -4.5474f, -11.6013f, 4.5f, 9.0f, 24.75f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-24.6408f, -1.7037f, 8.6633f, -0.0547f, -0.7426f, -0.0405f));
        PartDefinition cube_r41 = head3.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(290, 53).addBox(-2.2897f, -4.6639f, -11.263f, 4.5f, 9.0f, 24.75f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(23.86f, -1.6233f, 9.1586f, -0.1048f, 0.6529f, -0.0375f));
        PartDefinition upperjaw_a3 = head3.addOrReplaceChild("upperjaw_a3", CubeListBuilder.create(), PartPose.offset(0.141f, 3.8949f, -12.4641f));
        PartDefinition upperjaw3 = upperjaw_a3.addOrReplaceChild("upperjaw3", CubeListBuilder.create(), PartPose.offset(0.0f, -5.0f, 10.0f));
        PartDefinition cube_r42 = upperjaw3.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(257, 254).addBox(-14.5f, 7.25f, -13.75f, 29.0f, 8.0f, 29.0f, new CubeDeformation(-1.0f)).texOffs(212, 164).addBox(-14.5f, -9.25f, -13.75f, 29.0f, 18.5f, 27.5f, new CubeDeformation(-1.0f)), PartPose.offsetAndRotation(-0.6281f, -3.0892f, -33.3028f, -0.0873f, 0.0f, 0.0f));
        PartDefinition cube_r43 = upperjaw3.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(43, 56).addBox(-18.0f, -6.6656f, -44.7888f, 36.0f, 36.0f, 21.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.4781f, -6.8908f, 23.7479f, -0.0873f, 0.0f, 0.0f));
        PartDefinition cube_r44 = upperjaw3.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(412, 175).addBox(-16.9171f, -19.9948f, -3.3496f, 11.3f, 21.8f, 11.3f, new CubeDeformation(-0.7f)), PartPose.offsetAndRotation(-33.7982f, -18.5976f, 16.9259f, -0.4046f, 0.2161f, 0.2581f));
        PartDefinition cube_r45 = upperjaw3.addOrReplaceChild("cube_r45", CubeListBuilder.create().texOffs(243, 208).addBox(-14.1157f, -12.1025f, -4.1449f, 11.8f, 19.3f, 11.8f, new CubeDeformation(-0.2f)), PartPose.offsetAndRotation(-33.7982f, -18.5976f, 16.9259f, -0.7302f, -0.2979f, -0.2833f));
        PartDefinition cube_r46 = upperjaw3.addOrReplaceChild("cube_r46", CubeListBuilder.create().texOffs(364, 175).addBox(-12.4418f, -11.6923f, -4.2449f, 12.0f, 19.5f, 12.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-33.7982f, -18.5976f, 16.9259f, 0.1232f, -0.7704f, -1.7132f));
        PartDefinition cube_r47 = upperjaw3.addOrReplaceChild("cube_r47", CubeListBuilder.create().texOffs(316, 291).addBox(-2.5388f, 2.1895f, -3.5908f, 13.5f, 22.5f, 13.5f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-33.7982f, -18.5976f, 16.9259f, -0.975f, -0.2926f, -0.1931f));
        PartDefinition jaw_a3 = head3.addOrReplaceChild("jaw_a3", CubeListBuilder.create(), PartPose.offset(0.141f, 5.1635f, -24.6336f));
        PartDefinition jaw3 = jaw_a3.addOrReplaceChild("jaw3", CubeListBuilder.create(), PartPose.offset(0.0f, 2.25f, 0.5f));
        PartDefinition cube_r48 = jaw3.addOrReplaceChild("cube_r48", CubeListBuilder.create().texOffs(304, 307).addBox(-14.0f, -2.15f, -14.0f, 28.0f, 7.0f, 28.0f, new CubeDeformation(-2.0f)), PartPose.offsetAndRotation(-0.6281f, -3.9281f, -11.154f, -0.0873f, 0.0f, 0.0f));
        PartDefinition cube_r49 = jaw3.addOrReplaceChild("cube_r49", CubeListBuilder.create().texOffs(221, 208).addBox(-13.5135f, -6.5288f, -77.7345f, 27.027f, 11.43f, 23.562f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.4781f, 11.0427f, 52.6645f, -0.0873f, 0.0f, 0.0f));
        PartDefinition left_leg = bone.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(24.7492f, -10.75f, 87.21f));
        PartDefinition cube_r50 = left_leg.addOrReplaceChild("cube_r50", CubeListBuilder.create().texOffs(163, 102).addBox(-16.0281f, -9.2586f, -14.545f, 28.35f, 24.3f, 32.4f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(14.4028f, 39.2679f, -20.3613f, -0.957f, -0.0531f, -0.0693f));
        PartDefinition cube_r51 = left_leg.addOrReplaceChild("cube_r51", CubeListBuilder.create().texOffs(303, 197).addBox(-25.271f, -60.7109f, -29.7687f, 48.6f, 56.7f, 52.65f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(14.4028f, 43.3179f, -22.3863f, -0.6545f, 0.0f, 0.0f));
        PartDefinition left_lower = left_leg.addOrReplaceChild("left_lower", CubeListBuilder.create(), PartPose.offset(16.7235f, 62.95f, -41.7793f));
        PartDefinition cube_r52 = left_lower.addOrReplaceChild("cube_r52", CubeListBuilder.create().texOffs(43, 164).addBox(-4.05f, -11.1375f, -6.075f, 12.15f, 28.35f, 12.15f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-1.8969f, 43.03f, 23.3046f, -0.3052f, 0.0131f, -0.0893f));
        PartDefinition cube_r53 = left_lower.addOrReplaceChild("cube_r53", CubeListBuilder.create().texOffs(357, 321).addBox(-13.7326f, -2.576f, -11.0015f, 16.2f, 16.2f, 10.125f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(4.7882f, 31.3334f, 13.1033f, 1.1778f, 0.0076f, -0.0869f));
        PartDefinition cube_r54 = left_lower.addOrReplaceChild("cube_r54", CubeListBuilder.create().texOffs(191, 149).addBox(-15.7576f, -16.1497f, -6.4668f, 20.25f, 32.4f, 20.25f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(4.7882f, 31.3334f, 13.1033f, 0.8723f, 0.0076f, -0.0869f));
        PartDefinition cube_r55 = left_lower.addOrReplaceChild("cube_r55", CubeListBuilder.create().texOffs(257, 189).addBox(-16.605f, -16.605f, -24.3f, 33.21f, 33.21f, 32.4f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-1.3498f, 4.088f, 4.4501f, 0.4382f, -0.0531f, -0.0693f));
        PartDefinition right_leg = bone.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-18.6732f, -14.75f, 101.2454f));
        PartDefinition cube_r56 = right_leg.addOrReplaceChild("cube_r56", CubeListBuilder.create().texOffs(307, 169).addBox(-14.175f, -16.2f, 0.0f, 28.35f, 24.3f, 10.125f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-20.0714f, 54.1854f, -37.0966f, -0.957f, 0.0139f, 0.0493f));
        PartDefinition cube_r57 = right_leg.addOrReplaceChild("cube_r57", CubeListBuilder.create().texOffs(43, 164).addBox(-25.271f, -60.7109f, -29.7687f, 48.6f, 56.7f, 52.65f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-19.1248f, 47.3179f, -36.4216f, -0.6545f, 0.0f, 0.0f));
        PartDefinition cube_r58 = right_leg.addOrReplaceChild("cube_r58", CubeListBuilder.create().texOffs(205, 68).addBox(-16.2f, -24.3f, -16.2f, 32.4f, 48.6f, 32.4f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-20.4702f, 58.3128f, -51.0979f, -0.6952f, 0.0139f, 0.0493f));
        PartDefinition right_lower = right_leg.addOrReplaceChild("right_lower", CubeListBuilder.create(), PartPose.offset(-24.8857f, 65.25f, -57.8136f));
        PartDefinition cube_r59 = right_lower.addOrReplaceChild("cube_r59", CubeListBuilder.create().texOffs(43, 111).addBox(-16.8218f, -45.0431f, -14.2447f, 33.21f, 33.21f, 32.4f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(3.4455f, 35.3736f, 9.4169f, 0.4394f, 0.0167f, 0.0179f));
        PartDefinition cube_r60 = right_lower.addOrReplaceChild("cube_r60", CubeListBuilder.create().texOffs(43, 286).addBox(-11.5817f, -13.2751f, -0.9993f, 20.25f, 32.4f, 20.25f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(6.6855f, 35.3736f, 9.4169f, 0.8724f, 0.0774f, 3.0E-4f));
        PartDefinition cube_r61 = right_lower.addOrReplaceChild("cube_r61", CubeListBuilder.create().texOffs(411, 335).addBox(-9.5567f, 1.8097f, -6.6514f, 16.2f, 16.2f, 10.125f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(6.6855f, 35.3736f, 9.4169f, 1.1778f, 0.0774f, 3.0E-4f));
        PartDefinition cube_r62 = right_lower.addOrReplaceChild("cube_r62", CubeListBuilder.create().texOffs(183, 177).addBox(-7.7348f, -7.5745f, 11.6544f, 12.15f, 28.35f, 12.15f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(6.6855f, 35.3736f, 9.4169f, -0.3053f, 0.0829f, -0.002f));
        PartDefinition right_feet = right_lower.addOrReplaceChild("right_feet", CubeListBuilder.create(), PartPose.offsetAndRotation(3.9661f, 57.1518f, 20.752f, 0.0f, 0.0f, -0.1309f));
        PartDefinition cube_r63 = right_feet.addOrReplaceChild("cube_r63", CubeListBuilder.create().texOffs(388, 307).addBox(-8.5187f, 21.7564f, 11.0637f, 16.2f, 8.1f, 20.25f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(8.9235f, -21.5526f, -23.4851f, -0.0361f, 0.0864f, 0.0855f));
        return LayerDefinition.create(meshdefinition, 512, 512);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }
}

