/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  software.bernie.geckolib.model.GeoModel
 */
package dev.deepnight.source.dawn.client;

import dev.deepnight.source.dawn.entity.AirEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class AirModel
extends GeoModel<AirEntity> {
    public ResourceLocation getModelResource(AirEntity object) {
        return new ResourceLocation("simple_mobs", "geo/air.geo.json");
    }

    public ResourceLocation getTextureResource(AirEntity object) {
        return new ResourceLocation("simple_mobs", "textures/air.png");
    }

    public ResourceLocation getAnimationResource(AirEntity animatable) {
        return new ResourceLocation("simple_mobs", "animations/air.animation.json");
    }
}

