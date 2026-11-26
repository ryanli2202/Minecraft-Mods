/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  software.bernie.geckolib.model.GeoModel
 *  dev.deepnight.source.dawn.client.LegacyGeoRenderer
 */
package dev.deepnight.source.dawn.client.renderer;

import dev.deepnight.source.dawn.client.Korok4Model;
import dev.deepnight.source.dawn.entity.Korok4Entity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;
import dev.deepnight.source.dawn.client.LegacyGeoRenderer;

public class Korok4Renderer
extends LegacyGeoRenderer<Korok4Entity> {
    public Korok4Renderer(EntityRendererProvider.Context context) {
        super(context, (GeoModel)new Korok4Model());
    }

    protected float getDeathMaxRotation(Korok4Entity entityLivingBaseIn) {
        return 0.0f;
    }
}

