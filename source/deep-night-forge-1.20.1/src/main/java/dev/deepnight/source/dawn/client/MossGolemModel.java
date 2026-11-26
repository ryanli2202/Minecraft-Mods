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

import dev.deepnight.source.dawn.entity.MossGolemEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class MossGolemModel
extends GeoModel<MossGolemEntity> {
    public ResourceLocation getModelResource(MossGolemEntity object) {
        return new ResourceLocation("simple_mobs", "geo/brute.geo.json");
    }

    public ResourceLocation getTextureResource(MossGolemEntity object) {
        return new ResourceLocation("simple_mobs", "textures/brute.png");
    }

    public ResourceLocation getAnimationResource(MossGolemEntity animatable) {
        return new ResourceLocation("simple_mobs", "animations/brute.animation.json");
    }

    public void setCustomAnimations(MossGolemEntity entity, long uniqueID, AnimationState customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        var bone = this.getAnimationProcessor().getBone("head");
        MossGolemEntity entityIn = entity;
        EntityModelData extraData = (EntityModelData)customPredicate.getData(software.bernie.geckolib.constant.DataTickets.ENTITY_MODEL_DATA);
        bone.setRotX(extraData.headPitch() * ((float)Math.PI / 180));
        bone.setRotY(extraData.netHeadYaw() * ((float)Math.PI / 180));
    }
}

