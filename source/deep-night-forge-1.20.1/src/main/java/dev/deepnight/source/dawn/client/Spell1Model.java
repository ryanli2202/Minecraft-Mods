/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  software.bernie.geckolib.model.GeoModel
 */
package dev.deepnight.source.dawn.client;

import dev.deepnight.source.dawn.entity.Spell1Entity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class Spell1Model
extends GeoModel<Spell1Entity> {
    public ResourceLocation getModelResource(Spell1Entity object) {
        return new ResourceLocation("simple_mobs", "geo/spell.geo.json");
    }

    public ResourceLocation getTextureResource(Spell1Entity object) {
        return new ResourceLocation("simple_mobs", "textures/spell.png");
    }

    public ResourceLocation getAnimationResource(Spell1Entity animatable) {
        return new ResourceLocation("simple_mobs", "animations/spell.animation.json");
    }
}

