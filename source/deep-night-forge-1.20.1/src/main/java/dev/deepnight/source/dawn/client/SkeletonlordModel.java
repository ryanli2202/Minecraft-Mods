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

import dev.deepnight.source.dawn.entity.SkeletonlordEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class SkeletonlordModel
extends GeoModel<SkeletonlordEntity> {
    public ResourceLocation getModelResource(SkeletonlordEntity object) {
        return new ResourceLocation("simple_mobs", "geo/skeleton_lord.geo.json");
    }

    public ResourceLocation getTextureResource(SkeletonlordEntity object) {
        return new ResourceLocation("simple_mobs", "textures/skeleton_lord.png");
    }

    public ResourceLocation getAnimationResource(SkeletonlordEntity animatable) {
        return new ResourceLocation("simple_mobs", "animations/skeleton_lord.animation.json");
    }

    public void setCustomAnimations(SkeletonlordEntity entity, long uniqueID, AnimationState customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        var head = this.getAnimationProcessor().getBone("caped_head");
        SkeletonlordEntity entityIn = entity;
        EntityModelData extraData = (EntityModelData)customPredicate.getData(software.bernie.geckolib.constant.DataTickets.ENTITY_MODEL_DATA);
        head.setRotY(extraData.netHeadYaw() * ((float)Math.PI / 180));
        head.setRotX(extraData.headPitch() * ((float)Math.PI / 180));
    }
}

