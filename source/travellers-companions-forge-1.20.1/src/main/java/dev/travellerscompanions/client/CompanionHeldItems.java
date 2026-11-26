package dev.travellerscompanions.client;

import dev.travellerscompanions.CompanionEntity;
import dev.travellerscompanions.CommonKatanaCombat;
import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.core.util.Vec3f;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/** Applies the same item tracks and elbow pivot used by PlayerAnimator's held-item bridge. */
final class CompanionHeldItems extends ItemInHandLayer<CompanionEntity,CompanionRenderer.Model> {
    private final ItemInHandRenderer items;
    CompanionHeldItems(CompanionRenderer renderer,ItemInHandRenderer items){super(renderer,items);this.items=items;}
    @Override protected void renderArmWithItem(LivingEntity e,ItemStack stack,ItemDisplayContext context,HumanoidArm arm,PoseStack pose,MultiBufferSource buffer,int light){
        if(stack.getItem() instanceof net.mcreator.commonweapons.item.TestItem katana&&e instanceof CompanionEntity companion){
            String previous=katana.animationprocedure;
            float elapsed=companion.tickCount-companion.animationStarted;
            katana.animationprocedure=CommonKatanaCombat.SPECIAL.equals(companion.animation)&&elapsed>=0&&elapsed<companion.animationDuration?"attack":"empty";
            try{renderEquipped(e,stack,context,arm,pose,buffer,light);}finally{katana.animationprocedure=previous;}
        }else renderEquipped(e,stack,context,arm,pose,buffer,light);
    }
    private void renderEquipped(LivingEntity e,ItemStack stack,ItemDisplayContext context,HumanoidArm arm,PoseStack pose,MultiBufferSource buffer,int light){
        var animation=getParentModel().activeAnimation;
        if(animation==null){super.renderArmWithItem(e,stack,context,arm,pose,buffer,light);return;}
        if(stack.isEmpty())return;boolean left=arm==HumanoidArm.LEFT;
        pose.pushPose();getParentModel().translateToHand(arm,pose);
        var bend=animation.getBend(left?"leftArm":"rightArm");
        if(dev.kosmx.playerAnim.impl.Helper.isBendEnabled()){pose.translate(0,.25,0);pose.mulPose(new Quaternionf().rotateAxis(bend.getRight(),new Vector3f((float)Math.cos(-bend.getLeft()),0,(float)Math.sin(-bend.getLeft()))));pose.translate(0,-.25,0);}
        pose.mulPose(Axis.XP.rotationDegrees(-90));pose.mulPose(Axis.YP.rotationDegrees(180));pose.translate((left?-1:1)/16.0,.125,-.625);
        String key=left?"leftItem":"rightItem";var pos=animation.get3DTransform(key,TransformType.POSITION,Vec3f.ZERO).scale(1/16f);var rot=animation.get3DTransform(key,TransformType.ROTATION,Vec3f.ZERO);
        pose.translate(pos.getX(),pos.getY(),pos.getZ());pose.mulPose(Axis.ZP.rotation(rot.getZ()));pose.mulPose(Axis.YP.rotation(rot.getY()));pose.mulPose(Axis.XP.rotation(rot.getX()));
        items.renderItem(e,stack,context,left,pose,buffer,light);pose.popPose();
    }
}
