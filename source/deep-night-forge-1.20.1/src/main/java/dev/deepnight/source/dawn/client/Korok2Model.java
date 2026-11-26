/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  software.bernie.geckolib.model.GeoModel
 */
package dev.deepnight.source.dawn.client;

import dev.deepnight.source.dawn.entity.Korok2Entity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class Korok2Model
extends GeoModel<Korok2Entity> {
    public ResourceLocation getModelResource(Korok2Entity object) {
        return new ResourceLocation("simple_mobs", "geo/korok_2.geo.json");
    }

    public ResourceLocation getTextureResource(Korok2Entity object) {
        return new ResourceLocation("simple_mobs", "textures/korok_2.png");
    }

    public ResourceLocation getAnimationResource(Korok2Entity animatable) {
        return new ResourceLocation("simple_mobs", "animations/korok.animation.json");
    }
}

