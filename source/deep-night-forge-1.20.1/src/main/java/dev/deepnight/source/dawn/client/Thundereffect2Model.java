/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  software.bernie.geckolib.model.GeoModel
 */
package dev.deepnight.source.dawn.client;

import dev.deepnight.source.dawn.entity.Thundereffect2Entity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class Thundereffect2Model
extends GeoModel<Thundereffect2Entity> {
    public ResourceLocation getModelResource(Thundereffect2Entity object) {
        return new ResourceLocation("simple_mobs", "geo/thunder_effect2.geo.json");
    }

    public ResourceLocation getTextureResource(Thundereffect2Entity object) {
        return new ResourceLocation("simple_mobs", "textures/a.png");
    }

    public ResourceLocation getAnimationResource(Thundereffect2Entity animatable) {
        return new ResourceLocation("simple_mobs", "animations/thunder_effect2.animation.json");
    }
}

