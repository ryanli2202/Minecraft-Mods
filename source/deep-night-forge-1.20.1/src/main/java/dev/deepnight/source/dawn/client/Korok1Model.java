/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  software.bernie.geckolib.model.GeoModel
 */
package dev.deepnight.source.dawn.client;

import dev.deepnight.source.dawn.entity.Korok1Entity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class Korok1Model
extends GeoModel<Korok1Entity> {
    public ResourceLocation getModelResource(Korok1Entity object) {
        return new ResourceLocation("simple_mobs", "geo/korok_1.geo.json");
    }

    public ResourceLocation getTextureResource(Korok1Entity object) {
        return new ResourceLocation("simple_mobs", "textures/korok_1.png");
    }

    public ResourceLocation getAnimationResource(Korok1Entity animatable) {
        return new ResourceLocation("simple_mobs", "animations/korok.animation.json");
    }
}

