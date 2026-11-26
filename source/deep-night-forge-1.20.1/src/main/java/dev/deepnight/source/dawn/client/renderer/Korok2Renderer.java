/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  software.bernie.geckolib.model.GeoModel
 *  dev.deepnight.source.dawn.client.LegacyGeoRenderer
 */
package dev.deepnight.source.dawn.client.renderer;

import dev.deepnight.source.dawn.client.Korok2Model;
import dev.deepnight.source.dawn.entity.Korok2Entity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;
import dev.deepnight.source.dawn.client.LegacyGeoRenderer;

public class Korok2Renderer
extends LegacyGeoRenderer<Korok2Entity> {
    public Korok2Renderer(EntityRendererProvider.Context context) {
        super(context, (GeoModel)new Korok2Model());
    }

    protected float getDeathMaxRotation(Korok2Entity entityLivingBaseIn) {
        return 0.0f;
    }
}

