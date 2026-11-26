package dev.travellerscompanions.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.travellerscompanions.*;
import net.minecraft.util.Mth;

/** NPC poses for the movement controller. ParCool's player-only animator is not invoked on mobs. */
public final class CompanionLocomotion {
    private CompanionLocomotion() {}
    public static boolean posing(CompanionEntity e) {
        return e.parkour.busy() || e.parkourAction() == CompanionParkour.FAST_RUN && e.onGround()
            && !e.isUsingItem() && !e.guarding() && !e.isCasting() && e.getTarget() == null;
    }
    public static void rotate(CompanionEntity e, PoseStack pose, float partial) {
        if (!posing(e)) return;
        float elapsed = e.parkourElapsed(partial);
        if (e.parkourAction() == CompanionParkour.ROLL) {
            float p = Mth.clamp(elapsed / Math.max(1, e.parkourDuration()), 0, 1);
            float tuck = Mth.sin(p * Mth.PI);
            pose.translate(0, .9 - .32 * tuck, 0);
            pose.mulPose(Axis.XP.rotationDegrees(-360 * (1 - (1 - p) * (1 - p))));
            pose.translate(0, -.9, 0);
        } else if (e.parkourAction() == CompanionParkour.FAST_RUN) {
            float lean = Math.min(1, elapsed / 8) * 22;
            pose.translate(0, .9, 0); pose.mulPose(Axis.XP.rotationDegrees(-lean)); pose.translate(0, -.9, 0);
        }
    }
    public static void pose(CompanionRenderer.Model m, CompanionEntity e, float walk, float speed, float partial) {
        float elapsed = e.parkourElapsed(partial);
        if (e.parkourAction() == CompanionParkour.FAST_RUN) {
            float swing = Mth.cos(walk * .6662f), amount = Math.min(1, speed * 1.4f);
            m.rightArm.xRot = -.35f - swing * 1.15f * amount;
            m.leftArm.xRot = -.35f + swing * 1.15f * amount;
            m.rightArm.zRot = .15f; m.leftArm.zRot = -.15f;
            m.rightLeg.xRot = swing * 1.25f * amount; m.leftLeg.xRot = -swing * 1.25f * amount;
            m.head.xRot -= .38f;
        } else if (e.parkourAction() == CompanionParkour.CLIMB) {
            float p = Mth.clamp(elapsed / Math.max(1, e.parkourDuration()), 0, 1);
            float reach = Mth.sin(p * Mth.PI);
            m.rightArm.xRot = m.leftArm.xRot = -2.45f * reach;
            m.rightArm.zRot = .18f * reach; m.leftArm.zRot = -.18f * reach;
            m.rightLeg.xRot = -1.15f * reach; m.leftLeg.xRot = .4f * reach;
            m.body.xRot = .2f * reach;
        } else if (e.parkourAction() == CompanionParkour.ROLL) {
            float p = Mth.clamp(elapsed / Math.max(1, e.parkourDuration()), 0, 1);
            float tuck = Mth.sin(p * Mth.PI);
            m.rightArm.xRot = m.leftArm.xRot = -1.6f * tuck;
            m.rightLeg.xRot = m.leftLeg.xRot = -1.15f * tuck;
            m.head.xRot = .55f * tuck;
        }
    }
}
