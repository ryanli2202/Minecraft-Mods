/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import dev.deepnight.source.dawn.client.model.ModelFireball;
import dev.deepnight.source.dawn.entity.Korok5BalloonEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class Korok5BalloonRenderer
extends MobRenderer<Korok5BalloonEntity, ModelFireball<Korok5BalloonEntity>> {
    public Korok5BalloonRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelFireball(context.bakeLayer(ModelFireball.LAYER_LOCATION)), 0.0f);
    }

    @Override
    public ResourceLocation getTextureLocation(Korok5BalloonEntity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/a.png");
    }
}

