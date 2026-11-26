/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  software.bernie.geckolib.animatable.GeoEntity
 *  software.bernie.geckolib.core.animation.AnimationState
 *  software.bernie.geckolib.cache.object.GeoBone
 *  software.bernie.geckolib.model.GeoModel
 *  software.bernie.geckolib.model.data.EntityModelData
 */
package dev.deepnight.source.dawn.client;

import dev.deepnight.source.dawn.entity.DragonLordEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class DragonLordModel
extends GeoModel<DragonLordEntity> {
    public ResourceLocation getModelResource(DragonLordEntity object) {
        return new ResourceLocation("simple_mobs", "geo/dragon_lord.geo.json");
    }

    public ResourceLocation getTextureResource(DragonLordEntity object) {
        return new ResourceLocation("simple_mobs:textures/entities/dragon_lord/dragon_lord.png");
    }

    public ResourceLocation getAnimationResource(DragonLordEntity animatable) {
        return new ResourceLocation("simple_mobs", "animations/dragon_lord.animation.json");
    }

    public void setCustomAnimations(DragonLordEntity entity, long uniqueID, AnimationState customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        var head = this.getAnimationProcessor().getBone("head");
        EntityModelData extraData = (EntityModelData)customPredicate.getData(software.bernie.geckolib.constant.DataTickets.ENTITY_MODEL_DATA);
        head.setRotY(extraData.netHeadYaw() * 0.4f * ((float)Math.PI / 180));
        head.setRotX(extraData.headPitch() * ((float)Math.PI / 180));
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        var head2 = this.getAnimationProcessor().getBone("head2");
        DragonLordEntity entityIn = entity;
        head2.setRotY(extraData.netHeadYaw() * 0.4f * ((float)Math.PI / 180));
        head2.setRotX(extraData.headPitch() * ((float)Math.PI / 180));
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        var head3 = this.getAnimationProcessor().getBone("head3");
        head3.setRotY(extraData.netHeadYaw() * 0.4f * ((float)Math.PI / 180));
        head3.setRotX(extraData.headPitch() * ((float)Math.PI / 180));
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        var neck_1 = this.getAnimationProcessor().getBone("neck2_a1");
        neck_1.setRotZ(extraData.netHeadYaw() * 0.4f * ((float)(-Math.PI) / 180));
        neck_1.setRotX(extraData.headPitch() * ((float)Math.PI / 180));
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        var neck_2 = this.getAnimationProcessor().getBone("neck2_a2");
        neck_2.setRotZ(extraData.netHeadYaw() * 0.4f * ((float)(-Math.PI) / 180));
        neck_2.setRotX(extraData.headPitch() * ((float)Math.PI / 180));
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        var neck_3 = this.getAnimationProcessor().getBone("neck2_a3");
        neck_3.setRotZ(extraData.netHeadYaw() * 0.4f * ((float)(-Math.PI) / 180));
        neck_3.setRotX(extraData.headPitch() * ((float)Math.PI / 180));
    }
}

