/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  software.bernie.geckolib.model.GeoModel
 *  dev.deepnight.source.dawn.client.LegacyGeoRenderer
 */
package dev.deepnight.source.dawn.client.renderer;

import dev.deepnight.source.dawn.client.AirModel;
import dev.deepnight.source.dawn.entity.AirEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;
import dev.deepnight.source.dawn.client.LegacyGeoRenderer;

public class AirRenderer
extends LegacyGeoRenderer<AirEntity> {
    public AirRenderer(EntityRendererProvider.Context context) {
        super(context, (GeoModel)new AirModel());
    }

    protected float getDeathMaxRotation(AirEntity entityLivingBaseIn) {
        return 0.0f;
    }
}

