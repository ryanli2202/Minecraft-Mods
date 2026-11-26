/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  software.bernie.geckolib.model.GeoModel
 *  dev.deepnight.source.dawn.client.LegacyGeoRenderer
 */
package dev.deepnight.source.dawn.client.renderer;

import dev.deepnight.source.dawn.client.Korok1Model;
import dev.deepnight.source.dawn.entity.Korok1Entity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;
import dev.deepnight.source.dawn.client.LegacyGeoRenderer;

public class Korok1Renderer
extends LegacyGeoRenderer<Korok1Entity> {
    public Korok1Renderer(EntityRendererProvider.Context context) {
        super(context, (GeoModel)new Korok1Model());
    }

    protected float getDeathMaxRotation(Korok1Entity entityLivingBaseIn) {
        return 0.0f;
    }
}

