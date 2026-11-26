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

import dev.deepnight.source.dawn.entity.EntEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class EntModel
extends GeoModel<EntEntity> {
    public ResourceLocation getModelResource(EntEntity object) {
        return new ResourceLocation("simple_mobs", "geo/ent.geo.json");
    }

    public ResourceLocation getTextureResource(EntEntity object) {
        if (object.getDisplayName().getString().equals("Staff")) {
            return new ResourceLocation("simple_mobs", "textures/entities/ent_staff.png");
        }
        return new ResourceLocation("simple_mobs", "textures/ent.png");
    }

    public ResourceLocation getAnimationResource(EntEntity animatable) {
        return new ResourceLocation("simple_mobs", "animations/ent.animation.json");
    }

    public void setCustomAnimations(EntEntity entity, long uniqueID, AnimationState customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        var head = this.getAnimationProcessor().getBone("head_rot");
        EntEntity entityIn = entity;
        EntityModelData extraData = (EntityModelData)customPredicate.getData(software.bernie.geckolib.constant.DataTickets.ENTITY_MODEL_DATA);
        head.setRotY(extraData.netHeadYaw() * ((float)Math.PI / 180) * 0.5f);
        head.setRotX(extraData.headPitch() * ((float)Math.PI / 180) * 0.5f);
    }
}

