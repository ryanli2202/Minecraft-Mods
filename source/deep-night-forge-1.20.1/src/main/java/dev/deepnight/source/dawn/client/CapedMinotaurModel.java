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

import dev.deepnight.source.dawn.entity.CapedMinotaurEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class CapedMinotaurModel
extends GeoModel<CapedMinotaurEntity> {
    public ResourceLocation getModelResource(CapedMinotaurEntity object) {
        return new ResourceLocation("simple_mobs", "geo/capedminotaur.geo.json");
    }

    public ResourceLocation getTextureResource(CapedMinotaurEntity object) {
        return new ResourceLocation("simple_mobs", "textures/capedminotaur.png");
    }

    public ResourceLocation getAnimationResource(CapedMinotaurEntity animatable) {
        return new ResourceLocation("simple_mobs", "animations/capedminotaur.animation.json");
    }

    public void setCustomAnimations(CapedMinotaurEntity entity, long uniqueID, AnimationState customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        var head = this.getAnimationProcessor().getBone("head");
        CapedMinotaurEntity entityIn = entity;
        EntityModelData extraData = (EntityModelData)customPredicate.getData(software.bernie.geckolib.constant.DataTickets.ENTITY_MODEL_DATA);
        head.setRotY(extraData.netHeadYaw() * ((float)Math.PI / 180));
        head.setRotX(extraData.headPitch() * ((float)Math.PI / 180));
    }
}

