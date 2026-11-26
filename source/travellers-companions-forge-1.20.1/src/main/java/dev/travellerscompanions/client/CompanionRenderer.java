package dev.travellerscompanions.client;

import dev.travellerscompanions.*;
import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.impl.animation.AnimationApplier;
import dev.kosmx.playerAnim.impl.IMutableModel;
import net.bettercombat.client.animation.AnimationRegistry;
import net.bettercombat.logic.WeaponRegistry;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.*;

public final class CompanionRenderer extends MobRenderer<CompanionEntity,CompanionRenderer.Model> {
    public CompanionRenderer(EntityRendererProvider.Context ctx){super(ctx,new Model(ctx.bakeLayer(ModelLayers.PLAYER)),.5f);addLayer(new HumanoidArmorLayer<>(this,new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)),ctx.getModelManager()));addLayer(new CompanionHeldItems(this,ctx.getItemInHandRenderer()));addLayer(new CustomHeadLayer<>(this,ctx.getModelSet(),ctx.getItemInHandRenderer()));}
    @Override public ResourceLocation getTextureLocation(CompanionEntity e){return TravellersCompanions.id("textures/entity/"+e.skinKey()+".png");}
    @Override protected void setupRotations(CompanionEntity e,com.mojang.blaze3d.vertex.PoseStack pose,float age,float yaw,float partial){
        super.setupRotations(e,pose,age,e.parkour.busy()?e.parkourYaw():yaw,partial);
        CompanionLocomotion.rotate(e,pose,partial);
    }
    public static dev.kosmx.playerAnim.core.data.KeyframeAnimation animation(String name){if(name==null||name.isEmpty())return null;var result=AnimationRegistry.animations.get(name);var id=ResourceLocation.tryParse(name);return result!=null?result:id==null?null:dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry.getAnimation(id);}
    public static final class Model extends PlayerModel<CompanionEntity> {
        public AnimationApplier activeAnimation;
        public Model(ModelPart root){super(root,false);}
        @Override public void prepareMobModel(CompanionEntity e,float walk,float speed,float partial){
            rightArmPose=ArmPose.EMPTY;leftArmPose=ArmPose.EMPTY;
            if(!e.getMainHandItem().isEmpty())rightArmPose=ArmPose.ITEM;
            if(!e.getOffhandItem().isEmpty())leftArmPose=ArmPose.ITEM;
            if(e.isUsingItem()){
                if(e.getUsedItemHand()==InteractionHand.OFF_HAND&&e.getOffhandItem().getItem() instanceof ShieldItem)leftArmPose=ArmPose.BLOCK;
                else if(e.getMainHandItem().getItem() instanceof BowItem)rightArmPose=ArmPose.BOW_AND_ARROW;
                else if(e.getMainHandItem().getItem() instanceof CrossbowItem)rightArmPose=ArmPose.CROSSBOW_CHARGE;
            }super.prepareMobModel(e,walk,speed,partial);
        }
        @Override public void setupAnim(CompanionEntity e,float walk,float speed,float age,float yaw,float pitch){
            // Reset positions because PlayerModel shares one instance between all companions.
            head.setPos(0,0,0);body.setPos(0,0,0);rightArm.setPos(-5,2,0);leftArm.setPos(5,2,0);rightLeg.setPos(-1.9f,12,0);leftLeg.setPos(1.9f,12,0);
            super.setupAnim(e,walk,speed,age,yaw,pitch);
            activeAnimation=null;
            ((IMutableModel)(Object)this).getEmoteSupplier().set(null);
            float elapsed=age-e.animationStarted;String name=e.animation;boolean attacking=elapsed>=0&&elapsed<e.animationDuration;
            boolean parkour=CompanionLocomotion.posing(e);
            boolean walking=!parkour&&!attacking&&speed>.025f&&e.onGround()&&!e.isInWaterOrBubble()&&!e.isPassenger()&&TravellersCompanions.SPA_WALK.get();
            if(walking&&CompanionRenderer.animation("seriousplayeranimations:walking")!=null)name="seriousplayeranimations:walking";
            else {walking=false;if(!attacking){var weapon=WeaponRegistry.getAttributes(e.getMainHandItem());name=weapon==null?null:weapon.pose();}}
            var data=CompanionRenderer.animation(name);
            boolean shielding=e.isUsingItem()&&e.getUsedItemHand()==InteractionHand.OFF_HAND&&e.getOffhandItem().getItem() instanceof ShieldItem;
            if(data!=null&&!parkour&&!e.guarding()&&!shielding&&(!e.isUsingItem()||attacking)){
                float time=walking?data.returnToTick+(walk*12.7324f)%Math.max(1,data.endTick-data.returnToTick):attacking?elapsed/Math.max(1,e.animationDuration)*data.endTick:Math.min(10,data.endTick);
                var animation=new KeyframeAnimationPlayer(data,(int)time);float fraction=time-(int)time;
                activeAnimation=new AnimationApplier(animation);activeAnimation.setTickDelta(fraction);
                // Use PlayerAnimator's absolute pivot convention and its bend handling.
                ((IMutableModel)(Object)this).getEmoteSupplier().set(activeAnimation);
                activeAnimation.updatePart("head",head);activeAnimation.updatePart("torso",body);activeAnimation.updatePart("rightArm",rightArm);activeAnimation.updatePart("leftArm",leftArm);
                if(attacking||walking){activeAnimation.updatePart("rightLeg",rightLeg);activeAnimation.updatePart("leftLeg",leftLeg);}
            }
            if(parkour)CompanionLocomotion.pose(this,e,walk,speed,age-e.tickCount);
            if(e.guarding()){rightArm.xRot=-1.35f;rightArm.yRot=-.55f;rightArm.zRot=-.5f;leftArm.xRot=-1.2f;leftArm.yRot=.65f;leftArm.zRot=.3f;}
            hat.copyFrom(head);jacket.copyFrom(body);rightSleeve.copyFrom(rightArm);leftSleeve.copyFrom(leftArm);rightPants.copyFrom(rightLeg);leftPants.copyFrom(leftLeg);
        }
    }
}
