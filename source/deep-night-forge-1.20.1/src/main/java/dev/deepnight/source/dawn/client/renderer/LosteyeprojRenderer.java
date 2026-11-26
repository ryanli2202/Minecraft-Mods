/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import dev.deepnight.source.dawn.entity.LosteyeprojEntity;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class LosteyeprojRenderer
extends MobRenderer<LosteyeprojEntity, ChickenModel<LosteyeprojEntity>> {
    public LosteyeprojRenderer(EntityRendererProvider.Context context) {
        super(context, new ChickenModel(context.bakeLayer(ModelLayers.CHICKEN)), 0.0f);
    }

    @Override
    public ResourceLocation getTextureLocation(LosteyeprojEntity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/a.png");
    }
}

