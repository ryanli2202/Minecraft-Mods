/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import dev.deepnight.source.dawn.entity.NotchGrabEntity;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class NotchGrabRenderer
extends MobRenderer<NotchGrabEntity, PigModel<NotchGrabEntity>> {
    public NotchGrabRenderer(EntityRendererProvider.Context context) {
        super(context, new PigModel(context.bakeLayer(ModelLayers.PIG)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(NotchGrabEntity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/a.png");
    }
}

