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

import dev.deepnight.source.dawn.entity.FireGiantEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class FireGiantModel
extends GeoModel<FireGiantEntity> {
    public ResourceLocation getModelResource(FireGiantEntity object) {
        return new ResourceLocation("simple_mobs", "geo/fire_giant.geo.json");
    }

    public ResourceLocation getTextureResource(FireGiantEntity object) {
        return new ResourceLocation("simple_mobs", "textures/fire_giant.png");
    }

    public ResourceLocation getAnimationResource(FireGiantEntity animatable) {
        return new ResourceLocation("simple_mobs", "animations/fire_giant.animation.json");
    }

    public void setCustomAnimations(FireGiantEntity entity, long uniqueID, AnimationState customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        var head = this.getAnimationProcessor().getBone("head");
        FireGiantEntity entityIn = entity;
        EntityModelData extraData = (EntityModelData)customPredicate.getData(software.bernie.geckolib.constant.DataTickets.ENTITY_MODEL_DATA);
        head.setRotY(extraData.netHeadYaw() * ((float)Math.PI / 180));
        head.setRotX(extraData.headPitch() * ((float)Math.PI / 180));
    }
}

