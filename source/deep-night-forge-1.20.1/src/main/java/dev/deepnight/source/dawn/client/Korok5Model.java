/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  software.bernie.geckolib.model.GeoModel
 */
package dev.deepnight.source.dawn.client;

import dev.deepnight.source.dawn.entity.Korok5Entity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class Korok5Model
extends GeoModel<Korok5Entity> {
    public ResourceLocation getModelResource(Korok5Entity object) {
        return new ResourceLocation("simple_mobs", "geo/korok_5.geo.json");
    }

    public ResourceLocation getTextureResource(Korok5Entity object) {
        return new ResourceLocation("simple_mobs", "textures/korok_5.png");
    }

    public ResourceLocation getAnimationResource(Korok5Entity animatable) {
        return new ResourceLocation("simple_mobs", "animations/korok_5.animation.json");
    }
}

