/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  software.bernie.geckolib.model.GeoModel
 */
package dev.deepnight.source.dawn.client;

import dev.deepnight.source.dawn.entity.Korok4Entity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class Korok4Model
extends GeoModel<Korok4Entity> {
    public ResourceLocation getModelResource(Korok4Entity object) {
        return new ResourceLocation("simple_mobs", "geo/korok_4.geo.json");
    }

    public ResourceLocation getTextureResource(Korok4Entity object) {
        return new ResourceLocation("simple_mobs", "textures/korok_4.png");
    }

    public ResourceLocation getAnimationResource(Korok4Entity animatable) {
        return new ResourceLocation("simple_mobs", "animations/korok.animation.json");
    }
}

