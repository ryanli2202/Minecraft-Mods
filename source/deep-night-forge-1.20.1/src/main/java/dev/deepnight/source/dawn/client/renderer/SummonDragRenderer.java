/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import dev.deepnight.source.dawn.client.model.Modelprojectile1;
import dev.deepnight.source.dawn.entity.SummonDragEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SummonDragRenderer
extends MobRenderer<SummonDragEntity, Modelprojectile1<SummonDragEntity>> {
    public SummonDragRenderer(EntityRendererProvider.Context context) {
        super(context, new Modelprojectile1(context.bakeLayer(Modelprojectile1.LAYER_LOCATION)), 0.0f);
    }

    @Override
    public ResourceLocation getTextureLocation(SummonDragEntity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/a.png");
    }
}

