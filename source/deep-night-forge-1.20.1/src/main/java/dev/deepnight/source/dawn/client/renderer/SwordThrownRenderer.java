/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import dev.deepnight.source.dawn.client.model.Modelsword_thrown;
import dev.deepnight.source.dawn.entity.SwordThrownEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SwordThrownRenderer
extends MobRenderer<SwordThrownEntity, Modelsword_thrown<SwordThrownEntity>> {
    public SwordThrownRenderer(EntityRendererProvider.Context context) {
        super(context, new Modelsword_thrown(context.bakeLayer(Modelsword_thrown.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(SwordThrownEntity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/sword_thrown.png");
    }
}

