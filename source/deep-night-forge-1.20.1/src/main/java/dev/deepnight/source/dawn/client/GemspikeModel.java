/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  software.bernie.geckolib.model.GeoModel
 */
package dev.deepnight.source.dawn.client;

import dev.deepnight.source.dawn.entity.GemspikeEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GemspikeModel
extends GeoModel<GemspikeEntity> {
    public ResourceLocation getModelResource(GemspikeEntity object) {
        return new ResourceLocation("simple_mobs", "geo/gemspike.geo.json");
    }

    public ResourceLocation getTextureResource(GemspikeEntity object) {
        return new ResourceLocation("simple_mobs", "textures/gemspike.png");
    }

    public ResourceLocation getAnimationResource(GemspikeEntity animatable) {
        return new ResourceLocation("simple_mobs", "animations/gemspike.animation.json");
    }
}

