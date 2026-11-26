/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import dev.deepnight.source.dawn.client.model.Modelunknown;
import dev.deepnight.source.dawn.entity.TrackingThunderEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class TrackingThunderRenderer
extends MobRenderer<TrackingThunderEntity, Modelunknown<TrackingThunderEntity>> {
    public TrackingThunderRenderer(EntityRendererProvider.Context context) {
        super(context, new Modelunknown(context.bakeLayer(Modelunknown.LAYER_LOCATION)), 0.0f);
    }

    @Override
    public ResourceLocation getTextureLocation(TrackingThunderEntity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/a.png");
    }
}

