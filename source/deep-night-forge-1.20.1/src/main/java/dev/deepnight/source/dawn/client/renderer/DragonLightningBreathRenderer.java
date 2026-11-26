/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import dev.deepnight.source.dawn.client.model.Modelprojectile1;
import dev.deepnight.source.dawn.entity.DragonLightningBreathEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class DragonLightningBreathRenderer
extends MobRenderer<DragonLightningBreathEntity, Modelprojectile1<DragonLightningBreathEntity>> {
    public DragonLightningBreathRenderer(EntityRendererProvider.Context context) {
        super(context, new Modelprojectile1(context.bakeLayer(Modelprojectile1.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(DragonLightningBreathEntity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/a.png");
    }
}

