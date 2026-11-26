/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  software.bernie.geckolib.model.GeoModel
 *  dev.deepnight.source.dawn.client.LegacyGeoRenderer
 */
package dev.deepnight.source.dawn.client.renderer;

import dev.deepnight.source.dawn.client.Korok3Model;
import dev.deepnight.source.dawn.entity.Korok3Entity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;
import dev.deepnight.source.dawn.client.LegacyGeoRenderer;

public class Korok3Renderer
extends LegacyGeoRenderer<Korok3Entity> {
    public Korok3Renderer(EntityRendererProvider.Context context) {
        super(context, (GeoModel)new Korok3Model());
    }

    protected float getDeathMaxRotation(Korok3Entity entityLivingBaseIn) {
        return 0.0f;
    }
}

