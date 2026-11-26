/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  software.bernie.geckolib.model.GeoModel
 */
package dev.deepnight.source.dawn.client;

import dev.deepnight.source.dawn.entity.HerobrineSpike2Entity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HerobrineSpike2Model
extends GeoModel<HerobrineSpike2Entity> {
    public ResourceLocation getModelResource(HerobrineSpike2Entity object) {
        return new ResourceLocation("simple_mobs", "geo/herobrine_spike2.geo.json");
    }

    public ResourceLocation getTextureResource(HerobrineSpike2Entity object) {
        return new ResourceLocation("simple_mobs", "textures/herobrine_spike.png");
    }

    public ResourceLocation getAnimationResource(HerobrineSpike2Entity animatable) {
        return new ResourceLocation("simple_mobs", "animations/herobrine_spike2.animation.json");
    }
}

