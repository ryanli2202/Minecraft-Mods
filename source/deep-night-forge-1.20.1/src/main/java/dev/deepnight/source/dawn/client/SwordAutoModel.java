/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  software.bernie.geckolib.model.GeoModel
 */
package dev.deepnight.source.dawn.client;

import dev.deepnight.source.dawn.entity.SwordAutoEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SwordAutoModel
extends GeoModel<SwordAutoEntity> {
    public ResourceLocation getModelResource(SwordAutoEntity object) {
        return new ResourceLocation("simple_mobs", "geo/sword_auto.geo.json");
    }

    public ResourceLocation getTextureResource(SwordAutoEntity object) {
        return new ResourceLocation("simple_mobs", "textures/entities/sword_thrown.png");
    }

    public ResourceLocation getAnimationResource(SwordAutoEntity animatable) {
        return new ResourceLocation("simple_mobs", "animations/sa.animation.json");
    }
}

