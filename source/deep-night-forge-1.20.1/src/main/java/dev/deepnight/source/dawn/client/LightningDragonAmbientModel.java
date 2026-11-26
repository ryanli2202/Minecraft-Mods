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

import dev.deepnight.source.dawn.entity.LightningDragonAmbientEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class LightningDragonAmbientModel
extends GeoModel<LightningDragonAmbientEntity> {
    public ResourceLocation getModelResource(LightningDragonAmbientEntity object) {
        return new ResourceLocation("simple_mobs", "geo/lightning_dragon.geo.json");
    }

    public ResourceLocation getTextureResource(LightningDragonAmbientEntity object) {
        return new ResourceLocation("simple_mobs", "textures/lightning_dragon.png");
    }

    public ResourceLocation getAnimationResource(LightningDragonAmbientEntity animatable) {
        return new ResourceLocation("simple_mobs", "animations/lightning_dragon.animation.json");
    }

    public void setCustomAnimations(LightningDragonAmbientEntity entity, long uniqueID, AnimationState customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        var head = this.getAnimationProcessor().getBone("head");
        LightningDragonAmbientEntity entityIn = entity;
        EntityModelData extraData = (EntityModelData)customPredicate.getData(software.bernie.geckolib.constant.DataTickets.ENTITY_MODEL_DATA);
        head.setRotY(extraData.netHeadYaw() * ((float)Math.PI / 180));
        head.setRotX(extraData.headPitch() * ((float)Math.PI / 180));
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        var bone = this.getAnimationProcessor().getBone("bone2");
        if (entity.getEntityData().get(LightningDragonAmbientEntity.atanyaw).floatValue() > 70.0f) {
            bone.setRotZ(70.0f);
        } else if (entity.getEntityData().get(LightningDragonAmbientEntity.atanyaw).floatValue() < -70.0f) {
            bone.setRotZ(-70.0f);
        } else {
            bone.setRotZ(entity.getEntityData().get(LightningDragonAmbientEntity.atanyaw).floatValue());
        }
        if (entity.getEntityData().get(LightningDragonAmbientEntity.pitch).floatValue() > 50.0f) {
            bone.setRotX(50.0f);
        } else if (entity.getEntityData().get(LightningDragonAmbientEntity.pitch).floatValue() < -50.0f) {
            bone.setRotX(-50.0f);
        } else {
            bone.setRotX(entity.getEntityData().get(LightningDragonAmbientEntity.pitch).floatValue());
        }
    }
}

