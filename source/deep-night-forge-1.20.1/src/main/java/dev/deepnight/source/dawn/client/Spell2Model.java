/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  software.bernie.geckolib.model.GeoModel
 */
package dev.deepnight.source.dawn.client;

import dev.deepnight.source.dawn.entity.Spell2Entity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class Spell2Model
extends GeoModel<Spell2Entity> {
    public ResourceLocation getModelResource(Spell2Entity object) {
        return new ResourceLocation("simple_mobs", "geo/spell2.geo.json");
    }

    public ResourceLocation getTextureResource(Spell2Entity object) {
        return new ResourceLocation("simple_mobs", "textures/spell2.png");
    }

    public ResourceLocation getAnimationResource(Spell2Entity animatable) {
        return new ResourceLocation("simple_mobs", "animations/spell2.animation.json");
    }
}

