/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  software.bernie.geckolib.model.GeoModel
 */
package dev.deepnight.source.dawn.client;

import dev.deepnight.source.dawn.entity.GhostprojectileEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GhostprojectileModel
extends GeoModel<GhostprojectileEntity> {
    public ResourceLocation getModelResource(GhostprojectileEntity object) {
        return new ResourceLocation("simple_mobs", "geo/ghostproj.geo.json");
    }

    public ResourceLocation getTextureResource(GhostprojectileEntity object) {
        return new ResourceLocation("simple_mobs", "textures/a.png");
    }

    public ResourceLocation getAnimationResource(GhostprojectileEntity animatable) {
        return new ResourceLocation("simple_mobs", "animations/ghostproj.animation.json");
    }
}

