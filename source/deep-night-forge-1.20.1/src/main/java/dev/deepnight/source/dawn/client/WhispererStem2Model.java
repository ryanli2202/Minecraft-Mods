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

import dev.deepnight.source.dawn.entity.WhispererStem2Entity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class WhispererStem2Model
extends GeoModel<WhispererStem2Entity> {
    public ResourceLocation getModelResource(WhispererStem2Entity object) {
        return new ResourceLocation("simple_mobs", "geo/whisperer_stem2.geo.json");
    }

    public ResourceLocation getTextureResource(WhispererStem2Entity object) {
        return new ResourceLocation("simple_mobs", "textures/whisperer_stem2.png");
    }

    public ResourceLocation getAnimationResource(WhispererStem2Entity animatable) {
        return new ResourceLocation("simple_mobs", "animations/whisperer_stem2.animation.json");
    }

    public void setCustomAnimations(WhispererStem2Entity entity, long uniqueID, AnimationState customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        var head = this.getAnimationProcessor().getBone("head2");
        WhispererStem2Entity entityIn = entity;
        EntityModelData extraData = (EntityModelData)customPredicate.getData(software.bernie.geckolib.constant.DataTickets.ENTITY_MODEL_DATA);
        head.setRotY(extraData.netHeadYaw() * ((float)Math.PI / 180));
        head.setRotX(extraData.headPitch() * ((float)Math.PI / 180));
    }
}

