/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  software.bernie.geckolib.model.GeoModel
 */
package dev.deepnight.source.dawn.client;

import dev.deepnight.source.dawn.entity.HerobrineSpike1Entity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HerobrineSpike1Model
extends GeoModel<HerobrineSpike1Entity> {
    public ResourceLocation getModelResource(HerobrineSpike1Entity object) {
        return new ResourceLocation("simple_mobs", "geo/herobrine_spike1.geo.json");
    }

    public ResourceLocation getTextureResource(HerobrineSpike1Entity object) {
        return new ResourceLocation("simple_mobs", "textures/herobrine_spike.png");
    }

    public ResourceLocation getAnimationResource(HerobrineSpike1Entity animatable) {
        return new ResourceLocation("simple_mobs", "animations/herobrine_spike1.animation.json");
    }
}

