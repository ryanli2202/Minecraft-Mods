/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  software.bernie.geckolib.model.GeoModel
 */
package dev.deepnight.source.dawn.client;

import dev.deepnight.source.dawn.entity.RumbleEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RumbleModel
extends GeoModel<RumbleEntity> {
    public ResourceLocation getModelResource(RumbleEntity object) {
        return new ResourceLocation("simple_mobs", "geo/rumble.geo.json");
    }

    public ResourceLocation getTextureResource(RumbleEntity object) {
        return new ResourceLocation("simple_mobs", "textures/rumble.png");
    }

    public ResourceLocation getAnimationResource(RumbleEntity animatable) {
        return new ResourceLocation("simple_mobs", "animations/rumble.animation.json");
    }
}

