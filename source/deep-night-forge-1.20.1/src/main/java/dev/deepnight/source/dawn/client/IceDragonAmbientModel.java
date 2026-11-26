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

import dev.deepnight.source.dawn.entity.IceDragonAmbientEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class IceDragonAmbientModel
extends GeoModel<IceDragonAmbientEntity> {
    public ResourceLocation getModelResource(IceDragonAmbientEntity object) {
        return new ResourceLocation("simple_mobs", "geo/icedragon.geo.json");
    }

    public ResourceLocation getTextureResource(IceDragonAmbientEntity object) {
        return new ResourceLocation("simple_mobs", "textures/icedragon.png");
    }

    public ResourceLocation getAnimationResource(IceDragonAmbientEntity animatable) {
        return new ResourceLocation("simple_mobs", "animations/dragon_ambient.animation.json");
    }

    public void setCustomAnimations(IceDragonAmbientEntity entity, long uniqueID, AnimationState customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        var head = this.getAnimationProcessor().getBone("head");
        IceDragonAmbientEntity entityIn = entity;
        EntityModelData extraData = (EntityModelData)customPredicate.getData(software.bernie.geckolib.constant.DataTickets.ENTITY_MODEL_DATA);
        head.setRotY(extraData.netHeadYaw() * ((float)Math.PI / 180));
        head.setRotX(extraData.headPitch() * ((float)Math.PI / 180));
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        var bone = this.getAnimationProcessor().getBone("bone2");
        if (entity.getEntityData().get(IceDragonAmbientEntity.atanyaw).floatValue() > 90.0f) {
            bone.setRotZ(90.0f);
        } else if (entity.getEntityData().get(IceDragonAmbientEntity.atanyaw).floatValue() < -90.0f) {
            bone.setRotZ(-90.0f);
        } else {
            bone.setRotZ(entity.getEntityData().get(IceDragonAmbientEntity.atanyaw).floatValue());
        }
        if (entity.getEntityData().get(IceDragonAmbientEntity.pitch).floatValue() > 50.0f) {
            bone.setRotX(50.0f);
        } else if (entity.getEntityData().get(IceDragonAmbientEntity.pitch).floatValue() < -50.0f) {
            bone.setRotX(-50.0f);
        } else {
            bone.setRotX(entity.getEntityData().get(IceDragonAmbientEntity.pitch).floatValue());
        }
    }
}

