/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import dev.deepnight.source.dawn.client.model.Modelgroundspike;
import dev.deepnight.source.dawn.entity.GroundSpikeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class GroundSpikeRenderer
extends MobRenderer<GroundSpikeEntity, Modelgroundspike<GroundSpikeEntity>> {
    public GroundSpikeRenderer(EntityRendererProvider.Context context) {
        super(context, new Modelgroundspike(context.bakeLayer(Modelgroundspike.LAYER_LOCATION)), 0.0f);
    }

    @Override
    public ResourceLocation getTextureLocation(GroundSpikeEntity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/groundspike.png");
    }
}

