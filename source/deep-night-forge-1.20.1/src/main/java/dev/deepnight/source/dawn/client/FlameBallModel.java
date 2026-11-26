/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  software.bernie.geckolib.model.GeoModel
 */
package dev.deepnight.source.dawn.client;

import dev.deepnight.source.dawn.entity.FlameBallEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FlameBallModel
extends GeoModel<FlameBallEntity> {
    public ResourceLocation getModelResource(FlameBallEntity object) {
        return new ResourceLocation("simple_mobs", "geo/flame_ball.geo.json");
    }

    public ResourceLocation getTextureResource(FlameBallEntity object) {
        return new ResourceLocation("simple_mobs", "textures/entities/flame_ball.png");
    }

    public ResourceLocation getAnimationResource(FlameBallEntity animatable) {
        return new ResourceLocation("simple_mobs", "animations/flame_ball.animation.json");
    }
}

