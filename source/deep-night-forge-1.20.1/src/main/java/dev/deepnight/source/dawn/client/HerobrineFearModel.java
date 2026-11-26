/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  software.bernie.geckolib.model.GeoModel
 */
package dev.deepnight.source.dawn.client;

import dev.deepnight.source.dawn.entity.HerobrineFearEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HerobrineFearModel
extends GeoModel<HerobrineFearEntity> {
    public ResourceLocation getModelResource(HerobrineFearEntity object) {
        return new ResourceLocation("simple_mobs", "geo/herobrine_fear.geo.json");
    }

    public ResourceLocation getTextureResource(HerobrineFearEntity object) {
        return new ResourceLocation("simple_mobs", "textures/entities/a.png");
    }

    public ResourceLocation getAnimationResource(HerobrineFearEntity animatable) {
        return new ResourceLocation("simple_mobs", "animations/herobrine_fear.animation.json");
    }
}

