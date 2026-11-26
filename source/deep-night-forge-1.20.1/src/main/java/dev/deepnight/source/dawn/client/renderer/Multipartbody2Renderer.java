/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import dev.deepnight.source.dawn.client.model.Modelcustom_model;
import dev.deepnight.source.dawn.entity.Multipartbody2Entity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class Multipartbody2Renderer
extends MobRenderer<Multipartbody2Entity, Modelcustom_model<Multipartbody2Entity>> {
    public Multipartbody2Renderer(EntityRendererProvider.Context context) {
        super(context, new Modelcustom_model(context.bakeLayer(Modelcustom_model.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(Multipartbody2Entity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/a.png");
    }
}

