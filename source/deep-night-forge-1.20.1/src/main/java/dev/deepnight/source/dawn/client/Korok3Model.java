/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  software.bernie.geckolib.model.GeoModel
 */
package dev.deepnight.source.dawn.client;

import dev.deepnight.source.dawn.entity.Korok3Entity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class Korok3Model
extends GeoModel<Korok3Entity> {
    public ResourceLocation getModelResource(Korok3Entity object) {
        return new ResourceLocation("simple_mobs", "geo/korok_3.geo.json");
    }

    public ResourceLocation getTextureResource(Korok3Entity object) {
        return new ResourceLocation("simple_mobs", "textures/korok_3.png");
    }

    public ResourceLocation getAnimationResource(Korok3Entity animatable) {
        return new ResourceLocation("simple_mobs", "animations/korok.animation.json");
    }
}

