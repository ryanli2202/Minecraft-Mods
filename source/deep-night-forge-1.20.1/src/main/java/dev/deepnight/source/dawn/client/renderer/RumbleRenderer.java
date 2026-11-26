/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  software.bernie.geckolib.model.GeoModel
 *  dev.deepnight.source.dawn.client.LegacyGeoRenderer
 */
package dev.deepnight.source.dawn.client.renderer;

import dev.deepnight.source.dawn.client.RumbleModel;
import dev.deepnight.source.dawn.entity.RumbleEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;
import dev.deepnight.source.dawn.client.LegacyGeoRenderer;

public class RumbleRenderer
extends LegacyGeoRenderer<RumbleEntity> {
    public RumbleRenderer(EntityRendererProvider.Context context) {
        super(context, (GeoModel)new RumbleModel());
    }

    protected float getDeathMaxRotation(RumbleEntity entityLivingBaseIn) {
        return 0.0f;
    }
}

