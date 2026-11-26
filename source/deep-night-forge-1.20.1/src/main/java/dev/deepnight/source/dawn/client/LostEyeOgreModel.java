/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  software.bernie.geckolib.model.GeoModel
 */
package dev.deepnight.source.dawn.client;

import dev.deepnight.source.dawn.entity.LostEyeOgreEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class LostEyeOgreModel
extends GeoModel<LostEyeOgreEntity> {
    public ResourceLocation getModelResource(LostEyeOgreEntity object) {
        return new ResourceLocation("simple_mobs", "geo/losteye.geo.json");
    }

    public ResourceLocation getTextureResource(LostEyeOgreEntity object) {
        return new ResourceLocation("simple_mobs", "textures/losteye.png");
    }

    public ResourceLocation getAnimationResource(LostEyeOgreEntity animatable) {
        return new ResourceLocation("simple_mobs", "animations/losteye.animation.json");
    }
}

