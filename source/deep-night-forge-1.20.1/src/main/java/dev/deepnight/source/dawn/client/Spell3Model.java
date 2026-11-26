/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  software.bernie.geckolib.model.GeoModel
 */
package dev.deepnight.source.dawn.client;

import dev.deepnight.source.dawn.entity.Spell3Entity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class Spell3Model
extends GeoModel<Spell3Entity> {
    public ResourceLocation getModelResource(Spell3Entity object) {
        return new ResourceLocation("simple_mobs", "geo/spell3.geo.json");
    }

    public ResourceLocation getTextureResource(Spell3Entity object) {
        return new ResourceLocation("simple_mobs", "textures/entities/a.png");
    }

    public ResourceLocation getAnimationResource(Spell3Entity animatable) {
        return new ResourceLocation("simple_mobs", "animations/spell3.animation.json");
    }
}

