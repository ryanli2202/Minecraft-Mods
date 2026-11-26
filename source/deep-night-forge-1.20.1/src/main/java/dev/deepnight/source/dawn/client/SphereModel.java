/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  software.bernie.geckolib.model.GeoModel
 */
package dev.deepnight.source.dawn.client;

import dev.deepnight.source.dawn.entity.SphereEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SphereModel
extends GeoModel<SphereEntity> {
    public ResourceLocation getModelResource(SphereEntity object) {
        return new ResourceLocation("simple_mobs", "geo/sphere.geo.json");
    }

    public ResourceLocation getTextureResource(SphereEntity object) {
        return new ResourceLocation("simple_mobs", "textures/entities/a.png");
    }

    public ResourceLocation getAnimationResource(SphereEntity animatable) {
        return new ResourceLocation("simple_mobs", "animations/sphere.animation.json");
    }
}

