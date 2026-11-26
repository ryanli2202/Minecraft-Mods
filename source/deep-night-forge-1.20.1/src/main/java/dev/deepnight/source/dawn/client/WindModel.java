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

import dev.deepnight.source.dawn.entity.WindEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class WindModel
extends GeoModel<WindEntity> {
    public ResourceLocation getModelResource(WindEntity object) {
        return new ResourceLocation("simple_mobs", "geo/wind.geo.json");
    }

    public ResourceLocation getTextureResource(WindEntity object) {
        return new ResourceLocation("simple_mobs", "textures/a.png");
    }

    public ResourceLocation getAnimationResource(WindEntity animatable) {
        return new ResourceLocation("simple_mobs", "animations/wind.animation.json");
    }

    public void setCustomAnimations(WindEntity entity, long uniqueID, AnimationState customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        var head = this.getAnimationProcessor().getBone("bone11");
        WindEntity entityIn = entity;
        EntityModelData extraData = (EntityModelData)customPredicate.getData(software.bernie.geckolib.constant.DataTickets.ENTITY_MODEL_DATA);
        head.setRotX(entity.getEntityData().get(WindEntity.pitch).floatValue() * ((float)(-Math.PI) / 180));
    }
}

